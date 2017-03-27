package com.zhangry.demo.ssh.dao;

import com.thinvent.common.page.Page;
import com.thinvent.common.page.QueryParameter;
import com.thinvent.data.hibernate.BaseDAO;
import com.zhangry.demo.ssh.entity.User;

import java.util.Map;

/**
 * Created by zhangry on 2017/3/15.
 */
public interface UserDao extends BaseDAO<User, String> {

    Page<User> getUserPage(QueryParameter queryParameter, Map<String, Object> params);

}
