/**
 * 分类管理
 * 
 * @param divId
 * @param conf
 * @returns {DataSource}
 */
DbomManage = function(divId, param, action, conf) {
	{
		this.bomTree = null;
		this.currentNode = null;
		this.conf = conf;
		this.param = param;
		this.divId = divId;
		this.mappingUrl = "/oa/system/dbom/";
		this.menu = null;
		var _self = this;
	}
	;

	this.loadTree = function() {
		var setting = {
			data : {
				key : {
					name : "text"
				},
				simpleData : {
					enable : true,
					idKey : "id",
					pIdKey : "parentId"
				}
			},
			showLine : true,
			callback : {
				onClick : this.clickHandler,
				onRightClick : this.rightClickHandler
			}
		};
		var url = __ctx + _self.mappingUrl + action;
		if (conf.url) {
			url = conf.url;
		}

		$.post(
			url,
			param,
			function(result) {
				_self.bomTree = $.fn.zTree.init(
						$("#" + _self.divId), setting, result);
				var expandAll = _self.conf.expandAll;
				if (expandAll) {
					_self.bomTree.expandAll(true);
				} else {
					_self.bomTree.expandAll(false);
				}
				// 设置左边树的高度
				var toolbarHeight = $(".tree-toolbar").height();
				var allHeight = $("#defLayout").height();
				var treeHeight = (parseInt(allHeight) - parseInt(toolbarHeight) * 3)
						+ "px";
				$("#" + _self.divId).css({
					"height" : treeHeight
				});
				// 设置默认选中的节点
				if (_self.conf.checkedData) {
					var ids = _self.conf.checkedData.split(",");
					$(ids).each(function(i, id) {
						var node=_self.bomTree.getNodeByParam("id", id);
						_self.bomTree.checkNode(node);
					})
				}

			});

	};

	this.rightClickHandler = function(event, treeId, treeNode) {
		_self.bomTree.selectNode(treeNode);
		_self.menu.hide();
		justifyRightClickPosition(event);
		_self.menu.show({
			top : event.pageY,
			left : event.pageX
		});

	};
	// 点击事件处理
	this.clickHandler = function(event, treeId, treeNode) {
		_self.currentNode = treeNode;
		if (_self.conf.onClick) {
			_self.conf.onClick(treeNode);
		}
	};

	// 展开收起
	this.treeExpandAll = function(type) {
		_self.bomTree.expandAll(type);
	};

	this.sortNode = function() {
		var typeId = _self.currentNode.typeId;
		var url = __ctx + _self.mappingUrl + "sortList.do?parentId=" + typeId;
		url = url.getNewUrl();
		DialogUtil.open({
			height:300,
			width: 600,
			title : '排序',
			url: url, 
			isResize: true,
			sucCall:function(rvalue){
				// 重新加载树。
				_self.loadGlobalTree();
			}
		});
	}
	this.addDbomType = function() {
		var mill=(parseInt(Math.random()*10000)).toString();
		var url = __ctx + "/oa/system/dbom/edit.do?mill="+mill;
		DialogUtil.open({
			height:800,
			width: 800,
			title : '编辑dbom',
			url: url, 
			isResize: true,
			sucCall:function(rvalue){
				if (rvalue != undefined) {
					location.reload();
				}
			}
		});
	}
	this.editDbomType = function() {
		var dbomCode = $("#dbomCode").val();
		if (!dbomCode || dbomCode == '' || dbomCode == null) {
			$.ligerDialog.warn("请选择需要编辑的分类！");
			return;
		} else {
			var mill=(parseInt(Math.random()*10000)).toString();
			var url = __ctx + "/oa/system/dbom/edit.do?dbomCode="+dbomCode+"&mill="+mill;
			DialogUtil.open({
				height:800,
				width: 800,
				title : '编辑dbom',
				url: url, 
				isResize: true,
				sucCall:function(rvalue){
					if (rvalue != undefined) {
						location.reload();
					}
				}
			});
		}

	}
	this.delDbomType = function() {
		var dbomCode = $("#dbomCode").val();
		var mill=(parseInt(Math.random()*10000)).toString();
		var url = __ctx + "/oa/system/dbom/delete.do?mill="+mill;
		if (!dbomCode || dbomCode == '' || dbomCode == null) {
			$.ligerDialog.warn("请选择需要删除的分类！");
		}else{
			url+="&dbomCode="+dbomCode;
			$.ajax({
				url:url,
				type : "POST",
				success:function(result){
					if(result.result==1){
						$("#dbomCode").find("option:selected").remove();
						$.ligerDialog.alert("dbom分类删除成功！");

					}
				}
			})
		}
	}
}