<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<% 	
String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort();
%>
<html>
    <head>
        <title>设置告警邮件</title>
		<base href="<%=basePath%>"/>
        <link href="assets/plugins/formvalidation/css/formValidation.min.css" rel="stylesheet" type="text/css"/>

        <script src="assets/plugins/formvalidation/formValidation.js" type="text/javascript"></script>
		<script src="assets/plugins/formvalidation/framework/bootstrap.js" type="text/javascript"></script>
		<script type="text/javascript" src="js/jquery.form.js"></script>
		<script type="text/javascript" src="assets/others/script/validation-ext.js"></script>
		<script src="assets/plugins/formvalidation/language/zh_CN.js" type="text/javascript"></script>
    </head>
    <body>
        <form id="edit-form" class="form-horizontal" action="user/setEmail" method="post"
                      data-fv-framework="bootstrap">
            
			<div class="box-body">
				<div class="form-group">
					<label class="col-sm-2 control-label">EMAIL</label>
					<div class="col-sm-6">
						<input type="text" name="email" data-op="contains" class="form-control input-sm" 
						data-fv-emailaddress="true" data-fv-emailaddress-multiple="true" value="${user.email}"
						placeholder="EMAIL">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">每日定时（优先）</label>

					<div class="col-sm-3">
						<input type="text" id="hour"  name="hour" data-op="contains" class="form-control input-sm" 
						data-fv-notempty="false" data-fv-stringlength="true" data-fv-stringlength-max="2" data-fv-digits="true"
						data-fv-between-max="23" data-fv-between="true" data-fv-between-min="0" value="${user.hour}"
						placeholder="整点 数字 例如：9">
					</div>
					
					
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">时间间隔(小时)</label>

					<div class="col-sm-3">
						<select name="everyHour" id="everyHour" class="prov form-control" style="z-index:1;cursor:pointer;"><option value="">请选择</option>
						<option value="1">1小时</option><option value="2">2小时</option><option value="3">3小时</option>
						<option value="4">4小时</option><option value="6">6小时</option><option value="8">8小时</option>
						<option value="12">12小时</option>
						</select> 	
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
	var everyHour='${user.everyHour}';
	if(everyHour!=''){
		 $("#everyHour").val(${user.everyHour});
	}
	function submit1(){
		fv = $('#edit-form').data('formValidation');
		if($('#newPassword').val()!=$('#repeatPassword').val()){
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
					if (data.success) 
					{
						var a = BootstrapDialog.alert(data.message);
						setTimeout(function () {
							a.close();
							//window.location.href='jsp/security/user-change-pwd.jsp';
							//window.location.href='logout';
						}, 2000);
						//$(":password").val('');
						
					}else{
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