package com.zhangry.demo.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangry on 2017/3/17.
 */
public class DateUtil {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
    public static final String DATETIME_FORMAT2 = "yyyy/MM/dd hh:mm:ss";
    private static final Object lockObj = new Object();
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap();

    private DateUtil() {
    }

    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = (ThreadLocal)sdfMap.get(pattern);
        if(tl == null) {
            Object var2 = lockObj;
            synchronized(lockObj) {
                tl = (ThreadLocal)sdfMap.get(pattern);
                if(tl == null) {
                    tl = new ThreadLocal<SimpleDateFormat>() {
                        protected SimpleDateFormat initialValue() {
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, tl);
                }
            }
        }

        return (SimpleDateFormat)tl.get();
    }

    public static Date toDate(String date, String pattern) {
        AssertUtil.notEmpty(date);
        AssertUtil.notEmpty(pattern);

        try {
            Date result = getSdf(pattern).parse(date);
            return result;
        } catch (ParseException var4) {
            throw ExceptionUtil.unchecked(var4);
        }
    }

    public static Date toDate(String date) {
        return toDate(date, "yyyy-MM-dd hh:mm:ss");
    }

    public static String toChar(Date date, String pattern) {
        AssertUtil.notNull(date);
        AssertUtil.notEmpty(pattern);
        SimpleDateFormat sdf = getSdf(pattern);
        return sdf.format(date);
    }

    public static String toChar(Date date) {
        AssertUtil.notNull(date);
        SimpleDateFormat sdf = getSdf("yyyy-MM-dd hh:mm:ss");
        return sdf.format(date);
    }

    public static Date addDay(Date date, int days) {
        AssertUtil.notNull(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(6, days);
        return calendar.getTime();
    }
}

