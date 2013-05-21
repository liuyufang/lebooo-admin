<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>处理举报</title>
</head>

<body>
<c:if test="${not empty message}">
    <div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
</c:if>

<table id="contentTable" class="table table-striped table-bordered table-condensed">
    <thead><tr><th>标题</th><th>内容</th><th>时间<th>举报人</th><th>管理</th></tr></thead>
    <tbody>
    <c:forEach items="${spams}" var="spam">
        <tr>
            <td><a href="${ctx}/lebooo/reportspam/detail/${spam.id}">${spam.title}</a></td>
            <td>${spam.content}</td>
            <td>
                <fmt:formatDate value="${spam.date}" pattern="yyyy年MM月dd日  HH时mm分ss秒" />
            </td>
            <td><a href="${ctx}/lebooo/user/detail/${spam.user.id}">${spam.user.name}</a></td>
            <td><a href="${ctx}/admin/user/delete/${user.id}">删除</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
