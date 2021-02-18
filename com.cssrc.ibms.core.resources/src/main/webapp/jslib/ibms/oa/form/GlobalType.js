/**
 * 分类管理
 * @param catKey
 * @param divId
 * @param conf
 * @returns {GlobalType}
 */
GlobalType=function(catKey,divId,conf,mappingUrl){
	{
		this.glTypeTree=null;
		this.currentNode=null;
		this.dataId=null;
		this.conf=conf;
		this.catKey=catKey;
		this.divId=divId;
		if(conf.dataId){//文件分类关联记录
			this.dataId=conf.dataId;
		}
		if(mappingUrl==null || mappingUrl==""){
			mappingUrl = "/oa/system/globalType/";
		this.mappingUrl = mappingUrl;
		var _self=this;
	};
	
	this.loadGlobalTree=function(){
		var setting = {
			data: {
				key : { name: "typeName"},
				simpleData: {enable: true,idKey: "typeId",pIdKey: "parentId"}
			},
			showLine: true,
			callback:{onClick: this.clickHandler,onRightClick: this.rightClickHandler}
		};
		var url=__ctx + _self.mappingUrl + "getByCatKey.do";
		if(conf.url){
			url=conf.url;
		}
		if(_self.dataId){
			url+="?dataId=" + _self.dataId;
		}
		//是否添加维度
		if(conf.dimension){
			if(url.indexOf("?")>-1){
				url+="&dimension=" + conf.dimension;
			}else{
				url+="?dimension=" + conf.dimension;
			}
		}
		//默认维度信息
		if(conf.dimensionKey){
			if(url.indexOf("?")>-1){
				url+="&dimensionKey=" + conf.dimensionKey;
			}else{
				url+="?dimensionKey=" + conf.dimensionKey;
			}
		}
	    		
        var params={catKey:this.catKey};
        $.post(url,params,function(result){
            for(var i=0;i<result.length;i++){
                var node=result[i];
                if(node.parentId==0){
                    node.icon="/";
                    node.isRoot=1;
                }else if(node.isParent=="true"){
                	node.icon="+";
                }else{
                	node.icon="-";
                }
            }
            
            _self.glTypeTree=$.fn.zTree.init($("#" + _self.divId), setting,result);
            
            var depth = _self.conf.expandByDepth;
            if(depth!=null && depth>=0)
            {
                var nodes = _self.glTypeTree.getNodesByFilter(function(node){
                    return (node.level==depth);
                });
                if(nodes.length>0){
                    for(var idx=0;idx<nodes.length;idx++){
                        _self.glTypeTree.expandNode(nodes[idx],false,false);
                    }
                }
            }
            else
            {
                _self.glTypeTree.expandAll(true);
            }
            // 设置左边树的高度
           	var toolbarHeight=$(".tree-toolbar").height();
        	var allHeight=$("#defLayout").height();
        	var treeHeight=(parseInt(allHeight)-parseInt(toolbarHeight)*3)+"px";
        	$("#" + _self.divId).css({"height":treeHeight});
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
		_self.glTypeTree.expandAll(type);
	};
	
	//物理删除
	this.delNode=function (userId){
		var typeId=_self.currentNode.typeId;
		//删除权限控制
		var curUserId=userId;
		var nodeUserId=_self.currentNode.userId;
		//非系统用户不能删除系统节点
		if(curUserId>0&&nodeUserId==0){
			$.ligerDialog.warn("该用户无删除该节点权限！");
			return;
		}
		if(typeId!=0){
			var url=__ctx + _self.mappingUrl + "del.do";
			var params={typeId:typeId};
			if(_self.dataId){
				url+="?dataId=" + _self.dataId;
			}
    		$.post(url,params,function(data){
    			_self.loadGlobalTree();
    			$.ligerDialog.success('物理删除成功!','提示信息');
    		});
		}
	};
	
	//逻辑删除
	this.delLogicNode=function (userId){
		var typeId=_self.currentNode.typeId;
		//删除权限控制
		var curUserId=userId;
		var nodeUserId=_self.currentNode.userId;
		//非系统用户不能逻辑系统节点
		if(curUserId>0&&nodeUserId==0){
			$.ligerDialog.warn("该用户无删除该节点权限！");
			return;
		}
		if(typeId!=0){
			var url=__ctx + _self.mappingUrl + "delLogic.do";
			var params={typeId:typeId};
			if(_self.dataId){
				url+="?dataId=" + _self.dataId;
			}
    		$.post(url,params,function(data){
    			_self.loadGlobalTree();
    			$.ligerDialog.success('逻辑删除成功!','提示信息');
    		});
		}
	};
	//还原
	this.restore=function (userId){
		var typeId=_self.currentNode.typeId;
		
		if(typeId!=0){
			var url=__ctx + _self.mappingUrl + "restore.do";
			var params={typeId:typeId};
			
    		$.post(url,params,function(data){
    			_self.loadGlobalTree();
    			$.ligerDialog.success('还原成功!','提示信息');
    		});
		}
	};
	//导出分类
	this.exportNode=function(){
		var catId=$("#dkey").val();
		var typeId=_self.currentNode.typeId;
		if(typeId!=0){
			window.location.href=__ctx +"/oa/system/globalType/exportXml.do?typeId="+typeId+"&catId="+catId;
		}
	}

	//导入分类
	this.importNode=function(){
		var catId=$("#dkey").val();
		var typeId=_self.currentNode.typeId;
		if(typeId!=0){
			var url=__ctx +"/oa/system/globalType/import.do";
			var obj={typeId:typeId,catId:catId};
			var dialogWidth=550;
			var dialogHeight=250;
			var conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1});
			url=url.getNewUrl();
			
			DialogUtil.open({
				height:conf.dialogHeight,
				width: conf.dialogWidth,
				title : '系统分类导入',
				url: url,
				obj : obj,
				isResize: true,
				//自定义参数
				sucCall:function(rtn){
					_self.loadGlobalTree();
				}
			});
		}
	}
	//文件节点删除
	this.delFileNode=function(){
   	 var typeId=_self.currentNode.typeId;
		if(typeId!=0){
			var url=__ctx + _self.mappingUrl + "delLogic.do";
			var params={typeId:typeId};
			if(_self.dataId){
				url+="?dataId=" + _self.dataId;
			}
			$.ligerDialog.confirm( "确认删除此节点！","提示信息", function(rtn) {
				if(rtn){
					$.post(url,params,function(data){
		    			_self.loadGlobalTree();
		    			$.ligerDialog.success('删除成功!','提示信息');
		    		});
				}
			});
		}
    };
	//打包下载
	this.download=function (){
		var typeId=_self.currentNode.typeId;
	   /* var fileUrl = __ctx+"/oa/form/dataTemplate/getFileIdsByType.do?" +
	    		"typeId=" + typeId +"&paramJson="+JSON.stringify(objJson);
	    $.ajax({
	  	  type: "POST",
	      url:fileUrl,
	  	  data:{},
	  	  dataType : "text",
	      async:false,
	      success:function(fileIds){
	    	  if(fileIds&&fileIds!=""){
	    		  window.location.href = __ctx + '/oa/system/sysFile/download.do?fileId='+fileIds;
	    	  }else{
	    		  $.ligerDialog.success("没有文件进行下载！",'提示信息');
	    	  }
	  	  }
	  });*/
		var fileUrl = __ctx+"/oa/system/sysFile/downLoadFileByType.do?";
		fileUrl += "typeId=" + typeId +"&paramJson="+JSON.stringify(objJson);
	    window.location.href = fileUrl;
	    
	};
	
	this.openGlobalTypeDlg=function(isAdd,isPrivate){
		var typeId=_self.currentNode.typeId;
		var isRoot=0
		if(_self.currentNode.isRoot){
			isRoot=1;
		}
		var url=__ctx + _self.mappingUrl + "dialog.do";
		if(isAdd){
			url+="?parentId="+typeId +"&isRoot="+isRoot;
		}else{
			url+="?typeId="+typeId;
		}
		if(isPrivate){
			url+="&isPrivate=1";
		}
		if(_self.dataId){
			url+="&dataId=" + _self.dataId;
		}
	 	url=url.getNewUrl();
	 	
	 	DialogUtil.open({
	 		height:400,
	 		width: 500,
	 		title : '增加',
	 		url: url, 
	 		isResize: true,
	 		//自定义参数
	 		sucCall:function(rtn){
	 			if(rtn.flush){
	 				_self.loadGlobalTree();
	 			}
	 		}
	 	});
	};
	this.sortNode=function(){
		var typeId=_self.currentNode.typeId;
		var url=__ctx  + _self.mappingUrl + "sortList.do?parentId="+typeId;
		if(_self.dataId){
			url+="&dataId=" + _self.dataId;
		}
	 	url=url.getNewUrl();
	 	
	 	DialogUtil.open({
	 		height:550,
	 		width: 500,
	 		title : '节点排序',
	 		url: url, 
	 		isResize: true,
	 		//自定义参数
	 		sucCall:function(rtn){
	 			_self.loadGlobalTree();
	 		}
	 	});
	}
}
}