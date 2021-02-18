package com.cssrc.ibms.core.db.mybatis.query.entity;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

public class FieldLogic extends FieldTable
{
	private boolean isGroup;
	private List<FieldLogic> groupLogics = new ArrayList();
	private Integer dataType;
	private int judgeType;
	private JudgeSingle judgeSingle;
	private JudgeScope judgeScope;
	private JudgeScript judgeScript;
	protected String fieldRelation;

	public boolean isGroup()
	{
		return this.isGroup;
	}

	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}

	public List<FieldLogic> getGroupLogics() {
		return this.groupLogics;
	}

	public void setGroupLogics(List<FieldLogic> groupLogics) {
		this.groupLogics = groupLogics;
	}

	public Integer getDataType() {
		return this.dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public int getJudgeType() {
		return this.judgeType;
	}

	public void setJudgeType(int judgeType) {
		this.judgeType = judgeType;
	}

	public JudgeSingle getJudgeSingle() {
		return this.judgeSingle;
	}

	public void setJudgeSingle(JudgeSingle judgeSingle) {
		this.judgeSingle = judgeSingle;
	}

	public JudgeScope getJudgeScope() {
		return this.judgeScope;
	}

	public void setJudgeScope(JudgeScope judgeScope) {
		this.judgeScope = judgeScope;
	}

	public JudgeScript getJudgeScript() {
		return this.judgeScript;
	}

	public void setJudgeScript(JudgeScript judgeScript) {
		this.judgeScript = judgeScript;
	}

	public String getFieldRelation() {
		return this.fieldRelation;
	}

	public void setFieldRelation(String fieldRelation) {
		this.fieldRelation = fieldRelation;
	}

	public String toString()
	{
		return new ToStringBuilder(this).append("isGroup", this.isGroup).append("groupLogics.size", this.groupLogics.size()).append("dataType", this.dataType).append("judgeType", this.judgeType).append("fieldRelation", this.fieldRelation).append("judgeSingle", this.judgeSingle).append("JudgeScope", this.judgeScope).toString();
	}
}

