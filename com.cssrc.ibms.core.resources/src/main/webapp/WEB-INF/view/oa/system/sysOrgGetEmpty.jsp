<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- 为什么要多做一个jsp页面?主要是为了防止直接或get.do时，发生的js报错 -->
<html>
<head>
	<title>组织架构明细</title>
</head>
<body>
	<div class="panel" id="toppanel">	
		<div class="panel-body" id="pbody">
			<c:if test="${isShow}">
				<div style="text-align: center;margin-top: 10%;">该组织已被逻辑删除，若要操作该组织请先右键还原该组织!</div>
			</c:if>
			<c:if test="${!isShow}">
				<div style="text-align: center;margin-top: 10%;">尚未指定具体组织!</div>
			</c:if>
		</div>
	</div>
</body>
</html>