if (typeof Export == 'undefined') {
	Export = {};
	Export.colSetting = {};
}
/**
 * 初始化导出
 */
Export.initExportMenu = function (){
	
	//判断是否有指定导出列表格式为: menus="[{text:'导出CTC量具',templateName:'量具管理台帐_LJ'},{text:'导出CTC设备',templateName:'量具管理台帐_SB'}]"
	var set_menus = $("div.exportMenu").attr("menus");
	//判断是否增加文件后缀划分，表单设计界面，是否有分类界面，以__filterKey__参数值为区分
	var isAddFilter = $("div.exportMenu").attr("isAddFilter");
	//在表单模板中设置，导出全部数据名称,可设置或者不设置，如果设置了名称，优先界面设置，如果不设置名称，默认为:表单设计名称_LIST
	var templateName = $("div.exportMenu").attr("templateName");
	//判断是否为undefined
	if(templateName==null||templateName==undefined||templateName=="undefined"||templateName==''){
		templateName = "";
	}
	//下拉列表值
	//var exportMenusJson = "[{ text: '导出全部数据(FR)',click:function(){ Export.exportFR(this,0,'"+templateName+"',"+isAddFilter+");}}";
	//下拉列表值-全部数据
	var exportMenusJson = "[{text: '导出全部数据(EXS)',click:function(){ Export.setExportCol(this,0);}}";
	//下拉列表值-选中导出
	exportMenusJson += ",{text: '导出选中数据(EXS)',click:function(){ Export.setExportCol(this,1);}}";
	//下拉列表值-部分导出
	exportMenusJson += ",{text: '导出当前页数据(EXS)',click:function(){ Export.setExportCol(this,2);}}";
	
	exportMenusJson += ",{text: '导出列表关联附件',click:function(){ Export.exportExcel(this,3);}}";
		//判断是否为null
	if(!(set_menus == null || set_menus==undefined || set_menus=="undefined" ||set_menus=="")){
		var set_menus_json = eval("(" + set_menus + ")");
		//循环列表
		for(var i=0;i<set_menus_json.length;i++){
			var menusObj = set_menus_json[i];
			exportMenusJson+=",{ text: '"+menusObj.text+"',click:function(){ Export.exportFR(this,0,'"+menusObj.templateName+"',"+isAddFilter+");}}"
		}
	}	
	exportMenusJson+="]";
	//导出字段初始化
	this.initExportCol();
    var menu1 = { width: 160, 
    	items:eval("(" + exportMenusJson + ")")
    };
	    
    $("div.exportMenu").ligerMenuBar({
			items: [
     			  { text: '<a href="####" class="link export"><span></span>导出&nbsp;&nbsp;</a>', menu: menu1 }
                  ]});
    $("div.exportMenu").removeClass('l-menubar');
    $("div.l-menubar-item-down").css("right","6px");
    $("div.l-menubar-item").css("padding-right","0");
};


var exportDialog=null;
//json: {colName1:true,}
Export.initExportCol = function (){
	/*$('#exportField input[name="field"]').each(function(){
		var name = $(this).val();
		var desc = $(this).parents("td").next().text();		
		Export.colSetting[name] = {checked:true,desc:desc};
	})*/
	//从数据库中获取，exportField中的导出字段不准确（权限与显示字段的相同）
	$.ajax({
		url : __ctx + '/oa/system/userCustom/getExportField.do',
		data : {
			displayId:jQuery.getParameter("__displayId__")
		},
		type : 'post',
		success: function(result){
			var fields = JSON2.parse(result);
			if($.isEmpty(fields)||fields.length==0){
				$('#exportField input[name="field"]').each(function(){
					var name = $(this).val();
					var desc = $(this).parents("td").next().text();		
					Export.colSetting[name] = {checked:true,desc:desc};
				});
			}else{
				for(var i=0;i<fields.length;i++){
					var name = fields[i].name;
					var desc = fields[i].desc;
					var checked = fields[i].checked;
					if(checked){
						Export.colSetting[name] = {checked:true,desc:desc};
					}
				}
			}
			
		}
	});
}
Export.setExportCol = function (obj,exportType){
	//多表的
	if(window.location.href.indexOf("querySetting")){
    	Export.exportExcel(obj,exportType); 
	}else{
		var url = "/ibms/oa/form/dataTemplate/exportFieldSet.do?"
			var colSetting = JSON2.stringify(Export.colSetting);
			//console.log(colSetting);
			var colSettingStr = encodeURI(colSetting);
			//console.log(colSetting);
			url+="colSetting="+colSettingStr;
			DialogUtil.open({
		        height:"500",
		        width : "400",
		        title : '过滤字段设置',
		        url: url, 
		        //自定义参数
		        sucCall:function(rtn){
		        	Export.colSetting = rtn.colSettingInfo;
		        	Export.exportExcel(obj,exportType);
		        }
		    });
	}
}
/**
 * 设置导出字段
 */
Export.setExportField = function (obj,exportType){
	var exportIds = "";
	
	$('input[type="checkbox"][class="pk"]:checked').each(function() {
			exportIds += $(this).val() + ",";
	});
	if (exportIds != "")
		exportIds = exportIds.substring(0, exportIds.length - 1);
	if(exportType == 1 && exportIds==""){
		alert("请选择导出的！");
		return;
	}
		
	if(exportDialog==null){
		Export.initExportDialog();
		exportDialog=$.ligerDialog.open(
			{title:'导出字段',
			target:$("#exportField"),
			width:400,height:250,
			buttons:
				[ {text :'导出',onclick: function(){
					Export.exportExcel(obj,exportType,exportIds,false);
				}},
				{text :'取消',onclick: function (item, dialog) {
						dialog.hide();
					}
				}]});	
	}
	exportDialog.show();
}

/**
 * 导出初始化
 */
Export.initExportDialog =function (){
	Export.handlerCheckAll();
}

Export.handlerCheckAll =function(){
	$("#checkFieldAll").click(function(){
		var state=$(this).attr("checked");
		if(state==undefined)
			Export.checkFieldAll (false);
		else
			Export.checkFieldAll (true);
	});
}

Export.checkFieldAll =function (checked){
	$("input[type='checkbox'][class='field']").each(function(){
		$(this).attr("checked", checked);
	});
}

/**
 * 通过Finreport模板导出报表
 * @param {} obj
 * @param {} exportType
 * @param {} exportIds
 */
Export.exportFR =function (obj,exportType,templateName,isAddFilter){
	var me = $(obj), params = {};
	var exportIds = "";
	
	$('input[type="checkbox"][class="pk"]:checked').each(function() {
			exportIds += $(this).val() + ",";
	});
	if (exportIds != "")
		exportIds = exportIds.substring(0, exportIds.length - 1);
	if(exportType == 1 && exportIds==""){
		alert("请选择导出的记录！");
		return;
	}
		//导出的分页
	var page = $('.panel-page');
	var tableIdCode =page.find("#tableIdCode").val(),
		p = page.find("#currentPage"+tableIdCode),
		z = page.find("#oldPageSize"+tableIdCode),
		totalPage = page.find("#totalPage"+tableIdCode);
	if(exportType == 2 && totalPage.val()<=0){
		alert("当前页没有记录！");
		return;
	}
	
	//查询的条件
	var searchForm = $("div.panel-top").find("form[name='searchForm']");
	params = serializeObject(searchForm);
	//自定义显示的最外层Div
	var container = $("div[ajax='ajax']");
	//自定义显示组件的ID
	var displayId=container.attr("displayId");
	var filterKey=container.attr("filterKey");
	
	//params.__displayId__=displayId;
	//params.__filterKey__ = filterKey;


	params.p= p.val();
	params.z= z.val();
	params.oz= z.val();
	//导出的选项
	//params.__exportIds__= exportIds;
	//params.__exportType__= exportType;

	//var form = $('#exportForm');
	//form.empty();
	//生成条件语句
	var sqlStr = "";
	for(var key in params){
		sqlStr+="&"+key+"="+params[key];
		//var input = $("<input type='hidden' name='"+key+"' value='"+params[key]+"'/>");
		//form.append(input);
    }
	//if(exportType == 1 ||exportType == 2){
		//form.submit();
	//}else{
		var url="/ibms/oa/form/dataTemplate/exportData.do?__displayId__="+displayId+"&__filterKey__="+filterKey+"&__exportType__="+exportType+"&templateName="+templateName+"&isAddFilter="+isAddFilter+sqlStr+"&isFR=1";
		
		DialogUtil.open({
			height:height,
			width: width,
			title : '报表',
			url: url, 
			isResize: true,
			//自定义参数
			sucCall:function(rtn){
				//刷新原来的当前的页面信息
				if(!(rtn == undefined || rtn == null || rtn == '')){
					locationPrarentPage();
				}	
			}
		});
	
}
/**
 * 导出Excel
 * @param {} obj
 * @param {} exportType
 * @param {} exportIds
 * @param {} 前置后置事件获取的索引 
 */
Export.exportExcel =function (obj,exportType,keyName){
	$.ligerDialog.waitting(" ");
	if(keyName){
		try{
			getPreScript(keyName);
		}catch(e){
			console.log(e);
			alert("前置脚本执行出错！");
		}
	}
	var me = $(obj), params = {};
	var exportIds = "";
	
	$('input[type="checkbox"][class="pk"]:checked').each(function() {
			exportIds += $(this).val() + ",";
	});
	if (exportIds != "")
		exportIds = exportIds.substring(0, exportIds.length - 1);
	if(exportType == 1 && exportIds==""){
		alert("请选择导出的！");
		$.ligerDialog.closeWaitting();
		return;
	}
	//导出的分页
	var page = $('.panel-page');
	var tableIdCode =page.find("#tableIdCode").val(),
		p = page.find("#currentPage"+tableIdCode),
		z = page.find("#oldPageSize"+tableIdCode),
		totalPage = page.find("#totalPage"+tableIdCode);
	if(exportType == 2 && totalPage.val()<=0){
		alert("当前页没有记录！");
		$.ligerDialog.closeWaitting();
		return;
	}
	
	//查询的条件
	var searchForm = $("div.panel-top").find("form[name='searchForm']");
	params = serializeObject(searchForm);
	//自定义显示的最外层Div
	var container = $("div[ajax='ajax']");
	//自定义显示组件的ID
	var displayId=container.attr("displayId");
	var filterKey=container.attr("filterKey");
	
	params.__displayId__=displayId;
	params.__filterKey__ = filterKey;


	params.p= p.val();
	params.z= z.val();
	params.oz= z.val();
	//导出的选项
	params.__exportIds__= exportIds;
	params.__exportType__= exportType;

	//将dbom过滤条件加上
	var dbomSql=container.attr("dbomsql");
	params.__dbomSql__ = dbomSql;
	//用户自定义导出字段
	params.colSetting = JSON2.stringify(Export.colSetting);


	//当选择导出全部和当前分页时清空过滤条件  exportType为0,2
	if(exportType==0||exportType==2)    {
		params.Q_gw_S="";
		params.Q_sjlx_S="";
		params.Q_sjmc_S="";
		params.Q_zxzt_S="";
	}

	//导出当前分页时，获取当前分页全部ID，按照导出选中记录的方式导出
	if(exportType==2)    {
		var exportIds2 = "";
		$('input[type="checkbox"][class="pk"]').each(function() {
			exportIds2 += $(this).val() + ",";
		});
		//导出的选项
		params.__exportIds__= exportIds2;
		params.__exportType__= 1;
	}


	var form = $('#exportForm');
	form.empty();
	for(var key in params){
		var paramValue = params[key];
		
		if(paramValue!=null&&typeof(paramValue)=="string"){
			//将字符串中的单引号换成双引号，避免在解析时出错
			paramValue = paramValue.replace(/'/g,'"');
		}
		var input = $("<input type='hidden' name='"+key+"' value='"+paramValue+"'/>");
		form.append(input);
    }	
	form.ajaxForm({success:showResponse});
	form.submit();
}
function showResponse(responseText) {
	$.ligerDialog.closeWaitting();
	var obj = new com.ibms.form.ResultMessage(responseText);
	if (obj.isSuccess()) {
		var path = obj.data.path;
		if(path!=null||path!=""){
			path = encodeURI(path);
		}else{
			$.ligerDialog.error("返回的下载地址无效！","提示信息");
			return;
		}
		var url = __ctx+ "/oa/system/sysFile/downLoadTempFile.do"
		url+="?tempFilePath="+path;
		window.location.href =  url;//下载文件
	} else {
		$.ligerDialog.err(obj.getMessage(),"提示信息");
	}
}