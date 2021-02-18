<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>访问地址管理</title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript">
</script>
</head>
<body>
	<div class="panel">
		<!-- <div class="hide-panel">
			<div class="hide-panel"> -->
				<div class="panel-top">
					<div class="tbar-title">
						<span class="tbar-label">访问地址管理列表</span>
					</div>
					<div class="panel-toolbar">
						<div class="toolBar">
							<div class="group">
								<f:a alias="searchAddress" css="link search" id="btnSearch">
									查询</f:a>
							</div>
							
							<div class="group">
								<f:a alias="addAddress" css="link add" href="edit.do?toolId=${toolId}">
									添加</f:a>
							</div>
							
							<div class="group">
								<f:a alias="delAddress" css="link del" action="del.do?toolId=${toolId}">
									删除</f:a>
							</div>
							
							<div class="group">
								<a class="link reset" onclick="$.clearQueryForm()">重置</a>
							</div>
						</div>
					</div>
					<div class="panel-search">
						<form id="searchForm" method="post" action="list.do">
							<input type="hidden" name="toolId" value="${toolId}" />
							<ul class="row plat-row">
								<li><span class="label">别名:</span> <input type="text"
									name="Q_alias_SL" class="inputText"
									value="${param['Q_alias_SL']}" /></li>
								<li><span class="label">路径说明:</span> <input type="text"
									name="Q_addressDesc_SL" class="inputText"
									value="${param['Q_addressDesc_SL']}" /></li>
							</ul>
						</form>
					</div>
				<!-- </div> -->
			<!-- </div> -->
		</div>
		<div class="panel-body">
			<c:set var="checkAll">
				<input type="checkbox" id="chkall" />
			</c:set>
			<display:table name="addressList" id="addressItem"
				requestURI="list.do" sort="external" cellpadding="1" cellspacing="1"
				export="false" class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
					<input type="checkbox" class="pk" name="addressId" id="addressId"
						value="${addressItem.addressId}">
				</display:column>
				<display:column property="alias" title="别名" style="text-align:left"></display:column>
				<display:column property="addressDesc" title="路径说明"
					style="text-align:left"></display:column>
				<display:column property="url" title="访问路径" style="text-align:left"></display:column>
				<display:column title="管理" media="html"
					style="width:50px;text-align:center" class="rowOps">
					<f:a alias="updAddress" css="link edit"
						href="edit.do?addressId=${addressItem.addressId}&toolId=${addressItem.toolId}">编辑</f:a>
					<f:a alias="delAddress" css="link del"
						href="del.do?addressId=${addressItem.addressId}&toolId=${addressItem.toolId}">删除</f:a>
					<f:a alias="defAddress" css="link edit"
						href="viewDef.do?addressId=${addressItem.addressId}&toolId=${addressItem.toolId}">展示视图自定义</f:a>
				</display:column>
			</display:table>
		</div>
	</div>
</body>
</html>


