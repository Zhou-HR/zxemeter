<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<% 	
String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort();
%>
<html>
    <head>
        <title>设置电费单价</title>
		<base href="<%=basePath%>"/>
        <link href="assets/plugins/formvalidation/css/formValidation.min.css" rel="stylesheet" type="text/css"/>

        <script src="assets/plugins/formvalidation/formValidation.js" type="text/javascript"></script>
		<script src="assets/plugins/formvalidation/framework/bootstrap.js" type="text/javascript"></script>
		<script type="text/javascript" src="js/jquery.form.js"></script>
		<script type="text/javascript" src="assets/others/script/validation-ext.js"></script>
		<script src="assets/plugins/formvalidation/language/zh_CN.js" type="text/javascript"></script>
    </head>
    <body>
        <form id="edit-form" class="form-horizontal" action="meter/updatePrice" method="post"
                      data-fv-framework="bootstrap">
            
			<div class="box-body">
				<div class="form-group">
					<label class="col-sm-2 control-label" style='margin-top:-3px;'>价格（元）/度</label>
					<div class="col-sm-6">
						<input type="text" name="price" data-op="contains" class="form-control input-sm" style='width:200px;'						
						data-fv-notempty="true" data-fv-numeric="true" placeholder="价格（元）/度">
                        <input type="hidden" name="companyId" id='companyId1'/>
					</div>
				</div>
				
			</div>
        </form>
<script>
	$('#companyId1').val($('#companyId').val())
    
	function submit1(){
        var price=$('#price').val();
        
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