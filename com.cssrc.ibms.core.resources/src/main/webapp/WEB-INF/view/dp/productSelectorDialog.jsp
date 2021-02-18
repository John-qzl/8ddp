<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>产品选择器--产品维度</title>
	<%@include file="/commons/include/form.jsp" %>
	<base target="_self"/> 
	<f:link href="Aqua/css/ligerui-all.css"></f:link>
	<link rel="stylesheet" href="${ctx }/jslib/tree/zTreeStyle.css" type="text/css" />
	<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerComboBox.js"></script>

	<script type="text/javascript">
		var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
		// 型号id、名称
		var moduleId = $.getParameter("moduleId");
		var moduleName = $.getParameter("moduleName");
		var isSingle = '${isSingle}'=='0'?true:false;
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
		var productSelectorTree;
		//加载树
		function loadTree(){
		
			var setting = {
				data: {
					key : {
						name: "name",
						title: "name"
					},
					simpleData: {
						enable: true,
						idKey: "id",
						pIdKey: "parentId",
						rootPId: "-1"
					}
				},
				view: {
					selectedMulti: false
				},
				check: check
				
			};
			//一次性加载
			var url="${ctx}/product/dimension/tree/getTreeData.do?flag=true";
			$.ajax({
				async : false,
				cache : false,
				type : 'POST',
				dataType : "json",
				data:{
					moduleId : moduleId,
		        	moduleName : moduleName
		        },
				url : url,
				success : function(data) {
					/* var result1 =  eval(result) ; */
					productSelectorTree=$.fn.zTree.init($("#productSelectorTree"), setting,data);
					var toolTree = $.fn.zTree.getZTreeObj("productSelectorTree");
					var treeNodes = toolTree.transformToArray(toolTree.getNodes());
					for (var i = 0; i < treeNodes.length; i++) {
						var element = treeNodes[i];
						if (element.type=='root') {
							element.icon ='xiaohuojian';

//							element.icon =__ctx + '/jslib/tree/img/diy/1_open.png';
						}
						if(element.type=='folder'){
							element.icon = 'file';
//							element.icon = __ctx + '/jslib/tree/img/diy/2.png';
						}
						if(element.type=='form'){
							element.icon = 'yulanbiaodan';
//							element.icon = __ctx + '/jslib/tree/img/diy/3.png';
						}
						toolTree.updateNode(element);
					}

					if(expandDepth!=0)
					{
						productSelectorTree.expandAll(true);
						var nodes = productSelectorTree.getNodesByFilter(function(node){
							return (node.level < expandDepth);
						});
						if(nodes.length>0){
							for(var i=0;i<nodes.length;i++){
								productSelectorTree.expandNode(nodes[i],true,false);
							}
						}
					}else productSelectorTree.expandAll(true);
				},
				error : function() {
					$.ligerDialog.error('产品维度结构树加载失败！');
				}
			});
			
		};
		//保存
		function select(){
			var productSelectorTree = $.fn.zTree.getZTreeObj("productSelectorTree");
			var nodes = productSelectorTree.getCheckedNodes(true);
			var ids="";var names="";
			$.each(nodes,function(i,n){
				if(n.type!="form"){
					return true;
				}
				ids+=n.id+",";
				names+=n.name+",";
			});
			
			ids=ids.substring(0,ids.length-1);	
			names=names.substring(0,names.length-1);
			if(dialog.options.sucCall){
				dialog.options.sucCall({ids:ids,names:names});
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
			<div id="productSelectorTree" class="ztree"></div>
			</div>
		</div>
	</div>
</body>
</html>

