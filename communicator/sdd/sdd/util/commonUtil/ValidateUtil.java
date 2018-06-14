package com.kuangchi.sdd.util.commonUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidateUtil {

    public static boolean isRequired(String str) {
        return (!(StringUtil.isNull(str)));
    }

    public static boolean isString(String str) {
        if ("".equals(str))
            return false;
        return StringUtil.isWord(str);
    }

    public static boolean compareString(String str1, String str2) {
        return str1.equals(str2);
    }

    public static boolean isDate(String str, String dateFormat) {
        if (!(StringUtil.isNull(str))) {
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            formatter.setLenient(false);
            try {
                formatter.format(formatter.parse(str));
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static int compareDate(String date1, String date2, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime())
                return 1;
            if (dt1.getTime() < dt2.getTime())
                return -1;

            return 0;
        } catch (Exception exception) {
            exception.printStackTrace();

            return 0;
        }
    }

    public static boolean isNumber(String str) {
        if ("".equals(str))
            return false;
        return StringUtil.isNumber(str);
    }

    public static boolean isEmail(String str) {
        if ("".equals(str))
            return false;
        return StringUtil.isEmail(str);
    }
}