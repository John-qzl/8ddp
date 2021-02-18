<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.zhuozhengsoft.pageoffice.*,java.awt.*" %>  
<%@ page import="com.zhuozhengsoft.pageoffice.wordwriter.WordDocument" %>  
<%@ page import="com.alibaba.fastjson.JSONArray" %>  

<%@ taglib uri="http://java.pageoffice.cn" prefix="po" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.pageoffice.cn" prefix="po" %> 
<%@ taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@ taglib prefix="ibms" uri="http://www.cssrc.com.cn/paging" %>
<%@ taglib prefix="spr" uri="http://www.springframework.org/tags"%>


<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
String cpath = request.getContextPath();
String sysdate = (String)request.getAttribute("sysdate");
String userId=  (String)request.getAttribute("userId");
PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);  
poCtrl.setServerPage( cpath + "/poserver.do"); //此行必须  
//保存word 服务端路径
poCtrl.setSaveFilePage(cpath + "/oa/system/officeTemplate/saveOfficeFile.do");
//文档编辑权限保护密码
String password=(String)request.getAttribute("password");

poCtrl.setProtectPassword(password);
//设置界面样式
poCtrl.setMenubar(false);//隐藏菜单栏
poCtrl.setTitlebar(false);
//设置页面的显示标题
poCtrl.setCaption("");
//设置模板打开后的回调函数
poCtrl.setJsFunction_AfterDocumentOpened("init()");
//自定义工具栏
poCtrl.addCustomToolButton("保存", "saveDialog()", 1);
poCtrl.addCustomToolButton("-", "", 2);
poCtrl.addCustomToolButton("另存为", "saveAsDialog()", 11);
poCtrl.addCustomToolButton("-", "", 2);
poCtrl.addCustomToolButton("打印", "printDialog()", 6);
poCtrl.addCustomToolButton("-", "", 2);
poCtrl.addCustomToolButton("全屏/还原", "doSetFullScreen()", 4);

//数据word document
WordDocument wordWriter = (WordDocument)request.getAttribute("wordWriter");
//打开模板
String filepath = (String)request.getAttribute("filepath");
String officeFile=(String)request.getAttribute("officeFile");

poCtrl.webOpen(cpath+filepath, OpenModeType.docSubmitForm,userId);
poCtrl.setWriter(wordWriter);
/* 
if(officeFile!=null&&!"".equals(officeFile)){
	poCtrl.setWriter(wordWriter);
}else{
	poCtrl.setWriter(wordWriter);
} */
poCtrl.setTagId("PageOfficeCtrl1"); //此行必须 


%>
<html>
<head>
<meta http-equiv=Pragma content=no-cache>
<meta http-equiv=Cache-Control content=no-cache>
<meta http-equiv=Expires content=0>
<meta http-equiv="Content-Type" content="text/html;" />
<title>office报表模板</title>
<script type="text/javascript" src="${ctx}/js/dynamic.jsp"></script>
<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.js"></script>
<script type="text/javascript">
var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)

function init(){
	//保存文档
	document.getElementById("PageOfficeCtrl1").WebSave();
	//循环书签设置数据
	insertCell();
	//开启编辑保护
	/* 不能加保护，否则会导致所有文档都无法编辑 */
	<%-- document.getElementById("PageOfficeCtrl1").Document.Protect(3,false,"<%=password%>"); --%>
	if(${ext!=null}){
		var retVal=new Object();
		retVal.result="1";
		retVal.filename="${officeTemplate.dataEntry}.${officeTemplate.title}${dataid}.doc";
		
		dialog.get("sucCall")(retVal);
		dialog.close();
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

//循环标签只能用js插入数据
function insertCell(){
	var obj = document.getElementById("PageOfficeCtrl1").Document;
    var drlist = document.getElementById("PageOfficeCtrl1").DataRegionList;
    drlist.Refresh();
	$(${cellData}).each(function(i,cell){
        drlist.GetDataRegionByName(cell.mid).Locate();
		$(cell.mdata).each(function(i,_d){
			obj.Application.Selection.typetext(_d[cell.mfiled]);
			obj.Application.Selection.movedown();//自动移动到下一行
		})
	})
	
}


</script>
</head>


<body id="bodyMainDiv">
<form name="officeDocForm" method="post"  id="officeDocForm">
	<input type="hidden" id="tableName" name="tableName" value="${officeTemplate.dataEntry}">
	<input type="hidden" id="title" name="title" value="${officeTemplate.title}">
	<input type="hidden" id="dataid" name="dataid" value="${dataid}">
	
	<po:PageOfficeCtrl id="PageOfficeCtrl1" />
</form>
<div class="l-tab-loading" style="display: block;"/>
</body>

</html>