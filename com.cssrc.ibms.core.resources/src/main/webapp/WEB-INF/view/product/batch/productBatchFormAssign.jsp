<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/commons/include/html_doctype.html" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>批次表单下发管理</title>
    <%@include file="/commons/include/form.jsp" %>
    <f:gtype name="CAT_FORM" alias="CAT_FORM"></f:gtype>
    <f:link href="tree/zTreeStyle.css"></f:link>
    <f:resources name="ROOT_PID" alias="ROOT_PID"></f:resources>
    <script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
    <script type="text/javascript" src="${ctx}/js/project/product/batch/formAssign/AcceptancePlanTree.js"></script>
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
        
        .l-layout-header {
        	display: none;
        }
        
        .l-layout-left {
        	border-style: solid;
        	border-color:#DEDEDE;
        }

        html, body {
            padding: 0px;
            margin: 0;
            width: 100%;
            height: 100%;
            overflow: hidden;
        }
    </style>
    <script type="text/javascript">
        // 产品验收策划映射
        var mappingUrl = "/product/acceptance/plan/";
        var batchId = $.getParameter("batchId");
        var batchKey = $.getParameter("batchKey");
        var acceptancePlanTree = new AcceptancePlanTree("formAssign", "acceptancePlanTree", {
            onClick: acceptancePlanTreeOnClick
        }, mappingUrl, batchId, batchKey);
        $(function () {
            layui.use('element', function () {
                var element = layui.element;
            })
            // 布局
            loadLayout();
            // 加载树
            acceptancePlanTree.loadGlobalTree();
            // 加载根节点（批次）基本信息（默认）
            rootNodeInfo();
        });

        // 布局
        function loadLayout() {
            $("#defLayout").ligerLayout({
                leftWidth: 234,
                onHeightChanged: heightChanged,
                allowLeftResize: false
            });
            // 取得layout的高度
            var height = $(".l-layout-center").height() - 28;
            var _height = $('.tree-toolbar').height()
            $("#acceptancePlanTree").height(height - _height);
        }

        // 布局大小改变的时候通知tab，面板改变大小
        function heightChanged(options) {
            $("#acceptancePlanTree").height(options.middleHeight - 90);
        }
        
     	// 加载根节点右侧批次信息 （产品批次明细） 
		function rootNodeInfo() {
		  	var url="${ctx}"+"/oa/form/dataTemplate/detailData.do?__displayId__=10000021200168&__pk__=" + batchId;
			url = encodeURI(url); 
			$("#formAssignFrame").attr("src",url);
		}
        
    </script>
</head>
<body>
<div id="defLayout">
    <div position="left" style="text-align: left;">
        <!--  
        <div class="tree-toolbar" id="pToolbar">
            <div class="group"><a class="link reload" id="treeFresh" onclick="acceptancePlanTree.reFresh()">刷新</a></div>
            <div class="group"><a class="link expand" id="treeExpandAll"
                                  href="javascript:acceptancePlanTree.treeExpandAll(true)">展开</a></div>
            <div class="group"><a class="link collapse" id="treeCollapseAll"
                                  href="javascript:acceptancePlanTree.treeExpandAll(false)">收起</a></div>
        </div>
         -->
        <div id="acceptancePlanTree" class="ztree" style="overflow:auto;clear:left;"></div>
    </div>
    <div position="center">
        <iframe id="formAssignFrame" src="" frameborder="no" width="100%" height="100%"></iframe>
    </div>
</div>
</body>
</html>
