package com.cssrc.ibms.core.user.intf;

import com.cssrc.ibms.core.user.model.SysOrg;

/**
 * 
 * <p>Title:IOrgHandler</p>
 * @author Yangbo 
 * @date 2016-8-4下午03:39:36
 */
public abstract interface IOrgHandler
{
  public abstract SysOrg getByType(String paramString);
}

