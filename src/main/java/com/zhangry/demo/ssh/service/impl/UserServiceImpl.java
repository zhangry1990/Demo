package com.zhangry.demo.ssh.service.impl;

import com.thinvent.common.exception.ServiceException;
import com.thinvent.common.page.Page;
import com.thinvent.common.page.QueryParameter;
import com.thinvent.service.impl.BaseServiceImpl;
import com.zhangry.demo.ssh.dao.UserDao;
import com.zhangry.demo.ssh.entity.User;
import com.zhangry.demo.ssh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by zhangry on 2017/3/15.
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, String> implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public Page<User> getUserPage(QueryParameter queryParameter, Map<String, Object> params) {
        Page<User> page = null;
        try {
            page = userDao.getUserPage(queryParameter, params);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            throw new ServiceException("查询道路设施列表失败！", e);
        }
        //将结果集转换为带分页的JSON
        return page;
    }
}
