<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>

<html>
  <head>
    <title>编辑文件信息</title>
    <%@include file="/commons/include/form.jsp"%>
    <%@include file="/commons/include/customForm.jsp" %> 
    <f:sysFile name="SECURITY_JUEMI" alias="SECURITY_JUEMI"></f:sysFile>
	<f:sysFile name="SECURITY_JIMI" alias="SECURITY_JIMI"></f:sysFile>
	<f:sysFile name="SECURITY_MIMI" alias="SECURITY_MIMI"></f:sysFile>
	<f:sysFile name="SECURITY_GONGKAI" alias="SECURITY_GONGKAI"></f:sysFile>
	<f:sysFile name="JUEMI" alias="JUEMI"></f:sysFile>
	<f:sysFile name="JIMI" alias="JIMI"></f:sysFile>
	<f:sysFile name="MIMI" alias="MIMI"></f:sysFile>
	<f:sysFile name="GONGKAI" alias="GONGKAI"></f:sysFile>
	<script type="text/javascript">
		$(function(){
			function showRequest(formData, jqForm, options) { 
				return true;
			} 
			$("a.save").click(saveHandler);
		});
		
		function saveHandler(){
			
			var filename = $('#filename').val();
			if(filename == ''){
				$.ligerDialog.warn("文件名称不能为空！");
			}else{
				$.ajax({
				   url : "${ctx}/oa/system/sysFile/save.do",
				   type : "POST",
				   data : {
					   fileId: $('#fileId').val(),
					   fileName : $('#filename').val(),
					   security : $('#security').val()
				   },
				   success : function(data){
						showResponse(data);
				   }
				});
			}
		}
	
		function showResponse(responseText){
			var obj=new com.ibms.form.ResultMessage(responseText);
			if(obj.isSuccess()){
				var megs = obj.getMessage();
				//保存成功后返回的信息："提示信息&&&*&*keyId"
				var megsArray = megs.split('&&&*&*');
				var message = '';
				var keyId ='';
				if(megsArray.length>1){
					message = megsArray[0];
					keyId = megsArray[1];
				 }else{
				 	keyId = '';
					message = megs;
				 }
				 //获取表单数据
				 //var data=CustomForm.getData();
				 var data = preGetDataHandler(CustomForm,CustomForm.getData());
				 var tableId = $('#tableId').val();
				 //表单数据保存后的后处理
				 var params = $.extend({},{tableId:tableId},{keyId:keyId});
				 var saveResult = afterSaveHandler(data,params);
				 if(saveResult == true){
					 $.ligerDialog.confirm( message + ",是否继续操作","提示信息", function(rtn) {
							if(!rtn){
								window.close();
							}
						});
				 }else if(saveResult == false){
				  	$.ligerDialog.warn( "业务后处理失败！","提示信息", function(rtn) {
							if(!rtn){
								window.close();
							}
						});
				 }
			 
			}else{
				alert(obj.getMessage());
			}
		}
		
		function preSaveHandler(){
			//添加保存前处理
			return true;
		}
		
		function preGetDataHandler(CustomForm,data){
			//添加保存前获取数据处理
			return data;
		}
		
		function afterSaveHandler(data,params){
			//
		    //var json=params;
		    //alert(json.tableId);
		    //alert(json.keyId);
			// 添加保存后处理
			return true;
		}
		
	</script>
	
  </head>
  
  <body>
  <div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">编辑文件信息</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
				    <c:if test="${sysFile.filename != null}">
						<div class="group">
							<a class="link save" href="####">保存</a>
						</div>
						
					</c:if>
					<div class="group">
						<a class="link back" href="list.do">返回</a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="sysFileForm" method="post" action="save.do">
			    <input id="fileId" name="fileId" type="hidden" value="${sysFile.fileId}" />
				<table class="table-detail" cellpadding="0" cellspacing="0" border="0" type="main">
					<tr>
						<th width="20%">文件名称:</th>
						<td><input type="text" id="filename" name="filename" value="${sysFile.filename}" class="inputText" style="width: 70%" /></td>
					</tr>
					<tr>
						<th width="20%">文件密级:${sysFile.security}</th>
						<td>
							<select id="security" name="security" style="width: 70%" >
				                <option value="${GONGKAI}" <c:if test="${sysFile.security eq SECURITY_GONGKAI}"> selected="selected"</c:if> >${GONGKAI}</option>
				                <option value="${MIMI}" <c:if test="${sysFile.security eq SECURITY_MIMI}"> selected="selected"</c:if> >${MIMI}</option>
				                <option value="${JIMI}" <c:if test="${sysFile.security eq SECURITY_JIMI}"> selected="selected"</c:if> >${JIMI}</option>
				                <option value="${JUEMI}" <c:if test="${sysFile.security eq SECURITY_JUEMI}"> selected="selected"</c:if> >${JUEMI}</option>
							</select>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
  </body>
</html>
