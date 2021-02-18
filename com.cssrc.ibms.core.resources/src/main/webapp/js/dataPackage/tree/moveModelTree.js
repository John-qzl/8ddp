/**
 * 
 */
GlobalPackageType=function(catKey,divId,conf,mappingUrl,id,nodeName){
	{	
		this.catKey=catKey;
		//ztree的id
		this.divId=divId;
		this.conf=conf;
		this.mappingUrl = mappingUrl;	
		this.id = id;
		this.nodeName = nodeName;
		this.glTypeTree=null;
		this.currentNode=null;	
		var _self=this;
	};
	//加载树
	this.loadGlobalTree=function(){
		var setting = {
			async: {enable: false},//同步
			data: {
				key:{name:"name",
					type:"type"
					},
				simpleData: {//简单数据模式（Array）
					enable: true,
					idKey: "id",
					pIdKey: "parentId"
				}
			},
			view: {
				selectedMulti: false,//同时选中多个节点
				showLine: true
			},
			callback:{
				onClick: this.clickHandler,
				onRightClick: this.rightClickHandler
			}
		};
		_self.glTypeTree = $.fn.zTree.init($("#" + _self.divId), setting);	
		$(function(){
			$.ajax({  
		        async : false,  
		        cache : false,  
		        type: 'POST',  
		        dataType : "json",  
		        url: __ctx + _self.mappingUrl + "getFolderTree.do?projectId="+_self.id+"&projectName="+_self.nodeName,
		        success:function(data){    
		            _self.glTypeTree.addNodes(null,data);
		            var toolTree = $.fn.zTree.getZTreeObj("pTree");
					var treeNodes = toolTree.transformToArray(toolTree.getNodes());
					for (var i = 0; i < treeNodes.length; i++) {
						var element = treeNodes[i];
						if (element.type=='root') {
							element.icon = 'xiaohuojian';
						} else if(element.type=='folder'){
							element.icon = 'file';
						} else{
							element.icon = 'yulanbiaodan';
						}
						toolTree.updateNode(element);
					}
		        }, 
		        error:function(){
	    			$.ligerDialog.error('数据包结构树加载失败！');
				}
		    }); 
		});
	};
	
	//左击事件处理
	this.clickHandler=function(event, treeId, treeNode){
		_self.currentNode=treeNode;
		if(_self.conf.onClick){
			_self.conf.onClick(event, treeId, treeNode);
		}
	};
}