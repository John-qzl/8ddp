<%@ page language="java" contentType="text/html; charset=UTF-8"   pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
	$(function(){//隐藏意见控件
		var opinion = $("textarea[opinionname]");
		opinion.removeAttr("validate");
		opinion.hide();
	});
	
	function beforeClick(operatorType){<c:if test="${not empty mapButton.button}">
		switch(operatorType){<c:forEach items="${mapButton.button }" var="btn"  ><c:if test="${not empty btn.prevscript}">
				case ${btn.operatortype}:
				${btn.prevscript}
				break;</c:if></c:forEach>
			}</c:if>
	}
	
	function afterClick(operatorType){<c:if test="${not empty mapButton.button}">
		switch(operatorType){<c:forEach items="${mapButton.button }" var="btn" ><c:if test="${not empty btn.afterscript}">
			case ${btn.operatortype}:
				${btn.afterscript}
				break;</c:if></c:forEach>
			}</c:if>
	}
	
	function openOpinionDialog(){
		var rtn=CustomForm.validate();
		if(!rtn){
			$.ligerDialog.warn('表单验证不成功,请检查表单是否正确填写!','提示');
			return;
		}
		var obj = {data:CustomForm.getData()};
		//增加抄送人员
		var url=__ctx + "/oa/flow/task/transToOpinionDialog.do?taskId=${task.id}";
		url=url.getNewUrl();
		
		DialogUtil.open({
			height:300,
			width: 500,
			title : '抄送人员',
			url: url, 
			isResize: true,
			//自定义参数
			obj: obj,
			sucCall:function(rtn){
				if(rtn=="ok"){
					if(window.opener && window.opener.location){
						window.opener.location.href=window.opener.location.href.getNewUrl();
					}
					window.close();
				}
			}
		});
	}
</script>
<div class="panel-top noprint">
	<div class="panel-toolbar">
		<div class="toolBar">
			<div class="group"><a id="btnNotify" class="link agree" onclick="openOpinionDialog()">反馈</a></div>
			
			<div class="group"><a id="btnSave" class="link save">保存</a></div>
			
			<div class="group"><a class="link setting" onclick="showTaskUserDlg()">流程图</a></div>
			
			<div class="group"><a class="link search" onclick="showTaskOpinions()">审批历史</a></div>
			
			<div class="group"><a class="link sendMessage" onclick="showTaskCommunication('沟通')">沟通</a></div>
			
			<div class="group"><a class="link switchuser" onclick="showTaskTransTo()">加签</a></div>
		</div>
	</div>
</div>