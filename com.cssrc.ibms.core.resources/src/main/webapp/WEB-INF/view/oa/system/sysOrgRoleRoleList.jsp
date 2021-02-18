<%@ page language="java" contentType="text/html; charset=UTF-8"    pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title></title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript" src="${ctx }/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript" src="${ctx }/jslib/ibms/oa/system/GradeRoleSelectDialog.js"></script>
<script type="text/javascript">
	var orgId="${orgId}";
	var scope="${scope}";
	
	var reloadUrl=__ctx + "/oa/system/sysOrgRole/roleList.do?orgId=${orgId}";
	var url=__ctx + "/oa/system/sysOrgRole/addOrgRole.do";
	if(scope=="grade"){
		reloadUrl=__ctx + "/oa/system/sysOrgRole/roleGradeList.do?orgId=${orgId}&authId=${param.authId}&topOrgId=${param.topOrgId}";
		url=__ctx + "/oa/system/sysOrgRole/addGradeOrgRole.do";
	}
	
		
	function dlgCallBack(roles){
		if(roles==null||roles==""){
			return;
		}
		para="roleIds="+roles+"&orgId="+orgId;
		$.post(url,para,function(data){
			var obj=new com.ibms.form.ResultMessage(data);
			if(obj.isSuccess()){
				 $.ligerDialog.success(obj.getMessage(),"提示信息",function(rtn){
					  location.href=reloadUrl;
				 });
			}else{
				$.ligerDialog.err('出错信息',"分配角色授权失败",obj.getMessage());
			}
		});
	};
	
	

	$(function(){
		$("a.add").click(function(){
			if(scope=="grade"){
				GradeRoleSelectDialog({orgId: orgId,callback:dlgCallBack});
			}
			else{
				RoleDialog({callback:dlgCallBack});
			}		
		});		
	});
</script>
</head>
<body>
<div class="panel">
       
       <c:choose>
	   		<c:when test="${scope eq 'global' }">
		  		<f:tab curTab="authorize" tabName="sysOrg%"/>
	   		</c:when>
	   		<c:otherwise>
		  		<f:tab curTab="authorize" tabName="sysOrgGrade%"/>
	   		</c:otherwise>
	   </c:choose>
       <c:choose>
       		<c:when test="${empty sysOrg}">
					<div style="text-align: center;margin-top: 10%;">尚未指定具体组织!</div>
				</c:when>
       		<c:otherwise>
       		<div class="hide-panel">
       		<div class="panel-top">
	       		<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group"><a class="link add" href="javascript:;">分配角色</a></div>
						
						<c:choose>
							<c:when test="${scope=='global' }">
								<div class="group"><a class="link del" action="${ctx}/oa/system/sysOrgRole/del.do">移除</a></div>
							</c:when>
							<c:otherwise>
								<div class="group"><a class="link del" action="${ctx}/oa/system/sysOrgRole/delGrade.do">移除</a></div>
							</c:otherwise>
						</c:choose>
					</div>	
				</div>
		 	</div>
		 	</div>
			<div class="panel-body">
			        <c:set var="checkAll">
					<input type="checkbox" id="chkall"/>
					</c:set>
				    <display:table name="orgRoles" id="orgRole" cellpadding="1" cellspacing="1" export="false" class="table-grid">
						<display:column title="${checkAll}" media="html" style="width:30px;text-align:center;">							
							<c:choose>
							<c:when test="${scope=='global' }">
								<c:choose>
									<c:when test="${orgRole.orgid==orgId}">
										<input type="checkbox" class="pk" name="id" value="${orgRole.id}"/>
									</c:when>
									<c:otherwise>
										<%-- <input type="checkbox" class="pk" disabled="disabled" name="id" value="${orgRole.id}"/> --%>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${orgRole.orgid==orgId && orgRole.canDel==1 }">
										<input type="checkbox" class="pk" name="id" value="${orgRole.id}"/>
									</c:when>
									<c:otherwise>
<%-- 										<input type="checkbox" class="pk" disabled="disabled" name="id" value="${orgRole.id}"/>
 --%>									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
						</display:column>
										
						<display:column property="role.roleName" title="角色名"></display:column>
						<display:column property="role.roleDesc" title="角色备注"></display:column>
						<display:column property="orgName" title="所属组织"></display:column>
						<display:column title="管理" media="html" >
							<c:choose>
								<c:when test="${scope=='global' }">
									<c:choose>
										<c:when test="${orgRole.orgid==orgId}">
											<a href="${ctx}/oa/system/sysOrgRole/del.do?id=${orgRole.id}" class="link del">移除</a>
										</c:when>
										<c:otherwise>
											<a  class="link del disabled">移除</a>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${orgRole.orgid==orgId && orgRole.canDel==1 }">
											<a href="${ctx}/oa/system/sysOrgRole/delGrade.do?id=${orgRole.id}" class="link del">移除</a>
										</c:when> 
										<c:otherwise>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
						</display:column>
					</display:table>
	   		</div>
       		</c:otherwise>
       </c:choose>       
	  </div> 					
</body>
</html>