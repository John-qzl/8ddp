<%--
   用户表明细
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户表明细</title>
<%@include file="/commons/include/get.jsp"%>
<script type="text/javascript"
	src="${ctx }/jslib/lg/plugins/ligerTab.js"></script>
<script type="text/javascript">
	/*KILLDIALOG*/
	var dialog = null;
	try {
		dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	} catch (e) {

	}
	$(function() {
		var h = $('body').height();
		$("#tabMyInfo").ligerTab({
		//	height : h - 60
		});
	});
</script>
</head>
<body>


	<div class="panel-toolbar" id="pToolbar">
		<div class="toolBar">
			<c:if test="${canReturn==0 && openType!='detail'}">
				<div class="group">
					<a class="link back" href="${returnUrl}">返回</a>
				</div>
			</c:if>
			<c:if test="${openType=='detail'}">
				<div class="group">
					<a class="link close" href="javascript:;" onclick="dialog.close();">关闭</a>
				</div>
			</c:if>
			<c:if test="${canReturn==1 && !isRight}">
				<div class="group">
					<a class="link editdata"
						href="${ctx}/oa/system/sysUser/editCommon.do?userId=${sysUser.userId}">修改信息</a>
				</div>
			</c:if>
		</div>
	</div>


	<div id="tabMyInfo" style="overflow: hidden; position: relative;">

		<div title="基本信息" tabid="userdetail">
			<div class="panel-detail">
				<table class="table-detail" cellpadding="0" cellspacing="0"
					border="0">
					<tr>
						<td rowspan="10" align="center" width="26%">
							<div class="person_pic_div">
								<p>
									<img src="${ctx}/${pictureLoad}" alt="个人相片"
										style="width: 100%; height: 480px" />
								</p>
							</div>
						</td>
						<th width="18%" style="height: 48px !important">帐 号:</th>
						<td>${sysUser.username}</td>
					</tr>
					<tr>
						<th style="height: 48px !important">用户姓名:</th>
						<td>${sysUser.fullname}</td>
					</tr>

					<tr>
						<th style="height: 48px !important">用户性别:</th>
						<td><c:choose>
								<c:when test="${sysUser.sex==1}">
									         男
								   	</c:when>
								<c:otherwise>
								                      女
								   	</c:otherwise>
							</c:choose></td>
					</tr>
					<tr>
						<th style="height: 48px !important">当前状态:</th>
						<td><c:choose>
								<c:when test="${sysUser.status==1}">
										激活
								   	</c:when>
								<c:when test="${sysUser.status==0}">
										禁用
								   	</c:when>
								<c:otherwise>
								                        删除
								   	</c:otherwise>
							</c:choose></td>
					</tr>

					<tr>
						<th style="height: 48px !important">入职时间:</th>
						<td>${f:shortDate(sysUser.accessionTime)}</td>
					</tr>

					<tr>
						<th style="height: 48px !important">邮箱地址:</th>
						<td>${sysUser.email}</td>
					</tr>

					<tr>
						<th style="height: 48px !important">手 机:</th>
						<td>${sysUser.mobile}</td>
					</tr>

					<tr>
						<th style="height: 48px !important">电 话:</th>
						<td>${sysUser.phone}</td>
					</tr>

					<tr>
						<th style="height: 48px !important">技术职称:</th>
						<td><c:choose>
								<c:when test="${sysUser.skilltitle=='zlgcs'}">
										助理工程师
								   	</c:when>
								<c:when test="${skilltitle=='gcs'}">
										工程师
								   	</c:when>
								<c:when test="${skilltitle=='gjgcsf'}">
										高级工程师（副高）
								   	</c:when>
								<c:when test="${skilltitle=='gjgcsz'}">
										高级工程师（正高）
								   	</c:when>
								<c:when test="${skilltitle=='yjy'}">
										研究员
								   	</c:when>
								<c:otherwise>
								                       助理研究员
								   	</c:otherwise>

							</c:choose></td>
					</tr>

					<tr>
						<th style="height: 48px !important">专 业:</th>
						<td>${sysUser.major}</td>
					</tr>


				</table>
			</div>

		</div>
		<c:if test="${isOtherLink==0}">
			<div title="所属组织" tabid="orgdetail">
				<div
					style="overflow-y: auto; overflow-x: hidden; border: 0px solid #6F8DC6;">
					<div class="panel-data">
						<table id="orgItem" class="table-grid" cellpadding="1"
							cellspacing="1">
							<thead>
								<th style="width: 25%; text-align: center !important;">组织名称</th>
								<th style="width: 25%; text-align: center !important;">是否主组织</th>
								<th style="width: 50%; text-align: center !important;">主要负责人</th>
							</thead>
							<c:forEach items="${orgList}" var="orgItem" varStatus="status">
								<tr class="${status.index%2==0?'odd':'even'}">
									<td style="text-align: center;">${orgItem.orgName}</td>
									<td style="text-align: center;"><c:choose>
											<c:when test="${orgItem.isPrimary==1}">
											是
									   	</c:when>
											<c:otherwise>
									        否
									   	</c:otherwise>
										</c:choose></td>
									<!-- <td style="text-align: center;">${orgItem.chargeName}</td> -->
									<td style="text-align: center;"><c:choose>
											<c:when test="${orgItem.isCharge==1}">
												是
										   	</c:when>
											<c:otherwise>
										     否
										   	</c:otherwise>
										</c:choose></td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</div>

			</div>
			<div title="所属岗位" tabid="posdetail"
				icon="${ctx}/styles/default/images/nav-sales.png">
				<div
					style="overflow-y: auto; overflow-x: hidden; border: 0px solid #6F8DC6;">
					<div class="panel-data">
						<table id="posItem" class="table-grid" cellpadding="1"
							cellspacing="1">
							<thead>
								<th style="width: 25%; text-align: center !important;">岗位名称</th>
								<th style="width: 25%; text-align: center !important;">是否主岗位</th>

							</thead>
							<c:forEach items="${posList}" var="posItem" varStatus="status">
								<tr class="${status.index%2==0?'odd':'even'}">
									<td style="text-align: center;">${posItem.posName}</td>
									<td style="text-align: center;"><c:choose>
											<c:when test="${posItem.isPrimary==1}">
										是
								   	</c:when>
											<c:otherwise>
								         否
								   	</c:otherwise>
										</c:choose></td>

								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</div>
			<div title="所属角色" tabid="roldetail">
				<div
					style="overflow-y: auto; overflow-x: hidden; border: 0px solid #6F8DC6;">
					<div class="panel-data">
						<table id="rolItem" class="table-grid" cellpadding="1"
							cellspacing="1">
							<thead>
								<th style="width: 25%; text-align: center !important;">角色名称</th>
								<%--<th style="width: 25%; text-align: center !important;">子系统名称</th> --%>
							</thead>
							<c:forEach items="${roleList}" var="rolItem" varStatus="status">
								<tr class="${status.index%2==0?'odd':'even'}">
									<td style="text-align: center;">${rolItem.roleName}</td>
								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</div>

			<%-- <div title="所属组织角色" tabid="orgRoldetail">
			<div
				style="overflow-y: auto; overflow-x: hidden; border: 0px solid #6F8DC6;">
				<div class="panel-data">
					<table id="rolItem" class="table-grid" cellpadding="1"
						cellspacing="1">
						<thead>
							<th style="width: 25%; text-align: center !important;">组织</th>
							<th style="width: 25%; text-align: center !important;">角色</th>
						</thead>
						<c:forEach items="${sysOrgRoles}" var="sysOrgRole">
				<tr>
					<td style="text-align: center;">
					${sysOrgRole.key.orgName}
					</td>
					<td style="text-align: center;">
					<c:forEach items="${sysOrgRole.value}" var="sysRole">
						${sysRole.roleName}
					</c:forEach>
					</td>
				</tr>
				</c:forEach>
					</table>
				</div>
			</div>
		</div> --%>

			<%--		<div title="参数属性" tabid="params">--%>
			<%--			<div--%>
			<%--				style="overflow-y: auto; overflow-x: hidden; border: 0px solid #6F8DC6;">--%>
			<%--				<div class="panel-detail">--%>
			<%--					<table id="paramItem" class="table-grid" cellpadding="1"--%>
			<%--						cellspacing="1">--%>
			<%--						<thead>--%>
			<%--							<th style="width: 25%; text-align: center !important;">参数名</th>--%>
			<%--							<th style="width: 25%; text-align: center !important;">参数值</th>--%>
			<%--						</thead>--%>
			<%--						<c:forEach items="${userParamList}" var="para" varStatus="status">--%>
			<%--							<tr class="${status.index%2==0?'odd':'even'}">--%>
			<%--								<td style="text-align: center;">${para.paramName}</td>--%>
			<%--								<td style="text-align: center;">${para.paramValue}</td>--%>
			<%--							</tr>--%>
			<%--						</c:forEach>--%>
			<%--					</table>--%>
			<%--				</div>--%>
			<%--			</div>--%>
			<%--		</div>--%>
		</c:if>

		<div title="签章模型" tabid="signmodel">
			<table class="table-detail" cellpadding="0" cellspacing="0"
				border="0">
				<tr>

					<div class="person_pic_div">
						<p>
							<img id="signModelPath" width="700" height="400" src="${ctx}/${signModelPath}" alt="电子签章" />
						</p>
					</div>
					</td>
				</tr>
			</table>
		</div>
	</div>
</body>
</html>
