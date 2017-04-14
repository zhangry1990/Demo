package com.zhangry.demo.data.hibernate;

import com.zhangry.demo.common.page.Page;
import com.zhangry.demo.common.page.QueryParameter;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangry on 2017/3/16.
 */
public interface BaseDAO<T, Id extends Serializable> {
    Session getSession();

    void save(T var1);

    void save(List<T> var1);

    void delete(T var1);

    void delete(Collection<T> var1);

    void delete(Id var1);

    void batchDelete(Id[] var1);

    void batchDelete(Id[] var1, Class var2);

    T get(Id var1);

    List<T> get(Collection<Id> var1);

    List<T> find(CharSequence var1, Map<String, Object> var2);

    List<T> findInCache(CharSequence var1, Map<String, Object> var2);

    T findUnique(CharSequence var1, Map<String, Object> var2);

    T findUniqueInCache(CharSequence var1, Map<String, Object> var2);

    List<T> find(Map<String, Object> var1, QueryParameter.Sort... var2);

    List<T> findInCache(Map<String, Object> var1, QueryParameter.Sort... var2);

    T findUnique(Map<String, Object> var1, QueryParameter.Sort... var2);

    T findUniqueInCache(Map<String, Object> var1, QueryParameter.Sort... var2);

    int execute(String var1, Object... var2);

    int execute(String var1, Map<String, ?> var2);

    List<T> find(Criterion... var1);

    List<T> findInCache(Criterion... var1);

    T findUnique(Criterion... var1);

    T findUniqueInCache(Criterion... var1);

    Criteria createCriteria(Criterion... var1);

    void initProxyObject(Object var1);

    void flush();

    Query distinct(Query var1);

    Criteria distinct(Criteria var1);

    String getIdName();

    String getIdName(Class var1);

    Page<T> findPage(QueryParameter var1, String var2, Object... var3);

    Page<T> findPageInCache(QueryParameter var1, String var2, Object... var3);

    Page<T> findPage(QueryParameter var1, String var2, Map<String, ?> var3);

    Page<T> findPageInCache(QueryParameter var1, String var2, Map<String, ?> var3);

    Page<T> findPageBySql(QueryParameter var1, String var2, Object... var3);

    Page<T> findPageBySql(QueryParameter var1, String var2, Map<String, ?> var3);

    List<Map<String, Object>> findBySql(String var1, Object... var2);

    List<Map<String, Object>> findBySql(String var1, Map<String, Object> var2);

    <T> List<T> findBySql(Class<T> var1, String var2, Object... var3);

    <T> List<T> findBySql(Class<T> var1, String var2, Map<String, ?> var3);

    int executeSql(String var1, Object... var2);

    int executeSql(String var1, Map<String, ?> var2);

    int executeBatchSql(String var1, List<Map<String, Object>> var2, String... var3);

    int executeBatchSql(String var1, List<List<String>> var2);

    int executeBatchSql(List<String> var1);
}
