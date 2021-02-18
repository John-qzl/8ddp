<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>TAB</title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerTab.js"></script>
<script type="text/javascript">
	$(function() {
		$("#tabMyInfo").ligerTab({});
	});
</script>
<f:link href="from-jsp.css"></f:link></head>
<body>
	<div id="tabMyInfo" class="panel-nav" style="overflow: hidden; position: relative;">
		<c:forEach var="item" items="${resList}">
			<div title="${item.resName}" tabid="${item.alias }">
				<ul>
					<c:forEach items="${resrouceMap[item.alias]}" var="subItem">
						<div class="tab">
							<div style="height: 64px; text-align: center;" title="${subItem.resName}" onclick="jQuery.openFullWindow('${subItem.defaultUrl}')">
								<img id="iconImg" alt="" src="${ctx}/${subItem.icon}" width="64px;" height="64px;" />
							</div>
							<div class="title">${subItem.resName}</div>
						</div>
					</c:forEach>
				</ul>
			</div>
		</c:forEach>
	</div>
</body>
</html>


