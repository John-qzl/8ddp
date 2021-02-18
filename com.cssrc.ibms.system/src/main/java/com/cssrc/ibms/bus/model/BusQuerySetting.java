package com.cssrc.ibms.bus.model;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.db.mybatis.query.entity.FieldShow;

import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * 
 * <p>Title:BusQuerySetting</p>
 * @author Yangbo 
 * @date 2016-8-8下午02:18:21
 */
public class BusQuerySetting extends BaseModel {
	private static final long serialVersionUID = 2815452680584496462L;
	protected Long id;
	protected String tableName;
	protected String displayField;
	protected Long userId;
	protected List<FieldShow> fieldShowList;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setDisplayField(String displayField) {
		this.displayField = displayField;
	}

	public String getDisplayField() {
		return this.displayField;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public List<FieldShow> getFieldShowList() {
		return this.fieldShowList;
	}

	public void setFieldShowList(List<FieldShow> fieldShowList) {
		this.fieldShowList = fieldShowList;
	}

	public boolean equals(Object object) {
		if (!(object instanceof BusQuerySetting)) {
			return false;
		}
		BusQuerySetting rhs = (BusQuerySetting) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(
				this.tableName, rhs.tableName).append(this.displayField,
				rhs.displayField).append(this.userId, rhs.userId).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.tableName).append(this.displayField).append(
						this.userId).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append(
				"tableName", this.tableName).append("displayField",
				this.displayField).append("userId", this.userId).toString();
	}
}
