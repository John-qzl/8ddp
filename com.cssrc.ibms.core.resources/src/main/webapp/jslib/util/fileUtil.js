function downloadFile(id){
		var fileId=id;
		var callback = function(data){
			var dataObj = new com.ibms.form.ResultMessage(data);
			var filePath = dataObj.data.filePath;
			var fileType = dataObj.data.fileType;
			if(filePath == 'none'){
				$.ligerDialog.warn("文件不存在！","提示信息");
			}else{
				window.location.href = __ctx + '/oa/system/sysFile/download.do?fileId='+fileId;
			}
		};
		isExistFile(fileId, callback);
	};
	
function isExistFile(fileId, callback){
		$.ajax({
			url : __ctx + '/oa/system/sysFile/isExist.do?fileId='+fileId,
			type : "post",
			success : function(data){
				callback.call(this, data);
			}
			,error : function(){
				$.ligerDialog.warn("连接超时，请联系系统管理员！","提示信息");
			} 
		})
	};
	/**
	 * 自定义SQL查询中的附件下载设计
	 * column是附件所在的列
	 */
	function changeFileFieldShow(column){
		//获取除菜单栏的所有行
    	$("table tr:gt(0)").each(function(){
    		//循环获取的行进行处理
    		var tds = $(this).children();
    		//获取附件所在列
    		var td = tds.eq(column).text();
    		var td0 = $.trim(td);
    		var tdObj = tds[column];
    		if(td0!=""){
    			var td1 = eval("("+td0+")");
    			var divStr = "";
    			//有附件时，拼接成可下载的链接
    			for(var key in td1){
    				var a = td1[key];
    				for(var k in a){
    						if(k=="id"){
    							divStr+="<a href="+'"'+"#"+'"'+" onclick="+'"'+"javascript:downloadFile("+a[k]+")"+'"';
    						}
    						if(k=="name"){
    							divStr+=">"+a[k]+",\t</a>";
    						}
    				}
				}
    			//设置表格行开始和结束标签之间的HTML
    			tdObj.innerHTML=divStr;
    		}
    		 
    	});
    }