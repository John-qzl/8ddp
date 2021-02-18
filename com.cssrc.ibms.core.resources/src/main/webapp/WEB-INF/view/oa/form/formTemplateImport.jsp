<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>批量导入模板</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript">
	var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	dialog.set('title', '批量导入模板');
	$(function() {
		$("#batchImport").click(function() {
			var options = {
				beforeSubmit : function() {
					var rtn = check();
					if (rtn.flag) {
						$.ligerDialog.waitting('正在导入中,请稍候...');
					} else {
						$.ligerDialog.error(rtn.msg);
						return false;
					}
				},
				success : function(result) {
					$.ligerDialog.closeWaitting();
					var rtn = JSON2.parse(result);
					if(rtn.result=="true"){
						$.ligerDialog.success(rtn.message)
					}else{
						$.ligerDialog.err("", "日志信息", rtn.message);
					}
				}
			};
			$("#bpmFormTemplateForm").ajaxForm(options);
			$("#bpmFormTemplateForm").submit();
		})
	});


	function check() {
		var rtn = {
			flag : false,
			msg : ""
		}
		var xmlFile = $('#xmlFile').val();
		if($.isEmpty(xmlFile)){
			rtn.msg = "请上传文件！";
			return rtn;
		}
		if(xmlFile.indexOf('.xml')==-1){
			rtn.msg = "上传的文件必须是xml类型！";
			return rtn;
		}
		rtn.flag = true;
		return rtn;
	}
</script>
</head>

<body>
	<div class="panel-top">
		<div class="tbar-title">
			<span class="tbar-label">表单模板导入</span>
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
			method="post" action="batImport.do" enctype="multipart/form-data">
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