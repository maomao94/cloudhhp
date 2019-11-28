package com.hehanpeng.framework.cloudhhp.common.util;

import com.hehanpeng.framework.cloudhhp.common.annotation.Required;
import com.hehanpeng.framework.cloudhhp.common.exception.BizException;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hehanpeng
 * 2019/4/23 18:41
 */
public class AnnontaionUtils {

    /**
     * 检查bean里标记为@Required的field是否为空，为空则抛异常
     *
     * @param bean 要检查的bean对象
     * @throws Exception
     */
    public static void checkRequiredFields(Object bean) throws BizException {
        List<String> requiredFields = new ArrayList<>();
        List<Field> fields = new ArrayList<>(Arrays.asList(bean.getClass().getDeclaredFields()));
        fields.addAll(Arrays.asList(bean.getClass().getSuperclass().getDeclaredFields()));
        for (Field field : fields) {
            try {
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                if (field.isAnnotationPresent(Required.class)) {
                    if (field.get(bean) == null || (field.get(bean) instanceof String && StringUtils.isEmpty(field.get(bean).toString()))) {
                        //两种情况，一种是值为null，另外一种情况是类型为字符串，但是字符串内容为空的，都认为是没有提供值
                        requiredFields.add(field.getName());
                    }
                }
                field.setAccessible(isAccessible);
            } catch (SecurityException | IllegalArgumentException
                    | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (!requiredFields.isEmpty()) {
            String msg = "必填字段 " + requiredFields + " 必须提供值";
            throw new BizException("9999", msg);
        }
    }
}
