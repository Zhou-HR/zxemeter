<%@page import="org.apache.commons.lang3.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
%>
<html>
<head>
    <title>电表安装</title>
    <base href="<%=basePath%>"/>
    <link rel="stylesheet" type="text/css" href="css/inner.css"/>
    <link href="assets/plugins/bootstrap-select/css/bootstrap-select.min.css" rel="stylesheet" type="text/css"/>
    <script src="assets/plugins/bootstrap-select/js/bootstrap-select.min.js" type="text/javascript"></script>
    <script src="assets/others/script/ajax-dict-select.js" type="text/javascript"></script>
    <link href="assets/plugins/datepicker/bootstrap-datepicker3.css" rel="stylesheet" type="text/css"/>
    <script src="assets/plugins/datepicker/bootstrap-datepicker.js" type="text/javascript"></script>
    <script src="assets/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="css/base.css"/>
    <link rel="stylesheet" type="text/css" href="css/image.css"/>
    <link href="css/label-choose-menu.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<form class="form-inline" action="" id="search-form" style="margin-top: 15px">
    <div class="box-search width-border">
        <div class="search-default">
            <div class="row">

                <div class="col-md-3">
                    <div class="input-group">
                        <div class="input-group-addon">项目编号</div>
                        <input type="text" id="projectNo" name="projectNo" data-op="contains"
                               class="form-control input-sm" placeholder="项目编号">
                    </div>
                    <input type="hidden" id="companyId" name="companyId" value="">
                </div>

                <div class="col-md-3">
                    <div class="input-group">
                        <div class="input-group-addon">新表号</div>
                        <input type="text" name="newMeterNo" data-op="contains" class="form-control input-sm"
                               placeholder="新表号">
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="input-group">
                        <div class="input-group-addon">旧表号</div>
                        <input type="text" name="oldMeterNo" data-op="contains" class="form-control input-sm"
                               placeholder="旧表号">
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

            <div class="row">

                <div class="col-md-3">
                    <div class="input-group">
                        <div class="input-group-addon">是否使用新表箱</div>
                        <div>
                            <select name="newBox" class="prov form-control" style="cursor:pointer;">
                                <option value="">请选择</option>
                                <option value="1">是</option>
                                <option value="0">否</option>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="input-group">
                        <div class="input-group-addon">是否原国网表</div>
                        <div>
                            <select name="stateGrid" class="prov form-control" style="cursor:pointer;">
                                <option value="">请选择</option>
                                <option value="1">是</option>
                                <option value="0">否</option>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="col-md-2">

                </div>

                <div class="col-md-2">

                </div>


            </div>


        </div>
</form>

<div class="box-body">
    <div class="row">
        <div class="col-md-12">
            <script type="text/javascript">
                var table_setting = {
                    add_url: '',
                    edit_url: '',
                    edit_forward_url: '',
                    delete_url: ''
                };
            </script>
            <div id="toolbar" class="btn-group">

            </div>
            <table id="table"
                   data-toolbar="#toolbar"
                   data-url="meter/pagingMeterInstall"
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

                <thead style='display:none'>
                <tr>
                    <!-- 					<th data-radio="true"></th> -->

                    <th data-field="projCode" data-halign="left" data-align="left">项目编号</th>


                    <th data-field="newMeterNumber" data-halign="left" data-align="left">新表表号</th>

                    <th data-field="newMeterValue" data-halign="left" data-align="left">新表读数</th>
                    <th data-field="oldMeterNumber" data-halign="left" data-align="left">旧表表号</th>
                    <th data-field="oldMeterValue" data-halign="left" data-align="left">旧表读数</th>

                    <th data-field="ifUseNewBox" data-halign="left" data-formatter="dealYesNo" data-align="left">是否新表箱
                    </th>

                    <th data-field="ifStateGrid" data-halign="left" data-formatter="dealYesNo" data-align="left">
                        是否原国网表
                    </th>

                    <th data-field="createTime" data-halign="left" data-formatter="dealTime" data-align="left">安装时间</th>
                    <th data-field="cityname" data-halign="left" data-align="left">城市</th>
                    <th data-field="unitname" data-halign="left" data-align="left">部门</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>

<script>
    $('#installDate1').datepicker({
        format: 'yyyy-mm-dd',
        weekStart: 1,
        autoclose: true,
        todayBtn: 'linked',
        language: 'zh-CN'
    });

    $('#installDate2').datepicker({
        format: 'yyyy-mm-dd',
        weekStart: 1,
        autoclose: true,
        todayBtn: 'linked',
        language: 'zh-CN'
    });

    function dealTime(value, row, index) {
        var str = value.substring(0, 19);

        return str;
    }

    function dealYesNo(value, row, index) {
        if (value == 1)
            return '是';
        return '否';
    }
</script>
</body>
</html>
