<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>门户编辑</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}" />
	
	<f:link href="web.css" ></f:link>
	<link href="${ctx}/js/portal/miniui/res/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
	<link href="${ctx}/jslib/gridster/dashboard.css" rel="stylesheet" type="text/css" />
	<link href="${ctx}/jslib/gridster/jquery.gridster.min.css" rel="stylesheet" type="text/css" />
	
	<f:js pre="jslib/lang/common" ></f:js>
	<f:js pre="jslib/lang/js" ></f:js>
	<f:js pre="jslib/lang/view/oa/system" ></f:js>
	<script type="text/javascript" src="${ctx}/js/dynamic.jsp"></script>
</head>
<body>
	<div class="panel-top">	
		<div class="panel-toolbar">
			<div class="form-item fl">
				<label class="form-label">当前布局:</label>
				<div class="input-block">
					<input type="text" class="input" name="name" value="${insPortal.name}"/>
				</div>
			</div>
			<div class="form-item fl">
				<label class="form-label">布局描述:</label>
				<div class="input-block">
					<input type="text" class="input" name="desc" value="${insPortal.desc}"/>
				</div>
			</div>
			<div class="toolBar fr" style="margin-top:0px;">
				<div class="group">
					<a class="link save" id="saveGlobal">保存</a>
				</div>
				<div class="group">
					<a class="link add" id="addPortal">加入栏目</a>
				</div>
				<div class="group">
					<a class="link reload" id="refreshPortal">刷新</a>
				</div>
			</div>
		</div>
	</div>
	<div class="gridster" style="padding:0px;">
		<ul></ul>
		
		<input id="portId" name="portId" type="hidden" value="${insPortal.portId}" />
	</div>
	
	<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/gridster/dashboard-compress.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/gridster/jquery.gridster.min.js"></script>
	
	<script type="text/javascript" src="${ctx}/jslib/lg/ligerui.all.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerDialog.js" ></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerResizable.js" ></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/util/DialogUtil.js" ></script>
	<script type="text/javascript">
		$(document).ready(function () {
			var editorEnable = "1";
			$('.gridster ul').dashboard({
				 cfg:${portalCols},
				 editorEnable: editorEnable
			});	
		});
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