package com.cssrc.ibms.core.table;

import java.util.ArrayList;
import java.util.List;

import com.cssrc.ibms.api.form.model.ITableModel;
import com.cssrc.ibms.core.util.appconf.AppConfigUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 表对象。
 *
 */
public class TableModel implements ITableModel {
	/**
	 * 自定义字段的字段前缀(F_)
	 */
	public static String CUSTOMER_COLUMN_PREFIX = "F_";
	/**
	 * 自定义表的表前缀(W_)
	 */
	public static String CUSTOMER_TABLE_PREFIX = "W_";

	/**
	 * 默认创建时间
	 */
	public static final String CUSTOMER_COLUMN_CREATETIME = "CREATETIME";

	// 表名
	private String name = "";
	// 表注释
	private String comment = "";
	// 列列表
	private List<ColumnModel> columnList = new ArrayList<ColumnModel>();

	static {
		String customerTablePrefix = AppConfigUtil.get("CUSTOMER_TABLE_PREFIX");
		CUSTOMER_TABLE_PREFIX = StringUtil.isEmpty(customerTablePrefix) ? CUSTOMER_TABLE_PREFIX
				: customerTablePrefix;
		String customerColumnPrefix = AppConfigUtil
				.get("CUSTOMER_COLUMN_PREFIX");
		CUSTOMER_COLUMN_PREFIX = StringUtil.isEmpty(customerColumnPrefix) ? CUSTOMER_COLUMN_PREFIX
				: customerColumnPrefix;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		if (StringUtil.isNotEmpty(comment)) {
			comment = comment.replace("'", "''");
		}
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * 添加列对象。
	 * 
	 * @param model
	 */
	public void addColumnModel(ColumnModel model) {
		this.columnList.add(model);
	}

	public List<ColumnModel> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<ColumnModel> columnList) {
		this.columnList = columnList;
	}

	public List<ColumnModel> getPrimayKey() {
		List<ColumnModel> pks = new ArrayList<ColumnModel>();
		for (ColumnModel column : columnList) {
			if (column.getIsPk()) {
				pks.add(column);
			}
		}
		return pks;
	}

	@Override
	public String toString() {
		return "TableModel [name=" + name + ", comment=" + comment
				+ ", columnList=" + columnList + "]";
	}
}
