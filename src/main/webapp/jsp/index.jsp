<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
	<head>
		<title>首页</title>

		<link href="css/arvato.css" rel="stylesheet" type="text/css" />
		<link href="css/table.css" rel="stylesheet" type="text/css" />

		<link rel="stylesheet" type="text/css" href="css/home.css" />
		<link rel="stylesheet" type="text/css" href="css/base1.css" />

		<!-- 	<link href="assets/plugins/datepicker/bootstrap-datepicker3.css" rel="stylesheet" type="text/css" />
		<script src="assets/plugins/datepicker/bootstrap-datepicker.js" type="text/javascript"></script>	 -->

		<link href="js/lin/bootstrap-datepicker3.min.css" rel="stylesheet" type="text/css" />
		<script src="js/lin/bootstrap-datepicker.js" type="text/javascript"></script>
		<!-- <script src="assets/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js" type="text/javascript"></script> -->
		<script src="assets/plugins/bootstrap-dialog/js/bootstrap-dialog.min.js" type="text/javascript"></script>
		<!-- 	<script type="text/javascript" src="http://api.map.baidu.com/api?v=3.0&ak=n73iqPvDO4gzblDbyzuA1lFtZEDwqVQU"></script>
		<script type="text/javascript" src="http://api.map.baidu.com/library/TextIconOverlay/1.2/src/TextIconOverlay_min.js"></script>
		<script type="text/javascript" src="http://api.map.baidu.com/library/MarkerClusterer/1.2/src/MarkerClusterer_min.js"></script> -->



		<!-- <script src="//a.amap.com/jsapi_demos/static/china.js"></script> -->
		<script type="text/javascript" src="https://webapi.amap.com/maps?v=1.4.14&key=35197568a6e6c13623a6e65acdfe249c&plugin=AMap.MarkerClusterer"></script>
			<!--解决ie Promise报错-->
		<script src="js/lin/polyfill.min.js"></script>
		<!--解决IE不认识 Es6语法-->
		<script src="js/lin/babel.min.js"></script>
		<style>
			.scroll-box-title{
				display: flex;
				align-items: center;
				justify-content:space-around;
			}
			.scroll-box-title span{
				text-align: center;
				flex: 1;
			}
			.scroll-box {
				overflow: hidden;
			}
			.scroll-box ul {
				list-style: none;
				width: 100%;
				height: 100%;
				padding: 10px;
			}
			
			.scroll-box ul li {
				width: 100%;
				height: 40px;
				box-sizing: border-box;
				line-height: 40px;
				text-align: center;
				display: flex;
				align-items: center;
				justify-content: space-around;
			}
				.scroll-box ul li span{
					text-align: center;
					flex: 1;

			}
			label{
				max-width: none;
				border-radius: 2px!important;
				margin-bottom: 0;
			}
			.echart-title label{
				font-size: 20px;
				
			}
			.timeContainer{
				position: absolute;
				top: 0;
				display: flex;
				right: 0;
				align-items: center;
				z-index: 100;
			}
			.timeContainer label{
				display: flex;
				align-items: center;
				margin-right: 30px;
				
			}
			.mapMask{
				position: absolute;
				width: 100%;
				height: 100%;
				top: 0;
				left: 0;
				background: rgba(0,0,0,.5);
				z-index: 12233;
				display: flex;
				align-items: center;
				justify-content: center;
			}
			.loading{
            width: 80px;
            height: 40px;
			}
        .loading span{
            display: inline-block;
            width: 8px;
            height: 100%;
            border-radius: 4px;
            background: lightgreen;
            animation: load 1s ease infinite;
        }

        @keyframes load{
            0%,100%{
                height: 40px;
                background: lightgreen;
            }
            50%{
                height: 70px;
                margin: -15px 0;
                background: lightblue;
            }
        }
        .loading span:nth-child(2){
            -webkit-animation-delay:0.2s;
        }
        .loading span:nth-child(3){
            -webkit-animation-delay:0.4s;
        }
        .loading span:nth-child(4){
            -webkit-animation-delay:0.6s;
        }
        .loading span:nth-child(5){
            -webkit-animation-delay:0.8s;
        }
		
		/* ----------radio样式重写------------------- */

.radio input[type="radio"] {
  position: absolute;
  opacity: 0;
}
.radio input[type="radio"] + .radio-label:before {
  content: '';
  background: #f4f4f4;
  border-radius: 100%;
  border: 1px solid #b4b4b4;
  display: inline-block;
  width: 1em;
  height: 1em;
  position: relative;
  margin-right: 0.5em;
  vertical-align: top;
  margin-top:4px;
  cursor: pointer;
  text-align: center;
  -webkit-transition: all 250ms ease;
  transition: all 250ms ease;
}
.radio input[type="radio"]:checked + .radio-label:before {
  background-color: #3197EE;
  box-shadow: inset 0 0 0 4px #f4f4f4;
  border: 1px solid #3197EE;
}
.radio input[type="radio"]:focus + .radio-label:before {
  outline: none;
  border-color: #3197EE;
}
.radio input[type="radio"]:disabled + .radio-label:before {
  box-shadow: inset 0 0 0 4px #f4f4f4;
  border-color: #b4b4b4;
  background: #b4b4b4;
}
.radio input[type="radio"] + .radio-label:empty:before {
  margin-right: 0;
}


		</style>
	</head>
	<body>
		<!-- Right side column. Contains the navbar and content of the page -->
		<div class="content">


			<div id='home'>
				
				<div class="row mt25" style="margin-top:0px;">
					<div class="col-lg-12">
						<div class="border pos-r clear-fix">
							<div class="timeContainer">
								
							</div>


							<div class="echart" style="height: 500px;">
								<div id="chart2" style="width: 100%; height: 100%"></div>
							</div>
							
							  <div class="mapMask" id="allbar" style="background: none;">
								  暂无数据
							  </div>
						</div>
					</div>
				</div>

				<div class="row mt25">
					<div class="col-md-12">
						<div class="border pos-r clear-fix">
							<span class="echart-title" style='text-align:center'> <label style='color:#0099dd' onclick="window.all();">报警信息</label>
							</span>
							<div class="echart-title scroll-box-title">
								
								<span>公司</span>
								<span>客户</span>
								<span>表号</span>
								
								
								<span>最后上报时间</span>
								<span>报警级别</span>
								
								<span>结果描述</span>
							</div>
							<div class="echart scroll-box">
								<ul>


								</ul>
							</div>
						</div>
					</div>
				</div>

			</div>
		
			
			<script src="js/echarts/echarts-all.js" type="text/javascript"></script>
			<script type="text/babel" src="js/echarts/data.js?version=3.1.32(2019-08-13)"></script>

			<script type="text/babel">
			console.log(echarts.version);
			
			$('.portlet-title').hide();
			
				$("#chooceYear").datepicker({
					autoclose: true,
					startView: 2,
					maxViewMode: 2,
					minViewMode: 2,
					format: "yyyy",
					forceParse: false,
					orientation: 'bottom',
					language: 'cn',
				})
				// $("#chooceMonth").datepicker({
				// 	autoclose: true,
				// 	startView: 2,
				// 	maxViewMode: 1,
				// 	minViewMode: 1,
				// 	format: "mm",
				// 	forceParse: false,
				// 	orientation: 'bottom',
				// 	language: 'cn',
				// })


				var getViewPort = function() {
					var e = window,
						a = 'inner';
					if (!('innerWidth' in window)) {
						a = 'client';
						e = document.documentElement || document.body;
					}
					return {
						width: e[a + 'Width'],
						height: e[a + 'Height']
					};
				}
			
		

				function getYear(val) {
					getMeterValue(val)
				}
				//各省用电量柱状图数据
				function getMeterValue(val) {
					new Promise((resolve, reject) => {
						//$.get('http://47.103.102.210:9081/gdiot/stat/pagingMeterValue?year=' + val, (res) => {
						$.get('stat/companyYearMonthValue' + val, (res) => {
							resolve(res)
						})
					}).then((p) => {
						if(!p.rows.length){
							return;
						}
						$('#allbar').css('display', 'none')
						var xArr = [];
						var yearArr = [];
						var avgArr = [];
                        var i=0;
						for (var item in p.rows) {
                            i++;
							var value=parseFloat(p.rows[item].yearValue);
							console.log(value);
							yearArr.push(value);
							value=parseFloat(p.rows[item].avgValue);
							console.log(value);
							avgArr.push(value);
							xArr.push(p.rows[item].company);
                            //xArr.push('客户'+i);
						}
						allBar(xArr, yearArr, avgArr, p);
						$('#resetChart').click(() => {
							allBar(xArr, yearArr, avgArr, p);
						})
					})
				}
				getMeterValue('')

				
				//滚动信息
				new Promise((resolve, reject) => {
					$.get('meterWarning/get24HrWarning', (res) => {
						resolve(res)
					})
				}).then((p) => {
					scrollBox(p.list);
				})

				function scrollBox(arr) {
					//获得当前<ul>
					var $uList = $(".scroll-box ul");
					var timer = null;
					let liHtml = '';
					for (let item in arr) {
						liHtml += '<li><span>' + arr[item].company + '</span><span>' + arr[item]
							.unit + '</span><span>' + arr[item].meterNo + '</span>'+ '<span>' + arr[item].lastSendTime + '</span><span>' +
							 arr[item].level1+'级' + '</span><span>' + arr[item].description + '</span></li>';
					}
					$uList.html(liHtml)
					//触摸清空定时器
					$uList.hover(function() {
							clearInterval(timer);
						},
						function() { //离开启动定时器
							timer = setInterval(function() {
									scrollList();
								},
								1500);
						}); //自动触发触摸事件
					//滚动动画
					timer = setInterval(function() {
							scrollList();
						},
						1500);

					function scrollList() {

						//获得当前<li>的高度
						var scrollHeight = $(".scroll-box ul li:first").height();
						//滚动出一个<li>的高度
						$uList.stop().animate({
								marginTop: -scrollHeight
							},
							800, //滚动出一个<li>的高度 需要花费的时间
							function() {
								//动画结束后，将当前<ul>marginTop置为初始值0状态，再将第一个<li>拼接到末尾。
								$uList.css({
									marginTop: 0
								}).find("li:first").appendTo($uList);
							});

					}
				}
			</script>
	</body>
</html>
