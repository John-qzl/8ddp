<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/commons/include/html_doctype.html" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>（型号）通用模板管理</title>
    <%@include file="/commons/include/form.jsp" %>
    <f:gtype name="CAT_FORM" alias="CAT_FORM"></f:gtype>
    <%-- <f:link href="tree/zTreeStyle.css"></f:link> --%>
    <link rel="stylesheet" href="${ctx}/jslib/tree/zTreeStyle.css" type="text/css">
    <f:resources name="ROOT_PID" alias="ROOT_PID"></f:resources>
    <script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
    <script type="text/javascript" src="${ctx}/js/project/module/template/CommonTemplateTree.js"></script>
    <script type="text/javascript" src="${ctx}/js/project/module/template/CommonTemplateMenu.js"></script>
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
        .l-layout-left .l-layout-header {
        	/* border-left: 1px solid #d9d9d9; */
        	border-right: 1px solid #d9d9d9;
        	border-bottom: 0;
        	color: #333;
    		background: #fff;
    		font-weight: bold;
		}
		.l-layout-left .l-layout-header-toggle:before{
			color: #747887;
			font-weight: normal;
		}
        a.link {
    		color: #666;
		}
		.tree-toolbar{
			padding: 0;
			border-bottom: 1px solid #d9d9d9;
			background: #f5f5f5;
		}
		.group {
		    margin: 0 4px;
		    height: 32px;
		    line-height: 32px;
		}
		.tree-toolbar a{
			float: none;
		}
		a.link{
			color: #666!important;
		}
		a.link:hover {
    		color: #347EFE!important;
		}
		a.link.reload:before,a.link.expand::before,a.link.collapse:before,a.link.import:before,a.link.copy:before{
			margin-right: 5px;
			color: #347EFE!important;
		}
    </style>
    <script type="text/javascript">
        //表单模板页面
        var catKey = "${CAT_FORM}";
        var curMenu = null;
        var commonTemplateMenu = new CommonTemplateMenu();
        var mappingUrl = "/template/manage/";
        // 型号id，名称
        var id = "${moduleId}";
        var nodeName = "${moduleName}";
        var ofPart=$.getParameter("ofPart");
        var commonTemplateTree = new CommonTemplateTree(catKey, "commonTemplateTree", {
            onClick: commonTemplateTreeOnClick,
            onRightClick: commonTemplateTreeOnRightClick
        }, mappingUrl, id, nodeName);
        $(function () {
            //ligerui Tab
            layui.use('element', function () {
                var element = layui.element;
            })
            //布局
            loadLayout();
            //加载树
            commonTemplateTree.loadGlobalTree();
            //加载菜单
            $(document).click(hiddenMenu);
        });

        // 结构树布局
        function loadLayout() {
            $("#defLayout").ligerLayout({
                leftWidth: 255,// 结构树宽度
                onHeightChanged: heightChanged,
                allowLeftResize: false
            });
            //取得layout的高度
            var height = $(".l-layout-center").height() - 35;
            var _height = $('.tree-toolbar').height()
            $("#commonTemplateTree").height(height - _height);
        }

        //布局大小改变的时候通知tab，面板改变大小
        function heightChanged(options) {
            $("#commonTemplateTree").height(options.middleHeight - 90);
        }
        
        //onceClick的回调函数
        function urlAttr(obj, url) {
            $(obj).attr('src', url);
        }

        //用来加载tab对应的iframe
        function onceClick(obj, urlAttr, urlObj, url) {
            var bol = true;
            $(obj).click(function () {
                if (bol) {
                    urlAttr(urlObj, url);
                }
                bol = false;
            })
        }
		
        function checkCurUserRole() {
            var flag = false;
            $.ajax({
                async: false,
                dataType: 'json',
                cache: false,
                type: 'post',
                data: {
                    fcId: id
                },
                url: "${ctx}/project/tree/stree/getNodeCharger.do",
                success: function (data) {
                    flag = data;
                },
                error: function () {
                }
            });

            return flag;
        }
		
        //更新搜索到的节点颜色
        function updateNodes(highlight, nodes) {
            commonTemplateTree.loadGlobalTree();
            for (var i = 0; i < nodes.length; i++) {
                nodes[i].highlight = highlight;
                commonTemplateTree.glTypeTree.updateNode(nodes[i]);
            }
        }

        //刷新树
        function reFresh() {
            commonTemplateTree.loadGlobalTree();
        }
        function exportTemp(){
            	 $.ajax({
                     async: 'false',
                     dataType: 'json',
                     url: "${ctx}/model/user/role/isManager.do",
                     success: function (data) {
                    	 if(data.useRole=="1"){
                    		 var node = commonTemplateTree.currentNode;

                             if (node != null) {
                                  if (node.type == "form") {
                                	   var url = "${ctx}/dp/form/exportTempExcel.do?tempId="+node.id;
                                       window.location.href = url; 
                                  	   return;
                                 } 
                             }
                             $.ligerDialog.error("请选择模板进行操作", "提示");
                    	 }
                    	 else{
                    		 $.ligerDialog.warn("您没有权限！");
                    	 }
                    	  
                     },
                     error: function () {
                         alert("");
                     }
                 })
        }
        // r1.新建文件夹
        function createFolder() {
        	 $.ajax({
                 async: 'false',
                 dataType: 'json',
                 url: "${ctx}/model/user/role/isManager.do",
                 success: function (data) {
                	 if(data.useRole=="1"){
                		 var node = commonTemplateTree.currentNode;

                         if (node != null) {
                             if (node.type == "folder" || node.type == "root") {
                                 DialogUtil.open({
                                     url: "${ctx}/oa/form/dataTemplate/editData.do?__displayId__=10000021480075&moduleId=" + id + "&folderId=" + node.id + "&handleType=add",
                                     width: 800,
                                     height: 700,
                                     sucCall: function (rtn) {
                                         reFresh();
                                     }
                                 });
                                 return;
                             }
                         }
                         $.ligerDialog.error("请选择文件夹进行操作", "提示");
                	 }
                	 else{
                		 $.ligerDialog.warn("您没有权限！");
                	 }
                	  
                 },
                 error: function () {
                     alert("");
                 }
             })
            
        }

        // r2.编辑文件夹
        function editFolder() {
            var node = commonTemplateTree.currentNode;
            if (node != null) {
                if (node.type == "folder" && node.parentId != -1) {
                    DialogUtil.open({
                        url: "${ctx}/oa/form/dataTemplate/editData.do?__displayId__=10000021480075&__pk__=" + node.id + "&moduleId=" + id + "&handleType=edit",
                        width: 800,
                        height: 700,
                        sucCall: function (rtn) {
                            reFresh();
                        }
                    });
                    return;
                }
            }
            $.ligerDialog.error("请选择需要编辑的文件夹", "提示");
        }
		
        // r3.删除文件夹
        function deleteFolder() {
            var node = commonTemplateTree.currentNode;
            if (node != null) {
                if (node.parentId != -1) {
                    if (node.type == "folder") {
                        var len = node.children;
                        if (len == undefined) {
                            $.ligerDialog.confirm('是否删除该文件夹', function (rtn) {
                                if (rtn) {
                                    url = "${ctx}/dp/form/dpDataTemplate/deleteData.do?__displayId__=10000021480075&__pk__=" + node.id;
                                    $.post(url, function (data) {
                                        if (data.result == 1) {
                                            $.ligerDialog.dpTip({
                                                content: data.message,
                                                title: '提示'
                                            });
                                        } else {
                                            $.ligerDialog.error(data.message, "提示");
                                        }
                                        reFresh();
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

        // r4.新增模板
        function createModel() {
        	 $.ajax({
                 async: 'false',
                 dataType: 'json',
                 url: "${ctx}/model/user/role/isManager.do",
                 success: function (data) {
                	 if(data.useRole=="1"){
                		  var node = commonTemplateTree.currentNode;
                          if (node != null) {
                              if (node.type == "folder" || node.type == "root") {
                                  DialogUtil.open({
                                      url: "${ctx}/template/manage/templateInfor.do?moduleId=" + id + "&folderId=" + node.id,
                                      width: 1200,
                                      height: 700,
                                      title: "新增型号通用模板",
                                      sucCall: function (rtn) {
                                          reFresh();
                                      }
                                  });
                                  return;
                              }
                          }
                          $.ligerDialog.error("添加模板前请选择文件夹", "提示");
                	 }
                	 else{
                		 $.ligerDialog.warn("您没有权限！");
                	 }
                	  
                 },
                 error: function () {
                     alert("");
                 }
             })
          
        }
        
      	// r5.删除模板
        //只有所级管理员才可以删
        function deleteModel() {
            var node = commonTemplateTree.currentNode;
            var id = node.id;
            var isCreatePeople = "0";
            if (node != null) {
                if (node.type != "folder" && node.type != "root") {
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/dp/form/deleteFormTemplateOnlyAdministrator.do",
                        data: {ID: id},
                        async: false,
                        success: function (result) {
                            isCreatePeople = result;
                        }
                    });
                    if (isCreatePeople == "1") {
                        $.ligerDialog.confirm('是否删除该模板', function (rtn) {
                            if (rtn) {
                                url = "${ctx}/dp/form/deleteFormTemplate.do?id=" + node.id;
                                $.post(url, function (data) {
                                    if (data.success == "true") {
                                    	 $.ligerDialog.success("删除成功！","",function(){
                                           window.location.reload(true);
                                           
                   						});
                                       
                                    } else {
                                        $.ligerDialog.error(data.msg, "删除失败");
                                    }

                                });
                            }
                        });
                    } else {
                        $.ligerDialog.warn("只有所级管理员才可以删除通用表单模版");
                    }

                    return;
                }
            }
            $.ligerDialog.error("请选择需要删除的模板", "提示");
        }

        // r6.编辑表单模板
        function editModel() {
            //
            var node = commonTemplateTree.currentNode;
            var id = node.id;
            var url = "${ctx}/dp/form/existInstance.do";
            var params = {id: id};
            var isCreatePeople = "0";

            $.post(url, params, function (data) {
                if (data.success == "true") {
                	/* 
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/dp/form/checkUser.do",
                        data: {ID: id},
                        async: false,
                        success: function (result) {
                            isCreatePeople = result;
                        }
                    });
                     */
                    isCreatePeople = "1";
                    if (isCreatePeople == "1") {
                        DialogUtil.open({
                            url: "${ctx}/template/manage/commonEdit.do?id=" + id + "&fcName=" + nodeName,
                            width: 1200,
                            height: 700,
                            title: "编辑表单模板",
                            sucCall: function (rtn) {
                                window.location.reload();
                            }
                        });
                    } else {
                        $.ligerDialog.warn("只有表单模版创建人才有权限编辑该表单模版");
                    }
                } else {
                    $.ligerDialog.error(data.msg, "编辑失败");
                }
            });
        }

        //导出表单模板excel功能
        function exportModel() {
            var node = commonTemplateTree.currentNode;
            var url = "${ctx}/dp/form/exportModel.do?modelId=" + node.id;
            window.location.href = url;
        }

        //是通用模板的excel上传事件
        function uploadFile(fileId, upStatus) {
        	 $.ajax({
                 async: 'false',
                 dataType: 'json',
                 url: "${ctx}/model/user/role/isManager.do",
                 success: function (data) {
                	 if(data.useRole=="1"){
                	     var node = commonTemplateTree.currentNode;
                         if (node != null) { 
                         	//点击了节点
                             if (node.type == "folder" || node.type == "root") {
                             	//如果点击了文件夹
                             }else{

                             	//点击的不是文件夹
                             	node=commonTemplateTree.currentNode.getParentNode();
                             }
                         }else{
                             //没有点击节点
                             var node = commonTemplateTree.rootNode;
                         }
                         if(ofPart=="CPYS"){
                             var uploadJsp="${ctx}/editor/tempFileUpload.jsp?pid=" + id + "&fcid=" +node.id+ "&type=" + "excel&extype=module";
                         }else if (ofPart=="BCSY"){
                             var uploadJsp="${ctx}/editor/tempFileUploadForBCSY.jsp?pid=" + id + "&fcid=" +node.id+ "&type=" + "excel&extype=module";
                         }else if (ofPart=="WQSJ"){
                             var uploadJsp="${ctx}/editor/tempFileUploadForWQSJ.jsp?pid=" + id + "&fcid=" +node.id+ "&type=" + "excel&extype=module";
                         }
                         debugger;
                         DialogUtil.open({
                             url:uploadJsp,
                             height: 300,
                             width: 500,
                             title: "导入模板Excel",
                             isResize: true,
                             sucCall: function (rtn) {
                                 reFresh();
                             }
                         });
                         return;
                	 }
                	 else{
                		 $.ligerDialog.warn("您没有权限！");
                	 }
                	  
                 },
                 error: function () {
                     alert("");
                 }
             })
       
        }
		
        function uploadFile_Word(fileId, upStatus) {
            var node = commonTemplateTree.currentNode;
            if (node != null) {
                if (node.type == "folder" || node.type == "root") {
                    DialogUtil.open({
                        url: "${ctx}/editor/tempFileUpload.jsp?fcid=" + id + "&pid=" + node.id + "&type=" + "word",
                        height: 300,
                        width: 500,
                        title: "导入模板Word",
                        isResize: true,
                        sucCall: function (rtn) {
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
            var node = commonTemplateTree.currentNode;
            if (node != null) {
                if (node.type != "folder" && node.type != "root") {
                    $.ligerDialog.open({
                        url: "${ctx}/dp/form/selectFolder.do?fcId=" + id + "&fcName=" + nodeName + "&nodeid=" + node.id,
                        width: 300,
                        height: 500,
                        title: "选择目标文件夹",
                        sucCall: function (rtn) {
                            reFresh();
                        }
                    });

                    return;
                }
            }
            $.ligerDialog.error("请选择需要移动的模板", "提示");
        }

        //复制模板
        function copyModel() {
            var node = commonTemplateTree.currentNode;

            if (node != null) {
                if (node.type == "folder" || node.type == "root") {
                    DialogUtil.open({
                        url: "${ctx}/dp/form/selectMutilModel.do?fcId=" + id + "&fcName=" + nodeName + "&nodeid=" + node.id,
                        width: 300,
                        height: 500,
                        title: "表单模板选择器",
                        sucCall: function (rtn) {
                            reFresh();
                        }
                    });
                    return;
                }
            }
            $.ligerDialog.error("复制模板前请先选择文件夹", "提示");
        }

        function openMyLinkDialog(CustomConf) {
            var defaultConf = {
                height: 800,
                width: 1000,
                title: "表单填写",
                params: {}
            }
            if (!CustomConf.url) {
                alert("url不能为空！");
                return;
            }
            var conf = $.extend({}, defaultConf, CustomConf);
            DialogUtil.open({
                params: conf.params,
                height: conf.height,
                width: conf.width,
                url: conf.url,
                showMax: false,
                showToggle: false,
                title: conf.title,
                showMin: false,
                sucCall: function (rtn) {
                    if (!(rtn == undefined || rtn == null || rtn == '')) {
                    	
                    }
                }
            });
        }

        //模板批量删除
        //20201103zmz做权限控制时发现这个按钮被隐藏了, 如果需要放出来的话,需要加上删除权限 (只有所管理员可以删
        function TemplateListDelete() {
            reFresh();
            var node = commonTemplateTree.currentNode;

            var url = "${ctx}" + "/oa/form/dataTemplate/preview.do?__displayId__=10000019200641&__dbomSql__=F_PROJECT_ID=" + id;
            openMyLinkDialog({url: url.getNewUrl(), title: "批量删除", height: 600, width: 900});

        }
        

     // 型号--通用模板结构树左击事件
     function commonTemplateTreeOnClick(event, treeId, treeNode) {

         var content = $('.layui-tab-content');
         var _height = $('.l-layout-content').height()
             - content.prev('.layui-tab-title').height() - 18;
         $('.layui-tab-content').height(_height)


         var url = null;
         $('div[position="center"]>div').remove();
         //根节点
         if (treeNode.id == "1") {
             url = "";
             $('div[position="center"]').append($('.templates .template3').clone(true));
             $("#rootFrame").attr("src", url);
         } else {
             //文件夹节点
             if (treeNode.type == "folder") {
                 url = "${ctx}" + treeNode.tempUrl;
                 $('div[position="center"]').append($('.templates .template2').clone(true));
                 $("#folderFrame").attr("src", url);
             }
             //表单模板
             else {
                 url = "${ctx}" + treeNode.tempUrl;
                 $('div[position="center"]').append($('.templates .template1').clone(true));
                 //属性页
                 $("#attrFrame").attr("src", url);
                 //表单页
                 $("#tableFrame").attr("src", "${ctx}" + "/dp/form/showTable.do?id=" + treeNode.id);
                 //检查项页
                 var itemUrl = "${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000021701132&Q_table_temp_ID_S=" + treeNode.id;
                 //检查条件页
                 var conditionUrl = "${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000000840054&Q_table_temp_ID_S=" + treeNode.id;
                 //签署项页
                 var signUrl = "${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000000720043&Q_table_temp_ID_S=" + treeNode.id;
                 //调用onceClick函数
                 onceClick('.item3', urlAttr, '#itemFrame', itemUrl);
                 onceClick('.item4', urlAttr, '#conditionFrame', conditionUrl);
                 onceClick('.item5', urlAttr, '#signFrame', signUrl);
             }

         }
     };

     // 型号--通用模板结构事件
     function commonTemplateTreeOnRightClick(event, treeId, treeNode) {
         hiddenMenu();
         $.ajax({
             async: 'false',
             dataType: 'json',
             url: "${ctx}/model/user/role/isManager.do",
             success: function (data) {
            	 if(data.useRole=="1"){
            		 if (treeNode) {
                         var height = $(".l-layout-center").height() - 28;
                         commonTemplateTree.currentNode = treeNode;
                         commonTemplateTree.glTypeTree.selectNode(treeNode);
                         /* var flag = checkCurUserRole(); */
                         var flag = true;
                         if (flag) {
                             ishandler(treeNode);
                             if (event.pageY > height - 168) {
                                 curMenu.show({top: event.pageY - 168, left: event.pageX});
                             } else {
                                 curMenu.show({top: event.pageY, left: event.pageX});
                             }

                         }
                     }
            	 }
            	 else{
            		 $.ligerDialog.warn("您没有权限！");
            	 }
            	  
             },
             error: function () {
                 alert("");
             }
         })
      
     };

     // 隐藏右键菜单
     function hiddenMenu() {
         if (curMenu) {
             curMenu.hide();
         }
     }

     // （型号）通用模板结构树右键菜单事件处理
     function ishandler(treeNode) {
         curMenu = commonTemplateMenu.getMenu(treeNode, handler);
     }

     function handler(item) {
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
    </script>
</head>
<body>
<div id="defLayout">
    <div position="left" title="表单模板"
         style="text-align: left; border: 1px solid #d9d9d9; border-left: 0;">
        <div class="tree-toolbar" id="pToolbar">
            <div class="group"><a class="link reload" id="treeFresh" onclick="reFresh()">刷新</a></div>
            <!-- <div class="group"><a class="link expand" id="treeExpandAll"
                                  href="javascript:commonTemplateTree.treeExpandAll(true)">展开</a></div>-->
            <div class="group"><a class="link collapse" id="treeCollapseAll"
                                  href="javascript:commonTemplateTree.treeExpandAll(false)">收起</a></div> 
            <div class="group"><a class="link collapse" id="treeCollapseAll"
                                  href="javascript:createModel()">新增表单模板</a></div>
            <div class="group"><a class="link collapse" id="treeCollapseAll"
                                  href="javascript:uploadFile()">导入模板Excel</a></div>
            <div class="group"><a class="link collapse" id="treeCollapseAll"
                                  href="javascript:createFolder()">新增文件夹</a></div>
             <div class="group"><a class="link collapse" id="exportTemp"
                                  href="javascript:exportTemp()">模板excel导出</a></div>
                                  &nbsp;&nbsp;&nbsp;&nbsp;

           <!--  <div class="group"><a class="link collapse" id="ImportTemplate"
                                  href="javascript:importTemplate()">导入模板</a></div> -->
            <!--                       
            <div class="group"><a class="link collapse" id="TemplateListDelete" href="javascript:TemplateListDelete()">批量删除</a></div>
             -->
        </div>
        <div id="commonTemplateTree" class="ztree" style="overflow:auto;clear:left"></div>
    </div>
    <!-- 初始化数据，用于页面高度计算，勿删！！ -->
    <div position="center">
    	<!-- 去除说明，在产品类别层级新增模板时添加 -->
    </div>
</div>


<div class="templates" style="display:none;">
    <div class="layui-tab layui-tab-brief template1">
        <ul class="layui-tab-title">
        	<!-- 去除属性页面
            <li class="layui-this">属性</li>
             -->
            <li>检查表</li>
           <%-- <li class="item3">检查项</li>--%>
            <li class="item4">检查条件</li>
            <li class="item5">签署</li>
        </ul>
        <div class="layui-tab-content">
        	<!-- 
            <div class="layui-tab-item layui-show">
                <iframe id="attrFrame" src="" frameborder="no" width="100%" height="100%"></iframe>
            </div>
             -->
            <div class="layui-tab-item layui-show">
                <iframe id="tableFrame" src="" frameborder="no" width="100%" height="100%"></iframe>
            </div>
            <%--<div class="layui-tab-item">
                <iframe id="itemFrame" src="" frameborder="no" width="100%" height="100%"></iframe>
            </div>--%>
            <div class="layui-tab-item">
                <iframe id="conditionFrame" src="" frameborder="no" width="100%" height="100%"></iframe>
            </div>
            <div class="layui-tab-item">
                <iframe id="signFrame" src="" frameborder="no" width="100%" height="100%"></iframe>
            </div>
        </div>
    </div>

    <div class="layui-tab layui-tab-brief template2">
        <ul class="layui-tab-title">
            <li class="layui-this">文件夹信息</li>
        </ul>
        <div class="layui-tab-content">
            <div class="layui-tab-item layui-show">
                <iframe id="folderFrame" src="" frameborder="no" width="100%" height="100%"></iframe>
            </div>
        </div>
    </div>

    <div class="layui-tab layui-tab-brief template3">
        <ul class="layui-tab-title">

        </ul>
        <div class="layui-tab-content">
            <div class="layui-tab-item layui-show">
                <iframe id="rootFrame" src="" frameborder="no" width="100%" height="100%"></iframe>
            </div>
        </div>
    </div>
</div>
</body>
</html>
