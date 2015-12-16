package ru.trendtech.common.mobileexchange.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 20.01.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomException extends RuntimeException{
    private int code;
    private String message;

    public CustomException(){
    }

    public CustomException(int code, String message){
        this.code = code;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
