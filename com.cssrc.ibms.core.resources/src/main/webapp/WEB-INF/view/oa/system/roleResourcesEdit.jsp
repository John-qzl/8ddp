<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>角色资源分配</title>
	<%@include file="/commons/include/form.jsp" %>
	<base target="_self"/> 
	<f:link href="Aqua/css/ligerui-all.css"></f:link>
	<f:resources name="ROOT_PID" alias="ROOT_PID"></f:resources>
	<f:resources name="ROOT_ID" alias="ROOT_ID"></f:resources>
	<%-- <link rel="stylesheet" href="${ctx }/jslib/tree/zTreeStyle.css" type="text/css" /> --%>
	<f:link href="tree/zTreeeStyle.css"></f:link>
	<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerComboBox.js"></script>
	<script type="text/javascript">
		/*KILLDIALOG*/
		var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
		//树展开层数
		var expandDepth = 2; 
		$(function()
		{	  loadTree();
			  $("a.save").click(save);
		});
	  
		//树
		var resourcesTree;
		//加载树
		function loadTree(){
		
			var setting = {
				data: {
					key : {
						name: "resName",
						title: "resName"
					},
					simpleData: {
						enable: true,
						idKey: "resId",
						pIdKey: "parentId",
						rootPId: "${ROOT_PID}"
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
			var url="${ctx}/oa/system/resources/getSysRolResTreeChecked.do?roleId=${roleId}";
			
			$.post(url,function(result){
				
				resourcesTree=$.fn.zTree.init($("#resourcesTree"), setting,eval(result));
				if(expandDepth!=0)
				{
					resourcesTree.expandAll(false);
					var nodes = resourcesTree.getNodesByFilter(function(node){
						return (node.level < expandDepth);
					});
					if(nodes.length>0){
						for(var i=0;i<nodes.length;i++){
							resourcesTree.expandNode(nodes[i],true,false);
						}
					}
				}else resourcesTree.expandAll(true);
			})
				
			
			
		};
		//保存
		function save(){
			//
			var resourcesTree = $.fn.zTree.getZTreeObj("resourcesTree");
			var nodes = resourcesTree.getCheckedNodes(true);
			var resIds="";
			$.each(nodes,function(i,n){
				if(n.resId!="${ROOT_ID}")resIds+=n.resId+",";
			});
			
			resIds=resIds.substring(0,resIds.length-1);
		
			var url="${ctx}/oa/system/roleResources/upd.do";
			var data= "roleId=${roleId}&resIds="+resIds;
			$.post(url,data,function(result){
				//
				var obj=new com.ibms.form.ResultMessage(result);
				if(obj.isSuccess()){
					$.ligerDialog.confirm('角色资源分配成功,是否继续?','提示信息',function(rtn){
						if(!rtn){
							dialog.close();
						}
					});
				}else{
					$.ligerDialog.warn('角色资源分配出错!');
				}
			})
		};
	</script>
<f:link href="from-jsp.css"></f:link></head>
<body>
    <div class="panel">
		<div class="panel-top">
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" id="btnSearch">保存</a></div>
				
				<div class="group"><a class="link del" onclick="javasrcipt:dialog.close()">关闭</a></div>
				</div>
				</div>
			</div>
		<div class="panel-body">
			<div class="panel-detail">
			<div id="resourcesTree" class="ztree"></div>
			</div>
		</div>
	</div>
</body>
</html>

