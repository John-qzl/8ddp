
/**
 * 构造方法
 */
var FunctionMenu = function() {};

//参数和方法
FunctionMenu.prototype = {
		frameId : "",
		//初始化
		init : function(id) {
			this.loadMenu();
			frameId = id;
		},
		//菜单
		loadMenu : function(){
			foldMenu = $.ligerMenu({ top: 100, left: 100, width: 120, items:
				[{ text: '增加节点', click: this.addNode},
				 { text: '编辑节点', click: this.editNode},
				 { text: '删除节点', click: this.delNode },
				// { text: '导入资源', click: this.importXml },
				// { text: '导出资源', click: this.exportXml },
				 { text: '节点排序', click: this.sortNode }]
				 //{ text: '移动节点', click: this.moveNode }]
			});
			systemMenu=$.ligerMenu({ top: 100, left: 100, width: 120, items:
				[{ text: '增加节点', click: this.addNode },
				// { text: '导入资源', click: this.importXml },
				 { text: '节点排序', click: this.sortNode }]
			});

			leafMenu = $.ligerMenu({ top: 100, left: 100, width: 120, items:
				[{ text: '编辑节点', click: this.editNode  },
				 { text: '删除节点', click: this.delNode },
				 { text: '编辑URL', click: this.editUrl } ]
			});
		},

		//树右击事件
		zTreeOnRightClick : function(e, treeId, treeNode) {
			foldMenu.hide();
			leafMenu.hide();
			if (treeNode) {
				functionTree.selectNode(treeNode);
				var isfolder=treeNode.isFolder;
				var h=$(window).height() ;
				var w=$(window).width() ;
				var menuWidth=120;
				var menuHeight=75;

				var menu=null;
				if(isfolder==1){
					if(treeNode.funId!=0){
						menu=foldMenu;
					}else{
						menu=systemMenu;
					}
				}
				else if(isfolder==0){
					menu=leafMenu;
				}
				var x=e.pageX,y=e.pageY;
				if(e.pageY+menuHeight>h){
					y=e.pageY-menuHeight;
				}
				if(e.pageX+menuWidth>w){
					x=e.pageX-menuWidth;
				}
				menu.show({ top: y, left: x });
			}
		},
		//添加资源
		addNode : function (){
			var url=__ctx+"/oa/system/recFun/edit.do?parentId="+getSelectNode().funId+"&typeId="+getSelectNode().typeId;
			$("#"+frameId).attr("src",url);
		},
		//移动节点
		moveNode : function (){
			var selectNode=getSelectNode();
			if(!selectNode) return;
			var sourceId=selectNode.funId;
			if(sourceId==rootId){
				$.ligerDialog.warn('该节点为系统节点 ,不允许该操作');
				return;
			}
			var url=__ctx+"/oa/system/recFun/MoveTree.do";
			var param = {sourceNode:selectNode};
			var winArgs="dialogWidth=230px;dialogHeight=450px;help=0;status=0;scroll=0;center=1";
			url=url.getNewUrl();

			DialogUtil.open({ 
				height:550,
				width: 250,
				title : '移动节点',
				url: url, 
				isResize: true,
				//自定义参数
				param: param,
				sucCall:function(obj){
					if(!obj) return;
					var targetId = obj.targetNode.funId;
					url=__ctx+"/oa/system/recFun/move.do";
					var params={targetId:targetId,sourceId:sourceId};
					$.post(url,params,function(result){
						loadTree();
					});
				}
			});
		},
		//移动节点
		moveNode : 	function(){
			var selectNode=getSelectNode();
			if(!selectNode) return;
			var sourceId=selectNode.funId;
			if(sourceId==rootId){
				$.ligerDialog.warn('该节点为系统节点 ,不允许该操作');
				return;
			}
			var url=__ctx+"/oa/system/recFun/MoveTree.do";
			var param = {sourceNode:selectNode};
			var winArgs="dialogWidth=230px;dialogHeight=450px;help=0;status=0;scroll=0;center=1";
			url=url.getNewUrl();

			DialogUtil.open({ 
				height:550,
				width: 250,
				title : '移动节点',
				url: url, 
				isResize: true,
				//自定义参数
				param: param,
				sucCall:function(obj){
					if(!obj) return;
					var targetId = obj.targetNode.funId;
					url=__ctx+"/oa/system/recFun/move.do";
					var params={targetId:targetId,sourceId:sourceId};
					$.post(url,params,function(result){
						loadTree();
					});
				}
			});
		},

		//编辑资源
		editNode : function(){
			var selectNode=getSelectNode();
			var funId=selectNode.funId;
			if(funId==rootId){
				$.ligerDialog.warn('该节点为系统节点 ,不允许该操作');
				return;
			}
			var url=__ctx+"/oa/system/recFun/edit.do?funId="+selectNode.funId+"&typeId="+getSelectNode().typeId;
			$("#"+frameId).attr("src",url);

		},
		//导入资源
		importXml : function(){
			var selectNode=getSelectNode();
			var funId=selectNode.funId;

			var obj={funId:funId};
			var url=__ctx +"/oa/system/recFun/import.do";

			var conf=$.extend({},{dialogWidth:550 ,dialogHeight:200,help:0,status:0,scroll:0,center:1});

			url=url.getNewUrl();

			DialogUtil.open({
				height:conf.dialogHeight,
				width: conf.dialogWidth,
				title : '导入资源',
				url: url, 
				isResize: true,
				//自定义参数
				obj: obj
			});
		},

		//导出资源
		exportXml : function(){
			var selectNode=getSelectNode();
			var funId=selectNode.funId;
			window.location.href=__ctx+"/oa/system/recFun/exportXml.do?funId="+funId;
		},
		//节点排序
		sortNode : function(){
			var selectNode=getSelectNode();
			var funId=selectNode.funId;
			var url=__ctx +'/oa/system/recFun/sortList.do?funId='+funId+"&typeId="+getSelectNode().typeId;
			url=url.getNewUrl();
			DialogUtil.open({
				height:400,
				width: 600,
				title : '节点排序',
				url: url, 
				isResize: true,
				//自定义参数
				sucCall:function(rtn){
					reFresh();
				}
			});

		},
		//删除资源
		delNode : function(){
			var selectNode=getSelectNode();
			var funId=selectNode.funId;
			var typeId=getSelectNode().typeId;
			if(funId==rootId){
				$.ligerDialog.warn('该节点为系统节点 ,不允许该操作');
				return;
			}
			var callback = function(rtn) {
				if(!rtn) return;
				var url=__ctx+"/oa/system/recFun/del.do";
				var params={funId:funId,typeId:typeId};
				$.post(url,params,function(responseText){
					var obj=new com.ibms.form.ResultMessage(responseText);
					if(obj.isSuccess()){//成功
						functionTree.removeNode(selectNode);
						$("#"+frameId).attr("src","about:blank");
					}
					else{
						$.ligerDialog.error( obj.getMessage(),"出错了");
					}
				});
			};
			$.ligerDialog.confirm('确认删除吗？','提示信息',callback);
		},
		//编辑资源
		editUrl : function(){
			var selectNode=getSelectNode();
			var funId=selectNode.funId;
			var url=__ctx+"/oa/system/recFun/edit.do?funId="+funId+"&typeId="+getSelectNode().typeId;
			$("#"+frameId).attr("src",url);
		}
};

var _FunctionMenu_ = new FunctionMenu();// 默认生成一个对象