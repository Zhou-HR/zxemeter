<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
%>
<html>
<head>
    <title>告警参数设置</title>
    <base href="<%=basePath%>"/>
    <link href="assets/plugins/formvalidation/css/formValidation.min.css" rel="stylesheet" type="text/css"/>

    <script src="assets/plugins/formvalidation/formValidation.js" type="text/javascript"></script>
    <script src="assets/plugins/formvalidation/framework/bootstrap.js" type="text/javascript"></script>
    <script type="text/javascript" src="js/jquery.form.js"></script>
    <script type="text/javascript" src="assets/others/script/validation-ext.js"></script>
    <script src="assets/plugins/formvalidation/language/zh_CN.js" type="text/javascript"></script>
</head>
<body>
<form id="edit-form" class="form-horizontal" action="user/setMeterProperty" method="post"
      data-fv-framework="bootstrap">

    <div class="box-body">
        <div class="form-group">
            <label class="col-sm-2 control-label">告警电压</label>

            <div class="col-sm-3">
                <input type="text" id="maxVoltage" name="maxVoltage" data-op="contains" class="form-control input-sm"
                       data-fv-notempty="false" data-fv-stringlength="true" data-fv-digits="true"
                       value="${maxVoltage}"
                       placeholder="告警电压">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">告警电量</label>

            <div class="col-sm-3">
                <input type="text" id="maxEvalue" name="maxEvalue" data-op="contains" class="form-control input-sm"
                       data-fv-notempty="false" data-fv-stringlength="true" data-fv-digits="true"
                       value="${maxEvalue}"
                       placeholder="告警电量">
            </div>
        </div>


        <div class="form-group">
            <label class="col-sm-2 control-label"></label>

            <div class="col-sm-6">
                <button type="button" class="btn btn-primary btn-sm" title="确定" onclick="submit1();">
                    确定
                </button>
            </div>
        </div>

    </div>
</form>
<script>

    function submit1() {
        fv = $('#edit-form').data('formValidation');
        if ($('#newPassword').val() != $('#repeatPassword').val()) {
            var a = BootstrapDialog.alert("新密码2次输入不一致!");
            setTimeout(function () {
                a.close();
            }, 2000);
            return;
        }
        fv.validate();
        if (fv.isValid()) {
            $('#edit-form').ajaxSubmit({
                success: function (data, status) {
                    if (data.success) {
                        var a = BootstrapDialog.alert(data.message);
                        setTimeout(function () {
                            a.close();
                            //window.location.href='jsp/security/user-change-pwd.jsp';
                            //window.location.href='logout';
                        }, 2000);
                        //$(":password").val('');

                    } else {
                        var a = BootstrapDialog.alert(data.message);
                        setTimeout(function () {
                            a.close();
                        }, 2000);
                    }
                }
            });
        }

    }


</script>

</body>
</html>