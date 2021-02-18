/**
 * 产品数据包结构树
 */
GlobalPackageType=function(catKey,divId,conf,mappingUrl,id,pid,nodeName){
	{	
		this.catKey=catKey;
		//ztree的id
		this.divId=divId;
		this.conf=conf;
		this.mappingUrl = mappingUrl;	
		this.id = id;
		this.pid = pid;
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
				key:{name:"Name",
					type:"Type",
					productId:"ProductId",
					projectId:"ProjectId"},//树节点名称
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

		//默认选中根节点
		$(function(){
			$.ajax({  
		        async : false,  
		        cache : false,  
		        type: 'POST',  
		        dataType : "json", 
		        data:{
		        	nodeName : _self.nodeName
		        },
		        url: __ctx + _self.mappingUrl + "getTreeData.do?id="+_self.id+"&pid="+_self.pid,
		        success:function(data){                   
		            _self.glTypeTree.addNodes(null,data);

		            var rootNode = getRootNdoe(_self.glTypeTree);
		    		_self.glTypeTree.selectNode(rootNode,false);
		            var toolTree = $.fn.zTree.getZTreeObj("pTree");
					var treeNodes = toolTree.transformToArray(toolTree.getNodes());
					for (var i = 0; i < treeNodes.length; i++) {
						var element = treeNodes[i];
						if (element.Type=='root') {
							//element.icon = 'xiaohuojian';
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
	
	
	//树节点上下移动后的刷新（默认选中之前选中的节点）
	this.reFreshLoadGlobalTree=function(Id){

		var setting = {
			async: {enable: false},//同步
			data: {
				key:{name:"Name",
					type:"Type",
					productId:"ProductId",
					projectId:"ProjectId"},//树节点名称
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

		//默认选中刷新之前选中的节点
		$(function(){
			$.ajax({  
		        async : false,  
		        cache : false,  
		        type: 'POST',  
		        dataType : "json", 
		        data:{
		        	nodeName : _self.nodeName
		        },
		        url: __ctx + _self.mappingUrl + "getTreeData.do?id="+_self.id+"&pid="+_self.pid,
		        success:function(data){                   
		            _self.glTypeTree.addNodes(null,data);

		            var selectNode = getRootNdoe(_self.glTypeTree);
		    	//	_self.glTypeTree.selectNode(rootNode,false);
		            var toolTree = $.fn.zTree.getZTreeObj("pTree");
					var treeNodes = toolTree.transformToArray(toolTree.getNodes());
					
					for (var i = 0; i < treeNodes.length; i++) {
						var element = treeNodes[i];
						if (element.id==Id) {
							selectNode=element;
							break;
						}
					}
					_self.glTypeTree.selectNode(selectNode,false);
					for (var i = 0; i < treeNodes.length; i++) {
						var element = treeNodes[i];
						if (element.Type=='root') {
							//element.icon = 'xiaohuojian';
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
	
	
	
	
	//右击事件处理
	this.rightClickHandler=function(event, treeId, treeNode){
		_self.currentNode=treeNode;
		if( _self.conf.onRightClick){
			_self.conf.onRightClick(event, treeId, treeNode);
		}
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
	
	//添加或编辑
	this.editNode=function(isAdd){
		var id=_self.currentNode.id;
		var name= _self.currentNode.Name;
		var productId = _self.currentNode.ProductId;
		var projectId = _self.currentNode.ProjectId;		
		var url=null;
		var title=null;		
		
		if (isAdd) {
			//var tempUrl =  _self.currentNode.tempUrl.replace("detailData","editData");
			var tempUrl ="/dataPackage/tree/ptree/editData.do?__displayId__=10000000410255";

			var userId = "";
			var userName = "";
			$.ajax({
				  type: "POST",
			      url:__ctx+"/dataPackage/tree/ptree/getUser.do",
				  data:{},
			      async:false,
			      dataType : "json", 
			      success:function(data){
			    	  userId = data[0].USERID;
			    	  userName = data[0].FULLNAME;
			      }   		  
			});
			 url = __ctx + tempUrl + "&__dbomFKName__="+name+"&__dbomFKValue__="+id+"&productId="
					+productId+"&projectId="+projectId+"&userId="+userId+"&userName="+userName;
			 title = '新增';
				 
		} else {			
			var parentNode = _self.currentNode.getParentNode();
			//var tempUrl =  parentNode.tempUrl.replace("detailData","editData");
			var tempUrl ="/dataPackage/tree/ptree/editData.do?__displayId__=10000000410255";
			url = __ctx + tempUrl + "&__pk__="+ id +"&__dbomFKName__="+parentNode.Name+"&__dbomFKValue__="
						+parentNode.id+"&productId="+productId+"&projectId="+projectId;
			title = '编辑';
		}
		
	 	url=url.getNewUrl();
	 	url = encodeURI(url);

	 	DialogUtil.open({
			height:600,
			width: 900,
			title : title,
			url: url, 
			isResize: true,
			//自定义参数?
			sucCall:function(rtn){
				if(rtn == "ok"){
					reFresh();
					var url=__ctx + mappingUrl +"detailData.do?__displayId__=10000000410255&__pk__="+id
						+"&__dbomFKName__="+name.split(",")[1];
					$("#baseInfo").attr("src",url);
	 			}
			}
		});

	}
	
	//删除
	this.delNode=function (){
		var id=_self.currentNode.id;

		if(id!=0){			
			var url=__ctx + _self.mappingUrl + "del.do?";	
			var params={id:id};
			$.ligerDialog.confirm("此操作将删除该节点，确认删除吗？","提示信息", function(rtn) {
				if(rtn){
					$.post(url,params,function(result){
						var obj=new  com.ibms.form.ResultMessage(result);
						if(obj.isSuccess()){
							$.ligerDialog.success("删除成功!","提示信息",function(){
								reFresh();
							});
						} else {
							$.ligerDialog.error(obj.getMessage(),'提示信息');
						}
		    		});
				}
			});
		}
	};
//	
//	//检索到的节点颜色改变
//	function getFontCss(treeId,treeNode) {
//		return(treeNode.highlight)?{"font-weight":"bold"}:{"font-weight":"normal"};
//	}
	
	//刷新
	function reFresh() {
		_self.loadGlobalTree();

	}
	function getRootNdoe(tree){
		var rootNode = tree.getNodesByFilter(function(node){
			return node.level==0})[0];
		return rootNode;
	}
}