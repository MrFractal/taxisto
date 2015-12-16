package ru.trendtech.utils;

import javapns.communication.KeystoreManager;
import javapns.notification.AppleNotificationServer;
import javapns.notification.AppleNotificationServerBasicImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Created by ivanenok on 4/4/2014.
 */
public class SecurityUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityUtils.class);

//    private static byte[] certificateBytes = null;

    public static void verifyKeyStore(String path, String password, boolean production) {
        try {
            LOGGER.info("Validating keystore reference: ");
            KeystoreManager.validateKeystoreParameter(getCertificateStream(path));
            LOGGER.info("VALID  (keystore was found)");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (password != null) {
            try {
                LOGGER.info("Verifying keystore content: ");
                AppleNotificationServer server = new AppleNotificationServerBasicImpl(getCertificateStream(path), password, production);
                KeystoreManager.verifyKeystoreContent(server, getCertificateStream(path));
                LOGGER.info("VERIFIED  (no common mistakes detected)");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static InputStream getCertificateStream(String fileName) {
        return SecurityUtils.class.getClassLoader().getResourceAsStream(fileName);
    }
//
//    public static byte[] getCertificateBytes() {
//        if (certificateBytes == null) {
//            InputStream certifacteStream = getCertificateStream();
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//
//            byte[] buffer = new byte[4096];
//
//            try {
//                int bytesRead = 0;
//                while ((bytesRead = certifacteStream.read(buffer, 0, buffer.length)) != -1) {
//                    outputStream.write(buffer, 0, bytesRead);
//                }
//            } catch (IOException e) {
//                LoggerFactory.getLogger(SecurityUtils.class.getCanonicalName())
//                        .error("Error reading the certificate", e);
//            }
//
//            certificateBytes = outputStream.toByteArray();
//        }
//        return certificateBytes;
//    }
}
