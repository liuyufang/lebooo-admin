<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>后台账号</title>
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>

    <div class="row-fluid no-space">
        <div class="span3 left-nav">
            <!--Sidebar content-->
        </div>
        <div class="span9 right-main">
            <div class="menubar">
                <button class="btn" disabled>启用</button>
                <button class="btn" disabled>禁用</button>
                <button class="btn" disabled>删除</button>
                <button class="btn pull-right">+ 添加管理员</button>
            </div>
            <table id="contentTable" class="table table-striped table-bordered table-condensed">
                <tbody>
                <c:forEach items="${users}" var="user">
                    <tr>
                        <td><input type="checkbox" /></td>
                        <td>${user.name}</td>
                        <td>${user.email}</td>
                        <td>
                            <fmt:formatDate value="${user.registerDate}" pattern="yyyy年MM月dd日  HH时mm分ss秒" />
                        </td>
                        <td>(上次登录ip)</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>


	

</body>
</html>
