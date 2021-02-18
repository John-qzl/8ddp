package com.cssrc.ibms.api.system.util;

import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.api.system.model.ICurrentSystem;
import com.cssrc.ibms.api.system.intf.ISysDataSourceService;
import com.cssrc.ibms.api.system.intf.ISysParameterService;

public class SysContextUtil
{
    /**
     * 获取当前项目系统基本参数
     * 
     * @return
     */
    public static ICurrentSystem getCurrentSystem()
    {
        ISysParameterService sysParameterService = (ISysParameterService)AppUtil.getBean(ISysParameterService.class);
        return sysParameterService.getCurrentSystem();
    }
    
    /**
     * 获取当前项目名称--appname
     * 
     * @return
     */
    public static String getAppName()
    {
        ISysDataSourceService sysDataSourceService = AppUtil.getBean(ISysDataSourceService.class);
        return sysDataSourceService.getAppName();
    }
    
    /**
     * 获取当前项目表空间--tablespace
     * 
     * @return
     */
    public static String getTableSpace()
    {
        ISysDataSourceService sysDataSourceService = AppUtil.getBean(ISysDataSourceService.class);
        return sysDataSourceService.getTableSpace();
    }
    
    /**
     * 获取当前数据库类型
     * 
     * @return
     */
    public static String getJdbcType()
    {
        ISysDataSourceService sysDataSourceService = AppUtil.getBean(ISysDataSourceService.class);
        return sysDataSourceService.getJdbcType();
    }
    /**
     * 
     * 获取logo
     * @return
     */
    public static String getLogo(String logo) {
        if(StringUtil.isNotEmpty(logo)) {
            return AppUtil.getContextPath()+logo;
        }else {
            return AppUtil.getContextPath()+"/styles/images/logo.png";
        }
    }
}
