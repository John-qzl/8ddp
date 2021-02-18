package com.cssrc.ibms.api.rpc.intf;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.activity.model.IProcessCmd;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.activity.model.IProcessTask;
import com.cssrc.ibms.api.form.model.IFormDialog;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.system.model.BaseInsPortalParams;
import com.cssrc.ibms.api.system.model.IMessageSend;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;


public interface CommonService  {
	
	/**start 自定义对话框 相关接口调用。**/
    /**
     * 获取"自定义对话框"信息。
     * @param alias 自定义对话框别名
     * @return
     */
    public IFormDialog getByFormDialogAlias(String alias);
    /**
     * 获取"自定义对话框"中的列表数据。
     * @param alias 自定义对话框别名
     * @param paramsMap 参数，key-value
     * @return
     */
    public IFormDialog getFormDialogData(String alias,Map<String, Object> paramsMap);
    
    /**
     * 获取"自定义对话框"中的树型结构的数据。
     * @param alias  自定义对话框别名
     * @param params 参数，key-value
     * @param isRoot 是否根节点
     * @return
     */
    public List<?> getFormDialogTreeData(String alias,Map<String,Object> params,boolean isRoot);
    
    /**
     * 获取"自定义对话框"中的外键显示值数据
     * @param dialogAlias 自定义对话框别名
     * @param params 参数，key-value
     * @return
     */
    public Map<String, Object> getFKColumnShowData(String dialogAlias,Map<String, Object> params);
	/**end 自定义对话框 相关接口调用。
	
	
	
	/**
	 * 根据表id获取(主表,rel关系表,sub子表)信息
	 * @param tableId 表ID
	 * @param need 0为正常字段 1为正常字段加上隐藏字段 2 为正常字段加上逻辑删除字段
	 * @return
	 */
	public IFormTable getByTableId(Long tableId, int need);
	
	/**
	 * 新增或编辑业务数据模板数据
	 * @param displayId 
	 * @param pkData 
	 * @param userId
	 * @param contextPath
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> editData(Long displayId, String pkData, Long userId, String contextPath) throws Exception; 
	
	/**end 编辑数据 相关接口调用。**/
	//保存表单数据
	public String saveFormData(IFormTable bpmFormTable,Long tableId,int isNeed, String pkData, String formData, String formKey)throws Exception;
	/**end 保存表单数据 相关接口调用。 **/
	
	/**
	 * 删除数据
	 * @param displayId dataTemplateId
	 * @param _pk_ 主键
	 * @throws Exception
	 */
	public void deleteData(Long displayId, String _pk_) throws Exception;
	
	
	//获取业务数据详细信息
	public String getFormData(Long id, String pk, Long curUserId, String contextPath) throws Exception;
	
	//查询表单"业务数据"列表。
	public String getDisplay(Long id, Map<String, Object> params,Map<String, Object> queryParams, Long userId)throws Exception;

	/**start 附件相关操作接口**/
	//附件下载方法接口
	public void downAttach(HttpServletRequest request,HttpServletResponse response,String fileId) throws IOException;
	
	//附件上传服务方法接口
	String uploadAttach(MultipartHttpServletRequest request,HttpServletResponse response, ISysUser appUser, long userId) throws Exception;
	
	//导出业务数据
	public void exportExcel(Long id, String[] exportIds, int exportType,Map<String, Object> params, HttpServletResponse response, Long curUserId) throws Exception;
   
	//报表打印服务
	public ModelAndView printReport(ModelAndView mv, String title);
    //报表打印下载接口
	public byte[] signDownload(HttpServletRequest request, String title, String refUserSign) throws Exception;
	
	//获取流程实例
	public IProcessRun getProcessRun(Long runId);
	//启动流程
	public String startProcess(IProcessCmd processCmd);
	//流程启动界面
	public ModelAndView startFlowView(HttpServletRequest request,ModelAndView mv, String flowKey, Long userId) throws Exception;
	//根据流程定义key获取历史实例变量的值
	public Map<String,Object> getHisVariablesByKey(String flowKey, String businessKey);
	//根据流程Key启动流程
	public String startProcess(String taskId, String flowKey, String pk, String userId, Map<String, Object>variables);
	//激活流程实例
	public void activateProcessInstanceById(String instanceId);
	//挂起流程实例
	public void suspendProcessInstanceById(String instanceId);
    /**
     * 根据dialog 别名获取 对话框数据源的表信息
     * @param alias
     */
    public IFormTable getFormDilaogTableInfo(String alias);
	//获取任务列表（包括待办、已办等）
	public Map<String,Object> getTaskMap(BaseInsPortalParams params);
	//获取待办任务数量与总数比（notRead/count）
	public String pendingMattersNum(Long userId);
	//获取待办事宜列表（获取的是(page-1)*pageSize到(page-1)*pageSize+pageSize长度的list）
	public List<? extends IProcessTask> pendingMattersList(BaseInsPortalParams params,Long userId);
	
	 /**
     * 获取用户收到未读的消息数量
     * @param userId
     * @return
     */
    public int getCountReceiverMessage(Long userId);
    
    /**
     * 获取未读消息列表
     * @param userId
     * @return
     */
    public List<?extends IMessageSend> getCountReceiverList(QueryFilter queryFilter);
	
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
     * @param userId 用户ID
     * @return 返回对象为json串，可以直接转成List
     * 返回值不为空说明用户对sysname拥有权限
     */
    public abstract String getSysRightByUser(Long userId);
    
    
    /**
     * 通过rpc获取所有对话框
     * @param queryFilter
     * @return
     */
    public List<?extends IFormDialog> getAllFormDialoggetAll(QueryFilter queryFilter);

    /**
     * 获取当前服务提供者应用基本信息
     * @return
     */
    public AppInfo getAppInfo();
    
    
    public String getClassName();
    
    
    
    /**
     * 同步 用户 组织 角色 岗位等主数据
     * @param mdmData
     */
    public void syncMdmData(Object mdmData);

    /**
     * 获取与指定用户相关的业务数据个数
     */
    public String getBusinessDataNum(Long userId);

    /**
     * 获取服务提供者接口地址
     * @return
     */
    /*    
     * public String getServiceClass();
     * 
     * */
    
    /**
     * 根据文件ID获取文件字节数组
     */
    public byte[] getFileByteByFileId(Long userId);
    
    
    
    /** 
    * @Title: sendSyncMdmData 
    * @Description: TODO(主数据同步，远程发送mq消息) 
    * @param @param mdmData     
    * @return void    返回类型 
    * @throws 
    */
    public void sendSyncMdmData(Serializable mdmData);
}