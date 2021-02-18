/**
 * 靶场任务策
 */

// 1.新增定制(废弃,改为直接启动
function newAdd(obj){
    var moduleId = $.getParameter("moduleId");
    var moduleCode=$.getParameter("moduleCode");
    var action = $(obj).attr("action");
    action += "&moduleId="+moduleId;
    action += "&moduleCode="+moduleCode;
    action += "&handleType=add"
    $(obj).attr("action", action);
    openLinkDialog({
        scope : obj,
        width : 1200,
        height : 800,
        isFull : false,
        title : '试验策划'
    })
}
//3.靶场策划（表单）明细定制
function acceptancePlanDetail(acceptancePlanId){
    var __displayId__ = $.getParameter("__displayId__");
    DialogUtil.open({
        url:__ctx+"/oa/form/dataTemplate/detailData.do?__displayId__="+__displayId__+"&__pk__="+acceptancePlanId,
        height: 600,
        width: 1000,
        title:"靶场策划明细",
        isResize: true,
        sucCall:function(rtn){
            //reFresh() ;
        }
    });
}
function newStart(obj) {
    var moduleId = $.getParameter("moduleId");
    var moduleCode=$.getParameter("moduleCode");
    var action = $(obj).attr("action");
    action += "&moduleId="+moduleId;
    action += "&moduleCode="+moduleCode;
    action += "&handleType=start"
    $(obj).attr("action", action);
    openLinkDialog({
        scope : obj,
        width : 1200,
        height : 800,
        isFull : false,
        title : '试验策划'
    })
}