<%@page import="org.apache.commons.lang3.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
%>
<html>
<head>
    <title>国动集团-基站展示</title>
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
    <script src="assets/others/script/jquery.cityselect.js" type="text/javascript"></script>
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
                        <div class="input-group-addon">项目名称</div>
                        <input type="text" name="projectName" data-op="contains" class="form-control input-sm"
                               placeholder="项目名称">
                    </div>
                </div>

                <div class="col-md-2">
                    <div class="input-group">
                        <div class="input-group-addon">安装状态</div>
                        <div>
                            <select name="projectNoStatus" class="prov form-control" style="cursor:pointer;">
                                <option value="">请选择</option>
                                <option value="0">未安装</option>
                                <option value="1">已安装</option>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="col-md-2">
                    <div class="input-group">
                        <div class="input-group-addon">运行状态</div>
                        <div>
                            <select name="status" class="prov form-control" style="cursor:pointer;">
                                <option value="">请选择</option>
                                <option value="0">离线</option>
                                <option value="1">在线</option>
                            </select>
                        </div>
                    </div>
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
                   data-url="station/paging"
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
                    <th data-field="projectNo" data-halign="left" data-align="left">项目编号</th>
                    <th data-field="projectName" data-halign="left" data-align="left">项目名称</th>
                    <th data-field="meterNo1" data-halign="left" data-formatter="detail" data-align="left">表号</th>
                    <th data-field="meterStatus1" data-halign="left" data-formatter="show" data-align="left">状态</th>
                    <th data-field="gdmapJd" data-halign="left" data-align="left">经度</th>
                    <th data-field="gdmapWd" data-halign="left" data-align="left">纬度</th>
                    <th data-field="address" data-halign="left" data-align="left">地址</th>
                    <th data-field="cityname" data-halign="left" data-align="left">城市</th>
                    <th data-field="unitname" data-halign="left" data-align="left">部门</th>

                    <th data-field="projectNo" data-halign="left" data-formatter="map" data-align="left">查看地图</th>
                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>

<script>
    function show(value, row, index) {
        //console.log(value);
        var str = '离线';
        if (!value) {
            return '-';
        }
        if (value == 1) str = '在线';
        return str;
    }

    function detail(value, row, index) {
        return "<a href=nb/toListNB?meterNo=" + value + " style='color:#337ab7;' >" + value + "</a>";
    }

    function map(value, row, index) {
        return "<a href=station/toListMap?projectNo=" + value + " style='color:#337ab7;' >查看地图</a>";
    }
</script>
</body>
</html>
