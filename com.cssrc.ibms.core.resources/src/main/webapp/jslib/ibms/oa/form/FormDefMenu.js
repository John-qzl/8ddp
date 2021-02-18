FormDefMenu=function(){
	{
		this.rootMenu=null;
		this.menuMenu=null;
		this.treeNode=null;
	};
	this.getMenu=function(treeNode,handler,roleNames){
		this.treeNode=treeNode;
		var isRoot=0;
		if(treeNode.isRoot) isRoot=1;
		
		//判断是否已逻辑删除
		if(treeNode.gltype_delFlag == "1"){
			this.menuMenu = $.ligerMenu({top: 100, left: 100, width: 120, items:
                [
                { text: '增加分类', click: handler },
                { text: '编辑分类', click: handler  },
                { text: '物理删除分类', click: handler }
                ]
                });
		}else{
			this.menuMenu = $.ligerMenu({top: 100, left: 100, width: 120, items:
                [
                { text: '增加分类', click: handler },
                { text: '编辑分类', click: handler  },
                { text: '物理删除分类', click: handler },
                { text: '逻辑删除分类', click: handler }
                ]
                });
		}
	
		if(this.rootMenu==null){
			this.rootMenu=$.ligerMenu({ top: 100, left: 100, width: 120, items:
		        [{ text: '增加分类', click: handler  }]
		         });
		};

		if(isRoot==1){
			return this.rootMenu;
		}
		else{
			return this.menuMenu;
		}
	};
};
