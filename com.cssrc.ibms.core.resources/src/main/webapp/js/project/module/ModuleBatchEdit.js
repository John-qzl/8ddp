/**
 * 型号批次新增/编辑JS
 */
$(function(){
	// 操作类型
	var handleType = $.getParameter("handleType");
	// 型号id
	var moduleId = $.getParameter("moduleId");
	// 型号阶段id
	var modulePeriodId = $.getParameter("modulePeriodId");
	
	if(handleType=="add"){
		// 页面初始化
		$.ajax({
		    url: __ctx+"/module/period/getPeriodAndModule.do?modulePeriodId="+modulePeriodId,
		    async:false,
		    success:function(data){
		    	if(data.success){
		    		// 所属型号
		    		$("input[name='m:xhpcb:ssxh']").attr("value", data.MODULEID);
		    		$("input[name='m:xhpcb:xhmc']").attr("value", data.MODULENAME);
		    		$("input[name='m:xhpcb:xhmc']").attr("disabled", true);
		    		$("input[name='m:xhpcb:xhzs']").attr("value", data.XHZS);
		    		$("input[name='m:xhpcb:xhzs']").attr("disabled", true);
		    		$("input[name='m:xhpcb:xhzsID']").attr("value", data.XHZSID);
		    		$("input[name='m:xhpcb:xhzzh']").attr("value", data.XHZZH);
		    		$("input[name='m:xhpcb:xhzzh']").attr("disabled", true);
		    		$("input[name='m:xhpcb:xhzzhID']").attr("value", data.XHZZHID);
		    		// 所属型号阶段
		    		$("input[name='m:xhpcb:ssxhjd']").attr("value", modulePeriodId);
		    	}	
		    }
		 })
	}
	
})
