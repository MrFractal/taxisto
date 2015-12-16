package ru.trendtech.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 /**
   * Created by petr on 23.10.2014.
 */
 public class JsonUtils {
    public static RestTemplate template() {
        RestTemplate template = new RestTemplate();
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter());
        converters.add(new StringHttpMessageConverter());
        template.setMessageConverters(converters);
//        template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        template.setRequestFactory(new SimpleClientHttpRequestFactory());
        return template;
    }



    public static Map getMapByJsonString(String json) {
        Map<String, String> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            //convert JSON string to Map
            map = mapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
            });
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
            return map;
    }

 }