if (typeof Clist=="undefined"){
	var Clist = {};
}

Clist.toHtml= function(custLinkLists){
	if(custLinkLists==null){ return false;}
	var confs = custLinkLists.confs;
	for(var i=0;i<confs.length;i++){
		var custLinkList = confs[i];
		Clist.addTr(custLinkList);
	}
}

Clist.addTr = function(custLinkList){
	var trObj = $('table[var=linkListTemplateTable]').find('tr');
	var container = $('#linkTbl tbody');
	var newTrObj = trObj.clone(true,true);
	newTrObj.find("input[name='mc']").val(custLinkList.name);
	newTrObj.find("input[name='url']").val(custLinkList.url);
	newTrObj.find("select[name='ms']").val(custLinkList.desc);
	container.append(newTrObj);
}
/**
 * html---->js对象
 */
Clist.toBean = function(){
	var confs = [];
	$('#linkTbl tbody').find('tr').each(function(){
		var conf = {};
		conf.name = $(this).find('[name=mc]').val();
		conf.url = $(this).find('[name=url]').val();
		conf.desc = $(this).find('[name=ms]').val();
		confs.push(conf);
	});
	return {confs:confs};
}
Clist.init = function(){
	var obj =$.parseJSON($('#custLinkLists').val());
	var html = Clist.toHtml(obj);
}
Clist.add =function(){
	var trObj = $('table[var=linkListTemplateTable]').find('tr');
	var container = $('#linkTbl tbody');
	var newTrObj = trObj.clone(true,true);
	container.append(newTrObj);	
}
Clist.save =function(){
	
	function check(){
		var flag = false;
		$('#linkTbl [name=mc]').each(function(){
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

	var bean =  Clist.toBean();
	$.ajax({
		url : __ctx + '/oa/system/CustLinkList/save.do',
		data : {
			custLinkLists : JSON2.stringify(bean)
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
Clist.delTr =function(obj){
	$(obj).closest("tr").remove();
}
Clist.moveTr =function(obj, isUp){
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