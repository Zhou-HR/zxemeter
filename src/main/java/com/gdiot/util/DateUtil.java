package com.gdiot.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhouHR
 */
public class DateUtil {
    /**
     * 时间戳转换成日期格式字符串
     *
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param date_str 字符串日期
     * @param format   如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String shortenTimeStr(String timestr) {
        String[] parts = timestr.split("\\.");
        String timestr2 = parts[0];//+ "." + parts[1].substring(0, 3) ;
//		System.out.println("timestr2:" + timestr2);
        return timestr2;
    }

    public static long date2TimeStampLong(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date_str).getTime(); //  /1000
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long date2TimeStampLong_l(String date_str, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date_str).getTime(); //  
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 取得当前时间戳（精确到秒）
     *
     * @return
     */
    public static String timeStamp() {
        long time = System.currentTimeMillis();
        String t = String.valueOf(time / 1000);
        return t;
    }

    /**
     * 获取指定日期的年月日
     *
     * @param time
     * @return
     */
    public static String getYearMonthDay(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        System.out.println("YearMonthDay=" + sdf.format(date));
        return sdf.format(date);
    }

    /**
     * 获取指定日期的年份
     *
     * @param time
     * @return
     */
    public static String getYear(long time) {
        Date date = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);//一年中的第几天
        int month = calendar.get(Calendar.MONTH) + 1;//第几个月
        int year = calendar.get(Calendar.YEAR);//年份数值
        System.out.println("year=" + year);
        return String.valueOf(year);
    }

    /**
     * 获取指定日期的月
     *
     * @param time
     * @return
     */
    public static String getMonth(long time) {
        Date date = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;//第几个月
        System.out.println("month=" + month);
        return String.valueOf(month);
    }

    /**
     * 获取指定日期的日
     *
     * @param time
     * @return
     */
    public static String getDay(long time) {
        Date date = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);//一年中的第几天
        System.out.println("day=" + day);
        return String.valueOf(day);
    }

    public static void main(String[] args) {
        String timeStamp = timeStamp();
        System.out.println("timeStamp=" + timeStamp); //运行输出:timeStamp=1470278082
        System.out.println(System.currentTimeMillis());//运行输出:1470278082980
        //该方法的作用是返回当前的计算机时间，时间的表达格式为当前计算机时间和GMT时间(格林威治时间)1970年1月1号0时0分0秒所差的毫秒数

        String date = timeStamp2Date(timeStamp, "yyyy-MM-dd HH:mm:ss");
        System.out.println("date=" + date);//运行输出:date=2016-08-04 10:34:42

        String timeStamp2 = date2TimeStamp(date, "yyyy-MM-dd HH:mm:ss");
        System.out.println(timeStamp2);  //运行输出:1470278082
    }

    private static final char[] HEX_CHAR_TABLE = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };
    private static final Map<Character, Byte> MAP = new HashMap<>();

    static {
        for (int i = 0; i < HEX_CHAR_TABLE.length; i++) {
            char c = HEX_CHAR_TABLE[i];
            MAP.put(c, (byte) i);
        }
    }

    public static String toHexString(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            sb.append(HEX_CHAR_TABLE[(b & 0xf0) >> 4]);
            sb.append(HEX_CHAR_TABLE[b & 0x0f]);
        }
        return sb.toString();
    }

    public static byte[] toByteArray(String hexString) {
        byte[] result = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length() / 2; i++) {
            char hi = hexString.charAt(i * 2);
            char lo = hexString.charAt(i * 2 + 1);
            result[i] = (byte) ((MAP.get(hi) << 4) + MAP.get(lo));
        }
        return result;
    }
}
