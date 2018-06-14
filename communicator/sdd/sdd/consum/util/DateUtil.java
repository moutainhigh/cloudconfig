package com.kuangchi.sdd.consum.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class DateUtil {

    private static Map<String, DateFormat> dateFormatCache = new HashMap();

    private static final String DEFALT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String SHORT_DATE_FORMAT = "yyyy-MM-dd";

    public static String getFormatDate(String dateStr) {
        return getFormatDate(dateStr, null);
    }

    public static String getFormatDate(String dateStr, String dateFormat) {
        if ((dateStr == null) || (dateStr.length() == 0))
            return null;

        String defDateFormat = dateFormat;
        if ((defDateFormat == null) || (defDateFormat.length() == 0))
            defDateFormat = "yyyy-MM-dd HH:mm:ss";

        Format formatter = new SimpleDateFormat(defDateFormat);
        try {
            return formatter.format(formatter.parseObject(dateStr));
        } catch (ParseException pe) {
            pe.printStackTrace();
            return null;
        }
    }

    public static String getSysDate() {
        return new Timestamp(System.currentTimeMillis()).toString().substring(0, 10);
    }

    public static String getSysDateTime() {
        return new Timestamp(System.currentTimeMillis()).toString().substring(0, 19);
    }

    public static Date getDate(String dateStr, String dateFormat) {
        if ((dateStr == null) || (dateStr.length() == 0))
            return null;
        Date result = null;
        try {
            if ((dateFormat == null) || (dateFormat.length() == 0))
                result = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            else
                result = new SimpleDateFormat(dateFormat).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Date getDateTime(String dateStr) {
        return getDate(dateStr, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date getDate(String dateStr) {
        return getDate(dateStr, "yyyy-MM-dd");
    }

    public static String getDateString(Date date) {
        if (date == null)
            return null;
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String getDateTimeString(Date date) {
        if (date == null)
            return null;
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static String getDateString(Date date, String dateFormat) {
        if (date == null)
            return null;
        if ((dateFormat == null) || (dateFormat.length() == 0))
            return new SimpleDateFormat("yyyy-MM-dd").format(date);
        return new SimpleDateFormat(dateFormat).format(date);
    }

    public static Timestamp getSysTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp getTimestamp(String dateStr) {
        Timestamp rtValue = null;
        if ((dateStr == null) || (dateStr.length() == 0))
            return null;
        try {
            rtValue = Timestamp.valueOf(dateStr);
        } catch (Exception e) {
            dateStr = dateStr.trim() + " 00:00:00";
            try {
                rtValue = Timestamp.valueOf(dateStr);
            } catch (RuntimeException re) {
                re.printStackTrace();
            }
        }
        return rtValue;
    }

    public static String getTimestampString(Timestamp timestamp) {
        if (timestamp == null)
            return null;
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp);
    }

    public static String getFormatDateTimeDesc(long longDate) {
        return new Timestamp(longDate).toString().substring(0, 19);
    }

    public static boolean isToday(Date thisDate) {
        String today = getFormatDateTimeDesc(System.currentTimeMillis());
        String thisDateCal = getFormatDateTimeDesc(thisDate.getTime());

        return (today.substring(0, 10).endsWith(thisDateCal.substring(0, 10)));
    }

    public static Date add(Date date, int n) {
        if (date == null)
            return null;
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(5, n);
        return gc.getTime();
    }

    public static Date add(int n) {
        return add(new Date(), n);
    }

    public static String asString(Date date) {
        String key = "asString";
        DateFormat formatter = (DateFormat) dateFormatCache.get(key);
        if (formatter == null) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormatCache.put(key, formatter);
        }
        return formatter.format(date);
    }

    public static String asShortString(Date date) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    public static String asHtml(Date date) {
        return asHtml(date, TimeZone.getDefault());
    }

    public static String asHtml(Date date, TimeZone timeZone) {
        String key = timeZone.getID();
        DateFormat formatter = (DateFormat) dateFormatCache.get(key);
        if (formatter == null) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formatter.setTimeZone(timeZone);
            dateFormatCache.put(key, formatter);
        }
        synchronized (formatter) {
            String s = formatter.format(date);
            return s;
        }
    }

    public static String asShortString(Date date, TimeZone timeZone) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(timeZone);
        return formatter.format(date);
    }

    public static String asNameSuffix(Date date, TimeZone timeZone) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(timeZone);
        return formatter.format(date);
    }

    public static Date getDate(int time) {
        return new Date(time * 1000L);
    }

    public static int currentTimeSeconds() {
        return (int) (System.currentTimeMillis() / 1000L);
    }

    public static int secondsAfter(Date a, Date b) {
        return (int) ((a.getTime() - b.getTime()) / 1000L);
    }

    public static int secondsBefore(Date a, Date b) {
        return secondsAfter(b, a);
    }

    public static Date getDate(int yy, int mm, int dd) {
        return new GregorianCalendar(yy, mm - 1, dd).getTime();
    }

    public static int unixTimeStamp() {
        return (int) (System.currentTimeMillis() / 1000L);
    }

    public static int unixTimeStamp(Date date) {
        return (int) (date.getTime() / 1000L);
    }

    public static void main(String[] args) {
    }
}