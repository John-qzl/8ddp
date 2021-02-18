if(typeof ad =="undefined"){
	var ad = {colorWhere:"",isExpand:false};
	var urlPool = {
			add : __ctx+"/oa/system/advancedQuery/dialog.do?displayId=var1",
			edit: __ctx+"/oa/system/advancedQuery/dialog.do?displayId=var1&queryKey=var2",
			del: __ctx+"/oa/system/advancedQuery/del.do?displayId=var1&queryKeys=var2",
			query: __ctx+"/oa/system/advancedQuery/dialog.do?displayId=var1&queryKey=var2"
	}
}
$(document).ready(function () {
	ad.init();
})
ad.init = function(){
	var hasAdvancedQuery = $('div.compSearchBox').length==0?false:true;
	if(!hasAdvancedQuery){
		return;
	}
	var displayId = $.getParameter("__displayId__");
	changeExpand();
    $('.searchShift>.ss_btn').click(function(){
    	ad.isExpand = ad.isExpand==true?false:true;
    	changeExpand();
    })
    $('.addBtn span').click(function () {
		ad.add(displayId);
	})
	var url = __ctx + "/oa/system/advancedQuery/getAqInfo.do?displayId="+displayId;
	$.post(url,{},function(data){
		if(data==null||data.length==0){return;}	
		for(var i=0;i<data.length;i++){
			var v = data[i];
			var displayId = v.displayId;
			var queryKey = v.queryKey;
			var queryName = v.queryName;
			append(displayId,queryName,queryKey);
		}
		var key = ad.colorWhere;
		var obj = $('div.csb_btn[queryKey='+key+']').find('div.csb_btnCon');
		obj.addClass('clickColor');
	});
}
ad.add =function(displayId){
	var url = urlPool.add;
	url = url.replace("var1",displayId);
	DialogUtil.open({
		height:800,
		width: 1000,
		url: url,
		showMax: false,                             //是否显示最大化按钮 
	    showToggle: false,                         //窗口收缩折叠
	    title: "添加高级查询",
	    showMin: false,
		sucCall:function(rtn){
			if(rtn.success){
				var displayId = rtn.data.displayId;
				var queryName = rtn.data.queryName;
				var queryKey = rtn.data.queryKey;
				append(displayId,queryName,queryKey);
			}else{
				 $.ligerDialog.error("添加高级查询失败！");
			}
		}
	});
}
ad.del = function(obj){
	var displayId = $(obj).parents('div.csb_btn').attr('displayId');
	var querykey = $(obj).parents('div.csb_btn').attr('queryKey');
	var url = urlPool.del;
	url = url.replace("var1",displayId).replace("var2",querykey);
	$.ajax({
		url : url,
		data : {},
		async : false, //{false：同步加载，true：异步加载} 
		type : 'post',
		success : function() {
			$(obj).parents('div.csb_btn').remove();
		}
	});
}
ad.edit = function(obj){	
	var displayId = $(obj).parents('div.csb_btn').attr('displayId');
	var querykey = $(obj).parents('div.csb_btn').attr('queryKey');
	var url = urlPool.edit;
	url = url.replace("var1",displayId).replace("var2",querykey);
	DialogUtil.open({
		height:800,
		width: 1000,
		url: url,
		showMax: false,                             //是否显示最大化按钮 
	    showToggle: false,                         //窗口收缩折叠
	    title: "添加高级查询",
	    showMin: false,
		sucCall:function(rtn){
			if(rtn.success){
				var displayId = rtn.data.displayId;
				var queryName = rtn.data.queryName;
				var queryKey = rtn.data.queryKey;
				$(obj).parents('div.csb_btn').attr('displayId',displayId);
				$(obj).parents('div.csb_btn').attr('queryKey',queryKey);
				$(obj).parents('div.csb_btn').find('div.csb_btnCon').text(queryName);
			}else{
				 $.ligerDialog.error("添加高级查询失败！");
			}
		}
	});
}
ad.doQuery = function(obj){
	var isQuery = colorManage(obj)
	var searchObj = $(obj).parents('div.panel-top').find('.link.ajaxSearch');
	var querykey = "";
	if(isQuery){
		querykey = $(obj).parents('div.csb_btn').attr('queryKey');
	}
	handlerAdvancedQuery(searchObj,querykey);
}

function append(displayId,queryName,queryKey){	
	var csb_btn = $('<div class="csb_btn" displayId="'+displayId+'" queryKey="'+queryKey+'"></div>');
    csb_btn.append($('<div onclick="ad.doQuery(this)" class="csb_btnCon">'+queryName+'</div>'));
    var csb_btnOpe = $('<div class="csb_btnOpe"></div>');
    csb_btnOpe.append('<p onclick="ad.edit(this)"><span class="icon-survey1"></span>编辑</p>');
    csb_btnOpe.append('<p onclick="ad.del(this)"><span class="icon-delete"></span>删除</p>');
    csb_btn.append(csb_btnOpe);
    $('.csb_btnbox').append(csb_btn);
	$('.csb_btn').mouseenter(function(){
		var _this  = $(this);
		setTimeout(function(){
			var leftX = _this.find('.csb_btnCon').outerWidth();
			_this.find('.csb_btnOpe').css({'left':leftX});
			_this.find('.csb_btnOpe').show();
		},500);
	}).mouseleave(function(){$('.csb_btnOpe').hide()})
}

function colorManage(argu) {
	var flag = true;
	if ($(argu).hasClass('clickColor')) {
		$(argu).removeClass('clickColor');
		ad.colorWhere = "";
		flag =false;
	} else {
		var key = $(argu).parents('div.csb_btn').attr('queryKey');
		ad.colorWhere = key;
		$('.csb_btnCon').removeClass('clickColor');
		$(argu).addClass('clickColor');
	}
	return flag;

}
/**
 * 根据ad.isExpand值进行展开收缩
 */
function changeExpand() {
	var bol =  ad.isExpand;
	if (bol) {
		 $('.panel-complex-search .compSearchBox').show();
		 $('#searchForm').hide();
		 $('.ss_btn').text('切换成简单搜索');
		 expandHeight();
		 
	} else{
		 $('.ss_btn').text('切换成高级搜索');		
		 $('#searchForm').show();		
		 $('.panel-complex-search .compSearchBox').hide();		
		 expandHeight();
	}
}
//点击收缩时panel-body高度变化控制
function expandHeight(){
	$('.search_box').show();
	$('.searchShift label').text('收缩');
	var panelBody = $('.panel-complex-search').parents('.panel-top').next(".panel-body");
	var panelTopH = panelBody.prev('.panel-top').outerHeight();
	var panelH = panelBody.closest('.panel').height();
	panelBody.height(panelH - panelTopH -51);
}

