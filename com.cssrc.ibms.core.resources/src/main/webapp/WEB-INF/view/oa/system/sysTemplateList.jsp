<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>内部模板管理</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/ImportExportXmlUtil.js"></script>
<script type="text/javascript">
//导出自定义表
function ExportXml(){
	var ids = "";
	$("input[type='checkbox'][class='pk']:checked").each(function(){ 
			var obj=$(this);
			ids+= obj.val()+",";
	
	});
	if(ids==''){
		$.ligerDialog.warn("请选择一条记录进行导出！");
		return;
	}else{
		ids=ids.substring(0,ids.length-1);
	}
	var url = __ctx + "/oa/system/sysTemplate/exportXml.do";
	$("#exportIds").val(ids);
	$("#exportXmlForm").submit();
};
//导入自定义表
function ImportXml(){
	var url= __ctx + "/oa/system/sysTemplate/import.do";
	var winArgs="dialogWidth:600px;dialogHeight:400px;help:0;status:1;scroll:1;center:1";
	var rtn=ImportExportXml.showModalDialog(url,"",winArgs);
	if(rtn){
		window.location.reload(true);
	} 
	
}
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">消息模板管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					
					<div class="group"><a class="link add" href="edit.do">添加</a></div>
					
					<div class="group"><a class="link update" id="btnUpd" action="edit.do">编辑</a></div>
					<div class="group">
						<a class="link download"  href="javascript:;" onclick="ExportXml()">导出</a>
					</div>
					<!-- 
					<div class="group">
						<a class="link upload"  href="javascript:;" onclick="ImportXml()">导入</a>
					</div> -->
					
					<div class="group">
					    <a class="link reset" onclick="$.clearQueryForm()">重置</a>
					</div>
				</div>	
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.do">
					<ul class="row plat-row">
						<li><span class="label">模板名称:</span><input type="text" name="${param['Q_name_SL']}"  class="inputText" /></li>
						<li><span class="label">标题:</span><input type="text" name="${param['Q_title_SL']}"  class="inputText" /></li>
						<li><span class="label">模板用途:</span>
						<select name="Q_useType_S" value="${param['Q_useType_S']}">
							<option value="">全部</option>
							<option value = "1"  <c:if test="${param['Q_useType_S'] == '1'}">selected</c:if>>终止提醒</option>
							<option value = "2"  <c:if test="${param['Q_useType_S'] == '2'}">selected</c:if>>催办提醒</option>
							<option value = "3"  <c:if test="${param['Q_useType_S'] == '3'}">selected</c:if>>审批提醒</option>
							<option value = "4"  <c:if test="${param['Q_useType_S'] == '4'}">selected</c:if>>撤销提醒</option>
							<option value = "5"  <c:if test="${param['Q_useType_S'] == '5'}">selected</c:if>>取消转办</option>
							<option value = "6"  <c:if test="${param['Q_useType_S'] == '6'}">selected</c:if>>沟通提醒</option>
							<option value = "7"  <c:if test="${param['Q_useType_S'] == '7'}">selected</c:if>>归档提醒</option>
							<option value = "8"  <c:if test="${param['Q_useType_S'] == '8'}">selected</c:if>>转办提醒</option>
							<option value = "9"  <c:if test="${param['Q_useType_S'] == '9'}">selected</c:if>>退回提醒</option>
							<option value = "10" <c:if test="${param['Q_useType_S'] == '10'}">selected</c:if>>被沟通人提交</option>
							<option value = "11" <c:if test="${param['Q_useType_S'] == '11'}">selected</c:if>>取消代理</option>
							<option value = "12" <c:if test="${param['Q_useType_S'] == '12'}">selected</c:if>>抄送提醒</option>
							<option value = "13" <c:if test="${param['Q_useType_S'] == '13'}">selected</c:if>>流程节点无人员</option>
							<option value = "14" <c:if test="${param['Q_useType_S'] == '14'}">selected</c:if>>跟进事项预警</option>
							<option value = "15" <c:if test="${param['Q_useType_S'] == '15'}">selected</c:if>>跟进事项到期</option>
							<option value = "16" <c:if test="${param['Q_useType_S'] == '16'}">selected</c:if>>跟进事项完成,评价</option>
							<option value = "17" <c:if test="${param['Q_useType_S'] == '17'}">selected</c:if>>跟进事项通知</option>
							<option value = "18" <c:if test="${param['Q_useType_S'] == '18'}">selected</c:if>>跟进事项已评价</option>
							<option value = "19" <c:if test="${param['Q_useType_S'] == '19'}">selected</c:if>>逾期提醒</option>
							<option value = "22" <c:if test="${param['Q_useType_S'] == '22'}">selected</c:if>>代理提醒</option>
							<option value = "23" <c:if test="${param['Q_useType_S'] == '23'}">selected</c:if>>消息转发</option>
							<option value = "24"  <c:if test="${param['Q_useType_S'] == '24'}">selected</c:if>>重启任务</option>
							<option value = "25"  <c:if test="${param['Q_useType_S'] == '25'}">selected</c:if>>通知任务所属人(代理)</option>
							<option value = "26"  <c:if test="${param['Q_useType_S'] == '26'}">selected</c:if>>加签提醒</option>
							<option value = "27"  <c:if test="${param['Q_useType_S'] == '27'}">selected</c:if>>被加签人提交</option>
							<option value = "28"  <c:if test="${param['Q_useType_S'] == '28'}">selected</c:if>>取消流转</option>	
						</select></li>
					</ul>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="sysTemplateList" id="sysTemplateItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<display:column title="${checkAll}" media="html">
			  		<input type="checkbox" class="pk" name="id" value="${sysTemplateItem.id}">
				</display:column>
				<display:column property="name" title="模板名称" sortable="true" sortName="NAME" maxLength="20"></display:column>
				<display:column property="plainContent" title="文本内容" sortable="true" sortName="PLAINCONTENT" maxLength="36"></display:column>
				<display:column property="htmlContent" title="模板html内容" sortable="true" sortName="HTMLCONTENT" maxLength="38"></display:column>
				<display:column  title="模板用途" sortable="true" sortName="USETYPE">
				<c:choose >
					<c:when test="${sysTemplateItem.useType==1}"><span class="red">终止提醒</span></c:when>
					<c:when test="${sysTemplateItem.useType==2}"><span class="red">催办提醒</span></c:when>
					<c:when test="${sysTemplateItem.useType==3}"><span class="red">审批提醒</span></c:when>
					<c:when test="${sysTemplateItem.useType==4}"><span class="red">撤销提醒</span></c:when>
					<c:when test="${sysTemplateItem.useType==5}"><span class="red">取消转办</span></c:when>
					<c:when test="${sysTemplateItem.useType==6}"><span class="red">沟通提醒</span></c:when>
					<c:when test="${sysTemplateItem.useType==7}"><span class="red">归档提醒</span></c:when>
					<c:when test="${sysTemplateItem.useType==8}"><span class="red">转办提醒</span></c:when>
					<c:when test="${sysTemplateItem.useType==9}"><span class="red">退回提醒</span></c:when>
					<c:when test="${sysTemplateItem.useType==10}"><span class="red">被沟通人提交</span></c:when>
					<c:when test="${sysTemplateItem.useType==11}"><span class="red">取消代理</span></c:when>
					<c:when test="${sysTemplateItem.useType==12}"><span class="red">抄送提醒</span></c:when>
					<c:when test="${sysTemplateItem.useType==13}"><span class="red">流程节点无人员</span></c:when>
					<c:when test="${sysTemplateItem.useType==14}"><span class="red">跟进事项预警</span></c:when>
					<c:when test="${sysTemplateItem.useType==15}"><span class="red">跟进事项到期</span></c:when>
					<c:when test="${sysTemplateItem.useType==16}"><span class="red">跟进事项完成,评价</span></c:when>
					<c:when test="${sysTemplateItem.useType==17}"><span class="red">跟进事项通知</span></c:when>
					<c:when test="${sysTemplateItem.useType==18}"><span class="red">跟进事项已评价</span></c:when>
					<c:when test="${sysTemplateItem.useType==19}"><span class="red">逾期提醒</span></c:when>
					<c:when test="${sysTemplateItem.useType==22}"><span class="red">代理提醒</span></c:when>
					<c:when test="${sysTemplateItem.useType==23}"><span class="red">消息转发</span></c:when>
					<c:when test="${sysTemplateItem.useType==24}"><span class="red">重启任务</span></c:when>
					<c:when test="${sysTemplateItem.useType==25}"><span class="red">通知任务所属人(代理)</span></c:when>
					<c:when test="${sysTemplateItem.useType==26}"><span class="red">加签提醒</span></c:when>
					<c:when test="${sysTemplateItem.useType==27}"><span class="red">被加签人提交</span></c:when>
					<c:when test="${sysTemplateItem.useType==28}"><span class="red">取消流转</span></c:when>
				</c:choose>
				</display:column>
				
				<display:column  title="是否默认模板" sortable="true" sortName="ISDEFAULT" >
				<c:choose >
					<c:when test="${sysTemplateItem.isDefault==1}"><span class="red">是</span></c:when>
					<c:when test="${sysTemplateItem.isDefault==0}"><span class="red">否</span></c:when>
				</c:choose>
				</display:column>
				<display:column title="管理" media="html">
					<a href="del.do?id=${sysTemplateItem.id}" class="link del">删除</a>
					<a href="edit.do?id=${sysTemplateItem.id}" class="link edit">编辑</a>
					<a href="get.do?id=${sysTemplateItem.id}" class="link detail">明细</a>
				</display:column>
			</display:table>
			<ibms:paging tableId="sysTemplateItem"/>
		</div> 	
		<div style="display: none">
			<form  id="exportXmlForm" target="download" action="exportXml.do">
				<input id="exportIds" name="ids">
			</form>
			<iframe id="download" name="download" height="0px" width="0px"></iframe>
		</div>		
	</div>
</body>
</html>


