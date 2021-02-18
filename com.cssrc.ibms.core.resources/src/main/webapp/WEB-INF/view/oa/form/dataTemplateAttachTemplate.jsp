<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>流程事件脚本编辑</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerTab.js"></script>
<script type="text/javascript" src="${ctx}/jslib/javacode/codemirror.js"></script>
<script type="text/javascript" src="${ctx}/jslib/javacode/InitMirror.js"></script>
<script type="text/javascript">
	var defId="${defId}";
	$(function() {
           $("#tabHtml").ligerTab({height:600});
           $("a.save").click(function() {
				$("#fileHtmlForm").attr("action","htmlSave.do");
				submitForm();
			});
           
           //初始化附件模板
           $("a.reload").click(function() {
				$.ligerDialog.confirm( "该操作会初始化模板，是否确定操作！","提示信息", function(rtn) {
					if(rtn){
						var url=location.href;
						href=url+"&reload=1";
				    	location.href=href;
					}else {
						dialog.close();
					}
	   		});
		});
	});
	
	function submitForm(){
		InitMirror.save();
		var options={};
		if(showResponse){
			options.success=showResponse;
		}
		var frm=$('#fileHtmlForm').form();
		frm.ajaxForm(options);
		if(frm.valid()){
			frm.submit();
		}
	}
	
	function showResponse(responseText) {
		var obj = new com.ibms.form.ResultMessage(responseText);
		if (!obj.isSuccess()) {
			$.ligerDialog.err('出错信息',"附件模板编辑HTML编辑失败",obj.getMessage());
			return;
		} else {
			$.ligerDialog.success('附件模板HTML编辑成功!','提示信息',function() {
				window.close();
			});
		}
	}
	
</script>
</head>
<body>
	<c:set var="fileHtml" value="${htmlMap.fileHtml}"></c:set>
	<c:set var="attachHtml" value="${htmlMap.attachHtml}"></c:set>

	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label"> 文件附件HTML模板编辑 </span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" id="btnSearch">保存</a>
					</div>
					
					<div class="group">
						<a class="link del" onclick="window.close()">关闭</a>
					</div>
					<div class="group">
						<a class="link reload" id="reload">初始化</a>
					</div>

				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="fileHtmlForm" method="post" action="htmlSave.do">
				<div id="tabHtml">
					<div tabid="fileHtml" title="文件树HTML">

						<table class="table-detail" cellpadding="0" cellspacing="0"
							border="0">
							<tr>
								<th width="20%">HTML编辑内容:<span class="required">*</span>
								</th>
								<td><textarea id="fileHtml" name="fileTempHtml"
										 codemirror="true" mirrorheight="600px" rows="10" cols="80">${fn:escapeXml(fileHtml)}></textarea>
								</td>
							</tr>
						</table>
					</div>
					<div tabid="attachHtml" title="附件列表HTML">
						<table class="table-detail" cellpadding="0" cellspacing="0"
							border="0">
							<tr>
								<th width="20%">HTML编辑内容::<span class="required">*</span>
								</th>
								<td><textarea id="attachHtml" name="attacTempHtml"
										 codemirror="true" mirrorheight="600px" rows="10" cols="80">${fn:escapeXml(attachHtml)}</textarea>
								</td>
							</tr>
						</table>
					</div>
					<input type="hidden" id="formKey" name="formKey" value="${formKey}"class="inputText" /> 
					<input type="hidden" id="tableId" name="tableId" value="${tableId}" class="inputText" />
					<input type="hidden" id="dataTempId" name="dataTempId" value="${dataTempId}" class="inputText" />
				</div>
			</form>

		</div>
	</div>
</body>
</html>