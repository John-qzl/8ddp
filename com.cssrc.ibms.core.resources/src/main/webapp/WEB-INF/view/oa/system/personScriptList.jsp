<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<%@include file="/commons/include/get.jsp" %>
<title>人员脚本管理</title>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">人员脚本列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					
					<div class="group"><a class="link add" href="edit.do">添加</a></div>
					
					<div class="group"><a class="link update" id="btnUpd" action="edit.do">编辑</a></div>
					
					<div class="group"><a class="link del"  action="del.do">删除</a></div>
					
				</div>	
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.do">
					<div class="row">
						<span class="label">脚本所在类名:</span><input type="text" name="Q_className_SL"  class="inputText" value="${param['Q_className_SL']}"/>
						<span class="label">方法名:</span><input type="text" name="Q_methodName_SL"  class="inputText" value="${param['Q_methodName_SL']}"/>
						<span class="label">方法描述:</span><input type="text" name="Q_methodDesc_SL"  class="inputText" value="${param['Q_methodDesc_SL']}"/>
						<span class="label">是否有效:</span>
						<select name="Q_enable_L" class="select" value="${param['Q_enable_L']}">
							<option value="">请选择</option>
							<option value="0" <c:if test="${param['Q_enable_L'] == '0'}">selected</c:if>>是</option>
							<option value="1" <c:if test="${param['Q_enable_L'] == '1'}">selected</c:if>>否</option>
						</select>
					</div>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="personScriptList" id="personScriptItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
			  		<input type="checkbox" class="pk" name="id" value="${personScriptItem.id}">
				</display:column>
				<display:column title="方法名称" sortable="true" sortName="METHOD_NAME">
					<a class="link" href="edit.do?id=${personScriptItem.id}" style="color:blue;">${personScriptItem.methodName}</a> 
				</display:column>
				<display:column property="methodDesc" title="方法描述" maxLength="80"></display:column>
				<display:column property="className" title="脚本所在类的类名" sortable="true" sortName="CLASS_NAME" maxLength="80"></display:column>
				<display:column property="classInsName" title="类实例名" sortable="true" sortName="CLASS_INS_NAME"></display:column>
				<display:column title="是否有效" sortable="true" sortName="enable">
					<c:choose>
						<c:when test="${personScriptItem.enable eq 0}"><span class="green">是</span></c:when>
						<c:when test="${personScriptItem.enable eq 1}"><span class="red">否</span></c:when>
					</c:choose>
				</display:column>
			</display:table>
			<ibms:paging tableId="personScriptItem"/>
		</div>
	</div>
</body>
</html>


