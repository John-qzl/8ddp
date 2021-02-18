package com.cssrc.ibms.core.db.mybatis.query.script;

import com.cssrc.ibms.core.db.mybatis.query.entity.JudgeScript;


public abstract interface ISqlScript
{
  public abstract String getSQL(JudgeScript paramJudgeScript);
}
