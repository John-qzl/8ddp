<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<%@ page import="com.ibms.core.util.AppUtil" %>
<%@ taglib uri="http://www.cssrc.com.cn/functions" prefix="ibms"%>
<ibms:sysparam paramname="CALL_FILE_PATH" alias="CALL_FILE_PATH"></ibms:sysparam>
<ibms:sysparam paramname="CALL_FILE_TEMP_PATH" alias="CALL_FILE_TEMP_PATH"></ibms:sysparam>
<ibms:sysparam paramname="CALL_FILE_SYS_TEMP_PATH" alias="CALL_FILE_SYS_TEMP_PATH"></ibms:sysparam>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户上传图片</title>
<%@include file="/commons/include/get.jsp" %>
<script src="${ctx}/jslib/jquery/jquery-1.7.2.min.js"></script>
<script src="${ctx}/jslib/jquery/jquery.form.js"></script>
<script src="${ctx}/jslib/jquery/jquery.Jcrop.js"></script>
<script type="text/javascript">
  var jcrop_api;
  var imgSrc;
  var x,y,w,h;
  //jQuery(function($){
  //后缀
  var extName = "jpg";
  $(function() {
	    var boundx,boundy,
	        $preview = $('#preview-pane'),
	        $pcnt = $('#preview-pane .preview-container'),
	        $pimg = $('#preview-pane .preview-container img'),
	        xsize = $pcnt.width(),
	        ysize = $pcnt.height();
	    $('#target').Jcrop({
	      onChange: updatePreview,
	      onSelect: updatePreview,
	      aspectRatio: 100/40
	    },function(){
	      var bounds = this.getBounds();
	      boundx = bounds[0];
	      boundy = bounds[1];
	      jcrop_api = this;
	      $preview.appendTo(jcrop_api.ui.holder);
	    });
	    function updatePreview(c)
	    {
	      x = c.x;
	      y = c.y;
	      w = c.w;
	      h = c.h;
	      var bounds = jcrop_api.getBounds();
	      boundx = bounds[0];
	      boundy = bounds[1];
		 if (parseInt(c.w) > 0)
		{
		  var rx = xsize / c.w;
		  var ry = ysize / c.h;
		
		  $pimg.css({
		    width: Math.round(rx * boundx) + 'px',
		    height: Math.round(ry * boundy) + 'px',
		    marginLeft: '-' + Math.round(rx * c.x) + 'px',
		    marginTop: '-' + Math.round(ry * c.y) + 'px'
		  });
		}
	    };
    });
  
  function upload(imgObj){
	  
	  var file = document.getElementById("file");
 		if(imgObj.value == "" ||imgObj.value == undefined){
 			return;
 		}
 		//后缀
 		var fileNameObj =  imgObj.value;
 		extName =fileNameObj.substring(fileNameObj.lastIndexOf(".")+1);
 
 		document.getElementById('textfield').value=imgObj.value;
	   var options = {
				 url:'${ctx}/oa/system/sysUser/uploadPic.do',
				 type:'POST',
				 scope : this,
				 success:function(d){
					  	imgSrc = "${CALL_FILE_TEMP_PATH}"+d.fileName;
					   jcrop_api.setImage(imgSrc);
					   document.getElementById("imgPreview").src = imgSrc;
				},
				 failure : function() {
			alert("调用upload方法错误！");
			}
		 };
		 $('#ulform').ajaxSubmit(options);
		 
		
		 
	};
	
	function onload()
	{
		window.parent.signphotoid = "";
		window.parent.originalsignphotoid = "";
		<c:choose>
	  		<c:when test="${originalsignphotopath==''}">
	  			imgSrc="${CALL_FILE_SYS_TEMP_PATH}default_image_pool.jpg";
	  		</c:when>
	  		<c:otherwise>
	  			imgSrc="${CALL_FILE_PATH}${originalsignphotopath}";
	  		</c:otherwise>
  		</c:choose>
	};

    function docut(){
    	if(x == undefined|| y ==undefined||
    			w ==0 || w == undefined||
    			h ==0 || h == undefined){
    		alert( "所选区域错误！");
    		return;
    	}
    	var userId = ${userId};
  	   	var options = {
			 waitMsg : "正在提交数据...",
			 url:'${ctx}/oa/system/sysUser/doCut.do?isSignUpload=true&x='+this.x+'&y='+this.y+'&w='+this.w+'&h='+this.h+'&imgSrc='+this.imgSrc+'&userId='+userId,
			 type:'POST',
			 scope : this,
			 success:function(d){
				window.parent.signphotoid = d.photoid;
				window.parent.originalsignphotoid = d.originalphotoid;
				var tempphotopath = d.tempphotopath;
				tempphotopath = "${CALL_FILE_TEMP_PATH}"+tempphotopath+"."+extName+"?radom="+Math.random();
				window.parent.closeTabForSign("SysUserUploadSignPic",tempphotopath);
			 },
			 failure : function() {
			 alert("调用checkIfExist方法错误！");
			}
		 };
		$('#ulform').ajaxSubmit(options);
    }
</script>
<link rel="stylesheet" href="${ctx}/css/system/css/jquery.Jcrop.css" type="text/css" />
<f:link href="from-jsp.css"></f:link>
</head>
<body onload ="onload()">
<div class="container">
	  <div class="file-box">
		  <form id="ulform"  runat="server" method="post">
			<input type='text' name='textfield' id='textfield' class='txt' />  
			<input type='button' class='btn' value='选择图片' />
			<input type="file" name="imgFile" accept="image/*" class="file" id="fileField" size="28" onchange="upload(this)" />
			<input type="button" value = "确定" class="btn" onclick="docut()"/>
		  </form>
	  </div>
	  
	  <div class="file-box">
	 	 <c:choose>
	  		<c:when test="${originalsignphotopath==''}">
	  			 <img  id="target" class="source-img" src="${CALL_FILE_SYS_TEMP_PATH}default_image_pool.jpg" />
	  		</c:when>
	  		<c:otherwise>
	  			 <img  id="target" class="source-img" src="${CALL_FILE_PATH}${originalsignphotopath}" />
	  		</c:otherwise>
	  	</c:choose>
	    
	  </div>
	  
	  <div id="preview-pane">
	  	<div class="preview-container">
		  	<c:choose>
		  		<c:when test="${signphotopath==''}">
		  			 <img class="jcrop-preview" src="${CALL_FILE_SYS_TEMP_PATH}default_image_pool.jpg" id="imgPreview" width="100px" height="100px"/>
		  		</c:when>
		  		<c:otherwise>
		  			<img class="jcrop-preview" src="${CALL_FILE_PATH}${signphotopath}" id="imgPreview" width="100px" height="100px"/>
		  		</c:otherwise>
		  	</c:choose>
			
		</div>
	  </div>
</div>
</body>
</html>


