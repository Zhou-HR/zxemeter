<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<title>地图</title>

		<link href="css/arvato.css" rel="stylesheet" type="text/css" />
		<link href="css/table.css" rel="stylesheet" type="text/css" />

		<link rel="stylesheet" type="text/css" href="css/home.css" />
		<link rel="stylesheet" type="text/css" href="css/base1.css" />

		<link href="assets/plugins/datepicker/bootstrap-datepicker3.css" rel="stylesheet" type="text/css" />
		<script src="assets/plugins/datepicker/bootstrap-datepicker.js" type="text/javascript"></script>
		<script src="assets/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js" type="text/javascript"></script>
		<script src="assets/plugins/bootstrap-dialog/js/bootstrap-dialog.min.js" type="text/javascript"></script>
		<!--
		<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=jfYWWCfmvUPCeD2rA2WkGhGCEVzfpZpI">
			//v2.0版本的引用方式：src="http://api.map.baidu.com/api?v=2.0&ak=您的密钥"
		</script>-->
		<script src="https://webapi.amap.com/maps?v=1.4.14&key=ffa71c2cf4175713841a760caeb02ea0"></script>
		<style>
			.portlet.light{
				padding-bottom: 0;
				margin-bottom: 0;
			}
		</style>

	</head>
	<body>
		<!-- Right side column. Contains the navbar and content of the page -->
		<form class="form-inline" action="" id="search-form">
			<div class="box-search width-border">
				<div class="search-default">
					<div class="row">

						<div class="col-md-3">
							<div class="input-group">
								<div class="input-group-addon">表号</div>
								<input type="text" id="projectNo" name="projectNo" value="${projectNo}" data-op="contains" class="form-control input-sm" placeholder="表号">
							</div>
						</div>

						<div class="col-md-3">
							<div class="input-group">
								<div class="input-group-addon">经度</div>
								<input type="text" id="longitude" readonly name="longitude" value="${jd}" data-op="contains" class="form-control input-sm"
								 placeholder="经度">
							</div>
						</div>

						<div class="col-md-3">
							<div class="input-group">
								<div class="input-group-addon">纬度</div>
								<input type="text" id="latitude" readonly name="latitude" value="${wd}" data-op="contains" class="form-control input-sm"
								 placeholder="纬度">
							</div>
						</div>

						<div class="search-button">
							<div class="col-md-3">
								<button type="button" class="btn btn-primary btn-sm" title="搜索" onclick="query()">
									<i class="glyphicon glyphicon-search"></i> 搜索
								</button>

								<button type="button" class="btn btn-default btn-sm" title="重置" onclick="$.reset()">
									<i class="glyphicon glyphicon-trash"></i> 重置
								</button>
							</div>
						</div>

					</div>



				</div>
			</div>
		</form>
		<div class="content" style="padding-top: 0;">

			<div id='home'>
				<div class="row">
					<div class="col-lg-12" style="width: 100%;">
						<div class="border pos-r clear-fix" style="width: 100%; height: 100%;">
							
							<div id="echart" class="echart" style="width: 100%;">
								<div id="baiducontainer" style="width: 100%; height: 100%;"></div>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
		<script type="text/javascript">
			
			$('#echart').css({
				//'height': (window.innerHeight - 272) + 'px'
				'height': (window.innerHeight- 232) + 'px'
			})
			
			/*
			var map = new BMap.Map("baiducontainer");
			// 创建地图实例  
			var point = new BMap.Point(116.404, 39.915);
			// 创建点坐标  
			map.centerAndZoom(point, 5);
			map.enableScrollWheelZoom();*/
			
			var map = new AMap.Map('baiducontainer', {
				resizeEnable: true, //是否监控地图容器尺寸变化
				zoom:5, //初始化地图层级
				//center: [116.397428, 39.90923] //初始化地图中心点
				center: [106.397428, 35.90923] //初始化地图中心点
			});
			/*
			var showStation4 = function(longitude, latitude) {
				//point = new BMap.Point(longitude, latitude);
				map = new BMap.Map("baiducontainer");
				point = new BMap.Point(longitude, latitude);
				map.centerAndZoom(point, 15);
				map.enableScrollWheelZoom();
				var marker = new BMap.Marker(point); // 创建标注    
				map.addOverlay(marker); // 将标注添加到地图中 
				//map.setViewport(point)
			}*/
			
			var showStation4 = function(longitude, latitude,projectName) {
				var myArray=new Array();
				myArray[0]=longitude;
				myArray[1]=latitude;
				//var str='['+longitude+','+latitude+']';
				//str=
				//alert(str);
				var map = new AMap.Map('baiducontainer', {
					resizeEnable: true, //是否监控地图容器尺寸变化
					zoom:17, //初始化地图层级
					center: myArray //初始化地图中心点
				});
				
				// 创建gps坐标位置点标记
				var lnglat = myArray;
				var m1 = new AMap.Marker({
					position: lnglat,
					icon: "https://webapi.amap.com/theme/v1.3/markers/n/mark_r.png"
				});
				map.add(m1);
				m1.setLabel({
					offset: new AMap.Pixel(20, 20),
					content: projectName
				});
				/*
				var map = new AMap.Map('baiducontainer', {
					resizeEnable: true, //是否监控地图容器尺寸变化
					zoom:18, //初始化地图层级
					center: [108.958074, 34.280548] //初始化地图中心点
				});
				
				// 创建gps坐标位置点标记
				var lnglat = [108.958074, 34.280548];
				var m1 = new AMap.Marker({
					position: lnglat,
					icon: "https://webapi.amap.com/theme/v1.3/markers/n/mark_r.png"
				});
				map.add(m1);
				m1.setLabel({
					offset: new AMap.Pixel(20, 20),
					content: "GPS 坐标系中首开广场"
				});*/
			}
			
			// 初始化地图，设置中心点坐标和地图级别  
			var query = function() {
				console.log($('#projectNo').val());
				if($('#projectNo').val()==''){
					return;
				}
				$.ajax({
					type: 'POST',
					url: 'station/getProject',
					data: $('#search-form').serialize(),
					success: function(data) {
						//alert(data.longitude)
						var longitude = data.longitude;
						var latitude = data.latitude;
						showStation4(longitude, latitude,data.projectName);
						$('#longitude').val(longitude);
						$('#latitude').val(latitude);
					}
				});
			}
			if($('#projectNo').val())
				query();
		</script>



	</body>
</html>
