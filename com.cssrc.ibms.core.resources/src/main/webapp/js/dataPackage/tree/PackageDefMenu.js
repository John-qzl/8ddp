/**
 * 
 */
PackageDefMenu=function(){
	{
		this.defMenu=null;
		this.type = null;
	};
	this.getMenu=function(treeNode,handler){		
		this.type = treeNode.Type;
		this.defMenu = null;
		//if (this.type == "root"||this.type == "普通分类节点"){
		if (this.type == "root"){//普通分类节点也可以编辑、删除
			this.defMenu = $.ligerMenu({top: 100, left: 100, width: 100, items:
                [
                { text: '新增', click: handler }
                ]
                });
		} else if (this.type == "fcType") { //发次
			this.defMenu = $.ligerMenu({top: 100, left: 100, width: 100, items:
                [
                { text: '新增', click: handler },
                { text: '编辑', click: handler }
                ]
                });
		} else if(this.type != "普通分类节点"){
			this.defMenu = $.ligerMenu({top: 100, left: 100, width: 100, items:
				[
					{ text: '编辑', click: handler },
					{ text: '删除', click: handler }
				]
			});
		}else{
			this.defMenu = $.ligerMenu({top: 100, left: 100, width: 100, items:
				[
					{ text: '新增', click: handler },
					{ text: '编辑', click: handler },
					{ text: '删除', click: handler }
				]
			});
		}

		return this.defMenu;
	};
};
