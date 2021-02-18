package com.cssrc.ibms.core.db.mybatis.query;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FieldCommandImpl implements CriteriaCommand {
	private static Log logger = LogFactory.getLog(FieldCommandImpl.class);
	private String property;
	private Object value;
	private String operation;
	private QueryFilter filter;

	public FieldCommandImpl(String property, Object value, String operation,
			QueryFilter filter) {
		this.property = property;
		this.value = value;
		this.operation = operation;
		this.filter = filter;
	}


	public String getPartHql() {
		String[] propertys = this.property.split("[.]");
		if ((propertys != null) && (propertys.length > 1)
				&& (!"vo".equals(propertys[0]))) {
			if (!this.filter.getAliasSet().contains(propertys[0])) {
				this.filter.getAliasSet().add(propertys[0]);
			}
		}
		String partHql = "";
		if ("LT".equals(this.operation)) {
			partHql = this.property + " < ? ";
			this.filter.getParamValueList().add(this.value);
		} else if ("GT".equals(this.operation)) {
			partHql = this.property + " > ? ";
			this.filter.getParamValueList().add(this.value);
		} else if ("LE".equals(this.operation)) {
			partHql = this.property + " <= ? ";
			this.filter.getParamValueList().add(this.value);
		} else if ("GE".equals(this.operation)) {
			partHql = this.property + " >= ? ";
			this.filter.getParamValueList().add(this.value);
		} else if ("LK".equals(this.operation)) {
			partHql = this.property + " like ? ";
			this.filter.getParamValueList().add(
					"%" + this.value.toString() + "%");
		} else if ("LFK".equals(this.operation)) {
			partHql = this.property + " like ? ";
			this.filter.getParamValueList().add(this.value.toString() + "%");
		} else if ("RHK".equals(this.operation)) {
			partHql = this.property + " like ? ";
			this.filter.getParamValueList().add("%" + this.value.toString());
		} else if ("NULL".equals(this.operation)) {
			partHql = this.property + " is null ";
		} else if ("NOTNULL".equals(this.operation)) {
			partHql = this.property + " is not null ";
		} else if (!"EMP".equals(this.operation)) {
			if (!"NOTEMP".equals(this.operation)) {
				if ("NEQ".equals(this.operation)) {
					partHql = this.property + " !=? ";
					this.filter.getParamValueList().add(this.value);
				} else {
					partHql = partHql + this.property + " =? ";
					this.filter.getParamValueList().add(this.value);
				}
			}
		}
		return partHql;
	}
	
	public String getPartSql() {
		String prop = this.property.replace("##", "_");
		String partSql = "";
		if ("LT".equals(this.operation)) {
			partSql = prop + " < ? ";
			this.filter.getParamValueList().add(this.value);
		} else if ("GT".equals(this.operation)) {
			partSql = prop + " > ? ";
			this.filter.getParamValueList().add(this.value);
		} else if ("LE".equals(this.operation)) {
			partSql = prop + " <= ? ";
			this.filter.getParamValueList().add(this.value);
		} else if ("GE".equals(this.operation)) {
			partSql = prop + " >= ? ";
			this.filter.getParamValueList().add(this.value);
		} else if ("LK".equals(this.operation)) {
			partSql = prop + " like ? ";
			this.filter.getParamValueList().add(
					"%" + this.value.toString() + "%");
		} else if ("LFK".equals(this.operation)) {
			partSql = prop + " like ? ";
			this.filter.getParamValueList().add(this.value.toString() + "%");
		} else if ("RHK".equals(this.operation)) {
			partSql = prop + " like ? ";
			this.filter.getParamValueList().add("%" + this.value.toString());
		} else if ("NULL".equals(this.operation)) {
			partSql = prop + " is null ";
		} else if ("NOTNULL".equals(this.operation)) {
			partSql = prop + " is not null ";
		} else if (!"EMP".equals(this.operation)) {
			if (!"NOTEMP".equals(this.operation)) {
				if ("NEQ".equals(this.operation)) {
					partSql = prop + " !=? ";
					this.filter.getParamValueList().add(this.value);
				} else {
					partSql = partSql + prop + " =? ";
					this.filter.getParamValueList().add(this.value);
				}
			}
		}
		return partSql;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

}
