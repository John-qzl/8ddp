<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>表单类别表管理</title>
	<%@include file="/commons/include/get.jsp" %>
	<script type="text/javascript" src="${ctx}/jslib/util/form.js"></script>
	<script type="text/javascript">
	
	</script>
</head>
<body>
	<div class="panel">

			<div class="panel-top">
				<div class="tbar-title">
					<span class="tbar-label">表单类型管理列表</span>
				</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<f:a alias="searchType" css="link search" id="btnSearch">查询</f:a>
					</div>
					
					<div class="group">
						<f:a alias="addType" css="link add" href="edit.do">添加</f:a>
					</div>
					
					<div class="group">
						<f:a alias="delType" css="link del" name="delType" action="del.do">删除</f:a>
					</div>
				</div>	
			</div>
			<div class="panel-search">
					<form id="searchForm" method="post" action="list.do">
						<ul class="row plat-row">
							<li><span class="label">类型名称:</span><input type="text" name="Q_typeName_SL"  class="inputText"  value="${param['Q_typeName_SL']}"/></li>				
						    <li><span class="label">类型别名:</span><input type="text" name="Q_alias_SL"  class="inputText"  value="${param['Q_alias_SL']}"/></li>
						</ul>
					</form>
			</div>

		</div>
		<div class="panel-body">
		    	<c:set var="checkAll">
					<input type="checkbox" id="chkall"/>
				</c:set>
			    <display:table export="true" name="recTypeList" id="recTypeItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1"   class="table-grid">
					<display:column title="${checkAll}" media="html" style="width:30px;text-align:center;">
						  	<input type="checkbox" class="pk" name="typeId" value="${recTypeItem.typeId}">
					</display:column>
					<display:column property="typeName" title="类型名称" sortable="true" sortName="typeName" style="text-align:left"></display:column>
					<display:column property="alias" title="类型别名" sortable="true" sortName="alias" style="text-align:left"></display:column>
					<display:column property="typeDesc" title="类型描述" sortable="true" sortName="typeDesc"  style="text-align:left"></display:column>
					
					<display:column title="管理" media="html" style="text-align:center;width:13%;" class="rowOps">
						<f:a alias="delRecType" css="link del" name="delReType" href="del.do?typeId=${recTypeItem.typeId}" >删除 </f:a>
						<f:a alias="updateRecType" css="link edit" href="edit.do?typeId=${recTypeItem.typeId}" >编辑</f:a>
						<f:a alias="recTypeInfo" css="link detail" href="get.do?typeId=${recTypeItem.typeId}">明细</f:a>
						<f:a alias="recInfo" css="link parameter" href="${ctx}/oa/system/recFun/tree.do?typeId=${recTypeItem.typeId}">表单管理</f:a>
					</display:column>
				</display:table>
				<ibms:paging tableId="recTypeItem"/>
		</div>
</body>
</html>


