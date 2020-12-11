<%@page import="org.apache.commons.lang3.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
%>
<html>
<head>
    <title>电量监控</title>
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
                        <input type="text" name="meterNo" value="${meterNo}" data-op="contains"
                               class="form-control input-sm" placeholder="表号">
                    </div>
                    <input type="hidden" id="companyId" name="companyId" value="">

                </div>


                <div class="col-md-3">
                    <div class="input-group" style="z-index:1;">
                        <div class="input-group-addon">数据来源</div>
                        <div>
                            <select id="source" name="source" class="prov form-control"
                                    style="z-index:1;cursor:pointer;">
                                <option value="">请选择</option>

                                <option value="lora">lora</option>
                                <option value="nb">nb</option>
                                <option value="mqtt_2g">mqtt_2g</option>


                            </select>
                        </div>
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="input-group">
                        <div class="input-group-addon" style="height:28px;">发包时间</div>
                        <div class="pull-left search connect">
                            <i class="icon-inp"></i>
                            <input type="text" id="installDate1" name="installDate1" value="${installDate1}"
                                   class="form-control" value="" style="width: 100%"/>
                        </div>
                    </div>
                </div>

                <div class="col-md-1">
                    <div class="input-group">
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
                   data-url="nb/paging"
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
                    <th data-field="meterNo" data-halign="left" data-align="left" data-formatter="queryMeterNo">表号</th>

                    <th data-field="evalue" data-halign="left" data-align="left">电量</th>
                    <th data-field="ecurrent" data-formatter="getCurrent" data-halign="left" data-align="left">电流</th>
                    <th data-field="evoltage" data-formatter="getVoltage" data-halign="left" data-align="left">电压</th>
                    <th data-field="esignal" data-halign="left" data-align="left">信号强度</th>
                    <th data-field="eseq" data-halign="left" data-align="left">上报序号</th>
                    <th data-field="source" data-halign="left" data-align="left">数据来源</th>
                    <th data-field="platform" data-halign="left" data-align="left">平台</th>

                    <th data-field="collectTime" data-formatter="getTime" data-halign="left" data-align="left">时间</th>
                    <th data-field="" data-halign="left" data-formatter="show" data-align="left">查看原始数据</th>

                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>

<script>
    $("body").bind('keyup', function (event) {
        if (event.keyCode == 13) {
            $.query();
        }
    });

    $('#installDate1').datepicker({
        format: 'yyyy-mm-dd',
        weekStart: 1,
        autoclose: true,
        todayBtn: 'linked',
        language: 'zh-CN'
    });

    function getCurrent(value, row, index) {
        //console.log(row.evoltageb);
        if (row.evoltageb) {
            return 'A相:' + row.ecurrenta + '<br>' + 'B相:' + row.ecurrentb + '<br>' + 'C相:' + row.ecurrentc;
        }
        return row.ecurrent;
    }

    function getVoltage(value, row, index) {
        //console.log(row.evoltageb);
        if (row.evoltageb) {
            return 'A相:' + row.evoltagea + '<br>' + 'B相:' + row.evoltageb + '<br>' + 'C相:' + row.evoltagec;
        }
        return row.evoltage;
    }

    function getSource(value, row, index) {
        console.log(row.evoltageb);

        if (row.evoltageb || row.ecurrentb) {
            return "nb三相";
        }
        return "nb单相";
        /*
        var str=$('#source').find("option:selected").text();
        //alert(str);
        if(str=='请选择'){str='nb单相';}
        return str;*/
    }

    function queryMeterNo(value) {
        return "<a href=nb/toListNB?meterNo=" + value + " style='color:#337ab7;' >" + value + "</a>";
    }

    function show(value, row, index) {
        return "<a onclick=\"showValue(" + index + ",'" + row.origValue
            + "');\">查看原始数据<a>";
    }

    function showValue(index, origValue) {
        //alert($('#origValue'));
        index++;
        if ($('#origValue').length > 0) {

            if ($('#origValue').hasClass(index)) {
                $('#origValue').remove();
                return;
            }
            $('#origValue').remove();
        }
        //alert(index);
        //alert(tr.length);
        var tr = "<tr id='origValue' class='" + index + "'><td colspan='11'>" + origValue + "</td></tr>";
        //$("table").append(tr);
        $("#table tr:eq(" + index + ")").after(tr);
    }

    function getTime(value) {
        var date = new Date(value * 1000);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        m = m < 10 ? ('0' + m) : m;
        var d = date.getDate();
        d = d < 10 ? ('0' + d) : d;
        var h = date.getHours();
        h = h < 10 ? ('0' + h) : h;
        var minute = date.getMinutes();
        var second = date.getSeconds();
        minute = minute < 10 ? ('0' + minute) : minute;
        second = second < 10 ? ('0' + second) : second;
        return y + '-' + m + '-' + d + ' ' + ' ' + h + ':' + minute + ':' + second;
    }

    function export1() {

        var name = $('#name').val();
        window.location.href = 'company/export1?name=' + encodeURI(encodeURI(name));
    }
</script>
</body>
</html>
