/**
 * 工作队列表定制
 */
if(!TeamListUtil){
	var TeamListUtil = {};
}
/**
 * 新增
 */
TeamListUtil.add = function(conf){
	var right = TeamListUtil.getButtonRight("add");
	/*
	if(!right.isCan){
		$.ligerDialog.warn(right.message);
		return;
	}
	*/
	if(typeof openLinkDialog=="function"){
		openLinkDialog(conf);
	}else{
		$.ligerDialog.warn("openLinkDialog not a funciton!");
	}
}
/**
 * 删除
 */
TeamListUtil.del = function(obj){
	var idArr = [];
	$('input.pk:checked').each(function(){
		idArr.push($(this).val());
	})
	ids = idArr.toString();
	if(ids==""){
		$.ligerDialog.warn("请选择记录！");
		return false;
	}
	var right = TeamListUtil.getButtonRight("del",ids);
	if(!right.isCan){
		$.ligerDialog.warn(right.message);
		return;
	}
	if(typeof handlerDelSelect=="function"){
		teamdel(obj);
	}else{
		$.ligerDialog.warn("handlerDelSelect not a funciton!");
	}
}
/**
 * 编辑
 */
TeamListUtil.edit = function(conf){
	var right = TeamListUtil.getButtonRight("edit",conf.id);
	if(!right.isCan){
		$.ligerDialog.warn(right.message);
		return;
	}
	if(typeof openLinkDialog=="function"){
		openLinkDialog(conf);
	}else{
		$.ligerDialog.warn("openLinkDialog not a funciton!");
	}
}
TeamListUtil.getButtonRight =function(buttonName,ids){
	var sssjb = $.getParameter("__dbomFKValue__");
	var right = {isCan:false,message:"您没有操作权限！"};
	switch(buttonName){
	case 'add':
		right = getAddRight(sssjb);
		break;
	case 'del':
		right = getDelRight(sssjb,ids);
		break;
	case 'edit':
		right = getEditRight(sssjb,ids);
		break;
	}
	return right;	
	function getAddRight(){
		var url = __ctx+"/dataPackage/tree/ptree/getTeamButtonRight.do";
		$.ajax({
			  type: "POST",
		      url:url,
			  data:{buttonName:"add",sssjb:sssjb},
			  dataType : "text",
		      async:false,
		      success:function(result){
		    	  rtn = JSON2.parse(result)
		      }   		  
		});
		return rtn;
	}
	function getDelRight(){
		var url = __ctx+"/dataPackage/tree/ptree/getTeamButtonRight.do";
		$.ajax({
			  type: "POST",
		      url:url,
			  data:{buttonName:"del",ids:ids,sssjb:sssjb},
			  dataType : "text",
		      async:false,
		      success:function(result){
		    	  rtn = JSON2.parse(result)
		      }   		  
		});
		return rtn;
	}
	function getEditRight(){
		var url = __ctx+"/dataPackage/tree/ptree/getTeamButtonRight.do";
		$.ajax({
			  type: "POST",
		      url:url,
			  data:{buttonName:"edit",ids:ids,sssjb:sssjb},
			  dataType : "text",
		      async:false,
		      success:function(result){
		    	  rtn = JSON2.parse(result)
		      }   		  
		});
		return rtn;
	}
}
/**
 * 删除
 * @returns
 */
function teamdel(obj){
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
			/*
			 *DESCRIPTION :先去相关的数据包中查询是否有数据包详细信息使用该工作队再决定是否删除
			 *AUTHOR : SGL
			 *TIME : 2018/6/27 11:26
			 */
			$.ajax({
				async : true,//同步:false JS代码加载到当前ajax的时候会把页面里所有的代码停止加载,页面处于假死状态,当该ajax执行完毕后,才会继续运行其他代码,页面解除假死状态.
				//异步:true  默认设置值,前台会继续执行ajax后面的脚本
				cache : true, //cahce的作用:第一次请求完毕之后,如果之后再去请求,可以直接从缓存读取而不需要再到服务器端读取
				type: 'POST',
				dataType : "json",
				data : {ID:delIds.toString(),sssjb:sssjb},
				url: __ctx+"/dataPackage/workTeam/checkWorkTeamIfUsed.do",
				success:function(data){
					var teamList = data.teamList;
					var message = "";
					if(teamList.length >0){

						for(var i = 0 ; i<teamList.length ; i ++){
							if(i == 0){
								message =  message +　teamList[i];
							}else{
								message += message　+ "、"+ teamList[i];
							}
						}
						message = message +  " 工作队绑定了数据包实例信息,请先删除数据包实例,再删除工作队信息!"
						$.ligerDialog.warn(message,"提示信息",function(){
							location.href = window.location.href.getNewUrl();
							diadialog.close();
						});
					}else{
						$.ligerDialog.success("删除成功","",function(){
							location.href = window.location.href.getNewUrl();
							diadialog.close();
						})
					}
				},
				error:function(){
					$.ligerDialog.error('删除出错');
				}

			});

			//如果选中的工作队没有使用,则删掉该工作队
			//删除操作
			//$.ajax({
			//	  type: "POST",
			//      url:$(ele).attr("action"),
			//	  data:{ID:delIds.toString(),__pk__:delIds.toString(),noDirect:'true'},
			//	  dataType : "text",
			//      async:false,
			//      success:function(result){
			//      }
			//});

		}
	});
}