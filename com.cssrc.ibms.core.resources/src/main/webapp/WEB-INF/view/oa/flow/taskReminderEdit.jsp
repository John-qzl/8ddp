<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>任务催办设置</title>
<base target="_self" />
<%@include file="/commons/include/form.jsp"%>
<f:link href="jquery.qtip.css"></f:link>
<f:link href="form.css"></f:link>
<f:link href="codemirror/lib/codemirror.css"></f:link>
<script type="text/javascript" src="${ctx}/jslib/ibms/displaytag.js"></script>
<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=taskReminder"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/TemplateDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/ScriptDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ckeditor/ckeditor_remind.js"></script>
<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.qtip.js"></script>
<script type="text/javascript" src="${ctx}/jslib/codemirror/lib/codemirror.js"></script>
<script type="text/javascript" src="${ctx}/jslib/codemirror/lib/util/matchbrackets.js"></script>
<script type="text/javascript" src="${ctx}/jslib/codemirror/mode/groovy/groovy.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/taskReminderEdit.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/SelectorInit.js"></script>


<style>
.sub {
	display: none;
}

.condExp-control {
	width: 98%;
	padding: 2px;
	margin: 2px;
	border: solid 1px #A8CFEB;
}

.condExp-control [name='flowOperate'] {
	width: 65px;
}

.condExp-editor {
	width: 540px;
	padding: 2px;
	margin: 2px;
	height: 100px;
	border: solid 1px #A8CFEB;
	overflow: auto;
}

.condExp-editor-input {
	width: 100%;
	height: 100px;
}

.script-control {
	width: 98%;
	padding: 2px;
	margin: 2px;
	border: solid 1px #A8CFEB;
}

.script-editor {
	width: 98%;
	padding: 2px;
	margin: 2px;
	height: 100px;
	border: solid 1px #A8CFEB;
	overflow: auto;
}

.script-editor-input {
	width: 100%;
	height: 100px;
}

.send-msg-tr {
	display: none;
}

.choose-assigner {
	display: none;
}

.day-input {
	width: 40px;
	padding: 4px 6px;
	box-sizing: border-box;
	border: 1px solid #CCC;
}

.flowvalue {
	width: 60px;
	padding: 4px 6px;
	box-sizing: border-box;
	border: 1px solid #CCC;
}

.reminder-layout, .reminder-edit {
	height: auto !important; /*解决流程下不现实的问题*/
}
</style>
<script type="text/javascript">
	//CodeMirror Editor
	var condExpScriptEditor = null;
	var actionScriptEditor = null;
	var curTime = '${taskReminder.times}';
	var actDefId = "${actDefId}";
	var nodeId = "${nodeId}";
	var quarzCron = "${taskReminder.quarzCron}";
	var hoursAry = [ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
			17, 18, 19, 20, 21, 22, 23 ];
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">编辑任务节点催办时间设置</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">

						<a class="link add" id="btnAdd"> 增加 </a>
					</div>

					<div class="group">

						<a class="link save" id="dataFormSave" href="####"> 保存 </a>
					</div>

					<div class="group">
						<a class="link del" onclick="javascript:dialog.close();"> 关闭 </a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<div class="reminder-layout" id="reminder-layout">
				<div class="reminder-edit" position="center">
					<div>
						<form id="taskReminderForm" method="post" action="save.do">
							<div class="reminder-div">
								<div class="reminder-div-tab" id="reminder-div-tab">
									<div class="reminder-div-base" title="催办基本信息设置">
										<div class="panel-detail">
											<fieldset class="fieldset-detail">
												<legend>
													<span>到期条件设置</span>
												</legend>
												<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
													<tr>
														<th width="120px">名称:</th>
														<td>
															<input id="name" name="name" style="width: 100px" value="${taskReminder.name}" class="inputText" />
														</td>
														<!-- 												<th >默认:</th> -->
														<!-- 												<td > -->
														<%-- 													<input id="isDefaultTrue" name="isDefault" type="radio" value="1"  <c:if test="${taskReminder.isDefault==1 }">checked="checked"</c:if> /> --%>
														<!-- 													<label for="isDefaultTrue">是</label>				 -->
														<%-- 													<input id="isDefaultFalse" name="isDefault" type="radio" value="0" <c:if test="${taskReminder.isDefault!=1 }">checked="checked"</c:if> /> --%>
														<!-- 													<label for="isDefaultFalse">否</label> -->
														<!-- 												</td> -->
														<th width="100px">当前节点:</th>
														<td>
															<input type="text" style="width: 100px" value="${nodeId}" disabled="disabled" />
														</td>
													</tr>
													<tr>
														<th>相对节点:</th>
														<td>
															<select name="relativeNodeId" style="width: 100px">
																<c:forEach items="${nodes}" var="node">
																	<option value="${node.nodeId}"<c:if test="${node.nodeId==taskReminder.relativeNodeId}">selected="selected"</c:if>>${node.nodeName}</option>
																</c:forEach>
															</select>
														</td>
														<th>相对动作:</th>
														<td>
															<select name="relativeNodeType" style="width: 100px">
																<option value="0">创建</option>
																<option value="1" <c:if test="${taskReminder.relativeNodeType==1}">selected="selected"</c:if>>完成</option>
															</select>
														</td>
													</tr>
													<tr>
														<th>相对时间:</th>
														<td id="completeTr">
															<input class="day-input" type="text" style="width: 50px" name="completeTimeDay" value="${completeTimeDay}" />
															<span>天</span>
															<select id="completeTimeHour" style="width: 80px" class="hourInput" name="completeTimeHour">
																<c:forEach var="i" begin="0" end="23" step="1">
																	<option value="${i}" <c:if test="${completeTimeHour==i}">selected="selected"</c:if>>${i}小时</option>
																</c:forEach>
															</select>
															<select id="completeTimeMinute" style="width: 80px" class="minuteInput" name="completeTimeMinute">
																<c:forEach var="i" begin="0" end="4" step="1">
																	<option value="${i}" <c:if test="${completeTimeMinute==i}">selected="selected"</c:if>>${i}分钟</option>
																</c:forEach>
																<c:forEach var="i" begin="5" end="59" step="5">
																	<option value="${i}" <c:if test="${completeTimeMinute==i}">selected="selected"</c:if>>${i}分钟</option>
																</c:forEach>
															</select>
														</td>
														<th>相对时间类型:</th>
														<td>
															<select name="relativeTimeType" style="width: 100px">
																<option value="0">工作日</option>
																<option value="1" <c:if test="${taskReminder.relativeTimeType==1}">selected="selected"</c:if>>日历日</option>
															</select>
														</td>
													</tr>
													<tr>
														<th>
															<a href="javascript:;" class="link tipinfo">
																<span style="z-index: 100; text-align: left;">条件表达要求是返回Boolean值的脚本。返回true,表示满足条件；返回talse,表示条件不满足。如果表达式为空，将视为返回true。</span>
															</a>
															条件表达式:
														</th>
														<td colspan="3">
															<div class="condExp-control">
																<a href="####" class="link var" title="常用脚本" onclick="condExpSelectScript(this)">常用脚本</a>
																<%-- 													<f:flowVar defId="${defId}"></f:flowVar> --%>
																<span class="green">表单变量</span>
																<select name="flowVar" onchange="selectFlowVar(this,1)" style="width: 100px">
																	<option value="">请选择...</option>
																	<optgroup label="表单变量"></optgroup>
																	<c:forEach items="${flowVars}" var="flowVar">
																		<option class="flowvar-item" value="${flowVar.fieldName}" fname="${flowVar.fieldName}" fdesc="${flowVar.fieldDesc}" ftype="${flowVar.fieldType}">${flowVar.fieldDesc}</option>
																	</c:forEach>
																	<c:if test="${not empty defVars}">
																		<optgroup label="自定义变量"></optgroup>
																		<c:forEach items="${defVars}" var="defVars">
																			<option class="flowvar-item" value="${defVars.varKey}" fname="${defVars.varKey}" fdesc="${defVars.varName}" ftype="${defVars.varDataType}">${defVars.varName}</option>
																		</c:forEach>
																	</c:if>
															</select> <span class="green">比较</span> <select name="flowOperate">
															</select> <span class="green">值</span>
															<input name="flowValue" class="flowvalue" />
															<a onclick="generateExpress(this)" href="####" class="button"> <span>生成</span>
															</a>

															</div>
															<div class="condExp-editor">
																<textarea id="condExp" name="condExp" class="condExp-editor-input">${taskReminder.condExp}</textarea>
															</div>
														</td>
													</tr>
												</table>
											</fieldset>
											<fieldset class="fieldset-detail">
												<legend>
													<span>到期动作设置</span>
												</legend>
												<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
													<tr>
														<th width="100">执行动作:</th>
														<td colspan="3">
															<select id="action" onchange="change()" name="action">
																<option value="0"<c:if test="${taskReminder.action==0}">selected="selected"</c:if>>无动作</option>
																<option value="1" <c:if test="${taskReminder.action==1}">selected="selected"</c:if>>执行同意操作</option>
																<option value="2" <c:if test="${taskReminder.action==2}">selected="selected"</c:if>>执行反对操作</option>
																<option value="3" <c:if test="${taskReminder.action==3}">selected="selected"</c:if>>执行驳回操作</option>
																<option value="4" <c:if test="${taskReminder.action==4}">selected="selected"</c:if>>执行驳回到发起人操作</option>
																<option value="5" <c:if test="${taskReminder.action==5}">selected="selected"</c:if>>执行交办操作</option>
																<option value="6" <c:if test="${taskReminder.action==6}">selected="selected"</c:if>>结束该流程</option>
																<option value="7" <c:if test="${taskReminder.action==7}">selected="selected"</c:if>>调用指定方法</option>
															</select>
														</td>
													</tr>
													<tr class="sub" width="100">
														<th>执行脚本:</th>
														<td colspan="3">
															<div class="condExp-control">
																<a href="####" class="link var" title="常用脚本" onclick="scriptSelectScript(this)">常用脚本</a>
																<span class="green">表单变量:</span>
																<%-- 														<f:flowVar defId="${defId}" controlName="selFlowVar"></f:flowVar> --%>
																<select name="flowVar" onchange="selectFlowVar(this,2)">
																	<option value="">请选择...</option>
																<c:forEach items="${flowVars}" var="flowVar">
																	<option class="flowvar-item" value="${flowVar.fieldName}" fname="${flowVar.fieldName}" fdesc="${flowVar.fieldDesc}" ftype="${flowVar.fieldType}">${flowVar.fieldDesc}</option>
																</c:forEach>
															</select>
															</div>
															<div class="script-editor">
																<textarea rows="6" cols="60" id="script" name="script" class="script-editor-input">${taskReminder.script}</textarea>
															</div>
														</td>
													</tr>
													<tr class="choose-assigner" width="100">
														<th>指定交办人员:</th>
														<td colspan="3">
															<input type="hidden" name="assignerId" value="${taskReminder.assignerId}" />
															<input type="text" name="assignerName" readonly="readonly" value="${taskReminder.assignerName}" />
															<a href="javascript:;" onclick="chooseAssigner()" class="button"> <span>选择</span>
															</a>
														</td>
													</tr>
												</table>
											</fieldset>
											<fieldset class="fieldset-detail">
												<legend>
													<span>发送催办消息设置</span>
												</legend>
												<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
													<tr>
														<th width="100">发送催办信息:</th>
														<td colspan="3">
															<label> <input type="checkbox" id="needSendMsg" /> 发送
															</label>
														</td>
													</tr>
													<tr class="send-msg-tr">
														<th>开始发送时间:</th>
														<td id="startTr" colspan="3">
															<input class="day-input" type="text" name="reminderStartDay" value="${reminderStartDay}" />
															<span>天</span> <select style="width: 80px" id="reminderStartHour" class="hourInput" name="reminderStartHour">
																<c:forEach var="i" begin="0" end="23" step="1">
																	<option value="${i}"
																		<c:if test="${reminderStartHour==i}">selected="selected"</c:if>>${i}小时</option>
																</c:forEach>
															</select>
															<select style="width: 80px" id="reminderStartMinute" class="minuteInput" name="reminderStartMinute">
																<c:forEach var="i" begin="0" end="4" step="1">
																	<option value="${i}" <c:if test="${reminderStartMinute==i}">selected="selected"</c:if>>${i}分钟</option>
																</c:forEach>
																<c:forEach var="i" begin="5" end="59" step="5">
																	<option value="${i}" <c:if test="${reminderStartMinute==i}">selected="selected"</c:if>>${i}分钟</option>
																</c:forEach>
															</select>
														</td>
													</tr>
													<tr class="send-msg-tr">
														<th>
															<a href="javascript:;" class="tipinfo">
																<span>每过多长的时间发送催办信息。 </span>
															</a>
															发送的间隔:
														</th>
														<td id="endTr">
															<input class="day-input" type="text" name="reminderEndDay" value="${reminderEndDay}" />
															<span>天</span>
															<select style="width: 80px" id="reminderEndHour" class="hourInput" name="reminderEndHour">
																<c:forEach var="i" begin="0" end="23" step="1">
																	<option value="${i}" <c:if test="${reminderEndHour==i}">selected="selected"</c:if>>${i}小时</option>
																</c:forEach>
															</select>
															<select style="width: 80px" id="reminderEndMinute" class="minuteInput" name="reminderEndMinute">
																<c:forEach var="i" begin="1" end="4" step="1">
																	<option value="${i}" <c:if test="${reminderEndMinute==i}">selected="selected"</c:if>>${i}分钟</option>
																</c:forEach>
																<c:forEach var="i" begin="5" end="59" step="5">
																	<option value="${i}" <c:if test="${reminderEndMinute==i}">selected="selected"</c:if>>${i}分钟</option>
																</c:forEach>
															</select>
														</td>
														<th>发送信息次数:</th>
														<td>
															<select name="times" style="width: 50px">
																<c:forEach var="i" begin="0" end="10" step="1">
																	<option value="${i}" <c:if test="${taskReminder.times==i}">selected="selected"</c:if>>${i}</option>
																</c:forEach>
															</select>
														</td>
													</tr>
													<tr class="send-msg-tr">
														<th>Quarz Cron:</th>
														<td id="startTr" colspan="3">
															<input class="inputText" type="text" name="quarzCron" value="${taskReminder.quarzCron}" />
															<a href="javascript:;" class="link tipinfo">
																<span style="z-index: 100; text-align: left;">quartz cron 表达式。(0 0 9 * * ？ *)每天早上9点发送催办消息</span>
															</a>
														</td>
													</tr>
												</table>
											</fieldset>
											<!-- 界面显示模板 -->
											<fieldset class="fieldset-detail">
												<legend>
													<span>任务紧急程度预警设置</span>
												</legend>
												<table class="table-grid" id="taskWarningSetList" cellpadding="0" cellspacing="0" border="0">
													<thead>
														<tr>
															<td colspan="5">
																<a class="link" id="addWarning">增加预警项</a>
																<span style="color: gray;">到期动作为“无动作”时方才有效</span>
															</td>
														</tr>
														<tr align="center">
															<th>名称</th>
															<th>相对到期类型</th>
															<th>相对时间</th>
															<th>紧急程度</th>
															<th>管理</th>
														</tr>
													</thead>
													<c:forEach items="${taskReminder.taskWarningSetList}" var="warningSet">
														<tr>
															<td>
																<input class="input" type="text" style="width: 60px" name="warnName" value="${warningSet.name}" />
															</td>
															<td>
																<select name="relativeType" style="width: 80px">
																	<option value="before" <c:if test="${warningSet.relativeType eq 'before'}">selected="selected"</c:if>>之前</option>
																	<option value="after" <c:if test="${warningSet.relativeType eq 'after'}">selected="selected"</c:if>>之后</option>
																</select>
															</td>

															<td id="relativeTime">
																<input class="day-input" style="width: 40px" type="text" name="reminderDueDay" value="${warningSet.reminderDueDay}" />
																<span>天</span>
																<select class="hourInput" name="reminderDueHour" style="width: 80px">
																	<c:forEach var="i" begin="0" end="23" step="1">
																		<option value="${i}" <c:if test="${warningSet.reminderDueDay==i}">selected="selected"</c:if>>${i}小时</option>
																	</c:forEach>
																</select>
																<select id="reminderDueMinute" class="minuteInput" name="reminderDueMinute" style="width: 80px">
																	<c:forEach var="i" begin="0" end="4" step="1">
																		<option value="${i}" <c:if test="${warningSet.reminderDueMinute==i}">selected="selected"</c:if>>${i}分钟</option>
																		<c:forEach var="i" begin="5" end="59" step="5">
																			<option value="${i}" <c:if test="${warningSet.reminderDueMinute==i}">selected="selected"</c:if>>${i}分钟</option>
																		</c:forEach>
																	</c:forEach>
																</select>
															</td>

															<td>
																<select name="level" style="width:100px">
																	<c:forEach items="${taskReminderConfs}" var="warningSetting">
																		<option value="${warningSetting.level}" <c:if test="${warningSet.level eq warningSetting.level}">selected="selected"</c:if>>${warningSetting.name}</option>
																	</c:forEach>
																</select>
															</td>
															<td>
																<a class="link del" onclick="delWaringLine(this)">删除</a>
															</td>
														</tr>
													</c:forEach>
												</table>
											</fieldset>
										</div>
									</div>
									<div class="reminder-div-msg-mail" title="邮件内容">
										<div class="panel-detail">
											<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
												<tr>
													<th width="60">邮件主题:</th>
													<td>
														<textarea id="mailSubject" name="mailSubject" rows="3">${taskReminder.mailSubject}</textarea>
													</td>
												</tr>
												<tr>
													<th width="60">抄送人:</th>
													<td>
														<input name="copyTo" type="text" ctlType="selector" class="users" value="${taskReminder.copyTo}" lablename="抄送人" initvalue="${taskReminder.copyToID}"/>
														<div>
														<span class="green">表单变量:</span>
														<select name="flowVar" onchange="selectFormVarCopyTo(this,'formVarCopyTo')" style="width: 100px">
																<option value="">请选择...</option>
																<c:forEach items="${flowVars}" var="flowVar">
																	<option class="flowvar-item" value="${flowVar.fieldName}" fname="${flowVar.fieldName}" fdesc="${flowVar.fieldDesc}"
																		ftype="${flowVar.fieldType}">${flowVar.fieldDesc}</option>
																</c:forEach>
														</select>
														</div>
														<textarea id="formVarCopyTo" name="formVarCopyTo" rows="2">${taskReminder.formVarCopyTo}</textarea>
													</td>
												</tr>
												<tr>
													<th width="60">邮件内容:</th>
													<td>
														<div>
															<a href="javascript:;" class="link var" title="选择模板内容" onclick="slectTemplate('mailText',false)">选择模板内容</a>
															<span class="green">表单变量:</span>
															<select name="flowVar" onchange="selectMailFlowVar(this,'mailText')" style="width: 100px">
																<option value="">请选择...</option>
																<c:forEach items="${flowVars}" var="flowVar">
																	<option class="flowvar-item" value="${flowVar.fieldName}" fname="${flowVar.fieldName}" fdesc="${flowVar.fieldDesc}"
																		ftype="${flowVar.fieldType}">${flowVar.fieldDesc}</option>
																</c:forEach>
															</select>
														</div>
														<textarea id="mailText" name="mailText" class="ckeditor-editor" rows="10" >${taskReminder.mailText}</textarea>
													</td>
												</tr>
											</table>
										</div>
									</div>
									<div class="reminder-div-msg-inter" title="站内消息内容">
										<div class="panel-detail">
											<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
												<tr>
													<th width="60">站内消息内容:</th>
													<td>
														<div>
															<a href="####" class="link var" title="选择模板内容" onclick="slectTemplate('msgContent',false)">选择模板内容</a>
														</div>
														<textarea id="msgContent" name="msgContent" class="ckeditor-editor" rows="12" cols="50">${taskReminder.msgContent}</textarea>
													</td>
												</tr>
											</table>
										</div>
									</div>
									<!-- 
							    <div class="reminder-div-msg-sms" title="手机短信内容">
							    	<div class="panel-detail">
									    <table class="table-detail" cellpadding="0" cellspacing="0" border="0">
									    	<tr >
										     	<th width="60" >手机短信内容: </th>
												<td>
												<div>
													<a href="####"  class="link var" title="选择模板内容" onclick="slectTemplate('smsContent',true)">选择模板内容</a>
												</div>
												<textarea id="smsContent" name="smsContent" rows="12" cols="50">${taskReminder.smsContent}</textarea>
												</td>
											</tr>
										</table>
									</div>
						     	</div>
						     	 -->
								</div>
							</div>
							<div>
								<input type="hidden" name="taskDueId" value="${taskReminder.taskDueId}" />
								<input type="hidden" name="actDefId" value="${actDefId}" />
								<input type="hidden" name="nodeId" value="${nodeId}" />
								<input type="hidden" id="defId" name="defId" value="${defId}" />
								<input type="hidden" name="warningSetJson" id="warningSetJson">
								<textarea id="warningTemplate" style="display: none;">
									<tr>
										<td>
											<input class="input" type="text" name="warnName" value="" style="width: 60px" />
										</td>
										<td>
											<select name="relativeType" style="width: 80px">
												<option value="before">之前</option>
												<option value="after">之后</option>
											</select>
										</td>
										<td id="relativeTime">
											<input class="day-input" style="width: 40px" type="text" name="reminderDueDay" value="" />
											<span>天</span>
											<select id="reminderDueHour" style="width: 80px" class="hourInput" name="reminderDueHour">
												<option value="0">0小时</option>
												<option value="1">1小时</option>
												<option value="2">2小时</option>
												<option value="3">3小时</option>
												<option value="4">4小时</option>
												<option value="5">5小时</option>
												<option value="6">6小时</option>
												<option value="7">7小时</option>
												<option value="8">8小时</option>
												<option value="9">9小时</option>
												<option value="10">10小时</option>
												<option value="11">11小时</option>
												<option value="12">12小时</option>
												<option value="13">13小时</option>
												<option value="14">14小时</option>
												<option value="15">15小时</option>
												<option value="16">16小时</option>
												<option value="17">17小时</option>
												<option value="18">18小时</option>
												<option value="19">19小时</option>
												<option value="20">20小时</option>
												<option value="21">21小时</option>
												<option value="22">22小时</option>
												<option value="23">23小时</option>
											</select>
											<select id="reminderDueMinute" style="width: 80px" class="minuteInput" name="reminderDueMinute">
												<option value="0">0分钟</option>
												<option value="1">1分钟</option>
												<option value="2">2分钟</option>
												<option value="3">3分钟</option>
												<option value="4">4分钟</option>
												<option value="5">5分钟</option>
												<option value="10">10分钟</option>
												<option value="15">15分钟</option>
												<option value="20">20分钟</option>
												<option value="25">25分钟</option>
												<option value="30">30分钟</option>
												<option value="35">35分钟</option>
												<option value="40">40分钟</option>
												<option value="45">45分钟</option>
												<option value="50">50分钟</option>
												<option value="55">55分钟</option>
											</select>
										</td>
									
										<td>
											<select name="level" style="width: 100px">
												<c:forEach var="item" items="${taskReminderConfs}">
													<option value="${item.level}">${item.name}</option>
												</c:forEach>
											</select>
										</td>
										<td>
											<a class="link del" onclick="delWaringLine(this)">删除</a>
										</td>
									</tr>
								</textarea>

							</div>
						</form>
					</div>
				</div>
				<div class="reminders-list" position="right">
					<div class="reminders-div">
						<table class="table-grid" id="reminders-list-table">
							<thead>
								<tr>
									<th>名称</th>
									<th>管理</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${taskReminders }" var="reminder" varStatus="status">
									<tr <c:if test="${status.index%2==0 }">class="odd"</c:if> <c:if test="${status.index%2==1 }">class="even"</c:if>>
										<td>
											<span>${reminder.name }</span>
											<input class="pk" type="hidden" value="${reminder.taskDueId}" />
										</td>
										<td>
											<a class="link del" href="del.do?taskDueId=${reminder.taskDueId}">删除</a>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<a href="" id="goLocation" style="display: none;"></a>
	</div>
</body>
</html>