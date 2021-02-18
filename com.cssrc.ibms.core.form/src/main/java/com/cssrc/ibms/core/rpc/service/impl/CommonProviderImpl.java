package com.cssrc.ibms.core.rpc.service.impl;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.api.activity.intf.IBpmService;
import com.cssrc.ibms.api.activity.intf.IProcessRunService;
import com.cssrc.ibms.api.activity.model.IProcessCmd;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.activity.model.IProcessTask;
import com.cssrc.ibms.api.activity.model.ITaskAmount;
import com.cssrc.ibms.api.form.model.IFormDialog;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.form.model.ITableModel;
import com.cssrc.ibms.api.jms.intf.IMessageProducer;
import com.cssrc.ibms.api.report.inf.IReportParamService;
import com.cssrc.ibms.api.report.inf.IReportTemplateService;
import com.cssrc.ibms.api.report.inf.ISignService;
import com.cssrc.ibms.api.report.model.IReportParam;
import com.cssrc.ibms.api.report.model.IReportTemplate;
import com.cssrc.ibms.api.rpc.intf.AppInfo;
import com.cssrc.ibms.api.rpc.intf.CommonService;
import com.cssrc.ibms.api.rpc.intf.ICommonParam;
import com.cssrc.ibms.api.system.intf.BaseIndexShowService;
import com.cssrc.ibms.api.system.intf.IMessageSendService;
import com.cssrc.ibms.api.system.intf.ISysBusEventService;
import com.cssrc.ibms.api.system.intf.ISysFileService;
import com.cssrc.ibms.api.system.model.BaseInsPortalParams;
import com.cssrc.ibms.api.system.model.IMessageSend;
import com.cssrc.ibms.api.system.model.ISysFile;
import com.cssrc.ibms.api.sysuser.intf.IResourcesService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.excel.util.ExcelUtil;
import com.cssrc.ibms.core.form.model.DataTemplate;
import com.cssrc.ibms.core.form.model.FormData;
import com.cssrc.ibms.core.form.model.FormDialog;
import com.cssrc.ibms.core.form.model.FormTable;
import com.cssrc.ibms.core.form.service.DataTemplateService;
import com.cssrc.ibms.core.form.service.FormDefService;
import com.cssrc.ibms.core.form.service.FormDialogService;
import com.cssrc.ibms.core.form.service.FormHandlerService;
import com.cssrc.ibms.core.form.service.FormTableService;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.date.DateFormatUtil;


/** 
 * 
* @ClassName: CommonProviderImpl 
* @Description:该类作为dubbo服务的实现类，不注入的dubbo服务中心，
* 不同业务系统 需要根据自身业务需求继承该类注入dubbo服务。
* @author zxg 
* @date 2017年4月27日 下午2:32:40 
*  
*/
public abstract class CommonProviderImpl implements CommonService, Serializable {
	private static final long serialVersionUID = 1L;
	@Resource
	private FormHandlerService formHandlerService;
	@Resource
	private FormDialogService formDialogService;
	@Resource
	private FormDefService formDefService;
	@Resource
	private FormTableService formTableService;
	@Resource
	private IProcessRunService processRunService;
	@Resource
	private DataTemplateService dataTemplateService;
	@Resource
	private ISysBusEventService sysBusEventService;
	@Resource
	private ISysFileService sysFileService;
	@Resource
	private IReportTemplateService reportTemplateService;
	@Resource
	private IReportParamService reportParamService;
	@Resource
	private ISignService signService;
	@Resource 
	private BaseIndexShowService indexShowService;
	@Resource
	private HistoryService historyService;
	@Resource
	private IBpmService bpmService;
	@Resource
    private IResourcesService resourcesService;
	@Resource
	private IMessageSendService messageSendService;
	@Resource
	IMessageProducer messageProducer;
	@Resource
	Properties pluginproperties;
	/**
	 * 1.先考虑从方法formHandlerService.getBpmFormData(xxxx)中获取信息。
	 * 2.需要返回属性名词，属性代号，属性值。
	 * **/
	public Object getFormDatatest(Map<String,Object>params,Long tableId,String pkValue){
		String instanceId = null;
		String actDefId = null; 
		String nodeId = null;
		boolean isCopyFlow = false;
		FormData formData = null;
		Object jObject = null;
		try {
			formData = formHandlerService.getBpmFormData(params,tableId, pkValue, instanceId, actDefId, nodeId, isCopyFlow);
			Map<String, Object> map = formData.getMainFields();
			jObject = JSONObject.toJSON(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jObject;
	}
	/**
	 * 1.从"自定义对话框"中获取数据列表信息。
	 * 2.需要返回表头，表体数据(keyValue)。
	 * */
	public Object getDataListtest(String formDialogAlias,Map<String, Object> params){
		
		
		Object jObject = null;
		return jObject;
	}
    
    // 获取"自定义对话框"信息。
    public FormDialog getByFormDialogAlias(String alias)
    {
        FormDialog bpmFormDialog = formDialogService.getByAlias(alias);
        return bpmFormDialog;
    }
    
    // 获取"自定义对话框"中的列表数据。
    public FormDialog getFormDialogData(String alias, Map<String, Object> paramsMap)
    {
        FormDialog bpmFormDialog = null;
        try
        {
            bpmFormDialog = formDialogService.getData(alias, paramsMap);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bpmFormDialog;
    }
	
	    //获取"自定义对话框"中的树型结构的数据。
		@SuppressWarnings("rawtypes")
		public List getFormDialogTreeData(String alias,Map<String,Object> params,boolean isRoot){
			List list = new ArrayList();
			try {
				list = formDialogService.getTreeData(alias,params,isRoot);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list;
		}
		
	//获取"自定义对话框"中的外键显示值数据
		public Map<String, Object> getFKColumnShowData(String dialogAlias,Map<String, Object> params){
			Map<String, Object> resultMap = new HashMap<String, Object>();
			try {
				resultMap = formDialogService.getFKColumnShowData(dialogAlias, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return resultMap;
		}



		/**
		 * 根据表id获取(主表,rel关系表,sub子表)信息.
		 *
		 * <p>detailed comment</p>
		 * @author [创建人] WeiLei <br/> 
		 * 		   [创建时间] 2016-9-12 上午10:56:54 <br/> 
		 * 		   [修改人] WeiLei <br/>
		 * 		   [修改时间] 2016-9-12 上午10:56:54
		 * @param request
		 * @param response
		 * @throws Exception
		 * @see
		 */
		@Override
		public FormTable getByTableId(Long tableId, int need) {
			return formTableService.getByTableId(tableId, need);
		}
		
		/**
		 * 新增或编辑业务数据模板数据.
		 *
		 * <p>detailed comment</p>
		 * @param displayId
		 * @param pkData
		 * @param userId
		 * @param contextPath
		 * @return
		 * @throws Exception
		 * @see
		 */
		@Override
		public Map<String, Object> editData(Long displayId, String pkData, Long userId, String contextPath) throws Exception{
			Map<String, Object> dataMap = dataTemplateService.editData(displayId, pkData, userId, contextPath);
			return dataMap;
		}
		/**
		 * 保存表单数据.
		 *
		 * <p>detailed comment</p>
		 * @author [创建人] WeiLei <br/> 
		 * 		   [创建时间] 2016-9-12 上午10:56:54 <br/> 
		 * 		   [修改人] hhj <br/>
		 * 		   [修改时间] 
		 * @param tableId
		 * @param need
		 * @param pkData
		 * @param formData
		 * @param displayId
		 * @throws Exception
		 * @see
		 */
		@Override
		public String saveFormData(IFormTable bpmFormTable,Long tableId,int isNeed, String pkData, String formData, String formKey) throws Exception {
			
			formHandlerService.saveFormData((FormTable)bpmFormTable, tableId,isNeed, pkData, formData, formKey);
			return pkData;
		}
		
		/**
		 * 删除数据 ，包括批量删除数据
		  * @author hhj
		 * @param bpmDataTemplateId
		 * @param _pk_ 用逗号撇开。
		 */
		@Override
		public void deleteData(Long bpmDataTemplateId, String _pk_) throws Exception{
			try{
				dataTemplateService.deleteData(bpmDataTemplateId,_pk_);
				dataTemplateService.delFileOfData(_pk_);
			} catch(Exception exception){
				exception.printStackTrace();
			}
			
		}
		
		/**
		 * 附件下载
		 *@author YangBo 
		 *@param request
		 *@param response
		 *@param fileId
		 *@throws IOException
		 */
		@Override
		public void downAttach(HttpServletRequest request,HttpServletResponse response,String fileId) throws IOException{
			sysFileService.downAttach(request,response,fileId);
			
		}
		/**
		 * 附件上传实现
		 * @author YangBo 
		 * @date 2016年12月8日下午9:50:40
		 */
		@Override
		public String uploadAttach(MultipartHttpServletRequest request,HttpServletResponse response,ISysUser appUser,long userId) throws Exception{
			String resultMsg=sysFileService.uploadAttach(request,response,appUser,userId);
			return resultMsg;
		}
		/**
		 * 业务数据详细
		 * @author YangBo 
		 * @throws Exception 
		 * @date 2016年12月8日下午8:39:46
		 */
		@Override
		public String getFormData(Long id, String pk, Long curUserId, String contextPath) throws Exception {
			contextPath = AppUtil.getContextPath();
			String detailData= dataTemplateService.getForm(id,pk, curUserId, contextPath);
			return detailData; 
		}
		/**
		 * 导出业务数据Excel非FR
		 * @author YangBo 
		 * @throws Exception 
		 * @date 2016年12月10日下午2:41:09
		 */
		@Override
		public void exportExcel(Long id, String[] exportIds, int exportType,Map<String, Object> params, HttpServletResponse response, Long curUserId) throws Exception {
			HSSFWorkbook wb = dataTemplateService.export(id,exportIds,exportType,params, curUserId);
			String fileName = "DataTemplate_"+  DateFormatUtil.getNowByString("yyyyMMddHHmmdd");
			ExcelUtil.downloadExcel(wb, fileName, response);
		}
		
		/**
		 * @author YangBo 
		 * @date 2016年12月10日下午4:00:29
		 * mv="report/freport/reportPreView.jsp"
		 */
		@SuppressWarnings("unchecked")
		@Override
		public ModelAndView printReport(ModelAndView mv, String title) {
			IReportTemplate reportTemplate=reportTemplateService.getByTitle(title);
			List<IReportParam> params= (List<IReportParam>) reportParamService.getByReportid(reportTemplate.getReportid());
			mv.addObject("reportTemplate", reportTemplate);
			mv.addObject("params", params);
			return mv;
		}
        @Override
        public byte[] signDownload(HttpServletRequest request, String title, String refUserSign) throws Exception
        {
            return signService.signDownload(request, title, refUserSign);
        }
        
        /**
         * 获取流程实例对象
         */
		@Override
		public IProcessRun getProcessRun(Long runId) {
			IProcessRun processRun=processRunService.getById(runId);
			return processRun;
		}
		
		/**
		 * 启动流程
		 */
		@Override
		public String startProcess(IProcessCmd processCmd) {
			String result = processRunService.startProcessRpc(processCmd);
			return result;
		}
		@Override
		public ModelAndView startFlowView(HttpServletRequest request,ModelAndView mv, String flowKey, Long userId) throws Exception {
			mv = processRunService.startFlowView(request, mv, flowKey, userId);
			return mv;
		}
		
    /**
     * 根据dialog 别名获取 对话框数据源的表名以及表ID
     * 
     * @param alias
     */
    @Override
    public FormTable getFormDilaogTableInfo(String alias)
    {
        FormDialog formDialog = formDialogService.getByAlias(alias);
        String tablename=formDialog.getObjname();
        tablename=tablename.replace(ITableModel.CUSTOMER_TABLE_PREFIX, "");
        tablename=tablename.replace(ITableModel.CUSTOMER_TABLE_PREFIX.toLowerCase(), "");
        FormTable formTable =this.formTableService.getByTableName(tablename);
        return formTable;
    }
    
    /**
     * 获取任务列表（包括待办、已办等）
     */
    @Override
    public Map<String, Object> getTaskMap(BaseInsPortalParams params)
    {
        Map<String, Object> result = indexShowService.getTaskMap(params);
        return result;
    }
    
    /**
     * 获取待办任务数量与总数比（notRead/count）
     */
    @Override
    public String pendingMattersNum(Long userId)
    {
    	List<? extends ITaskAmount> countlist = this.bpmService.getMyTasksCount(userId);
    	int count = 0;
		int notRead = 0;
		for (ITaskAmount taskAmount : countlist) {
			count += taskAmount.getTotal();
			notRead += taskAmount.getNotRead();
		}
		String dataNum = notRead+"/"+count;
        return dataNum;
    }
    
    /**
     * 待办事宜列表（获取的是(page-1)*pageSize到(page-1)*pageSize+pageSize长度的list）
     */
    @Override
    public List<? extends IProcessTask> pendingMattersList(BaseInsPortalParams params,Long userId)
    {
    	int pageSize = params.getPageSize();
    	params.setPageSize(-1);
    	//此时的list是获取的所有数据
    	List<? extends IProcessTask> list = indexShowService.pendingMatters(params,userId);
    	List<? extends IProcessTask> listPart = new ArrayList<IProcessTask>();
    	int page = params.getPage();
    	//开始位置，如果开始位置大于列表长度就返回空
    	int indexStart = (page-1)*pageSize;
    	if(indexStart<=list.size()){
    		//结束位置
    		int indexEnd = indexStart+pageSize;
        	if(indexEnd>list.size()){
        		//如果结束位置大于列表长度就将结束位置置为列表长度
        		indexEnd = list.size();
        	}
        	//截取开始位置到结束位置-1的数据
        	listPart = list.subList(indexStart, indexEnd);
    	}
    	return listPart;
    }
		
    /**
     * 获取用户的请求
     * @param userId
     * @return
     */
    @Override
    public int getMyRequestList(ICommonParam commonParam)
    {
        Object user=commonParam.getUser();
        return this.processRunService.getMyRequestList(Long.valueOf(user.toString()));
    }
    
    /**
     * 获取用户的代办任务
     * @param userId
     * @return
     */
    @Override
    public int getPendingMattersList(ICommonParam commonParam)
    {
        Object user=commonParam.getUser();
        return this.processRunService.getPendingMattersList(Long.valueOf(user.toString()));
    }
    
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
    @Override
    public int getTemplateDataList(ICommonParam commonParam)
    {
        Object user=commonParam.getUser();
        Object userField=commonParam.getUserField();
        Object template=commonParam.getTemplate();
        DataTemplate dataTemplate=dataTemplateService.getByAlias(template.toString());
        FormTable formTable=this.formTableService.getById(dataTemplate.getTableId());
        StringBuffer sql=new StringBuffer("select * from "+formTable.getFactTableName());
        sql.append(" where "+userField+" =?");
        List<Object> param=new ArrayList<Object>();
        param.add(user);
        JdbcTemplate jdbcTemplate = (JdbcTemplate)AppUtil.getBean("jdbcTemplate");
        List<Map<String, Object>> result=jdbcTemplate.queryForList(sql.toString(), param.toArray());
        if(result!=null){
            return result.size();
        }else{
            return 0;
        }
    }
    
    /**
	 * 根据流程定义key获取历史实例变量的值.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人]  WeiLei <br/> 
	 * 		   [创建时间] 2017年4月26日 下午1:50:15 <br/> 
	 * 		   [修改人]  WeiLei <br/>
	 * 		   [修改时间] 2017年4月26日 下午1:50:15
	 * @param flowKey : 流程定义key
	 * @param businessKey : 业务数据Id
	 * @see
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getHisVariablesByKey(String flowKey, String businessKey) {
		
		Map<String, Object> variables = new HashMap<String, Object>();
		List<IProcessRun> processRun = (List<IProcessRun>) processRunService.getByFlowKey(flowKey, businessKey);
		if(processRun.size() > 0){
			String instanceId = processRun.get(processRun.size()-1).getActInstId();
			List<HistoricVariableInstance> historicVariables = historyService.createHistoricVariableInstanceQuery().processInstanceId(instanceId).list();
			for(HistoricVariableInstance historicVariable : historicVariables){
				String varName = historicVariable.getVariableName();
				String varValue = historicVariable.getValue().toString();
				variables.put(varName, varValue);
			}
		}
		return variables;
	}
	
	/**
	 * 根据流程定义Key启动流程.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人]  WeiLei <br/> 
	 * 		   [创建时间] 2017年4月26日 下午1:50:15 <br/> 
	 * 		   [修改人]  WeiLei <br/>
	 * 		   [修改时间] 2017年4月26日 下午1:50:15
	 * @param taskId : 任务Id
	 * @param flowKey : 流程定义Key
	 * @param pk : 数据Id
	 * @param variables : 流程变量
	 * @see
	 */
	public String startProcess(String taskId, String flowKey, String pk, String userId, Map<String, Object> variables) {
		
		String result = processRunService.startProcessRpc(taskId, flowKey, pk, userId, variables);
		return result;
	}
	
	/**
	 * 
	 */
	@Override
	public String getDisplay(Long id, Map<String, Object> params,
			Map<String, Object> queryParams, Long userId) throws Exception {
		
		String providerHtml=dataTemplateService.getDisplay(id, params, queryParams, userId);
		return providerHtml;
	}
		
	/**
     * @param userId 用户ID
     * @return 返回对象为json串，可以直接转成List
     * 返回值不为空说明用户对sysname拥有权限
     */
    @Override
    public String getSysRightByUser(Long userId)
    {
        
        return resourcesService.getSysRightByUser(userId);
    }
    
    /**
     * 通过rpc获取所有对话框
     * @param queryFilter
     * @return
     */
    @Override
    public List<? extends IFormDialog> getAllFormDialoggetAll(QueryFilter queryFilter)
    {
        return formDialogService.getAll(queryFilter);
    }
    
    /**
     * 获取当前服务提供者应用基本信息
     * @return
     */
    @Override
    public AppInfo getAppInfo()
    {
        return new AppInfo();
    }
    
    /**
     * 根据流程实例ID,挂起流程实例.
     */
	public void suspendProcessInstanceById(String instanceId) {
		bpmService.suspendProcessInstanceById(instanceId);
	}
	
	/**
	 * 根据流程实例ID,激活流程实例.
	 */
	public void activateProcessInstanceById(String instanceId) {
		bpmService.activateProcessInstanceById(instanceId);
	}
	
	/**
	 * 获取指定文件的字节数组.
	 */
	public byte[] getFileByteByFileId(Long fileId) {
		return sysFileService.getFileByteByFileId(fileId);
	}
	
	/**
	 * 获取用户收到未读的消息数量
	 */
	public int getCountReceiverMessage(Long userId) {
		//未读消息
		int messCount = this.messageSendService.getCountReceiverByUser(userId);
		return messCount;
	}
	
	/**
	 * 获取未读消息列表
	 */
	public List<?extends IMessageSend> getCountReceiverList(QueryFilter queryFilter) {
		List<?extends IMessageSend> notReadMsgList = this.messageSendService.getReceiverByUser(queryFilter);
		return notReadMsgList;
	}
	
	
    public void sendSyncMdmData(Serializable mdmData) {
        String topicName=pluginproperties.getProperty("jms.massage.topic.syncmdm");
        messageProducer.sendTopic(mdmData, topicName);
    }

	
}