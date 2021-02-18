<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>

	<head>
		<title>表单角色表管理</title>
		<%@include file="/commons/include/get.jsp" %>
		<script type="text/javascript" src="${ctx }/jslib/ibms/oa/system/SysDialog.js"></script>
<f:link href="from-jsp.css"></f:link>		<script type="text/javascript">
			var dataId = '<%=session.getAttribute("dataId") %>';
			var dataTemplateId = '<%=session.getAttribute("dataTemplateId") %>';
			var typeAlias = '<%=session.getAttribute("typeAlias") %>';
		</script>
		<script type="text/javascript">
			$(function() {
				handleClick();
				
				
				//默认click事件解绑
				$("div.group > a.link.del").unbind("click");
				//单击删除超链接的事件处理
				$("div.group > a.link.del").click(function() {
					var str = '确认删除吗？';
					var action = $(this).attr("action");
					var $aryId = $("input[type='checkbox'][disabled!='disabled'][class='pk']:checked");

					if($aryId.length == 0) {
						$.ligerDialog.warn("请选择记录！");
						return false;
					}
					//提交到后台服务器进行日志删除批处理的日志编号字符串
					var delId = "";
					var keyName = "";
					var len = $aryId.length;
					var canDelInfo = "";
					$aryId.each(function(i) {
						var obj = $(this);
						var canDel = obj.attr("allowDel") == "0" ? false : true;
						var rolename = obj.parent().text().trim();
						if(!canDel) {
							canDelInfo += rolename + ",";
						}
						if(i < len - 1) {
							delId += obj.val() + ",";
						} else {
							keyName = obj.attr("name");
							delId += obj.val();
						}
					});
					if(canDelInfo.indexOf(",") > -1) {
						canDelInfo = canDelInfo.substring(0, canDelInfo.length - 1);
						$.ligerDialog.warn("以下角色不能删除：" + canDelInfo);
						return false;
					}
					var url = action + "?" + keyName + "=" + delId;
					$.ligerDialog.confirm(str, '提示信息', function(rtn) {
						if(rtn) {
							var form = new com.ibms.form.Form();
							form.creatForm("form", action);
							form.addFormEl(keyName, delId);
							//by weilei:批量删除数据时，由于后台接收参数中无keyName，所以指定删除参数名为__pk__
							form.addFormEl("__pk__", delId);
							form.submit();
						}
					});
					return false;

				});
			});

			function handleClick() {
				//人员选择器
				$("a[name=addUser]").click(function() {
					var roleSonId;
					var $aryId = $("input[type='checkbox'][disabled!='disabled'][class='pk'][id='role']:checked");
					if($aryId.length != 1) {
						$.ligerDialog.warn("请选择一个角色！");
						return false;
					}
					roleSonId = $aryId.val();
					UserDialog({
						callback: userCallBack,
						isSingle: false
					});
				});
				$("a.setting").click(function() {
					$.ligerDialog.confirm("是否进行更新？", "提示信息", function(rtn) {
						if(rtn) {
							var url = "${ctx}/oa/system/recRoleSon/roleSync.do?";
							window.location.href = url;
						}
					});
				});
			}
			//资源分配
			function editRoleRes() {
				var roleSonId;
				var $aryId = $("input[type='checkbox'][disabled!='disabled'][class='pk'][id='role']:checked");
				if($aryId.length != 1) {
					$.ligerDialog.warn("请选择一个角色！");
					return false;
				}
				roleSonId = $aryId.val();
				var url = __ctx + "/oa/system/recRoleSonFun/edit.do?roleSonId=" + roleSonId + "&typeId=${typeId}";
				var winArgs = "dialogWidth=600px;dialogHeight=460px;status=0;help=0;";
				url = url.getNewUrl();
				DialogUtil.open({
					height: 500,
					width: 600,
					title: '功能点分配',
					url: url,
					isResize: true
				});
			}
			//用户回调函数
			function userCallBack(userIds, fullnames) {
				var roleSonId = $("input[type='checkbox'][disabled!='disabled'][class='pk'][id='role']:checked").val();
				if(userIds.length > 0) {
					var form = new com.ibms.form.Form();
					form.creatForm("form", "${ctx}/oa/system/recRoleSon/addUser.do");
					form.addFormEl("userIds", userIds);
					form.addFormEl("roleSonId", roleSonId);
					form.submit();
				}
			};
			//checkBox处理
			function handlerCheckAll(obj) {
				var state = $(obj).attr("checked");
				if(state == undefined)
					checkAll(false, obj);
				else
					checkAll(true, obj);
			}

			function checkAll(checked, obj) {
				$(obj).parents('table').find("input#user[type='checkbox']").each(function() {
					$(this).attr("checked", checked);
				});
			}
			
		</script>
	</head>

	<body>
		<div class="panel">
			<div class="panel-top">
				<div class="tbar-title">
					<span class="tbar-label">记录角色管理列表</span>
				</div>
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group">
							<f:a alias="addRole" name="addRole" css="link add" href="${ctx}/oa/system/recRoleSon/edit.do?">添加角色</f:a>
						</div>
						<div class="group">
							<f:a alias="delRole" name="delRole" css="link del" action="del.do?">移除角色</f:a>
						</div>
						
						<div class="group">
							<f:a alias="addUser" name="addUser" css="link add" href="####">新增用户</f:a>
						</div>
						<div class="group">
							<f:a alias="roleRes" css="link add" href="####" onclick="editRoleRes()">功能点分配</f:a>
						</div>
						
						<div class="group">
							<f:a alias="initRole" css="link setting">更新</f:a>
						</div>
					</div>
				</div>
			</div>
			<div class="panel-body" style="overflow: hidden;">
				<div id="roleAndUser" style="margin-bottom:4px;background:#f0f1f1;height: 100%;">
					<div class="panel-body-thead" style="width: 100%;height: 42px;overflow: hidden;">
						<table id="roleUserItem" class="table-grid">
							<thead>
								<tr>
									<th width="5%"><input type="checkbox" class="pk" onclick="handlerCheckAll(this)" name="user" id="chkall_user"></th>
									<th width="20%">用户名称</th>
									<!-- <th>帐号</th> -->
									<th width="20%">所属组织</th>
									<th width="20%">电话</th>
									<th width="30%">手机</th>
									<th width="120px">管理</th>
								</tr>
							</thead>
						</table>
					</div>
					<div class="panel-body-tbody" style="width: 100%;overflow: auto;">
						<c:forEach items="${recRoleSonList}" var="roleSon">
							<c:if test="${roleSon.isHide!=1}">
								<c:set var="userList" value="${roleSon.userList}" />
								<table id="roleUserItem" class="table-grid">
									<c:if test="${roleSon.allowDel==0}">
										<caption style="text-align:left;height:30px;background-color:#ffffff;border-style:none">
											<font size="3">
												<input style="margin-left:1.56%" allowDel="${roleSon.allowDel}" type="checkbox" class="pk" id="role" name="roleSonId" value="${roleSon.roleSonId}" /> 角色(不可删除)：【${roleSon.roleSonName}】
											</font>
										</caption>
									</c:if>
									<c:if test="${roleSon.allowDel!=0}">
										<caption style="text-align:left;height:30px;background-color:#ffffff;border-style:none">
											<font size="3">
												<%-- <c:if test="${roleSon.allowDel==0}"><input style="margin-left:1.56%" type="checkbox" class="disabled" id="role" name="roleSonId" value="${roleSon.roleSonId}" disabled="disabled"></c:if>
								<c:if test="${roleSon.allowDel==1}"><input style="margin-left:1.56%" type="checkbox" class="pk" id="role" name="roleSonId" value="${roleSon.roleSonId}"></c:if> --%>
												<input style="margin-left:1.56%" allowDel="${roleSon.allowDel}" type="checkbox" class="pk" id="role" name="roleSonId" value="${roleSon.roleSonId}" /> 角色：【${roleSon.roleSonName}】
											</font>
										</caption>
									</c:if>
									<tbody>
										<c:forEach items="${userList}" var="user">
											<tr style="background: #fff;">
												<td style="text-align:center;width: 5%;"><input type="checkbox" class="pk" name="user" id="user" value="${user.userId}"></td>
												<td style="text-align:center;width:20%">${user.fullname}</td>
												<%-- <td style="text-align:center;">${user.username}</td> --%>
												<td style="text-align:center;width:20%">${user.orgName}</td>
												<td style="text-align:center;width:20%">${user.phone}</td>
												<td style="text-align:center;width:30%">${user.mobile}</td>
												<td style="text-align:center;width:120px">
													<a class="link del" href="${ctx}/oa/system/recRoleSon/delUser.do?roleSonId=${roleSon.roleSonId}&__pk__=${user.userId}"></a>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</c:if>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
	</body>

</html>