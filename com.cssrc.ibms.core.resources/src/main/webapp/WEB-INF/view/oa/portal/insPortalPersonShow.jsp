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
				<div class="toolBar fr" style="margin-top:0px;">
					<div class="group">
						<a class="link setting" id="editPortal" onclick="setPort()">设置门户</a>
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
	
	<script type="text/javascript" src="${ctx}/jslib/lg/ligerui.all.js"></script><!-- å¼å¥ligerui.all.js-->
	<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerDialog.js" ></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerResizable.js" ></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/util/DialogUtil.js" ></script>
	<script type="text/javascript">	
		$(document).ready(function () {
			var editorEnable = "0";
			$('.gridster ul').dashboard({
				 cfg:${portalCols},
				 editorEnable: editorEnable
			});	
		});
		//打开一个新的页面显示这个栏目的more
		function mgrNewsRow(colId,name){
			top['index'].showTabFromPage({
				title:name+'-信息公告',
				tabId:'colNewsMgr_'+colId,
				url:__ctx+'/oa/portal/insNews/byColId.do?colId='+colId
			});
		}
		
		//设置门户按钮，打开一个新页面设置门户
		function setPort(){
			top['index'].showTabFromPage({
				title:'编辑门户',
				tabId:'portId_'+portId,
				iconCls:'icon-window',
				url:__ctx+'/oa/portal/insPortal/personShowEdit.do?'
			});
		}
	</script>
</body>
</html>