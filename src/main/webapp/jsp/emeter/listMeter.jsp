<%@page import="org.apache.commons.lang3.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
%>
<html>
<head>
    <title>电表展示</title>
    <base href="<%=basePath%>"/>
    <link href="assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
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
                               placeholder="表号">
                    </div>
                    <input type="hidden" id="companyId" name="companyId" value="">
                </div>

                <div class="col-md-3">
                    <div class="input-group" style="z-index:1;">
                        <div class="input-group-addon">类型</div>

                        <select name="meterType" class="prov form-control" style="z-index:1;cursor:pointer;">
                            <option value="">请选择</option>
                            <option value="nb">nb</option>
                            <option value="lora">lora</option>
                            <option value="2g">2g</option>
                        </select>

                    </div>

                </div>

                <div class="col-md-3">
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

                <div class="col-md-2 text-right">
                    <button type="button" class="btn btn-primary btn-sm" title="搜索" onclick="$.query()">
                        <i class="glyphicon glyphicon-search"></i> 搜索
                    </button>
                    <button type="button" class="btn btn-default btn-sm" title="重置" onclick="$.reset()">
                        <i class="glyphicon glyphicon-trash"></i> 重置
                    </button>
                </div>

            </div>

            <div class="row" id="row">

                <div class="col-md-3">

                </div>

                <div class="col-md-3">
                    <input type="file" id="file1" name="files" style="display:none" onchange="compress();"
                           accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet">
                </div>

                <div class="col-md-2">

                </div>

                <div class="col-md-3 text-right">

                    <button type="button" class="btn btn-primary btn-sm" title="下载模板" onclick="export1()">
                        <i class="glyphicon glyphicon-download"></i> 下载模板
                    </button>
                    <button type="button" class="btn btn-primary btn-sm" title="导入电表" onclick="export2()">
                        <i class="glyphicon glyphicon-upwnload"></i> 导入电表
                    </button>
                </div>
            </div>

            <div class="row" id="row">

                <div class="col-md-3">

                </div>

                <div class="col-md-3">

                </div>

                <div class="col-md-2">

                </div>

                <div class="col-md-3 text-right">
                    <button type="button" class="btn btn-primary btn-sm" title="设置电费单价" id='add'
                    ">
                    <i class="glyphicon glyphicon-upwnload"></i> 设置电费单价
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
                    add_url: 'jsp/emeter/price-edit.jsp',
                    edit_url: 'user/detail/',
                    edit_forward_url: 'jsp/security/user-edit',
                    delete_url: 'meter/remove/'
                };
            </script>
            </script>
            <
            div
            id = "toolbar"

            class

            = "btn-group" >

                < /div>
                < table
            id = "table"
            data - toolbar = "#toolbar"
            data - url = "meter/paging"
            data - method = "post"
            data - pagination = "true"
            data - side - pagination = "server"
            data - flat = "true"
            data - query - params = "$.getParams"
            data - id - field = "id"
            data - toggle = "table"
            data - striped = "true"
            data - show - refresh = "false"
            data - show - toggle = "false"
            data - show - columns = "false" >

                < thead
            style = 'display:none' >
                < tr >
                < !-- < th
            data - radio = "true" > < /th> -->
                < th
            data - field = "company"
            data - halign = "left"
            data - align = "left" > 公司 < /th>
                < th
            data - field = "unit"
            data - halign = "left"
            data - align = "left" > 客户 < /th>

                < th
            data - field = "meterNo"
            data - halign = "left"
            data - align = "left" > 表号 < /th>
                < th
            data - field = "imei"
            data - halign = "left"
            data - align = "left" > IMEI < /th>

                < th
            data - field = "meterType"
            data - halign = "left"
            data - align = "left" > 电表类型 < /th>
                < th
            data - field = "newValue"
            data - halign = "left"
            data - align = "left" > 新表读数 < /th>
                < th
            data - field = "oldMeterNo"
            data - halign = "left"
            data - align = "left" > 老表表号 < /th>
                < th
            data - field = "oldValue"
            data - halign = "left"
            data - align = "left" > 老表读数 < /th>
                < th
            data - field = "platform"
            data - halign = "left"
            data - align = "left" > 平台 < /th>
                < th
            data - field = "price"
            data - halign = "left"
            data - align = "left" > 电费单价(元 / 度) < /th>
                < th
            data - field = "status"
            data - halign = "left"
            data - formatter = "show"
            data - align = "left" > 状态 < /th>
                < th
            data - field = "lahezhaStatus"
            data - halign = "left"
            data - formatter = "show1"
            data - align = "left" > 拉合闸状态 < /th>
                < th
            data - field = "createTime"
            data - halign = "left"
            data - align = "left" > 导入时间 < /th>
                < th
            data - formatter = "$.opFormatterDel"
            data - align = "center" > 操作 < /th>
                < /tr>
                < /thead>
                < /table>
                < /div>
                < /div>
                < /div>

                < script >

                function show(value, row, index) {
                    var str = '离线';
                    if (value == 1) str = '在线';
                    return str;
                }

            function show1(value, row, index) {
                var str = '合闸';
                if (value == 0) str = '拉闸';
                return str;
            }

            function export1() {
                //alert($('#companyId').val());
                if (confirm("确定要下载模板吗")) {
                    window.location.href = 'export/downloadMeterImportTemplate';
                }
            }

            function export2() {
                //alert($('#companyId').val());
                document.querySelector('#file1').click();
            }

            function compress() {
                let fileObj = document.getElementById('file1').files[0] //上传文件的对象
                var filepath = $("#file1").val();
                var extStart = filepath.lastIndexOf(".");
                var ext = filepath.substring(extStart, filepath.length).toUpperCase();
                if (ext != ".XLSX") {
                    alert("文件限于xlsx格式");
                    return false;
                }
                var size = fileObj.size / 1024;
                if (size > 2000) {
                    alert('文件大小不能大于2M');
                    return false;
                }
                var formData = new FormData();
                formData.append('file', $('#file1')[0].files[0]); // 固定格式
                formData.append('companyId', $('#companyId').val());
                //alert($('#companyId').val())
                $.ajax({        // ajax前记得加点 . !!!!!!!!!!!!!!!!!
                    url: "meter/saveImport",
                    type: "POST",
                    cache: false,
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function (data) {
                        //alert(data);
                        $.query();
                        alert(data.message)

                    },
                    fail: function (data) {
                        alert("调用失败！")
                    }
                });
            }

            </script>
</body>
</html>
