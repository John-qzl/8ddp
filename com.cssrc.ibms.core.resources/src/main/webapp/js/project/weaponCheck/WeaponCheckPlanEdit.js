/**
 * 武器所检策划新增/编辑JS
 */
$(function () {

    //为excel上传组员信息绑定事件
    $("#importTeamFromExcel").on("click", function () {
        var url = __ctx + "/weaponCheck/importTeamForWeaponCheckPlan.do";
        title = 'excel导入组员信息';
        url = url.getNewUrl();
        url = encodeURI(url);

        DialogUtil.open({
            height: 600,
            width: 900,
            title: title,
            url: url,
            isResize: true,
            //自定义参数?
            sucCall: function (rtn) {
                showTeam(rtn[0]);
            }
        });
    });

    // 操作类型
    var handleType = $.getParameter("handleType");
    // 型号id
    var moduleId = $.getParameter("moduleId");
    var moduleCode = $.getParameter("moduleCode");
    moduleCode=decodeURI(decodeURI(moduleCode));  //对可能出现的中文乱码进行解码

    // 原为先新增后启动，现改为直接发起流程审批
    if (handleType == "add" || handleType == "start") {
        //     if (handleType == "add") {

        //预填值
        $("input[name='m:bcrwchbgb:xhID']").attr("value", moduleId);
        $("input[name='m:bcrwchbgb:xhdh']").attr("value", moduleCode);
        //总结流程通过之后会过来改策划的这个归档状态为未生成pdf
        //归档页面是用未归档来过滤审批通过但是没有进行数据确认的记录
        $("input[name='m:bcrwchbgb:gdlczt']").val("未归档");

        $("input[name='m:bcrwchbgb:spzt']").attr("value", "审批中");
        //       } else if (handleType == "start") {

       // $("input[name='m:bcrwchbgb:gdlczt']").attr("value", "未生成");
        //填充策划报告编号
        var date = new Date();
        var year = date.getFullYear();
        var lastNextNumber="";
        $.ajax({
            url:__ctx+"/weapon/check/plan/getNextWeaponCheckPlanNumber.do",
            type:'post',
            data:{
            	'xhId':moduleId
            },
            dataType:"json",
            async:false,
            success:function(result){
                console.log("请求成功");
                console.log(result);
                lastNextNumber=result;
            },
            error:function () {
                console.log("ajax请求失败");
            }
        })
        var theHoleCode=moduleCode+lastNextNumber;
        $("input[name='m:bcrwchbgb:chbgbbh']").val(theHoleCode);
        //		这个用脚本做了
        //		$("input[name='m:bcrwchbgb:spzt']").attr("value", "审批中");

        //      }

    }else {
        //
        //如果不是填写表单
        //隐藏excel导入组员选项
        //时代变了,只有在组长那个节点才显示组员表单,这里移交给其他地方控制了
        //$("#importTeamFromExcel").remove();
    }


    //如果不是组长节点
    //隐藏文件上传
    if(typeof(taskDefinitionKey) != "undefined"){
        //要求:流程中一直可以显示组员添加.只是在查看信息时候不显示
        $("#addButton").attr("style","text-align:left;font-size:18px");
    	  if (taskDefinitionKey!="UserTask2"){
    	        $("#ysyjwjName").attr("style","display:none");
    	        $("#ysyjwj").hide();
    	  //      $("#testTeam").hide();

    	    }else {
    	        //如果是组长节点,则显示添加组员按钮
    	   //     $("#addButton").attr("style","text-align:left;font-size:18px");
    	    }
    }
  
})
//选择组员自动回填单位
$(document).on('change','#childTable .hidden',function(e){

    //获取input输入的值
    var userId = $(this).val();
    var parentElements=this.parentNode.parentNode;
    var brotherElements=parentElements.nextElementSibling;
    var url= __ctx+"/dataPackage/tree/ptree/getOrgByUserId.do";
    $.ajax({
        url:url,
        data:{
            'userId':userId
        },
        type:'post',
        async : false,
        success : function(data) {
            if(data!=null&&data!=""){
                var s1=brotherElements.firstChild;
                var s2=s1.firstChild;
                s2.value=data.orgId;
                var s3=s2.nextElementSibling;
                s3.value=data.orgName;
            }
        }

    });
});


function showTeam(data) {
    var html="";
    for(var i=0;i<data.length;i++){
        var detailedData=data[i];
        html+='<tr class="listRow" formtype="edit"> ';

        html+='<td tname="r:r:cpyszb:zw">';
        html+='<input name="r:cpyszb:zw"  type="hidden"  value='+'"'+detailedData.gw+'"'+'>';
        html+='<span>'+detailedData.gw;
        html+='</span></td>';
        html+='<td tname="r:r:cpyszb:xm">';
        html+='<input name="r:cpyszb:xmID" type="hidden" value='+'"'+detailedData.userId+'"'+'>';
        html+='<input name="r:cpyszb:xm" type="hidden" value='+'"'+detailedData.xm+'"'+'>';
        html+='<span>'+detailedData.xm;
        html+='</span></td>';

        html+='<td tname="r:r:cpyszb:dw">';
        html+='<input name="r:cpyszb:dwID" type="hidden" value='+'"'+detailedData.dwId+'"'+'>';
        html+='<input name="r:cpyszb:dw" type="hidden" value='+'"'+detailedData.dw+'"'+'>';
        html+='<span>'+detailedData.dw;
        html+='</span></td>';
        if (detailedData.fzxm){
            //如果负责项目在excel中有值，则添加原有值
            html+='<td tname="r:r:cpyszb:fzxm">';
            html+='<input name="r:cpyszb:fzxm" type="hidden"  value='+'"'+detailedData.fzxm+'"'+'>';
            html+='<span>'+detailedData.fzxm;
            html+='</span></td>';
        }else {
            //如果excel没有负责项目，补一个input
            html+='<td tname="r:r:cpyszb:fzxm">';
            html+='<input name="r:cpyszb:fzxm" isunique="0" type="text" lablename="负责项目" class="inputText input-dhwbk tableHighLight" validate="{maxlength:100}" isflag="tableflag" value="" right="w">';
        }

        html+='<td class="trManage"><a class="link moveup" onclick="CustomForm.moveTr(this,true,\'reltable\')" href="####">上移</a>&nbsp;<a class="link movedown" onclick="CustomForm.moveTr(this,false,\'reltable\')" href="####">下移</a>&nbsp;<a class="link del" href="####">删除</a>&nbsp;<br></td>';

        html+='</tr>';
    }
    $("#context").append(html);
}


