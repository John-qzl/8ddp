<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="ibms" uri="http://www.ibms.cn/detailFun"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>栏目明细</title>
<%@include file="/commons/portalCustom.jsp"%>
</head>s
<body>
	<ibms:toolbar toolbarId="toolbar1" />
	<div id="form1" class="form-outer">
		<div style="padding: 5px;">
			<table style="width: 100%" class="table-detail" cellpadding="0" cellspacing="1">
				<caption>栏目基本信息</caption>
				<tr>
					<th>栏目名称：</th>
					<td>${insColumn.name}</td>

					<th>栏目Key：</th>
					<td>${insColumn.key}</td>
				</tr>
				<tr>
					<th>是否启用：</th>
					<td>${insColumn.enabled}</td>
					<th>是否允许关闭：</th>
					<td>${insColumn.allowClose}</td>
				</tr>
				<tr>
					<th>每页记录数：</th>
					<td>${insColumn.numsOfPage}</td>
					<th>信息栏目类型：</th>
					<td>${insColumn.colType}</td>
				</tr>
			</table>
		</div>
		<div style="padding: 5px">
			<table class="table-detail" cellpadding="0" cellspacing="1">
				<caption>更新信息</caption>
				<tr>
					<th>创建人</th>
					<td><f:userName userId="${insColumn.createBy}" /></td>
					<th>创建时间</th>
					<td><fmt:formatDate value="${insColumn.createTime}" pattern="yyyy-MM-dd HH:mm" /></td>
				</tr>
				<tr>
					<th>更新人</th>
					<td><f:userName userId="${insColumn.updateBy}" /></td>
					<th>更新时间</th>
					<td><fmt:formatDate value="${insColumn.updateTime}" pattern="yyyy-MM-dd HH:mm" /></td>
				</tr>
			</table>
		</div>
	</div>
	<ibms:detailScript baseUrl="oa/portal/insColumn" formId="form1" entityName="com.cssrc.ibms.index.model.InsColumn" />
</body>
</html>