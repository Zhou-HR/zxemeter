<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<% 	
String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort();
%>
<html>
<head>
	<title>低电量电表统计</title>
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
	                            
	                         </div>
							 
	                         <div class="col-md-3">
	                            
	                         </div>
				                
							<div class="col-md-2">
								
							</div>
				               
			                <div class="col-md-2">
								
							</div>
							
							<div class="search-button">
								<div class="col-md-2">
									<button type="button" class="btn btn-primary btn-sm" title="导出" onclick="export1()">
										<i class="glyphicon glyphicon-download"></i> 导出
									</button>
			
									<button type="button" style="visibility:hidden;" class="btn btn-default btn-sm" title="重置" onclick="reset1()">
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
                            edit_url: '',
                            edit_forward_url: '',
                            delete_url: ''
                        };
                    </script>
                    <div id="toolbar" class="btn-group">
						
                    </div>
                    <table id="table"
                           data-toolbar="#toolbar"
                           data-url="meter/pagingMeterValueLow"
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
							<th data-field="projectNo" data-halign="left" data-align="left">项目编号</th>
							<th data-field="projectName" data-halign="left" data-align="left">项目名称</th>
							<th data-field="meterNo" data-halign="left" data-align="left" >表号</th>							
							<th data-field="evalue" data-halign="left" data-align="left">电量</th>	
							
							
						</tr>
					</thead>
		</table>
                </div>
            </div>
        </div>

<script>
	function export1(){
		if(confirm("确定要导出数据吗")){
			window.location.href='export/meterLowValue';
		}		
	}
</script>
</body>
</html>
