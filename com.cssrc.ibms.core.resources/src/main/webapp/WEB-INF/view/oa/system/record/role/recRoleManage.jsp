<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>表单功能点管理</title>
<%@include file="/commons/include/form.jsp" %>
<f:link href="tree/zTreeStyle.css"></f:link>
<f:resources name="ROOT_PID" alias="ROOT_PID"></f:resources>
<script type="text/javascript"	src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript">
$(function() {	
	//布局
	loadLayout();
	//加载树
	loadTree();
});
//布局
function loadLayout(){
	$("#layout").ligerLayout( {
		leftWidth : 200,
		onHeightChanged: heightChanged,
		allowLeftResize:false
	});
	//取得layout的高度
    var height = $(".l-layout-center").height();
    $("#recTypeTree").height(height-90);
};
//布局大小改变的时候通知tab，面板改变大小
function heightChanged(options){
 	$("#recTypeTree").height(options.middleHeight -90);
};
var recTypeTree;
var rootId=0;
//加载树
function loadTree(){
	var setting = {
		async: {enable: false},
		data: {
			key:{name:"typeName"},
			simpleData: {
				enable: true,
				idKey: "typeId",
				pIdKey: "parentId",
				rootPId: "-1"
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
			beforeDrop: null,
			onDrop: null
		}
	};
	var url="${ctx}/oa/system/recType/getSystemTreeData.do";
	var params = {
		typeId:'${typeId}'
	};
	$.post(url,params,function(result){
		recTypeTree=$.fn.zTree.init($("#recTypeTree"), setting,result);
		recTypeTree.expandAll(false);
		var node = recTypeTree.getNodeByParam("typeId","0",null);
		recTypeTree.expandNode(node,true,false,false,false);
	});

};
//左击
function zTreeOnLeftClick(event, treeId, treeNode){
	var typeId=treeNode.typeId;
	if(typeId==rootId){
		return;
	}
	var url="${ctx}/oa/system/recRole/list.do?typeId="+treeNode.typeId;
	$("#listFrame").attr("src",url);
};
//展开收起
function treeExpandAll(type){
	recTypeTree = $.fn.zTree.getZTreeObj("recTypeTree");
	recTypeTree.expandAll(type);
};
//刷新
function reFresh(){
	loadTree();
};
</script>
</head>
<body >
<div id="layout" >
	<div position="left" title="角色所属类别" style="text-align: left;" >
<div class="tree-toolbar" id="pToolbar">	
				<div class="group"><a class="link expand" id="treeExpandAll" href="javascript:treeExpandAll(true)" >展开</a></div>
				
				<div class="group"><a class="link collapse" id="treeCollapseAll" href="javascript:treeExpandAll(false)" >收起</a></div>
			
		</div>
		<div id="recTypeTree" class="ztree" style="overflow:auto;clear:left"></div>
	</div>
	<div position="center">
		<iframe id="listFrame" src="" frameborder="no" width="100%" height="100%"></iframe>
	</div>
</div>
</body>
</html>

