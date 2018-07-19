package com.campray.lesswalletandroid.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author :Phills
 * @project:TimeUtil
 * @date :2017-08-26-17:27
 */
public class TimeUtil {

    public final static String FORMAT_TIME = "HH:mm:ss";
    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_DATE_TIME_SECOND = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_DATE = "yyyy/MM/dd";

    public static String getFormatToday(String dateFormat) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(currentTime);
    }

    public static Date stringToDate(String dateStr, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date stringToDate(String dateStr, String dateFormat, TimeZone timeZone) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        formatter.setTimeZone(timeZone);
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String dateToString(Date date, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(date);
    }

    public static String dateToString(Date date, String dateFormat,TimeZone timeZone) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        formatter.setTimeZone(timeZone);
        return formatter.format(date);
    }

    /**
     * 修改日期字符串
     * @param dateStr 原始时间字符串（）
     * @param dateFormat 原始时间格式
     * @param timeZone 原始时间时区
     * @param year 要增加的年数
     * @param month 要增加的月数
     * @param dayOfMonth 要增加的天数
     * @return
     */
    public static String modifyDateStr(String dateStr,String dateFormat,TimeZone timeZone,int year,int month,int dayOfMonth){
        Date sdate= TimeUtil.stringToDate(dateStr, dateFormat, timeZone);
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeZone(timeZone);
        calendar.setTime(sdate);
        calendar.add(Calendar.DAY_OF_MONTH,dayOfMonth);
        calendar.add(Calendar.MONTH,month);
        calendar.add(Calendar.YEAR,year);
        Date edate=calendar.getTime();
        return TimeUtil.dateToString(edate,dateFormat,timeZone);
    }

    /**
     * 判断是否为今天(效率比较高)
     *
     * @param dateStr 传入的 日期时间
     * @param dateFormat
     * @param timeZone
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean isToday(String dateStr, String dateFormat,TimeZone timeZone){
        //当前系统时间
        Calendar pre = Calendar.getInstance();
        pre.setTimeZone(timeZone);
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);
        //传入时间
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(timeZone);
        Date date = TimeUtil.stringToDate(dateStr,dateFormat,timeZone);
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            if (cal.get(Calendar.DAY_OF_YEAR) == pre.get(Calendar.DAY_OF_YEAR)) {
                return true;
            }
        }
        return false;
    }

}
