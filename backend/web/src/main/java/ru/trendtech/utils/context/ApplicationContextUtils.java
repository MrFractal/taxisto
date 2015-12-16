package ru.trendtech.utils.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by petr on 04.12.2014.
 */

// убрать нафиг
public class ApplicationContextUtils {
    private static class SingletonHolder {
        private static ApplicationContext ctx = new ClassPathXmlApplicationContext("META-INF/application-context.xml");
    }
    public static ApplicationContext getInstance() {
        return SingletonHolder.ctx;
    }
}