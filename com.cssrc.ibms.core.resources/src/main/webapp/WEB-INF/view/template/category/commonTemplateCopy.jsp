<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>通用模板选择器</title>
<%@include file="/commons/include/form.jsp"%>
<base target="_self" />
<f:link href="Aqua/css/ligerui-all.css"></f:link>
<%--<link rel="stylesheet" href="${ctx }/jslib/tree/zTreeStyle.css"
	type="text/css" />--%>
	<f:link href="tree/zTreeStyle.css"></f:link>
<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript"
	src="${ctx}/jslib/lg/plugins/ligerComboBox.js"></script>
<script type="text/javascript">
	/*KILLDIALOG*/
	var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	debugger;
	var categoryId = '${categoryId}';
	var isSingle = false;
	// 树展开层数
	var expandDepth = 2;
	$(function() {
		loadTree();
		$("a.save").click(commonMultiCopy);
	});

	var check;
	if (isSingle) {
		check = {
			chkStyle : "radio",
			enable : true,
			radioType : "all"
		};
	} else {
		check = {
			chkboxType : {
				"Y" : "ps",
				"N" : "ps"
			},
			enable : true
		};
	}
	// 通用模板结构树
	var commonTemplateTree;
	// 加载通用模板结构树
	function loadTree() {

		var setting = {
			data : {
				key : {
					name : "name",
					title : "name"
				},
				simpleData : {
					enable : true,
					idKey : "id",
					pIdKey : "parentId",
					rootPId : "-1"
				}
			},
			view : {
				selectedMulti : false
			},
			check : check

		};
		//一次性加载
		var url = "${ctx}/template/manage/getTreeData.do?moduleId=0&moduleName=1";
		$.ajax({
		    url:url,
		    async:false,
		    success:function(result){
		    	if (result == "[]") {
					$.ligerDialog.warn("没有通用模板,不能复制！");
					dialog.close();
				} else {
					debugger;
					commonTemplateTree = $.fn.zTree.init($("#commonTemplateTree"),
							setting, eval(result));
					var toolTree = $.fn.zTree.getZTreeObj("commonTemplateTree");
					var treeNodes = toolTree.transformToArray(toolTree.getNodes());
					for (var i = 0; i < treeNodes.length; i++) {
						var element = treeNodes[i];
						if (element.type == 'root') {
							element.icon = 'xiaohuojian';
						} else if (element.type == 'folder') {
							element.icon = 'file';
						} else {
							element.icon = 'yulanbiaodan';
						}
						toolTree.updateNode(element);
					}
					if (expandDepth != 0) {
						commonTemplateTree.expandAll(true);
						var nodes = commonTemplateTree.getNodesByFilter(function(
								node) {
							return (node.level < expandDepth);
						});
						if (nodes.length > 0) {
							for (var i = 0; i < nodes.length; i++) {
								commonTemplateTree
										.expandNode(nodes[i], true, false);
							}
						}
					} else
						commonTemplateTree.expandAll(true);
				}
		    }
		});
		/* $.post(url, function(result) {
			if (result == "[]") {
				$.ligerDialog.warn("没有通用模板,不能复制！");
				dialog.close();
			} else {
				debugger;
				commonTemplateTree = $.fn.zTree.init($("#commonTemplateTree"),
						setting, eval(result));
				var toolTree = $.fn.zTree.getZTreeObj("commonTemplateTree");
				var treeNodes = toolTree.transformToArray(toolTree.getNodes());
				for (var i = 0; i < treeNodes.length; i++) {
					var element = treeNodes[i];
					if (element.type == 'root') {
						element.icon = 'xiaohuojian';
					} else if (element.type == 'folder') {
						element.icon = 'file';
					} else {
						element.icon = 'yulanbiaodan';
					}
					toolTree.updateNode(element);
				}
				if (expandDepth != 0) {
					commonTemplateTree.expandAll(true);
					var nodes = commonTemplateTree.getNodesByFilter(function(
							node) {
						return (node.level < expandDepth);
					});
					if (nodes.length > 0) {
						for (var i = 0; i < nodes.length; i++) {
							commonTemplateTree
									.expandNode(nodes[i], true, false);
						}
					}
				} else
					commonTemplateTree.expandAll(true);
			}
		}) */
	};
	
	// 批量复制通用模板
	function commonMultiCopy() {
		var commonTemplateTree = $.fn.zTree.getZTreeObj("commonTemplateTree");
		var nodes = commonTemplateTree.getCheckedNodes(true);
		var ids = "";
		var names = "";
		$.each(nodes, function(i, n) {
			if (n.type != "form") {
				return true;
			}
			ids += n.id + ",";
			names += n.name + ",";
		});

		ids = ids.substring(0, ids.length - 1);
		names = names.substring(0, names.length - 1);
		url = "${ctx}/template/manage/commonMultiCopy.do?categoryId=" + categoryId +"&templateIds=" + ids;
		$.post(url, function(data) {
			if (data.success == "true") {
				$.ligerDialog.confirm('模板复制成功，是否继续?', function(rtn) {
					if (!rtn) {
						if(dialog.options.sucCall){
							dialog.options.sucCall({ids:ids,names:names});
						}
						dialog.close();
					}
				});
			} else {
				$.ligerDialog.error(data.msg, "复制失败");
			}
		});
	};
</script>
<f:link href="from-jsp.css"></f:link>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" id="btnCopy">复制</a>
					</div>

					<div class="group">
						<a class="link del" onclick="javasrcipt:dialog.close()">关闭</a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<div class="panel-detail">
				<div id="commonTemplateTree" class="ztree"></div>
			</div>
		</div>
	</div>
</body>
</html>
