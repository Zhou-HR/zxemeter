<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>中性智能电表平台</title>
        <sitemesh:write property='head'/>
        <!-- Bootstrap table -->
        <link rel="stylesheet" href="assets/plugins/bootstrap-table/dist/bootstrap-table.css">
    </head>
    <body class="page-header-fixed page-sidebar-closed-hide-logo page-container-bg-solid page-content-white">
        <sitemesh:write property='body'/>
        <script src="assets/plugins/bootstrap-table/dist/bootstrap-table.js" type="text/javascript"></script>
        <script src="assets/plugins/bootstrap-table/dist/locale/bootstrap-table-zh-CN.min.js" type="text/javascript"></script>
        <script src="assets/plugins/bootstrap-table/dist/extensions/flat-json/bootstrap-table-flat-json.js" type="text/javascript"></script>

        <script src="assets/others/script/table-ext.js" type="text/javascript"></script>
        <script src="assets/plugins/jquery.form.js" type="text/javascript"></script>
    </body>
</html>
