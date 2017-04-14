package com.zhangry.demo.common.util;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.Lists;
import com.zhangry.demo.common.page.QueryParameter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dozer.DozerBeanMapper;

/**
 * Created by zhangry on 2017/3/17.
 */
public class MapperUtil {
    private static DozerBeanMapper dozer = new DozerBeanMapper();
    private static JsonMapper jsonMapper = new JsonMapper();

    private MapperUtil() {
    }

    public static String convertToJson(Object object) {
        return jsonMapper.convertToJson(object);
    }

    public static <T> T converToObject(String jsonString, Class<T> clazz) {
        return jsonMapper.convertToObject(jsonString, clazz);
    }

    public static <T> T convertToObject(String jsonString, JavaType javaType) {
        return jsonMapper.convertToObject(jsonString, javaType);
    }

    public static JavaType createCollectionType(Class<?> collectionClass, Class... elementClasses) {
        return jsonMapper.createCollectionType(collectionClass, elementClasses);
    }

    public static <T> T update(String jsonString, T object) {
        return jsonMapper.update(jsonString, object);
    }

    public static String convertToJson(Object obj, String... columns) {
        return jsonMapper.convertToJson(obj, columns);
    }

    public static String listJsonToPageJson(String listJson, QueryParameter parameter) {
        return jsonMapper.listJsonToPageJson(listJson, parameter);
    }

    public static List<Map<String, Object>> convertToListMap(String jsonData) {
        return jsonMapper.convertToListMap(jsonData);
    }

    public static <T> T convertToMap(String jsonData, String[] parameters, boolean isReserved) {
        return jsonMapper.convertToMap(jsonData, parameters, isReserved);
    }

    public static Map<String, Object> convertToMap(String jsonData) {
        return jsonMapper.convertToMap(jsonData);
    }

    public static <T> T map(Object source, Class<T> destinationClass) {
        return dozer.map(source, destinationClass);
    }

    public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
        List<T> destinationList = Lists.newArrayList();
        Iterator var3 = sourceList.iterator();

        while(var3.hasNext()) {
            Object sourceObject = var3.next();
            T destinationObject = dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }

        return destinationList;
    }

    public static void copy(Object source, Object destinationObject) {
        dozer.map(source, destinationObject);
    }
}

