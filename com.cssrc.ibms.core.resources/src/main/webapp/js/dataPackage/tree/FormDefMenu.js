FormDefMenu=function(){
	{
		this.menuMenu=null;
		this.treeNode=null;
		this.typeId = null;
		this.parentId = null;
	};
	this.getMenu=function(treeNode,handler,roleNames){
		this.treeNode = treeNode;
		this.typeId = treeNode.typeId;
		this.parentId = treeNode.parentId;
		this.menuMenu = null;
		if (this.typeId == "0"){
			this.menuMenu = $.ligerMenu({top: 100, left: 100, width: 100, items:
                [
                { text: '新增', click: handler,"roleNames":roleNames }
                ]
                });
		} else if(this.parentId == "0"){
			this.menuMenu = $.ligerMenu({top: 100, left: 100, width: 100, items:
                [
                /*{ text: '新增', click: handler },*/
                { text: '编辑', click: handler,"roleNames":roleNames },
                { text: '删除', click: handler,"roleNames":roleNames }
                ]
                });
		}else{
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
