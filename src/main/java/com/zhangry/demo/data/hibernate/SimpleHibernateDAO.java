package com.zhangry.demo.data.hibernate;

import com.zhangry.demo.common.exception.DAOException;
import com.zhangry.demo.common.page.QueryParameter.Sort;
import com.zhangry.demo.common.util.AssertUtil;
import com.zhangry.demo.common.util.ReflectUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhangry on 2017/3/16.
 */
public class SimpleHibernateDAO<T, Id extends Serializable> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    protected SessionFactory sessionFactory;
    protected Class<T> entityClass = ReflectUtil.getClassGenricType(this.getClass());

    public SimpleHibernateDAO() {
    }

    public Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    public void save(T entity) {
        AssertUtil.notNull(entity, "entity不能为空");

        try {
            this.getSession().saveOrUpdate(entity);
        } catch (HibernateException var3) {
            throw new DAOException(var3);
        }
    }

    public void save(List<T> entityList) {
        AssertUtil.notEmpty(entityList, "entityList不能为空");
        Iterator var2 = entityList.iterator();

        while(var2.hasNext()) {
            T entity = var2.next();
            this.save(entity);
        }

    }

    public void delete(T entity) {
        AssertUtil.notNull(entity, "entity不能为空");

        try {
            this.getSession().delete(entity);
        } catch (HibernateException var3) {
            throw new DAOException(var3);
        }
    }

    public void delete(Collection<T> collection) {
        Iterator var2 = collection.iterator();

        while(var2.hasNext()) {
            T item = var2.next();
            this.delete(item);
        }

    }

    public void delete(Id id) {
        AssertUtil.notNull(id, "id不能为空");
        this.delete(this.get(id));
    }

    public void batchDelete(Id[] ids) {
        this.batchDelete(ids, this.entityClass);
    }

    public void batchDelete(Id[] ids, Class clazz) {
        if(ids != null && ids.length != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("(");
            Serializable[] var4 = ids;
            int var5 = ids.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Id id = var4[var6];
                sb.append("'").append(id).append("',");
            }

            sb.deleteCharAt(sb.length() - 1);
            sb.append(")");
            this.execute("delete from " + clazz.getSimpleName() + " where " + this.getIdName(clazz) + " in " + sb, new Object[0]);
        } else {
            throw new DAOException("ids length == 0.");
        }
    }

    public T get(Id id) {
        AssertUtil.notNull(id, "id不能为空");
        return this.getSession().get(this.entityClass, id);
    }

    public List<T> get(Collection<Id> ids) {
        return this.find(new Criterion[]{Restrictions.in(this.getIdName(), ids)});
    }

    public List<T> find(CharSequence hql, Map<String, Object> namedParameters) {
        return this.createQuery(hql.toString(), namedParameters).list();
    }

    public List<T> findInCache(CharSequence hql, Map<String, Object> namedParameters) {
        return this.createQuery(hql.toString(), namedParameters).setCacheable(true).list();
    }

    public T findUnique(CharSequence hql, Map<String, Object> namedParameters) {
        return this.createQuery(hql.toString(), namedParameters).uniqueResult();
    }

    public T findUniqueInCache(CharSequence hql, Map<String, Object> namedParameters) {
        return this.createQuery(hql.toString(), namedParameters).setCacheable(true).uniqueResult();
    }

    public List<T> find(Map<String, Object> properties, Sort... sorts) {
        Criteria c = this.multiPropertiesAndSortsCriteria(properties, sorts);
        return c.list();
    }

    public List<T> findInCache(Map<String, Object> properties, Sort... sorts) {
        Criteria c = this.multiPropertiesAndSortsCriteria(properties, sorts);
        return c.setCacheable(true).list();
    }

    private Criteria multiPropertiesAndSortsCriteria(Map<String, Object> properties, Sort... sorts) {
        Criteria c = null;
        if(properties == null) {
            c = this.createCriteria(new Criterion[0]);
        } else {
            List<Criterion> criterions = new ArrayList();
            Iterator var5 = properties.keySet().iterator();

            while(var5.hasNext()) {
                String key = (String)var5.next();
                criterions.add(Restrictions.eq(key, properties.get(key)));
            }

            c = this.createCriteria((Criterion[])criterions.toArray(new Criterion[0]));
        }

        Sort[] var8 = sorts;
        int var9 = sorts.length;

        for(int var10 = 0; var10 < var9; ++var10) {
            Sort item = var8[var10];
            if(item.isAsc()) {
                c.addOrder(Order.asc(item.getFieldName()));
            } else {
                c.addOrder(Order.desc(item.getFieldName()));
            }
        }

        return c;
    }

    public T findUnique(Map<String, Object> properties, Sort... sorts) {
        Criteria c = this.multiPropertiesAndSortsCriteria(properties, sorts);
        return c.uniqueResult();
    }

    public T findUniqueInCache(Map<String, Object> properties, Sort... sorts) {
        Criteria c = this.multiPropertiesAndSortsCriteria(properties, sorts);
        return c.setCacheable(true).uniqueResult();
    }

    public int execute(String hql, Object... values) {
        boolean var3 = false;

        try {
            int result = this.createQuery(hql, values).executeUpdate();
            return result;
        } catch (Exception var5) {
            throw new DAOException(var5);
        }
    }

    public int execute(String hql, Map<String, ?> values) {
        boolean var3 = false;

        try {
            int result = this.createQuery(hql, values).executeUpdate();
            return result;
        } catch (Exception var5) {
            throw new DAOException(var5);
        }
    }

    public List<T> find(Criterion... criterions) {
        try {
            return this.createCriteria(criterions).list();
        } catch (Exception var3) {
            throw new DAOException(var3);
        }
    }

    public List<T> findInCache(Criterion... criterions) {
        try {
            return this.createCriteria(criterions).setCacheable(true).list();
        } catch (Exception var3) {
            throw new DAOException(var3);
        }
    }

    public T findUnique(Criterion... criterions) {
        try {
            return this.createCriteria(criterions).uniqueResult();
        } catch (Exception var3) {
            throw new DAOException(var3);
        }
    }

    public T findUniqueInCache(Criterion... criterions) {
        try {
            return this.createCriteria(criterions).setCacheable(true).uniqueResult();
        } catch (Exception var3) {
            throw new DAOException(var3);
        }
    }

    public Criteria createCriteria(Criterion... criterions) {
        Criteria criteria = this.getSession().createCriteria(this.entityClass);
        Criterion[] var3 = criterions;
        int var4 = criterions.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Criterion c = var3[var5];
            criteria.add(c);
        }

        return criteria;
    }

    public void initProxyObject(Object proxy) {
        Hibernate.initialize(proxy);
    }

    public void flush() {
        this.getSession().flush();
    }

    public Query distinct(Query query) {
        query.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return query;
    }

    public Criteria distinct(Criteria criteria) {
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria;
    }

    public String getIdName() {
        ClassMetadata meta = this.sessionFactory.getClassMetadata(this.entityClass);
        return meta.getIdentifierPropertyName();
    }

    public String getIdName(Class clazz) {
        ClassMetadata meta = this.sessionFactory.getClassMetadata(clazz);
        return meta.getIdentifierPropertyName();
    }

    protected boolean isPropertyUnique(String propertyName, Object newValue, Object oldValue) {
        if(newValue != null && !newValue.equals(oldValue)) {
            Map<String, Object> mapParams = new HashMap(1);
            mapParams.put(propertyName, newValue);
            List<T> result = this.find((Map)mapParams, (Sort[])(new Sort[0]));
            return result.size() == 0;
        } else {
            return true;
        }
    }

    protected Query createQuery(String hql, Object... values) {
        AssertUtil.notEmpty(hql, "hql不能为空");
        Query query = this.getSession().createQuery(hql);
        if(values != null) {
            for(int i = 0; i < values.length; ++i) {
                query.setParameter(i, values[i]);
            }
        }

        return query;
    }

    protected Query createQuery(String hql, Map<String, ?> values) {
        AssertUtil.notEmpty(hql, "hql 不能为空");
        Query query = this.getSession().createQuery(hql);
        if(values != null) {
            query.setProperties(values);
        }

        return query;
    }
}
