GlobalMenu=function(){
	{
		this.rootMenu=null;
		this.menuMenu=null;
		this.treeNode=null;
		this.menuMenuFlat=null;
	};
	this.getMenu=function(treeNode,handler){
		this.treeNode=treeNode;
		var isRoot=0;
		if(treeNode.isRoot) isRoot=1;
		//判断是否已逻辑删除
		if(treeNode.gltype_delFlag == "1"){
			this.menuMenu=$.ligerMenu({ top: 100, left: 100, width: 120, items:
		        [{ text: '还原', click:handler  },
			        { text: '物理删除', click:handler }]
			        });
		}else{
			this.menuMenu=$.ligerMenu({ top: 100, left: 100, width: 120, items:
		        [{ text: '增加分类', click:handler  },
			        { text: '物理删除', click:handler },
			        { text: '逻辑删除', click:handler },
			        {text:'导出',click:handler},
			        {text:'导入',click:handler},
			        {text:'排序',click:handler},
			        { text: '刷新', click: handler }]
			        });
		}
		if(this.menuMenuFlat==null){
				this.menuMenuFlat=$.ligerMenu({ top: 100, left: 100, width: 120, items:
			        [   { text: '删除', click:handler },
				        {text:'导出',click:handler},
				        {text:'导入',click:handler}]
				        });
		}
		if(this.rootMenu==null){
			this.rootMenu=$.ligerMenu({ top: 100, left: 100, width: 120, items:
		        [{ text: '增加分类', click: handler  },
		         {text:'导出',click:handler},
		         {text:'导入',click:handler},
		         {text:'排序',click:handler},
		         { text: '刷新', click: handler  }]
		         });
		};

		if(isRoot==1){
			return this.rootMenu;
		}
		else{
		
			if(treeNode.type==0){
			
				return this.menuMenuFlat;
			}
				
			return this.menuMenu;
		}
		
		
	};
};

FlowTypeMenu=function(){
	{
		this.rootMenu=null;
		this.menuMenu=null;
		this.treeNode=null;
	};
	this.getMenu=function(treeNode,handler){
		this.treeNode=treeNode;
		var isRoot=0;
		if(treeNode.isRoot) isRoot=1;
		
		if(treeNode.gltype_delFlag == "1"){
			this.menuMenu = $.ligerMenu({top: 100, left: 100, width: 120, items:
                [
                { text: '还原', click:handler  },
			    { text: '物理删除', click:handler }
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

ReportTypeMenu=function(){
	{
		this.rootMenu=null;
		this.menuMenu=null;
		this.treeNode=null;
	};
	this.getMenu=function(treeNode,handler){
		this.treeNode=treeNode;
		var isRoot=0;
		if(treeNode.isRoot) isRoot=1;
		
		if(treeNode.gltype_delFlag == "1"){
			this.menuMenu = $.ligerMenu({top: 100, left: 100, width: 120, items:
                [
                 { text: '还原', click:handler  },
			     { text: '物理删除', click:handler }
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

/**
 * 数据字典菜单。
 * @returns {DiclMenu}
 */
DicMenu=function(){
	{
		this.rootMenu=null;
		this.menuMenu=null;
		this.treeNode=null;
	};
	this.getMenu=function(treeNode,handler){
		this.treeNode=treeNode;
		var isRoot=0;
		if(treeNode.isRoot) isRoot=1;
		
		if(treeNode.gltype_delFlag == "1"){
			var items=[{ text: '还原', click:handler  },
					   { text: '物理删除', click:handler }];
		}else{
			var items=[{ text: '增加字典分类', click:handler  },
				         { text: '编辑分类', click: handler },
				         { text: '导出', click: handler  },
				         { text: '导入', click: handler  },
				         { text: '排序', click: handler },
				         { text: '逻辑删除', click:handler },
					     { text: '物理删除', click:handler }];
		}
		
		if(treeNode.type==0){
			items.splice(0, 1);
		}
		this.menuMenu=$.ligerMenu({ top: 100, left: 100, width: 120, items:items});
	
		if(this.rootMenu==null){
			this.rootMenu=$.ligerMenu({ top: 100, left: 100, width: 120, items:
		        [{ text: '增加字典分类', click: handler  },
		         { text: '导出', click: handler  },
		         { text: '导入', click: handler  },
		         { text: '排序', click: handler }]
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


/**
 * 文件夹节点 by YangBo
 * @returns
 */
FileMenu=function(){
	{
		this.rootMenu=null;
		this.menuMenu=null;
		this.treeNode=null;
	};
	this.getMenu=function(treeNode,handler,menuRights){
		this.treeNode=treeNode;
		var isRoot=0;
		if(treeNode.isRoot) isRoot=1;
		
		if(this.menuMenu==null){
			var items=[];
			if(menuRights.filefolderadd==1){
				items.push({ text: '新增',key:'add', click: handler});
			}
			if(menuRights.filefolderedit==1){
				items.push({ text: '编辑',key:'edit', click: handler});
			}
			if(menuRights.filefolderdel==1){
				items.push({ text: '删除',key:'del', click: handler});
			}
			if(menuRights.filefolderdownload==1){
				items.push({ text: '打包下载',key:'download', click: handler});
			}
			this.menuMenu = $.ligerMenu({top: 100, left: 100, width: 120, items:items});
		};
		if(this.rootMenu==null){
			var rootItems=[];
			if(menuRights.filefolderadd==1){
				rootItems.push({ text: '新增分类',key:'add', click: handler});
			}
			if(menuRights.filefolderdownload==1){
				items.push({ text: '打包下载',key:'download', click: handler});
			}
			this.rootMenu=$.ligerMenu({ top: 100, left: 100, width: 120, items:rootItems});
		};

		if(isRoot==1){
			return this.rootMenu;
		}
		else{
			return this.menuMenu;
		}
	};
};






