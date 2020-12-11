<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html style="height:100%;">
<!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <meta charset="utf-8"/>
    <title>中性智能电表平台</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1" name="viewport"/>
    <!-- BEGIN GLOBAL MANDATORY STYLES -->
    <link href="assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
    <link href="assets/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css"/>
    <link href="assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
    <!-- END GLOBAL MANDATORY STYLES -->
    <!-- BEGIN THEME GLOBAL STYLES -->
    <link href="assets/apps/css/components.min.css" rel="stylesheet" type="text/css"/>
    <link href="assets/apps/css/plugins.min.css" rel="stylesheet" type="text/css"/>
    <!-- END THEME GLOBAL STYLES -->
    <!-- BEGIN PAGE LEVEL STYLES -->
    <link href="assets/apps/css/login.css" rel="stylesheet" type="text/css"/>
    <!-- END PAGE LEVEL STYLES -->
    <style>
        .login {
            display: flex;
            flex-direction: column;
            overflow: hidden;
        }

        .login .top, .login .bigContent {
            width: 100%;
        }

        .login .bigContent {
            flex: 1;
            background: url('images/loginBg.png');
            background-size: cover;
            background-position: 50%;

            display: flex;
            align-items: center;
            justify-content: flex-end;

        }

        .form-group {
            margin-bottom: 40px;
        }


    </style>
</head>
<!-- END HEAD -->

<body class="login">

<div class="top">
    <!-- https://shnbbucket.oss-cn-beijing.aliyuncs.com/20190505104115.png -->
    <img class="logoImg" src="images/topimg.png" alt="">
</div>
<div class="bigContent">
    <!-- BEGIN LOGIN -->
    <div class="content">
        <!-- BEGIN LOGIN FORM -->
        <form class="login-form" action="login" method="post" style="">
            <div class="alert alert-danger display-hide">
                <button type="button" onclick="clearError()" class="close" data-close="alert"></button>
                <span></span>
            </div>
            <div class="form-group">
                <!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
                <i class="fa fa-user"></i>
                <input class="form-control form-control-solid placeholder-no-fix" type="text" autocomplete="off"
                       placeholder="用户名" name="userName"/></div>
            <div class="form-group">
                <i class="fa fa-lock"></i>
                <input class="form-control form-control-solid placeholder-no-fix" type="password" autocomplete="off"
                       placeholder="密码" name="password"/>
                <input type="checkbox" id="remember-me">Remember me
            </div>
            <div class="form-actions">
                <button style="border-radius: 4px!important;" onclick='valid()' type="button"
                        class="btn dark-blue btn-block uppercase">登录
                </button>
            </div>
            <div class="message" style="margin-top: 10px;">
                <c:if test="${SPRING_SECURITY_LAST_EXCEPTION.message=='Bad credentials'}">
                    <span style="color:#F00">用户名或密码错误!</span>
                </c:if>
                <c:if test="${SPRING_SECURITY_LAST_EXCEPTION.message!='Bad credentials'}">
                    <span style="color:#F00">${SPRING_SECURITY_LAST_EXCEPTION.message}</span>
                </c:if>
                <%session.removeAttribute("SPRING_SECURITY_LAST_EXCEPTION");%>
            </div>
        </form>
        <!-- END LOGIN FORM -->
    </div>
    <!-- END LOGIN -->
</div>

<!-- BEGIN CORE PLUGINS -->
<script src="assets/plugins/jquery.min.js" type="text/javascript"></script>
<script src="assets/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<!-- END CORE PLUGINS -->
<script>
    var ls = localStorage;
    ls.setItem('index', '');

    $(function () {
        if (window.self != window.top) {
            window.top.location.href = "login.jsp";
        }
        var rememberme = ls.getItem('rememberme');
        //alert(rememberme);
        if (rememberme == '1') {
            $("#remember-me").attr("checked", true);
            var name = ls.getItem('name');
            var pwd = ls.getItem('pwd');
            $('input[name=userName]').val(name);
            $('input[name=password]').val(pwd);
        }

    });

    $("body").bind('keyup', function (event) {
        if (event.keyCode == 13) {
            valid();
        }
    });

    function clearError() {
        $('.alert-danger>span').html('');
        $('.alert-danger').addClass("display-hide");
    }

    function showError(message) {
        $('.alert-danger>span').html(message);
        $('.alert-danger').removeClass("display-hide");
    }

    function valid() {
        //remember-me
        if ($("#remember-me").prop("checked")) {

            //alert(ls);
            ls.setItem('name', $('input[name=userName]').val());
            ls.setItem('pwd', $('input[name=password]').val());
            ls.setItem('rememberme', '1');
            //alert(ls.length);

        } else {
            ls.setItem('name', '');
            ls.setItem('pwd', '');
            ls.setItem('rememberme', '0');
        }

        //alert($("#remember-me").prop("checked"));
        clearError();
        var $user = $('input[name=userName]');
        if ($user.val() == '' || $user.val() == null || $user.val() == undefined) {
            showError('请输入账号');
            return;
        }

        var $password = $('input[name=password]');
        if ($password.val() == '' || $password.val() == null || $password.val() == undefined) {
            showError('请输入密码');
            return;
        }

        $("form").submit();
    }


</script>
</body>
</html>
