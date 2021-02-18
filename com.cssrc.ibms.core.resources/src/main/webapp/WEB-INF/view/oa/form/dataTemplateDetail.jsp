<%@page pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<%@include file="/commons/include/customForm.jsp"%>
<f:link href="from-jsp.css"></f:link>
<script type="text/javascript"
	src="${ctx}/jslib/lg/plugins/ligerDialog.js"></script>
<script type="text/javascript"
	src="${ctx}/jslib/lg/plugins/ligerDrag.js"></script>
<script type="text/javascript"
	src="${ctx}/jslib/lg/plugins/ligerLayout.js"></script>
<script type="text/javascript"
	src="${ctx}/jslib/lg/plugins/ligerMenu.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerTab.js"></script>
<script type="text/javascript"
	src="${ctx}/jslib/lg/plugins/ligerAccordion.js"></script>
<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript">

var mian_layout_;
var tab = null;
var ctxPath = "<%=request.getContextPath()%>";
var accordion = null;
var setting = {
	view : {
		showLine : true,
		nameIsHTML : true
	},
	data : {
		key : {
			name : "resName"
		},
		simpleData : {
			enable : true,
			idKey : "resId",
			pIdKey : "parentId"
		}
	},
	callback : {
		onClick : zTreeOnClick
	}
};

$(function() {
	//布局
	mian_layout_ = $("#layoutMain").ligerLayout( {
		topHeight : 80,
		leftWidth : 180,
		height : '100%',
		onHeightChanged : heightChanged
	});
	//取得layout的高度
	var height = $(".l-layout-center").height();
	$("#leftTree").height(height - 45);

	$("div.l-layout-collapse-left,div.l-layout-left").css("margin", "2px 0 0 0");
	
	/* 当不想用一级tab请注释该tab高度  */
 	$("#framecenter").ligerTab( {
		height : height
	}); 

	//树面板
	$("#accordion1").ligerAccordion( {
		height : height,
		speed : null
	});

	//获取tab的引用
	tab = $("#framecenter").ligerGetTabManager();
	accordion = $("#accordion1").ligerGetAccordionManager();
	//加载菜单
	loadMenu();
	//隐藏加载对话框
	$("#pageloading").hide();
	$(".l-layout-header").html("<input type=\"button\" class=\"backBtn\" value=\"返回\" onclick=\"backTempList()\"/>");
});
//布局大小改变的时候通知tab，面板改变大小
function heightChanged(options) {
	$("#leftTree").height(options.middleHeight - 40);
	if (tab) {
		var tabContent = $(".l-tab-content"), h = tabContent.height();
		tabContent.height(h + options.diff + 15);
	}
	if (accordion && options.middleHeight - 25 > 0)
		accordion.setHeight(options.middleHeight - 25);

};

var aryTreeData = null;
//返回根节点
function getRootNodes() {
	var nodes = new Array();
	for ( var i = 0; i < aryTreeData.length; i++) {
		var node = aryTreeData[i];
		if (node.alias == '${alias}') {//by yangbo 修改成变量
			nodes.push(node);
		}
	}
	return nodes;
};

//加载菜单面板
function loadMenu() {
	$("#leftTree").empty();
	//一次性加载
	$.post("${ctx}/oa/console/getTreeByAlias.do?alias=${alias}", function(result) {
			aryTreeData = result;
		//获取根节点，加载顶部按钮菜单。
			var headers = getRootNodes();
			var head = headers[0];
			var resId = head.resId;
			loadTree(resId);
		});
};

//加载资源树
function loadTree(resId) {
	//加载树
	var nodes = new Array();
	getChildByParentId(resId, nodes);
	
	var zTreeObj = $.fn.zTree.init($("#leftTree"), setting, nodes);

	//根据配置的是否展开
	if (nodes.length > 0) {
		mian_layout_.setLeftCollapse(false);
		var firstTabId = null;
		for ( var idx = 0; idx < nodes.length; idx++) {
				zTreeObj.expandNode(nodes[idx],true, true);
				var url = ctxPath + nodes[idx].defaultUrl;
				//初始化加载一级tab
				addToTab(url,nodes[idx].resName,nodes[idx].resId,nodes[idx].icon);
				if(idx ==0){
					firstTabId = nodes[idx].resId;
					/* 当不想用一级tab请用  */
					//$('#tabFrame').attr("src", url);
				}
		}
		tab.selectTabItem(firstTabId);
	} else {
		mian_layout_.setLeftCollapse(true);
	}
};
//关联父子节点
function getChildByParentId(parentId, nodes) {
	for ( var i = 0; i < aryTreeData.length; i++) {
		var node = aryTreeData[i];
		if (node.parentId == parentId) {
			var preUrl =  node.defaultUrl;
			if(preUrl != null){
				if(preUrl.indexOf("detailData") != -1){
					node.defaultUrl += '&__pk__=${pkId}';
				}
				if(preUrl.indexOf("preview") != -1&&preUrl.indexOf("&Q_") != -1){
					node.defaultUrl += '${pkId}';
				}
			}
			nodes.push(node);
			getChildByParentId(node.resId, nodes);
		}
	}
};
//处理点击事件
function zTreeOnClick(event, treeId, treeNode) {
	var url = treeNode.defaultUrl;
	if (url != null && url != '' && url != 'null') {
		if (!url.startWith("http", false))
			url = ctxPath + url;
			//扩展了tab方法。
		if(treeNode.newOpen=="true"){
	            		$.openFullWindow(url);
	            	}
	            	else{
	            		//扩展了tab方法。
		            	addToTab(url,treeNode.resName,treeNode.resId,treeNode.icon);
	            		/* 当不想用一级tab请用  */
	            		/* $('#tabFrame').attr("src", url); */
	            	}
	}
};

//添加到tab或者刷新
function addToTab(url, txt, id, icon) {
	if (tab.isTabItemExist(id)) {
		tab.selectTabItem(id);
		tab.reload(id);
	} else {
		//tab.removeAll();//移除节点下tab
		tab.addTabItem( {
			tabid : id,
			text : txt,
			url : url,
			icon : icon
		});
	}
};
//返回上页
function backTempList(){
	window.location.href='${returnUrl}';
}

</script>
</head>

<body>
	<div id="layoutMain" style="margin: 0px 1px 0px 1px;" allowLeftResize="false">
		<div position="left" id="accordion1">
			<ul id='leftTree' class='ztree' style="overflow: auto; height: 100%"></ul>
		</div>
		<div position="center" id="framecenter">
			<!-- 当不想用一级tab请用 -->
			<!-- <iframe id="tabFrame" name="tabFrame"  height="100%"  width="100%" frameborder="0"></iframe> -->
		</div>
	</div>
</div>
</body>



</html>