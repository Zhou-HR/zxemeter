<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
    <head>
        <base href="<%=basePath%>"/>
        
        <link href="assets/plugins/formvalidation/css/formValidation.min.css" rel="stylesheet" type="text/css"/>

        <script src="assets/plugins/formvalidation/formValidation.min.js" type="text/javascript"></script>
        <script src="assets/plugins/formvalidation/framework/bootstrap.js" type="text/javascript"></script>
        <script src="assets/plugins/formvalidation/language/zh_CN.js" type="text/javascript"></script>
        <sitemesh:write property='head'/>
    </head>
    <body class="page-header-fixed page-sidebar-closed-hide-logo page-container-bg-solid page-content-white">
        <sitemesh:write property='body'/>
    </body>
</html>
