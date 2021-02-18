$(function(){
	$("a.link.search.ajax").unbind("click");
});
//处理删除一行
function goPageAjax(obj,n,tableIdCode){
	var url = $("#_nav"+tableIdCode).attr('href');
	url = replacecurrentPage(url,n,tableIdCode);
	//在page.ftl中，$("#oldPageSize"+tableIdCode)的值被设为当前的分页大小
	url = replagePageSize(url,$("#oldPageSize"+tableIdCode).val(),tableIdCode);
	url = replageOldPageSize(url,$("#oldPageSize"+tableIdCode).val(),tableIdCode);
	updateAjax(obj,url);
}
//首页
function firstAjax(obj,tableIdCode){
	goPageAjax(obj,1,tableIdCode);
}
//尾页
function lastAjax(obj,tableIdCode){
	var lastPage=parseInt($("#totalPage"+tableIdCode).val());
	if(lastPage<=0) return;
	goPageAjax(obj,lastPage,tableIdCode);
}
//上页 
function previousAjax(obj,tableIdCode){
	var currentPage=parseInt($("#currentPage"+tableIdCode).val());
	currentPage-=1;
	if(currentPage<1)currentPage=1;
	goPageAjax(obj,currentPage,tableIdCode);
}
//下页 
function nextAjax(obj,tableIdCode){
	var currentPage=parseInt($("#currentPage"+tableIdCode).val());
	var totalPage=parseInt($("#totalPage"+tableIdCode).val());
	if(totalPage<=0) return;
	currentPage+=1;
	if(currentPage>totalPage)currentPage=totalPage;
	goPageAjax(obj,currentPage,tableIdCode);
}
//修改每页显示记录数
function changePageSizeAjax(obj,tableIdCode){
	var url=$("#_nav"+tableIdCode).attr('href');
	url = replagePageSize(url,obj.value,tableIdCode);
	url = replacecurrentPage(url,$("#currentPage"+tableIdCode).val(),tableIdCode);
	var container = $(obj).closest("div[ajax='ajax']");
	updateAjax(obj,url);
}
/**
 * 跳转至第n页
 */
function jumpToAjax(obj,tableIdCode){
	var currentPage=$("#navNum"+tableIdCode).val();
	var str=/^[1-9]\d*$/;
	if(str.test(currentPage)){
		goPageAjax(obj,currentPage,tableIdCode);
	}else{
		$.ligerDialog.error("非法的页码!");
		$("#navNum"+tableIdCode).focus();
	}
}

// 分页模板下的刷新按钮onclick 如果url中有'' 单引号，js会出错，导致 新增修改后不能刷新
function refreshAjax(obj){
	//把锚点标记先移除出来
	var url=$(obj).attr("src");
	var index=url.indexOf("#");
	if(index!=-1){
		url=url.substring(0,index);
	}
	updateAjax(obj,url);
}

//组件中的点击事件
function linkAjax(obj){
	var $obj = $(obj);
	var url=$obj.attr("action");
	updateAjax(obj,url);
}
function handlerAdvancedQuery(obj,queryKey){
	var searchObj = $(obj).parents('div.panel-top').find('.link.ajaxSearch');
	var form = $(searchObj).closest("div.panel-top").find("form[name='searchForm']");
	//验证是否有效的规则
	var rtn=form.valid();
	if(!rtn){
		$.ligerDialog.warn("请检查输入查询条件是否有效！");
		return;
	}
	var href = form.attr("action");
	href+="?queryKey="+queryKey;
	updateAjax(obj,href);
}
//数据列表页面“查询”按钮事件
function handlerSearchAjax(obj){
	var form = $(obj).closest("div.panel-top").find("form[name='searchForm']");
	//验证是否有效的规则
	var rtn=form.valid();
	if(!rtn){
		$.ligerDialog.warn("请检查输入查询条件是否有效！");
		return;
	}
	//href 默认为 action="/ibms/oa/form/dataTemplate/getDisplay.do">
	var href = form.attr("action");
	updateAjax(obj,href);
}

//业务数据模板列表中的“查询”按钮
function updateAjax(obj,url){
	//查询参数
	var searchForm = $("div.panel-top").find("form[name='searchForm']");
	//获取查询列中的所有数据字典选项
	var dictionaryList = new Array();
	initDicStyle(dictionaryList);
	
	//序列化查询参数 serializeObject方法在下面
	var params = serializeObject(searchForm);
	//fromMultiTabView
	var fromMultiTabView = jQuery.getParameter("fromMultiTabView")
	var funId = jQuery.getParameter("funId");
	var pk = jQuery.getParameter("__pk__");
	var typeAlias = jQuery.getParameter("typeAlias");
	if(fromMultiTabView=="true"&&funId.length>0&&pk.length>0&&typeAlias.length>0){
		if(url.indexOf("fromMultiTabView")==-1){
			params.fromMultiTabView = fromMultiTabView;
		}
		if(url.indexOf("funId")==-1){
			params.funId = funId;
		}
		if(url.indexOf("__pk__")==-1){
			params.__pk__ = pk;
		}
		if(url.indexOf("typeAlias")==-1){
			params.typeAlias = typeAlias;
		}
	}
	//查询参数
	if($('.line-value-filter-table').length>0&&
			typeof getLvfParams =="function"){
		var lvfForm = $('.line-value-filter-table').find("form[name='lineValueForm']");
		var lvfParams = getLvfParams(lvfForm);
		//合并两个查询区域的参数
		for(var prop in lvfParams){
			params[prop] = lvfParams[prop];
		}
	}
	
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
	params.ajax=true;
	params.__displayId=displayId;
	params.__filterKey__ = filterKey;
	params.__dbomSql__ = dbomSql;
	params.__dbomFKName__ = dbomFKName;
	params.__dbomFKValue__ = dbomFKValue;
	/*$.ligerDialog.waitting("加载中...");*/
	TipsOps.loadingTable();
	//提交到后台，取得自更新后的Html元素，并替换掉之前的html元素
	$.post(url,params,function(data){
		/*$.ligerDialog.closeWaitting();*/
		try{
			data=eval("("+data+")");
		}catch(e){
			
		}
		//登录失效后继续操作，无法访问该路径，data.success为未定义，重新加载页面 by liubo
		if(data.success == undefined){
			$.ligerDialog.error("对不起，您的登录已过期，即将重新登录!", "提示信息",function(){
				window.location.reload();
			});
		}else if(data.success){
			
			container.replaceWith(data.html);
			//changelw
			// tableScroll (5);

			$('table thead .group').append('<span class="extBtn iconfont icon-refresh"></span>');
			var index = $('.col-filter').find('li').eq(2).attr('num');
			index = parseInt(index);
			var array = sessionStorage.getItem('columnFilter');
			if(!!array){
				array = array.split(',');
				for (var i = 0; i < array.length; i++) {
					$('.panel-table thead tr th').eq(array[i]).find('.extBtn').css('display','inline-block');
				}
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

			//修复查询后日期选择器无法点击的bug
			$('.wdateTime').live('focus',function(){
				var me = $(this), dateFmt=  (me.attr('datefmt')?me.attr('datefmt'):'yyyy-MM-dd');
				WdatePicker({dateFmt:dateFmt,alwaysUseStartDate:true});
				me.blur();
			});

			//去除多余的line-value-filter-table
			if($('div.line-value-filter-table').length>=2){
				$('div.line-value-filter-table').eq(1).remove();
			}
			//初始化数据字典项的样式
			getDicCheckedValue(dictionaryList);
			initReplaceHtml();
			getPanelBodyHeigt('.panel-body')//重新刷新panel-body div层!!!一定要放在渲染页面的最后面！！！！！
			$('div.PadOperaterMenu').each(function(){
				if(PadOperater){
					PadOperater.init(this);
				}
			})
		}else{
			$.ligerDialog.err( "提示","出错了",data.msg);
		}
	});
}

//初始化数据字典项的样式
function getDicCheckedValue(dictionaryList){
	$("div.panel-top").find('.dicComboBox,.dicComboTree,.dicCombo').each(function() {
		$(this).dicCombo();
	});
	//将原来的查询值重新赋值回去
	if(dictionaryList != undefined && dictionaryList != null && dictionaryList != ""){
		$("div.panel-top").find("form[name='searchForm']").find(".dicComboTree").each(function(i,n){
			n.value = dictionaryList[i];
		});
	}
}

//获取查询列中的所有数据字典选项
function initDicStyle(dictionaryList){
	var $dic = $("div.panel-top").find("form[name='searchForm']").find(".dicComboTree");
	if($dic.length > 0){
		//获取数据字典选中的值并赋到数组中
		$dic.each(function(i,n){
			dictionaryList[i] = n.value;
		});
	}
}

//初始化页面各元素和按钮事件
function initReplaceHtml(){
	//初始化查询
	handleAjaxSearchKeyPress();// 处理回车查询  方法在下面
	//初始化单选
	initDisplaytag();//绑定增、删、改、查、导按钮事件js                   方法在displaytag.js中
	$.initRowOps();//行功能键控件                 方法在jquery插件/jquery/plugins/jquery.rowOps.js中
	TipsOps.opsBtn();//操作按钮浮动窗口
	//初始化查询
	$.initFoldBox();//查询条件是否展开还是收起          方法在ibms/foldBox.js中
	if($.isEmpty(typeof __complexDetail__)){
		__complexDetail__.init();//生成权限按钮，并在url后添加类别信息	
	}
	try{
		//初始化导出菜单
		Export.initExportMenu(); //初始化导出按钮事件            方法在Export.js中
		if(typeof initQueryForm =="function"){
			initQueryForm();
		}
		if(typeof initQueryForm =="function"){
			initClickEvent();//列表绑定事件
		}
		if(typeof colIsShow =="function"){
			colIsShow();
		}
		//高级查询初始化
		if(typeof ad.init =="function"){ad.init();}
	}catch(e){
	}
};

	
/**
 * 处理回车查询
 */
function handleAjaxSearchKeyPress(){
	$(".panel-search :input").keypress(function(e) {
		if(	e.keyCode == 13){//回车
			event.preventDefault();
			$("a.link.ajaxSearch").click();
		}else if(e.keyCode == 27){//ESC
			var searchForm = $("#searchForm");
			if(searchForm)
				searchForm[0].reset();
		}		
    })
    $('.multiple_select').multipleSelect({
    	//初始化下拉多选框的样式
     });
};


/**
 * 序列化查询参数
 * @param {} form
 * @return {}
 */
function serializeObject(form){
	var o = {};
	var a = $(form).serializeArray();
	$.each(a, function() {
	    if (o[this.name]) {
	        if (!o[this.name].push) {
	            o[this.name] = [o[this.name]];
	        }
	        o[this.name].push(this.value || '');
	    } else {
	        o[this.name] = this.value || '';
	    }
	});
	return o;
}

/**
 * 打开关联表选择窗口
 * @param {} obj
 * @param {} width
 * @param {} height
 */
function openTableSelectDialog(conf){
	var keyName = conf.keyName;
	if(keyName){
		try{
			getPreScript(keyName);
		}catch(e){
			console.error(e);
			alert("前置脚本执行出错！");
		}
	}
	var width = conf.width||1000,
		height = conf.height||800,
		isFull =conf.isFull||false,
		isStart = conf.isStart||false,
		title = '关联表选择'||conf.title,
		url=__ctx+"/oa/form/dataTemplate/exportRelTableSelect.do?";
		url+="displayId="+$('div.panel[displayid]').attr('displayid');
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
			}
		});
}

/**
 * 打开窗口链接
 * @param {} obj
 * @param {} width
 * @param {} height
 * @param {} isFull
 * @param {} keyName:前置后置脚本获取的key（1_edit）
 */
function openLinkDialog(conf){
	conf = conf || {};
	var keyName = conf.keyName;
	if(keyName){
		try{
			var rtn = getPreScript(keyName);
			if(rtn!=null&&rtn==false){
				return;  
			}
		}catch(e){
			console.error(e);
			alert("前置脚本执行出错！");
		}
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
		isResize= conf.isResize||false,
		showMax= conf.showMax||false,
		title = conf.title||'';
	if(isFull){
		height=contentHeight;
		width=contentWidth;
	}
	if(title == ''){
		title = '提示';
	}
	if(isStart){
		$.ligerDialog.warn("请先绑定流程！");
		return;
	}
	var url=$(obj).attr("action");
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
	    isResize:isResize,
	    showMax:showMax,
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
				//自动刷新有树的页面情况
				//setTimeout(function(){
				//	parent.isRefresh=true;
				//	parent.reFresh();
				//},0);
				if(window.parent.document.getElementById("treeFresh")) {
					window.parent.document.getElementById("treeFresh").click();
				}
				//刷新原来的当前的页面信息
				locationPrarentPage();
				//如果存在ifrem父页面 存在 dbomtree 存在 reloadSelectNode方法，需要重新load 选中的当前树节点数据
				location.href=location.href.getNewUrl();
			}
		}
	});
}
//明细多tab页面
function openTreePanel(conf){
	var pkId = conf.__pk__;
	var alias = conf.alias;
	var url=$(obj).attr("action");
	url="/ibms/oa/form/dataTemplate/detail.do?alias="+alias+"&pkId="+pkId;
	url=url.getNewUrl();
	//window.open(url);//打开新窗口
	window.location.replace(url);
}

//当执行返回内容时，把当前页面按本次设置的刷新一次
function locationPrarentPage(){
	var btnloadDiv = $("div.l-bar-btnload");  //有刷新样式的DIV
	$("a",btnloadDiv).each(function(){      //有刷新样式的DIV的历遍超链接
		var aLink=$(this);
		var onclickStr = aLink.attr("onclick"); 
		if(onclickStr.indexOf("refreshAjax")>-1){
			aLink.click();
		};
	});
}

//日期控件
function datePicker(obj,type){
	if('yyyy-MM-dd'==type){
		WdatePicker({dateFmt:'yyyy-MM-dd',alwaysUseStartDate:true});
	}else{
		WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',alwaysUseStartDate:true});
	}
	$(obj).blur();
}

//按列排序js
function linkSortAjax(obj,tableIdCode){
	var $obj = $(obj);
	var url=$obj.attr("action");
	var sortField=$obj.attr("sort");
	var orderSeq="DESC";
	var curSortField=$("#sortField"+tableIdCode).val();
	var curOrderSeq=$("#orderSeq"+tableIdCode).val();
	
	if(sortField==curSortField){
		if(curOrderSeq=="DESC"){
			orderSeq="ASC";
		}
	}
	
	url = replaceOrderSeq(url,orderSeq,tableIdCode);
	url = replaceSortField(url,sortField,tableIdCode);

	updateAjax(obj,url);
}



/**
 * 替换url中的sortField参数，用于排序显示
 */
function replaceSortField(url,sortField,tableIdCode){
	//把锚点标记先移除出来
	var index=url.indexOf("#");
	if(index!=-1){
		url=url.substring(0,index);
	}
	var sortFieldParam=tableIdCode+'s';
	//查询的页码需要替换
	var reg=new RegExp(sortFieldParam + '=\\w*');
	if(reg.test(url)){
		url=url.replace(reg,sortFieldParam+'='+sortField);
	}else if(url.indexOf('?')!=-1){
		url+='&'+sortFieldParam+'='+sortField;
	}else{
		url+='?'+sortFieldParam+'='+sortField;
	}
	return url;
}

/**
 * 替换url中的orderSeq参数，用于排序显示
 */
function replaceOrderSeq(url,orderSeq,tableIdCode){
	//把锚点标记先移除出来
	var index=url.indexOf("#");
	if(index!=-1){
		url=url.substring(0,index);
	}
	var orderSeqParam=tableIdCode+'o';
	//查询的页码需要替换
	var reg=new RegExp(orderSeqParam + '=\\w*');
	if(reg.test(url)){
		url=url.replace(reg,orderSeqParam+'='+orderSeq);
	}else if(url.indexOf('?')!=-1){
		url+='&'+orderSeqParam+'='+orderSeq;
	}else{
		url+='?'+orderSeqParam+'='+orderSeq;
	}
	return url;
}


/**
 * 文件夹附件窗口页面
 */
function fileAttachDialog(name,actionUrl){
	var result=true;
	var isSingle=0;
	result=getPreScript(name);
	if(result){
		var conf={
				fileFolder: 0,nodekey:"",dimension:0, maindata: 0, mainfield: "", reldata: "", reldatafield: ""	
		};
		var obj=getParam(name);
		if(obj.fileFolder!=undefined){
			conf.fileFolder=obj.fileFolder;
		}
		conf.nodekey=obj.nodekey;
		if(obj.dimension!=undefined){
			conf.dimension=obj.dimension;
		}
		if(obj.dimensionKey!=undefined){
			conf.dimensionKey=obj.dimensionKey;
		}
		if(obj.maindata!=undefined){
			conf.maindata=obj.maindata;
		}
		if(obj.mainfield!=undefined){
			conf.mainfield=obj.mainfield;
		}
		if(obj.reldata!=undefined){
			conf.reldata=obj.reldata;
		}
		if(obj.reldatafield!=undefined){
			conf.reldatafield=obj.reldatafield;
		}
		var params={
				"fileFolder":conf.fileFolder,
				"nodekey":conf.nodekey,
				"dimension":conf.dimension,
				"dimensionKey":conf.dimensionKey,
				"maindata":conf.maindata,
				"mainfield":conf.mainfield,
				"reldatafield":conf.reldatafield,
				"reldata":conf.reldata
		};
		var url=actionUrl+'&isSingle='+isSingle+'&jsonStr='+JSON.stringify(params);
		url=url.getNewUrl();
		//window.open(url);//打开新窗口
		DialogUtil.open({
			height:900,
			width: 1500,
			title : "附件管理",
			url: url,
			//conf:conf,
			sucCall:function(rtn){
				if(rtn){
					var aft=getAfterScript(name);//后置方法
					if(!aft){
						alert("后置脚本执行出错");
					}
				}
				
			}
		});
	}else{
		alert("前置脚本执行出错");
	}
}

/**
 * 流程监控窗口页面
 */
function processDialog(name,actionUrl){
	var url=actionUrl;
	var params = actionUrl.getArgs();
	//校验此流程是否启动
	var urlCheck = __ctx+"/oa/form/dataTemplate/processIsStart.do";
	var _height = window.top.document.documentElement.clientHeight;
	var _width = window.top.document.documentElement.clientWidth;
	$.ajax({
		  type: "POST",
	      url:urlCheck,
		  data:{
			   defId:params.defId,
			   __pk__:params.__pk__
			   },
		  dataType : "text",
	      async:false,
	      success:function(result){
    		if (result=="false") {	   
    			$.ligerDialog.success("流程未启动","提示信息");
    			return;
    		}else{
    			DialogUtil.open({
					height: _height,
					width: _width,
					title : "流程监控",
					url: url,
					showMax: false,                             //是否显示最大化按钮 
				    showToggle: false,                         //窗口收缩折叠
				    showMin: false,
					sucCall:function(rtn){
						if(rtn){
							alert(rtn);
						}			
					}
				});
    		}
		  }		  
	});
	
}

/**
 * 流程监控启动窗口页面
 */
function processStartDialog(name,actionUrl){
	var result=true;
	result=(name);
	if(result){
		var conf={
				flowKey:""	
		};
		var obj=getParam(name);
		if(obj.flowKey!=undefined){
			conf.flowKey=obj.flowKey;
		}
		var params={
				"flowKey":conf.flowKey
		};
		var url=actionUrl+'&jsonStr='+JSON.stringify(params);
		DialogUtil.open({
			height:900,
			width: 1500,
			url: url,
			showMax: false,                             //是否显示最大化按钮 
		    showToggle: false,                         //窗口收缩折叠
		    showMin: false,
			sucCall:function(rtn){
				if(rtn){
				}
			}
		});
	}
}
/**
 * 明细多Tab功能-校验、调用展示
 */
function multiTabCheck(conf){
	var formKey = conf.formKey;
	var url=__ctx+"/oa/form/dataTemplate/multiTabCheck.do";
	$.ajax({
		  type: "POST",
	      url:url,
		  data:{formKey:formKey},
		  dataType : "text",
	      async:false,
	      success:function(result){
    	 		var data = jQuery.parseJSON(result);
    	 		if(!data.result){
    	 			$.ligerDialog.warn(data.message,'提示信息');
    	 			return;
    	 		}else{
    	 			multiTabView(conf);
    	 		}
		  },
		  error:function(XMLHttpRequest,textStatus,errorThrown){		    	 
	      	$.ligerDialog.error("multiTabCheck请求错误！",'提示信息');
		  }
	});
}
/**
 * 明细多Tab功能-展示
 */
function multiTabView(conf){
	var keyName = conf.keyName;
	if(keyName){
		try{
			getPreScript(keyName);
		}catch(e){
			console.error(e);
			alert("前置脚本执行出错！");
		}
	}
	var obj =conf.scope||this; 
	width= conf.width||1000,
	height=conf.height||800,
	openType = conf.openType||'',
	isFull =conf.isFull||false;
	if(isFull){
		height=window.top.document.documentElement.clientHeight;
		width=window.top.document.documentElement.clientWidth;
	}
	var url=$(obj).attr("action");
	url=url.getNewUrl();
	
	if(openType=='frist'){
		window.open(url);//打开新窗口
	}else if(openType=='second'){
		window.location.replace(url);
	}else{
		DialogUtil.open({
			height:height,
			width: width,
			url: url,
			showMax: true,                             //是否显示最大化按钮 
		    showToggle: true,                         //窗口收缩折叠
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
				}
			}
		});
	}
}
/**
 * 数据行td超链接函数
 * 详细信息：需进行权限控制
 */
function displayTypeClick(conf){
	if($.isEmpty(conf&&conf.type&&conf.scope)){
		return;
	}
	var me = $(conf.scope||this);
	var url = me.attr("action");
	var params = url.getArgs();
	var _height=window.top.document.documentElement.clientHeight;
	var _width=window.top.document.documentElement.clientWidth;
	if(conf.type=="hyperlink"){
		//20200118 zmz 将窗口大小从默认宽1000高全部改成 宽高都是0.8
		//原因是首页的待办看板调了这个方法,无法通过conf传参的方式指定窗口大小
		openLinkDialog({scope:conf.scope,width:_width*0.8,height:_height*0.8,isFull:false});
	}else if(conf.type=="detail"){
		var displayId = params.__displayId__;
		var permissionUrl = __ctx+"/oa/form/dataTemplate/getManagePermission.do";
		permissionUrl+="?displayId="+displayId;
		$.getJSON(permissionUrl,function(result){
			if(result.success){
				if(true){
					openLinkDialog({scope:conf.scope,width:1000,height:800,isFull:false});
				}else{
					$.ligerDialog.warn("没有明细访问权限！",'提示信息');
				}
			}else{
				$.ligerDialog.error("getManagePermission请求出错！",'提示信息');
			}
		});
	}else if(conf.type=="processWatch"){
		processDialog("xxx",url);
	}
}
/**
 * 导出选中的数据
 */
function handlerExpSelect(keyName)
{
	Export.exportExcel(this,1,keyName);
}
/**
 * 导出全部的数据
 */
function handlerExpAll(keyName)
{
	Export.exportExcel(this,0,keyName);
}
/**
 * 导出当页的数据
 */
function handlerExpPage(keyName)
{
	Export.exportExcel(this,2,keyName);
}
//-------------------------------------数据包列表事件定制---------------------------------
/**
 * 数据包详细信息列表: 当数据类型为表单时的弹框事件
 * @param obj
 * @returns
 */
function openFormTemplateLink(obj){
	//console.log("openFormTemplateLink");
	 ;
	DialogUtil.open({
		url:__ctx+"/dp/form/formEntry.do?slid="+obj,
		height: 500,
		width: 800,
		title:"表单录入",
		isResize: true,
		sucCall:function(rtn){	
			 reFresh() ;
		}
	});
}
function openFormManage(obj){

	//console.log(obj);
	DialogUtil.open({
		url:__ctx+"/dp/form/formManage.do?slid="+obj,
		height: 500,
		width: 1000,
		title:"表格预览",
		isResize: true,
		sucCall:function(rtn){	
			 reFresh() ;
		}
	});
}
function openFormRemoveMask(obj){

	//console.log(obj);
	DialogUtil.open({
		url:__ctx+"/dp/form/formManage.do?slid="+obj,
		height: 500,
		width: 1000,
		modal:false,
		title:"表格实例",
		isResize: true,
		sucCall:function(rtn){	
			 reFresh() ;
		}
	});
}
/**
 * 启动数据审批
 * @returns
 */
function startDataProcess(){
	alert("启动数据审批")	
	$.ajax({
		  type: "POST",
	      url:url,
		  data:{
			   defId:params.defId
			   },
		  dataType : "text",
	      async:false,
	      success:function(result){
	      }   		  
	});
}
/**
 * 下载到PAD
 * @returns
 */
function downLoadToPad(){
	var records = [];
	var $aryId = $("input[type='checkbox'][disabled!='disabled'][class='pk']:checked");
	if($aryId.length==0){
		$.ligerDialog.warn("没有选择记录");
		return;
	}
	$aryId.each(function(i){
		var obj=$(this);
		var record = new IbmsRecord("W_DATAPACKAGEINFO",obj.val(),{"F_ZXZT":"进行中"});
		records.push(record);
	});
	var updateService =  new RecordUpdate(records,function(){
		$.ligerDialog.success("状态更新成功","",function(){
			location.href = window.location.href.getNewUrl();
		});
	});
	updateService.update();
}
function updateCustomTableData(obj){
	var records = [];
	var $aryId = $("input[type='checkbox'][disabled!='disabled'][class='pk']:checked");
	if($aryId.length==0){
		$.ligerDialog.warn("没有选择记录");
		return;
	}
	$aryId.each(function(i){
		var obj=$(this);
		var record = new IbmsRecord("W_DATAPACKAGEINFO",obj.val(),{"F_ZXZT":"进行中"});
		records.push(record);
	});
	var updateService =  new RecordUpdate(records,function(result){
		$.ligerDialog.success("状态更新成功","",function(){
			location.href = window.location.href.getNewUrl();
		});
	});
	updateService.update();
}
