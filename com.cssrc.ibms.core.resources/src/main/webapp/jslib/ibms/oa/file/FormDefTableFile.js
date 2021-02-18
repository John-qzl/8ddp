/**
 * 表单设计-表附件初始化
 */
$(function(){
	var div = $('div[controlname=tablefile]');
	div.attr("class","");
	div.attr("style","height:500px");
	var v = TableFile.getVariable();
	$(div).each(function(i,e){
		var curDiv = $(e);
		var tablefilesetting = curDiv.find('textarea[name=tablefilesetting]').val();
		var set = $.parseJSON(tablefilesetting.replaceAll("\'","\""));
		var tableFile = new TableFile(curDiv,set,v);
		tableFile.render();
	})
});

var TableFile = function(div,setting,v){
	this.conf = {
			container : div, //容器
			fileDisplaySetting :{nodekey:"FILE",dimension:0, maindata: 0, mainfield: "", reldata: "", reldatafield:""},//附件列表展示控制
			listUrl :  v.ctx+"/oa/form/dataTemplate/tableFile.do",//附件列表入口
			treeUrl :  v.ctx+"/oa/form/dataTemplate/fileView.do",//附件树入口
			filePemission : {},//附件页面按钮权限信息
			v :v //变量
	};	
	//conf信息初始化
	this.conf.fileDisplaySetting = $.extend({},this.conf.fileDisplaySetting,setting);
}
TableFile.prototype = {
	render: function(){
		var v = this.conf.v;
		var div = this.conf.container;
		var url = this.conf.listUrl;
		//treeUrl-params
		/*var params = {
				isSingle : 0,
				__displayId__ : v.displayId,
				__pk__ : v.dataId,
				jsonStr : JSON.stringify(this.conf.fileDisplaySetting),
				isDetail:v.pageType=="detail"?true:false
		};*/
		var params = {
					formKey:v.formKey,
					dataId:v.dataId,
					isDetail:v.pageType=="detail"||v.pageType=="flowDetail"?true:false
		};
		params = $.extend({},params,this.conf.fileDisplaySetting);
		TableFile.delFile(null,-1);//先删除-1的数据
		var iframe = '<iframe id="fileFrame" name="fileFrame"  style="height:100%;"  width="100%" frameborder="0"></iframe>';
		$(div.find('div[name=fileList]')).append($(iframe));
		$("#fileFrame").attr("src", url+"?paramJson="+JSON.stringify(params));
	}
}
/**
 * 获取页面变量
 */
TableFile.getVariable = function(){
	var v = {};
	var pageType = $('input[name=pageType]').val();
	v.pageType = pageType;
	switch(pageType){
	case 'edit':
		v.ctx = $('input[name=ctx]').val();
		v.hasPk =  	$('input[name=dataId]').val()!=""?true:false;
		v.formKey = $('input[name=formKey]').val();
		v.tableId = $('input[name=tableId]').val();
		v.dataId =  $('input[name=dataId]').val()==""?-1:$('input[name=dataId]').val();
		break;
	case 'detail':
	case 'flowDetail':
		v.ctx = 	$('input[name=ctx]').val();
		v.formKey = $('input[name=formKey]').val();
		v.dataId =  $('input[name=dataId]').val();
		break;
	case 'flowEdit':
		v.ctx =     $('input[name=ctx]').val();
		v.formKey = $('input[name=formKey]').val();
		v.tableId = $('input[name=tableId]').val();
		v.dataId =  $('input[name=businessKey]').val()==""?-1:$('input[name=businessKey]').val();;
		v.hasPk =   $('input[name=businessKey]').val()!=""?true:false;
		break;
	}
	return v;
};
/**
 * 新增时：没有点击保存，将删除文件；
 * */
TableFile.delFile = function(tableId,dataId){
	var div = $('div[controlname=tablefile]');
	if(div.length==0){
		return;
	}
	var variable  = TableFile.getVariable();
	if(tableId!="undefined"&&tableId!=null){
		variable.tableId = tableId;
	}
	if(dataId!="undefined"&&dataId!=null){
		variable.dataId = dataId;
	}
	$.ajax({
		url :  variable.ctx + '/oa/system/sysFile/delFormFileData.do',
		data : {
			tableId	: variable.tableId,
			virtual_dataId:variable.dataId
		},
		async : false, //{false：同步加载，true：异步加载} 
		type : 'post',
		success : function(result) {
		}
	});
}
/**
 * 新增时：点击保存，将进行文件dataId的更新
 * */
TableFile.updateFile = function(data, params){
	var div = $('div[controlname=tablefile]');
	if(div.length==0){
		return;
	}
	var variable  = TableFile.getVariable();
	$.ajax({
		url :  variable.ctx + '/oa/system/sysFile/updateFileRecod.do',
		data : {
			tableId:variable.tableId,
			real_dataId : params.keyId,
			virtual_dataId:variable.dataId
		},
		async : false, //{false：同步加载，true：异步加载} 
		type : 'post',
		success : function(result) {
		}
	});
}
/**
 * 刷新
 */
TableFile.refresh = function(){
	var div = $('div[controlname=tablefile]');
	if(div.length==0){
		return;
	}
	div.attr("class","");
	div.attr("style","height:500px");
	var v = TableFile.getVariable();
	$(div).each(function(i,e){
		var curDiv = $(e);
		var tablefilesetting = curDiv.find('textarea[name=tablefilesetting]').val();
		var set = $.parseJSON(tablefilesetting.replaceAll("\'","\""));
		var tableFile = new TableFile(curDiv,set,v);
		tableFile.render();
	})
}