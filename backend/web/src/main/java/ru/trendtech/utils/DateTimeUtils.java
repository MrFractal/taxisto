package ru.trendtech.utils;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.MutableDateTime;
import org.joda.time.Years;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtils {

    private static final GregorianChronology DEFAULT_CHRONOLOGY = GregorianChronology.getInstance();

//    public static Date toDate(DateTime datetime) {
//        return datetime != null ? new Date(datetime.getMillis()) : null;
//    }

    public static long toDate(DateTime datetime) {
        return datetime != null ? datetime.getMillis() : 0;
    }

    public static Date toDate(long datetime) {
        return datetime != 0 ? new Date(datetime) : null;
    }

    public static DateTime toDateTime(long datetime) {
        //datetime+=21600000;
        return new DateTime(datetime);
    }

    public static LocalDate getLocalDate(int birthdayYear, int birthdayMonth, int birthdayDay) {
        return new LocalDate(birthdayYear, birthdayMonth, birthdayDay, DEFAULT_CHRONOLOGY);
    }



    public static int getCountYearsOld(LocalDate birthday){
        LocalDate now = new LocalDate();
        Years age = Years.yearsBetween(birthday, now);
        return age.getYears();
    }


    public static DateTime nowNovosib(){
        TimeZone tz = TimeZone.getTimeZone("GMT+7");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MILLISECOND, tz.getOffset(now().toDate().getTime()));
        DateTime nowDateTime = new DateTime(calendar2.getTime());
          return nowDateTime;
    }


    /*
  JODA TIME
String dateTime = "11/15/2013 08:00:00";
// Format for input
DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
// Parsing the date
DateTime jodatime = dtf.parseDateTime(dateTime);
// Format for output
DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MM/dd/yyyy");
// Printing the date
System.out.println(dtfOut.print(jodatime));

  STANDART JAVA WAY
  // Format for input
SimpleDateFormat dateParser = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
// Parsing the date
Date date = dateParser.parse(dateTime);
// Format for output
SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
// Printing the date
System.out.println(dateFormatter.format(date));
     */


    public static DateTime dateTimeByPattern(String time, String pattern) throws ParseException {
        DateFormat form = new SimpleDateFormat(pattern);
        Date date = form.parse(time);
        DateTime timeOfStart = DateTimeUtils.toDateTime(date.getTime());
        return timeOfStart;
    }


    public static LocalDate localeDateByPattern(String date, String pattern)   {
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern(pattern); // pattern: "dd.MM 'в' HH:mm"
        return LocalDate.parse(date, dtfOut);
    }


    public static String stringDateTimeByPattern(DateTime dateTime, String pattern){
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern(pattern); // pattern: "dd.MM 'в' HH:mm"
        return dtfOut.print(dateTime);
    }



    public static String splitToComponentTimes(BigDecimal biggy) {
        String result = "";
        long longVal = biggy.longValue();
        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;
        int[] ints = {hours , mins , secs};
           for(int i=0; i<ints.length; i++){
                if(i==0){
                    result+= ints[i]+":";
                }else if(i==1){
                    result+= ints[i];
                } else{
                    result+= ":"+ints[i];
                }
           }
        return result;
    }

    public static DateTime nowNovosib_GMT6(){
        TimeZone tz = TimeZone.getTimeZone("GMT+6");
        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MILLISECOND, tz.getOffset(now().toDate().getTime()));
        DateTime nowDateTime = new DateTime(calendar2.getTime());
        return nowDateTime;
    }


    public DateTime getDateTimeByDayNumber(int day){
        MutableDateTime mdt1 = new MutableDateTime();
        mdt1.setDayOfMonth(day);
        mdt1.setHourOfDay(23);
        mdt1.setMinuteOfDay(59);
        mdt1.setSecondOfDay(59);
        mdt1.setMillisOfDay(0); // if you want to make sure you're at midnight
        return new DateTime(mdt1.toDate());
    }


    public static DateTime startOfMonth(DateTime dateTime){
        return dateTime.dayOfMonth().withMinimumValue();
    }

    public static DateTime now() {
        return DateTime.now();
    }
}
