package ru.trendtech.services.http;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * User: max
 * Date: 2/4/13
 * Time: 4:48 PM
 */

public abstract class AbstractHttpService {
    private static final int CONNECTION_TIMEOUT = 10000;

    private static final int MAX_THREADS_FOR_SYNC_REQUEST = 200;

    private static final HttpClientBuilder HTTP_CLIENT_BUILDER;

    static {
        PoolingHttpClientConnectionManager safeClientConnManager = new PoolingHttpClientConnectionManager();
        safeClientConnManager.setDefaultMaxPerRoute(MAX_THREADS_FOR_SYNC_REQUEST);
        safeClientConnManager.setMaxTotal(MAX_THREADS_FOR_SYNC_REQUEST);
        HTTP_CLIENT_BUILDER = HttpClientBuilder.create();
        HTTP_CLIENT_BUILDER.setConnectionManager(safeClientConnManager);
        RequestConfig config = RequestConfig.copy(RequestConfig.DEFAULT).setConnectTimeout(CONNECTION_TIMEOUT).build();
        HTTP_CLIENT_BUILDER.setDefaultRequestConfig(config);
    }

    protected void doPut(String url, Object obj) throws HttpServiceAPIException {
        doPostOrUpdate(new HttpPut(url), obj);
    }

    private void doPostOrUpdate(HttpEntityEnclosingRequestBase request, Object obj) throws HttpServiceAPIException {
        String requestData = getGsonInstance().toJson(obj);
        StringEntity entity = new StringEntity(requestData, ContentType.APPLICATION_JSON);
//    BasicHeader basicHeader = new BasicHeader(HTTP.CONTENT_TYPE, "application/json");
////    httpPost.getParams().setBooleanParameter("http.protocol.expect-continue", false);
//    entity.setContentType(basicHeader);
        request.setEntity(entity);
        doRequest(request, Void.class);
    }

    protected void doPost(String url, Object obj) throws HttpServiceAPIException {
        doPostOrUpdate(new HttpPost(url), obj);
    }

    protected void doDelete(String url) throws HttpServiceAPIException {
        doRequest(new HttpDelete(url), null);
    }

    protected <T> T doGet(String url, Class<? extends T> responseType) throws HttpServiceAPIException {
        return doRequest(new HttpGet(url), responseType);
    }

    private <T> T doRequest(HttpUriRequest httpRequest, Class<? extends T> responseType) throws HttpServiceAPIException {
        T result;
        httpRequest.addHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
        httpRequest.addHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
        try {
            try (CloseableHttpClient httpClient = HTTP_CLIENT_BUILDER.build()) {
                HttpResponse response = httpClient.execute(httpRequest);
                String responseString = "";
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity responseEntity = response.getEntity();
                    responseString = CharStreams.toString(new InputStreamReader(responseEntity.getContent(), Charsets.UTF_8));
                    result = getGsonInstance().fromJson(responseString, responseType);
                    EntityUtils.consume(responseEntity);
                } else {
                    httpRequest.abort();
                    throw new HttpServiceAPIException(responseString);
                }
            }
        } catch (IOException e) {
            throw new HttpServiceAPIException(e);
        }
        return result;
    }


    private Gson getGsonInstance() {
        return new GsonBuilder().registerTypeAdapter(DateTime.class, new JsonSerializer<DateTime>() {
            @Override
            public JsonElement serialize(DateTime date, Type type, JsonSerializationContext jsonSerializationContext) {
                return date == null ? null : new JsonPrimitive(date.toDate().getTime());
            }
        }).create();
    }
}
