package com.wf.ew.common.config;

import com.wf.ew.common.utils.DateUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日期类型转换器
 * Created by 王帆 on 2018-08-17 上午 8:43.
 */
@Component
public class DateConverterConfig implements Converter<String, Date> {
    private static final List<String> formarts = new ArrayList<>(4);

    /**
     * 以下几种时间格式自动转成Date类型
     */
    static {
        formarts.add("yyyy-MM-dd");
        formarts.add("yyyy-MM-dd HH:mm");
        formarts.add("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public Date convert(String s) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        } else if (s.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return DateUtil.parseDate(s, formarts.get(0));
        } else if (s.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            return DateUtil.parseDate(s, formarts.get(1));
        } else if (s.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return DateUtil.parseDate(s, formarts.get(2));
        } else {
            throw new IllegalArgumentException("Invalid date value '" + s + "'");
        }
    }
}
