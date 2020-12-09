<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% 	
String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+request.getContextPath();
%>
<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<html lang="en">
	<!--<![endif]-->
	<!-- BEGIN HEAD -->
	<head>
		<meta charset="utf-8" />
		<title>中性智能电表平台</title>
		<base href="${empty basePath ? '/' : basePath}" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta content="width=device-width, initial-scale=1" name="viewport" />
		<meta content="" name="description" />
		<meta content="" name="author" />

		<c:if test="${empty menuNode}">
			<script>
				//location.href = "/index?redirect=" + location.pathname;
            </script>
		</c:if>

		<!-- BEGIN GLOBAL MANDATORY STYLES -->
		<link href="assets/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
		<link href="assets/plugins/simple-line-icons/simple-line-icons.min.css" rel="stylesheet" type="text/css" />
		<link href="assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
		<!-- END GLOBAL MANDATORY STYLES -->

		<!-- BEGIN THEME GLOBAL STYLES -->
		<link href="assets/apps/css/components.css" rel="stylesheet" id="style_components" type="text/css" />
		<link href="assets/apps/css/plugins.min.css" rel="stylesheet" type="text/css" />
		<!-- END THEME GLOBAL STYLES -->

		<!-- BEGIN THEME LAYOUT STYLES -->
		<link href="assets/apps/css/layout/css/layout.css" rel="stylesheet" type="text/css" />
		<link href="assets/apps/css/layout/css/themes/darkblue.css" rel="stylesheet" type="text/css" id="style_color" />
		<link href="assets/apps/css/layout/css/custom.min.css" rel="stylesheet" type="text/css" />
		<!-- END THEME LAYOUT STYLES -->

		<!-- BEGIN DIALOG STYLES -->
		<link rel="stylesheet" href="assets/plugins/bootstrap-dialog/css/bootstrap-dialog.min.css">
		<!-- END DIALOG STYLES -->

		<script src="assets/plugins/jquery.min.js" type="text/javascript"></script>
		<script src="assets/plugins/bootstrap/js/bootstrap.js" type="text/javascript"></script>

		<sitemesh:write property='head' />
		<style>
			.nav>li>a:hover {
				text-decoration: none;
				background-color: #3f4f62;
				color:#ffffff !important;
				}
				.navbar-nav>li>a {
					padding-top: 13px;
					padding-bottom: 13px;
					text-align: center;
				}
				.nav .open>a, .nav .open>a:focus, .nav .open>a:hover 
				{
					background-color: #3f4f62;
				}
				.dropdown-menu > li > a:hover{
					background: #3f4f62;
					color: white!important;
				}
				.dropdown-menu{
					box-shadow: 0 6px 12px rgba(0,0,0,.175);
				}
				.page-sidebar .page-sidebar-menu {
					padding-top: 0;
					}
				.portlet.light{
					margin-bottom: 0;
					padding: 10px;
				}
		</style>
	</head>
	<!-- END HEAD -->
	<body class="page-header-fixed page-sidebar-closed-hide-logo page-container-bg-solid page-content-white">
		<!-- BEGIN HEADER -->
		<div class="page-header navbar navbar-fixed-top">
			<!-- BEGIN HEADER INNER -->
			<div class="page-header-inner ">
				<!-- BEGIN LOGO -->
				<div class="page-logo" style="width: 226px;padding: 0;text-align: center;">
					<!-- <img src="https://shnbbucket.oss-cn-beijing.aliyuncs.com/20190505104115.png"  style="max-height: 100%;"> -->
					<img src="images/logo_white.png"  style="max-height: 0%;">
					
					
					
<!-- 					<div class="menu-down dropdown">
						<a href="javascript:;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
							<i class="fa fa-th-large"></i>
						</a>
						<ul class="dropdown-menu dropdown-menu-item">
							<c:forEach var="node" items="${topMenu}" varStatus="status">
								<li class="color-0${status.index+1}">
									<a href="toMenu/${node.id}">
										<i class="icon-home"></i><br>
										${node.name}
									</a>
								</li>
							</c:forEach>
						</ul>
					</div> -->
				<!-- 	<a href="index"></a>
					<div id='menuToggler' class="menu-toggler sidebar-toggler"></div> -->
				</div>
				<!-- END LOGO -->
				<div class="page-logo" style="width:100px;margin-left:160px;">
					<ul class="nav navbar-nav pull-right">

						<!-- BEGIN USER LOGIN DROPDOWN -->
						<li class="dropdown dropdown-user" style="width:175px;">
							<a class="toHome" style="width:125px;font-size:18px;">
								<i class="fa fa-home"></i>首页
							</a>

						</li>
					</ul>
				</div>

				<c:forEach var="node" items="${menuNodeTop.child}" varStatus="idxStatus">
					<c:if test="${idxStatus.index==0}">
						<div class="page-logo" style="width:100px;margin-left:40px;">
					</c:if>
					<c:if test="${idxStatus.index>0}">
						<div class="page-logo" style="width:100px;margin-left:80px;">
					</c:if>
					<ul class="nav navbar-nav pull-right">

						<li class="dropdown dropdown-user" >
							<c:if test="${not empty node.child}">
								<a class="dropdown-toggle" style="width:175px;font-size:16px;" data-toggle="dropdown" data-hover="dropdown"
								 data-close-others="true">
									<i class="${node.icon}" ></i>${node.name}<i class="fa fa-angle-down"></i>
								</a>
							</c:if>
							<c:if test="${empty node.child}">
								<a data-url="${node.path}" class="top1" style="width:175px;font-size:16px;">
									<i class="${node.icon}" ></i>${node.name}
								</a>
							</c:if>
							<c:if test="${not empty node.child}">
								<ul class="dropdown-menu dropdown-menu-default">
									<c:forEach var="node1" items="${node.child}">
										<li>
											<a data-url="${node1.path}" class="top1">
												<i class="fa fa-angle-right"></i> ${node1.name}
											</a>
										</li>
									</c:forEach>
								</ul>
							</c:if>
						</li>
					</ul>
			</div>
			</c:forEach>

			<!-- BEGIN RESPONSIVE MENU TOGGLER -->
			<a href="javascript:;" class="menu-toggler responsive-toggler" data-toggle="collapse" data-target=".navbar-collapse">
			</a>
			<!-- END RESPONSIVE MENU TOGGLER -->
			<!-- BEGIN TOP NAVIGATION MENU -->
			<div class="top-menu">
				<ul class="nav navbar-nav pull-right">

					<li class="dropdown dropdown-user" style="height:46px;">
						<a style="height: 100%;" class="dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true">
							<img alt="" class="img-circle" src="assets/apps/img/avatar.png" />
							<span class="username username-hide-on-mobile">${userName}</span>
							<i class="fa fa-angle-down"></i>
						</a>
						<ul class="dropdown-menu">
							<li>
								<a href="jsp/security/user-change-pwd.jsp">
									<i class="icon-magic-wand"></i> 修改密码 </a>
							</li>
							<li>
								<a href="logout">
									<i class="icon-key"></i> Log Out </a>
							</li>
						</ul>
					</li>
					<!-- END USER LOGIN DROPDOWN -->
				</ul>
			</div>
			<!-- END TOP NAVIGATION MENU -->
		</div>
		<!-- END HEADER INNER -->
		</div>
		<!-- END HEADER -->
		<!-- BEGIN HEADER & CONTENT DIVIDER -->
		<div class="clearfix"></div>
		<!-- END HEADER & CONTENT DIVIDER -->
		<!-- BEGIN CONTAINER -->

		<div class="page-container" style="display: flex;">
			<!-- BEGIN SIDEBAR -->
			<div class="page-sidebar-wrapper" style="overflow-y: auto;width: 256px;background: #364150;">
				<!-- BEGIN SIDEBAR -->
				<div class="page-sidebar navbar-collapse collapse" style="width: 100%;">

					<c:if test="${not empty role or opType==1}">
						<!-- BEGIN SIDEBAR MENU -->

						<ul id="menu" style="display:block;" class="page-sidebar-menu page-header-fixed page-sidebar-menu-light"
						 data-keep-expanded="false" data-auto-scroll="true" data-slide-speed="200">

							<c:forEach var="node" items="${menuNode.child}">
								<li id="${node.companyId}" class="nav-item">
									<a class="nav-link">
										<i class="${empty node.icon ? 'icon-briefcase' : node.icon}"></i>
										<span class="title" style="font-size:16px;">${node.name}</span>
										<span class="arrow"></span>
									</a>
									<c:if test="${not empty node.child}">
										<ul class="sub-menu">
											<c:forEach var="node1" items="${node.child}">
												<li id="${node1.companyId}" class="nav-item">
													<a href="${empty node1.path ? 'javascript:;' : node1.path}" class="nav-link">
														<i class="${empty node1.icon ? 'icon-briefcase' : node1.icon}"></i>
														<span class="title">${node1.name}</span>
														<c:if test="${not empty node1.child}">
															<span class="arrow ajaxCompany"></span>
														</c:if>
													</a>

												</li>
											</c:forEach>
										</ul>
								</li>
					</c:if>
					</c:forEach>
					</ul>

					<!-- END SIDEBAR MENU -->
					</c:if>
				</div>
				<!-- END SIDEBAR -->
			</div>
			<!-- END SIDEBAR -->
			<!-- BEGIN CONTENT -->
			<div class="page-content-wrapper">
				<!-- BEGIN CONTENT BODY -->
				<div class="page-content" style="margin-left: 0!important;overflow-y: auto;padding-bottom: 0;">
					<!-- <div class="row">
						<div class="col-md-12 col-sm-12"> -->
							<div class="portlet light">
								<div class="portlet-title">
									<div class="caption font-green">
										<span class="caption-subject bold uppercase">
											<sitemesh:write property='title' />
										</span>
									</div>
									<div class="actions">
										<a class="btn btn-circle btn-icon-only btn-default fullscreen"> </a>
									</div>
								</div>
								<div class="portlet-body">
									<sitemesh:write property='body' />
								</div>
							</div>
						<!-- </div> -->
					<!-- </div> -->
				</div>
				<!-- END CONTENT BODY -->
			</div>
			<!-- END CONTENT -->
		</div>
		</div>
	
		<!-- END CONTAINER -->
		<!-- BEGIN FOOTER -->
		<div class="page-footer">
			<div class="page-footer-inner">
				Copyright &copy; 2019. All rights reserved.
			</div>
			<div class="scroll-to-top">
				<i class="icon-arrow-up"></i>
			</div>
		</div>
		<script>
			var role='${role}';
			if(window.location.href.indexOf('index')<0){
				$('.page-sidebar-wrapper').css({
				'height': (window.innerHeight - 46-33) + 'px',
				// 'width':'256px'
			})
			$('.page-content-wrapper').css({
				'height': (window.innerHeight - 46-33) + 'px'
			})
			}
			else{
				if(role==''){
					$('.page-sidebar-wrapper').css({
					'display':'none'})
				}
					
			}

		</script>
		<!-- END FOOTER -->
		<!--[if lt IE 9]>
        <script src="assets/plugins/respond.min.js" type="text/javascript"></script>
        <script src="assets/plugins/excanvas.min.js" type="text/javascript"></script>
        <![endif]-->
		<!-- BEGIN CORE PLUGINS -->
		<script src="assets/plugins/js.cookie.min.js" type="text/javascript"></script>
		<script src="assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
		<!-- END CORE PLUGINS -->
		<!-- BEGIN THEME GLOBAL SCRIPTS -->
		<script src="assets/apps/scripts/app.js" type="text/javascript"></script>
		<!-- END THEME GLOBAL SCRIPTS -->
		<!-- BEGIN THEME LAYOUT SCRIPTS -->
		<script src="assets/apps/scripts/layout.js" type="text/javascript"></script>
		<!-- END THEME LAYOUT SCRIPTS -->
		<!-- BEGIN DIALOG SCRIPTS -->
		<script src="assets/plugins/bootstrap-dialog/js/bootstrap-dialog.js" type="text/javascript"></script>
		<!-- END DIALOG SCRIPTS -->
		<script type="text/javascript" src="assets/others/script/base.js"></script>
		<script>
			var ls=localStorage;
			//alert(ls.getItem('index'))
			$('.page-logo>ul>li>a').each(function(){
				//alert($(this).text())
				//alert(ls.getItem('index'))
				$(this).css('color','');
				if($(this).text()==ls.getItem('index')){
					$(this).css('color','#ffffff');
				}
				if(ls.getItem('index')==''){
					if($(this).text().indexOf('首页')>0){
						$(this).css('color','#ffffff');
					}
				}
			});
			
			//ls.
			//var opType=${opType};
			//alert(opType);
			// if(opType==1){
			// $('#menu').show();
			// }
			var role = '${role}';
			//alert(role);
			// if(role.length>0){
			// $('#menu').show();
			// }
			//var roleId='${sessionScope.roleId}';
			var roleId = '${roleId}';
			//alert(roleId);
			//alert(roleId.length);
			var slideOffeset = -200;
			if (roleId) {
				$('#01').children('.nav-link').children('span.arrow').addClass('open');
				var that = $('#' + roleId).children('.nav-link'); //a href      
				var liShiyebu = that.closest('.nav-item'); //li
				if (roleId.length == 4) {
					var companyId = roleId.substring(0, 2);
					//alert(companyId);
					var companyHref = $('#' + companyId).children('.nav-link');
					var liCompany = companyHref.closest('.nav-item'); //li
					showShiyebu(liCompany, companyId);
					var pSub = liCompany.parent().closest('.sub-menu');
					pSub.show();
					var ppSub = pSub.parent().closest('.sub-menu');
					if (ppSub)
						ppSub.show();
					//showStation(liShiyebu,roleId);

					that = $('#' + roleId).children('.nav-link'); //a href 
					//alert($('#'+roleId).length);
					liShiyebu = that.closest('.nav-item'); //li

					liShiyebu.parent().closest('li').addClass('open');
					//事业部内部箭头打开
					that.children('span.arrow').addClass('open');
					liShiyebu.parent().closest('li').children('.nav-link').children('span.arrow').addClass('open');
				}
				if (liShiyebu) {
					var pSub = liShiyebu.parent().closest('.sub-menu');
					pSub.show();
					var ppSub = pSub.parent().closest('.sub-menu');
					if (ppSub)
						ppSub.show();
				}
				if (roleId.length == 2) {
					var sub = that.next();
					if (sub)
						sub.show();
				}
				if (roleId.length == 1) {
					var sub = that.next();
					if (sub)
						sub.show();
				}

			}


			//我这里重写li的parent把 open 的li 关掉，现在关不掉
			function clear(parent) {
				parent.children('li.open').children('a').children('.arrow').removeClass('open');
				parent.children('li.open').children('.sub-menu:not(.always-open)').slideUp(0);
				parent.children('li.open').removeClass('open');
			}

			function showShiyebu(liShiyebu, id) {
				$.ajax({
					type: 'POST',
					url: 'getShiyebu?id=' + id,
					data: '',
					success: function(data) {
						//alert(data);
						var companyHead = '<ul class="sub-menu" style="display: block;">';
						var companyLi1 = '<li id="';
						var companyLi2 =
							'" class="nav-item"><a href="javascript:;" class="nav-link"><i class="fa fa-folder"></i><span class="title" onclick="clickShiYeBu(this);">';
						var companyUl = companyHead;
						for (var i in data) {
							var item = data[i];
							companyUl += companyLi1 + item.companyId + companyLi2 + item.name +
								'</span><span onclick="ajaxClick(this);" class="arrow ajax"></span></a></li>';
						}
						companyUl += '</ul>';
						liShiyebu.append(companyUl);
					}
				});
			}

			$('span.ajaxCompany').on('click', function(e) {
				//alert(1);
				var liShiyebu = $(this).closest('.nav-item'); //li
				var parent = liShiyebu.parent(); //ul sub-menu
				var id = liShiyebu.attr('id');
				$.ajax({
					url: "index/saveOpen?roleId=" + id
				});
				//alert(id);
				var that = $(this).closest('.nav-item').children('.nav-link');
				//alert($(this).remove());
				var the = that;
				var it = $(this);

				var hasSubMenu = that.next().hasClass('sub-menu');
				var sub = that.next();
				//var menu=$('sub-menu', the)

				if (hasSubMenu) {

					return;
				} else {
					clear(parent);
					//alert(liShiyebu.attr('id'));
					showShiyebu(liShiyebu, id);

					$('.arrow', the).addClass("open");

				}
				App.scrollTo(the, slideOffeset);

			});

			var ajaxClick = function(span) {
				//alert(span);
				//$('span.ajax').on('click',function (e){
				//e.preventDefault();
				//alert(App);
				var menu = $('.page-sidebar-menu');
				var liShiyebu = $(span).closest('.nav-item'); //li
				var parent = liShiyebu.parent(); //ul sub-menu
				var id = liShiyebu.attr('id');

				$.ajax({
					url: "index/saveOpen?roleId=" + id
				});


				var that = $(span).closest('.nav-item').children('.nav-link');
				//alert($(this).remove());
				var the = that;
				var it = $(span);

				var hasSubMenu = that.next().hasClass('sub-menu');
				var sub = that.next();

				/**/
				if (hasSubMenu) {
					if (sub.is(":visible")) {
						sub.slideUp(0);
						$('.arrow', the).removeClass("open");
					} else {
						clear(parent);
						liShiyebu.addClass('open');
						sub.slideDown(0);
						$('.arrow', the).addClass("open");
					}

					return;
				} else {
					clear(parent);
					//alert(liShiyebu.attr('id'));
					showStation(liShiyebu, id, it);
					$('.arrow', the).addClass("open");

				}
				App.scrollTo(the, slideOffeset);
				//that.addClass('nav-toggle');
			}

			var ul = '<ul class="sub-menu" style="display:block;">';
			var li1 = '<li class="nav-item" ><a class="nav-link "><i class="fa fa-rss"></i><span class="title" data-val="';
			var li2 = '" ';
			var li3 = ' onclick="clickProjectNo(this)">';

			function showStation(liShiyebu, id, it) {
				$.ajax({
					type: 'POST',
					url: 'permission/getStation?id=' + id,
					data: '',
					success: function(data) {
						if (data.length == 0) {
							if (it) {
								it.remove();
							}

							return;
						}
						var head = ul;
						for (var i in data) {
							var item = data[i];
							//alert(item.longitude);
							var projectName = item.projectName;
							//if(projectName.length>6) projectName=projectName.substring(0,8);
							head += li1 + item.projectNo + li2 + ' data-val1="' + item.longitude + '" data-val2="' + item.latitude + '" ' +
								li3 + projectName + '</span>';
							var color = '';
							var status = item.meterStatus1;
							if (status == 1) {
								color = 'color:#46e046;';
							}
							if (status == 2) {
								color = 'color:yellow;';
							}
							if (status == 3) {
								color = 'color:red;';
							}
							head += '<i class="fa fa-cloud" style="' + color + '"></i></li>';

						}
						liShiyebu.append(head + '</ul>');
						liShiyebu.addClass('open');

					}
				});
			}

			$('#73').on('click', function(e) {
				//alert($(this).hasClass('open'));
				//var open=$(this).hasClass('open');
				//if(open)
				{
					//清空roleId
					$.ajax({
						url: "index/saveOpen?roleId="
					});
				}
			});
			
			var query2NotExist=true;
			if(typeof query2 != 'undefined' && query2 instanceof Function){
				query2NotExist=false;
			}

			$('.title').on('click', function(e) {
				if(query2NotExist){
					if (role && role.length > 0) {
						//alert($(this).parent().attr('href'));
						//window.location.href="jsp/emeter/listNB.jsp";
						return;
					}

					if ($(this).closest('.nav-item').attr('id') == '73') {
						return;
					}
					e.preventDefault();
					//window.location.href="jsp/emeter/listNB.jsp";
					//alert($(this).html());
					var title = $(this).html();
					$('.caption-subject').html(title + '');

					var roleId = $(this).closest('li.nav-item').attr('id');
					//alert(roleId);
					if (roleId == '01') {
						$('#companyId').val('');
					} else {
						$('#companyId').val(roleId);
					}
					$('#projectNo').val('');
					$.query();
					if(typeof query != 'undefined' && query instanceof Function){
						query();
					}
				}
			});

			function clickShiYeBu(ele) {
				if(query2NotExist){
					var title = $(ele).html();
					$('.caption-subject').html(title + '');
					var roleId = $(ele).closest('li.nav-item').attr('id');
					//alert(roleId);
					$('#companyId').val(roleId);
					$('#projectNo').val('');
					$.query();
					if(typeof query != 'undefined' && query instanceof Function){
						query();
					}
				}
			}

			function clickProjectNo(ele) {
				if(query2NotExist){
					var title = $(ele).html();
					$('.caption-subject').html(title + '');
					var span = $(ele);
					//alert($('#baiducontainer').length);
					$('#projectNo').val(span.data('val'));
					$.query();
					if(typeof query != 'undefined' && query instanceof Function){
						query();
					}
					if ($('#baiducontainer').length > 0) {
						var longitude = span.data('val1');
						var latitude = span.data('val2');
						//alert(longitude);
						//alert(latitude);
						showStation4(longitude, latitude);
						$('#longitude').val(longitude);
						$('#latitude').val(latitude);
					}
				}
			}

			$('.top1').on('click', function(e) {
				//alert(1);
				$.ajax({
					url: "index/saveOpType?opType=1"
				});
				$('.page-logo>ul>li>a').each(function(){
					//alert($(this).text())
				});
				//alert($(this).text());
				if($(this).text().indexOf('地图定位')>0||$(this).text().indexOf('首页')>0){
					ls.setItem('index',$(this).text());
					
					//alert($(this).text());
				}else{
					var amenu=$(this).parent().parent().parent();
					//var amenu=$(this).closest('li');
					//alert(amenu.children('a').text());
					ls.setItem('index',amenu.children('a').text());
				}
				//alert(ls.getItem('index'))
				if($(this).data('url').indexOf('https')==-1){
					window.location.href='<%=basePath%>/'+$(this).data('url');
				}else{
				//	window.location.href=$(this).data('url');
			        window.open($(this).data('url'),'_blank');
                               
				}
				
			});

			$('.toHome').on('click', function(e) {
				ls.setItem('index',$(this).text());
				$.ajax({
					url: "index/saveOpType?opType=0"
				});
				
				window.location.href='<%=basePath%>/jsp/index.jsp';
			});
			
			



			
		</script>
	</body>
</html>


<script>

</script>
