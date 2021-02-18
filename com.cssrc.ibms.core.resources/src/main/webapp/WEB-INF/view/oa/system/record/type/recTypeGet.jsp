<%--
	表单类别表
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>编辑 表单类别</title>
	<%@include file="/commons/include/getById.jsp" %>
</head>
<body>
<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">表单类型详细信息</span>
			</div>
			<c:if test="${isOtherLink==0}">
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group"><a class="link back" href="../recType/list.do">返回</a></div>
					</div>
				</div>
			</c:if>
		</div>
		<div class="panel-body">		
				<form id="recTypeForm" method="post" action="save.do">
					<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<th width="20%">表单类型名称: </th>
							<td>${recType.typeName}</td>
						</tr>
						<tr>
							<th width="20%">表单类型别名: </th>
							<td>${recType.alias}</td>
						</tr>						
						<tr>
							<th width="20%">表单类型描述: </th>
							<td>${recType.typeDesc}</td>
						</tr>
					</table>
				</form>
			
		</div>
</div>
</body>
</html>
