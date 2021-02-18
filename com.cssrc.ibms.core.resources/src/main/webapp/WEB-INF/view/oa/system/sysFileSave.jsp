<%@ page language="java" import="java.util.*,com.zhuozhengsoft.pageoffice.*" pageEncoding="utf-8"%>
<%@page import="com.cssrc.ibms.core.util.common.CommonTools"%>
<%@page import="com.cssrc.ibms.core.util.appconf.AppUtil"%>
<%@page import="com.cssrc.ibms.core.util.file.FileOperator"%>
<%@page import="com.cssrc.ibms.core.util.file.FileUtil"%>
<%@page import="com.cssrc.ibms.system.controller.SysFileController"%>
<%@page import="java.io.File"%>
<%
	String fileId = CommonTools.Obj2String(request.getAttribute("fileId"));
	String fileName = CommonTools.Obj2String(request.getAttribute("fileName"));
	String fileType = CommonTools.Obj2String(request.getAttribute("fileType"));
	String path = CommonTools.Obj2String(request.getAttribute("path"));
	Boolean isNoGroup = path.startsWith("group");//判断是否为分布式文件存储

	String filePath = AppUtil.getAttachPath() + File.separator + "preview" + File.separator + fileName;
	
	if(isNoGroup){
		//如果是分布式文件存储，临时文件存放在项目的attachFile文件夹下
		filePath = FileUtil.getRootPath()+File.separator+"attachFile"+File.separator+"fileView"+fileId+"."+fileType;
	}
	
	String origFileName = CommonTools.Obj2String(request.getAttribute("origFileName"));
	String origFilePath = "";
	if(origFileName!=""){
		origFilePath = AppUtil.getAttachPath() + File.separator + origFileName;
	}
	
	String keyName = CommonTools.Obj2String(request.getAttribute("keyName"));
	
	FileSaver fs = new FileSaver(request, response);
	fs.saveToFile(filePath);
	
	if(isNoGroup){//若为分布式文件存储，在保存成功后需要做特殊处理
		SysFileController.updateFastDFSFile(fileId, path, fileType);
	}else{
		if(!fileName.equals(origFileName)){ //文件加密处理
			//step1: 删除原始文件
			File origFile = new File(origFilePath);
			if(origFile.exists()) origFile.delete();
			//step2: 将当前操作文件，另存一份为原始文件
			FileOperator.copyFile(filePath, origFilePath);
			//step 3:生成加密文件
			FileOperator.keyName = keyName;
			FileOperator.encrypt(filePath, origFilePath);
		}
	}
	
	fs.close();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>文件保存</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

  </head>
  
  <body>
  </body>
</html>
