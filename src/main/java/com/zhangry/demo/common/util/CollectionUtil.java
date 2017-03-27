package com.zhangry.demo.common.util;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * Created by zhangry on 2017/3/17.
 */
public class CollectionUtil {
    private CollectionUtil() {
    }

    public static <T> List<T> filter(List<T> unfiltered, Predicate<? super T> predicate) {
        return Lists.newArrayList(Iterables.filter(unfiltered, predicate));
    }

    public static <T> T find(List<T> list, Predicate<? super T> predicate) {
        return Iterables.find(list, predicate, (Object)null);
    }

    public static Map<String, Object> queryStringToMap(String queryString) {
        AssertUtil.notNull(queryString, "queryString cannot be null.");
        if(queryString.contains("#")) {
            queryString = queryString.substring(0, queryString.indexOf("#"));
        }

        List<String> list = Lists.newArrayList(queryString.split("&"));
        Map<String, Object> result = new HashMap(list.size());
        Iterator var4 = list.iterator();

        while(var4.hasNext()) {
            String item = (String)var4.next();
            String[] keyValuePair = item.split("=");
            result.put(keyValuePair[0], keyValuePair[1]);
        }

        return result;
    }

    public static Map extractToMap(Collection collection, String keyPropertyName, String valuePropertyName) {
        HashMap map = new HashMap(collection.size());

        try {
            Iterator var4 = collection.iterator();

            while(var4.hasNext()) {
                Object obj = var4.next();
                map.put(PropertyUtils.getProperty(obj, keyPropertyName), PropertyUtils.getProperty(obj, valuePropertyName));
            }

            return map;
        } catch (Exception var6) {
            throw ExceptionUtil.unchecked(var6);
        }
    }

    public static <T> List<T> union(Collection<T> a, Collection<T> b) {
        List<T> result = new ArrayList(a);
        result.addAll(b);
        return result;
    }

    public static <T> List<T> subtract(Collection<T> a, Collection<T> b) {
        List<T> list = new ArrayList(a);
        Iterator var3 = b.iterator();

        while(var3.hasNext()) {
            T element = var3.next();
            list.remove(element);
        }

        return list;
    }

    public static <T> List<T> intersection(Collection<T> a, Collection<T> b) {
        List<T> list = new ArrayList();
        Iterator var3 = a.iterator();

        while(var3.hasNext()) {
            T element = var3.next();
            if(b.contains(element)) {
                list.add(element);
            }
        }

        return list;
    }

    public static List<Map<String, Object>> listToTree(List<Map<String, Object>> datas, String idField, String pIdField) {
        if(datas != null && datas.size() != 0) {
            List<Map<String, Object>> rootDatas = new ArrayList(datas.size());
            int length = datas.size();
            Map<String, Object> data = null;

            for(int i = 0; i < length; ++i) {
                data = (Map)datas.get(i);
                if(data.get(pIdField) == null || data.get(pIdField).toString().trim().length() == 0) {
                    rootDatas.add(data);
                    datas.remove(i);
                    --i;
                    --length;
                }
            }

            iterativeData(datas, rootDatas, idField, pIdField);
            return rootDatas;
        } else {
            return datas;
        }
    }

    private static List<Map<String, Object>> listToTree(List<Map<String, Object>> datas, String idField, String pIdField, String nameField, Map<String, String> params, String... includeField) {
        AssertUtil.notNull(params, "params cannot be null, it must contains four parameters: id, pId, children, name . ");
        String targetIdField = (String)params.get("id");
        String targetPIdField = (String)params.get("pId");
        String childrenName = (String)params.get("children");
        String name = (String)params.get("name");
        if(!StringUtil.isNullOrEmpty(targetIdField) && !StringUtil.isNullOrEmpty(targetPIdField) && !StringUtil.isNullOrEmpty(childrenName) && !StringUtil.isNullOrEmpty(name)) {
            List<Map<String, Object>> results = listToTree(datas, idField, pIdField);
            if(targetIdField.equals(idField) && targetPIdField.equals(pIdField) && childrenName.equals("children") && nameField.equals(name)) {
                return results;
            } else {
                iterativeChangeKeys(results, idField, pIdField, nameField, params, includeField);
                return results;
            }
        } else {
            throw new IllegalArgumentException(StringUtil.format("params 的 id, pId, children, name 参数必须同时指定，当前四个参数分别为：{0}, {1}, {2}, {3}", new String[]{targetIdField, targetPIdField, childrenName, name}));
        }
    }

    public static void main(String[] args) {
        List<Map<String, Object>> datas = new ArrayList();
        Map<String, Object> map = new HashMap();
        map.put("ID", "1");
        map.put("PID", (Object)null);
        map.put("NAME", "top");
        map.put("asdf", "top");
        datas.add(map);
        map = new HashMap();
        map.put("ID", "2");
        map.put("PID", "1");
        map.put("NAME", "level1-1");
        datas.add(map);
        map = new HashMap();
        map.put("ID", "3");
        map.put("PID", "1");
        map.put("NAME", "level1-2");
        datas.add(map);
        map = new HashMap();
        map.put("ID", "4");
        map.put("PID", "2");
        map.put("NAME", "level2-1");
        datas.add(map);
        map = new HashMap();
        map.put("ID", "5");
        map.put("PID", "4");
        map.put("NAME", "level3-1");
        datas.add(map);
        System.out.println(listToZtree(datas, "ID", "PID", "NAME", new String[]{"asdf", "NAME"}));
    }

    public static String listToZtree(List<Map<String, Object>> datas, String idField, String pIdField, String nameField, String... includeField) {
        Map<String, String> param = new HashMap(4);
        param.put("id", "id");
        param.put("pId", "pId");
        param.put("children", "children");
        param.put("name", "name");
        List<Map<String, Object>> lists = listToTree(datas, idField, pIdField, nameField, param, includeField);
        return JSON.toJSONString(lists);
    }

    private static void iterativeChangeKeys(List<Map<String, Object>> results, String idField, String pIdField, String nameField, Map<String, String> params, String... includeField) {
        String targetIdField = (String)params.get("id");
        String targetPIdField = (String)params.get("pId");
        String childrenName = (String)params.get("children");
        String targetNameField = (String)params.get("name");
        String[] includeFields = includeField;
        Map<String, Object> includeFieldVal = null;
        Iterator var16 = results.iterator();

        while(var16.hasNext()) {
            Map<String, Object> map = (Map)var16.next();
            Object id = map.get(idField);
            Object pId = map.get(pIdField);
            Object name = map.get(nameField);
            Object children = map.get("children");
            String[] var18;
            int var19;
            int var20;
            String key;
            if(includeFields != null) {
                includeFieldVal = new HashMap(includeFields.length);
                var18 = includeFields;
                var19 = includeFields.length;

                for(var20 = 0; var20 < var19; ++var20) {
                    key = var18[var20];
                    includeFieldVal.put(key, map.get(key));
                }
            }

            map.clear();
            map.put(targetIdField, id);
            map.put(targetPIdField, pId);
            map.put(targetNameField, name);
            if(includeFields != null) {
                var18 = includeFields;
                var19 = includeFields.length;

                for(var20 = 0; var20 < var19; ++var20) {
                    key = var18[var20];
                    map.put(key, includeFieldVal.get(key));
                }
            }

            if(children != null) {
                map.put(childrenName, children);
                iterativeChangeKeys((List)map.get(childrenName), idField, pIdField, nameField, params, includeField);
            }
        }

    }

    private static void iterativeData(List<Map<String, Object>> datas, List<Map<String, Object>> parentDatas, String idField, String parentIdFiled) {
        int length = parentDatas.size();
        Map<String, Object> data = null;

        for(int i = 0; i < length; ++i) {
            data = (Map)parentDatas.get(i);
            List<Map<String, Object>> children = new ArrayList();
            data.put("children", children);
            int len = datas.size();
            Map<String, Object> d = null;

            for(int j = 0; j < len; ++j) {
                d = (Map)datas.get(j);
                if(data.get(idField).equals(d.get(parentIdFiled))) {
                    children.add(d);
                    datas.remove(j);
                    --j;
                    --len;
                }
            }

            iterativeData(datas, children, idField, parentIdFiled);
        }

    }
}

