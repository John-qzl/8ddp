
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>参数编辑窗口</title>
	<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
	<script type="text/javascript">
		$(function() {
			$("a.save").click(function() {
				$("#sysBackUpRestoreForm").attr("action","save.do");
				$("#saveData").val(1);
				submitForm();
			});
		});
		//提交表单
		function submitForm(){
			var options={};
			if(showResponse){
				options.success=showResponse;
			}
			var frm=$('#sysBackUpRestoreForm').form();
			frm.ajaxForm(options);
			if(frm.valid()){
				frm.submit();
			}
		}
		
		function showResponse(responseText) {
			var obj = new com.ibms.form.ResultMessage(responseText);
			if (obj.isSuccess()) {
				$.ligerDialog.confirm(obj.getMessage()+",是否继续操作","提示信息", function(rtn) {
					if(rtn){
						window.location.href = "${ctx}/oa/system/sysBackUpRestore/list.do";
					}else{
						window.location.href = "${ctx}/oa/system/sysBackUpRestore/list.do";
					}
				});
			} else {
				$.ligerDialog.error(obj.getMessage(),"提示信息");
			}
		}
	</script>
</head>
<body>
<div class="panel">
	<div class="panel-top">
		<c:choose>
			    <c:when test="${sysBackUpRestore.backid !=null}">
			        <span class="tbar-label">编辑/span>
			    </c:when>
			    <c:otherwise>
			        <span class="tbar-label">添加备份</span>
			    </c:otherwise>			   
		 </c:choose>
		<div class="panel-toolbar">
			<div class="toolBar">
				<div class="group"><a class="link save" id="dataFormSave" href="####">保存</a></div>
				
				<div class="group"><a class="link back" href="list.do">返回</a></div>
			</div>
		</div>
	</div>
	<div class="panel-body">
		<form id="sysBackUpRestoreForm" method="post" action="save.do">
			<table class="table-detail" cellpadding="0" cellspacing="0" border="0" type="main">
				<tr>
					<th width="20%">备份名称: </th>
			       	<td><input type="text" id="backname" name="backname" value="${sysBackUpRestore.backname}" validate="{required:true}" class="inputText"/></td>
				</tr>
				<tr>
					<th width="30%">备注: </th>
					<td><input type="text" id="comments" name="comments" value='${sysBackUpRestore.comments}' style="width: 400px" class="inputText" validate="{required:false}"  /></td>
				</tr>
			</table>
			<input type="hidden" name="id" value="${sysBackUpRestore.backid}" />
		</form>
	</div>
</div>
</body>
</html>
