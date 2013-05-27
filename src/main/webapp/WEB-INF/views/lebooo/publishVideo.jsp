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


<form id="publishVideoForm" action="${ctx}/lebooo/publishvideo" method="post" enctype="multipart/form-data">

    <div class="row-fluid" style="border: 1px solid #797979;">
        <div style="float:left; width: 540px; padding: 1.5em; height: 400px">
            <p style="font-size: 24px;">发布视频</p>
            <div><textarea name="description" maxlength="140" placeholder="添加描述.." style="width:540px;height:100px;"></textarea></div>
            <div style="margin-top: 1em;">视频：<input type="file" name="video" /></div>
            <div style="margin-top: 1em;">截图：<input type="file" name="photo" /></div>
            <div><input type="submit" value="发&nbsp;&nbsp;布" style="margin-top: 1em; padding:10px; width: 5em; background-color: #76D6FF; border: 1px solid #797979; color: #ffffff; font-size: 18px;" /></div>
        </div>
        <div style="float:right; width: 300px; padding: 1.5em; border-left: 1px solid #797979; height: 400px">
            <div>发布到<br/>
                <input name="username" type="text" placeholder="用户名" />
            </div>
            <div>
                <select id="publishTimeOption" name="publishTimeOption">
                    <option value="now">现在发布</option>
                    <option value="schedule">定时发布</option>
                </select>
                <div id="publishDateTimeInputGroup" style="display: none;">
                    <input type="text" name="publishDate" style="width: 10em;" />
                    <input type="text" name="publishTime" style="width: 5em;"/>
                </div>
                <script>
                    $('#publishTimeOption').change(function(){
                        if(this.value == 'schedule'){
                            $('#publishDateTimeInputGroup').show();
                            $('[name=publishDate]', '#publishDateTimeInputGroup').focus();
                            $('#publishVideoForm')[0].action = '${ctx}/lebooo/publishvideo/schedule';
                        }else{
                            $('#publishDateTimeInputGroup').hide();
                            $('#publishVideoForm')[0].action = '${ctx}/lebooo/publishvideo';
                        }
                    });
                </script>
            </div>
        </div>
    </div>
</form>

</body>
</html>