<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>联系人明细</title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript">
	//放置脚本
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">联系人详细信息</span>
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
				<th width="20%">姓名:</th>
				<td>${oaLinkman.name}</td>
			</tr>
			<tr>
				<th width="20%">性别:</th>
				<td>${oaLinkman.sex}</td>
			</tr>
			<tr>
				<th width="20%">电话:</th>
				<td>${oaLinkman.phone}</td>
			</tr>
			<tr>
				<th width="20%">邮箱:</th>
				<td>${oaLinkman.email}</td>
			</tr>
			<tr>
				<th width="20%">公司:</th>
				<td>${oaLinkman.company}</td>
			</tr>
			<tr>
				<th width="20%">工作:</th>
				<td>${oaLinkman.job}</td>
			</tr>
			<tr>
				<th width="20%">地址:</th>
				<td>${oaLinkman.address}</td>
			</tr>
			<tr>
				<th width="20%">创建时间:</th>
				<td>
				<fmt:formatDate value="${oaLinkman.createtime}" pattern="yyyy-MM-dd"/>
				</td>
			</tr>
			<tr>
				<th width="20%">状态:</th>
				<td>
					<c:if test="${oaLinkman.status==1}">启用</c:if>
					<c:if test="${oaLinkman.status==0}">禁用</c:if>
				</td>
			</tr>
		</table>
		</div>
	</div>
</body>
</html>

