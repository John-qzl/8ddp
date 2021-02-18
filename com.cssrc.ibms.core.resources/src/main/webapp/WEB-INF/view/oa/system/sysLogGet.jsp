<%--
	系统日志详细
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>详细信息</title>
	<%@include file="/commons/include/getById.jsp" %>
</head>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysAuditLink.js"></script>
<script type="text/javascript">
	//系统日志的详细信息的超链接
	$(function(){
		SysAuditLink.initLink();
	});
</script>
<body>
<div class="panel">
<div class="hide-panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">详细信息</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link back" href="../sysLog/list.do">返回</a></div>
				</div>
			</div>
		</div>
	</div>
		<div class="panel-body">
				<form id="sysLogForm">
					<div class="panel-detail">
						<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<th width="20%">操作名称:</th>
								<td>${sysLog.opName}</td>
							</tr>
							<tr>
								<th width="20%">执行时间:</th>
								<td><fmt:formatDate value="${sysLog.opTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							</tr>
							<tr>
								<th width="20%">执行人姓名:</th>
								<td>${sysLog.executorName}</td>
							</tr>
							<tr>
								<th width="20%">执行人账号:</th>
								<td>${sysLog.executor}</td>
							</tr>
							<tr>
								<th width="20%">执行人IP:</th>
								<td>${sysLog.fromIp}</td>
							</tr>
							<tr>
								<th width="20%">归属模块:</th>
								<td>${sysLog.ownermodel}</td>
							</tr>
							<tr>
								<th width="20%">日志类型:</th>
								<td>${sysLog.exectype}</td>
							</tr>
							<tr>
								<th width="20%">执行方法:</th>
								<td>${sysLog.exeMethod}</td>
							</tr>
							<tr>
								<th width="20%">执行结果:</th>
								<td>
									<c:choose>
									   	<c:when test="${sysLog.result == 1}">
											成功
									   	</c:when>
								       	<c:otherwise>
								       		失败
									   	</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<%-- <tr>
								<th width="20%">请求URL:</th>
								<td>${sysLog.requestURI}</td>
							</tr> --%>
							<tr>
								<th width="20%">详细信息:</th>
								<td>${sysLog.detail}</td>
							</tr>
						</table>
					</div>
				</form>
		</div>
</div>

</body>
</html>
