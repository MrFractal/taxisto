package ru.trendtech.services.push;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import ru.trendtech.integration.push.manager.AndroidPushManager;
//import ru.trendtech.integration.push.manager.ApplePushManager;
//import ru.trendtech.integration.push.manager.PushManager;

/**
 * Created by ivanenok on 4/4/2014.
 */

//
//@Component
//public class PushManagerFactory {
//    private static final Logger LOGGER = LoggerFactory.getLogger(PushManagerFactory.class);
//
//    private static ApplePushManager apmInstance = null;
//
//    private static AndroidPushManager andInstance = null;
//
//    @Autowired
//    private PushConfig pushConfig;
//
//    /**
//     * return the instance of the manager class according to device type if the instance is null already then it will create a new instance of the manager class
//     *
//     * @param type type of service for pushing
//     */
//
//    public synchronized PushManager getInstance(PushServiceKind type) {
//        PushManager result = null;
//        switch (type) {
//            case APPLE:
//                if (apmInstance == null) {
//                    apmInstance = new ApplePushManager();
//                    apmInstance.configureAccount(pushConfig.getAppleCertPath(), pushConfig.getApplePassword(), pushConfig.isAppleProduction());
//                    LOGGER.info("Apple notification manager initialized successfully");
//                }
//                result = apmInstance;
//                break;
//            case GOOGLE:
//                if (andInstance == null) {
//                    andInstance = new AndroidPushManager();
//                    andInstance.configureAccount(pushConfig.getGoogleKey(), pushConfig.getGooglePassword(), pushConfig.isGoogleProduction());
//                    LOGGER.info("Android notification manager initialized successfully");
//                }
//                result = andInstance;
//                break;
//            default:
//                throw new IllegalArgumentException("Unknown type of notification manager requested");
//        }
//        return result;
//    }
//}