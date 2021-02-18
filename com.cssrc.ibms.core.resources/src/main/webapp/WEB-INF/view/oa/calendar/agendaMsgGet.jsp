<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/portalCustom.jsp"%>
<%@taglib prefix="ib" uri="http://www.ibms.cn/detailFun"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>日程交流</title>
<style type="text/css">
a:link,a:visited {
	text-decoration: none; 
}

li {
	border-bottom: 1px dotted #ccc;
	list-style-type: none;
}
</style>
</head>
<body>

	<div class="mini-panel" title="评论" iconCls="icon-edit" style="width: 100%; height: 220px;" showFooter="true">
		<div property="footer" style="text-align: center; padding-bottom: 5px;padding-top:3px">
			<a id="submitCm" class="mini-button" iconCls="icon-check" onclick="submitCm()"><b>提交</b></a> 
			<span style="font-size: 24px;">&nbsp;&nbsp;&nbsp;</span> 
			<a class="mini-button" onclick="clear()"><b>重置</b></a>
		</div>
		
		<div id="p1">
			<form id="form" name="form" method="post">
				<input name="agendaId" type="hidden" value="${agendaId}">
				<table style="width: 100%; padding: 0;" cellspacing="0" cellpadding="0">
					<tr>
						<td><input name="contents" class="mini-textarea" id="contents" required="true" emptyText="请输入评论内容" vtype="maxLength:128" style="height: 90px; width: 100%" /></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	
	<div id="agendaMsgList" name="agendaMsgList">
		<c:forEach items="${agendaMsgList}" var="agendaMsg">
			<div>
				<input id="id" type="hidden" value="${agendaMsg.id}">
				<span id="name"><b>${agendaMsg.replyer}</b>
				</span>&nbsp;发表于&nbsp;<span id="time">
					<b><fmt:formatDate value="${agendaMsg.sendTime}" pattern="yyyy-MM-dd HH:mm:ss" /></b>
				</span>
				<div style="text-align:right;float:right;">
					 <c:if test="${agendaMsg.replyId == curUserId}">
						<span class="delPart">
						<a class="del" href="Javascript:delMsg('${agendaMsg.id}')">[删除]</a></span>
					 </c:if>
				</div>
				<div id="contents">${agendaMsg.contents}</div>
				<hr>
		   </div>
			
		</c:forEach>
	</div>
	
	<script type="text/javascript">
		//提交
		function submitCm() {
			var form = new mini.Form("form");
			form.validate();

			if (!form.isValid()) {
				return;
			}
			var formData = $("#form").serializeArray();
			_SubmitJson({
				url : __ctx + '/oa/calendar/agendaMsg/save.do',
				method : 'POST',
				data : formData,
				success : function(result) {
					location.reload();
				}
			});
		}
		//重置
		function clear() {
			$(".mini-textarea").find(".mini-textbox-input").val("");
		}
		
		//删除回复
		function delMsg(id) {
			var dataId =id;
			_SubmitJson({
				url : __ctx + '/oa/calendar/agendaMsg/del.do',
				method : 'POST',
				data : {
					ids : dataId
				},
				success : function(result) {
					location.reload();
					return;
				}
			});
		}
		
	</script>
	
</body>
</html>