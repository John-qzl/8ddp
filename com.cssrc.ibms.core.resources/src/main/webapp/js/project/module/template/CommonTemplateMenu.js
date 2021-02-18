/**
 * （型号）通用模板管理结构树右键菜单定义
 */
CommonTemplateMenu=function(){
	{
		this.defMenu=null;
		this.type = null;
	};
	this.getMenu=function(treeNode,handler){
		this.type = treeNode.type;
		this.defMenu = null;
		if (this.type == "root"){
			//新增表单模板和导入模板excel的右键选项因需求变更,于2020-08-03去掉 by zmz
			 
			this.defMenu = $.ligerMenu({top: 200, left: 550, width: 150, items:
                [
                { text: '新增文件夹', click: handler },
                { text: '新增表单模板', click: handler },
                { text: '导入模板Excel', click: handler }
             /*  
                { text: '导入模板Word', click: handler },
                { text: '复制模板', click: handler }
                */
                ]
                });
		} else{
			if(this.type=="folder"){
				this.defMenu = $.ligerMenu({top: 200, left: 550, width: 150, items:
	                [
	                { text: '新增文件夹', click: handler },
	                { text: '编辑文件夹', click: handler },
	                { text: '删除文件夹', click: handler },
	                { text: '新增表单模板', click: handler },
	               
	                { text: '导入模板Excel', click: handler }
	                /*
	                { text: '导入模板Word', click: handler },
	                { text: '复制模板', click: handler }
	                */
	                ]
	                });
			}else if(this.type=="form"){
				this.defMenu = $.ligerMenu({top: 200, left: 550, width: 150, items:
	                [
	                { text: '删除表单模板', click: handler },
	                /*
	                { text: '移动表单模板', click: handler },
	                */
	                { text: '编辑表单模板', click: handler }
	                /*
	                { text: '导出表单模板', click: handler }
	                */
	                ]
	                });
			}
			
		}

		return this.defMenu;
	};
};
