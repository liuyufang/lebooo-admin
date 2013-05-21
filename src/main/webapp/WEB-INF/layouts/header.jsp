<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div id="header">
	<div id="title">
	    <shiro:user>
            <h1>后台管理
                <div class="pull-right">
                    <span class="name"><shiro:principal property="name"/></span>
                    <a href="${ctx}/profile" class="menu">发布视频</a>
                    <a href="${ctx}/logout" class="menu">退出登录</a>

                    <!--<ul class="dropdown-menu">
                        <shiro:hasRole name="admin">
                            <li><a href="${ctx}/admin/user">Admin Users</a></li>
                            <li class="divider"></li>
                        </shiro:hasRole>
                        <li><a href="${ctx}/profile">发布视频</a></li>
                        <li><a href="${ctx}/logout">退出登录</a></li>
                    </ul>  -->
                </div>
            </h1>
        </shiro:user>
	</div>
</div>