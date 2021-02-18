/*
 * 型号维度结构树
 */
ModuleTree = function(catKey, divId, conf, mappingUrl, id, nodeName) {
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
					moduleId : _self.id,
		        	moduleName : _self.nodeName
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
						if (element.type=='module') {
							element.icon = 'xiaohuojian';
						} else if(element.type=='modulePeriod'){
							element.icon = 'Group-';
						} else if(element.type=='moduleBatch'){
							element.icon = 'augichuojian';
						} else if(element.type=='moduleDispatch'){
							element.icon = 'weixing';
						} else if(element.type=='category'){
							element.icon = '1180huojiandaodan';
						} else if(element.type=='batch'){
							element.icon = 'ic_layers_px';
						} else{
							element.icon = 'shezhi';
						}
						
						toolTree.updateNode(element);
					}
				},
				error : function() {
					$.ligerDialog.error('产品维度结构树加载失败！');
				}
			});
		});
	};

	// 左击事件处理
	this.clickHandler = function(event, treeId, treeNode) {
		_self.currentNode = treeNode;
		debugger;
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

	// 新增/编辑节点
	this.editNode = function(isAdd) {
		debugger
		var curNode = _self.currentNode;
		var dataId = curNode.id;
		var nodeType = curNode.type;
		var parentId = curNode.parentId;
		var Id = "";
		var url = null;
		var title = null;

		if (isAdd) {
			var tempUrl =  curNode.handleUrl.replace("preview","editData");
			url = __ctx + tempUrl + "&handleType=add";
			if(nodeType=="module"){
				// 型号下建型号阶段
				url = url + "&moduleId=" + dataId
				title = '新增型号阶段';
			}else if(nodeType=="modulePeriod"){
				// 型号阶段下建型号批次
				url = url + "&modulePeriodId=" + dataId + "&moduleId=" + curNode.moduleId;
				title = '新增型号批次';
			}else if(nodeType=="moduleBatch"){
				// 型号批次下建型号发次
				url = url + "&moduleBatchId=" + dataId + "&modulePeriodId=" + curNode.modulePeriodId + "&moduleId=" + curNode.moduleId;
				title = '新增型号发次';
			}
		} else {
			var tempUrl =  curNode.tempUrl.replace("detailData","editData");
			url = __ctx + tempUrl + "&__pk__="+ dataId + "&handleType=edit";
			if(nodeType=="modulePeriod"){
				title = '编辑型号阶段';
			}else if(nodeType=="moduleBatch"){
				title = '编辑型号批次';
			}else if(nodeType=="moduleDispatch"){
				title = '编辑型号发次';
			}else if(nodeType=="product"){
				title = '编辑产品';
				// url重置
				url = __ctx + curNode.handleUrl.replace("detailData","editData") + "&__pk__="+ dataId + "&handleType=edit";
			}
		}

		url = url.getNewUrl();
		url = encodeURI(url);
		// 判断（如果是根节点）则无权限限制
		if (dataId == "0") {
			DialogUtil.open({
				height : 600,
				width : 900,
				title : title,
				url : url,
				isResize : true,
				// 自定义参数?
				sucCall : function(rtn) {
					if (rtn == "ok") {
						reFresh();
					}
				}
			});
		} else {
			if (parentId == "0") {
				Id = dataId;
			} else {
				Id = parentId
			}
		}
		var result = "1";

		if (result == "1") {
			DialogUtil.open({
				height : 600,
				width : 900,
				title : title,
				url : url,
				isResize : true,
				// 自定义参数?
				sucCall : function(rtn) {
					if (rtn == "ok") {
						reFresh();
					}
				}
			});
		} else if (result == "2") {
			$.ligerDialog.warn("请先给该型号分配项目办负责人员！");
		} else {
			$.ligerDialog.warn("只有负责该型号的项目办人员才能对该型号及该型号下的发次操作！");
		}
	}

	// 删除型号节点
	this.delNode = function() {
		var curNode = _self.currentNode;
		var nodeType = curNode.type;
		var title;
		var tempUrl = curNode.tempUrl;
		var handleUrl = curNode.handleUrl;
		var displayId = getRequest(tempUrl.substr(tempUrl.indexOf('?')),'__displayId__');
		if(nodeType=='modulePeriod'){
			title = '型号阶段';
		}else if(nodeType=='moduleBatch'){
			title = '型号批次';
		}else if(nodeType=='moduleDispatch'){
			title = '型号发次';
		}else if(nodeType=='product'){
			title = '产品';
			displayId = getRequest(handleUrl.substr(handleUrl.indexOf('?')),'__displayId__');
		}
		var url = __ctx
				+ '/oa/form/dataTemplate/deleteData.do?__displayId__=' + displayId + '&__pk__='
				+ curNode.id;
		$.ligerDialog.confirm('确认删除吗？', '提示信息', function(rtn) {
			if (rtn) {
				$.ajax({
					type : "POST",
					url : url,
					async : false,
					success : function(data) {
						$.ligerDialog.success(title + "删除成功！");
					}
				});
//				window.location.reload();
				moduleTree.reFresh();
			}
		});

	}
	
	// 选择产品
	this.selectProduct = function() {
		var curNode = _self.currentNode;
		var nodeType = curNode.type;
		var url = __ctx + '/dp/productSelectorDialog.do?';
		url += '&moduleId=' + curNode.moduleId;
		url += '&moduleName=' + curNode.moduleName;
		url += '&isSingle=' + 1;// 0:单选，1：多选
		DialogUtil.open({
			height : 500,
			width : 400,
			url : url,
			showMax : true, // 是否显示最大化按钮
			showToggle : false, // 窗口收缩折叠
			title : "产品选择",
			showMin : false,
			sucCall : function(rtn) {
				$.ligerDialog.warn("暂未开放！");
			}
		});
	}

	function reFresh() {
		// 重新加载
		_self.loadGlobalTree();
	}
	
	// 型号维度结构树操作后刷新事件
	this.reFresh=function() {
		_self.loadGlobalTree();		
	}
	
	function getRootNdoe(tree) {
		return tree.getNodesByFilter(function(node) {
			return node.level == 0;
		});
	}
}

// 产品维度结构树左击事件 
function moduleTreeOnClick(event, treeId, treeNode) {
	var url = null;
	if (treeNode.type == "batch") {
		// 产品批次节点
		url = __ctx + treeNode.batchBuilderUrl + "&batchId=" + treeNode.id + "&batchKey=" + treeNode.name;
	} else if(treeNode.type == "product"){
		// 产品-数据包节点
		url = __ctx + treeNode.tempUrl + "&id=" + treeNode.id + "&productId=" + treeNode.batchId + "&productName=" + treeNode.batchKey;
	}else {
		// 非数据包节点（型号、产品类别）--展示明细
		url = __ctx + treeNode.tempUrl + "&__pk__=" + treeNode.id;
	}
	url = encodeURI(url);
	$("#dataPackageFrame").attr("src", url);
};

// 产品维度结构树右击事件
function moduleTreeOnRightClick(event, treeId, treeNode) {
    moduleHiddenMenu();
    if (treeNode) {
    	var height = $(".l-layout-center").height() - 28;
    	moduleTree.currentNode = treeNode;
    	moduleTree.glTypeTree.selectNode(treeNode);
        moduleIsHandler(treeNode);
        if(event.pageY>height-24){
        	curModuleMenu.show({top: event.pageY-24, left: event.pageX});
        }else{
        	curModuleMenu.show({top: event.pageY, left: event.pageX});
        }
    }
};
// 隐藏右键菜单
function moduleHiddenMenu() {
    if (curModuleMenu) {
        curModuleMenu.hide();
    }
}
// 右击事件处理
function moduleIsHandler(treeNode) {
    curModuleMenu = moduleMenu.getMenu(treeNode, moduleHandler);
}
function moduleHandler(item) {

    moduleHiddenMenu();
    var txt = item.text;
    switch (txt) {
        case "新增":
        	moduleTree.editNode(true);
            break;
        case "编辑":
        	moduleTree.editNode(false);
            break;
        case "删除":
        	moduleTree.delNode();
        	break;
        case "选择产品":
        	moduleTree.selectProduct();
        	break;
    }
}