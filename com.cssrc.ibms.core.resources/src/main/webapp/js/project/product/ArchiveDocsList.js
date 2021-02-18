//生成产品验收的附件压缩包
function archiveAttach(acceptancePlanId){
    $.ligerDialog.waitting("正在整合附件...", "提示信息");
    $.ajax({
        url: __ctx+"/product/acceptance/plan/saveAsArchiveForCPYS.do",
        async:true,
        data:{"acceptancePlanId":acceptancePlanId},
        success:function(data){
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

//生成靶场试验的附件压缩包
function archiveAttachForBCSY(acceptancePlanId){
    $.ligerDialog.waitting("正在整合附件...", "提示信息");
    $.ajax({
        url: __ctx+"/rangeTest/mission/plan/saveAsArchiveForBCSY.do",
        async:true,
        data:{"acceptancePlanId":acceptancePlanId},
        success:function(data){
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