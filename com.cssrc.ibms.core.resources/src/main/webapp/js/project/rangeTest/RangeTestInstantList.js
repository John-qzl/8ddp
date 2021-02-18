/**
 * 靶场试验表单实例 按钮点击事件重写
 */
//excel上传
function uploadFile() {
    var missionId = $.getParameter("missionId");

    var url = __ctx + "/mission/rangeTestInstantFileUpload.do?missionId=" + missionId;
    debugger;
    DialogUtil.open({
        url: url,
        height: 300,
        width: 500,
        title: "数据Excel导入",
        isResize: true,
        sucCall: function (rtn) {
            location.href = window.location.href.getNewUrl();
        }
    });
}

//废弃
function depleted(id) {
    var missionId = $.getParameter("missionId");
    $.ligerDialog.confirm("废弃之后的数据，如需再次使用请联系管理员，确认废弃吗？", "提示信息", function (rtn) {
        if (rtn) {
            $.ajax({
                url: __ctx + "/model/instance/scrapRangeTestInstanceById.do?missionId=" + missionId + "&instantId=" + id,
                async: false,
                success: function (data) {
                    if (data == "1") {
                        $.ligerDialog.warn("报告正在审批或已完成，无法进行废弃！", '提示信息');

                    } else {
                        location.href = window.location.href.getNewUrl();
                    }
                }
            })
        }
    });
}
$(function(){
	var missionId = $.getParameter("missionId");
	var dataExport=$('.export');
	var edit=$('.edit');
	var url= __ctx+"/model/user/role/getRoleByRangeId.do";
	$.ajax({
		'url':url,
		'data':{
			'missionId':missionId,
		},
		type: "POST",
		 async:true,
		 success:function(data){
	    	if(data.useRole=="1"){
	    		dataExport.attr("style","visibility:visible");
	    		edit.attr("style","visibility:visible");
	    	}  
	     }
	});
});
//excel上传
function dataImport() {
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

//上传实例文件
function uploadFileInstance(){
    var missionId = $.getParameter("missionId");
    $.ajax({
        url: __ctx + "/model/instance/scrapRangeTestInstanceById.do?missionId=" + missionId + "&instantId=" + "",
        async: false,
        success: function (data) {
            if (data == "1") {
                $.ligerDialog.warn("报告正在审批或已完成，无法进行废弃！", '提示信息');

            } else {
                url = __ctx + "/mission/bcInstantFileUpload.do?missionId=" + missionId;
                debugger;
                DialogUtil.open({
                    url: url,
                    height: 300,
                    width: 500,
                    title: "数据Excel导入",
                    isResize: true,
                    sucCall: function (rtn) {
                        location.href = window.location.href.getNewUrl();
                    }
                });
            }
        }
    })
    
}

//下载文件
function downloadFile(file) {
    var path=$(file).attr("path");
    window.location.href = path;//下载文件
    updateService.update();
}