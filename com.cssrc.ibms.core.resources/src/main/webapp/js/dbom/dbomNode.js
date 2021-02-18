function showDataSource(){
	var mill=(parseInt(Math.random()*10000)).toString();
	var url = __ctx + "/oa/system/dbomNode/dataSource.do?mill="+mill;
	DialogUtil.open({
		height:800,
		width: 800,
		title : '',
		url: url, 
		isResize: true,
		sucCall:function(rvalue){
			if (rvalue != undefined) {
				if(rvalue.reset){
					$("#dataSource").val("");
					$("#dynamicNode").val("");
					$("#dataFormat").val("");
				}else{
					if(rvalue.tablename!=$("#dataSource").val()){
						$("#dynamicNode").val("");
						$("#dataFormat").val("");
					}
					$("#dataSource").val(rvalue.tablenamefm);
					$("#dataSourceHide").val(rvalue.tablename);
				}
			}
		}
	});
}

function showDynamicNode(){
	var datasource=$("#dataSource").val();
	var modelType=$("#modelType").val();
	if( datasource == '' || datasource == null){
		$.ligerDialog.warn("请先选择数据源！");
		return;
	}else{
		if(modelType==1){
			datasource="W_"+datasource;
		}else{
			datasource="V_"+datasource;
		}
		var mill=(parseInt(Math.random()*10000)).toString();
		var url = __ctx + "/oa/system/dbomNode/dynamicNode.do?mill="+mill;
		url+="&tableName="+datasource;
		DialogUtil.open({
			height:800,
			width: 800,
			title : '',
			url: url, 
			isResize: true,
			sucCall:function(rvalue){
				if (rvalue != undefined) {
					if(rvalue.reset){
						$("#dynamicNode").val("");
						$("#dataFormat").val("");
					}else{
						$("#dynamicNode").val(rvalue.files);
						$("#dataFormat").val(rvalue.filesfm);
					}
				}
			}
		});
	}

}


function showUrl(){
	var datasource=$("#dataSource").val();
	var modelType=$("#modelType").val();
	if( datasource == '' || datasource == null){
		$.ligerDialog.warn("请先选择数据源！");
		return;
	}else{
		if(modelType==1){
			datasource="W_"+datasource;
		}else{
			datasource="V_"+datasource;
		}
		var mill=(parseInt(Math.random()*10000)).toString();
		var url = __ctx + "/oa/system/dbomNode/formDefList.do?mill="+mill;
		url+="&tableName="+datasource;
		DialogUtil.open({
			height:800,
			width: 800,
			title : '',
			url: url, 
			isResize: true,
			sucCall:function(rvalue){
				if (rvalue != undefined) {
					if(rvalue.reset){
						$("#url").val("");
					}else{
						$("#url").val(rvalue.url);
					}
				}
			}
		});
	}

}