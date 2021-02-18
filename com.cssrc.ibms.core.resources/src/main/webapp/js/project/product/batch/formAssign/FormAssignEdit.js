/**
 * 产品批次-表单下发 新增/编辑JS
 */
$(function() {
	// 定制初始化
	customInit();
	// 操作类型
	var handleType = $.getParameter("handleType");
	// 验收策划id
	var acceptancePlanId = $.getParameter("acceptancePlanId");
	// 产品批次id
	var productBatchId = $.getParameter("productBatchId");
	// 产品类别id
	var productCategoryId = $.getParameter("productCategoryId");

	if (handleType == "add") {
		$("input[name='m:dataPackageInfo:acceptancePlanId']").attr("value",
				acceptancePlanId);
		$("input[name='m:dataPackageInfo:productBatchId']").attr("value",
				productBatchId);
		$("input[name='m:dataPackageInfo:productCategoryId']").attr("value",
				productCategoryId);
		$("input[name='m:dataPackageInfo:zxzt']").attr("value",
				"未开始");
	}
})

function customInit() {
	
	// 选择产品模板
	$('a.selectTemplate').click(function() {
		var purl = decodeURI(window.location.href);
		var params = purl.getArgs();
		var acceptancePlanId = $.getParameter("acceptancePlanId");
		var productCategoryId = params.productCategoryId;
		var url = __ctx + '/template/manage/categoryTemplateSelectView.do?';
		url += 'productCategoryId=' + productCategoryId;
		url += '&acceptancePlanId=' + acceptancePlanId;
		DialogUtil.open({
			height : 500,
			width : 400,
			url : url,
			showMax : true, //是否显示最大化按钮 
			showToggle : false, //窗口收缩折叠
			title : "产品模板选择器",
			showMin : false,
			sucCall : function(rtn) {
				$('[name="m:dataPackageInfo:templateId"]').val(rtn.ids);
				$('[name="m:dataPackageInfo:templateName"]').val(rtn.names);
				//数据名称的同步添加
				$('[name="m:dataPackageInfo:sjmc"]').val(rtn.names);
			}
		});
	})
	
	// 重置选择模板
	$('a.resetTemplate').click(function() {
		$('[name="m:dataPackageInfo:templateId"]').val("");
		$('[name="m:dataPackageInfo:templateName"]').val("");
	})
	function check(){
		debugger;
	}
}
