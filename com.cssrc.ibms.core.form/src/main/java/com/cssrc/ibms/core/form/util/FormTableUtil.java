package com.cssrc.ibms.core.form.util;

import java.util.Iterator;
import java.util.List;

import com.cssrc.ibms.core.form.model.FormField;
import com.cssrc.ibms.core.form.model.FormTable;

public class FormTableUtil {
	/**
	 * 删除外部表的主键外键字段。
	 * 
	 * @param table
	 * @param fields
	 */
	public static void removeField(FormTable table, List<FormField> fields) {
		// 判断是否为外部表
		if (!table.isExtTable())
			return;
		String pk = table.getPkField();
		String fk = table.getRelation();
		for (Iterator<FormField> it = fields.iterator(); it.hasNext();) {
			FormField field = it.next();
			// 字段可能同时为主键和外键，因此只需删除一次即可
			if (field.getFieldName().equalsIgnoreCase(fk)) {
				it.remove();
			} else if (field.getFieldName().equalsIgnoreCase(pk)) {
				it.remove();
			}
		}
	}
}
