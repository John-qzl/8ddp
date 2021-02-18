/*
 * 产品维度结构树
 */
ProductTree = function(catKey, divId, conf, mappingUrl, id, nodeName) {
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
			// drag : {
			// autoExpandTrigger : true,
			// prev : dropPrev,
			// inner : dropInner,
			// next : dropNext
			// },
			// enable : true,
			// showRemoveBtn : false,
			// showRenameBtn : false,
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
				// 型号下建产品类别
				url = url + "&moduleId=" + dataId
				title = '新增产品类别';
			}else if(nodeType=="category"){
				// 产品类别下建产品批次
				url = url + "&categoryId=" + dataId + "&moduleId=" + curNode.moduleId;
				title = '新增产品批次';
			}else if(nodeType=="batch"){
				// 产品批次下建产品
				url = url + "&batchId=" + dataId + "&moduleId=" + curNode.moduleId + "&categoryId=" + curNode.categoryId;
				title = '新增产品';
			}
		} else {
			var tempUrl =  curNode.tempUrl.replace("detailData","editData");
			url = __ctx + tempUrl + "&__pk__="+ dataId + "&handleType=edit";
			if(nodeType=="category"){
				title = '编辑产品类别';
			}else if(nodeType=="batch"){
				title = '编辑产品批次';
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
		if(nodeType=='category'){
			title = '产品类别';
		}else if(nodeType=='batch'){
			title = '产品批次';
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
				window.location.reload();
			}
		});

	}

	// 产品维度结构树操作后刷新事件
	function reFresh() {
		// 重新加载
		_self.loadGlobalTree();
	}
	
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
function productTreeOnClick(event, treeId, treeNode) {
	var url = null;
	if (treeNode.type == "batch") {
		// 3.产品批次节点
		url = __ctx + treeNode.batchBuilderUrl + "&batchId=" + treeNode.id + "&batchKey=" + treeNode.name;
	} else if(treeNode.type == "product"){
		// 5.产品-数据包节点
		url = __ctx + treeNode.tempUrl + "&id=" + treeNode.id + "&productId=" + treeNode.categoryId + "&productName=" + treeNode.categoryName;
	} else if(treeNode.type == "category"){
		// 2.产品类别节点
		url = __ctx + treeNode.categoryBuilderUrl + "&categoryId=" + treeNode.id + "&categoryKey=" + treeNode.name;
	} else if(treeNode.type == "acceptancePlan"){
		// 4.产品验收策划节点
		url = __ctx + treeNode.acceptancePlanBuilderUrl + "&acceptancePlanId=" + treeNode.id + "&acceptancePlanCode=" + treeNode.name
		+ "&batchId=" + treeNode.batchId + "&batchKey=" + treeNode.batchKey + "&productCategoryId=" + treeNode.categoryId;
	}else {
		// 非数据包节点（型号）--展示明细
		url = __ctx + treeNode.tempUrl + "&__pk__=" + treeNode.id;
	}
	$("#allProductCategory").remove();
	url = encodeURI(url);
	$("#dataPackageFrame").attr("height","100%");
	$("#dataPackageFrame").attr("src", url);
};

// 产品维度结构树右击事件
function productTreeOnRightClick(event, treeId, treeNode) {
    hiddenMenu();
    var url=__ctx+"/model/user/role/getUseRole.do";
    $.ajax({
        async: 'false',
        dataType: 'json',
        data: {moduleId:treeNode.moduleId},
        /* url: "${ctx}/project/tree/stree/getCurUserInfo.do", */
        url:url,
        success: function (data) {
        	if(data.useRole=="1"){
        		  if (treeNode) {
          	    	var height = $(".l-layout-center").height() - 28;
          	    	prodTree.currentNode = treeNode;
          	    	prodTree.glTypeTree.selectNode(treeNode);
          	        ishandler(treeNode,data);
          	        if(event.pageY>height-24){
          	        	curMenu.show({top: event.pageY-24, left: event.pageX});
          	        }else{
          	        	curMenu.show({top: event.pageY, left: event.pageX});
          	        }
          	 	}
        	}else{
        		$.ligerDialog.warn("您没有权限！");
        	}
        	
        }
    });
  
};
//隐藏右键菜单
function hiddenMenu() {
    if (curMenu) {
        curMenu.hide();
    }
}
//右击事件处理
function ishandler(treeNode,role) {
    curMenu = productMenu.getMenu(treeNode, handler,role);
}
function handler(item) {
    hiddenMenu();
    var txt = item.text;
    var role=item.role;
    switch (txt) {
        case "新增批次":
        	prodTree.editNode(true);
            break;
        case "编辑批次":
        	prodTree.editNode(false);
            break;
        case "删除批次":
        	prodTree.delNode();
        	break;
        case "新增":
        	prodTree.editNode(true);
            break;
        case "编辑":
        	prodTree.editNode(false);
            break;
        case "删除":
        	prodTree.delNode();
        	break;
    }
}
// 获取url后制定参数名的参数值
function getRequest(paramUrl,paramNme) { 
	var theRequest = new Object();
	if (paramUrl.indexOf("?") != -1) {
		var str = paramUrl.substr(1);
		strs = str.split("&");
		for (var i = 0; i < strs.length; i++) {
			theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
		}
	}
	return theRequest[paramNme];
}