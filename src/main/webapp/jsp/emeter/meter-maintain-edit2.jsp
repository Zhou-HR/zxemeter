<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <title>电表委派</title>

        <script src="assets/others/script/ajax-dict-select.js" type="text/javascript"></script>
		<script type="text/javascript" src="assets/others/script/validation-ext.js"></script>
		<script>
		if(IEVersion()!=-1){alert('IE浏览器不支持图片压缩上传，建议下载谷歌浏览器！')}
		var imgCount=$('.mpic').length;
		//alert(imgCount);
		
		function del(id,ele){
			//
			//alert($('#'+id).remove())
			$('#'+id).remove();
			$(ele).remove();
			$('#imgs').val($('#imgs').val().replace(",'"+id+"'",''));
			imgCount--;
		}
	
		
		function compress() { 
			if(IEVersion()!=-1){alert('IE浏览器不支持图片压缩上传，建议下载谷歌浏览器！');return;}
			//alert(window.atob);
			//alert(imgCount);
			if(imgCount>=5){
				alert('最多5张照片');
				return false;
			}
			let fileObj = document.getElementById('file1').files[0] //上传文件的对象
			var filepath=$("#file1").val();
			var extStart=filepath.lastIndexOf(".");
			var ext=filepath.substring(extStart,filepath.length).toUpperCase();
			if(ext!=".BMP"&&ext!=".PNG"&&ext!=".GIF"&&ext!=".JPG"&&ext!=".JPEG"){
				alert("图片限于png,gif,jpeg,jpg,bmp格式");
				return false;
			}
			var size=fileObj.size/1024;
			if(size>6000){
				alert('文件大小不能大于6M');
				return false;
			}
			
			
			let reader = new FileReader()
			reader.readAsDataURL(fileObj)
			reader.onload = function(e) {
				
				let image = new Image() //新建一个img标签（还没嵌入DOM节点)
				image.src = e.target.result
				image.onload = function() {
					
					//alert(size);
					let canvas = document.createElement('canvas'), 
					context = canvas.getContext('2d'),
					imageWidth = image.width / 2,    //压缩后图片的大小
					imageHeight = image.height / 2,
					data = ''

					canvas.width = imageWidth
					canvas.height = imageHeight

					context.drawImage(image, 0, 0, imageWidth, imageHeight)
					data = canvas.toDataURL('image/jpeg',0.3)
					
					//alert(data.length) 93wan
					
					var form=document.querySelector("#edit-form");
					var formData = new FormData(form);
					var file=document.getElementById("file1").files[0];
					
					
					//alert(size)
					if(size>1000){//大于1M，压缩
						var blob1=convertBase64UrlToBlob(data);
						//alert(blob1);
						formData.set("files",blob1);
						
					}
					//alert($.ajax);
					//alert(formData);
					//formData.set("filetype",ext);
					
					//var windowURL = window.URL || window.webkitURL;
					//var dataURL = windowURL.createObjectURL(file);
					
					//alert($.ajax);
					
					$.ajax({
						type: 'POST',
						url: 'meterMaintain/upload',
						data: formData,
						contentType: false,
						processData: false,
						success: function(data1) {
							if(data1.success){
								alert('上传成功');
								//压缩完成 
								//document.getElementById('img1').src = data
								var a=new Image();
								a.src=data1.message;
								a.class='mpic';
								//a.id=formData.get('id');
								a.id=data1.path;
								//var id=formData.get('id');
								//alert(a.id);
								a.style.width='100px';
								a.style.cursor='pointer';
								a.onclick=function(){window.open(data1.message,'_blank');}
								//$('#img').append("<img src='"+data1.message+"' id='"+'345'+"'>");
								$('#img').append(a);
								
								$('#img').append("<input type='button' onclick='del("+a.id+",this)' value='删除'>");
								
								$('#imgs').val($('#imgs').val()+",'"+data1.path+"'");
								imgCount++;
							}
							else{
								alert(data1.message);
							}
							
						}
					});					

					
				}
			}
		}
		
		function convertBase64UrlToBlob(urlData){
			//alert(window.atob);
			var bytes=window.atob(urlData.split(',')[1]);        //去掉url的头，并转换为byte
			//alert(2)
			//处理异常,将ascii码小于0的转换为大于0
			var ab = new ArrayBuffer(bytes.length);
			var ia = new Uint8Array(ab);
			
			for (var i = 0; i < bytes.length; i++) {
				ia[i] = bytes.charCodeAt(i);
			}
			
			return new Blob( [ab] , {type : 'image/png'});
		}
		 
		
		function nowtime(){//将当前时间转换成yyyymmdd格式
			var mydate = new Date();
			var str = "" + mydate.getFullYear()+'-';
			var mm = mydate.getMonth()+1
			if(mydate.getMonth()>9){
			 str += mm;
			}
			else{
			 str += "0" + mm;
			}
			str+='-';
			if(mydate.getDate()>9){
			 str += mydate.getDate();
			}
			else{
			 str += "0" + mydate.getDate();
			}
			return str;
		}
		var ymd=nowtime();
		$('#dealDate').val(ymd);
		
		function IEVersion() {
            var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串  
            var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1; //判断是否IE<11浏览器  
            var isEdge = userAgent.indexOf("Edge") > -1 && !isIE; //判断是否IE的Edge浏览器  
            var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
            if(isIE) {
                var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
                reIE.test(userAgent);
                var fIEVersion = parseFloat(RegExp["$1"]);
                if(fIEVersion == 7) {
                    return 7;
                } else if(fIEVersion == 8) {
                    return 8;
                } else if(fIEVersion == 9) {
                    return 9;
                } else if(fIEVersion == 10) {
                    return 10;
                } else {
                    return 6;//IE版本<=7
                }   
            } else if(isEdge) {
                return 'edge';//edge
            } else if(isIE11) {
                return 11; //IE11  
            }else{
                return -1;//不是ie浏览器
            }
        }
	</script>
    </head>
    <body>
        <section class="content">
            <div class="box box-primary">
                <form id="edit-form" class="form-horizontal" action="${empty entity? 'meter/updateMaintainResult' : 'meter/updateMaintainResult'}" method="post"
                      data-fv-framework="bootstrap">
                    <c:if test="${not empty entity}">
                        <input type="hidden" name="id" value="${entity.id}"/>
                    </c:if>
                    <div class="box-body">
						
                        <div class="form-group">
                            <label class="col-sm-2 control-label">表号</label>
                            <div class="col-sm-6">
                                <input type="text" readonly class="form-control" name="meterNo" value="${entity.meterNo}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">发起人</label>

                            <div class="col-sm-6">
                                <input type="text" readonly class="form-control" name="senderName" value="${entity.senderName}">
								<input type="hidden" readonly class="form-control" name="sender" value="${entity.sender}">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">委派人员</label>

                            <div class="col-sm-6">
                                <input type="text" readonly class="form-control" name="receiverName" value="${entity.receiverName}">
                            </div>
                        </div>
					   <div class="form-group">
							<label class="col-sm-2 control-label">委派日期</label>

							<div class="col-sm-6">
								<input type="text" readonly class="form-control" id="date" name="sendDate" value="${entity.sendDate}">
							</div>
						</div>
                        <div class="form-group">
							<label class="col-sm-2 control-label">委派原因</label>

							<div class="col-sm-6">
								<input type="text" readonly  class="form-control" name="reason" data-fv-notempty="true" value="${entity.reason}">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">处理日期</label>

							<div class="col-sm-6">
								<input type="text" readonly class="form-control" id="dealDate" name="dealDate" value="${entity.dealDate}">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">处理结果(200字)</label>

							<div class="col-sm-6">
							<!--
								<input type="text" class="form-control" name="dealResult" value="${entity.dealResult}">
							-->
								<textarea rows="3" data-fv-notempty="true" name="dealResult" maxlength="200" style="width:419px;">${entity.dealResult}</textarea>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">上传照片(最大6M)</label>

							<div class="col-sm-6">
								<input type="file" id="file1" name="files" onchange="compress();" accept="image/jpeg,image/JPG,image/png,image/bmp,image/gif">
								<input type='hidden' id='imgs' name='imgs'>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">已上传照片(最多5张)</label>

							<div class="col-sm-6" id="img">
								<c:forEach var="node" items="${list}" varStatus="status">
								 <img id='${node.id}' src='${node.path}' class='mpic' onclick="window.open(this.src,'_blank')" style="width:100px;cursor:pointer;">
								 <input type='button' onclick="del('${node.id}',this)" value='删除'>	
								</c:forEach>
							</div>
						</div>
                    </div>
                    
                </form>
            </div>
        </section>
	
    </body>
</html>
