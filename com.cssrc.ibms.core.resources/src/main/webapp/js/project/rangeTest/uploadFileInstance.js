/*
* 靶场和所检上传文件实例表单值补充
* */

$(function () {
    var missionId=$.getParameter("missionId");
    $("input[name='m:tb_instant:planId']").val(missionId);
    $("input[name='m:tb_instant:status']").val("正在使用");
    $("input[name='m:tb_instant:bdzl']").val("17");

})