/**
 * 任务新增/编辑JS
 */
$(function(){
	// 操作类型
	var handleType = $.getParameter("handleType");
	// 型号id
	var moduleId = $.getParameter("moduleId");
	if(handleType=="add"){
		$("input[name='m:bcsyrwb:ssxh']").attr("value", moduleId);
	}
})
