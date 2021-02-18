<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%><!-- 定义了浏览器的文本模式（防止IE8进入杂项） -->
<html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<head>
	<title>${currentSystem.companyName}${currentSystem.systemName}</title>
	<!-- 标题栏logo -->
	<link rel="shortcut icon" href="${ctx}/favicon.ico" type="image/x-icon" />
	
	<link rel="stylesheet" type="text/css" href="${ctx}/jslib/tree/zTreeStyle.css"></link>
	<f:link href="Aqua/css/ligerui-common.css"></f:link>
	<f:link href="Aqua/css/ligerui-layout.css"></f:link>
	<f:link href="Aqua/css/ligerui-dialog.css"></f:link>
	<f:link href="Aqua/css/ligerui-tab.css"></f:link>
	<f:link href="Aqua/css/ligerui-menu.css"></f:link>
	<f:link href="frame/frame.css"></f:link>
	<!-- 根据屏幕分辨率调整css -->
	<link rel="stylesheet" id="screenLink">
	<%@include file="/commons/include/color.jsp"%>
	
    <style>
    	.viewFramework-topMenubar-inner{
			width: 100%;
		    left: 0;    
		    top: 6px;
		    padding: 0 10px;
		    display: inline-block;
		    vertical-align: middle;
		    box-sizing: border-box;
		    text-align: center;
		    position: absolute;
    	}
		.viewFramework-topbar-inner {
			width: auto;
		    padding: 0 5px;
		    float: right;
		    top: 0;
		    right: 25px;
		    position: absolute;
		    height: 71px;
		}
    	.viewFramework-sidebar{
    		top: 70px;
    		z-index: 1000;
		    border-radius: 0 0 3px 3px;
		    -webkit-box-shadow: 3px 3px 4px rgba(0, 0, 0, 0.13);
		    -moz-box-shadow: 3px 3px 4px rgba(0, 0, 0, 0.13);
		    box-shadow: 3px 3px 4px rgba(0, 0, 0, 0.13);
		    box-sizing: border-box;
    	}
        .viewFramework-sidebar-linkHd, .viewFramework-sidebar-li>a.level0{
        	background:none;
        }
    </style>
</head>

<body class="viewFramework-body viewFramework-sidebar-full" >
    <div id="mainFrames" style="height:100%;">
    	<div class="viewFramework-topbar clearFix" role="heading">
    		<div class="top-left fl"><img src="${ctx}/styles/images/top/cmes/l.png" /></div>
    		<div class="top-right fr"><img src="${ctx}/styles/images/top/cmes/r.png" /></div>
    		<div class="top-right-s fr">
    			<img src="${ctx}/styles/images/top/cmes/middle.png" />
    		</div>
			<div class="viewFramework-hd">
				<div class="viewFramework-logo">
					<img id="logoImg" src="${ctx}/styles/images/top/cmes/logo2.png" />
					<%-- <img id="logoImg" src="${f:getLogo(currentSystem.systemLog)}" />  --%>
					<%-- <font>${currentSystem.companyName}${currentSystem.systemName}</font> --%>
				</div>
			</div>
			<div class="viewFramework-topMenubar-inner">
				<ul id="sidemenu">
					<%-- <li class="top-menu active" >
						<c:choose>
							<c:when test="${empty currentSystem.systemUrl}">
								<a href="${ctx}/oa/console/main.do">
									<div class="top-menu-icon"><span class="icon-mainpage"></span></div>
									<div class="top-menu-font">首页</div>
								</a>
							</c:when>
							<c:otherwise>
								<a href="${currentSystem.systemUrl}">
									<div class="top-menu-icon"><span class="icon-mainpage"></span></div>
									<div class="top-menu-font">首页</div>
								</a>
							</c:otherwise>
						</c:choose>
					</li> --%>
					<c:forEach items="${menuList}" var="menu" varStatus="cu">
						<li alias="${menu.alias}" class="top-menu <c:if test="${cu.count==1}">active</c:if>" >
							<a href="javascript:;" menuId="${menu.resId}" name="${menu.resName}" img="${menu.icon}" url="${menu.defaultUrl}" onclick="initSubtMenu(this,'${menu.resId}')">
								<div class="top-menu-icon"><span class="${menu.icon}"></span></div>
								<div class="top-menu-font">${menu.resName}</div>
							</a>
						</li>
					</c:forEach>
				</ul>
			</div>
			<div class="viewFramework-topbar-inner">
				<ul>				
					<li class="v-t-menu" >
						<font>
							<c:choose>
								<c:when test="${empty currentSystem.systemUrl}">
									<a href="${ctx}/oa/console/main.do" class="t-menu icon-mainpage">首页</a>
								</c:when>
								<c:otherwise>
									<a href="${currentSystem.systemUrl}" class="t-menu icon-mainpage">首页</a>
								</c:otherwise>
							</c:choose>
						</font>
					</li>
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
							<i class="base-tool-arrow"></i>
						</div>
						<div class="v-t-menu-drop">
							<a href="javascript:show('/oa/system/sysUser/modifyPwdView.do?userId=${userId}','修改密码')">修改密码</a>
							<a href="javascript:show('/oa/system/sysUser/get.do?userId=${userId}&canReturn=1','个人资料')" >个人资料</a>
							<a href="${ctx}/logout">退出</a>
						</div>
					</li>
				</ul>
			</div>			
		</div>
        <div id="layoutMain" style="margin: 0px;">
        	<div id="menuListnav" class="viewFramework-sidebar" role="navigation">      
				<div class="viewFramework-sidebar-bottom">
					<div class="clearFix">
						<div class="btn fr v-s-b-sq"><a class="link menu" href="#" id="toggleSidebar"></a></div>					
					</div>
				</div>
				<div id="viewFrameworkSidebar" class="viewFramework-sidebar-main">
					<ol class="viewFramework-sidebar-ol" id="sidebar_tree"></ol>
				</div>
			</div>
			<div class="viewFramework-product" id="framecenter" >
		        <div tabid="home" title="首页" icon="icon-mainpage">
					<iframe src="" name="home" id="home"  class="viewFramework-mainFrame" border="0" vspace="0" hspace="0" marginwidth="0" marginheight="0" framespacing="0" frameborder="0" ></iframe>
				</div>
			</div>  
        </div>
    </div>
    
    <script type="text/javascript">
		if (top != this) {
			//当这个窗口出现在iframe里，表示其目前已经timeout，需要把外面的框架窗口也重定向登录页面
			top.location = '<%=request.getContextPath()%>/oa/console/main.do';
		}
		var ctxPath = "<%=request.getContextPath()%>";
		var __ctx = ctxPath;
		var isWarning = '${isWarning}';
		var warningMsg = '${warningMsg}';
        top['index']=window;
        var menuList = ${menuList};
	</script>
	
	<f:js pre="jslib/lang/common"></f:js>
	<f:js pre="jslib/lang/js"></f:js>
	<script type="text/javascript" src="${ctx}/jslib/util/json2.js"></script>
	<script type="text/javascript" src="${ctx}/js/framework/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/js/screen/screen.js"></script>
	<script type="text/javascript" src="${ctx}/js/framework/jquery.ztree.js"></script>
	<script type="text/javascript" src="${ctx}/js/framework/jquery.mousewheel.js"></script>
	<script type="text/javascript" src="${ctx}/js/framework/jquery.rollbar.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/framework/base.js"></script>
	<script type="text/javascript" src="${ctx}/js/framework/framework.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/util/util.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/ligerui.all.js"></script>
	<script type="text/javascript" src="${ctx}/js/portal/share.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.qtip.js" ></script>
    <script type="text/javascript" src="${ctx}/js/framework/frameworkTopLeft.js"></script> 
	<script type="text/javascript">
		$(function(){
			//加载第一个一级菜单的子菜单（树）
			loadTree(menuList[0].resId);
			//初始化子菜单
			initLeftMenu('topLeft');
			//初始话tab页
			initTab();    
			//加载门户
		    $('#home').attr("src",ctxPath+"/oa/portal/insPortal/show.do?key=personal");
		});

		function show(url,subject){
			top['index']._OpenWindow({
					url : ctxPath+url,
					title : subject,
					width : 1100,
					height : 700
			});		
		}
	</script>
</body>
</html>
