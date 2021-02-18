package com.cssrc.ibms.core.db.mybatis.query.script.impl;

import com.cssrc.ibms.core.db.mybatis.query.entity.JudgeScript;
import com.cssrc.ibms.core.db.mybatis.query.script.ISqlScript;

public class SqlScript implements ISqlScript {
	public String getSQL(JudgeScript judgeScript) {
		if (judgeScript == null) {
			return "";
		}
		return judgeScript.getValue();
	}
}
