package ru.trendtech.controllers;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petr on 08.02.14.
 */
public class Utils {
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
}
