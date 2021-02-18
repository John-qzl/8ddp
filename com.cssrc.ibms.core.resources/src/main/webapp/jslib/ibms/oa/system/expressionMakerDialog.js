function ExpressionMakerDialog(conf)
{	
	if(!conf) conf={};	
	var url=__ctx + "/oa/expression/expression.do";
	
	var dialogWidth=780;
	var dialogHeight=570;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	url=url.getNewUrl();
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '',
		url: url, 
		isResize: true,	
		sucCall:function(rtn){
		}
	});
}