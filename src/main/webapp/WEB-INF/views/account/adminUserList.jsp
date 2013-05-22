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
                <a href="#myModal" role="button" class="btn pull-right" data-toggle="modal">+ 添加管理员</a>
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

    <!-- Modal -->
    <div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <form id="inputForm" action="${ctx}/register" method="post" class="form-horizontal">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 id="myModalLabel">添加管理员</h3>
            </div>
            <div class="modal-body">
                <fieldset>
                    <div id="register_error"></div>
                    <div class="control-group">
                        <label for="name" class="control-label">用户名:</label>
                        <div class="controls">
                            <input type="text" id="name" name="name" class="input-large required"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <label for="loginName" class="control-label">登录名:</label>
                        <div class="controls">
                            <input type="text" id="loginName" name="loginName" class="input-large required" minlength="3"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <label for="plainPassword" class="control-label">密码:</label>
                        <div class="controls">
                            <input type="password" id="plainPassword" name="plainPassword" class="input-large required"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <label for="confirmPassword" class="control-label">确认密码:</label>
                        <div class="controls">
                            <input type="password" id="confirmPassword" name="confirmPassword" class="input-large required" equalTo="#plainPassword"/>
                        </div>
                    </div>
                </fieldset>
                <script>
                    $(document).ready(function() {
                        //聚焦第一个输入框
                        $('#myModal').on('shown', function () {
                            $("#name").focus();
                        });
                        //为inputForm注册validate函数
                        $("#inputForm").validate({
                            rules: {
                                loginName: {
                                    remote: "${ctx}/register/checkLoginName"
                                }
                            },
                            messages: {
                                loginName: {
                                    remote: "用户登录名已存在"
                                }
                            },
                            // ajax 提交表单
                            submitHandler: function(){
                                $.ajax({
                                    type: 'post',
                                    url: $('#inputForm')[0].action,
                                    data: $('#inputForm').serialize(),
                                    success: function(){
                                        $('#myModal').modal('hide');
                                        $('#inputForm')[0].reset();
                                        $('#myModal .alert-error').remove();
                                    },
                                    error: function(){
                                        $('#register_error').append('<div class="alert alert-error"><button type="button" class="close" data-dismiss="alert">&times;</button><strong>错误!</strong> 创建失败。</div>');
                                    }
                                });
                            }
                        });
                    });
                </script>
            </div>
            <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>
                <button id="register_submit_btn" type="submit" class="btn btn-primary">确认添加</button>
            </div>
        </form>
    </div>

</body>
</html>
