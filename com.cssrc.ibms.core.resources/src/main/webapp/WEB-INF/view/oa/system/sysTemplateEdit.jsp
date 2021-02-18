<%--
	time:2012-12-19 15:38:01
	desc:edit the 自定义表代码模版
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>编辑内部消息模版</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ckeditor/ckeditor_rule.js"></script>
<script type="text/javascript">
	$(function() {
		var options = {};
		if (showResponse) {
			options.success = showResponse;
		}
		var frm = $('#sysTemplateForm').form();
		/*$("a.save").click(function() {
			frm.ajaxForm(options);
			if (frm.valid()) {
				$('#sysTemplateForm').submit();
			}
		});*/
		$("a.save").click(save);
	});
	
	function save(){
		//检查数据
		/*var valRes=validata();
    		 if(!valRes) return;*/
    		 var rtn=$("#sysTemplateForm").valid();
    		 if(!rtn) return;
    		 var url=__ctx+ "/oa/system/sysTemplate/save.do";
	  		 $('#htmlContent').val(htmlContentEditor.getData());
    		 var para=$('#sysTemplateForm').serialize();
    		 $.post(url,para,showResult);
	}
	function showResult(responseText)
	{			
		var obj=new com.ibms.form.ResultMessage(responseText);
		
		if(!obj.isSuccess()){
			$.ligerDialog.error( obj.getMessage(),"出错了");
			return;
		}else{
			$.ligerDialog.success(obj.getMessage(),'提示信息',function(rtn){
				if(rtn) window.close();						
			});
		}
	}

	function showResponse(responseText) {
		var obj = new com.ibms.form.ResultMessage(responseText);
		if (obj.isSuccess()) {
			$.ligerDialog.confirm(obj.getMessage() + ",是否继续操作","提示信息",
							function(rtn) {
								if (rtn) {
									this.close();
									$("#sysTemplateForm").resetForm();
								} else {
									window.location.href = "${ctx}/oa/system/sysTemplate/list.do";

								}
							});
		} else {
			$.ligerDialog.error(obj.getMessage(),'提示信息');
		}
	}

	
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<c:choose>
					<c:when test="${sysTemplate.id !=null}">
						<span class="tbar-label">编辑内部消息模版</span>
					</c:when>
					<c:otherwise>
						<span class="tbar-label">添加内部消息模版</span>
					</c:otherwise>
				</c:choose>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" id="dataFormSave" href="####">保存</a>
					</div>
					
					<div class="group">
						<a class="link back" href="list.do">返回</a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="sysTemplateForm" method="post" action="save.do">
				<table class="table-detail" cellpadding="0" cellspacing="0"
					border="0" type="main">
					<tr>
						<th width="20%">模版名称:</th>
						<td><input type="text" id="name" name="name" value="${sysTemplate.name}" class="inputText" validate="{required:true,maxlength:200}" style="width: 250px;"/></td>
					</tr>
					
					<tr>
						<th width="20%">模板用途</th>
						<td><select id="useType" name="useType"  style="width: 250px;">
							<option value = "1"  <c:if test="${sysTemplate.useType eq 1}">selected="selected"</c:if>>终止提醒</option>
							<option value = "2"  <c:if test="${sysTemplate.useType eq 2}">selected="selected"</c:if>>催办提醒</option>
							<option value = "3"  <c:if test="${sysTemplate.useType eq 3}">selected="selected"</c:if>>审批提醒</option>
							<option value = "4"  <c:if test="${sysTemplate.useType eq 4}">selected="selected"</c:if>>撤销提醒</option>
							<option value = "5"  <c:if test="${sysTemplate.useType eq 5}">selected="selected"</c:if>>取消转办</option>
							<option value = "6"  <c:if test="${sysTemplate.useType eq 6}">selected="selected"</c:if>>沟通提醒</option>
							<option value = "7"  <c:if test="${sysTemplate.useType eq 7}">selected="selected"</c:if>>归档提醒</option>
							<option value = "8"  <c:if test="${sysTemplate.useType eq 8}">selected="selected"</c:if>>转办提醒</option>
							<option value = "9"  <c:if test="${sysTemplate.useType eq 9}">selected="selected"</c:if>>退回提醒</option>
							<option value = "10" <c:if test="${sysTemplate.useType eq 10}">selected="selected"</c:if>>被沟通人提交</option>
							<option value = "11" <c:if test="${sysTemplate.useType eq 11}">selected="selected"</c:if>>取消代理</option>
							<option value = "12" <c:if test="${sysTemplate.useType eq 12}">selected="selected"</c:if>>抄送提醒</option>
							<option value = "13" <c:if test="${sysTemplate.useType eq 13}">selected="selected"</c:if>>流程节点无人员</option>
							<option value = "14" <c:if test="${sysTemplate.useType eq 14}">selected="selected"</c:if>>跟进事项预警</option>
							<option value = "15" <c:if test="${sysTemplate.useType eq 15}">selected="selected"</c:if>>跟进事项到期</option>
							<option value = "16" <c:if test="${sysTemplate.useType eq 16}">selected="selected"</c:if>>跟进事项完成,评价</option>
							<option value = "17" <c:if test="${sysTemplate.useType eq 17}">selected="selected"</c:if>>跟进事项通知</option>
							<option value = "18" <c:if test="${sysTemplate.useType eq 18}">selected="selected"</c:if>>跟进事项已评价</option>
							<option value = "19" <c:if test="${sysTemplate.useType eq 19}">selected="selected"</c:if>>逾期提醒</option>
							<option value = "22" <c:if test="${sysTemplate.useType eq 22}">selected="selected"</c:if>>代理提醒</option>
							<option value = "23" <c:if test="${sysTemplate.useType eq 23}">selected="selected"</c:if>>消息转发</option>
							<option value = "24"  <c:if test="${sysTemplate.useType eq 24}">selected="selected"</c:if>>重启任务</option>
							<option value = "25"  <c:if test="${sysTemplate.useType eq 25}">selected="selected"</c:if>>通知任务所属人(代理)</option>
							<option value = "26"  <c:if test="${sysTemplate.useType eq 26}">selected="selected"</c:if>>加签提醒</option>
							<option value = "27"  <c:if test="${sysTemplate.useType eq 27}">selected="selected"</c:if>>被加签人提交</option>
							<option value = "28"  <c:if test="${sysTemplate.useType eq 28}">selected="selected"</c:if>>取消流转</option>	
						</select></td>
					</tr>
					<!--<td><input type="text" id="useType" name="useType" value="${sysTemplate.useType}" class="inputText" validate="{required:true,maxlength:200}" style="width: 250px;"  /></td>
						  -->
					
					<tr>
						<th width="20%">标题:</th>
						<td><input type="text" id="title" name="title" value="${sysTemplate.title}" class="inputText" validate="{required:true,maxlength:200}" style="width: 250px;"/></td>
					</tr>

					
					<tr>
						<th width="20%">模板html内容:</th>
						<td>
						<textarea  id= "htmlContent" name="htmlContent"  style="width:800px;height:250px;">${fn:escapeXml(sysTemplate.htmlContent)}</textarea>
						<script type="text/javascript">
							htmlContentEditor = ckeditor('htmlContent');
						</script></td>
					</tr>
					
					<tr>
						<th width="20%">普通内容:</th>
						<td><textarea name="plainContent" style="width:800px;height:250px;">${fn:escapeXml(sysTemplate.plainContent)}</textarea>
						</td>
					</tr>
					
					<tr>
						<th width="20%">是否默认模板:</th>
						<td>
							<input type="radio" name="isDefault" value="0" <c:if test="${sysTemplate.isDefault eq 0 or empty sysTemplate.isDefault}"> checked='checked' </c:if>>否
							&nbsp;&nbsp;
							<input type="radio" name="isDefault" value="1" <c:if test="${sysTemplate.isDefault eq 1 }"> checked='checked' </c:if>>是
						</td>
					</tr>

				</table>
				<input type="hidden" name="id" value="${sysTemplate.id}" />
			</form>

		</div>
	</div>
</body>
</html>
