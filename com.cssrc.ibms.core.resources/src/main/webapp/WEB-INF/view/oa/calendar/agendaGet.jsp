<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>日程明细</title>
<%@include file="/commons/portalCustom.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/util/util.js"></script>
</head>
<body>
	<div id="form1" class="form-outer">
		<div style="padding: 5px;">
		
			<table style="width: 90%,height:90%" class="table-detail" cellpadding="0" cellspacing="1">
				<caption>栏目基本信息</caption>
				<tr>
					<th>日程类型：</th>
					<td>${agenda.type}</td>
				</tr>
				<tr>
					<th>标题：</th>
					<td>${agenda.subject}</td>
				</tr>
				<tr>
					<th>接收人：</th>
					<td>${agenda.executors}</td>
				</tr>
				<tr height>
					<th>内容：</th>
					<td>${agenda.content}</td>
				</tr>
				<tr>
					<th>紧急程度：</th>
					<td>
					<c:if test="${agenda.grade=='COMMON'}">
						一般
					</c:if>
					<c:if test="${agenda.grade=='MAJOR'}">
						重要
					</c:if>
					<c:if test="${agenda.grade=='URGENT'}">
						紧急
					</c:if>
					</td>
				</tr>
				<tr>
					<th>开始时间：</th>
					<td><fmt:formatDate value="${agenda.startTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				</tr>
				
				<tr>
					<th>结束时间：</th>
					<td><fmt:formatDate value="${agenda.endTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
				</tr>
				
				<tr>
					<th>提醒方式：</th>
					<td>${agenda.warnWay}</td>
				</tr>
				
			</table>
		</div>
		
	</div>
	
</body>
</html>