package com.cssrc.ibms.system.model;

import java.util.Date;

import com.cssrc.ibms.api.system.model.ISysParam;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * 系统参数
 * <p>Title:SysParam</p>
 * @author Yangbo 
 * @date 2016-8-3上午09:22:57
 */
public class SysParam extends BaseModel implements ISysParam{
	@SysFieldDescription(detail="属性ID")
	protected Long paramId;
	@SysFieldDescription(detail="属性key值")
	protected String paramKey;
	@SysFieldDescription(detail="属性名称")
	protected String paramName;
	@SysFieldDescription(detail="数据类型")
	protected String dataType;
	@SysFieldDescription(detail="参数类型",maps="{\"1\":\"个人\",\"0\":\"组织\"}")
	protected Short effect;
	@SysFieldDescription(detail="所属维度")
	protected Long belongDem;
	@SysFieldDescription(detail="数据来源")
	protected String sourceType;
	@SysFieldDescription(detail="数据来源key")
	protected String sourceKey;
	@SysFieldDescription(detail="参数描述")
	protected String description = "";
	@SysFieldDescription(detail="参数状态")
	protected Short status_;
	@SysFieldDescription(detail="分类")
	protected String category;
	@SysFieldDescription(detail="创建人ID")
	protected Long sysParam_creatorId;// 创建人ID
	@SysFieldDescription(detail="创建时间")
	protected Date sysParam_createTime;// 创建时间
	@SysFieldDescription(detail="更改人ID")
	protected Long sysParam_updateId;// 更改人ID
	@SysFieldDescription(detail="更改时间")
	protected Date sysParam_updateTime;// 更改时间

	static {
		DATA_COLUMN_MAP.put("String", "paramValue");
		DATA_COLUMN_MAP.put("Date", "paramDateValue");
		DATA_COLUMN_MAP.put("Integer", "paramIntValue");
		DATA_TYPE_MAP.put("String", "字符");
		DATA_TYPE_MAP.put("Date", "日期");
		DATA_TYPE_MAP.put("Integer", "数字");

		CONDITION_US.put("=", "=");
		CONDITION_US.put("&lt;", "<");
		CONDITION_US.put("&gt;", ">");
		CONDITION_US.put("!=", "!=");
		CONDITION_US.put("&gt;=", ">=");
		CONDITION_US.put("&lt;=", "<=");
		CONDITION_US.put("LIKE", "like");
	}

	public Long getSysParam_creatorId() {
		return sysParam_creatorId;
	}

	public void setSysParam_creatorId(Long sysParam_creatorId) {
		this.sysParam_creatorId = sysParam_creatorId;
	}

	public Date getSysParam_createTime() {
		return sysParam_createTime;
	}

	public void setSysParam_createTime(Date sysParam_createTime) {
		this.sysParam_createTime = sysParam_createTime;
	}

	public Long getSysParam_updateId() {
		return sysParam_updateId;
	}

	public void setSysParam_updateId(Long sysParam_updateId) {
		this.sysParam_updateId = sysParam_updateId;
	}

	public Date getSysParam_updateTime() {
		return sysParam_updateTime;
	}

	public void setSysParam_updateTime(Date sysParam_updateTime) {
		this.sysParam_updateTime = sysParam_updateTime;
	}

	public Short getEffect() {
		return this.effect;
	}

	public void setEffect(Short effect) {
		this.effect = effect;
	}

	public void setParamId(Long paramId) {
		this.paramId = paramId;
	}

	public Long getParamId() {
		return this.paramId;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	public String getParamKey() {
		return this.paramKey;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamName() {
		return this.paramName;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataType() {
		return this.dataType;
	}

	public Long getBelongDem() {
		return this.belongDem;
	}

	public void setBelongDem(Long belongDem) {
		this.belongDem = belongDem;
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

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Short getStatus_() {
		return this.status_;
	}

	public void setStatus_(Short status_) {
		this.status_ = status_;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SysParam)) {
			return false;
		}
		SysParam rhs = (SysParam) object;
		return new EqualsBuilder().append(this.paramId, rhs.paramId).append(
				this.paramKey, rhs.paramKey).append(this.paramName,
				rhs.paramName).append(this.dataType, rhs.dataType).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.paramId)
				.append(this.paramKey).append(this.paramName).append(
						this.dataType).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("paramId", this.paramId)
				.append("paramKey", this.paramKey).append("paramName",
						this.paramName).append("dataType", this.dataType)
				.toString();
	}
}
