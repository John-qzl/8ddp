<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/commons/include/html_doctype.html" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

    <title>靶场试验策划主页面</title>
    <%@include file="/commons/include/form.jsp" %>
    <f:gtype name="CAT_FORM" alias="CAT_FORM"></f:gtype>
    <f:resources name="ROOT_PID" alias="ROOT_PID"></f:resources>
    <link rel="stylesheet" href="${ctx}/layui/css/layui.css">
    <script type="text/javascript" src="${ctx}/layui/layui.js"></script>
    <style type="text/css">
        .tree-title {
            overflow: hidden;
            width: 100%;
        }

        html, body {
            padding: 0px;
            margin: 0;
            width: 100%;
            height: 100%;
            overflow: hidden;
        }

        .w100 {
            width: 100%;
            height: 100%;
        }
    </style>
    <script type="text/javascript">

        var missionId = "<%=request.getParameter("missionId")%>";
        var batchKey = "<%=request.getParameter("batchKey")%>";
        // 验收策划
        var acceptancePlanId = "<%=request.getParameter("acceptancePlanId")%>";
        var productCategoryId = "<%=request.getParameter("productCategoryId")%>";
        var xhId = "<%=request.getParameter("xhId")%>";
        var checkData;

        //默认打开的页面
        function openDefaultFrame() {
            // 产品策划-表单下发 节点
            debugger;
            var formAssignUrl = "${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000029900146"
                //	  				+ "&batchName=" + batchKey
                + "&acceptancePlanId=" + acceptancePlanId
                + "&xhId=" + xhId
                + "&__dbomSql__=F_acceptancePlanId=" + acceptancePlanId;
            $("#formAssign").attr("src", encodeURI(encodeURI(formAssignUrl)));
        }

        // 数据回传查看校验--验收策划通过、表单下发
        function dataReturnViewCheck() {
            $.ajax({
                url: "${ctx}/product/category/batch/dataReturnViewCheck.do",
                data: {
                    batchId: batchId,
                    batchKey: batchKey
                },
                async: false,
                success: function (data) {
                    checkData = data;
                }
            });
        }

        $(function () {
            layui.use('element', function () {
                var element = layui.element;
                var content = $('.layui-tab-content');
                var _height = content.closest('.layui-tab').height()
                    - content.prev('.layui-tab-title').height() - 18;
                $('.layui-tab-content').height(_height)

            })
            // 设置默认打开的第一个页面
            openDefaultFrame();

            $('.layui-tab-title').on('click', 'li', function () {
                switch ($(this).attr("tabid")) {
                    case "formAssign":
                        debugger;
                        // 表单下发
                        var formAssignUrl = "${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000029900146"
                            //	  				+ "&batchName=" + batchKey
                            + "&acceptancePlanId=" + acceptancePlanId
                            + "&xhId=" + xhId
                            + "&__dbomSql__=F_acceptancePlanId=" + acceptancePlanId;
                        $("#formAssign").attr("src", encodeURI(encodeURI(formAssignUrl)));
                        break;
                    case "dataReturnView":
                        // 数据回传查看
                        var dataReturnViewUrl = "${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000029900166"
                            + "&missionId=" + acceptancePlanId;
                        $("#dataReturnView").attr("src", encodeURI(encodeURI(dataReturnViewUrl)));
                        break;
                    case "acceptanceSummary":
                        // 数据确认
                        var acceptanceSummaryUrl = "${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000029490089"
                            + "&acceptancePlanId=" + acceptancePlanId
                            + "&__dbomSql__=F_ssch=" + acceptancePlanId;
                        $("#acceptanceSummary").attr("src", encodeURI(encodeURI(acceptanceSummaryUrl)));
                        break;
                    case "ImportAcceptanceSummary":
                        // 导入数据验收总结
                        var acceptanceSummaryUrl = "${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000027770111&batchId=" + batchId + "&batchName=" + batchKey
                            + "&acceptancePlanId=" + acceptancePlanId
                            + "&productCategoryId=" + productCategoryId;
                        $("#ImportAcceptanceSummary").attr("src", encodeURI(encodeURI(acceptanceSummaryUrl)));
                        break;
                        //20200916  把归档调到了策划一级 by zmz
                    case "file":
                        // 归档
                        fileurl = "${ctx}/oa/form/dataTemplate/preview.do?__displayId__=10000029710069"
                            + "&xhId=" + xhId
                            + "&__dbomSql__=ID="+acceptancePlanId;
                        $("#file").attr("src", encodeURI(encodeURI(fileurl)));
                        break;
                }

            });
        });

    </script>
</head>
<body>
<div style="width: 100%; top: 0px; left: 0px; height: 100%;">
    <div class="layui-tab layui-tab-brief template1">
        <ul class="layui-tab-title">
            <li tabid="formAssign" class="item2 layui-this">表单下发</li>
            <li tabid="dataReturnView" class="item3">数据回传查看</li>
            <li tabid="acceptanceSummary" class="item4">数据确认</li>
            <li tabid="file" class="item5">归档</li>
            <!-- <li tabid="ImportAcceptanceSummary" class="item5">导入数据验收总结</li>  -->
        </ul>
        <div class="layui-tab-content">

            <div class="layui-tab-item layui-show">
                <iframe id="formAssign" src="" frameborder="no" width="100%" height="100%"></iframe>
            </div>
            <div class="layui-tab-item">
                <iframe id="dataReturnView" src="" frameborder="no" width="100%" height="100%"></iframe>
            </div>
            <div class="layui-tab-item">
                <iframe id="acceptanceSummary" src="" frameborder="no" width="100%" height="100%"></iframe>
            </div>
            <div class="layui-tab-item">
                <iframe id="file" src="" frameborder="no" width="100%" height="100%"></iframe>
            </div>
            <!-- 	<div class="layui-tab-item">
                 <iframe id="ImportAcceptanceSummary" src="" frameborder="no" width="100%" height="100%"></iframe>
             </div>  -->
        </div>
    </div>
</div>
</body>
</html>
