<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>系统岗位表，实际是部门和职务的对应关系表管理</title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript" src="${ctx }/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript">
	$(function() {
	});
	//选择后的回调函数，保存岗位用户关系数据
	function dlgCallBack(userIds) {
		if(userIds==""){
			return;
		}
		
		var posId = $(":checkbox[class='pk']:checked").val();
		var url = "${ctx}/oa/system/userPosition/addPosUser.do"; //添加人员，保存人员岗位关系
		para = "userIds=" + userIds + "&posId=" + posId;
		$.post(url, para, function(data) {
			var obj = new com.ibms.form.ResultMessage(data);
			if (obj.isSuccess()) {
				$.ligerDialog.success(obj.getMessage(), "提示信息", function(rtn) {
					location.href = "list.do?orgId=" + ${orgId};
				});
			} else {
				$.ligerDialog.err('出错信息', "查询组织人员失败", obj.getMessage());
			}

		});
	};
	function addClick() {
		chb = $(":checkbox[class='pk']:checked");
		if (chb.length == 0) {
			$.ligerDialog.warn("至少要选择一个岗位!", '提示信息')
			return;
		}
		if (chb.length >= 2) {
			$.ligerDialog.warn("最多只能选择一个岗位!", '提示信息')
			return;
		}
		
		if(${action eq 'global'}){
			UserDialog({callback:dlgCallBack,isSingle:false});
		}else{
			GradeUserDialog({callback:dlgCallBack,isSingle:true}); 
		}

	};
	
	
</script>
</head>
<body>
	<div class="panel">
	 
		<c:choose>
			<%--全局的 global,分级授权grade--%>
			<c:when test="${action=='global' }">
		  		<f:tab curTab="pos" tabName="sysOrg%"/>
			</c:when>
			<c:otherwise>
		  		<f:tab curTab="pos" tabName="sysOrgGrade%"/>
			</c:otherwise>
		</c:choose>

		<c:choose>
			<c:when test="${orgId==0}">
				<div style="text-align: center; margin-top: 10%;">尚未指定具体组织!</div>
			</c:when>
			<c:otherwise>
				<div class="panel-top">
					<div class="tbar-title">
						<span class="tbar-label">系统岗位</span>
					</div>
					<div class="panel-toolbar">
						<div class="toolBar">
							<div class="group">
								<a class="link search" id="btnSearch">查询</a>
							</div>
							<c:choose>
								<c:when test="${action=='global'}">
									
									<div class="group">
										<a class="link add" href="${ctx}/oa/system/position/edit.do?orgId=${orgId}&action=add&topOrgId=${param.topOrgId}&authId=${param.authId}"> 增加岗位
										</a>
									</div>
									
									<div class="group" id="addUser">
										<a class="link add" href="javascript:;" onclick="addClick()">岗位加入人员</a>
								    </div>
								    
									<div class="group">
										<a class="link update" id="btnUpd" action="edit.do?orgId=${orgId}&action=add&topOrgId=${param.topOrgId}&authId=${param.authId}">修改</a>
									</div>
									
									<div class="group" id="del">
										<a class="link del" action="del.do">删除</a>
									</div>
								</c:when>
								<c:otherwise>
									<c:if test="${not empty orgAuth && fn:contains(orgAuth.posPerms, 'add')}">
										
										<div class="group">
											<a class="link add" href="${ctx}/oa/system/position/edit.do?orgId=${orgId}&action=add&authId=${param.authId}"> 增加岗位
											</a>
										</div>
									</c:if>
									<c:if test="${not empty orgAuth && fn:contains(orgAuth.userPerms, 'add')}">
										
										<div class="group" id="addUser">
											<a class="link add" href="javascript:;" onclick="addClick()">岗位加入人员</a>
									    </div>
									</c:if>
									<c:if test="${not empty orgAuth && fn:contains(orgAuth.posPerms, 'edit')}">
										
										<div class="group">
											<a class="link update" id="btnUpd" action="edit.do?orgId=${orgId}&action=add&authId=${param.authId}">修改</a>
										</div>
									</c:if>
									<c:if test="${not empty orgAuth && fn:contains(orgAuth.posPerms, 'del')}">
										
										<div class="group" id="del">
											<a class="link del" action="del.do">删除</a>
										</div>
									</c:if>
								</c:otherwise>
							</c:choose>
							
							
							
							<div class="group">
							    <a class="link reset" onclick="$.clearQueryForm()">重置</a>
							</div>
						</div>
					</div>
					<div class="panel-search">
						<form id="searchForm" method="post" action="list.do?orgId=${orgId}">
							<div class="row">
								<span class="label">岗位名称:</span><input type="text" name="Q_posName_SL" class="inputText" value="${param['Q_posName_SL']}" />
								 <span class="label">岗位描述:</span><input type="text" name="Q_posDesc_SL" class="inputText" value="${param['Q_posDesc_SL']}" />
							</div>
						</form>
					</div>
				</div>
				<div class="panel-body">
					<c:set var="checkAll">
						<input type="checkbox" id="chkall" />
					</c:set>
					<display:table name="positionList" id="positionItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
						<display:column title="${checkAll}" media="html" style="width:30px;">
							<input type="checkbox" class="pk" name="posId" value="${positionItem.posId}">
						</display:column>
						<display:column property="posName" title="岗位名称" sortable="true" sortName="POSNAME"></display:column>
						<display:column property="posCode" title="岗位代码" sortable="true" sortName="POSCODE"></display:column>
						<display:column property="orgName" title="组织名称" sortable="true" sortName="ORGID"></display:column>
						<display:column property="jobName" title="职务名称" sortable="true" sortName="JOBID"></display:column>
						<display:column property="userNames" title="岗位下用户"></display:column>
						<display:column title="管理" media="html" style="width:220px">
							<c:choose>
								<c:when test="${action=='global'}">
								  <a href="del.do?posId=${positionItem.posId}" class="link del">删除</a>
								  <a href="edit.do?posId=${positionItem.posId}&orgId=${orgId}&authId=${param.authId}" class="link edit">编辑</a>
								</c:when>
								<c:otherwise>
									<c:if test="${not empty orgAuth && fn:contains(orgAuth.posPerms, 'del')}">
										<a href="del.do?posId=${positionItem.posId}" class="link del">删除</a>
									</c:if>
									<c:if test="${not empty orgAuth && fn:contains(orgAuth.posPerms, 'edit')}">
										<a href="edit.do?posId=${positionItem.posId}&orgId=${orgId}&authId=${param.authId}" class="link edit">编辑</a>
									</c:if>
								</c:otherwise>
							</c:choose>
							<a href="get.do?posId=${positionItem.posId}&orgId=${orgId}&authId=${param.authId}" class="link detail">明细</a>
						</display:column>
					</display:table>
					<ibms:paging tableId="positionItem" />
				</div>
				<!-- end of panel-body -->
			</c:otherwise>
		</c:choose>
	</div>
	<!-- end of panel -->
</body>
</html>

