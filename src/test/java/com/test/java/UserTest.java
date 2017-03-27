package com.test.java;

import com.thinvent.common.page.Page;
import com.thinvent.common.page.QueryParameter;
import com.zhangry.demo.ssh.entity.User;
import com.zhangry.demo.ssh.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangry on 2017/3/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    private UserService userService;

    @Test
    public void getUserList() {
        QueryParameter queryParameter = new QueryParameter();
        Page<User> userPage = new Page<>();
        Map<String, Object> params = new HashMap<String, Object>();
        queryParameter.setPageNo(1);
        queryParameter.setPageSize(10);
        userPage = userService.getUserPage(queryParameter, params);
        System.err.println(userPage.getTotalCount());
    }
}
