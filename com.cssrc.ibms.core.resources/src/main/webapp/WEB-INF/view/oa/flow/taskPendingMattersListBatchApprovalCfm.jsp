<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<%@include file="/commons/include/get.jsp" %>
<title>批量审批</title>
<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerDialog.js"></script>
<script type="text/javascript">
var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
var taskIds="${param.taskIds}";
function save(){
	var result=$("#frmApprove").form().valid();
	if(!result) return;
	
	
	var params= {taskIds:taskIds,opinion:document.getElementById("opinion").value};
	$.ligerDialog.confirm('批量批准,只能批准“同意”,<br/>是否确认批量审批吗？','提示',function(rtn){
		if(!rtn) return ;
		$.post("${ctx}/oa/flow/task/batComplte.do",params,function(msg){
		var obj=new com.ibms.form.ResultMessage(msg);
		if(obj.isSuccess()){
				$.ligerDialog.successExt('提示','详细审批结果如下：',obj.getMessage(),function(){
	    		dialog.get("sucCall")('ok');
				dialog.close(); 
	    	 });
		}else{
			$.ligerDialog.err('提示','出错了!',obj.getMessage());
		}
	});
	});
 }
</script>
</head>

<body>
	<div class="panel">
	<div class="hide-panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">批量审批</span>
			</div>
		</div>
		</div>
		<div class="panel-body">
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" id="dataFormSave" href="####" onclick="save()">审批</a></div>
					
					<div class="group"><a class="link close"  href="####" onclick="dialog.close();">关闭</a></div>
				</div>
			</div>
			<form id="frmApprove" class="plat-form">
				<div  class="row plat-row"><span class="label" style="font-weight: bold;">审批意见：</span></div>
				<div class="row">
					<span class="label"><textarea rows="7" cols="61" id="opinion" name="opinion" validate="{required:true}"></textarea></span>
				</div>
				
			</form>
		</div>
	</div>

</body>
</html>