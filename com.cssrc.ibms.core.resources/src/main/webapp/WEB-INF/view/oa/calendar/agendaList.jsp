<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/commons/include/html_doctype.html"%>
<html>
  <head>
	<%@include file="/commons/include/get.jsp"%>
	<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.qtip.js" ></script>
    <title>日程列表</title>
	<script type="text/javascript">
	function show(url,Id,subject){
		top['index']._OpenWindow({
				url : url + Id,
				title : subject,
				width : 650,
				height :800
		});
	}
	</script>
  </head>
  
  <body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">日程列表</span>
			</div>
			
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group" id="search"><a class="link search">查询</a></div>
					
					<div class="group"><a class="link reset" onclick="$.clearQueryForm()">重置</a></div>
					
					<div class="group" id="del"><a class="link del" action="del.do">删除</a></div>
				</div>
			</div>
			
			<div class="panel-search" >
				<form id="searchForm" method="get" action="list.do">
					<ul class="row plat-row">
						<li><span class="label">标题:</span><input type="text" name="Q_subject_SL"  class="inputText"  value="${param['Q_subject_SL']}" /></li>
						<li><span class="label">创建人:</span><input type="text" name="Q_creator_SL"  class="inputText"  value="${param['Q_creator_SL']}"/></li>
						<li><span class="label">内容关键字:</span><input type="text" name="Q_content_SL"  class="inputText"   value="${param['Q_content_SL']}"/></li>					
						<div class="row_date">
							<li><span class="label">日程跨度 从:</span><input type="text"  name="Q_startTime_DL"  class="inputText date" value="${param['Q_startTime_DL']}"/></li>
							<li><span class="label">至:</span><input type="text" name="Q_endTime_DG" class="inputText date" value="${param['Q_endTime_DG']}"/></li>
						</div>
					</ul>
				</form>
			</div>
	    </div>
	</div>
		    
	<div class="panel-body">
		<c:set var="checkAll">
			<input type="checkbox" id="chkall" style="text-align: center" />
		</c:set>
		
		<display:table name="agendaList" id="agendaItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
			<display:column title="${checkAll}" media="html" style="width:30px;text-align:center">
				<input type="checkbox" class="pk" name="agendaId" value="${agendaItem.agendaId}">
			</display:column>
			<display:column title="序号" media="html" style="width:40px;text-align:center">${agendaItem_rowNum}</display:column>
			<display:column property="subject" title="日程标题" style="text-align:center"></display:column>
			<display:column property="creator" title="创建人" style="width:150px;text-align:center"></display:column>
			<display:column property="executors" title="执行人" style="width:400px;text-align:center"></display:column>
			<display:column title="开始时间" style="width:150px;text-align:center" >
				<fmt:formatDate value="${agendaItem.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</display:column>
			<display:column title="截止时间" style="width:150px;text-align:center" >
				<fmt:formatDate value="${agendaItem.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</display:column>
			
			<display:column property="grade" title="级别" style="width:80px;text-align:center"></display:column>
			<display:column  title="状态" style="width:80px;text-align:center">
				<c:choose>
					<c:when test="${agendaItem.status>0}">
						<span class="green">完成</span>
					</c:when>
					<c:otherwise>
						<span class="red">进行中</span>
					</c:otherwise>
				</c:choose>
			</display:column>
			
			
			<display:column title="管理" media="html" style="width:250px;text-align:center">
				<f:a alias="delFile" href="${ctx}/oa/calendar/agenda/del.do?agendaId=${agendaItem.agendaId}" css="link del">删除</f:a>
				<a href="${ctx}/oa/calendar/agenda/finish.do?agendaId=${agendaItem.agendaId})" class="link stop">结束</a>
				<a href="javascript:show('${ctx}/oa/calendar/agenda/detail.do?agendaId=','${agendaItem.agendaId}','日程详情')" class="link preview">详情</a>
			</display:column>
			
		</display:table>
		
		<ibms:paging tableId="agendaItem"/>
	</div>
	
  </body>
</html>
