/**
 * 型号-通用模板新增/编辑JS
 */
$(function(){
	// 操作类型
	var handleType = $.getParameter("handleType");
	// 型号id
	var moduleId = $.getParameter("moduleId");
	// 所属父文件夹
	var folderId = $.getParameter("folderId");
	
	if(handleType=="add"){
		// 型号id
		$("input[name='m:temp_file:module_ID']").attr("value", moduleId);
		$("input[name='m:temp_file:temp_file_ID']").val(folderId);
	}
	
})
