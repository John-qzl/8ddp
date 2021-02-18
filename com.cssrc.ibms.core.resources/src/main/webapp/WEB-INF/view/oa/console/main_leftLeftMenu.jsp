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
<c:choose>
	<c:when test="${msgCount<=9}">
		<c:set var="msgCountInfo" value="${msgCount}" />
		<c:set var="msgCountFF" value="serif" />
		<c:set var="msgLeft" value="7px" />
	</c:when>
	<c:when test="${msgCount<=99}">
		<c:set var="msgCountInfo" value="${msgCount}" />
		<c:set var="msgCountFF" value="serif" />
		<c:set var="msgLeft" value="4px" />
	</c:when>
	<c:otherwise>
		<c:set var="msgCountInfo" value="\e804" />
		<c:set var="msgCountFF" value="iconfont" />
		<c:set var="msgLeft" value="4px" />
	</c:otherwise>			
</c:choose>
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
	<f:link href="css/jquery.qtip.css" ></f:link>
	<%@include file="/commons/include/color.jsp"%>
	<style type="text/css">
		.viewFramework-sidebar{
			top: 0px;
		}
		.viewFramework-topbar{
			height: auto;
			line-height: normal;
			padding: 5px;
		}
		.viewFramework-logo{
			font-size: 16px;
			padding: 5px 0px 5px 0px;
		}
		.v-t-menu-arrow:before{
			font-family: "iconfont";
			content: "\e6a7";
		}
		li.v-t-menu{
			padding: 0 5px;
			line-height: 50px;
		}
		li.v-t-menu:hover {
		    background: none;
		    text-decoration: none;
		    box-sizing: initial;
		    border-bottom: 0;
	    }
	    li.v-t-menu:hover .v-t-menu-arrow:before {
		  font-family: "iconfont";
		  content: "\e697";
		}
		.v-t-menu-drop{
			position: fixed;
		    left: 218px;
		    margin-top: 75px;
		    width: auto;
		}
		.viewFramework-sidebar-bottom{
			top: 0;
			left: 43px;
			height: 37px;
	    }
		.viewFramework-sidebar-main{
			width: 220px;
			margin-top: 10px;
		}
		.btn{
			border: 0;
			background: #CBD9ED;
			padding: 3px 12px;
		}
		.viewFramework-product{
			padding: 0px;
		}
		.l-tab-links ul{
			margin-left: 43px;
		}
		.l-tab-links-left{
			left: 40px;
		}	
		.viewFramework-sidebar-toggle{
			top: 0px;
	    	left: 220px;
	    	height: 30px;
	    	background-color: #CBD9ED;
		}
	
		.v-t-m-i-03 span.dot-red{
			position: relative;
		    top: -7px;
		    left: -15px;
		    width: 20px;
		    height: 20px;
		}
		.v-t-m-i-03 span.dot-red:before{
		    content: '${msgCountInfo}';
		    position: absolute;
		    top: -15px;
		    left: ${msgLeft};
		    font-size: 12px;
			font-family: "${msgCountFF}";
		}
		.v-t-m-i-03:before {
		    content: inherit;
		}
	</style>
	<!--[if lte IE 8]>
		<style type="text/css">
			.viewFramework-sidebar-bottom{
				top: 0;
				left: 45px;
				height: 40px;
				z-index: 102;
		    }
		</style>
	<![endif]-->
	<!--[if !IE]><!-->
		<style type="text/css">
			.viewFramework-sidebar {
				z-index: 102;
			}
		</style>
	<!--<![endif]-->
</head>
<body class="viewFramework-body viewFramework-sidebar-full" > <!--viewFramework-sidebar-mini-->
	<div id="layoutMain" style="margin: 0px;top:0px;">		
		<div class="viewFramework-sidebar-toggle">
			<div class="clearFix">
				<div class="btn fr v-s-b-sq"><a class="link menu" href="#" id="toggleSidebar"></a></div>
			</div>
		</div>	
		<div class="viewFramework-sidebar" role="navigation">
			<div class="viewFramework-topbar clearFix">
				<div style="text-align:center;">
					<div class="viewFramework-logo">						
						<c:choose>
							<c:when test="${empty currentSystem.systemUrl}">
								<a href="${ctx}/oa/console/main.do"><img id="logoImg" src="${f:getLogo(currentSystem.systemLog)}" /> </a>
							</c:when>
							<c:otherwise>
								<a href="${currentSystem.systemUrl}"><img id="logoImg" src="${f:getLogo(currentSystem.systemLog)}" /> </a>
							</c:otherwise>
						</c:choose>		
					</div>
					<div class="viewFramework-logo" style="height:45px;">
						<font class="toggle" style="font-size: 12px;">${currentSystem.companyName}</font></br>
						<font class="toggle" style="padding: 5px;">${currentSystem.systemName}</font>
					</div>
				</div>			
			</div>
			<div class="viewFramework-user">
					<ul>
						<li class="v-t-menu" >
							<c:choose>
								<c:when test="${empty curUser.photo}">
									<font class="v-t-m-i-04"></font>
								</c:when>
								<c:otherwise>
									<img id="userImg" style="width:30px;height:30px;margin-top:-2px;" src="${ctx}/${curUser.photo}" />
								</c:otherwise>
							</c:choose>		
						</li>
						<li class="v-t-menu toggle">
							<font><security:authentication property="principal.fullname" /></font>
						</li>
						<!-- <li class="v-t-menu" href="##">
							<font><a class="t-menu v-t-m-i-02">设置</a></font>
						</li> -->
						
						<li class="v-t-menu fr toggle">
							<div class="v-t-menu-arrow">
								<%-- <i class="base-tool-arrow"></i> --%>
							</div>
							<div class="v-t-menu-drop">
								<a href="javascript:show('/oa/system/sysUser/modifyPwdView.do?userId=${userId}','修改密码')">修改密码</a>
								<a href="javascript:show('/oa/system/sysUser/get.do?userId=${userId}&canReturn=1','个人资料')" >个人资料</a>
								<a href="${ctx}/logout">退出</a>
							</div>
						</li>
						<li class="v-t-menu fr toggle">
								<font>
									<a id="msg" class="t-menu v-t-m-i-03 <c:if test='${msgCount>0}'>active</c:if>" href="javascript:addToTab('${ctx}/oa/system/messageReceiver/list.do','收到的消息')">
										<c:choose>
											<c:when test="${msgCount=='0'}">
												<span style="color:#fff;font-weight:bold">(${msgCountInfo})</span>
											</c:when>
											<c:otherwise>
												<span id="countMsg" style="color:red;font-weight:bold">(${msgCountInfo})</span>
											</c:otherwise>			
										</c:choose>
									</a>
								</font>
						</li>
					</ul>
			</div>	
			<div id="viewFrameworkSidebar" class="viewFramework-sidebar-main">
				<ol class="viewFramework-sidebar-ol" id="sidebar_tree"></ol>
			</div>
		</div>
			
		<div class="viewFramework-product" id="framecenter" >
			<div tabid="home" title="我的主页" icon="icon-zhuye">
				<iframe src="" name="home" id="home"  class="viewFramework-mainFrame" border="0" vspace="0" hspace="0" marginwidth="0" marginheight="0" framespacing="0" frameborder="0" ></iframe>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
		if (top != this) {
			//当这个窗口出现在iframe里，表示其目前已经timeout，需要把外面的框架窗口也重定向登录页面
			top.location = '<%=request.getContextPath()%>/oa/console/main.do';
		}
		var ctxPath = "<%=request.getContextPath()%>";
		var isWarning = '${isWarning}';
		var warningMsg = '${warningMsg}';
        top['index']=window;
	</script>
	<f:js pre="jslib/lang/common"></f:js>
	<f:js pre="jslib/lang/js"></f:js>
	<script type="text/javascript" src="${ctx}/jslib/util/json2.js"></script>
	<script type="text/javascript" src="${ctx}/js/framework/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/js/framework/jquery.ztree.js"></script>
	<script type="text/javascript" src="${ctx}/js/framework/jquery.mousewheel.js"></script>
	<script type="text/javascript" src="${ctx}/js/framework/jquery.rollbar.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/framework/base.js"></script>
	<%-- <script type="text/javascript" src="${ctx}/js/framework/ligerMenu.js"></script>
	<script type="text/javascript" src="${ctx}/js/framework/ligerTab.js"></script> --%>
	<script type="text/javascript" src="${ctx}/js/framework/framework.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/util/util.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/ligerui.all.js"></script>
	<script type="text/javascript" src="${ctx}/js/portal/share.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.qtip.js" ></script>
	
	<script type="text/javascript">
		$(function(){
			//加载菜单树
			loadTree();
			//初始化菜单
			initLeftMenu();
			//初始话tab页
			initTab();    
		    //加载门户
		    $('#home').attr("src",ctxPath+"/oa/portal/insPortal/show.do?key=personal");
			//initHeight();
		});
		function show(url,subject){
			top['index']._OpenWindow({
					url : ctxPath+url,
					title : subject,
					width : 1100,
					height : 700
			});		
		}
		/* function initHeight(){
			var top = $('#userImg').offset().top;
			var drop_ = $('.v-t-menu-drop',$('#layoutMain'));
			drop_.css({'top':top});
		} */
	</script>
</body>
</html>
