<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<% 	
String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort();
%>
<html>
<head>
	<title>派单查看</title>
	<base href="<%=basePath%>"/>
	<link rel="stylesheet" type="text/css" href="css/inner.css" />
	<link href="assets/plugins/bootstrap-select/css/bootstrap-select.min.css" rel="stylesheet" type="text/css"/>
	<script src="assets/plugins/bootstrap-select/js/bootstrap-select.min.js" type="text/javascript"></script>
	<script src="assets/others/script/ajax-dict-select.js" type="text/javascript"></script>
	<link href="assets/plugins/datepicker/bootstrap-datepicker3.css" rel="stylesheet" type="text/css" />
	<script src="assets/plugins/datepicker/bootstrap-datepicker.js" type="text/javascript"></script>
	<script src="assets/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="css/base.css"/>
	<link rel="stylesheet" type="text/css" href="css/image.css"/>
	<link href="css/label-choose-menu.css" rel="stylesheet" type="text/css"/>
</head>
<body>
	<form class="form-inline" action="" id="search-form" style="margin-top: 15px">
    		<div class="box-search width-border">
    				<div class="search-default">
	                    <div class="row">
						
	                        <div class="col-md-3">
	                            <div class="input-group">
	                                <div class="input-group-addon">表号</div>
	                                <input type="text" name="meterNo" data-op="contains" class="form-control input-sm" placeholder="表号">
	                            </div><input type="hidden" id="companyId" name="companyId" value="">
								<input type="hidden" id="projectNo" name="projectNo" value="">
	                         </div>
							 
	                         <div class="col-md-3">
	                            <div class="input-group">
									<div class="input-group-addon">处理状态</div>
									<div >
										<select name="status" class="prov form-control" style="cursor:pointer;"><option value="">请选择</option><option value="0">未处理</option><option value="1">已处理</option></select> 										
								    </div>
								</div>
	                         </div>
				                
							<div class="col-md-2">
								
							</div>
				               
			                <div class="col-md-2">
								
							</div>
							
							<div class="search-button">
								<div class="col-md-2">
									<button type="button" class="btn btn-primary btn-sm" title="搜索" onclick="$.query()">
										<i class="glyphicon glyphicon-search"></i> 搜索
									</button>
			
									<button type="button" class="btn btn-default btn-sm" title="重置" onclick="$.reset()">
										<i class="glyphicon glyphicon-trash"></i> 重置
									</button>
								</div>
							</div>
			                   
		                </div>
		             
						
                   
            		
                </div>
    	</form>

        <div class="box-body">
            <div class="row">
                <div class="col-md-12">
                    <script type="text/javascript">
                        var table_setting = {
                            add_url: '',
                            edit_url: 'meter/detail/',
                            edit_forward_url: 'jsp/emeter/meter-person-edit',
							edit_url2: 'meter/editMaintain/',
                            edit_forward_url2: 'jsp/emeter/meter-maintain-check',
                            delete_url: ''
                        };
                    </script>
                    <div id="toolbar" class="btn-group">
						
                    </div>
                    <table id="table"
                           data-toolbar="#toolbar"
                           data-url="meter/pagingMeterMaintain"
                           data-method="post"
                           data-pagination="true"
                           data-side-pagination="server"
                           data-flat="true"
                           data-query-params="$.getParams"
                           data-id-field="id"
                           data-toggle="table"
                           data-striped="true"
                           data-show-refresh="false"
                           data-show-toggle="false"
                           data-show-columns="false">
	     
					<thead style='display:none'>
						<tr>
		<!-- 					<th data-radio="true"></th> -->
							<th data-field="company" data-halign="left" data-align="left">公司</th>
								<th data-field="unit" data-halign="left" data-align="left">事业部</th>
																			
							<th data-field="meterNo" data-halign="left" data-align="left">表号</th>
							
							<th data-field="senderName" data-halign="left" data-align="left" >发起人</th>
							<th data-field="sendDate" data-halign="left" data-align="left">发起时间</th>	
							
							<th data-field="reason" data-halign="left" data-align="left">理由</th>
							<th data-field="receiverName" data-halign="left" data-align="left">委派人</th>							
							
							<th data-field="dealDate" data-halign="left" data-align="left">处理时间</th>
							
							<th data-field="dealerName" data-halign="left" data-align="left">处理人</th>
							<th data-field="dealResultShort" data-halign="left" data-align="left">处理结果</th>
							<th data-field="id" data-halign="left" data-formatter="$.op1" data-align="left">查看详细</th>
							
							
						</tr>
					</thead>
		</table>
                </div>
            </div>
        </div>

<script>
	$.op1= function (value, row, index) {
		//alert(row.userCode);
		var a="<a id='b_p_" + row.id + "' style='color:#337ab7;' >查看详细</a>";
		
		//if(row.userCode)
        return  a;
    };
</script>
</body>
</html>
