<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%><!-- 定义了浏览器的文本模式（防止IE8进入杂项） -->
<html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
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
	<f:link href="iconfont.css"></f:link>
	<f:link href="iconImg.css"></f:link>
	<f:link href="frame/frame.css"></f:link>
	<%@include file="/commons/include/color.jsp"%>
	
	<style type="text/css">
		.l-tab-content{
			padding-top: 5px;
		}
		.menu-icon{
        	margin-right: 5px;
        }
        .viewFramework-topbar{
  			height: 70px;
        }
        .viewFramework-sidebar{
    		top: 70px;
        }
		.top-right img,
		.top-left img,
		.top-center img,
		.top-right-s img,
		.top-right-t img,
		.top-left-s img,
		.top-left-t img{
  			height: 70px;
		}
	</style>
	
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
		    debugger;
			//加载菜单树
			loadTree();
			//初始化菜单
			initLeftMenu();
			//初始话tab页
			initTab();    
		    //加载门户
		    $('#home').attr("src",ctxPath+"/oa/portal/insPortal/show.do?key=personal");
		});
	</script>
</head>
<body class="viewFramework-body viewFramework-sidebar-full" > <!--viewFramework-sidebar-mini-->
	<%@include file="main_top.jsp"%>
	<div id="layoutMain" style="margin: 0px;top:0px;">
		<div class="viewFramework-sidebar" role="navigation">
			<div id="viewFrameworkSidebar" class="viewFramework-sidebar-main">
				<ol class="viewFramework-sidebar-ol" id="sidebar_tree"></ol>
			</div>
			<div class="viewFramework-sidebar-bottom">
				<div class="clearFix">
					<!-- <div class="btn fl v-s-b-setting"><a class="link set" href="#"></a></div>
					<div class="btn fl v-s-b-skin"><a class="link skin" href="#"></a></div> -->
					<div class="btn fr v-s-b-sq"><a class="link menu" href="#" id="toggleSidebar"></a></div>
					
					<!-- <button class="btn fl v-s-b-setting" title="设置"></button>
					<button class="btn fl v-s-b-skin" title="更换皮肤"></button>
					<button class="btn fr v-s-b-sq" title="收缩菜单" id="toggleSidebar"></button> -->
				</div>
			</div>
		</div>
			
		<div class="viewFramework-product" id="framecenter" >
			<div tabid="home" title="我的主页" icon="icon-mainpage">
				<iframe src="" name="home" id="home"  class="viewFramework-mainFrame" border="0" vspace="0" hspace="0" marginwidth="0" marginheight="0" framespacing="0" frameborder="0" ></iframe>
			</div>
		</div>
	</div>
</body>
</html>
