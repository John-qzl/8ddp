<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>代办已办模板配置</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/PendingSetting.js"></script>
<f:link href="flow/pendingSetting.css" ></f:link>
<script type="text/javascript">
	var options = {};
	$(function() {
		__PendingSetting__.init();
		$("select.rightselect").change(setPermision);
		if (showResponse) {
			options.success = showResponse;
		}
		$("a.save").click(saveData);
	})
	var saveData = function() {
		var form = $('#pendingSettingForm');
		if (!form.valid()) {
			return;
		}
		//显示字段
		var displayField = JSON2
				.stringify(__PendingSetting__.getDisplayField());
		//条件字段
		var conditionField = JSON2.stringify(__PendingSetting__
				.getConditionField());
		$('#displayField').val(displayField);
		$('#conditionField').val(conditionField);
		form.ajaxForm(options);
		form.submit();
	};
	function setPermision() {
		var me = $(this), val = me.val(), right = me.attr('right');
		if (val == "")
			return;
		$("span[name='r_span'],span[name='w_span'],span[name='b_span']", obj)
				.hide();
		$('td[var="' + right + '"]').each(function() {
			var self = $(this), rightSelect = $("[var='right']", self);
			rightSelect.next().hide();
			rightSelect.next().next().hide();
			if (val == 0) {
				rightSelect.val('none');
			} else if (val == 1) {
				rightSelect.val('everyone');
			}
		});
		me.val("");
	}

	function showResponse(responseText) {
		$.ligerDialog.closeWaitting();
		var obj = new com.ibms.form.ResultMessage(responseText);
		if (obj.isSuccess()) {
			var tipCallBack = function(rtn) {
				if (rtn) {
					window.location.href = location.href.getNewUrl();
				} else {
					window.close();
				}
			};
			$.ligerDialog.confirm(obj.getMessage() + ",是否继续操作", "提示信息",
					tipCallBack);
		} else {
			$.ligerDialog.err(obj.getMessage(), "提示信息");
		}
	}
</script>

</head>
<body>
	<div class="panel">
		<div class="panel-container">
			<div class="panel-title">
				<jsp:include page="incDefinitionHead.jsp">
					<jsp:param value="节点表单设置" name="title" />
				</jsp:include>
			</div>
  			<f:tab curTab="pendingSetting" tabName="flow%"/>
			<div class="panel-top">
				<div style="width: 100%;" class="tbar-title">
					<span class="tbar-label">代办已办模板配置</span>
				</div>
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group">
							<a class="link save" id="dataFormSave" href="javascript:;">
								
								保存
							</a>
						</div>
					</div>
				</div>
			</div>

			<div class="panel-body" style="height: 700px;">
				<form action="save.do" method="post" id="pendingSettingForm">
					<textarea style="display: none;" id="displayField" name="displayField">${fn:escapeXml(displayField)}</textarea>
					<textarea style="display: none;" id="conditionField" name="conditionField">${fn:escapeXml(conditionField)}</textarea>
					<input type="hidden" id="defId" name="defId" value="${defId}" />
					<input type="hidden" id="tableId" name="tableId" value="${tableId}" />
					
					
					

					<div tabid="displaySetting" id="table" title="代办/已办模板">
						<td>
							<select name="templateAlias" id="templateAlias" validate="{required:true}">
								<option value="">--请选择代办/已办模板--</option>
								<c:forEach items="${templates}" var="template">
									<option value="${template.alias}" <c:if test="${templateAlias==template.alias}">selected="selected"</c:if>>${template.templateName}</option>
								</c:forEach>
							</select>
							<div class="tipbox">
								<a href="####" class="tipinfo">
									<span>添加更多数据模板，请到自定义表单模板中添加类型为"业务数据模板"的模板</span>
								</a>
							</div>
					</div>
					<div tabid="displaySetting" id="table" title="显示列字段">
						<div>
							<table id="displayFieldTbl" class="table-grid">
								<thead>
									<tr>
										<th width="5%">序号</th>
										<th width="15%">列名</th>
										<th width="20%">注释</th>
										<th width="20%">
											<select class="rightselect" right="fieldRight">
												<option selected="selected" value="">显示权限</option>
												<option value="0">无</option>
												<option value="1">所有人</option>
											</select>
										</th>
										<th width="10%">管理</th>
									</tr>
								</thead>
							</table>
						</div>
					</div>
					<br />
					<!-- 查询条件 -->
					<div tabid="conditionSetting" id="table" title="查询条件字段">
						<div class="condition-cols">
							<div>
								<div class="condition-cols-div">
									<c:set var="checkAll">
										<input type="checkbox" id="chkall" />
									</c:set>
									<table id="condition-columnsTbl" cellpadding="0" cellspacing="0" border="0" class="table-detail">
										<thead>
											<tr class="leftHeader">
												<th style="width: 30px;">${checkAll}</th>
												<th>列名</th>
												<th>注释</th>
												<th>类型</th>
											</tr>
										</thead>
										<tbody>
											<c:if test="${!empty tableFields}">
												<c:forEach items="${tableFields}" var="field" varStatus="status">
													<tr <c:if test="${status.index%2==0}">class="odd"</c:if> <c:if test="${status.index%2!=0}">class="even"</c:if>>
														<td>
															<input class="pk" type="checkbox" settype="${field.settype}" name="select" fieldname="${field.fieldName}" fieldfmt='${field.fieldfmt}'
																fieldtype="${field.fieldType}" fielddesc="${field.fieldDesc}" controltype="${field.controlType}">
														</td>
														<td>${field.fieldName}</td>
														<td>${field.fieldDesc}</td>
														<td>${field.fieldType}</td>
													</tr>
												</c:forEach>
											</c:if>
										</tbody>
									</table>
								</div>
							</div>
						</div>
						<div class="condition-conds">
							<div class="condition-conds-div condition-conds-build" id="condition-build-div">
								<div class="condition-conds-div-left">
									<div class="condition-conds-div-left-div">
										<a style="margin-top: 180px;" id="selectCondition" href="####" class="button">
											<span>==></span>
										</a>
									</div>
								</div>
								<div class="condition-conds-div-right">
									<div class="condition-conds-div-right-div">
										<table id="conditionTbl" cellpadding="0" cellspacing="0" border="0" class="table-detail">
											<thead>
												<tr class="leftHeader">
													<th nowrap="nowrap">列名</th>
													<th nowrap="nowrap">显示名</th>
													<th nowrap="nowrap">控件类型</th>
													<th nowrap="nowrap">条件</th>
													<th nowrap="nowrap">值来源</th>
													<th nowrap="nowrap">值</th>
													<th nowrap="nowrap">管理</th>
												</tr>
											</thead>
											<tbody>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>


	<div class="hidden">
		<!-- 显示字段模板 -->
		<div id="displayFieldTemplate">
			<table cellpadding="1" cellspacing="1" class="table-detail">
				<tr var="displayFieldTr">
					<input type="hidden" var="type" value="" />
					<input type="hidden" var="style" value="" />
					<input type="hidden" var="controltype" value="" />
					<input type="hidden" var="settype" value="" />
					<td var="index">&nbsp;</td>
					<td var="name">&nbsp;</td>
					<td>
						<input type="text" var="desc" value="" />
					</td>
					<td var="fieldRight"></td>
					<td>
						<a class="link moveup" href="javascript:;" title="上移" onclick="__PendingSetting__.moveTr(this,true)"></a>
						<a class="link movedown" href="javascript:;" title="下移" onclick="__PendingSetting__.moveTr(this,false)"></a>
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>


