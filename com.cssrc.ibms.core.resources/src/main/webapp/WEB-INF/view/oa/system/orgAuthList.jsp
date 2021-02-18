<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>组织管理员</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript">
	//返回
	function returnBack(){
	    window.location.href="${ctx}/oa/system/sysOrg/get.do?orgId=${orgId}";
	}
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">组织管理员列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
				<div class="group"><a class="link search" id="btnSearch">查询</a></div>
				<div class="group"><a class="link back" href="javascript:void(0);" onclick="returnBack()">返回</a></div>
				<div class="l-bar-separator" id="${action }"></div>
				<c:choose>
					<c:when test="${param.isGrade eq true }">
					    <c:if test="${not empty userAuth && fn:contains(userAuth.orgauthPerms, 'add')}">
							<div class="group"><a class="link add" href="edit.do?orgId=${orgId}&isGrade=${grade}&topOrgId=${topOrgId}">添加</a></div>

						</c:if>
						<c:if test="${not empty userAuth && fn:contains(userAuth.orgauthPerms, 'edit')}">
							<div class="group"><a class="link update" id="btnUpd" action="edit.do?orgId=${orgId}&isGrade=${grade}&topOrgId=${topOrgId}">修改</a></div>

						</c:if>
						<c:if test="${not empty userAuth && fn:contains(userAuth.orgauthPerms, 'del')}">
							<div class="group"><a class="link del"  action="del.do">删除</a></div>
						</c:if>
					</c:when>
					<c:otherwise>
					<div class="group"><a class="link add" href="edit.do?orgId=${orgId}&isGrade=${grade}&topOrgId=${topOrgId}">添加</a></div>

						<div class="group"><a class="link update" id="btnUpd" action="edit.do?orgId=${orgId}&isGrade=${grade}&topOrgId=${topOrgId}">修改</a></div>

						<div class="group"><a class="link del"  action="del.do">删除</a></div>

					</c:otherwise>
				</c:choose>
			</div>
			</div>
			<div class="panel-search">
				<form id="searchForm" method="post" action="list.do">
					<div class="row">
						<span class="label">管理员:</span><input type="text" name="Q_userName_SL"  class="inputText" style="width:13%;" value="${param['Q_userName_SL']}"/>
						<span class="label">管理组织:</span><input type="text" name="Q_orgName_SL"  class="inputText" style="width:13%;" value="${param['Q_orgName_SL']}"/>
					</div>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="orgAuthList" id="orgAuthItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
			  		<input type="checkbox" class="pk" name="id" value="${orgAuthItem.id}">
				</display:column>
				<display:column property="userName" title="管理员" sortable="true" sortName="USER_ID"></display:column>
				<display:column property="orgName" title="管理组织" sortable="true" sortName="ORG_ID"></display:column>
				<display:column property="orgPerms" title="子组织操作权限" sortable="true" sortName="ORG_PERMS"></display:column>
				<display:column property="userPerms" title="用户操作权限" sortable="true" sortName="USER_PERMS"></display:column>
				<display:column title="管理" media="html" style="width:220px">
					<c:choose>
						<c:when test="${param.isGrade eq true}">
						<c:if test="${not empty userAuth && fn:contains(userAuth.orgauthPerms, 'del')}">
						  	<a href="del.do?id=${orgAuthItem.id}" class="link del">删除</a>
						  </c:if>
						  <c:if test="${not empty userAuth && fn:contains(userAuth.orgauthPerms, 'edit')}">
						  	<a href="edit.do?id=${orgAuthItem.id}&orgId=${orgId}&isGrade=${grade}&topOrgId=${topOrgId}" class="link edit">编辑</a>
						  </c:if>
						</c:when>
						<c:otherwise>
						  <a href="del.do?id=${orgAuthItem.id}" class="link del">删除</a>
							<a href="edit.do?id=${orgAuthItem.id}&orgId=${orgId}&isGrade=${grade}&topOrgId=${topOrgId}" class="link edit">编辑</a>
						</c:otherwise>
					</c:choose>
				</display:column>
			</display:table>
		<ibms:paging tableId="orgAuthItem"/>
		</div><!-- end of panel-body -->
	</div> <!-- end of panel -->
</body>
</html>


