<%@ page language="java" pageEncoding="utf-8" %>
<!DOCTYPE HTML>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<html>
    <head>
        <base href="<%=basePath%>"/>
        <title>会话时间已过期</title>
        <script type="text/javascript">
            function jump(count) {
                window.setTimeout(function () {
                    count--;
                    if (count > 0) {
                        document.getElementById("num").innerHTML = count;
                        jump(count);
                    } else {
                        location.href = "login.jsp";
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
            <div class="img">提示信息</div>
            会话时间已过期，请重新登陆。
            <span style="color:red;" id="num">3</span>
            秒后跳转到登录页面，
            <a href="login.jsp">立即跳转</a>
            。
        </div>
    </body>
</html>