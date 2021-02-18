<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>系统错误日志管理</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript">
	//导出系统日志数据
	function exportData(){
		var url = __ctx + '/oa/system/sysErrorLog/exportData.do';
    	window.location.href = url;
	}
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">系统错误日志管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					<!-- <div class="group"><a class="link del"  action="del.do">删除</a></div> -->
					<div class="group"><a class="link reset" onclick="$.clearQueryForm()">重置</a></div>
			        <div class="group"><a class="link export" href="#" onclick="exportData()">导出</a></div>
				</div>	
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.do">
					<ul class="row plat-row">
						<li>
							<span class="label">错误码:</span><input type="text" name="Q_id_L"  class="inputText" value="${param['Q_id_L']}"/>
						</li>
						<li>
							<span class="label">用户:</span><input type="text" name="Q_account_SL"  class="inputText"  value="${param['Q_account_SL']}" />
						</li>
						<li>
							<span class="label">IP地址:</span><input type="text" name="Q_ip_SL"  class="inputText" value="${param['Q_ip_SL']}" />
						</li>
						<li>					
							<span class="label">错误URL:</span><input type="text" name="Q_errorurl_SL"  class="inputText" value="${param['Q_errorurl_SL']}" />
						</li>
					</ul>
					<ul class="row plat-row">
						<div class="row_date">
							<li>
								<span class="label">错误日期 从:</span><input  name="Q_beginerrordate_DL"  class="inputText date" value="${param['Q_beginerrordate_DL']}" />
							</li>
							<li>
								<span class="label">至: </span><input  name="Q_enderrordate_DG" class="inputText date"  value="${param['Q_enderrordate_DG']}" />
							</li>
						</div>
					</ul>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="sysErrorLogList" id="sysErrorLogItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1"  class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
			  		<input type="checkbox" class="pk" name="id" value="${sysErrorLogItem.id}">
				</display:column>
				<%-- <display:column property="id" title="错误码" sortable="true" sortName="id" maxLength="80"></display:column> --%>
				<display:column property="name" title="用户名" sortable="true" sortName="name"></display:column>
				<display:column property="account" title="用户账户" sortable="true" sortName="account"></display:column>
				<display:column property="ip" title="IP地址" sortable="true" sortName="ip"></display:column>
				<display:column property="errorurl" title="错误URL" sortable="true" sortName="errorurl" maxLength="80"></display:column>
				<display:column  title="错误日期" sortable="true" sortName="errordate">
					<fmt:formatDate value="${sysErrorLogItem.errordate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</display:column>
				<display:column title="操作结果">操作失败</display:column>
				<display:column title="管理" media="html" style="width:100px;line-height:21px;">
					<%-- <a href="del.do?id=${sysErrorLogItem.id}" class="link del">删除</a> --%>
					<a href="get.do?id=${sysErrorLogItem.id}" class="link detail">明细</a>
					
				</display:column>
			</display:table>
			<ibms:paging tableId="sysErrorLogItem"/>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


