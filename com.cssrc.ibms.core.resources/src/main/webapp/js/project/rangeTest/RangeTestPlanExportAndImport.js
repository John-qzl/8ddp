/**
 * 靶场试验 - 全部任务 - 数据导出 的模板js
 * 场景:pad已经采集完了数据,现在要在中转机把数据打包为zip传递给服务器
 * note by zmz
 * 20200907
 */

function dataExport(){
    var records = [];
    var ids = getSelectIds(true,false);
    if(!ids) return;
    $.ligerDialog.waitting("导出中...", "提示信息");
    var url = __ctx + "/dataPackage/tree/ptree/ExportRangeTestPackages.do?";
    $.get(url, {missionId: ids}, function (responseText) {
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
$(function(){
	debugger;
	var xhId = $.getParameter("xhId");
	var dataEx=$('.dataEx');
	var dataImport=$('.dataImport');

	var url= __ctx+"/model/user/role/getUseRole.do";
	$.ajax({
		'url':url,
		'data':{
			'moduleId':xhId,
		},
		type: "POST",
		 async:true,
		 success:function(data){
	    	if(data.useRole=="1"){
	    		dataEx.attr("style","visibility:visible");
	    		dataImport.attr("style","visibility:visible");
	    	
	    	}  
	    	
	     }
	});
});
function dataImport(){
    var url = __ctx + '/io/rangeTestPlanDataImport.do';
    DialogUtil.open({
        height: 500,
        width: 800,
        url: url,
        title: "数据包导入",
        sucCall: function (rtn) {
            reFresh()
        }
    });
}

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