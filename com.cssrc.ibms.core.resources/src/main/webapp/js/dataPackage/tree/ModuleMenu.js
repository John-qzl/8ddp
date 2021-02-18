/**
 * 型号维度结构树菜单
 */
ModuleMenu=function(){
	{
		this.menuMenu=null;
		this.treeNode=null;
		this.nodeType = null;
		this.parentId = null;
	};
	this.getMenu=function(treeNode,handler){
		this.treeNode = treeNode;
		this.nodeType = treeNode.type;
		this.parentId = treeNode.parentId;
		this.menuMenu = null;
		if (this.nodeType == "module"){
			// 根节点"型号"只能进行新增（产品类别操作）
			this.menuMenu = $.ligerMenu({top: 100, left: 100, width: 100, items:
                [
                { text: '新增', click: handler }
                ]
                });
		} else if(this.nodeType == "modulePeriod"||this.nodeType == "moduleBatch"){
			// 型号阶段节点、型号批次节点
			this.menuMenu = $.ligerMenu({top: 100, left: 100, width: 100, items:
                [
                { text: '新增', click: handler },
                { text: '编辑', click: handler },
                { text: '删除', click: handler }
                ]
                });
		} else if(this.nodeType == "moduleDispatch"){
			// 型号发次节点
			this.menuMenu = $.ligerMenu({top: 100, left: 100, width: 100, items:
                [
                { text: '选择产品', click: handler },
                { text: '编辑', click: handler },
                { text: '删除', click: handler }
                ]
                });
		} else{
			// 产品节点
			this.menuMenu = $.ligerMenu({top: 100, left: 100, width: 100, items:
                [
                { text: '编辑', click: handler },
                { text: '删除', click: handler }
                ]
                });
		}

		return this.menuMenu;
	};
};
