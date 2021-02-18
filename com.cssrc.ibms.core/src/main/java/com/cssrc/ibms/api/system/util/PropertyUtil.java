package com.cssrc.ibms.api.system.util;

import com.cssrc.ibms.api.system.intf.ISysParameterService;
import com.cssrc.ibms.core.util.appconf.AppConfigUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 系统参数getByAlias方法接口实现
 * @author Yangbo 2016-7-22
 *
 */
public class PropertyUtil
{
    public static String getByAlias(String alias)
    {
        ISysParameterService service = (ISysParameterService)AppUtil.getBean(ISysParameterService.class);
        return service.getByAlias(alias);
    }
    
    public static String getByAlias(String alias, String defaultValue)
    {
        ISysParameterService service = (ISysParameterService)AppUtil.getBean(ISysParameterService.class);
        return service.getByAlias(alias, defaultValue);
    }
    
    public static Integer getIntByAlias(String alias)
    {
        ISysParameterService service = (ISysParameterService)AppUtil.getBean(ISysParameterService.class);
        return service.getIntByAlias(alias);
    }
    
    public static Integer getIntByAlias(String alias, Integer defaulValue)
    {
        ISysParameterService service = (ISysParameterService)AppUtil.getBean(ISysParameterService.class);
        return service.getIntByAlias(alias, defaulValue);
    }
    
    public static Long getLongByAlias(String alias)
    {
        ISysParameterService service = (ISysParameterService)AppUtil.getBean(ISysParameterService.class);
        return service.getLongByAlias(alias);
    }
    
    public static boolean getBooleanByAlias(String alias)
    {
        ISysParameterService service = (ISysParameterService)AppUtil.getBean(ISysParameterService.class);
        return service.getBooleanByAlias(alias);
    }
    
    public static boolean getBooleanByAlias(String alias, boolean defaulValue)
    {
        ISysParameterService service = (ISysParameterService)AppUtil.getBean(ISysParameterService.class);
        return service.getBooleanByAlias(alias, defaulValue);
    }
    
    public static String getSaveType()
    {
        String saveType = getByAlias("file.saveType", "Folder");
        return saveType.trim().toLowerCase();
    }
    
    public static String getValueByName(String paramName)
    {
        ISysParameterService service = (ISysParameterService)AppUtil.getBean(ISysParameterService.class);
        return service.getOneParameter(paramName);
    }
    
    /**
       * 获取任务或实例页面处理路径。
       * 
       * @param id
       * @param isTask
       * @return
       */
    public static String getUrl(String id, boolean isTask)
    {
        if (BeanUtils.isEmpty(id)) {
            return "";
        }
        String appurl=AppConfigUtil.get("appproperties", "app.url");
        String port=AppConfigUtil.get("appproperties", "app.port");
        port=StringUtil.isEmpty(port)?"80":port;
        String serviceurl="http://"+appurl+":"+port+AppUtil.getContextPath();
        
        if (isTask)
        {
            serviceurl += "/oa/flow/task/toStart.do?taskId="+id;
        }
        else
        {
            serviceurl += "/oa/flow/processRun/processRun/info.do?runId=" + id;
        }
        return serviceurl;
    }
}
