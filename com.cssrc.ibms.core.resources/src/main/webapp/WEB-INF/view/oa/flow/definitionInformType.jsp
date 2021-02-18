<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>通知方式配置</title>
<base target="_self" />
<%@include file="/commons/include/form.jsp"%>
<f:link href="jquery.qtip.css"></f:link>
<f:link href="form.css"></f:link>
<f:link href="codemirror/lib/codemirror.css"></f:link>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/SelectorInit.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/TemplateDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ckeditor/ckeditor_remind.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/definitionInformType.js"></script>

</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label"> 设置流程任务节点通知方式 </span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<div class="group">
							<a class="link save" id="dataFormSave" href="####">保存</a>
						</div>
					</div>
					<div class="group">
						<div class="group">
							<a class="link del" href="####" onclick="dialog.close()">关闭</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="informTypeForm" method="post" action="saveInformType.do">
				<div style="padding: 8px 8px 8px 8px">
					<b>说明：</b>
					<ul>
						<li>该节点产生任务将下面选择方式发送通知给用户。</li>
					</ul>
				</div>

				<div style="padding: 8px 8px 8px 8px" id="nodeTypeDiv">
					<label><b>通知方式 ${bpmNodeSet.nodeName} : </b></label>
					<c:forEach items="${handlersMap}" var="item">
						<input type="checkbox" name="informType" value="${item.key }" <c:if test="${fn:contains(bpmNodeSet.informType,item.key)}">checked="checked"</c:if> />
          				${item.value.title }
					</c:forEach>
					<input type="hidden" name="actDefId" value="${bpmNodeSet.actDefId}" />
					<input type="hidden" name="nodeId" value="${bpmNodeSet.nodeId}" />
					<input type="hidden" name="parentActDefId" value="${parentActDefId}" />
					<input type="hidden" id="informTypes" name="informTypes" value="" />
					<input type="hidden" id="informConf" name="informConf" value="" />

				</div>
			</form>
			<div class="reminder-div-tab" id="reminder-div-tab">
				<div class="reminder-div-msg-mail" title="邮件内容">
					<div class="panel-detail">
						<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
							<tr>
								<th width="60">邮件主题:</th>
								<td>
									<textarea id="mailSubject" name="mailSubject" rows="1">${bpmNodeSet.mailSubject}</textarea>
								</td>
							</tr>
							<tr>
								<th width="60">抄送人:</th>
								<td>
									<input name="copyTo" type="text" id="copyTo" ctlType="selector" class="users" value="${bpmNodeSet.copyTo}" lablename="抄送人" initvalue="${bpmNodeSet.copyToID}" />
									<div>
										<span class="green">表单变量:</span> <select name="flowVar" onchange="selectFormVarCopyTo(this,'formVarCopyTo')" style="width: 100px">
											<option value="">请选择...</option>
											<c:forEach items="${flowVars}" var="flowVar">
												<option class="flowvar-item" value="${flowVar.fieldName}" fname="${flowVar.fieldName}" fdesc="${flowVar.fieldDesc}" ftype="${flowVar.fieldType}">${flowVar.fieldDesc}</option>
											</c:forEach>
										</select>
									</div>
									<textarea id="formVarCopyTo" name="formVarCopyTo" rows="2">${bpmNodeSet.formVarCopyTo}</textarea>
								</td>
							</tr>
							<tr>
								<th width="60">邮件内容:</th>
								<td>
									<div>
										<a href="javascript:;" class="link var" title="选择模板内容" onclick="slectTemplate('mailText',false)">选择模板内容</a> <span class="green">表单变量:</span> <select name="flowVar"
											onchange="selectMailFlowVar(this,'mailText')" style="width: 100px">
											<option value="">请选择...</option>
											<c:forEach items="${flowVars}" var="flowVar">
												<option class="flowvar-item" value="${flowVar.fieldName}" fname="${flowVar.fieldName}" fdesc="${flowVar.fieldDesc}" ftype="${flowVar.fieldType}">${flowVar.fieldDesc}</option>
											</c:forEach>
										</select>
									</div>
									<textarea id="mailText" name="mailText" class="ckeditor-editor" rows="10">${bpmNodeSet.mailText}</textarea>
								</td>
							</tr>
						</table>
					</div>
				</div>
				<div class="reminder-div-msg-inter" title="站内消息内容"></div>
				<div class="reminder-div-msg-inter" title="RTX消息"></div>
			</div>
		</div>
	</div>
</body>
</html>