<%@page pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>${processRun.subject}-流程图</title>
<%@include file="/commons/include/form.jsp"%>
<f:link href="jquery.qtip.css"></f:link>
<f:link href="jquery/plugins/powerFloat.css"></f:link>
<f:link href="from-jsp.css"></f:link>
<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery-powerFloat.js"></script>
<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.qtip.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/ViewSubFlowWindow.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/ViewSuperExecutionFlowWindow.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/easyTemplate.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/form.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/ProcessUrgeDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/FlowUtil.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/ShowExeInfo.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/UserInfo.js"></script>
<script type="text/javascript">
var processInstanceId = "${processInstanceId}";
var isStatusLoaded = false;
var _height = "${shapeMeta.height}";
function setIframeHeight() {
	//有可能会存在多个iframe的情况
	var mainIFrame = window.parent.document.getElementsByName("flowchart");
	if (!mainIFrame)
		return;
	$(mainIFrame).each(function() {
		//使用jQuery直接添加height属性
		$(this).css("height", _height);
	});
};

	$(function() {
		$.each($("div.flowNode"), function() {
			var obj = $(this);
			var nodeId = $(this).attr('id');
			if (obj.attr('type') == 'userTask'
					|| obj.attr('type') == 'multiUserTask') {
				obj.css('cursor', 'pointer');
				//var url="${ctx}/oa/flow/processRun/taskUser.do?processInstanceId="+processInstanceId+"&nodeId=" + nodeId;
				//obj.powerFloat({ eventType: "click", target:url, targetMode: "ajax"});
				//只有用户任务和会签任务显示节点。
				checkStatusInfo(nodeId);
			}
			if (obj.attr('type') == 'callActivity') {
				obj.css('cursor', 'pointer');
				obj.click(function() {
					var nodeId = obj.attr('id');
					var conf = {
						nodeId : nodeId,
						processInstanceId : processInstanceId
					};
					viewSubFlow(conf);
				});
			}
		});
		 setTimeout(function(){
			var panelBodyHeight = $("div.flowNode").parents('.panel-table').height() + 160;
			var panelBody = $("div.flowNode").parents('.panel-body');
			panelBody.height("auto");
		},0) 
		
	});

	function showResult() {
		var targetUrl = $(this).attr("candidateUserUrl");
		var template = $("#txtReceiveTemplate").val();
		var conf = {
			content : {
				text : '加载中...',
				ajax : {
					url : targetUrl,
					type : "GET",
					success : function(data, status) {
						var html = easyTemplate(template, data).toString();
						this.set("content.text", html);
					}
				},
				title : {
					text : "执行人列表"
				}
			},
			position : {
				at : 'top left',
				target : 'event',
				viewport : $(window)
			},
			show : {
				event : "focus mouseenter"
			},
			hide : {
				event : 'unfocus mouseleave',
				fixed : true
			},
			style : {
				classes : 'ui-tooltip-light ui-tooltip-shadow'
			}
		};
		$(this).qtip(conf);
	}

	function viewSubFlow(conf) {
		if (!conf)
			conf = {};
		var url = "${ctx}/oa/flow/processRun/subFlowImage.do?processInstanceId="
				+ conf.processInstanceId
				+ "&nodeId="
				+ conf.nodeId
				+ "&processDefinitionId=" + conf.processDefinitionId;
		var winArgs = "dialogWidth=800px;dialogHeight=600px;help=0;status=0;scroll=1;center=1;";
		var winArgs = "height=600,width=800,status=no,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes";
		url = url.getNewUrl();
		var win = window.open(url, "subFlow", winArgs);
		win.focus();
	}

	//初始化qtip
	function checkStatusInfo(nodeId) {
		var conf = {
			content : {
				text : function() {
					var html = getTableHtml(nodeId);
					if (html) {
						return html;
					} else {
						return "<span style='color:red;line-height:24px;'>未执行</span>";
					}

				},
				title : {
					text : "任务执行情况"
				}
			},
			position : {
				at : 'center',
				target : 'event',
				adjust : {
					x : -15,
					y : -15
				},
				viewport : ($.isIE6() ? "" : $(window))
			},
			show : {
				effect : function(offset) {
					$(this).slideDown(200);
					$("a[candidateUserUrl]").each(showResult);
				}
			},
			hide : {
				event : 'mouseleave',
				fixed : true,
				delay : 300
			},
			style : {
				classes : 'ui-tooltip-light ui-tooltip-shadow',
				width : ($.isIE6() ? 279 : "")
			}
		};
		$("#" + nodeId).qtip(conf);
	};

	var taskNodeStatus = {};

	//加载流程状态数据。
	function loadStatus(nodeId) {
		var status = taskNodeStatus[nodeId];
		if (!status && status != "") {
			var url = "${ctx}/oa/flow/processRun/getFlowStatusByInstanceIdAndNodeId.do";
			var params = {
				instanceId : processInstanceId,
				nodeId : nodeId
			};
			$.ajax({
				url : url,
				async : false,
				data : params
			}).done(function(result) {
				status = result;
			});
			taskNodeStatus[nodeId] = status;
		}
		return status;
	};

	//构建显示的html
	function getTableHtml(nodeId) {
		var node = loadStatus(nodeId);
		if (!node)
			return false;
		var taskOpinionList = node.taskOpinionList;
		var taskExecutorList = node.taskExecutorList;
		var lastCheckStatus = node.lastCheckStatus;
		var html = [ '<div style="max-height:340px;width:260px;overflow:auto;">' ];
		if (lastCheckStatus != "-2") { //正在执行的节点
			if (taskOpinionList.length == 0) {
				return false;
			} else {
				var tableHtml = $("#txtTaskStatus").val();
				var str = easyTemplate(tableHtml, node);
				html.push(str);
				html.push('</div>');
			}
		} else if (lastCheckStatus == "-2") { //未执行的节点
			if (taskExecutorList.length == 0) {
				return false;
			} else {
				var tableHtml = $("#txtTaskStatusExecutors").val();
				var str = easyTemplate(tableHtml, node);
				html.push(str);
				html.push('</div>');
			}
		}

		return html.join('');
	}
</script>
</head>
<body>

	<div class="panel">
		<div class="l-layout-header">
			流程实例-【<i>${processRun.subject}</i>】明细。
		</div>
		<c:if test="${param.tab eq 1 }">
			<c:choose>
				<c:when test="${processRun.status==2  || processRun.status==3}">
					<f:tab curTab="processImage" tabName="process" />
				</c:when>
				<c:otherwise>
					<f:tab curTab="processImage" tabName="process" hideTabs="copyUser" />
				</c:otherwise>
			</c:choose>
		</c:if>

		<div class="panel-top">
			<div class="l-layout-header">
				流程实例-【<i>${processRun.subject}</i>】流程图。
			</div>
			<c:forEach items="${barFlowStatus}" var="bar">
				<div class="target">
					<div class="icon" style="background: ${bar.color};"></div>
					<span>${bar.text}</span>
				</div>
			</c:forEach>
		</div>
		<div class="panel-body" style="background:#fff">
			<div class="panel-table">

				<div style="padding-top: 20px; background-color: white; clear: both; overflow: auto;">
					<div style="margin-bottom: 5px;">
						<b>说明：</b>点击任务节点可以查看节点的执行人员。
						<c:if test="${superInstanceId != null}">
							<a class="link setting" onclick="ViewSuperExecutionFlowWindow({'actInstanceId':'${superInstanceId}'})">查看主流程</a>
						</c:if>
					</div>
					<div id="divTaskContainer"
						style="margin:0 auto;  position: relative;background:url('${ctx}/bpmImage?processInstanceId=${processInstanceId}&randId=<%=Math.random()%>') no-repeat;width:${shapeMeta.width}px;height:${shapeMeta.height}px;">
						${shapeMeta.xml}</div>
				</div>
				<textarea id="txtTaskStatus" style="display: none">
				<#list data.taskOpinionList as obj>
					<table class="table-task" cellpadding="0" cellspacing="0" border="0">
					<tr><th>任务名称: </th>
					<td>\${obj.taskName}</td></tr>
					<#if (obj.checkStatus == -1)> <!-- 正在审批 -->
						<tr>
							<th>执行人: </th>
							<#if (obj.taskExeStatus==null)>
								<td></td>
							<#else>
								<td> <a href="javascript:userDetail('\${obj.taskExeStatus.executorId}');">\${obj.taskExeStatus.executor}</a>--\${obj.taskExeStatus.mainOrgName}\${obj.taskExeStatus.isRead==false?"(未读)":"(已读)"}</td>
							</#if>
						</tr>
						<tr>
							<th>候选人: </th>
							<#if (obj.candidateUserStatusList==null)>
								<td></td>
							<#else>
								<td>
									<#list obj.candidateUserStatusList as candidateUserStatus>
										<#if (candidateUserStatus.type=="user")>
										<a href="javascript:userDetail('\${candidateUserStatus.executorId}');">\${candidateUserStatus.candidateUser}</a>
											<span>--\${candidateUserStatus.mainOrgName}\${candidateUserStatus.isRead==false?"(<font color='red'>未读</font>)":"(<font color='green'>已读</font>)"} </span><br/>
										<#else>
											<#if (candidateUserStatus.type=="org")>
												<span><a href="javascript:;" candidateUserUrl="${ctx}/oa/system/userPosition/getUserListByOrgId.do?orgId=\${candidateUserStatus.executorId}">\${candidateUserStatus.candidateUser}</a>(组织)</span><br/>
											<#elseif (candidateUserStatus.type=="pos")>
												<span><a href="javascript:;" candidateUserUrl="${ctx}/oa/system/userPosition/getUserListByPosId.do?posId=\${candidateUserStatus.executorId}">\${candidateUserStatus.candidateUser}</a>(岗位)</span><br/>
											<#elseif (candidateUserStatus.type=="role")>
												<span><a href="javascript:;" candidateUserUrl="${ctx}/oa/system/userRole/getUserListByRoleId.do?roleId=\${candidateUserStatus.executorId}">\${candidateUserStatus.candidateUser}</a>(角色)</span><br/>
											<#elseif (candidateUserStatus.type=="group")>
												<span>\${candidateUserStatus.candidateUser}(用户分组)</span><br/>
											</#if>
										</#if>
									</#list>
								</td>
							</#if>
						</tr>
					<#else>
						<tr>
							<th>执行人: </th>
							<td><a href="javascript:userDetail('\${obj.exeUserId}');">\${obj.exeFullname}</a></td>
						</tr>
					</#if>
					<tr><th  nowrap="nowrap">开始时间: </th>
					<td>\${obj.startTimeStr}</td></tr>
					
					<tr><th>结束时间: </th>
					<td>\${obj.endTimeStr}</td></tr>
					
					<tr><th >时长: </th>
					<td>\${obj.durTimeStr}</td></tr>
					
					<tr><th>状态: </th>
					<td>\${obj.status}</td></tr>
					
					<tr><th>意见: </th>
					<td>\${obj.opinion==null?"":obj.opinion}</td></tr>
					</table><br>
				</#list>
			</textarea>
				<textarea id="txtTaskStatusExecutors" style="display: none">
				<div>执行状态 ： <span style='color:red;line-height: 24px;'>未执行</span> </div>
					<table class="table-task" cellpadding="0" cellspacing="0" border="0">
						<tr >
						<th width="50">执行人: </th>
							<td>
							<#list data.taskExecutorList as obj>
							<a href="javascript:userDetail('\${obj.executeId}');">\${obj.executor}</a>
								-- \${obj.mainOrgName}</br>
							</#list>
							</td>
						</tr>
					</table>
			</textarea>
				<textarea id="txtReceiveTemplate" style="display: none;">
			    <div  style="overflow:auto">
			  		<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
				  		<#list data as obj>
					  		<tr>
					  			<th>\${obj_index+1}</th>
					  			<#if (obj.fullname==null)>
					  				<td>\${obj.userName}</td>
				  				<#else>
				  					<td>\${obj.fullname}</td>
			  					</#if>
					  		</tr>
				  		</#list>
			  		</table>
			  	</div>
		   </textarea>
			</div>
		<div >
  		 <display:table name="taskOpinionList" id="taskOpinionItem" requestURI="list.do" sort="external" cellpadding="0" cellspacing="0" class="table-grid">
			<display:column title="序号" style="width:30px;">
			  ${taskOpinionItem_rowNum}
			</display:column>
			<display:column title="流程标题" property="flowSubject"></display:column>
			<display:column title="任务名称" property="taskName"></display:column>
			<display:column title="开始时间">
				<fmt:formatDate value="${taskOpinionItem.startTime}" pattern="yyyy-MM-dd HH:mm"/>
			</display:column>
			<display:column title="结束时间">
				<fmt:formatDate value="${taskOpinionItem.endTime}" pattern="yyyy-MM-dd HH:mm"/>
			</display:column>
			<display:column title="处理时长">
			  ${f:getTime(taskOpinionItem.durTime)}
			</display:column>
			<display:column  title="执行人">
			
				<c:choose>
					<c:when test="${taskOpinionItem.exeUserId ne null and taskOpinionItem.exeUserId ne null}">
						<a href="${ctx}/oa/system/sysUser/get.do?userId=${taskOpinionItem.exeUserId}&canReturn=2&hasClose=1" target="_blank">${taskOpinionItem.exeFullname}</a>
					</c:when>
					<c:otherwise>
						<c:forEach items="${taskOpinionItem.candidateUsers }" var="user">
							<a href="${ctx}/oa/system/sysUser/get.do?userId=${user.userId}&canReturn=2&hasClose=1" target="_blank">${user.fullname}</a>
							<br/>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</display:column>
			<display:column  titleKey="审批意见"  maxLength="30">
				 ${f:parseText(taskOpinionItem.opinion)}
				<c:if test="${taskOpinionItem.checkStatus==15}">
					<a  href="####" onclick="return false;" opinionId="${taskOpinionItem.opinionId }" checkStatus="15" >接收人</a>
				</c:if>
				<c:if test="${taskOpinionItem.checkStatus==38 || taskOpinionItem.checkStatus==43}">
					<a  href="####" onclick="return false;" opinionId="${taskOpinionItem.opinionId }" checkStatus="40" >接收人</a>
				</c:if>
				
			</display:column>
			<display:column title="印章">
				<c:if test="${taskOpinionItem.signImage!=null}">
					<img src="data:image/gif;base64,${taskOpinionItem.signImage}">
				</c:if>
			</display:column>
			<display:column title="审批状态">
				<f:taskStatus status="${taskOpinionItem.checkStatus}" flag="0"></f:taskStatus>
			</display:column>
		</display:table>
		</div>
		</div>

	</div>
</body>
</html>