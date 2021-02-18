<%@page pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>

<html>
<head>
<%@include file="/commons/include/form.jsp" %>

<title>明细多Tab</title>

<script type="text/javascript" src="${ctx}/js/dynamic.jsp"></script>
<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/util.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/form.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/base.js"></script>
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
var tree = null;
var ctxPath = "<%=request.getContextPath()%>";
var accordion = null;

var setting = {
	view : {
		showLine : true,
		nameIsHTML : true
	},
	data : {
		key : {
			name : "funName"
		},
		simpleData : {
			enable : true,
			idKey : "funId",
			pIdKey : "parentId"
		}
	},
	callback : {
		onClick : zTreeOnClick
	}
};

$(function() {
	//布局
	mian_layout_ = $("#layoutFunMain").ligerLayout( {
		topHeight : 80,
		leftWidth : 180,
		height : "100%"
		//onHeightChanged : heightChanged
	});
	//取得layout的高度
	var height = $(".l-layout-center").height();
	$("#funTree").height(height - 45);

	$("div.l-layout-collapse-left,div.l-layout-left")
			.css("margin", "2px 0 0 0");
	//Tab
	$("#frameCenter").ligerTab( {
		height : height,
		onBeforeSelectTabItem : function(tabid) {
			currTabId = tabid;
		}
	});

	//面板
	$("#accordion1").ligerAccordion( {
		height : height,
		speed : null
	});

	//获取tab的引用
	tab = $("#frameCenter").ligerGetTabManager();
	accordion = $("#accordion1").ligerGetAccordionManager();
	//加载菜单
	loadMenu();
	//隐藏加载对话框
	$("#pageloading").hide();

	$("#menuPanel").delegate(
			"a.menuItem",
			"click",
			function() {
				var id = $(this).attr("id");
				loadTree(id);
				$(this).siblings().removeClass("menuItem_hover").end()
						.addClass("menuItem_hover");
				jQuery.setCookie("selectTab", id);
			});

	window.onresize = function() {
		initRollButton()
	};

	//右下角提醒框
	/**SysPopupRemindUtil.show("", null, "300");**/
});

//布局大小改变的时候通知tab，面板改变大小
function heightChanged(options) {

	$("iframe").each(function() {
		var tabName = $(this).attr("name");
		if (tabName != undefined) {
			$(this).height(options.middleHeight - 35);
		}
		if (tabName == "home") {
			$(this).attr("src", $(this).attr("src"));
		}
	});
	$("#funTree").height(options.middleHeight - 40);
	if (tab) {
		var tabContent = $(".l-tab-content"), h = tabContent.height();
		tabContent.height(h + options.diff + 15);
	}
	//    tab.addHeight(options.diff);
	if (accordion && options.middleHeight - 25 > 0)
		accordion.setHeight(options.middleHeight - 25);

}

var aryTreeData = null;
//返回根节点
function getRootNodes() {
	var nodes = new Array();
	for ( var i = 0; i < aryTreeData.length; i++) {
		var node = aryTreeData[i];
		if (node.parentId == -1) {
			nodes.push(node);
		}
	}
	return nodes;
};
//初始化菜单滚动按钮
function initRollButton() {
	// 滚动 按钮宽度  =  窗体宽度 - 320 - div.welcome
	var welcomeWidth = 390;
	$("div.menuParent").width(parseInt($(window).width()) - welcomeWidth - 320);

	var pWidth = $("div.menuParent").width(), sWidth = $("div.menuPanel")
			.width();
	if (sWidth <= 0)
		return;
	var left = pWidth - sWidth;
	if (left <= 0) {
		$(".nav_left").show();
		$(".nav_right").show();
	} else {
		$(".nav_left").hide();
		$(".nav_right").hide();
	}
	$("div.menuPanel").css("left", 0);
};
//加载菜单面板
function loadMenu() {
	$("#funTree").empty();
	var url="${ctx}/oa/system/recFun/getRecRolResTreeData.do";
	var params = {
			typeAlias:"${typeAlias}",
			__pk__ : "${__pk__}"
		};
	//一次性加载
	$.post(url, params,function(result){
		aryTreeData = result;

		//获取根节点，加载顶部按钮菜单。
			var headers = getRootNodes();
			var len = headers.length;
			var menuContainer = $("#menuPanel");
			for ( var i = 0; i < len; i++) {
				var head = headers[i];
				var menuItemHtml = getMenuItem(head);
				menuContainer.append($(menuItemHtml));
			}
			initRollButton();
			if (len > 0) {
				var selectTab = jQuery.getCookie("selectTab");
				var obj = $("#" + selectTab);
				if (selectTab && obj.length > 0) {
					$("#" + selectTab).addClass("menuItem_hover");
					loadTree(selectTab);
				} else {
					var head = headers[0];
					var funId = head.funId;
					$("#" + funId).addClass("menuItem_hover");
					loadTree(funId);
				}
			}
		});
}

//加载资源树
function loadTree(funId) {
	var targetRes = $("#" + funId);
	var defaultUrl = targetRes.attr("url");
	if (defaultUrl && defaultUrl.length > 8) {
		if (!defaultUrl.startWith("http", false))
			defaultUrl = ctxPath + defaultUrl;
		addToTab(defaultUrl, targetRes.text().trim(), funId, targetRes
				.attr("icon"));
	}
	//加载树
	var nodes = new Array();
	getChildByParentId(funId, nodes);
	var zTreeObj = $.fn.zTree.init($("#funTree"), setting, nodes);

	//根据配置的是否展开
	if (nodes.length > 0) {
		mian_layout_.setLeftCollapse(false);
		for ( var idx = 0; idx < nodes.length; idx++) {
			zTreeObj.expandNode(nodes[idx],true, true);
		}
		//zTreeObj.expandAll(false);
	} else {
		mian_layout_.setLeftCollapse(true);
	}
	//打开窗口
	if (nodes.length > 0) {
		var firstTabId;
		for ( var idx = 0; idx < nodes.length-1; idx++) {
			var url = nodes[idx].defaultUrl;

			if (url != null && url != "" && url != "null") {
				url = newUrl(url,nodes[idx].funId);
				if (!url.startWith("http", false))
					url = ctxPath + url;
				if(nodes[idx].isOpen=="1"){
					addToTab(url,nodes[idx].funName,nodes[idx].funId,nodes[idx].icon);
					if($.isEmpty(firstTabId)){
						firstTabId = nodes[idx].funId;
					}
				}
			}
		}
		//默认在第一个窗口，且不能关闭
		tab.selectTabItem(firstTabId);
		$("li[tabid="+firstTabId+"]").find("div][class=l-tab-links-item-close]").remove();
	}
}
/**
 * 对url进行修改
 *1.列表-添加参数 ： 
 		fromMultiTabView-表明入口是明细多Tab
 		funId、typeAlias - 用于对按钮信息的控制；
 *2.详细-添加参数：
 		fromMultiTabView-表明入口是明细多Tab
 		funId、typeAlias - 用于对按钮信息的控制；
 */
 function newUrl(url,funId){
	  var params = {
	      typeAlias:"${typeAlias}",
	      pk : "${__pk__}",
	      dataTemplateId : "${__displayId__}"
	  };  
	  //添加fromMultiTabView
	  if(url.indexOf("&")>0||url.indexOf("=")>0){
	    url += "&fromMultiTabView=true";
	  }else{
	    if(url.indexOf("?")>0){
	      url += "fromMultiTabView=true";
	    }else{
	      url += "?fromMultiTabView=true";
	    }
	  }
	  //列表
	  if(url.indexOf("dataTemplate/preview.do")>0){
	    url+="&funId="+funId;
	    url+="&typeAlias="+params.typeAlias;
	    url+="&__pk__="+params.pk;
	  }
	  
	  if(url.indexOf("[pk]")){
	    url = url.replace("[pk]",params.pk);
	  }
	  if(url.indexOf("[typeAlias]")){
	    url = url.replace("[typeAlias]",params.typeAlias);
	  }
	  if(url.indexOf("[funId]")){
	    url = url.replace("[funId]",funId);
	  }
	  if(url.indexOf("[dataTemplateId]")){
	    url = url.replace("[dataTemplateId]",params.dataTemplateId);
	  }
	  return url;
	}
//加载顶部菜单项
function getMenuItem(node) {
	var url = node.defaultUrl ? node.defaultUrl : "";
	var str = "<a class=\"menuItem\" id=\"" + node.funId + "\" url=\"" + url
			+ "\" icon=\""+node.icon + "\">";
	if (node.icon != "null" && node.icon != "") {
		str += "<img src=\"" + node.icon + "\" />";
	}
	str += "<span >" + node.funName + "</span></a>";
	return str;
}
//关联父子节点
function getChildByParentId(parentId, nodes) {
	for ( var i = 0; i < aryTreeData.length; i++) {
		var node = aryTreeData[i];
		if (node.parentId == parentId) {
			nodes.push(node);
			getChildByParentId(node.funId, nodes);
		}
	}
}

//处理点击事件
function zTreeOnClick(event, treeId, treeNode) {
	var url = treeNode.defaultUrl;
	if (url != null && url != "" && url != "null") {
		url = newUrl(url,treeNode.funId);
		if (!url.startWith("http", false))
			url = ctxPath + url;
			//扩展了tab方法。
		if(treeNode.newOpen=="true"){
	          $.openFullWindow(url);
       	}
       	else{
       		//扩展了tab方法。
        	addToTab(url,treeNode.funName,treeNode.funId,treeNode.icon);
       	}
	}
}

//添加到tab或者刷新
function addToTab(url, txt, id, icon) {
	if (tab.isTabItemExist(id)) {
		tab.selectTabItem(id);
		tab.reload(id);
	} else {
		tab.addTabItem( {
			tabid : id,
			text : txt,
			url : url,
			icon : icon
		});
	}
}

// firefox下切换tab的高度处置
</script>
<style type="text/css">

#pageloading {
	position: absolute;
	left: 0px;
	top: 0px;
	background: white url("${ctx}/styles/${skinStyle}/images/loading.gif")
		no-repeat center;
	width: 100%;
	height: 100%;
	height: 700px;
	z-index: 99999;
}

</style>
</head>
<body style="padding: 0px;">
	<div id="layoutFunMain" style="margin: 0px 1px 0px 1px;">
		<div position="left" id="accordion1"
			title="表单详细">
			<ul id="funTree" class="ztree" style="overflow: auto; margin-top:2px;height: 96%"></ul>
		</div>
		<div position="center" id="frameCenter">
		</div>
	</div>
</body>
</html>
