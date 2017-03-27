<%--
  Created by IntelliJ IDEA.
  User: zhangry
  Date: 2017/3/15
  Time: 14:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctxp" value="${pageContext.request.contextPath}"></c:set>
<html>
<head>
    <link href="${ctxp}/css/bootstrap/bootstrap-table.css" rel="stylesheet">
    <title>用户列表</title>
    <script type="text/javascript" src="${ctxp}/js/jquery/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="${ctxp}/js/bootstrap/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctxp}/js/modules/user.js"></script>
</head>
<body>
    <div id="mainGrid">
        <table id="gridTable"></table>
    </div>
</body>
</html>
