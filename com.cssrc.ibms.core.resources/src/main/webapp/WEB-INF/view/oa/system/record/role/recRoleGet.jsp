<%--
角色明细 by yangbo
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>表单角色明细</title>
	<%@include file="/commons/include/getById.jsp" %>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/record/RecRoleEdit.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/util/util.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/util/json2.js"></script>
	<script type="text/javascript">
		$(function() {
			//初始化角色过滤条件
			__RecRole__.init();
		});
	</script>
</head>
<body>
<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">表单角色详细信息</span>
			</div>
			<c:if test="${isOtherLink==0}">
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link back" href="../recRole/list.do">返回</a></div>
				</div>
			</div>
			</c:if>
		</div>
		<div class="panel-body">
			<div class="panel-detail">
				<form id="recRoleForm" method="post" action="add.do">
					<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
					    <tr>
							<th width="20%">角色名称:</th>
							<td>${recRole.roleName}</td>
						</tr>
						<tr>
							<th width="20%">角色别名:</th>
							<td>${recRole.alias}</td>
						</tr>
						<tr>
							<th width="20%">备注:</th>
							<td>${recRole.roleDesc}</td>
						</tr>
						<tr>
							<th width="20%">允许删除:</th>
							<td>
							<c:choose>
							<c:when test="${recRole.allowDel eq 1}">允许</c:when>
							<c:when test="${recRole.allowDel eq 0}">不允许</c:when>
							<c:otherwise>未设定</c:otherwise>
							</c:choose>
							</td>
						</tr>
						<tr>
							<th width="20%">允许编辑:</th>
							<td>
							<c:choose>
							<c:when test="${recRole.allowEdit eq 1}">允许</c:when>
							<c:when test="${recRole.allowEdit eq 0}">不允许</c:when>
							<c:otherwise>未设定</c:otherwise>
							</c:choose>
							</td>
						</tr>
						<tr style="display:none">
							<th width="20%">是否启用:</th>
							<td>
							<c:choose>
							<c:when test="${recRole.status eq 1}">启用</c:when>
							<c:when test="${recRole.status eq 0}">禁用</c:when>
							<c:otherwise>未设定</c:otherwise>
							</c:choose>
							</td>
						</tr>
						<tr >
							<th width="20%">角色人员信息 </th>
							<td var="filter">
							</td>
						</tr>
						<textarea style="display:none;" id="filterField" name="filterField" >${fn:escapeXml(recRole.filter)}</textarea>
					</table>
				</form>
			</div>
		</div>
</div>

</body>
</html>
