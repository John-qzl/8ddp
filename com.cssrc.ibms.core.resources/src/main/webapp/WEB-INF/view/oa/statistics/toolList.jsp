<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>统计工具管理</title>
	<%@include file="/commons/include/get.jsp" %>
	<script type="text/javascript" src="${ctx}/jslib/util/form.js"></script>
	<script type="text/javascript">
	
	</script>
</head>
<body>
	<div class="panel">

			<div class="panel-top">
				<div class="tbar-title">
					<span class="tbar-label">统计工具管理列表</span>
				</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<f:a alias="searchTool" css="link search" id="btnSearch">查询</f:a>
					</div>
					
					<div class="group">
						<f:a alias="addTool" css="link add" href="edit.do">添加</f:a>
					</div>
					
					<div class="group">
						<f:a alias="delTool" css="link del" name="delTool" action="del.do">删除</f:a>
					</div>
				</div>	
			</div>
			<div class="panel-search">
					<form id="searchForm" method="post" action="list.do">
						<ul class="row plat-row">
							<li><span class="label">工具名称:</span><input type="text" name="Q_name_SL"  class="inputText"  value="${param['Q_name_SL']}"/></li>				
						    <li><span class="label">工具别名:</span><input type="text" name="Q_alias_SL"  class="inputText"  value="${param['Q_alias_SL']}"/></li>
						</ul>
					</form>
			</div>

		</div>
		<div class="panel-body">
		    	<c:set var="checkAll">
					<input type="checkbox" id="chkall"/>
				</c:set>
			    <display:table export="true" name="toolList" id="toolItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1"   class="table-grid">
					<display:column title="${checkAll}" media="html" style="width:30px;text-align:center;">
						  	<input type="checkbox" class="pk" name="toolId" value="${toolItem.toolId}">
					</display:column>
					<display:column property="name" title="工具名称" sortable="true" sortName="name" style="text-align:left"></display:column>
					<display:column property="alias" title="工具别名" sortable="true" sortName="alias" style="text-align:left"></display:column>
					<display:column property="toolDesc" title="工具描述" sortable="true" sortName="toolDesc"  style="text-align:left"></display:column>
					
					<display:column title="管理" media="html" style="text-align:center;width:13%;" class="rowOps">
						<f:a alias="delTool" css="link del" name="delTool" href="del.do?toolId=${toolItem.toolId}" >删除 </f:a>
						<f:a alias="updateTool" css="link edit" href="edit.do?toolId=${toolItem.toolId}" >编辑</f:a>
					</display:column>
				</display:table>
				<ibms:paging tableId="toolItem"/>
		</div>
</body>
</html>


