<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
    <div id="delete_error"></div>

    <div class="row-fluid main">
        <div class="span3 left-nav">
            <ul>
                <shiro:hasRole name="admin"><li onclick="window.location.href='${ctx}/admin/user'">管理员账号<span class="icon-chevron-right pull-right"></span></li></shiro:hasRole>
                <li class="active">机器人<span class="icon-chevron-right pull-right"></span></li>
            </ul>
        </div>
        <div class="span9 right-main">
            <div class="menubar">
                <button id="btn_enable" class="btn" disabled>启用</button>
                <button id="btn_disable" class="btn" disabled>禁用</button>
                <button id="btn_delete" class="btn" disabled>删除</button>
                <a href="#myModal" role="button" class="btn pull-right" data-toggle="modal">+ 添加机器人</a>
            </div>
            <form id="contentForm">
                <table id="contentTable" class="table table-striped table-condensed">
                    <tbody>
                    <c:forEach items="${robots.content}" var="robot">
                        <tr>
                            <td><input type="checkbox" name="id" value="${robot.id}"/></td>
                            <td>${robot.name}<c:if test="${robot.status == 'disabled'}"><span class="status_disabled">(已禁用)</span></c:if></td>
                            <td>${robot.email}</td>
                            <td>
                                <c:if test="${robot.lastLoginDate == null}">
                                    (上次登录时间)
                                </c:if>
                                <c:if test="${robot.lastLoginDate != null}">
                                    <fmt:formatDate value="${robot.lastLoginDate}" pattern="yyyy-MM-dd  HH:mm" />
                                </c:if>
                            </td>
                            <td>${robot.lastLoginIp == null ? "(上次登录ip)" : robot.lastLoginIp}</td>
                            <td style="width:4em;"> <a href="#editRobotModal" role="button" class="btn pull-right" data-toggle="modal" onclick="editRobot(${robot.id});">编辑</a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </form>
            <tags:pagination page="${robots}" paginationSize="5"/>
            <script>
                function showMessage(type, msg){
                    var m = $('<div class="alert alert-'+ type +'"><button type="button" class="close" data-dismiss="alert">&times;</button>'+ msg +'</div>').appendTo('#delete_error');
                    setTimeout(function(){
                        m.remove();
                    }, 1600);
                }
                function addStatusDisabled(checkebox){
                    var tdRobotName = $(checkebox).parent().next();
                    $(tdRobotName).each(function(){
                        if($('.status_disabled', this).length == 0){
                            $(this).append('<span class="status_disabled">(已禁用)</span>');
                        }
                    });
                }
                function removeStatusDisabled(checkebox){
                    var tdRobotName = $(checkebox).parent().next();
                    $('.status_disabled', tdRobotName).remove();
                }
                $(function(){
                    $('#contentTable input[type=checkbox]').click(function(){
                        if($('#contentTable input:checked').length == 0){
                            $('.right-main .menubar button').attr('disabled', true);
                        }else{
                            $('.right-main .menubar button').removeAttr('disabled');
                        }
                    });
                    $('#btn_delete').click(function(){
                        var checked = $('#contentTable input:checked');
                        $.ajax({
                            url: '${ctx}/admin/robot/deleteMulti',
                            data: $('#contentForm').serialize(),
                            success: function(){
                                $(checked).parent().parent().remove();
                            },
                            error: function(){
                                showMessage('error', '<strong>错误!</strong> 删除用户失败。');
                            }
                        });
                    });
                    $('#btn_enable').click(function(){
                        var checked = $('#contentTable input:checked');
                        $.ajax({
                            url: '${ctx}/admin/robot/enableMulti',
                            data: $('#contentForm').serialize(),
                            success: function(){
                                removeStatusDisabled(checked);
                            },
                            error: function(){
                                showMessage('error', '<strong>错误!</strong> 启用用户失败。');
                            }
                        });
                    });
                    $('#btn_disable').click(function(){
                        var checked = $('#contentTable input:checked');
                        $.ajax({
                            url: '${ctx}/admin/robot/disableMulti',
                            data: $('#contentForm').serialize(),
                            success: function(){
                                addStatusDisabled(checked);
                            },
                            error: function(){
                                showMessage('error', '<strong>错误!</strong> 禁用用户失败。</div>');
                            }
                        });
                    });
                });
            </script>
        </div>
    </div>


    <!-- dialog:添加新机器人 -->
    <div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <form id="inputForm" action="${ctx}/register/ajax" method="post" class="form-horizontal">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 id="myModalLabel">添加机器人</h3>
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
                                        showMessage("success", '已创建');
                                        $('#myModal').modal('hide');
                                        $('#inputForm')[0].reset();
                                        $('#myModal .alert-error').remove();
                                    },
                                    error: function(){
                                        $('#register_error').append('<div class="alert alert-error"><button type="button" class="close" data-dismiss="alert">&times;</button><strong>错误!</strong> 创建失败。</div>');
                                    }
                                });
                            }
                        }); // validate
                        // 按回车提交
                        $("#inputForm input").keydown(function(event){
                            if(event.keyCode == 13){
                                $('#inputForm').submit();
                                event.preventDefault();  // 阻止Modal隐藏
                            }
                        });
                    });
                </script>
            </div>
            <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true" onclick="$('#inputForm')[0].reset()">取消</button>
                <button id="register_submit_btn" type="submit" class="btn btn-primary">确认添加</button>
            </div>
        </form>
    </div>



    <!-- dialog:编辑新机器人 -->
    <div id="editRobotModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="editRobotModalLabel" aria-hidden="true">
        <form id="editRobotForm" action="${ctx}/admin/robot/update" method="post" class="form-horizontal">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 id="editRobotModalLabel">编辑机器人</h3>
            </div>
            <div class="modal-body">
                <fieldset>
                    <div id="update_error"></div>
                    <div class="control-group">
                        <input type="hidden" name="id" />
                        <label for="name" class="control-label">用户名:</label>
                        <div class="controls">
                            <input type="text" name="name" class="input-large required"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <label for="loginName" class="control-label">登录名:</label>
                        <div class="controls">
                            <input type="text" name="loginName" class="input-large required" minlength="3"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <label for="plainPassword" class="control-label">密码:</label>
                        <div class="controls">
                            <input type="password" name="plainPassword" class="input-large" placeholder="留空表示不修改"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <label for="confirmPassword" class="control-label">确认密码:</label>
                        <div class="controls">
                            <input type="password" name="confirmPassword" class="input-large" equalTo="#editRobotModal [name=plainPassword]"/>
                        </div>
                    </div>
                </fieldset>
                <script>
                    $(document).ready(function() {
                        var form = $('#editRobotForm')[0];
                        //聚焦第一个输入框
                        $('#editRobotModal').on('shown', function () {
                            form.name.focus();
                        });
                        //为inputForm注册validate函数
                        $(form).validate({
                            // ajax 提交表单
                            submitHandler: function(){
                                $.ajax({
                                    type: 'post',
                                    url: form.action,
                                    data: $(form).serialize(),
                                    success: function(){
                                        $('#editRobotModal').modal('hide');
                                        form.reset();
                                        $('#editRobotModal .alert-error').remove();
                                    },
                                    error: function(){
                                        $('#update_error').append('<div class="alert alert-error"><button type="button" class="close" data-dismiss="alert">&times;</button><strong>错误!</strong> 保存失败。</div>');
                                    }
                                });
                            }
                        }); // validate
                        // 按回车提交
                        $("input", form).keydown(function(event){
                            if(event.keyCode == 13){
                                $(form).submit();
                                event.preventDefault();  // 阻止Modal隐藏
                            }
                        });
                    });
                    function editRobot(id){
                        $.ajax({
                            type: 'GET',
                            url: '${ctx}/admin/robot/' + id,
                            dataType: 'json',
                            success: function(robot){
                                var form = $('#editRobotForm')[0];
                                form.id.value = id;
                                form.name.value = robot.name;
                                form.loginName.value = robot.loginName;
                            }
                        });
                    }
                </script>
            </div>
            <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>
                <button id="update_submit_btn" type="submit" class="btn btn-primary">保存</button>
            </div>
        </form>
    </div>


</body>
</html>
