/**
 * 武器所检的下发表单的自动填充js
 */
var xhId;
$(function() {
    // 定制初始化
    //从靶场把这个复制过来的时候没有做相应的改变
    customInit();
    // 操作类型
    var handleType = $.getParameter("handleType");
    // 验收策划id
    var acceptancePlanId = $.getParameter("acceptancePlanId");
    //初始化型号id
    getXhid(acceptancePlanId);
    if (handleType == "add") {
        $("input[name='m:dataPackageInfo:acceptancePlanId']").attr("value",
            acceptancePlanId);
        $("input[name='m:dataPackageInfo:zxzt']").attr("value",
            "未开始");
    }
})
//获取型号id
//	因为靶场实验没有产品表，策划直接隶属于型号
function getXhid(acceptancePlanId) {
    url=__ctx+"/rangeTest/mission/plan/getModuleidByPlanid.do";

    $.ajax({
        type: "POST",
        url:url,
        data:{
            'acceptancePlanId' :acceptancePlanId,
        },
        dataType : "json",
        success:function(result){
            console.log("ajax成功!")
            xhId=result[0].xhid;
        },
        error: function(XMLHttpRequest, textStatus, errorThrown){
            console.log("ajax失败!");
            alert(XMLHttpRequest.readyState + XMLHttpRequest.status + XMLHttpRequest.responseText);
        }
    });
}

function customInit() {

    // 选择产品模板
    $('a.selectTemplate').click(function() {
        var url = __ctx + '/template/manage/missionTemplateSelectView.do?';
        url += 'xhId=' + xhId;
        debugger;
        DialogUtil.open({
            height : 500,
            width : 400,
            url : url,
            showMax : true, //是否显示最大化按钮
            showToggle : false, //窗口收缩折叠
            title : "产品模板选择器",
            showMin : false,
            sucCall : function(rtn) {
                $("input[name='m:dataPackageInfo:templateId']").val(rtn.ids);
                $("input[name='m:dataPackageInfo:templateName']").val(rtn.names);
                //数据名称的同步添加
                $("input[name='m:dataPackageInfo:sjmc']").val(rtn.names);

            }
        });
    })

    // 重置选择模板
    $('a.resetTemplate').click(function() {
        $('[name="m:dataPackageInfo:sxmbID"]').val("");
        $('[name="m:dataPackageInfo:sxmbmc"]').val("");
    })
    function check(){
    }
}
