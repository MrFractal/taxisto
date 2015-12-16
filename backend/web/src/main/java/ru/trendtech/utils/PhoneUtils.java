package ru.trendtech.utils;

import com.google.i18n.phonenumbers.PhoneNumberMatch;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

/**
 * Created by max on 10.02.14.
 */
public class PhoneUtils {
    private static final PhoneNumberUtil PHONE_NUMBER_UTIL = PhoneNumberUtil.getInstance();

    public static String normalizeNumber(String phone) {
        String result = null;
        try {
            Iterable<PhoneNumberMatch> numbers = PHONE_NUMBER_UTIL.findNumbers(phone, "RU");
            for (PhoneNumberMatch phoneNumberMatch : numbers) {
                Phonenumber.PhoneNumber number = phoneNumberMatch.number();
                result = PHONE_NUMBER_UTIL.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);
            }
        }catch(Exception t){
            t.printStackTrace();
              throw t;
        }
        return result;
    }

//    public static boolean isValid(String phone) {
//        boolean result = false;
//        Iterable<PhoneNumberMatch> numbers = PHONE_NUMBER_UTIL.findNumbers(phone, "RU");
//        for (PhoneNumberMatch phoneNumberMatch : numbers) {
//            Phonenumber.PhoneNumber number = phoneNumberMatch.number();
//            result = PHONE_NUMBER_UTIL.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);
//        }
//        return result;
//    }

}
