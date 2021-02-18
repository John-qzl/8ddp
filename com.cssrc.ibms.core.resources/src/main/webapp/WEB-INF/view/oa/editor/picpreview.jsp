<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title>Insert title here</title>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<link rel="stylesheet" href="${ctx}/styles/default/css/dp.css">


<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/AttachMent.js"></script>
<%@include file="/commons/include/form.jsp" %>
<script type="text/javascript" charset="utf-8" src="${ctx}/editor/jquery.js"></script>
<f:js pre="jslib/lang/js"></f:js>
<script type="text/javascript" src="${ctx}/jslib/lg/ligerui.all.js"></script>
<script type="text/javascript">
    $(window).on('load', function () {
        // 实现瀑布流
        waterFall()
    })


    //实现瀑布流
    function waterFall() {

        // 拿到所有盒子
        var allBox = $('.preview-pic');
        // 列数
        var cols = 4;
        //盒子定位
        var heightArr = [];
        // 遍历
        $.each(allBox, function (index, value) {
            // 取出单独盒子的高度
            var boxHeight = $(value).outerHeight();
            //console.log(boxHeight)
            // 判断是否第一行
            if (index < cols) {
                heightArr[index] = boxHeight
                $(value).css({
                    "position": "absolute",
                    "top": "20px",
                    "left": index * 0.25 * 100 + "%",
                    "bottom":"320px"
                })
            } else {
                // 求最矮盒子的高度

                var minBoxHeight = Math.min.apply(null, heightArr)
                // 取出最高高度的索引
                var minBoxIndex = $.inArray(minBoxHeight, heightArr);
                // 定位
                $(value).css({
                    "position": "absolute",
                    "top": minBoxHeight + 40 + "px",
                    "left": minBoxIndex * 0.25 * 100 + "%",
                    "bottom":minBoxHeight + 340 + "px"
                })
                //更新数组中最矮的高度
                heightArr[minBoxIndex] += (boxHeight + 20)
            }
        })
    }

    function picDelete(obj) {

        $.ligerDialog.confirm('确认删除吗?', function (rtn) {
            if (rtn) {
                var url = "${ctx}/dp/form/picdel.do";
                var params = {fileId: obj};
                $.post(url, params, function (data) {
                    //console.log(data);
                    if (data.success == true) {
                        $.ligerDialog.success(data.msg, "删除成功");
                        location.reload()
                    } else
                        $.ligerDialog.error(data.msg, "删除失败");
                });
            }
        });
    }

    function diadelete(obj1, obj2) {


        $.ligerDialog.confirm('确认删除吗?', function (rtn) {
            if (rtn) {
                var url = "${ctx}/dp/form/picdel.do";
                var params = {fileId: obj1, slid: obj2};
                $.post(url, params, function (data) {
                    //console.log(data);
                    if (data.success == true) {
                        $.ligerDialog.success(data.msg, "删除成功", function () {
                            location.reload();
                        });
                    } else
                        $.ligerDialog.error(data.msg, "删除失败");
                });
            }
        });
    }
    /** 下载文件
     * @param obj
     */
    function picDownload(obj) {

        $.ligerDialog.confirm('确认下载此文件吗?', function (rtn) {
            if (rtn) {
            window.location.href = __ctx + '/oa/system/sysFile/download.do?fileId=' + obj;
            }
        });
    }
    /**
     * 关闭窗体返回值
     */
    function closeWindow(){
        dialog.get("sucCall")(returnArray);
        dialog.close();
    };
</script>
</head>
<body>
<div class="preview-box">
    <c:choose>
        <c:when test="${empty ifdel}">
            <c:forEach items="${images}" var="image">
                <div class="preview-pic" align="center">
                    <div class="pic" >
                        <c:choose>
                            <c:when test="${empty image.slid}">
                                <c:if test="${image.fileType =='jpg' || image.fileType =='png' || image.fileType =='gif'|| image.fileType =='bmp'}">
                                <%--图片预览    --%>
                                <img src="${image.imgSrc}"/>
                                <a>${image.fileName}</a>
                                <div class="cancel-btn" onclick="javascript:picDelete(${image.fileId})"></div>
                                <div class="save-btn" onclick="javascript:picDownload(${image.fileId})"></div>

                                </c:if>
                                <c:if test="${image.fileType !='jpg'&& image.fileType !='png' &&image.fileType !='gif' && image.fileType !='bmp'}">
                                    <%--附件下载--%>
                                <img src="${image.imgSrc}" style="width:42px; height:50px"/>
                                    <div class="file-border">
                                    <a   onclick="javascript:picDownload(${image.fileId})">${image.fileName}</a>
                                    </div>
                                    <%--附件删除--%>
                                    <div class="file-border">
                                    <a  onclick="javascript:picDelete(${image.fileId})">删除</a>
                                    </div>

                                    <%--<div class="save-btn" onclick="javascript:picDownload(${image.fileId})"></div>--%>
                                </c:if>
                            </c:when>
                            <c:otherwise>

                                <img src="${image.imgSrc}" style="width:42px; height:50px"/>
                                <a>${image.fileName}</a>
                                <%--附件删除--%>
                                <div class="cancel-btn"
                                     onclick="javascript:diadelete(${image.fileId},${image.slid})"></div>
                                <%--附件下载--%>
                                <div class="save-btn"
                                     onclick="javascript:picDownload(${image.fileId},${image.slid})">
                                </div>

                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <c:forEach items="${images}" var="image">
                <div class="preview-pic" align="center">
                    <div class="pic">
                        <c:choose>
                            <c:when test="${empty image.slid}">
                                <c:if test="${image.fileType == 'jpg'  || image.fileType =='png' || image.fileType =='gif'|| image.fileType =='bmp'}">
                                    <%--图片预览    --%>
                                    <img src="${image.imgSrc}"/>
                                    <a>${image.fileName}</a>
                                    <div class="save-btn" onclick="javascript:picDownload(${image.fileId})"></div>
                                </c:if>
                                <c:if test="${image.fileType !='jpg'&& image.fileType !='png' &&image.fileType !='gif' && image.fileType !='bmp'}">
                                <%--文件下载--%>
                                <img src="${image.imgSrc}" style="width:42px; height:50px"/>
                                    <div class="file-border">
                                        <a onclick="javascript:picDownload(${image.fileId})">${image.fileName}</a>
                                    </div>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <%--图片预览--%>
                                <img src="${image.imgSrc}"/>
                                <a>${image.fileName}</a>
                                //图片下载
                                <div class="save-btn" onclick="javascript:picDownload(${image.fileId})"></div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>

</div>
</body>
</html>