package com.cssrc.ibms.core.db.mybatis.query.script;

import com.cssrc.ibms.core.db.mybatis.query.entity.JudgeSingle;


public abstract interface ISingleScript
{
  public abstract String getSQL(JudgeSingle paramJudgeSingle);
}

