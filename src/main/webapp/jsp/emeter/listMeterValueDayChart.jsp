<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<% 	
String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort();
%>
<html>
<head>
	<title>公司日电量图表统计</title>
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
	
	<link href="css/table1.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<form class="form-inline" action="" id="search-form" style="margin-top: 15px">
    		<div class="box-search width-border">
    				<div class="search-default">
	                    <div class="row">
						
	                        <div class="col-md-3">
	                            <div class="input-group">
	                                <div class="input-group-addon" style="height:28px;">日期</div>
	                                <div class="pull-left search connect" style="width: 100px;">
										<i class="icon-inp"></i>
										<input type="text" id="date1" name="date1" class="form-control" value="${date1}" style="width: 100%" />
									</div>
	                            </div>
								<input type="hidden" id="companyId" name="companyId" >
								<input type="hidden" id="projectNo" name="projectNo" >
	                         </div>
							 
	                         <div class="col-md-3">
	                            
	                         </div>
				                
							<div class="col-md-2">
								
							</div>
				               
			                <div class="col-md-2">
								
							</div>
							
							<div class="search-button">
								<div class="col-md-2">
									<button type="button" class="btn btn-primary btn-sm" title="搜索" onclick="query2()">
										<i class="glyphicon glyphicon-search"></i> 搜索
									</button>
			
									<button type="button" class="btn btn-default btn-sm" title="重置" onclick="reset1()">
										<i class="glyphicon glyphicon-trash"></i> 重置
									</button>
								</div>
							</div>
			                   
		                  </div>
						  
						  
            		
                </div>
    	</form>

		<div class="content">
				<div class="row mt25">
					<div class="col-lg-12">
						<div class="border pos-r clear-fix">
							<span class="echart-title"> <label>公司电量</label> 
                                                <!-- <select id="ttype" name="ttype" title="请选择" style="width:70px">
                                                   <option value="1">日</option>
                                                   <option value="2">时</option>
                                                </select> -->
                                          
							</span>
							<div class="echart">
								<div id="chart2" style="width: 100%; height: 100%;"></div>
							</div>
						</div>
					</div>
				</div>		
		</div>
		
<script src="js/echarts/echarts-all.js" type="text/javascript"></script>
	

<script>
	
	$('#menu').hide();
	
	$('#date1').datepicker({
		format: 'yyyy-mm-dd',
		weekStart: 1,
		autoclose: true,
		todayBtn: 'linked',
		language: 'zh-CN'
	});
	
	
	
	query2();
	
	function query2(){
		$.ajax({
			type : 'POST',
			url : 'stat/getCompanyValueDayChart',
			data: $('#search-form').serialize(),
			success : function(data) {
			
				option2 =  {
					title : {
						text: '',
						subtext: ''
					},
					tooltip : {
						trigger: 'axis'
					},
					legend: {
						data:['电量','电费']
					},
					toolbox: {
						show : true,
						feature : {
							
						}
					},
					calculable : true,
					xAxis : [
						{
							type : 'value',
							boundaryGap : [0, 0.01]
						}
					],
					yAxis : [
						{
							type : 'category',
							data : data.x
						}
					],
					series : [
						{
							name:'电量',
							type:'bar',
							data:data.y1
						},
                        {
							name:'电费',
							type:'bar',
							data:data.y2
						}
					]
				};
				var chart2 = echarts.init(document.getElementById("chart2"));				
				chart2.setOption(option2);
			
			}
		});
	}
	var date1=$('#date1').val();
	//alert(date1);
	function reset1(){
		//alert(1);
		$('#meterNo').val('');
		$('#projectNo').val('');
		$('#companyId').val('');
		$('#date1').val(date1);
		$('#time2').val('');
		//$('.caption-subject').html('国动集团');
		query2();
	}
	
	
	
</script>	
      
</body>
</html>
