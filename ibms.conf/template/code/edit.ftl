<#assign class=table.variable.class>
<#assign classVar=table.variable.classVar>
<#assign package=table.variable.package>
<#assign fieldList=table.fieldList>
<#assign subtables=table.subTableList>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<%@include file="/commons/include/ueditor.jsp" %>
<html>
<head>
	<title>编辑 ${table.tableDesc}</title>
	<%@include file="/commons/include/customForm.jsp" %>
	<#if subtables?size!=0>
	<script type="text/javascript" src="<#noparse>${ctx}</#noparse>/jslib/ibms/formdata.js"></script>
	<script type="text/javascript" src="<#noparse>${ctx}</#noparse>/jslib/ibms/subform.js"></script>
	</#if>
	<#if flowKey?exists>
	<script type="text/javascript" src="<#noparse>${ctx}</#noparse>/jslib/ibms/oa/bpm/FlowDetailWindow.js"></script>
	</#if>
	<script type="text/javascript">
		$(function() {
			var options={};
			if(showResponse){
				options.success=showResponse;
			}
			var frm=<#noparse>$('#</#noparse>${classVar}Form').form();
			$("a.save").click(function() {
				frm.ajaxForm(options);
				if(frm.valid()){
					OfficePlugin.submit();
					if(WebSignPlugin.hasWebSignField){
						WebSignPlugin.submit();
					}
					<#if subtables?size!=0>
					frm.sortList();
					</#if>
					<#noparse>$('#</#noparse>${classVar}Form').submit();
				}
			});
			<#if flowKey?exists>
			$("a.run").click(function() {
				frm.ajaxForm(options);
				<#noparse>$('#</#noparse>${classVar}Form').attr("action","run.do");
				if(frm.valid()){
					if(WebSignPlugin.hasWebSignField){
						WebSignPlugin.submit();
					}
					OfficePlugin.submit();
					<#if subtables?size!=0>
					frm.sortList();
					</#if>
					<#noparse>$('#</#noparse>${classVar}Form').submit();
				}
			});
			</#if>
		});
		
		function showResponse(responseText) {
			var obj = new com.ibms.form.ResultMessage(responseText);
			if (obj.isSuccess()) {
				$.ligerDialog.confirm(obj.getMessage()+",是否继续操作","提示信息", function(rtn) {
					if(rtn){
						this.close();
					}else{
						window.location.href = "<#noparse>${ctx}</#noparse>/${system}/${package}/${classVar}/list.do";
					}
				});
			} else {
				$.ligerDialog.error(obj.getMessage(),"提示信息");
			}
		}
	</script>
</head>
<body>
<div class="panel">
	<div class="panel-top">
		<div class="tbar-title">
		    <c:choose>
			    <c:when test="<#noparse>${not empty </#noparse>${classVar}Item.id}">
			        <span class="tbar-label"><span></span>编辑${table.tableDesc}</span>
			    </c:when>
			    <c:otherwise>
			        <span class="tbar-label"><span></span>添加${table.tableDesc}</span>
			    </c:otherwise>	
		    </c:choose>
		</div>
		<div class="panel-toolbar">
			<div class="toolBar">
				<div class="group"><a class="link save" id="dataFormSave" href="####">保存</a></div>
				
				<#if flowKey?exists>
				<c:if test="<#noparse>${</#noparse>runId==0}">
				<div class="group"><a class="link run"  href="####">提交</a></div>
				
				</c:if>
				<c:if test="<#noparse>${</#noparse>runId!=0}">
					<div class="group"><a class="link detail"  onclick="FlowDetailWindow({runId:<#noparse>${</#noparse>runId}})" href="####" >流程明细</a></div>
					
				</c:if>
				</#if>
				<div class="group"><a class="link back" href="list.do">返回</a></div>
			</div>
		</div>
	</div>
	<form id="${classVar}Form" method="post" action="save.do">
		<div type="custform">
			<div class="panel-detail">
				${html}
			</div>
		</div>
		<input type="hidden" name="${table.pkField?lower_case}" value="<#noparse>${</#noparse>${classVar}.${table.pkField?lower_case}}"/>
		<input type="hidden" id="saveData" name="saveData"/>
		<input type="hidden" name="executeType"  value="start" />
	</form>
</body>
</html>
