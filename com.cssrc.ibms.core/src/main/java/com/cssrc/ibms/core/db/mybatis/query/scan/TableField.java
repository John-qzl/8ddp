package com.cssrc.ibms.core.db.mybatis.query.scan;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.core.db.mybatis.query.annotion.Field;
import com.cssrc.ibms.core.util.string.StringUtil;
/**
 * 
 * <p>Title:TableField</p>
 * @author Yangbo 
 * @date 2016-8-8下午03:08:36
 */
public class TableField {
	private String fieldType;
	private String name;
	private String var;
	private String desc;
	private String options;
	private short controlType;
	private String style;
	private String dataType;

	public TableField() {
	}

	public TableField(java.lang.reflect.Field field,
			Field qField) {
		this.fieldType = field.getType().getName();
		this.var = field.getName();
		this.name = qField.name();
		this.desc = qField.desc();
		if (StringUtil.isNotEmpty(qField.options()))
			this.options = qField.options();
		this.controlType = qField.controlType();
		this.style = qField.style();
		this.dataType = qField.dataType();
	}

	public String getFieldType() {
		return this.fieldType;
	}

	public String getName() {
		return this.name;
	}

	public String getVar() {
		return this.var;
	}

	public String getDesc() {
		return this.desc;
	}

	public String getOptions() {
		return this.options;
	}

	public short getControlType() {
		return this.controlType;
	}

	public String getStyle() {
		return this.style;
	}

	public String getDataType() {
		return this.dataType;
	}

	public String toString() {
		return new ToStringBuilder(this).append("fieldType", this.fieldType)
				.append("name", this.name).append("desc", this.desc).append(
						"dataType", this.dataType).toString();
	}
}
