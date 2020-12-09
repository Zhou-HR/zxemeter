<%@ page language="java" pageEncoding="utf-8" %>
<!DOCTYPE HTML>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
    <head>
        <base href="<%=basePath%>"/>
        <title>请联系管理员授权!</title>
        <script type="text/javascript">
            function jump(count) {
                window.setTimeout(function () {
                    count--;
                    if (count > 0) {
                        document.getElementById("num").innerHTML = count;
                        jump(count);
                    } else {
                        //location.href = "login.jsp";
						window.history.back();
                    }
                }, 1000);
            }
        </script>
    </head>
    <body onload="jump(3)">
        <script type="text/javascript">
            if (window.self != window.top) {
                window.top.location.href = "errors/session-time-out.jsp";
            }
        </script>
        <div class="error_middle">
            <div class="img">提示信息：没有访问当前页面的权限!</div>
            请联系管理员授权!
            <span style="color:red;" id="num">3</span>
            秒后返回原页面，
            <a href="javascript:void(0)" onclick="window.history.back();">立即跳转</a>
            。
        </div>
    </body>
</html>