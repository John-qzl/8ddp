<%@page import="com.cssrc.ibms.core.util.http.RequestUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/commons/include/html_doctype.html"%>
<%
	Long typeId =RequestUtil.getLong(request, "typeId",0) ; // 附件分类编号
	String uploadType = RequestUtil.getString(request, "uploadType");
	String fileFormates = RequestUtil.getString(request, "fileFormates");
%>
<html>
  <head>
  	<%@include file="/commons/include/form.jsp" %>
	<title>附件上传</title>
  </head>
  	   <script type="text/javascript" src="${ctx}/media/swf/fileupload/swfobject.js"></script>
       <script type="text/javascript">
       var uploadPath="${ctx}/oa/system/sysFile/fileUpload.do;jsessionid=<%=session.getId()%>?fileTypeId=<%=typeId%>" ;
       var delPath="${ctx}/oa/system/sysFile/deleteFile.do;jsessionid=<%=session.getId()%>" ;
       var fileExt="document@*.doc;*.docx#images@*.jpg;*.png,*.gif";
       var fileFormates = '<%=fileFormates%>';
       var uploadType = '<%=uploadType%>';
       var mark = false;
       var confObj=null;
       function initFlashUpload(){
           confObj = window.dialogArguments;
    	   
    	   if(confObj!=undefined && confObj!=null && confObj!=""){      //模态窗口的接收方法
    		   if(confObj.uploadType!=null&&confObj.uploadType=="pictureShow"){
    			   fileFormates = confObj.fileFormates;
    			   uploadPath +="&uploadType=pictureShow&fileFormates="+fileFormates;                 //有特殊符号时要转义 如 &
        		   uploadPath = encodeURIComponent(uploadPath); 
        		   mark = true; 
    		   }   		  
    	   }else{         //OPEN方式打开的 窗口接收内容
    		   if(uploadType!=null && uploadType=="pictureShow"){
        		   uploadPath +="&uploadType=pictureShow&fileFormates="+fileFormates;                  //有特殊符号时要转义 如 &
        		   uploadPath = encodeURIComponent(uploadPath); 
        	   }
    		   mark = false; 
    	   }
    	   
    	   //设置swfobject对象参数
      	   var swfVersionStr = '10.0.0';
           var xiSwfUrlStr = '${ctx}/media/swf/fileupload/playerProductInstall.swf';
           var flashvars = {};
           flashvars.uploadPath=uploadPath;
           flashvars.delPath=delPath;
           flashvars.fileExt=fileExt;
           flashvars.fileFormates=fileFormates;
           var params = {};
           params.quality = 'high';
           params.bgcolor = '#ffffff';
           params.allowscriptaccess = 'sameDomain';
           params.allowfullscreen = 'true';
           var attributes = {};
           attributes.id = 'flexupload';
           attributes.name = 'flexupload';
           attributes.align = 'middle';
           swfobject.embedSWF( '${ctx}/media/swf/fileupload/flexupload.swf', 'flashContent', 
              '100%', '100%', swfVersionStr, xiSwfUrlStr, flashvars, params, attributes);
           swfobject.createCSS('#flashContent', 'display:block;text-align:left;');
       }
    	function winClose(r){
    			if(r){
					var arry = $.parseJSON(r);
					if(mark  || confObj!=null){
						window.returnValue=arry;
					}else{
						window.opener.returnValue.call(this, arry);
					}
				}
				window.close();
		};
    	
    	$(function(){
    		initFlashUpload();
    	})
	</script>
  	<body>
  		<div id="flashContent" >
			<h1>找不到上传控件</h1>
			<p><a href="http://www.adobe.com/go/getflashplayer"><img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" /></a></p>
		</div>
	  
	</body>
</html>