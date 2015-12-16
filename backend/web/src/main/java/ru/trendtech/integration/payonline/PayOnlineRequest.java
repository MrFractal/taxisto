package ru.trendtech.integration.payonline;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

/**
 * File created by petr on 09/09/2015 11:15.
 */


public class PayOnlineRequest {
    private String url;
    private HashMap<String, String> params = new LinkedHashMap<>();

    public PayOnlineRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

//    public void addParameter(String name, String value) {
//        params.put(name, String.format("{%s}", value));
//    }

    public void addParameter(String name, String value) {
        params.put(name, value);
    }


    public String buildParams(){
        String result = "";
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!result.isEmpty()){
                //if(!first){
                    result = result + "&";
                //}
            }
            result = result + (entry.getKey() + "=" + entry.getValue());
            first = false;
        }
        return result;
    }

    public String toUrl(){
        //        return uriComponents.toString();
        //UriComponents uriComponents = UriComponentsBuilder.fromUriString(url + "&" + buildParams()).build();
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(url + buildParams()).build();
        return uriComponents.toUriString();
    }

    public List<NameValuePair> getEntityParams() {
        ArrayList<NameValuePair> result = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return result;
    }
}
