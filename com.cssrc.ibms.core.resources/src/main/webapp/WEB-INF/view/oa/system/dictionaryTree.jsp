<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>数据字典</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<%@include file="/commons/include/form.jsp" %>
	<f:gtype name="CAT_DIC" alias="CAT_DIC"></f:gtype>
	<f:link href="Aqua/css/ligerui-all.css"></f:link>
	<f:link href="tree/zTreeStyle.css"></f:link>
	<f:link href="web.css" ></f:link>
	<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/GlobalType.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/GlobalMenu.js"></script>
	<script type="text/javascript">
		var catKey="${CAT_DIC}";
		var menuMenu;
		var dictTree;
		var dicMenu=new DicMenu();
		var curMenu;
		var selectDictionaryId=0;
			
		var globalType=new GlobalType(catKey,"glTypeTree",
			{
				onClick:zTreeOnLeftClick,
				onRightClick:catRightClick,
				expandByDepth:0
			}
		);
		
		
		$(function(){
			layout();
			globalType.loadGlobalTree();
		});
	
		//布局
		function layout(){
			$("#layout").ligerLayout( {leftWidth : 210,height: '100%', onHeightChanged: heightChanged});
			//取得layout的高度
	        var height = $(".l-layout-center").height();
			
	        $("#glTypeTree").height(height-60);
		};
		//布局大小改变的时候通知tab，面板改变大小
	    function heightChanged(options){
	     	//$("#glTypeTree").height(options.middleHeight - 60);
	    };
	    
	    function hiddenMenu(){
			if(curMenu){
				curMenu.hide();
			}
			if(menuMenu){
				menuMenu.hide();
			}
		}
	    
	    function handler(item){
           	hiddenMenu();
           	var txt=item.text;
           	switch(txt){
           		case "增加字典分类":
           			globalType.openGlobalTypeDlg(true);
           			break;
           		case "编辑分类":
           			globalType.openGlobalTypeDlg(false);
           			break;
           		case "导入":
           			globalType.importNode();
           			break;
           		case "导出":
           			globalType.exportNode();
           			break;
           		case "排序":
           			globalType.sortNode();
           			break;
           		case "物理删除":
           			globalType.delNode();
           			break;
           		case "逻辑删除":
           			globalType.delLogicNode();
           			break;
           		case "还原":
           			globalType.restore();
           			break;
           	}
        }
		
	    
		/**
    	 * 树右击事件
    	 */
    	function catRightClick(event, treeId, treeNode) {
    		hiddenMenu();
    		if (treeNode) {
    			globalType.currentNode=treeNode;
    			globalType.glTypeTree.selectNode(treeNode);
    			curMenu=dicMenu.getMenu(treeNode, handler);
    			if(curMenu){
    				justifyRightClickPosition(event);
    				curMenu.show({ top: event.pageY, left: event.pageX });	
    			}
    		}
    	};
		
		
		//左击
		function zTreeOnLeftClick(treeNode){
			if(treeNode.isRoot==undefined){
				//对于已经逻辑删除的不显示信息
				if(treeNode.gltype_delFlag == 1){
					var html = "<div style=\"text-align: center;margin-top: 10%;\">该分类已被逻辑删除，若要操作该分类请先右键还原该分类!</div>";
					$("#dictTree").html(html);
				}else{
					loadDictByTypeId();
				}
			}
				
		};
		//展开收起
		function treeExpandAll(type){
			globalType.treeExpandAll(type);
		};

		
		/**
		*获取选择的节点。
		*/
		function getSelectNode(){
			dictTree = $.fn.zTree.getZTreeObj("dictTree");
			var nodes = dictTree.getSelectedNodes();
			if(nodes){
				selectDictionaryId=nodes[0].dicId;
				return nodes[0];
			}
			return null;
		}

		//加载数据字典
		function loadDictByTypeId(){
			var selectNode=globalType.currentNode;
			if(!selectNode){
				$.ligerDialog.warn('没有选择节点');
				return;
			}
			var dropInner=selectNode.type==1;
			
			var typeId=selectNode.typeId;
			var setting = {
					edit: {
						drag: {
							prev: true,
							inner: dropInner,
							next: true,
							isMove:true
						},
						enable: true,
						showRemoveBtn: false,
						showRenameBtn: false
					},
					data: {
						key : {name: "itemName"},
						simpleData: {enable: true,idKey: "dicId",pIdKey: "parentId"}
					},
					view: {selectedMulti: false},
					callback:{onRightClick: dictRightClick,
						onDrop: onDrop,beforeDrop:onBeforeDrop }
				};
			var url="${ctx}/oa/system/dictionary/getByTypeId.do";
			var params={typeId:typeId};
			
			$.post(url,params,function(result){
				for(var i = 0;i<result.length;i++){
					//拼接显示的数据,根节点不做处理
					if(result[i].parentId != 0){
						result[i].itemName += "-" + result[i].itemKey + "-" + result[i].itemValue;
					}
					if(result[i].dic_delFlag == "1"){//已被逻辑删除的节点后面跟着一个*
						result[i].itemName += "*";
					}
				}
				updDicRootNode(result);
				dictTree=$.fn.zTree.init($("#dictTree"), setting,result);

				var expandDepth = 0;
				if(expandDepth!=0)
				{
					dictTree.expandAll(false);
					var nodes = dictTree.getNodesByFilter(function(node){
						return (node.level < expandDepth);
					});
					if(nodes.length>0){
						for(var i=0;i<nodes.length;i++){
							dictTree.expandNode(nodes[i],true,false);
						}
					}
				}
				
				if(selectDictionaryId>0){
					var node = dictTree.getNodeByParam("dicId", selectDictionaryId, null);
					dictTree.selectNode(node);
				}
			});
		}
		
		function onBeforeDrop(treeId, treeNodes, targetNode, moveType){
			if(targetNode.isRoot==1 && moveType!="inner"){
				return false;
			}
			return true;
		}
		
		function onDrop(event, treeId, treeNodes, targetNode, moveType) {
			if(targetNode==null || targetNode==undefined) return false;
			var targetId=targetNode.dicId;
			var dragId=treeNodes[0].dicId;
			var url=__ctx + "/oa/system/dictionary/move.do";
			var params={targetId:targetId,dragId:dragId,moveType:moveType};

			$.post(url,params,function(result){});
		}
		
		//标记根节点。
		function updDicRootNode(result){
			for(var i=0;i<result.length;i++){
				var node=result[i];
				if(node.parentId==0){
					node.isRoot=1;
					node.parentId==0;
					node.itemValue=node.itemName;
					node.icon=__ctx + "/styles/default/images/icon/root.png";
					break;
				}
			}
		}
		
		//编辑字典数据
		function editDict(){
			var selectNode=getSelectNode();
			var dicId=selectNode.dicId;
			
			if(selectNode.isRoot==1){
				return ;
			}
			var url="${ctx}/oa/system/dictionary/edit.do?dicId=" + dicId +"&isAdd=0";
			var conf={callBack:function(){
				loadDictByTypeId();
			}};
			hiddenMenu();
			url=url.getNewUrl();
		
			DialogUtil.open({
				height:250,
				width: 450,
				title : '字典数据',
				url: url, 
				isResize: true,
				//自定义参数
				conf: conf
			});
		}
		
		function dictRightClick(e, treeId, treeNode){
			 if (treeNode) {
			    dictTree.selectNode(treeNode);
			    hiddenMenu();
			    menuMenu = getDictMenu(treeNode);
			    if(menuMenu){
    				justifyRightClickPosition(e);
    			    menuMenu.show({ top: e.pageY, left: e.pageX });
			    }
			   }
			  }
		var men1,men2,men3;
		function getDictMenu(treeNode){
		   //var items=new Array();
		   if(treeNode.isRoot==1){
		    if(!men1)
		    men1=$.ligerMenu({ top: 100, left: 100, width: 120, items:
		           [{ text: '增加字典项', click:addDict },
		           { text: '排序', click:sortDict }]
		          });
		    return men1;
		   }
		   else{
		    if(treeNode.type==1){
	    	 if(treeNode.dic_delFlag == "1"){
	    		 men2=$.ligerMenu({ top: 100, left: 100, width: 120, items:
			            [{ text: '还原', click:restore },
			         	 { text: '物理删除', click:delDict } ,]});
	    	 }else{
	    		 men2=$.ligerMenu({ top: 100, left: 100, width: 120, items:
			            [{ text: '增加字典项', click:addDict },
			             { text: '编辑字典项', click:editDict},
			             { text: '排序', click:sortDict} ,
			         	 {text: '物理删除', click:delDict } ,
			         	 { text: '逻辑删除', click:delLogicDict } ,]});
	    	 }
		     return men2;
		    }else{
	    	 if(treeNode.dic_delFlag == "1"){
	    		 men3=$.ligerMenu({ top: 100, left: 100, width: 120, items:
			            [{ text: '还原', click:restore },
			         	 { text: '物理删除', click:delDict } ,]});
	    	 }else{
	    		 men3=$.ligerMenu({ top: 100, left: 100, width: 120, items:
			            [{ text: '增加字典项', click:addDict },
			             { text: '编辑字典项', click:editDict},
			             { text: '排序', click:sortDict} ,
			          	 {text: '物理删除', click:delDict } ,
			          	 { text: '逻辑删除', click:delLogicDict } ,]});
	    	 }
		     return men3;
		    }
		   }
		  //	menuMenu=$.ligerMenu({ top: 100, left: 100, width: 120, items:items});
		   return menuMenu;
		  }
		
		//增加字典
		function addDict(){
			var selectNode=getSelectNode();
			var dicId=selectNode.dicId;
			var url="${ctx}/oa/system/dictionary/edit.do?dicId=" + dicId +"&isAdd=1";
			if(selectNode.isRoot){
				url+="&isRoot=1";
			}
			var conf={callBack:function(){
				loadDictByTypeId();
			}};
			hiddenMenu();
			url=url.getNewUrl();
			DialogUtil.open({
				height:260,
				width: 450,
				title : '增加字典',
				url: url, 
				isResize: true,
				//自定义参数
				conf: conf
			});
		}
		//数字字典排序
		function sortDict(){
			var selectNode=getSelectNode();
			var dicId=selectNode.dicId;
			var url="${ctx}/oa/system/dictionary/sortList.do?parentId=" + dicId;
			var conf={callBack:function(){
				loadDictByTypeId();
			}};
			hiddenMenu();
			url=url.getNewUrl();

			DialogUtil.open({
				height:550,
				width: 450,
				title : '数字字典排序',
				url: url, 
				isResize: true,
				//自定义参数
				conf: conf,
				sucCall:function(rtn){
					loadDictByTypeId();
				}
			});
		}
		//删除字典
		function delDict(){
			var callback=function(rtn){
				if(!rtn) return;
				var selectNode=getSelectNode();
				var dicId=selectNode.dicId;
				selectDictionaryId=selectNode.getParentNode().dicId;
				var url="${ctx}/oa/system/dictionary/del.do";
				var params={dicId:dicId};
				$.post(url,params,function(responseText){
					
					var obj=new  com.ibms.form.ResultMessage(responseText);
					if(obj.isSuccess()){
						$.ligerDialog.success("删除字典项成功!","提示信息",function(){
							loadDictByTypeId();
						});
					}
					else{
						$.ligerDialog.error("删除字典项失败!","提示信息");
					}
				});
			};
			$.ligerDialog.confirm('将删除该字典项及下面的所有字典项目，确认删除吗？','提示信息',callback);
		}
		//逻辑删除字典
		function delLogicDict(){
			var callback=function(rtn){
				if(!rtn) return;
				var selectNode=getSelectNode();
				var dicId=selectNode.dicId;
				//有些数据字典没有父节点
				//selectDictionaryId=selectNode.getParentNode().dicId;
				var url="${ctx}/oa/system/dictionary/delLogic.do";
				var params={dicId:dicId};
				$.post(url,params,function(responseText){
					
					var obj=new  com.ibms.form.ResultMessage(responseText);
					if(obj.isSuccess()){
						$.ligerDialog.success("逻辑删除字典项成功!","提示信息",function(){
							loadDictByTypeId();
						});
					}
					else{
						$.ligerDialog.error("逻辑删除字典项失败!","提示信息");
					}
				});
			};
			$.ligerDialog.confirm('将逻辑删除该字典项及下面的所有字典项目，确认逻辑删除吗？','提示信息',callback);
		}
		//还原被逻辑删除的分类
		function restore() {
			var callback = function(rtn) {
				if(!rtn) return;
				var selectNode=getSelectNode();
				var dicId=selectNode.dicId;
				
				var url="${ctx}/oa/system/dictionary/restore.do";
				var params={dicId:dicId};
				$.post(url, params, function(result) {
					var obj=new  com.ibms.form.ResultMessage(result);
					if(obj.isSuccess()){
						$.ligerDialog.success("逻辑删除字典项还原成功!","提示信息",function(){
							loadDictByTypeId();
						});
					}
					else{
						$.ligerDialog.error("逻辑删除字典项还原失败!","提示信息");
					}
				});
			};
			$.ligerDialog.confirm("确认要还原已逻辑删除的分类吗？", '提示信息', callback);

		};
		
	</script>
	
</head>
<body>
<div id="layout">
	<div position="left"  title="数据字典分类">
		<div class="tree-toolbar">
			<div class="toolBar">
				<div class="group"><a class="link reload" id="treeFresh" href="javascript:globalType.loadGlobalTree();" >刷新</a></div>
				
				<div class="group"><a class="link expand" id="treeExpandAll" href="javascript:treeExpandAll(true)" >展开</a></div>
				
				<div class="group"><a class="link collapse" id="treeCollapseAll" href="javascript:treeExpandAll(false)" >收起</a></div>
			</div>
		</div>
		<div id="glTypeTree" class="ztree"></div>
	</div>
	<div position="center" title="数据字典管理" style=" overflow:scroll">
		<div class="tipbox">
			<a href="####" class="tipinfo">
				<span >
					注：已逻辑删除的数据后面有一个*标识;
					<br/>
					添加数据字典项，点击右键操作,也可以使用鼠标进行拖动
				</span>
			</a>
		</div>
		<div style="width:100%;display:inline;">
			<span style="color:red;">&nbsp;&nbsp;数据字典数据显示的格式为"name-key-value",name用于显示,key确认唯一性,value用于存储</span>
		</div>
		<div id="dictTree" class="ztree" style="margin-top:0px;"></div>
	</div>
	
</div>
</body>
</html>

