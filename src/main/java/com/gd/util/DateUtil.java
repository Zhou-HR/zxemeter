package com.gd.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author ZhouHR
 */
public class DateUtil {

    public static String getYearMonth(Date now) {
        Date date = new Date();
        if (now != null) date = now;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

        return sdf.format(date);
    }

    /**
     * 将当前时间格式化为yyyyMMdd
     *
     * @return
     */
    public static String getToday() {
        Date date = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        return sdf.format(date);
    }

    public static String getTime(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(date);
    }

    public static String getTodayTime() {
        Date date = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(date);
    }

    public static String getToday2() {
        Date date = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return sdf.format(date);
    }


    /**
     * 将时间格式化为yyyyMMdd HH
     *
     * @param date
     * @return
     */
    public static String getHour(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");

        return sdf.format(date);
    }

    public static String getDmpHour(Date date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");

        return sdf.format(date);
    }

    public static String getNowYear() {
        String where = "";
        Calendar c1 = Calendar.getInstance();
        Integer year = c1.get(Calendar.YEAR);
        Integer month = c1.get(Calendar.MONTH);
        month++;
        if (month >= 4) {
            String start = year.toString() + "0401";
            where = " and t1.create_date>='" + start + "' ";
            return where;
        } else {
            String end = year.toString() + "0401";
            year--;
            String start = year.toString() + "0401";
            where = " and t1.create_date>='" + start + "' and t1.create_date<'" + end + "' ";
            return where;
        }

    }

    public static void main(String[] args) {
        System.out.println(getDmpHour(new Date()));
    }

}
