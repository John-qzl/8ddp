
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>系统错误日志明细</title>
<%@include file="/commons/include/getById.jsp"%>
<script type="text/javascript">
	//放置脚本
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">系统错误日志详细信息</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link back" href="list.do">返回</a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
		<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<th width="20%">错误码:</th>
				<td>${sysErrorLog.hashcode}</td>
			</tr>
 
			 <tr>
				<th width="20%">用户名:</th>
				<td>${sysErrorLog.name}</td>
			</tr>
 
			<tr>
				<th width="20%">用户账户:</th>
				<td>${sysErrorLog.account}</td>
			</tr>
 
			<tr>
				<th width="20%">IP地址:</th>
				<td>${sysErrorLog.ip}</td>
			</tr>
 
			<tr>
				<th width="20%">错误URL:</th>
				<td>${sysErrorLog.errorurl}</td>
			</tr>
 			<tr>
				<th width="20%">错误日期:</th>
				<td>
				<fmt:formatDate value="${sysErrorLog.errordate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
			<tr>
				<th width="20%">错误信息:</th>
				<td><div style="max-height: 300px;overflow: auto;">${sysErrorLog.error}</div></td>
			</tr>
 
	
		</table>
		</div>
		
	</div>
</body>
</html>

