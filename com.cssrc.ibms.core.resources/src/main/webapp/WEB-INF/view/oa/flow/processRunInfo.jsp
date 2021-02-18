<%@ page pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<%@include file="/commons/include/customForm.jsp"%>
<title>流程明细</title>
<f:link href="from-jsp.css"></f:link>
<script type="text/javascript" src="${ctx}/jslib/util/form.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/ProcessUrgeDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/FlowUtil.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/FlowRightDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/CheckVersion.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/SelectUtil.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/FlowUtil.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibmssign/IbmsWebSign.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/ProcessRunInfo.js"></script>
<link href="${ctx}/jslib/ibmssign/IbmsWebSign.css" rel="stylesheet" type="text/css" />
<!--start  自定义js文件，css文件  -->
${headHtml}
<!--end    自定义js文件，css文件  -->
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
})
var isExtForm=eval('${isExtForm}');
var taskDefinitionKey="${taskOpinion.taskKey}";
var runId="${processRun.runId}";
var curUserId="${taskOpinion.exeUserId}";
var curUserName='${taskOpinion.exeFullname}';
var curUserLoginName='${taskOpinion.exeFullname}';
var signId="${taskOpinion.signId}";
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">流程明细--${processRun.subject}</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<%-- <c:if test='${isCanRedo}'>
						<div class="group">
							<a href="javascript:;" onclick="redo(${processRun.runId})" class="link redo">
								
								追回
							</a>
						</div>
					</c:if> --%>
					<%-- <c:if test="${isCanRecover}"> --%>
						
						<%-- <div class="group">
							<a href="javascript:void(0);" onclick="recover(${processRun.runId})" class="link redo">
								
								撤销
							</a>
						</div> --%>
						 <c:if test="${!isEnd}"> 
						<div class="group">
							<a href="javascript:void(0);" onclick="urge(${processRun.actInstId})" class="link urge">
								
								催办
							</a>
						</div>
					</c:if>
					<c:if test="${isFirst and (processRun.status==4 or processRun.status==5)}">
						
						<div class="group">
							<a href="javascript:executeTask('${processRun.actInstId}')" class="link run">
								
								重新提交
							</a>
						</div>
						
						<div class="group">
							<a href="javascript:delByInstId(${processRun.actInstId})" class="link del">
								
								删除
							</a>
						</div>
					</c:if>
					<%-- <c:if test="${isCopy}">
						
						<div class="group">
							<a href="javascript:void(0);" class="link copy"
								onclick="checkVersion({type:false,defId:'${processRun.defId}',businessKey:'${processRun.businessKey}'});">
								
								复制
							</a>
						</div>
					</c:if> --%>
					<c:if test="${isFinishedDiver}">
						
						<div class="group">
							<a href="javascript:void(0);" onclick="divert(${processRun.runId},${processRun.actInstId})" class="link goForward">
								
								转发
							</a>
						</div>
					</c:if>
					
					<%-- <div class="group">
						<a action="${ctx}/oa/flow/processRun/get.do?isOpenDialog=1&link=1&runId=${processRun.runId}" onclick="showProcessRunInfo(this)"
							class="link detail">
							
							运行明细
						</a>
					</div> --%>
					
					<%-- <div class="group">
						<a action="${ctx}/oa/flow/processRun/processImage.do?aprocess&isOpenDialog=1&link=1&runId=${processRun.runId}"
							onclick="showProcessRunInfo(this)" class="link flowDesign">
							
							流程图
						</a>
					</div> --%>
					<div class="group">
						<a action="${ctx}/oa/flow/processRun/processImageList.do?actInstId=${processRun.actInstId}&notShowTopBar=0"
							onclick="showProcessRunInfo(this)" class="link flowDesign">
							
							流程图及审批历史
						</a>
					</div>
					
					<%-- <div class="group">
						<a action="${ctx}/oa/flow/taskOpinion/list.do?action=process&isOpenDialog=1&link=1&runId=${processRun.runId}" onclick="showProcessRunInfo(this)"
							class="link history">
							
							审批历史
						</a>
					</div> --%>
					
					<c:if test="${processRun.status==2}">
						<div class="group">
							<a action="${ctx}/oa/flow/proCopyto/getCopyUserByInstId.do?isOpenDialog=1&link=1&runId=${processRun.runId}" onclick="showProcessRunInfo(this)"
								class="link copyTo">
								
								抄送人
							</a>
						</div>
						
					</c:if>
					<c:if test="${isPrintForm}">
						<a href="javascript:void(0);" onclick="printForm(${processRun.runId})" class="link print">
							
							打印表单
						</a>
						
					</c:if>
					<%-- <div class="group">
						<a href="javascript:void(0);" onclick="downloadToWord(${processRun.runId})" class="link print">
							
							导出成word文档
						</a>
					</div> --%>
					
				</div>
			</div>
		</div>
		<div class="panel-body" id="ibms-sign-panel" style="width: 98%; height: 90%; margin: 0 auto;">
			<div class="panel-table">
				<br>
				<c:choose>
					<c:when test="${bpmNodeSet.isHideOption==0}">
						<div class="noprint">
							<table class="table-detail">
								<tbody>
									<tr>
										<th>意见</th>
										<td>
											<c:if test="${spyjModel}">
												<%--20200118 zmz 将意见框的宽度从184调至0.7--%>
												<textarea id="opsition" name="opsition" style="resize: none; width: 70%; min-height: 57px;" disabled="disabled">${taskOpinion.opinion}</textarea>
											</c:if>
											<c:if test="${!spyjModel}">
												${taskOpinion.opinion}
											</c:if>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${isExtForm==true }">
						<div id="divExternalForm" formUrl="${form}"></div>
					</c:when>
					<c:otherwise>
						${form}
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
	<input type="hidden" id="businessKey" name="businessKey" value="${processRun.businessKey}">
	<input type="hidden" id="ctx" name="ctx" value="${ctx}">
	<input type="hidden" id="formKey" name="formKey" value="${formKey}">
	<input type="hidden" name="pageType" value="flowDetail" />
	<input type="hidden" name="dataId" value="${processRun.businessKey}" />
</body>
</html>