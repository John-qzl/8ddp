/**
 * 模版列表批量删除定制
 */
if(!TemplateListUtil){
	var TemplateListUtil = {};
}

/**
 * 删除
 */
TemplateListUtil.del = function(obj){
	var idArr = [];

	$('input.pk:checked').each(function(){
		idArr.push($(this).val());
	})
	ids = idArr.toString();
	if(ids==""){
		$.ligerDialog.warn("请选择记录！");
		return false;
	}
	templatedel(obj);
}

var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
/**
 * 删除
 * @returns
 */
function templatedel(obj){
	var ele = obj;
	var $aryId = $("input[type='checkbox'][disabled!='disabled'][class='pk']:checked");		
	if($aryId.length == 0){
		$.ligerDialog.warn("请选择记录！");
		return false;
	}		
	var delIds=[];
	$aryId.each(function(i){
		delIds.push($(this).val());
	});
	var sssjb = $.getParameter("__dbomFKValue__");
	$.ligerDialog.confirm("确定删除吗！",'提示信息',function(rtn) {
		if(rtn) {
			
			$.ajax({
				async : true,//同步:false JS代码加载到当前ajax的时候会把页面里所有的代码停止加载,页面处于假死状态,当该ajax执行完毕后,才会继续运行其他代码,页面解除假死状态.
				//异步:true  默认设置值,前台会继续执行ajax后面的脚本
				cache : true, //cahce的作用:第一次请求完毕之后,如果之后再去请求,可以直接从缓存读取而不需要再到服务器端读取
				type: 'POST',
				dataType : "json",
				data : {ID:delIds.toString(),sssjb:sssjb},
				url: __ctx+"/dp/form/templateListDelete.do",
				success:function(data){
					var templateList = data.templateList;
					var message = "";
					if(templateList.length >0){

						for(var i = 0 ; i<templateList.length ; i ++){
							if(i == 0){
								message =  message +　templateList[i];
							}else{
								message = message　+ "、"+ templateList[i];
							}
						}
						message = message +  " 模版存在未完成的表单,无法删除!"
						$.ligerDialog.warn(message,"提示信息",function(){
							location.href = window.location.href.getNewUrl();
							dialog.close();
						});
					}else{
						$.ligerDialog.success("删除成功","",function(){
							location.href = window.location.href.getNewUrl();
							dialog.close();
						})
					}
				},
				error:function(){
					$.ligerDialog.error('删除出错');
				}

			});

		}
	});
}