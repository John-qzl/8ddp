<%@page language="java" pageEncoding="UTF-8" %>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>状态设置</title>
<%@include file="/commons/include/form.jsp" %>
<f:sysUser name="UN_LOCKED" alias="UN_LOCKED"></f:sysUser>
<f:sysUser name="LOCKED" alias="LOCKED"></f:sysUser>
<f:sysUser name="DYNPWD_STATUS_BIND" alias="DYNPWD_STATUS_BIND"></f:sysUser>
<f:sysUser name="DYNPWD_STATUS_UNBIND" alias="DYNPWD_STATUS_UNBIND"></f:sysUser>
<f:sysUser name="DYNPWD_STATUS_OUT" alias="DYNPWD_STATUS_OUT"></f:sysUser>
<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=sysUser"></script>
<script type="text/javascript">
		$(function() {
			function showRequest(formData, jqForm, options) { 
				return true;
			} 							
			valid(showRequest,showResponse);
			$("a.save").click(function() {
				$('#sysUserForm').submit(); 
			});
		});
				
		
	</script>
</head>
<body>
<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">设置【${sysUser.fullname}】状态</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" id="dataFormSave">保存</a></div>
					
					<div class="group"><a class="link back" href="${returnUrl}">返回</a></div>
				</div>
			</div>
		</div>
		<div class="panel-body">
				<form id="sysUserForm" method="post" action="editStatus.do">
					<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<th width="20%">当前状态:</th>
							<td >
                             <select name="status"  class="select" style="width:245px !important">
									<option value="${DYNPWD_STATUS_BIND}" <c:if test="${sysUser.status==1}">selected</c:if> >激活</option>
									<option value="${DYNPWD_STATUS_UNBIND}" <c:if test="${sysUser.status==0}">selected</c:if> >禁用</option>
									<option value="${DYNPWD_STATUS_OUT}" <c:if test="${sysUser.status==-1}">selected</c:if>>删除</option>
							 </select>
                            </td>
						</tr>
					</table>
					<input type="hidden" name="userId" value="${sysUser.userId}" />
				</form>
		</div>
</div>
</body>
</html>