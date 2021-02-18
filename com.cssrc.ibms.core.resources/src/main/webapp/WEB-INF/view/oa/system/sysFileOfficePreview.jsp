<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.zhuozhengsoft.pageoffice.*,java.awt.*" %>
<%@ taglib uri="http://java.pageoffice.cn" prefix="po" %>

<%
    String contextPath = request.getContextPath();
	String fileId = (String)request.getAttribute("fileId");
	String fileName =(String) request.getAttribute("fileName");
	String filePath = (String)request.getAttribute("filePath");
	String path = (String)request.getAttribute("path");
	
	String fileType =(String) request.getAttribute("fileType");
	
    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>打开文档路径："+filePath);
    
    OpenModeType modelType = OpenModeType.docNormalEdit;
   	if("ppt".equals(fileType) || "pptx".equals(fileType)) modelType = OpenModeType.pptNormalEdit;
   	if("xls".equals(fileType) || "xlsx".equals(fileType)) modelType = OpenModeType.xlsNormalEdit;
   	if("doc".equals(fileType) || "docx".equals(fileType)) modelType = OpenModeType.docNormalEdit;
    
	PageOfficeCtrl poCtrl1 = new PageOfficeCtrl(request);
	poCtrl1.setServerPage(contextPath+"/poserver.do"); //设置服务器页面
	
	poCtrl1.setCustomToolbar(true);
	poCtrl1.setTitlebar(false);
	poCtrl1.setMenubar(false);
	
	//poCtrl1.addCustomToolButton("保存", "Save()", 11);
	//poCtrl1.addCustomToolButton("-", "", 2);
	poCtrl1.addCustomToolButton("打印", "Print()", 6);
	poCtrl1.addCustomToolButton("-", "", 0);
	poCtrl1.addCustomToolButton("全屏/还原", "SetFullScreen()", 4);
	//设置保存页面
	// poCtrl1.setSaveFilePage(contextPath+"/oa/system/sysFile/save.do?fileId="
	//	+fileId+"&fileName="+fileName+"&fileType="+fileType+"&path="+path); 
	poCtrl1.webOpen(filePath, modelType, "");
	poCtrl1.setTagId("PageOfficeCtrl1");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Insert title here</title>
<script type="text/javascript" src="<%=contextPath%>/jslib/jquery/jquery.js"></script>
<script type="text/javascript">
 
  //打印
  function Print(){
	  document.getElementById("PageOfficeCtrl1").ShowDialog(4);
  };
  
  //保存
  function Save(){
	  document.getElementById('PageOfficeCtrl1').WebSave();
  };
  
  //全屏
  function SetFullScreen(){
      document.getElementById("PageOfficeCtrl1").FullScreen = !document.getElementById("PageOfficeCtrl1").FullScreen;
  };
  
</script>
</head>
<body>
	<po:PageOfficeCtrl id="PageOfficeCtrl1"/>
</body>
</html>