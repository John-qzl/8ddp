/**
 * DBomTree
 * @author weilei
 */
DBomTree = function(code, divId, conf, mappingUrl){
	{
		this.code = code;
		this.divId = divId;
		this.conf = conf;
		this.dbomTree = null;
		this.currentNode = null;
		if(mappingUrl==null || mappingUrl==""){
			mappingUrl = "/oa/system/dbomNode/";
		}
		this.mappingUrl = mappingUrl;
		this.url = __ctx + mappingUrl + "getTree.do?treeCode="+code;
		var _self=this;
	};
	
	this.loadDBomTree=function(){
		var setting = {
			data: {
				key : {name:"text"},
				simpleData : {enable:true, idKey:"id", pIdKey:"parentId",isParent:"isParent"}
			},
			async: {
				enable: false
			},
			showLine: true,
			callback:{
				onClick: this.clickHandler,
				onRightClick: this.rightClickHandler,
				beforeExpand:this.beforeExpandClick,
				onAsyncSuccess: this.zTreeOnAsyncSuccess
			}
		};
		//初始化tree
		this.dbomTree=$.fn.zTree.init($("#"+divId), setting);
		//加载tree data
		var mill=(parseInt(Math.random()*10000)).toString();
		$.ajax({
			url : _self.url+"&mill="+mill,
			data:{
				pcode:"",
				curcode:"",
				curId:"",
				customFilter:this.conf.customFilter,
				ptext:""
			},
			type : 'post',
			success:function(data){
				_self.dbomTree.addNodes(null,data);
				$(data).each(function(i,item){
					_self.dbomTree.expandNode(_self.dbomTree.getNodeByParam(item.id),true,false);
				})

			},
			error:function(){
    			$.ligerDialog.success('dbom tree 加载失败');
			}
		});
	};
	
	//点击事件处理
	this.clickHandler = function(event, treeId, treeNode){
		_self.currentNode = treeNode;
		if(_self.conf.onClick){
			_self.conf.onClick(treeNode);
		}
	};
	
	//展开收起
	this.treeExpandAll = function(type){
		_self.dbomTree.expandAll(type);
	};
	//获取当前节点所有父节点返回数组
	this.getAllFilter=function(filterArray, treeNode){
		var nodeType = treeNode.type;
		if(nodeType == '1'){//动态节点
			filterArray.push({
				code : treeNode.code,
				text : treeNode.text
			});
			getAllFilter(filterArray, treeNode.getParentNode());
		}
		return filterArray;
	};
	
	//节点展开之前先获取node数据
	this.beforeExpandClick=function(treeId,treeNode){
		var tabname="";
		if(treeNode.children.length==0){
			_self.appendNodeData(treeNode);
		}
	};
	//在当前节点下添加子节点
	this.appendNodeData=function(treeNode){
		var pcode = '';
		var ptext = '';
		var filterArray=_self.getAllFilter(new Array(),treeNode)
		for(var index=0; index<filterArray.length; index++){
			pcode += filterArray[index].code + ';'; 
			ptext +=	filterArray[index].text + ';'; 
		}
		pcode = ''==pcode?'':pcode.substring(0, pcode.length-1);
		ptext = ''==ptext?'':ptext.substring(0, ptext.length-1);
		var mill=(parseInt(Math.random()*10000)).toString();
		$.ajax({
			url : _self.url+"&mill="+mill,
			data:{
				pcode:pcode,
				ptext:ptext,
				curId:treeNode.id,
				curcode:treeNode.code
			},
			type : 'post',
			success:function(data){
				_self.dbomTree.addNodes(treeNode,data);
			},
			error:function(){
    			$.ligerDialog.success('dbom tree 加载失败');
			}
		});
	};
	
	//刷新选中的节点数据
	this.reloadSelectNode=function(){
		var nodes=_self.dbomTree.getSelectedNodes();
		$(nodes).each(function(i,node){
			_self.dbomTree.removeChildNodes(node);
			_self.appendNodeData(node);
			_self.dbomTree.expandNode(node,true,false);
		})
	}
}