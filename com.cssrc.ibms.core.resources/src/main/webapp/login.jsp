<%@include file="/commons/include/html_doctype.html"%><!-- 定义了浏览器的文本模式（防止IE8进入杂项） -->
<%@page import="org.springframework.security.authentication.AuthenticationServiceException"%>
<%@page import="org.springframework.security.authentication.AccountExpiredException"%>
<%@page import="org.springframework.security.authentication.DisabledException"%>
<%@page import="org.springframework.security.authentication.LockedException"%>
<%@page import="org.springframework.security.authentication.BadCredentialsException"%>
<%@ taglib uri="http://www.cssrc.com.cn/functions" prefix="ibms"%>
<%@page import="java.util.Enumeration"%>
<%@ page pageEncoding="UTF-8" %>
<%@page import="org.springframework.security.web.WebAttributes"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<ibms:sysparam paramname="SYS_UITYPE" alias="SYS_UITYPE"></ibms:sysparam>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 <meta http-equiv="X-UA-Compatible" content="IE=edge" />
<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.js"></script>
	<script type="text/javascript">
		//根据主题参数跳转对应登录页面
		var theme = "${SYS_UITYPE}";
		if(theme == 1){
			//新样式
			location.href="${ctx}/loginv1.jsp";
		}else{
			location.href="${ctx}/loginIbms.jsp";
		}
		
	</script>
</head>
<body>
</body>
</html>

 
