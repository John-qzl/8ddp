package rtx;

import com.cssrc.ibms.api.sysuser.model.ISysUser;


public class RTXUserProxy
{
  public static String getRTXName(ISysUser sysUser)
  {
    return sysUser.getUsername();
  }
}




