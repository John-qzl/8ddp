package com.cssrc.ibms.core.db.mybatis.query.entity;

public abstract interface QueryConstants
{
  public static final String QUERY_AND = "AND";
  public static final String QUERY_OR = "OR";
  public static final String QUERY_NOT = "NOT";
  public static final String SQL_RELATION_EQUAL = "=";
  public static final String SQL_RELATION_GT = ">";
  public static final String SQL_RELATION_GT_EQUAL = ">=";
  public static final String SQL_RELATION_LT = "<";
  public static final String SQL_RELATION_LT_EQUAL = "<=";
  public static final String SQL_RELATION_LIKE = " LIKE ";
  public static final String SORT_ASC = "ASC";
  public static final String SORT_DESC = "DESC";
  public static final int JUDGE_SINGLE = 1;
  public static final int JUDGE_SCOPE = 2;
  public static final int JUDGE_SCRIPT = 3;
  public static final String FILTER_KEY = "__FILTERKEY__";
  public static final String FILTER_FLAG = "__FILTER_FLAG__";
  public static final String IS_QUERY = "__IS_QUERY__";
  public static final short IS_QUERY_YES = 0;
  public static final short IS_QUERY_NOT = 1;
}

