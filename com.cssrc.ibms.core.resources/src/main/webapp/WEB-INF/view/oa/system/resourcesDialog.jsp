<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
	<title>设置图标</title>
	<%@include file="/commons/include/form.jsp" %>
	<f:link href="tree/zTreeStyle.css"></f:link>
	<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
	<script type="text/javascript">
		/*KILLDIALOG*/
		var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
		//选中的图标
		var selected;
		var imgSrc="";
		var folderTree = null;
		var selectedNode=null;
		var menu=null;
		var expandByDepth = 0;
		var layoutHeight;
		$(function() {
			var height = dialog._height?dialog._height-60:'91%';
			//布局
			$("#defLayout").ligerLayout({
				 bottomHeight :50,
				 height: height,
				 allowBottomResize:false,
				 onHeightChanged: heightChanged,
			});
			//取得layout的高度
			layoutHeight = $("#defLayout").height();
			initTab();
		});

		function heightChanged(options){
			if(options){
			    if (accordion && options.middleHeight - 28 > 0){
			    	$("#iconframecenter").height(options.middleHeight - 146);
			        accordion.setHeight(options.middleHeight + 11);
			    }
			}else{
			    var height = $(".l-layout-center").height();
					$("#iconframecenter").height(height - 146);
		    }
		}
		
		function cleanSelectIcon(){
			//移除已选择
			var selected=$("#iconfontFrame").contents().find('li.selected');
			selected.removeClass('selected');
			var icomoonselected=$("#icomoonFrame").contents().find('li.selected');
			icomoonselected.removeClass('selected');
			var imgselected=$("#iconImgFrame").contents().find('li.selected');
			imgselected.removeClass('selected');
		}
		
		function selectIcon(){
			var selected=$("#iconfontFrame").contents().find('li.selected');
			var icomoonselected=$("#icomoonFrame").contents().find('li.selected');
			var imgselected=$("#iconImgFrame").contents().find('li.selected');	
			if(selected.length==0 && icomoonselected.length==0 && imgselected.length==0){
				$.ligerDialog.success("没有选择图标！","提示信息");
				return ;
			}/* else if(selected.length>0 && icomoonselected.length>0 && imgselected.length>0){
				$.ligerDialog.success("只能选择一种类型的一个图标！","提示信息");
				return ;
			} */
			var iconVal = "";
			if(selected.length>0){
				iconVal = selected.attr("iconclass");
			}
			if(icomoonselected.length>0){
				iconVal = icomoonselected.attr("iconclass");
			}
			if(imgselected.length>0){
				iconVal = imgselected.attr("iconclass");
			}
			var obj={iconVal:iconVal};
			dialog.get('sucCall')(obj);
			dialog.close();
		}
		
		//初始化tab页
		function initTab(){
		 	var tabItems = [];
		    tab = $("#iconframecenter").ligerTab({
		    	height: layoutHeight,
		        //showSwitchInTab : true,
		        //showSwitch: true,
		        //contextmehu: true,
				dblClickToClose : false, 
				onBeforeSelectTabItem : function(tabid) {
					if(onBeforeSelectTabItemCallBack){
						return onBeforeSelectTabItemCallBack(tabid);
					}
				},
				onAfterSelectTabItem : function(tabid) {
					if(onAfterSelectTabItemCallBack){
						onAfterSelectTabItemCallBack(tabid);
					}
				}
		    });
		}
		
		//tab前置回调事件
		//前置可以通过返回 return false组织tab切换。
		var onBeforeSelectTabItemCallBack=function(id){
			//$(".l-tab-content>div[tabid='"+id+"'] .l-text.l-text-combobox").hide();
		};
		//tab后置回调事件
		var onAfterSelectTabItemCallBack=function(id){
			var frame = $("#"+id+"Frame");
			frame.attr("src",frame.attr("url"));
		};

		function loadMenu(){
			menu= $.ligerMenu({top: 100, left: 100, width: 120, items:
				[{text:'新建文件夹',click:createFolder},
				 {text:'删除文件夹',click:delFolder}
				]
			});
		}

		function createFolder(){
			$("#listFrame").attr('src','folderEdit.do');
			$("#listFrame").load(function(){
				$("#listFrame").contents().find('#parentFoler').text(selectedNode.folderName);
				$("#listFrame").contents().find('#path').val(selectedNode.folderPath);
			});
		}

		function openFolder(){
			$("#listFrame").attr('src','icons.do?path='+selectedNode.folderPath);
		}

		function delFolder(){
			var path=selectedNode.folderPath;
			$.post('delFile.do',{path:path},function(data){
				var obj=new com.ibms.form.ResultMessage(data);
				if(obj.isSuccess()){
					$.ligerDialog.success(obj.getMessage(),"提示信息",function(rtn){
						if(rtn){
							window.parent.location.reload();
						}
					});
				}else{
					$.ligerDialog.err('出错信息',"设置文件图标失败",obj.getMessage());
				}
			});
		}

		function loadTree(){
			var setting = {
					async: {enable: false},
					data : {
						key : {name : "folderName"},
						simpleData : {
							enable : true,
							idKey : "folderId",
							pIdKey : "parentId",
							rootPId : 0
							}
					},

					callback : {
						onRightClick: zTreeOnRightClick,
						onClick: zTreeOnLeftClick
					}
				};
				$.post("${ctx}/oa/system/resources/getFolderData.do",function(result) {
					for(var i=0;i<result.length;i++){
						result[i].icon="${ctx}/styles/default/images/resicon/folderClosed.gif";
					}
					folderTree = $.fn.zTree.init($("#folderTree"), setting,result);

		            if(expandByDepth!=0)
		            {
		                var nodes = folderTree.getNodesByFilter(function(node){
		                    return (node.level==expandByDepth);
		                });

		                if(nodes.length>0){
		                    for(var idx=0;idx<nodes.length;idx++){
		                    	folderTree.expandNode(nodes[idx],true,false);
		                    }
		                }
		            }
		            else
		            {
		            	folderTree.expandAll(true);
		            }
				});
		}

		//展开收起
		function treeExpandAll(type){
			resourcesTree = $.fn.zTree.getZTreeObj("folderTree");
			resourcesTree.expandAll(type);
		};

		function zTreeOnLeftClick(event, treeId, treeNode){
			selectedNode=treeNode;
			openFolder();
		}
		/**
		 * 树右击事件
		 */
		function zTreeOnRightClick(e, treeId, treeNode) {
			if (treeNode && treeId!=0) {
				folderTree.selectNode(treeNode);
				selectedNode=treeNode;

				var h=$(window).height() ;
				var w=$(window).width() ;
				var menuWidth=120;
				var menuHeight=75;

				var x=e.pageX,y=e.pageY;
				if(e.pageY+menuHeight>h){
					y=e.pageY-menuHeight;
				}
				if(e.pageX+menuWidth>w){
					x=e.pageX-menuWidth;
				}
				menu.show({ top: y, left: x });
			}
		}
	</script>
</head>
<body>
	<div id="defLayout" style="height:100%;">
		<div position="center" id="iconframecenter">
			<div tabid="iconfont" title="iconfont字体图标">
				<iframe id="iconfontFrame" src="icons.do" url="icons.do" frameborder="no" width="100%" height="100%"></iframe>
			</div>
			<div tabid="icomoon" title="icomoon字体图标">
				<iframe id="icomoonFrame" url="icomoon.do" frameborder="no" width="100%" height="100%"></iframe>
			</div>
			<div tabid="iconImg" title="图片图标">
				<iframe id="iconImgFrame" url="imgIcons.do" frameborder="no" width="100%" height="100%"></iframe>
			</div>
		</div>
	</div>
	<div position="bottom"  class="bottom">
		<div class="btn-component">
			<div class="x-btn x-btn-ok">
				<a href='####' class='link ok' onclick="selectIcon()" >选择</a>
			</div>
			<div class="x-btn x-btn-clean">
				<a href='####' class='link clean' onclick="cleanSelectIcon()" >清除</a>
			</div>
			<div class="x-btn x-btn-close">
				<a href='####' class='link cancel' onclick="dialog.close()">取消</a>
			</div>
		</div>

	</div>
</body>
</html>
