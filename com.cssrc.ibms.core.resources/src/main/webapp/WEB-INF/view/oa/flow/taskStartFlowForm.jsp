<%@ page pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>流程启动--${bpmDefinition.subject} --版本:${bpmDefinition.versionNo}</title>
<%@include file="/commons/include/customForm.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/BpmImageDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/ReportUtil.js"></script>
<!-- 报表打印浏览js-->
<script type="text/javascript" src="${ctx}/jslib/ibms/panelBodyHeight.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/taskStartFlowForm.js"></script>
<!--panel-body高度计算-->
<!--start  自定义js文件，css文件  -->
${headHtml}
<!--end    自定义js文件，css文件  -->
<script type="text/javascript">
$(function(){
	$(".taskopinion").each(function(){
		$(this).removeClass("taskopinion");
		var actInstId=$(this).attr("instanceId");
		$(this).load("${ctx}/oa/flow/taskOpinion/listform.do?actInstId="+actInstId);
	});
	//初始化流程图
	$(".flowchart").each(function(){
		$(this).removeClass("flowchart");
		var url=$(this).attr("url");
		if(url){
            $(this).load(url);
        }
	});
})
</script>
<script type="text/javascript">

var dialog;
if(frameElement){
	dialog=frameElement.dialog;
	if(dialog.options.title == "提示"){
		dialog.set('title','流程启动');
	}
}

var taskDefinitionKey=null;
var isExtForm=${isExtForm};
var isFormEmpty=${isFormEmpty};
var isNeedSubmitConfirm=${bpmDefinition.submitConfirm==1};
var bpmGangedSets=[];
var curUserId="${curUserId}";
var curUserName='${curUserName}';
var curUserLoginName='${curUserLoginName}';
var curDate='${curDate}';
// 当前时间:年月日时分秒
var curDateTime = '${curDateTime}';

var hasLoadComplete=false;
var actDefId="${bpmDefinition.actDefId}";
var form;
var globalBusinessKey="";
if(${!empty bpmGangedSets}){
	bpmGangedSets = ${bpmGangedSets};
}
</script>

<style type="text/css" media="print">
.noprint {
	display: none;
}

.printForm {
	display: block !important;
}

.noForm {
	font-size: 14px;
	font-weight: bold;
	text-align: center;
}

input, select {
	border: none !important;
}

.link {
	display: none !important;
}

select {
	position: absolute;
	clip: rect(0px, 100px, 21px, 0px);
	width: 120px;
	margin-top: -10px;
}

.l-text {
	border: none;
}

.l-trigger {
	display: none;
}

td {
	text-align: left !important;
	min-width: 120px;
}
</style>
</head>
<body>
	<div class="l-tab-loading" style="display: none;"></div>
	<form id="frmWorkFlow" method="post">
		<input type="hidden" id="actDefId" name="actDefId" value="${bpmDefinition.actDefId}" />
		<input type="hidden" name="defId" value="${bpmDefinition.defId}" />
		<input type="hidden" id="businessKey" name="businessKey" value="${businessKey}" />
		<input type="hidden" name="runId" value="${runId}" />
		<input type="hidden" id="startNode" name="startNode" value="" />
		<input type="hidden" name="ctx" value="${ctx}" />
		<input type="hidden" name="formKey" value="${formKey}" />
		<input type="hidden" name="tableId" value="${tableId}" />
		<input type="hidden" name="pageType" value="flowEdit" />
		<div class="panel">
			<%@include file="incToolBarStart.jsp"%>
			<!-- 最上面一排按钮button栏 -->
			<div class="panel-body printForm" style="width: 100%;">
				<div style="padding: 6px 8px 3px 12px;" class="noprint">
					<b>流程简述：</b>${bpmDefinition.descp}
				</div>
				<c:choose>
					<c:when test="${isMultipleFirstNode}">
						<div id="flowNodeList">
							<table class="table-grid">
								<thead>
									<tr>
										<th height="28" width="20%">选择起始路径</th>
										<td>
											<c:forEach items="${flowNodeList}" var="flowNode">
												<lable>${flowNode.nodeName}<input type="radio" name="flowNode" value="${flowNode.nodeId}" /></lable>
											</c:forEach>
										</td>
									</tr>
								</thead>
							</table>
						</div>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${bpmDefinition.showFirstAssignee==1}">
						<div id="jumpDiv" class="noprint" style="display: none;" tipInfo="正在加载表单请稍候..."></div>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${isFormEmpty==true}">
						<div class="noForm">没有设置流程表单。</div>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${isExtForm}">
								<div id="divExternalForm" formUrl="${form}"></div>
							</c:when>
							<c:otherwise>
								<div type="custform" style="height: 87%">${form}</div>
								<input type="hidden" name="formKey" value="${formKey}" />
								<input type="hidden" name="formData" id="formData" />
								<c:if test="${not empty  paraMap}">
									<c:forEach items="${paraMap}" var="item">
										<input type="hidden" name="${item.key}" value="${item.value}" />
									</c:forEach>
								</c:if>
							</c:otherwise>
						</c:choose>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</form>
</body>

</html>