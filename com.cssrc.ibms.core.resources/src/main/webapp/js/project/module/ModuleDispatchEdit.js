/**
 * 型号发次新增/编辑JS
 */
$(function(){
	// 操作类型
	var handleType = $.getParameter("handleType");
	// 型号id
	var moduleId = $.getParameter("moduleId");
	// 型号阶段id
	var modulePeriodId = $.getParameter("modulePeriodId");
	// 型号批次id
	var moduleBatchId = $.getParameter("moduleBatchId");
	
	if(handleType=="add"){
		// 页面初始化
		$.ajax({
		    url: __ctx+"/module/batch/getBatchPeriodModule.do?moduleBatchId="+moduleBatchId,
		    async:false,
		    success:function(data){
		    	if(data.success){
		    		// 所属型号
		    		$("input[name='m:xhfcb:ssxh']").attr("value", data.MODULEID);
		    		$("input[name='m:xhfcb:xhdh']").attr("value", data.MODULEKEY);
		    		$("input[name='m:xhfcb:xhdh']").attr("disabled", true);
		    		// 所属型号阶段
		    		$("input[name='m:xhfcb:ssxhjd']").attr("value", modulePeriodId);
		    		// 所属型号批次
		    		$("input[name='m:xhfcb:ssxhpc']").attr("value", data.BATCHID);
		    		$("input[name='m:xhfcb:pcdh']").attr("value", data.BATCHKEY);
		    		$("input[name='m:xhfcb:pcdh']").attr("disabled", true);
		    		$("input[name='m:xhfcb:pcmc']").attr("value", data.BATCHNAME);
		    		$("input[name='m:xhfcb:pcmc']").attr("disabled", true);
		    	}	
		    }
		 })
	}else if(handleType=="edit"){
		// 编辑
		$("input[name='m:xhfcb:xhdh']").attr("disabled", true);
		$("input[name='m:xhfcb:pcdh']").attr("disabled", true);
	}
	
})
