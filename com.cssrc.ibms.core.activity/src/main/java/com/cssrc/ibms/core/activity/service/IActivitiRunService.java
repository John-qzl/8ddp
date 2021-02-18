package com.cssrc.ibms.core.activity.service;

import java.util.Map;

import com.cssrc.ibms.core.activity.model.ProcessCmd;

public interface IActivitiRunService
{
    /** 
    * @Title: execute 
    * @Description: TODO(流程执行接口) 
    * @param @param cmd
    * @param @return    设定文件 
    * @return Map<String,Object>    返回类型 
    * @throws 
    */
    public Map<String,Object> execute(ProcessCmd cmd);
    
    /** 
    * @Title: redo 
    * @Description: TODO(流程执行取消操作) 
    * @param @param cmd
    * @param @return    设定文件 
    * @return Map<String,Object>    返回类型 
    * @throws 
    */
    public Map<String,Object> redo(ProcessCmd cmd);

    /** 
    * @Title: tranto 
    * @Description: TODO(流程跳转操作) 
    * @param @param cmd
    * @param @return    设定文件 
    * @return Map<String,Object>    返回类型 
    * @throws 
    */
    public Map<String,Object> tranto(ProcessCmd cmd);
    
    /** 
    * @Title: callBack 
    * @Description: TODO(流程任务执行后回调) 
    * @param @param cmd
    * @param @return    设定文件 
    * @return Map<String,Object>    返回类型 
    * @throws 
    */
    public Map<String,Object> callBack(ProcessCmd cmd);
    
    /** 
    * @Title: preTranto 
    * @Description: TODO(流程执行准备数据) 
    * @param @param cmd
    * @param @return    设定文件 
    * @return Map<String,Object>    返回类型 
    * @throws 
    */
    public Map<String,Object> preTranto(ProcessCmd cmd);

}
