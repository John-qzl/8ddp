
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<%@include file="/commons/include/form.jsp"%>

<title>编辑 常用语</title>

<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=taskApprovalItems"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/taskApprovalItemsEdit.js"></script>

<script type="text/javascript">
	var isAdmin = ${isAdmin};
	$(function() {
		//点击返回
		$("a.back").click(function() {
			location.href = 'list.do?isAdmin=' + isAdmin;
		})

		//TaskReminder form Edit Layout
		tab = $("#reminder-div-tab").ligerTab({});

	})
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">设置常用语</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" id="dataFormSave" onclick="save()">
							
							保存
						</a>
					</div>
					
					<div class="group">
						<a class="link back" href="####">
							
							返回
						</a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<div class="reminder-layout" id="reminder-layout">
				<div class="reminder-edit" position="center">
					<div style="height: 570px; overflow: auto;">
						<form id="taskApprovalItems" method="post" action="save.do">
							<div class="reminder-div">
								<div class="reminder-div-tab" id="reminder-div-tab">
									<c:choose>
										<c:when test="${isAdmin==1}">
											<div class="reminder-div-all" title="全局设置">
												<input type="hidden" name="type" id="type" value="1" />
												<div class="panel-detail">
													<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
														<tr>
															<th width="20%">常用语:</th>
															<td>
																<textarea rows="5" cols="60" id="approvalItem" name="approvalItem" style="margin-top: 5px; margin-bottom: 5px;">${taskApprovalItems.expression}</textarea>
																<div class="tipbox">
																	<a class="tipinfo">
																		<span>一行就是一条常用语</span>
																	</a>
																</div>
															</td>
														</tr>
													</table>
												</div>
											</div>
											<div class="reminder-div-flow-type" title="根据流程分类设置">
												<input type="hidden" name="type" id="type" value="2" />
												<div class="panel-detail">
													<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
														<tr id="trFlowType">
															<th width="20%">流程分类:</th>
															<td>
																<div id="flowTypeDiv">
																	<%-- <select id="flowType" name="flowType">
																<option value="">-请选择-</option>
																<c:forEach items="${globalTypeList}" var="globalType">
																	<c:choose>
																		<c:when test="${globalType.typeId==taskApprovalItems.typeId }">
																			<option value="${globalType.typeId}" selected="selected">${globalType.typeName }</option>
																		</c:when>
																		<c:otherwise>
																			<option value="${globalType.typeId }">${globalType.typeName}</option>
																		</c:otherwise>
																	</c:choose>
																</c:forEach>
															</select> --%>
																	<input type="hidden" id="refFlowKey" name="refFlowKey" />
																	<span id="flowTypeArray"> </span>
																	<a class="link search" href="javascript:void(0);" onclick="flowTypeSelector()">选择流程分类</a>
																</div>
															</td>
														</tr>
														<tr>
															<th width="20%">常用语:</th>
															<td>
																<textarea rows="5" cols="60" id="approvalItem" name="approvalItem" style="margin-top: 5px; margin-bottom: 5px;">${taskApprovalItems.expression}</textarea>
																<div class="tipbox">
																	<a class="tipinfo">
																		<span>一行就是一条常用语</span>
																	</a>
																</div>
															</td>
														</tr>
													</table>
												</div>
											</div>
											<div class="reminder-div-flow" title="根据流程设置">
												<input type="hidden" name="type" id="type" value="3" />
												<div class="panel-detail">
													<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
														<tr>
															<th width="20%">流程定义:</th>
															<td>
																<input type="hidden" id="refDefKey" name="refDefKey" />
																<span id="refDefArray"> </span>
																<a class="link search" href="javascript:void(0);" onclick="referDefinition('${bpmDefinition.defId}')">选择流程</a>
															</td>
														</tr>
														<tr>
															<th width="20%">常用语:</th>
															<td>
																<textarea rows="5" cols="60" id="approvalItem" name="approvalItem" style="margin-top: 5px; margin-bottom: 5px;">${taskApprovalItems.expression}</textarea>
																<div class="tipbox">
																	<a class="tipinfo">
																		<span>一行就是一条常用语</span>
																	</a>
																</div>
															</td>
														</tr>
													</table>
												</div>
											</div>
											<div class="reminder-div-flow" title="根据流程节点设置">
												<input type="hidden" name="type" id="type" value="5" />
												<div class="panel-detail">
													<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
														<tr>
															<th width="20%">流程节点定义:</th>
															<td>
																<input type="hidden" id="defNodeKey" name="defNodeKey" />
																<span id="defNodeArray"> </span>
																<a class="link search" href="javascript:void(0);" onclick="referFlowTask('${bpmDefinition.defId}')">选择流程节点</a>
															</td>
														</tr>
														<tr>
															<th width="20%">code:</th>
															<td>
																<input type="text" id="approvalItem_code" name="code" />
															</td>
														</tr>
														<tr>
															<th width="20%">是否默认:</th>
															<td>
																<input type="checkbox" id="approvalItem_default_" name="default_"/>
															</td>
														</tr>
														<tr>
															<th width="20%">常用语:</th>
															<td>
																<textarea rows="5" cols="60" id="approvalItem" name="approvalItem" style="margin-top: 5px; margin-bottom: 5px;">${taskApprovalItems.expression}</textarea>
																<div class="tipbox">
																	<a class="tipinfo">
																		<span>一行就是一条常用语,格式同html文本</span>
																	</a>
																</div>
															</td>
														</tr>
													</table>
												</div>
											</div>
										</c:when>
										<c:when test="${isAdmin==0}">
											<div class="reminder-div-all" title="个人常用语设置">
												<input type="hidden" name="type" id="type" value="4" />
												<div class="panel-detail">
													<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
														<tr>
															<th width="20%">常用语:</th>
															<td>
																<textarea rows="5" cols="60" id="approvalItem" name="approvalItem" style="margin-top: 5px; margin-bottom: 5px;">${taskApprovalItems.expression}</textarea>
																<div class="tipbox">
																	<a class="tipinfo">
																		<span>一行就是一条常用语</span>
																	</a>
																</div>
															</td>
														</tr>
													</table>
												</div>
											</div>
										</c:when>
									</c:choose>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
</body>
</html>
