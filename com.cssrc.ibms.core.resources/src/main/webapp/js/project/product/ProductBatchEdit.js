/**
 * 产品批次新增/编辑JS
 */
$(function(){
	// 操作类型
	var handleType = $.getParameter("handleType");
	// 型号id
	var moduleId = $.getParameter("moduleId");
	// 产品类别id
	var categoryId = $.getParameter("categoryId");
	if(handleType=="add"){
		$("input[name='m:cplbpcb:ssxh']").attr("value", moduleId);
		// 类别为：类别
		$("input[name='m:cplbpcb:lbhpc']").attr("value", "batch");
		$("input[name='m:cplbpcb:sscplb']").attr("value", categoryId);
		// 页面初始化
		$.ajax({
		    url: __ctx+"/product/category/batch/getMapById.do?categoryId="+categoryId,
		    async:false,
		    success:function(data){
		    	if(data.success){
		    		$("input[name='m:cplbpcb:cpdh']").attr("value", data.F_CPDH);
		    		$("input[name='m:cplbpcb:cpmc']").attr("value", data.F_CPMC);
		    		$("input[name='m:cplbpcb:yzdw']").attr("value", data.F_YZDW);
		    		$("input[name='m:cplbpcb:yzdwID']").attr("value", data.F_YZDWID);
		    		$("input[name='m:cplbpcb:zrbm']").attr("value", data.F_ZRBM);
		    		$("input[name='m:cplbpcb:zrbmID']").attr("value", data.F_ZRBMID);
		    	}	
		    }
		 })
	}
})
