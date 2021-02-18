<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>dbom tree管理</title>
<%@include file="/commons/include/get.jsp"%>
<f:gtype name="CAT_REPORT" alias="CAT_REPORT"></f:gtype>
<f:link href="tree/zTreeStyle.css"></f:link>
<script type="text/javascript"
	src="${ctx}/jslib/lg/plugins/ligerMenu.js"></script>
<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/js/dbom/DbomManageTree.js"></script>
<script type="text/javascript">
	var dbomManage = null;
	$(function() {
		$("#layout").ligerLayout({
			leftWidth : 225,
			height : '100%',
			allowLeftResize : false
		}); 
		
		height = $('#layout').height();
		$("#dbomViewFrame").height(height - 25);
		
		var pCode = $("#dbomCode").find("option:selected").val();
		dbomManage = new DbomManage("dbomTree", "nodeType=-1&pCode=" + pCode,
				"getDobomByCode.do", {
					expandAll : false
				});
		dbomManage.loadTree();
		dbomManage.menu=getMenu();
		$("#addDbomType").on("click",dbomManage.addDbomType);
		$("#editDbomType").on("click",dbomManage.editDbomType);
		$("#delDbomType").on("click",dbomManage.delDbomType);

	});
	function getMenu() {
		var menu = $.ligerMenu({
			top : 100,
			left : 100,
			width : 100,
			items : <f:menu>[ {
				text : '新建节点',
				click : 'addNode',
				alias : 'addDbomNode'
			}, {
				text : '编辑节点',
				click : 'editNode',
				alias : 'editDbomNode'
			}, {
				text : '删除节点',
				click : 'delNode',
				alias : 'delDbomNode'
			} ]
			</f:menu>
		});
		return menu;
	}
	
	 //新增dbom 节点
	function addNode(){
		var mill=(parseInt(Math.random()*10000)).toString();
		var url="${ctx}/oa/system/dbomNode/edit.do?mill="+mill;
		$("#dbomViewFrame").attr("src",url);
	};
	
     //编辑dbom 节点
	function editNode() {
		var cnode=dbomManage.currentNode;
		var type = cnode.type;
		if(type != '-1'){
			var mill=(parseInt(Math.random()*10000)).toString();
			var url="${ctx}/oa/system/dbomNode/edit.do?mill="+mill+"&cnodeId="+cnode.id;
			$("#dbomViewFrame").attr("src",url);
		}else{
			$.ligerDialog.warn("当前节点不可编辑！");
		}
	};

	//删除dbom 节点
	function delNode() {
		alert(3);
	};

</script>
</head>
<body>
	<div id="layout" style="bottom: 1; top: 1">
		<div position="left" title="dbom管理"
			style="height: 100%; width: 100% !important;">
			<div style="width: 100%;">
				<button type="button" id="addDbomType" onclick="">新建分类</button>
				<button type="button" id="editDbomType">编辑分类</button>
				<button type="button" id="delDbomType">删除分类</button>
			</div>
			<div style="width: 100%;">
				<select id="dbomCode" style="width: 99.8% !important;">
					<c:forEach var="dbom" items="${dbomDatas}" varStatus="vs">
						<option value="${dbom.code}">${dbom.name}</option>
					</c:forEach>
				</select>
			</div>
			<div class="tree-toolbar" id="pToolbar">
				<div class="toolBar"
					style="text-overflow: ellipsis; overflow: hidden; white-space: nowrap">
					<div class="group">
						<a class="link reload" id="treeReFresh"></a>
					</div>
					
					<div class="group">
						<a class="link expand" id="treeExpand"></a>
					</div>
					
					<div class="group">
						<a class="link collapse" id="treeCollapse"></a>
					</div>
					
					<div class="group">
						<a class="link search" id="treeSearch"></a>
					</div>
				</div>
			</div>
			<ul id="dbomTree" class="ztree" style="overflow: auto;">

			</ul>
		</div>
		<div position="center" id="dbomView" style="height: 100%;">
			<iframe id="dbomViewFrame" frameborder="0" width="100%"
				height="100%" scrolling="auto"></iframe>
		</div>
	</div>


</body>
</html>


