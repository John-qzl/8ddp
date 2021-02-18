<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title><title>表单页</title>
    <c:set var="ctx" value="${pageContext.request.contextPath}"/>
    <%@include file="/commons/include/form.jsp" %>
    <f:gtype name="CAT_FORM" alias="CAT_FORM"></f:gtype>
    <f:link href="tree/zTreeStyle.css"></f:link>
    <f:resources name="ROOT_PID" alias="ROOT_PID"></f:resources>
    <link rel="stylesheet" href="${ctx}/layui/css/layui.css">
    <style media="screen">
        td {
            /*文本不会换行，文本会在在同一行上继续，直到遇到 <br> 标签为止。*/
            /*white-space: nowrap;*/
            /*width: 100%;*/
            /*height: auto;*/
            /*word-wrap:break-word;*/
            /*word-break:break-all;*/
            /*overflow: hidden;*/
        }
        .dpCheckbox {
            vertical-align: middle !important;
            margin: 0 2px;
        }
        .dpInputBtn {
            vertical-align: middle !important;
            background-color: #3EAAF5;
            color: white;
            border: none !important;
            margin: 0 2px;
        }
        .dpInputText {
            vertical-align: middle !important;
            /*width: 60px !important;*/
            height: 22px !important;
            margin: 0 2px;
            line-height: 22px!important;
            padding-bottom:0 !important;
            padding-top:0 !important;
        }
        table {
            border: 0px;
        }
    </style>
    <script type="text/javascript" src="${ctx}/layui/layui.js"></script>
    <script type="text/javascript">
        $(function () {
            //增加包络分析按钮
            addDEA();
            //ligerui Tab
            layui.use('element', function () {
                var element = layui.element;
            })
            var inputs = $("#content").find("input");
            for (var i = 0; i < inputs.length; i++) {
                //只读
                $(inputs[i]).attr("readonly", "true");
                //增加文本框提示
                var value =  $(inputs[i]).attr("value");
                $(inputs[i]).attr("title", value);
            }
            //根据附件中是否存在附件决定附件按钮的样式
            var fileBtns = $('.dpInputBtn');
            var fileIdArray = [];
            for (var j = 0, fileCount = fileBtns.length; j < fileCount; j++) {
                var inputs = $(fileBtns[j]).parent('td').find("input");
                var slId = $("#slid").val();
                var fileObj = {};
                //附件所在单元格唯一id根据 文本框或者勾选框确认(附件按钮不会单独存在)
                for (var i = 0, len = inputs.length; i < len; i++) {
                    //如果文本框以及勾选框在一个单元格中,附件绑定的ID为文本框ID
                    if ($(inputs[i]).attr("type") == "text") {
                        fileObj['id'] = $(inputs[i]).attr("id");
                        fileObj['slId'] = slId;
                        break;
                    } else if ($(inputs[i]).attr("type") == "checkbox") {
                        fileObj['id'] = $(inputs[i]).attr("id");
                        fileObj['slId'] = slId;
                    }
                }
                fileIdArray.push(fileObj);
            }
            $.ajax({
                async: false,
                cache: true,
                type: 'POST',
                data: {fileIdArray: JSON.stringify(fileIdArray)},
                url: __ctx + "/dp/form/checkWhetherFileExist.do",
                success: function (data) {
                    //后台获取的当前表单的所有fileBtn的有无附件情况(json格式存放)
                    var result = JSON.parse(data);
                    for (var j = 0, fileCount = fileBtns.length; j < fileCount; j++) {
                        //根据是否存在(yes/no),更改按钮样式
                        var ifExist = result[0][j];
                        if (ifExist == 'yes') {
                            $(fileBtns[j]).css("background", "red")
                        }
                    }
                },
                error: function () {
                    $.ligerDialog.error('');
                }
            });
        });

        function exportExcel() {
            var slid = $("#slid").val();
            var conditions = $("#condition").html();
            //	var content=$("#content").html();
            var signs = $("#sign").html();
            var html = conditions + signs;
            var url = "${ctx}/dp/form/exportExcel.do?html=" + encodeURIComponent(html) + "&slid=" + slid;
            window.location.href = url;
        }
        function diagramupload(obj) {
            window.top.uploadId = $(this).attr('uploadId');
            $.ligerDialog.open({
                url: "${ctx}/dp/form/diagramupload.do?id=" + obj + "&slid=" + $("#slid").val(),
                height: 500,
                width: 1000,
                title: "示意图上传",
                isResize: true,
                sucCall: function (rtn) {

                },
                buttons: [{
                    text: '关闭',
                    onclick: function () {
                        $.ligerDialog.close(); //有遮盖层
                        $(".l-dialog-frame,.l-dialog,.l-window-mask").remove();
                        location.reload()

                    }
                }]
            });
        }

        function diagrampreview(obj) {
            //DialogUtil.open({
            $.ligerDialog.open({
                url: "${ctx}/dp/form/picpreview.do?diagramid=" + obj + "&slid=" + $("#slid").val(),
                //height: window.screen.availHeight,
                //width: window.screen.availWidth,
                height: 400,
                width: 800,
                title: "示意图预览",
                isResize: true,
                sucCall: function (rtn) {

                },
                buttons: [{
                    text: '关闭',
                    onclick: function () {
                        $.ligerDialog.close(); //有遮盖层
                        $(".l-dialog-frame,.l-dialog,.l-window-mask").remove();
                        location.reload();
                    }
                }]
            });
        }
        //附件查看
        function addAndShowPhoto(arg) {
            //
            var obj = "";
            var slId = $("#slid").val();
            var ckResultName = "";
            //根据slId获取对应的型号类型
            $.ajax({
                type: "POST",
                url: "${ctx}/project/tree/stree/getCondResultNameByInsId.do",
                data: {
                    slId: slId
                },
                dataType: "json",
                async: false,
                success: function (data) {
                    ckResultName = data;
                }
            });
            var inputs = $(arg).parent('td').find("input");
            for (var i = 0; i < inputs.length; i++) {
                if ($(inputs[i]).attr("type") == "text") {
                    obj = $(inputs[i]).attr("id");
                    break;
                } else if ($(inputs[i]).attr("type") == "checkbox") {
                    obj = $(inputs[i]).attr("id");
                }
            }

            var url = "${ctx}/dp/form/existPicture.do";
            var params = {tableId: obj, ckResultName: ckResultName};
            $.post(url, params, function (data) {
                if (data.success == "true") {
                    DialogUtil.open({
                        url: "${ctx}/dp/form/picpreview.do?id=" + obj + "&ifdel=" + 'no' + '&ckResultName=' + ckResultName,
                        height: window.screen.availHeight,
                        width: window.screen.availWidth,
                        title: "附件查看",
                        isResize: true,
                        sucCall: function (rtn) {

                        }
                    });
                } else {
                    $.ligerDialog.error('当前检查项未上传附件无法查看', "查看失败");
                }
            });
        }
        function addDEA() {
            var tds = $("#content table").find("td");
            for (var i = 0; i < tds.length; i++) {
                if ($(tds[i]).find('input[type="text"]').length > 0) {
                    // $(tds[i]).append("&nbsp;<button onclick='deaAnalysis(this)'>包络分析</button>");
                }
            }
        }
        function deaAnalysis(arg) {
            var inputs = $(arg).parent('td').find("input");
            var ckResultId = $(inputs).attr("id");
            var slid = ${slid};

            DialogUtil.open({
                url: __ctx + "/dataPackage/dea/getQueryInfoByFrom.do?ckResultId=" + ckResultId + "&slid=" + slid,
                height: 650,
                width: 1400,
                title: "包络分析",
                isResize: true,
                sucCall: function (rtn) {

                }
            });
        }
    </script>
</head>
<body>
<div class="layui-tab layui-tab-brief template1" >
    <ul class="layui-tab-title">
        <li class="item1 layui-this">表格预览</li>
        <!-- <li class="item2">检查项管理</li> -->
    </ul>
    <div class="layui-tab-content">
        <div class="layui-tab-item layui-show">

            <div class="panel-toolbar">
                <div class="toolBar">
               <%--      <div class="group"><a class="link export" onclick="javascript:exportExcel()" formId="${id}">导出</a>
                    <div class="group"><a class="link export" onclick="printhtml()">打印</a> --%>
                    </div>
                </div>
            </div>
            <div id="context">
<c:if test="${!ifAllNullCondires}">
            <fieldset class="layui-elem-field" >
                <legend>检查条件</legend>
                <div id="condition" class="layui-field-box">
                    <table class="layui-table ">
                        <tr>
                            <c:forEach items="${condires}" var="condi">
                                <td align="right " width="20% ">${condi.F_NAME }：</td>
                                <td align="left ">${condi.F_VALUE}</td>
                            </c:forEach>
                        </tr>
                    </table>
                </div>
            </fieldset>
</c:if>
            <fieldset class="layui-elem-field">
                <legend>检查内容</legend>
                <div id="content" class="layui-field-box">
                    ${content}
                </div>
            </fieldset>
            <fieldset class="layui-elem-field">
                <legend>签署</legend>
                <div id="sign" class="layui-field-box">
                    <table class="layui-table ">
                        <c:forEach items="${signresult}" var="user">
                            <tr>
                                <td align="right " width="20% ">${user.F_NAME }：</td>
                                <td align="left ">
                                    <c:if test="${empty user.imgSrc}">${user.F_SIGNUSER }</c:if>
                                    <c:if test="${not empty user.imgSrc}"><img src="${user.imgSrc}"/></c:if>
                                </td>
                                <td>
                                    签署日期
                                </td>
                                <td>${user.F_SIGNTIME}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </fieldset >
         </div>
        </div>
        <div class="layui-tab-item ">
            <table border="1" width="100%" class="layui-table">
                <tr>
                    <td></td>
                    <td>检查项描述</td>
                    <td>I类单点</td>
                    <td>II类单点</td>
                    <td>易错难</td>
                    <td>拧紧力矩要求</td>
                    <td>最后一次动作</td>
                    <td>上传示意图</td>
                </tr>
                <c:forEach items="${checkitem }" var="item" varStatus="i">
                    <tr>
                        <td>${i.index+1}</td>
                        <td>${item.F_DESCRIPTION }</td>
                        <td>${item.F_ILDD }</td>
                        <td>${item.F_IILDD }</td>
                        <td>${item.F_YCN }</td>
                        <td>${item.F_NJLJYQ }</td>
                        <td>${item.F_ZHYCDZ }</td>
                        <td>
                            <c:choose>
                                <c:when test="${empty item.F_SKETCHMAP}">
                                    <span uploadId="${item.ID}" onclick="diagramupload(${item.ID})"
                                          uploadId='${item.ID}'><img src="${ctx}/dpImg/upload.png" alt="上传"></span>
                                </c:when>
                                <c:otherwise>
                                    <span onclick="diagrampreview(${item.F_SKETCHMAP})"
                                          uploadId='${item.F_SKETCHMAP}'><img src="${ctx}/dpImg/uploaded.png" alt="查看"></span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>
<input type="hidden" id="slid" name="slid" value="${slid }"/>
<script>
function printhtml(){
	 debugger;
	 let newstr = document.getElementById("context").innerHTML;
     let oldstr = document.body.innerHTML;
     document.title="${title}";
     document.body.innerHTML = newstr;
     pagesetup_null();
     window.print();
     document.body.innerHTML = oldstr;
     
}
//设置网页打印的页眉页脚为空
function pagesetup_null(){                
            var     hkey_root,hkey_path,hkey_key;
            hkey_root="HKEY_CURRENT_USER"
            hkey_path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
            try{
                  var RegWsh = new ActiveXObject("WScript.Shell");
                  hkey_key="header";
                  RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"");
                  hkey_key="footer";
                  RegWsh.RegWrite(hkey_root+hkey_path+hkey_key,"");
               }catch(e){

               }
       }
 
</script>
</body>
</html>
