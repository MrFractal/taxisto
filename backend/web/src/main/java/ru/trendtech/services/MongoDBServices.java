package ru.trendtech.services;

//import com.mongodb.*;
//import ru.trendtech.repositories.mongo.LocationRepositoryMongoDB;

//import org.springframework.data.mongodb.core.MongoOperations;
//import ru.trendtech.domain.mongo.Location;


import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.trendtech.common.mobileexchange.model.common.CustomException;
import ru.trendtech.common.mobileexchange.model.common.DriverActivityInfo;
import ru.trendtech.common.mobileexchange.model.common.DriverInfo;
import ru.trendtech.common.mobileexchange.model.common.logging.LoggingEventInfoMongo;
import ru.trendtech.common.mobileexchange.model.web.ActivityDriverResponse;
import ru.trendtech.common.mobileexchange.model.web.LocationMongoResponse;
import ru.trendtech.common.mobileexchange.model.web.LoggingEventMongoResponse;
import ru.trendtech.domain.Driver;
import ru.trendtech.domain.admin.WebUser;
import ru.trendtech.domain.mongo.*;
import ru.trendtech.models.ModelsUtils;
import ru.trendtech.repositories.DriverRepository;
import ru.trendtech.repositories.WebUserRepository;
import ru.trendtech.repositories.mongo.EventsRepository;
import ru.trendtech.repositories.mongo.LocationMongoRepository;
import ru.trendtech.repositories.mongo.LoggingEventMongoRepository;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.trendtech.services.logging.Log;
import ru.trendtech.utils.DateTimeUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by petr on 11.08.14.
 */

@Component
public class MongoDBServices {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDBServices.class);
    @Autowired
    private EventsRepository eventsRepository;
    @Autowired
    private WebUserRepository webUserRepository;
    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private MongoOperations mongoOperations;
    @Autowired
    private LoggingEventMongoRepository loggingEventMongoRepository;
    //@Autowired
    //LocationMongoRepository locationMongoRepository;

    public LoggingEventMongoResponse findLoggingEventMongoList(){
        LoggingEventMongoResponse response =new LoggingEventMongoResponse();
        Iterable<LoggingEventMongo> logginEventCollection = loggingEventMongoRepository.findAll();
            for (LoggingEventMongo loggingEventMongo : logginEventCollection) {
                LoggingEventInfoMongo loggingEventInfoMongo = new LoggingEventInfoMongo();
                   if(loggingEventMongo.getClientId()!=null){
                       WebUser webUser = webUserRepository.findOne(loggingEventMongo.getClientId());
                       loggingEventInfoMongo.setWebUserModel(ModelsUtils.toModel(webUser, null));
                   }
                   if(loggingEventMongo.getDriverId()!=null){
                       DriverInfo driverInfo = ModelsUtils.toModel(driverRepository.findOne(loggingEventMongo.getDriverId()));
                       loggingEventInfoMongo.setDriverInfo(driverInfo);
                   }
                       response.getLoggingEventInfoMongoList().add(ModelsUtils.toModel(loggingEventInfoMongo, loggingEventMongo));
                //LoggingEventMongo loggingEventMongoDelete = mongoOperations.findOne( Query.query(Criteria.where("_id").is("544f72d728e39f00e8c39543")),LoggingEventMongo.class, "logging_event");
                //mongoOperations.findAndRemove(Query.query(Criteria.where("_id").is("544f72d728e39f00e8c39543")),LoggingEventMongo.class, "logging_event");
                //loggingEventMongoRepository.delete(loggingEventMongoRepository.findOne(loggingEventMongoDelete.getId()));
                //LOGGER.info("loggingEventInfoMongo = "+loggingEventInfoMongo.getJsonQuery()+" sss = "+loggingEventInfoMongo.getId());
            }
          return response;
    }



/*
        date_time: { type: Number },
        object_type: { type: Number},//0 - system, 1 - driver, 2 - client, 3 - web_user
        object_id: { type: String},//driver_id, client_id, web_user_id
        event_id: { type: Number},//0 - read, 1 - insert, 2 - delete, 3 - change
        mission_id: { type: Number},

 */
    //objType, objId, eventId, missionId, field1, field2, field3
    public void createEvent(int object_type, String object_id, int event_id, long missionId, String event_field_1, String event_field_2, String event_field_3){ // , long clientId, long driverId
        Events events = new Events();
        events.setDate_time(new DateTime().getMillis()/1000);
        events.setObject_type(object_type); // 1- driver, 2 - client, 3 - webUser
        events.setObject_id(object_id);
        events.setEvent_id(event_id);
        events.setMission_id(missionId);
        events.setEvent_field_1(event_field_1); // название метода, события
        events.setEvent_field_2(event_field_2); // id того, кто инициировал
        events.setEvent_field_3(event_field_3); // error message
        //events.setClientId(clientId);
        //events.setDriverId(driverId);

        insertEvents(events);
    }



    private static class TimeActivityDetails{
        private int timeWork;
        private int timeBusy;

        public int getTimeWork() {
            return timeWork;
        }
        public void setTimeWork(int timeWork) {
            this.timeWork = getTimeWork()+timeWork;
        }
        public int getTimeBusy() {
            return timeBusy;
        }
        public void setTimeBusy(int timeBusy) {
            this.timeBusy = getTimeBusy()+timeBusy;
        }
    }




    @Transactional(propagation = Propagation.REQUIRED)
     public ActivityDriverResponse activityDriver(String security_token, long driverId, long startTime, long endTime){ // , int numberPage, int sizePage
         WebUser webUser = webUserRepository.findByToken(security_token);
         if(webUser == null){
            throw new CustomException(1, "Web user not found");
         }
         ActivityDriverResponse response = new ActivityDriverResponse();
         DBCollection dbCollection = mongoOperations.getCollection("driver_activity");

//         for(DBObject dbo: dbCollection.getIndexInfo()){
//             Iterator iterator = dbo.keySet().iterator();
//               while(iterator.hasNext()){
//                   LOGGER.info("dbo----------------------: "+iterator.next());
//               }
//         }
         BasicDBObject gtQuery = new BasicDBObject();

         if(driverId!=0){
             gtQuery.put("driverId", driverId);
         }
         if(startTime!=0 && endTime!=0){
             gtQuery.put("dateTime", new BasicDBObject("$gt", startTime).append("$lt", endTime));
         }else if(startTime!=0 && endTime==0){
             gtQuery.put("dateTime", new BasicDBObject("$gt", startTime));
         }else if(startTime==0 && endTime!=0){
             gtQuery.put("dateTime", new BasicDBObject("$lt", endTime));
         } else if(startTime==0 && endTime==0){
             // set time to default, because it's to long wait time
             startTime = new DateTime().withTimeAtStartOfDay().minusDays(2).getMillis()/1000;
             endTime = new DateTime().getMillis()/1000;
             gtQuery.put("dateTime", new BasicDBObject("$gt", startTime).append("$lt", endTime));
         }

         // общее кол-во секунд между timeStart и timeEnd
         long diffSeconds = endTime-startTime;

         LOGGER.info("****************************** diffSeconds = "+diffSeconds+" start="+startTime+" end="+endTime);

         //long totalItems = dbCollection.count(gtQuery);
         //response.setTotalItems(totalItems);

         //long count = totalItems / sizePage;
         //int lastPageNumber = (int) (count==0?count:count+1);
         //response.setLastPageNumber(lastPageNumber);

         DBCursor cursor = dbCollection.find(gtQuery); // 1 - for ascending, 2 - for descending [skip((numberPage - 1) * sizePage).limit(sizePage)]
         HashMap<Long,TimeActivityDetails> hashMap = new HashMap();

         while(cursor.hasNext()){
             DBObject dbObject = cursor.next();
             if(dbObject!=null){
                 DriverActivity driverActivity = mongoOperations.getConverter().read(DriverActivity.class, dbObject);  //- See more at: http://revelfire.com/spring-data-mongodb-convert-from-raw-query-dbobject/#sthash.gVci7WsA.dpuf
                 TimeActivityDetails details = hashMap.get(driverActivity.getDriverId());
                   if(details == null){
                       details = new TimeActivityDetails();
                   }
                 if(driverActivity.getTypeActivity()==1){
                     details.setTimeWork(driverActivity.getTimeAmount());
                 }else{
                     details.setTimeBusy(driverActivity.getTimeAmount());
                 }
                 hashMap.put(driverActivity.getDriverId(), details);
             }
         }

         for(Map.Entry<Long,TimeActivityDetails> entry: hashMap.entrySet()){
              long drvId = entry.getKey();
              TimeActivityDetails time = entry.getValue();
              long secTimeWork = time.getTimeWork();
              long secTimeBusy = time.getTimeBusy();
              long secTimeOffline =  diffSeconds-(secTimeWork+secTimeBusy);

              DriverActivityInfo info = new DriverActivityInfo();
              Driver driver = driverRepository.findOne(drvId);
              info.setDriverId(drvId);
              info.setFirstName(driver.getFirstName());
              info.setLastName(driver.getLastName());
              info.setTimeOnline(DateTimeUtils.splitToComponentTimes(new BigDecimal(secTimeWork)));
              info.setTimeOnlineBusy(DateTimeUtils.splitToComponentTimes(new BigDecimal(secTimeBusy)));
              info.setTimeOffline(DateTimeUtils.splitToComponentTimes(new BigDecimal(secTimeOffline)));
              response.getDriverActivityInfos().add(info);
              LOGGER.info("drvId: " + drvId + " timeWork: " + secTimeWork + " busy: " + secTimeBusy + " timeOffline=" + secTimeOffline);
         }
          return response;
     }




    public boolean checkTime(long driverId, int typeActivity){
        boolean result = true; // есть записи за последние 10 секунд
        DBCollection dbCollection = mongoOperations.getCollection("driver_activity");
        BasicDBObject gtQuery = new BasicDBObject();

        gtQuery.put("driverId", driverId);
        gtQuery.put("typeActivity", typeActivity);
        long startTime = new DateTime().minusSeconds(10).getMillis()/1000;
        long  endTime = new DateTime().getMillis()/1000;
        gtQuery.put("dateTime", new BasicDBObject("$gt", startTime).append("$lt", endTime));
        DBCursor cursor = dbCollection.find(gtQuery);
        if(cursor.size()==0){
            result = false;
        }
        return result;
    }




     public LocationMongoResponse findLocationListMongo(long missionId, long driverId, long startWhenSeen, long endWhenSeen, String type, int numberPage, int sizePage, int latStart, int latEnd, int lonStart, int lonEnd){
         LocationMongoResponse response = new LocationMongoResponse();
         try {
             DBCollection dbCollection = mongoOperations.getCollection("locations");
             BasicDBObject gtQuery = new BasicDBObject();

             if(startWhenSeen!=0 || endWhenSeen!=0){
                 gtQuery.put("when_seen", new BasicDBObject("$gt", startWhenSeen).append("$lt", endWhenSeen));
             }
             if(latStart!=0 || latEnd!=0){
                 gtQuery.put("latitude", new BasicDBObject("$gt", latStart).append("$lt", latEnd));
             }
             if(lonStart!=0 || lonEnd!=0){
                 gtQuery.put("longitude", new BasicDBObject("$gt", lonStart).append("$lt", lonEnd));
             }
             if(driverId!=0){
                 gtQuery.put("driverId", driverId);
             }
             if(missionId!=0){
                 gtQuery.put("missionId", missionId);
             }
             if(type!=null && !type.isEmpty()){
                 gtQuery.put("type", type);
             }

             long totalItems = dbCollection.count(gtQuery);
             response.setTotalItems(totalItems);

             DBCursor cursor = dbCollection.find(gtQuery).sort(new BasicDBObject("when_seen", -1)).skip((numberPage - 1) * sizePage).limit(sizePage);

             while(cursor.hasNext()){
                 DBObject dbObject = cursor.next();
                    if(dbObject!=null){
                        Location loc = mongoOperations.getConverter().read(Location.class, dbObject);  //- See more at: http://revelfire.com/spring-data-mongodb-convert-from-raw-query-dbobject/#sthash.gVci7WsA.dpuf
                        response.getListLocation().add(ModelsUtils.toModel(loc));
                    }
             }

             Query queryLocation = new Query();
              if(missionId!=0){
                  queryLocation.addCriteria(Criteria.where("missionId").is(missionId));
              }
              if(driverId!=0){
                  queryLocation.addCriteria(Criteria.where("driverId").is(driverId));
              }
              if(latStart!=0 || latEnd!=0){
                  LOGGER.info("latitude!!!");
                  queryLocation.addCriteria(
                          Criteria.where("latitude").exists(true).andOperator(
                                  Criteria.where("latitude").gt(latStart),
                                  Criteria.where("latitude").lt(latEnd)
                          )
                  );
              }
             if(lonStart!=0 || lonEnd!=0){
                 queryLocation.addCriteria(
                         Criteria.where("longitude").exists(true).andOperator(
                                 Criteria.where("longitude").gt(lonStart),
                                 Criteria.where("longitude").lt(lonEnd)
                         )
                 );
             }
              if(startWhenSeen!=0 || endWhenSeen!=0){
                  queryLocation.addCriteria(
                          Criteria.where("when_seen").exists(true).andOperator(
                                  Criteria.where("when_seen").gt(startWhenSeen),
                                  Criteria.where("when_seen").lt(endWhenSeen)
                          )
                  );
              }
              if(type!=null && !type.isEmpty()){
                  queryLocation.addCriteria(Criteria.where("type").is(type));
              }
             queryLocation.with(new Sort(Sort.Direction.DESC, "when_seen"));
             queryLocation.limit(sizePage);
             queryLocation.skip((numberPage - 1) * sizePage);

         }catch(Exception t){
            t.printStackTrace();
         }
            return  response;
     }







    public void insertLogging(LoggingEventMongo loggingEventMongo){
        /*
        ПОЛУЧЕНИЕ mongoOperation ИЗ КОНФИГУРАЦИИ
        MongoOperations mongoOperation = (MongoOperations)ctx.getBean("mongoTemplate");
                        LOGGER.info("mongoDBService ="+mongoDBServices);
                        LOGGER.info("mongoOperation = "+mongoOperation);
         */
        //loggingEventMongo.setId(getNextSequenceId("logging_event_id"));
        loggingEventMongoRepository.save(loggingEventMongo);
        LOGGER.info("loggingEventMongo id = " + loggingEventMongo.getId());
    }


    // логирование событий
    public void insertEvents(final Events events){
        Thread insertEvents = new Thread(new Runnable() {
            @Override
            public void run() {
                eventsRepository.save(events);
            }
        });
        insertEvents.start();
    }



    public long getNextSequenceId(String key) throws SequenceException {
        //get sequence id
        Query query = new Query(Criteria.where("_id").is(key));
        LOGGER.info("query = " + query);
        //increase sequence id by 1
        Update update = new Update();
        update.inc("seq", 1);

        //return new increased id
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);
        //this is the magic happened.
        SequenceId seqId = mongoOperations.findAndModify(query, update, options, SequenceId.class);

        //if no id, throws SequenceException
        //optional, just a way to tell user when the sequence id is failed to generate.
        if (seqId == null) {
            throw new SequenceException("Unable to get sequence id for key : " + key);
        }
        LOGGER.info("seqId = " + seqId);
        return seqId.getSeq();
    }




        //public static void main(String[] args) throws Exception {
        //connectToMongoDB1();
        //connectToMongoDB2();
        //}
        //static void connectToMongoDB1() throws Exception {
        // For XML
        //ApplicationContext ctx = new GenericXmlApplicationContext("mongo-config.xml");
        // For Annotation
        //ApplicationContext ctx = new AnnotationConfigApplicationContext(SpringMongoConfig.class);
        //MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
        //MongoOperations mongoOps = new MongoTemplate(new Mongo(), "database");
        //mongoOps.insert(new Person("Joe", 34));
        //log.info(mongoOps.findOne(new Query(where("name").is("Joe")), Person.class));
        //DBCollection collectionLocation= mongoOperation.getCollection("location");
        //LOGGER.info("collectionLocation = "+collectionLocation);
        //Iterable it = locationRepositoryMongoDB.findByDriverId("1");
        //LOGGER.info("it = "+it);
//        LOGGER.info("collectionLocation.count() = "+collectionLocation.find());
//
//        DBCursor dbCursor = collectionLocation.find();
//
//                    while(dbCursor.hasNext()){
//                        LOGGER.info("val = "+dbCursor.next());
//                    }
//              List<Location> res = mongoOperation.findAll(Location.class);
//               LOGGER.info("res = "+res);

//}

//  static void connectToMongoDB2() throws Exception {
//        try {
//
//            MongoClient mongoClient = new MongoClient("dev.taxisto.ru",27017);
//
//            DB db = mongoClient.getDB("taxisto_stg");
//
//            DBCollection locationTable = db.getCollection("locations");
//            //long count = locationTable.count();
//
//            DBCursor dbCursor = locationTable.find();
//                while(dbCursor.hasNext()){
//                    BasicDBObject document =  (BasicDBObject)dbCursor.next();
//                    LOGGER.info("RESULT: "+document);
//                }

           /*
            List<String> dbs = mongoClient.getDatabaseNames();
            for(String dbName : dbs){
                LOGGER.info("dbName = "+dbName);
            }
            */

            /*
            Set<String> tables = db.getCollectionNames();
            for(String coll : tables){
                LOGGER.info("collection names = "+coll);
            }
            */

            /*
            // update
            DBCollection tableUpd = db.getCollection("drivers");
            BasicDBObject query = new BasicDBObject();
            query.put("name", "petro");
            BasicDBObject newDocument = new BasicDBObject();
            newDocument.put("name", "petro upd");
            BasicDBObject updateObj = new BasicDBObject();
            updateObj.put("$set", newDocument);
            tableUpd.update(query, updateObj);
            */

//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
  //  }
}
