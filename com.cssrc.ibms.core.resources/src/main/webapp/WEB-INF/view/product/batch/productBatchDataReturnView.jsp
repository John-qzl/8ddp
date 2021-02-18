<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/commons/include/html_doctype.html" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>批次--数据回传查看</title>
    <%@include file="/commons/include/form.jsp" %>
    <f:gtype name="CAT_FORM" alias="CAT_FORM"></f:gtype>
    <f:link href="tree/zTreeStyle.css"></f:link>
    <f:resources name="ROOT_PID" alias="ROOT_PID"></f:resources>
    <script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
    <%-- 去除，改为 产品 结构树
    <script type="text/javascript" src="${ctx}/js/project/product/batch/dataReturnView/BatchPlanTree.js"></script>
     --%>
    <script type="text/javascript" src="${ctx}/js/project/product/batch/dataReturnView/PlanProductTree.js"></script>
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
        /* 
        var mappingUrl = "/product/acceptance/plan/";
         */
        var mappingUrl = "/product/infor/";
        var batchId = $.getParameter("batchId");
        var batchKey = $.getParameter("batchKey");
        // 产品验收策划id
        var acceptancePlanId = $.getParameter("acceptancePlanId");
        /* 
        var acceptancePlanTree = new AcceptancePlanTree("dataReturnView", "acceptancePlanTree", {
            onClick: acceptancePlanTreeOnClick
        }, mappingUrl, batchId, batchKey);
         */
        var planProductTree = new PlanProductTree("dataReturnView", "planProductTree", {
             onClick: planProductTreeOnClick
        }, mappingUrl, batchId, batchKey, acceptancePlanId);
        
        $(function () {
            layui.use('element', function () {
                var element = layui.element;
            })
            // 布局
            loadLayout();
            // 加载树
            planProductTree.loadGlobalTree();
        });

        // 布局
        function loadLayout() {
            $("#defLayout").ligerLayout({
                leftWidth: 200,
                onHeightChanged: heightChanged,
                allowLeftResize: false
            });
            // 取得layout的高度
            var height = $(".l-layout-center").height() - 28;
            var _height = $('.tree-toolbar').height()
            $("#planProductTree").height(height - _height);
        }

        // 布局大小改变的时候通知tab，面板改变大小
        function heightChanged(options) {
            $("#planProductTree").height(options.middleHeight - 90);
        }
        
    </script>
</head>
<body>
<div id="defLayout">
    <div position="left" style="text-align: left;">
        <div id="planProductTree" class="ztree" style="overflow:auto;clear:left;"></div>
    </div>
    <div position="center">
        <iframe id="dataReturnViewFrame" src="" frameborder="no" width="100%" height="100%"></iframe>
    </div>
</div>
</body>
</html>
