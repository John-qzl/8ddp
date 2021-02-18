<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="ibms" uri="http://www.ibms.cn/detailFun"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>栏目类型明细</title>
<%@include file="/commons/portalCustom.jsp"%>
</head>
<body>
	<ibms:toolbar toolbarId="toolbar1" />
	<div id="form1" class="form-outer">
		<div style="padding: 5px;">
			<table style="width: 100%" class="table-detail" cellpadding="0" cellspacing="1">
				<caption>栏目类型基本信息</caption>
				<tr>
					<th>栏目名称：</th>
					<td>${insColType.name}</td>
				</tr>
				<tr>
					<th>栏目Key：</th>
					<td>${insColType.key}</td>
				</tr>
				<tr>
					<th>栏目映射URL：</th>
					<td>${insColType.url}</td>
				</tr>
				<tr>
					<th>栏目更多URL：</th>
					<td>${insColType.moreUrl}</td>
				</tr>
				<tr>
					<th>栏目分类描述：</th>
					<td>${insColType.memo}</td>
				</tr>
			</table>
		</div>
		<div style="padding: 5px">
			<table class="table-detail" cellpadding="0" cellspacing="1">
				<caption>更新信息</caption>
				<tr>
					<th>创建人</th>
					<td><f:userName userId="${insColType.createBy}" /></td>
					<th>创建时间</th>
					<td><fmt:formatDate value="${insColType.createTime}" pattern="yyyy-MM-dd HH:mm" /></td>
				</tr>
				<tr>
					<th>更新人</th>
					
					<td><f:userName userId="${insColType.updateBy}"/></td>
					<th>更新时间</th>
					<td><fmt:formatDate value="${insColType.updateTime}" pattern="yyyy-MM-dd HH:mm" /></td>
				</tr>
			</table>
		</div>
	</div>
	<ibms:detailScript baseUrl="oa/portal/insColType" formId="form1" entityName="com.cssrc.ibms.index.model.InsColType" />
</body>
</html>