package com.aaa.mybatisplus.util;

import lombok.SneakyThrows;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * description: 时间工具类
 *
 * @author 田留振(tianliuzhen @ haoxiaec.com)
 * @version 1.0
 * @date 2019/7/10
 */
public class TimeUtil {

    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_PATTERN = "yyyy-MM-dd";

    public static final String DATE_PATTERN2 = "yyyy/MM/dd";


    /**
     * 获取当天时间
     *
     * @return
     */
    public static String getNowDateDetail() {
        return DateTimeFormatter.ofPattern(DEFAULT_PATTERN).format(LocalDateTime.now());
    }

    /**
     * 获取当天时间
     *
     * @return
     */
    public static String getNowDate() {
        return DateTimeFormatter.ofPattern(DATE_PATTERN2).format(LocalDate.now());
    }

    /**
     * 获取当天最后时间
     *
     * @return
     */
    public static String getNowDateLastStr() {
        return DateTimeFormatter.ofPattern(DEFAULT_PATTERN).format(LocalDateTime.of(LocalDate.now(), LocalTime.MAX));
    }

    /**
     * 获取当天最后时间
     *
     * @return
     */
    public static Date getNowDateLast() {
        Date date = Date.from(LocalDateTime.of(LocalDate.now(), LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant());
        return date;
    }


    /**
     * 昨天开始时间
     *
     * @return
     */
    public static String getYesterdayStart() {
        return DateTimeFormatter.ofPattern(DATE_PATTERN).format(LocalDate.now().plusDays(-1));
    }

    /**
     * 昨天结束时间
     *
     * @return
     */
    public static String getYesterdayEnd() {
        return DateTimeFormatter.ofPattern(DEFAULT_PATTERN)
                .format(LocalDateTime.of(LocalDate.now().plusDays(-1), LocalTime.MAX));
    }


    /**
     * 一周前开始时间
     *
     * @return
     */
    public static String getWeekAgoStart() {
        return DateTimeFormatter.ofPattern(DATE_PATTERN).format(LocalDate.now().plusDays(-7));
    }

    /**
     * 一周前结束时间
     *
     * @return
     */
    public static String getWeekAgoEnd() {
        return DateTimeFormatter.ofPattern(DEFAULT_PATTERN)
                .format(LocalDateTime.of(LocalDate.now().plusDays(-7), LocalTime.MAX));
    }

    /**
     * 一月前开始时间
     *
     * @return
     */
    public static String getMonthAgoStart() {
        return DateTimeFormatter.ofPattern(DATE_PATTERN).format(LocalDate.now().plusDays(-30));
    }

    /**
     * 一月前结束时间
     *
     * @return
     */
    public static String getMonthAgoEnd() {
        return DateTimeFormatter.ofPattern(DEFAULT_PATTERN)
                .format(LocalDateTime.of(LocalDate.now().plusDays(-30), LocalTime.MAX));
    }

    public static LocalDateTime getNextWeekTime(LocalDateTime time) {
        Date date = localDateTimeToDate(time);
        LocalDateTime someAfterDayTime = getSomeAfterDayTime(date, 7);
        return someAfterDayTime;
    }

    /**
     * 获取昨天时间戳
     *
     * @return 1569427200
     */
    public static Long getYesterdayStamp() {
        return getTodayStamp() - 24 * 10 * 60 * 60;
    }

    /**
     * 获取几天前时间戳
     *
     * @return 1569427200
     */
    public static Long getYesterdayStampByDay(int day) {
        return getTodayStamp() - 24 * day * 60 * 60;
    }

    /**
     * 获取几分钟前的时间戳
     *
     * @return 1569427200
     */
    public static Long getYesterdayStampByMinute(int minute) {
        return System.currentTimeMillis() / 1000 - minute * 60;
    }


    /**
     * 获取今天时间戳
     *
     * @return 1569513600
     */
    public static Long getTodayStamp() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Long today = c.getTimeInMillis() / 1000;
        return today;
    }

    public static LocalDateTime getSomeAfterDayTime(Date dNow, int count) {
        //得到日历
        Calendar calendar = Calendar.getInstance();
        //把当前时间赋给日历
        calendar.setTime(dNow);
        //设置为前3月
        calendar.add(Calendar.DATE, count);
        //得到前3月的时间
        Date dAfter = calendar.getTime();
        return dateToLocalDateTime(dAfter);
    }


    /**
     * 获取指定时间到现在的剩余秒数
     *
     * @param time
     * @return
     */
    public static Long getSecondCountByDate(LocalDateTime time) {
        LocalDateTime now = LocalDateTime.now();
        long t1 = time.toEpochSecond(ZoneOffset.ofHours(0));
        long t2 = now.toEpochSecond(ZoneOffset.ofHours(0));
        long dif = t1 - t2;
        return dif;
    }

    /**
     * date 转成 localDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * localDateTime 转成 date
     *
     * @param time
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime time) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = time.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        return date;
    }

    /**
     * 获取之前的时间
     *
     * @return
     */
    public static String getBeforeDayByDay(int day) {
        return DateTimeFormatter.ofPattern(DATE_PATTERN).format(LocalDate.now().plusDays(-day));
    }

    /**
     * 获取前几个小时的时间戳
     *
     * @param day
     */
    public static String getIntegerByDay(int day) {
        Calendar calendar = Calendar.getInstance();
        /* HOUR_OF_DAY 指示一天中的小时 */
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - day);
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_PATTERN);

        return date2TimeStamp(df.format(calendar.getTime()), DEFAULT_PATTERN);
    }

    /**
     * 获取前几个小时的时间戳
     *
     * @param hour
     */
    public static String getIntegerByHour(int hour) {
        Calendar calendar = Calendar.getInstance();
        /* HOUR_OF_DAY 指示一天中的小时 */
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - hour);
        SimpleDateFormat df = new SimpleDateFormat(DEFAULT_PATTERN);

        return date2TimeStamp(df.format(calendar.getTime()), DEFAULT_PATTERN);
    }

    /**
     * 将时间戳转成日期字符串
     *
     * @param timeStamp 时间戳的值,类型为：Long
     * @param pattern   转成字符串的格式
     * @return
     */
    public static String getDateStringByTimeSTamp(Long timeStamp, String pattern) {
        String result = null;
        Date date = new Date(timeStamp * 1000);
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        result = sd.format(date);
        return result;
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

    /**
     * 计算昨天0点的时间戳
     *
     * @return java.lang.Long
     */
    public static Long getYesterdayStartBYt() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - 1, 0, 0, 0);
        long tt = calendar.getTime().getTime() / 1000;
        System.out.println(tt);
        return tt;
    }


    /**
     * 将timestamp转为LocalDateTime
     *
     * @param timestamp
     * @return java.time.LocalDateTime
     */
    public static LocalDateTime timestamToDatetime(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * 将LocalDataTime转为timestam
     *
     * @param ldt
     * @return long
     */
    public static long datatimeToTimestamp(LocalDateTime ldt) {
        long timestamp = ldt.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        return timestamp;
    }

    /**
     * 获取指定时间戳 前后时间加减
     *
     * @param timestamp
     * @return
     */
    public static Long getSpecifyTimestampByTimestamp(long timestamp) {
        LocalDateTime today_end = LocalDateTime.of(timestamToDatetime(timestamp * 1000).toLocalDate(), LocalTime.MAX);

        long timeStampSec = datatimeToTimestamp(today_end) / 1000;
        return Long.valueOf(String.format("%010d", timeStampSec));
    }

    public static void main(String[] args) {
        System.out.println(getNowDateLast());
    }

    public static String getTimeDifference(String beginTime, String endTime) {
        DateFormat sdf = new SimpleDateFormat(DEFAULT_PATTERN);
        Date begin = null;
        Date end = null;
        try {
            begin = sdf.parse(beginTime);
            end = sdf.parse(endTime);
            //计算时间差
            long diff = end.getTime() - begin.getTime();
            //计算天数
            long days = diff / (1000 * 60 * 60 * 24);
            //计算小时
            long hours = (diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            //计算分钟
            long minutes = (diff % (1000 * 60 * 60)) / (1000 * 60);
            //计算秒
            long seconds = (diff % (1000 * 60)) / 1000;
            //输出
            return "" + days + "天" + hours + "小时" + minutes + "分" + seconds + "秒";
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("你输入的日期格式不对，请重新输入！");
        }
        return "";
    }

    /**
     * 字符串  转 Date
     *
     * @param dateStr
     * @return
     */
    @SneakyThrows
    public static Date strToDate(String dateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_PATTERN);

        return formatter.parse(dateStr);
    }

    /**
     * Date  转 字符串
     *
     * @param date
     * @return
     */
    public static String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_PATTERN);
        return formatter.format(date);
    }

    /**
     * 字符串 转 LocalDateTime
     *
     * @param str
     * @return
     */
    public static LocalDateTime strToLocalDateTime(String str) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(DEFAULT_PATTERN);
        return LocalDateTime.parse(str, df);
    }

    /**
     * LocalDateTime  转 字符串
     *
     * @param date
     * @return
     */
    public static String localDateTimeToStr(LocalDateTime date) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(DEFAULT_PATTERN);
        return df.format(date);
    }
}
