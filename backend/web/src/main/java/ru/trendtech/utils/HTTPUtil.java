package ru.trendtech.utils;


import org.apache.commons.httpclient.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by petr on 11.11.2014.
 */

public class HTTPUtil {

    public static String resolveIpAddress(HttpServletRequest request){
        String ip;
        if(!StringUtils.isEmpty(request.getHeader("HTTP_CLIENT_IP"))){
            ip = request.getHeader("HTTP_CLIENT_IP");
        } else if(!StringUtils.isEmpty(request.getHeader("x-forwarded-for"))){
            ip = request.getHeader("x-forwarded-for");
        } else{
            ip = request.getRemoteAddr();
        }

        if(!StringUtils.isEmpty(ip)){
            String str[] = ip.split(",");
            ip = str[0].trim();
                if(str.length > 1){
                    ip = str[1].trim();
                }
        }
            return ip;
    }


/*
    function sendSMS(phone, message) {
        var request = require('request');
        var headers = {'Content-Type': 'application/x-www-form-urlencoded'};
        var options = {
                url: 'http://smsc.ru/sys/send.php',
                method: 'GET',
                headers: headers,
                qs: {'login': 'ttehnolodgis',
                'psw': 'KrexQ8b',
                'phones': normalizePhone(phone),
                'mes': message,
                'charset': 'utf-8'}
        };
        request(options, function (error, response, body) {
            if (!error && response.statusCode == 200) {
                console.log(body);
            }
        });
*/



    public static void senRestQuery(final String strUrl, final JSONObject jsonBody){
        HttpURLConnection conn = null;
        final SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {

                URL url = new URL(strUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                String input = jsonBody.toString();

                System.out.println("input: "+input);

                OutputStream os = conn.getOutputStream();
                os.write(input.getBytes());
                os.flush();


                if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    //throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            }

            protected void done() {
                try {
                    get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
        if(conn != null){
            conn.disconnect();
        }
    }



    /*
    try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String input = jsonBody.toString();

            System.out.println("input: "+input);

            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                //throw new RuntimeException("Failed : HTTP error code : "
                //        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));


            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
     */




    public static String sendHttpQuery(final String url, final List<NameValuePair> urlParameters) {
        String result = "";
            try {
                HttpClient httpClient = HttpClientBuilder.create().build();

                HttpPost postRequest = new HttpPost(url);


                if (!CollectionUtils.isEmpty(urlParameters)) {
                    postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
                }
                //httpClient.getParams().setParameter("http.protocol.content-charset", "win-1251");

                HttpResponse response = httpClient.execute(postRequest);
                BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent()),  StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                result = sb.toString();
            }catch(IOException io){
                io.getMessage();
            }
            return result;
    }


    /*
    public static String sendHttpQuery(final String url, final List<NameValuePair> urlParameters){
        final SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {

                HttpClient httpClient = HttpClientBuilder.create().build();

                HttpPost postRequest = new HttpPost(url);
                if (!CollectionUtils.isEmpty(urlParameters)) {
                    postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
                }
                //httpClient.getParams().setParameter("http.protocol.content-charset", "win-1251"); // dsmirnov8080@gmail.com

                HttpResponse response = httpClient.execute(postRequest);
                BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            }

            protected void done() {
                try {
                    // Retrieve the return value of doInBackground
                    serverAnswer = get();
                    System.out.println("get() = "+serverAnswer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
            return serverAnswer;
    }
    */


    public static String senPostQuery(String url, List<NameValuePair> urlParameters) {
        try {
            //HttpClient httpClient = HttpClientBuilder.create().build();
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(url);
            if (!CollectionUtils.isEmpty(urlParameters)) {
                postRequest.setEntity(new UrlEncodedFormEntity(urlParameters));
            }
            httpClient.getParams().setParameter("http.protocol.content-charset", "win-1251"); // dsmirnov8080@gmail.com
            HttpResponse response = httpClient.execute(postRequest);
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }catch(IOException io){
            return io.getMessage();
        }
    }





    public static void main(String[] args) {
        AsyncRestTemplate asycTemp = new AsyncRestTemplate();
        String url ="https://engine.paymentgate.ru/payment/rest/getOrderStatusExtended.do";
        HttpMethod method = HttpMethod.POST;
        Class<String> responseType = String.class;
        //create request entity using HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map urlParam = new HashMap();
        headers.add("userName", "taxisto_auto-api");
        headers.add("password", "test");
        headers.add("orderId", "5dce3c10-2f12-48ac-9498-6c54782829f2");

        HttpEntity<String> requestEntity = new HttpEntity<String>("params", headers);
        ListenableFuture<ResponseEntity<String>> future = asycTemp.exchange(url, method, requestEntity, responseType, url);

        try {
            //waits for the result
            ResponseEntity<String> entity = future.get();
            //prints body source code for the given URL
            System.out.println(entity.getBody());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }



    /*
    public static void main sendHttpQuery(String orderId){
        AsyncRestTemplate asycTemp = new AsyncRestTemplate();
        String url ="https://engine.paymentgate.ru/payment/rest/getOrderStatusExtended.do";
        HttpMethod method = HttpMethod.POST;
        Class<String> responseType = String.class;
        //create request entity using HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        headers.add("userName", "taxisto_auto-api");
        headers.add("password", "<KhjfhjHHvfT,.88}!j&Buijklisw4");
        headers.add("orderId", orderId);

        HttpEntity<String> requestEntity = new HttpEntity<String>("params", headers);
        ListenableFuture<ResponseEntity<String>> future = asycTemp.exchange(url, method, requestEntity, responseType);
        try {
            //waits for the result
            ResponseEntity<String> entity = future.get();
            //prints body source code for the given URL
            System.out.println(entity.getBody());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    */



}
