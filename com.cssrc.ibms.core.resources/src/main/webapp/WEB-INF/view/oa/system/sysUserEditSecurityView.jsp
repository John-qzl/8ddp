<%@page language="java" pageEncoding="UTF-8" %>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>密级设置</title>
<%@include file="/commons/include/form.jsp" %>
<f:sysUser name="UN_LOCKED" alias="UN_LOCKED"></f:sysUser>
<f:sysUser name="LOCKED" alias="LOCKED"></f:sysUser>
<f:sysUser name="SECURITY_FEIMI" alias="SECURITY_FEIMI"></f:sysUser>
<f:sysUser name="SECURITY_YIBAN" alias="SECURITY_YIBAN"></f:sysUser>
<f:sysUser name="SECURITY_ZHONGYAO" alias="SECURITY_ZHONGYAO"></f:sysUser>
<f:sysUser name="SECURITY_HEXIN" alias="SECURITY_HEXIN"></f:sysUser>
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
				<span class="tbar-label">设置【${sysUser.fullname}】密级</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" id="dataFormSave">保存</a></div>
					
					<div class="group"><a class="link back" href="${returnUrl}">返回</a></div>
				</div>
			</div>
		</div>
		<div class="panel-body">
				<form id="sysUserForm" method="post" action="editSecurity.do">
					<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<th width="20%">用户名:</th>
							<td>${sysUser.fullname}</td>
						</tr>
						<tr>
							<th width="20%">当前密级:</th>
							<td >
                             	<select id="security" name="security" class="select" style="width:245px !important">
									<c:forEach items="${securityUserMap}" var="securityUserMap" >
										<option value="${securityUserMap.key}" <c:if test="${securityUserMap.key eq sysUser.security}">
											selected="selected"</c:if> >
											${securityUserMap.value}
										</option>
									</c:forEach>
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