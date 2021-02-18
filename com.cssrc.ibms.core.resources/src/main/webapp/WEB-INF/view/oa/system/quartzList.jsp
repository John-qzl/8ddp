<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>定时任务管理</title>
<%@include file="/commons/include/get.jsp" %>
</head>
<body>
	<div class="panel">
	<div class="hide-panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">定时任务管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link add" href="edit.do">添加</a></div>
					<div class="group"></div>
					<div class="group">
						<a class="link edit" href="${ctx}/oa/system/quartzLog/list.do">执行日志</a>
					</div>
				</div>	
			</div>
		</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
			 
			 <display:table name="sysJobList" id="JobDetail" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
			  		<input type="checkbox" class="pk" name="id" value="${JobDetail.name}">
				</display:column>
				<display:column property="group" title="任务组" ></display:column>
				<display:column property="name" title="任务名称" ></display:column>
				<display:column property="jobClass.name" title="任务类" ></display:column>
				<display:column property="description" title="备注" ></display:column>
		        <display:column title="管理" media="html" style="width:50px;text-align:center" class="rowOps">
					<a href="planeList.do?name=${JobDetail.name}"  class="link detail">计划管理</a>
					<f:a alias="delSysJob" href="del.do?name=${JobDetail.name}" css="link del">删除</f:a>
					<a  href="executeJob.do?name=${JobDetail.name}" class="link run">执行</a> 
					<a href="edit.do?name=${JobDetail.name}"  class="link detail">明细</a>
					<a href="${ctx}/oa/system/quartzLog/list.do?Q_jobName_S=${JobDetail.name}" class="link edit">日志</a>
			  </display:column>
			</display:table>
		</div> 			
	</div>
</body>
</html>


