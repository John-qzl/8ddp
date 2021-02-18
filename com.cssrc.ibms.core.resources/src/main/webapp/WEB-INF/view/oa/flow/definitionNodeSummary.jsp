<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
    <title>节点概要</title>
       <%@include file="/commons/include/form.jsp" %>  
         <script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/FlowEventWindow.js" ></script>
 		<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/FlowRuleWindow.js" ></script>
        <script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/FlowReminderWindow.js" ></script>
   		<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/BpmNodeButtonWindow.js" ></script>
   		<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/BpmNodeUserSetWindow.js" ></script>
   		 <script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/FlowApprovalItemWindow.js" ></script>
<style>
.green-set{
 	font-weight: bold;color: green;font-size: 20px;
 }
.red-set{
	font-weight: bold;color: red;font-size: 20px;
}
.normal-set{
	font-weight: bold;font-size: 20px;
}

.header {
	height: 26px;
    margin-top: 2px;
    padding:8px 5px 5px;
    background:#ebebeb;
    border-bottom: solid 1px #cacaca;
    border-top: solid 1px #cacaca;
}
</style>
</head>  
  
<body>
	<div class="panel">
	<c:if test="${empty nodeId}">
		<div class="panel-container">
		<div class="panel-title">
			<jsp:include page="incDefinitionHead.jsp">
				<jsp:param value="节点概要" name="title" />
				<jsp:param value="${returnUrl }" name="returnUrl"/>
			</jsp:include>
		</div>
  		<f:tab curTab="nodeSummary" tabName="flow%"/>
	</c:if>	
	<c:if test="${!empty nodeId}">
	      <div class="panel-container">
	</c:if>
			<div class="panel-body">
				<div style="margin: 0 5px;">
					<!--
                   	作者：rzq
                   	时间：2017-05-16
                   	描述：给div增加margin：0 5px。优化样式
                   -->
					图示：<span class="green-set">√</span>：表示已经设置 &nbsp;&nbsp;&nbsp;&nbsp;
					<span class="red-set" >×</span>：表示未设置&nbsp;&nbsp;&nbsp;&nbsp;
					<span class="normal-set" >-</span>：表示没有该功能&nbsp;&nbsp;&nbsp;&nbsp;
					<a href="####" class="link edit" title="设置" ></a>：表示可以对该功能进行设置
				</div>
				<form method="post" style="margin: 0 5px;">
                   <!--
                   	作者：rzq
                   	时间：2017-05-16
                   	描述：给form增加margin：0 5px。优化样式
                   -->
					<input type="hidden" name="defId" value="${defId}" />
					 <input type="hidden" name="nodeId" value="${nodeId}" />
					<table class="table-grid" style="width: 100%">
							<tr>
								<td width="15px" nowrap="nowrap"  rowspan="2" style="text-align: center;font-weight: bold;" class="header"  >序号</td>
								<td rowspan="2" style="text-align: center;font-weight: bold;" class="header ">节点名称</td>
								<td rowspan="2" colspan="2" style="text-align: center;font-weight: bold;" class="header">人员设置</td>
							<!-- 	<td rowspan="2" colspan="2" style="text-align: center;font-weight: bold;" class="header">常用语设置</td> -->
								<td colspan="5" style="text-align: center;font-weight: bold;" class="header">流程事件</td>
								<td rowspan="2" colspan="2" style="text-align: center;font-weight: bold;" class="header">流程规则</td>
								<td rowspan="2" colspan="2" style="text-align: center;font-weight: bold;" class="header">表单设置</td>
								<td rowspan="2" colspan="2" style="text-align: center;font-weight: bold;" class="header">操作按钮</td>
								<td rowspan="2" colspan="2" style="text-align: center;font-weight: bold;" class="header">催办设置</td>
							</tr>
							<tr>
								<td nowrap="nowrap" style="text-align: center;font-weight: bold;" class="header">前置</td>
								<td nowrap="nowrap" style="text-align: center;font-weight: bold;" class="header">后置</td>
								<td nowrap="nowrap" style="text-align: center;font-weight: bold;"class="header">分配</td>
							    <td nowrap="nowrap" style="text-align: center;font-weight: bold;"class="header">开始</br>结束</td>
								<td nowrap="nowrap" style="text-align: center;font-weight: bold;" class="header">&nbsp;</td>
							</tr>
							<tr>
								<td>-</td>
								<td>全局设置</td>
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<%-- <td>
									<c:choose>
										<c:when test="${globalApprovalMap['global']}">
											<span class="green-set">√</span>
										</c:when>
										<c:otherwise>
											<span class="red-set" >×</span>
										</c:otherwise>
									</c:choose> 
								</td>
								<td>
									<a href="####" class="link edit" title="设置" onclick="FlowApprovalItemWindow({actDefId:'${actDefId}',activitiId:'',defId:'${defId}',callback:function(){window.location.reload()}});"></a>
								</td> --%>
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<td>
									<c:choose>
										<c:when test="${formMap['global']}">
											<span class="green-set">√</span>
										</c:when>
										<c:otherwise>
											<span class="red-set">×</span>
										</c:otherwise>
									</c:choose> 
								</td>
								<td><a href="${ctx}/oa/flow/nodeSet/list?defId=${defId}" class="link edit" title="设置" ></a></td>
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
							</tr>
							<tr>
								<td>-</td>
								<td>开始节点</br>(${startFlowNode.nodeName})</td>
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<!-- <td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td> -->
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<td>
									<c:choose>
										<c:when test="${startScriptMap[startFlowNode.nodeId]}">
											<span class="green-set">√</span>
										</c:when>
										<c:otherwise>
											<span class="red-set">×</span>
										</c:otherwise>
									</c:choose> 
								</td>
								<td>
									<a href="####" class="link edit" title="设置" 
									onclick="FlowEventWindow({type:'startEvent',actDefId:'${actDefId}',activitiId:'${startFlowNode.nodeId}',defId:'${defId}',callback:function(){window.location.reload()}});"></a>
								</td>
						
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<td>
									<c:choose>
										<c:when test="${formMap['start']}">
											<span class="green-set">√</span>
										</c:when>
										<c:otherwise>
											<span class="red-set">×</span>
										</c:otherwise>
									</c:choose> </td>
								<td><span class="normal-set">-</span></td>
								<td>
									<c:choose>
										<c:when test="${buttonMap[startFlowNode.nodeId]}">
											<span class="green-set">√</span>
										</c:when>
										<c:otherwise>
											<span class="red-set">×</span>
										</c:otherwise>
									</c:choose> 
								</td>
								<td><a href="####" class="link edit" title="设置" onclick="BpmNodeButtonWindow({defId:'${defId}',nodeId:'',callback:function(){window.location.reload()}})"></a></td>
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
							</tr>
							<c:forEach items="${endFlowNodeList}" var="endFlowNode">
							<tr>
								<td>-</td>
								<td>结束节点</br>(${endFlowNode.nodeName})</td>
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<!-- <td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td> -->
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<td>
									<c:choose>
										<c:when test="${endScriptMap[endFlowNode.nodeId]}">
											<span class="green-set">√</span>
										</c:when>
										<c:otherwise>
											<span class="red-set">×</span>
										</c:otherwise>
									</c:choose> 
								</td>	
								<td>
									<a href="####" class="link edit" title="设置" onclick="FlowEventWindow({type:'endEvent',actDefId:'${actDefId}',activitiId:'${endFlowNode.nodeId}',defId:'${defId}',callback:function(){window.location.reload()}});"></a>
								</td>	
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
								<td><span class="normal-set">-</span></td>
							</tr>
							</c:forEach>
						<c:forEach items="${nodeSetList}" var="nodeSet" varStatus="i">
							<tr>
								<td>${i.count}</td>
								<td>${nodeSet.nodeName}</br>(${nodeSet.nodeId})</td>
								
								<!-- 人员设置 -->
								<td>
									<c:choose>
										<c:when test="${nodeUserMap[nodeSet.setId]}">
											<span class="green-set">√</span>
										</c:when>
										<c:otherwise>
											<span class="red-set">×</span>
										</c:otherwise>
									</c:choose> 
								</td>
								<td>
									<a href="####" class="link edit" title="设置" onclick="UserSetWindow({defId:'${defId}',nodeId:'${nodeSet.nodeId}',parentActDefId:'${parentActDefId}',callback:function(){window.location.reload()}});"></a>
								</td>
								<%-- <!-- 常用语 -->
								<td>
									<c:choose>
										<c:when test="${taskApprovalItemsMap[nodeSet.setId]}">
											<span class="green-set">√</span>
										</c:when>
										<c:otherwise>
											<span class="red-set">×</span>
										</c:otherwise>
									</c:choose> 
								</td>
								<td>
									<a href="####" class="link edit" title="设置" onclick="FlowApprovalItemWindow({actDefId:'${actDefId}',activitiId:'${nodeSet.nodeId}',defId:'${defId}',callback:function(){window.location.reload()}});"></a>
								</td> --%>
								
								<!-- 流程事件-前置脚本 -->
								<td>	
									<c:choose>
										<c:when test="${preScriptMap[nodeSet.setId]}">
											<span class="green-set">√</span>
										</c:when>
										<c:otherwise>
											<span class="red-set">×</span>
										</c:otherwise>
									</c:choose> 
								</td>
								<!-- 流程事件-后置脚本 -->
								<td>	
									<c:choose>
										<c:when test="${afterScriptMap[nodeSet.setId]}">
											<span class="green-set">√</span>
										</c:when>
										<c:otherwise>
											<span class="red-set">×</span>
										</c:otherwise>
									</c:choose> 
								</td>
								
								<!-- 流程事件-分配脚本 -->
								<td>	
									<c:choose>
										<c:when test="${assignScriptMap[nodeSet.setId]}">
											<span class="green-set">√</span>
										</c:when>
										<c:otherwise>
											<span class="red-set">×</span>
										</c:otherwise>
									</c:choose> 
								</td>
								<td>
									<span class="normal-set">-</span>
								</td>
								<td>
									<a href="####" class="link edit" title="设置" onclick="FlowEventWindow({type:'userTask',actDefId:'${actDefId}',activitiId:'${nodeSet.nodeId}',defId:'${defId}',parentActDefId:'${parentActDefId}'});"></a>
								</td>
								<!-- 流程规则 -->
								<td>	
									<c:choose>
										<c:when test="${nodeRulesMap[nodeSet.setId]}">
											<span class="green-set">√</span>
										</c:when>
										<c:otherwise>
											<span class="red-set">×</span>
										</c:otherwise>
									</c:choose> 
								</td>
								<td>
									<a href="####" class="link edit" title="设置" onclick="FlowRuleWindow({deployId:'${deployId}',actDefId:'${nodeSet.actDefId}',nodeId:'${nodeSet.nodeId}',nodeName:'${nodeSet.nodeName}',parentActDefId:'${parentActDefId}',callback:function(){window.location.reload()}});"></a>
								</td>
								<!-- 流程表单 -->
								<td>	
									<c:choose>
										<c:when test="${bpmFormMap[nodeSet.setId]}">
											<span class="green-set">√</span>
										</c:when>
										<c:otherwise>
											<span class="red-set">×</span>
										</c:otherwise>
									</c:choose> 
								</td>
								<td>
									<a href="${ctx}/oa/flow/nodeSet/list?defId=${defId}&parentActDefId=${parentActDefId}" class="link edit" title="设置" ></a>
								</td>
								
								<!-- 操作按钮-->
								<td>	
									<c:choose>
										<c:when test="${nodeButtonMap[nodeSet.setId]}">
											<span class="green-set">√</span>
										</c:when>
										<c:otherwise>
											<span class="red-set">×</span>
										</c:otherwise>
									</c:choose> 
								</td>
								<td>
									<a href="####" class="link edit" title="设置" onclick="BpmNodeButtonWindow({defId:'${defId}',nodeId:'${nodeSet.nodeId}',callback:function(){window.location.reload()}})"></a>
								</td>
								
								<!-- 催办设置-->
								<td>	
									<c:choose>
										<c:when test="${taskReminderMap[nodeSet.setId]}">
											<span class="green-set">√</span>
										</c:when>
										<c:otherwise>
											<span class="red-set">×</span>
										</c:otherwise>
									</c:choose> 
								</td>
								<td>
									<a href="####" class="link edit" title="设置" onclick="FlowReminderWindow({actDefId:'${actDefId}',nodeId:'${nodeSet.nodeId}',parentActDefId:'${parentActDefId}',callback:function(){window.location.reload()}})"></a>
								</td>
							</tr>
						</c:forEach>
					</table>
					<div style="height: 40px"></div>
				</form>
			</div>
		</div>
	</div>	
</body>
</html>