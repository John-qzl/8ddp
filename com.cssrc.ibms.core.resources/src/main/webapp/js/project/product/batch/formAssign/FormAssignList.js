/**
 * 产品批次-表单下发列表
 */

function newAdd(obj){
	// 验收策划id
	var acceptancePlanId = $.getParameter("acceptancePlanId");
	// 产品批次id
	var productBatchId = $.getParameter("productBatchId");
	// 产品类别id
	var productCategoryId = $.getParameter("productCategoryId");
	var action = $(obj).attr("action");
	action += "&acceptancePlanId="+acceptancePlanId;
	action += "&productBatchId="+productBatchId;
	action += "&productCategoryId="+productCategoryId;
	action += "&handleType=add"
	$(obj).attr("action", action);
	openLinkDialog({
		scope : obj,
		width : 800,
		height : 600,
		isFull : false,
		title : '表单下发'
	})
}

var clickTimer;
// 产品类别模板（表单）预览
function categoryTemplatePreview(templateId){
	clearTimeout(clickTimer);
	clickTimer = setTimeout(function(){
		DialogUtil.open({
			url:__ctx+"/template/manage/categoryTempFormPreview.do?templateId="+templateId,
			height: 600,
			width: 1000,
			title:"产品表单预览",
			isResize: true,
			sucCall:function(rtn){	
				 //reFresh() ;
			}
		});
	},250)
}
$(function(){
	var acceptancePlanId = $.getParameter("acceptancePlanId");
	var add = $('.add');
	var del=$('.del');
	var edit=$('.edit');
	var url= __ctx+"/model/user/role/getRoleByPlanId.do";
	$.ajax({
		'url':url,
		'data':{
			'acceptancePlanId':acceptancePlanId,
		},
		type: "POST",
		 async:true,
		 success:function(data){
			 if(data.useRole=="1"){
	    		add.attr("style","visibility:visible");
	    		del.attr("style","visibility:visible");
	    		edit.attr("style","visibility:visible");

	    		$('div.PadOperaterMenu').each(function(){
	    			PadOperater.init(this);
	    		})
	    	}  
	    	else{
	    		/*add.hide();
	    		del.hide();
	    		edit.hide();*/

	    	}
	     }
	});

})