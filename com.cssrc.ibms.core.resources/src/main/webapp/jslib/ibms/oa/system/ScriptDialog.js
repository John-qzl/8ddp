function ScriptDialog(conf)
{
	if(!conf) conf={};	
	var url=__ctx + "/oa/expression/expression.do";	//脚本设计器
	
	var dialogWidth=800;
	var dialogHeight=600;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	url=url.getNewUrl();
	
	var that = this;
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '脚本',
		url: url, 
		isResize: true,
		//自定义参数
		sucCall:function(rtn){
			if(rtn!=undefined){
				if(conf.callback){
					conf.callback.call(that,rtn);
				}
			}
		}
	});
}

function setScriptCondition(conf){
	if(!conf) conf={};	
	var url=__ctx + "/oa/expression/setScriptCondition.do";	//条件脚本设计器
	
	var dialogWidth=800;
	var dialogHeight=600;
	
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:1,center:1},conf);

	url=url.getNewUrl();
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '条件脚本设计器',
		url: url, 
		isResize: true,
		defId : conf.defId,
		//自定义参数
		sucCall:function(rtn){
			if(rtn!=undefined){
				if(conf.callback){
					conf.callback.call(this,rtn);
				}
			}
		}
	});
}
