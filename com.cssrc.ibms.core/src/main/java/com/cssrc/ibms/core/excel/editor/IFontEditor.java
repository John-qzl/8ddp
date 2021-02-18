package com.cssrc.ibms.core.excel.editor;

import com.cssrc.ibms.core.excel.style.font.Font;


/**
 * 字体编辑器
 *@author zhulongchao
 */
public interface IFontEditor {
	/**
	 * 修改字体属性
	 * @param font 字体，可设置或获取字体属性
	 */
	public void updateFont(Font font);
}
