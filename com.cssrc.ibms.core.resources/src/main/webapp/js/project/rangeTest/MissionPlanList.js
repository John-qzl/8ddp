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
$(function(){
	debugger;
	var moduleId = $.getParameter("moduleId");
	var del=$('.del');
	var run=$('.run');
	var edit=$('.edit');
	var url= __ctx+"/model/user/role/getUseRole.do";
	$.ajax({
		'url':url,
		'data':{
			'moduleId':moduleId,
		},
		type: "POST",
		 async:true,
		 success:function(data){
	    	if(data.useRole=="1"){
	    		del.attr("style","visibility:visible");
	    		run.attr("style","visibility:visible");
	    		edit.attr("style","visibility:visible");
	    	}  
	    	
	     }
	});
});

//重写删除  可以删除草稿箱  zmz觉得只会传来id而不是ids,但是没有时间给zmz去验证了
function topDelete(ids){
	debugger;
	var url=__ctx+"/rangeTest/mission/plan/topDelete.do";
	//所属模块
	var ofPart="";
	var displayId=$.getParameter("__displayId__");
	//武器所检的
	if (displayId=="10000031900580"){
		ofPart="WQSJ";
	}else {
		//靶场试验的
		ofPart="BCSY";
	}
	$.ligerDialog.confirm('确认删除吗？','提示信息',function(rtn) {
		if(rtn) {
			$.ajax({
				data:{
					'Ids':ids,
					'ofPart':ofPart
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