package com.cssrc.ibms.core.db.mybatis.query.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.core.util.bean.BeanUtils;

public class JudgeScope extends JudgeSingle {
	private String compareEnd;
	private String valueEnd;
	private String relation;
	private Integer optType;

	public JudgeSingle getBeginJudge() {
		JudgeSingle judge = new JudgeSingle();
		judge.setFieldName(this.fieldName);
		judge.setCompare(this.compare);
		judge.setValue(this.value);
		return judge;
	}

	public JudgeSingle getEndJudge() {
		JudgeSingle judge = new JudgeSingle();
		judge.setFieldName(this.fieldName);
		judge.setCompare(this.compareEnd);
		judge.setValue(this.valueEnd);
		return judge;
	}

	public void setCompareEnd(String compareEnd) {
		this.compareEnd = compareEnd;
	}

	public void setValueEnd(String valueEnd) {
		this.valueEnd = valueEnd;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getRelation() {
		if (BeanUtils.isEmpty(this.relation)) {
			return "AND";
		}
		return this.relation;
	}

	public Integer getOptType() {
		return this.optType;
	}

	public void setOptType(Integer optType) {
		this.optType = optType;
	}

	public String toString() {
		return new ToStringBuilder(this).append("fieldName", this.fieldName)
				.append("compare", this.compare).append("value", this.value)
				.append("compareEnd", this.compareEnd).append("valueEnd",
						this.valueEnd).append("relation", this.relation)
				.toString();
	}
}
