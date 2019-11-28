package com.hehanpeng.framework.cloudhhp.module.forward.util;

import com.hehanpeng.framework.cloudhhp.common.constants.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author hehanpeng
 * @version V1.0
 */
@Slf4j
public class DateUtil {

    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_YYYYMMDDHHMMSSSSS = "yyyyMMddhhmmssSSS";

    public static final String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddhhmmss";

    public static String getCurrentDate() {
        String formatPattern_Short = "yyyyMMddhhmmss";
        SimpleDateFormat format = new SimpleDateFormat(formatPattern_Short);
        return format.format(new Date());
    }

    public static String getSeqString() {
        SimpleDateFormat fm = new SimpleDateFormat("yyyyMMddHHmmss"); // "yyyyMMdd G
        return fm.format(new Date());
    }

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获取当前时间，格式为 yyyyMMddHHmmss
     * @return
     */
    public static String getCurrentTimeStr(String format) {
        format = StringUtils.isBlank(format) ? FORMAT_YYYY_MM_DD_HH_MM_SS : format;
        Date now = new Date();
        return date2Str(now, format);
    }

    public static String date2Str(Date date) {
        return date2Str(date, FORMAT_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 时间转换成 Date 类型
     * @param date
     * @param format
     * @return
     */
    public static String date2Str(Date date, String format) {
        if ((format == null) || format.equals("")) {
            format = FORMAT_YYYY_MM_DD_HH_MM_SS;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        if (date != null) {
            return sdf.format(date);
        }
        return "";
    }

    /**
     * 获取批量付款预约时间
     * @return
     */
    public static String getRevTime() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        String dateString = new SimpleDateFormat(DateUtil.FORMAT_YYYYMMDDHHMMSS).format(cal.getTime());
        System.out.println(dateString);
        return dateString;
    }

    /**
     * 时间比较
     * @param date1
     * @param date2
     * @return DATE1>DATE2返回1，DATE1<DATE2返回-1,等于返回0
     */
    public static int compareDate(String date1, String date2, String format) {
        DateFormat df = new SimpleDateFormat(format);
        try {
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 把给定的时间减掉给定的分钟数
     * @param date
     * @param minute
     * @return
     */
    public static Date minusDateByMinute(Date date, int minute) {
        Date newDate = new Date(date.getTime() - (minute * 60 * 1000));
        return newDate;
    }

    /**
     * 把给定的时间加上给定的秒数
     * @param date
     * @param value
     * @return
     */
    public static Date plusDateBySecond(Date date, int value) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.SECOND, value);
        return now.getTime();
    }

    /**
     * 把给定的时间加上给定的毫秒数
     * @param date
     * @param value
     * @return
     */
    public static Date plusDateByMilliSecond(Date date, int value) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.MILLISECOND, value);
        return now.getTime();
    }

    /**
     * 计算下次激活时间
     * @param safRetryLimit
     * @param retryCount
     * @param safBaseInterval
     * @param safIntervalDelta
     * @return
     */
    public static Date calcNextActiveTime(int safRetryLimit, int retryCount, long safBaseInterval, long safIntervalDelta, String fwdType) {
        Calendar cal = Calendar.getInstance();
        if (retryCount > safRetryLimit) {
            return null;
        }

        double nextInterval = getNextInterval(fwdType, retryCount, safBaseInterval, safIntervalDelta);
        cal.add(Calendar.MILLISECOND, (int)nextInterval);
        return cal.getTime();
    }

    private static double getNextInterval(String fwdType, int retryCount, long safBaseInterval, long safIntervalDelta) {
        if (CommonConstants.FWD_TYPE.FWDTYPE_NORMAL.equals(fwdType)) {
            return safBaseInterval + safIntervalDelta * retryCount;
        } else if (CommonConstants.FWD_TYPE.FWDTYPE_POW.equals(fwdType)) {
            return safBaseInterval + Math.pow(safIntervalDelta/1000, retryCount) * 1000;
        } else {
            log.error("invalid fwdType:" + fwdType);
            return 0;
        }
    }
}
