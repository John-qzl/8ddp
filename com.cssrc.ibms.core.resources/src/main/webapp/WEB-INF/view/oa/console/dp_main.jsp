<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<!DOCTYPE HTML>
<html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<c:set var="ctx" value="${pageContext.request.contextPath}" />
<head>
<title>${currentSystem.companyName}${currentSystem.systemName}</title>

<!-- 标题栏logo -->
<link rel="shortcut icon" href="${ctx}/favicon.ico" type="image/x-icon" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@include file="/commons/include/color.jsp"%>
<f:link href="Aqua/css/ligerui-common.css"></f:link>
<f:link href="Aqua/css/ligerui-layout.css"></f:link>
<f:link href="Aqua/css/ligerui-dialog.css"></f:link>
<f:link href="Aqua/css/ligerui-tab.css"></f:link>
<f:link href="Aqua/css/ligerui-menu.css"></f:link>
<f:link href="iconfont.css"></f:link>
<f:link href="iconImg.css"></f:link>

<link rel="stylesheet" href="${ctx}/styles/framework/basic.css">
<script type="text/javascript">
    debugger;
var ctxPath = "<%=request.getContextPath()%>";
</script>
<f:js pre="jslib/lang/common"></f:js>
<f:js pre="jslib/lang/js"></f:js>
<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/util.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/base.js"></script>
<script type="text/javascript" src="${ctx}/js/lib/ligerui.all.js"></script>

<script src="${ctx}/js/portal/share.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	var _height = $(window).height()-$('.viewFramework-topbar').height();
	$('#layoutMain').height(_height);
    debugger;
	$('#home').attr("src",ctxPath+"/dataPackage/tree/projectTree/manage.do?flag=${flag}");

})
</script>
<style media="screen">
	iframe{
		width: 100%;height: 100%;border: none;
	}
</style>

</head>
<body style="padding: 0px;">
	<%@include file="main_top.jspf"%>
	<div id="layoutMain" style="margin: 0px;">
		<iframe id="home" src="" width="" height=""></iframe>
	</div>
</body>
</html>
