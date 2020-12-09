<%@ page language="java" pageEncoding="utf-8" %>
<!DOCTYPE HTML>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
    <head>
        <base href="<%=basePath%>"/>
        <title>正在跳转</title>
        <script type="text/javascript">
            
            location.href = "https://datav.aliyuncs.com/share/51b24355ac8e63c6a5bdc576ef6e3466";
            
        </script>
    </head>
    <body >
        
        <div class="error_middle">
            正在跳转
        </div>
    </body>
</html>