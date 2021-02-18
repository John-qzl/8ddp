package com.cssrc.ibms.api.rpc.intf;

import java.util.Map;

public interface ICommonParam extends Map<String,Object>
{
    public static String KEY_USER="user";
    public static String KEY_USERFIELD="user_field";
    public static String KEY_TEMPLATEALIAS="templateAlias";

    public static ICommonParam paramBean=null;

    public Object getUser();
    public Object getUserField();
    public Object getTemplate();
}
