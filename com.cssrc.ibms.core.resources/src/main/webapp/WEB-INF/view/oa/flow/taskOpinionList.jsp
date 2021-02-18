<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<%@include file="/commons/include/get.jsp" %>
	<title>${f:removeHTMLTag(processRun.subject)}--流程审批历史</title>
	<f:link href="jquery.qtip.css" ></f:link>
	<script type="text/javascript" src="${ctx}/jslib/util/easyTemplate.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.qtip.js" ></script>
	<script type="text/javascript" src="${ctx}/jslib/util/form.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/ProcessUrgeDialog.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/FlowUtil.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/ShowExeInfo.js" ></script>
	<script type="text/javascript">
		$(function(){
			$("a[opinionId]").each(showResult);	
		});
	
		
		function showResult(){
			var opinionId=$(this).attr("opinionId"),
				checkStatus= $(this).attr("checkStatus");
			var template=$("#txtReceiveTemplate").val();
			$(this).qtip({  
				content:{
					text:'加载中...',
					ajax:{
						url:__ctx +"/oa/flow/commuReceiver/getByOpinionId.do",
						type:"GET",
						data:{opinionId: opinionId },
						success:function(data,status){
							var html=easyTemplate(template,data).toString();
							this.set("content.text",html);
						}
					},
					title:{
						text: checkStatus==36?'重启审批人' :(checkStatus==15?'沟通接收人' : '流转接收人')			
					}
				},
			        position: {
			        	at:'top left',
			        	target:'event',
			        	
   					viewport:  $(window)
			        },
			        show:{
			        	event:"click",
	   			     	solo:true
			        },   			     	
			        hide: {
			        	event:'unfocus',
			        	fixed:true
			        },  
			        style: {
			       	  classes:'ui-tooltip-light ui-tooltip-shadow'
			        } 			    
		 	});	
		}
		
	</script>
	
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="l-layout-header">流程实例-【<i>${processRun.subject}</i>】审批历史明细。</div>
		</div>
		<c:if test="${param.tab eq 1 }">
			<c:choose>
					<c:when test="${processRun.status==2  || processRun.status==3}">
						<f:tab curTab="taskOpinion" tabName="process" />
					</c:when>
					<c:otherwise>
						<f:tab curTab="taskOpinion" tabName="process" hideTabs="copyUser"/>
					</c:otherwise>
			</c:choose>
		</c:if>
		<div class="panel-body" style="height:100%;overflow:auto;">
	
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
  <textarea id="txtReceiveTemplate"  style="display: none;">
    <div  style="height:150px;width:150px;overflow:auto">
	  	
	  		<#list data as obj>
	  		<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
	  		<tr>
	  			<th>接收人</th>
	  			<td>\${obj.receivername }</td>
	  		</tr>
	  		<tr>
	  			<th>状态</th>
	  			<td>
	  				<#if (obj.status==0) >
	  					<span class="red">未读</span>
	  				<#elseif (obj.status==1)>
	  					<span class="green">已读</span>
	  				<#elseif (obj.status==2)>
	  					<span class="green">已反馈</span>
	  				<#elseif (obj.status==3)>
	  					<span class="red">已取消</span>
	  				</#if>
	  			</td>
	  		</tr>
	  		<#if (obj.status==0) >
		  		<tr>
		  			<th>创建时间</th>
		  			<td>\${obj.createtimeStr}</td>
		  		</tr>
		  	<#elseif (obj.status==1)>
			  	<tr>
		  			<th>接收时间</th>
		  			<td>\${obj.receivetimeStr}</td>
			  	</tr>
		  	<#elseif (obj.status==2)>
		  		<tr>
		  			<th>反馈时间</th>
		  			<td>\${obj.feedbacktimeStr}</td>
		  		</tr>
		  	<#elseif (obj.status==3)>
		  		<tr>
		  			<th>取消时间</th>
		  			<td>\${obj.feedbacktimeStr}</td>
		  		</tr>
	  		</#if>
	  		</table>
	  		</#list>
	  	
  	</div>
  </textarea>
</body>
</html>
