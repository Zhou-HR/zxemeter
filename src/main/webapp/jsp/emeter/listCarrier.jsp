<%@page import="org.apache.commons.lang3.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
%>
<html>
<head>
    <title>运营商比例</title>
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

    <link href="assets/plugins/formvalidation/css/formValidation.min.css" rel="stylesheet" type="text/css"/>

    <script src="assets/plugins/formvalidation/formValidation.js" type="text/javascript"></script>
    <script src="assets/plugins/formvalidation/framework/bootstrap.js" type="text/javascript"></script>
    <script type="text/javascript" src="js/jquery.form.js"></script>
    <script type="text/javascript" src="assets/others/script/validation-ext.js"></script>
    <script src="assets/plugins/formvalidation/language/zh_CN.js" type="text/javascript"></script>


</head>
<body>
<form class="form-inline" action="" id="search-form" style="margin-top: 15px">
    <div class="box-search width-border">
        <div class="search-default">
            <input type="hidden" id="companyId" name="companyId">
            <input type="hidden" id="projectNo" name="projectNo">

            <div class="row" style="margin-top:10px;">
                <div class="col-md-1">

                </div>

                <div class="col-md-3">
                    <div class="input-group">
                        <div class="input-group-addon">运营商电费所占比例（0-100）</div>

                    </div>
                </div>

                <div class="col-md-3">
                    <div class="input-group">
                        <div class="input-group-addon">运营商电费单价(分/度)</div>

                    </div>
                </div>
                <div class="search-button">
                    <div class="col-md-2">
                        <button type="button" class="btn btn-primary btn-sm" title="设置下列基站的比例和单价" onclick="set()">
                            设置下列基站的比例和单价
                        </button>


                    </div>
                </div>
                <div class="search-button">
                    <div class="col-md-2">
                        <button type="button" class="btn btn-primary btn-sm" title="恢复基站的比例和单价默认值"
                                onclick="restoreDefault()">
                            恢复基站的比例和单价默认值
                        </button>


                    </div>
                </div>

            </div>

            <c:forEach var="node" items="${list}" varStatus="status">
                <div class="row " name="div1" style="dispaly:block !important;margin-top:10px;">
                    <div class="col-md-1">
                        <div class="input-group">
                            <input type="checkbox" name="check1" style="width:20px;height:20px;">
                        </div>
                    </div>
                    <input type="hidden" name='name1' value="${node.value}">
                    <div class="col-md-3">
                        <div class="input-group">
                            <div class="input-group-addon">${node.text}比例</div>
                            <input type="text" name="part" value="" data-op="contains" class="form-control input-sm"
                                   placeholder="${node.text}比例0-100"
                                   data-fv-notempty="true" data-fv-stringlength="true" data-fv-stringlength-max="3"
                                   data-fv-digits="true"
                                   data-fv-between-max="100" data-fv-between="true" data-fv-between-min="0"
                            >

                        </div>
                    </div>

                    <div class="col-md-3">
                        <div class="input-group">
                            <div class="input-group-addon">${node.text}单价(分)/度</div>
                            <input type="text" name="price" value="100" data-op="contains" class="form-control input-sm"
                                   placeholder=""
                                   data-fv-notempty="true" data-fv-stringlength="true" data-fv-stringlength-max="5"
                                   data-fv-digits="true"
                                   data-fv-between-max="10000" data-fv-between="true" data-fv-between-min="1"
                            >
                        </div>
                    </div>
                    <c:if test="${status.index==1}">
                        <div class="search-button">
                            <div class="col-md-2">
                                <button type="button" id='showAll1' class="btn btn-primary btn-sm" onclick="showAll()">
                                    显示所有运营商
                                </button>


                            </div>
                        </div>
                    </c:if>
                </div>
            </c:forEach>


        </div>


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

                            <th data-field="carrierName" data-halign="left" data-align="left">运营商</th>
                            <th data-field="carrierPart" data-halign="left" data-align="left">运营商比例</th>
                            <th data-field="carrierPrice" data-halign="left" data-align="left">运营商单价(分)/度</th>

                            <th data-field="address" data-halign="left" data-align="left">地址</th>
                            <th data-field="cityname" data-halign="left" data-align="left">城市</th>
                            <th data-field="unitname" data-halign="left" data-align="left">部门</th>
                            <th data-field="projectDate" data-halign="left" data-align="left">日期</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>

        <script>
            function showAll() {
                console.log($('#showAll1').html());
                if ($('#showAll1').html() == '显示所有运营商') {
                    $("input[name='check1']").each(function (j, item) {
                        var $item = $(item);
                        $item = $item.parent().parent().parent();
                        $item.show();

                    });
                    $('#showAll1').html('只显示三大运营商');
                } else {
                    initCarrier();
                    $('#showAll1').html('显示所有运营商');
                }

            }

            initCarrier();

            function initCarrier() {
                var index = 0;
                $("input[name='check1']").each(function (j, item) {


                    var $item = $(item);
                    $item = $item.parent().parent().parent();
                    if (index > 2) {
                        $item.hide();
                    }

                    index++;
                });
            }


            function set() {
                var arr = new Array();
                var i = 0;
                $("input[name='check1']").each(function (j, item) {
                    //console.log("下标:"+j);
                    //console.log("value值:"+item.checked);
                    if (item.checked) {
                        arr.push(i)
                    }
                    i++;

                });
                if (arr.length == 0) {
                    alert('请先选择一个运营商!');
                    return;
                }

                $("input[name='check1']").each(function (j, item) {
                    if (item.checked) {
                        //arr.push(i)
                    } else {
                        var $item = $(item);
                        //alert($item);
                        $item = $item.parent().parent().parent();
                        $item.remove();
                    }
                    var $item = $(item);
                    $item = $item.parent().parent().parent();
                    $item.addClass('form-group');
                });
                if (!jQuery.isFunction(jQuery.fn.formValidation)) {
                    console.log('formValidation');
                    return;
                }
                var $form = $('#search-form'),
                    fv = $form.data('formValidation');

                fv.validate();
                $("input[name='check1']").each(function (j, item) {

                    var $item = $(item);
                    $item = $item.parent().parent().parent();
                    $item.removeClass('form-group');
                });
                $('#showAll1').hide();
                if (fv.isValid()) {
                    var result = '';
                    $("input[name='name1']").each(function (j, item) {
                        result += item.value + ',';
                    });
                    result = result.substr(0, result.length - 1);
                    result += "|";
                    $("input[name='part']").each(function (j, item) {
                        console.log("下标:" + j);
                        console.log("value值:" + item.value);
                        result += item.value + ',';
                    });
                    var a = confirm("是否确定更改下列基站的运营商比例与单价？");
                    if (!a) {
                        return;
                    }

                    $.ajax({
                        type: 'POST',
                        url: 'station/setCarrier',
                        data: $('#search-form').serialize(),
                        success: function (data) {
                            if (data.message) {
                                alert(data.message);

                            }
                            $.query();
                        }
                    });
                }


            }

            function restoreDefault() {
                var a = confirm("是否确定恢复下列基站的运营商比例与单价默认值？");
                if (!a) {
                    return;
                }

                $.ajax({
                    type: 'POST',
                    url: 'station/restoreDefault',
                    data: $('#search-form').serialize(),
                    success: function (data) {
                        if (data.message) {
                            alert(data.message);

                        }
                        $.query();
                    }
                });
            }
        </script>
</body>
</html>
