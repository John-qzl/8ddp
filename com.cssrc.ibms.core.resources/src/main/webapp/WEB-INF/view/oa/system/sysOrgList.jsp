<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>组织架构管理</title>
<%@include file="/commons/include/get.jsp"%>
<f:link href="tree/zTreeStyle.css"></f:link>

<script type="text/javascript" src="${ctx }/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysOrgSearch.js"></script>
<script type="text/javascript">
	var orgTree; //树
	var menu;
	//普通管理员菜单
	var menu_user;
	var menu_root;
	//已逻辑删除的菜单
	var menu_LogicDel;
	var height;
	var expandDepth = 2;
	var type ="system";
	var typeVal="all";
	var isRight = ${isRight};
	var isImplemnet = ${isImplemnet};
	//左击的树节点
	var selectNode;

	$().ready(function (){
		$("#layout").ligerLayout({
			leftWidth : 225,
			height : '100%',
			allowLeftResize : false
		});

		height = $('#layout').height();
		$("#viewFrame").height(height - 25);
		$('#demensionId').change(function() {
			var demensionId = $(this).val();
			loadTree(1);
		});
		$("#treeReFresh").click(function() {
			var demensionId = $("#demensionId").val();
			loadTree(1);
		});

		$("#treeExpand").click(function() {
			orgTree = $.fn.zTree.getZTreeObj("orgTree");
			var treeNodes = orgTree.transformToArray(orgTree.getNodes());
			for(var i=0;i<treeNodes.length;i++){
				if(treeNodes[i].children){
					orgTree.expandNode(treeNodes[i], true, true, false);
				}
			}
		});
		$("#treeCollapse").click(function() {
			orgTree.expandAll(false);
		});
		//查询
		$("#treeSearch").click(function(){
			SysOrgSearch({callback:selectOrg});
		});
		//菜单
		getMenu();
		//首先加载行政维度
		loadTree(1);
		$("#demensionId").val(1);
		$("#orgTree").height(height-95);
	});

	function selectOrg(orgId){
		debugger;
		var node=orgTree.getNodeByParam("orgId", orgId, null);
		orgTree.selectNode(node);
		$("#viewFrame").attr("src", "get.do?orgId=" + orgId);
	}

	//刷新
	function refreshNode(){
		debugger;
		var selectNode=getSelectNode();
		reAsyncChild(selectNode);
	};
	//刷新节点
	function reAsyncChild(targetNode){
		
		var orgId=targetNode.orgId;
		if(orgId==0){
			loadTree(selid);
		}else{
			orgTree = $.fn.zTree.getZTreeObj("orgTree");
			if(targetNode.isParent){
				orgTree.reAsyncChildNodes(targetNode, "refresh", false);
			}else{
				var targetParentNode = orgTree.getNodeByParam("orgId",targetNode.orgSupId, null);
				orgTree.reAsyncChildNodes(targetParentNode, "refresh", false);
			}
		}
	};

	function loadTree(selid) {
		var setting = {
				data: {
					key : {
						name: "orgName" // orgPathname
					},

					simpleData: {
						enable: true,
						idKey: "orgId",
						pIdKey: "orgSupId",
						rootPId: 0
					}
				},
				// 拖动
				edit : {
					enable : true,
					showRemoveBtn : false,
					showRenameBtn : false,
					drag : {
						prev : true,
						inner : true,
						next : true,
						isMove : true,
						isCopy : true
					}
				},
				view : {
					selectedMulti : false
				},
				async: {
					enable: true,
					url:"${ctx}/oa/system/sysOrg/getTreeDataManage.do?demId="+1+"&type="+type+"&typeVal="+typeVal,
					autoParam:["orgId","orgSupId"],
					dataFilter: filter
				},
				callback:{
					onClick : zTreeOnLeftClick,
					onRightClick : zTreeOnRightClick,
					beforeDrop : beforeDrop,
					onDrop : onDrop,
					onAsyncSuccess: zTreeOnAsyncSuccess
				}

			};
			orgTree=$.fn.zTree.init($("#orgTree"), setting);
	};

	//过滤节点
	function filter(treeId, parentNode, childNodes) {
			if (!childNodes) return null;
			for (var i=0, l=childNodes.length; i<l; i++) {
				var node = childNodes[i];
				if (node.isRoot == 1) {
					node.icon = __ctx + "/styles/default/images/icon/root.png";
				} else {
					if (node.ownUser == null || node.ownUser.length < 1) {
					//	node.orgName += "[未]";
					}
// 					setIcon(node);
				}
			}
			return childNodes;
	};

	//判断是否为子结点,以改变图标
	function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
		orgTree = $.fn.zTree.getZTreeObj("orgTree");

		var treeNodes = orgTree.transformToArray(orgTree.getNodes());
		for (var i = 0; i < treeNodes.length; i++) {
			var element = treeNodes[i];
			if (element.isLeaf==0) {
				element.icon = 'folder';
			} else if(element.level==0){
				element.icon = 'closefolder';
			} else{
				element.icon = 'expandfolder';
			}
			orgTree.updateNode(element);
		}
	};

	function setIcon(node){
		if(node.iconPath.length>0)
			node.icon ='folder' ;





	}

	//拖放 前准备
	function beforeDrop(treeId, treeNodes, targetNode, moveType) {

		if (!treeNodes)
			return false;
		if (targetNode.isRoot == 1)
			return false;
		return true;
	};

	//左击事件
	function zTreeOnLeftClick(event, treeId, treeNode) {
		//对于已经逻辑删除的组织架构不显示信息
		if(treeNode.isDelete == 1){
			$("#viewFrame").attr("src", __ctx+"/oa/system/sysOrg/getEmpty.do?isDelete=1");
		}else{
			// var isRoot = treeNode.isRoot;
			// if (isRoot == 1) {
			// 	return;
			// }
			selectNode=treeNode
			var orgId = treeNode.code;
			$("#viewFrame").attr("src", "get.do?orgId=" + orgId);
		}
	};

	/**
	 * 右击事件
	 */
	function zTreeOnRightClick(e, treeId, treeNode) {
		orgTree.selectNode(treeNode);
		menu.hide();
		menu_user.hide();
	 	menu_root.hide();
	 	menu_LogicDel.hide();
	 	//对于权限管理员，无右击权限
	 	if(isRight){

	 	}else if(isImplemnet){//对于系统实施人员较高
	 		if (treeNode.isRoot == 1) {//根节点时，把删除和编辑隐藏掉
				menu_root.show({
					top : e.pageY,
					left : e.pageX
				});
			} else if(treeNode.isDelete == 1){//非根节点，并且已被逻辑删除
				justifyRightClickPosition(e);
		 		menu_LogicDel.show({
		 			top : e.pageY,
					left : e.pageX
		 		});
		 	} else {
				justifyRightClickPosition(e);
				menu.show({
					top : e.pageY,
					left : e.pageX
				});
			}
	 	}else{//普通管理用户
	 		if (treeNode.isRoot == 1) {//根节点时，把删除和编辑隐藏掉
				menu_root.show({
					top : e.pageY,
					left : e.pageX
				});
			} else if(treeNode.isDelete == 1){//非根节点，并且已被逻辑删除
				justifyRightClickPosition(e);
		 		menu_LogicDel.show({
		 			top : e.pageY,
					left : e.pageX
		 		});
		 	} else {
				justifyRightClickPosition(e);
				menu_user.show({
					top : e.pageY,
					left : e.pageX
				});
			}
	 	}
	};

	//右键菜单
	function getMenu() {
		var menus = [
						{
							text : '增加组织',
							click : addNode
						}, {
							text : '编辑',
							click : editNode
						},
            // {
			// 				text : '参数属性',
			// 				click : orgParam
			// 			},
            {
							text : '排序',
							click : sortNode
						}, {
							text : '物理删除',
							click : delNode
						},  {
							text : '逻辑删除',
							click : delLogicNode
						}, {
							text : '刷新',
							click : refreshNode
						},{
							text : '增加岗位',
							click : addPosNode
						}
		             ];

		menu_user = $.ligerMenu({
			top : 100,
			left : 100,
			width : 100,
			items : menus
		});

		//当前管理员设置只分给系统实施人员
		menus.push({
			text : '管理员设置',
			click : orgAuth
			});

		menu = $.ligerMenu({
			top : 100,
			left : 100,
			width : 100,
			items : menus
		});


		menu_LogicDel = $.ligerMenu({
			top : 100,
			left : 100,
			width : 100,
			items:<f:menu>
				[
				  {
					text : '还原',
					click : 'restore',
					alias:''
				}, {
					text : '物理删除',
					click : 'delNode',
					alias:'delOrg'
				}
				]
				</f:menu>
		});

		menu_root = $.ligerMenu({
			top : 100,
			left : 100,
			width : 100,
			items : [ {
				text : '增加组织',
				click : addNode
			},{
				text : '排序',
				click : sortNode
			},{
                text : '编辑',
                click : editNode
            } ]
		});
	};
	//编辑组织分级管理员
	function orgAuth() {
		orgTree = $.fn.zTree.getZTreeObj("orgTree");
		var nodes = orgTree.getSelectedNodes();
		var treeNode = nodes[0];
		var orgId = treeNode.orgId;
		var url = __ctx + "/oa/system/orgAuth/list.do?orgId="+ orgId;
		$("#viewFrame").attr("src", url);
	};
	//编辑组织参数属性
	function orgParam() {
		orgTree = $.fn.zTree.getZTreeObj("orgTree");
		var nodes = orgTree.getSelectedNodes();
		var treeNode = nodes[0];
		var orgId = treeNode.orgId;
		var url = __ctx + "/oa/system/sysOrgParam/editByOrgId.do?orgId="+ orgId;
		$("#viewFrame").attr("src", url);
	};
	//排序
	function sortNode(){
		//关闭菜单
	 	menu.hide();
	 	menu_root.hide();
	 	menu_LogicDel.hide();
		var treeNode=getSelectNode();
		var orgId = treeNode.orgId;
		var demId = treeNode.demId;
		var url=__ctx +'/oa/system/sysOrg/sortList.do?orgId='+orgId+"&demId="+demId;
	 	url=url.getNewUrl();
	 	DialogUtil.open({
	 		height:400,
	 		width: 600,
	 		title : '排序',
	 		url: url,
	 		isResize: true,
	 		//自定义参数
	 		sucCall:function(rtn){
	 			var demensionId = $("#demensionId").val();
	 			loadTree(demensionId);
	 		}
	 	});

	}
	function getSelectNode(){
		orgTree = $.fn.zTree.getZTreeObj("orgTree");
		var nodes = orgTree.getSelectedNodes();
		var treeNode = nodes[0];
		return treeNode;
	}

	function gradeManage(){
		var treeNode=getSelectNode();
		var orgId = treeNode.orgId;
		var url = __ctx + "/oa/system/grade/manage.do?orgId="+ orgId;
		$("#viewFrame").attr("src", url);
	}

	 //增加岗位
	function addPosNode() {
		var treeNode=getSelectNode();
		var orgId = treeNode.orgId;
		var demId = treeNode.demId;
		var url = __ctx + "/oa/system/position/edit.do?orgId=" + orgId + "&demId=" + demId + "&action=add";
		$("#viewFrame").attr("src", url);
	};

     //增加组织
	function addNode() {
		var treeNode=getSelectNode();
		var orgId = treeNode.orgId;
		var demId = treeNode.demId;
		var url = "edit.do?orgId=" + orgId + "&demId=" + demId + "&action=add";
		$("#viewFrame").attr("src", url);
	};

	//编辑节点
	function editNode() {
		var treeNode=getSelectNode();
		var orgId = treeNode.orgId;//如果是新增时它就变成父节点Id
		var demId = treeNode.demId;
		var url = "edit.do?orgId=" + orgId + "&demId=" + demId + "&flag=upd";
		$("#viewFrame").attr("src", url);
	};

	//还原被逻辑删除的组织
	function restore() {
		var treeNode=getSelectNode();
		var callback = function(rtn) {
			if (!rtn) return;
			var params = "orgId=" + treeNode.orgId;
			$.post("restore.do", params, function(data) {
				var json =JSON.parse(data);
				if(json.result=='1'){
					$.ligerDialog.success(json.message);
					reAsyncChild(treeNode);
				}else{
					$.ligerDialog.warn(json.message);
				}
			});
		};
		$.ligerDialog.confirm("确认要还原已逻辑删除的组织架构吗？", '提示信息', callback);

	};

	function delNode() {
		var treeNode=getSelectNode();
		var callback = function(rtn) {
			if (!rtn) return;
			var params = "orgId=" + treeNode.orgId;
			$.post("orgdel.do", params, function(data) {
				//alert(data);
				var json =JSON.parse(data);
				if(json.result=='1'){
					orgTree.removeNode(treeNode);
					$.ligerDialog.success(json.message);
				}else{
					$.ligerDialog.warn(json.message);
				}
				//orgTree.removeNode(treeNode);
			});
		};
		$.ligerDialog.confirm("确认要物理删除此组织吗，其下组织也将被删除？", '提示信息', callback);

	};

	//逻辑删除组织，将是否删除的状态设置为已删除，为"1"
	function delLogicNode() {
		var treeNode=getSelectNode();
		var callback = function(rtn) {
			if (!rtn) return;
			var params = "orgId=" + treeNode.orgId;
			$.post("orgLogicdel.do", params, function(data) {
				var json =JSON.parse(data);
				if(json.result=='1'){
					//orgTree.removeNode(treeNode);
					$.ligerDialog.success(json.message);
					reAsyncChild(treeNode);
				}else{
					$.ligerDialog.warn(json.message);
				}
			});
		};
		$.ligerDialog.confirm("确认要逻辑删除此组织吗，其下组织也将被删除？", '提示信息', callback);

	};

	//拖放 后动作
	function onDrop(event, treeId, treeNodes, targetNode, moveType) {
		if (targetNode == null || targetNode == undefined)	return;
		var targetId = targetNode.orgId;
		var dragId = treeNodes[0].orgId;
		var url = __ctx + "/oa/system/sysOrg/move.do";
		var params = {
			targetId : targetId,
			dragId : dragId,
			moveType : moveType
		};

		$.post(url, params, function(result) {
			var demensionId = $("#demensionId").val();
			loadTree(demensionId);
		});
	}
</script>
</head>
<body>

	<div id="layout" style="bottom: 1; top: 1">
		<div position="left" title="组织机构管理" id="rogTree"
			style="height: 100%; width: 100% !important;">
<%--			<div style="width: 100%;">--%>
<%--				<select id="demensionId" style="width: 99.8% !important;">--%>
<%--					<!-- <option value="0">---------全部---------</option> -->--%>
<%--					<c:forEach var="dem" items="${demensionList}">--%>
<%--						<option value="${dem.demId}">${dem.demName}</option>--%>
<%--					</c:forEach>--%>
<%--				</select>--%>
<%--			</div>--%>
			<div class="tree-toolbar" id="pToolbar">
				<div class="toolBar">
					<div class="group" >
						<a class="link reload" id="treeReFresh" style="margin-left:1px">刷新</a>
					</div>
					<div class="group" >
						<a class="link expand" id="treeExpand" style="margin-left:1px">展开</a>
					</div>
					<div class="group" >
						<a class="link collapse" id="treeCollapse" style="margin-left:1px">收起</a>
					</div>
					<div class="group" >
						<a class="link search" id="treeSearch" style="margin-left:1px">搜索</a>
					</div>
				</div>
			</div>
			<ul id="orgTree" class="ztree" style="overflow:auto;"></ul>
		</div>
		<div position="center" id="orgView" style="height: 100%;">
			<div class="l-layout-header">组织架构信息</div>
			<iframe id="viewFrame" src="getEmpty.do" frameborder="0" width="100%"
				height="100%" scrolling="auto"></iframe>
		</div>
	</div>

</body>
</html>


