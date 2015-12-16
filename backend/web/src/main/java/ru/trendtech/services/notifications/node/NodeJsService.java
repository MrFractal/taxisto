package ru.trendtech.services.notifications.node;

import io.socket.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.driver.search.FindDriversService;
import ru.trendtech.services.driver.search.StartSearch;
import ru.trendtech.services.driver.search.StartSearchDrivers;
import ru.trendtech.services.email.ServiceEmailNotification;
import ru.trendtech.utils.StrUtils;
import ru.trendtech.utils.TokenUtil;

import java.net.MalformedURLException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by petr on 21.08.14.
 */

@Service
public class NodeJsService {//implements IOCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeJsService.class);
    private static SocketIO mSocket;
    @Value("${node.url}")
    private String nodeUrl = "";
    @Autowired
    private ServiceEmailNotification serviceEmailNotification;
    @Autowired
    private FindDriversService findDriversService;

    private NodeJsService(){}

    public boolean isConnected() {
        return mSocket.isConnected();
    }



    public boolean notified(String event, JSONObject jsonRes){
        //  ---------- send to socket [mission fired]-----------------
        boolean result = false;
        if(isConnected()){
            sendMessageSocket(event,jsonRes);
             result =true;
        }
        return result;
    }



    private void sendConnectMessage(){
        try{
            if(mSocket.isConnected()){
                mSocket.emit("connected_server", "");
            }else{
                //LOGGER.debug("Соединение НЕ активно, уходим на реконнект");
                reconnect();
            }
        }catch(java.lang.NullPointerException f){
            LOGGER.info("in NPE");
        }
    }



    public void connectToSocket(){
        if(mSocket == null){
            try{
                //LOGGER.debug("Создаем инстанс соккета URL: "+ nodeUrl);
                Properties prop = new Properties();
                prop.setProperty("typeOfClient", "0");
                prop.setProperty("security_token", TokenUtil.getMD5("fractal" + StrUtils.generateAlphaNumString(5)));
                //mSocket = new SocketIO(nodeUrl, ioCallBack);
                mSocket = new SocketIO(nodeUrl, prop);
                mSocket.connect(ioCallBack);
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sendEchoMessage();
                sendConnectMessage();
            }catch(MalformedURLException e){
                e.printStackTrace();
            }
        }else{
            //LOGGER.debug("Инстанс создан, отправляю тестовое сообщение");
            sendEchoMessage();
        }
    }


    public void sendEchoMessage(){
        try{
            if(mSocket.isConnected()){
                LOGGER.debug("Соединение активно!");
                JSONObject json = new JSONObject();
                json.put("echo","test message");
                mSocket.emit("check_connection", json);
            }else{
                LOGGER.debug("Соединение НЕ активно! Уходим на реконнект");
                reconnect();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LOGGER.info("in JSONException");
        } catch(java.lang.NullPointerException f){
            LOGGER.info("in NPE");
        }
    }



    public void sendMessageSocket(final String event, final JSONObject json)  {
        Thread sendMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                mSocket.emit(event, json); // "booked_mission_fired"
                //LOGGER.debug(" ---SocketIO SEND MESSAGE FOR EVENT---: "+event);
            }
        });
        sendMessage.start();
    }



    public static void destroy(){
        LOGGER.debug(" -------------------------- Socket disconect by context destroyed ----------------------------------");
        if(mSocket!=null){
            mSocket.disconnect();
        }
    }



    public  void reconnect(){
        Thread reconnectThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!mSocket.isConnected()) {
                    try {
                        try {
                            mSocket = new SocketIO(nodeUrl,ioCallBack); // 5000 -stg, 5001 -dev "http://109.120.152.140:5000/"
                            LOGGER.info("RECONNECT NODE JS ---------------------------->");
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }catch(RuntimeException r){
                        LOGGER.info("runtime exception socketio: "+r.getMessage());
                    }
                }
            }
        });
        reconnectThread.start();
    }



    IOCallback ioCallBack = new IOCallback() {
        @Override
        public void onDisconnect() {
            reconnect();
        }


        @Override
        public void onConnect() {
            sendEchoMessage();
            sendConnectMessage();
        }


        @Override
        public void onMessage(String s, IOAcknowledge ioAcknowledge) {
        }


        @Override
        public void onMessage(JSONObject jsonObject, IOAcknowledge ioAcknowledge){
            try {
                String nameEvent = jsonObject.getString("event");
                //LOGGER.info("~~~~~~~~~~~~~~~~~ Пришло событие с нода ~~~~~~~~~~~~~~~~~: "+nameEvent);
                if(!StringUtils.isEmpty(nameEvent)){
                    if(nameEvent.equals("client_disconnected")){
                        String clientId = jsonObject.getString("clientId");
                        if(!StringUtils.isEmpty(clientId)){
                            //administrationService.cancelMissionByNode(Long.parseLong(clientId));
                        }
                    }
                    else if(nameEvent.equals("checked")){
                        //LOGGER.info("nameEvent = "+nameEvent);
                    }
                    else if(nameEvent.equals("ask_client_for_autosearch_node_response")){
                        String missionIdStr = jsonObject.getString("missionId");
                        LOGGER.info(String.format("Запрос на переход в режим AUTO_SEARCH для миссии с id=%s успешно отправлен", missionIdStr));
                        if(!StringUtils.isEmpty(missionIdStr)){
                            long missionId = Long.parseLong(missionIdStr);
                            if(findDriversService.getQueueMission().containsKey(missionId)){
                                LOGGER.info("**************************************** -1");
                                ScheduledFuture scheduledFuture = ((ScheduledFuture) (findDriversService.getQueueMission().get(missionId)));
                                LOGGER.info("**************************************** -2");
                                StartSearch startSearch = (StartSearch)(scheduledFuture).get();
                                LOGGER.info("````````````````````````````````BEFORE startSearchDrivers " + startSearch.isAskedClient);
                                StartSearch.isAskedClient = true;
                                LOGGER.info("````````````````````````````````AFTER startSearchDrivers " + startSearch.isAskedClient);
                            }else{
                                LOGGER.info(String.format("Миссия id=%s отменена или отсутсвует в пуле потоков", missionId));
                            }
                        }
                    }
                }
                //LOGGER.debug("Ответ с сервера Node.js: "+jsonObject);
            } catch (JSONException e) {
                LOGGER.info("Ошибка сервера json="+e.getMessage());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void on(String s, IOAcknowledge ioAcknowledge, Object... objects) {
            LOGGER.debug("SocketIO said string: "+s);
        }

        @Override
        public void onError(SocketIOException e) {
            LOGGER.debug("Возникла ошибка. : "+e.getMessage()+" to str: "+e.toString()+" desc: "+e.fillInStackTrace());
        }
    };
}


