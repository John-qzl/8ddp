<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/commons/include/html_doctype.html" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

    <title>数据包结构树</title>
    <%@include file="/commons/include/form.jsp" %>
    <f:gtype name="CAT_FORM" alias="CAT_FORM"></f:gtype>
    <f:link href="tree/zTreeStyle.css"></f:link>
    <f:resources name="ROOT_PID" alias="ROOT_PID"></f:resources>
    <link rel="stylesheet" href="${ctx}/jslib/tree/zTreeStyle.css" type="text/css">

    <script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>

    <script type="text/javascript" src="${ctx}/js/dataPackage/tree/PackageTree.js"></script>
    <script type="text/javascript" src="${ctx}/js/dataPackage/tree/PackageDefMenu.js"></script>
    <script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/CommonDialog.js"></script>
<%--    <style type="text/css">
        .tree-title {
            overflow: hidden;
            width: 100%;
        }
        html, body {
            padding: 0px;
            margin: 0;
            width: 100%;
            height: 100%;
            overflow: scroll;
        }
    </style>--%>
    <script type="text/javascript">
        //发次点击事件后 跳转到的数据包页面
        var catKey = "${CAT_FORM}";
        var curMenu = null;
        var packageDefMenu = new PackageDefMenu();
        var mappingUrl = "/dataPackage/tree/ptree/";
        var id = "<%=request.getParameter("id")%>";
        var pid = "<%=request.getParameter("pid")%>";
        var nodeName = "<%=request.getParameter("nodeName")%>";

        var pTree = new GlobalPackageType(catKey, "pTree", {
            onClick: onClick,
            onRightClick: zTreeOnRightClick
        }, mappingUrl, id, pid, nodeName);
        $(function () {
            //布局
            loadLayout();
            //加载树
            pTree.loadGlobalTree();
            //加载右侧数据包信息
            var url = "${ctx}" + "/oa/form/dataTemplate/detailData.do?__displayId__=10000000560353&__pk__=" + id;

            url = encodeURI(url);
            $("#dataPackageFrame").attr("src", url);

            //根据当前用户角色判断对应权限(是否包含项目办)
            checkCurUserRole("xmb");

            //加载菜单
            $(document).click(hiddenMenu);
        });

        //布局
        function loadLayout() {
            $("#defLayout").ligerLayout({
                leftWidth: 280,
                onHeightChanged: heightChanged,
                allowLeftResize: true
            });
            //取得layout的高度
            var height = $(".l-layout-center").height() - 28;
            var _height = $('.tree-toolbar').height();
            $("#pTree").height(height - _height);
        }
        //布局大小改变的时候通知tab，面板改变大小
        function heightChanged(options) {
            $("#pTree").height(options.middleHeight - 90);
        }

        /**
         * 树左击事件
         */
        function onClick(event, treeId, treeNode) {
            var url = null;

            if (treeNode.id == "0") {
                url = "${ctx}" + treeNode.tempUrl
                        + "&__pk__=" + treeNode.ProjectId;
            } else {
            	if(treeNode.Type=="普通分类节点"){
            		var parentName = treeNode.getParentNode().Name;
                    url = "${ctx}" + treeNode.tempUrl
                            + "&id=" + treeNode.id
                            + "&type=" + treeNode.Type
                            + "&name=" + treeNode.Name
                            + "&projectId=" + id
                            + "&projectName=" + nodeName
                            + "&__dbomFKName__=" + parentName;
            	}else{
            		var parentName = treeNode.getParentNode().Name;
                    url = "${ctx}" + "/dataPackage/tree/dataPackage/main2.do?"
                            + "&id=" + treeNode.id
                            + "&type=" + treeNode.Type
                            + "&name=" + treeNode.Name
                            + "&projectId=" + id
                            + "&projectName=" + nodeName
                            + "&__dbomFKName__=" + parentName;
            	}
                
            }
            url = encodeURI(url);
            $("#dataPackageFrame").attr("src", url);
        }
        ;

        /**
         * 树右击事件
         */
        function zTreeOnRightClick(event, treeId, treeNode) {
            hiddenMenu();
            if (treeNode) {
            	var height = $(".l-layout-center").height() - 28;

                pTree.currentNode = treeNode;
                pTree.glTypeTree.selectNode(treeNode);
                //设置权限,目前只允许项目办修改数据包结构树的新增修改
                var ifCheck = checkCurUserRole("xmb");
                if (ifCheck) {
                    ishandler(treeNode);
                    if(event.pageY>height-74){
                    	curMenu.show({top: event.pageY-74, left: event.pageX});
                    }else{
                    	curMenu.show({top: event.pageY, left: event.pageX});
                    }
                    
                } else {
					$.ligerDialog.warn("只有项目办才能进行新增编辑此操作!");
                    return;
                }
            }
        }
        ;
        //隐藏右键菜单
        function hiddenMenu() {
            if (curMenu) {
                curMenu.hide();
            }
        }
        //右击事件处理
        function ishandler(treeNode) {
            curMenu = packageDefMenu.getMenu(treeNode, handler);
        }

        function handler(item) {
            hiddenMenu();
            var txt = item.text;
            var type = item.Type;
            switch (txt) {
                case "新增":
                    pTree.editNode(true);
                    break;
                case "编辑":
                    pTree.editNode(false);
                    break;
                case "删除":
                    pTree.delNode();
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
        //导入模板
        function nodeImport() {
            var tree = pTree.glTypeTree;
            var node = tree.getSelectedNodes()[0];
            var type = node.Type;//普通分类节点
            if (!node) {
                $.ligerDialog.warn("请选择被导入的节点！");
                return;
            }
            if (type != "普通分类节点" && type != "root") {
                $.ligerDialog.warn("只能选择普通分类节点或根节点进行导入！");
                return;
            }
            CommonDialog("cpsjbjgsdcxxlb", function (data) {
                var name = node.Name;
                var mbmc = data.F_MBMC;
                var sxjdId = data.F_SXJDID;
                var sxfcId = data.F_SSFCID;
                var str = "确定将【" + data.F_MBMC + "】模板的信息全部导入作为【" + name + "】的子节点吗！";
                $.ligerDialog.confirm(str, '提示信息', function (rtn) {
                    if (rtn) {
                        $.ajax({
                            type: "POST",
                            url: __ctx + '/dataPackage/tree/ptree/importDataPackageTreeInfo.do',
                            data: {
                                sourceId: data.F_SXJDID,
                                sourceFcId: data.F_SSFCID,
                                targetId: node.id,
                                targetFcName: name,
                                targetFcId: node.ProjectId,
                                targetXhId: node.ProductId
                            },
                            dataType: "text",
                            async: false,
                            success: function (responseText) {
                                var obj = new com.ibms.form.ResultMessage(responseText);
                                if (obj.isSuccess()) {
                                    $.ligerDialog.success("导入成功", "", function () {
                                        reFresh()
                                    });
                                } else {
                                    $.ligerDialog.err("提示信息", "", obj.getMessage());
                                }
                            }
                        });
                    }
                });
            });

        }
        //导出模板
        function nodeExport() {
            var tree = pTree.glTypeTree;
            var node = tree.getSelectedNodes()[0];
            if (!node) {
                $.ligerDialog.warn("请选择所要导出的节点！");
                return;
            }
            var nodeInfo = {sxjdID: node.id, sxjdmc: node.Name, ssfcID: node.ProjectId};
            var url = __ctx + '/oa/form/dataTemplate/editData.do?__displayId__=10000002050455';
            openMyLinkDialog({url: url.getNewUrl(), title: "导出模板", height: 400, width: 500, params: nodeInfo});

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
                showMax: false,                             //是否显示最大化按钮
                showToggle: false,                         //窗口收缩折叠
                title: conf.title,
                showMin: false,
                sucCall: function (rtn) {
                    if (!(rtn == undefined || rtn == null || rtn == '')) {
                        //location.href=location.href.getNewUrl();
                    }
                }
            });
        }

        function getSelectNode() {
            var tree = pTree.glTypeTree;
            var nodes = tree.getSelectedNodes();
            return nodes[0];
        }

        /**
         *节点上移
         */
        function setUp() {

            var node = getSelectNode();
            var Id = node.id;
            var fcId = node.ProjectId;
            $.ajax({
                data: {
                    Id: Id,
                    fcId: fcId
                },
                url: "${ctx}/dataPackage/tree/ptree/setUp.do",
                success: function () {

                	pTree.reFreshLoadGlobalTree(Id);

                }
            })
        }

        /**
         *节点下移
         */
        function setDown() {

            var node = getSelectNode();
            var Id = node.id;
            var fcId = node.ProjectId;

            $.ajax({
                data: {
                    Id: Id,
                    fcId :fcId
                },
                url: "${ctx}/dataPackage/tree/ptree/setDown.do",
                success: function () {

                	pTree.reFreshLoadGlobalTree(Id);

                }
            })
        }

        //模版管理
        function deleteTemplate() {
        	var tree = pTree.glTypeTree;
            var node = tree.getSelectedNodes()[0];
            var FcId = node.ProjectId;

            var url = "${ctx}" + "/oa/form/dataTemplate/preview.do?__displayId__=10000019300061&__dbomSql__=F_SSFCID="+FcId;
            openMyLinkDialog({url: url.getNewUrl(), title: "模板管理", height: 600, width: 900});

            /*		openMyLinkDialog({url: url.getNewUrl(), title: "导出模板", height: 400, width: 500, params: nodeInfo});
             url = encodeURI(url);
             $("#dataPackageFrame").attr("src",url);
             var tree = pTree.glTypeTree;
             var node = tree.getSelectedNodes()[0];
             var type = node.Type;//普通分类节点

             CommonDialog("mbgl", function (data) {
             var mbmc = data.F_MBMC;
             var Id = data.ID;

             var str = "确定将【" + data.F_MBMC + "】模板的信息删除吗！";
             $.ligerDialog.confirm(str, '提示信息', function (rtn) {
             if (rtn) {
             $.ajax({
             //	type: "POST",
             url: __ctx + '/dataPackage/tree/ptree/deleteTemplate.do',
             data: {
             Id: data.ID

             },
             //	dataType: "text",
             //	async: false,
             success: function (responseText) {
             var obj = new com.ibms.form.ResultMessage(responseText);
             if (obj.isSuccess()) {
             $.ligerDialog.success("删除成功", "", function () {
             //	reFresh()
             });
             } else {
             $.ligerDialog.err("提示信息", "", obj.getMessage());
             }
             }
             });
             }
             });
             });*/

        }
        //节点移动
        function treeMove() {
            var node = getSelectNode();

            if (!node) {
                $.ligerDialog.warn("请选择移动的目标节点！");
                return;
            }
            var Id = node.id;
            var fcId = node.ProjectId;
            if (node.Type == "普通分类节点" || node.Type == 'root') {
            	DialogUtil.open({
                    height: 500,
                    width: 300,
                    url: __ctx + '/dataPackage/tree/treeMove.do',
                    title: "数据包节点移动选择器",
                    params: {fcId: fcId,Id: Id,  isSingle: false},
                    sucCall: function (rtn) {
                        var Ids = rtn.ids;
                        if (!Ids || Ids == "") {
                            $.ligerDialog.warn("请选择要移动的数据包节点！")
                            $.ligerDialog.closeWaitting();
                            return;
                        }
                        $.ajax({
                            url: __ctx + '/dataPackage/tree/ptree/treeMove.do',
                            data: {
                                Id: Ids,
                                toId: Id,
                                fcId: fcId
                            },
                            success: function (responseText) {
                            	pTree.reFreshLoadGlobalTree(Id);
                            }
                        });
                    }
                });
            }else{
            	$.ligerDialog.warn("禁止选择非普通分类节点");
            	return;
            }
        }
        /**
    	 * Description : 数据包结构树复制非普通节点
    	 * Author : XYF
    	 * Date : 2018年8月16日下午1:57:10
    	 * Return
    	 */
        function copyNodes() {
            var node = getSelectNode();

            if (!node) {
                $.ligerDialog.warn("请选择目标节点！");
                return;
            }
            var Id = node.id;
            var fcId = node.ProjectId;
            if (node.Type == "普通分类节点" || node.Type == 'root') {
            	DialogUtil.open({
                    height: 500,
                    width: 300,
                    url: __ctx + '/dataPackage/tree/copyNodes.do',
                    title: "数据包节点复制选择器",
                    params: {fcId: fcId,Id: Id,  isSingle: false},
                    sucCall: function (rtn) {
                        var Ids = rtn.ids;
                        if (!Ids || Ids == "") {
                            $.ligerDialog.warn("请选择要复制的数据包节点！")
                            $.ligerDialog.closeWaitting();
                            return;
                        }
                        $.ajax({

                            url: __ctx + '/dataPackage/tree/ptree/copyNodes.do',
                            data: {
                                Ids: Ids,
                                toId: Id,
                                fcId: fcId
                            },
                            success: function (responseText) {
                            	pTree.reFreshLoadGlobalTree(Id);
                            }
                        });
                    }
                });
            	
            }else{
            	$.ligerDialog.warn("禁止选择非普通分类节点");
            	return;
            }
        }
        /**
         * @Author  shenguoliang
         * @Description:校验当前用户所属角色的权限
         * @Params
         * @Date 2018/5/17 18:15
         * @Return
         */
        function checkCurUserRole(role) {
            var flag = false;
            var roleNames = "";
            $.ajax({
                async: false,
                dataType: 'json',
                cache: false,
                type: 'post',
                data: {},
                url: "${ctx}/project/tree/stree/getCurUserInfo.do",
                success: function (data) {
                    var ISysUser = data;
                    roleNames = ISysUser.roles;
                    if (roleNames.indexOf(role) >= 0) {
                        flag = true;

                    } else {
                        $("#import").css("display", 'none');
                        $("#export").css("display", 'none');
                    }
                },
                error: function () {
                }
            });
            return flag;
        }

    </script>
</head>
<body>
<div id="defLayout">
    <div position="left" title="数据包结构树"
         style="text-align: left; border-style: solid; border-color: #FFFFFF #EEEEEE #FFFFFF #FFFFFF ">
        <div class="tree-toolbar" id="pToolbar">
            <div class="group"><a class="link reload" id="treeFresh" href="javascript:reFresh()">刷新</a></div>
            <div class="group"><a class="link expand" id="treeExpandAll"
                                  href="javascript:pTree.treeExpandAll(true)">展开</a></div>
            <div class="group"><a class="link collapse" id="treeCollapseAll"
                                  href="javascript:pTree.treeExpandAll(false)">收起</a></div>
            <div class="group"><a class="link collapse" id="import" href="javascript:nodeImport()">导入</a></div>
            <div class="group"><a class="link collapse" id="export" href="javascript:nodeExport()">导出</a></div>
            <div class="group"><a class="link collapse" id="deleteTemplate" href="javascript:deleteTemplate()">模版管理</a></div>
            <div class="group"><a class="link collapse" id="treeSetDown" href="javascript:setDown()">下移</a></div>
            <div class="group"><a class="link collapse" id="treeSetUp" href="javascript:setUp()">上移</a></div>
            <div class="group"><a class="link collapse" id="treeMove" href="javascript:treeMove()">移动其他节点</a></div>
            <div class="group"><a class="link collapse" id="copyNodes" href="javascript:copyNodes()">复制其他节点</a></div>
        </div>
        <div id="pTree" class="ztree" style="overflow-x:auto;clear:left";width:800px></div>
    </div>

    <div position="center">
        <iframe id="dataPackageFrame" src="" frameborder="no" width="100%" height="100%"></iframe>
    </div>
</div>
</body>
<%--修改zTree树的宽度高度滚动条显示--%>
<script type="text/javascript" src="${ctx}/js/dataPackage/tree/zTreeStyle/zTreeWidthHeightScroll.js"></script>
</html>