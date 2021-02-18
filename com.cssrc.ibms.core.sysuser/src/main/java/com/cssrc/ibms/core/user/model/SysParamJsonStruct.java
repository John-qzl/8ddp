package com.cssrc.ibms.core.user.model;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * <p>Title:SysParamJsonStruct</p>
 * @author Yangbo 
 * @date 2016-8-6下午03:25:26
 */
public class SysParamJsonStruct implements Serializable {
	private static final long serialVersionUID = 1L;
	private Boolean branch = Boolean.valueOf(false);
	private List<SysParamJsonStruct> sub;
	private String ruleType;
	private String dataType;
	private String paramKey;
	private String compType = "";
	private String paramCondition;
	private String paramValue;
	private String conDesc;
	private String expression;

	public String getRuleType() {
		return this.ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getCompType() {
		return this.compType;
	}

	public void setCompType(String compType) {
		this.compType = compType;
	}

	public String getConDesc() {
		return this.conDesc;
	}

	public void setConDesc(String conDesc) {
		this.conDesc = conDesc;
	}

	public Boolean getBranch() {
		return this.branch;
	}

	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getParamKey() {
		return this.paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	public String getParamCondition() {
		return this.paramCondition;
	}

	public void setParamCondition(String paramCondition) {
		this.paramCondition = paramCondition;
	}

	public String getParamValue() {
		return this.paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getExpression() {
		return this.expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public void setBranch(Boolean branch) {
		this.branch = branch;
	}

	public List<SysParamJsonStruct> getSub() {
		return this.sub;
	}

	public void setSub(List<SysParamJsonStruct> sub) {
		this.sub = sub;
	}

	public String toString() {
		return "ConditionJsonStruct [ruleType=" + this.ruleType + ", dataType="
				+ this.dataType + ", paramKey=" + this.paramKey + ", compType="
				+ this.compType + ", paramCondition=" + this.paramCondition
				+ ", paramValue=" + this.paramValue + ", conDesc="
				+ this.conDesc + ", expression=" + this.expression + "]";
	}
}
