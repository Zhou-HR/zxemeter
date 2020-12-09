<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <title>电表委派</title>

        <script src="assets/others/script/ajax-dict-select.js" type="text/javascript"></script>
		<script type="text/javascript" src="assets/others/script/validation-ext.js"></script>
    </head>
    <body>
        <section class="content">
            <div class="box box-primary">
                <form id="edit-form" class="form-horizontal" action="${empty entity? 'meter/sendMaintain' : 'meter/sendMaintain'}" method="post"
                      data-fv-framework="bootstrap">
                    <c:if test="${not empty entity}">
                        <input type="hidden" name="id" value="${entity.id}"/>
                    </c:if>
                    <div class="box-body">
						
                        <div class="form-group">
                            <label class="col-sm-2 control-label">表号</label>
                            <div class="col-sm-6">
                                <input type="text" readonly class="form-control" name="meterNo" value="${entity.meterNo}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">发起人</label>

                            <div class="col-sm-6">
                                <input type="text" readonly class="form-control" name="userCode" value="${entity.senderName}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">委派人员</label>

                            <div class="col-sm-6">
                                <input type="text" readonly class="form-control" name="userCode" value="${entity.receiverName}">
                            </div>
                        </div>
					   <div class="form-group">
							<label class="col-sm-2 control-label">委派日期</label>

							<div class="col-sm-6">
								<input type="text" readonly class="form-control" id="date" name="sendDate" value="">
							</div>
						</div>
                        <div class="form-group">
							<label class="col-sm-2 control-label">委派原因</label>

							<div class="col-sm-6">
								<input type="text"  class="form-control" name="reason" data-fv-notempty="true" value="${entity.reason}">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">处理日期</label>

							<div class="col-sm-6">
								<input type="text" readonly class="form-control" name="dealDate" value="${entity.dealDate}">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">处理结果</label>

							<div class="col-sm-6">
								<input type="text" readonly class="form-control" name="dealResult" value="${entity.dealResult}">
							</div>
						</div>
						
                    </div>
                    
                </form>
            </div>
        </section>
	<script>
		function nowtime(){//将当前时间转换成yyyymmdd格式
			var mydate = new Date();
			var str = "" + mydate.getFullYear()+'-';
			var mm = mydate.getMonth()+1
			if(mydate.getMonth()>9){
			 str += mm;
			}
			else{
			 str += "0" + mm;
			}
			str+='-';
			if(mydate.getDate()>9){
			 str += mydate.getDate();
			}
			else{
			 str += "0" + mydate.getDate();
			}
			return str;
		}
		var ymd=nowtime();
		$('#date').val(ymd);
	</script>
    </body>
</html>
