<%--
	time:2012-12-19 15:38:01
	desc:edit the 自定义表代码模版
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>编辑 自定义表代码模版</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
<script type="text/javascript"src="${ctx}/jslib/ibms/oa/system/ScriptDialog.js"></script>

<script type="text/javascript">
	$(function() {
		var options = {};
		if (showResponse) {
			options.success = showResponse;
		}
		var frm = $('#sysScriptForm').form();
		$("a.save").click(function() {
			frm.ajaxForm(options);
			if (frm.valid()) {
				$('#sysScriptForm').submit();
			}
		});
		
		$("#script_editor").click(function(){
			ScriptDialog({
				callback:function(obj){
					InitMirror.editor.setCode(obj);
				}
			});
		});
	});

	function showResponse(responseText) {
		var obj = new com.ibms.form.ResultMessage(responseText);
		if (obj.isSuccess()) {
			$.ligerDialog.confirm(obj.getMessage() + ",是否继续操作","提示信息",
							function(rtn) {
								if (rtn) {
									this.close();
									$("#sysScriptForm").resetForm();
								} else {
									window.location.href = "${ctx}/oa/system/sysScript/list.do";
								}
							});
		} else {
			$.ligerDialog.error(obj.getMessage(),'提示信息');
		}
	}

	
</script>
<script type="text/javascript">
function selectCategory(obj) {
	var selObj = $(obj);
	$("#category").val(selObj.val());
}
	</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<c:choose>
					<c:when test="${sysScript.id !=null}">
						<span class="tbar-label">编辑自定义表代码模版</span>	
					</c:when>
					<c:otherwise>
						<span class="tbar-label">添加自定义表代码模版</span>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" id="dataFormSave" href="####">保存</a>
					</div>
					
					<div class="group">
						<a class="link back" href="list.do">返回</a>
					</div>
					<div class="group">
						<a class="link update" id="script_editor" href="javascript:;">脚本编辑器</a>
					</div>
					<div class="group">
						<a href="####" class="tipinfo">
					        <span>
			                 	 因为jdbcDao在app-beans.xml文件中注册过。
			                 <br/>因此在groovy脚本中直接写：jdbcDao.upd("UPDATE  CWM_SYS_USER SET PHOTO =15061542312 WHERE USERID = -1")。就能够成功运行。
					       </span>
				        </a>
			       </div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="sysScriptForm" method="post" action="save.do">
				<table class="table-detail" cellpadding="0" cellspacing="0"
					border="0" type="main">
					<tr>
						<th width="20%">脚本名称:</th>
						<td><input type="text" id="name" name="name" value="${sysScript.name}" class="inputText" validate="{required:true,maxlength:200}" /></td>
					</tr>
					<tr>
						<th width="20%">脚本分类:</th>
						<td><input id="category" name="category"
							value="${sysScript.category}" /> <select
							onchange="selectCategory(this)">
								<c:forEach items="${categoryList}" var="catName">
									<option value="${catName}">${catName}</option>
								</c:forEach>
						</select></td>
						</tr>
					
					<tr>
						<th width="20%">备注:</th>
						<td><input type="text" id="memo" name="memo" value="${sysScript.memo}" class="inputText" validate="{required:true,maxlength:200}" style="width: 350px;" /></td>
					</tr>
					<tr>
						<th width="20%">脚本内容:</th>
						<td><textarea name="script" cols="120" rows="20" style="width:550px;">${fn:escapeXml(sysScript.script)}</textarea>
						</td>
					</tr>
				</table>
				<input type="hidden" name="id" value="${sysScript.id}" />
			</form>

		</div>
	</div>
</body>
</html>
