<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>门户编辑</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}" />
	<f:js pre="jslib/lang/common" ></f:js>
	<f:js pre="jslib/lang/js" ></f:js>
	<f:js pre="jslib/lang/view/oa/system" ></f:js>
	
	<f:link href="../../dist/css/sunplat.css"></f:link>
	<link href="${ctx}/js/portal/miniui/res/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
	<link href="${ctx}/jslib/gridster/dashboard.css" rel="stylesheet" type="text/css" />
	<link href="${ctx}/jslib/gridster/jquery.gridster.min.css" rel="stylesheet" type="text/css" />
	
	<script type="text/javascript" src="${ctx}/js/dynamic.jsp"></script>
	<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/gridster/dashboard-compress.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/gridster/jquery.gridster.min.js"></script>
	
	<script type="text/javascript" src="${ctx}/jslib/lg/ligerui.all.js"></script><!-- å¼å¥ligerui.all.js-->
	<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerDialog.js" ></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerResizable.js" ></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/util/DialogUtil.js" ></script>

<script>
	$(document).ready(function () {
		var editorEnable = "1";
		$('.gridster ul').dashboard({
			 cfg:${portalCols},
			 editorEnable: editorEnable
		});	
	});
</script>
</head>
<body>
	<div class="panel-top">
		<div class="panel-toolbar">
			<div class="btn-table fr" style="margin-top:0px;">
				<div class="x-btn">
					<a class="link save" id="savePortal">保存</a>
				</div>
				<div class="x-btn">
					<a class="link add" id="addPortal">加入栏目</a>
				</div>
				<div class="x-btn">
					<a class="link reload" id="refreshPortal">刷新</a>
				</div>
			</div>
		</div>
	</div>
	<div class="gridster" style="padding:0px;">
		<ul></ul>
		
		<input id="portId" name="portId" type="hidden" value="${insPortal.portId}" />
	</div>
	
<script type="text/javascript">
	//打开一个新的页面显示这个栏目的more
	function mgrNewsRow(colId,name,moreUrl){
		top['index'].showTabFromPage({
			title:name,
			tabId:'colNewsMgr_'+colId,
			url:moreUrl
		});
	}
	
	function refreshPage(){
		var newUrl =  __ctx+"/oa/portal/insPortal/global.do?portId=${insPortal.portId}";
	  	location.href = newUrl;
	}
</script>
</body>
</html>