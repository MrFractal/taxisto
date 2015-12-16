package ru.trendtech.utils;

import org.apache.commons.lang3.text.WordUtils;

import java.util.Date;
import java.util.Random;

/**
 * Created by max on 10.02.14.
 */
public class StrUtils {
    private static final Random RANDOM = new Random(new Date().getTime());

    private static final String ALPHA_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String NUM = "0123456789";

    private static final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String SMALL_NUM = "0123456789abcdefghijklmnopqrstuvwxyz";

    private static String generateDigitString(int symbolsCount) {
        return generateString(NUM, symbolsCount);
    }

    private static String generateString(String chars, int symbolsCount) {
        StringBuilder sb = new StringBuilder(symbolsCount);
        for (int i = 0; i < symbolsCount; i++) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static String generateSMSCode() {
        return generateDigitString(3);
    }

    public static String generateSMSCodeByCountSymbol(int countSymbols) {
        return generateDigitString(countSymbols);
    }

    public static String generateDriverLogin() {
        return generateDigitString(6);
    }

    public static String generateAlphaNumString(int symbolsCount) {
        return generateString(ALPHA_NUM, symbolsCount);
    }

    public static String generateSmallNumString(int symbolsCount) {
        return generateString(SMALL_NUM, symbolsCount);
    }

    public static String generateAlphaString(int symbolsCount) {
        return generateString(ALPHA, symbolsCount);
    }

    public static String generateNumString(int symbolsCount) {
        return generateString(NUM, symbolsCount);
    }

    public static String generateRandomPhone() {
        String str = generateString(NUM, 7);
        return "+7983" + str;
    }

    public static String generateRandomAutoNumber() {
        return generateAlphaString(1) + generateNumString(3) + generateAlphaString(2) + "54";
    }

    public static String capitalize(String str){
        return WordUtils.capitalize(str);
    }
}
