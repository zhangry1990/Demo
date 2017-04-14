package com.zhangry.demo.data.hibernate;

import com.zhangry.demo.common.exception.DAOException;
import com.zhangry.demo.common.page.Page;
import com.zhangry.demo.common.page.QueryParameter;
import com.zhangry.demo.common.util.AssertUtil;
import com.zhangry.demo.common.util.StringUtil;
import com.zhangry.demo.data.hibernate.*;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.springframework.orm.hibernate5.SessionFactoryUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangry on 2017/3/16.
 */
public class HibernateDAO<T, Id extends Serializable> extends SimpleHibernateDAO<T, Id> implements BaseDAO<T, Id> {
    public HibernateDAO() {
    }

    public Page<T> findPage(QueryParameter queryParameter, String hql, Object... values) {
        return this.findPageInterval(queryParameter, hql, false, values);
    }

    public Page<T> findPageInCache(QueryParameter queryParameter, String hql, Object... values) {
        return this.findPageInterval(queryParameter, hql, true, values);
    }

    private Page<T> findPageInterval(QueryParameter queryParameter, String hql, boolean isCacheable, Object... values) {
        AssertUtil.notNull(queryParameter, "queryParameter不能为空");
        int pageNo = queryParameter.getPageNo();
        int pageSize = queryParameter.getPageSize();
        Page<T> page = new Page(pageSize, true);
        if(pageNo <= 0) {
            pageNo = 1;
        }

        page.setPageNo(pageNo);
        page.setSortList(queryParameter.getSortList());
        if(page.isAutoCount()) {
            long totalCount = this.countHqlResult(hql, values);
            page.setTotalRows((int)totalCount);
            page.setTotalCount((int)totalCount);
        }

        if(queryParameter.getSortList().size() > 0) {
            hql = this.setOrderParameterToHql(hql, queryParameter);
        }

        Query q = this.createQuery(hql, values);
        if(isCacheable) {
            q.setCacheable(true);
        }

        this.setPageParameterToQuery(q, page);
        List result = q.list();
        page.setResult(result);
        return page;
    }

    public Page<T> findPage(QueryParameter queryParameter, String hql, Map<String, ?> values) {
        return this.findPageInterval(queryParameter, hql, false, values);
    }

    public Page<T> findPageInCache(QueryParameter queryParameter, String hql, Map<String, ?> values) {
        return this.findPageInterval(queryParameter, hql, true, values);
    }

    private Page<T> findPageInterval(QueryParameter queryParameter, String hql, boolean isCacheable, Map<String, ?> values) {
        AssertUtil.notNull(queryParameter, "queryParameter不能为空");
        int pageNo = queryParameter.getPageNo();
        int pageSize = queryParameter.getPageSize();
        Page<T> page = new Page(pageSize, true);
        if(pageNo <= 0) {
            pageNo = 1;
        }

        page.setPageNo(pageNo);
        if(page.isAutoCount()) {
            long totalCount = this.countHqlResult(hql, values);
            page.setTotalRows((int)totalCount);
            page.setTotalCount((int)totalCount);
        }

        if(queryParameter.getSortList().size() > 0) {
            hql = this.setOrderParameterToHql(hql, queryParameter);
        }

        Query q = this.createQuery(hql, values);
        this.setPageParameterToQuery(q, page);
        List result;
        if(isCacheable) {
            result = q.setCacheable(true).list();
        } else {
            result = q.list();
        }

        page.setResult(result);
        return page;
    }

    public List<Map<String, Object>> findBySql(String sql, Object... values) {
        try {
            SQLQuery q = this.createSqlQuery(sql, values);
            List<Map<String, Object>> list = q.setResultTransformer(new ResultToMap()).list();
            return list;
        } catch (Exception var5) {
            var5.printStackTrace();
            throw new DAOException(var5);
        }
    }

    public List<Map<String, Object>> findBySql(String sql, Map<String, Object> values) {
        try {
            SQLQuery q = this.createSqlQuery(sql, values);
            return q.setResultTransformer(new ResultToMap()).list();
        } catch (Exception var4) {
            throw new DAOException(var4);
        }
    }

    public Page<T> findPageBySql(QueryParameter queryParameter, String sql, Object... values) {
        return this.findBySqlInternel(queryParameter, sql, values);
    }

    public Page<T> findPageBySql(QueryParameter queryParameter, String sql, Map<String, ?> values) {
        return this.findBySqlInternel(queryParameter, sql, values);
    }

    private Page<T> findBySqlInternel(QueryParameter queryParameter, String sql, Object obj) {
        AssertUtil.notNull(obj);
        boolean isMap = false;
        if(obj instanceof Map) {
            isMap = true;
        }

        try {
            Page<T> page = new Page();
            page.setPageNo(queryParameter.getPageNo());
            page.setPageSize(queryParameter.getPageSize());
            page.setAutoCount(queryParameter.isAutoCount());
            page.setSortList(queryParameter.getSortList());
            if(page.isAutoCount()) {
                try {
                    String totalCountSql = this.prepareCountHql(sql);
                    SQLQuery cq;
                    if(isMap) {
                        cq = this.createSqlQuery(totalCountSql, (Map)obj);
                    } else {
                        cq = this.createSqlQuery(totalCountSql, (Object[])((Object[])obj));
                    }

                    Object o = cq.uniqueResult();
                    Long count = Long.valueOf(0L);
                    if(o instanceof Long) {
                        count = (Long)o;
                    } else if(o instanceof BigDecimal) {
                        BigDecimal b = (BigDecimal)o;
                        count = Long.valueOf(b.longValue());
                    } else if(o instanceof BigInteger) {
                        BigInteger b = (BigInteger)o;
                        count = Long.valueOf(b.longValue());
                    }

                    page.setTotalCount(count.intValue());
                } catch (Exception var11) {
                    throw new DAOException(var11);
                }
            }

            if(page.getPageNo() > page.getTotalPages()) {
                page.setPageNo(1);
            }

            SQLQuery q;
            if(isMap) {
                q = this.createSqlQuery(sql, (Map)obj);
            } else {
                q = this.createSqlQuery(sql, (Object[])((Object[])obj));
            }

            q.addEntity(this.entityClass);
            if(page.isFirstSetted()) {
                q.setFirstResult(page.getFirst());
            }

            if(page.isPageSizeSetted()) {
                q.setMaxResults(page.getPageSize());
            }

            page.setResult(q.list());
            return page;
        } catch (HibernateException var12) {
            throw new DAOException(var12);
        }
    }

    public <T> List<T> findBySql(Class<T> clazz, String sql, Object... values) {
        try {
            return this.createSqlQuery(sql, values).addEntity(clazz).list();
        } catch (Exception var5) {
            throw new DAOException(var5);
        }
    }

    public <T> List<T> findBySql(Class<T> clazz, String sql, Map<String, ?> values) {
        try {
            return this.createSqlQuery(sql, values).addEntity(clazz).list();
        } catch (Exception var5) {
            throw new DAOException(var5);
        }
    }

    public int executeSql(String sql, Object... values) {
        return this.createSqlQuery(sql, values).executeUpdate();
    }

    public int executeSql(String sql, Map<String, ?> values) {
        return this.createSqlQuery(sql, values).executeUpdate();
    }

    public int executeBatchSql(String sql, List<Map<String, Object>> dataList, String... columns) {
        AssertUtil.notEmpty(dataList);
        AssertUtil.notEmpty(columns);
        PreparedStatement pstmt = null;
        Map map = null;

        try {
            pstmt = SessionFactoryUtils.getDataSource(this.sessionFactory).getConnection().prepareStatement(sql);
            Iterator var6 = dataList.iterator();

            while(var6.hasNext()) {
                Map<String, Object> aDataList = (Map)var6.next();
                map = aDataList;
                int j = 1;
                String[] var9 = columns;
                int var10 = columns.length;

                for(int var11 = 0; var11 < var10; ++var11) {
                    String key = var9[var11];
                    pstmt.setObject(j, map.get(key));
                    ++j;
                }

                pstmt.addBatch();
            }

            int[] counts = pstmt.executeBatch();
            this.logger.info("====================当前sql = [" + sql + "] execute counts : " + counts.length + " ===================");
            int var40 = counts.length;
            return var40;
        } catch (Exception var37) {
            this.logger.error("sql 执行失败， 当前sql = [" + sql + "], map = [" + map + "]", var37);
            throw new RuntimeException(var37);
        } finally {
            try {
                if(pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException var35) {
                this.logger.error(var35.getMessage(), var35);
                throw new RuntimeException(var35);
            } finally {
                if(pstmt != null) {
                    pstmt = null;
                }

            }

        }
    }

    public int executeBatchSql(String sql, List<List<String>> dataList) {
        PreparedStatement pstmt = null;
        List data = null;

        try {
            pstmt = SessionFactoryUtils.getDataSource(this.sessionFactory).getConnection().prepareStatement(sql);

            int j;
            for(int index = 0; index < dataList.size(); ++index) {
                data = (List)dataList.get(index);
                j = 1;

                for(Iterator var7 = data.iterator(); var7.hasNext(); ++j) {
                    String value = (String)var7.next();
                    pstmt.setObject(j, value);
                }

                pstmt.addBatch();
            }

            int[] counts = pstmt.executeBatch();
            j = counts.length;
            return j;
        } catch (Exception var33) {
            this.logger.error("sql 执行失败， 当前sql = [" + sql + "], data = [" + data + "]", var33);
            throw new DAOException(var33);
        } finally {
            try {
                if(pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException var31) {
                this.logger.error(var31.getMessage(), var31);
                throw new DAOException(var31);
            } finally {
                if(pstmt != null) {
                    pstmt = null;
                }

            }

        }
    }

    public int executeBatchSql(List<String> sqlList) {
        Statement stmt = null;
        String currentSql = null;

        try {
            stmt = SessionFactoryUtils.getDataSource(this.sessionFactory).getConnection().createStatement();

            for(int index = 0; index < sqlList.size(); ++index) {
                currentSql = (String)sqlList.get(index);
                stmt.addBatch(currentSql);
            }

            int[] counts = stmt.executeBatch();
            int var5 = counts.length;
            return var5;
        } catch (Exception var31) {
            this.logger.error("sql 执行失败， 当前sql = [" + currentSql + "]", var31);
            throw new RuntimeException(var31);
        } finally {
            try {
                if(stmt != null) {
                    stmt.close();
                }
            } catch (SQLException var29) {
                this.logger.error(var29.getMessage(), var29);
                throw new RuntimeException(var29);
            } finally {
                if(stmt != null) {
                    stmt = null;
                }

            }

        }
    }

    private String setOrderParameterToHql(String hql, QueryParameter queryParameter) {
        StringBuilder builder = new StringBuilder(hql);
        builder.append(" ORDER BY ");
        Iterator var4 = queryParameter.getSortList().iterator();

        while(var4.hasNext()) {
            QueryParameter.Sort orderBy = (QueryParameter.Sort)var4.next();
            builder.append(orderBy.toString() + ", ");
        }

        return builder.substring(0, builder.length() - 2);
    }

    private Query setPageParameterToQuery(Query q, Page page) {
        if(page.isFirstSetted()) {
            q.setFirstResult(page.getFirst());
        }

        q.setMaxResults(page.getPageSize());
        return q;
    }

    private Criteria setPageRequestToCriteria(Criteria c, Page page) {
        AssertUtil.isTrue(page.getPageSize() > 0, "Page Size must larger than zero");
        c.setFirstResult(page.getFirst());
        c.setMaxResults(page.getPageSize());
        if(page.isOrderBySetted()) {
            Iterator var3 = page.getSortList().iterator();

            while(var3.hasNext()) {
                QueryParameter.Sort sort = (QueryParameter.Sort)var3.next();
                if(sort.isAsc()) {
                    c.addOrder(Order.asc(sort.getFieldName()));
                } else {
                    c.addOrder(Order.desc(sort.getFieldName()));
                }
            }
        }

        return c;
    }

    private long countHqlResult(String hql, Object... values) {
        String countHql = null;

        try {
            countHql = this.prepareCountHql(hql);
            return ((Long)this.createQuery(countHql, values).uniqueResult()).longValue();
        } catch (Exception var5) {
            throw new DAOException("hql can't be auto count, hql is:" + countHql);
        }
    }

    private long countHqlResult(String hql, Map<String, ?> values) {
        String countHql = null;

        try {
            countHql = this.prepareCountHql(hql);
            return ((Long)this.createQuery(countHql, values).uniqueResult()).longValue();
        } catch (Exception var5) {
            throw new DAOException("hql can't be auto count, hql is:" + countHql);
        }
    }

    private String prepareCountHql(String orgHql) {
        return "select count (*) " + StringUtil.removeSelect(StringUtil.removeOrders(orgHql));
    }

    protected SQLQuery createSqlQuery(String sql, Object... values) {
        SQLQuery sqlQuery = this.getSession().createSQLQuery(sql);
        if(values != null) {
            for(int i = 0; i < values.length; ++i) {
                if(values[i] != null) {
                    sqlQuery.setParameter(i, values[i]);
                }
            }
        }

        return sqlQuery;
    }

    protected SQLQuery createSqlQuery(String sql, Map<String, ?> values) {
        SQLQuery sqlQuery = this.getSession().createSQLQuery(sql);
        if(values != null) {
            sqlQuery.setProperties(values);
        }

        return sqlQuery;
    }
}
