package com.wf.ew.common;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wf.ew.common.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 列表分页、排序、搜索通用参数封装
 * Created by wangfan on 2019-04-26 上午 10:34.
 */
public class PageParam extends Page {
    private static final Pattern humpPattern = Pattern.compile("[A-Z]");  // 驼峰正则匹配
    private static final boolean needToLine = true;  // 是否需要把前端传递的字段驼峰转下划线
    private static final String FILED_PAGE = "page";  // 前端分页页数参数名称
    private static final String FILED_LIMIT = "limit";  // 前端分页数量参数名称
    private static final String FILED_SORT = "sort";  // 前端排序字段参数名称
    private static final String FILED_ORDER = "order";  // 前端排序方式参数名称
    private static final String VALUE_ORDER_ASC = "asc";  // order升序的值
    private static final String VALUE_ORDER_DESC = "desc";  // order降序的值
    private HashMap<String, Object> pageData;  // 页面传递的其他参数

    public PageParam() {
        super();
    }

    /**
     * 从request中获取参数并构建PageParam对象
     */
    public PageParam(HttpServletRequest request) {
        String fOrder = null, fSort = null;
        HashMap map = new HashMap();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = request.getParameter(name);
            if (StringUtil.isBlank(value)) {
                value = null;
            }
            if (FILED_PAGE.equals(name)) {
                if (value != null) {
                    setPage(Long.parseLong(value));
                }
            } else if (FILED_LIMIT.equals(name)) {
                if (value != null) {
                    setLimit(Long.parseLong(value));
                }
            } else if (FILED_ORDER.equals(name)) {
                if (value != null) {
                    fOrder = value;
                }
            } else if (FILED_SORT.equals(name)) {
                if (value != null) {
                    fSort = (needToLine ? humpToLine(value) : value);
                }
            } else {
                map.put(name, value);
            }
        }
        setPageData(map);
        // 同步排序方式到MyBatisPlus中
        if (VALUE_ORDER_DESC.equals(fOrder)) {
            if (fSort != null) {
                addOrderDesc(fSort);
            }
        } else if (VALUE_ORDER_ASC.equals(fOrder)) {
            if (fSort != null) {
                addOrderAsc(fSort);
            }
        }
    }

    public long getPage() {
        return getCurrent();
    }

    public void setPage(long page) {
        setCurrent(page);
    }

    public long getLimit() {
        return getSize();
    }

    public void setLimit(long limit) {
        setSize(limit);
    }

    public HashMap<String, Object> getPageData() {
        return pageData;
    }

    public void setPageData(HashMap<String, Object> data) {
        this.pageData = data;
    }

    /**
     * 往pageData里面增加参数
     */
    public PageParam put(String key, Object value) {
        pageData.put(key, value);
        return this;
    }

    /**
     * 获取pageData里面参数
     */
    public String getString(String key) {
        Object o = pageData.get(key);
        if (o == null) {
            return null;
        }
        return (String) o;
    }

    /**
     * 获取pageData里面参数
     */
    public Integer getInt(String key) {
        Object o = pageData.get(key);
        if (o == null) {
            return null;
        }
        return Integer.parseInt((String) o);
    }

    /**
     * 获取pageData里面参数
     */
    public Float getFloat(String key) {
        Object o = pageData.get(key);
        if (o == null) {
            return null;
        }
        return Float.parseFloat((String) o);
    }

    /**
     * 获取pageData里面参数
     */
    public Double getDouble(String key) {
        Object o = pageData.get(key);
        if (o == null) {
            return null;
        }
        return Double.parseDouble((String) o);
    }

    /**
     * 获取pageData里面参数
     */
    public Boolean getBoolean(String key) {
        Object o = pageData.get(key);
        if (o == null) {
            return null;
        }
        return Boolean.parseBoolean((String) o);
    }

    /**
     * 获取pageData里面参数
     */
    public Object get(String key) {
        return pageData.get(key);
    }

    /**
     * 构建查询条件
     */
    public QueryWrapper getWrapper() {
        QueryWrapper queryWrapper = new QueryWrapper();
        Iterator<String> iterator = pageData.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = pageData.get(key);
            if (value != null) {
                queryWrapper.like(needToLine ? humpToLine(key) : key, value);
            }
        }
        return queryWrapper;
    }

    /**
     * 设置默认排序方式
     */
    public PageParam setDefaultOrder(String[] ascs, String[] descs) {
        if (arrayIsEmpty(getOrderDescs()) && arrayIsEmpty(getOrderAscs())) {
            addOrderAsc(ascs);
            addOrderDesc(descs);
        }
        return this;
    }

    /**
     * 增加asc排序方式
     */
    public PageParam addOrderAsc(String... ascs) {
        if (ascs != null) {
            List<String> tAscs = new ArrayList<>();
            if (getOrderAscs() != null) {
                tAscs.addAll(arrayToList(getOrderAscs()));
            }
            for (int i = 0; i < ascs.length; i++) {
                if (!tAscs.contains(ascs[i])) {
                    tAscs.add(ascs[i]);
                }
            }
            setOrderAsc(listToArray(tAscs));
        }
        return this;
    }

    /**
     * 增加desc排序方式
     */
    public PageParam addOrderDesc(String... descs) {
        if (descs != null) {
            List<String> tDescs = new ArrayList<>();
            if (getOrderDescs() != null) {
                tDescs.addAll(arrayToList(getOrderDescs()));
            }
            for (int i = 0; i < descs.length; i++) {
                if (!tDescs.contains(descs[i])) {
                    tDescs.add(descs[i]);
                }
            }
            setOrderDesc(listToArray(tDescs));
        }
        return this;
    }

    /**
     * 设置asc排序方式
     */
    public PageParam setOrderAsc(String... ascs) {
        if (ascs != null) {
            List<OrderItem> orderItems = new ArrayList<>();
            for (int i = 0; i < ascs.length; i++) {
                OrderItem orderItem = new OrderItem();
                orderItem.setAsc(true);
                orderItem.setColumn(ascs[i]);
                orderItems.add(orderItem);
            }
            setOrders(orderItems);
        }
        return this;
    }

    /**
     * 设置desc排序方式
     */
    public PageParam setOrderDesc(String... descs) {
        if (descs != null) {
            List<OrderItem> orderItems = new ArrayList<>();
            for (int i = 0; i < descs.length; i++) {
                OrderItem orderItem = new OrderItem();
                orderItem.setAsc(false);
                orderItem.setColumn(descs[i]);
                orderItems.add(orderItem);
            }
            setOrders(orderItems);
        }
        return this;
    }

    /* 驼峰转下划线 */
    private static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /* listToArray */
    private String[] listToArray(List<String> list) {
        String[] strs = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strs[i] = list.get(i);
        }
        return strs;
    }

    /* arrayToList */
    private List<String> arrayToList(String[] array) {
        List<String> strList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            strList.add(array[i]);
        }
        return strList;
    }

    /* 判断数组是否为空 */
    private boolean arrayIsEmpty(String[] array) {
        return (array == null || array.length == 0);
    }

    /**
     * 获取升序排序的字段
     *
     * @return
     */
    public String[] getOrderAscs() {
        List<String> ascs = new ArrayList<>();
        List<OrderItem> orders = getOrders();
        for (OrderItem order : orders) {
            if (order.isAsc()) {
                ascs.add(order.getColumn());
            }
        }
        return listToArray(ascs);
    }

    /**
     * 获取降序排序的字段
     *
     * @return
     */
    public String[] getOrderDescs() {
        List<String> descs = new ArrayList<>();
        List<OrderItem> orders = getOrders();
        for (OrderItem order : orders) {
            if (!order.isAsc()) {
                descs.add(order.getColumn());
            }
        }
        return listToArray(descs);
    }

}
