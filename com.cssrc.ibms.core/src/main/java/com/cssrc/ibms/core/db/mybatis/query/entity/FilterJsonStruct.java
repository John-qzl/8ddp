package com.cssrc.ibms.core.db.mybatis.query.entity;

import java.io.Serializable;
import java.util.List;

public class FilterJsonStruct implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int RULE_TYPE_NORMAL = 1;
	public static final int RULE_TYPE_SCRIPT = 2;
	private Boolean branch = Boolean.valueOf(false);
	private List<FilterJsonStruct> sub;
	private Integer ruleType;
	private Integer optType;
	private String flowvarKey;
	private String compType = "";
	private String judgeCon1;
	private String judgeVal1;
	private String judgeCon2;
	private String judgeVal2;
	private String conDesc;
	private String tables;
	private String script;
	private String source = "1";

	protected Integer isHidden = Integer.valueOf(0);

	private String datefmt = "yyyy-MM-dd";

	private String table = "";

	private String mainTable = "";

	private String relation = "";

	public Integer getRuleType() {
		return this.ruleType;
	}

	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}

	public Integer getOptType() {
		return this.optType;
	}

	public void setOptType(Integer optType) {
		this.optType = optType;
	}

	public String getFlowvarKey() {
		return this.flowvarKey;
	}

	public void setFlowvarKey(String flowvarKey) {
		this.flowvarKey = flowvarKey;
	}

	public String getCompType() {
		return this.compType;
	}

	public void setCompType(String compType) {
		this.compType = compType;
	}

	public String getJudgeCon1() {
		return this.judgeCon1;
	}

	public void setJudgeCon1(String judgeCon1) {
		this.judgeCon1 = judgeCon1;
	}

	public String getJudgeVal1() {
		return this.judgeVal1;
	}

	public void setJudgeVal1(String judgeVal1) {
		this.judgeVal1 = judgeVal1;
	}

	public String getJudgeCon2() {
		return this.judgeCon2;
	}

	public void setJudgeCon2(String judgeCon2) {
		this.judgeCon2 = judgeCon2;
	}

	public String getJudgeVal2() {
		return this.judgeVal2;
	}

	public void setJudgeVal2(String judgeVal2) {
		this.judgeVal2 = judgeVal2;
	}

	public String getConDesc() {
		return this.conDesc;
	}

	public void setConDesc(String conDesc) {
		this.conDesc = conDesc;
	}

	public String getScript() {
		return this.script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public Boolean getBranch() {
		return this.branch;
	}

	public void setBranch(Boolean branch) {
		this.branch = branch;
	}

	public List<FilterJsonStruct> getSub() {
		return this.sub;
	}

	public void setSub(List<FilterJsonStruct> sub) {
		this.sub = sub;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDatefmt() {
		return this.datefmt;
	}

	public void setDatefmt(String datefmt) {
		this.datefmt = datefmt;
	}

	public String getTable() {
		return this.table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getMainTable() {
		return this.mainTable;
	}

	public void setMainTable(String mainTable) {
		this.mainTable = mainTable;
	}

	public String getRelation() {
		return this.relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getTables() {
		return this.tables;
	}

	public void setTables(String tables) {
		this.tables = tables;
	}

	public Integer getIsHidden() {
		return this.isHidden;
	}

	public void setIsHidden(Integer isHidden) {
		this.isHidden = isHidden;
	}

	public String toString() {
		return "ConditionJsonStruct [ruleType=" + this.ruleType + ", optType="
				+ this.optType + ", flowvarKey=" + this.flowvarKey
				+ ", compType=" + this.compType + ", judgeCon1="
				+ this.judgeCon1 + ", judgeVal1=" + this.judgeVal1
				+ ", judgeCon2=" + this.judgeCon2 + ", judgeVal2="
				+ this.judgeVal2 + ", conDesc=" + this.conDesc + ", script="
				+ this.script + "]";
	}
}
