<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
	<title>系统分类信息</title>
</head>
<body>
	<div class="panel" id="toppanel">	
		<div class="panel-body" id="pbody">
			<c:if test="${isShow}">
				<div style="text-align: center;margin-top: 10%;">该分类已被逻辑删除，若要操作该分类请先右键还原该分类!</div>
			</c:if>
			<c:if test="${!isShow}">
				<div style="text-align: center;margin-top: 10%;">尚未指定具体分类!</div>
			</c:if>
		</div>
	</div>
</body>
</html>