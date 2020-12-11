<%@page import="org.apache.commons.lang3.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
%>
<html>
<head>
    <title>公司月电量图表统计</title>
    <base href="<%=basePath%>"/>
    <link rel="stylesheet" type="text/css" href="css/inner.css"/>
    <link href="assets/plugins/bootstrap-select/css/bootstrap-select.min.css" rel="stylesheet" type="text/css"/>
    <script src="assets/plugins/bootstrap-select/js/bootstrap-select.min.js" type="text/javascript"></script>
    <script src="assets/others/script/ajax-dict-select.js" type="text/javascript"></script>
    <link href="assets/plugins/datepicker/bootstrap-datepicker3.css" rel="stylesheet" type="text/css"/>
    <script src="assets/plugins/datepicker/bootstrap-datepicker.js" type="text/javascript"></script>
    <script src="assets/plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="css/base.css"/>
    <link rel="stylesheet" type="text/css" href="css/image.css"/>
    <link href="css/label-choose-menu.css" rel="stylesheet" type="text/css"/>

    <link href="css/table1.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<form class="form-inline" action="" id="search-form" style="margin-top: 15px">
    <div class="box-search width-border">
        <div class="search-default">
            <div class="row">

                <div class="col-md-3">
                    <div class="input-group">
                        <div class="input-group-addon">年份</div>
                        <select name="year" id='year' class="prov form-control" style="z-index:1;cursor:pointer;">
                            <option value="">请选择</option>
                            <option value="2019">2019</option>
                            <option value="2020">2020</option>
                            <option value="2021">2021</option>
                            <option value="2022">2022</option>
                            <option value="2023">2023</option>
                            <option value="2024">2024</option>
                            <option value="2025">2025</option>
                            <option value="2026">2026</option>
                        </select>
                    </div>
                    <input type="hidden" id="companyId" name="companyId">
                    <input type="hidden" id="projectNo" name="projectNo">
                </div>

                <div class="col-md-3">
                    <div class="input-group" style="">
                        <div class="input-group-addon">月份</div>
                        <select name="month" id='month' class="prov form-control" style="z-index:1;cursor:pointer;">
                            <option value="">请选择</option>
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                            <option value="5">5</option>
                            <option value="6">6</option>
                            <option value="7">7</option>
                            <option value="8">8</option>
                            <option value="9">9</option>
                            <option value="10">10</option>
                            <option value="11">11</option>
                            <option value="12">12</option>
                        </select>

                    </div>
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
                            <i class="glyphicon glyphicon-download"></i> 导出电量
                        </button>

                    </div>
                </div>

            </div>

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

                        <button type="button" class="btn btn-primary btn-sm" title="导出" onclick="export2()">
                            <i class="glyphicon glyphicon-download"></i> 导出电费
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

    function query2() {
        $.ajax({
            type: 'POST',
            url: 'stat/getCompanyValueChart',
            data: $('#search-form').serialize(),
            success: function (data) {

                option2 = {
                    title: {
                        text: '',
                        subtext: ''
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        data: ['电量', '电费']
                    },
                    toolbox: {
                        show: true,
                        feature: {}
                    },
                    calculable: true,
                    xAxis: [
                        {
                            type: 'value',
                            boundaryGap: [0, 0.01]
                        }
                    ],
                    yAxis: [
                        {
                            type: 'category',
                            data: data.x
                        }
                    ],
                    series: [
                        {
                            name: '电量',
                            type: 'bar',
                            data: data.y1
                        },
                        {
                            name: '电费',
                            type: 'bar',
                            data: data.y2
                        }
                    ]
                };
                var chart2 = echarts.init(document.getElementById("chart2"));
                chart2.setOption(option2);

            }
        });
    }

    function reset1() {
        //alert(1);
        $('#year').val('');
        $('#projectNo').val('');
        $('#companyId').val('');
        $('#month').val('');
        $('#time2').val('');
        //$('.caption-subject').html('国动集团');
        query2();
    }

    function export1() {
        if (confirm("确定要导出电量数据吗")) {
            window.location.href = 'export/monthMeterValue?year=' + $('#year').val() + '&month=' + $('#month').val();
        }
    }

    function export2() {
        if (confirm("确定要导出电费数据吗")) {
            window.location.href = 'export/monthMeterFee?year=' + $('#year').val() + '&month=' + $('#month').val();
        }
    }

</script>

</body>
</html>
