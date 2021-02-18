/**
 * 产品验收总结-列表
 */

function newAdd(obj){
	var batchId = $.getParameter("batchId");
	var batchName = $.getParameter("batchName");
	var acceptancePlanId = $.getParameter("acceptancePlanId");
	var action = $(obj).attr("action");
	$.ajax({
		url: __ctx+"/product/acceptance/report/getAcceptanceReport.do?acceptancePlanId="+acceptancePlanId,
		async:true,	
		  success:function(data){
			if(data!="0"){
				$.ligerDialog.warn("当前策划下已有报告！",'提示信息');
				return;
			}
		  }
	   })
	action += "&batchId="+batchId;
	action += "&batchName="+batchName;
	action += "&acceptancePlanId="+acceptancePlanId;
	action += "&handleType=add"
	$(obj).attr("action", action);
	openLinkDialog({
		scope : obj,
		width : 1200,
		height : 800,
		isFull : false,
		title : '验收总结'
	})
}
//3.产品验收策划（表单）明细定制
function acceptanceSummaryDetail(acceptancePlanId){
	var __displayId__ = $.getParameter("__displayId__");
	
	DialogUtil.open({
		url:__ctx+"/oa/form/dataTemplate/detailData.do?__displayId__="+__displayId__+"&__pk__="+acceptancePlanId,
		height: 600,
		width: 1000,
		title:"验收策划明细",
		isResize: true,
		sucCall:function(rtn){	
			 //reFresh() ;
		}
	});
}
