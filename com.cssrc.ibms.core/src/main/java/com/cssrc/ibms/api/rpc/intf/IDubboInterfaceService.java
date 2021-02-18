package com.cssrc.ibms.api.rpc.intf;

import java.util.List;
import java.util.Map;

public interface IDubboInterfaceService
{
    /**
     * 获取接口代理服务
     * @param _class
     * @param version
     * @return
     */
    public  <T> T getService(Class<T> _class, String version);
    
    /**
     * 获取接口代理服务 返回一个集合，所有实现T的消费者
     * @param _class
     * @param version
     * @return
     */
    public  <T> List<T> getReferenceServiceList(Class<T> _class, String version);
    
    /**
     * 获取接口代理服务 返回一个集合，所有实现T的消费者
     * @param _class
     * @param filterorg 是否要过滤当前应用所属单位
     * @return
     */
    public  <T> List<T> getReferenceServiceList(Class<T> _class, boolean filterorg);
    
    
    /**
     * @param _class
     * @param filterorg 是否要过滤当前应用所属单位
     * @return 返回接口全路径的集合
     */
    public <T> List<Map<String, String>> getReferenceServiceNameList(Class<T> _class, boolean filterorg);
    
    /**
     * 获取用户的请求
     * @param userId
     * @return
     */
    public int getMyRequestList(ICommonParam commonParam);
    
    
    /**
     * 获取用户的代办任务
     * @param userId
     * @return
     */
    public int getPendingMattersList(ICommonParam commonParam);
    
    /**
     * 
     * 用户参与的业务模板中data list
     * 比如用户参与的合同，参与项目或者其他业务表数据，
     * 通过业务表banding的表单。
     * @param userId 用户id
     * @param templateAlias 表单别名
     * @param userField 业务表中用户ID对应的字段名--formfield 中的filedName值
     * @return
     */
    public int getTemplateDataList(ICommonParam commonParam);

    /**
     * 获取一个dubbo 服务接口代理类
     * @param forName
     * @return
     */
    public <T> T getDubboService(Class<T> class_);

    
}