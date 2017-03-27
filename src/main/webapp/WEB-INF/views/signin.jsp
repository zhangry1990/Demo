<%--
  Created by IntelliJ IDEA.
  User: zhangry
  Date: 2017/3/3
  Time: 14:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctxp" value="${pageContext.request.contextPath}"></c:set>
<html>
    <head>
        <title>登录Demo</title>
        <link type="text/css" src="/css/signin.css"/>
    </head>
    <body>
        <div>
            <!-- BEGIN LOGIN FORM -->
            <form class="login-form" action="${ctxp}/userList" method="post">
                <h3>用户登录</h3>
                <div>
                    <label>用户名</label>
                    <div>
                        <i></i>
                        <input type="text" name="username" id="username" size="25" value="" autocomplete="off" placeholder="用户名"/>
                    </div>
                </div>
                <div>
                    <label>密码</label>
                    <div>
                        <input type="password" name="password" id="password" size="25" value="" autocomplete="off" placeholder="密码"/>
                    </div>
                </div>
                <div>
                    <button id="loginBut" type="submit">登录</button>
                </div>
            </form>
            <!-- END LOGIN FORM -->
        </div>
    </body>
</html>
