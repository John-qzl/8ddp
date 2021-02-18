/*
 * 批次-验收策划结构树
 */
AcceptancePlanTree = function(catKey, divId, conf, mappingUrl, id, nodeName) {
	{
		this.catKey = catKey;
		this.divId = divId;
		this.conf = conf;
		this.mappingUrl = mappingUrl;
		this.id = id;
		this.nodeName = nodeName;
		this.glTypeTree = null;
		this.currentNode = null;
		var _self = this;
	};

	// 加载树
	this.loadGlobalTree = function() {
		var setting = {
			async : {
				enable : false
			},// 同步
			data : {
				key : {
					name : "name",
					type : "type",
					moduleId : "moduleId",// 型号id
					categoryId : "categoryId",// 类别id
					unionId : "unionId"
				},
				simpleData : {
					enable : true,
					idKey : "id",
					pIdKey : "parentId"
				}
			},
			view : {
				selectedMulti : false,// 同时选中多个节点
				showLine : true
			},
			callback : {
				onClick : this.clickHandler,
				onRightClick : this.rightClickHandler
			}
		};

		_self.glTypeTree = $.fn.zTree.init($("#" + _self.divId), setting);

		// 默认选中根节点
		var rootNode = getRootNdoe(_self.glTypeTree);
		_self.glTypeTree.selectNode(rootNode, false);
		$(function() {
			$.ajax({
				async : false,
				cache : false,
				type : 'POST',
				dataType : "json",
				data:{
					batchId : _self.id,
					batchKey : _self.nodeName
		        },
				url : __ctx + _self.mappingUrl + "getTreeData.do?",
				success : function(data) {
					// 结构树添加所有节点
					_self.glTypeTree.addNodes(null,data);
		            var rootNode = getRootNdoe(_self.glTypeTree);
		            // 默认选中根节点
		    		_self.glTypeTree.selectNode(rootNode,false);
		    		var toolTree = $.fn.zTree.getZTreeObj(_self.divId);
					var treeNodes = toolTree.transformToArray(toolTree.getNodes());
					// 遍历添加树节点图标
					for (var i = 0; i < treeNodes.length; i++) {
						var element = treeNodes[i];
						if (element.type=='batch') {
							element.icon = 'ic_layers_px';
						} else if(element.type=='acceptancePlan'){
							element.icon = 'copy';
						} else{
							element.icon = 'copy';
						}
						
						toolTree.updateNode(element);
					}
				},
				error : function() {
					$.ligerDialog.error('产品批次-验收策划结构树加载失败！');
				}
			});
		});
	};

	// 左击事件处理
	this.clickHandler = function(event, treeId, treeNode) {
		_self.currentNode = treeNode;
		if (_self.conf.onClick) {
			_self.conf.onClick(event, treeId, treeNode);
		}
	};
	
	// 右击事件处理
	this.rightClickHandler = function(event, treeId, treeNode) {
		_self.currentNode = treeNode;
		if (_self.conf.onRightClick) {
			_self.conf.onRightClick(event, treeId, treeNode);
		}
	};

	// 展开收起
	this.treeExpandAll = function(type) {
		_self.glTypeTree.expandAll(type);
	};

	function reFresh() {
		// 重新加载
		_self.loadGlobalTree();
	}
	
	// 产品批次-验收策划结构树操作后刷新事件
	this.reFresh=function() {
		_self.loadGlobalTree();		
	}
	
	function getRootNdoe(tree) {
		return tree.getNodesByFilter(function(node) {
			return node.level == 0;
		});
	}
}

// 产品批次-验收策划结构树左击事件 
function acceptancePlanTreeOnClick(event, treeId, treeNode) {
	var url = null;
	if (treeNode.type == "acceptancePlan"){
		// 产品-验收策划节点
//		var clickUrl = "/oa/form/dataTemplate/preview.do?__displayId__=10000021200549";
//		url = __ctx + clickUrl + "&acceptancePlanId=" + treeNode.id + "&acceptancePlanKey=" + treeNode.name
//					+ "&productBatchId=" + treeNode.productBatchId + "&productCategoryId=" + treeNode.productCategoryId;
		// 演示虚构
		var clickUrl = "/oa/form/dataTemplate/preview.do?__displayId__=10000000680207";
		url = __ctx + clickUrl + "&projectId=10000021920521&projectName=制导DK&__dbomSql__=F_SSSJB=10000021920882";
		
	}else {
		// --展示明细
		url = __ctx + treeNode.clickUrl + "&__pk__=" + treeNode.id;
	}
	url = encodeURI(url);
	$("#dataReturnViewFrame").attr("src", url);
};
