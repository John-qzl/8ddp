<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title></title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript" src="${ctx }/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript">
	$(function() {

	});
	function dlgCallBack(userIds){
		var orgId="${orgId}";
		var path="${path}";
		var url="addOrgUser.do";
		para="userIds="+userIds+"&orgId="+orgId;
		$.post(url,para,function(data){
			var obj=new com.ibms.form.ResultMessage(data);
			if(obj.isSuccess()){
				 $.ligerDialog.success(obj.getMessage(),"提示信息",function(rtn){
					  location.href="userList.do?orgId="+orgId+"&path="+path;
				 });
			}else{
				$.ligerDialog.err('出错信息',"查询组织人员失败",obj.getMessage());
			}

		});
	};
	function addClick(){
		UserDialog({callback:dlgCallBack,isSingle:false});
	};
	//当前组织加入用户
	function dlgCallBack1(userIds, fullnames) {
		if (userIds.length > 0) {
			var form = new com.ibms.form.Form();
			form.creatForm("form", "${ctx}/oa/system/userPosition/add.do");
			form.addFormEl("orgId", "${orgId}");
			form.addFormEl("userIds", userIds);
 	 		form.submit();
		}
	};
	function add() {
		UserDialog({
			callback : dlgCallBack1,
			isSingle : false
		});
	}


</script>
</head>
<body>
<div class="panel">
		<c:choose>
		  	<c:when test="${action=='global' }">
	  			<f:tab curTab="info" tabName="sysOrg%"/>
		  	</c:when>
		  	<c:otherwise>
		  		<f:tab curTab="info" tabName="sysOrgGrade%"/>
		  	</c:otherwise>
	   </c:choose>

       <c:choose>
       		<c:when test="${empty sysOrg}">
					<div style="text-align: center;margin-top: 10%;">尚未指定具体组织!</div>
				</c:when>
       		<c:otherwise>
       		<div class="panel-top">
	       		<div class="panel-toolbar">
					<div class="toolBar">
						<div class="toolBar">
							<div class="group"><a class="link search" id="btnSearch">查询</a></div>
							<c:if test="${!isRight}">
								<c:choose>
	 								<c:when test="${action=='global'}">
	 									<div class="group">
											<a class="link add"  href="${ctx}/oa/system/sysUser/edit.do?selectedOrgId=${orgId}">新增人员</a>
										</div>
										<!-- <div class="group">
											<a class="link add" href="javascript:add();"><span></span>加入用户</a>
										</div> -->
										<div class="group"><a class="link del" action="${ctx}/oa/system/userPosition/del.do">移除</a></div>
 									</c:when>
	 								<c:otherwise>
	 									<c:if test="${not empty orgAuth && fn:contains(orgAuth.userPerms, 'add')}">
											<div class="group"><a class="link add"  href="${ctx}/oa/system/sysUser/editGrade.do?orgId=${orgId}&topOrgId=${param.topOrgId}">新增人员</a></div>
										</c:if>
	 									<c:if test="${not empty orgAuth && fn:contains(orgAuth.userPerms, 'delete')}">
											<div class="group"><a class="link del" action="${ctx}/oa/system/userPosition/del.do">移除</a></div>
										</c:if>
	 								</c:otherwise>
								</c:choose>
							</c:if>
							<div class="group"><a class="link reset" onclick="$.clearQueryForm()">重置</a></div>
						</div>
					</div>
				</div>
				<div class="panel-search">
					<form id="searchForm" method="post" action="userList.do?orgId=${orgId}">
						<ul class="row plat-row">
							<li><span class="label">姓名:</span><input type="text" name="Q_fullname_SL"  class="inputText"  value="${param['Q_fullname_SL']}"/></li>
						    <li><span class="label">账号:</span><input type="text" name="Q_username_SL"  class="inputText"  value="${param['Q_username_SL']}"/></li>
						</ul>
					</form>
		 		</div>
		 	</div>
			<div class="panel-body">
		        <c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
				</c:set>
			    <display:table name="userPositionList" id="userPositionItem"  requestURI="userList.do" sort="external" cellpadding="1" cellspacing="1" export="false"  class="table-grid">
					<display:column title="${checkAll}" media="html" style="width:30px;text-align:center;">
						  	<input type="checkbox" class="pk" name="userPosId" value="${userPositionItem.userPosId}">
					</display:column>
					<display:column property="orgName" title="所属组织" sortable="true" sortName="orgId"></display:column>
					<display:column property="posName" title="岗位" sortable="true" sortName="posId"></display:column>
						<%--
					<display:column property="userName" title="姓名" sortable="true" sortName="userId">
				      --%>
					<display:column  title="姓名" sortable="true" sortName="userId">
						<f:userName userId="${userPositionItem.userId}" />
					</display:column>
					<display:column property="username" title="帐号" sortable="true" sortName="userId"></display:column>
					<display:column   title="主岗位" >
						<c:choose>
							<c:when test="${userPositionItem.isPrimary==1}"><span class="green">是</span></c:when>
							<c:otherwise><span class="red">否</span></c:otherwise>
						</c:choose>
					</display:column>
					<display:column   title="负责人" >
						<c:choose>
							<c:when test="${userPositionItem.isCharge==1}"><span class="green">是</span></c:when>
							<c:otherwise><span class="red">否</span></c:otherwise>
						</c:choose>
					</display:column>
					<display:column title="状态" sortable="true" sortName="status">
						<c:choose>
							<c:when test="${userPositionItem.status==1}">
								<span class="green">激活</span>

						   	</c:when>
						   	<c:when test="${userPositionItem.status==0}">
						   		<span class="red">禁用</span>

						   	</c:when>
					       	<c:otherwise>
					       		<span class="red">删除</span>

						   	</c:otherwise>
						</c:choose>
					</display:column>

						<display:column title="管理" media="html" style="width:50px;text-align:center" class="rowOps">
							<c:if test="${!isRight}">
								<c:choose>
									<c:when test="${action=='global'}">
										<c:if test="${not empty orgAuth && fn:contains(orgAuth.userPerms, 'delete')}">
											<a href="${ctx}/oa/system/userPosition/del.do?userPosId=${userPositionItem.userPosId}" class="link del">删除</a>
										</c:if>
										<c:if test="${not empty orgAuth && fn:contains(orgAuth.userPerms, 'edit')}">
											<a class="link edit"  href="${ctx}/oa/system/sysUser/editGrade.do?orgId=${orgId}&userId=${userPositionItem.userId}">编辑</a>
											<c:choose>
												<c:when test="${userPositionItem.isPrimary==0}">
													<a class="link primary" href="setIsPrimary.do?userPosId=${userPositionItem.userPosId}">设为主岗位</a>
												</c:when>
												<c:otherwise>
													<a class="link notPrimary" href="setIsPrimary.do?userPosId=${userPositionItem.userPosId}">设为非主岗位</a>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${userPositionItem.isCharge==0}">
													<a class="link charge" id="charge" title="设为负责人" href="setIsCharge.do?userPosId=${userPositionItem.userPosId}">设置为负责人</a>
												</c:when>
												<c:otherwise>
													<a class="link noCharge" id="noCharge" title="设为非负责人" href="setIsCharge.do?userPosId=${userPositionItem.userPosId}">设置为非负责人</a>
												</c:otherwise>
											</c:choose>
										</c:if>
		 							</c:when>
		 							<c:otherwise>
											<c:if test="${not empty orgAuth && fn:contains(orgAuth.userPerms, 'delete')}">
												<a href="${ctx}/oa/system/userPosition/del.do?userPosId=${userPositionItem.userPosId}" class="link del">删除</a>
											</c:if>
											<c:if test="${not empty orgAuth && fn:contains(orgAuth.userPerms, 'edit')}">
												<a class="link edit"  href="${ctx}/oa/system/sysUser/editGrade.do?orgId=${orgId}&userId=${userPositionItem.userId}"><span></span>编辑</a>
												<c:choose>
													<c:when test="${userPositionItem.isPrimary==0}">
														<a class="link primary" href="setIsPrimary.do?userPosId=${userPositionItem.userPosId}">设为主岗位</a>
													</c:when>
													<c:otherwise>
														<a class="link notPrimary" href="setIsPrimary.do?userPosId=${userPositionItem.userPosId}">设为非主岗位</a>
													</c:otherwise>
												</c:choose>
												<c:choose>
													<c:when test="${userPositionItem.isCharge==0}">
														<a class="link charge" id="charge" title="设为负责人" href="setIsCharge.do?userPosId=${userPositionItem.userPosId}">设置为负责人</a>
													</c:when>
													<c:otherwise>
														<a class="link noCharge" id="noCharge" title="设为非负责人" href="setIsCharge.do?userPosId=${userPositionItem.userPosId}">设置为非负责人</a>
													</c:otherwise>
												</c:choose>
											</c:if>
										</c:otherwise>
									</c:choose>
								</c:if>

								<a href="${ctx}/oa/system/sysUser/get.do?userId=${userPositionItem.userId}&canReturn=0" class="link detail">明细</a>
						 </display:column>
					</display:table>
				<ibms:paging tableId="userPositionItem"/>

	   		</div>
       		</c:otherwise>
       </c:choose>

	  </div>
</body>
</html>