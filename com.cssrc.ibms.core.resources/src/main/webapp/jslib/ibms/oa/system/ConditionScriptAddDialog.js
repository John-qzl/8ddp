
function ConditionScriptAddDialog(conf)
{
	var dialogWidth=700;
	var dialogHeight=480;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	if(!conf.isSingle)conf.isSingle=false;
	var url=__ctx + '/oa/system/conditionScript/addDialog.do?defId='+conf.data.defId;
	url=url.getNewUrl();
	
	var that =this;
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '',
		url: url, 
		isResize: true,
		//自定义参数
		data: conf.data,
		sucCall:function(rtn){
			if(conf.callback)
			{
				if(rtn && rtn.status){
					 conf.callback.call(that,rtn.data);
				}
			}
		}
	});
};

