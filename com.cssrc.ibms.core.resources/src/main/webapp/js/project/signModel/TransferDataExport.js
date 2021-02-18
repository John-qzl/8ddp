/*
$(function (){
    //解绑默认点击事件
    $("div.group > a.link.del").unbind('click');
    //绑定自定义点击事件
    $("div.group > a.link.del").on("click",deleteChecked());
});*/
function dataExport(){
    var records = [];
    var ids = getSelectIds(true,false);
    if(!ids) return;
    $.ligerDialog.waitting("导出中...", "提示信息");
    var url = __ctx + "/dataPackage/tree/ptree/exportPADSignModelToZip.do?";
    $.get(url, {signModelIds: ids}, function (responseText) {
        debugger;
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
            updateService.update();
        } else {
            $.ligerDialog.err("提示信息", "", obj.getMessage());
        }
    });
}

/**
 * 获取选中的数据的id
 * @param isAlert
 * @param isSingle
 */
// 获取列表选中数据id数组字符串
function getSelectIds(isAlert,isSingle){
    var idArr = [];
    $('input.pk:checked').each(function(){
        idArr.push($(this).val());
    })
    if(isAlert){
        if(idArr.length==0){
            $.ligerDialog.warn("请选择数据！");
            return false;
        }
        if(idArr.length!=1&&isSingle){
            $.ligerDialog.warn("请选择一条数据！");
            return false;
        }
        return idArr.toString();
    }else{
        return idArr.toString();
    }
}

//数据导入
function dataImport(){
    var url = __ctx + '/signModel/importSignModelFromZip.do';
    DialogUtil.open({
        height: 500,
        width: 800,
        url: url,
        title: "导入签章",
        sucCall: function (rtn) {
            window.location.reload(true);
        }
    });
}


/**
 * 签章批量删除功能
 * $("div.group > a.link.del").unbind('click')  绑定的点击事件解绑
 */
function deleteChecked() {
    debugger;
    var records = [];
    var ids = getSelectIds(true, false);
    if (!ids) return;
    $.ligerDialog.confirm("此操作将使选中的用户恢复至默认签章，是否继续？", '提示信息', function (rtn) {
        if (rtn) {
            $.ajax({
                url: __ctx + "/signModel/mission/plan/DeleteSyncToSignModel.do",
                data: {
                    signModelIds: ids,
                },
                success: function () {
                    window.location.reload(true)
                }
            })
        }
    });
}

/*    var records = [];
    var ids = getSelectIds(true,false);
    if(!ids) return;
    $.ligerDialog.warning("此操作将使选中的用户恢复至默认签章，是否继续？", "提示信息");*/
/*
    var url = __ctx + "/dataPackage/tree/ptree/exportPADSignModelToZip.do?";
    $.get(url, {signModelIds: ids}, function (responseText) {
        debugger;
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
            updateService.update();
        } else {
            $.ligerDialog.err("提示信息", "", obj.getMessage());
        }
    });
*/

//网页上传签章
function uploadSignModel(){
    var url = __ctx + '/signModel/uploadSignModel.do';
    DialogUtil.open({
        height: 500,
        width: 800,
        url: url,
        title: "上传签章",
        sucCall: function (rtn) {

            window.location.reload(true);
        }
    });
}