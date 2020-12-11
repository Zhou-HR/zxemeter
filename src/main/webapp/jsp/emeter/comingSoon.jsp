<%@page import="org.apache.commons.lang3.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
%>
<html>
<head>
    <title>网站建设中</title>
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
<h1>网站建设中...</h1>

<script>
    $.opFormatter1 = function (value, row, index) {
        var op1 = "<button id='b_e_" + row.id + "' type='button' class='btn btn-default btn-xs' title='修改' ><span class='glyphicon glyphicon-edit'></span></button>";
        var op2 = "<button id='b_d_" + row.id + "' type='button' class='btn btn-default btn-xs' title='删除' ><span class='glyphicon glyphicon-remove'></span></button>";
        return op1 + op2;
    };

    function export1() {

        var name = $('#name').val();
        window.location.href = 'company/export1?name=' + encodeURI(encodeURI(name));
    }
</script>
</body>
</html>
