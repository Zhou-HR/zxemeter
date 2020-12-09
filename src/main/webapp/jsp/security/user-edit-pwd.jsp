<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>修改密码</title>
        <script type="text/javascript" src="assets/others/script/validation-ext.js"></script>
    </head>
    <body>
        <form id="edit-form" class="form-horizontal" action="user/changePwd" method="post"
                      data-fv-framework="bootstrap">
            <div class="box-search width-border">
                <div class="search-default">
                    <div class="row">
                        <div class="col-md-3">
                            <div class="input-group">
                                <div class="input-group-addon">旧密码</div>
                                <input type="password" name="oldPassword" data-op="contains" class="form-control input-sm" 
								data-fv-notempty="true" data-fv-stringlength="true" data-fv-stringlength-min="6"
								placeholder="旧密码">
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="input-group">
                                <div class="input-group-addon">新密码</div>
                                <input type="password" name="newPassword" data-op="contains" class="form-control input-sm" 
								data-fv-notempty="true" data-fv-stringlength="true" data-fv-stringlength-min="6"
								placeholder="新密码">
                            </div>
                        </div>
						<div class="col-md-3">
                            <div class="input-group">
                                <div class="input-group-addon">确认密码</div>
                                <input type="password" name="repeatPassword" data-op="contains" class="form-control input-sm" 
								data-fv-notempty="true" data-fv-stringlength="true" data-fv-stringlength-min="6"
								placeholder="确认密码">
                            </div>
                        </div>
						
						 <div class="col-md-3">
                            <button type="button" class="btn btn-primary btn-sm" title="确定" onclick="submit1();">
                             确定
							</button>
                        </div>

                    </div>
                </div>

                
            </div>
        </form>
<script>
	
	function submit1(){
		fv = $('#edit-form').data('formValidation');
		fv.validate();
		if (fv.isValid()) {
			$('#edit-form').ajaxSubmit({
				success: function (data, status) {
					if (data.success) 
					{
						var a = BootstrapDialog.alert(data.message);
						setTimeout(function () {
							a.close();
						}, 2000);
						$(":password").val('');
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