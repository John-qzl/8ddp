$(function() {
	var displayId = jQuery.getParameter("__displayId__");	
	qlCache.displayId = displayId;
	initQueryForm();
	initClickEvent();	
	colIsShow();

});


/***********************    js变量  ***********************/
var qlCacheFactory = function (){
	this.isSave = true;
	this.displayId = null; //业务数据模板id
	this.querySetInfo = null;//查询区域设置信息
	this.lineValueFilter = {init:false,isIconOpen:false};//列值过滤信息已初始化字段	 {field1：true}
	//change
	this.ary = [];
};
var qlCache = new qlCacheFactory();/**（作用域：右键刷新前不清空）*/
var defaultInfo = { searchForm: "searchForm" ,lvform:"lineValueForm"};


/***********************    查询自定义设置  ***********************/
//初始化
function initQueryForm(displayId) {
	if($.isEmpty(qlCache.querySetInfo)) {
		initQuerySetInfo();
	}
	querySetLive();
}

//清空查询区域
function clearQueryForm() {
	//searcherForm清空
	$('#' + defaultInfo.searchForm).find('input[type=text]').each(function() {
		$(this).val("");
	});
	$('#' + defaultInfo.searchForm).find('input[type=hidden]').each(function() {
		$(this).val("");
	});
	//lvForm清空
	$('#' + defaultInfo.lvform).find('ul').remove();
	qlCache.lineValueFilter = {init:true};
	$.ligerDialog.success("清空完毕！", "提示信息");
	var obj =$('a.link.ajaxSearch');
	qlCache.ary = [];
	sessionStorage.removeItem('columnFilter');
	handlerSearchAjax(obj);
}
//打开用户设置窗口
function openQuerySetDialog() {
	var url = __ctx + "/oa/system/userCustom/querySetDialog.do?";
	var querySetInfoEncode = encodeURI(JSON2.stringify(qlCache.querySetInfo));
	url += "querySetInfo=" + querySetInfoEncode;
	DialogUtil.open({
		height: "500",
		width: "400",
		title: '查询字段设置',
		url: url,
		//自定义参数
		sucCall: function(rtn) {
			qlCache.querySetInfo = rtn.querySetInfo;
			querySetLive();
			saveQuerySetInfo();
		}
	});
}
//设置信息生效
function querySetLive() {
	$('#' + defaultInfo.searchForm).find('li').each(function() {
		var liObj = $(this);
		var name = liObj.attr("var");			
		var isHide = true;
		if($.isEmpty(name)){
			isHide = false;
		}else{
			for(var j = 0; j < qlCache.querySetInfo.length; j++) {
				var col = qlCache.querySetInfo[j];
				if(name == col.name) {
					if(col.checked) {
						isHide = false;
					}
					break;
				}
			}
		}
		
		if(isHide) {
			liObj.hide();
		} else {
			liObj.show();
		}
	})
}
//保存设置信息到数据库中
function saveQuerySetInfo() {
	var url = __ctx + "/oa/system/userCustom/saveQuerySetInfo.do";
	$.ajax({
		type: "POST",
		url: url,
		data: {
			displayId: qlCache.displayId,
			querySetInfo: JSON2.stringify(qlCache.querySetInfo)
		},
		dataType: "text",
		async: false,
		success: function(result) {
			var obj = new com.ibms.form.ResultMessage(result);
			if(obj.isSuccess()) {
				$.ligerDialog.success(obj.getMessage(), "提示信息");
			} else {
				$.ligerDialog.err(obj.getMessage(), "提示信息");
			}
		}
	});
}
//获取用户设置信息
function initQuerySetInfo() {
	if($.isEmpty(qlCache.querySetInfo)) {
		var url = __ctx + "/oa/system/userCustom/getQuerySetInfo.do?";
		if($.isEmpty(qlCache.displayId)) {
			qlCache.displayId = jQuery.getParameter("__displayId__");
		}
		$.ajax({
			type: "POST",
			url: url,
			data: { displayId: qlCache.displayId },
			dataType: "text",
			async: false,
			success: function(result) {
				var data = jQuery.parseJSON(result);
				qlCache.querySetInfo = data;
			}
		});
	}
}

/***********************    表头自定义设置  ***********************/
/**
 * 事件绑定
 */
function initClickEvent(){
	//点击显示
	$('.table-manage').unbind();
	$('.table-manage').bind('click',function(){
		if(qlCache.isSave){
			var top = $(this).parents('.panel-body').offset().top + 50;
			$('.user-defined-table').css('top',top+'px');
			$('.user-defined-table').toggleClass('dis');
			if(!$('.user-defined-table').hasClass('dis')){
				$('.table-pop').show();
				showColData();
			}else{
				$('.table-pop').hide();
			}	
		}else{
			$.ligerDialog.warn("数据未保存！", "提示信息");
		}
	})
	
	$('.table-pop').on('click',function  () {
		var curFieldName = $('.col-filter').find('li').eq(2).attr('name')
		$('th[name='+curFieldName+']').find('a.link').removeClass('shouqi1').addClass('shouqi2');
		$(this).hide();
		$('.user-defined-table').addClass('dis');
		$('.col-filter').hide();
		$('.line-value-filter-table').hide();
		showOrHide(curFieldName,true);
	})
		
	//选择显示
	$('.user-defined-table').off('click','.thead-title')
	$('.user-defined-table').on('click','.thead-title',function(){
		qlCache.isSave = false;
		var name = $(this).attr('name');
		var index = $('th[name="'+name+'"]').index();
		$(this).toggleClass('checked')
		if($(this).hasClass('checked')){
			$('table tr').find('th:eq('+index+')').show()	
			$('table tr').find('td:eq('+index+')').show()	
		}else{
			$('table tr').find('th:eq('+index+')').hide()
			$('table tr').find('td:eq('+index+')').hide()
		}
	})
	
	//保存自定义设置
	$('.user-defined-table').off('click','.table-btn-save');
	$('.user-defined-table').on('click','.table-btn-save',function(){
		var displaySetInfo = [];
		$('.thead-title').each(function(index,data){
			if($(data).hasClass('checked')){
				displaySetInfo.push({name:$(data).attr('name'),desc:$(data).attr('title'),checked:true})
			}else{
				displaySetInfo.push({name:$(data).attr('name'),desc:$(data).attr('title'),checked:false})
			}
			
		})
		saveColSet($('.user-defined-table'),displaySetInfo)
	});
	
	//点击取消按钮
	$('.user-defined-table').off('click','.table-btn-del');
	$('.user-defined-table').on('click','.table-btn-del',function(){
		if(qlCache.isSave){
			$('.user-defined-table').addClass('dis');
			$('.table-pop').css('display','none');
		}else{
			$.ligerDialog.warn("数据未保存！", "提示信息");
		}
	});
}
/**
 * 获取数据时判断列是否显示
 */
function colIsShow(){
	$.ajax({
		type:"get",
		url:__ctx + "/oa/system/userCustom/getDisplaySetInfo.do",
		async:true,
		data:{displayId:qlCache.displayId},
		success:function(res){			
			$(JSON.parse(res)).each(function(index,data){
				if(!data.checked){
					var name = data.name;
					var index = $('th[name="'+name+'"]').index();
					$('table tr').find('th:eq('+index+')').hide();
					$('table tr').find('td:eq('+index+')').hide();
				}
			})
		}
	});
}
/**
 * 加载显示的列
 */
function showColData(){
	$.ajax({
		type:"get",
		data:{displayId:qlCache.displayId},
		dataType:"json",
		url:__ctx + "/oa/system/userCustom/getDisplaySetInfo.do?",
		success:function(data){
			var html = '';
			$(data).each(function(index,data){
				if(data.checked){
					html += '<div class="thead-title checked" index="'+index+'" title="'+data.desc +'" name="'+data.name+'">'+data.desc+'</div>'
				}else{
					html += '<div class="thead-title" index="'+index+'" title="'+data.desc +'" name="'+data.name+'">'+data.desc+'</div>'
				}
			})
			
			html += '<div class="panel-toolbar table-btn"><div class="toolBar">'
				  + '<div class="group">'
				  +	'  <a class="link save table-btn-save">保存</a></div>'
				  + '<div class="group">'
				  + '  <a class="link del table-btn-del">取消</a></div></div></div>'
				  
			$('.user-defined-table').html(html)
		},
		error:function(err){
			$.ligerDialog.error("加载显示列失败！", "提示信息");
		}
	});
	
}
/**
 * 保存更改显示列后的内容
 */
function saveColSet(obj,arr){
	$.ajax({
		type:"post",
		url:__ctx + "/oa/system/userCustom/saveDisplaySetInfo.do?",
		async:true,
		data:{displayId:qlCache.displayId,
				displaySetInfo:JSON2.stringify(arr)			
			},
		success:function(data){
			$.ligerDialog.success(JSON.parse(data).message, "提示信息");
			obj.addClass('dis');
			qlCache.isSave = true;
			$('.table-pop').hide();
		},
		error:function(err){
			$.ligerDialog.success("保存失败！", "提示信息");
		}
	});
}

/***********************    列值过滤自定义设置  ***********************/

function initLvfClickEvent(fieldName){
	$('.line-value-filter-table').off('click','.table-btn-save');
	$('.line-value-filter-table').on('click','.table-btn-save',function(){	
		//change
		var index = $('.col-filter').find('li').eq(2).attr('num');
		// var 
		if (qlCache.ary.join('').indexOf(index)==-1) {
			qlCache.ary.push(index);
		} else{
			null;
		}	
		var obj =$('.link.ajaxSearch');
		var form = $('.line-value-filter-table').find("form[name='lineValueForm']");
		//验证是否有效的规则
		var rtn=form.valid();
		if(!rtn){
			$.ligerDialog.warn("请检查输入查询条件是否有效！");
			return;
		}
		var href=form.attr("action");
		lvfSearcherAjax(obj,href,fieldName);
	});
	$('.line-value-filter-table .table-btn-del').unbind('click');
	$('.line-value-filter-table').on('click','.table-btn-del',function(){
		showOrHide(fieldName,true);
		$('.line-value-filter-table').hide();
	});
}
/**
 *表头子弹点击后弹出的选项卡
 * */
function showUL(obj){
	$(obj).removeClass('shouqi2').addClass('shouqi1')
	var thObj = $(obj).parents("th");
	//change
	var index = thObj.index();
	$('.col-filter').find('li').eq(2).attr('num',index);
	
	
	
	var fieldName = thObj.attr("name");
	$('.col-filter').find('li').eq(2).attr('name',fieldName);	
	var left = thObj.offset().left + thObj.width()- 89 + $('.panel-body').scrollLeft() ;
	$('.col-filter').css('left',left+'px');
	$('.col-filter').is(':hidden')?$('.col-filter').show():$('.col-filter').hide();
	$('.line-value-filter-table').is(':hidden')?null:$('.line-value-filter-table').hide();	
	//悬浮框位置判断
	var left_xf = left - $('.panel-body').scrollLeft();
	var width = $('.panel-body').width();
	var right_xf = width - left_xf;
	var top_xf = $(obj).offset().top;
	$('.line-value-filter-table').css('top',top_xf+31+'px');
	if (left_xf>right_xf) {
		$('.line-value-filter-table').css('left',left_xf-315+'px');
	} else{
		$('.line-value-filter-table').css('left',left_xf+100+'px')
	}
	//table-pop的显示隐藏
	$('.table-pop').show();
}
/**
 * 列值过滤设置窗口展示
 * @returns {Boolean}
 */
function showLvfp(){	
	$('.line-value-filter-table').fadeIn();
	if(!qlCache.lineValueFilter.init){
		if(typeof initLineValueFilterHtml=="function"){
			initLineValueFilterHtml(defaultInfo.lvform);
		}
		qlCache.lineValueFilter.init=true;
	}
	var lvf = qlCache.lineValueFilter;
	var fieldName = $('.col-filter').find('li').eq(2).attr('name');	
	initLvfClickEvent(fieldName);
	if(lvf[fieldName]&&lvf[fieldName]==true){
		showOrHide(fieldName,false);
	}else{
		var url = __ctx + "/oa/system/userCustom/lvfField.do?"
		url += "displayId=" + qlCache.displayId;
		url += "&fieldName=" + fieldName;
		url = url.getNewUrl();
		$.ajax({
			type:"get",
			url:url,
			async:true,
			success:function(data){
				var obj =  $.parseJSON(data);
				obj.lvform = defaultInfo.lvform;
				appendFieldHtml(obj);
				if(obj.fieldType=="date"){
					$('.wdateTime').live('focus',function(){
						var me = $(this), dateFmt=  (me.attr('datefmt')?me.attr('datefmt'):'yyyy-MM-dd');
						WdatePicker({dateFmt:dateFmt,alwaysUseStartDate:true});
						me.blur();
					});
				}
				showOrHide(fieldName,false);
				lvf[fieldName]=true;
			},
			error:function(err){
				$.ligerDialog.error("失败！", "提示信息");
			}
		});
	}
	return false;
}
/**
 * @param name:fieldName
 * @param isHide : false/true
 */
function showOrHide(name,isHide){
	var ulObj = $('#'+defaultInfo.lvform+' ul[name='+name+']');
	var buttonObj= $('#'+defaultInfo.lvform).find('.panel-toolbar.table-btn');
	if(isHide){
		ulObj.hide();
		buttonObj.hide();
	}else{
		ulObj.show();
		buttonObj.show();
	}
}

/**
 * 根据列值过滤信息进行过滤
 * @param obj ： form
 * @param url ： form的action属性
 * @param fieldName ： 字段名称
 */




//局部清空查询区域
function clearPart(abc) {
	//lvForm清空
	$('#' + defaultInfo.lvform).find('ul[name = '+abc+']').remove();
	qlCache.lineValueFilter = {init:false};
	var obj =$('a.link.ajaxSearch');
	handlerSearchAjax(obj);
}



function lvfSearcherAjax(obj,url,fieldName){
	showOrHide(fieldName,true);
	//查询参数
	var lvfForm = $('.line-value-filter-table').find("form[name='lineValueForm']");
	var params = getLvfParams(lvfForm);	
	var searchForm = $("div.panel-top").find("form[name='searchForm']");
	//序列化查询参数 serializeObject方法在下面
	var Sparams = serializeObject(searchForm);
	//合并两个查询区域的参数
	for(var prop in Sparams){
		params[prop] = Sparams[prop];
	}
	
	//获取查询列中的所有数据字典选项
	var dictionaryList = new Array();
	getDicCheckedValue(dictionaryList);	
	//业务数据模板  显示的最外层Div
	var container = $(obj).closest("div[ajax='ajax']");
	//业务数据模板  显示组件的ID
	var displayId=container.attr("displayId");
	var filterKey=container.attr("filterKey");
	//DBom管理-dbom使用：树节点拼接SQL语句
	var dbomSql=container.attr("dbomSql");
	//DBom管理-dbom使用：子表新增表单时，存储外键字段名称，格式为m:Table1:Field1
	var dbomFKName =container.attr("dbomFKName");
	//DBom管理-dbom使用：子表新增表单时，存储外键字段值
	var dbomFKValue =container.attr("dbomFKValue");
	
	params.__displayId=displayId;
	params.__filterKey__ = filterKey;
	params.__dbomSql__ = dbomSql;
	params.__dbomFKName__ = dbomFKName;
	params.__dbomFKValue__ = dbomFKValue;
	TipsOps.loadingTable();
	//提交到后台，取得自更新后的Html元素，并替换掉之前的html元素
	$.post(url,params,function(data){
		//登录失效后继续操作，无法访问该路径，data.success为未定义，重新加载页面 by liubo
		if(data.success == undefined){
			$.ligerDialog.error("对不起，您的登录已过期，即将重新登录!", "提示信息",function(){
				window.location.reload();
			});
		}else if(data.success){
			container.replaceWith(data.html);
			$('table thead .group').append('<span class="extBtn iconfont icon-refresh" style="display:none;"></span>');
			
			//change
			var index = $('.col-filter').find('li').eq(2).attr('num');
			index = parseInt(index);
			var array = qlCache.ary;
			sessionStorage.setItem('columnFilter',array);
			for (var i = 0; i < array.length; i++) {
				$('.panel-table thead tr th').eq(array[i]).find('.extBtn').css('display','inline-block');
			}
			$('.extBtn').click(function(){
				var this_ = $(this);
				var p = this_.parents('th');
				var exName = p.attr('name');
				pIndex = p.index();
				pIndex+='';
				var nIndex = array.join('').indexOf(pIndex);
				array.splice(nIndex,1);
				sessionStorage.setItem('columnFilter',array);
				clearPart(exName);
			})
			// tableScroll (5);
			
			
			////////////////////////////
			//取出多余的line-value-filter-table
			//初始化数据字典项的样式
			getDicCheckedValue(dictionaryList);
			//TipsOps.pageAnimate(); //yangbo  重现判断页脚
			//TipsOps.panelBody();//重新刷新panel-body div层 yangbo
			initReplaceHtml();
			//防止过滤后行数过少，高度把下次过滤的ul遮住；
			$('.panel-body').css('min-height','300px');
		}else{
			$.ligerDialog.err( "提示","出错了",data.msg);
		}
	});
}
/**
 *排序url处理
 */
function sort(obj,sortType,tableIdCode){
	
	var fieldName = $('.col-filter').find('li').eq(2).attr('name');
	var url = $(obj).attr("action");
	var map = url.getArgs();
	if(url.indexOf(tableIdCode+'o')==-1){
		url +="&"+tableIdCode+"o="+sortType;
	}else{
		var oldSort = map[tableIdCode+"o"];
		url = url.replace("="+oldSort,"="+sortType);
	}
	if(url.indexOf(tableIdCode+'s')==-1){
		
		url +="&"+tableIdCode+"s="+fieldName;
	}else{
		var oldFieldName = map[tableIdCode+"s"];
		url = url.replace("="+oldFieldName,"="+fieldName);
	}
	if(url.indexOf('__isQueryData')==-1){
		url +="&__isQueryData="+true;
	}
	if(url.indexOf(tableIdCode+'__flag__')==-1){
		url +="&"+tableIdCode+"__flag__="+true;
	}
	//url处理，去除带lvf的参数
	if(url.indexOf("?")>-1){
		var params = url.getArgs();
		var basicUrl = url.substr(0,url.indexOf("?"));
		for(var prop in params){
			var val = params[prop];
			if(prop.indexOf("LVF_")>-1){
				continue;
			}
			if(basicUrl.indexOf("?")==-1){
				basicUrl+="?"+prop+"="+val;
			}else{				
				basicUrl+="&"+prop+"="+val;
			}
		}
		url = basicUrl;
	}
	showOrHide(fieldName,true);
	updateAjax(obj,url);
}




