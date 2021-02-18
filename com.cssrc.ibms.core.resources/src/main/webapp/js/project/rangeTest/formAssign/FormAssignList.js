/**
 *  靶场实验/武器所检-表单下发列表
 */

function newAdd(obj){
	// 策划id
	var acceptancePlanId = $.getParameter("acceptancePlanId");

	var action = $(obj).attr("action");
	action += "&acceptancePlanId="+acceptancePlanId;
	action += "&handleType=add"
	$(obj).attr("action", action);
	openLinkDialog({
		scope : obj,
		width : 1000,
		height : 600,
		isFull : false,
		title : '表单下发'
	})
}
/*$(function(){
	debugger;
	var xhId = $.getParameter("xhId");
	var add=$('.add');
	var del=$('.del');
	var edit=$('.edit');
	var url= __ctx+"/model/user/role/getUseRole.do";
	$.ajax({
		'url':url,
		'data':{
			'moduleId':xhId,
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
	     }
	});
});*/
var clickTimer;
//点击数据包姓名后,显示的数据包预览
		//这个template还不知道从哪来的
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
