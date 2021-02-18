
<%--
	time:2012-12-19 15:38:01
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>

<html>
<head>
<title>内部消息模版明细</title>
<%@include file="/commons/include/getById.jsp"%>
<script type="text/javascript">
	//放置脚本
</script>
<script type="text/javascript" src="${ctx}/jslib/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ckeditor/ckeditor_rule.js"></script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">内部消息详细信息</span>
			</div>
			<c:if test="${canReturn==0}">
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link back" href="list.do">返回</a>
					</div>
				</div>
			</div>
			</c:if>
		</div>
		<div class="panel-body">
		<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
			 
			<tr>
				<th width="20%">模板名称:</th>
				<td>${sysTemplate.name}</td>
			</tr>
 
			<tr>
				<th width="20%">模板用途:</th>
				<td>
					<c:choose>
						<c:when test="${sysTemplate.useType eq 1 }"><span class="green">终止提醒</span></c:when>
						<c:when test="${sysTemplate.useType eq 2 }"><span class="green">催办提醒</span></c:when>
						<c:when test="${sysTemplate.useType eq 3 }"><span class="green">审批提醒</span></c:when>
						<c:when test="${sysTemplate.useType eq 4 }"><span class="green">撤销提醒</span></c:when>
						<c:when test="${sysTemplate.useType eq 5 }"><span class="green">取消转办</span></c:when>
						<c:when test="${sysTemplate.useType eq 6 }"><span class="green">沟通提醒</span></c:when>
						<c:when test="${sysTemplate.useType eq 7 }"><span class="green">归档提醒</span></c:when>
						<c:when test="${sysTemplate.useType eq 8 }"><span class="green">转办提醒</span></c:when>
						<c:when test="${sysTemplate.useType eq 9 }"><span class="green">退回提醒</span></c:when>
						<c:when test="${sysTemplate.useType eq 10}"><span class="green">被沟通人提交</span></c:when>
						<c:when test="${sysTemplate.useType eq 11}"><span class="green">取消代理</span></c:when>
						<c:when test="${sysTemplate.useType eq 12}"><span class="green">抄送提醒</span></c:when>
						<c:when test="${sysTemplate.useType eq 13}"><span class="green">流程节点无人员</span></c:when>
						<c:when test="${sysTemplate.useType eq 14}"><span class="green">跟进事项预警</span></c:when>
						<c:when test="${sysTemplate.useType eq 15}"><span class="green">跟进事项到期</span></c:when>
						<c:when test="${sysTemplate.useType eq 16}"><span class="green">跟进事项完成,评价</span></c:when>
						<c:when test="${sysTemplate.useType eq 17}"><span class="green">跟进事项通知</span></c:when>
						<c:when test="${sysTemplate.useType eq 18}"><span class="green">跟进事项已评价</span></c:when>
						<c:when test="${sysTemplate.useType eq 19}"><span class="green">逾期提醒</span></c:when>
						<c:when test="${sysTemplate.useType eq 22}"><span class="green">代理提醒</span></c:when>
						<c:when test="${sysTemplate.useType eq 23}"><span class="green">消息转发</span></c:when>
						<c:when test="${sysTemplate.useType eq 24}"><span class="green">重启任务</span></c:when>
						<c:when test="${sysTemplate.useType eq 25}"><span class="green">通知任务所属人(代理)</span></c:when>
						<c:when test="${sysTemplate.useType eq 26}"><span class="green">加签提醒</span></c:when>
						<c:when test="${sysTemplate.useType eq 27}"><span class="green">被加签人提交</span></c:when>
						<c:when test="${sysTemplate.useType eq 28}"><span class="green">取消流转	</span></c:when>
					</c:choose>
				</td>
			</tr>
			
			<tr>
				<th width="20%">标题:</th>
				<td>${sysTemplate.title}</td>
			</tr>
			
			<tr>
				<th width="20%">模板html内容:</th>
				<td>
					<textarea  id= "htmlContent" name="htmlContent"  readonly="readonly" style="width:800px;height:250px;" >${fn:escapeXml(sysTemplate.htmlContent)}</textarea>
					<script type="text/javascript">
						htmlContentEditor = ckeditor('htmlContent');
					</script></td>
			</tr>
			
			<tr>
				<th width="20%">普通内容:</th>
				<td>
					<textarea name="plainContent" cols="120" rows="15" readonly="readonly" >${fn:escapeXml(sysTemplate.plainContent)}</textarea>
				</td>
			</tr>
			
			<tr>
				<th width="20%">是否默认模板:</th>
					<td> 
						<c:choose>
							<c:when test="${sysTemplate.isDefault==1}">
								是
							</c:when>
							<c:otherwise>
								否
							</c:otherwise>
						</c:choose>
					</td>
			</tr>
		</table>
		</div>
		
	</div>
</body>
</html>

