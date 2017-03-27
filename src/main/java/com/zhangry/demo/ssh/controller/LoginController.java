package com.zhangry.demo.ssh.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by zhangry on 2017/3/15.
 */
@Controller
public class LoginController extends BaseController {


    @RequestMapping(value = "/signin", method = RequestMethod.GET)
    public String signin() {

        return "signin";
    }

}
