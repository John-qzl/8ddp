
function VarsWindow(conf)
{
		   	if(!conf) conf={};
		   	var url=__ctx + "/oa/flow/defVar/edit.do?defId="+conf.defId +"&varId=" + conf.varId;
		   
		   	var dialogWidth=500;
			var dialogHeight=300;
			conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
			url=url.getNewUrl();
			
			DialogUtil.open({
				height:conf.dialogHeight,
				width: conf.dialogWidth,
				title : '',
				url: url, 
				isResize: true,
				//自定义参数
				sucCall:function(rtn){
					if(conf){
						location.reload();
					}
				}
			});
}
