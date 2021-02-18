<%--
	desc:edit the 参数编辑
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>参数编辑窗口</title>
	<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
	<script type="text/javascript">
		$(function() {
			$("#dataFormSave").click(function() {
				$("#sysParameterForm").attr("action","save.do");
				$("#saveData").val(1);
				submitForm();
			});
		});
		//提交表单
		function submitForm(){
			var options={};
			if(showResponse){
				options.success=showResponse;
			}
			var frm=$('#sysParameterForm').form();
			frm.ajaxForm(options);
			if(frm.valid()){
				frm.submit();
			}
		}
		
		function showResponse(responseText) {
			var obj = new com.ibms.form.ResultMessage(responseText);
			if (obj.isSuccess()) {
				$.ligerDialog.confirm(obj.getMessage()+",是否继续操作","提示信息", function(rtn) {
					if(rtn){
						window.location.href = "${ctx}/oa/system/sysParameter/edit.do?id=${sysParameter.id}&type=${type}";
					}else{
						window.location.href = "${ctx}/oa/system/sysParameter/list.do?type=${type}";
					}
					
					var types = obj.data.types;
					window.parent.loadGlobalTree(JSON.parse(types));
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
		<c:choose>
			    <c:when test="${sysParameter.id !=null}">
			        <span class="tbar-label">编辑参数表</span>
			    </c:when>
			    <c:otherwise>
			        <span class="tbar-label">添加参数表</span>
			    </c:otherwise>			   
		 </c:choose>
		<div class="panel-toolbar">
			<div class="toolBar">
				<div class="group"><a class="link save" id="dataFormSave" href="####">保存</a></div>
				
				<div class="group"><a class="link back" href="list.do?type=${type}">返回</a></div>
			</div>
		</div>
	</div>
	<div class="panel-body">
		<form id="sysParameterForm" method="post" action="save.do">
			<table class="table-detail" cellpadding="0" cellspacing="0" border="0" type="main">
				<tr>
					<th width="20%">参数名称: </th>
			       	<td>
			       		<c:choose>
			       			<c:when test="${sysParameter != null}">
			       				<div>${sysParameter.paramname}</div>
			       				<input type="hidden" id="paramname" name="paramname" value="${sysParameter.paramname}"/>
			       			</c:when>
			       			<c:otherwise>
			       				<input type="text" id="paramname" name="paramname" value="${sysParameter.paramname}" style="width: 230px" validate="{required:true}" class="inputText"/>
			       			</c:otherwise>
			       		</c:choose>
			       	</td>
				</tr>
				<%-- <tr>
					<th width="20%">参数类型: </th>
					<td><input type="text" id="datatype" name="datatype" value="${sysParameter.datatype}"  class="inputText" validate="{required:false}"  /></td>
				</tr> --%>
				<tr>
					<th width="20%">参数分类: </th>
			       	<td>
			       		<c:choose>
			       			<c:when test="${sysParameter != null}">
			       				<input type="text" id="type" name="type" value="${sysParameter.type}" style="width: 230px" validate="{required:true}" class="inputText"/>
			       			</c:when>
			       			<c:otherwise>
			       				<input type="text" id="type" name="type" value="${type}" style="width: 230px" validate="{required:true}" class="inputText"/>
			       			</c:otherwise>
			       		</c:choose>
			       	</td>
				</tr>
				<tr>
					<th width="20%">参数值: </th>
					
					<td>
						<textarea rows="5" cols="32" id="paramvalue" name="paramvalue" style="resize:none" validate="{required:false}">${sysParameter.paramvalue}</textarea>
					</td>
				</tr>
				<tr>
					<th width="30%">参数描述: </th>
					<td>
					<%-- <input type="text" id="paramdesc" name="paramdesc" value='${sysParameter.paramdesc}' style="width: 230px" class="inputText" validate="{required:false}"  /> --%>
						<textarea rows="5" cols="32" id="paramdesc" name="paramdesc" style="resize:none" validate="{required:false}">${sysParameter.paramdesc}</textarea>
					</td>
				</tr>
			</table>
			<input type="hidden" name="id" value="${sysParameter.id}" />
			<input type="hidden" name="datatype" value="${sysParameter.datatype}" />
		</form>
	</div>
</div>
</body>
</html>
