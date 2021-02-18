<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>任务管理列表</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerLayout.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerMenu.js" ></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerMenuBar.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/TaskDetailWindow.js"></script>
<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.qtip.js" ></script>
<f:link href="jquery.qtip.css" ></f:link>
<script type="text/javascript" src="${ctx}/jslib/util/easyTemplate.js"></script>
<script type="text/javascript">
	//为某个任务分配人员
	function assignTask(){
		var taskIds=new Array();
		$("input[name='id']:checked").each(function(){
			taskIds.push($(this).val());
		});
		if(taskIds.length==0){
			$.ligerDialog.warn("没有选择任务!",'提示信息');
			return;
		}
		UserDialog({
			isSingle:true,
			callback:function(userId,fullname){
				if(!userId) return;
				var url="${ctx}/oa/flow/task/assign.do";
				var params= {taskIds:taskIds.join(','),userId:userId};
				$.post(url,params,function(data){
					document.location.reload();
				});
				
			}
		});
	}
	
	function setTaskAssignee(link,taskId){
		var url="${ctx}/oa/flow/task/changeAssignee.do?taskId="+taskId;
		url=url.getNewUrl();		
		DialogUtil.open({
			height:340,
			width: 680,
			title : '更改任务执行人',
			url: url, 
			isResize: true,
			//自定义参数
			sucCall:function(rtn){
				if(rtn){
					document.location.reload();
				}
			}
		});
	}
	
	function setTaskOwner(link,taskId){
		var url="${ctx}/oa/flow/task/setOwner.do";
		var callback=function(userId,fullname){
			$(link).siblings("span").html('<img src="${ctx}/styles/images/user.png">' + fullname);	
		};
		setTaskExecutor(taskId,url,callback);
	}
	
	//设置任务的执行人
	function setTaskExecutor(taskId,url,callback){
		//显示用户选择器
		UserDialog({
			isSingle:true,
			callback:function(userId,fullname){
				if(userId=='' || userId==null || userId==undefined) return;
				var params= { taskId:taskId,userId:userId};
				$.post(url,params,function(responseText){
					var obj=new com.ibms.form.ResultMessage(responseText);
					if(obj.isSuccess()){
						$.ligerDialog.success(obj.getMessage(),'提示信息',function(){
							document.location.reload();
   						});
					}else{
						 $.ligerMsg.error('操作失败!'); 
					}
				});
			}
		});
	}
	//执行任务
	function executeTask(taskId){
		 var url="${ctx}/oa/flow/task/doNext.do?taskId="+taskId;
		 jQuery.openFullWindow(url);
	}
	
	function init(){
		var layout=$("#taskLayout").ligerLayout({rightWidth:300,height: '100%',isRightCollapse:true});
		$("tr.odd,tr.even").each(function(){
			$(this).bind("mousedown",function(event){
				if(event.target.tagName!="TD")  return;
				var strFilter='input[type="checkbox"][class="pk"]';
				var obj=$(this).find(strFilter);
				if(obj.length==1){
					var taskId=obj.val();
					layout.setRightCollapse(false);
					$("#taskDetailPanel").html("<div>正在加载...</div>");
					//在任务表单明细面版中加载任务详细
					$("#taskDetailPanel").load('${ctx}/oa/flow/task/miniDetail.do?manage=true&taskId='+taskId,null);
				}
			});    
		});
	}
	
	$(function(){
		init();
		endProcess();
		//handScroll();
	});
	
	
	
	function endProcess(){
		$.confirm("a.link.stop",'确认结束该流程吗？');
	}
	
	$(function(){
		$("a[taskId]").each(showResult);
		$("a[targetUrl]").each(showResultByUrl);
	});
	function showResult(){
		var template=$("#txtReceiveTemplate").val();
		var jsonValue = $(this).next(":hidden") ;
		var html=easyTemplate(template,$.parseJSON(jsonValue.val())).toString();
		$(this).qtip({
			content:{
				text:html,
				title:{
					text: '执行人列表'
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
	        	event:'mouseleave',
	        	fixed:true
	        },  
	        style: {
	       	  classes:'ui-tooltip-light ui-tooltip-shadow'
	        } 			    
	 	});	
			
	}
	function showResultByUrl(){
		var template=$("#txtReceiveTemplate").val();
		var url = $(this).attr("targetUrl") ;
		$(this).qtip({
			content:{
				text:'加载中...',
				ajax:{
					url:url,
					type:"GET",
					success:function(data,status){
						var html=easyTemplate(template,data).toString();
						this.set("content.text",html);
					}
				},title:{
					text: '执行人列表'
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
	        	event:'mouseleave',
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
	<div id="taskLayout" type="layout" >
          	<div position="center" style="overflow: auto;" handScroll="true">
          		<div class="panel">
           		<div class="panel-top">
					<div class="tbar-title">
						<span class="tbar-label">任务管理列表</span>
					</div>
					<div class="panel-toolbar">
						<div class="toolBar">
							<div class="group"><a class="link search" >查询</a></div>
<!-- 							 -->
							<!-- 删除功能只删除了任务，act_ru_execution表里还有 -->
<!-- 							<div class="group"><a class="link del" action="delete.do">删除任务</a></div>  -->
						</div>	
					</div>
					<div class="panel-search">
						<form id="searchForm" method="post" action="list.do">
							<ul class="row plat-row">
								<li><span class="label">流程定义名称:</span><input type="text" name="Q_processName_SL"  class="inputText" value="${param['Q_processName_SL']}" /></li>
								<li><span class="label">事项名称:</span><input type="text" name="Q_subject_SL"  class="inputText"  value="${param['Q_subject_SL']}"/></li>
								<li><span class="label">任务名称:</span><input type="text" name="Q_name_SL"  class="inputText"  value="${param['Q_name_SL']}"/></li>
								<li><span class="label">任务ID:</span><input type="text" name="Q_id_SL"  class="inputText"  value="${param['Q_id_SL']}"/></li>
							</ul>
						</form>
					</div>		
				</div>
				<div class="panel-body">
			    	<c:set var="checkAll">
						<input type="checkbox" id="chkall"/>
					</c:set>
				    <display:table name="taskList" id="taskItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1"   class="table-grid">
						<display:column title="${checkAll}" media="html" style="width:30px;">
							  	<input type="checkbox" class="pk" name="id" value="${taskItem.id}">
						</display:column>
						<display:column media="html" title="事项名称" sortable="true" sortName="subject"  >
							<a href="#${taskItem.id}" onclick="javascript:executeTask(${taskItem.id},'${taskItem.name}')" >${taskItem.subject}</a>
						</display:column>
						<display:column property="name" title="任务名称" sortable="true"  sortName="name_" style="text-align:left;"></display:column>
						
						<display:column title="执行人" sortable="true" sortName="assignee_">
							<f:userName userId="${taskItem.assignee}" taskId="${taskItem.id}"/>
						</display:column>
						<display:column title="状态" sortable="true" sortName="delegation_">
							<c:choose>
								<c:when test="${empty taskItem.delegationState}">待执行</c:when>
								<c:otherwise>${taskItem.delegationState}</c:otherwise>
							</c:choose>
						</display:column>
						<display:column title="任务创建时间" sortable="true" sortName="create_time_">
							<fmt:formatDate value="${taskItem.createTime}" pattern="yyyy-MM-dd HH:mm:ss" />
						</display:column>
					
					</display:table>
					<ibms:paging tableId="taskItem"/>
				</div><!-- end of panel-body -->	
			</div>
			<!--  end of panel -->			
		</div> 
		<div id="taskDetailPanel" position="right" title="任务明细"></div>
	</div>
	
	
	
	<textarea id="txtReceiveTemplate"  style="display: none;">
    <div style="height:150px;width:150px;overflow:auto">
    <table class="table-detail" cellpadding="0" cellspacing="0" border="0">
  		<#list data as obj>
  		<tr>
  			<th>\${obj_index+1}</th>
  			<td>
  			<#if ("org"==obj.type)>
				<a href="${ctx}/oa/system/userPosition/userList.do?orgId=\${obj.executeId}&hasClose=true" target="_blank">\${obj.executor}</a>
			<#elseif ("pos"==obj.type)>
				<a href="${ctx}/oa/system/userPosition/edit.do?posId=\${obj.executeId}&hasClose=true" target="_blank">\${obj.executor}</a> 
			<#elseif ("role"==obj.type)>
				<a href="${ctx}/oa/system/userRole/edit.do?roleId=\${obj.executeId}&hasClose=true" target="_blank">\${obj.executor}</a>
			<#elseif (obj.executeId!=null)>
				<a href="${ctx}/oa/system/sysUser/get.do?userId=\${obj.executeId}&hasClose=true" target="_blank">\${obj.executor}</a>
			<#elseif (obj.userId!=null)>
				<#if (obj.userName!=null)>
				<a href="${ctx}/oa/system/sysUser/get.do?userId=\${obj.userId}&hasClose=true" target="_blank">\${obj.userName}</a>
				<#elseif (obj.fullname!=null)>
				<a href="${ctx}/oa/system/sysUser/get.do?userId=\${obj.userId}&hasClose=true" target="_blank">\${obj.fullname}</a>
				</#if>
			</#if>
  			
  			</td>
  		</tr>
  		</#list>
	</table>
  	</div>
  </textarea>
</body>
</html>


