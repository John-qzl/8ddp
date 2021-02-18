

<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>表单功能点管理</title>
<%@include file="/commons/include/form.jsp" %>
<f:link href="tree/zTreeStyle.css"></f:link>
<f:resources name="ROOT_PID" alias="ROOT_PID"></f:resources>
<script type="text/javascript"	src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript"	src="${ctx}/jslib/ibms/oa/system/record/FunctionMenu.js"></script>
<script type="text/javascript">

	var rootId=0;
	var foldMenu;
	var leafMenu;
	var systemMenu;	
	$(function(){
		//布局
		loadLayout();
		//菜单
		_FunctionMenu_.init("listFrame");
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
        $("#functionTree").height(height-90);
	};
	//布局大小改变的时候通知tab，面板改变大小
    function heightChanged(options){
     	$("#functionTree").height(options.middleHeight -90);
    };
    //树
	var functionTree;
    var expandByDepth = 1;
	//加载树
	function loadTree(){
		var setting = {
			async: {enable: false},
			data: {
				key:{name:"funName"},
				simpleData: {
					enable: true,
					idKey: "funId",
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
				onRightClick: _FunctionMenu_.zTreeOnRightClick,
				beforeDrop: null,
				onDrop: null
			}
		};
		var url="${ctx}/oa/system/recFun/getSystemTreeData.do";
		var params = {
				typeId:'${typeId}'
			};
		$.post(url,params,function(result){
			functionTree=$.fn.zTree.init($("#functionTree"), setting,result);
			functionTree.expandAll(false);
			var node = functionTree.getNodeByParam("funId","0",null);
			functionTree.expandNode(node,true,false,false,false);
		});
	
	};
	//左击
	function zTreeOnLeftClick(event, treeId, treeNode){
		var funId=treeNode.funId;
		if(funId==rootId){
			return;
		}
		var url="${ctx}/oa/system/recFun/get.do?funId="+treeNode.funId;
		$("#listFrame").attr("src",url);
	};

	//展开收起
	function treeExpandAll(type){
		functionTree = $.fn.zTree.getZTreeObj("functionTree");
		functionTree.expandAll(type);
	};


	//选择资源节点。
	function getSelectNode(){
		functionTree = $.fn.zTree.getZTreeObj("functionTree");
		var nodes  = functionTree.getSelectedNodes();
		var node   = nodes[0];
		return node;
	}
	//刷新
	function reFresh(){
		loadTree();
	};
	
	//添加完成后调用该函数
	function addResource(id,text,icon,isFolder){
		//
		var parentNode=getSelectNode();
		functionTree = $.fn.zTree.getZTreeObj("functionTree");
		var treeNode= {funId:id,typeId:parentNode.typeId,parentId:parentNode.funId,funName:text,icon:icon,isFolder:isFolder};
		functionTree.addNodes(parentNode,treeNode);
	}
	//编辑完成后调用该函数。
	function editResource(text,icon,isFolder){
		var selectNode=getSelectNode();
		selectNode.funName=text;
		selectNode.icon=icon;
		selectNode.isFolder=isFolder;
		functionTree = $.fn.zTree.getZTreeObj("functionTree");
		functionTree.updateNode(selectNode);
	}
</script>

</head>
<body >
<div id="layout" >
	<div position="left" title="表单功能点管理" style="text-align: left;" >
	<div class="tree-toolbar" id="pToolbar">	
				<div class="group"><a class="link reload" id="treeFresh" href="javascript:reFresh();"></a></div>
				
				<div class="group"><a class="link expand" id="treeExpandAll" href="javascript:treeExpandAll(true)" ></a></div>
				
				<div class="group"><a class="link collapse" id="treeCollapseAll" href="javascript:treeExpandAll(false)" ></a></div>
			
		</div>
		<div id="functionTree" class="ztree" style="overflow:auto;clear:left"></div>
	</div>
	<div position="center">
		<iframe id="listFrame" src="" frameborder="no" width="100%" height="100%"></iframe>
	</div>
</div>
</body>
</html>

