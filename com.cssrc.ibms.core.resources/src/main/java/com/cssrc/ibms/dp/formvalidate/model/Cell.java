package com.cssrc.ibms.dp.formvalidate.model;

import org.dom4j.Element;

/**
 * 用于校验表单设计器模板
 * 最小单位：单元格
 * @author scc
 */
public class Cell {
	/**
	 * 单元格唯一标记
	 * c1  -   以此为初始值
	 */
	private String key;
	/**
	 * td节点 如需更改 直接对其进行操作
	 * 操作：加属性 减属性 加减属性值 移除该节点
	 */
	private Element td;
	
	private int colspan;//列合并数 
	private int rowspan;//行合并数
	private boolean type=false;//true为检查项 false为普通文本项
	
	//以下为检查项的属性
	private boolean ifInput;//是否含有input
	private boolean ifCheckbox;//是否含有checkbox
	private boolean ifPhoto;//是否含有拍照
	private String title;//全称
	private String simpleTitle;//简称
	
	//以下为普通文本项的属性
	private String content;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getColspan() {
		return colspan;
	}
	/**
	 * 列合并数至少为1
	 * @param colspan
	 */
	public void setColspan(int colspan) {
		/*if(colspan==0){
			colspan=1;
		}*/
		this.colspan = colspan;
	}

	public int getRowspan() {
		return rowspan;
	}
	/**
	 *  确保行合并数至少为1
	 * @param rowspan
	 */
	public void setRowspan(int rowspan) {
		/*if(rowspan==0){
			rowspan=1;
		}*/
		this.rowspan = rowspan;
	}
	/**
	 * true为检查项 false为普通文本项
	 * @return
	 */
	public boolean isType() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
	}

	public boolean isIfInput() {
		return ifInput;
	}

	public void setIfInput(boolean ifInput) {
		this.ifInput = ifInput;
		type=true;
	}

	public boolean isIfCheckbox() {
		return ifCheckbox;
	}

	public void setIfCheckbox(boolean ifCheckbox) {
		this.ifCheckbox = ifCheckbox;
		type=true;
	}

	public boolean isIfPhoto() {
		return ifPhoto;
	}

	public void setIfPhoto(boolean ifPhoto) {
		this.ifPhoto = ifPhoto;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSimpleTitle() {
		return simpleTitle;
	}

	public void setSimpleTitle(String simpleTitle) {
		this.simpleTitle = simpleTitle;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Element getTd() {
		return td;
	}

	public void setTd(Element td) {
		this.td = td;
	}
	
	
}
