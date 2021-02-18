<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="ib" uri="http://www.ibms.cn/detailFun"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>新闻评论明细</title>
<%@include file="/commons/portalCustom.jsp"%>
</head>
<body>
	<ib:toolbar toolbarId="toolbar1" />
	<div id="form1" class="form-outer">
		<div style="padding: 5px;">
			<table style="width: 100%" class="table-detail" cellpadding="0" cellspacing="1">
				<caption>新闻评论基本信息</caption>
				<tr>
					<th>信息ID：</th>
					<td>${insNewsCm.newId}</td>
				</tr>
				<tr>
					<th>评论人名：</th>
					<td>${insNewsCm.fullName}</td>
				</tr>
				<tr>
					<th>评论内容：</th>
					<td>${insNewsCm.content}</td>
				</tr>
				<tr>
					<th>赞同与顶：</th>
					<td>${insNewsCm.agreeNums}</td>
				</tr>
				<tr>
					<th>反对与鄙视次数：</th>
					<td>${insNewsCm.refuseNums}</td>
				</tr>
				<tr>
					<th>是否为回复：</th>
					<td>${insNewsCm.isReply}</td>
				</tr>
				<tr>
					<th>回复评论ID：</th>
					<td>${insNewsCm.repId}</td>
				</tr>
			</table>
		</div>
		<div style="padding: 5px">
			<table class="table-detail" cellpadding="0" cellspacing="1">
				<caption>更新信息</caption>
				<tr>
					<th>创建人</th>
					<td><ibms:userLabel userId="${insNewsCm.createBy}" /></td>
					<th>创建时间</th>
					<td><fmt:formatDate value="${insNewsCm.createTime}" pattern="yyyy-MM-dd HH:mm" /></td>
				</tr>
				<tr>
					<th>更新人</th>
					<td><ibms:userLabel userId="${insNewsCm.updateBy}" /></td>
					<th>更新时间</th>
					<td><fmt:formatDate value="${insNewsCm.updateTime}" pattern="yyyy-MM-dd HH:mm" /></td>
				</tr>
			</table>
		</div>
	</div>
	<ib:detailScript baseUrl="oa/portal/insNewsCm" formId="form1" entityName="com.cssrc.ibms.index.model.InsNewsCm"/>
</body>
</html>