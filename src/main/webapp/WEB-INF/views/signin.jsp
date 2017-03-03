<%--
  Created by IntelliJ IDEA.
  User: zhangry
  Date: 2017/3/3
  Time: 14:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>登录Demo</title>
    <link type="text/css" src="/css/signin.css"/>
</head>
<body>
<div>
    <!-- BEGIN LOGIN FORM -->
    <form class="login-form" action="rest/user/login" method="post">
        <h3 class="form-title">用户登录</h3>
        <div class="alert alert-danger display-hide">
            <button class="close" data-close="alert"></button>
            <span>
				 输入您的用户名和密码
			</span>
        </div>
        <div class="form-group">
            <!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
            <label class="control-label visible-ie8 visible-ie9">用户名</label>
            <div class="input-icon">
                <i class="fa fa-user"></i>
                <input name="username" id="username" size="25" value="starzou" class="form-control placeholder-no-fix"
                       type="text" autocomplete="off" placeholder="用户名"/>
            </div>
        </div>
        <div class="form-group">
            <label class="control-label visible-ie8 visible-ie9">密码</label>
            <div class="input-icon">
                <i class="fa fa-lock"></i>
                <input name="password" id="password" size="25" value="123456" class="form-control placeholder-no-fix"
                       type="password" autocomplete="off" placeholder="密码"/>
            </div>
        </div>
        <div class="form-actions">
            <label class="checkbox">
                <input type="checkbox" name="remember" value="1" checked="checked"/> 记住我 </label>
            <button id="loginBut" type="submit" class="btn blue pull-right">
                登录 <i class="m-icon-swapright m-icon-white"></i>
            </button>
        </div>
        <div class="forget-password">
            <h4>忘记密码 ?</h4>
            <p>点击 <a href="javascript:;" id="forget-password">这里</a> 重置您的密码.
            </p>
        </div>
        <div class="create-account">
            <p>
                还没有账号 ?&nbsp; <a href="javascript:;" id="register-btn">创建一个账号</a>
            </p>
        </div>
    </form>
    <!-- END LOGIN FORM -->
</div>
</body>
</html>
