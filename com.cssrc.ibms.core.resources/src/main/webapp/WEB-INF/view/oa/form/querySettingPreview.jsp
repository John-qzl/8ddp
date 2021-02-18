<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>业务数据模板预览</title>
	<%@include file="/commons/include/get.jsp" %>
	<f:link href="tree/zTreeStyle.css"></f:link>
	<link rel="stylesheet" href="${ctx}/jslib/jquery/multiple-select.css" />
	<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/ajaxgrid.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/Export.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/SelectorUtil.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/util/fileUtil.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/jquery/multiple-select.js"></script><!-- 下拉多选框 -->
	<!--start  自定义js文件，css文件  -->
		${headHtml}
	<!--end    自定义js文件，css文件  -->
	<script type="text/javascript">
	$(function(){
		handleAjaxSearchKeyPress();
		Export.initExportMenu();
	});

	</script>
</head>
<body>
	<div id="content" style="height:100%">
		${html}
	</div>
</body>
</html>