package com.cssrc.ibms.core.db.mybatis.query.script;

import com.cssrc.ibms.core.db.mybatis.query.entity.JudgeScope;


public abstract interface IScopeScript
{
  public abstract String getSQL(JudgeScope paramJudgeScope);
}

