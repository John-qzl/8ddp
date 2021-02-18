<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>(表单设计)批量导入</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript">
	var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	dialog.set('title', '批量导入(表单设计)');
	$(function() {
		$("#batchImport").click(function() {
			if(checkAndDeal()){
				$("#bpmFormTemplateForm").submit();
			}
		})
	});
	
	function checkAndDeal() {
		var rtn = {
			flag : false,
			msg : ""
		}
		var xmlFile = $('#xmlFile').val();
		if($.isEmpty(xmlFile)){
			rtn.msg = "请上传文件！";
			$.ligerDialog.error(rtn.msg);
			return false;
		}
		if(xmlFile.indexOf('.xml')==-1){
			rtn.msg = "上传的文件必须是xml类型！";
			$.ligerDialog.error(rtn.msg);
			return false;
		}
		return true;
	}
</script>
</head>

<body>
	<div class="panel-top">
		<div class="tbar-title">
			<span class="tbar-label">表单设计导入</span>
		</div>
		<div class="panel-toolbar">
			<div class="toolBar">
				<div class="group">
					<a class="link save" id="batchImport">导入</a>
				</div>
				
				<div class="group">
					<a class="link del" onclick="javasrcipt:dialog.close()">关闭</a>
				</div>
			</div>
		</div>
	</div>
	<div class="panel-body">
		<form id="bpmFormTemplateForm" name="bpmFormTemplateForm"
			method="post" action="batImport.do"  onsubmit="checkAndDeal()"  enctype="multipart/form-data">
			<div class="row">

				<table id="tableid" class="table-detail" cellpadding="0"
					cellspacing="0" border="0">
					<tr>
						<th width="22%">选择文件：</th>
						<td width="78%"><input type="file" size="40" name="xmlFile"
							id="xmlFile" /></td>
					</tr>
				</table>
				<br> 提示：<font color=red>请选择*.xml文件进行导入！ </font>
			</div>
		</form>
	</div>
	<!-- end of panel-body -->
</body>
</html>