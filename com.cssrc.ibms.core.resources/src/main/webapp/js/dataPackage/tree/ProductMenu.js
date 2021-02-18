/**
 * 产品维度结构树菜单
 */
ProductMenu=function(){
	{
		this.menuMenu=null;
		this.treeNode=null;
		this.nodeType = null;
		this.parentId = null;
	};
	this.getMenu=function(treeNode,handler,role){
		this.treeNode = treeNode;
		this.nodeType = treeNode.type;
		this.parentId = treeNode.parentId;
		this.menuMenu = null;
		if (this.nodeType == "module"){
			// 根节点"型号"只能进行新增（产品类别操作）
			this.menuMenu = $.ligerMenu({top: 100, left: 100, width: 100, items:
                [
                { text: '新增', click: handler,"role":role }
                ]
                });
		} else if(this.nodeType == "category"){
			// 产品类别节点
			this.menuMenu = $.ligerMenu({top: 100, left: 100, width: 100, items:
                [
                { text: '新增批次', click: handler },
                { text: '编辑批次', click: handler },
                { text: '删除批次', click: handler }
                ]
                });
		}else if(this.nodeType == "batch"){
			// 产品批次、策划节点
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
