package com.cssrc.ibms.core.form.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.form.model.IFormRun;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

/**
 * 对象功能:流程表单运行情况 Model对象 开发人员:zhulongchao
 */
public class FormRun extends BaseModel implements IFormRun {

	// 主键
	protected Long id;
	// 表单定义ID
	protected Long formdefId;
	// 表单定义key
	protected Long formdefKey;
	// 流程实例ID
	protected String actInstanceId;
	// ACTDEFID
	protected String actDefId;
	// 流程节点id
	protected String actNodeId;
	// 流程运行ID
	protected Long runId;
	// 表单类型0,任务节点1,开始表单2,全局表单
	protected Short setType = 0;
	// FORMTYPE
	protected Short formType = -1;
	// FORMURL
	protected String formUrl;

	protected Long mobileFormKey;
	protected Long mobileFormId;
	protected String mobileFormUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFormdefId() {
		return formdefId;
	}

	public void setFormdefId(Long formdefId) {
		this.formdefId = formdefId;
	}

	public Long getFormdefKey() {
		return formdefKey;
	}

	public void setFormdefKey(Long formdefKey) {
		this.formdefKey = formdefKey;
	}

	public String getActInstanceId() {
		return actInstanceId;
	}

	public void setActInstanceId(String actInstanceId) {
		this.actInstanceId = actInstanceId;
	}

	public String getActDefId() {
		return actDefId;
	}

	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}

	public String getActNodeId() {
		return actNodeId;
	}

	public void setActNodeId(String actNodeId) {
		this.actNodeId = actNodeId;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	public Short getSetType() {
		return setType;
	}

	public void setSetType(Short setType) {
		this.setType = setType;
	}

	public Short getFormType() {
		return formType;
	}

	public void setFormType(Short formType) {
		this.formType = formType;
	}

	public String getFormUrl() {
		return formUrl;
	}

	public void setFormUrl(String formUrl) {
		this.formUrl = formUrl;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof FormRun)) {
			return false;
		}
		FormRun rhs = (FormRun) object;
		return new EqualsBuilder().append(this.id, rhs.id)

		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)

		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id)

		.toString();
	}

	public Long getMobileFormKey() {
		return mobileFormKey;
	}

	public void setMobileFormKey(Long mobileFormKey) {
		this.mobileFormKey = mobileFormKey;
	}

	public Long getMobileFormId() {
		return mobileFormId;
	}

	public void setMobileFormId(Long mobileFormId) {
		this.mobileFormId = mobileFormId;
	}

	public String getMobileFormUrl() {
		return mobileFormUrl;
	}

	public void setMobileFormUrl(String mobileFormUrl) {
		this.mobileFormUrl = mobileFormUrl;
	}

}