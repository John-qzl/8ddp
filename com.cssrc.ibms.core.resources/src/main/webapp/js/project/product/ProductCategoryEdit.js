/**
 * 产品类别新增/编辑JS
 */
$(function(){
	// 操作类型
	var handleType = $.getParameter("handleType");
	// 型号id
	var moduleId = $.getParameter("moduleId");
	if(handleType=="add"){
		$("input[name='m:cplbpcb:ssxh']").attr("value", moduleId);
		// 类别为：类别
		$("input[name='m:cplbpcb:lbhpc']").attr("value", "category");
	}
})
