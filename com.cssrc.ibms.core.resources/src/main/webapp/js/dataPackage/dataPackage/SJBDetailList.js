//批量新增
function volumeAdd(){	
	//权限控制
	var rightResult = rightCheck('pladd');
	if(!rightResult.isCan){
		$.ligerDialog.warn(rightResult.message);
	        return false;
	}
	var callBack = function(item, Dialog){
		Dialog.close();
		var __dbomFKName__ = $.getParameter("__dbomFKName__");
		var __dbomFKValue__ = $.getParameter("__dbomFKValue__");
		var projectId = $.getParameter("projectId");
		if(item.type == 'ok'){//批量新增表单
			var url = __ctx+'/oa/form/dataTemplate/editData.do?__displayId__=10000001780378&__dbomFKName__='+__dbomFKName__+'&__dbomFKValue__='+__dbomFKValue__;
			url+='&projectId='+projectId;
			openMyLinkDialog({url:url.getNewUrl()});
		}else{//批量新增其它
			var url = __ctx+'/oa/form/dataTemplate/editData.do?__displayId__=10000001780372&__dbomFKName__='+__dbomFKName__+'&__dbomFKValue__='+__dbomFKValue__;
			url+='&projectId='+projectId;
			openMyLinkDialog({url:url.getNewUrl()});
		}
	};
	var p = {
            type: 'confirm',
            content: "批量新增记录，记录分为表单类型、其他类型！",
            buttons: [{ text: "<font color='red'>表单类型</font>", onclick: callBack, type: 'ok' }, { text: "其他类型", onclick: callBack, type: 'no'}],
			width:'500',
			height:'60',
			title:'批量新增',
			allowClose :true,
			showMax: false,
            showToggle: false,
            showMin: false
        };
    $.ligerDialog(p);
}
function projectOfficeManage(){
	var h=window.top.document.documentElement.clientHeight;
	var w=window.top.document.documentElement.clientWidth;
	var type = decodeURI(location.href.getNewUrl().split("F_TYPE=")[1].split("&")[0]);
	var Url = __ctx+'/oa/form/dataTemplate/preview.do?__displayId__=10000020080069&__dbomSql__=F_TYPE='+type;

	DialogUtil.open({
		url:Url,
		height: h*0.8,
		width: w*0.8,
		title:"项目办人员管理",
		isResize: true,
		sucCall:function(rtn){	
			
		}
	});
}
function openMyLinkDialog(CustomConf){
	var defaultConf = {
		height:800,
		width:1000,
		title:"表单填写"
	}
	if(!CustomConf.url){
		alert("url不能为空！");
		return;
	}
	var conf = $.extend({},defaultConf,CustomConf);
	DialogUtil.open({
		height:conf.height,
		width: conf.width,
		url: conf.url,
		showMax: false,                             //是否显示最大化按钮 
	    showToggle: false,                         //窗口收缩折叠
	    title: conf.title,
	    showMin: false,
		sucCall:function(rtn){			
			if(!(rtn == undefined || rtn == null || rtn == '')){
				locationPrarentPage();
				location.href=location.href.getNewUrl();
			}
		}
	});
}
/**
 * 数据包详细信息列表: 当数据类型为表单时的弹框事件
 * @param obj
 * @returns
 */

var clickTimer;

function openFormTemplateLink(obj,dataId){
	var dataId = dataId;
	clearTimeout(clickTimer);

	//权限控制
	var rightResult = rightCheck('formEdit',dataId,'已完成,PAD端进行中,');
	var result = "";
	if(!rightResult.isCan){
		$.ligerDialog.warn(rightResult.message);
		return false;
	}
	$.ajax({
		  type: "POST",
	      url:__ctx+"/dp/form/powerManage.do",
		  data:{dataId : dataId},
	      async:false,
	      success:function(data){
	    	   result = data;
	      }   		  
	});
	if(result=="no"){
		$.ligerDialog.warn("您不能录入当前表单！");
		  return false;
	}

	//var packageId = $("input[type='checkbox'][disabled!='disabled'][class='pk']:checked")[0].value;
	//"&ckResultName='+packageId
	DialogUtil.open({
		url:__ctx+"/dp/form/formEntry.do?slid="+obj+"&packageId="+dataId+"",
		height: 750,
		width: 1200,
		title:"表单录入",
		isResize: true,
		sucCall:function(rtn){	
			reFresh() ;
		}
	});

}

function openFormManage(obj){
	clearTimeout(clickTimer);
	clickTimer = setTimeout(function(){
		DialogUtil.open({
			url:__ctx+"/dp/form/formManage.do?slid="+obj,
			height: 750,
			width: 1200,
			title:"表格管理",
			isResize: true,
			sucCall:function(rtn){	
				 //reFresh() ;
			}
		});
	},250)
}
/**
 * 删除
 * @returns
 */
function mydel(obj){
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
	//权限控制
	var rightResult = rightCheck('del',null,'未开始,待下载,已终止');
	if(!rightResult.isCan){
		$.ligerDialog.warn(rightResult.message);
	        return false;
	}
	$.ligerDialog.confirm("确定删除吗！",'提示信息',function(rtn) {
		if(rtn) {
			$.ajax({
				  type: "POST",
			      url:$(ele).attr("action"),
				  data:{ID:delIds.toString(),__pk__:delIds.toString(),noDirect:'true'},
				  dataType : "text",
			      async:false,
			      success:function(result){
			      }   		  
			});
			location.href = window.location.href.getNewUrl();
		}
	});
}
/**
 * 批量删除
 * @returns
 */
function myqzdel(obj){
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
	//权限控制
	var rightResult = rightCheck('qzdel');
	if(!rightResult.isCan){
		$.ligerDialog.warn(rightResult.message);
	        return false;
	}
	var userId = "";
    var result = "";
    var packageId = jQuery.getParameter("__dbomFKValue__");

    $.ajax({
        url:__ctx+"/dataPackage/tree/ptree/getFzeIdById.do",
        data:{packageId : packageId},
        async:false,
        success:function(data){
        	userId = data;
        }   		  
      });
    $.ajax({
      url:__ctx+"/dataPackage/tree/ptree/checkUser.do",
      data:{userId : userId},
      async:false,
      success:function(data){
    	   result = data;
      }
    });
    if(result=="0"){
    	$.ligerDialog.warn("只有当前节点负责人有强制删除权限！");
		return false;
    }

	$.ligerDialog.confirm("确定删除吗！",'提示信息',function(rtn) {
		if(rtn) {
			$.ajax({
				  type: "POST",
			      url:$(ele).attr("action"),
				  data:{ID:delIds.toString(),__pk__:delIds.toString(),noDirect:'true'},
				  dataType : "text",
			      async:false,
			      success:function(result){
			      }
			});
			location.href = window.location.href.getNewUrl();			
		}
	});
	return false;
}
//新增、编辑
function myOpenLinkDialog(conf,buttonName){
	conf = conf || {};
	var keyName = conf.keyName;	
	
	//权限控制
	var rightResult = rightCheck(buttonName,conf.scope);
	if(!rightResult.isCan){
		$.ligerDialog.warn(rightResult.message);
	    return;
	}
	var	contentWidth = window.top.document.documentElement.clientWidth,
		contentHeight = window.top.document.documentElement.clientHeight;
	conf.width>contentWidth?conf.width = contentwidth:null;
	conf.height>contentHeight?conf.height = contentHeight:null;
	var obj =conf.scope||this; 
		width= conf.width||1000,
		height=conf.height||760,
		isFull =conf.isFull||false,
		isStart = conf.isStart||false,
		title = conf.title||'';
	if(isFull){
		height=contentHeight;
		width=contentWidth;
	}
	if(title == ''){
		title = '提示';
	}
	var url=$(obj).attr("action");
	var purl = decodeURI(window.location.href);
    var params = purl.getArgs();
    var projectName = params.projectName;
    var projectId =params.projectId;
	url+='&projectName='+projectName;
	url+='&projectId='+projectId;
	var optClass=$(obj).attr("class");

	//判断是管理列的操作还是顶上的启动按钮
	if(optClass.indexOf("ops_more")==-1&&optClass.indexOf("run")>0){
		//获取选中的表单，若是选中一个就和管理列中的启动一样，若是没有选中就创建一个新的流程
		var $aryId = $("input[type='checkbox'][disabled!='disabled'][class='pk']:checked");
		//如果选中的某条流程实例没有发起，则将获取的id拼接到URL中
		var startId="";
		
		if($aryId.length > 1){
			$.ligerDialog.warn("只能选择一条记录或者不选！");
			return false;
		}else if($aryId.length == 1){
			//判断启动未发起的表单实例还是启动新的表单流程
			var idStart = $("input[type='checkbox'][disabled!='disabled'][class='pk']:checked")[0];
			if(idStart!=undefined){
				var idStartValue = idStart.value;
				//判断选中记录是否启动的方法在表单模板中，getStartId(idStartValue);
				if(getStartId(idStartValue)){
					startId = idStartValue;
				}
			}
		}
		
		//没有选中就走新建流程启动
		if(startId!=""){
			var name="businessKey";
			url=url +"&" +name +"=" +startId ;
		}
	}
	url=url.getNewUrl();
	DialogUtil.open({
		height:height,
		width: width,
		url: url,
		showMax: false,                             //是否显示最大化按钮 
	    showToggle: false,                         //窗口收缩折叠
	    title: title,
	    showMin: false,
		sucCall:function(rtn){
			if(keyName){
				try{
					getAfterScript(keyName);
				}catch(e){
					console.error(e);
					alert("后置脚本执行出错");
				}
			}
			//rtn作为回调对象，可进行定制和扩展
			if(!(rtn == undefined || rtn == null || rtn == '')){
				//刷新原来的当前的页面信息
				locationPrarentPage();
				//如果存在ifrem父页面 存在 dbomtree 存在 reloadSelectNode方法，需要重新load 选中的当前树节点数据
				location.href=location.href.getNewUrl();
			}
		}
	});
}

/**
 * 数据包详细列表按钮权限信息
 * @param ids
 * @param canOperaterZt
 * @param needgw
 * @param needFzr
 * @returns
 */
function rightCheck(buttonName,obj,canOperaterZt){
	var sssjb = $.getParameter("__dbomFKValue__");
	var rtn = {isCan:false};
	var params = {buttonName:buttonName};
	switch(buttonName){
	case 'add':
	case 'pladd':
		params.sssjb = sssjb;
		break;
	case 'edit':
		 ;
		var ids = $(obj).parents('tr').find('input.pk').val()
		if(!ids){
			return;
		}else{
			params.ids = ids;
			params.sssjb = sssjb;
		}
		break;
	case 'del':

		var ids = getSelectIds(true,false);
		if(!ids){
			return;
		}else{
			params.ids = ids;
			params.sssjb = sssjb;
			params.canOperaterZt=canOperaterZt;

		}
		break;
	case 'qzdel':
		var ids = getSelectIds(true,false);
		if(!ids){
			return;
		}else{
			params.ids = ids;
			params.sssjb = sssjb;
		}
		break;
	case 'formEdit':
		//var ids = getSelectIds(true,true);
		var ids = obj;
		if(!ids){
			return;
		}else{
			params.ids = ids;
			params.sssjb = sssjb;
			params.canOperaterZt=canOperaterZt;

		}
		break;
	}
	var url = __ctx+"/dataPackage/tree/ptree/getButtonRight.do";
	$.ajax({
		  type: "POST",
	      url:url,
		  data:params,
		  dataType : "text",
	      async:false,
	      success:function(result){
	    	  rtn = JSON2.parse(result)
	      }   		  
	});
	return rtn;
}
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

//清空数据包所有业务表
function delAll() {
	$.ligerDialog.confirm("确定清空所有数据吗！",'提示信息',function(rtn) {
		if(rtn) {
			window.location.href = __ctx + "/dataPackage/tree/ptree/delAllData.do";
			parent.location.reload();
		}
	});
}