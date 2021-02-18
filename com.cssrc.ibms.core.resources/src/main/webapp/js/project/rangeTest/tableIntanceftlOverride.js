function uploadFile() {
    var missionId=$.getParameter("missionId");
    var url=__ctx+"/WEB-INF/view/misssion/instantFileUpload.jsp?missionId="+missionId;
    debugger;
    DialogUtil.open({
        url:url,
        height: 300,
        width: 500,
        title: "数据Excel导入",
        isResize: true,
        sucCall: function (rtn) {
            location.href = window.location.href.getNewUrl();
        }
    });
}
function depleted(id){
    debugger;
    var missionId=$.getParameter("missionId");
    $.ligerDialog.confirm("废弃之后的数据，如需再次使用请联系管理员，确认废弃吗？","提示信息", function(rtn) {
        if(rtn){
            $.ajax({
                url: __ctx+"/model/instance/scrapRangeTestInstanceById.do?missionId="+missionId+"&instantId="+id,
                async:false,
                success:function(data){
                    if(data=="1"){
                        $.ligerDialog.warn("报告正在审批或已完成，无法进行废弃！",'提示信息');

                    }else{
                        location.href = window.location.href.getNewUrl();
                    }
                }
            })
        }
    });
}