package ru.trendtech.integration.payonline;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * File created by petr on 11/09/2015 14:09
 */

/*
    <error>
        <code>1000</code>
        <message>Internal</message>
     </error>
 */

@Root(name = "error")
public class ErrorResponse extends PayOnlineResponse{
    @Element(name = "code", required = true)
    private String code;

    @Element(name = "message", required = false)
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
