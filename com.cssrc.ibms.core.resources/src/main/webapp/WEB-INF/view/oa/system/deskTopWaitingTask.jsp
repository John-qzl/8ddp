<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<%@include file="/commons/include/get.jsp" %>
<title>待办事宜</title>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/BpmTaskExeAssignDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/SelectUtil.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/UserInfo.js"></script>
<script type="text/javascript">

function executeTask(taskId,obj){
	 var url="${ctx}/oa/flow/task/toStart.do?taskId="+taskId;
	 var rtn = jQuery.openFullWindow(url);
	 var sub = $(obj);
	 var class1 = sub.attr("class");
	 if(class1=='message close-message'){
		sub.attr("class","message open-message"); 
	 }
}

//重启任务
function restartTask(taskId){
	var url="${ctx}/oa/flow/task/restartTask.do?taskId="+taskId;
	var rtn = jQuery.openFullWindow(url);
	refreshPortal();
}


function reload(){
	location.href=location.href.getNewUrl();
	parent.globalType.loadGlobalTree();
}



function batOperator(operator){
	if(operator=="approve"){
		if ($("#btnApprove").attr('class').indexOf('disabled')>0){return false;}	
	}
	else{
		if ($("#btnBatDelegate").attr('class').indexOf('disabled')>0){return false;}
	}
	
	var aryId = $("input[type='checkbox'][disabled!='disabled'][class='pk']:checked");
	var len=aryId.length;
	if(len==0){
		$.ligerDialog.warn('请选择记录!','提示');
		return;
	}
	
	var taskIds=new Array();
	$("input[name='id']:checked").each(function(){
		taskIds.push($(this).val());
	});
	var ids=taskIds.join(",");
	
	
	var url=__ctx + "/oa/flow/task/pendingMattersListBatchApprovalCfm.do?taskIds="+ids;
	if(operator=="delegate"){
		url=__ctx + "/oa/flow/task/delegateDialog.do?taskIds="+ids;
	}
	url=url.getNewUrl();

	DialogUtil.open({
		height: 250,
		width: 500,
		title : '批量审批',
		url: url, 
		isResize: true,
		//自定义参数
		sucCall:function(rtn){
			if (rtn=='ok'){
				location.href=location.href.getNewUrl();
			}
		}
	});
}
//更多
function more(){
	var c = {
			id : '${funcitonId}',
			text:'${title}',
			attributes:{
				url:'${moreUrl}',
				iconCls:'${iconCls}'
			}
	}
	if('${moreUrl}'.indexOf('/js/')>=0){
	    App.clickTopSeaTab(c);
	}else{
		App.clickTopTabUrl(c);
	}
}
//刷新
function taskRefresh(){
   var items = Ext.getCmp("DeskTop").items;
   for (var i = 0; i < items.getCount(); i++) {  
        var c = items.get(i);  
        c.items.each(function(portlet,index) {
                    if(portlet.getId()=='p'+ ${portalId}){
                       portlet.getUpdater().refresh();
                    }
                });  
    }
    	//window.opener.taskRefresh();
		//window.close();
}

</script>
</head>
<body >
	<div class="panel" >
		<div class="panel-body">
		    	<c:set var="checkAll">
					<input type="checkbox" id="chkall"/>
				</c:set>
			    <display:table name="taskList" id="taskItem" requestURI="pendingMattersList.do" sort="external" cellpadding="1" cellspacing="1"  class="table-grid">
					<display:column title="${checkAll}" media="html" style="width:20px;">
							<%-- 15 为沟通意见 , 38为流转意见--%>
						  	<c:choose>
							<c:when test="${taskItem.description eq '15' || taskItem.description eq '38'}">
								<input type="checkbox" class="pk" name="id"  disabled="disabled" value="${taskItem.id}">
							</c:when>
							<c:otherwise >
								<input type="checkbox" class="pk" name="id"  value="${taskItem.id}">
							</c:otherwise>
						</c:choose>
					</display:column>
					<display:column title="事项名称"  sortable="true" sortName="subject"  style="text-align:left;" >
							<c:choose>
									<c:when test="${taskItem.description eq '36'}">
										<a name="subject"  href="#${taskItem.id}" onclick="javascript:restartTask(${taskItem.id},this)" title='${taskItem.subject}'  <c:if test="${taskItem.hasRead == 0}"> class='message close-message'</c:if><c:if test="${taskItem.hasRead != 0}"> class='message open-message'</c:if> >${f:subString(taskItem.subject)}</a>
									</c:when>
								<c:otherwise>
										<a name="subject"  href="#${taskItem.id}" onclick="javascript:executeTask(${taskItem.id},this)" title='${taskItem.subject}' <c:if test="${taskItem.hasRead == 0}"> class='message close-message'</c:if><c:if test="${taskItem.hasRead != 0}"> class='message open-message'</c:if> >${f:subString(taskItem.subject)}</a>
								</c:otherwise>
							</c:choose>
							
					</display:column>
					<display:column property="processName" title="流程名称"  sortable="true" sortName="processName"  style="text-align:left"></display:column>
					<display:column  title="创建人" sortable="true" sortName="creator" style="text-align:left">
						<a href="javascript:userDetail('${taskItem.creatorId}');">${taskItem.creator}</a>
					</display:column>
					<display:column title="创建时间" sortable="true" sortName="create_time_">
						<fmt:formatDate value="${taskItem.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</display:column>
					<display:column title="状态" style="width:30px;" >
						<c:choose>
							<c:when test="${taskItem.taskStatus==1}">审批中</c:when>
							<c:when test="${taskItem.taskStatus==5}">已撤销</c:when>
							<c:when test="${taskItem.taskStatus==6}">已驳回</c:when>
							<c:when test="${taskItem.taskStatus==7}">已追回</c:when>
						</c:choose>
					</display:column>
					<display:column title="待办类型"  style="width:30px;">
					
						<c:choose>
							<c:when test="${taskItem.description=='-1'}">
								<span class="green">待办</span>
							</c:when>
							<c:when test="${taskItem.description eq '21' }" >
								<span class="brown">转办</span>
							</c:when>
							<c:when test="${taskItem.description eq '15' }" >
								<span class="orange">沟通意见</span>
							</c:when>
							<c:when test="${taskItem.description eq '26' }" >
								<span class="brown">代理</span>
							</c:when>
							<c:when test="${taskItem.description eq '38' }" >
								<span class="red">流转意见</span>
							</c:when>
						</c:choose>
					</display:column>
				</display:table>
              
           		
		</div>
		 
	</div>
	<div style="float: inherit;"><a href="##" onclick="more()" style="float: right;">更多(more)</a></div>
	
	<!--     <div><a href="##" onclick="refreshPortal()">test</a></div>	 -->	
</body>
</html>


