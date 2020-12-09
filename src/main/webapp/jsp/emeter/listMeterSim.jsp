<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<% 	
String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort();
%>
<html>
	<head>
		<title>电表流量展示</title>
		<base href="<%=basePath%>" />
			<link href="assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="css/inner.css" />
		<link href="assets/plugins/bootstrap-select/css/bootstrap-select.min.css" rel="stylesheet" type="text/css" />
		<script src="assets/plugins/bootstrap-select/js/bootstrap-select.min.js" type="text/javascript"></script>
		<script src="assets/others/script/ajax-dict-select.js" type="text/javascript"></script>
		<link href="assets/plugins/datepicker/bootstrap-datepicker3.css" rel="stylesheet" type="text/css" />
		<script src="assets/plugins/datepicker/bootstrap-datepicker.js" type="text/javascript"></script>
		<script src="assets/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js" type="text/javascript"></script>
		<link rel="stylesheet" type="text/css" href="css/base.css" />
		<link rel="stylesheet" type="text/css" href="css/image.css" />
		<link href="css/label-choose-menu.css" rel="stylesheet" type="text/css" />
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
						</div>					

						<div class="col-md-3">
								

						</div>

						<div class="col-md-3">
							
						</div>

						<div class="col-md-2 text-right">
							<button type="button" class="btn btn-primary btn-sm" title="搜索" onclick="$.query()">
								<i class="glyphicon glyphicon-search"></i> 搜索
							</button>
							<button type="button" class="btn btn-default btn-sm" title="重置" onclick="$.reset()">
								<i class="glyphicon glyphicon-trash"></i> 重置
							</button>
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
							edit_url: '',
							edit_forward_url: '',
							delete_url: ''
						};
					</script>
					<div id="toolbar" class="btn-group">

					</div>
					<table id="table"
                       data-toolbar="#toolbar"
                       data-url="meterSim/paging"
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
								<th data-field="meterNo" data-halign="left" data-align="left">表号</th>
								<th data-field="cardNumber" data-halign="left" data-align="left">卡号</th>
								<th data-field="usageMonth" data-halign="left" data-align="left">当月流量</th>
								<th data-field="usageYesterday" data-halign="left" data-align="left">昨天流量</th>
								<th data-field="insertDate" data-halign="left" data-align="left">创建日期</th>
								<th data-field="logId" data-halign="left" data-align="left">logId</th>
								

							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>

<script>
	$('#installDate1').datepicker({
		format: 'yyyy-mm-dd',
		weekStart: 1,
		autoclose: true,
		todayBtn: 'linked',
		language: 'zh-CN'
	});

	$('#installDate2').datepicker({
		format: 'yyyy-mm-dd',
		weekStart: 1,
		autoclose: true,
		todayBtn: 'linked',
		language: 'zh-CN'
	});

	function show(value, row, index) {
		var str = '离线';
		if (value == 1) str = '在线';
		return str;
	}
	function show1(value, row, index) {
		var str = '合闸';
		if (value == 0) str = '拉闸';
		return str;
	}
	
	function export1(){
		//alert($('#companyId').val());
		if(confirm("确定要导出数据吗")){			
			window.location.href='export/offlineReport';
		}		
	}
    
    function export2(){
		//alert($('#companyId').val());
		if(confirm("确定要导出数据吗")){			
			window.location.href='export/meterInstallReport';
		}		
	}


</script>
	</body>
</html>
