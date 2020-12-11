<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>用户列表</title>
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
                        <div class="input-group-addon">用户名</div>
                        <input type="text" name="name" data-op="contains" class="form-control input-sm"
                               placeholder="用户名">
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="input-group">
                        <div class="input-group-addon">姓名</div>
                        <input type="text" name="realname" data-op="contains" class="form-control input-sm"
                               placeholder="姓名">
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="input-group">
                        <div class="input-group-addon">手机</div>
                        <input type="text" name="phone" class="form-control input-sm" placeholder="手机">
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="input-group">
                        <div class="input-group-addon">部门</div>
                        <input type="text" name="dept" class="form-control input-sm" placeholder="部门">
                    </div>
                </div>


            </div>
        </div>

        <div class="row search-button">
            <div class="col-md-12">
                <button type="button" class="btn btn-primary btn-sm" title="搜索" onclick="$.query()">
                    <i class="glyphicon glyphicon-search"></i> 搜索
                </button>

                <button type="button" class="btn btn-default btn-sm" title="重置" onclick="$.reset()">
                    <i class="glyphicon glyphicon-trash"></i> 重置
                </button>
            </div>
        </div>
    </div>
</form>

<div class="box-body">
    <div class="row">
        <div class="col-md-12">
            <script type="text/javascript">
                var table_setting = {
                    add_url: 'jsp/security/user-edit.jsp',
                    edit_url: 'user/detail/',
                    edit_forward_url: 'jsp/security/user-edit',
                    delete_url: 'user/remove/'
                };
            </script>
            <div id="toolbar" class="btn-group">

            </div>
            <table id="table"
                   data-toolbar="#toolbar"
                   data-url="user/paging1"
                   data-method="post"
                   data-pagination="true"
                   data-side-pagination="server"
                   data-flat="true"
                   data-query-params="$.getParams"
                   data-id-field="id"
                   data-toggle="table"
                   data-striped="true"
                   data-show-refresh="true"
                   data-show-toggle="true"
                   data-show-columns="true">
                <thead>
                <tr>
                    <th data-field="id" data-visible="false"></th>
                    <th data-field="name">用户名</th>
                    <th data-field="realname">姓名</th>
                    <th data-field="phone">手机</th>
                    <th data-field="dept">部门</th>


                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
</body>
<script>
    $.opFormatter1 = function (value, row, index) {
        var op1 = "<button id='b_e_" + row.id + "' type='button' class='btn btn-default btn-xs' title='修改' ><span class='glyphicon glyphicon-edit'></span></button>";
        var op2 = "<button id='b_d_" + row.id + "' type='button' class='btn btn-default btn-xs' title='删除' ><span class='glyphicon glyphicon-remove'></span></button>";
        return op1 + op2;
    };

</script>
</html>