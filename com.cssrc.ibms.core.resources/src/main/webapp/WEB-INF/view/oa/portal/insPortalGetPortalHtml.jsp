<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%><!-- 定义了浏览器的文本模式（防止IE8进入杂项） -->
<html>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>
<%@include file="/commons/dynamic.jspf"%>
<%@include file="/commons/columnCustom.jsp"%>

<head>
	<title>Portal单页</title>
	<link href="${ctx}/styles/portal/portal.css" rel="stylesheet" type="text/css" />
</head>
<body>
	${html}
</body>
</html>