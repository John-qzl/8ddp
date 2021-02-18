<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.cssrc.ibms.api.sysuser.util.UserContextUtil" %>
<%@ include file="/commons/include/html_doctype.html" %>
<%
    String fullName = UserContextUtil.getCurrentUser().getFullname();
%>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <%@include file="/commons/include/form.jsp" %>
    <title>实例文件上传</title>
    <script type="text/javascript" src="${ctx}/jslib/fileupload/plupload/plupload.full.min.js"></script>
    <script type="text/javascript"
            src="${ctx}/jslib/fileupload/plupload/jquery.plupload.queue/jquery.plupload.queue.js"></script>

</head>

<body>
<div class="panel">
    <!-- 文件上传：操作按钮 -->
    <div class="panel-top">
        <div class="panel-toolbar">
            <div class="group"><a class="link add" id="add" href="####">添加</a></div>
            <div class="group"><a class="link upload" id="upload" href="####" onclick="javascript:uploadFile()">上传</a>
            </div>
            <div class="group"><a class="link close" href="####" onclick="javascript:closeWindow()">关闭</a></div>
<%--            <div class="group"><a class="link down" href="${ctx}/temp/FlashInstall.exe">Flsh插件</a></div>
            <div class="group"><span style="color:red">*如果多附件上传请点击Flash插件进行安装 </span></div>--%>
        </div>
    </div>

    <!-- 文件上传：文件列表 -->
    <div class="panel-body">
        <table id="fileTable" width="100%" cellpadding="1" cellspacing="1" class="table-grid">
            <tr align="center" class="table-grid-th">
                <td nowrap style="width:30px;height:10px;padding:2px 0px 4px 5px;">序号</td>
                <td nowrap style="width:80px;height:10px;padding:2px 0px 4px 5px;">上传状态</td>
                <td style="height:10px;padding:2px 0px 4px 5px;">文件名</td>
                <c:if test="${isDisplay == true}">
                    <td nowrap style="width:80px;height:10px;padding:2px 0px 4px 5px;">密级</td>
                </c:if>
                <td nowrap style="width:60px;height:10px;padding:2px 0px 4px 5px;">上传人</td>
                <!--  <td style="height:10px;padding:2px 0px 4px 5px;min-width:150px;">描述</td> -->
                <c:if test="${useDimension == true&&fileId ==0}">
                    <td nowrap style="height:10px;padding:2px 0px 4px 5px;min-width:80px;">所属维度</td>
                </c:if>
                <td nowrap style="width:30px;height:10px;padding:2px 0px 4px 5px;min-width:60px;">删除</td>
            </tr>

        </table>
    </div>

    <input type="hidden" id="pid" name="pid" value="${pid }"/>
    <input type="hidden" id="fcid" name="fcid" value="${fcid }"/>
    <input type="hidden" id="type" name="type" value="${type }"/>
</div>

</body>
<script type="text/javascript">

    function getqueryString(name) {

        var query = window.location.search.substring(1);
        var vars = query.split("&");
        for (var i = 0; i < vars.length; i++) {
            var pair = vars[i].split("=");
            if (pair[0] == name)
                return pair[1];
        }
        return null;
    }
    //    window.onload = function () {
    var pid = getqueryString("pid");
    var fcid = getqueryString("fcid");
    var type = getqueryString("type");
    var parentId= getqueryString("parentId");
    //    }

    var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
    var obj = dialog.get("objd"); //前置后置脚本获取

    //文件上传集合
    var uploadArray = new Array();
    //文件上传结果集合
    var returnArray = new Array();
    //文件上传个数（累计值）
    var completeNum = 0;
    //文件组件初始化
    var maxSize = 2 * 1024 * 1024 * 1024;
    <%--var isGlType=${isGlType};--%>
    var actionUrl;
    var missionId=$.getParameter("missionId");
    actionUrl = '${ctx}/model/instance/rangeInstantfileUpload.do?missionId=' + missionId ;

    //初始化文件上传组件
    var add = new plupload.Uploader({
        runtimes: 'flash,silverlight,html4',
        browse_button: 'add',
        url: actionUrl,
        flash_swf_url: '${ctx}/jslib/fileupload/plupload/Moxie.swf',
        silverlight_xap_url: '${ctx}/jslib/fileupload/plupload/Moxie.xap',
        unique_names: true,
        multiple_queues: true,
        //上传给后台的参数
        multipart_params: {
            fileIndex: '',
            security: '',
            describe: '',
            dimension: ''
        },
        filters: {
            max_file_size: '500mb'
            //prevent_duplicates:true
        },
        init: {
            //添加文件
            FilesAdded: function (up, files) {
                uploadArray[uploadArray.length] = add.files;
                plupload.each(files, function (file) {
                	debugger;
                    var filename = file.name;
                    if (filename.length > 5000) {
                        $.ligerDialog.warn("文件名过长，请重新选择！", "提示信息");
                        return true;
                    }
                    var index=filename.lastIndexOf(".");
                    var ext=filename.substring(index,filename.length);
                    var check=false;
                    if(ext.indexOf("pdf")>=0||ext.indexOf("xlsx")>=0||ext.indexOf("doc")>=0||ext.indexOf("excel")>=0)
                    	check=true;
                    if(!check){
                    	$.ligerDialog.warn("实例文件上传只能上传word,excel和pdf！", "提示信息");
                        return true;
                    }
                    var fileTable = document.getElementById("fileTable");
                    var currRowNum = fileTable.rows.length
                    var fileTr, fileTd;
                    fileTr = fileTable.insertRow(currRowNum);
                    fileTr.setAttribute("align", "center");
                    fileTr.setAttribute("align", "center");
                    fileTd = fileTr.insertCell(0); //序号
                    fileTd.innerHTML = currRowNum;
                    fileTd = fileTr.insertCell(1); //上传状态
                    fileTd.innerHTML = '等待';
                    fileTd = fileTr.insertCell(2); //文件名
                    fileTd.innerHTML = file.name;
                    fileTd = fileTr.insertCell(3); //密级
                    var securityHTML = '';
                    securityHTML += '<select id="security_' + currRowNum + '" style="width:100%;min-width:80px">';
                    <c:forEach items='${securityMap}' var='securityMap'>
                    securityHTML += '<option value=${securityMap.key}>${securityMap.value}</option>';
                    </c:forEach>
                    securityHTML += '</select>';
                    fileTd.innerHTML = securityHTML;
                    <c:if test='${isDisplay != true}'>
                    fileTd.style.display = "none"; //隐藏密级属性
                    </c:if>
                    fileTd = fileTr.insertCell(4); //上传人
                    fileTd.innerHTML = '<%=fullName%>';
                    /* fileTd = fileTr.insertCell(5); //描述 */
                    /* fileTd.innerHTML = '<input type="text" id="describe_' + currRowNum + '" style="width:95%"/>'; */

                    fileTd = fileTr.insertCell(5); //维度
                    var dimensionHtml = '<a href="####" onclick="javascript:selectDimension(this);" id="dimension_' + currRowNum + '"><img src="${ctx}/styles/images/next.gif" border="0"></a>';
                    dimensionHtml += '<input type="hidden" id="dimensionValue" value=""/>';
                    fileTd.innerHTML = dimensionHtml;
                    <c:if test='${useDimension != true || fileId !=0}'>
                    fileTd.style.display = "none"; //隐藏维度属性
                    </c:if>

                    fileTd = fileTr.insertCell(6); //删除
                    fileTd.innerHTML = '<a href="####" onclick="javascript:deleteFile(this);" id="deleteImg_' + currRowNum + '"><img src="${ctx}/styles/images/cancel.png" border="0"></a>';
                    //检测同名文件
                    isExistFile(fileTr);
                });

            },

            //每个文件上传调用
            FileUploaded: function (uploader, file, respoanseObject) {
                var response = $.parseJSON(respoanseObject.response);
                callback(response);

            },
            //全部完成后调用
            UploadComplete: function (uploader, files) {
                $.ligerDialog.closeWaitting();
            },

            //上传前，获取需要上传的文件列表
            BeforeUpload: function (uploader, file) {
                var num = uploader.total.uploaded;
                var fileDatas = getFileDatas();
                var fileData = fileDatas[0];
                fileData.childNodes[1].innerHTML = "上传中";
                fileData.childNodes[3].firstChild.setAttribute("disabled", "disabled");
                fileData.childNodes[5].firstChild.setAttribute("disabled", "disabled");
                var attributes = new Array();
                var fileIndex = fileData.childNodes[0].innerHTML;
                var security = $('#security_' + fileIndex).val();
                var describe = $('#describe_' + fileIndex).val();
                var dimension = "";
                if ($('#dimension_' + fileIndex).length > 0) {
                    dimension = $('#dimension_' + fileIndex).parent().find('#dimensionValue').val();
                }
                attributes[0] = fileIndex;
                attributes[1] = security;
                attributes[2] = describe;
                attributes[3] = dimension;
                //fileUploader.submitUploadForm(uploadArray[index][0], uploadArray[index][1], attributes);
                add.setOption("multipart_params", {
                    fileIndex: attributes[0],
                    security: attributes[1],
                    describe: attributes[2],
                    dimension: attributes[3]
                })
            }
        }
    });

    /**
     * 删除文件上传列表
     */
    function deleteFile(obj) {
        var delIndex = obj.id.split('_')[1];
        var fileTable = document.getElementById("fileTable");
        //如果当前文件已上传，不可以删除
        var status = fileTable.rows[delIndex].childNodes[1].innerHTML;
        if (status == '完成') {
            $.ligerDialog.warn("文件已上传成功，不可删除！", "提示信息");
            return;
        }
        //删除files中的文件
        var toremove = '';
        for (var i in add.files) {
            if (add.files[i].name == fileTable.rows[delIndex].childNodes[2].innerHTML) {
                toremove = i;
            }
        }
        add.files.splice(toremove, 1);
        fileTable.deleteRow(delIndex);
        //删除文件集合
        uploadArray[0].splice(delIndex - 1, 0);
        var copyArray = new Array();
        copyArray = uploadArray.slice(0);
        var nextId = 0;
        //重新排列序号，如果没有序号，这一步省略
        for (var index = delIndex; index < fileTable.rows.length; index++) {
            //序号列
            fileTable.rows[index].cells[0].innerHTML = index;
            nextId = parseInt(index) + 1;
            //密级列
            var security = document.getElementById("security_" + nextId);
            security.id = "security_" + index;
            //描述列
            var describe = document.getElementById("describe_" + nextId);
            describe.id = "describe_" + index;
            //维度列
            var dimension = document.getElementById("dimension_" + nextId);
            if (dimension) {
                dimension.id = "dimension_" + index;
            }
            //删除列
            var deleteImg = document.getElementById("deleteImg_" + nextId);
            deleteImg.id = "deleteImg_" + index;
        }

        //重新更新文件集合顺序
        uploadArray.splice(0, uploadArray.length);
        for (var index = 0; index < copyArray.length; index++) {
            var array = copyArray[index];
            if (array != null) {
                uploadArray.push(array);
            }
        }
    }
    ;
    /**
     * 关闭窗体返回值
     */
    function closeWindow() {
        dialog.get("sucCall")(returnArray);
        dialog.close();
    }
    ;
    /**
     * 文件上传
     */
    function uploadFile() {
        debugger;
        var result = true;
        if (obj == "" || obj == undefined || obj == null) {
            var result = true;
        } else {
            var result = obj.prescript();
        }
        //先执行前置脚本，对文件类型，大小和数量等进行定制
        if (result) {
            if (uploadArray.length > 0) {
                //获取需要上传的文件列表
                $.ligerDialog.waitting("正在上传中，请稍后...", "提示信息");
                add.start();
            } else {
                $.ligerDialog.warn("请先添加文件！", "提示信息");
            }

        } else {
            alert("上传前置脚本执行出错");
        }

    }
    ;

    /**
     * 获取待上传的文件数据
     */
    function getFileDatas() {

        var fileData = new Array();
        var fileTable = document.getElementById("fileTable");
        var fileTrs = fileTable.getElementsByTagName("tr");
        var rows = 0;
        for (var index = 1; index < fileTrs.length; index++) {
            if (fileTrs[index].childNodes[1].innerHTML == "等待") {
                fileData[rows] = fileTrs[index];
                rows++;
            }
        }
        return fileData;
    }
    ;

    /**
     * 文件上传完成后，回调函数
     */
    function callback(response) {
        debugger;
        var fileTable = document.getElementById("fileTable");
        var fileTrs = fileTable.getElementsByTagName("tr");
//		var responseText = $.parseJSON(response);
        var responseText = response;
        var fileName = responseText.fileName;
        var fileIndex = responseText.fileIndex;
        if (responseText.success == "true") {
            fileTrs[1].childNodes[1].innerHTML = "完成";
            /*  //存储返回结果
             returnArray.push(responseText); */
            completeNum++;
            if (completeNum == uploadArray.length) {
                $.ligerDialog.confirm("文件上传成功！是否继续操作", "提示信息", function (rtn) {
                    if (rtn) {
                        //清空当前上传数据
                        uploadArray.splice(0, uploadArray.length);
                    } else {
                        dialog.get("sucCall")(returnArray);
                        dialog.close();
                    }
                });
                if (obj == "" || obj == undefined || obj == null) {
                    var aft = true;
                } else {
                    var aft = obj.afterscript(responseText);//执行后置方法(文件上传完可做的操作)
                }
                if (!aft) {
                    alert("上传后置脚本执行出错");
                }

            }
        } else {//文件过大报错
            /* var fileSize = responseText.fileSize;
            if (!fileSize && fileSize != undefined) {
                $.ligerDialog.warn("[" + fileName + "]文件超过" + maxSize / 1024 / 1024 + "M", "提示信息");
            } else {
                $.ligerDialog.warn("[" + fileName + "]上传文件失败，请联系系统管理员！", "提示信息");
            }

            fileTrs[fileIndex].childNodes[1].innerHTML = '<span style="color: red;">错误</span>'; */

            $.ligerDialog.warn(response.context, "提示信息");
        }
    }
    ;
    /**
     * 检查当前添加文件是否存在同名
     */
    function isExistFile(fileTr) {

        var fileId = fileTr.childNodes[0].innerHTML;
        var fileName = fileTr.childNodes[2].innerHTML;

        var fileTable = document.getElementById("fileTable");
        var fileTrs = fileTable.getElementsByTagName("tr");

        for (var i = 1; i < fileTrs.length; i++) {
            var id = fileTrs[i].childNodes[0].innerHTML;
            var name = fileTrs[i].childNodes[2].innerHTML;
            if (name == fileName && id != fileId) {
                $.ligerDialog.warn("列表中存在同名文件！", "消息提示");
                //删除files中的文件
                var toremove = '';
                for (var i in add.files) {
                    if (add.files[i].name == fileTable.rows[fileTr.rowIndex].childNodes[2].innerHTML) {
                        toremove = i;
                    }
                }
                add.files.splice(toremove, 1);
                fileTable.deleteRow(fileTr.rowIndex);
                //删除文件集合
                uploadArray.splice(-1, 1, null);
                //去掉空值
                var copyArray = new Array();
                copyArray = uploadArray.slice(0);
                //重新更新文件集合顺序
                uploadArray.splice(0, uploadArray.length);
                for (var index = 0; index < copyArray.length; index++) {
                    var array = copyArray[index];
                    if (array != null) {
                        uploadArray.push(array);
                    }
                }
                return;
            }
        }
    }
    ;

    //组件初始化
    add.init();

</script>
</html>
