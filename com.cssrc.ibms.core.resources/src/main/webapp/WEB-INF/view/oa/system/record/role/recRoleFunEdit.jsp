<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>角色功能点分配</title>
	<%@include file="/commons/include/form.jsp" %>
	<base target="_self"/> 
	<f:link href="Aqua/css/ligerui-all.css"></f:link>
	<link rel="stylesheet" href="${ctx }/jslib/tree/zTreeStyle.css" type="text/css" />
	<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerComboBox.js"></script>
	<script type="text/javascript">
		/*KILLDIALOG*/
		var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
		var leftTree,rightTree;
		//当前按钮树信息，{funId：,buttons:}
		var buttonTree = {};
		//按钮树信息缓存区
		var buttonTreesCache = [];
		//树展开层数
		var expandDepth = 2; 
		$(function()
		{	 loadLeftTree();
			 $("a.save").click(save);
		});	
		//加载树
		function loadLeftTree(){
			var setting = {
				data: {
					key : {
						name: "funName",
						title: "funName"
					},
					simpleData: {
						enable: true,
						idKey: "funId",
						pIdKey: "parentId",
						rootPId: "-1"
					}
				},
				view: {
					selectedMulti: true
				},
				check: {
					enable: true,
					chkboxType: { "Y": "ps", "N": "s" }
				},
				callback:{
					onClick: zTreeOnLeftClick,
					beforeDrop: null,
					onDrop: null
				}
				
			};
			//一次性加载
			var url="${ctx}/oa/system/recFun/getRecRolFunTreeChecked.do?roleId=${roleId}&typeId=${typeId}";			
			$.post(url,function(result){				
				leftTree=$.fn.zTree.init($("#leftTree"), setting,eval(result));
				if(expandDepth!=0)
				{
					leftTree.expandAll(false);
					var nodes = leftTree.getNodesByFilter(function(node){
						return (node.level < expandDepth);
					});
					if(nodes.length>0){
						for(var i=0;i<nodes.length;i++){
							leftTree.expandNode(nodes[i],true,false);
						}
					}
				}else leftTree.expandAll(true);
			})			
		};
		function zTreeOnLeftClick(){
			//查找下个按钮信息前： 把当前按钮信息更新到buttonsCache中；
			if(!$.isEmpty(rightTree)){
				updateCache();
			}
			var funNode = getSelectNode();
			var funId = funNode.funId;
			//提交前先查看缓存区，是否有按钮信息（有-直接获取，没有-请求后台）
			var res = checkCache(funId);
			if(res.flag){
				loadRightTree(funId,res.buttons);
				buttonTree.funId = funId;
				buttonTree.buttons = res.buttons;
			}else{
				getRightTreeServer(funId);
			}
		}
		function checkCache(funId){
			var result = {};
			var flag = false;
			if(!$.isEmpty(buttonTreesCache)){
				for(var i=0;i<buttonTreesCache.length;i++){
					var cache_funId = buttonTreesCache[i].funId;
					if(cache_funId==funId){
						flag = true;
						result.buttons = buttonTreesCache[i].buttons;
						break;
					}
				}
			}
			result.flag = flag;
			return result;
		}
		//将访问后台的返回数据进行缓存
		function addCache(){
			var bt_clone = $.cloneObject(buttonTree);
			buttonTreesCache.push(bt_clone);
			var buttonArr = JSON2.stringify(buttonTreesCache);
			$('#buttonRight').val(buttonArr);
		}
		//当用户再次点击功能点时，进行更新
		function updateCache(){
			var funId = buttonTree.funId;
			var buttons = [];
			var nodes = rightTree.getNodes();
			for(var i=0;i<nodes.length;i++){
				var button = {};
				button.id = nodes[i].id;
				button.parentId = nodes[i].parentId;
				button.checked = nodes[i].checked;
				button.desc = nodes[i].desc;
				buttons.push(button);
				var c_nodes = nodes[i].children;
				if(c_nodes){
					for(var j=0;j<c_nodes.length;j++){
						var button = {};
						button.id = c_nodes[j].id;
						button.parentId = c_nodes[j].parentId;
						button.checked = c_nodes[j].checked;
						button.desc = c_nodes[j].desc;
						buttons.push(button);
					}
				}
			}
			if(!$.isEmpty(buttonTreesCache)){
				for(var i=0;i<buttonTreesCache.length;i++){
					var cache_funId = buttonTreesCache[i].funId;
					if(cache_funId==funId){
						buttonTreesCache[i].buttons = buttons;
					}
				}
			}
			var buttonArr = JSON2.stringify(buttonTreesCache);
			$('#buttonRight').val(buttonArr);
			
		}
		//选择资源节点。
		function getSelectNode(){
			leftTree = $.fn.zTree.getZTreeObj("leftTree");
			var nodes  = leftTree.getSelectedNodes();
			var node   = nodes[0];
			return node;
		}
		//从客户端取得按钮信息
		function getRightTreeServer(funId){
			var url="${ctx}/oa/system/recFun/getButtonTreeChecked.do?roleId=${roleId}&funId="+funId;	
			$.post(url,function(result){
				var parentButton = {};
				parentButton.id = 0;
				parentButton.parentId = -1;
				parentButton.checked = "false";
				parentButton.desc=	"按钮";
				
				var buttons = eval(result);
				buttons.push(parentButton);
				buttonTree.funId = funId;
				buttonTree.buttons = buttons;
				loadRightTree(funId,buttons)
				addCache();
			});
		}
		//点击左侧功能点，右侧展示按钮信息
		function loadRightTree(funId,buttons){

			var setting = {
					data: {
						key : {
							name: "desc",
							title: "desc"
						},
						simpleData: {
							enable: true,
							idKey: "id",
							pIdKey: "parentId",
							rootPId: "-1"
						}
					},
					view: {
						selectedMulti: true
					},
					check: {
						enable: true,
						chkboxType: { "Y": "ps", "N": "s" }
					}
				};
			    rightTree=$.fn.zTree.init($("#rightTree"), setting,buttons);
				if(expandDepth!=0)
				{
					rightTree.expandAll(false);
					var nodes = rightTree.getNodesByFilter(function(node){
						return (node.level < expandDepth);
					});
					if(nodes.length>0){
						for(var i=0;i<nodes.length;i++){
							rightTree.expandNode(nodes[i],true,false);
						}
					}
				}else rightTree.expandAll(true);
		}
		//保存
		function save(){
			//保存时更新当前缓存
			if(!$.isEmpty(rightTree)){
				updateCache();
			}
			var data = [];
			var nodes = leftTree.getCheckedNodes(true);
			$.each(nodes,function(i,n){				
				var singleData = {};	
				if(n.funId!="-1"){
					singleData.funId = n.funId;
					for(var i=0;i<buttonTreesCache.length;i++){
						var cache_funId = buttonTreesCache[i].funId;
						var cache_buttons = buttonTreesCache[i].buttons;
						if(cache_funId==n.funId){
							//将选中的按钮保存
							var buttons = [];
							for(var i=0;i<cache_buttons.length;i++){
								var cache_button = cache_buttons[i];
								if(cache_button.id=="0"){
									continue;
								}
								buttons.push(cache_button);
							}
							singleData.buttons = buttons;
							break;
						}
					}	
					data.push(singleData);
				}
			});	
			var dataJson = JSON2.stringify(data);
			var url="${ctx}/oa/system/recRoleFun/upd.do";
			var data= "roleId=${roleId}&dataJson="+dataJson;
			$.post(url,data,function(result){
				var obj=new com.ibms.form.ResultMessage(result);
				if(obj.isSuccess()){
					$.ligerDialog.confirm('角色功能点分配成功,是否继续?','提示信息',function(rtn){
						if(!rtn){
							dialog.close();
						}
					});
				}else{
					$.ligerDialog.warn('角色功能点分配出错!');
				}
			})
		};
	</script>
<f:link href="from-jsp.css"></f:link></head>
<body>
	<div class="panel-top">
		<div class="tbar-title">
			<span class="tbar-label">功能点分配</span>
		</div>
		<div class="panel-toolbar">
			<div class="toolBar">
				<div class="group"><a class="link save" id="btnSearch">保存</a></div>
				
				<div class="group"><a class="link del" onclick="javasrcipt:dialog.close()">关闭</a></div>
			</div>	
		</div>
	</div>
	 <div class="panel-detail">
	  	<div position="left"  style="width: 49%;float:left;border: solid 1px #cacaca;margin-right: 5px;">
	  		<div class="treeTitle">功能点树</div>
			<div id="leftTree" class="ztree" style="height: 325px;"></div>
		</div>
		<div position="right"   style="width: 49%;float:left;border: solid 1px #cacaca;">
		 	<div class="treeTitle">按钮树</div>
			<div id="rightTree" class="ztree" style="height: 325px;"></div>
		</div>
	 </div>
	 <div class="panel-detail">
		<textarea style="display:none;" id="buttonRight" name="buttonRight" ></textarea>
	</div> 
</body>
</html>

