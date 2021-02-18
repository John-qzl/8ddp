
<%--
	time:2016-09-02 19:52:41
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>系统配置参数表明细</title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript">
	//放置脚本
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">系统配置参数表详细信息</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link back" href="list.do">返回</a>
					</div>
				</div>
			</div>
		</div>
		<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<th width="25%">参数名称:</th>
				<td>${sysParameter.paramname}</td>
			</tr>
			<tr>
				<th width="25%">参数类型:</th>
				<td>${sysParameter.datatype}</td>
			</tr>
			<tr>
				<th width="25%">参数值:</th>
				<td>${sysParameter.paramvalue}</td>
			</tr>
			<tr>
				<th width="25%">参数描述:</th>
				<td>${sysParameter.paramdesc}</td>
			</tr>
		</table>
		</div>
	</div>
</body>
</html>

