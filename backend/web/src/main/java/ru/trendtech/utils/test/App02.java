package ru.trendtech.utils.test;

import ru.trendtech.utils.DateTimeUtils;

/**
 * Created by petr on 06.11.2015.
 */
public class App02 {
    public static void main(String[] args) {
        System.out.println(DateTimeUtils.stringDateTimeByPattern(DateTimeUtils.nowNovosib_GMT6(), "dd.MM 'в' HH:mm"));
    }
}
