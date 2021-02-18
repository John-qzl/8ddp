<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>职务表管理</title>
<%@include file="/commons/include/get.jsp" %>

</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">职务表管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					
					<div class="group"><a class="link add" href="edit.do">添加</a></div>
					
					<div class="group"><a class="link update" id="btnUpd" action="edit.do">编辑</a></div>
					
					<div class="group"><a class="link del"  action="del.do">删除</a></div>
					
					<div class="group"><a class="link reset" onclick="$.clearQueryForm()">重置</a></div>
				</div>	
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.do">
					<div class="row">
						<span class="label">名称:</span><input type="text" name="Q_jobname_SL"  class="inputText" value="${param['Q_jobname_SL']}"/>
						<span class="label">代码:</span><input type="text" name="Q_jobcode_SL"  class="inputText" value="${param['Q_jobcode_SL']}" />
						<span class="label">描述:</span><input type="text" name="Q_jobdesc_SL"  class="inputText" value="${param['Q_jobdesc_SL']}" />
					</div>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="jobList" id="jobItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
			  		<input type="checkbox" class="pk" name="jobid" value="${jobItem.jobid}">
				</display:column>
				<display:column property="jobname" title="职务名称" sortable="true" sortName="JOBNAME"></display:column>
				<display:column property="jobcode" title="代码" sortable="true" sortName="JOBCODE"></display:column>
				<display:column property="grade" title="级别" sortable="true" sortName="GRADE"></display:column>
				
				<display:column title="管理" media="html" style="width:220px">
					
					<a href="del.do?jobid=${jobItem.jobid}" class="link del">删除</a>
					<a href="edit.do?jobid=${jobItem.jobid}" class="link edit">编辑</a>
					<a href="get.do?jobid=${jobItem.jobid}" class="link detail">明细</a>
				</display:column>
			</display:table>
			<ibms:paging tableId="jobItem"/>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


