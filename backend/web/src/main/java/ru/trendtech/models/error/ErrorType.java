package ru.trendtech.models.error;

import org.springframework.util.StringUtils;
import ru.trendtech.common.mobileexchange.model.common.CustomException;

import java.util.ResourceBundle;

/**
 * Created by petr on 10.07.2015.
 */
public enum ErrorType {
    CLIENT_NOT_FOUND(1),
    DRIVER_NOT_FOUND(2),
    WEB_USER_NOT_FOUND(3),
    FAILED_SECURITY(4);

    private final int code;


    ErrorType(int code) {
        this.code = code;
    }

    public static CustomException getCustomExceptionByKey(String keyException, String language){
        String bundle = "lang";
        if(!StringUtils.isEmpty(language)){
             bundle = bundle + "_" + language;
        }
            ResourceBundle resourceBundle = ResourceBundle.getBundle(bundle);
            String messageException = resourceBundle.getString(keyException);
            ErrorType errorType = ErrorType.valueOf(keyException);
        return new CustomException(errorType.getCode(), messageException);
    }



    public int getCode() {
        return code;
    }


    @Override
    public String toString() {
        return code + ": ";
    }

}
