package com.cssrc.ibms.core.web.tag;

public enum ToolbarButtonType {

	add("新建"), 

	copyAdd("复制新建"), 

	newSearch("新建查询方案"), 

	searchList("新建查询列表"), 

	newAttach("新建附件"), 

	previewAttachs("预览所有附件"), 

	downloadAttachs("下载附件列表"), 

	saveCurGridView("保存当前方案"), 

	saveAsNewGridView("保存为新方案"), 

	printCurPage("打印当前页"), 

	printAllPage("打印所有页"), 

	printOneRecord("打印单条记录明细"), 

	exportCurPage("导出当前页"), 

	exportAllPage("导出所有页"), 

	exportOneRecord("导出单条记录明细"), 

	detail("明细"), 

	addMenu("增加菜单"), 

	edit("编辑"), 

	remove("删除"), 

	popupSearchMenu("高级查询方案菜单"), 

	popupAttachMenu("附件菜单"), 

	popupSettingMenu("工具菜单"), 

	popupAddMenu("添加菜单"), 

	fieldSearch("字段查询"), 

	save("保存"), 

	prevRecord("上一条记录"), 

	nextRecord("下一条记录");

	private String text;

	private ToolbarButtonType(String text) 
	{ 
		this.text = text; 
	}

	public String getText()
	{
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getKey() {
		return name();
	}

}
