$(function(){
	//实现在FormUtil.js中 初始化表单tab
	FormUtil.initTab();
	//对于当前页面展示自定义对话框的情况添加列表显示div
	FormUtil.initDialogList();
	//初始化日期控件。
	FormUtil.initCalendar();
	//附件初始化
	//AttachMent.init();//附件权限初始化移至customForm中
	//子表权限初始化
	SubtablePermission.init();
	//rel表权限初始化
	ReltablePermission.init();
	//初始化只读查询,实现在ReadOnlyQuery.js中
	ReadOnlyQuery.init();
	
	//实现在FormUtil.js中,初始化统计函数事件绑定
	FormUtil.InitMathfunction();
	//FormUtil.js绑定"自定义对话框"。处理主表的"自定义对话框", 子表和rel表情况的放在CustomForm.js中处理
	FormUtil.initCommonDialog();
	//FormFKShowUtil.js   初始化外键显示值。
	FormFKShowUtil.initFKColumnShow();
	//绑定"自定义查询"。
	FormUtil.initCommonQuery();
	//绑定外部链接link TODO没用过
	FormUtil.initExtLink();
	//主要是处理子表弹出框模式，
	QueryUI.init();
});
$(window).bind("load",function(){
	//Office控件初始化。
	OfficePlugin.init();
	//WebSign控件初始化。
	//WebSignPlugin.init();
	//PictureShow 图片展示控件
	PictureShowPlugin.init();	
	//初始化第一个表单tab 中部分样式问题
	FormUtil.initTabStyle();
	
});