package com.hehanpeng.framework.cloudhhp.common.util;

import java.math.BigDecimal;
import java.util.List;

/**
 * 字符串工具类
 * 
 */
public class StringUtil {

    /*
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        if (null == str || "".equals(str.trim()) || "null".equals(str.trim()) || str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    /*
     * 判断字符串是否为空
     */
    public static boolean isEmpty(Object str) {
        if (null == str || "".equals(str) || "null".equals(str.toString())) {
            return true;
        }
        return false;
    }

    /*
     * 判断字符串是否为空
     */
    public static boolean isNotEmpty(Object str) {
        return !isEmpty(str);
    }

    /*
     * 判断list对象是否为空
     */
    public static boolean listIsEmpty(List<?> list) {
        return (list == null || list.size() == 0) ? true : false;
    }

    /*
     * 空字符串对象转空字符串
     */
    public static String null2String(String str) {
        return str == null ? "" : str;
    }

    /*
     * 去掉字符串前后空格
     */
    public static String trim(String str) {
        return str.trim();
    }

    /**
     * 判断非空对象并转换成字符串
     * @param obj
     * @return
     */
    public static String toString(Object obj){
    	return isEmpty(obj)?"":obj.toString().trim();
    }
    
    /**
     * 把 数组1 与数组2 相加。
     * 
     * @param src 开始数组，
     * @param dst 结束数组
     * @return 返回 开始数组+结束数据
     */
    public static byte[] appendArray(byte[] src, byte[] dst) {
        byte[] newBytes = new byte[src.length + dst.length];
        System.arraycopy(src, 0, newBytes, 0, src.length);
        System.arraycopy(dst, 0, newBytes, src.length, dst.length);
        return newBytes;
    }

    /**
     * 
     * 判断对象是否为空
     * 
     * @param obj
     * @return
     * @see 1.0
     * @since 1.0
     */
    public static boolean isNull(Object obj) {
        return obj == null || "null".equals(obj.toString());
    }

    /**
     * 判断数组是否为空
     * @param values
     * @return
     */
    public static boolean areNotEmpty(String[] values)
    {
      boolean result = true;
      if ((values == null) || (values.length == 0)){
          result = false;
      }
      else {
        for (String value : values) {
          result &= !(isEmpty(value));
        }
      }
      return result;
    }
    
    /**
     * 
     * 
     * @param Number
     * @param decimal
     * @return
     */
    public static String parseDecNumner(String Number, int decimal) {
        String value = null;
        int divid = 1;
        if (decimal > 0) {
            for (int i = 0; i < decimal; i++) {
                divid = divid * 10;
            }
            value = String.valueOf(Float.parseFloat(Number) / (divid * 1.0));
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            int pos = value.indexOf(".") + 1;
            pos = value.length() - pos;
            if (pos < decimal) {
                for (int i = pos; i < decimal; i++) {
                    // value = value + "0";
                    sb.append("0");
                }
                value = sb.toString();
            } else if (pos > decimal) {
                value = value.substring(0, value.length() - pos + decimal);
            }
        } else {
            value = Number;
        }
        return value;
    }

    public static String fillString(String str, int i) {
        if (str.length() > i)
            return str.substring(0, i);
        int len = str.length();
        // 不足i位左边补0
        for (int j = 0; j < i - len; j++) {
            str = "0" + str;
        }
        return str;
    }

    /**
     * 金额转换，flag如果是0，元转为分；flag如果是1，分转为元；
     * 
     * @param flag,txnAt
     * @return
     */
    public static String transTxnAt(int flag, String txnAt) {

        if (txnAt == null || txnAt.equals("")) {
            return txnAt;
        }

        BigDecimal tmpTxnAt = new BigDecimal(txnAt);
        String objTxnAt = null;
        if (flag == 0) {
            tmpTxnAt = tmpTxnAt.multiply(new BigDecimal(100));
            objTxnAt = String.valueOf(tmpTxnAt.longValue());
        } else {
            objTxnAt = parseDecNumner(txnAt, 2);
        }
        return objTxnAt;
    }
    
    public static String rightEmptyString(String str, int i) {
        if (str.length() > i)
            return str.substring(0, i);
        int len = str.length();

        StringBuffer sb = new StringBuffer();
        sb.append(str);
        // 不足i位右边补空格
        for (int j = 0; j < i - len; j++) {
            sb.append(" ");
            // str = str+"0";
        }
        return sb.toString();
    }

    
}
