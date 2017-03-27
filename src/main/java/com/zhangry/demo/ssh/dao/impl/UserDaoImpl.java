package com.zhangry.demo.ssh.dao.impl;

import com.thinvent.common.page.Page;
import com.thinvent.common.page.QueryParameter;
import com.thinvent.data.hibernate.HibernateDAO;
import com.zhangry.demo.ssh.dao.UserDao;
import com.zhangry.demo.ssh.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangry on 2017/3/15.
 */
@Repository
public class UserDaoImpl extends HibernateDAO<User, String> implements UserDao {

    @Override
    public Page<User> getUserPage(QueryParameter queryParameter, Map<String, Object> params) {
        //防止params为空
        if (params == null) {
            params = new HashMap<>();
        }
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(" SELECT * FROM DEMO_USER WHERE 1 = 1 ");
        if(params != null && params.size() != 0) {
            // 设施类型
            if (params.get("facilityType") != null && StringUtils.isNotBlank(String.valueOf(params.get("facilityType")))) {
                sqlBuilder.append(" AND FACILITY_TYPE = :facilityType");
            }
            // 设施名称
            if (params.get("facilityName") != null && StringUtils.isNotBlank(String.valueOf(params.get("facilityName")))) {
                sqlBuilder.append(" AND FACILITY_NAME = :facilityName");
            }
        }
        // 按创建时间降序排序
        sqlBuilder.append(" ORDER BY AGE DESC ");

        return this.findPageBySql(queryParameter, sqlBuilder.toString(), params);
    }
}
