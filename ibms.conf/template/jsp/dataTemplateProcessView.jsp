<%@page pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>流程监控</title>
<%@include file="/commons/include/customForm.jsp"%>
<script type="text/javascript">

var dialog = null; //调用页面的dialog对象(ligerui对象)
if(frameElement){
	dialog = frameElement.dialog;
	if(!dialog && dialog!=undefined){
		dialog.set('title','流程监控');
	}
}
$(function(){
	//高度计算
	$('.l-tab-content').height($('#formTab').height()-$('.l-tab-links').height()-40);
	
	$(".taskopinion").each(function(){
		$(this).removeClass("taskopinion");
		var actInstId=$(this).attr("instanceId");
		$(this).load("${ctx}/oa/flow/taskOpinion/listform.do?actInstId="+actInstId);
	});
	$(".flowchart").each(function(){
	    $(this).removeClass("flowchart");
	    var actInstId=$(this).attr("instanceId");
	    $(this).load("${ctx}/oa/flow/processRun/processImageDiv.do?actInstId="+actInstId+"&notShowTopBar=0");
	 });
	 //高度计算
	 $('.l-tab-content').height($('#formTab').height()-$('.l-tab-links').height()-40);
})

</script>
</head>
<body>
	<div id="formTab" style="height:100%;overflow-y:auto">
		<div title="${processRun.processName }">
			<div class="flowchart" name="editable-input" instanceid="${processRun.actInstId }" style="height:50%;"></div> 
<%-- 			<iframe src="${ctx}/oa/flow/processRun/processImage.do?actInstId=${processRun.actInstId }&notShowTopBar=0" name="flowchart" id="flowchart" marginwidth="0" marginheight="0" frameborder="0"  scrolling="yes" width="100%"></iframe>	  --%>
			<div class="l-layout-header">审批历史</div>
			<div class="taskopinion" name="editable-input" instanceid="${processRun.actInstId }" style="height:50%;"> 
			</div>
		</div>
		<c:if test="${!empty processConditionList}">
			<c:forEach items="${processConditionList}" var="processRun" varStatus="status">
				<div title="${processRun.processName }">	
					<div class="flowchart" name="editable-input" instanceid="${processRun.actInstId }" style="height:50%;"></div>
<%-- 					<iframe src="${ctx}/oa/flow/processRun/processImage.do?actInstId=${processRun.actInstId }&notShowTopBar=0" name="flowchart" id="flowchart" marginwidth="0" marginheight="0" frameborder="0"  scrolling="yes" width="100%"></iframe> --%>
					<div class="l-layout-header">审批历史</div>
					<div class="taskopinion" name="editable-input" instanceid="${processRun.actInstId }" style="height:500px;"> 
					</div> 
				</div>
			</c:forEach> 
		</c:if>
	</div>
</body>
</html>