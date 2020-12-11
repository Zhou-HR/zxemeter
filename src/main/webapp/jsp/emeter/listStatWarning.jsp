<%@page import="org.apache.commons.lang3.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
%>
<html>
<head>
    <title>告警统计</title>
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
                        <div class="input-group-addon">表号</div>
                        <input type="text" name="meterNo" data-op="contains" class="form-control input-sm"
                               placeholder="表号" value="${meterNo}">
                    </div>
                    <input type="hidden" id="companyId" name="companyId" value="">
                    <input type="hidden" id="projectNo" name="projectNo" value="">
                </div>

                <div class="col-md-3">

                </div>

                <div class="col-md-2">

                </div>

                <div class="col-md-2">

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
                   data-url="stat/pagingWarning"
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
                    <th data-field="company" data-halign="left" data-align="left">公司</th>
                    <th data-field="unit" data-halign="left" data-align="left">客户</th>
                    <th data-field="meterNo" data-halign="left" data-formatter="queryMeterNo" data-align="left">表号</th>

                    <th data-field="evalue1" data-halign="left" data-align="left">1月</th>
                    <th data-field="evalue2" data-halign="left" data-align="left">2月</th>
                    <th data-field="evalue3" data-halign="left" data-align="left">3月</th>
                    <th data-field="evalue4" data-halign="left" data-align="left">4月</th>
                    <th data-field="evalue5" data-halign="left" data-align="left">5月</th>
                    <th data-field="evalue6" data-halign="left" data-align="left">6月</th>
                    <th data-field="evalue7" data-halign="left" data-align="left">7月</th>
                    <th data-field="evalue8" data-halign="left" data-align="left">8月</th>
                    <th data-field="evalue9" data-halign="left" data-align="left">9月</th>
                    <th data-field="evalue10" data-halign="left" data-align="left">10月</th>
                    <th data-field="evalue11" data-halign="left" data-align="left">11月</th>
                    <th data-field="evalue12" data-halign="left" data-align="left">12月</th>

                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>

<script>
    $('#menu').hide();

    $('#date1').datepicker({
        format: 'yyyy-mm-dd',
        weekStart: 1,
        autoclose: true,
        todayBtn: 'linked',
        language: 'zh-CN'
    });


    function queryMeterNo(value) {
        return "<a href=nb/toListNB?meterNo=" + value + " style='color:#337ab7;' >" + value + "</a>";
    }

    function detail(value) {
        return "<a href=nb/toListNB?meterNo=" + value + " style='color:#337ab7;' >查看</a>";
    }

    function export1() {

        var name = $('#name').val();
        window.location.href = 'company/export1?name=' + encodeURI(encodeURI(name));
    }
</script>
</body>
</html>
