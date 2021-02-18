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
					type:"type",
					status:"status"
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
			debugger;
			$.ajax({
		        async : false,
		        cache : false,
		        type: 'POST',
		        dataType : "json",
		        url: __ctx + _self.mappingUrl + "getTreeData.do?projectId="+_self.id+"&projectName=全部任务",
		        success:function(data){
		        
		            _self.glTypeTree.addNodes(null,data);
		            var toolTree = $.fn.zTree.getZTreeObj("pTree");
					var treeNodes = toolTree.transformToArray(toolTree.getNodes());

					for (var i = 0; i < treeNodes.length; i++) {
						var element = treeNodes[i];
//						console.log(element)
						if (element.type=='root') {
							//element.icon = 'xiaohuojian';
							//element.icon = '1180huojiandaodan';
							element.icon = 'file';
						} else if(element.type=='folder'){
							element.icon = 'file';
						} else if (element.status == "未完成") {
							element.icon = 'biaodanunfinish';
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
			url = __ctx + tempUrl + "&__dbomFKName__="+name+"&__dbomFKValue__="+id+"&productId="
						+productId+"&projectId="+projectId;
			title = '新增';
		} else {
			var parentNode = _self.currentNode.getParentNode();
			var tempUrl =  parentNode.tempUrl.replace("detailData","editData");
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
							$.ligerDialog.error('删除失败!','提示信息');
						}
		    		});
				}
			});
		}
	};

	//刷新
	function reFresh() {
		_self.loadGlobalTree();
	}
}
