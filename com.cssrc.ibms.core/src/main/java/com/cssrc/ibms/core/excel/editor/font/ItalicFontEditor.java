package com.cssrc.ibms.core.excel.editor.font;

import com.cssrc.ibms.core.excel.editor.IFontEditor;
import com.cssrc.ibms.core.excel.style.font.Font;

/**
 * 实现一些常用的字体<br/>
 * 该类用于设置斜体
 * 
 * @author zhulongchao
 * 
 */
public class ItalicFontEditor implements IFontEditor {

	public void updateFont(Font font) {
		font.italic(true);
	}

}
