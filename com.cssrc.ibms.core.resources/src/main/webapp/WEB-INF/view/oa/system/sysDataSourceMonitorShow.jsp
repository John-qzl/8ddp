<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<%@include file="/commons/include/get.jsp" %>
<html>
<head>
	<title>数据源信息监控</title>
</head>
<body>
	<div class="panel">
		<div class="panel-body">
			<iframe src="${ctx}/druid/index.html" id="druid" name="druid" width="100%" height="98%" frameborder="0">
			</iframe>
		</div>
		
	</div>
</body>
</html>


