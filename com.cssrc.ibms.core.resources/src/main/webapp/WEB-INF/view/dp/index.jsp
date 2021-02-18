<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>表单定义管理</title>
<%@include file="/commons/include/form.jsp" %> 
<f:link href="from-jsp.css"></f:link>
<f:gtype name="CAT_FORM" alias="CAT_FORM"></f:gtype>
<f:link href="tree/zTreeStyle.css"></f:link>
<link rel="stylesheet" href="../layui/css/layui.css">
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
				<div class="layui-tab layui-tab-brief">
					<ul class="layui-tab-title">
						<li class="layui-this">属性</li>
						<li>检查表</li>
						<li tableName="table1">检查项</li>
						<li tableName="table2">检查条件</li>
						<li tableName="table3">签署</li>
					</ul>
					<div class="layui-tab-content">
						<div class="layui-tab-item layui-show">
							<p class="layui-elem-quote">检查表模板</p>
							<form class="layui-form" action="">
								<div class="layui-form-item">
									<label class="layui-form-label">名称：</label>
									<div class="layui-input-block">
										<input class="layui-input" type="text" disabled vlaue='箭上静态阻值检查表'>
									</div>
								</div>
								<div class="layui-form-item">
									<label class="layui-form-label">编号：</label>
									<div class="layui-input-block">
										<input class="layui-input" type="text" disabled vlaue='1'>
									</div>
								</div>
								<div class="layui-form-item">
									<label class="layui-form-label">所属发次：</label>
									<div class="layui-input-block">
										<input class="layui-input" type="text" disabled vlaue='Y32'>
									</div>
								</div>
								<div class="layui-form-item">
									<label class="layui-form-label">行数：</label>
									<div class="layui-input-block">
										<input class="layui-input" type="text" disabled vlaue='12'>
									</div>
								</div>
								<div class="layui-form-item">
									<label class="layui-form-label">备注：</label>
									<div class="layui-input-block">
										<input class="layui-input" type="text" disabled vlaue=''>
									</div>
								</div>
								<div class="layui-form-item">
									<label class="layui-form-label">所属文件夹：</label>
									<div class="layui-input-block">
										<input class="layui-input" type="text" disabled vlaue=''>
									</div>
								</div>


							</form>
						</div>
						<div class="layui-tab-item">
							<table class="layui-table">
								<thead>
									<tr>
										<th>XYTCH接点</th>
										<th>三用表量程</th>
										<th>正向阻值</th>
										<th>实测值</th>
									</tr>					
								</thead>
								<tbody>
									<tr>
										<td>28V+：1、2、38～45、46、47、25、26、52 28V-：3、4、94～100、90、91、106、107、88、89、50、51</td>
										<td>100Ω</td>
										<td>≥2KΩ</td>
										<td><input class="layui-input" type="text" readonly="readonly"></td>
									</tr>
									<tr>
										<td>28V+：1、2、38～45、46、47、25、26、52 28V-：3、4、94～100、90、91、106、107、88、89、50、51</td>
										<td>100Ω</td>
										<td>≥2KΩ</td>
										<td><input class="layui-input" type="text" readonly="readonly"></td>
									</tr>
									<tr>
										<td>28V+：1、2、38～45、46、47、25、26、52 28V-：3、4、94～100、90、91、106、107、88、89、50、51</td>
										<td>100Ω</td>
										<td>≥2KΩ</td>
										<td><input class="layui-input" type="text" readonly="readonly"></td>
									</tr>
									<tr>
										<td>28V+：1、2、38～45、46、47、25、26、52 28V-：3、4、94～100、90、91、106、107、88、89、50、51</td>
										<td>100Ω</td>
										<td>≥2KΩ</td>
										<td><input class="layui-input" type="text" readonly="readonly"></td>
									</tr>
								</tbody>
							</table>
						</div>
						<div class="layui-tab-item">
							<table id="table1"></table>

						</div>
						<div class="layui-tab-item">
							<table id="table2"></table>

						</div>
						<div class="layui-tab-item">
							<table id="table3"></table>
						</div>
					</div>

				</div>

			</div>
		</div>
		
</body>
<script type="text/javascript" src="../layui/layui.js"></script>
<script type="text/javascript" src="./index.js"></script>
</html>
