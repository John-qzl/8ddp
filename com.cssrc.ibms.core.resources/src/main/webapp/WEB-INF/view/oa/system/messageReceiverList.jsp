
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>消息接收者管理</title>
	<%@include file="/commons/include/get.jsp" %>
	<script type="text/javascript">
	function mark(t){
		var ids = "";
		if(t){
			var tr = $(t).parents("tr"),
				pk = $("input.pk",tr).val();

			if(!pk)return;
			ids = pk;
		}
		else{
			var idArr = [];

			$("input.pk").each(function(){
				var me = $(this),
					state = me.attr("checked");

				if(state&&me.closest("tr").find('td').eq(5).text().indexOf('未读消息')>0)
					idArr.push(me.val());
			});
			if(idArr.length==0){
				$.ligerDialog.warn('请选择至少一条未读记录!','提示');
				return;
			}
			ids = idArr.join(',');
		}
		var url = __ctx + '/oa/system/messageReceiver/mark.do';
		var params={ids:ids};
		$.post(url,params,function(d){
			if(t){
				return;
			}
			var json = eval("("+d+")");
			if(json.result){
				$.ligerDialog.success(json.message,'提示',function(){
					
					//刷新头上的未读消息数量
					refreshMsg();
					//刷新当前tab页
					location.href=location.href.getNewUrl();	
				});
			}
			else{
				$.ligerDialog.error(json.message,'提示');
			}
		});
	};
	
	//刷新顶部的未读消息数量
	function refreshMsg(){
		//刷新头上的未读消息数量
		var path = __ctx + '/oa/system/messageReceiver/getMsgCount.do';
		$.ajax({
			type:"get",
			    async:false,
			    url:path,
			    dataType:"json",
			    success:function(res){
			    	$("#countMsg",window.parent.document).text("("+res.message+")");
			    },
			error:function(err){
				console.log(err.message);
			}
		});
	};
	
	function delMsg(t){
		var ids = "";
		var str="确认删除吗？";
		
		if(t){
			ids = t;
		} else {
			var idArr = [];

			$("input.pk").each(function(){
				var me = $(this),
					state = me.attr("checked");

				if(state&&me.closest("tr").find('td').eq(5).text().indexOf('未读消息')>0)
					idArr.push(me.val());
			});
			if(idArr.length==0){
				$.ligerDialog.warn('请至少选择一条记录!','提示');
				return;
			}
			ids = idArr.join(',');
		}
		
		$.ligerDialog.confirm(str,'提示信息',function(rtn) {
			if(rtn) {
				var url = __ctx + '/oa/system/messageReceiver/delMsg.do';
				
				$.ajax({
					type:"get",
				    async:false,
				    url:url,
				    data:{
				    	ids:ids
				    },
				    dataType:"json",
				    success:function(res){
				    	if(res.result){
							$.ligerDialog.success(res.message,'提示',function(){					
								//刷新头上的未读消息数量
								refreshMsg();
								//刷新当前tab页
								location.href=location.href.getNewUrl();	
							});
						}
				    },
					error:function(err){
						console.log(err.message);
					}
				});
			}
		});
	};
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">消息接收管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
											
					<div class="group"><a class="link delMsg" onclick="delMsg()">删除</a></div>
					
					<div class="group"><a class="link done" onclick="mark()">标记为已读</a></div>
					<div class="group"><a class="link reset" onclick="$.clearQueryForm()">重置</a></div>
				</div>	
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.do">
					<ul class="row plat-row">
				        <li><span class="label">标题:</span><input type="text" name="Q_subject_SL"  class="inputText" value="${param['Q_subject_SL']}"/></li>																						
						<li>
							<span class="label">消息类型:</span>												
							<select name="Q_messageType_S" class="select" value="${param['Q_messageType_S']}">
							    <option value="">全部</option>
								<option value="1" <c:if test="${param['Q_messageType_S'] == 1}">selected</c:if>>个人信息</option>
								<option value="2" <c:if test="${param['Q_messageType_S'] == 2}">selected</c:if>>日程安排</option>
								<option value="3" <c:if test="${param['Q_messageType_S'] == 3}">selected</c:if>>计划任务</option>
								<option value="4" <c:if test="${param['Q_messageType_S'] == 4}">selected</c:if>>系统信息</option>
								<option value="5" <c:if test="${param['Q_messageType_S'] == 5}">selected</c:if>>代办提醒 </option>
								<option value="6" <c:if test="${param['Q_messageType_S'] == 6}">selected</c:if>>流程提醒 </option>
						    </select>
					    </li>
					    
					    <li>
						    <span class="label">是否已读:</span>												
							<select name="Q_receiveTime_S" class="select" value="${param['Q_receiveTime_S']}">
							    <option value="">全部</option>
								<option value="1" <c:if test="${param['Q_receiveTime_S'] == 1}">selected</c:if>>未读</option>
								<option value="2" <c:if test="${param['Q_receiveTime_S'] == 2}">selected</c:if>>已读</option>
						    </select>
					    </li>
					    
					    <div class="row_date">
						<li><span class="label">发送时间:</span><input  name="Q_beginreceiveTime_DL"  class="inputText datetime" value="${param['Q_beginreceiveTime_DL']}"/></li>
						<li><span class="label">至: </span><input  name="Q_endreceiveTime_DG" class="inputText datetime" value="${param['Q_endreceiveTime_DG']}"/></li>
					    </div>							
					</ul>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="messageReceiverList" id="messageReceiverItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1"   class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
					  	<input type="checkbox" class="pk" name="id" value="${messageReceiverItem.rid}">
				</display:column>
				<display:column property="userName" title="发信人" sortable="true" sortName="userName"></display:column>
				<display:column property="subject" title="标题" sortable="true" sortName="subject"></display:column>
				<display:column title="消息类型" sortable="true" sortName="messageType" style="text-align:center;">
				<c:choose>
					<c:when test="${messageReceiverItem.messageType==1}">
					       个人信息
				   	</c:when>
				   	<c:when test="${messageReceiverItem.messageType==2}">
					        日程安排
				   	</c:when>
				   	<c:when test="${messageReceiverItem.messageType==3}">
					       计划任务
				   	</c:when>
				   	<c:when test="${messageReceiverItem.messageType==4}">
				                   系统信息
				   	</c:when>
				   	<c:when test="${messageReceiverItem.messageType==5}">
				                   代办提醒
				   	</c:when>
				   	<c:when test="${messageReceiverItem.messageType==6}">
				                  流程提醒
				   	</c:when>
			       	<c:otherwise>
				                   其他                 
				   	</c:otherwise>
			    </c:choose>
				</display:column>	
				<display:column title="发送时间" sortable="true" sortName="sendTime" style="text-align:center;">
					<fmt:formatDate value="${messageReceiverItem.sendTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</display:column>							
				<display:column  title="收信时间" sortable="true" sortName="receiveTime" style="text-align:center;">
					<c:choose>
						<c:when test="${messageReceiverItem.receiveTime!=null}">
							<fmt:formatDate value="${messageReceiverItem.receiveTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</c:when>
						<c:otherwise>
							<span style="color:red;">未读消息</span>
						</c:otherwise>
					</c:choose>
				</display:column>
				<display:column title="管理" media="html" style="width:180px;text-align:center">
					<c:if test="${messageReceiverItem.rid!=null}">
						<a href="####" onclick="delMsg(${messageReceiverItem.rid});" class="link delMsg">删除</a>
					</c:if>
					<c:if test="${messageReceiverItem.canReply==1}">
					<a href="${ctx}/oa/system/messageReply/edit.do?messageId=${messageReceiverItem.id}" class="link edit">回复</a>
					</c:if>
					<a href="${ctx}/oa/system/messageRead/list.do?messageId=${messageReceiverItem.id}" class="link detail">明细</a>
				</display:column>
			</display:table>
			<ibms:paging tableId="messageReceiverItem"/>
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
</body>
</html>


