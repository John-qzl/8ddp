package com.cssrc.ibms.api.activity.model;

public interface ITaskWarningSet
{
    public static final String RELATIVE_TYPE_BEFOR = "before";
    public static final String RELATIVE_TYPE_AFTER = "after";
    int getLevel();
    String getKey();
}
