<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div id="header">
	<div id="title">
	    <shiro:user>
            <h1>后台管理
                <div class="pull-right">
                    你好，<a href="${ctx}/profile"><span class="name"><shiro:principal property="name"/></span></a>
                    <a href="${ctx}/lebooo/addvideo" class="menu">发布视频</a>
                    <a href="${ctx}/logout" class="menu">退出登录</a>
                </div>
            </h1>
            <div class="menubar">
                <a href="${ctx}/lebooo/user">乐播用户</a><a href="${ctx}/lebooo/discover">管理发现</a><a href="${ctx}/lebooo/reportspam">处理举报</a><shiro:hasRole name="admin"><a href="${ctx}/admin/user" >后台账号</a></shiro:hasRole>
            </div>
            <script>
                $(function(){
                    $('.menubar a').each(function(){
                        if($(this).text() == document.title.split(':')[1]){
                            $(this).addClass('active');
                        }
                    });
                });
            </script>
        </shiro:user>
	</div>
</div>