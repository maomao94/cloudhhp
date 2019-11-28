package com.hehanpeng.framework.cloudhhp.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * 常用的日期转换类
 */
public class DateUtil {
    public final static String defaultSimpleFormater = "yyyy-MM-dd HH:mm:ss";
    public final static String defaultSimpleFormater2 = "yyyyMMdd HH:mm:ss";
    public static final String YYMM = "yyyyMM";

    public static final String DDHHMMSS = "ddHHmmss";

    public static final String YYMMDDHHMMSSSSS = "yyMMddHHmmssSSS";

    public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    
    public static final String YYYYMMDDHHMMSSSSS2 = "yyyy-MM-dd HH:mm:ss,SSS";

    public static final String YYMMDD = "yyMMdd";

    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static final String YYYYMMDD = "yyyyMMdd";

    public static final String HHMMSS = "HHmmss";

    /**
     * 默认简单日期字符串
     * 
     * @return
     */
    public static String getDefaultSimpleFormater() {
        return defaultSimpleFormater;
    }

    /**
     * 格式化日期
     * 
     * @param date
     * @param formatString
     * @return
     */
    public static String format(Date date, String formatString) {
        if (date == null) {
            return null;
        } else {
            SimpleDateFormat df = new SimpleDateFormat(formatString);
            return df.format(date);
        }
    }

    /**
     * 格式化日期(使用默认格式)
     * 
     * @param date
     * @return
     */
    public static String format(Date date) {
        return format(date, defaultSimpleFormater);
    }

    /**
     * 转换成日期
     * 
     * @param dateString
     * @param formatString
     * @return
     */
    public static Date parse(String dateString, String formatString) {
        SimpleDateFormat df = new SimpleDateFormat(formatString);
        try {
            return df.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 转换成日期(使用默认格式)
     * 
     * @param dateString
     * @return
     */
    public static Date parse(String dateString) {
        return parse(dateString, defaultSimpleFormater);
    }

    /**
     * 昨天
     * 
     * @return
     */
    public static Date yesterday() {
        return addDay(-1);
    }

    /**
     * 明天
     * 
     * @return
     */
    public static Date tomorrow() {
        return addDay(1);
    }

    /**
     * 现在
     * 
     * @return
     */
    public static Date now() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 按日加
     * 
     * @param value
     * @return
     */
    public static Date addDay(int value) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_YEAR, value);
        return now.getTime();
    }

    /**
     * 按日加,指定日期
     * 
     * @param date
     * @param value
     * @return
     */
    public static Date addDay(Date date, int value) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.DAY_OF_YEAR, value);
        return now.getTime();
    }

    /**
     * 获取本月1日
     * 
     * @return
     */
    public static Date getCureentMonthFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date getCurrentDayStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();

    }

    public static Date getCurrentDayEnd() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();

    }

    public static Date getSomeDayStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getSomeDayEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 判断某一时间是否在【startTime，endTime】区间
     * @param someTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public static boolean isEffectiveTime(Date someTime, Date startTime, Date endTime) {
        if (someTime.getTime() == startTime.getTime() || someTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar some = Calendar.getInstance();
        some.setTime(someTime);

        Calendar start = Calendar.getInstance();
        start.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        return some.after(start) && some.before(end);
    }

    /**
     * 获取本月最后一天
     * 
     * @return
     */
    public static Date getCureentMonthLastDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 获取指定日期的1号
     * 
     * @return
     */
    public static Date getMonthFirstDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取指定日期月份的最后一天
     * 
     * @return
     */
    public static Date getMonthLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static Date getMonthLastDay2(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);
        calendar.roll(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 按月加
     * 
     * @param value
     * @return
     */
    public static Date addMonth(int value) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MONTH, value);
        return now.getTime();
    }

    /**
     * 按月加,指定日期
     * 
     * @param date
     * @param value
     * @return
     */
    public static Date addMonth(Date date, int value) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.MONTH, value);
        return now.getTime();
    }

    /**
     * 按年加
     * 
     * @param value
     * @return
     */
    public static Date addYear(int value) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, value);
        return now.getTime();
    }

    /**
     * 按年加,指定日期
     * 
     * @param date
     * @param value
     * @return
     */
    public static Date addYear(Date date, int value) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.YEAR, value);
        return now.getTime();
    }

    /**
     * 按小时加
     * 
     * @param value
     * @return
     */
    public static Date addHour(int value) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR_OF_DAY, value);
        return now.getTime();
    }

    /**
     * 按小时加,指定日期
     * 
     * @param date
     * @param value
     * @return
     */
    public static Date addHour(Date date, int value) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.HOUR_OF_DAY, value);
        return now.getTime();
    }

    /**
     * 按分钟加
     * 
     * @param value
     * @return
     */
    public static Date addMinute(int value) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, value);
        return now.getTime();
    }

    /**
     * 按分钟加,指定日期
     * 
     * @param date
     * @param value
     * @return
     */
    public static Date addMinute(Date date, int value) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.MINUTE, value);
        return now.getTime();
    }

    /**
     * 年份
     * 
     * @return
     */
    public static int year() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.YEAR);
    }

    /**
     * 月份
     * 
     * @return
     */
    public static int month() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.MONTH);
    }

    /**
     * 日(号)
     * 
     * @return
     */
    public static int day() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 小时(点)
     * 
     * @return
     */
    public static int hour() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.HOUR);
    }

    /**
     * 分钟
     * 
     * @return
     */
    public static int minute() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.MINUTE);
    }

    /**
     * 秒
     * 
     * @return
     */
    public static int second() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.SECOND);
    }

    /**
     * 星期几(礼拜几)
     * 
     * @return
     */
    public static int weekday() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 是上午吗?
     * 
     * @return
     */
    public static boolean isAm() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.AM_PM) == 0;
    }

    /**
     * 是下午吗?
     * 
     * @return
     */
    public static boolean isPm() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.AM_PM) == 1;
    }

    public static String getDDHHMMSS() {
        return getDateStr(DDHHMMSS);
    }

    public static String getDateStr(String formatPattern) {
        Date date = new Date();
        return getDateStr(date, formatPattern);
    }

    private static String getDateStr(Date date, String formatPattern) {
        String dateStr = "";
        if (date == null)
            date = new Date();
        try {
            SimpleDateFormat df = new SimpleDateFormat(formatPattern);
            dateStr = df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    /**
     * 
     * 字符串转日期对象
     * 
     * @param dateStr 日期字符串
     * @param formatPattern 日期格式
     * @return
     * @see 1.0
     * @since 1.0
     */
    public static Date str2Date(String dateStr, String formatPattern) {
        SimpleDateFormat df = new SimpleDateFormat(formatPattern);
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 
     * 日期对象转字符串
     * 
     * @param date 日期对象
     * @param formatPattern 格式
     * @return
     * @see 1.0
     * @since 1.0
     */
    public static String date2Str(Date date, String formatPattern) {
        SimpleDateFormat df = new SimpleDateFormat(formatPattern);
        String dateStr = df.format(date);
        return dateStr;
    }
    
    
    /**
     * 根据模式返回一个新的SimpleDateFormat,防止在多线程的时候出现混乱
     * 
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSimpleDateFormat(String pattern) {
        return new SimpleDateFormat(pattern);
    }
    /**
     * 获得系统当前日期时间
     * 
     * @return
     */
    public static String getCurrentDateTime() {
        SimpleDateFormat sdf = getSimpleDateFormat(YYYYMMDDHHMMSS);
        return sdf.format(Calendar.getInstance().getTime());
    }
    
    public static long getStampFromTime(Date now,int num) {
    	SimpleDateFormat sdf = getSimpleDateFormat(YYYYMMDD);
    	Date date = null;
        try {
            date = sdf.parse(sdf.format(addDay(now,num)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
    	return cal.getTimeInMillis();
    }
    
    
    /**
     * 获得当天剩余时间毫秒数
     * @return
     */
    public static long getRestOfTheDay() {
    	LocalDateTime midNight=LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
    	long millSeconds=ChronoUnit.MILLIS.between(LocalDateTime.now(), midNight);
    	return millSeconds;
    }
    
    /**
     * 根据入参判断是否超时
     * @return
     */
    public static boolean isTimeExpired(long timeMillis,long expires) {
		return timeMillis+expires < System.currentTimeMillis();
	}
}
