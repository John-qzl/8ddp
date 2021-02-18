package com.cssrc.ibms.core.db.mybatis.query.script.impl;

import com.cssrc.ibms.core.db.mybatis.query.entity.JudgeScope;
import com.cssrc.ibms.core.db.mybatis.query.script.IScopeScript;
import com.cssrc.ibms.core.db.mybatis.query.script.ISingleScript;

public class ScopeScript implements IScopeScript {
	public String getSQL(JudgeScope judgeScope) {
		StringBuilder sb = new StringBuilder();

		ISingleScript queryScript = SingleScriptFactory
				.getQueryScript(judgeScope.getOptType());

		String scriptBegin = queryScript.getSQL(judgeScope.getBeginJudge());

		String scriptEnd = queryScript.getSQL(judgeScope.getEndJudge());

		sb.append(" (").append(scriptBegin).append(" ").append(
				judgeScope.getRelation()).append(" ").append(scriptEnd).append(
				") ");

		return sb.toString();
	}
}
