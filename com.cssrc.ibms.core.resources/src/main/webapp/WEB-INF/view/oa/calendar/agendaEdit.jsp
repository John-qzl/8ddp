<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="ibms" uri="http://www.ibms.cn/formFun"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>日程编辑</title>
<%@include file="/commons/portalCustom.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/util/util.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/util/DialogUtil.js" ></script>
<script type="text/javascript"  src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
</head>
<body>
	<div id="p1" class="form-outer" >
		<form id="form1" method="post">
			<div class="form-inner">
				<input id="pkId" name="agendaId" class="mini-hidden" value="${agenda.agendaId}" />
				<table class="table-detail" cellspacing="1" cellpadding="0">
					<tr id="tempTr">
						<th>日程类型：</th>
						<td>
							<input id="type" name="type" class="mini-combobox"  url="${ctx}/oa/calendar/agenda/getType.do"  value="${agenda.type}" textfield="typeName" valuefield="typeId" emptyText="请选择..."/> 
						</td>
					</tr>
					
					<tr>
						<th>标题 ：</th>
						<td><input name="subject" value="${agenda.subject}" class="mini-textbox" vtype="maxLength:50" style="width: 90%" required="true" emptyText="请输入栏目名称" /></td>
					</tr>

					<tr>
						<th>接收人:</th>
						<td>							
							<input id="executorIds"  name="executorIds" type="hidden" value="${agenda.executorIds}" />
							
							<input id="executors"  name="executors" value="${agenda.executors}"  text="${agenda.executors}"  
							style="width: 180px;" allowInput="false" class="mini-buttonedit" onbuttonclick="chooseUser" />
						</td>
					</tr>
					
					
					<tr>
						<th>内容：</th>
						<td><textarea name="content" class="mini-textarea" vtype="maxLength:512" style="width: 90%" rows="10">${agenda.content}</textarea></td>
					</tr>
					
					<tr>
						<th>紧急程度 :</th>
						<td>
							<div id="grade" name="grade" class="mini-radiobuttonlist"  value="${agenda.grade}" 
							    data="[{id:'COMMON',text:'一般'},{id:'MAJOR',text:'重要'},{id:'URGENT',text:'紧急'}]" >
							</div>    
						</td>
					</tr>
					
					<tr>
						<th>开始时间：</th>
						
						<td><input name="startTime" value="${agenda.startTime}" class="mini-datepicker"  showtime="true"  format="yyyy-MM-dd HH:mm:ss" required="true"  />
					</tr>
					
					<tr>
						<th>结束时间：</th>
						<td><input name="endTime" value="${agenda.endTime}" class="mini-datepicker"  showtime="true" format="yyyy-MM-dd HH:mm:ss" required="true" />
					</tr>
					
					
					
					<tr>
						<th>提醒方式:</th>
						<td>
							<div id="warnWay" name="warnWay" class="mini-radiobuttonlist"  value="${agenda.warnWay}" 
							    data="[{id:'0',text:'不提醒'},{id:'1',text:'邮件'},{id:'2',text:'站内信'}]" >
							</div>    
						</td>
					</tr>
					
					<%-- <tr>
						<th>附件:</th>
						<td>
							<div id="loadType" name="loadType" class="mini-radiobuttonlist"  value="${agenda.loadType}" onvaluechanged="changeLoadType"
							    data="[{id:'NOWARN',text:'不提醒'},{id:'MAIL',text:'邮件'},{id:'MSG',text:'站内信'}]" >
							</div>    
						</td>
					</tr> --%>
					
				</table>
			</div>
		</form>
	</div>
	<div  style="text-align: center; padding-bottom: 5px">
	<a id="submitCm" class="mini-button"  onclick="submitCm()"><b>提交</b></a> 
			<span style="font-size: 24px;">&nbsp;&nbsp;&nbsp;</span> 
	</div>
	

	<script type="text/javascript">
		var dialog = null; //调用页面的dialog对象(ligerui对象)
		if (frameElement) {
			dialog = frameElement.dialog;
		}
		//增加参与人按钮
		function chooseUser() {
			var user=$("#executorIds");
			var username=mini.get("executors");
			var oldUserNames=$("#executors").val();
			var oldUserIds=$("#executorIds").val();
			var userId="";
			var userName="";
			
			UserDialog({
 	        	selectUserIds:oldUserIds,
 	        	selectUserNames:oldUserNames,
 	        	isResize:false,
 	        	callback:function(userIds,userNames){
 	        		user.val(userIds);   //用逗号分隔收件人Id列表
 	        		username.setText(userNames);  //用逗号分隔收件人姓名列表
 	        		username.setValue(userNames);  //用逗号分隔收件人姓名列表
 		        }
 	        });
		};
			
			function submitCm() {
				 var form = new mini.Form("form1");
				 form.validate();
				 if (!form.isValid()) {
			            return;
			        }
				 var formData=$("#form1").serializeArray();
				_SubmitJson({
					url : __ctx + '/oa/calendar/agenda/save.do',
					method : 'POST',
					data : formData,
					success : function(result) {
						//
						if(dialog){
							dialog.get("sucCall")('ok');
							dialog.close();
						}else {
							window.opener.reload();
							window.returnValue = "ok";
							window.close();
						}
						
					}
				});
			};
	</script>
</body>
</html>