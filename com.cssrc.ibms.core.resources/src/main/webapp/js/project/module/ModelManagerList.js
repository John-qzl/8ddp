/**
 * 型号管理员/list.js
 */

/*$(function(){
  var moduleId = $.getParameter("__xhId__");
  var url= __ctx+"/model/user/role/getUseRole.do";
  var del=$('.deleteData');
  $.ajax({
    'url':url,
    'data':{
      'moduleId':moduleId,
    },
    type: "POST",
     async:true,
     success:function(data){
        if(data.useRole=="1"){
          $("#addTeamButton").attr("style","visibility:visible");
          del.attr("style","visibility:visible");
        }  
        
       }
  });
})*/
function dlgCallBack(userIds, fullnames) {
		if (userIds.length > 0) {
			var type ="manage";
			var __pk__ = $.getParameter("__xhId__");
			var url=__ctx+"/module/manage/setManager.do";
			$.ajax({
				url:url,
				data:{
					"type":type,
					"id":__pk__,
					"userIds":userIds,
					"fullNames":fullnames
					},
					success:function(data){
						location.href=location.href.getNewUrl();
					}
			});
		}
	};
	function dlgCallBackTeam(userIds, fullnames) {
		if (userIds.length > 0) {
			var type ="team";
			var __pk__ = $.getParameter("__xhId__");
			var url=__ctx+"/module/manage/setManager.do";
			$.ajax({
				url:url,
				data:{
					"type":type,
					"id":__pk__,
					"userIds":userIds,
					"fullNames":fullnames
					},
					success:function(data){
						location.href=location.href.getNewUrl();
					}
			});
		}
	};
	function addManager() {
		debugger;
		var moduleId = $.getParameter("__xhId__");
		UserDialog({
			callback : dlgCallBack,
			isSingle : false,
			dataType:"modelManageFilter",
			dataId:moduleId,
		});
	}
	
	function addTeam() {
		debugger;
		var moduleId = $.getParameter("__xhId__");
		UserDialog({
			callback : dlgCallBackTeam,
			isSingle : false,
			dataType:"modelManageFilter",
			dataId:moduleId,
		});
	}
	function deletes(ids){
		  var url= __ctx+"/module/manage/deleteManage.do";
		  var moduleId = $.getParameter("__xhId__");
		  $.ligerDialog.confirm('确认删除吗？','提示信息',function(rtn) {
				if(rtn) {
					  $.ajax({
						    'url':url,
						    'data':{
						      'moduleId':moduleId,
						      ids:ids
						    },
						    type: "POST",
						     async:true,
						     success:function(data){
						        if(data.useRole=="1"){
						        	  $.ligerDialog.success("删除成功！","",function(){
							    		  window.location.reload();
										});
						        	}  
						        else{
						        	$.ligerDialog.warn("您没有权限删除！",'提示信息');
						        }
						    }
					  });
				}
		  });
		
	}
	function deleteSelect(){
		var records = [];
		var ids = getSelectIds(true,false);
		if(!ids) return;
		  var url= __ctx+"/module/manage/deleteManage.do";
		  var moduleId = $.getParameter("__xhId__");
		  $.ligerDialog.confirm('确认删除吗？','提示信息',function(rtn) {
				if(rtn) {
					  $.ajax({
						    'url':url,
						    'data':{
						      'moduleId':moduleId,
						      ids:ids
						    },
						    type: "POST",
						     async:true,
						     success:function(data){
						        if(data.useRole=="1"){
						        	  $.ligerDialog.success("删除成功！","",function(){
							    		  window.location.reload();
										});
						        	}  
						        else{
						        	$.ligerDialog.warn("您没有权限删除！",'提示信息');
						        }
						    }
					  });
				}
		  });
	}
	// 获取列表选中数据id数组字符串
	function getSelectIds(isAlert,isSingle){
		var idArr = [];
		$('input.pk:checked').each(function(){
			idArr.push($(this).val());
		})
		if(isAlert){
			if(idArr.length==0){
				$.ligerDialog.warn("请选择数据！");
				return false;
			}
			if(idArr.length!=1&&isSingle){
				$.ligerDialog.warn("请选择一条数据！");
				return false;
			}
			return idArr.toString();
		}else{
			return idArr.toString();
		}
	}

//excel导入型号团队
function importModuleTeam() {
	var moduleId=$.getParameter("__xhId__");
	var url = __ctx + '/upload/importModuleTeamFromExcel.do?moduleId='+moduleId;
	DialogUtil.open({
		height: 500,
		width: 800,
		url: url,
		title: "excel导入型号团队",
		sucCall: function (rtn) {
			if (rtn=="true"){
				window.location.href = window.location.href.getNewUrl();
			}
		}
	});
}
