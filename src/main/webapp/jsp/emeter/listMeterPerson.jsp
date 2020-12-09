<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<% 	
String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort();
%>
<html>
<head>
	<title>电表指派</title>
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
	                                <div class="input-group-addon">设备ID</div>
	                                <input type="text" name="deviceNo" data-op="contains" class="form-control input-sm" placeholder="设备ID">
	                            </div>
	                         </div>
				                
							<div class="col-md-2">
								<div class="input-group" style="z-index:1;">
									<div class="input-group-addon">类型</div>
									<div >
										<select name="meterType" class="prov form-control" style="z-index:1;cursor:pointer;"><option value="">请选择</option><option value="nb">nb</option><option value="lora">lora</option><option value="2g">2g</option></select> 										
								    </div>
								</div>
							</div>
				               
			                <div class="col-md-2">
								<div class="input-group">
									<div class="input-group-addon">平台</div>
									<div >
										<select name="platform" class="prov form-control" style="cursor:pointer;"><option value="">请选择</option><option value="移动">移动</option><option value="电信">电信</option><option value="联通">联通</option><option value="国动">国动</option></select> 										
								    </div>
								</div>
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
		             
						<div class="row">
						
	                        <div class="col-md-3">
	                            <div class="input-group">
	                                <div class="input-group-addon">项目编号</div>
	                                <input type="text" id="projectNo" name="projectNo" data-op="contains" class="form-control input-sm" placeholder="项目编号">
	                            </div>
	                         </div>
							 
							 <div class="col-md-3">
								<div class="input-group">
									<div class="input-group-addon">安装状态</div>
									<div >
										<select name="projectNoStatus" class="prov form-control" style="cursor:pointer;"><option value="">请选择</option><option value="0">未安装</option><option value="1">已安装</option></select> 										
								    </div>
								</div>
							</div>
							
							<div class="col-md-2">
								<div class="input-group">
									<div class="input-group-addon">运行状态</div>
									<div >
										<select name="status" class="prov form-control" style="cursor:pointer;"><option value="">请选择</option><option value="0">离线</option><option value="1">在线</option></select> 										
								    </div>
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
							edit_url2: 'meter/edit/',
                            edit_forward_url2: 'jsp/emeter/meter-maintain-edit',
                            delete_url: ''
                        };
                    </script>
                    <div id="toolbar" class="btn-group">
						
                    </div>
                    <table id="table"
                           data-toolbar="#toolbar"
                           data-url="meter/pagingMeterPerson"
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
							<th data-field="unit" data-halign="left" data-align="left">客户</th>
							<th data-field="deviceNo" data-halign="left" data-align="left" >设备ID</th>													
							<th data-field="meterNo" data-halign="left" data-align="left">表号</th>
							
							<th data-field="meterType" data-halign="left" data-align="left" >电表类型</th>
							<th data-field="newValue" data-halign="left" data-align="left">表读数</th>	
							
							<th data-field="platform" data-halign="left" data-align="left">平台</th>
							
							<th data-field="status" data-halign="left" data-formatter="show" data-align="left">状态</th>
							
							<th data-field="userName" data-halign="left" data-align="left">委派人</th>
							<th data-formatter="$.op1" data-align="center">操作</th>
							<th data-formatter="$.op2" data-align="center">操作</th>
							
						</tr>
					</thead>
		</table>
                </div>
            </div>
        </div>

<script>
	$.op= function (value, row, index) {
		//alert(row.userCode);
		var a="<a id='b_e_" + row.id + "'   >修改</a>";
		var b="<a id='b_e_" + row.id + "'   >派单</a>";
		//if(row.userCode)
        return  a+' '+b;
    };
	$.op1= function (value, row, index) {
		//alert(row.userCode);
		var a="<a id='b_e_" + row.id + "'  style='color:#337ab7;' >指定委派人</a>";
		
		//if(row.userCode)
        return  a;
    };
	$.op2= function (value, row, index) {
		//alert(row.userCode);
		
		var b="<a id='b_p_" + row.id + "'  style='color:#337ab7;' >派单</a>";
		if(row.userCode){
			return  b;
		}
        
    };
	function show(value, row, index) {
		var str='离线';
		if(value==1) str='在线';
		return str;
	}
</script>
</body>
</html>
