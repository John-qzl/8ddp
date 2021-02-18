<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>系统日志开关编辑页面</title>
<%@include file="/commons/include/get.jsp" %>
<%@include file="/commons/include/form.jsp" %>
<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
<script type="text/javascript">
$(function() {
	$("a.save").click(function() {
		var options={};
		if(showResponse){
			options.success=showResponse;
		}
		var frm=$('#sysLogSwitchForm').form();
		frm.ajaxForm(options);
		if(frm.valid()){
			frm.submit();
		}
	});
});

//获取选中的日志类型
function getCheckedTypes(){
	var aryChk=$("input:checkbox[name='execType']:checked");
	if(aryChk.length==0)
		$("#execTypes").val("");
	else{
		var execTypes=[];
		aryChk.each(function(){
			execTypes.push($(this).val());
		});
		$("#execTypes").val(execTypes.join(","));
	}
}

function showResponse(responseText) { 
	var obj = new com.ibms.form.ResultMessage(responseText);
	if (obj.isSuccess()) {
		$.ligerDialog.confirm(obj.getMessage()+",是否继续操作","提示信息", function(rtn) {
			if(!rtn){
				window.location.href = "${ctx}/oa/system/sysLogSwitch/management.do?";
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
	        <span class="tbar-label">编辑系统日志开关信息页面</span>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" id="dataFormSave" href="####">保存</a></div>
					
					<div class="group"><a class="link back" href="management.do">返回</a></div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="sysLogSwitchForm" method="post" action="save.do">
				<table class="table-detail" cellpadding="0" cellspacing="0" border="0" type="main">
					<tr>
						<th width="20%">模块名: </th>
				       	<td>
				       		${modelxx}
				       		<input type="hidden" id="model" name="model" value="${modelxx}" />
				       	</td>
					</tr>
					<tr>
						<th width="20%">备注: </th>
				       	<td><input type="text" id="memo" name="memo" value="${sysLogSwitch.memo}" style="width: 230px" validate="{required:true}" class="inputText"/></td>
					</tr>
					<tr>
						<th width="20%">状态: </th>
						<td>
							<select name="status" class="select" style="width:230px !important">
								<option value="1" <c:if test="${sysLogSwitch.status==1}">selected</c:if> >开启</option>
								<option value="0" <c:if test="${sysLogSwitch.status==0}">selected</c:if> >关闭</option>
							</select>	
						</td>
					</tr>
					<tr>
						<th width="20%">日志类型: </th>
						<td>
							<div style="width:500px;">
								<c:forEach items="${execTypeArr}" var="item">
									<li style="width:33.3%;float:left;">
									   <input type="checkbox" name="execType" value="${item.name}" <c:if test="${item.status eq '1'}"> checked</c:if> onclick="getCheckedTypes();"/>
							            ${item.name}
							        <li>
								</c:forEach>
							</div>
							<input type="hidden" id="execTypes" name="execTypes" value="${sysLogSwitch.execTypes}"/>
						</td>
					</tr>
				</table>
				<input type="hidden" name="id" value="${sysLogSwitch.id}" />
			</form>
		</div>
	</div>
</body>
</html>


