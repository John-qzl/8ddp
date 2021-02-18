/**
 * 产品验收策划-列表
 */

// 1.新增定制
function newAdd(obj){
	var batchId = $.getParameter("batchId");
	var batchName = $.getParameter("batchName");
	var action = $(obj).attr("action");
	action += "&batchId="+batchId;
	action += "&batchName="+batchName;
	action += "&handleType=add"
	$(obj).attr("action", action);
	openLinkDialog({
		scope : obj,
		width : 1200,
		height : 800,
		isFull : false,
		title : '验收策划'
	})
}
//$(function(){
//	var batchId = $.getParameter("batchId");
//	var del=$('.del');
//	var run=$('.run');
//	var url= __ctx+"/model/user/role/getAcceptanceRole.do";
//	$.ajax({
//		'url':url,
//		'data':{
//			'batchId':batchId,
//		},
//		type: "POST",
//		 async:true,
//		 success:function(data){
//	    	if(data.useRole=="1"){
//	    		del.attr("style","visibility:visible");
//	    		run.attr("style","visibility:visible");
//	    	}  
//	    	
//	     }
//	});
//});
// 2.启动流程定制--（改为直接启动）
function newStart(obj){
	debugger;
	var batchId = $.getParameter("batchId");
	var batchName = $.getParameter("batchName");
	var action = $(obj).attr("action");
	action += "&batchId="+batchId;
	action += "&batchName="+batchName;
	action += "&handleType=start"
	$(obj).attr("action", action);
	openLinkDialog({
		keyName:'2_start',
		scope : obj,
		width : 1000,
		height : 800,
		isFull : false,
		isStart:false,
		isResize: true,
		showMax: true
	})
}

// 3.产品验收策划（表单）明细定制
function acceptancePlanDetail(acceptancePlanId){
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
function topDelete(ids){
	debugger;
	var url=__ctx+"/product/acceptance/plan/topDelete.do";
	$.ligerDialog.confirm('确认删除吗？','提示信息',function(rtn) {
		if(rtn) {
			$.ajax({
				data:{
					'Ids':ids,
				},
				type: "POST",
			      url:url,
			      async:false,
			      success:function(data){
			    	  if(data.message=="0"){
			    		  $.ligerDialog.warn("您当前没有权限无法删除！","",function(){
							});
			    	  }
			    	  else{
			    		  $.ligerDialog.success("删除成功！","",function(){
				    		  window.location.reload();
							});
			    	  }
			      }   		  
			});
			
		}
	});
}

// 4.下发依据文件定制
function filesAssign(acceptancePlanId){
	// 页面初始化
	$.ajax({
		url : __ctx
				+ "/product/acceptance/plan/filesAssign.do?acceptancePlanId="
				+ acceptancePlanId,
		async : false,
		success : function(data) {
			$.ligerDialog.success("策划依据文件下发成功！","",function(){
				location.href = window.location.href.getNewUrl();
			});
		}
	})
}
