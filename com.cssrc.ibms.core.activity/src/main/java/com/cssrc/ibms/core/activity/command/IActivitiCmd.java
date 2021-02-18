package com.cssrc.ibms.core.activity.command;

import java.util.Map;

/** 
* @ClassName: ProcessCmd 
* @Description: TODO(流程执行命令接口) 
* @author zxg 
* @date 2017年6月8日 上午10:08:18 
*  
*/
public interface IActivitiCmd
{


    /** 
    * @Title: getFormDataMap 
    * @Description: TODO(获取表单数据) 
    * @param @return    
    * @return Map    返回类型 
    * @throws 
    */
    Map getFormDataMap();

    /** 
    * @Title: getBusinessKey 
    * @Description: TODO(获取业务PK) 
    * @param @return     
    * @return String    返回类型 
    * @throws 
    */
    String getBusinessKey();

    /** 
    * @Title: setBusinessKey 
    * @Description: TODO(设置业务PK) 
    * @param @param string     
    * @return void    返回类型 
    * @throws 
    */
    void setBusinessKey(String string);

    /** 
    * @Title: getActDefId 
    * @Description: TODO(获取流程定义ID) 
    * @param @return     
    * @return String    流程定义ID
    * @throws 
    */
    String getActDefId();
    
    /** 
    * @Title: execute 
    * @Description: TODO(执行流程流转方法) 
    * @param @return    Map
    * @return Map<String,Object>    返回执行结果
    * @throws 
    */
    public Map<String,Object> execute();

    /** 
    * @Title: getTaskId 
    * @Description: TODO(获取任务ID) 
    * @param @return    
    * @return String    返回类型 
    * @throws 
    */
    String getTaskId();

    /** 
    * @Title: getRunId 
    * @Description: TODO(获取process run ID) 
    * @param @return     
    * @return Long    返回类型 
    * @throws 
    */
    Long getRunId();
    
    
    /** 
    * @Title: getRunId 
    * @Description: TODO(setprocess run ID) 
    * @param @return     
    * @return Long    返回类型 
    * @throws 
    */
    void setRunId(Long runId);

    /** 
    * @Title: isSaveForm 
    * @Description: TODO(是否保存表单) 
    * @param @return   
    * @return boolean    返回类型 
    * @throws 
    */
    boolean isSaveForm();
}
