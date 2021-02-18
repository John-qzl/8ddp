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
		var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	
		//树展开层数
		var expandDepth = 2; 
		$(function()
		{		loadFunTree();
				loadRoleTree();
			  $("a.save").click(save);
		});
	  
		//树
		var funTree;
		//加载树
		function loadFunTree(){
		
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
				}
				
			};
			//一次性加载
			var url="${ctx}/oa/system/recFun/getRecRolFunTreeChecked.do?typeId=${typeId}";
			$.post(url,function(result){
				
				funTree=$.fn.zTree.init($("#funTree"), setting,eval(result));
				if(expandDepth!=0)
				{
					funTree.expandAll(false);
					var nodes = funTree.getNodesByFilter(function(node){
						return (node.level < expandDepth);
					});
					if(nodes.length>0){
						for(var i=0;i<nodes.length;i++){
							funTree.expandNode(nodes[i],true,false);
						}
					}
				}else funTree.expandAll(true);
			})
		};
		
		var roleTree;
		
		//加载树
		function loadRoleTree(){
		
			var setting = {
				data: {
					key : {
						name: "roleName",
						title: "roleName"
					},
					simpleData: {
						enable: true,
						idKey: "roleId",
						rootPId: null
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
			//
			//一次性加载
			var url="${ctx}/oa/system/recRole/getRoleTreeData.do?typeId=${typeId}";
			$.post(url,function(result){
				
				roleTree=$.fn.zTree.init($("#roleTree"), setting,eval(result));
				if(expandDepth!=0)
				{
					roleTree.expandAll(false);
					var nodes = roleTree.getNodesByFilter(function(node){
						return (node.level < expandDepth);
					});
					if(nodes.length>0){
						for(var i=0;i<nodes.length;i++){
							roleTree.expandNode(nodes[i],true,false);
						}
					}
				}else roleTree.expandAll(true);
			})
		};
		
		//保存
		function save(){
			var funTree = $.fn.zTree.getZTreeObj("funTree");
			var resNodes = funTree.getCheckedNodes(true);
			var funIds="";
			$.each(resNodes,function(i,n){
				if(n.funId!="0")funIds+=n.funId+",";
			});
			
		
			funIds=funIds.substring(0,funIds.length-1);
			if(funIds==""){
				$.ligerDialog.confirm('请选择功能点','提示信息');
				return;
			}
			var roleTree = $.fn.zTree.getZTreeObj("roleTree");
			var roleNodes = roleTree.getCheckedNodes(true);
			var roleIds="";
			$.each(roleNodes,function(i,n){
				if(n.funId!="0")roleIds+=n.roleId+",";
			});
			
			roleIds=roleIds.substring(0,roleIds.length-1);
			
			if(roleIds==""){
				$.ligerDialog.confirm('请选择角色','提示信息');
				return;
			}
			var url="${ctx}/oa/system/recRoleFun/saveBatch.do";
			var data= "&funIds="+funIds+"&roleIds="+roleIds;
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
			<div id="funTree" class="ztree" style="height: 325px;"></div>
		</div>
		<div id="fieldSetting" position="right"   style="width: 49%;float:left;border: solid 1px #cacaca;">
		 	<div class="treeTitle">角色树</div>
			<div id="roleTree" class="ztree" style="height: 325px;"></div>
		</div>
	 </div> 
	 
</body>
</html>

