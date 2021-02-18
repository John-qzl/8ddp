
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>接收状态管理</title>
	<%@include file="/commons/include/get.jsp" %>
	<f:link href="form.css" ></f:link>
	<script type="text/javascript"
		src="${ctx}/jslib/ibms/oa/form/AttachMent.js"></script>
	<script type="text/javascript"
		src="${ctx}/jslib/ibms/oa/system/HtmlUploadDialog.js"></script>
	<script type="text/javascript"
		src="${ctx}/jslib/ibms/oa/system/FlexUploadDialog.js"></script>
	
	<script type="text/javascript">
		$().ready(function (){
			AttachMent.init("r");
			
			refreshMsg();
		});
		
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
		}
	</script>
	</head>
<body>
<div class="panel">
<div class="hide-panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">消息详细信息</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link back" href="${returnUrl}">返回</a></div>
				</div>
			</div>
		</div>
		</div>
		<div class="panel-body">
			<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<th width="20%">标题:</th>
					<td>${messageSend.subject}</td>
				</tr>
				<tr>
					<th width="20%">发信人:</th>
					<td>${messageSend.userName}</td>
				</tr>
				<tr>
					<th width="20%">发信时间:</th>
					<td><fmt:formatDate value="${messageSend.sendTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				</tr>
				<tr>
					<th width="20%">附件:</th>
					<td colspan="3">
						<div name="div_attachment_container">
							<div class="attachement"></div>
							<textarea style="display: none" controltype="attachment"
								name="attachment" lablename="附件" validate="{}">${messageSend.attachment}</textarea>
						</div>
					</td>
				</tr>
				<tr>
					<th width="20%">内容:</th>
					<td>${messageSend.content}</td>
				</tr>
			</table>
			
		</div>
		<br/>
	    <c:if test="${replyList!=null}">
	    <div class="panel-data">
			<table id="dicTable" class="table-grid table-list" id="0" cellpadding="1" cellspacing="1">
		   		<thead>
		    		<th style="width:13%">回复人</th>
		    		<th>回复内容</th>
		    		<th style="width:15%">回复时间</th>	
		    		<!--<th style="width:6%">操作</th>-->
		    	</thead>					    	
		    	<tbody>
			    	<c:forEach items="${replyList}" var="replyItem">
			    		<tr style="<c:if test="${replyItem.isPrivate==1&&replyItem.replyId!=userId}">display:none</c:if>;height:50px" class="${status.index%2==0?'odd':'even'}">					    								    								    								    								    			
			    			<td>${replyItem.reply }</td> 
			    			<td>${replyItem.content }</td>
			    			<td><fmt:formatDate value="${replyItem.replyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td> 
			    			<!--<td><a href="del.do?id=${replyItem.id}" class="link del" 
			    				style='${replyItem.replyId==userId?'display:':'display:none'}'>删除</a></td> -->				    								    			
			    		</tr>
			    	</c:forEach>
		    	</tbody>					    	
		     </table>
	     </div>	
		</c:if>							
				
</div>
</body>
</html>


