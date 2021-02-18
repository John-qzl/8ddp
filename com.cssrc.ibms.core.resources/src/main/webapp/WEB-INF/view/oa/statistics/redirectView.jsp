<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<%
	String basePath=request.getContextPath();
	request.setAttribute("basePath", basePath);
%>
<html>
<head>
	<title>中转页面</title>
<%@include file="/commons/include/get.jsp" %>
</head>

<body>
	 <div style="height: 100%; width: 100%;">
		<iframe frameborder="0" width="100%"  height="100%"  name="outSystem" 
				src="${basePath}/oa/redirect/trans.do?tool=${tool}&address=${address}"></iframe>
	 </div>
</body>
</html>
