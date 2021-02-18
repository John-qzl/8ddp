/**
 * 签章分配页面的图片请求
 */
$(function() {
    var ctx=$.getParameter("ctx");
    var fileId=$("input[name='m:PADhcqzb:qzid']").val();
    if (fileId==""){
        //新增页面
        $("#PADUserName").hide();
        $("#uploadSignModelA").attr("onclick","uploadSignModel()");
        $("#pic").attr("src","/dp/styles/images/default_sign_model.png");
    }else {
        //分配页面
        $("#uploadSignModelTr").hide();
        $("#pic").attr("src",ctx+"/oa/system/sysFile/getFileById.do?fileId="+fileId);
    }



})

//网页上传签章
function uploadSignModel(){
    var url = __ctx + '/signModel/uploadSignModel.do';
    DialogUtil.open({
        height: 500,
        width: 800,
        url: url,
        title: "上传签章",
        sucCall: function (rtn) {
            debugger;
            var fileId=rtn[0].fileId;
            $("#pic").attr("src","/dp/oa/system/sysFile/getFileById.do?fileId="+fileId);
            $("input[name='m:PADhcqzb:qzid']").val(fileId);
        }
    });
}