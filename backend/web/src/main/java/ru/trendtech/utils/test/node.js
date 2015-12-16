var io = require('socket.io').listen(5001);

var mysql = require('mysql');
var db = mysql.createConnection({
    host: '127.0.0.1',
    user: 'root',
    password: 'zx9504',
    database: 'db_taxisto_dev'
//  database : 'db_taxisto_stg'
});

var javaURL = 'http://dev.taxisto.ru'; //dev
//var javaURL = 'http://localhost:9000'; //stg

var GRAYLOG_SERVER = 'http://1.trendtech.ru:12201/gelf'; //dev
//var GRAYLOG_SERVER = 'http://1.trendtech.ru:12202/gelf'; //stg

db.connect();
console.log("running");

//---------------------Create connection to mongodb-----------------
var mongoose = require('mongoose');
mongoose.connect('mongodb://dev0.taxisto.ru/test');
var mdb = mongoose.connection;
mdb.on('error', function (err) {
    console.log('MongoDB connection error:', err.message);
});
mdb.once('open', function callback() {
    console.log("Connected to mongodb");
});

//version 2.8.1
var requestify = require('requestify');
var LocationModel = createAndCompileLocationScheme();
var MissionInviteModel = createAndCompileMissionInviteScheme();
var LogEventModel = createAndCompileLogEventScheme();

//--------------------Clear sockets in database--------------------

clearDB();

//--------------------Global variables-----------------------

var serverSocketID = null;
var amount_penalty = 0;
var penalty_cancel_common_mission = 0;
var free_waiting_minutes = 10;

//--------------------Main events----------------------------------

/*
 //Р—Р°РіРѕС‚РѕРІРєР° РґР»СЏ Р°РІС‚РѕСЂРёР·Р°С†РёРё
 io.set('authorization', function (handshakeData, callback) {

 var security_token = handshakeData.query.security_token;
 var client_type = handshakeData.query.client_type;

 var clientId = 3;

 db.query("SELECT token FROM client WHERE id = ?", [clientId], function (err, rows) {
 if (err) throw err;

 if (rows.length > 0){
 callback(null, true); // error first, 'authorized' boolean second
 }
 });
 });
 */

io.sockets.on('connection', function (socket) {

    socket.on('connected', function (userInfo) {
        console.log('connected', userInfo);
        db.query("UPDATE device_info SET socket_id = ? WHERE id = ?", [socket.id, userInfo.device_id], function (err, rows) {
            if (err) throw err;
            logEvent(2, 0, 3, 0, 'connected', "device_id=" + userInfo.device_id, "");
        });
    });

    socket.on('webuser_socketid_update', function (userInfo) {
        console.log('webuser_socketid_update', userInfo);
        db.query("UPDATE web_users SET socket_id = ? WHERE id = ?", [socket.id, userInfo.webUser_id], function (err, rows) {
            if (err) throw err;
            logEvent(2, 0, 3, 0, 'webuser_socketid_update', "id=" + userInfo.webUser_id, "");
        });
    });

    socket.on('connected_driver', function (userInfo) {
        console.log('connected_driver', userInfo);

        logEvent(1, userInfo.driver_id, 3, 0, 'connected_driver', "device_id=" + userInfo.device_id, "");

        db.query("UPDATE device_info SET socket_id = ? WHERE id = ?", [socket.id, userInfo.device_id], function (err, rows) {
            if (err) throw err;
        });
        db.query("UPDATE driver SET state = 'AVAILABLE' WHERE id = ? and state NOT LIKE('%BUSY%')", [userInfo.driver_id], function (err, rows) {
            if (err) throw err;
        });
    });

    socket.on('connected_server', function (userInfo) {
        console.log('connected_server', userInfo);
        logEvent(0, 0, 0, 0, 'connected_server', "", "");
        serverSocketID = socket.id;
    });

    socket.on('disconnect', function () {
        console.log('disconnected client', socket.id);
        db.query("SELECT id FROM device_info WHERE socket_id = ?", [socket.id], function (err, rows) {
            if (err) throw err;
            if (!isEmpty(rows)) {
                var deviceId = rows[0]["id"];
                db.query("UPDATE device_info SET socket_id = NULL WHERE socket_id = ?", [socket.id], function (err, rows) {
                    if (err) throw err;

                    db.query("SELECT driver_id FROM driver_device_info WHERE devices_id = ?", [deviceId], function (err, rows) {
                        if (err) throw err;
                        if (!isEmpty(rows)) {//is drivers device
                            var driverId = rows[0]["driver_id"];
                            logEvent(1, driverId, 0, 3, 'disconnected_driver', "device_id=" + deviceId, "");
                            db.query("UPDATE driver SET state = 'OFFLINE' WHERE id = ? and state NOT LIKE('%BUSY%') and type_x = '0'", [driverId], function (err, rows) {
                                if (err) throw err;
                            });
                        } else {//is clients device
                            db.query("SELECT client_id FROM client_device_info WHERE devices_id = ?", [deviceId], function (err, rows) {
                                if (err) throw err;
                                if (!isEmpty(rows)) {
                                    var clientId = rows[0]["client_id"];
                                    logEvent(2, clientId, 0, 3, 'disconnected_client', "device_id=" + deviceId, "");
                                    var socket = io.sockets.sockets[serverSocketID];
                                    if (socket) {
                                        socket.json.send({'event': 'client_disconnected',
                                            'clientId': clientId});
                                    }
                                }
                            });
                        }
                    });
                });
            } else {
                console.log("device with socket " + socket.id + " not found");
            }
        });
    });

    socket.on('check_connection', function () {
        socket.json.send({'event': 'checked'});
    });

    socket.on('driver_location', function (info) {
        //console.log(info);
        var missionId = info.missionId;
        if (missionId == 0) {
            missionId = null;
        }

        saveDriverLocation(info);

        db.query("UPDATE mission SET distance_real = ? WHERE id = ? AND state = 'IN_TRIP'", [info.location.distance, info.missionId], function (err, rows) {
            if (err) throw err;

        });

        //send location to client if driver is going for mission
        var d_state = "FREE_VIEW";
        if (missionId != null) {
            var d_state = info.location.status;
            console.log("send assigned driver location");
            sendDataToClientByMission(missionId, {'event': 'assigned_driver_location',
                'location': {'latitude': info.location.latitude,
                    'longitude': info.location.longitude,
                    'driverId': info.location.driverId,
                    'distance': info.location.distance,
                    'angle': info.location.angle}
            });
        }

        saveLocation(info, d_state);
    });

    socket.on('mission_cancel_order', function (info) {

        logEvent(0, 0, 3, info.missionId, 'mission_cancel_order', "driverId=" + info.driverId, "");

        socket.json.send({'event': 'mission_cancel_order_response',
            'missionId': info.missionId
        });

        sendDataToDriverByDriverID(info.driverId, {'event': 'mission_client_canceled', 'missionId': info.missionId}, function (success) {
        });
    });

    socket.on('send_message_to_client', function (info) {

        logEvent(0, 0, 3, info.clientId, 'send_message_to_client', "clientId=" + info.clientId, "text=" + info.text);

        sendDataToClient(info.clientId, {'event': 'message_to_client',
            'text': info.text});

        socket.json.send({'event': 'send_message_to_client_response',
            'clientId': info.clientId
        });

    });

    socket.on('driver_refresh', function (info) {

        logEvent(1, info.driverId, 3, 0, 'driver_refresh', "webUserId=" + info.webUserId, "");

        sendDataToDriverByDriverID(info.driverId, {'event': 'driver_forced_state_refresh'}, function (success) {
            socket.json.send({'event': 'driver_refresh_response',
                'driverId': info.driverId,
                'webUserId': info.webUserId
            });
        });

    });

    socket.on('open_mission_card', function (info) {

        logEvent(0, 0, 3, info.missionId, 'open_mission_card', "missionId=" + info.missionId, "text=" + info.text);

        sendDataToARM(info.missionId, {'event': 'open_mission_card','missionId': info.missionId,'text': info.text});

        socket.json.send({'event': 'open_mission_card_response',
            'missionId': info.missionId
        });

    });


    socket.on('mission_assign', function (info) {
        console.log('************************!!! mission_assign !!!**********************************', info);
        saveDriverLocation(info);

        db.query("SELECT M.state, D.taxopark_id \
                  FROM mission AS M \
                  LEFT JOIN driver D ON M.driverInfo_id = D.id \
                  WHERE M.id = ? AND (D.type_x = true OR M.driverInfo_id IS NULL)" , [info.missionId], function (err, rows) {
            if (err) throw err;

            if (rows.length > 0) {
                var state = rows[0]['state'];
                var taxopark_id = rows[0]['taxopark_id'];

                //db.query("UPDATE mission SET driverInfo_id = ?, late_driver_booked_min = ?, time_assigning = NOW(), state = 'ASSIGNED' WHERE id = ? AND (state = 'NEW' OR state = 'BOOKED' OR state = 'AUTO_SEARCH' OR state = 'TURBO')", [info.location.driverId, info.arrivalTime, info.missionId], function (err, rows) {
                db.query("UPDATE mission SET driverInfo_id = ?, late_driver_booked_min = ?, time_assigning = NOW(), state = 'ASSIGNED', taxopark_id = ? WHERE id = ? AND (state NOT IN ('CANCELED', 'COMPLETED') )", [info.location.driverId, info.arrivalTime, taxopark_id, info.missionId], function (err, rows) {
                    if (err) throw err;
                    var affectedRows = rows['affectedRows'];
                    var assigned = affectedRows == 1;

                    logEvent(1, info.location.driverId, 3, info.missionId, 'mission_assign', "assigned=" + assigned, "");

                    if (info.location.driverId == 22) {
                        //assigned = false;
                    }

                    socket.json.send({'event': 'mission_assign_response',
                        'missionId': info.missionId,
                        'assigned': assigned
                    });

                    if (assigned) {
                        db.query("UPDATE driver SET current_mission_id = ? WHERE id = ?", [info.missionId, info.location.driverId], function (err, rows) {
                            if (err) throw err;
                        });

                        db.query("SELECT m.clientInfo_id, c.phone FROM mission m INNER JOIN client c ON c.id = m.clientInfo_id WHERE m.id = ?", [info.missionId], function (err, rows) {
                            if (err) throw err;
                            var clientId = rows[0]['clientInfo_id'];
                            var phone = rows[0]['phone'];
                            console.log("phone:", phone);

                            if (isTerminalPhone(phone) || state == 'AUTO_SEARCH') {
                                sendAssignedSMS(info.missionId, phone);
                            }
                            db.query("UPDATE client SET mission_id = ? WHERE id = ?", [info.missionId, clientId], function (err, rows) {
                                if (err) throw err;
                            });
                        });

                        removeMissionFromDeclinedList(info.missionId);
                        missionBecameUnavailable(info.missionId, info.location.driverId);
                        sendDataToClientByMission(info.missionId, {'event': 'driver_assigned',
                            'arrivalTime': info.arrivalTime,
                            'missionId': info.missionId,
                            'location': {'latitude': info.location.latitude,
                                'longitude': info.location.longitude,
                                'driverId': info.location.driverId,
                                'distance': info.location.distance}
                        });
                    }
                });
            }
        });
    });

    socket.on('mission_assign_msg', function (info) {
        console.log('************************!!! mission_assign msg !!!**********************************', info);

        removeMissionFromDeclinedList(info.missionId);
        missionBecameUnavailable(info.missionId, info.location.driverId);
        sendDataToClientByMission(info.missionId, {'event': 'driver_assigned',
            'arrivalTime': info.arrivalTime,
            'missionId': info.missionId,
            'location': {'latitude': info.location.latitude,
                'longitude': info.location.longitude,
                'driverId': info.location.driverId,
                'distance': info.location.distance}
        });
    });

    socket.on('mission_assign_second_order', function (info) {
        console.log('************************!!! mission_assign_second_order !!!**********************************', info);
        saveDriverLocation(info);

        db.query("SELECT state FROM mission WHERE id = ?", [info.missionId], function (err, rows) {
            if (err) throw err;
            var state = rows[0]['state'];

            logEvent(1, info.location.driverId, 3, info.missionId, 'mission_assign_second_order', "assigned=", "");

            socket.json.send({'event': 'mission_assign_second_order_response',
                'missionId': info.missionId,
                'assigned': true
            });

            sendDataToClientByMission(info.missionId, {'event': 'driver_assigned',
                'arrivalTime': info.arrivalTime,
                'missionId': info.missionId,
                'location': {'latitude': info.location.latitude,
                    'longitude': info.location.longitude,
                    'driverId': info.location.driverId,
                    'distance': info.location.distance}
            });
            missionBecameUnavailable(info.missionId, info.location.driverId);
        });
    });

    socket.on('mission_became_unavailable', function (info) {

        logEvent(0, 0, 3, info.missionId, 'mission_became_unavailable', "", "");

        socket.json.send({'event': 'mission_became_unavailable_response',
            'missionId': info.missionId
        });

        missionBecameUnavailable(info.missionId, info.driverId);
    });

    socket.on('mission_arrived', function (info) {
        console.log('mission_arrived', info);
        db.query("UPDATE mission SET time_arriving = NOW(), driverInfo_id = ?, state = 'ARRIVED' WHERE id = ? AND state <> 'CANCELED'", [info.driverId, info.missionId], function (err, rows) {
            if (err) throw err;

            var affectedRows = rows['affectedRows'];
            var updated = affectedRows == 1;

            if (updated) {
                //db.query("SELECT m.booking_state, m.clientInfo_id, m.driverInfo_id, d.first_name AS d_first_name, d.auto_color, d.auto_model, d.auto_mumber, d.phone AS d_phone, c.first_name AS c_first_name, c.phone AS c_phone FROM mission m INNER JOIN driver d ON m.driverInfo_id = d.id INNER JOIN client c ON m.clientInfo_id = c.id WHERE m.id = ?", [info.missionId], function(err, rows) {
                db.query("SELECT m.booking_state, m.clientInfo_id, m.driverInfo_id, m.price_expected_amount, m.to_lat, d.first_name AS d_first_name, d.auto_color, d.auto_model, d.auto_mumber, d.phone AS d_phone, c.first_name AS c_first_name, c.phone AS c_phone FROM mission m INNER JOIN driver d ON m.driverInfo_id = d.id INNER JOIN client c ON m.clientInfo_id = c.id WHERE m.id = ?", [info.missionId], function (err, rows) {
                    if (err) throw err;
                    var clientId = rows[0]['clientInfo_id'];
                    var driverId = rows[0]['driverInfo_id'];
                    var c_name = rows[0]['c_first_name'];
                    var c_phone = rows[0]['c_phone'];
                    var d_auto_color = rows[0]['auto_color'];
                    var d_auto_model = rows[0]['auto_model'];
                    var d_auto_number = rows[0]['auto_mumber'];
                    var d_name = rows[0]['d_first_name'];
                    var d_phone = rows[0]['d_phone'];
                    var booking_state = rows[0]['booking_state'];
                    var booked = booking_state != 'NONE';
                    var price = rows[0]['price_expected_amount'];
                    var to_lat = rows[0]['to_lat'];

                    logEvent(1, driverId, 3, info.missionId, 'mission_arrived', "booked=" + booked, "");

                    db.query("UPDATE driver SET current_mission_id = ? WHERE id = ?", [info.missionId, driverId], function (err, rows) {
                        if (err) throw err;
                    });

                    db.query("UPDATE client SET mission_id = ? WHERE id = ?", [info.missionId, clientId], function (err, rows) {
                        if (err) throw err;
                    });

                    if (to_lat == 0) {
                        var price_msg = " РќР°С‡Р°Р»СЊРЅР°СЏ СЃС‚РѕРёРјРѕСЃС‚СЊ: " + price + " СЂСѓР±";
                    } else {
                        var price_msg = " РЎС‚РѕРёРјРѕСЃС‚СЊ: " + price + " СЂСѓР±";
                    }

                    var message;
                    if (booked) {
                        if (isTerminalPhone(c_phone)) {
                            message = "Р—РґСЂР°РІСЃС‚РІСѓР№С‚Рµ! Р’Р°С€ РўР°РєСЃРёСЃС‚Рѕ РїРѕРґСЉРµС…Р°Р» Рё Р¶РґРµС‚ РІР°СЃ РІ РЅР°Р·РЅР°С‡РµРЅРЅРѕРµ РІСЂРµРјСЏ. Р’РѕРґРёС‚РµР»СЊ: " + d_name + " " + d_phone + ", РјР°С€РёРЅР°:" + d_auto_color + " " + d_auto_model + " " + d_auto_number;
                        } else {
                            message = "Р—РґСЂР°РІСЃС‚РІСѓР№С‚Рµ, " + rows[0]['c_first_name'] + "! Р’Р°С€ РўР°РєСЃРёСЃС‚Рѕ РїРѕРґСЉРµС…Р°Р» Рё Р¶РґРµС‚ РІР°СЃ РІ РЅР°Р·РЅР°С‡РµРЅРЅРѕРµ РІСЂРµРјСЏ. Р’РѕРґРёС‚РµР»СЊ: " + d_name + " " + d_phone + ", РјР°С€РёРЅР°:" + d_auto_color + " " + d_auto_model + " " + d_auto_number;
                        }
                    } else {
                        if (isTerminalPhone(c_phone)) {
                            message = "Р—РґСЂР°РІСЃС‚РІСѓР№С‚Рµ! Р’Р°С€ РўР°РєСЃРёСЃС‚Рѕ: " + d_auto_color + " " + d_auto_model + " " + d_auto_number + " - РїРѕРґСЉРµС…Р°Р». Р’РѕРґРёС‚РµР»СЊ: " + d_name + " " + d_phone;
                        } else {
                            message = "Р—РґСЂР°РІСЃС‚РІСѓР№С‚Рµ, " + rows[0]['c_first_name'] + "! Р’Р°С€ РўР°РєСЃРёСЃС‚Рѕ: " + d_auto_color + " " + d_auto_model + " " + d_auto_number + " - РїРѕРґСЉРµС…Р°Р». Р’РѕРґРёС‚РµР»СЊ: " + d_name + " " + d_phone + price_msg;
                        }
                    }
                    console.log(message);
                    sendSMS(c_phone, message);

                    sendDataToClient(clientId, {'event': 'driver_arrived',
                        'missionId': info.missionId,
                        'driverId': driverId,
                        'booked': booked});
                    socket.json.send({'event': 'mission_arrived_response',
                        'missionId': info.missionId});
                });
            }else{
                var dataDict = {'event': 'dispatcher_message',
                    'text': 'Р—Р°РєР°Р· Р±С‹Р» РѕС‚РјРµРЅРµРЅ! РЎРІСЏР¶РёС‚РµСЃСЊ СЃ РґРёСЃРїРµС‚С‡РµСЂРѕРј.'};
                sendDataToDriverByDriverID(info.driverId, dataDict, function (success) {
                    logEvent(1, info.driverId, 3, info.missionId, 'mission_canceled_unknown', "", "");
                });
            }
        });
    });

    socket.on('mission_started', function (info) {
        console.log('mission_started', info);
        db.query("SELECT time_arriving, driverInfo_id FROM mission WHERE id = ?", [info.missionId], function (err, rows) {
            if (err) throw err;
            var arrivingDate = new Date(rows[0]['time_arriving']);
            var driverId = rows[0]['driverInfo_id'];
            var nowDate = new Date();
            var MS_PER_MINUTE = 60000;
            var waiting_time = Math.abs(nowDate - arrivingDate) / MS_PER_MINUTE;
            waiting_time = Math.ceil(waiting_time);

            db.query("UPDATE mission SET time_seating = NOW(), waiting_time= ?, state = 'IN_TRIP' WHERE id = ?", [waiting_time, info.missionId], function (err, rows) {
                if (err) throw err;

                logEvent(1, driverId, 3, info.missionId, 'mission_started', "", "");

                sendDataToClientByMission(info.missionId, {'event': 'mission_started',
                    'driverId': info.driverId,
                    'missionId': info.missionId});
                socket.json.send({'event': 'mission_started_response',
                    'missionId': info.missionId});
            });
        });
    });

    socket.on('mission_decline', function (info) {
        logEvent(1, info.driverId, 3, info.missionId, 'mission_decline', "", "");
        addMissionToDeclinedList(info.driverId, info.missionId);
    });

    socket.on('mission_driver_canceled', function (info) {
        db.query("SELECT state, time_arriving, driverInfo_id FROM mission WHERE id = ?", [info.missionId], function (err, rows) {
            if (err) throw err;
            var time_arriving = rows[0]['time_arriving'];
            var driverId = rows[0]['driverInfo_id'];
            var state = rows[0]['state'];
            var now = new Date();

            logEvent(1, driverId, 3, info.missionId, 'mission_driver_canceled', "", "");

            if (state == 'ARRIVED') {
                if (time_arriving.addMinutes(free_waiting_minutes) < now) {//do not fine
                    db.query("UPDATE mission SET state = 'CANCELED' WHERE id = ?", [info.missionId], function (err, rows) {
                        if (err) throw err;

                        saveCancelDetails({'missionId': info.missionId,
                            'cancel_by': 'driver',
                            'previous_state': state});

                        removeMissionFromDeclinedList(info.missionId);
                        clearDriverAndClient(info.missionId);
                        sendDataToClientByMission(info.missionId, {'event': 'mission_driver_canceled',
                            'missionId': info.missionId});
                        socket.json.send({'event': 'mission_driver_canceled_response',
                            'missionId': info.missionId});
                    });
                    return;
                }
            }

            //must fined
            db.query("UPDATE mission SET state = 'CANCELED' WHERE id = ?", [info.missionId], function (err, rows) {
                if (err) throw err;

                saveCancelDetails({'missionId': info.missionId,
                    'cancel_by': 'driver',
                    'previous_state': state});

                fineDriver(info.missionId, penalty_cancel_common_mission, function (missionId) {
                    removeMissionFromDeclinedList(missionId);
                    clearDriverAndClient(missionId);
                    sendDataToClientByMission(missionId, {'event': 'mission_driver_canceled',
                        'missionId': missionId});
                    socket.json.send({'event': 'mission_driver_canceled_response',
                        'missionId': missionId});
                });
            });

            db.query("SELECT c.phone FROM mission m INNER JOIN client c ON m.clientInfo_id = c.id WHERE m.id = ?", [info.missionId], function (err, rows) {
                if (err) throw err;
                var phone = rows[0]['phone'];
                if (isTerminalPhone(phone)) {
                    var message = "Р’РѕРґРёС‚РµР»СЊ РѕС‚РјРµРЅРёР» РїРѕРµР·РґРєСѓ. РџСЂРёРЅРѕСЃРёРј СЃРІРѕРё РёР·РІРёРЅРµРЅРёСЏ";
                    sendSMS(phone, message);
                }
            });
        });
    });

    socket.on('mission_canceled', function (info) {
        console.log('mission_canceled', info);
        if (info.missionId == 0) {
            return;
        }
        db.query("SELECT id, state, clientInfo_id FROM mission WHERE id = ?", [info.missionId], function (err, rows) {
            if (err) throw err;
            var state = rows[0]['state'];
            var clientId = rows[0]['clientInfo_id'];
            if (state == 'NEW') {
                var m_id = rows[0]['id'];
                missionBecameUnavailable(m_id);
            }

            logEvent(2, clientId, 3, info.missionId, 'mission_canceled', "state0", "");

            saveCancelDetails({'missionId': info.missionId,
                'cancel_by': 'client',
                'previous_state': state});

            db.query("UPDATE mission SET state = 'CANCELED' WHERE id = ?", [info.missionId], function (err, rows) {
                // 			if (err) throw err;
                var m_id = info.missionId;
                if (err) {
                    m_id = 0;
                }
                var affectedRows = rows['affectedRows'];

                logEvent(2, clientId, 3, info.missionId, 'mission_canceled', "state1", affectedRows);

                removeMissionFromDeclinedList(info.missionId);
                clearDriverAndClient(info.missionId);

                sendDataToDriverByMission(info.missionId, {'event': 'mission_client_canceled',
                    'missionId': info.missionId});
                socket.json.send({'event': 'mission_canceled_response',
                    'missionId': m_id});
            });
        });
    });

    socket.on('mission_finished', function (info) {
        console.log('mission_finished', info);
        finishPause(info.missionId);

        db.query("UPDATE mission SET distance_real = ? WHERE id = ?", [info.distanceInFact, info.missionId], function (err, rows) {
            if (err) throw err;

            logEvent(1, 0, 3, info.missionId, 'mission_finished', "", "");
// 			sendDataToClientByMission(info.missionId, {'event': 'mission_finished',
// 													   'missionId': info.missionId});
            socket.json.send({'event': 'mission_finish_response',
                'missionId': info.missionId});
        });
    });

    socket.on('mission_late', function (info) {
        db.query("UPDATE mission SET late_driver_booked_min = late_driver_booked_min + ?, is_late = 1 WHERE id = ?", [info.time, info.missionId], function (err, rows) {
            if (err) throw err;

            logEvent(1, 0, 3, info.missionId, 'mission_late', "time=" + info.time, "");

            sendDataToClientByMission(info.missionId, {'event': 'mission_late',
                'missionId': info.missionId,
                'time': info.time});
            socket.json.send({'event': 'mission_late_response',
                'missionId': info.missionId});
        });
    });

    socket.on('own_driver_mission_received', function (info) {

        var seconds = 10;
        db.query("UPDATE driver_location SET time_of_next_offer = DATE_ADD(NOW(), INTERVAL ? SECOND), mission_id_od = ? WHERE driver_id = ?", [seconds, info.missionId, info.driverId], function (err, rows) {
            if (err) throw err;

            logEvent(1, 0, 3, info.missionId, 'own_driver_mission_received', "driverId=" + info.driverId, "");
            socket.json.send({'event': 'own_driver_mission_received_response',
                'missionId': info.missionId, 'driverId': info.driverId});
        });
    });


    socket.on('mission_pause_begin', function (info) {
        console.log('mission_pause_begin', info);
        if (info.location == undefined) {
            console.log("old driver build");
            return;
        }
        saveLocation(info, "stop_with_client");
        db.query("UPDATE mission SET state = 'IN_TRIP_PAUSED' WHERE id = ?", [info.missionId], function (err, rows) {
            if (err) throw err;
            db.query("INSERT INTO pauses (mission_id, address, pause_lat, pause_long, start_pause) VALUES (?,?,?,?,NOW())", [info.missionId, info.location.address, info.location.latitude, info.location.longitude], function (err, rows) {
                if (err) throw err;

                logEvent(1, 0, 1, info.missionId, 'mission_pause_begin', "", "");

                socket.json.send({'event': 'mission_pause_begin_response',
                    'missionId': info.missionId});
                sendDataToClientByMission(info.missionId, {'event': 'mission_pause_begin',
                    'missionId': info.missionId});
            });
        });
    });

    socket.on('mission_pause_end', function (info) {
        console.log(info);
        db.query("UPDATE mission SET state = 'IN_TRIP' WHERE id = ?", [info.missionId], function (err, rows) {
            if (err) throw err;
            finishPause(info.missionId);
            logEvent(1, 0, 3, info.missionId, 'mission_pause_end', "", "");
            sendDataToClientByMission(info.missionId, {'event': 'mission_pause_end',
                'missionId': info.missionId});
            socket.json.send({'event': 'mission_pause_end_response',
                'missionId': info.missionId});
        });
    });

    socket.on('mission_readytogo', function (info) {
        console.log('mission_readytogo', info);
        db.query("UPDATE mission SET time_ready_to_go = NOW() WHERE id = ?", [info.missionId], function (err, rows) {
            if (err) throw err;
            logEvent(2, 0, 3, info.missionId, 'mission_readytogo', "", "");
            sendDataToDriverByMission(info.missionId, {'event': 'mission_readytogo',
                'missionId': info.missionId});
        });
    });

    socket.on('mission_declined_by_reason', function (info) {
        console.log('mission_declined_by_reason', info);

        db.query("SELECT id FROM mission WHERE id = ?", [info.missionId, info.driverId], function (err, rows) {
            if (err) throw err;
            logEvent(1, info.driverId, 3, info.missionId, 'mission_declined_by_reason', "driverId=" + info.driverId, "");
            sendDataToDriverByDriverID(info.driverId, {'event': 'new_mission_declined', 'missionId': info.missionId, 'latitude': info.latitude, 'longitude': info.longitude}, function (success) {
            });
        });

    });

    socket.on('mission_declined_responded', function (info) {
        console.log('mission_declined_responded', info);

        db.query("SELECT id FROM driver WHERE id = ?", [info.driverId], function (err, rows) {
            if (err) throw err;
            logEvent(1, info.driverId, 3, info.missionId, 'mission_declined_responded', "driverId_responded=" + info.driverId_responded, "");
            sendDataToDriverByDriverID(info.driverId, {'event': 'mission_declined_responded_notified', 'driverId': info.driverId_responded, 'arrivalTime': info.arrivalTime}, function (success) {
            });
        });
    });

    socket.on('driver_got_new_mission', function (info) {
        console.log('driver_got_new_mission', info);

        logEvent(1, info.driverId, 3, info.missionId, 'driver_got_new_mission', "", "");
    });

    socket.on('mission_payment_finished', function (info) {
        console.log('mission_payment_finished', info);
        db.query("UPDATE mission SET time_finishing = NOW(), state = 'COMPLETED' WHERE id = ?", [info.missionId], function (err, rows) {
            if (err) throw err;
            console.log(rows);
            var affectedRows = rows['affectedRows'];
            if (affectedRows == 0) {
                sendSMS("+79030760303", JSON.stringify(info));
            }
            db.query("SELECT m.clientInfo_id, m.driverInfo_id, m.price_in_fact_amount, m.price_in_fact_currency, c.phone FROM mission m INNER JOIN client c ON m.clientInfo_id = c.id WHERE m.id = ?", [info.missionId], function (err, rows) {
                if (err) throw err;

                var phone = rows[0]['phone'];
                var driverId = rows[0]['driverInfo_id'];
                var price_in_fact_amount = rows[0]['price_in_fact_amount'];
                var price_in_fact_currency = rows[0]['price_in_fact_currency'];

                var message = "РћР±С‰Р°СЏ СЃС‚РѕРёРјРѕСЃС‚СЊ Р’Р°С€РµР№ РїРѕРµР·РґРєРё СЃРѕСЃС‚РѕРІР»СЏРµС‚: " + price_in_fact_amount + " " + price_in_fact_currency;
                //sendSMS(phone, message);
                logEvent(1, driverId, 3, info.missionId, 'mission_payment_finished', "", "");

                clearDriverAndClient(info.missionId);
                sendDataToClientByMission(info.missionId, {'event': 'mission_payment_finished',
                    'missionId': info.missionId,
                    'price_in_fact_amount': price_in_fact_amount,
                    'price_in_fact_currency': price_in_fact_currency});

                sendDataToDriverByMission(info.missionId, {'event': 'mission_payment_finished_response',
                    'missionId': info.missionId});
            });
        });
    });

    socket.on('find_drivers', function (info) {
        console.log('find_drivers', info);
        if (info.missionId == 0 || info.missionId == undefined) {
            console.log('mission id is 0!!!', info);
            return;
        }
        db.query("SELECT M.auto_class, M.payment_type, M.from_lat, M.from_long, M.test_order, M.coast \
            ,M.clientInfo_id AS clientId \
            ,M.price_expected_amount AS price_expected_amount \
            ,SE.servicesExpected AS service \
            FROM mission AS M \
            LEFT JOIN services_expected SE ON M.id = SE.mission_id \
            WHERE M.id = ?", [info.missionId], function (err, rows) {
            if (err) throw err;

            var auto_class = rows[0]['auto_class'];
            var payment_type = rows[0]['payment_type'];
            var from_lat = rows[0]['from_lat'];
            var from_long = rows[0]['from_long'];
            var test_order = rows[0]['test_order'];
            var coast = rows[0]['coast'];
            var services = '';
            var clientId = rows[0]['clientId'];
            var price_expected_amount = rows[0]['price_expected_amount'];

            var count = 0;
            if (info.count == undefined) {
                count = info.radius;
            } else {
                count = info.count;
            }

            var query = "SELECT L.driver_id, L.latitude, L.longitude, L.mission_id, L.angle, L.mission_id_od, L.time_of_next_offer \
                        ,D.state, D.declined_driver_id, T.priority, DR.id AS driver_id_od \
                        ,get_distance_in_meters_between_geo_locations(?, ?, L.latitude, L.longitude) AS distance \
                        ,get_distance_in_meters_between_geo_locations(MDR.to_lat, MDR.to_long, L.latitude, L.longitude) AS distance_left \
                        ,DR.salary_priority AS salaryPriority \
                        ,DR.type_salary AS typeSalary\
                        ,DS.id \
                        ,RG.radius AS zoneRadius\
						FROM driver_location L \
						INNER JOIN driver D ON L.driver_id = D.id AND D.state = 'AVAILABLE' \
						INNER JOIN taxopark_partners T ON T.id = D.taxopark_id \
						INNER JOIN mission M ON M.id = ? \
						LEFT JOIN mission MDR ON D.current_mission_id = MDR.id \
						LEFT JOIN driver_requisite DR ON L.driver_id = DR.driver_id \
                        LEFT JOIN driver_setting DS ON L.driver_id = DS.driver_id \
                        LEFT JOIN driver_services DSRV ON DS.id = DSRV.driver_setting_id \
                        LEFT JOIN region RG ON M.region_id = RG.id \
						WHERE D.type_x = '0'";

            /*РџРѕРєР° РЅРµ Р·Р°РїСѓСЃРєР°РµРј*/
            /*
             for (i = 0; i < rows.length; i++) {
             if ((rows[i]['service']) != null) {
             var services = rows[i]['service'];
             query = query.concat(" AND ('"+ services +"') IN (SELECT driverServices FROM driver_services AS DSRV2 WHERE DS.id = DSRV2.driver_setting_id)");
             }
             }
             */

            //query = query.concat(" AND ('"+ services +"') IN (SELECT driverServices FROM driver_services AS DSRV2 WHERE DS.id = DSRV2.driver_setting_id)");

            if (payment_type == 'CARD') {
                query = query.concat(" AND D.entrepreneur = '1'");
            }

            /*if (services != ''){
             query = query.concat(" AND DSRV.driverServices IN (" + services + ")");
             }*/

            if (auto_class == 'COMFORT') {
                query = query.concat(" AND (D.auto_class = 'COMFORT' OR D.auto_class = 'BUSINESS')");
            } else if (auto_class == 'BUSINESS') {
                query = query.concat(" AND D.auto_class = 'BUSINESS'");
            }

            if (test_order == '1'){
                query = query.concat(" AND DR.id IS null");
            }

            /*****************************************************************************************************/
            if (isOfferForPriority1Driver(count) == true) { //РџСЂРµРґР»РѕР¶РёС‚СЊ 1-РјСѓ РїСЂРёРѕСЂРёС‚РµС‚Сѓ

                query = query.concat(" AND DR.id IS NOT null \
                    AND ((DR.salary_priority = '0' \
                    AND DR.type_salary = '0') OR (DR.salary_priority = '2')) \
                    AND DR.active = '1' \
                    AND D.current_mission_id IS null \
                    GROUP BY L.driver_id \
                    ORDER BY salary_priority ASC, distance ASC");
            }else if (isOfferForPriority1And3Driver(count) == true){ //РџСЂРµРґР»РѕР¶РёС‚СЊ 1-РјСѓ Рё 3-РјСѓ РїСЂРёРѕСЂРёС‚РµС‚Сѓ

                //AND (L.time_of_next_offer < NOW() OR L.time_of_next_offer IS null) \
                query = query.concat(" AND ((DR.salary_priority = '0' \
                    AND DR.type_salary = '0') OR (DR.salary_priority = '2')) \
                    AND DR.active = '1' \
                    AND D.current_mission_id IS null \
                    GROUP BY L.driver_id \
                    ORDER BY distance ASC");

            }else if (isOfferForPriority1And3AndGrifonDriver(count) == true){ //РџСЂРµРґР»РѕР¶РёС‚СЊ 1-РјСѓ, 3-РјСѓ РїСЂРёРѕСЂРёС‚РµС‚Сѓ, Р“СЂРёС„Р°Рј

                //AND (L.time_of_next_offer < NOW() OR L.time_of_next_offer IS null) \
                query = query.concat(" AND ((DR.salary_priority = '0' \
                    AND DR.type_salary = '0') OR (DR.salary_priority = '2') OR (DR.type_salary = '2')) \
                    AND DR.active = '1' \
                    AND D.current_mission_id IS null \
                    GROUP BY L.driver_id \
                    ORDER BY distance ASC");

                /* query = query.concat(" AND DR.id IS NOT null AND (L.mission_id_od <> '" + info.missionId + "') AND (L.time_of_next_offer < NOW() OR L.time_of_next_offer IS null) AND (DR.salary_priority = '0' OR DR.salary_priority = '1') AND DR.type_salary != '2' AND DR.active = '1' AND D.current_mission_id IS null GROUP BY L.driver_id ORDER BY distance ASC LIMIT 1");
                 }else if (isOfferForDriverInTrip(count) == true){

                 query = query.concat(" AND DR.id IS NOT null AND (L.mission_id_od <> '" + info.missionId + "') AND (L.time_of_next_offer < NOW() OR L.time_of_next_offer IS null) AND (DR.salary_priority = '0' OR DR.salary_priority = '1') AND DR.type_salary != '2' AND DR.active = '1' AND D.current_mission_id IS NOT null AND L.distance > 0 AND MDR.to_address <> '' GROUP BY L.driver_id HAVING distance_left <= 1 ORDER BY distance_left ASC LIMIT 1");

                 }else if (isOfferForGrifonDriver(count) == true){

                 query = query.concat(" AND DR.id IS NOT null AND (L.mission_id_od <> '" + info.missionId + "') AND (L.time_of_next_offer < NOW() OR L.time_of_next_offer IS null) AND (DR.type_salary = '2' OR DR.salary_priority = '0') AND DR.active = '1' AND D.current_mission_id IS null GROUP BY L.driver_id ORDER BY DR.type_salary DESC, distance ASC LIMIT 1");
                 */
                //}else{

                //query = query.concat(" AND (DR.id IS null OR DR.active = '0' OR DR.salary_priority = '2') AND D.current_mission_id IS null GROUP BY L.driver_id ORDER BY distance ASC");
            }
            /*****************************************************************************************************/
            //checkOffer(info.missionId, 0, function (success, driverId) {
            if (1 == 1) { //Р•СЃР»Рё РїСЂРµРґР»РѕР¶РёР»Рё РјРёСЃСЃРёСЋ РјРµРЅРµРµ 10 СЃРµРєСѓРЅРґ РЅР°Р·Р°Рґ, С‚Рѕ Р¶РґРµРј СЂРµР°РєС†РёРё РІРѕРґРёС‚РµР»СЏ

                db.query(query, [from_lat, from_long, info.missionId, coast], function (err, rows) {
                    if (err) throw err;


                    var count = 0;
                    if (info.count == undefined) {
                        count = info.radius;
                    } else {
                        count = info.count;
                    }

                    //anton
                    //CheckAndInformJavaForDriverX(count, info.missionId, clientId);

                    //Р•РґРµСЃСЊ РїСЂРѕР±РµР¶РёРјСЃСЏ РїРѕ РјР°СЃСЃРёРІСѓ Рё РЅР°Р№РґРµРј РЅСѓР¶РЅС‹С… РІРѕРґРёС‚РµР»РµР№ 1-РіРѕ РїСЂРёРѕСЂРёС‚РµС‚Р°
                    //Р’С‹РЅРµСЃС‚Рё РІ РѕС‚РґРµР»СЊРЅСѓСЋ С„СѓРЅРєС†РёСЋ
                    var radius = 0;
                    var drivers1PriorityAvail = false;
                    if (count <= 12){

                        for (i = 0; i < rows.length; i++) {

                            var driver_loc = rows[i];
                            var distance = distanceFromLatLonInKm(rows[i]['latitude'],
                                rows[i]['longitude'],
                                from_lat,
                                from_long);

                            radius = getRadiusForDriver(count, driver_loc);
                            var occupied = true;
                            if (driver_loc.mission_id == null || driver_loc.mission_id == 0) {
                                occupied = false;
                            }

                            if (distance <= radius && occupied == false) {//driver is near
                                if (driver_loc.salaryPriority == '0') {
                                    drivers1PriorityAvail = true;
                                    break;
                                }
                            }
                        }
                    }

                    console.log("1111111111111111111111111111111111111111111111111111 " + count + " / " + " missionId: " + info.missionId, rows);
                    var near_drivers = [];
                    if (count <= 60) { //РџСЂРµРґР»РѕР¶РёС‚СЊ 1-РјСѓ РїСЂРёРѕСЂРёС‚РµС‚Сѓ

                        for (i = 0; i < rows.length; i++) {
                            var driverId = rows[i]['driver_id'];
                            var distance = distanceFromLatLonInKm(rows[i]['latitude'],
                                rows[i]['longitude'],
                                from_lat,
                                from_long);
                            var priority = rows[i]['priority'];
                            var driver_loc = rows[i];

                            var occupied = true;
                            if (driver_loc.mission_id == null || driver_loc.mission_id == 0) {
                                occupied = false;
                            }

                            //Р—РґРµСЃСЊ РїСЂРѕРїСѓСЃРєР°РµРј РІСЃРµ, РєСЂРѕРјРµ РїСЂРёРѕСЂРёС‚РµС‚Р° 1
                            var pass = false;
                            console.log("!!!!!!!!!!!!!!!!COUNT!!!!!!!!!!!!!!!!!!!!!!!!! ", count);
                            console.log("!!!!!!!!!!!!!!!!CHECK!!!!!!!!!!!!!!!!!!!!!!!!! ", driver_loc);
                            console.log("!!!!!!!!!!!!!!!!drivers1PriorityAvail!!!!!!!!!!!!!!!!!!!!!!!!! ", drivers1PriorityAvail);
                            if (count <= 12 && drivers1PriorityAvail == true){
                                if (driver_loc.salaryPriority != '0'){
                                    console.log("!!!!!!!!!!!!!!!!PROPUSK!!!!!!!!!!!!!!!!!!!!!!!!! ", driver_loc);
                                    //continue;
                                    pass = true;
                                }
                            }

                            radius = getRadiusForDriver(info.count, driver_loc);
                            console.log("!!!!!!!!!!!!!!!!RADIUS!!!!!!!!!!!!!!!!!!!!!!!!! ", radius);

                            if (distance <= radius && occupied == false && pass == false) {//driver is near

                                //checkOffer2(info.missionId, driverId, function (success, driverId) {
                                //if (!success) { //Р•СЃР»Рё РїСЂРµРґР»РѕР¶РёР»Рё РјРёСЃСЃРёСЋ РјРµРЅРµРµ 10 СЃРµРєСѓРЅРґ РЅР°Р·Р°Рґ, С‚Рѕ Р¶РґРµРј СЂРµР°РєС†РёРё РІРѕРґРёС‚РµР»СЏ

                                console.log("333333333333333333333333333333333333333333333333333333333333333333333 " + driverId);
                                missionIsDeclinedForDriver(driverId, info.missionId, function (success, driverId, mission_id) {
                                    if (!success) {
                                        console.log("444444444444444444444444444444444444444444444444444444444444444444444444444444" + " driverId: " + driverId);

                                        driverIsNotBooked(driverId, mission_id, function (success, driverId, mission_id) {
                                            if (success) {

                                                db.query("UPDATE driver_location SET mission_id_od = ?, time_of_next_offer = DATE_ADD(NOW(), INTERVAL 10 SECOND) WHERE driver_id = ?", [info.missionId, driverId], function (err, rows1) {
                                                    if (err) throw err;

                                                    logEvent(1, driverId, 3, mission_id, 'driver_location_updated', "mission_id=" + mission_id, "");
                                                    console.log("5555555555555555555555555555555555555555555555555555555555555", driverId);

                                                    sendDataToDriverByDriverID(driverId, {'event': 'new_mission', 'missionId': mission_id}, function (success, m_id) {
                                                        if (success) {
                                                            addMissionToDeclinedList(driverId, m_id);
                                                            logEvent(1, driverId, 3, m_id, 'sent_mission_to_driver', "mission_id=" + m_id, "driver_in_trip=" +"false");
                                                        }
                                                    });

                                                });
                                            } else {
                                                logEvent(1, driverId, 3, info.missionId, 'not_sent_mission_to_driver', "mission_id=" + info.missionId, "driver_is_booked");
                                            }
                                        });

                                    }
                                });
                            }
                        }

                    } else {//РџСЂРµРґР»РѕР¶РёС‚СЊ РѕР±С‹С‡РЅРѕРјСѓ

                    }
                });


            }

            var near_drivers = [];

            var query1 = "SELECT L.driver_id, L.latitude, L.longitude, L.mission_id, L.angle, L.mission_id_od, L.time_of_next_offer\
                        ,D.state, D.declined_driver_id, T.priority, DR.id AS driver_id_od\
                        ,get_distance_in_meters_between_geo_locations(?, ?, latitude, longitude) AS distance\
						FROM driver_location L \
						INNER JOIN driver D ON L.driver_id = D.id AND D.state = 'AVAILABLE' \
						INNER JOIN taxopark_partners T ON T.id = D.taxopark_id \
						INNER JOIN mission M ON M.id = ?\
						LEFT JOIN driver_requisite DR ON L.driver_id = DR.driver_id \
						WHERE D.current_mission_id IS null";

            if (payment_type == 'CARD') {
                query1 = query1.concat(" AND D.entrepreneur = '1'")
            }

            if (auto_class == 'COMFORT') {
                query1 = query1.concat(" AND (D.auto_class = 'COMFORT' OR D.auto_class = 'BUSINESS')");
            } else if (auto_class == 'BUSINESS') {
                query1 = query1.concat(" AND D.auto_class = 'BUSINESS'");
            }

            if (test_order == '1') {
                query1 = query1.concat(" AND DR.id IS null");
            }

            db.query(query1, [from_lat, from_long, info.missionId], function (err, rows1) {
                if (err) throw err;

                //console.log("final drivers for radar " + count + " / " + " missionId: " + info.missionId, rows1);

                var near_drivers = [];
                for(i=0; i<rows1.length; i++) {

                    var driverId = rows1[i]['driver_id'];
                    var distance = distanceFromLatLonInKm(rows1[i]['latitude'],
                        rows1[i]['longitude'],
                        from_lat,
                        from_long);

                    var driver_loc = rows1[i];

                    var occupied = true;
                    if (driver_loc.mission_id == null || driver_loc.mission_id == 0) {
                        occupied = false;
                    }

                    near_drivers.push({'driver_id': driver_loc.driver_id,
                        'latitude': driver_loc.latitude,
                        'longitude': driver_loc.longitude,
                        'occupied': occupied,
                        'angle': driver_loc.angle});
                }

                socket.json.send({'event': 'find_drivers_response',
                    'drivers': near_drivers});

            });

        });
    });

    socket.on('got_mission_invite', function (info) {
        console.log('got_mission_invite', info);
        var invite = new MissionInviteModel({driverId: info.driverId,
            missionId: info.missionId,
            time: Date.now()});
        invite(function (err) {
            if (err) return console.error(err);
        });
    });

    //----------------------------BOOKED MISSIONS-----------------------

    socket.on('booked_mission_fired', function (info) {
        console.log('booked_mission_fired', info);

        db.query("SELECT booked_driver_id, from_address, to_address, time_starting, count_notified FROM mission WHERE id = ?", [info.missionId], function (err, rows) {
            if (err) throw err;

            var driverId = rows[0]['booked_driver_id'];
            var from_address = rows[0]['from_address'];
            var to_address = rows[0]['to_address'];
            var time_starting = rows[0]['time_starting'];
            var dataDict = {'event': 'booked_fired',
                'missionId': info.missionId,
                'count_notified': rows[0]['count_notified'],
                'from_address': from_address,
                'to_address': to_address,
                'time_starting': time_starting};

            logEvent(0, 0, 0, info.missionId, 'booked_mission_fired', "", "");

            sendDataToDriverByDriverID(driverId, dataDict, function (success) {
                socket.json.send({'event': 'booked_mission_fired_response',
                    'success': success});
            });
        });
    });

    socket.on('booked_mission_failed', function (info) {
        console.log('booked_mission_failed', info);

        db.query("UPDATE mission SET state = 'CANCELED' WHERE id = ?", [info.missionId], function (err, rows) {
            if (err) throw err;
            sendDataToClientByMission(info.missionId, {'event': 'mission_booked_driver_not_found',
                'missionId': info.missionId});

            logEvent(0, 0, 3, info.missionId, 'booked_mission_failed', 'state = CANCELED', "");

            missionBecameUnavailable(info.missionId);

// 			db.query("SELECT c.phone FROM mission m INNER JOIN client c WHERE m.clientInfo_id = c.id AND m.id = ?", [info.missionId], function(err, rows) {
// 				if (err) throw err;
// 				var phone = rows[0]['phone'];
// 				sendSMS(phone, "Рљ СЃРѕР¶Р°Р»РµРЅРёСЋ, РјС‹ РЅРµ РЅР°С€Р»Рё РІРѕРґРёС‚РµР»СЏ РЅР° Р’Р°С€ Р·Р°РєР°Р·.")
// 			});
        });
    });

    socket.on('mission_booked_canceled_by_server', function (info) {
        console.log('mission_booked_canceled_by_server', info);
        logEvent(0, 0, 0, info.missionId, 'mission_booked_canceled_by_server', "", "");
        missionBecameUnavailable(info.missionId);
    });

    socket.on('get_test', function (info) {


        var nowDate = new Date();
        //nowDate.setHours(nowDate.getHours() + 6);

        /*
         var nowDate2 = new Date("2015-02-01 23:45:01");
         nowDate2.setHours(nowDate2.getHours() + 6);

         getLastOfferedDriver(info, function (success, dr_id, time_of_next_offer) {
         if (success) {

         console.log('get_test nowDate', nowDate);
         console.log('get_test time_of_next_offer', time_of_next_offer);

         var res = nowDate > time_of_next_offer;

         socket.json.send({'event': 'getLastOfferedDriver',
         'success': true, 'dr_id': dr_id, 'time_of_next_offer': time_of_next_offer, 'res': res});
         }
         });
         */

    });

    socket.on('mission_canceled_by_server', function (info) {
        console.log('mission_canceled_by_server', info);
// 		sendDataToClientByMission(info.missionId, {'event': 'mission_server_canceled',
//												   'missionId': info.missionId});
        logEvent(0, 0, 0, info.missionId, 'mission_canceled_by_server', "", "");
        missionBecameUnavailable(info.missionId);
    });

    //РѕРїРѕРІРµС‰Р°РµС‚ РІРѕРґРёС‚РµР»СЏ, С‡С‚Рѕ РѕРЅ РІСЉРµС…Р°Р» РёР»Рё РІС‹РµС…Р°Р» РёР· СѓРґР°Р»РµРЅРЅРѕР№ Р·РѕРЅС‹
    socket.on('server_region_change', function (info) {

        logEvent(1, info.driverId, 3, info.missionId, 'region_change', "message=" + info.message, "fromRemote=" + info.fromRemote);

        sendDataToDriverByDriverID(info.driverId, {'event': 'driver_region_change', 'message': info.message, 'fromRemote': info.fromRemote}, function (success) {
            socket.json.send({'event': 'server_region_change_response',
                'driverId': info.driverId
            });
        });

    });

    socket.on('mission_canceled_by_operator', function (info) {
        console.log('mission_canceled_by_operator', info);

        logEvent(0, 0, 0, info.missionId, 'mission_canceled_by_operator', "", "");

        sendDataToClientByMission(info.missionId, {'event': 'mission_server_canceled',
            'missionId': info.missionId});
        sendDataToDriverByMission(info.missionId, {'event': 'mission_removed_from_driver',
            'missionId': info.missionId});
        missionBecameUnavailable(info.missionId);
    });

    socket.on('mission_booked_client_cancel', function (info) {
        console.log('mission_booked_client_cancel', info);

        db.query("SELECT state FROM mission WHERE id = ?", [info.missionId], function (err, rows) {
            if (err) throw err;
            var state = rows[0]['state'];
            saveCancelDetails({'missionId': info.missionId,
                'cancel_by': 'client',
                'previous_state': state});

            db.query("UPDATE mission SET state = 'CANCELED' WHERE id = ?", [info.missionId], function (err, rows) {
                var m_id = info.missionId;
                if (err) {
                    m_id = 0;
                }
                logEvent(2, 0, 3, info.missionId, 'mission_booked_client_cancel', 'state = CANCELED', "");

                socket.json.send({'event': 'mission_booked_client_canceled_response',
                    'missionId': m_id});

                sendDataToDriverByMission(info.missionId, {'event': 'mission_booked_client_canceled',
                    'missionId': info.missionId});
                missionBecameUnavailable(info.missionId);
            });
        });
    });

    socket.on('new_booked_apears', function (info) {
        console.log('new_booked_apears', info);
        db.query("SELECT di.socket_id, ddi.driver_id, di.id AS device_id FROM driver_device_info ddi INNER JOIN device_info di ON di.id = ddi.devices_id INNER JOIN driver d ON d.id = ddi.driver_id INNER JOIN mission m ON m.id = ? WHERE di.socket_id IS NOT NULL AND m.auto_class = d.auto_class", [info.missionId], function (err, rows) {
            if (err) throw err;

            logEvent(2, 0, 0, info.missionId, 'new_booked_apears', "", "");

            for (var i in rows) {
                if (rows[i] !== undefined) {
                    var socketID = rows[i]['socket_id'];
                    var socket = io.sockets.sockets[socketID];
                    var dataDict = {'event': 'new_booked', 'missionId': info.missionId};
                    if (socket) {
                        console.log("send booked to driver:", rows[i]['driver_id']);
                        socket.json.send(dataDict);
                    } else {
//						saveFailedData(rows[i]['device_id'], dataDict);
                    }
                }
            }

        });
    });


    socket.on('take_booked', function (info) {
        console.log('take_booked', info);
        db.query("SELECT M.state, D.taxopark_id \
                  FROM mission AS M \
                  LEFT JOIN driver D ON M.driverInfo_id = D.id \
                  WHERE M.id = ? AND (D.type_x = true OR M.driverInfo_id IS NULL)", [info.missionId], function (err, rows) {
            if (err) throw err;

            if (rows.length > 0) {
                var state = rows[0]['state'];
                var taxopark_id = rows[0]['taxopark_id'];

                db.query("UPDATE mission SET booking_state = 'DRIVER_ASSIGNED', time_assigning = NOW(), booked_driver_id = ?, driverInfo_id= ?, taxopark_id = ? WHERE id = ? AND booking_state = 'WAITING'", [info.driverId, info.driverId, taxopark_id, info.missionId], function (err, rows) {
                    if (err) throw err;

                    var affectedRows = rows['affectedRows'];
                    var took = affectedRows == 1;

                    logEvent(1, info.driverId, 3, info.missionId, 'take_booked', "tooked=" + took, "");

                    missionBecameUnavailable(info.missionId, info.driverId);

                    socket.json.send({'event': 'take_booked_response',
                        'missionId': info.missionId,
                        'took': took
                    });

                    if (took) {
                        sendDataToClientByMission(info.missionId, {'event': 'mission_booked_driver_assigned',
                            'missionId': info.missionId});
                        db.query("SELECT c.phone FROM mission m INNER JOIN client c ON c.id = m.clientInfo_id WHERE m.id = ?", [info.missionId], function (err, rows) {
                            if (err) throw err;
                            var phone = rows[0]['phone'];
                            sendSMS(phone, "Р’Р°С€Р° Р±СЂРѕРЅСЊ РїРѕРґС‚РІРµСЂР¶РґРµРЅР°. РћС‚РєСЂРѕР№С‚Рµ РїСЂРёР»РѕР¶РµРЅРёРµ, С‡С‚РѕР±С‹ РїРѕСЃРјРѕС‚СЂРµС‚СЊ РґР°РЅРЅС‹Рµ Рѕ РўР°РєСЃРёСЃС‚Рѕ.");
                        });
                    }
                });
            }
        });
    });


    socket.on('approve_booked', function (info) {
        console.log('approve_booked', info);
        db.query("SELECT m.count_notified FROM mission m WHERE id = ?", [info.missionId], function (err, rows) {
            if (err) throw err;
            var count_notified = rows[0]['count_notified'];
            count_notified = count_notified + 1;
            var query = "";
            var params = [];
            if (count_notified > 1) {
                query = "UPDATE mission SET booking_state = 'DRIVER_NOTIFIED', count_notified = ? WHERE id = ?";
            } else {
                query = "UPDATE mission SET count_notified = ? WHERE id = ?";
            }
            db.query(query, [count_notified, info.missionId], function (err, rows) {
                if (err) throw err;
                var affectedRows = rows['affectedRows'];
                var approved = affectedRows != 0;

                logEvent(1, 0, 3, info.missionId, 'approve_booked', "count_notified=" + count_notified, "approved=" + approved);

                socket.json.send({'event': 'approve_booked_response',
                    'missionId': info.missionId,
                    'approved': approved
                });
            });
        });

    });

    socket.on('mission_booked_driver_cancel', function (info) {
        console.log('mission_booked_driver_cancel', info);
        socket.json.send({'event': 'mission_booked_driver_cancel_response',
            'missionId': info.missionId,
            'canceled': false,
            'message': 'Р•СЃР»Рё РІС‹ РѕС‚РєР°Р¶РёС‚РµСЃСЊ РѕС‚ РїРѕРµР·РґРєРё, С‚Рѕ СЃ Р’Р°СЃ Р±СѓРґРµС‚ РІР·СЏС‚ С€С‚СЂР°С„ РІ СЂР°Р·РјРµСЂРµ ' + amount_penalty + ' СЂСѓР±'});
    });

    socket.on('mission_booked_fine_driver', function (info) {
        console.log('mission_booked_fine_driver', info);
        if (info.missionId == 0) {
            console.log('missionId is 0!!!');
            return;
        }
        fineDriver(info.missionId, amount_penalty, function (missionId) {
            db.query("UPDATE mission SET state = 'BOOKED', booking_state = 'WAITING', booked_driver_id = NULL, driverInfo_id = NULL, time_assigning = NULL, time_assigning = NULL, count_notified = 0 WHERE id = ?", [info.missionId], function (err, rows) {
                if (err) throw err;

                logEvent(1, 0, 3, info.missionId, 'mission_booked_fine_driver', "amount_penalty=" + amount_penalty, "");

                console.log("booked has declined, let's find new drivers");
                findDriversForBooked(missionId);
            });
        });
// 		db.query("SELECT d.account_id, m.driverInfo_id FROM mission m INNER JOIN driver d ON d.id = m.driverInfo_id WHERE m.id = ?", [info.missionId], function(err, rows) {
// 			if (err) throw err;
// 			var account_id = rows[0]['account_id'];
// 			var driverId = rows[0]['driverInfo_id'];
// 			var penalty_in_coins = amount_penalty*100;
// 			db.query("UPDATE account SET money_amount = money_amount - "+penalty_in_coins+" WHERE id = ?", [account_id], function(err, rows) {
// 				if (err) throw err;
// 				db.query("INSERT INTO driver_cash_flow (date_operation, operation, sum, driver_id, mission_id) VALUES (NOW(),'1',?,?,?)", [(penalty_in_coins*(-1)), driverId, info.missionId], function(err, rows) {
// 					if (err) throw err;
// 				});
// 			});
//
//
//
// 		});

    });

    socket.on('mission_not_taken_fine_own_driver', function (info) {
        console.log('mission_not_taken_fine_own_driver', info);
        if (info.missionId == 0) {
            console.log('missionId is 0!!!');
            return;
        }

        db.query("SELECT M.state AS state, M.price_expected_amount \
            ,D.current_mission_id AS current_mission_id \
            ,D.type_x AS type_x \
            ,DR.type_salary AS type_salary\
            FROM mission AS M \
            LEFT JOIN driver D ON M.driverInfo_id = D.id \
            LEFT JOIN driver_requisite DR ON D.id = DR.driver_id \
            WHERE M.id = ?", [info.missionId], function (err, rows) {
            if (err) throw err;

            //РќСѓР¶РЅР° РїСЂРѕРІРµСЂРєР° РЅР°
            //+1. РњРёСЃСЃРёСЏ РјРѕР¶РµС‚ Р±С‹С‚СЊ Рё РЅРµ РІ СЃС‚Р°С‚СѓСЃРµ NEW Рё РЅР° Р¤Р°РЅС‚РѕРјРЅРѕРј РІРѕРґРёС‚РµР»Рµ. РўР°РєСѓСЋ РЅСѓР¶РЅРѕ РѕР±СЂР°Р±Р°С‚С‹РІР°С‚СЊ РєР°Рє NEW.
            //+2. Р•СЃР»Рё РІРѕРґРёС‚РµР»СЊ СЃ type_salary '2' Рё СЃСѓРјРјР° Р·Р°РєР°Р·Р° < 250 СЂСѓР±, С‚Рѕ РµРіРѕ РЅРµ С€С‚СЂР°С„СѓРµРј.
            //+3. Р•СЃР»Рё РїР°РґР°Р»СЊС‰РёРє РЅРµ РІР·СЏР» Р·Р°РєР°Р·, С‚Рѕ С€С‚СЂР°С„ 200 СЂ.

            var amount_penalty = 0;

            if (rows[0]["type_salary"] == 2){
                amount_penalty = 200;
            }

            /*
             if (rows[0]["price_expected_amount"] < 250 && rows[0]["type_salary"] == 2) {

             return;
             }
             */

            if ((rows[0]["state"] == 'NEW' && rows[0]["current_mission_id"] == null) || (rows[0]["type_x"] == '1')) {

                fineOwnDriver(info.driverId, info.missionId, amount_penalty, function (missionId) {
                });

                var dataDict = {'event': 'dispatcher_message',
                    'text': 'РЎ Р’Р°СЃ Р±С‹Р» СЃРїРёСЃР°РЅ С€С‚СЂР°С„ РІ СЂР°Р·РјРµСЂРµ ' + amount_penalty + ' СЂСѓР±. Р·Р° РЅРµРїСЂРёРЅСЏС‚С‹Р№ Р·Р°РєР°Р·!'};
                sendDataToDriverByDriverID(info.driverId, dataDict, function (success) {
                    logEvent(1, info.driverId, 3, info.missionId, 'mission_not_taken_fine_own_driver', "", "");
                });
            }
        });
    });

    socket.on('driver_message', function (info) {
        var dataDict = {'event': 'dispatcher_message',
            'text': info.text};
        sendDataToDriverByDriverID(info.driverId, dataDict, function (success) {
            socket.json.send({'event': 'driver_message_response',
                'success': success});
            logEvent(1, info.driverId, 3, 0, 'driver_message', info.text, "");
        });
    });

    socket.on('driver_message_arm', function (info) {
        var dataDict = {'event': 'dispatcher_message',
            'text': info.text};
        sendDataToDriverByDriverID(info.driverId, dataDict, function (success) {
            socket.json.send({'event': 'driver_message_response',
                'driverId': info.driverId,
                'success': success});
            logEvent(1, info.driverId, 3, 0, 'driver_message_arm', info.text, "");
        });
    });

    socket.on('transfer_mission_to_driver', function (info) {
        var dataDict1 = {'event': 'mission_assigned_to_driver',
            'missionId': info.missionId};
        sendDataToDriverByDriverID(info.to_driverId, dataDict1, function (success) {

        });

        if (info.from_driverId != undefined) {
            var dataDict2 = {'event': 'mission_removed_from_driver',
                'missionId': info.missionId};
            sendDataToDriverByDriverID(info.from_driverId, dataDict2, function (success) {

            });


            if (info.state == 'ASSIGNED' && info.booked == true) {
                console.log('RETURN !!!');
                  return;
            } else {
                var dataDict3 = {'event': 'driver_changed',
                    'missionId': info.missionId,
                    'arrivalTime': info.arrivalTime,
                    'distance': info.distance};
                sendDataToClientByMission(info.missionId, dataDict3);
                logEvent(1, info.driverId, 3, info.missionId, 'driver_changed', "distance=" + info.distance, "arrivalTime=" + info.arrivalTime);
            }

        }
    });

    socket.on('autosearch_canceled', function (info) {
        missionBecameUnavailable(info.missionId);

        var dataDict = {'event': 'cancel_autosearch', 'missionId': info.missionId};

        sendDataToClientByMission(info.missionId, dataDict);
        //logEvent(1, info.missionId, 3, 0, 'autosearch_canceled', "", "");
    });

    //---------------------------CARD PAYMENTS

    socket.on('ask_client_for_card_payment', function (info) {
        console.log('ask_client_for_card_payment', info);
        var dataDict = {'event': 'mission_client_payment_by_card',
            'missionId': info.missionId,
            'amount': info.amount};
        sendDataToClientByMission(info.missionId, dataDict);
        logEvent(2, info.missionId, 3, info.missionId, 'ask_client_for_card_payment', "amount=" + info.amount, "");
    });

    socket.on('send_client_card_payment_decision', function (info) {
        console.log('send_client_card_payment_decision', info);
        var dataDict = {'event': 'mission_client_payment_card_decision',
            'missionId': info.missionId,
            'agree': info.agree};
        //'isOld': info.isOld};
        sendDataToDriverByMission(info.missionId, dataDict);
        logEvent(2, info.missionId, 3, info.missionId, 'ask_client_for_card_payment', "agree=" + info.agree, "");
    });

    socket.on('send_mission_payment_result', function (info) {
        console.log('send_mission_payment_result', info);
        var dataDict = {'event': 'mission_card_payment_result',
            'missionId': info.missionId,
            'result': info.result,
            'answer': info.answer};
        sendDataToDriverByMission(info.missionId, dataDict);
        logEvent(2, info.missionId, 3, info.missionId, 'send_mission_payment_result', "result=" + info.result, "answer=" + info.answer);
    });

});

//------------------------MongoDB methods---------------

function createAndCompileLocationScheme() {
    var locationSchema = new mongoose.Schema({
//		clientId: { type: Number},
        missionId: { type: Number},
        driverId: { type: Number},
        latitude: { type: Number},
        longitude: { type: Number},
        when_seen: { type: Number},
        type: { type: String} //going_to_client, going_with_client, stop_with_client, trouble, free
    });

    return mongoose.model('Location', locationSchema);
}

function createAndCompileMissionInviteScheme() {
    var missionInviteSchema = new mongoose.Schema({
        driverId: { type: Number},
        missionId: { type: Number},
        time: { type: Number}
    });

    return mongoose.model('MissonInvites', missionInviteSchema);
}

function createAndCompileLogEventScheme() {
    var logEventSchema = new mongoose.Schema({
        date_time: { type: Number },
        object_type: { type: Number},//0 - system, 1 - driver, 2 - client, 3 - web_user
        object_id: { type: String},//driver_id, client_id, web_user_id
        event_id: { type: Number},//0 - read, 1 - insert, 2 - delete, 3 - change
        mission_id: { type: Number},
        event_field_1: {type: String},//event type
        event_field_2: {type: String},
        event_field_3: {type: String}
    });

    return mongoose.model('events', logEventSchema);
}

function logEvent(objType, objId, eventId, missionId, field1, field2, field3) {
    if (field3 == null) {
        field3 = "";
    }
    var logevent = new LogEventModel({ date_time: Math.floor(Date.now() / 1000),
        object_type: objType,
        object_id: objId,
        event_id: eventId,
        mission_id: missionId,
        event_field_1: field1,
        event_field_2: field2,
        event_field_3: field3});

    logevent.save(function (err) {
        if (err) return console.error(err);
    });
}

function saveLocation(info, state) {
    var d_id = info.location['driverId'];
    if (d_id == undefined) {
        d_id = 0;
    }
    var location = new LocationModel({driverId: d_id,
        missionId: info.missionId,
        latitude: info.location.latitude,
        longitude: info.location.longitude,
        when_seen: Date.now(),
        type: state});
    location.save(function (err) {
        if (err) return console.error(err);
    });
}

//-----------------------Save cencel details-----------

function saveCancelDetails(info) {
    db.query("INSERT INTO mission_canceled (cancel_by, mission_id, time_of_canceled, state_before_canceled) VALUES (?,?,NOW(),?)", [info.cancel_by, info.missionId, info.previous_state], function (err, rows) {
//		if (err) throw err;

    });
}

//------------------------Find new drivers for booked-----------

function findDriversForBooked(missionId, driverId) {
    db.query("SELECT auto_class, payment_type FROM mission WHERE id = ?", [missionId], function (err, rows) {
        if (err) throw err;

        var auto_class = rows[0]['auto_class'];
        var payment_type = rows[0]['payment_type'];

        var query = "SELECT di.socket_id, ddi.driver_id, di.id AS device_id FROM driver_device_info ddi INNER JOIN device_info di ON di.id = ddi.devices_id INNER JOIN driver d ON d.id = ddi.driver_id INNER JOIN mission m ON m.id = ? WHERE di.socket_id IS NOT NULL AND ddi.driver_id != ?";

        if (payment_type == 'CARD') {
            query = query.concat(" AND d.entrepreneur = '1'")
        }

        if (auto_class == 'COMFORT') {
            query = query.concat(" AND (d.auto_class = 'COMFORT' OR d.auto_class = 'BUSINESS')");
        } else if (auto_class == 'BUSINESS') {
            query = query.concat(" AND d.auto_class = 'BUSINESS'");
        }


        db.query(query, [missionId, driverId], function (err, rows) {
            if (err) throw err;

            for (var i in rows) {
                if (rows[i] !== undefined) {
                    var socketID = rows[i]['socket_id'];
                    var socket = io.sockets.sockets[socketID];
                    var dataDict = {'event': 'new_booked', 'missionId': missionId};
                    if (socket) {
                        socket.json.send(dataDict);
                    } else {
                        //								saveFailedData(rows[i]['device_id'], dataDict);
                    }
                }
            }

        });
    });
}

//------------------------Fine driver-------------------------

function fineDriver(missionId, penalty_in_rubles, callback) {
    db.query("SELECT d.account_id AS account_id, m.driverInfo_id FROM mission m INNER JOIN driver d ON d.id = m.driverInfo_id WHERE m.id = ?", [missionId], function (err, rows) {
        if (err) throw err;
        var account_id = rows[0]['account_id'];
        var driverId = rows[0]['driverInfo_id'];
        var penalty_in_coins = penalty_in_rubles * 100;
        db.query("UPDATE account SET money_amount = money_amount - " + penalty_in_coins + " WHERE id = ?", [account_id], function (err, rows) {
            if (err) throw err;
            db.query("INSERT INTO driver_cash_flow (date_operation, operation, sum, driver_id, mission_id) VALUES (NOW(),'1',?,?,?)", [(penalty_in_coins * (-1)), driverId, missionId], function (err, rows) {
                if (err) throw err;
                callback(missionId);
            });
        });

    });
}

function fineOwnDriver(driverId, missionId, penalty_in_rubles, callback) {
    db.query("SELECT account_id FROM driver WHERE id = ?", [driverId], function (err, rows) {
        if (err) throw err;
        var account_id = rows[0]['account_id'];
        var penalty_in_coins = penalty_in_rubles * 100;
        db.query("UPDATE account SET money_amount = money_amount - " + penalty_in_coins + " WHERE id = ?", [account_id], function (err, rows) {
            if (err) throw err;
            db.query("INSERT INTO driver_cash_flow (date_operation, operation, sum, driver_id, mission_id) VALUES (NOW(),'1',?,?,?)", [(penalty_in_coins * (-1)), driverId, missionId], function (err, rows) {
                if (err) throw err;
                callback(missionId);
            });
        });

    });
}

//------------------------Save driver location---------------

function saveDriverLocation(info) {
    var missionId = info.missionId;
    if (missionId == 0) {
        missionId = null;
    }

    var query = "UPDATE driver_location SET latitude=?, longitude=?, when_seen=Now(), mission_id=?, distance=?, angle=? WHERE driver_id=?";
    db.query(query, [info.location.latitude, info.location.longitude, missionId, info.location.distance, info.location.angle, info.location.driverId], function (err, rows) {
        if (err) throw err;
        var affectedRows = rows['affectedRows'];
        if (affectedRows == 0) {
            var query = "INSERT INTO driver_location (when_seen, latitude, longitude, driver_id, mission_id, distance, angle) VALUES (Now(),?,?,?,?,?,?)";
            db.query(query, [info.location.latitude, info.location.longitude, info.location.driverId, missionId, info.location.distance, info.location.angle], function (err, rows) {
                if (err) console.log(err);

                db.query("UPDATE mission SET distance_real=? WHERE id = ?", [info.location.distance, missionId], function (err, rows) {
                    if (err) throw err;
                });
            });
        }
    });
}

//------------------------Send data to client device by mission_id---------------

function sendDataToClientByMission(missionId, dataDict) {
    db.query("SELECT clientInfo_id FROM mission WHERE id = ?", [missionId], function (err, rows) {
        if (err) throw err;
        var client_id = rows[0]['clientInfo_id'];

        sendDataToClient(client_id, dataDict)
    });
}

//------------------------Send data to client by client id----------------

function sendDataToClient(clientId, dataDict) {
    db.query("SELECT devices_id FROM client_device_info WHERE client_id = ?", [clientId], function (err, rows) {
        if (err) throw err;
        for (var i in rows) {
            var d_id = rows[i]['devices_id'];
            console.log("send to client", d_id);
            db.query("SELECT socket_id FROM device_info WHERE socket_id IS NOT NULL AND id = ?", [d_id], function (err, rows) {
                if (err) throw err;
                if (rows[0] !== undefined) {
                    var socketID = rows[0]['socket_id'];
                    var socket = io.sockets.sockets[socketID];
                    if (socket != null) {
                        socket.json.send(dataDict);
                    } else {
//							saveFailedData(d_id, dataDict);
                    }
                }
            });
        }
    });
}

//РћС‚РїСЂР°РІРєР° РґР°РЅРЅС‹С… С‚РѕР»СЊРєРѕ РґРёСЃРїРµС‚С‡РµСЂР°Рј СЃ С‚РµР»РµС„РѕРЅРёРµР№
function sendDataToARM(missionId, dataDict) {
    db.query("SELECT socket_id FROM web_users WHERE sip_user IS NOT NULL", [missionId], function (err, rows) {
        if (err) throw err;
        for (var i in rows) {
            var socketId = rows[i]['socket_id'];

            var socket = io.sockets.sockets[socketId];
            if (socket != null) {
                socket.json.send(dataDict);
            } else {
            }
        }
    });
}

//------------------------Save failed data for device----------------------

function saveFailedData(deviceId, info) {
    console.log("SAVE FAILED DATA");
    db.query("UPDATE device_info SET last_failed_data = ? WHERE id = ?", [JSON.stringify(info), deviceId], function (err, rows) {
        if (err) throw err;

    });
}

//------------------------Send data to driver device by mission_id---------------

function sendDataToDriverByMission(missionId, dataDict) {
    if (missionId == 0) {
        return;
    }

    db.query("SELECT driverInfo_id FROM mission WHERE id = ?", [missionId], function (err, rows) {
        if (err) throw err;
        var driver_id = rows[0]['driverInfo_id'];
        sendDataToDriverByDriverID(driver_id, dataDict, function (success) {
            if (success) {
                console.log("sent to driver " + driver_id);
            }
        });

    });
}

//------------------------Send data to driver device by driver_id---------------

function sendDataToDriverByDriverID(driverId, dataDict, callback) {
    db.query("SELECT devices_id FROM driver_device_info WHERE driver_id = ?", [driverId], function (err, rows) {
        if (err) throw err;
        for (var i in rows) {
            var d_id = rows[i]['devices_id'];
            console.log("trying to sent to driver with device " + d_id);
            db.query("SELECT socket_id FROM device_info WHERE socket_id IS NOT NULL AND id = ?", [d_id], function (err, rows) {
                if (err) {
                    callback(false, dataDict.missionId);
//						saveFailedData(d_id, dataDict);
                    throw err;
                }
                if (rows[0] !== undefined) {
                    var socketID = rows[0]['socket_id'];
                    var socket = io.sockets.sockets[socketID];
                    if (socket) {
                        console.log("found socket");
                        socket.json.send(dataDict);
                        callback(true, dataDict.missionId);
                    } else {
                        console.log("socket not found");
//							saveFailedData(d_id, dataDict);
                        callback(false, dataDict.missionId);
                    }
                } else {
                    callback(false, dataDict.missionId);
                }
            });
        }
    });
}

//------------------------Mission became unavailable-----------

function missionBecameUnavailable(missionId, driverId) {
    if (driverId == undefined) {
        driverId = 0;
    }
    db.query("SELECT di.socket_id AS socket_id, ddi.driver_id AS driver_id FROM driver_device_info ddi INNER JOIN device_info di WHERE ddi.devices_id = di.id AND ddi.driver_id != ? AND di.socket_id IS NOT NULL", [driverId], function (err, rows) {
        if (err) throw err;
        for (var i in rows) {
            var socket_id = rows[i]["socket_id"];
            var socket = io.sockets.sockets[socket_id];
            if (socket) {
                socket.json.send({'event': 'mission_became_unavailable',
                    'missionId': missionId});
                logEvent(1, rows[i]["driver_id"], 3, missionId, 'mission_became_unavailable', "driverId=" + rows[i]["driver_id"], "");
            }
        }
    });
}

//------------------------Operations with declined list: add/remove/check if contains

function addMissionToDeclinedList(driverId, missionId) {
    db.query("SELECT declined_driver_id FROM driver WHERE id = ?", [driverId], function (err, rows) {
        if (err) throw err;
        var declined_driver_id = rows[0]["declined_driver_id"];
        var declined_ids = [];
        if (declined_driver_id != null) {
            declined_ids = declined_driver_id.split(',');
        }
        if (declined_ids.indexOf(missionId.toString()) < 0) {
            declined_ids.push(missionId);
        }
        var declined_str = declined_ids.join(',');
        db.query("UPDATE driver SET declined_driver_id = ? WHERE id = ?", [declined_str, driverId], function (err, rows) {
            if (err) throw err;
        });
    });
}


function removeMissionFromDeclinedList(missionId) {
    db.query("SELECT declined_driver_id, id FROM driver", function (err, rows) {
        if (err) throw err;
        for (var i in rows) {
            var declined_driver_id = rows[i]["declined_driver_id"];
            var driverId = rows[i]["id"];
            var declined_ids = [];
            if (declined_driver_id != null) {
                declined_ids = declined_driver_id.split(',');
            }
            var idx = declined_ids.indexOf(missionId.toString());
            if (idx >= 0) {
                declined_ids = declined_ids.splice(idx, 1);
            }
            var declined_str = declined_ids.join(',');
            db.query("UPDATE driver SET declined_driver_id = ? WHERE id = ?", [declined_str, driverId], function (err, rows) {
                if (err) throw err;
            });
        }
    });
}

//Р•СЃР»Рё РµСЃС‚СЊ Р±СЂРѕРЅСЊ Р±Р»РёР¶Рµ, С‡РµРј РЅР° Р±Р»РёР¶Р°Р№С€РёР№ С‡Р°СЃ, С‚Рѕ TRUE
function driverIsNotBooked(driverId, mission_id, callback) {

    db.query("SELECT * FROM mission WHERE booked_driver_id = ? AND state NOT IN('CANCELED','COMPLETED') AND DATE_ADD(NOW(), INTERVAL 1 HOUR) > time_starting LIMIT 1", [driverId], function (err, rows) {
        if (err) throw err;

        if (rows.length > 0) {
            callback(false, driverId, mission_id);
            return;
        } else {

            db.query("SELECT COUNT(id) AS ordersCount FROM mission WHERE state = 'ASSIGNED' AND driverInfo_id = ?", [driverId], function (err, rows) {
                if (err) throw err;

                if (rows.length > 0) {
                    if (rows[0]["ordersCount"] == 0){
                        callback(true, driverId, mission_id);
                    }else{
                        callback(false, driverId, mission_id);
                    }
                } else {
                    callback(true, driverId, mission_id);
                }
            });

        }
    });
}


function missionIsDeclinedForDriver(driverId, missionId, callback) {
    db.query("SELECT declined_driver_id FROM driver WHERE id = ?", [driverId], function (err, rows) {
        if (err) throw err;
        var declined_driver_id = rows[0]["declined_driver_id"];
        var declined_ids = [];
        if (declined_driver_id != null) {
            declined_ids = declined_driver_id.split(',');
        }
        if (declined_ids.indexOf(missionId.toString()) >= 0) {
            callback(true, driverId, missionId);
        } else {
            callback(false, driverId, missionId);
        }
    });
}

//------------------------Clear DB-------------

function clearDB() {
    db.query("UPDATE device_info SET socket_id = NULL", function (err, rows) {
        if (err) throw err;
        db.query("UPDATE driver SET state = 'OFFLINE' WHERE state NOT LIKE('%BUSY%') and type_x = '0'", function (err, rows) {
            if (err) throw err;
            console.log("DB has been cleared");
        });
    });
}

//------------------------Calculate distance between two geographic coordinates----------------

function distanceFromLatLonInKm(lat1, lon1, lat2, lon2) {
    var R = 6371; // Radius of the earth in km
    var dLat = deg2rad(lat2 - lat1);  // deg2rad below
    var dLon = deg2rad(lon2 - lon1);
    var a =
            Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2)
        ;
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    var d = R * c; // Distance in km
    return d;
}

function deg2rad(deg) {
    return deg * (Math.PI / 180)
}

//-------------------------Finish pause ----------

function finishPause(missionId) {
    db.query("SELECT start_pause FROM pauses WHERE mission_id = ? AND end_pause IS NULL ORDER BY start_pause", [missionId], function (err, rows) {
        if (err) throw err;
        if (!isEmpty(rows)) {
            var startPause = rows[objCount(rows) - 1]["start_pause"];
            db.query("UPDATE pauses SET end_pause = NOW() WHERE start_pause = ?", [startPause], function (err, rows) {
                if (err) throw err;
            });
        }
    });
}

//-------------------------Clear mission in driver and client-------------------------

function clearDriverAndClient(missionId) {
    if (missionId == 0) {
        return;
    }
    db.query("SELECT clientInfo_id, driverInfo_id FROM mission WHERE id = ?", [missionId], function (err, rows) {
        if (err) throw err;
        var clientId = rows[0]['clientInfo_id'];
        var driverId = rows[0]['driverInfo_id'];

        db.query("UPDATE client SET mission_id = NULL WHERE id = ?", [clientId], function (err, rows) {
            if (err) throw err;
        });

        db.query("UPDATE driver SET current_mission_id = NULL WHERE id = ? AND current_mission_id = ?", [driverId, missionId], function (err, rows) {
            if (err) throw err;

        });
    });
}

//-------------------------Clear mission in driver and client-------------------------

function findNearestOwnDriver(missionId) {
    if (missionId == 0) {
        return;
    }
    db.query("SELECT clientInfo_id, driverInfo_id FROM mission WHERE id = ?", [missionId], function (err, rows) {
        if (err) throw err;
        var clientId = rows[0]['clientInfo_id'];
        var driverId = rows[0]['driverInfo_id'];

        db.query("UPDATE client SET mission_id = NULL WHERE id = ?", [clientId], function (err, rows) {
            if (err) throw err;
        });

        db.query("UPDATE driver SET current_mission_id = NULL WHERE id = ?", [driverId], function (err, rows) {
            if (err) throw err;

        });
    });
}

//--------------------------Send assigned SMS------------

function sendAssignedSMS(missionId, phone) {
    db.query("SELECT d.first_name, d.auto_color, d.auto_model, d.auto_mumber, d.phone FROM mission m INNER JOIN driver d ON d.id = m.driverInfo_id WHERE m.id = ?", [missionId], function (err, rows) {
        if (err) throw err;
        var d_auto_color = rows[0]['auto_color'];
        var d_auto_model = rows[0]['auto_model'];
        var d_auto_number = rows[0]['auto_mumber'];
        var d_name = rows[0]['first_name'];
        var d_phone = rows[0]['phone'];
        var message = "Р—РґСЂР°РІСЃС‚РІСѓР№С‚Рµ! Р’Р°Рј РЅР°Р·РЅР°С‡РµРЅ РўР°РєСЃРёСЃС‚Рѕ: " + d_auto_color + " " + d_auto_model + " " + d_auto_number + ". Р’РѕРґРёС‚РµР»СЊ: " + d_name + " " + d_phone + ". РќРѕРјРµСЂ Р·Р°РєР°Р·Р°: " + missionId;
        console.log(message);
        sendSMS(phone, message);
    });
}

//--------------------------Send SMS--------------------

function sendSMS(phone, message) {
    var request = require('request');
    var headers = {'Content-Type': 'application/x-www-form-urlencoded'};
    var options = {
        url: 'http://smsc.ru/sys/send.php',
        method: 'GET',
        headers: headers,
        qs: {'login': 'ttehnolodgis',
            'psw': 'KrexQ8b',
            'phones': normalizePhone(phone),
            'mes': message,
            'charset': 'utf-8'}
    };
    request(options, function (error, response, body) {
        if (!error && response.statusCode == 200) {
            console.log(body);
        }
    });
}

//--------------------------Priority--------------------


function radiusForPriority1Driver(count) {

    return 3;
}

function isOfferForPriority1Driver(count) {
    if (count <=12){
        return true;
    }
}

function isOfferForPriority1And3Driver(count) {
    if (count >= 13 && count <= 30){
        return true;
    }
}

function isOfferForPriority1And3AndGrifonDriver(count) {
    if (count >= 31 && count <= 60){
        return true;
    }
}

function radiusForDriverInTrip(count) {

    return 3;
}

function isOfferForDriverInTrip(count) {

    if ((count >= 30 && count <= 32) || (count >= 50 && count <= 52)) {
        return true;
    }

    return false;
}


function getRadiusForDriver(count, driver_loc){

    if (count <=12){
        return 3;
    }else if (count >= 13 && count <= 30){
        return 5;
    }else if (count >= 31 && count <= 60){
        return 10;
    }

}

function offerMissionToDriver(info){

    db.query("UPDATE driver_location SET mission_id_od = ?, time_of_next_offer = DATE_ADD(NOW(), INTERVAL 10 SECOND) WHERE driver_id = ?", [info.missionId, info.driverId], function (err, rows) {
        if (err) throw err;
    });

    return true;
}


//РџСЂРѕРІРµСЂРёРј - РЅРµС‚ Р»Рё Р°РєС‚РёРІРЅРѕРіРѕ РїСЂРµРґР»РѕР¶РµРЅРёСЏ
//true - РµСЃС‚СЊ, false  - РЅРµС‚ РёР»Рё РїСЂРѕСЃСЂРѕС‡РµРЅРѕ
function checkOffer(missionId, driverId, callback) {

    db.query("SELECT mission_id_od, max(time_of_next_offer) AS time_of_next_offer, driver_id FROM driver_location WHERE mission_id_od = ? ORDER BY time_of_next_offer DESC LIMIT 1", [missionId], function (err, rows) {
        if (err) throw err;

        if (rows.length > 0) {
            var nowDate = new Date();
            var time_of_next_offer = rows[0]['time_of_next_offer'];

            //if (time_of_next_offer == null || time_of_next_offer == 'null'){
            if (time_of_next_offer == null){
                callback(false, driverId);
                return;
            }

            //console.log('***************** TIME OF NEXT OFFER ************************* ' + time_of_next_offer, rows);
            //console.log('***************** TIME OF NEXT OFFER DRIVER ID************************* ' + driverId);
            if (time_of_next_offer > nowDate && time_of_next_offer != null) {//РџРѕСЃР»РµРґРЅРёР№ РІРѕРґРёС‚РµР»СЊ РґСѓРјР°РµС‚ РµС‰Рµ
                //console.log('***************** TIME OF NEXT OFFER TRUE************************* ' + time_of_next_offer, rows);
                callback(true, driverId);
                return;
            }else{
                callback(false, driverId);
                return;
            }
        } else {
            callback(false, driverId);
            return;
        }

    });

    return false;
}


function CheckAndInformJavaForDriverX(count, missionId, clientId){

    if (count >= 36){

        db.query("select mf.* from mission_fantom_driver AS mf inner join mission m on mf.mission_id = m.id where m.clientInfo_id = ? and mf.time_assigning >= date_sub(NOW(), interval 60 minute)", [clientId], function (err, rows0) {
            if (err) throw err;

            //console.log('X RES ', rows0);
            //console.log('X RES length', rows0.length);

            if (rows0.length == 0) {
                db.query("SELECT id FROM mission WHERE id = ? AND driverInfo_id IS NULL", [missionId], function (err, rows) {

                    if (rows.length > 0) {
                        logEvent(0, 0, 0, missionId, 'setDriverTypeX', "count=" + count, "clientId=" + clientId);
                        //console.log('setDriverTypeX ');
                        requestify.post(javaURL + '/admin/setDriverTypeX', {'missionId': missionId, 'clientId': clientId})
                            .then(function (response) {
                            });
                    }
                });
            }
        });
    }

    return true;
}

function sendDataToGraylog(missionId, clientId, driverId, method, str1, str2, str3){

    requestify.post(GRAYLOG_SERVER, {'missionId': missionId, 'clientId': clientId, 'driverId': driverId, 'short_message': method, 'text1': str1, 'text2': str2, 'text3': str3})
        .then(function (response) {
        });

    return true;
}

//--------------------Check if object is empty--------------------

function isEmpty(obj) {
    if (obj == null) return true;

    if (obj.length > 0)    return false;
    if (obj.length === 0)  return true;

    return true;
}

function objCount(obj) {
    return Object.keys(obj).length;
}

Date.prototype.addHours = function (h) {
    var copiedDate = new Date(this.getTime());
    copiedDate.setHours(copiedDate.getHours() + h);
    return copiedDate;
}

Date.prototype.addMinutes = function (m) {
    var copiedDate = new Date(this.getTime());
    copiedDate.setMinutes(copiedDate.getMinutes() + m);
    return copiedDate;
}

Date.prototype.addSeconds = function (m) {
    var copiedDate = new Date(this.getTime());
    copiedDate.setSeconds(copiedDate.getSeconds() + m);
    return copiedDate;
}

function isTerminalPhone(phone) {
    var suffix = "_t";
    return phone.indexOf(suffix, phone.length - suffix.length) !== -1;
}

function normalizePhone(phone) {
    return phone.substring(0, 12);
    //phone;
}

//******************************************Р’РЎРЇРљРћР•*******************************************//
/*
 var nowDate = new Date();
 var date0 = nowDate.getFullYear() + '-' + ('0' + (nowDate.getMonth() + 1)).slice(-2) + '-' + ('0' + nowDate.getDate()).slice(-2);
 var time0 = ('0' + nowDate.getHours()).slice(-2) + ':' + ('0' + nowDate.getMinutes()).slice(-2) + ':' + ('0' + nowDate.getSeconds()).slice(-2);
 var formatedDate = date0 + ' ' + time0;
 */