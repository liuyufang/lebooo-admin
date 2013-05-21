<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>后台账号</title>
</head>

<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
	</c:if>

    <div class="row-fluid main">
        <div class="span3 left-nav">
            <ul>
                <li class="active">管理员账号<span class="icon-chevron-right pull-right"></span></li>
                <li>机器人<span class="icon-chevron-right pull-right"></span></li>
            </ul>
        </div>
        <div class="span9 right-main">
            <div class="menubar">
                <button class="btn" disabled>启用</button>
                <button class="btn" disabled>禁用</button>
                <button class="btn" disabled>删除</button>
                <button class="btn pull-right">+ 添加管理员</button>
            </div>
            <table id="contentTable" class="table table-striped table-condensed">
                <tbody>
                <c:forEach items="${users.content}" var="user">
                    <tr>
                        <td><input type="checkbox" /></td>
                        <td>${user.name}</td>
                        <td>${user.email}</td>
                        <td>
                            <fmt:formatDate value="${user.registerDate}" pattern="yyyy-MM-dd  HH:mm" />
                        </td>
                        <td>(上次登录ip)</td>
                        <td style="width:4em;"><button class="btn">编辑</button></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <tags:pagination page="${users}" paginationSize="3"/>
        </div>
    </div>


	

</body>
</html>
