<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>代码模版管理</title>
<%@include file="/commons/include/get.jsp" %>
</head>
<body>
	<div class="panel">
	<div class="hide-panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">定时任务日志</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					<div class="group">
						<a class="link back" href="${ctx}/oa/system/quartz/list.do">返回</a>
					</div>
				</div>	
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.do">
					<ul class="row plat-row">
						<li><span class="label">任务名称:</span><input type="text" name="Q_jobName_S"  value="${param['Q_jobName_S']}"  class="inputText" /></li>
						<li><span class="label">计划名称:</span><input type="text" name="Q_trigName_S"  value="${param['Q_trigName_S']}"  class="inputText" /></li>
					</ul>
				</form>
			</div>
		</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="list" id="quartzLog" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<display:column property="jobName" title="任务名称" sortable="true" sortName="jobName"></display:column>
				<display:column property="trigName" title="计划名称" sortable="true" sortName="trigName"></display:column>
				<display:column property="content" title="说明" sortable="true" ></display:column>
				<display:column title="开始时间" sortable="true" sortName="startTime">
						<fmt:formatDate value="${quartzLog.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</display:column>
				<display:column title="结束时间" sortable="true" sortName="endTime">
						<fmt:formatDate value="${quartzLog.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</display:column>
				<display:column property="runTime" title="用时" sortable="true" sortName="runTime"></display:column>
				<display:column title="状态" sortable="true" sortName="state">
				    <c:choose>
						<c:when test="${quartzLog.state eq '1'}">
							成功
					    </c:when>
						<c:otherwise>
						    失败
						</c:otherwise>   
				    </c:choose>
				</display:column>
			</display:table>
			<ibms:paging tableId="quartzLog"/>
		</div> 			
	</div>
</body>
</html>


