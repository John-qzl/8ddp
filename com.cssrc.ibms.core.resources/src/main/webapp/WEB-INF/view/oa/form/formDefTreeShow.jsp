<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<%@ taglib prefix="ht" tagdir="/WEB-INF/tags/wf"%>
<html>
<head>
	<title>表单树结构预览</title>
	<%@include file="/commons/include/form.jsp" %>
	
	<!-- zTree引入 -->
	<f:link href="tree/zTreeStyle.css"></f:link>
	<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerMenu.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerLayout.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/GlobalMenu.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/GlobalType.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/util/ZtreeCreator.js"></script>	
	<!-- ngjs引入 -->
	<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/angular/angular.min.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/angular/service/baseServices.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/angular/service/arrayToolService.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/angular/service/commonListService.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/formDefTree/FormDefTreeService.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/formDefTree/ShowController.js"></script>
	<script type="text/javascript">
		var ztreeCreator=null;
		var formKey="${formKey}";
		var __displayId__="${__displayId__}";
		
		$(function() {
			$("#defLayout").ligerLayout({leftWidth:300,height: '100%',allowLeftResize:false});
		});
		
		function refreshTheTree(){
			var scope=getScope("body");
			scope.refreshTheTree();
		}
	</script>
</head>
<body  ng-app="app" ng-controller="ShowController">
<div class="panel">
	<div id="defLayout" >
		<div position="left" title="树" style="overflow: auto;float:left;width:100%">
			<ul id="tree" class="ztree"></ul>
		</div>
		<div position="center" title="编辑">
			<iframe id="centerFrame"
		 		style="overflow: auto;width:100%;height:100%" frameborder="0"></iframe>
		</div>
     </div>
</div>
</body>
</html>
