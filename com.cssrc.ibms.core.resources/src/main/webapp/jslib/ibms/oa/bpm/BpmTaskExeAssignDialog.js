/**
 *  功能:流程转办对话框
 * 
 * @param conf:配置为一个JSON
 * 
 * dialogWidth:对话框的宽度。
 * dialogHeight：对话框高度。
 * 
 * taskId:任务ID。
 * taskName 任务名称
 */
function BpmTaskExeAssignDialog(conf)
{
	if(!conf) conf={};	
	var url=__ctx + '/oa/flow/taskExe/assign.do?taskId=' + conf.taskId+"&taskName="+conf.taskName;
	
	var dialogWidth = conf.width;
	var dialogHeight = conf.height;
	
	var dialogWidth=500 || dialogWidth;
	var dialogHeight=350 || dialogHeight;
	conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:1,center:1},conf);
	url=url.getNewUrl();
	
	
	var that =this;
	/*KILLDIALOG*/
	DialogUtil.open({
		height:conf.dialogHeight,
		width: conf.dialogWidth,
		title : '流程转办对话框',
		url: url, 
		isResize: true,
		//自定义参数
		sucCall:function(rtn){
			if (conf.callback) {
				conf.callback.call(that,rtn);
			}
		}
	});
}