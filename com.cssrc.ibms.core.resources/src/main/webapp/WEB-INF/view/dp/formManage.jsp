<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>表单模板管理</title>
<%@include file="/commons/include/form.jsp"%>
<f:gtype name="CAT_FORM" alias="CAT_FORM"></f:gtype>
<%-- <f:link href="tree/zTreeStyle.css"></f:link> --%>
<link rel="stylesheet" href="${ctx}/jslib/tree/zTreeStyle.css"
	type="text/css">
<f:resources name="ROOT_PID" alias="ROOT_PID"></f:resources>
<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript"
	src="${ctx}/js/dataPackage/tree/formTableTree.js"></script>
<script type="text/javascript"
	src="${ctx}/js/dataPackage/tree/FormTableDefMenu.js"></script>
<style type="text/css">
.tree-title {
	overflow: hidden;
	width: 100%;
}

.table-a table td {
	border: 1px solid #ff35d1;
	margin-left: 15px;
	text-align: center
}

html, body {
	padding: 0px;
	margin: 0;
	width: 100%;
	height: 100%;
	overflow: hidden;
}

.layui-tab-brief>.layui-tab-title .layui-this {
	color: #347EFE;
	font-weight: bold;
}

.layui-tab-brief>.layui-tab-more li.layui-this:after, .layui-tab-brief>.layui-tab-title .layui-this:after
	{
	border-color: #347EFE;
}

.l-layout-left .l-layout-header {
	border: 1px solid #d9d9d9;
	height: 34px;
	line-height: 34px;
	color: #333;
	font-weight: bold;
	background: #fff;
}

.tree-toolbar {
	padding: 0;
	border-bottom: 1px solid #d9d9d9;
	background: #f5f5f5;
}

.group {
	margin: 0 4px;
	height: 32px;
	line-height: 32px;
}

.tree-toolbar a {
	float: none;
}

a.link {
	color: #666 !important;
}

a.link:hover {
	color: #347EFE !important;
}

a.link.reload:before, a.link.expand::before, a.link.collapse:before, a.link.import:before,
	a.link.copy:before {
	margin-right: 5px;
	color: #347EFE !important;
}

.l-layout-left .l-layout-header-toggle:before {
	color: #a9adb1;
}

.l-layout-collapse-left-toggle-over:before {
	color: #347EFE !important;
}
</style>
<script type="text/javascript">
	//表单模板页面
	//
	var catKey = "${CAT_FORM}";
	var curMenu = null;
	var formDefMenu = new FormTableDefMenu();
	var mappingUrl = "/dp/form/";
	var id = "${fcId}";
	var nodeName = "${fcName}";
	var pTree = new GlobalPackageType(catKey, "pTree", {
		onClick : onClick,
		onRightClick : zTreeOnRightClick
	}, mappingUrl, id, nodeName);
	$(function() {
		//ligerui Tab
		layui.use('element', function() {
			var element = layui.element;
		})
		//布局
		loadLayout();
		//加载树
		pTree.loadGlobalTree();
		//加载菜单
		$(document).click(hiddenMenu);

	});

	//布局
	function loadLayout() {
		$("#defLayout").ligerLayout({
			leftWidth : 230,
			onHeightChanged : heightChanged,
			allowLeftResize : false
		});
		//取得layout的高度
		var height = $(".l-layout-center").height() - 35;
		var _height = $('.tree-toolbar').height()
		$("#pTree").height(height - _height);
	}

	//布局大小改变的时候通知tab，面板改变大小
	function heightChanged(options) {
		$("#pTree").height(options.middleHeight - 90);
	}

	/**
	 * 树左击事件
	 */

	//onceClick的回调函数
	function urlAttr(obj, url) {
		$(obj).attr('src', url);
	}

	//用来加载tab对应的iframe
	function onceClick(obj, urlAttr, urlObj, url) {
		var bol = true;
		$(obj).click(function() {
			if (bol) {
				urlAttr(urlObj, url);
			}
			bol = false;
		})
	}

	function onClick(event, treeId, treeNode) {

		var content = $('.layui-tab-content');
		var _height = content.closest('.layui-tab').height()
				- content.prev('.layui-tab-title').height() - 18;
		$('.layui-tab-content').height(_height)

		var url = null;
		$('div[position="center"]>div').remove();
		//根节点
		if (treeNode.id == "1") {
			url = "";
			$('div[position="center"]').append(
					$('.templates .template3').clone(true));
			$("#rootFrame").attr("src", url);
		} else {
			//文件夹节点
			if (treeNode.type == "folder") {
				url = "${ctx}" + treeNode.tempUrl;
				$('div[position="center"]').append(
						$('.templates .template2').clone(true));
				$("#folderFrame").attr("src", url);
			}
			//表单模板
			else {
				/* var parentName = treeNode.getParentNode().Name;
				 +"&id="+treeNode.id
				 +"&type="+treeNode.Type
				 +"&name="+treeNode.Name
				 +"&__dbomFKName__="+parentName; */
				url = "${ctx}" + treeNode.tempUrl;
				$('div[position="center"]').append(
						$('.templates .template1').clone(true));

				//给iframe添加url
				//属性页
				$("#attrFrame").attr("src", url);
				//表单页
				$("#tableFrame").attr("src",
						"${ctx}" + "/dp/form/showTable.do?id=" + treeNode.id);
				//检查项页
				var itemUrl = "${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000000720002&Q_table_temp_ID_S="
						+ treeNode.id;
				//检查条件页
				var conditionUrl = "${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000000840054&Q_table_temp_ID_S="
						+ treeNode.id;
				//签署项页
				var signUrl = "${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000000720043&Q_table_temp_ID_S="
						+ treeNode.id;
				//调用onceClick函数
				onceClick('.item3', urlAttr, '#itemFrame', itemUrl);
				onceClick('.item4', urlAttr, '#conditionFrame', conditionUrl);
				onceClick('.item5', urlAttr, '#signFrame', signUrl);
			}

		}
	};

	/**
	 * 树右击事件
	 */
	function zTreeOnRightClick(event, treeId, treeNode) {
		hiddenMenu();
		var fcId = $.getParameter("fcId");
		$.ajax({
			async : 'false',
			dataType : 'json',
			data : {
				fcId : fcId
			},
			url : "${ctx}/model/user/role/getUseFcRole.do",
			success : function(data) {
				if (data.useRole == "1") {
					if (treeNode) {
						var height = $(".l-layout-center").height() - 28;
						pTree.currentNode = treeNode;
						pTree.glTypeTree.selectNode(treeNode);
						/* var flag = checkCurUserRole(); */
						var flag = true;
						if (flag) {
							ishandler(treeNode);
							if (event.pageY > height - 168) {
								curMenu.show({
									top : event.pageY - 168,
									left : event.pageX
								});
							} else {
								curMenu.show({
									top : event.pageY,
									left : event.pageX
								});
							}

						}
					}
				} else {
					$.ligerDialog.warn("您没有权限！");
				}
			}
		});

	};

	/**
	 * @Author  shenguoliang
	 * @Description:
	 * @Params
	 * @Date 2018/5/17 18:51
	 * @Return
	 */
	function checkCurUserRole() {
		var flag = false;
		var fcId = $.getParameter("fcId");
		$.ajax({
			async : false,
			dataType : 'json',
			cache : false,
			type : 'post',
			data : {
				fcId : id
			},
			url : "${ctx}/project/tree/stree/getNodeCharger.do",
			success : function(data) {
				flag = data;
			},
			error : function() {
			}
		});

		return flag;
	}

	//隐藏右键菜单
	function hiddenMenu() {
		if (curMenu) {
			curMenu.hide();
		}
	}

	//右击事件处理
	function ishandler(treeNode) {
		curMenu = formDefMenu.getMenu(treeNode, handler);
	}

	function handler(item) {
		debugger;
		hiddenMenu();
		var txt = item.text;
		switch (txt) {
		case "新增文件夹":
			createFolder();
			break;
		case "编辑文件夹":
			editFolder();
			break;
		case "删除文件夹":
			deleteFolder();
			break;

		case "新增表单模板":
			createModel();
			break;
		case "删除表单模板":
			deleteModel();
			break;
		case "导入模板Excel":
			uploadFile();
			break;
		case "导入模板Word":
			uploadFile_Word();
			break;
		case "移动表单模板":
			moveModel();
			break;
		case "复制模板":
			copyModel();
			break;
		case "编辑表单模板":
			editModel();
			break;
		case "导出表单模板":
			exportModel();
			break;
		}
	}

	//更新搜索到的节点颜色
	function updateNodes(highlight, nodes) {
		pTree.loadGlobalTree();
		for (var i = 0; i < nodes.length; i++) {
			nodes[i].highlight = highlight;
			pTree.glTypeTree.updateNode(nodes[i]);
		}
	}

	//刷新树
	function reFresh() {
		pTree.loadGlobalTree();
	}

	//新建文件夹
	function createFolder() {
		var node = pTree.currentNode;

		if (node != null) {
			if (node.type == "folder" || node.type == "root") {
				DialogUtil.open({
							url : "${ctx}/oa/form/dataTemplate/editData.do?__displayId__=10000000720049&pid="
									+ id + "&fcid=" + node.id + "&flag=create",
							width : 800,
							height : 700,
							sucCall : function(rtn) {
								reFresh();
							}
						});
				return;
			}
		}
		$.ligerDialog.error("请选择文件夹进行操作", "提示");
	}

	//编辑文件夹
	function editFolder() {
		var node = pTree.currentNode;
		if (node != null) {
			if (node.type == "folder" && node.parentId != -1) {
				DialogUtil.open({
							url : "${ctx}/oa/form/dataTemplate/editData.do?__displayId__=10000000720049&__pk__="
									+ node.id + "&pid=" + id + "&flag=editor",
							width : 800,
							height : 700,
							sucCall : function(rtn) {
								reFresh();
							}
						});
				return;
			}
		}
		$.ligerDialog.error("请选择需要编辑的文件夹", "提示");
	}

	/*
	 function getNode(NodeArray){
	 if(NodeArray!=undefined){
	 for(i =0;i<NodeArray.length;i++){
	 var cNode=NodeArray[i];
	 if(cNode.type!="folder")
	 return false;
	 else{
	 var chNodeArray=cNode.children;
	 if(parseInt(chNodeArray.length)!=0)
	 getNode(chNodeArray);
	 }
	 }
	 }else{
	 return true;
	 }
	 }
	 */
	//删除文件夹
	function deleteFolder() {
		var node = pTree.currentNode;
		if (node != null) {
			if (node.parentId != -1) {
				if (node.type == "folder") {
					var len = node.children;
					if (len == undefined) {
						$.ligerDialog.confirm(
									'是否删除该文件夹',
									function(rtn) {
										if (rtn) {
											url = "${ctx}/dp/form/dpDataTemplate/deleteData.do?__displayId__=10000000720049&__pk__="
												  + node.id;
											$.post(
													url,
												    function(data) {
														if (data.result == 1) {
															$.ligerDialog.dpTip({
																content : data.message,
																title : '提示'
																});
																		//$.ligerDialog.tip(data.message,"提示");
														 } else {
																$.ligerDialog.error(
																		data.message,"提示");
																 }
																 	//setTimeout(function(){
																reFresh();
																	//},1000)

													});
											}

								});
						return;
					} else {
						$.ligerDialog.error("该文件夹下有子文件夹或模板不能删除", "提示");
						return;
					}
				}
			}
		}
		$.ligerDialog.error("请选择需要删除的文件夹", "提示");
	}

	//新增模板
	function createModel() {
		//
		var fcId = $.getParameter("fcId");
		  $.ajax({
                async: 'false',
                dataType: 'json',
                data: {fcId:fcId},
                url: "${ctx}/model/user/role/getUseFcRole.do",
                success: function (data) {
                	if(data.useRole=="1"){
                		var node = pTree.currentNode;
                		if (node != null) {
                			if (node.type == "folder" || node.type == "root") {
                				DialogUtil.open({
                					//	url:"${ctx}/editor/checkform.jsp?pid="+id+"&fcid="+node.id,
                					url : "${ctx}/dp/form/addFormTemplate.do?pid=" + id
                							+ "&fcid=" + node.id,
                					width : 1200,
                					height : 700,
                					title : "新增模板",
                					sucCall : function(rtn) {
                						reFresh();
                					}
                				});
                				return;
                			}
                		}else {
							DialogUtil.open({
								//	url:"${ctx}/editor/checkform.jsp?pid="+id+"&fcid="+node.id,
								url : "${ctx}/dp/form/addFormTemplate.do?pid=" + id
										+ "&fcid=" + id,
								width : 1200,
								height : 700,
								title : "新增模板",
								sucCall : function(rtn) {
									reFresh();
								}
							});
						}
                		/*$.ligerDialog.error("添加模板前请选择文件夹", "提示");*/
                	}
                	else{
                		 $.ligerDialog.warn("您没有权限！");
                	}
                }
            });

	}

	//编辑表单模板
	function editModel() {
		//
		var node = pTree.currentNode;
		var id = node.id;
		var url = "${ctx}/dp/form/existInstance.do";
		var params = {
			id : id
		};
		var isCreatePeople = "0";

		$.post(url, params, function(data) {
			DialogUtil.open({
				url : "${ctx}/dp/form/editFormTemplate.do?id=" + id
						+ "&fcName=" + nodeName,
				width : 1200,
				height : 700,
				title : "编辑表单模板",
				sucCall : function(rtn) {
					window.location.reload();
				}
			});
		});
	}

	/* //导出表单模板excel
	function exportModel() {
		var node = pTree.currentNode;
		var url = "${ctx}/dp/form/exportModel.do?modelId=" + node.id;
		window.location.href = url;
	} */
	//导出表单模板excel
	function exportTempModel() {
		var fcId = $.getParameter("fcId");
		  $.ajax({
              async: 'false',
              dataType: 'json',
              data: {fcId:fcId},
              url: "${ctx}/model/user/role/getUseFcRole.do",
              success: function (data) {
              	if(data.useRole=="1"){
            		var node = pTree.currentNode;
            	    if (node != null) {
                        if (node.type == "form") {
                        	var url = "${ctx}/dp/form/exportTempExcel.do?tempId=" + node.id;
                    		window.location.href = url;
                    		return;
                        }
            	    }
            	    $.ligerDialog.error("请选择模板进行操作", "提示");
              	}
              	else{
              		 $.ligerDialog.warn("您没有权限！");
              	}
              }
          });

	}
	//删除模板
	function deleteModel() {

		var node = pTree.currentNode;
		var id = node.id;
		var isCreatePeople = "0";
		if (node != null) {
			if (node.type != "folder" && node.type != "root") {
				//只有型号管理员才可以删除
				var moduleId = $.getParameter("fcId");
				$.ajax({
					async: 'false',
					dataType: 'json',
					data: {fcId:moduleId},
					url: "${ctx}/model/user/role/getUseFcRole.do",
					success: function (data) {
						if(data.useRole=="1"){
						$.ligerDialog.confirm('是否删除该模板', function(rtn) {
						if (rtn) {
							url = "${ctx}/dp/form/deleteFormTemplate.do?id="
									+ node.id;
							$.post(url, function(data) {
								if (data.success == "true") {
                               	 $.ligerDialog.success("删除成功！","",function(){
                                      window.location.reload(true);
              						});
                                  
                               }else {
									$.ligerDialog.error(data.msg, "删除失败");
								}

							});
						}
					});
						}else {
							$.ligerDialog.warn("您没有权限！");
						}
					}
				})
				return;
			}
		}
		$.ligerDialog.error("请选择需要删除的模板", "提示");
	}

	/**
	 * 导入模板Excel
	 */
	function uploadFile(fileId, upStatus) {
		var fcId = $.getParameter("fcId");
		  $.ajax({
              async: 'false',
              dataType: 'json',
              data: {fcId:fcId},
              url: "${ctx}/model/user/role/getUseFcRole.do",
              success: function (data) {
              	if(data.useRole=="1"){
              		var node = pTree.currentNode;
            		var id = pTree.id;
            		var fcid = $.getParameter("fcid");
            		if (node != null) {
            			if (node.type == "folder" || node.type == "root") {
            				DialogUtil.open({
            					url : "${ctx}/editor/tempFileUpload.jsp?pid=" + id
            							+ "&fcid=" + fcid + "&type=" + "excel"
            							+ "parentId=" + id + "&extype=batch",
            					height : 300,
            					width : 500,
            					title : "导入模板Excel",
            					isResize : true,
            					sucCall : function(rtn) {
            						reFresh();
            					}
            				});
            				return;
            			}
            		}else {
						//如果没有选择节点,则默认根节点
							DialogUtil.open({
								url : "${ctx}/editor/tempFileUpload.jsp?pid=" + fcid
										+ "&fcid=" + "1" + "&type=" + "excel"
										+ "parentId=" + fcid + "&extype=batch",
								height : 300,
								width : 500,
								title : "导入模板Excel",
								isResize : true,
								sucCall : function(rtn) {
									reFresh();
								}
							});
							return;

					}
            		/*$.ligerDialog.error("导入模板Excel前请选择文件夹", "提示");*/
              	}
              	else{
              		 $.ligerDialog.warn("您没有权限！");
              	}
              }
          });
	
	}

	/**
	 * 导入模板Word
	 */
	function uploadFile_Word(fileId, upStatus) {

		var node = pTree.currentNode;
		if (node != null) {
			if (node.type == "folder" || node.type == "root") {
				DialogUtil.open({
					url : "${ctx}/editor/tempFileUpload.jsp?fcid=" + id
							+ "&pid=" + node.id + "&type=" + "word",
					height : 300,
					width : 500,
					title : "导入模板Word",
					isResize : true,
					sucCall : function(rtn) {
						reFresh();
					}
				});
				return;
			}
		}
		$.ligerDialog.error("导入模板Word前请选择文件夹", "提示");
	}

	//移动模板
	function moveModel() {
		var fcId = $.getParameter("fcId");
		  $.ajax({
              async: 'false',
              dataType: 'json',
              data: {fcId:fcId},
              url: "${ctx}/model/user/role/getUseFcRole.do",
              success: function (data) {
              	if(data.useRole=="1"){
              		var node = pTree.currentNode;
            		if (node != null) {
            			if (node.type != "folder" && node.type != "root") {
            				$.ligerDialog.open({
            					url : "${ctx}/dp/form/selectFolder.do?fcId=" + id
            							+ "&fcName=" + nodeName + "&nodeid=" + node.id,
            					width : 300,
            					height : 500,
            					title : "选择目标文件夹",
            					sucCall : function(rtn) {
            						reFresh();
            					}
            				});

            				return;
            			}
            		}
            		$.ligerDialog.error("请选择需要移动的模板", "提示");
              	}
              	else{
              		 $.ligerDialog.warn("您没有权限！");
              	}
              }
          });
	
	}

	//复制模板
	function copyModel() {
		var fcId = $.getParameter("fcId");
		$.ajax({
					async : 'false',
					dataType : 'json',
					data : {
						fcId : fcId
					},
					url : "${ctx}/model/user/role/getUseFcRole.do",
					success : function(data) {
						if (data.useRole == "1") {
							var node = pTree.currentNode;
							if (node != null) {
								if (node.type == "folder"
										|| node.type == "root") {
									DialogUtil.open({
												url : "${ctx}/dp/form/selectMutilModel.do?fcId="
														+ id
														+ "&fcName="
														+ nodeName
														+ "&nodeid="
														+ node.id,
												width : 300,
												height : 500,
												title : "表单模板选择器",
												sucCall : function(rtn) {
													reFresh();
												}
											});

									return;
								}
							}
							$.ligerDialog.error("复制模板前请先选择文件夹", "提示");
						} else {
							$.ligerDialog.warn("您没有权限！");
						}
					}
				});
	}

	var url = "${ctx}/dp/form/exportTempExcel.do?tempId=" + node.id;
	window.location.href = url;
	function openMyLinkDialog(CustomConf) {
		var defaultConf = {
			height : 800,
			width : 1000,
			title : "表单填写",
			params : {}
		}
		if (!CustomConf.url) {
			alert("url不能为空！");
			return;
		}
		var conf = $.extend({}, defaultConf, CustomConf);
		DialogUtil.open({
			params : conf.params,
			height : conf.height,
			width : conf.width,
			url : conf.url,
			showMax : false, //是否显示最大化按钮
			showToggle : false, //窗口收缩折叠
			title : conf.title,
			showMin : false,
			sucCall : function(rtn) {
				if (!(rtn == undefined || rtn == null || rtn == '')) {
					//location.href=location.href.getNewUrl();
				}
			}
		});
	}

	//模板批量删除
	function TemplateListDelete() {
		var fcId = $.getParameter("fcId");
		  $.ajax({
              async: 'false',
              dataType: 'json',
              data: {fcId:fcId},
              url: "${ctx}/model/user/role/getUseFcRole.do",
              success: function (data) {
              	if(data.useRole=="1"){
              		reFresh();
            		var node = pTree.currentNode;

            		var url = "${ctx}"
            				+ "/oa/form/dataTemplate/preview.do?__displayId__=10000019200641&__dbomSql__=F_PROJECT_ID="
            				+ id;
            		openMyLinkDialog({
            			url : url.getNewUrl(),
            			title : "批量删除",
            			height : 600,
            			width : 900
            		}); 
              	}
              	else{
              		 $.ligerDialog.warn("您没有权限！");
              	}
              }
          });
		

	}

	// 复制通用模板
	function copyCommon() {
		var fcId = $.getParameter("fcId");
		  $.ajax({
              async: 'false',
              dataType: 'json',
              data: {fcId:fcId},
              url: "${ctx}/model/user/role/getUseFcRole.do",
              success: function (data) {
              	if(data.useRole=="1"){
              		DialogUtil.open({
    					url : "${ctx}/template/manage/commonTemplateCopyView.do?categoryId="
    							+ id,
    					width : 300,
    					height : 500,
    					title : "通用模板选择器",
    					sucCall : function(rtn) {
    						reFresh();
    					}
    				});
    				return;
              	}
              	else{
              		 $.ligerDialog.warn("您没有权限！");
              	}
              }
          });
	
	}
</script>
</head>
<body>
	<div id="defLayout">
		<div position="left" title="表单模板"
			style="text-align: left; border: 1px solid #d9d9d9; border-top: 0px;">
			<div class="tree-toolbar" id="pToolbar">
				<div class="group">
					<a class="link reload" id="treeFresh" onclick="reFresh()">刷新</a>
				</div>
				<!-- 因布局问题,不显示展开和收起按钮 2020-08-03 by zmz -->
				<div class="group">
					<a class="link expand" id="treeExpandAll"
						href="javascript:pTree.treeExpandAll(true)">展开</a>
				</div>
				<div class="group">
					<a class="link collapse" id="treeCollapseAll"
						href="javascript:pTree.treeExpandAll(false)">收起</a>
				</div>
				<div class="group">
					<a class="link copy" id="copyCommon" href="javascript:copyCommon()">复制模板</a>
				</div>
				<div class="group">
					<a class="link collapse" id="TemplateListDelete"
						href="javascript:TemplateListDelete()">批量删除</a>
				</div>
				<!-- 下面的两个按钮原来是右击事件,于2020-08-03更改为顶部按钮 by zmz-->
				<div class="group">
					<a class="link collapse" id="TemplateListDelete"
						href="javascript:createModel()">新增模板</a>
				</div>
				<div class="group">
					<a class="link collapse" id="TemplateListDelete"
						href="javascript:uploadFile()">Excel导入</a>
				</div>
				<div class="group">
					<a class="link collapse" id="TemplateListDelete"
						href="javascript:exportTempModel()">模板excel导出</a>
				</div>
			</div>
			<div id="pTree" class="ztree" style="overflow: auto; clear: left"></div>
		</div>
		<!-- 初始化数据，用于页面高度计算，误删！！ -->
		<div position="center">
			<div class="layui-tab layui-tab-brief template2">
				<ul class="layui-tab-title">
					<li class="layui-this">模板制作规范</li>
				</ul>
				<div class="layui-tab-content">
					<div class="layui-tab-item layui-show">
						<%--<h1><font face="verdana">制作表单模板规范:</font></h1>--%>
						<br />
						<h2>
							<font face="arial" color="red">★规范1：</font>清单项表单模板中只能是以行列式的表格！
						</h2>
						<br />
						<div class="table-a" style="text-align: center">
							<font face="arial" color="red">规范表格示例如下：</font>
							<table align="center">
								<tbody>
									<tr class="firstRow">
										<td width="120" valign="middle" style="word-break: break-all;">
											序号</td>
										<td width="120" valign="middle" style="word-break: break-all;">
											描述</td>
										<td width="120" valign="middle" style="word-break: break-all;">
											规定值(要求值)</td>
										<td width="120" valign="middle" style="word-break: break-all;">
											产品编号(实测值)</td>
										<td width="120" valign="middle" style="word-break: break-all;">
											产品编号(实测值)</td>
									</tr>
									<tr>
										<td width="120" valign="middle" style="word-break: break-all;">
											序号</td>
										<td width="120" valign="middle" style="word-break: break-all;">
											描述</td>
										<td width="120" valign="middle" style="word-break: break-all;">
											规定值</td>
										<td width="120" valign="middle" style="word-break: break-all;">
										</td>
										<td width="120" valign="middle" style="word-break: break-all;">
										</td>
									</tr>
									<tr>
										<td width="120" valign="middle" style="word-break: break-all;">
											1</td>
										<td width="120" valign="middle" style="word-break: break-all;">
											圆环外径（mm）</td>
										<td width="120" valign="middle" style="word-break: break-all;">
											755~760</td>
										<td width="120" valign="middle" style="word-break: break-all;"></td>
										<td width="120" valign="middle"></td>
									</tr>
									<tr>
										<td width="120" valign="middle" style="word-break: break-all;">
											2</td>
										<td width="120" valign="middle" style="word-break: break-all;">
											圆环质量（kg）</td>
										<td width="120" valign="middle" style="word-break: break-all;">
											20.0~20.5</td>
										<td width="120" valign="middle" style="word-break: break-all;"></td>
										<td width="120" valign="middle"></td>
									</tr>
								</tbody>
							</table>
						</div>
						<p>
							<br />
						</p>
						<br />
						<h2>
							<font face="arial" color="red">★规范2：</font>清单项表单模板中不允许出现单元格合并的情况！
						</h2>
						<br />
						<h2>
							<font face="arial" color="red">★规范3：</font>清单项表单模板中不允许出现图片！（数学公式请使用普通字符代替）
						</h2>
						<br />
						<h2>
							<font face="arial" color="red">★规范4：</font>目前支持判读符号有≥、＞、≤、＜、最小值~最大值、名义值(上偏差,下偏差)、名义值[上偏差,下偏差]、名义值±公差.
						</h2>
						<br />
						<h2>
							<font face="arial" color="red">★规范5：</font>判读支持不定性的汉字有:不满足、不符合、不能、不正常、不完整、有缺陷。
						</h2>
						<br />
						<h2>模板制作方式:</h2>
						<br />
						<h3>&nbsp;&nbsp;&nbsp;●&nbsp;方式1&nbsp;：&nbsp;点击新建模板，通过百度编辑器制作模板。</h3>
						<br />
						<h3>&nbsp;&nbsp;&nbsp;●&nbsp;方式2&nbsp;：&nbsp;在模板导入界面下载模板，使用excel进行编辑，编辑结束后通过excel模板导入将模板导入进来。</h3>
						<br />
						<h3>&nbsp;&nbsp;&nbsp;●&nbsp;方式3&nbsp;：&nbsp;点击复制模板，可以复制型号节点上的通用模板。</h3>
						<!-- <h3>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;√&nbsp;&nbsp;回答&nbsp;：&nbsp;<font
								face="arial" color="red">不需重新创建。</font>在选中模板树的根目录或者模板分类的节点上，右击->"复制模板"即可！
						</h3> -->
						<br />
						<%--<h3>●问题2:当有些模板不符合标准模板的情况，如何进行改造？</h3>--%>
						<%--<br/>--%>
						<%--<h3>回答:<font face="arial" color="red">不需重新创建。</font>在选中模板树的根目录或者模板分类的节点上，右击->"复制模板"即可！</h3>--%>
						<%--<img src="${ctx}/help/Tem-pre.jpg" align="top"/>--%>
						<%--<img src="${ctx}/WEB-INF/view/dp/image/Tem-pre.jpg" align="top"/>--%>

					</div>
				</div>
			</div>
		</div>
	</div>


	<div class="templates" style="display: none;">
		<div class="layui-tab layui-tab-brief template1">
			<ul class="layui-tab-title">
				<li class="layui-this">属性</li>
				<li>检查表</li>
				<%--20201116应客户要求,隐藏检查项--%>
				<%--<li class="item3">检查项</li>--%>
				<li class="item4">检查条件</li>
				<li class="item5">签署</li>
			</ul>
			<div class="layui-tab-content">
				<div class="layui-tab-item layui-show">
					<iframe id="attrFrame" src="" frameborder="no" width="100%"
						height="100%"></iframe>
				</div>
				<div class="layui-tab-item">
					<iframe id="tableFrame" src="" frameborder="no" width="100%"
						height="100%"></iframe>
				</div>
				<%--<div class="layui-tab-item">
					<iframe id="itemFrame" src="" frameborder="no" width="100%"
						height="100%"></iframe>
				</div>--%>
				<div class="layui-tab-item">
					<iframe id="conditionFrame" src="" frameborder="no" width="100%"
						height="100%"></iframe>
				</div>
				<div class="layui-tab-item">
					<iframe id="signFrame" src="" frameborder="no" width="100%"
						height="100%"></iframe>
				</div>
			</div>
		</div>

		<div class="layui-tab layui-tab-brief template2">
			<ul class="layui-tab-title">
				<li class="layui-this">文件夹信息</li>
			</ul>
			<div class="layui-tab-content">
				<div class="layui-tab-item layui-show">
					<iframe id="folderFrame" src="" frameborder="no" width="100%"
						height="100%"></iframe>
				</div>
			</div>
		</div>

		<div class="layui-tab layui-tab-brief template3">
			<ul class="layui-tab-title">

			</ul>
			<div class="layui-tab-content">
				<div class="layui-tab-item layui-show">
					<iframe id="rootFrame" src="" frameborder="no" width="100%"
						height="100%"></iframe>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
