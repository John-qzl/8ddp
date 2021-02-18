/**
 * 型号管理结构树
 */
GlobalType=function(catKey,divId,conf,mappingUrl,flag){
	{
		this.catKey=catKey;
		//ztree的id
		this.divId=divId;
		this.conf=conf;
		this.mappingUrl = mappingUrl;
		this.glTypeTree=null;
		this.currentNode=null;
		var _self=this;
	};

	//加载树
	this.loadGlobalTree=function(){
		var setting = {
			drag: {
				autoExpandTrigger: true,
				prev: dropPrev,
				inner: dropInner,
				next: dropNext
			},
			enable: true,
			showRemoveBtn: false,
			showRenameBtn: false,
			async: {enable: false},//同步
			data: {
				key:{name:"typeName"},//树节点名称
				simpleData: {//简单数据模式（Array）
					enable: true,
					idKey: "typeId",
					pIdKey: "parentId"
//					rootPId: "-1"
				}
			},
			view: {
				selectedMulti: false,//同时选中多个节点
				showLine: true
			},
			callback:{
				onClick: this.clickHandler,
				onRightClick: this.rightClickHandler,
				beforeDrag: beforeDrag,
				beforeDrop: beforeDrop
			}
		};


		_self.glTypeTree = $.fn.zTree.init($("#" + _self.divId), setting);
		//默认选中根节点
		var rootNode = getRootNdoe(_self.glTypeTree);
		_self.glTypeTree.selectNode(rootNode,false);
		$(function(){
			$.ajax({
				async : false,
				cache : false,
				type: 'POST',
				dataType : "json",
				url: __ctx + _self.mappingUrl + "getTreeData.do?flag="+encodeURI(flag)+"",
				success:function(data){
					//data[0].typeName = decodeURI(data[0].typeName) ;
					_self.glTypeTree.addNodes(null,data);
					for(var i = 0 ; i < data.length ; i ++){
						if(_self.currentNode != null && _self.currentNode !="" )
							if(data[i].typeId == _self.currentNode.typeId){
								_self.currentNode.typeName =data[i].typeName ;
								//修改当前发次节点下的节点的父节点名称

								$.ajax({
									async : false,
									type: 'POST',
									data : {
										parentTypeName :_self.currentNode.typeName ,
										typeId : _self.currentNode.typeId
									},
									url: __ctx + "/dataPackage/tree/ptree/doChangeParentTypeName.do",
									success:function(data){

									},
									error:function(){
									}
								});
							}
					}

					var toolTree = $.fn.zTree.getZTreeObj("sTree");
					var treeNodes = toolTree.transformToArray(toolTree.getNodes());
					for (var i = 0; i < treeNodes.length; i++) {
						var element = treeNodes[i];
						if (element.level==0) {
							element.icon = 'Group-';
						} else if(element.level==1){
							element.icon = 'xiaohuojian';
						} else{
							element.icon = '1180huojiandaodan';
						}
						toolTree.updateNode(element);
					}
				},
				error:function(){
					$.ligerDialog.error('型号发次树加载失败！');
				}
			});
		});
	};
	function dropPrev(treeId, nodes, targetNode) {
		var pNode = targetNode.getParentNode();
		if (pNode && pNode.dropInner === false) {
			return false;
		} else {
			for (var i=0,l=curDragNodes.length; i<l; i++) {
				var curPNode = curDragNodes[i].getParentNode();
				if (curPNode && curPNode !== targetNode.getParentNode() && curPNode.childOuter === false) {
					return false;
				}
			}
		}
		return true;
	}
	function dropInner(treeId, nodes, targetNode) {
		if (targetNode && targetNode.dropInner === false) {
			return false;
		} else {
			for (var i=0,l=curDragNodes.length; i<l; i++) {
				if (!targetNode && curDragNodes[i].dropRoot === false) {
					return false;
				} else if (curDragNodes[i].parentTId && curDragNodes[i].getParentNode() !== targetNode && curDragNodes[i].getParentNode().childOuter === false) {
					return false;
				}
			}
		}
		return true;
	}
	function dropNext(treeId, nodes, targetNode) {
		var pNode = targetNode.getParentNode();
		if (pNode && pNode.dropInner === false) {
			return false;
		} else {
			for (var i=0,l=curDragNodes.length; i<l; i++) {
				var curPNode = curDragNodes[i].getParentNode();
				if (curPNode && curPNode !== targetNode.getParentNode() && curPNode.childOuter === false) {
					return false;
				}
			}
		}
		return true;
	}
	
	//树节点上下移动后的刷新（默认选中之前选中的节点）
	this.reFreshLoadGlobalTree=function(Id){
		var setting = {
			async: {enable: false},//同步
			data: {
				key:{name:"typeName"},//树节点名称
				simpleData: {//简单数据模式（Array）
					enable: true,
					idKey: "typeId",
					pIdKey: "parentId"
//					rootPId: "-1"
				}
			},
			view: {
				selectedMulti: false,//同时选中多个节点
				showLine: true
			},
			callback:{
				onClick: this.clickHandler,
				onRightClick: this.rightClickHandler,
			}
		};

		//初始化型号管理树
		_self.glTypeTree = $.fn.zTree.init($("#" + _self.divId), setting);

		//默认选中之前选中的节点
	//	var rootNode = getRootNdoe(_self.glTypeTree);
	//	_self.glTypeTree.selectNode(rootNode,false);
		$(function(){
			$.ajax({
				async : false,
				cache : false,
				type: 'POST',
				dataType : "json",
				url: __ctx + _self.mappingUrl + "getTreeData.do?flag="+encodeURI(flag)+"",
				success:function(data){
					//data[0].typeName = decodeURI(data[0].typeName) ;
					_self.glTypeTree.addNodes(null,data);
					for(var i = 0 ; i < data.length ; i ++){
						if(_self.currentNode != null && _self.currentNode !="" )
							if(data[i].typeId == _self.currentNode.typeId){
								_self.currentNode.typeName =data[i].typeName ;
								//修改当前发次节点下的节点的父节点名称

								$.ajax({
									async : false,
									type: 'POST',
									data : {
										parentTypeName :_self.currentNode.typeName ,
										typeId : _self.currentNode.typeId
									},
									url: __ctx + "/dataPackage/tree/ptree/doChangeParentTypeName.do",
									success:function(data){

									},
									error:function(){
									}
								});
							}
					}

					var selectNode = getRootNdoe(_self.glTypeTree);
					var toolTree = $.fn.zTree.getZTreeObj("sTree");
					var treeNodes = toolTree.transformToArray(toolTree.getNodes());
					for (var i = 0; i < treeNodes.length; i++) {
						var element = treeNodes[i];

						if (element.typeId==Id) {
							selectNode=element;
							break;
						}
					}
					_self.glTypeTree.selectNode(selectNode,false);
					for (var i = 0; i < treeNodes.length; i++) {
						var element = treeNodes[i];
						if (element.level==0) {
							element.icon = 'Group-';
						} else if(element.level==1){
							element.icon = 'xiaohuojian';
						} else{
							element.icon = '1180huojiandaodan';
						}
						toolTree.updateNode(element);
					}
				},
				error:function(){
					$.ligerDialog.error('型号发次树加载失败！');
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
	//拖拽事件之前
	function beforeDrag(treeId, treeNodes) {
		for (var i=0,l=treeNodes.length; i<l; i++) {
			if (treeNodes[i].drag === false) {
				return false;
			}
		}
		return true;
	}
	//放置事件之前
	function beforeDrop(treeId, treeNodes, targetNode, moveType) {
		return targetNode ? targetNode.drop !== false : true;
	}

	//展开收起
	this.treeExpandAll=function(type){
		_self.glTypeTree.expandAll(type);
	};

	// 新增/编辑节点
	this.editNode=function(isAdd){
		var typeId=_self.currentNode.typeId;
		var typeName= _self.currentNode.typeName;
		var parentId = _self.currentNode.parentId;
		var Id="";
		var url=null;
		var title=null;


		if (isAdd) {
			var tempUrl =  _self.currentNode.tempUrl.replace("preview","editData");
			url = __ctx + tempUrl + "&__dbomFKName__="+typeName+"&__dbomFKValue__="+typeId + "&flag="+flag+"";
			title = '新增';
		} else {
			var parentNode = _self.currentNode.getParentNode();
			var tempUrl =  parentNode.tempUrl.replace("preview","editData");
			url = __ctx + tempUrl + "&__pk__="+ typeId +"&__dbomFKName__="+parentNode.typeName+"&__dbomFKValue__="+parentNode.typeId+ "&flag="+flag;
			title = '编辑';
		}

		url=url.getNewUrl();
		url = encodeURI(url);
		//判断（如果是根节点）则无权限限制
	/*	if(typeId=="0"){
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
		}else{
			if(parentId=="0"){
				Id=typeId;
			}else{
				Id=parentId
			}
		}*/
		var result="1";
		
		if(result=="1"){
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
		}else if(result=="2"){
			$.ligerDialog.warn("请先给该型号分配项目办负责人员！");
		}else{
			$.ligerDialog.warn("只有负责该型号的项目办人员才能对该型号及该型号下的发次操作！");
		}
	}
	
	// 删除型号节点
	this.delNode = function() {
		var typeId = _self.currentNode.typeId;
		var url = __ctx
				+ '/oa/form/dataTemplate/deleteData.do?__displayId__=10000021170075 &__pk__='
				+ typeId;
		$.ligerDialog.confirm('确认删除吗？', '提示信息', function(rtn) {
			if (rtn) {
				$.ajax({
					type : "POST",
					url : url,
					async : false,
					success : function(data) {
						$.ligerDialog.success("型号删除成功！");
					}
				});
				window.location.reload();
			}
		});

	}
	
	// 结构树操作后刷新事件
	function reFresh() {
		_self.loadGlobalTree();
		var treeNode = _self.currentNode ;
		if (treeNode.level == 0) {
        	// （静态）根节点
            url = __ctx + treeNode.tempUrl;
        } else {
            // 型号节点
            url = __ctx + treeNode.tempUrl
                    + "&__pk__=" + treeNode.typeId//型号id
                    + "&nodeName=" + treeNode.typeName;
        }
		url = encodeURI(url);
		$("#listFrame").attr("src",url);
		$("#listFrame").attr("src",$("#listFrame").attr("src"));
	}
	function getRootNdoe(tree){
		return tree.getNodesByFilter(function(node){
			return node.level==0;
		});
	}
}
/**
 * Description : 发次删除（删除前校验是否存在数据包节点）
 * Author : XYF
 * Date : 2018年9月10日上午10:03:25
 */
function projectDel(){
	var Ids = "";

	var result = "";
	var __displayId__ = $.getParameter("__displayId__");
	var i = 1;
	var __pk__ = "";
	$('input[type="checkbox"][name=ID][class=pk x-input x-input-checkbox x-input-checkbox-pk]:checked').each(function() {
		if(i==1){
			Ids = $(this).val()
		}else{
			Ids += "," + $(this).val();
		}
		i+=1;
	});
	__pk__ = Ids;
	$.ajax({
		  type: "POST",
	      url:__ctx+"/dataPackage/tree/ptree/projectDelCheck.do",
		  data:{Ids : Ids},
	      async:false,
	      success:function(data){
	    	   result = data;
	      }   		  
	});
	if(result!="0"&&result!=""){
		$.ligerDialog.warn(result+"发次下存在数据包信息，请先删除该发次下数据包！");
	}else{
//		var __dbomJHMSValue__ = decodeURI(location.href.getNewUrl().split("__dbomJHMSValue__=")[1].split("&")[0]);
		var url = __ctx+'/oa/form/dataTemplate/deleteData.do?__displayId__='+__displayId__+'&__pk__='+__pk__;
	//	var url = __ctx+'/oa/form/dataTemplate/deleteData.do?__displayId__=10000000340019';
		$.ligerDialog.confirm('确认删除吗？','提示信息',function(rtn) {
			if(rtn) {
				$.ajax({
					  type: "POST",
				      url:url,
				      async:false,
				      success:function(data){

				      }   		  
				});
				window.location.reload();
			}
		});
}
}
/**
 * Description : 型号删除（删除前校验是否存在数据包节点）
 * Author : XYF
 * Date : 2018年9月10日下午14:13:25
 */
function productDel(){
	var Ids = "";

	var result = "";
	var __displayId__ = $.getParameter("__displayId__");
	var i = 1;
	var __pk__ = "";
	$('input[type="checkbox"][name=ID][class=pk x-input x-input-checkbox x-input-checkbox-pk]:checked').each(function() {
		if(i==1){
			Ids = $(this).val()
		}else{
			Ids += "," + $(this).val();
		}
		i+=1;
	});
	__pk__ = Ids;
	$.ajax({
		  type: "POST",
	      url:__ctx+"/dataPackage/tree/ptree/productDelCheck.do",
		  data:{Ids : Ids},
	      async:false,
	      success:function(data){
	    	   result = data;
	      }   		  
	});
	if(result!="0"&&result!=""){
		$.ligerDialog.warn(result+"型号下存在发次信息，请先删除该型号下的发次！");
	}else{
//		var __dbomJHMSValue__ = decodeURI(location.href.getNewUrl().split("__dbomJHMSValue__=")[1].split("&")[0]);
		var url = __ctx+'/oa/form/dataTemplate/deleteData.do?__displayId__='+__displayId__+'&__pk__='+__pk__;
	//	var url = __ctx+'/oa/form/dataTemplate/deleteData.do?__displayId__=10000000340019';
		$.ligerDialog.confirm('确认删除吗？','提示信息',function(rtn) {
			if(rtn) {
				$.ajax({
					  type: "POST",
				      url:url,
				      async:false,
				      success:function(data){

				      }   		  
				});
				window.location.reload();
			}
		});
}
}