<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>业务数据模板预览</title>
	<%@include file="/commons/include/get.jsp" %>
	<link rel="stylesheet" href="${ctx}/styles/home/home.css">
	<!--初始化数据字典的样式-->
	<!--start  自定义js文件，css文件  -->
	${headHtml}
	<!--end    自定义js文件，css文件  -->
</head>
<body style="overflow: hidden;">
	<div id="content" style="height: 100%;">
		${html}
	</div>
</body>
</html>
