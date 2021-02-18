PackageViewTree=function(catKey,divId,conf,mappingUrl,id,nodeName){
	{	
		this.catKey=catKey;
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
				key:{
					name:"Name",
					type:"Type",
					productId:"ProductId",
					projectId:"ProjectId",
					unionId: "unionId"
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
		
		var url = __ctx + _self.mappingUrl;
		if (_self.divId == "projTree") {
			url += "getProjectTree.do";
		} else if (_self.divId == "prodTree") {
			url += "getProductTree.do";
		}
		
		//默认选中根节点
		$(function(){
			$.ajax({  
		        async : false,  
		        cache : false,  
		        type: 'POST',  
		        dataType : "json", 
		        data:{
		        	nodeName : _self.nodeName,
		        	id : _self.id
		        },
		        url: url,
		        success:function(data){       
		            _self.glTypeTree.addNodes(null,data);
		            var rootNode = getRootNdoe(_self.glTypeTree);
		            
		    		_self.glTypeTree.selectNode(rootNode,false);
		    		var toolTree = $.fn.zTree.getZTreeObj(_self.divId);
					var treeNodes = toolTree.transformToArray(toolTree.getNodes());
					
					for (var i = 0; i < treeNodes.length; i++) {
						var element = treeNodes[i];
						if (element.Type=='root') {
							element.icon = 'xiaohuojian';
						} else if(element.Type=='fcType'){
							element.icon = '1180huojiandaodan';
						} else if(element.Type=='普通分类节点'){
							element.icon = 'ic_layers_px';
						} else{
							element.icon = 'shezhi';
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
	
	//展开收起
	this.treeExpandAll=function(type){
		_self.glTypeTree.expandAll(type);
	};
	
	//刷新
	this.reFresh=function() {
		_self.loadGlobalTree();		
	}
	
	function getRootNdoe(tree){
		var rootNode = tree.getNodesByFilter(function(node){
			return node.level==0})[0];
		return rootNode;
	}
}