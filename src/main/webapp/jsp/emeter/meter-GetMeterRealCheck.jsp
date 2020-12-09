<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <title>电量实时查询</title>

        <script src="assets/others/script/ajax-dict-select.js" type="text/javascript"></script>
		<script type="text/javascript" src="assets/others/script/validation-ext.js"></script>
    </head>
    <body>
        <section class="content">
            <div class="box box-primary">
                <form id="edit-form" class="form-horizontal" action="${empty entity? 'down/getMeterRealCheck' : 'down/getMeterRealCheck'}" method="post"
                      data-fv-framework="bootstrap">
                    
                    <div class="form-group">
						<label class="col-sm-2 control-label">表号</label>
						<div class="col-sm-6">
							<input type="text" readonly class="form-control" name="meterNo" value="${entity.meterNo}">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">电表类型</label>
						<div class="col-sm-6">
							<input type="text" readonly class="form-control" name="meterType" value="${entity.meterType}">
						</div>
					</div> 
                    <div class="form-group">
						<label class="col-sm-2 control-label">imei</label>
						<div class="col-sm-6">
							<input type="text" readonly class="form-control" name="imei" value="${entity.imei}">
						</div>
					</div>   
					
                    <div class="form-group">
						<label class="col-sm-2 control-label">返回结果</label>
						<div class="col-sm-6">
							<input type="text" id="result" readonly class="form-control" value="">
						</div>
					</div>
                    
                </form>
            </div>
        </section>
    </body>
</html>
