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
	
	<script type="text/javascript">
		debugger;
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
	<f:js pre="jslib/lang/common"></f:js>
	<f:js pre="jslib/lang/js"></f:js>
	<script type="text/javascript" src="${ctx}/js/framework/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/js/framework/base.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/util/util.js"></script>
	<script type="text/javascript" src="${ctx}/js/lib/ligerui.all.js"></script>
	<script type="text/javascript" src="${ctx}/js/portal/share.js"></script>
	<script type="text/javascript" src="${ctx}/js/framework/framework.js"></script>

	<style type="text/css">
		.menu-icon{
        	font-weight: normal;
		    display: block;
		    margin-right: 0;
		    height: 50%;
		    width: 100%;
		    font-size: 22px;
		    padding-top: 5px;
		    box-sizing: border-box;
        }
        .viewFramework-topbar{
  			height: 70px;
        }
        .viewFramework-sidebar{
  			height: 70px;
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
		span.menu-font {
		    font-size: 14px;
		    width: 100%;
		    display: block;
		    padding-top: 5px;
		    height: auto;
		    font-weight: bold;
		}
		#framecenter .l-tab-content{
			height: 100%!important;
		}
		#framecenter .l-tab-content-item{
			height: 100%!important;
		}
		#framecenter .l-tab-content .l-tab-content-item iframe{
			height: 100%!important;
		}
	</style>
	<script type="text/javascript">
		$(document).ready(function () {
            debugger;
			//初始化菜单
			menuEach(menuList);
			// 菜单样式初始化
            menuInit();
			//主界面距离菜单高度重新计算
			/* var top = $("#sidemenu").height();
			$("#layoutMain").css("top",top+"px"); */
			
			//加载菜单事件
		    attachMenuEvents();
			//初始话tab页
			initTab();
		    //加载门户
		    $('#home').attr("src",ctxPath+"/oa/home/show.do");
			})
	        function menuInit() {
	            var sidemenu = $('.sidemenu'),
	                firstmenu = sidemenu.children('li.firstmenu'),
	                length = firstmenu.length,
	                firstmenuLi = $(firstmenu[0]),
	                eWidth = firstmenuLi.outerWidth(),
	                width = $('.viewFramework-topbar').outerWidth() - $('.viewFramework-hd').outerWidth() - $('.viewFramework-topbar-inner').outerWidth() - $('.arrow-icon').outerWidth() * 2 - 100,
	                count = parseInt(width / 100),
	                num = count -1 ;
	
	            firstmenuLi.addClass('first');
	            if (length > num) {
	                sidemenu.width(count * eWidth);
	                $(firstmenu[num]).addClass('last').prev().addClass('prev-last');
	                $('.arrow-icon').show();
	            } else {
	                sidemenu.width(length * eWidth);
	                $(firstmenu[length - 1]).addClass('last').prev().addClass('prev-last');
	                $('.arrow-icon').hide();
	            }
	
	            firstmenu.each(function (index, item) {
	                if (index > num) {
	                    $(item).hide();
	                }else{
	                	$(item).show();
	                }
	            })
	
	            // 左右轮播事件
	            $('.left.arrow-icon').click(function () {
	            	var sidemenu = $('.sidemenu'),
                    firstLi = sidemenu.find('li.first'),
                    firstPreLi = firstLi.prev('li');
                if (firstPreLi.length > 0) {
                    sidemenu.find('li.last').removeClass('last').hide().prev().addClass('last').prev().addClass('prev-last');
                    firstLi.removeClass('first');
                    firstPreLi.addClass('first').show();
                }
	                
	            })
	            $('.right.arrow-icon').click(function () {
	            	var sidemenu = $('.sidemenu'),
                    lastLi = sidemenu.find('li.last'),
                    lastNextLi = lastLi.next('li');
                if (lastNextLi.length > 0) {
                    sidemenu.find('li.first').removeClass('first').hide().next().addClass('first');
                    lastLi.removeClass('last').addClass('prev-last').prev().removeClass('prev-last');
                    lastNextLi.addClass('last').show();
                }
	            })
			}
		$(window).resize(function(){
			$('.sidemenu').children().remove();
			menuEach(menuList);
			$('.left.arrow-icon,.right.arrow-icon').unbind();
			menuInit();
			attachMenuEvents();
		})
	</script>
</head>
<body style="padding: 0px;">
	<%@include file="main_top.jsp"%>
	<div id="layoutMain" class="main">		
		<div position="center" class="framecenter" id="framecenter">
			<div tabid="home" title="我的工作台" icon="icon-mainpage">
				<iframe src="" name="home" id="home"  class="viewFramework-mainFrame" border="0" vspace="0" hspace="0" marginwidth="0" marginheight="0" framespacing="0" frameborder="0" ></iframe>
			</div>
		</div>
	</div>
</body>
</html>
