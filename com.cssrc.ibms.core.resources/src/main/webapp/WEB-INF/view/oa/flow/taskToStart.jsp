<%@ page pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%><!-- 定义了浏览器的文本模式（防止IE8进入杂项） -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@include file="/commons/include/customForm.jsp"%>
<%@include file="/commons/include/ueditor.jsp"%>
<html>
<head>
<title>流程任务-[${task.name}]执行</title>
<f:link href="from-jsp.css"></f:link>
<script type="text/javascript" src="${ctx}/jslib/ibmssign/IbmsWebSign.js"></script><!-- 签章js-->
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/ReportUtil.js"></script><!-- 报表打印浏览js-->
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/VoteContentUtil.js"></script><!-- 审批意见控件js-->
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/TaskJumpUtil.js"></script><!-- 流程跳转js-->
<link href="${ctx}/jslib/ibmssign/IbmsWebSign.css" rel="stylesheet" type="text/css" />
<%@include file="/commons/include/msg.jsp"%>
<!--start  自定义js文件，css文件  -->
${headHtml}
<!--end    自定义js文件，css文件  -->
<link rel="stylesheet" href="${ctx}/styles/default/css/8ddp/dialog.css">
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/TaskSignWindow.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/TaskAddSignWindow.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/TaskBackWindow.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/TaskImageUserDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/tabOperator.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/BpmTaskExeAssignDialog.js"></script>

<script type="text/javascript">
$(function(){
	//初始化流程图
	$(".flowchart").each(function(){
		$(this).removeClass("flowchart");
		var url=$(this).attr("url");
		if(url){
            $(this).load(url);
        }
	});
	
	//add by LH 2020/3/11 流程中的附件不能删除
	if(typeof taskDefinitionKey != "undefined" && !$.isEmpty(taskDefinitionKey)){
		$("a[onclick*='AttachMent.delFile']").remove();//所有删除文件的a标签移除
	}
})
</script>

<style type="text/css" media="print">
.noprint {
	display: none;
}

.printForm {
	display: "block";
}
</style>
<script type="text/javascript">

var dialog = null;
if(frameElement){
	dialog = frameElement.dialog;
	dialog.set('title','流程任务-[${task.name}]执行');
}
var taskId="${task.id}";
var taskDefinitionKey="${task.taskDefinitionKey}";
var isExtForm=${isExtForm};
var isEmptyForm=${isEmptyForm};
var isSignTask=${isSignTask};
var isHidePath=${isHidePath};
var isManage=${isManage};
var isNeedSubmitConfirm=${bpmDefinition.submitConfirm==1};
var isHandChoolse=${isHandChoolse};
var creatorId=${processRun.creatorId};
var bpmGangedSets = ${bpmGangedSets};
var __formDefId__="${processRun.formDefId}";
var __pk__="${processRun.businessKey}";

var curUserId=${curUserId};
var curUserName='${curUserName}';
var curUserLoginName='${curUserLoginName}';
var curDate='${curDate}';
// 当前时间:年月日时分秒
var curDateTime = '${curDateTime}';
// 获取对象bean 通过 processRunMap.creator的方式可以获取类 processRun.java中的参数值。
var processRunJson = ${processRunMap};
var processRunMap = eval(processRunJson);
// var creator = processRunMap.creator;
// var creatorId =processRunMap.creatorId;
// var createtime = processRunMap.createtime;
// 获取所有流程变量的json对象。
var variablesJson = ${variables};
var variablesMap = eval(variablesJson);
// alert(variablesMap.relRecordIds);
// 操作类型
// 1.完成任务
// 2.保存数据
var operatorType=1;
// ibms web sign 控件
var ibmsSign;
// 审批意见控件
var voteContent;
var spyjModels=${spyjModels};
var isCanAssignee=${isCanAssignee};
var defId="${bpmDefinition.defId}";
var curFlowKey="${bpmDefinition.actDefKey}";
var runId="${processRun.runId}";


</script>

</head>
<body>
	<div class="l-tab-loading" style="display: none;"></div>
	<form id="frmWorkFlow" method="post">
		<div class="panel">
			<div class="panel-top">
				<!-- 悬浮工具栏实现的对象topNavWrapper 和 topNav 名称ID可以更改,但要和css的对象一致-->
				<div class="l-layout-header noprint">
					任务审批处理--
					<b>${task.name}</b>
					--
					<i>[${bpmDefinition.subject}-V${bpmDefinition.versionNo}]</i>
				</div>
				<c:choose>
					<c:when test="${(empty task.executionId) && task.description=='15' }">
						<jsp:include page="incTaskNotify.jsp"></jsp:include>
					</c:when>
					<c:when test="${(empty task.executionId) && (task.description=='38' || task.description=='39') }">
						<jsp:include page="incTaskTransTo.jsp"></jsp:include>
					</c:when>
					<c:otherwise>
						<jsp:include page="incToolBarNode.jsp"></jsp:include>
					</c:otherwise>
				</c:choose>
			</div>

			<div class="panel-body">
				<c:choose>
					<c:when test="${bpmNodeSet.isHidePath==0||isManage==1}">
						<div id="jumpDiv" class="noprint" style="display: none;" tipInfo="正在加载表单请稍候..."></div>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${bpmNodeSet.isHideOption==0 && task.description!='15' &&  task.description!='38' && task.description!='39'}">
						<div class="noprint" id="sign-panel-body">
							<jsp:include page="incTaskOpinion.jsp"></jsp:include>
						</div>
					</c:when>
				</c:choose>
				<div class="printForm">
					<c:choose>
						<c:when test="${isEmptyForm==true}">
							<div class="noForm">没有设置流程表单。</div>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${isExtForm}">
									<c:choose>
										<c:when test="${!empty detailUrl}">
											<iframe id="frameDetail" src="${detailUrl}" scrolling="no" frameborder="no" width="100%" height="100%"></iframe>
										</c:when>
										<c:otherwise>
											<div class="printForm" id="divExternalForm" formUrl="${form}"></div>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<div class="printForm" type="custform">${form}</div>
									<input type="hidden" name="formData" id="formData" />
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</div>
				<input type="hidden" id="taskId" name="taskId" value="${task.id}" />
				<%--驳回和撤销投票为再次提交 --%>
				<c:choose>
					<c:when test="${processRun.status eq 5 or processRun.status eq 6}">
						<input type="hidden" id="voteAgree" name="voteAgree" value="34" />
					</c:when>
					<c:otherwise>
						<input type="hidden" id="voteAgree" name="voteAgree" value="1" />
					</c:otherwise>
				</c:choose>
				<input type="hidden" id="back" name="back" value="" />
				<input type="hidden" id="subbusinessKey" name="subbusinessKey" value="" />
				<input type="hidden" id="isBackToExtSub" name="isBackToExtSub" value="" />
				<input type="hidden" id="ibmsSignIds" name="ibmsSignIds" value="${ibmsSignIds}" />
				<input type="hidden" name="actDefId" value="${bpmDefinition.actDefId}" />
				<input type="hidden" name="defId" value="${bpmDefinition.defId}" />
				<input type="hidden" id="isManage" name="isManage" value="${isManage}" />
				<input type="hidden" id="businessKey" name="businessKey" value="${processRun.businessKey}" />
				<input type="hidden" id="startNode" name="startNode" value="${toBackNodeId}" />
				<input type="hidden" name="ctx" value="${ctx}" />
				<input type="hidden" name="formKey" value="${bpmNodeSet.formKey}" />
				<input type="hidden" name="tableId" value="${bpmNodeSet.formKey}" />
				<input type="hidden" name="pageType" value="flowEdit" />
				<input type="hidden" name="taskDefinitionKey" value="${task.taskDefinitionKey}" />
			</div>
		</div>
	</form>
</body>
<script type="text/javascript">
$(document).ready(function(){
	setTimeout(function(){
		$('div[name="editable-input"] .panel-body').removeAttr('style');
	},500)
})


</script>
</html>