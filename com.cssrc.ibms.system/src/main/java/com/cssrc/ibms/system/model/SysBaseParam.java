package com.cssrc.ibms.system.model;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import java.util.Date;
/**
 * 
 * <p>Title:SysBaseParam</p>
 * @author Yangbo 
 * @date 2016-8-3上午09:08:54
 */
public class SysBaseParam extends BaseModel {
	protected Long valueId;
	protected Long paramId;
	protected String paramValue = "";
	protected String paramName;
	protected String dataType;
	protected Long paramIntValue;
	protected Date paramDateValue;
	protected String sourceType;
	protected String sourceKey;
	protected String description = "";
	protected Short status_;

	public Long getValueId() {
		return this.valueId;
	}

	public void setValueId(Long valueId) {
		this.valueId = valueId;
	}

	public Long getParamId() {
		return this.paramId;
	}

	public void setParamId(Long paramId) {
		this.paramId = paramId;
	}

	public String getParamValue() {
		return this.paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getParamName() {
		return this.paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public Long getParamIntValue() {
		return this.paramIntValue;
	}

	public void setParamIntValue(Long paramIntValue) {
		this.paramIntValue = paramIntValue;
	}

	public Date getParamDateValue() {
		return this.paramDateValue;
	}

	public void setParamDateValue(Date paramDateValue) {
		this.paramDateValue = paramDateValue;
	}

	public String getSourceType() {
		return this.sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceKey() {
		return this.sourceKey;
	}

	public void setSourceKey(String sourceKey) {
		this.sourceKey = sourceKey;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Short getStatus_() {
		return this.status_;
	}

	public void setStatus_(Short status_) {
		this.status_ = status_;
	}
}
