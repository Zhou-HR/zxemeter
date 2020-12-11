<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>编辑用户</title>

    <script src="assets/others/script/ajax-dict-select.js" type="text/javascript"></script>
    <script type="text/javascript" src="assets/others/script/validation-ext.js"></script>
</head>
<body>
<section class="content">
    <div class="box box-primary">
        <form id="edit-form" class="form-horizontal" action="${empty entity? 'user/save' : 'user/edit'}" method="post"
              data-fv-framework="bootstrap">
            <c:if test="${not empty entity}">
                <input type="hidden" name="id" value="${entity.id}"/>
            </c:if>
            <div class="box-body">
                <div class="form-group">
                    <label class="col-sm-2 control-label">用户名</label>
                    <div class="col-sm-6">
                        <input type="text" readonly class="form-control" name="name" value="${entity.name}"
                               data-fv-notempty="true">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">姓名</label>

                    <div class="col-sm-6">
                        <input type="text" readonly class="form-control" name="realname" value="${entity.realname}"
                        >
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">密码</label>

                    <div class="col-sm-6">
                        <c:if test="${not empty entity}">
                            <input type="password" class="form-control" name="password" value="${entity.password}"
                                   data-fv-notempty="true" data-fv-stringlength="true" data-fv-stringlength-min="6">
                        </c:if>
                        <c:if test="${empty entity}">
                            <input type="text" onfocus="this.type='password'" class="form-control" name="password"
                                   value="${entity.password}"
                                   data-fv-notempty="true" data-fv-stringlength="true" data-fv-stringlength-min="6">
                        </c:if>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">部门</label>

                    <div class="col-sm-6">
                        <input type="text" readonly class="form-control" name="dept" value="${entity.dept}"
                        >
                        <input type="hidden" readonly class="form-control" name="status" value="0">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">职位</label>

                    <div class="col-sm-6">
                        <input type="text" readonly class="form-control" name="title" value="${entity.title}"
                        >
                    </div>
                </div>


                <div class="form-group">
                    <label class="col-sm-2 control-label">数据角色</label>

                    <div class="col-sm-6">
                        <select name="roles" data-url="role/list1" data-text-name="description" data-value="${roleIds}"
                                class="form-control selectpicker" multiple title="请选择"
                                data-fv-notempty="true" data-fv-icon="false"> </select>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">菜单角色</label>

                    <div class="col-sm-6">
                        <select name="menuRoles" data-url="roleMenu/list1" data-text-name="description"
                                data-value="${roleMenuIds}" class="form-control selectpicker" multiple title="请选择"
                                data-fv-notempty="true" data-fv-icon="false"> </select>
                    </div>
                </div>

            </div>
        </form>
    </div>
</section>
</body>
</html>
