/**
 * 靶场试验 归档  按钮事件重写
 * @param missionId
 */

function printPDF(missionId){
    $.ligerDialog.waitting("正在生成PDF...", "提示信息");
    $.ajax({
        url: __ctx+"/rangeTest/mission/plan/printPDF.do?missionId="+missionId,
        async:true,
        data:{"missionId":missionId},
        success:function(data){
            debugger;
            $.ligerDialog.closeWaitting();
            if(data.status=="1"){
                $.ligerDialog.success(data.msg,"提示信息",function(){
                    location.href = window.location.href.getNewUrl();
                });
            }else{
                $.ligerDialog.warn(data.msg,'提示信息');
            }

        }
    });
}