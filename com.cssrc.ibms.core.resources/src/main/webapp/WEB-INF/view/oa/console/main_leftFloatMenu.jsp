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
	<%@include file="/commons/include/color.jsp"%>
	
	<script type="text/javascript">
		if (top != this) {
			//当这个窗口出现在iframe里，表示其目前已经timeout，需要把外面的框架窗口也重定向登录页面
			top.location = '<%=request.getContextPath()%>/oa/console/main.do';
		}
		var ctxPath = "<%=request.getContextPath()%>";
		var isWarning = '${isWarning}';
		var warningMsg = '${warningMsg}';
        top['index']=window;
        var menuList = ${menuList};
	</script>
	
    <style>
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
		.viewFramework-sidebar-toggle{
			top: 70px;
	    	height: 30px;
	    	background-color: #CBD9ED;
		}
		.btn{
			border: 0;
			background: #CBD9ED;
			padding: 3px 12px;
		}		
		.viewFramework-sidebar-full .viewFramework-sidebar,
		.viewFramework-sidebar.sidebar-full{
			width: 120px;
			display: block;
			z-index: 99999;
		}
		.viewFramework-sidebar-full .toggle {
			display: inline-block;
		}
		.viewFramework-sidebar-full .viewFramework-product {
			left: 120px;
		}
    	.viewFramework-product{
    		padding: 0;
    	} 
        .sidemenu li.firstmenu{
        	width: 100%;
        }
        .sidemenu li.firstmenu > p{
			padding: 15px 0 40px 0;
        }
        .menu-icon{
        	width: 100%;
        	height: 40px;
        	line-height: 40px;
        	text-align: center;
        	font-size: 25px;
        	font-weight: bold;
        	float: left;
        }
        .menu-font{
        	width: 100%;
        	height: 20px;
        	line-height: 20px;
        	text-align: center;
        	font-size: 14px;
        	float: left;
        }   
        
        .sidemenu .secondmenu{
        	width: 200px;
	        position: fixed;
	    	left: 120px;
    		margin-top: -92px;
        }
		.l-tab-links ul{
			margin-left: 42px;
		}
		.l-tab-links-left{
			left: 42px;
		}	
    </style>
</head>

<body class="viewFramework-body viewFramework-sidebar-full" >
	<%@include file="main_top.jsp"%>
    <div id="layoutMain" style="margin: 0px;">
		<div id="menuListnav" class="viewFramework-sidebar" >
			<div id="menunav">
				<ul class="sidemenu cl" id="sidemenu">
					
				</ul>
			</div>
		</div>
		<div class="viewFramework-sidebar-toggle">
			<div class="clearFix">
				<div class="btn fr v-s-b-sq"><a class="link menu" href="#" id="toggleSidebar"></a></div>
			</div>
		</div>		
		<div class="viewFramework-product" id="framecenter" >
			<div tabid="home" title="我的工作台" icon="icon-mainpage">
				<iframe src="" name="home" id="home"  class="viewFramework-mainFrame" border="0" vspace="0" hspace="0" marginwidth="0" marginheight="0" framespacing="0" frameborder="0" ></iframe>
			</div>
		</div>
	</div>
	
	<f:js pre="jslib/lang/common"></f:js>
	<f:js pre="jslib/lang/js"></f:js>
	<script type="text/javascript" src="${ctx}/jslib/util/json2.js"></script>
	<script type="text/javascript" src="${ctx}/js/framework/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/js/framework/jquery.ztree.js"></script>
	<script type="text/javascript" src="${ctx}/js/framework/jquery.mousewheel.js"></script>
	<script type="text/javascript" src="${ctx}/js/framework/jquery.rollbar.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/framework/base.js"></script>
	<script type="text/javascript" src="${ctx}/js/framework/framework.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/util/util.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/ligerui.all.js"></script>
	<script type="text/javascript" src="${ctx}/js/portal/share.js"></script>
	<script type="text/javascript">
		$(function(){
			//初始化菜单
			menuEach(menuList);
			//加载菜单事件
			initEvents();
			//初始话tab页
			initTab();      
		    //加载门户
		    $('#home').attr("src",ctxPath+"/oa/portal/insPortal/show.do?key=personal");
			
			$('#menunav').rollbar({scroll:'vertical', zIndex:100});
			'load|resize'.replace(/\w+/g, function(eType){
				$(window).bind(eType, function(){
					clientHeight = document.documentElement.clientHeight - 108;
					$('#menunav').height(clientHeight);
				});
			});
			
		});
		
		function initEvents(){
			$('.sidemenu li.firstmenu>p').click(function(){
				showTab($(this));
			});
			$('.sidemenu .secondmenu>li>p.hasnext').click(function(){
				showTab($(this));
			});
			
			$('.sidemenu .threemenu>li>p.hasnext').click(function(){
				showTab($(this));
			});
			$('.fourmenu>li').click(function(){
				showTab($(this));
			}); 
			
		    $('.sidemenu li.firstmenu').mouseenter(function(){
				$(this).find('>p').addClass('hoverBg');
				caulMenuPosition($(this), 120);
			}).mouseleave(function(){
				$(this).find('>p').removeClass('hoverBg')
			});

			$('.sidemenu .secondmenu>li').mouseenter(function(){
				$(this).find('>p').addClass('subHoverBg');
				caulMenuPosition($(this), 200);
			}).mouseleave(function(){
				$(this).find('>p').removeClass('subHoverBg');
			});
			

			//左侧导航展开、收缩
		    $("#toggleSidebar").click(function () {
		    	var $body = $(document.body);
		    	if($body.hasClass('viewFramework-sidebar-full')) {
					$body.removeClass('viewFramework-sidebar-full').addClass('viewFramework-sidebar-mini');
				} else {
					$body.removeClass('viewFramework-sidebar-mini').addClass('viewFramework-sidebar-full');
				}
		    	
		        var navWidth = $("#menuListnav").width();
		        if (navWidth >0) {
		            $("#menuListnav").attr("Close", true);
		            $("#menuListnav").animate({ "width": "0px", "opacity": 0 });
		            $("#framecenter").animate({ "left": "0px" });
		            $(this).closest(".viewFramework-sidebar-toggle").animate({ "left": "0px" });
		        } else {
		            $("#menuListnav").attr("Close", false);
		            $("#menuListnav").animate({ "width": "120px", "opacity": 1 });
		            $("#framecenter").animate({ "left": "120px" });
		            $("#menuListnav").css("overflow", "auto");
		            $(this).closest(".viewFramework-sidebar-toggle").animate({ "left": "120px" });
		        }
		    }); 
		}
		function caulMenuPosition(obj, h){
			var left = obj.offset().left + h - 10;
			var top = obj.offset().top;			
			
			var outerWindow =window.top;
			var dialogHeight = outerWindow.document.documentElement.clientHeight;
			var height = obj.find('ul:first').height();
			if(height+top+10 > dialogHeight && height+10<dialogHeight){
				top = dialogHeight - height;
				obj.find('ul:first').css({'left':left,'top': top});
			}else{
				top = -obj.height();
				obj.find('ul:first').css({'left':left,'margin-top': top});
			}
			if(obj.height()>height){
				obj.find('ul:first').css({'height':obj.height()});
			}
		}
	</script>
</body>
</html>
