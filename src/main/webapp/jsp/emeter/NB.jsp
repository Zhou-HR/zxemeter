<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>首页</title>

    <link href="css/arvato.css" rel="stylesheet" type="text/css"/>
    <link href="css/table.css" rel="stylesheet" type="text/css"/>

    <link rel="stylesheet" type="text/css" href="css/home.css"/>
    <link rel="stylesheet" type="text/css" href="css/base1.css"/>

    <link href="assets/plugins/datepicker/bootstrap-datepicker3.css" rel="stylesheet" type="text/css"/>
    <script src="assets/plugins/datepicker/bootstrap-datepicker.js" type="text/javascript"></script>
    <script src="assets/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js" type="text/javascript"></script>
    <script src="assets/plugins/bootstrap-dialog/js/bootstrap-dialog.min.js" type="text/javascript"></script>
</head>
<body>
<!-- Right side column. Contains the navbar and content of the page -->
<div class="content">


    <div id='home'>
        <div class="row">
            <div class="col-lg-12" style="width: 100%; height: 800px;">
                <div class="border pos-r clear-fix" style="width: 100%; height: 100%;">
							<span class="echart-title"> <label>全国基站热力图</label> 
                                                
							</span>
                    <div class="echart" style="width: 100%; height: 100%;">
                        <div id="chart1" style="width: 100%; height: 100%;"></div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row mt25">
            <div class="col-lg-12">
                <div class="border pos-r clear-fix">
							<span class="echart-title"> <label>月电表电量</label>
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

        <div class="row mt25">

            <div class="col-md-4">
                <div class="border pos-r clear-fix">
							<span class="echart-title"> <label onclick="window.all();">地区报警量</label>
							</span>
                    <div class="echart">
                        <div id="chart3" style="width:100%; height: 100%;"></div>
                    </div>
                </div>
            </div>

            <div class="col-md-4">
                <div class="border pos-r clear-fix">
							<span class="echart-title"> <label onclick="window.all();">基站上线率</label>
							</span>
                    <div class="echart">
                        <div id="chart4" style="width: 100%; height: 100%;"></div>
                    </div>
                </div>
            </div>


            <div class="col-md-4">
                <div class="border pos-r clear-fix">
							<span class="echart-title"> <label onclick="window.all();">地区电量Top5</label>
							</span>
                    <div class="echart">
                        <div id="chart5" style="width: 100%; height: 100%;"></div>
                    </div>
                </div>
            </div>
        </div>


        <div class="row mt25" style="height:100px;">


        </div>


    </div>
    <script src="js/echarts/echarts-all.js" type="text/javascript"></script>
    <script type="text/javascript">
        var getViewPort = function () {
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

        //if ($('.page-sidebar-fixed').size() === 1)
        /*
    {
        //alert($('#menuToggler'));
        //$('#menuToggler').click();
        //$("body").addClass('page-portlet-fullscreen');
        var that=$('.portlet > .portlet-title .fullscreen');
        var portlet = that.closest(".portlet");
        //alert(portlet.length);

        var height = getViewPort().height -
            portlet.children('.portlet-title').outerHeight() -
            parseInt(portlet.children('.portlet-body').css('padding-top')) -
            parseInt(portlet.children('.portlet-body').css('padding-bottom'));

        that.addClass('on');
        portlet.addClass('portlet-fullscreen');
        $('body').addClass('page-portlet-fullscreen');
        portlet.children('.portlet-body').css('height', height);

        //mapChart.resize();
    }
    */
    </script>
    <script src="js/echarts/data.js" type="text/javascript"></script>


</body>
</html>