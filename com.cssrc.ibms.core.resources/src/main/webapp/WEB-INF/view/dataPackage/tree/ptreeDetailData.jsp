<%@page pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>表单明细</title>
<%@include file="/commons/include/customForm.jsp"%>
<!--start  自定义js文件，css文件  -->
${headHtml}
<!--end    自定义js文件，css文件  -->
<script type="text/javascript">
var dialog = null; //调用页面的dialog对象(ligerui对象)
if(frameElement){
	dialog = frameElement.dialog;
	if(!dialog && dialog!=undefined){
		dialog.set('title','表单明细');
	}
}
$(function(){
	$(".taskopinion").each(function(){
		$(this).removeClass("taskopinion");
		var actInstId=$(this).attr("instanceId");
		$(this).load("${ctx}/oa/flow/taskOpinion/listform.do?actInstId="+actInstId);
	});
})

</script>
</head>
<body style="overflow: auto">
	<c:if test="${not empty dbomFKName}">
		<!-- DBOM中使用，目的在于在新增表单数据时，父节点字段数据值自动带过来 -->
		<input type="hidden" name="${dbomFKName}" value="${dbomFKValue}" />
		<input type="hidden" name="dbomFKName" value="${dbomFKName}" />
		<input type="hidden" name="dbomFKValue" value="${dbomFKValue}" />		
	</c:if>
	<div style="height:100%">${form}</div>
	</div>
	<input type="hidden" name="ctx" value="${ctx}" />
	<input type="hidden" name="formKey" value="${formKey}"/>
	<input type="hidden" name="dataId" value="${pk}" />
	<input type="hidden" name="pageType" value="detail" />
</body>

</html>