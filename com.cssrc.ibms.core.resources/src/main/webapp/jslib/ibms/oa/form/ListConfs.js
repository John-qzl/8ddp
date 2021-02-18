if(typeof Lconf=="undefined"){
	var Lconf = {};
}
/**配置信息初始化*/
Lconf.init = function(listConfs){
	if(listConfs==null){ return false;}
	var confs = listConfs.confs;
	for(var i=0;i<confs.length;i++){
		var listConf = confs[i];
		Lconf.addTr(listConf);
	}
}
Lconf.addTr = function(listConf){
	var trObj = $('table[var=listConfTemplateTable]').find('tr');
	var container = $('#confsTbl tbody');
	var newTrObj = trObj.clone(true,true);	
	newTrObj.find('[var=name]').val(listConf.name);
	newTrObj.find('#dataNum').val(listConf.dataNum);
	newTrObj.find('#displayId').val(listConf.displayId);
	Lconf.DataTemplateChange(newTrObj.find('#displayId'),true);
	newTrObj.find('#advancedQuery').val(listConf.advancedQueryKey);
	container.append(newTrObj);
}
Lconf.addTrDefault = function(conf){
	var trObj = $('table[var=listConfTemplateTable]').find('tr');
	var container = $('#confsTbl tbody');
	var newTrObj = trObj.clone(true,true);
	container.append(newTrObj);	
	Lconf.DataTemplateChange(newTrObj.find('#displayId'));
}
Lconf.save = function(){
	function check(){
		var flag = false;
		$('#confsTbl [var=name]').each(function(){
			var val = $(this).val();
			if(val==""){
				 $.ligerDialog.warn("名称不能为空！");
				 flag=true;
				 return false;
			}
		})
		return flag;
	}
	if(check()){
		return false;
	}
	var listConfs = [];
	$('#confsTbl tbody').children('tr').each(function(){
		var trObj = $(this);
		var listConf = Lconf.tr2Conf(trObj);
		listConfs.push(listConf);
	});
	var confs = {confs:listConfs};

	$.ajax({
		url : __ctx + '/oa/system/listConfs/save.do',
		data : {
			listConfs : JSON2.stringify(confs)
		},
		async : false, //{false：同步加载，true：异步加载} 
		type : 'post',
		success : function(responseText) {
				var obj = new com.ibms.form.ResultMessage(responseText);
				if (obj.isSuccess()) {
					$.ligerDialog.confirm( obj.getMessage()+",是否继续操作","提示信息", function(rtn) {
						if(rtn){
							window.location.href =  location.href.getNewUrl();
						}else{
							dialog.close();
						}
					});					
				} else {
					$.ligerDialog.err(obj.getMessage(),"提示信息");
				}
		}
	});
}
Lconf.tr2Conf = function(trObj){
	var listConf = {};
	listConf.name = trObj.find('[var=name]').val();
	listConf.dataNum = trObj.find('#dataNum').val();
	listConf.displayId = trObj.find('#displayId').val();
	listConf.advancedQueryKey = trObj.find('#advancedQuery').val();
	if(listConf.advancedQueryKey=="no"){
		listConf.advancedQueryKey="";
	}
	return listConf;
}
Lconf.DataTemplateChange = function(obj,isOld){
	var displayId = $(obj).find(':selected').val();
	var name = $(obj).find(':selected').text();
	if(!isOld){
		$(obj).parents('tr').find('[var=name]').val(name);
	}
	var ad = $(obj).parents('tr').find('#advancedQuery');
	$.ajax({
		url : __ctx + '/oa/system/advancedQuery/getAqInfo.do',
		data : {
			displayId : displayId
		},
		async : false, //{false：同步加载，true：异步加载} 
		type : 'post',
		success : function(result) {
			if(result.length>0){
				ad.find('option').text("请选择...");
				for(var i=0;i<result.length;i++){
					var queryKey = result[i].queryKey;
					var queryName = result[i].queryName;
					var option = '<option value="'+queryKey+'">'+queryName+'</option>';
					ad.append($(option));
				}
			}else{
				ad.find('option').remove();
				var option = '<option value="no">没有数据...</option>';
				ad.append($(option));
			}
		}
	});
}
Lconf.moveTr = function(obj, isUp) {
	var thisTr = $(obj).parents("tr");
	var tbody =  $(obj).parents("tbody");
	//颜色控制
	$('tr[class=over]',tbody).attr('class','');//移除上一个行的颜色样式
	thisTr.attr('class','over');//为新的行添加样式
	if (isUp) {
		var prevTr = $(thisTr).prev();
		if (prevTr) {
			thisTr.insertBefore(prevTr);
		}
	} else {
		var nextTr = $(thisTr).next();
		if (nextTr) {
			thisTr.insertAfter(nextTr);
		}
	}
}
Lconf.delTr = function(obj){
	$(obj).closest("tr").remove();
}
Lconf.addAdvancedQuery = function(obj){
	var displayId = $(obj).closest("tr").find('#displayId').val();
	var url = __ctx+"/oa/system/advancedQuery/dialog.do?displayId="+displayId+"&queryKey=";
	DialogUtil.open({
		height:500,
		width: 800,
		url: url,
		showMax: false,                             //是否显示最大化按钮 
	    showToggle: false,                         //窗口收缩折叠
	    title: "新增高级查询",
	    showMin: false,
		sucCall:function(){
			var displayObj = $(obj).closest("tr").find('#displayId');
			Lconf.DataTemplateChange(displayObj);
		}
	});
}
Lconf.editAdvancedQuery = function(obj){
	var displayId = $(obj).closest("tr").find('#displayId').val();
	var queryKey = $(obj).closest("tr").find('#advancedQuery').val();
	var url = __ctx+"/oa/system/advancedQuery/dialog.do?displayId="+displayId+"&queryKey="+queryKey;
	DialogUtil.open({
		height:500,
		width: 800,
		url: url,
		showMax: false,                             //是否显示最大化按钮 
	    showToggle: false,                         //窗口收缩折叠
	    title: "编辑高级查询",
	    showMin: false,
		sucCall:function(){
			var displayObj = $(obj).closest("tr").find('#displayId');
			Lconf.DataTemplateChange(displayObj);
		}
	});
}