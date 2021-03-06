<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>角色列表</title>
    <link href="assets/plugins/bootstrap-select/css/bootstrap-select.min.css" rel="stylesheet" type="text/css"/>
    <script src="assets/plugins/bootstrap-select/js/bootstrap-select.min.js" type="text/javascript"></script>
    <script src="assets/others/script/ajax-dict-select.js" type="text/javascript"></script>
</head>
<body>
<form id="search-form">
    <div class="box-search width-border">
        <div class="search-default">
            <div class="row">

                <div class="col-md-3">
                    <div class="input-group">
                        <div class="input-group-addon">角色名</div>
                        <input type="text" name="name" data-op="contains" class="form-control input-sm"
                               placeholder="角色名">
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="input-group">
                        <div class="input-group-addon">描述</div>
                        <input type="text" name="description" data-op="contains" class="form-control input-sm"
                               placeholder="描述">
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="input-group">
                        <div class="input-group-addon">状态</div>
                        <select data-dict-url="dict/list/STATUS/STATUS" style="z-index:-1" name="status"
                                data-style="btn-sm" class="form-control input-sm selectpicker" title="请选择"> </select>
                    </div>
                </div>

                <div class="col-md-1">

                </div>

                <div class="search-button">
                    <div class="col-md-2">
                        <button type="button" class="btn btn-primary btn-sm" title="搜索" onclick="$.query()">
                            <i class="glyphicon glyphicon-search"></i> 搜索
                        </button>

                        <button type="button" class="btn btn-default btn-sm" title="重置" onclick="$.reset()">
                            <i class="glyphicon glyphicon-trash"></i> 重置
                        </button>
                    </div>
                </div>

            </div>
        </div>
</form>

<div class="box-body">
    <div class="row">
        <div class="col-md-12">
            <script type="text/javascript">
                var table_setting = {
                    add_url: 'jsp/security/role-edit.jsp',
                    edit_url: 'role/detail/',
                    edit_forward_url: 'jsp/security/role-edit',
                    delete_url: 'role/remove/'
                };
            </script>
            <div id="toolbar" class="btn-group">
                <button id="add" type="button" class="btn btn-default" title="新增">
                    <i class="glyphicon glyphicon-plus"></i>
                </button>
            </div>
            <table id="table"
                   data-toolbar="#toolbar"
                   data-url="role/paging1"
                   data-method="post"
                   data-pagination="true"
                   data-side-pagination="server"
                   data-flat="true"
                   data-query-params="$.getParams"
                   data-id-field="id"
                   data-toggle="table"
                   data-striped="true"
                   data-show-refresh="false"
                   data-show-toggle="false"
                   data-show-columns="false">
                <thead>
                <tr>
                    <th data-field="id" data-visible="false"></th>
                    <th data-field="name">角色名</th>
                    <th data-field="description">描述</th>
                    <th data-field="status.text">状态</th>
                    <th data-formatter="$.opFormatter" data-align="center">操作</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
</body>
</html>