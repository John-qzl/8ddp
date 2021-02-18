package com.cssrc.ibms.api.report.inf;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/** 
* @ClassName: ISignHandlerAdapter 
* @Description:前端js调用签名方法下载报表文件需要实现该接口，然后业务表单模板中配置接口实现的beanID.
* @author zxg 
* @date 2016年12月17日 上午10:13:36 
*  
*/
public interface ISignHandlerAdapter
{
    /**
     * 获取签名用户
     * @return
     */
    public List<Long> getSignUsers(HttpServletRequest request);
    
    /**
     * 后去签名报表参数
     * @return
     */
    public Map<String,String> getReportParam(HttpServletRequest request);

}
