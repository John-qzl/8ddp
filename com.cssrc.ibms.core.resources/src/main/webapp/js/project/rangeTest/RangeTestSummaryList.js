/**
 * 靶场试验总结-列表
 */
//2.启动流程定制--（改为直接启动）
function newStart(obj){
    var action = $(obj).attr("action");
    var missionId = $.getParameter("acceptancePlanId");
    //检查是否有回传数据
    $.ajax({
        url: __ctx + "/RangeTest/mission/report/countRangeTestInstances.do?missionId=" + missionId,
        async: false,
        success: function (data) {
            if (data == "0") {
                $.ligerDialog.warn("当前策划尚未回传任何表单！", '提示信息');
                return;
            }else {
                //检查是否已有报告
                $.ajax({
                    url: __ctx+"/RangeTest/mission/report/getRangeTestReport.do?missionId="+missionId,
                    async:true,
                    success:function(data){
                        if(data=="0"){
                            $.ligerDialog.warn("当前策划下已有报告！",'提示信息');
                            return;
                        }
                        //靶场试验不需要试验报告
                        /*else if(data=="2"){
                            $.ligerDialog.warn("当前未回传试验报告！",'提示信息');
                            return;
                        }*/
                        action += "&missionId="+missionId;
                        action += "&handleType=start"
                        $(obj).attr("action", action);
                        openLinkDialog({
                            keyName:'2_start',
                            scope : obj,
                            width : 1200,
                            height :800,
                            isFull : false,
                            isStart:false,
                            showMax:true
                        })
                    }
                })
            }
        }
    });
}
function updateReport(acceptanceReportId){
    debugger;
    $.ajax({
        url: __ctx+"/product/acceptance/report/updateReport.do?acceptanceReportId="+acceptanceReportId,
        async:true,
        success:function(data){
            $.ligerDialog.success("报告更新成功","",function(){
                location.href = window.location.href.getNewUrl();
            });
        }
    })
}


$(function(){
	var acceptancePlanId = $.getParameter("acceptancePlanId");
	var run=$('.run');
	var url= __ctx+"/model/user/role/getRoleByRangeId.do";
	$.ajax({
		'url':url,
		'data':{
			'missionId':acceptancePlanId,
		},
		type: "POST",
		 async:true,
		 success:function(data){
	    	if(data.useRole=="1"){
	    		run.attr("style","visibility:visible");
	    	}  
	     }
	});
});


function newAdd(obj){
    //相对应于产品验收是批次id和name,靶场试验应该要的是型号id和名称
/*    var batchId = $.getParameter("batchId");
    var batchName = $.getParameter("batchName");*/
    var missionId = $.getParameter("acceptancePlanId");
    var action = $(obj).attr("action");
    $.ajax({
        url: __ctx+"/RangeTest/mission/report/getRangeTestReport.do?missionId="+missionId,
        async:false,
        success:function(data){
            if(data=="1"){
                $.ligerDialog.warn("当前策划下已有报告！",'提示信息');
            }
            else if(data=="2"){
                console.log("1");
                debugger;
                $.ligerDialog.warn("当前未回传验收报告！",'提示信息');
            }
        }
    })
    console.log("2");
/*    action += "&batchId="+batchId;
    action += "&batchName="+batchName;*/
    action += "&missionId="+missionId;
    action += "&handleType=add"
    $(obj).attr("action", action);
    openLinkDialog({
        scope : obj,
        width : 1200,
        height : 800,
        isFull : false,
        title : '靶场试验总结'
    })
}
//3.靶场试验策划（表单）明细定制
function acceptanceSummaryDetail(missionId){
    var __displayId__ = $.getParameter("__displayId__");

    DialogUtil.open({
        url:__ctx+"/oa/form/dataTemplate/detailData.do?__displayId__="+__displayId__+"&__pk__="+missionId,
        height: 600,
        width: 1000,
        title:"数据确认明细",
        isResize: true,
        sucCall:function(rtn){
            //reFresh() ;
        }
    });
}
