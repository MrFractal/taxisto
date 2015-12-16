package ru.trendtech.services.push;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by ivanenok on 4/4/2014.
 */
@Component
public class PushConfig {
    @Value("${apple.password}")
    private String applePassword;

    @Value("${apple.cert.path}")
    private String appleCertPath;

    @Value("${apple.production}")
    private boolean appleProduction;

    @Value("${google.client.api.key}")
    private String googleKey;

    @Value("${google.password}")
    private String googlePassword;

    @Value("${google.production}")
    private boolean googleProduction;

    public String getApplePassword() {
        return applePassword;
    }

    public String getAppleCertPath() {
        return appleCertPath;
    }

    public boolean isAppleProduction() {
        return appleProduction;
    }

    public String getGoogleKey() {
        return googleKey;
    }

    public String getGooglePassword() {
        return googlePassword;
    }

    public boolean isGoogleProduction() {
        return googleProduction;
    }
}
