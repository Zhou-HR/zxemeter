<%@page import="org.apache.commons.lang3.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
%>
<html>
<head>
    <title>运营商电量统计</title>
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
                        <div class="input-group-addon">基站编号</div>
                        <input type="text" id="projectNo" name="projectNo" data-op="contains"
                               class="form-control input-sm" placeholder="基站编号" value="">
                    </div>
                    <input type="hidden" id="companyId" name="companyId" value="">

                </div>

                <div class="col-md-3">
                    <div class="input-group" style="z-index:1;">
                        <div class="input-group-addon">日期</div>
                        <div class="pull-left search connect" style="width: 100px;">
                            <i class="icon-inp"></i>
                            <input type="text" id="date1" name="date1" readonly value="${date1}" class="form-control"
                                   value="" style="width: 100%"/>
                        </div>
                    </div>
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

                        <button type="button" class="btn btn-default btn-sm" title="重置" onclick="reset1()">
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
                   data-url="stat/pagingCarrierValueDay"
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
                    <th data-field="name" data-halign="left" data-align="left">公司/基站</th>
                    <th data-field="yearmonthday" data-halign="left" data-align="left">日期</th>
                    <th data-field="ydValue" data-halign="left" data-align="left">移动电量</th>
                    <th data-field="ydFee" data-halign="left" data-align="left">移动电费</th>
                    <th data-field="dxValue" data-halign="left" data-align="left">电信电量</th>
                    <th data-field="dxFee" data-halign="left" data-align="left">电信电费</th>
                    <th data-field="ltValue" data-halign="left" data-align="left">联通电量</th>
                    <th data-field="ltFee" data-halign="left" data-align="left">联通电费</th>
                    <th data-field="totalValue" data-halign="left" data-align="left">总电量(千瓦时)</th>
                    <th data-field="totalFee" data-halign="left" data-align="left">总电费(元)</th>

                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>

<script>
    var date1 = '${date1}';
    $('#date1').datepicker({
        format: 'yyyy-mm-dd',
        weekStart: 1,
        autoclose: true,
        todayBtn: 'linked',
        language: 'zh-CN'
    });

    function reset1() {
        //alert(1);
        $('#projectNo').val('');
        $('#companyId').val('');
        $('#date1').val(date1);

        $('.caption-subject').html('国动集团');
        $.query();
    }

</script>
</body>
</html>
