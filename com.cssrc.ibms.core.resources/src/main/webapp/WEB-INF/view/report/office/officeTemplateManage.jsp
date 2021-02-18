<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>报表模板管理</title>
<%@include file="/commons/include/get.jsp"%>
<f:gtype name="CAT_REPORT" alias="CAT_REPORT"></f:gtype>
<f:link href="tree/zTreeStyle.css"></f:link>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerMenu.js"></script>
<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerLayout.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/GlobalMenu.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/GlobalType.js"></script>

<script type="text/javascript">
	var catKey="${CAT_REPORT}";

	var curMenu;
	var reportTypeMenu = new ReportTypeMenu();
	var globalType = new GlobalType(catKey, "glTypeTree", {
		onClick : treeClick,
		onRightClick : zTreeOnRightClick,
		expandByDepth : 1
	});

	$(function() {

		$("#defLayout").ligerLayout({
			leftWidth : 210,
			height : '100%',
			allowLeftResize : false
		});
		globalType.loadGlobalTree();

	});

	//展开收起
	function treeExpandAll(type) {
		globalType.treeExpandAll(type);
	};

	function treeClick(treeNode) {
		var typeId = treeNode.typeId;
		var parentId = treeNode.parentId;
		var url = "${ctx}/oa/system/officeTemplate/list.do";
		if (parentId != null) {
			url += "?Q_typeId_S=" + typeId;
		}
		$("#officeFrame").attr("src", url);
	}

	/**
	 * 树右击事件
	 */
	function zTreeOnRightClick(event, treeId, treeNode) {
		hiddenMenu();
		if (treeNode) {
			globalType.currentNode = treeNode;
			globalType.glTypeTree.selectNode(treeNode);
			curMenu = reportTypeMenu.getMenu(treeNode, handler);
			justifyRightClickPosition(event);
			curMenu.show({
				top : event.pageY,
				left : event.pageX
			});
		}
	};

	function hiddenMenu() {
		if (curMenu) {
			curMenu.hide();
		}
	}

	function handler(item) {
		hiddenMenu();
		var txt = item.text;
		switch (txt) {
		case "增加分类":
			globalType.openGlobalTypeDlg(true);
			break;
		case "编辑分类":
			globalType.openGlobalTypeDlg(false);
			break;
		case "删除分类":
			globalType.delNode();
			break;
		}
	}
</script>
</head>
<body>

	<div id="defLayout">
		<div position="left" title="office模板分类管理"
			style="overflow: auto; float: left; width: 100%">
			<div class="tree-toolbar">
				<span class="toolBar">
					<div class="group">
						<a class="link reload" id="treeFresh"
							href="javascript:globalType.loadGlobalTree()">刷新</a>
					</div>
					
					<div class="group">
						<a class="link expand" id="treeExpandAll"
							href="javascript:treeExpandAll(true)">展开</a>
					</div>
					
					<div class="group">
						<a class="link collapse" id="treeCollapseAll"
							href="javascript:treeExpandAll(false)">收起</a>
					</div>
				</span>
			</div>
			<ul id="glTypeTree" class="ztree"></ul>
		</div>
		<div position="center" title="office模板管理">
			<iframe id="officeFrame" height="100%" width="100%" frameborder="0"
				src="${ctx}/oa/system/officeTemplate/list.do"></iframe>
		</div>
	</div>

</body>
</html>


