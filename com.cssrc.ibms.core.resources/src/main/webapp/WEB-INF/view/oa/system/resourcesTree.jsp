<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>资源管理</title>
<%@include file="/commons/include/form.jsp" %>
<f:link href="tree/zTreeStyle.css"></f:link>
<f:resources name="ROOT_PID" alias="ROOT_PID"></f:resources>
<script type="text/javascript"	src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript"	src="${ctx}/jslib/ibms/oa/system/ResourcesMenu.js"></script>
<script type="text/javascript">

	var rootId=0;
	
	var foldMenu;
	var leafMenu;
	var systemMenu;	
	$(function(){
		//布局
		loadLayout();
		//菜单
		_ResourcesMenu_.init("listFrame");
		//加载树
		loadTree();
	});
	//布局
	function loadLayout(){
		$("#layout").ligerLayout( {
			leftWidth : 300,
			onHeightChanged: heightChanged,
			allowLeftResize:false
		});
		//取得layout的高度
        var height = $(".l-layout-center").height();
        $("#resourcesTree").height(height-90);
	};
	//布局大小改变的时候通知tab，面板改变大小
    function heightChanged(options){
     	$("#resourcesTree").height(options.middleHeight -90);
    };
    //树
	var resourcesTree;
    var expandByDepth = 1;
	//加载树
	function loadTree(){
		var setting = {
			async: {enable: false},
			data: {
				key:{name:"resName"},
				simpleData: {
					enable: true,
					idKey: "resId",
					pIdKey: "parentId",
					rootPId: "${ROOT_PID}"
				}
			},
			view: {
				selectedMulti: false
			},
			edit: {
				drag: {
					prev: false,inner: false,next: false,isMove:false
				},
				enable: true,
				showRemoveBtn: false,
				showRenameBtn: false
			},
			callback:{
				onClick: zTreeOnLeftClick,
				onRightClick: _ResourcesMenu_.zTreeOnRightClick,
				beforeDrop: null,
				onDrop: null
			}
		};
		var url="${ctx}/oa/system/resources/getSystemTreeData.do";
		var params=null;
		$.post(url,params,function(result){
			resourcesTree=$.fn.zTree.init($("#resourcesTree"), setting,result);
			resourcesTree.expandAll(false);
			var node = resourcesTree.getNodeByParam("resId","0",null);
			resourcesTree.expandNode(node,true,false,false,false);
		});
	
	};

	//左击
	function zTreeOnLeftClick(event, treeId, treeNode){
		var resId=treeNode.resId;
		if(resId==rootId){
			return;
		}
		var url="${ctx}/oa/system/resources/get.do?resId="+treeNode.resId;
		$("#listFrame").attr("src",url);
	};

	//展开收起
	function treeExpandAll(type){
		resourcesTree = $.fn.zTree.getZTreeObj("resourcesTree");
		resourcesTree.expandAll(type);
	};


	//选择资源节点。
	function getSelectNode(){
		resourcesTree = $.fn.zTree.getZTreeObj("resourcesTree");
		var nodes  = resourcesTree.getSelectedNodes();
		var node   = nodes[0];
		return node;
	}
	//刷新
	function reFresh(){
		loadTree();
	};
	
	//添加完成后调用该函数
	function addResource(id,text,icon,isFolder){
		var parentNode=getSelectNode();
		resourcesTree = $.fn.zTree.getZTreeObj("resourcesTree");
		var treeNode= {resId:id,parentId:parentNode.resId,resName:text,icon:icon,isFolder:isFolder};
		resourcesTree.addNodes(parentNode,treeNode);
	}
	//编辑完成后调用该函数。
	function editResource(text,icon,isFolder){
		var selectNode=getSelectNode();
		selectNode.resName=text;
		selectNode.icon=icon;
		selectNode.isFolder=isFolder;
		resourcesTree = $.fn.zTree.getZTreeObj("resourcesTree");
		resourcesTree.updateNode(selectNode);
	}
</script>
</head>
<body >
<div id="layout" >
	<div position="left" title="资源管理" style="text-align: left;" >
<div class="tree-toolbar" id="pToolbar">	
				<div class="group"><a class="link reload" id="treeFresh" href="javascript:reFresh();"></a></div>
				
				<div class="group"><a class="link expand" id="treeExpandAll" href="javascript:treeExpandAll(true)" ></a></div>
				
				<div class="group"><a class="link collapse" id="treeCollapseAll" href="javascript:treeExpandAll(false)" ></a></div>
			
		</div>
		<div id="resourcesTree" class="ztree" style="overflow:auto;clear:left"></div>
	</div>
	<div position="center">
		<iframe id="listFrame" src="" frameborder="no" width="100%" height="100%"></iframe>
	</div>
</div>
</body>
</html>

