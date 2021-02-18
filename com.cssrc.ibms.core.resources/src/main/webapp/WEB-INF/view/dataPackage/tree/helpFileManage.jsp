<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/commons/include/html_doctype.html" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

    <title>电子字典</title>
    <%@include file="/commons/include/form.jsp" %>
    <f:gtype name="CAT_FORM" alias="CAT_FORM"></f:gtype>
    <f:link href="tree/zTreeStyle.css"></f:link>
    <f:resources name="ROOT_PID" alias="ROOT_PID"></f:resources>
    <link rel="stylesheet" href="${ctx}/layui/css/layui.css">
    <link rel="stylesheet" href="${ctx}/jslib/tree/zTreeStyle.css" type="text/css">
    <link rel="stylesheet" href="${ctx}/styles/default/css/iconfont.css" type="text/css">
    <link rel="stylesheet" href="${ctx}/styles/default/css/iconImg.css" type="text/css">
    <link rel="stylesheet" href="${ctx}/styles/default/css/8ddp/projectTree.css" type="text/css">
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
        var mappingUrl = "/helpfile/manage/tree/";
        var sTree = new GlobalType(catKey, "sTree", {
            onClick: moduleTreeOnClick
/*             ,onRightClick: moduleTreeOnRightClick */
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
            //加载菜单
            $(document).click(hiddenMenu);
            var url = "${ctx}" + "/oa/form/dataTemplate/preview.do?__displayId__=10000028380212";
            $("#fileFrame").attr("src",url);
        });

        //布局
        function loadLayout() {
            $("#defLayout").ligerLayout({
                leftWidth: 200,
                onHeightChanged: heightChanged,
                allowLeftResize: true
            });
   
        }
        //布局大小改变的时候通知tab，面板改变大小
        function heightChanged(options) {
            $("#sTree").height(options.middleHeight - 90);
        }
		
         // 型号管理结构树左击事件
        function moduleTreeOnClick(event, treeId, treeNode) {
      
            debugger;
            switch (treeNode.level) {
                case 0:        
                	 $("#fileFrame").attr("src", treeNode.tempUrl);
                    break;
                case 1:
                    $("#fileFrame").attr("src", treeNode.tempUrl);
                    break;
            }
        }
        
	
        //文件管理树右击事件
        function moduleTreeOnRightClick(event, treeId,treeNode) {
            hiddenMenu();
            if (treeNode) {
            	debugger;
            	var height = $(".l-layout-center").height() - 28;
                sTree.currentNode = treeNode;
                sTree.glTypeTree.selectNode(treeNode);
                //设置权限,目前只允许项目办修改型号和发次
                var roleNames = '';
                var typeId=treeNode.typeId;
                
                $.ajax({
                    async: 'false',
                    dataType: 'json',
                    data: {type:typeId},
                    /* url: "${ctx}/project/tree/stree/getCurUserInfo.do", */
                    url: "${ctx}/model/manage/tree/getCurUserInfo.do",
                    success: function (data) {
                        var ISysUser = data.curUser;
                       /*  if (roleNames.indexOf("xmb") >= 0) { */
                            ishandler(treeNode,data);
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
        //右击事件处理
        function ishandler(treeNode,roleNames) {
            curMenu = formDefMenu.getMenu(treeNode, handler,roleNames);
        }
        function handler(item) {
            hiddenMenu();
            debugger;
            var txt = item.text;
            switch (txt) {
                case "新增":
                	if(item.roleNames.curUser.roles.indexOf("sjgly")>=0){
                		sTree.editNode(true);
                	}
                	else{
                		$.ligerDialog.warn("您没有权限！");
                	}
                    break;
                case "编辑":
                	if(item.roleNames.curUser.roles.indexOf("sjgly")>=0){
                		 sTree.editNode(false);
                	}
                	else{
                		$.ligerDialog.warn("您没有权限！");
                	}
                    break;
                case "删除":
                	if(item.roleNames.curUser.roles.indexOf("sjgly")>=0){
                		sTree.delNode();
               	}
               	else{
               		$.ligerDialog.warn("您没有权限！");
               	}
                	break;
            }
        }
        //隐藏右键菜单
        function hiddenMenu() {
            if (curMenu) {
                curMenu.hide();
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
        function getSelectNode() {
            var tree = sTree.glTypeTree;
            var nodes = tree.getSelectedNodes();
            return nodes[0];
        }
    </script>
    <style>
    	.layui-tab.layui-tab-brief.template1 .layui-tab-content{
    		border-top: 1px solid #d9d9d9;
    	}
    </style>
</head>
<body>
<div id="defLayout">
    <div position="left" title="电子词典"
         style="text-align: left; border: 1px solid #d9d9d9; background-color: #f5f5f5;">
        <div class="tree-toolbar" id="pToolbar">
            <div class="group"><a class="link reload" id="treeFresh" href="javascript:reFresh()">刷新</a></div>
            <div class="group"><a class="link expand" id="treeExpandAll" href="javascript:sTree.treeExpandAll(true)">展开</a></div>
            <div class="group"><a class="link collapse" id="treeCollapseAll" href="javascript:sTree.treeExpandAll(false)">收起</a></div>
        </div>
        <div id="sTree" class="ztree" style="overflow:auto;clear:left ;background-color: #fff;"></div>
    </div>

    <div position="center">
        <div class="layui-tab layui-tab-brief template1">
            <div class="layui-tab-content">
                <div class="layui-tab-item layui-show">
                    <iframe id="fileFrame" src="" frameborder="no" width="100%" height="100%"></iframe>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<%--修改zTree树的宽度高度滚动条显示--%>
<script type="text/javascript" src="${ctx}/js/dataPackage/tree/zTreeStyle/zTreeWidthHeightScroll.js"></script>

</html>
