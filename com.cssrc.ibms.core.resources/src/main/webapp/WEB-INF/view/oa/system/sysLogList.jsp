<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>系统日志管理</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript">
	//导出系统日志数据
	function exportData(){
		var url = __ctx + '/oa/system/sysLog/exportData.do';
    	window.location.href = url;
	}
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">系统日志管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					<!-- <div class="l-bar-separator"></div>
					<div class="group"><a class="link del"  action="del.do">删除</a></div> -->
			        <div class="group"><a class="link reset" onclick="$.clearQueryForm()">重置</a></div>
			        <div class="group"><a class="link export" href="#" onclick="exportData()">导出</a></div>
				</div>	
			</div>
			<div class="panel-search">
					<form id="searchForm" method="post" action="list.do">
							<ul class="row plat-row">
								<li><span class="label">操作名称:</span><input type="text" name="Q_opName_SL"  class="inputText" value="${param['Q_opName_SL']}"/></li>
								<li><span class="label">执行人姓名:</span><input type="text" name="Q_executorName_SL"  class="inputText" value="${param['Q_executorName_SL']}"/></li>
								<li><span class="label">执行人账户:</span><input type="text" name="Q_executor_SL"  class="inputText" value="${param['Q_executor_SL']}"/></li>
								<div class="row_date">
								<li><span class="label">执行时间 从:</span><input  name="Q_beginopTime_DL"  class="inputText date" value="${param['Q_beginopTime_DL']}"/></li>
								<li><span class="label">至: </span><input  name="Q_endopTime_DG" class="inputText date" value="${param['Q_endopTime_DG']}"/></li>
								</div>
							</ul>
							<ul class="row plat-row">
								<li><span class="label">执行人IP:</span><input type="text" name="Q_fromIp_SL"  class="inputText" value="${param['Q_fromIp_SL']}"/></li>
								<li><span class="label">归属模块:</span><input type="text" name="Q_ownermodel_SL"  class="inputText" value="${param['Q_ownermodel_SL']}"/></li>
								<li><span class="label">日志类型:</span><input type="text" name="Q_exectype_SL"  class="inputText" value="${param['Q_exectype_SL']}"/></li>
							</ul>
					</form>
			</div>
		</div>
		<div class="panel-body">
		    <display:table name="sysLogList" id="sysLogItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<display:column title="序号" media="html" style="width:40px;text-align:center">${sysLogItem_rowNum}</display:column>
				<display:column property="opName" title="操作名称" sortable="true" sortName="opName"></display:column>
				<display:column property="executorName" title="执行人姓名" sortable="true" sortName="executorName"></display:column>
				<display:column property="executor" title="执行人账号" sortable="true" sortName="executor"></display:column>
				<display:column property="fromIp" title="执行人IP" sortable="true" sortName="fromIp"></display:column>
				<display:column title="执行时间" sortable="true" sortName="opTime">
					<fmt:formatDate value="${sysLogItem.opTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</display:column>
				<%-- <display:column property="exeMethod" title="执行方法" sortable="true" sortName="exeMethod"></display:column>
				<display:column property="requestURI" title="请求URL" sortable="true" sortName="requestURI"></display:column> --%>
				<display:column property="ownermodel" title="归属模块" sortable="true" sortName="ownermodel"></display:column>
				<display:column property="exectype" title="日志类型" sortable="true" sortName="exectype"></display:column>
				<display:column property="detail" title="明细" style="white-space: normal!important;word-break: break-word;" sortable="true" sortName="detail"></display:column>
				<display:column title="执行结果">
					<c:choose>
					   	<c:when test="${sysLogItem.result==1}">
							<span style="color:green;">成功</span>
					   	</c:when>
				       	<c:otherwise>
				       		<span style="color:red;">失败</span>
					   	</c:otherwise>
					</c:choose>
				</display:column>
				<display:column title="管理" media="html" style="width:100px;text-align:center">
					<%-- <a href="del.do?auditId=${sysLogItem.auditId}" class="link del">删除</a> --%>
					<a href="get.do?auditId=${sysLogItem.auditId}" class="link detail">明细</a>
				</display:column>
			</display:table>
			<ibms:paging tableId="sysLogItem"/>
			
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


