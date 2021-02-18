<%@page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.zhuozhengsoft.pageoffice.*,java.awt.*" %>  
<%@ page import="com.zhuozhengsoft.pageoffice.wordwriter.*" %>  
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
    String sysdate = (String)request.getAttribute("sysdate");
	String userId=  (String)request.getAttribute("userId");
	String cpath = request.getContextPath();
	
	WordDocument doc = new WordDocument();
	PageOfficeCtrl poCtrl = new PageOfficeCtrl(request); 
	poCtrl.setWriter(doc);

	//设置连接PageOffice服务器端授权页面
    poCtrl.setServerPage( cpath + "/poserver.do"); //此行必须  
    //String password=(String)request.getAttribute("password");
   	//poCtrl.setProtectPassword(password);

    //设置保存文档的服务器页面
    poCtrl.setSaveFilePage(cpath + "/oa/system/officeTemplate/saveOfficeTemplateFile.do");  
 	//设置界面样式
 	poCtrl.setMenubar(false);//隐藏菜单栏
 	poCtrl.setTitlebar(false);
	//poCtrl.setOfficeToolbars(false);//隐藏Office工具条
	//poCtrl.setCustomToolbar(false);//隐藏自定义工具栏
	//设置页面的显示标题
	poCtrl.setCaption("在线编辑模板");
	poCtrl.setJsFunction_AfterDocumentOpened("init()");

	//自定义工具栏
    poCtrl.addCustomToolButton("打开", "openDialog()", 13);
    poCtrl.addCustomToolButton("-", "", 2);
    poCtrl.addCustomToolButton("另存为", "saveDialog()", 11);
    poCtrl.addCustomToolButton("-", "", 2);
    poCtrl.addCustomToolButton("标签", "showDataColumns()", 3);

    poCtrl.addCustomToolButton("-", "", 2);
    //poCtrl.addCustomToolButton("文档标签", "bookmark()", 3);
    //poCtrl.addCustomToolButton("-", "", 4);
    poCtrl.addCustomToolButton("全屏/还原", "doSetFullScreen()", 4);
	
	// 打开文档
	Object fileName = request.getAttribute("fileName");
	poCtrl.webOpen(cpath + fileName, OpenModeType.docNormalEdit,userId);

    poCtrl.setTagId("PageOfficeCtrl1"); //此行必须 

%>
<html>
<head>
<meta http-equiv=Pragma content=no-cache>
<meta http-equiv=Cache-Control content=no-cache>
<meta http-equiv=Expires content=0>
		
<title>office报表模板</title>
<f:link href="Aqua/css/ligerui-all.css" ></f:link>
<f:link href="tree/zTreeStyle.css" ></f:link>
<f:link href="web.css" ></f:link>
<f:js pre="jslib/lang/common" ></f:js>
<f:js pre="jslib/lang/js" ></f:js>
<f:js pre="jslib/lang/view/oa/system" ></f:js>

<script type="text/javascript" src="${ctx}/js/dynamic.jsp"></script>
<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.js"></script>
<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.validate.min.js"></script>
<script type="text/javascript" src="${ctx}/jslib/jquery/additional-methods.min.js"></script>
<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.validate.ext.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/json2.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/ligerui.min.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerDialog.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerResizable.js" ></script>

<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=officeTemplate"></script>

<script type="text/javascript">
	$(function() {
		function showRequest(formData, jqForm, options) { 
			return true;
		}
		if(${officeTemplate.officeid ==null}){
			valid(showRequest,showResponse,1);
		}else{
			valid(showRequest,showResponse);
		}
		$("Object").height("800px");
		//初始化已有书签
		$(${officeItemList}).each(function(i,item){
			var mark=new Object();
			mark.booMarkId=item.id;
			mark.tableName=item.tableName;
			mark.columnName=item.columnName;
			mark.columnId=item.columnId;
			mark.type=item.type;
			var el = document.createElement("input");  
			el.setAttribute("name","bookmark");
			el.setAttribute("type","hidden");
			el.setAttribute("value",JSON2.stringify(mark));
			document.getElementById("officeTemplateForm").appendChild(el);
		});

	});
	//选择数据类，表作为数据源
	function showDataSource(){
		var url = "dataSource.do?tableName=${officeTemplate.tableName}";
		var rvalue =window.showModalDialog(url,null,'dialogHeight=800px; dialogWidth=800px;help=no;status=no;scroll');
		if(rvalue!=undefined){
			var tabs=rvalue.tabs;
			var tabids=rvalue.tabIds;
			var tabNames=rvalue.tabNames;
			$("#tableName").val(tabNames);
			$("#dataEntry").removeAttr("disabled");
			$("#dataEntry option").remove();
			$(tabs).each(function(i,tab){
				$("#dataEntry").append("<option value='"+tab.NAME_+"'>"+tab.NAME_+"</option>");
			})
		}
	}
	function init(){
		<%-- 不能在这里加保护，否则会导致所有文档都无法在编辑-->
		<%-- document.getElementById("PageOfficeCtrl1").Document.Protect(3,false,"<%=password%>"); --%>
	}
	//通过选取的表，在此基础上选择列作为书签显示
	function showDataColumns() {
		if (!$("#tableName").val()||$("#tableName").val() == "") {
			$.ligerDialog.warn("请先选择数据源！");
			return false;
		} else {
			var urls = "dataColumns.do?tableName=" + $("#tableName").val();
			var params="width=800px;height=800px;frame:no;";
            document.getElementById("PageOfficeCtrl1").ShowHtmlModelessDialog(urls, "",params);
		}
	}

	//在 word 文档中增加一个标签
	function addDocMark(param){
        var tmpArr = param.split("=");
        var bkName = tmpArr[0];
        var content = tmpArr[1];
        var drlist = document.getElementById("PageOfficeCtrl1").DataRegionList;
        drlist.Refresh();
        try {
            document.getElementById("PageOfficeCtrl1").Document.Application.Selection.Collapse(0);
            drlist.Add(bkName, content);
            return "true";
        } catch (e) {
            return "false";
        }
	}
	
	//往form表单中增加元素
	function addFormMark(mark){
		var addFormEl=function(name,value){
			var el = document.createElement("input");  
			el.setAttribute("name",name);
			el.setAttribute("type","hidden");
			el.setAttribute("value",value);
			document.getElementById("officeTemplateForm").appendChild(el);
		};
		var tempArry=mark.split(";");
		var bm=new Object();
		bm.booMarkId=tempArry[0];
		bm.tableName=tempArry[1];
		bm.columnName=tempArry[2];
		bm.columnId=tempArry[3];
		bm.type=tempArry[4];
		addFormEl("bookmark",JSON2.stringify(bm));
	};
	

	
	//保存数据
	function doSubmit(){
		var marks=getDocMarks();
		$("#allmarks").val(marks);
		$("#officeTemplateForm").submit();
		if(__valid.errorList.length==0){
			document.getElementById('PageOfficeCtrl1').WebSave();
		}
	}
	
    //遍历当前Word中已存在的书签名称
    function getDocMarks() {
        var drlist = document.getElementById("PageOfficeCtrl1").DataRegionList;
        drlist.Refresh();
        var bkName = "";
        var bkNames = "";
        for (var i = 0; i < drlist.Count; i++) {
            bkName = drlist.Item(i).Name;
            bkNames += bkName + ",";
        }
        if(bkNames!=""){
        	return bkNames.substr(0, bkNames.length - 1);
        }else{
        	return "";
        }
        
    }

</script>
<script type="text/javascript">
	//对话框
	function saveDialog() {
		document.getElementById("PageOfficeCtrl1").ShowDialog(3);
	}
	function openDialog() {
		document.getElementById("PageOfficeCtrl1").ShowDialog(1);
	}
	//全屏
	function doSetFullScreen() {
		document.getElementById("PageOfficeCtrl1").FullScreen = !document
				.getElementById("PageOfficeCtrl1").FullScreen;
	}
</script>
</head>



<body id="bodyMainDiv">
<form name="officeTemplateForm" method="post"  id="officeTemplateForm" action="save.do">
	<input type="hidden" id ="officeid" name="officeid" value="${officeTemplate.officeid}"/>
	<input type="hidden" id ="filepath" name="filepath"  value="${officeTemplate.filepath}"/>
	<input type="hidden" id ="typeId" name="typeId"  value="${officeTemplate.typeId}"/>
	<input type="hidden" id ="tableName" name="tableName"  value="${officeTemplate.tableName}"/>
	
	<input type="hidden" id ="allmarks" name="allmarks"/>
	
	<div style="padding-top:20px;"></div>
	<div class="DivBlock">
	<div class="DivButton">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
				<td width="10%" align="right">模板名称</td>
				<td width="15%"><input type="text" id="title" name="title" styleClass="MyText"  value="${officeTemplate.title}"/></td>
				<td width="10%" align="right">模板类别</td>
				<td width="15%"><input type="text" id="officeType" name="officeType" styleClass="MyText"  value="${officeTemplate.officeType}"/></td>
				<td width="10%" align="center">
					<input type="button" class="MyButton" value="数据源" onClick="showDataSource();" />
				</td>
				<td width="10%" align="right">数据入口</td>
				<td width="15%" nowrap="nowrap">
					<select id="dataEntry" name="dataEntry" style="width: 150px;">
					<c:forEach items="${tables}" var="item">
						<option value="${item.NAME_}"<c:if test="${officeTemplate.dataEntry==item.NAME_}">selected="selected"</c:if>>			
						${item.NAME_} 
						</option>
					</c:forEach>
					</select>
					<font color="red" size="2px;">(只有通过该父数据类才能生成报告)</font>
				</td>
				<td align="left" width="20%">
					<input type="button" class="MyButton" value="保 存" onClick="doSubmit();" />
					<input type="button" class="MyButton" value="返 回 " onClick="doBack();" />
				</td>
				<td width="20%">&nbsp;</td>
		</tr>
	</table>
	</div>
	</div>
	<div style="padding-top:5px;"></div>
	<br>
	<po:PageOfficeCtrl id="PageOfficeCtrl1" />
</form>

</body>




</html>