<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>用户角色映射表管理</title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript" src="${ctx }/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript">
	function dlgCallBack(userIds, fullnames) {
		if (userIds.length > 0) {
			var form = new com.ibms.form.Form();
			form.creatForm("form", "${ctx}/oa/system/userRole/add.do");
			form.addFormEl("roleId", "${roleId}");
			form.addFormEl("userIds", userIds);
 	 		form.submit();
		}
	};

	function add() {
		var roleId=$.getParameter("roleId");
		UserDialog({
			callback : dlgCallBack,
			isSingle : false,
			dataType:"roleResFilter",
			dataId:roleId,
		});
	}
</script>
</head>
<body>
	<div class="panel">
		<%--<div class="hide-panel">--%>
			<div class="panel-top">
				<div class="tbar-title">
					<span class="tbar-label">用户角色映射表管理</span>
				</div>
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group"><a class="link search" id="btnSearch">查询</a></div>
						
						<div class="group">
							<a class="link add" href="javascript:add();">加入用户</a>
						</div>
						
						<div class="group">
							<a class="link del" action="del.do">删除</a>
						</div>
						
						<div class="group">
							<a class="link back"
								href="${ctx }/oa/system/sysRole/list.do">返回</a>
						</div>
					</div>
				</div>
				<div class="panel-search">
					<form id="searchForm" method="post" action="edit.do?roleId=${roleId}">
							<ul class="row plat-row">
								<li><span class="label">姓名:</span><input type="text" name="Q_fullname_SL"  class="inputText" value="${param['Q_fullname_SL']}"/></li>
								<li><span class="label">帐号:</span><input type="text" name="Q_username_SL"  class="inputText" value="${param['Q_username_SL']}"/>			</li>		
							</ul>
							
					</form>
				</div>
			</div>
		<%--</div>--%>
		<div class="panel-body">
			<c:set var="checkAll">
				<input type="checkbox" id="chkall" />
			</c:set>
			<display:table name="userRoleList" id="userRoleItem"
				requestURI="edit.do" sort="external" cellpadding="1"
				cellspacing="1" export="false" class="table-grid">
				<display:caption style="text-align:center;height:30px;background-color:#DDDDDD;color:#444444" ><font size="3" >对角色：【${roleName }】分配人员</font></display:caption>
				<display:column title="${checkAll}" media="html" style="width:30px;">
					<input type="checkbox" class="pk" name="userRoleId" value="${userRoleItem.userRoleId}">
				</display:column>

				<display:column property="fullname" title="用户名称" sortable="true" sortName="fullname"></display:column>
				<display:column property="username" title="帐号" sortable="true" sortName="username"></display:column>

				<display:column title="管理" media="html" style="width:180px">
					<a href="del.do?userRoleId=${userRoleItem.userRoleId}" class="link del">删除</a>
				</display:column>
			</display:table>
			<ibms:paging tableId="userRoleItem"/>
		</div>
		<!-- end of panel-body -->
	</div>
	<!-- end of panel -->
</body>
</html>


