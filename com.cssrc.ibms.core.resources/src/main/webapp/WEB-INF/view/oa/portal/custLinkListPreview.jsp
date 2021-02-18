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
	<script type="text/javascript">
	$(function(){
		$('#tab').ligerTab();
	})
	</script>
</head>
<body style="overflow: hidden;">
	
	<div id="tab">
	<div tabid="1" id="table" title="系统链接">
		<div id="page" style="height: 100%;">
			${html}
	    </div>
	</div>
	<div tabid="attach" id="table" title="附件下载">
			<div id="content" style="height: 100%;">
			${attachHtml}
	    </div>
	</div>
</div>
</body>
</html>