package com.zhangry.demo.ssh.service;

import com.thinvent.common.page.Page;
import com.thinvent.common.page.QueryParameter;
import com.thinvent.service.BaseService;
import com.zhangry.demo.ssh.entity.User;

import java.util.Map;

/**
 * Created by zhangry on 2017/3/15.
 */
public interface UserService extends BaseService<User, String> {

    Page<User> getUserPage(QueryParameter queryParameter, Map<String, Object> params);
}
