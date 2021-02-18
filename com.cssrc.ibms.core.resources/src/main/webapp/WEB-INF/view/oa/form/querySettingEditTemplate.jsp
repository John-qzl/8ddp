<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>

<html>
<head>
	<title>编辑 数据模板的模板</title>
	<%@include file="/commons/include/form.jsp" %>
	<link  rel="stylesheet" type="text/css" href="${ctx}/jslib/codemirror/lib/codemirror.css" >
	<script type="text/javascript" src="${ctx}/jslib/codemirror/lib/codemirror.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/codemirror/mode/xml/xml.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/codemirror/mode/javascript/javascript.js"></script>
    <script type="text/javascript" src="${ctx}/jslib/codemirror/mode/css/css.js"></script>
    <script type="text/javascript" src="${ctx}/jslib/codemirror/mode/htmlmixed/htmlmixed.js"></script>
	<script type="text/javascript">
		var editor=null;
		$(function() {
			var options={};
			if(showResponse){
				options.success=showResponse;
			}
			$('#sysQuerySettingTemplateForm').ajaxForm(options);
			$("a.save").click(function() {
				editor.save();
				$('#sysQuerySettingTemplateForm').submit();
			});
			var width = $("#templateHtml").width();
			var height = $("#templateHtml").height();
			editor = CodeMirror.fromTextArea(document.getElementById("templateHtml"), {
				mode: "text/html",
				tabMode: "indent",
				lineNumbers: true
			 });
			editor.setSize(width,height);
		});
		
		function showResponse(responseText) {
			var obj = new com.ibms.form.ResultMessage(responseText);
			if (obj.isSuccess()) {
				$.ligerDialog.confirm( obj.getMessage()+",是否继续操作","提示信息", function(rtn) {
					if(rtn){
						/* this.close(); */
					}else{
						this.close();
					}
				});
			} else {
				$.ligerDialog.err('出错信息',"编辑自定义表管理显示模板失败",obj.getMessage());
			}
		}
	</script>
</head>
<body>
<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">编辑自定义表管理显示模板</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" href="####">保存</a></div>
					
					<div class="group"><a class="link close" href="####" onclick="window.close();">关闭</a></div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="sysQuerySettingTemplateForm" method="post" action="saveTemplate.do" >
				<table class="table-detail">
					<tr>
						<th width="5%" nowrap="nowrap">模板</th>
						<td colspan="3">
							<textarea id="templateHtml" name="templateHtml" style="width: 99%;height: 700px;">${sysQuerySetting.templateHtml }</textarea>
						</td>
					</tr>
				</table>
				<input name="id" type="hidden" value="${sysQuerySetting.id }"/>
			</form>
		</div>
</div>
</body>
</html>
