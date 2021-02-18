<%@page import="org.springframework.security.authentication.AuthenticationServiceException"%>
<%@page import="org.springframework.security.authentication.AccountExpiredException"%>
<%@page import="org.springframework.security.authentication.DisabledException"%>
<%@page import="org.springframework.security.authentication.LockedException"%>
<%@page import="org.springframework.security.authentication.BadCredentialsException"%>
<%@ taglib uri="http://www.cssrc.com.cn/functions" prefix="ibms"%>
<%@include file="/commons/include/html_doctype.html"%><!-- 定义了浏览器的文本模式（防止IE8进入杂项） -->
<%@page import="java.util.Enumeration"%>
<%@ page pageEncoding="UTF-8" %>
<%@page import="org.springframework.security.web.WebAttributes"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<ibms:sysparam paramname="SYS_LOGIN_PNG" alias="loginBackPng"></ibms:sysparam>
<ibms:sysparam paramname="SYS_LOGIN_LOG" alias="loginLog"></ibms:sysparam>
<ibms:sysparam paramname="SYSTEM_VERSION" alias="version"></ibms:sysparam>
<html>
<head>
<meta charset="UTF-8">
	<title>登录</title>
	<%@include file="/commons/include/color.jsp"%>
    <f:link href="login_uiv1.css"></f:link>
	<f:link href="Aqua/css/ligerui-dialog.css"></f:link>
	<f:js pre="jslib/lang/common" ></f:js>
	<!--[if lte IE 8]>
		<style type="text/css">
			body{
				width:100%;
				height:100%;
			}
			.login-bg{
				display:block;
				position:absolute;
				top:0;
				left:0;
				z-index:0;
				width:100%;
				height:100%;
			}
		</style>
	<![endif]-->
	<!--[if !IE]><!-->
		<style type="text/css">
			.login-bg{
				display:none;
			}
		</style>
	<!--<![endif]-->
	<style type="text/css">
		body {
			background-image: url(${ctx}${loginBackPng});
			background-size: 100%;
		}
	</style>
	<script type="text/javascript" src="${ctx}/js/lib/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/ligerui.min.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerDialog.js" ></script><!-- ligerDialog弹窗 -->
	<script type="text/javascript" src="${ctx}/js/niceFormElem.js"></script>
	<script type="text/javascript">
		if(top!=this){//当这个窗口出现在iframe里，表示其目前已经timeout，需要把外面的框架窗口也重定向登录页面
			  top.location='${ctx}/loginv1.jsp';
		};
		
		function changePassword(userId){
			var title = "密码更新";
			var url = "${ctx}/oa/system/sysUser/modifyPwdView.do?userId="+userId;
			$.ligerDialog.open({
				title:title, 
    			url:url, 
    			height:400,
    			width:600,
    			isDrag:false,
    			showToggle:false,
    			isResize:false,
    			sucCall:function(rtn){
    				//rtn作为回调对象，可进行定制和扩展
    				if(!(rtn == undefined || rtn == null || rtn == '')){
    					window.location.reload(true);
    				}
    			}
			});
		}
		
		function versionShow(){
			var title = "系统版本";
			var url = "${ctx}/oa/system/version/show.do";
			$.ligerDialog.open({
				title:title, 
    			url:url,
    			width:600, 
    			height:500,
    			isDrag:false,
    			showToggle:false,
    			isResize:false,
    			sucCall:function(rtn){
    				//rtn作为回调对象，可进行定制和扩展
    				if(!(rtn == undefined || rtn == null || rtn == '')){
    					window.location.reload(true);
    				}
    			}
			});
		}

		$(function(){
			placeholder(); // 水印
			radioCheckbox();// checkbox | radio 美化
			$('#name').focus()
			$('.fm-ipt-txt').focus(function(){
				this.parentNode.style.borderColor = '#347EFE'
			}).blur(function(){
				this.parentNode.style.cssText = '';
			});
			
			$('.main_login').find('input').keydown(function(event){
				if(event.keyCode==13){
					$('#form-login').submit();
				}
			});
		});
	</script>
</head>
<body> 
	<img class="login-bg" src="${ctx}${loginBackPng}" alt="" />
	<div class="loginBox">
		
		<div class="loginBox-inner">
			<div class="lbox-hd clearFix">
				<img src="${ctx}/styles/default/images/login/logo.png">
				<div class="system_name">离线数据采集管理系统</div>
				<!-- <h1 class="lbox-hd-h1 fl">用户登录</h1> -->
				<%-- <a onclick="versionShow()" class="version" title="版本说明">系统版本：${version}</a> --%>
			</div>
			<form id="form-login" action="${ctx}/login.do" method="post">
			<div class="main_login">
					<div class="fm-ipt pr clearFix" style="border-color: #347EFE;">
						<input type="text" id="name" name="username" class="fm-ipt-txt fl" placeholder="用户名">
						<div class="fm-ipt-left icon-name"></div>
					</div>
	
					<div class="fm-ipt pr clearFix" >
						<input type="password" id="pwd" name="password" class="fm-ipt-txt fl" placeholder="密码">
						<div class="fm-ipt-left icon-pwd"></div>
					</div>
					<div class="btnbox">
						<a class="btn" onclick="document.getElementById('form-login').submit();"><button type="button">登　录</button></a>
					</div>
				</div>
			</form>
				<%-- <div class="down_link">
					<a href="${ctx}/oa/console/downloadPlugin.do?fileName=flashplayer.exe">flash插件</a>
					<a href="${ctx}/oa/console/downloadPlugin.do?fileName=pageOffice.exe">pageOffice插件</a>
				</div> --%>
		</div>
	</div>
	<div class="banner"><img src="${ctx}${loginLog}" onerror="this.style.display='none'" alt="loginLog"/></div>
	<!--版权-->
	<div class="login-copyright">
		 <%--<div class="copy-en">Copyright &copy; Wuxi Orient Software Tech Co.,Ltd</div>--%>
		 <%--<div class="copy-zh">中船重工奥蓝托无锡软件技术有限公司</div>--%>
	</div>
</body>
</html>