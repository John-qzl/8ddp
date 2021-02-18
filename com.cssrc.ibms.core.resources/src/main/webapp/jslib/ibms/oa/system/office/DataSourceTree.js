/**
 * 分类管理
 * @param divId
 * @param conf
 * @returns {DataSource}
 */
DataSource=function(divId,param,action,conf){
	{
		this.dsTree=null;
		this.currentNode=null;
		this.conf=conf;
		this.param=param;
		this.divId=divId;
		this.mappingUrl = "/oa/system/officeTemplate/";
		var _self=this;
	};
	
	this.loadTree=function(){
		var setting = {
			data: {
				key : { name: "DESC_"},
				simpleData: {enable: true,idKey: "ID",pIdKey: "PID"}
			},
			check: {
				enable: true,
				chkboxType: { "Y": "", "N": "" }
			},
			showLine: true,
			callback:{onClick: this.clickHandler,onRightClick: this.rightClickHandler}
		};
		var url=__ctx + _self.mappingUrl + action;
		if(conf.url){
			url=conf.url;
		}
    		
        $.post(url,param,function(result){
            for(var i=0;i<result.length;i++){
                var node=result[i];
                if(node.PID=="-1"){
                    node.icon=__ctx +"/styles/default/images/icon/root.png";
                    node.isRoot=1;
                }
            }
            
            _self.dsTree=$.fn.zTree.init($("#" + _self.divId), setting,result);
            
            var expandAll = _self.conf.expandAll;
            if(expandAll)
            {
            	_self.dsTree.expandAll(true);
            }
            else
            {
            	_self.dsTree.expandAll(false);
            }
            // 设置左边树的高度
           	var toolbarHeight=$(".tree-toolbar").height();
        	var allHeight=$("#defLayout").height();
        	var treeHeight=(parseInt(allHeight)-parseInt(toolbarHeight)*3)+"px";
        	$("#" + _self.divId).css({"height":treeHeight});
        	//设置默认选中的节点
        	if(_self.conf.checkedData){
             	var ids=_self.conf.checkedData.split(",");
        		$(ids).each(function(i,id){
        			_self.dsTree.checkNode(_self.dsTree.getNodeByParam("id",id));
        		})
        	}
   
        });
        
	};
	
	this.rightClickHandler=function(event, treeId, treeNode){
		if( _self.conf.onRightClick){
			_self.conf.onRightClick(event, treeId, treeNode);
		}
	};
	//点击事件处理
	this.clickHandler=function(event, treeId, treeNode){
		_self.currentNode=treeNode;
		if(_self.conf.onClick){
			_self.conf.onClick(treeNode);
		}
	};
	
	//展开收起
	this.treeExpandAll=function(type){
		_self.dsTree.expandAll(type);
	};

	this.sortNode=function(){
		var typeId=_self.currentNode.typeId;
		var url=__ctx  + _self.mappingUrl + "sortList.do?parentId="+typeId;
	 	url=url.getNewUrl();
	 	DialogUtil.open({
			height:300,
			width: 600,
			title : '',
			url: url, 
			isResize: true,
			sucCall:function(rtn){
			 	//重新加载树。
			 	_self.loadGlobalTree();
			}
		});
	}
}