/**
 * 产品新增/编辑JS
 */
$(function(){
	// 操作类型
	var handleType = $.getParameter("handleType");
	// 型号id
	var moduleId = $.getParameter("moduleId");
	// 产品类别id
	var categoryId = $.getParameter("categoryId");
	// 产品批次id
	var batchId = $.getParameter("batchId");
	
//	$("input[name='m:cpb:cppcdh']").attr("disabled", true);
//	$("select[name='m:cpb:xhjd']").attr("disabled", true);
	if(handleType=="add"){
		$("input[name='m:cpb:ssxh']").attr("value", moduleId);
		$("input[name='m:cpb:sscplb']").attr("value", categoryId);
		$("input[name='m:cpb:sscppc']").attr("value", batchId);
	}
})
