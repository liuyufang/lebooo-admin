<%@ page import="com.lebooo.admin.web.lebooo.PublishVideoController" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>发布视频</title>
</head>

<body>
<c:if test="${not empty message}">
    <div id="message" class="alert alert-success"><button data-dismiss="alert" class="close">×</button>${message}</div>
</c:if>

<c:if test="${not empty error}">
    <div class="alert alert-error">
        <button class="close" data-dismiss="alert">×</button>${error}.
    </div>
</c:if>


<form action="${ctx}/lebooo/publishvideo" method="post" enctype="multipart/form-data">

    <div><textarea name="description" maxlength="140" placeholder="添加描述.."></textarea></div>
    <div>视频：<input type="file" name="video" /></div>
    <div>截图：<input type="file" name="photo" /></div>
    <div><input type="submit" value="发布" /></div>

    <div>发布到
        <input name="username" type="text" placeholder="用户名" />
    </div>

</form>

</body>
</html>