<%@page import="org.apache.commons.lang3.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
%>
<html>
<head>
    <title>电量实时查询</title>
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
                               placeholder="表号">
                    </div>
                    <input type="hidden" id="companyId" name="companyId" value="">
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
                   data-url="down/pagingMeterRealCheck"
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

                    <th data-field="meterNo" data-halign="left" data-align="left">表号</th>
                    <th data-field="meterType" data-halign="left" data-align="left">电表类型</th>
                    <th data-field="imei" data-halign="left" data-align="left">imei</th>


                    <th data-field="meterNo" data-halign="left" data-formatter="getMeterData" data-align="left">查询实时电量
                    </th>


                </tr>
                </thead>
            </table>
        </div>
    </div>
</div>

<script>

    function getMeterData(value, row, index) {
        var str = "<button type='button' onclick=\"downnSend('" + value + "','GetMeterData',this);\" class='btn btn-default btn-xs' title='下发' ><span class='glyphicon glyphicon-edit'></span></button>";
        //alert(str);
        return str;
    }

    var edit = function (href, forward, callback) {
        dialog("电量实时查询", $('<div></div>').load(href + (forward == undefined ? "" : "?forward=" + forward)), callback);
    };

    function downnSend(value, type, item) {

        //item.disabled=true;
        edit('down/editDown/' + value, 'jsp/emeter/meter-GetMeterRealCheck');
        /*
        setTimeout(function () {
            item.disabled='';
        }, 5000);*/
    }

    function dialog(title, message, callback) {
        var dialog = BootstrapDialog.show({
            title: title,
            message: message,
            draggable: true,
            closable: true,
            size: BootstrapDialog.SIZE_WIDE,
            type: BootstrapDialog.TYPE_PRIMARY,
            buttons: [{
                id: 'btn-ok',
                label: '发送',
                cssClass: 'btn-primary',
                action: function (dialogRef) {
                    $('#result').val('');
                    // 如果未引用formValidation.js，下面的方法就无法执行，这里要直接返回
                    if (!jQuery.isFunction(jQuery.fn.formValidation)) {
                        return;
                    }
                    var $form = dialogRef.getModalBody().find("form"),
                        fv = $form.data('formValidation');

                    fv.validate();
                    if (!fv.isValid()) {
                        return;
                    }

                    var $button = this;
                    $button.disable();
                    $button.spin();

                    var i1 = 60;
                    var msg1 = '正在发送，等待返回结果,剩余';
                    var close1 = false;

                    var timer = setInterval(function () {
                        i1--;
                        if (i1 <= 0) {
                            $button.stopSpin();
                            $button.enable();
                            $('#result').val('超时');
                            clearInterval(timer);

                        } else {
                            $('#result').val(msg1 + i1 + '秒');
                        }

                    }, 1000);


                    // 指定时间后，自动启用按钮
                    //setTimeout(function () {
                    //    $button.stopSpin();
                    //    $button.enable();
                    //	clearInterval(timer);
                    //}, 30000);

                    var $form = dialogRef.getModalBody().find("form"),
                        fv = $form.data('formValidation');

                    fv.validate();
                    if (fv.isValid()) {
                        $form.ajaxSubmit({
                            success: function (data, status, $form) {
                                if (data.success) {
                                    $button.stopSpin();
                                    $button.enable();
                                    clearInterval(timer);
                                    var json = JSON.parse(data.message);
                                    $('#result').val('当前电量为；' + json.data + ' (kWh)');

                                } else {
                                    for (var field in data['message']) {
                                        var html = data['message'][field];
                                        if (field == "id") {
                                            html = "主键错误：" + html;
                                        } else {
                                            var label = $("#edit-form *[name='" + field + "']").parent().prev("label").html();
                                            if (label != undefined) {
                                                html = label + "：" + data['message'][field];
                                            }
                                        }
                                        BootstrapDialog.alert(html).setType(BootstrapDialog.TYPE_DANGER);
                                    }
                                }
                            }
                        });
                    }
                }
            }, {
                id: 'btn-cancle',
                label: '关闭',
                cssClass: 'btn-default',
                action: function (dialog) {
                    dialog.close();
                }
            }]
        });

        if ((typeof callback == 'function') && callback.constructor == Function) {
            callback(dialog);
        }
        return dialog;
    };

    //function
</script>
</body>
</html>
