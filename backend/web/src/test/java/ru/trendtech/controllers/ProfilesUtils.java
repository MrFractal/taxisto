package ru.trendtech.controllers;

import net.iharder.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import ru.trendtech.utils.HTTPUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Created by ivanenok on 3/20/14.
 */
class ProfilesUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfilesUtils.class);

    static {
        loadProfiles();
    }

    private static String getServerUrlCommon() {
        return getServerUrl() + "/common";
    }

    private static String getServerUrlAdmin() {
        return getServerUrl() + "/admin";
    }

    public static String getServerUrlAdminCourier(){
         return getServerUrl() + "/admin/courier";
    }

    private static String getServerUrlClient() {
        return getServerUrl() + "/client";
    }

    private static String getServerUrlClientCourier() {
        return getServerUrl() + "/client/courier";
    }

    private static String getServerUrlIntegrationCourier() {
        return getServerUrl() + "/courier/integration";
    }


    private static String getServerUrlCorporateClient() {
        return getServerUrl() + "/corporate";
    }

    private static String getServerUrlPayment() {
        return getServerUrl() + "/payment";
    }

    private static String getServerUrlDriver() {
        return getServerUrl() + "/driver";
    }

    private static String getServerUrlCourier() {
        return getServerUrl() + "/courier";
    }

    private static String getServerUrlPartner() {
        return getServerUrl() + "/partner";
    }

    public static String driverUrl(String url) {
        return getServerUrlDriver() + url;
    }

    public static String courierUrl(String url) {
        return getServerUrlCourier() + url;
    }


    public static String asteriskUrl() {
        return getServerUrl() + "/asterisk";
    }

    public static String commonUrl(String url) {
        return getServerUrlCommon() + url;
    }

    public static String clientUrl(String url) {
        return getServerUrlClient() + url;
    }

    public static String clientCourierUrl(String url) {
        return getServerUrlClientCourier() + url;
    }

    public static String integrationCourierUrl(String url) {
        return getServerUrlIntegrationCourier() + url;
    }


    public static String corporateClientUrl(String url) {
        return getServerUrlCorporateClient() + url;
    }

    public static String paymentUrl(String url) {
        return getServerUrlPayment() + url;
    }

    public static String partnerUrl(String url) {return getServerUrlPartner() + url; }

    public static String adminUrl(String url) {
        return getServerUrlAdmin() + url;
    }

    public static String adminCourierUrl(String url) {
        return getServerUrlAdminCourier() + url;
    }


    private static String getServerUrl() {
        // http://dev.taxisto.ru | http://stg.taxisto.ru | http://localhost:8080
        String url = "http://localhost:8080";//http://localhost:8080";// System.getProperties().getProperty("http://dev.taxisto.ru");//("test.server.url");
//        if (!StringUtils.isEmpty(url)) {
//            url = URI.create(url).normalize().toString();
//        }
//        if (url.endsWith("/")) {
//            url = url.substring(0, url.length() - 1);
//        }
        return url;
    }


    public static void setProfileForce(String profileName) {
        System.getProperties().setProperty("spring.profiles.active", profileName);
        loadProfiles();
    }

    public static void loadProfiles() {
        String property = System.getProperties().getProperty("spring.profiles.active");
        loadProfiles(property);
    }

    private static void loadProfiles(String... profileNames) {
        URI uri;
        try {
            uri = ClassLoader.getSystemResource("profiles/default.properties").toURI();
            Properties defaultProps = new Properties();
            defaultProps.load(new FileReader(new File(uri)));
            LOGGER.debug("Default properties: \n" + defaultProps);

            Properties customProps = new Properties();
            for (String profileName : profileNames) {
                if (profileName != null) {
                    customProps.clear();
                    uri = ClassLoader.getSystemResource("profiles/" + profileName + ".properties").toURI();
                    customProps.load(new FileReader(new File(uri)));
                    LOGGER.debug("Additional properties: \n" + customProps);
                    defaultProps.putAll(customProps);
                }
            }
            LOGGER.debug("All properties: \n" + defaultProps);
            System.getProperties().putAll(defaultProps);
        } catch (URISyntaxException e) {
            LOGGER.error("problem on profiles loading", e);
        } catch (IOException e) {
            LOGGER.error("problem on profiles loading.", e);
        }
    }



    public static String getPicture(String fileName) {
        String fromFile = null;
        String fullFileName = "images/" + fileName;
        try {
            URI uri = ClassLoader.getSystemResource(fullFileName).toURI();
            File file = new File(uri);
            fromFile = Base64.encodeFromFile(file.getAbsolutePath());
        } catch (URISyntaxException e) {
            LOGGER.error("problem on image loading", e);
        } catch (IOException e) {
            LOGGER.error("problem on image loading.", e);
        }
        return fromFile;
    }
}
