<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>系统备份表管理列表</title>
<%@include file="/commons/include/get.jsp" %>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">系统备份表管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					
					<div class="group"><a class="link add" href="edit.do">备份</a></div>
					
					<div class="group"><a class="link update" id="btnUpd" action="edit.do">编辑</a></div>
					
					<div class="group"><a class="link reload"  href="####" onclick="window.location.reload()">刷新</a></div>	
				</div>	
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.do">
					<div class="row">
						<span class="label">备份名称:</span><input type="text" name="Q_backname_SL"  class="inputText" value="${param['Q_backname_SL']}"/>
						<span class="label">执行人:</span><input type="text" name="Q_username_SL"  class="inputText" value="${param['Q_username_SL']}" />
					</div>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="sysBackUpRestoreList" id="sysBackUpRestoreItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
					<input type="checkbox" class="pk" name="backid"  value="${sysBackUpRestoreItem.backid}">
				</display:column>
				<display:column property="backname" title="备份名称" sortable="true" sortName="NAME"></display:column>
				<display:column property="datetime" title="备份时间" sortable="true" sortName="DATETIME"></display:column>
				<display:column property="username" title="执行人" sortable="true" sortName="USERNAME"></display:column>
				<display:column property="comments" title="备注" sortable="true" sortName="COMMENTS"></display:column>
				<display:column title="管理" media="html" style="width:180px">
					<a href="edit.do?backid=${sysBackUpRestoreItem.backid}" class="link edit">编辑</a>
					<a href="restore.do?datetime=${sysBackUpRestoreItem.datetime}" class="link edit">还原</a>
				</display:column>
			</display:table>
			<ibms:paging tableId="sysBackUpRestoreItem"/>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


