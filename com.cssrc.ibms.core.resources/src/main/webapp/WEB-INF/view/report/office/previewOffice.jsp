<%@page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.zhuozhengsoft.pageoffice.*,java.awt.*" %>  
<%@ page import="com.zhuozhengsoft.pageoffice.wordwriter.WordDocument" %>  
<%@ taglib uri="http://java.pageoffice.cn" prefix="po" %> 
<%
String cpath = request.getContextPath();
String sysdate = (String)request.getAttribute("sysdate");
String userId=  (String)request.getAttribute("userId");

PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);  
poCtrl.setServerPage( cpath + "/poserver.do"); //此行必须  

//设置界面样式
poCtrl.setMenubar(false);//隐藏菜单栏
poCtrl.setTitlebar(false);
//自定义工具栏
poCtrl.addCustomToolButton("打印", "printDialog()", 6);
poCtrl.addCustomToolButton("-", "", 2);
poCtrl.addCustomToolButton("全屏/还原", "doSetFullScreen()", 4);

//打开office文件
String opath = (String)request.getAttribute("opath");


//poCtrl.webOpen("/"+opath, OpenModeType.docNormalEdit,userId);
poCtrl.webOpen(cpath+"/file/read?opath="+opath, OpenModeType.docNormalEdit,userId);

poCtrl.setTagId("PageOfficeCtrl1"); //此行必须 


%>
<html>
<head>
<meta http-equiv=Pragma content=no-cache>
<meta http-equiv=Cache-Control content=no-cache>
<meta http-equiv=Expires content=0>
<meta http-equiv="Content-Type" content="text/html;" />
<title>office报表模板</title>
<%@include file="/commons/include/form.jsp" %>
<script type="text/javascript">
function init(){
	//保存文档
	//document.getElementById("PageOfficeCtrl1").WebSave();
	//开启编辑保护
	if(${ext!=null}){
		var retVal=new Object();
		retVal.result="1";
		retVal.filename="${officeTemplate.dataEntry}.${officeTemplate.title}${dataid}.doc";
		window.returnValue=retVal;
		window.close();
	}
}
//另存为
function saveAsDialog() {
	document.getElementById("PageOfficeCtrl1").ShowDialog(3);
}
//全屏
function doSetFullScreen() {
	document.getElementById("PageOfficeCtrl1").FullScreen = !document
			.getElementById("PageOfficeCtrl1").FullScreen;
}
//打印
function printDialog() {
	document.getElementById("PageOfficeCtrl1").ShowDialog(4); 
}
//保存
function saveDialog() {
	if(confirm("是否保存文档？")){
		document.getElementById("PageOfficeCtrl1").WebSave();
	}
}
</script>
</head>


<body id="bodyMainDiv">
<form name="officeDocForm" method="post"  id="officeDocForm">
	<input type="hidden" id="tableId" name="tableId" value="${officeTemplate.dataEntry}">
	<input type="hidden" id="title" name="title" value="${officeTemplate.title}">
	<input type="hidden" id="dataid" name="dataid" value="${dataid}">
	
	<po:PageOfficeCtrl id="PageOfficeCtrl1" />
</form>
<!-- <div class="l-tab-loading" style="display: block;"/> -->
</body>

</html>