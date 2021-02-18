<%--
	time:2016-07-26
	by yangbo
	desc:edit the 我的首页布局
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"  %>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>预览模版</title>
	<%@include file="/commons/include/home.jsp"%>
	
</head>
<body >
	<div class="index-page">${html}</div>
</body>
<!--[if IE]>
	<script type="text/javascript">
		document.write("<script src='${ctx}/jslib/ibms/index/indexPage.js'>"+"<"+"/script>");
	</script>
	<![endif]-->
</html>


