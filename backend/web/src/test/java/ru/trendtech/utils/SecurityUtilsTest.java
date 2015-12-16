package ru.trendtech.utils;

import org.testng.annotations.Test;

/**
 * Created by petr on 4/4/2014.
 */
public class SecurityUtilsTest {
    @Test
    public void testVerifyKeyStore() throws Exception {
//        SecurityUtils.verifyKeyStore("certs/CertificatesAPNs.p12", "123456", false);
//        SecurityUtils.verifyKeyStore("certs/CertificatesAPNsVoitov.p12", "123456", false);
        SecurityUtils.verifyKeyStore("certs/CertificatesAPNs.p12", "12345678", false);
    }
}
