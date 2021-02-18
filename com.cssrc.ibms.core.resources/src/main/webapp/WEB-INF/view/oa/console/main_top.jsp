<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<script type="text/javascript">
	function show(url,subject){
		top['index']._OpenWindow({
				url : ctxPath+url,
				title : subject,
				width : 1100,
				height : 700
		});		
	}
</script>
<div class="viewFramework-topbar clearFix" role="heading">
	<div class="viewFramework-hd">
		<div class="viewFramework-logo">
			<img id="logoImg" src="${f:getLogo(currentSystem.systemLog)}" /> 
			<font>${currentSystem.companyName}${currentSystem.systemName}</font>
			<a style="color:#fff;font-size: 14px;">${currentSystem.version}</a>
		</div>
	</div>
	<div class="viewFramework-topbar-inner">
		<ul>
			<li class="v-t-menu" >
				<font>
					<c:choose>
						<c:when test="${empty currentSystem.systemUrl}">
							<a class="t-menu v-t-m-i-01" href="${ctx}/oa/console/main.do">主页</a>
						</c:when>
						<c:otherwise>
							<a class="t-menu v-t-m-i-01" href="${currentSystem.systemUrl}">主页</a>
						</c:otherwise>
					</c:choose>
					</font>
			</li>
			
			<!-- <li class="v-t-menu" href="##">
				<font><a class="t-menu v-t-m-i-02">设置</a></font>
			</li> -->
			
			<li class="v-t-menu" >
					<font>
						<a id="msg" class="t-menu v-t-m-i-03" href="javascript:addToTab('${ctx}/oa/system/messageReceiver/list.do','收到的消息')">消息
							<c:choose>
								<c:when test="${msgCount=='0'}">
									<span style="color:#fff;font-weight:bold">(${msgCount})</span>
								</c:when>
								<c:otherwise>
									<span id="countMsg" style="color:red;font-weight:bold">(${msgCount})</span>
								</c:otherwise>

						</c:choose>
					</a>
				</font>
			</li>
			
			<li class="v-t-menu" href="javascript:;">
				<font class="v-t-m-i-04"><security:authentication property="principal.fullname" /></font>
				<div class="v-t-menu-arrow">
					<%-- <i class="base-tool-arrow"></i> --%>
				</div>
				<div class="v-t-menu-drop">
					<a href="javascript:show('/oa/system/sysUser/modifyPwdView.do?userId=${userId}','修改密码')">修改密码</a>
					<a href="javascript:show('/oa/system/sysUser/get.do?userId=${userId}&canReturn=1','个人资料')" >个人资料</a>
					<a href="${ctx}/logout">退出</a>
				</div>
			</li>
		</ul>
	</div>
	<div class="viewFramework-topMenubar-inner">
        <div class="right arrow-icon">
            <img src="${ctx}/styles/images/top/arrow-right.png" />
        </div>
        <ul id="sidemenu" class="sidemenu">
        </ul>
        <div class="left arrow-icon">
            <img src="${ctx}/styles/images/top/arrow-left.png" />
        </div>
    </div>
</div>

