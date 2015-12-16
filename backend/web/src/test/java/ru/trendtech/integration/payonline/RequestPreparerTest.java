package ru.trendtech.integration.payonline;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.UUID;

public class RequestPreparerTest {
    public static final Logger LOGGER = LoggerFactory.getLogger(RequestPreparerTest.class);





    @Test
    public void rebill() throws Exception {
        PayOnlineRequest request = RequestPreparer.rebill("21", 2, "RUB", "16RCWnNUzcJtzmh2FlRccZGFn2g=");
        request = RequestPreparer.addContentType(request, true);
        request = RequestPreparer.addEmail(request);
        request = RequestPreparer.addIp(request, "192.168.1.1");
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(request.getUrl());
        httpPost.setEntity(new UrlEncodedFormEntity(request.getEntityParams(), HTTP.UTF_8));
        CloseableHttpResponse response = client.execute(httpPost);
        String string = EntityUtils.toString(response.getEntity());
        // XmlMapper
        response.getEntity().getContent();
    }





    @Test
    public void testPayment() throws Exception {
        PayOnlineRequest request = RequestPreparer.payment("15", 1, "RUB");
        //request = RequestPreparer.addCardInfoReal(request);
        //request = RequestPreparer.addContentType(request, true);
        //request = RequestPreparer.addEmail(request);
        //request = RequestPreparer.addIp(request, "192.168.1.1");
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(request.getUrl());
        httpPost.setEntity(new UrlEncodedFormEntity(request.getEntityParams(), HTTP.UTF_8));
    }





    @Test
    public void testThreeDS() throws Exception {
        PayOnlineRequest request = RequestPreparer.completeThreeDS("54107940", "", "");
        request = RequestPreparer.addContentType(request, true);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(request.getUrl());
        httpPost.setEntity(new UrlEncodedFormEntity(request.getEntityParams(), HTTP.UTF_8));
        CloseableHttpResponse response = client.execute(httpPost);
        String string = EntityUtils.toString(response.getEntity());
        response.getEntity().getContent();
    }






    @Test
    public void testAuth() throws Exception {
        PayOnlineRequest request = RequestPreparer.auth("-4", 1, "RUB");
        request = RequestPreparer.addCardInfoReal(request);
        request = RequestPreparer.addContentType(request, true);
        request = RequestPreparer.addEmail(request);
        request = RequestPreparer.addIp(request, "192.168.2.4");
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(request.getUrl());
        httpPost.setEntity(new UrlEncodedFormEntity(request.getEntityParams(), HTTP.UTF_8));
        CloseableHttpResponse response = client.execute(httpPost);
        String string = EntityUtils.toString(response.getEntity());
        // XmlMapper
        response.getEntity().getContent();
    }





    @Test
    public void search() throws Exception {
        PayOnlineRequest request = RequestPreparer.search("55196073", "26:731"); // UUID.randomUUID().toString()
        //request = RequestPreparer.addContentType(request, true);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(request.getUrl());
        httpPost.setEntity(new UrlEncodedFormEntity(request.getEntityParams(), HTTP.UTF_8));
        CloseableHttpResponse response = client.execute(httpPost);
        String string = EntityUtils.toString(response.getEntity());
        LOGGER.info("return string" + string);
    }






    @Test
    public void completeTransaction() throws Exception {
        PayOnlineRequest request = RequestPreparer.completeTransaction("55196054", 1);
        //request = RequestPreparer.addCardInfoTest(request);
        //request = RequestPreparer.addContentType(request, true);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(request.getUrl());
        httpPost.setEntity(new UrlEncodedFormEntity(request.getEntityParams(), HTTP.UTF_8));
        CloseableHttpResponse response = client.execute(httpPost);
        String string = EntityUtils.toString(response.getEntity());
        LOGGER.debug("return string: {}", string);
    }



    @Test
    public void refundTransaction() throws Exception {
        PayOnlineRequest request = RequestPreparer.refundTransaction("51161337", 6);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(request.getUrl());
        httpPost.setEntity(new UrlEncodedFormEntity(request.getEntityParams(), HTTP.UTF_8));
        CloseableHttpResponse response = client.execute(httpPost);
        String string = EntityUtils.toString(response.getEntity());
        LOGGER.debug("return string: {}", string);
    }



    @Test
    public void voidTransactions() throws Exception {
        PayOnlineRequest request = RequestPreparer.voidTransactions("51161337");
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(request.getUrl());
        httpPost.setEntity(new UrlEncodedFormEntity(request.getEntityParams(), HTTP.UTF_8));
        CloseableHttpResponse response = client.execute(httpPost);
        String string = EntityUtils.toString(response.getEntity());
        LOGGER.debug("return string: {}", string);
    }



    @Test
    public void testCheck() throws Exception {
        PayOnlineRequest request = RequestPreparer.check("1", 6, "RUB"); //  UUID.randomUUID().toString()
        request = RequestPreparer.addCardInfoTest(request);
        request = RequestPreparer.addContentType(request, true);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(request.getUrl());
        httpPost.setEntity(new UrlEncodedFormEntity(request.getEntityParams(), HTTP.UTF_8));
        CloseableHttpResponse response = client.execute(httpPost);
        String string = EntityUtils.toString(response.getEntity());
        LOGGER.debug("return string: {}", string);
    }

}