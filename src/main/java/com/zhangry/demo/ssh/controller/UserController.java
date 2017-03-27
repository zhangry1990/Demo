package com.zhangry.demo.ssh.controller;

import com.thinvent.common.exception.ServiceException;
import com.thinvent.common.page.Page;
import com.thinvent.common.page.QueryParameter;
import com.thinvent.web.view.JsonView;
import com.zhangry.demo.ssh.entity.User;
import com.zhangry.demo.ssh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Created by zhangry on 2017/3/15.
 */
@Controller
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/userList", method = RequestMethod.GET)
    public ModelAndView userList() throws Exception {
        ModelAndView model = new ModelAndView();
        model.setViewName("/userList");

        return model;
    }

    @RequestMapping(value = "/gridTable", method = RequestMethod.POST)
    public JsonView getGridData(@RequestBody Map<String, Object> params) throws Exception {
        QueryParameter query = new QueryParameter();
        Page<User> roadFacilityPage = new Page<User>();
        try {
            Map<String, Object> queryMsg = (Map<String, Object>) params.get("queryMsg");
            //设置分页信息
            query.setPageNo(Integer.parseInt(params.get("offset").toString()));
            query.setPageSize(Integer.parseInt(params.get("limit").toString()));
            roadFacilityPage = userService.getUserPage(query, queryMsg);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new Exception("获取道路设施列表失败", e);
        }
        return new JsonView(roadFacilityPage);
    }
}
