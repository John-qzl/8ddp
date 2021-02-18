<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<%@include file="/commons/include/get.jsp"%>
<title>我的办结</title>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/SelectUtil.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/FlowUtil.js"></script>
<script type="text/javascript">

	function showDetail(obj){
		var title = obj.getAttribute('title');
		var url = "${ctx}/oa/flow/processRun/" + $(obj).attr("action");
		//jQuery.openFullWindow(url);
		 DialogUtil.open({
				height:screen.availHeight*0.7,
		        width: screen.availWidth*0.7,
		        title : title,
		        url: url, 
		        isResize: true,
		    }); 
	};
	
	//重新提交
	function executeTask(procInstId){
		 var url= "${ctx}/platform/bpm/task/toStart.do?instanceId="+procInstId;
		 //jQuery.openFullWindow(url);
		 DialogUtil.open({
			 	height:screen.availHeight*0.7,//2017-05-15：设置弹出窗口的高度(rzq)
		        width: screen.availWidth*0.7,//2017-05-15：设置弹出窗口的高度(rzq)
		        url: url,
		        isResize: true,
		    }); 
	};
</script>

</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">我的办结</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
				
					<div class="group">
						<a class="link search" id="btnSearch">查询</a>
						
						<div class="group"><a href="javascript:;" class="link reset" onclick="$.clearQueryForm();">重置</a></div>
					</div>
				</div>
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="myCompletedList.do">
						<ul class="row plat-row">
							<input type="hidden" name="nodePath" value="${param['nodePath']}" title="流程分类节点路径"></input>
							<li>
								<span class="label">请求标题:</span>
								<input type="text" name="Q_subject_SUPL" size="18" class="inputText"  value="${param['Q_subject_SUPL']}"/>
							</li>
							<li>
								<span class="label">流程名称:</span>
								<input type="text" name="Q_processName_SUPL" size="18" class="inputText"  value="${param['Q_processName_SUPL']}" />
								<input type="hidden"   id="orgId" name="Q_orgId_L" value="${param['Q_orgId_L']}" />
							</li>
							<li>
								<span class="label">状态:</span>
								<select name="Q_status_SN">
									<option value="">所有</option>
									<option value="2" <c:if test="${param['Q_status_SN'] == '2'}">selected</c:if>>结束</option>
									<option value="3" <c:if test="${param['Q_status_SN'] == '3'}">selected</c:if>>人工结束</option>
									<option value="10" <c:if test="${param['Q_status_SN'] == '10'}">selected</c:if>>逻辑删除</option>
								</select>
							</li>
							<!-- 是否有全局流水号 -->
							<c:if test="${hasGlobalFlowNo }">
								<li><span class="label">工单号:</span><input type="text" name="Q_globalFlowNo_SL"  class="inputText"  value="${param['Q_globalFlowNo_SL']}"/></li>
							</c:if>
							<div class="row_date">
								<li>
									<span class="label" style='overflow:inherit;'>创建时间&nbsp;从:</span>
									<input name="Q_begincreatetime_DL" id="Q_begincreatetime_DL" size="18" class="inputText datePicker" datetype="1"  value="${param['Q_begincreatetime_DL']}" />
								</li>
								<li>						
									<span class="label">至: </span>
									<input name="Q_endcreatetime_DG" id="Q_endcreatetime_DG" size="18" class="inputText datePicker" datetype="2" value="${param['Q_endcreatetime_DG']}"  />
								</li>
							</div>
							
						</ul>
					</form>
			</div>
		</div>
		<div class="panel-body" style="width: 98%;height: 90%;margin:0 auto">
			<display:table name="processRunList" id="processRunItem" requestURI="myCompletedList.do" sort="external" cellpadding="1"
				cellspacing="1" class="table-grid">
				<display:column titleKey="序号" media="html" style="width:20px;">${processRunItem_rowNum}</display:column>
				<!-- 是否有全局流水号 -->
				<c:if test="${hasGlobalFlowNo }">
					<display:column property="globalFlowNo" title="工单号" sortable="true"  sortName="globalFlowNo" style="text-align:left;"></display:column>
				</c:if>
				<display:column  titleKey="请求标题" sortable="true" sortName="subject" style="text-align:left">
						<c:choose>
							<c:when test="${!processRunItem.allowBackToStart and (processRunItem.status==4 or processRunItem.status==5)}">
								<a href="#${processRunItem.actInstId}" onclick="javascript:executeTask('${processRunItem.actInstId}')" title='${processRunItem.subject}'>${f:subString(processRunItem.subject)}</a>
							</c:when>
							<c:otherwise>
								<a name="processDetail" onclick="showDetail(this)" href="#${processRunItem.runId}"  action="info.do?prePage=myCompletedList&link=1&runId=${processRunItem.runId}" title='${processRunItem.subject}'>${f:subString(processRunItem.subject)}</a>								
							</c:otherwise>
						</c:choose>
				</display:column>
				<display:column property="processName" titleKey="流程名称" sortable="true" sortName="processName" style="text-align:left"></display:column>
				<display:column titleKey="创建时间" sortable="true" sortName="createtime">
					<fmt:formatDate value="${processRunItem.createtime}"
						pattern="yyyy-MM-dd HH:mm:ss" />
				</display:column>
				<display:column titleKey="持续时间" sortable="true" sortName="duration">
								${f:getDurationTime(processRunItem.createtime)}
				</display:column>
				<display:column titleKey="类型" >
						<c:out value="${processRunItem.typeName}"></c:out>
				</display:column>		
				<display:column titleKey="状态" sortable="true" sortName="status" style="width:50px;" >
						<f:processStatus status="${processRunItem.status}"></f:processStatus>
				</display:column>
			</display:table>
			<ibms:paging tableId="processRunItem"/>
		</div>
		<!-- end of panel-body -->
	</div>
	<!-- end of panel -->
</body>
</html>


