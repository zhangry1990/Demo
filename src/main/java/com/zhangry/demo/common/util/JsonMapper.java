package com.zhangry.demo.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Lists;
import com.thinvent.common.page.Page;
import com.thinvent.common.page.QueryParameter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Created by zhangry on 2017/3/17.
 */
class JsonMapper {
    private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);
    private ObjectMapper mapper;
    private SimpleDateFormat sdf;

    public JsonMapper() {
        this("yyyy-MM-dd hh:mm:ss");
    }

    public JsonMapper(String customDatePattern) {
        this.sdf = new SimpleDateFormat(customDatePattern);
        this.mapper = new ObjectMapper();
        this.mapper.setDateFormat(this.sdf);
        this.mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        this.mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        this.mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    }

    public String convertToJson(Object object) {
        try {
            return this.mapper.writeValueAsString(object);
        } catch (IOException var3) {
            logger.warn("write to json string error:" + object, var3);
            return null;
        }
    }

    public <T> T convertToObject(String jsonString, Class<T> clazz) {
        if(StringUtils.isEmpty(jsonString)) {
            return null;
        } else {
            try {
                return this.mapper.readValue(jsonString, clazz);
            } catch (IOException var4) {
                logger.warn("parse json string error:" + jsonString, var4);
                return null;
            }
        }
    }

    public <T> T convertToObject(String jsonString, JavaType javaType) {
        if(StringUtils.isEmpty(jsonString)) {
            return null;
        } else {
            try {
                return this.mapper.readValue(jsonString, javaType);
            } catch (IOException var4) {
                logger.warn("parse json string error:" + jsonString, var4);
                return null;
            }
        }
    }

    public JavaType createCollectionType(Class<?> collectionClass, Class... elementClasses) {
        return this.mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public <T> T update(String jsonString, T object) {
        try {
            return this.mapper.readerForUpdating(object).readValue(jsonString);
        } catch (JsonProcessingException var4) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", var4);
        } catch (IOException var5) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", var5);
        }

        return null;
    }

    public void enableEnumUseToString() {
        this.mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        this.mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    }

    public ObjectMapper getMapper() {
        return this.mapper;
    }

    public String convertToJson(Object obj, String... columns) {
        return this.objectToJson(obj, columns);
    }

    private String objectToJson(Object obj, String... columns) {
        if(obj == null) {
            return "";
        } else {
            Page objPage;
            String resultJson;
            if(columns != null && columns.length != 0) {
                if(this.isListObject(obj)) {
                    List<Object> objList = (List)obj;
                    resultJson = this.convertToJson(this.parseToListMap(objList, columns));
                    return resultJson.replace(":\"[", ":[").replace("\\\"", "\"").replace("]\"", "]");
                } else if(this.isSetObject(obj)) {
                    Set<Object> set = (Set)obj;
                    return this.objectToJson(Lists.newArrayList(set), columns);
                } else if(this.isPageObject(obj)) {
                    objPage = (Page)obj;
                    if(objPage.getResult() != null) {
                        objPage.setResult(this.parseToListMap(objPage.getResult(), columns));
                    }

                    resultJson = this.convertToJson(objPage.getResult());
                    return this.processPageToJson(objPage, resultJson);
                } else {
                    Map<Object, Object> objMap = new HashMap();
                    resultJson = null;
                    String[] var5 = columns;
                    int var6 = columns.length;

                    for(int var7 = 0; var7 < var6; ++var7) {
                        String p = var5[var7];
                        Map<String, Object> objValue = this.getValue(obj, p);
                        if(objValue != null) {
                            String key = (String)objValue.keySet().iterator().next();
                            Object objNext = objValue.values().iterator().next();
                            if(objNext == null) {
                                objMap.put(key, "");
                            } else {
                                objMap.put(key, objNext.toString());
                            }
                        }
                    }

                    return this.convertToJson(objMap).replace(":\"[", ":[").replace("\\\"", "\"").replace("]\"", "]");
                }
            } else if(!this.isPageObject(obj)) {
                return this.convertToJson(obj);
            } else {
                objPage = (Page)obj;
                resultJson = this.convertToJson(objPage.getResult());
                return this.processPageToJson(objPage, resultJson);
            }
        }
    }

    private boolean isSetObject(Object obj) {
        boolean result = false;

        try {
            Set<Object> set = (Set)obj;
            result = true;
        } catch (Exception var4) {
            ;
        }

        return result;
    }

    private String processPageToJson(Page objPage, String resultJson) {
        new StringBuilder();
        int totalPages = objPage.getTotalPages();
        int totalCount = objPage.getTotalCount();
        if(totalPages == 0) {
            objPage.setPageNo(0);
        }

        Integer pageSize = Integer.valueOf(objPage.getPageSize());
        if(totalPages == 0 && pageSize != null && pageSize.intValue() != 0) {
            totalPages = totalCount % pageSize.intValue() == 0?totalCount / pageSize.intValue():totalCount / pageSize.intValue() + 1;
        }

        StringBuilder strBuilder = (new StringBuilder("{\"totalpages\":")).append(totalPages).append(",\"currpage\":").append(objPage.getPageNo()).append(",\"totalrecords\":").append(totalCount).append(",\"total\":").append(totalCount).append(", \"result\":").append(resultJson).append("}");
        return strBuilder.toString();
    }

    public String listJsonToPageJson(String listJson, QueryParameter parameter) {
        List<Map<String, Object>> lstTmp = this.convertToListMap(listJson);
        Page page = new Page(parameter.getPageSize(), true);
        page.setPageNo(parameter.getPageNo());
        page.setTotalCount(lstTmp != null?lstTmp.size():0);
        if(lstTmp != null && lstTmp.size() != 0) {
            int startIndex = (page.getPageNo() - 1) * page.getPageSize();
            int endIndex = page.getPageNo() * page.getPageSize();
            if(startIndex < 0) {
                startIndex = 0;
            }

            if(endIndex >= lstTmp.size()) {
                endIndex = lstTmp.size() - 1;
            }

            lstTmp.subList(startIndex, endIndex);
            return this.processPageToJson(page, this.convertToJson(lstTmp));
        } else {
            return this.convertToJson(page);
        }
    }

    private List<Map<Object, Object>> parseToListMap(List<Object> objects, String[] columns) {
        List<Map<Object, Object>> list = new ArrayList(objects.size());
        Iterator var4 = objects.iterator();

        while(var4.hasNext()) {
            Object item = var4.next();
            Map<Object, Object> objMap = new HashMap(columns.length);
            String[] var7 = columns;
            int var8 = columns.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                String column = var7[var9];
                Map<String, Object> objValue = this.getValue(item, column);
                Object tmpValue = "";
                if(objValue.values().iterator().next() != null) {
                    tmpValue = String.valueOf(objValue.values().iterator().next());
                }

                if(!this.checkSpecialType(column)) {
                    objMap.put(objValue.keySet().iterator().next(), tmpValue);
                } else {
                    int index = column.indexOf(".{");
                    String propertyName = column.substring(0, index);
                    objMap.put(propertyName, tmpValue);
                }
            }

            list.add(objMap);
        }

        return list;
    }

    private boolean isListObject(Object obj) {
        boolean result = false;

        try {
            List<Object> lstObj = (List)obj;
            result = true;
        } catch (Exception var4) {
            ;
        }

        return result;
    }

    private boolean isPageObject(Object obj) {
        return obj.getClass().equals(Page.class);
    }

    private Map<String, Object> getValue(Object obj, String fieldName) {
        fieldName = fieldName.trim();
        String tmp;
        String datePattern;
        if(!this.checkSpecialType(fieldName)) {
            Object object = null;

            try {
                if(fieldName.contains(":")) {
                    String[] fieldNameArray = fieldName.split(":");
                    Assert.isTrue(fieldNameArray.length == 2 && !StringUtil.isNullOrEmpty(fieldNameArray[1]));
                    fieldName = fieldNameArray[0];
                    datePattern = fieldNameArray[1];
                    object = PropertyUtils.getProperty(obj, fieldName);
                    Assert.isTrue(object instanceof Date);
                    SimpleDateFormat sdf = new SimpleDateFormat();
                    if(datePattern.equals(JsonMapper.DATATYPE_DATETIME.DATE.name())) {
                        sdf.applyPattern("yyyy-MM-dd");
                    } else if(datePattern.equals(JsonMapper.DATATYPE_DATETIME.TIME.name())) {
                        sdf.applyPattern("HH:mm:ss");
                    } else if(datePattern.equals(JsonMapper.DATATYPE_DATETIME.CUSTOM.name())) {
                        sdf = this.sdf;
                    } else {
                        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
                    }

                    object = sdf.format(object);
                } else {
                    object = PropertyUtils.getProperty(obj, fieldName);
                    if(object instanceof Date) {
                        object = this.sdf.format(object);
                    }
                }
            } catch (Exception var9) {
                logger.warn("field value is null. fieldName = " + fieldName);
            }

            if(object instanceof String) {
                tmp = String.valueOf(object);
                object = tmp.contains("\"")?tmp.replaceAll("\"", "\\\\\\\""):tmp;
            }

            Map<String, Object> rtnObject = new HashMap();
            rtnObject.put(fieldName, object);
            return rtnObject;
        } else {
            int index = fieldName.indexOf(".{");
            tmp = fieldName.substring(0, index);
            datePattern = fieldName.substring(index + 1);
            datePattern = datePattern.substring(1, datePattern.length() - 1);
            Map<String, Object> map = this.getValue(obj, tmp);
            if(map == null) {
                return null;
            } else {
                String[] strParams = datePattern.contains("{")?this.getParams(datePattern):datePattern.split(",");
                Map<String, Object> rtnObject = new HashMap();
                rtnObject.put(map.keySet().iterator().next(), this.convertToJson(map.values().iterator().next(), strParams));
                return rtnObject;
            }
        }
    }

    private String[] getParams(String property) {
        List<String> result = new ArrayList();
        Deque<Character> czDeque = new ArrayDeque();
        StringBuilder tmpResult = new StringBuilder();
        StringBuilder str = new StringBuilder(property);
        int length = str.length();

        for(int i = 0; i < length; ++i) {
            char c = str.charAt(i);
            if(c == 44) {
                if(czDeque.isEmpty()) {
                    result.add(tmpResult.toString());
                    tmpResult.setLength(0);
                } else {
                    tmpResult.append(c);
                }
            } else {
                if(c == 123) {
                    czDeque.addLast(Character.valueOf(c));
                } else if(c == 125) {
                    czDeque.removeLast();
                }

                tmpResult.append(c);
            }

            if(i == length - 1) {
                czDeque = null;
                result.add(tmpResult.toString());
            }
        }

        return (String[])result.toArray(new String[result.size()]);
    }

    private boolean checkSpecialType(String fieldName) {
        return fieldName.contains(".{");
    }

    public List<Map<String, Object>> convertToListMap(String jsonData) {
        if(!jsonData.startsWith("[") && !jsonData.endsWith("]")) {
            jsonData = "[" + jsonData + "]";
        }

        return (List)this.convertToObject(jsonData, this.createCollectionType(List.class, new Class[]{Map.class}));
    }

    public <T> T convertToMap(String jsonData, String[] parameters, boolean isReserved) {
        T result = this.convertToMap(jsonData);
        if(result == null) {
            return null;
        } else if(parameters != null && parameters.length != 0) {
            List<String> lstParameters = new ArrayList();
            String[] var6 = parameters;
            int var7 = parameters.length;

            String k;
            for(int var8 = 0; var8 < var7; ++var8) {
                k = var6[var8];
                lstParameters.add(k);
            }

            Map<String, String> kv = (Map)result;
            List<String> lstRemovedKeys = new ArrayList();
            if(isReserved) {
                Set<String> keys = kv.keySet();
                Assert.isTrue(keys != null && keys.size() > 0);
                Iterator var15 = keys.iterator();

                while(var15.hasNext()) {
                    String k = (String)var15.next();
                    if(!lstParameters.contains(k)) {
                        lstRemovedKeys.add(k);
                    }
                }
            } else {
                lstRemovedKeys = lstParameters;
            }

            if(lstRemovedKeys != null && lstRemovedKeys.size() > 0) {
                Iterator var14 = lstRemovedKeys.iterator();

                while(var14.hasNext()) {
                    k = (String)var14.next();
                    if(kv.containsKey(k)) {
                        kv.remove(k);
                    }
                }
            }

            return kv;
        } else {
            return result;
        }
    }

    public Map<String, Object> convertToMap(String jsonData) {
        return (Map)this.convertToObject(jsonData, this.createCollectionType(Map.class, new Class[]{String.class, Object.class}));
    }

    public static enum DATATYPE_DATETIME {
        FULL,
        DATE,
        TIME,
        CUSTOM;

        private DATATYPE_DATETIME() {
        }
    }
}

