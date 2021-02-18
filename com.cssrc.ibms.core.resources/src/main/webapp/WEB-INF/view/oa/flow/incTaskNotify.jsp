<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
function openOpinionDialog(){
	var data=CustomForm.getData();
	//增加抄送人员
	var url=__ctx + "/oa/flow/task/opinionDialog.do?taskId=${task.id}&formData="+data;
	
	url=url.getNewUrl();
	DialogUtil.open({
		height:300,
		width: 500,
		title : '抄送人员',
		url: url, 
		isResize: true,
		//自定义参数
		sucCall:function(rtn){
			if(rtn=="ok"){
				location.href=location.href.getNewUrl();
			}
		}
	});
}
</script>
<div class="panel-top noprint">
	<div class="panel-toolbar">
		<div class="toolBar">
			<div class="group"><a id="btnNotify" class="link agree" onclick="openOpinionDialog()">反馈</a></div>
			
			<div class="group"><a class="link setting" onclick="showTaskUserDlg()">流程图</a></div>
			
			<div class="group"><a class="link search" onclick="showTaskOpinions()">审批历史</a></div>
			
			<div class="group"><a class="link sendMessage" onclick="showTaskCommunication('沟通')">沟通</a></div>
		</div>	
	</div>
</div>