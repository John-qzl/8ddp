<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/commons/include/html_doctype.html" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

    <title>型号管理结构树</title>
    <%@include file="/commons/include/form.jsp" %>
    <f:gtype name="CAT_FORM" alias="CAT_FORM"></f:gtype>
    <f:link href="tree/zTreeStyle.css"></f:link>
    <f:resources name="ROOT_PID" alias="ROOT_PID"></f:resources>
    <link rel="stylesheet" href="${ctx}/layui/css/layui.css">
    <link rel="stylesheet" href="${ctx}/jslib/tree/zTreeStyle.css" type="text/css">
    <link rel="stylesheet" href="${ctx}/styles/default/css/iconfont.css" type="text/css">
    <link rel="stylesheet" href="${ctx}/styles/default/css/iconImg.css" type="text/css">
    <script type="text/javascript" src="${ctx}/layui/layui.js"></script>
    <script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
    <%--<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.core.js"></script>--%>
    <script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.excheck.js"></script>
    <script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.exedit.js"></script>
    <script type="text/javascript" src="${ctx}/js/dataPackage/tree/DirectoryTree.js"></script>
    <script type="text/javascript" src="${ctx}/js/dataPackage/tree/FormDefMenu.js"></script>
    <script type="text/javascript">
        //型号类别
        var flag = "<%=request.getParameter("flag")%>";
        var flagType = flag;
        if (flag == "carry") {
            flag = "运载";
        } else if (flag == "space") {
            flag = "空间";
        } else {
            flag = "结构机构";
        }

        var catKey = "${CAT_FORM}";
        var curMenu = null;
        var formDefMenu = new FormDefMenu();
        var mappingUrl = "/model/manage/tree/";
        var sTree = new GlobalType(catKey, "sTree", {
            onClick: moduleTreeOnClick,
            onRightClick: moduleTreeOnRightClick
        }, mappingUrl, flag);

        $(function () {
            //ligerui Tab
            layui.use('element', function () {
                var element = layui.element;
                setTimeout(function() {
                	var content = $('.l-layout-content .layui-tab-content');
                    var _height = content.closest('.layui-tab').height()
                            - content.prev('.layui-tab-title').height() - 18;
                    $('.layui-tab-content').height(_height);
                  
                }, 100)
            })
            //布局
            loadLayout();
            //加载树
            sTree.loadGlobalTree();
            //加载列表
            var url = "${ctx}" + "/oa/form/dataTemplate/preview.do?__displayId__=10000021170075";
            url = encodeURI(url);
            $("#listFrame").attr("src", url);

            //加载菜单
            $(document).click(hiddenMenu);
        });

        //布局
        function loadLayout() {
            $("#defLayout").ligerLayout({
                leftWidth: 200,
                onHeightChanged: heightChanged,
                allowLeftResize: true
            });
            //取得layout的高度
            /* var height = $(".l-layout-center").height() - 28;
            var _height = $('.tree-toolbar').height();
            $("#sTree").height(height - _height); */
        }
        //布局大小改变的时候通知tab，面板改变大小
        function heightChanged(options) {
            $("#sTree").height(options.middleHeight - 90);
        }
		
         // 型号管理结构树左击事件
        function moduleTreeOnClick(event, treeId, treeNode) {
            var typeName = encodeURI(treeNode.typeName);
            var url = null;
            $('div[position="center"]>div').remove();

            switch (treeNode.level) {
                case 0:
                	// 根节点
                    $('div[position="center"]').append($('.template .template1').clone(true));
                	
                 	// 通用模板tab
                    $('.template1 .tab-commonTemplate').click(function () {
                        var formCommonTemplateUrl = "${ctx}/template/manage/common.do?moduleId=" + treeNode.typeId + "&moduleName=" + treeNode.typeName;//型号
                        $("#tab-commonTemplate-frame").attr("src", encodeURI(encodeURI(formCommonTemplateUrl)));
                    })
                    // 数据分析tab
                    $('.template1 .tab-dea').click(function () {
                        //设定表单模块的IFrame的请求地址
                        var formDEAUrl = "${ctx}/dataPackage/dea/getQueryInfo.do?xhId=" + treeNode.typeId + "&type=" + flagType;
                        $(".template1 #tab-dea-frame").attr("src", encodeURI(encodeURI(formDEAUrl)));
                    })
                    // 工作统计tab
                    $('.template1 .tab-analysis').click(function () {
                        //设定表单模块的IFrame的请求地址
                        var formComAnalysisUrl = "${ctx}/dataPackage/dp/dpCom/analysis.do?xhId=" + treeNode.typeId + "&type=" + flagType;
                        $("#tab-analysis-frame").attr("src", encodeURI(encodeURI(formComAnalysisUrl)));
                    })
                    break;
                case 1:
                    $('div[position="center"]').append($('.template .template2').clone(true));
                  	// 型号基本属性tab
                    /* $('.template2 .layui-this').click(function () {
                        //设定表单模块的IFrame的请求地址
                        var formDPUrl = "${ctx}/dataPackage/tree/dataPackage/show.do?xhId=" + treeNode.typeId + "&xhName=" + treeNode.typeName;//型号
                        $("#tab-dp-frame").attr("src", encodeURI(encodeURI(formDPUrl)));
                    }) */
                    // 数据查看tab（产品维度+型号维度）
                    $('.template2 .tab-dp').click(function () {
                        //设定表单模块的IFrame的请求地址
                        var formDPUrl = "${ctx}/dataPackage/tree/dataPackage/show.do?xhId=" + treeNode.typeId + "&xhName=" + treeNode.typeName;//型号
                        $("#tab-dp-frame").attr("src", encodeURI(encodeURI(formDPUrl)));
                    })
                    /* 
                    // 通用模板tab
                    $('.template2 .tab-commonTemplate').click(function () {
                        var formCommonTemplateUrl = "${ctx}/template/manage/common.do?moduleId=" + treeNode.typeId + "&moduleName=" + treeNode.typeName;//型号
                        $("#tab-commonTemplate-frame").attr("src", encodeURI(encodeURI(formCommonTemplateUrl)));
                    })
                    // 数据分析tab
                    $('.template2 .tab-dea').click(function () {
                        //设定表单模块的IFrame的请求地址
                        var formDEAUrl = "${ctx}/dataPackage/dea/getQueryInfo.do?xhId=" + treeNode.typeId + "&type=" + flagType;
                        $(".template2 #tab-dea-frame").attr("src", encodeURI(encodeURI(formDEAUrl)));
                    })
                    // 工作统计tab
                    $('.template2 .tab-analysis').click(function () {
                        //设定表单模块的IFrame的请求地址
                        var formComAnalysisUrl = "${ctx}/dataPackage/dp/dpCom/analysis.do?xhId=" + treeNode.typeId + "&type=" + flagType;
                        $("#tab-analysis-frame").attr("src", encodeURI(encodeURI(formComAnalysisUrl)));
                    })
                     */
                    break;
            }

            if (treeNode.level == 0) {
            	// （静态）根节点--全部型号
                url = "${ctx}" + treeNode.tempUrl;
                url = encodeURI(url);
                $("#listFrame").attr("src", url);
            } else {
                // 型号节点--具体型号
            	$('.template2 .tab-dp').click();
            }
            
        }
        
		
        // 型号管理结构树右击事件
        function moduleTreeOnRightClick(event, treeId, treeNode) {
            hiddenMenu();
            if (treeNode) {
            	var height = $(".l-layout-center").height() - 28;
                sTree.currentNode = treeNode;
                sTree.glTypeTree.selectNode(treeNode);
                //设置权限,目前只允许项目办修改型号和发次
                var roleNames = '';
                $.ajax({
                    async: 'false',
                    dataType: 'json',
                    data: {},
                    /* url: "${ctx}/project/tree/stree/getCurUserInfo.do", */
                    url: "${ctx}/model/manage/tree/getCurUserInfo.do",
                    success: function (data) {
                        var ISysUser = data;
                        roleNames = ISysUser.roles;
                       /*  if (roleNames.indexOf("xmb") >= 0) { */
                            ishandler(treeNode,roleNames);
                            if(event.pageY>height-24){
                            	curMenu.show({top: event.pageY-24, left: event.pageX});
                            }else{
                            	curMenu.show({top: event.pageY, left: event.pageX});
                            }
                        /* } */
                    },
                    error: function () {
                        alert("");
                    }
                })
            }
        };

        //隐藏右键菜单
        function hiddenMenu() {
            if (curMenu) {
                curMenu.hide();
            }
        }

        //右击事件处理
        function ishandler(treeNode,roleNames) {
            curMenu = formDefMenu.getMenu(treeNode, handler,roleNames);
        }
        function handler(item) {
            hiddenMenu();
            var txt = item.text;
            switch (txt) {
                case "新增":
                	if(item.roleNames.indexOf("sjgly")>=0){
                		sTree.editNode(true);
                	}
                	else{
                		$.ligerDialog.warn("您没有权限！");
                	}
                    break;
                case "编辑":
                	if(item.roleNames.indexOf("sjgly")>=0||item.roleNames.indexOf("xhgly")>=0){
                		 sTree.editNode(false);
                	}
                	else{
                		$.ligerDialog.warn("您没有权限！");
                	}
                    break;
                case "删除":
                	if(item.roleNames.indexOf("sjgly")>=0||item.roleNames.indexOf("xhgly")>=0){
                		sTree.delNode();
               	}
               	else{
               		$.ligerDialog.warn("您没有权限！");
               	}
                	break;
            }
        }

        //更新搜索到的节点颜色
        function updateNodes(highlight, nodes) {
            sTree.loadGlobalTree();

            for (var i = 0; i < nodes.length; i++) {
                nodes[i].highlight = highlight;
                sTree.glTypeTree.updateNode(nodes[i]);
            }
        }

        //刷新树
        function reFresh() {
            sTree.loadGlobalTree();
        }
        //服务端导入
        function serverImport() {
            var node = getSelectNode();
            if (!node || !node.typeId || node.parentId == 0) {
                $.ligerDialog.warn("请选择导入的发次节点！");
                return;
            }
            var fcId = node.typeId;
            var xhId = node.parentId;
            var url = __ctx + '/io/packageImport.do';
            DialogUtil.open({
                height: 500,
                width: 800,
                url: url,
                title: "数据包导入",
                params: {
                    isClient: false,
                    fcId: fcId,
                    xhId: xhId
                },
                sucCall: function (rtn) {
                    reFresh()
                }
            });
        }
        /**
         * @Author  shenguoliang
         * @Description: 服务器导出(重新开发)
         * @Params
         * @Date 2018/6/8 8:38
         * @Return
         */
        function serverExport() {
            var node = getSelectNode();
            if (!node || !node.typeId) {
                $.ligerDialog.warn("请选择导出的发次节点！");
                return;
            }
            var fcId = node.typeId;
            DialogUtil.open({
                height: 500,
                width: 300,
                url: __ctx + '/dataPackage/tree/dataPackageDialog2.do',
                title: "数据包节点导出选择器",
                params: {projectId: fcId, isSingle: false},
                sucCall: function (rtn) {
                    var ids = rtn.ids;
                    if (!ids || ids == "") {
                        $.ligerDialog.warn("请选择要导出的数据包节点！")
                        $.ligerDialog.closeWaitting();
                        return;
                    }
                    $.ligerDialog.waitting("导出中...","提示信息");

                    var url = __ctx + "/dataPackage/tree/ptree/serverExportPackages.do?";
                    $.get(url, {fcId: fcId, nodeIds: ids}, function (responseText) {
                        $.ligerDialog.closeWaitting();
                        var obj = new com.ibms.form.ResultMessage(responseText);
                        if (obj.isSuccess()) {
                            var path = obj.data.filePath;
                            if (path != null || path != "") {
                                path = encodeURI(path);
                            } else {
                                $.ligerDialog.error("返回的下载地址无效！", "提示信息");
                                return;
                            }
                            var url = __ctx + "/oa/system/sysFile/downLoadTempFile.do"
                            url += "?tempFilePath=" + path;
                            window.location.href = url;//下载文件
                        } else {
                            $.ligerDialog.err("提示信息", "", obj.getMessage());
                        }
                    });
                }
            });
            $.ligerDialog.closeWaitting();

        }

        /**
         *节点上移
         */
        function setUp() {
            var node = getSelectNode();
            var fcId = node.typeId;  //找出发次id
            var prId = node.parentId; //找出父节点id
            $.ajax({
                data: {
                    fcId: fcId,
                    prId: prId
                },
                /* url: "${ctx}/project/tree/stree/setUp.do", */
                url: "${ctx}/model/manage/tree/setUp.do",
                success: function () {
                	sTree.reFreshLoadGlobalTree(fcId);
                }
            })
        }

        /**
         *节点下移
         */
        function setDown() {
            var node = getSelectNode();
            var fcId = node.typeId;  //找出发次id
            var prId = node.parentId; //找出父节点id

            $.ajax({
                data: {
                    fcId: fcId,
                    prId: prId
                },
                /* url: "${ctx}/project/tree/stree/setDown.do", */
                url: "${ctx}/model/manage/tree/setDown.do",
                success: function () {
                	sTree.reFreshLoadGlobalTree(fcId);
                }
            })
        }

        /**
         *旧库导入新库FZH
         */
        function packageImportFromOldSystem() {
            var node = getSelectNode();
            if (!node || !node.typeId) {
                $.ligerDialog.warn("请选择导入的发次节点！");
                return;
            }
            var fcId = node.typeId;
            var url = __ctx + '/io/packageImportFromOldSystem.do';
            DialogUtil.open({
                height: 500,
                width: 800,
                url: url,
                title: "一期数据包系统迁移导入---现场实施技术人员专用",
                params: {fcId: fcId},
                sucCall: function (rtn) {

                }
            });
        }

        function getSelectNode() {
            var tree = sTree.glTypeTree;
            var nodes = tree.getSelectedNodes();
            return nodes[0];
        }
    </script>
</head>
<body>
<div id="defLayout">
    <div position="left" title="型号管理"
         style="text-align: left; border-style: solid; border-color: #EEEEEE ; background-color: #EEEEEE;">
        <div class="tree-toolbar" id="pToolbar">
            <div class="group"><a class="link reload" id="treeFresh" href="javascript:reFresh()">刷新</a></div>
            <div class="group"><a class="link expand" id="treeExpandAll" href="javascript:sTree.treeExpandAll(true)">展开</a></div>
            <div class="group"><a class="link collapse" id="treeCollapseAll" href="javascript:sTree.treeExpandAll(false)">收起</a></div>
            <!-- <div class="group"><a class="link collapse" id="serverImport" href="javascript:serverImport()">服务器导入</a></div>
            <div class="group"><a class="link collapse" id="serverExport" href="javascript:serverExport()">服务器导出</a></div>
            <div class="group"><a class="link collapse" id="treeSetUp" href="javascript:setUp()">上移</a></div>
            <div class="group"><a class="link collapse" id="treeSetDown" href="javascript:setDown()">下移</a></div> -->
        </div>
        <div id="sTree" class="ztree" style="overflow:auto;clear:left ;background-color: #EEEEEE;"></div>
    </div>
<!-- 
    <div position="center">
        <div class="layui-tab layui-tab-brief template1">
            <ul class="layui-tab-title">
                <li class="layui-this">型号列表</li>
            </ul>
            <div class="layui-tab-content">
                <div class="layui-tab-item layui-show">
                    <iframe id="listFrame" src="" frameborder="no" width="100%" height="100%"></iframe>
                </div>
            </div>
        </div>
    </div> -->
</div>

<!-- <div class="template" style="display:none;">
    <div class="layui-tab layui-tab-brief template1">
        <ul class="layui-tab-title">
            <li class="layui-this">型号列表</li>
            
            <li class="tab-commonTemplate">通用模板</li>
            <li class="tab-dea">数据分析</li>
            <li class="tab-analysis">工作统计</li>
        </ul>
        <div class="layui-tab-content">
            <div class="layui-tab-item layui-show">
                <iframe id="listFrame" src="" frameborder="no" width="100%" height="100%"></iframe>
            </div>
            
            <div class="layui-tab-item">
                <iframe id="tab-commonTemplate-frame" src="" frameborder="no" width="100%" height="100%"></iframe>
            </div>
            <div class="layui-tab-item">
                <iframe id="tab-dea-frame" src="" frameborder="no" width="100%" height="100%"></iframe>
            </div>
            <div class="layui-tab-item">
                <iframe id="tab-analysis-frame" src="" frameborder="no" width="100%" height="100%"></iframe>
            </div>
        </div>
    </div>

    <div class="layui-tab layui-tab-brief template2">
        <ul class="layui-tab-title">
        	基本属性tab隐藏
            <li class="layui-this">基本属性</li>
            
            <li class="tab-dp">数据查看</li>
            
            <li class="tab-commonTemplate">通用模板</li>
            <li class="tab-dea">数据分析</li>
            <li class="tab-analysis">工作统计</li>
            
        </ul>
        <div class="layui-tab-content">
        	
            <div class="layui-tab-item layui-show">
                <iframe id="listFrame" src="" frameborder="no" width="100%" height="100%"></iframe>
            </div>
            
            <div class="layui-tab-item">
                <iframe id="tab-dp-frame" src="" frameborder="no" width="100%" height="100%"></iframe>
            </div>
            
            <div class="layui-tab-item">
                <iframe id="tab-commonTemplate-frame" src="" frameborder="no" width="100%" height="100%"></iframe>
            </div>
            <div class="layui-tab-item">
                <iframe id="tab-dea-frame" src="" frameborder="no" width="100%" height="100%"></iframe>
            </div>
            <div class="layui-tab-item">
                <iframe id="tab-analysis-frame" src="" frameborder="no" width="100%" height="100%"></iframe>
            </div>
            
        </div>
    </div>
</div> -->
</body>
<%--修改zTree树的宽度高度滚动条显示--%>
<script type="text/javascript" src="${ctx}/js/dataPackage/tree/zTreeStyle/zTreeWidthHeightScroll.js"></script>

</html>
