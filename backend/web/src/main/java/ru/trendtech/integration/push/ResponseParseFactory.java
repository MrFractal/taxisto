package ru.trendtech.integration.push;

/**
 * Created by ivanenok on 4/4/2014.
 */

import org.json.simple.JSONValue;

import java.util.LinkedHashMap;

class ResponseParseFactory {

    /**
     * Class Constructor creates Logger class objects.
     */
    public ResponseParseFactory() {
    }

    /**
     * This method will used to return success JSON string.
     *
     * @param msg It contains the string message to send in response
     * @return String It contains the JSON string for success message
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public String getSuccessJsonString(String msg) {
        String jsonString = "";
        LinkedHashMap list = new LinkedHashMap();
        list.put("response", msg);
        jsonString = JSONValue.toJSONString(list);
        return jsonString;
    }

    /**
     * This method will used to return Server Error JSON string in response with it's code.
     *
     * @return String It contains Server Error message in JSON String
     */
    @SuppressWarnings({"rawtypes"})
    public String ServerErrorFound() {
        String jsonString = "";
        LinkedHashMap list = new LinkedHashMap();
        jsonString = JSONValue.toJSONString(list);
        return jsonString;
    }
}