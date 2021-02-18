<%--
	desc:edit the 消息回复
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>编辑 消息回复</title>
	<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=messageReply"></script>
	<script type="text/javascript">
		$(function() {
			refreshMsg();
			
			function showRequest(formData, jqForm, options) { 
				return true;
			} 
			function showResponse(responseText, statusText)  { 
				var obj=new com.ibms.form.ResultMessage(responseText);
				if(obj.isSuccess()){//成功
					$.ligerDialog.confirm('操作成功,继续修改吗?','提示信息',function(rtn){
						if(!rtn){
							location.href="${returnUrl}";
						}
						else{
							$('#messageReplyForm').resetForm();
						}
					});
					
			    }else{//失败
			    	$.ligerDialog.err('出错信息',"编辑消息回复失败",obj.getMessage());
			    }
			} 
			valid(showRequest,showResponse);
			$("a.save").click(function() {
				$('#messageReplyForm').submit(); 
			});
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
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">编辑消息回复</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" id="dataFormSave" href="javascript:;">保存</a></div>
					
					<div class="group"><a class="link back" href="${returnUrl}">返回</a></div>
				</div>
			</div>
		</div>
		<div class="panel-body">
				<form id="messageReplyForm" method="post" action="save.do">
					<div class="panel-detail">
						<table class="table-detail" cellpadding="0" cellspacing="0" border="0">						
							<tr>
								<th width="20%">标题: </th>
								<td>${subject}</td>
							</tr>
							<tr>
								<th width="20%">内容: </th>
								<td>${content}</td>
							</tr>
							<tr>
								<th width="20%">回复内容: </th>
								<td>
								<textarea id="content" name="content" rows="5" cols="38">${messageReply.content}</textarea>
								</td>
							</tr>
							<tr>
								<th width="20%">私密回复: </th>
								<td>
								<input type="radio" name="isPrivate" value="1" checked/>是
								<input type="radio" name="isPrivate" value="0" />否
								</td>
							</tr>
						</table>
					</div>
					<input type="hidden" name="id" value="${messageReply.id}" />
					<input type="hidden" id="messageId" name="messageId" value="${messageReply.messageId}"  class="inputText"/>
					<input type="hidden" id="replyId" name="replyId" value="${messageReply.replyId}"  class="inputText"/>
					<input type="hidden" id="reply" name="reply" value="${messageReply.reply}"  class="inputText"/>
				</form>
		</div>
</div>
</body>
</html>
