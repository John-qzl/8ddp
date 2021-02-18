<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>表单模板选择器</title>
	<%@include file="/commons/include/form.jsp" %>
	<base target="_self"/> 
	<f:link href="Aqua/css/ligerui-all.css"></f:link>
	<link rel="stylesheet" href="${ctx }/jslib/tree/zTreeStyle.css" type="text/css" />
	<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerComboBox.js"></script>
	<script type="text/javascript">
		/*KILLDIALOG*/
		var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
		//发次id  
		var projectId = dialog.options.params.projectId;
		var isSingle = dialog.options.params.isSingle;
		//树展开层数
		var expandDepth = 2; 
		$(function()
		{	  loadTree();
			  $("a.save").click(select);
		});
		
	  	var check;
	  	if(isSingle){
	  		check = {chkStyle:"radio",enable: true,radioType:"all"};
	  	}else{
	  		check = {chkboxType: { "Y": "ps", "N": "ps" },enable: true};
	  	}
		//树
		var dataPackageTree;
		//加载树
		function loadTree(){		
			var setting = {
				data: {
					key : {
						name: "F_JDMC",
						title: "F_JDMC"
					},
					simpleData: {
						enable: true,
						idKey: "ID",
						pIdKey: "F_PARENTID",
						rootPId: "-1"
					}
				},
				view: {
					selectedMulti: false
				},
				check: check
				
			};
			//一次性加载
			var url="${ctx}/dataPackage/tree/ptree/getTree.do?projectId="+projectId;
			
			$.post(url,function(result){
				var rootNode = {ID:"0",F_PARENTID:"-1",F_JDMC:"所有节点"};
				var nodes =JSON2.parse(result);
				nodes.push(rootNode);
				dataPackageTree=$.fn.zTree.init($("#dataPackageTree"), setting,nodes);				
				var nodes = dataPackageTree.getNodesByFilter(function(node){
					return (node.level >1);
				});
				for(var i=0;i<nodes.length;i++){
					var node = nodes[i];
					node.chkDisabled = true;
				}
				dataPackageTree.expandAll(true);
			})
				
			
			
		};
		//保存
		function select(){
			var dataPackageTree = $.fn.zTree.getZTreeObj("dataPackageTree");
			var nodes = dataPackageTree.getCheckedNodes(true);
			var ids="";
			$.each(nodes,function(i,n){
				if(n.ID=='0'){
					return true;
				}
				ids+=n.ID+",";
			});
			
			ids=ids.substring(0,ids.length-1);	
			if(dialog.options.sucCall){
				dialog.options.sucCall({ids:ids});
			}
			dialog.close();
		};
	</script>
<f:link href="from-jsp.css"></f:link></head>
<body>
    <div class="panel">
		<div class="panel-top">
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" id="btnSearch">确定</a></div>
				<div class="group"><a class="link del" onclick="javasrcipt:dialog.close()">关闭</a></div>
				</div>
				</div>
			</div>
		<div class="panel-body">
			<div class="panel-detail">
			<div id="dataPackageTree" class="ztree"></div>
			</div>
		</div>
	</div>
</body>
</html>

