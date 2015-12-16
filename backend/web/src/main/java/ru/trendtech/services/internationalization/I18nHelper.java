package ru.trendtech.services.internationalization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ResourceBundle;

/**
 * Created by petr on 17.09.2015.
 */
@Service
public class I18nHelper {
    @Value("${default_lang}")
    private String defaultLang;
    private ResourceBundle resourceBundle;
    private static final String bundleSuffix = "lang_";


    public String getMessage(String key){
        resourceBundle = ResourceBundle.getBundle(bundleSuffix + defaultLang);
        return resourceBundle.getString(key);
    }
}
