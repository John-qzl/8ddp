<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<%@include file="/commons/include/get.jsp"%>
<title>流程引用</title>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/ProcessUrgeDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript">
	function urge(id) {
		ProcessUrgeDialog({
			actInstId : id
		});
	};
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link search" id="btnSearch">
							查询
						</a>
					</div>
				</div>
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="nodesetDefReferSelector.do">
					<div class="row">
						<span class="label">流程标题:</span>
						<input type="text" name="Q_subject_SL" class="inputText" style="width: 120px;" value="${param['Q_subject_SL']}" />
						<span class="label">流程定义Key:</span>
						<input type="text" name="Q_defKey_SL" class="inputText" style="width: 120px;" value="${param['Q_defKey_SL']}"/>
						<br>
						<span class="label">创建时间:</span>
						<input type="text" name="Q_createtime_DL" class="inputText date" style="width: 120px;" value="${param['Q_createtime_DL']}"/>
						<span class="label">至</span>
						<input name="Q_endcreatetime_DG" class="inputText date" style="width: 120px;" value="${param['Q_endcreatetime_DG']}"/>
					</div>
				</form>
			</div>
		</div>
		<div class="panel-body">
			<c:set var="checkAll">
				<input type="checkbox" id="chkall" />
			</c:set>
			<display:table name="nodeSetList" id="nodeSetItem" requestURI="nodesetDefReferSelector.do" sort="external" cellpadding="1" cellspacing="1"
				export="false" class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
					<input type="checkbox" class="pk" name="nodeId" value="${nodeSetItem.defKey}:${nodeSetItem.nodeId}" nodeSubject="${nodeSetItem.subject}:${nodeSetItem.nodeName}">
				</display:column>
				<display:column property="subject" titleKey="流程标题" sortable="true" sortName="subject"></display:column>
				<display:column property="nodeName" titleKey="流程节点" sortable="true" sortName="nodeName"></display:column>
				<display:column property="versionNo" titleKey="版本号" sortable="true" sortName="versionNo" style="width:60px"></display:column>
				<display:column titleKey="状态" sortable="true" sortName="status" style="width:60px">
					<c:choose>
						<c:when test="${nodeSetItem.status eq 0}">未发布</span>
						</c:when>
						<c:when test="${nodeSetItem.status eq 1}">
							<span class="green">发布</span>
						</c:when>
						<c:when test="${nodeSetItem.status eq 2}">
							<span class="red">禁用</span>
						</c:when>
						<c:when test="${nodeSetItem.status eq 3}">
							<span class="red">禁用(实例)</span>
						</c:when>
						<c:when test="${nodeSetItem.status eq 4}">
							<span class="green">测试</span>
						</c:when>
						<c:otherwise>
							<span class="red">未选择</span>
						</c:otherwise>
					</c:choose>
				</display:column>
				<display:column titleKey="创建时间" sortable="true" sortName="createtime">
					<fmt:formatDate value="${nodeSetItem.createtime}" pattern="yyyy-MM-dd" />
				</display:column>
			</display:table>
			<ibms:paging tableId="nodeSetItem"></ibms:paging>
		</div>
	</div>
</body>
</html>


