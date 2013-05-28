<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>登录页</title>
    <style type="text/css">
        #header {display: none;}
    </style>
</head>

<body>
	<form id="loginForm" action="${ctx}/login" method="post" class="form-horizontal">
	<%
	String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
	if(error != null){
	%>
		<div class="alert alert-error input-medium controls">
			<button class="close" data-dismiss="alert">×</button>登录失败，请重试.
		</div>
	<%
	}
	%>
        <p style="font-size: 2em; margin-bottom: 1.5em">
            <img style="margin-bottom: 0.5em;" src="${ctx}/static/img/login.png" /><br/>
            后台登录
        </p>
		<div>
            <input type="text" id="username" name="username"  value="${username}" class="input-medium required" autocomplete="off" placeholder="账号" autofocus/>
		</div>
		<div>
            <input type="password" id="password" name="password" class="input-medium required" placeholder="密码"/>
		</div>

		<div style="margin-top: 1em;">
            <input id="submit_btn"  type="submit" value="登录"/>
		</div>
	</form>
</body>
</html>
