<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>表单定义管理</title>
<%@include file="/commons/include/form.jsp" %>
<f:link href="from-jsp.css"></f:link>
<f:gtype name="CAT_FORM" alias="CAT_FORM"></f:gtype>
<f:link href="tree/zTreeStyle.css"></f:link>
<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/GlobalType.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/FormDefMenu.js"></script>
	
	<script type="text/javascript">
		var catKey="${CAT_FORM}";
		var curMenu=null;
		
		var globalType=new GlobalType(catKey,"glTypeTree",
			{onClick:onClick,
			onRightClick:zTreeOnRightClick
			/* ,expandByDepth:2 */
			});
		var formDefMenu=new FormDefMenu();
				
		function onClick(treeNode){
			var url=__ctx+"/oa/form/formDef/list.do";
			if(treeNode.isRoot==undefined){
				var typeId=treeNode.typeId;
				url+="?categoryId="+typeId;
			} 
			$("#defFrame").attr("src",url);
		}
				
		function hiddenMenu(){
			if(curMenu){
				curMenu.hide();
			}
		}
		
		$(function (){
			//布局
			$("#defLayout").ligerLayout({ 
				leftWidth:210,
				height: '100%',
				allowLeftResize:true
			});
			$(document).click(hiddenMenu);
			globalType.loadGlobalTree();
		});
		        
		function handler(item){
		 	hiddenMenu();
		 	var txt=item.text;
		 	switch(txt){
		 		case "增加分类":
		 			globalType.openGlobalTypeDlg(true);
		 			break;
		 		case "编辑分类":
		 			globalType.openGlobalTypeDlg(false);
		 			break;
		 		case "物理删除分类":
		 			globalType.delNode();
		 			break;
		 		case "逻辑删除分类":
		 			globalType.delLogicNode();
		 			break;
		 	}
		}
		/**
		 * 树右击事件
		 */
		function zTreeOnRightClick(event, treeId, treeNode) {
			hiddenMenu();
			if (treeNode) {
				globalType.currentNode=treeNode;
				globalType.glTypeTree.selectNode(treeNode);
				curMenu=formDefMenu.getMenu(treeNode, handler);
				justifyRightClickPosition(event);
				curMenu.show({ top: event.pageY, left: event.pageX });
			}
		};
		//展开收起
		function treeExpandAll(type){
			globalType.treeExpandAll(type);
		};
	</script> 
</head>
<body>
   	<div id="defLayout">
			<div position="left" title="表单分类管理">
				<div class="tree-toolbar">
					<div class="toolBar">
							<div class="group"><a class="link reload" id="treeFresh" href="javascript:globalType.loadGlobalTree();" >刷新</a></div>
							<div class="group"><a class="link expand" id="treeExpandAll" href="javascript:treeExpandAll(true)" >展开</a></div>
							<div class="group"><a class="link collapse" id="treeCollapseAll" href="javascript:treeExpandAll(false)" >收起</a></div>
					</div>
				</div>
				<ul id="glTypeTree" class="ztree"></ul>
			</div>
			<div position="center">
				<iframe id="defFrame" height="100%" width="100%" frameborder="0" src="${ctx}/oa/form/formDef/list.do"></iframe>
			</div>
		</div>
</body>
</html>
