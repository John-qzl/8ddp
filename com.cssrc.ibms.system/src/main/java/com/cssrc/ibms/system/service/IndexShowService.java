package com.cssrc.ibms.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.api.activity.intf.IBpmService;
import com.cssrc.ibms.api.activity.intf.IProCopytoService;
import com.cssrc.ibms.api.activity.intf.IProcessRunService;
import com.cssrc.ibms.api.activity.intf.ITaskExeService;
import com.cssrc.ibms.api.activity.intf.ITaskReminderService;
import com.cssrc.ibms.api.activity.model.IProCopyto;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.activity.model.IProcessTask;
import com.cssrc.ibms.api.activity.model.ITaskAmount;
import com.cssrc.ibms.api.activity.model.ITaskExe;
import com.cssrc.ibms.api.activity.model.ITaskWarningSet;
import com.cssrc.ibms.api.system.intf.BaseIndexShowService;
import com.cssrc.ibms.api.system.intf.IMessageSendService;
import com.cssrc.ibms.api.system.intf.ISysParameterService;
import com.cssrc.ibms.api.system.model.BaseInsPortalParams;
import com.cssrc.ibms.api.system.model.IMessageSend;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.index.model.TaskListTab;
/**
 * 首页模板读取数据类
 * @author YangBo
 *
 */
@Service
public class IndexShowService implements BaseIndexShowService{
	@Resource
	private IBpmService bpmService;
	
	@Resource
	private IProcessRunService processRunService;
	
	@Resource
	private IMessageSendService messageSendService;
	
	@Resource
	private ITaskExeService taskExeService;
	
	@Resource
	private IProCopytoService proCopytoService;
	@Resource
	ITaskReminderService taskReminderService ;
	@Resource
	ISysParameterService sysParameterService;
	/**
	 * 待办事宜 服务访问
	 * @param params
	 * @return
	 */
	public List<? extends IProcessTask> pendingMatters(BaseInsPortalParams params,Long userId) {
		
		List<? extends IProcessTask> list = new ArrayList<IProcessTask>();
		PagingBean page = new PagingBean();
		
		page.setCurrentPage(Integer.valueOf(0));
		page.setPageSize(params.getPageSize());
		
		list = this.bpmService.getTasks(userId,
				null, null, null, null, "desc", page);
		return list;
	}
	
	/**
	 * 待办事宜
	 * @param params
	 * @return
	 */
	public List<? extends IProcessTask> pendingMatters(BaseInsPortalParams params) {
		
		List<? extends IProcessTask> list = new ArrayList<IProcessTask>();
		PagingBean page = new PagingBean();
		
		page.setCurrentPage(Integer.valueOf(0));
		page.setPageSize(params.getPageSize());
		
		list = this.bpmService.getTasks( UserContextUtil.getCurrentUserId(),
				null, null, null, null, "desc", page);
		return list;
	}
	
	/**
	 * 已办事宜
	 * @param params
	 * @return
	 */
	public List<?extends IProcessRun> alreadyMatters(BaseInsPortalParams params) {
		List<?extends IProcessRun> list = new ArrayList<IProcessRun>();
		
		PagingBean page = new PagingBean();
		page.setCurrentPage(Integer.valueOf(0));
		page.setPageSize(params.getPageSize());
		
		page.setShowTotal(false);
		
		try {
			list = this.processRunService.myAlready(UserContextUtil.getCurrentUserId(),page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 未读消息
	 * @param params
	 * @return
	 */
	public List<?extends IMessageSend> getMessage(BaseInsPortalParams params) {
		List<?extends IMessageSend> list = new ArrayList<IMessageSend>();
		
		PagingBean page = new PagingBean();
		page.setCurrentPage(Integer.valueOf(0));
		page.setPageSize(params.getPageSize());
		
		try {
			list = this.messageSendService.getNotReadMsgByUserId(UserContextUtil.getCurrentUserId().longValue(), page);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
	
	/**
	 * 办结事宜
	 * @param params
	 * @return
	 */
	public List<? extends IProcessRun> completedMatters(BaseInsPortalParams params) {
		List<? extends IProcessRun> list = new ArrayList<IProcessRun>();
		
		PagingBean page = new PagingBean();
		page.setCurrentPage(Integer.valueOf(0));
		page.setPageSize(params.getPageSize());

		page.setShowTotal(false);
		try {
			list = this.processRunService.completedMatters(UserContextUtil
					.getCurrentUserId(), page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 我的办结
	 * @param params
	 * @return
	 */
	public List<?extends IProcessRun> myCompleted(BaseInsPortalParams params) {
		List<?extends IProcessRun> list = new ArrayList<IProcessRun>();
		
		long curUserId = UserContextUtil.getCurrentUserId().longValue();
		PagingBean page = new PagingBean();
		page.setCurrentPage(Integer.valueOf(0));
		page.setPageSize(params.getPageSize());

		page.setShowTotal(false);
		try {
			list = this.processRunService.myCompleted(Long .valueOf(curUserId), page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 我的请求
	 * @param params
	 * @return
	 */
	public List<?extends IProcessRun> myRequest(BaseInsPortalParams params) {
		List<?extends IProcessRun> list = new ArrayList<IProcessRun>();
		
		long curUserId = UserContextUtil.getCurrentUserId().longValue();
		PagingBean page = new PagingBean();
		page.setCurrentPage(Integer.valueOf(0));
		page.setPageSize(params.getPageSize());

		page.setShowTotal(false);
		try {
			list = this.processRunService.myStart(Long .valueOf(curUserId), page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 我的草稿
	 * @param params
	 * @return
	 */
	public List<?extends IProcessRun> getMyDraft(BaseInsPortalParams params) {
		List<?extends IProcessRun> list = new ArrayList<IProcessRun>();
		
		long curUserId = UserContextUtil.getCurrentUserId().longValue();
		PagingBean page = new PagingBean();
		page.setCurrentPage(Integer.valueOf(0));
		page.setPageSize(params.getPageSize());

		page.setShowTotal(false);
		try {
			list = this.processRunService.getMyDraft(Long .valueOf(curUserId), page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 转办代理事宜
	 * @param params
	 * @return
	 */
	public List<?extends ITaskExe> getMyAccordingMatter(BaseInsPortalParams params) {
		List<?extends ITaskExe> list = new ArrayList<ITaskExe>();
		
		long curUserId = UserContextUtil.getCurrentUserId().longValue();
		PagingBean page = new PagingBean();
		page.setCurrentPage(Integer.valueOf(0));
		page.setPageSize(params.getPageSize());

		try {
			list = this.taskExeService.accordingMattersList(Long .valueOf(curUserId), page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 我的抄送转发
	 * @param params
	 * @return
	 */
	public List<?extends IProCopyto> getMyProCopyto(BaseInsPortalParams params){
		List<?extends IProCopyto> list = new ArrayList<IProCopyto>();
		
		long curUserId = UserContextUtil.getCurrentUserId().longValue();
		PagingBean page = new PagingBean();
		page.setCurrentPage(Integer.valueOf(0));
		page.setPageSize(params.getPageSize());

		try {
			list = this.proCopytoService.getMyProCopytoList(Long .valueOf(curUserId), page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	
	/**
	 * 待办任务Tab信息
	 * @return
	 */
	public TaskListTab pendingMattersTab(){
		List<? extends ITaskAmount> countlist = this.bpmService.getMyTasksCount(UserContextUtil.getCurrentUserId());
		int count = 0;
		int notRead = 0;
		for (ITaskAmount taskAmount : countlist) {
			count += taskAmount.getTotal();
			notRead += taskAmount.getNotRead();
		}
		TaskListTab taskTab = new TaskListTab();
		taskTab.setTabName("待办");
		taskTab.setDataNum(notRead+"/"+count);
		taskTab.setUrl("/oa/flow/task/pendingMatters.do");
		return taskTab;
	}
	
	/**
	 * 草稿Tab信息Tab
	 * @return
	 */
	public TaskListTab getMyDraftTab(){
		List<?extends IProcessRun> list = this.processRunService.getMyDraft(UserContextUtil.getCurrentUserId(),null);
		TaskListTab taskTab = new TaskListTab();
		taskTab.setTabName("草稿");
		taskTab.setDataNum(String.valueOf(list.size()));
		taskTab.setUrl("/oa/flow/processRun/myForm.do");
		return taskTab;
	}
	
	/**
	 * 已办事宜Tab
	 * @return
	 */
	public TaskListTab getMyAlreadyTab(){
		List<?extends IProcessRun> list = this.processRunService.myAlready(UserContextUtil.getCurrentUserId(), null);
		TaskListTab taskTab = new TaskListTab();
		taskTab.setTabName("已办");
		taskTab.setDataNum(String.valueOf(list.size()));
		taskTab.setUrl("/oa/flow/processRun/alreadyMatters.do");
		return taskTab;
	}
	
	/**
	 * 我的办结Tab
	 * @return
	 */
	public TaskListTab getMyCompletedMattersTab(){
		List<?extends IProcessRun> list = this.processRunService.completedMatters(UserContextUtil.getCurrentUserId(), null);
		TaskListTab taskTab = new TaskListTab();
		taskTab.setTabName("办结");
		taskTab.setDataNum(String.valueOf(list.size()));
		taskTab.setUrl("/oa/flow/processRun/completedMatters.do");
		return taskTab;
	}
	
	/**
	 *转办代理事宜Tab
	 * @return
	 */
	public TaskListTab getMyAccordingMatterTab(){
		
		List<?extends ITaskExe> list = this.taskExeService.accordingMattersList(UserContextUtil.getCurrentUserId(), null);
		TaskListTab taskTab = new TaskListTab();
		taskTab.setTabName("转办代理");
		taskTab.setDataNum(String.valueOf(list.size()));
		taskTab.setUrl("/oa/flow/taskExe/accordingMatters.do");
		return taskTab;
		
	}
	
	/**
	 * 抄送转发Tab
	 * @return
	 */
	public TaskListTab getMyProCopytoTab(){
		
		Integer proCount = this.proCopytoService.getCountByUser(UserContextUtil.getCurrentUserId());
		Integer noReadProCount = this.proCopytoService.getCountNotReadByUser(UserContextUtil.getCurrentUserId());
		TaskListTab taskTab = new TaskListTab();
		taskTab.setTabName("抄送");
		taskTab.setDataNum(noReadProCount + "/" + proCount);
		taskTab.setUrl("/oa/flow/proCopyto/myList.do");
		return taskTab;
		
	}
	
	/**
	 * 我的请求
	 * @return
	 */
	public TaskListTab getMyRequestTab(){
		List<?extends IProcessRun> list = this.processRunService.myStart(UserContextUtil.getCurrentUserId(), null);
		TaskListTab taskTab = new TaskListTab();
		taskTab.setTabName("我的请求");
		taskTab.setDataNum(String.valueOf(list.size()));
		taskTab.setUrl("/oa/flow/processRun/myRequest.do");
		return taskTab;
	}

	/**
	 * 封装任务列表信息
	 * @param params
	 * @return
	 */
	public List<TaskListTab> getTaskTab() {
		List<TaskListTab> list = new ArrayList<TaskListTab>();
		list.add(pendingMattersTab());
		list.add(getMyDraftTab());
		list.add(getMyAlreadyTab());
		/*	list.add(getMyCompletedMattersTab());
		list.add(getMyAccordingMatterTab());
		list.add(getMyProCopytoTab());*/
		list.add(getMyRequestTab());
		
		return list;
	}
	
	/**
	 * 任务列表模块数据
	 * @param params
	 * @return
	 */
	public Map<String,Object> getTaskMap(BaseInsPortalParams params){
		Map<String,Object> taskMap = new HashMap<String, Object>();
		//添加tab
		List<TaskListTab> taskTab = this.getTaskTab();
		taskMap.put("taskTab", taskTab);
		//添加列表值
		//待办任务
		Long userId = UserContextUtil.getCurrentUserId();
		List<? extends IProcessTask> pendingMatters = this.pendingMatters(params,userId);
		taskMap.put("pendingMatters", pendingMatters);
		
		//草稿
		List<?extends IProcessRun> myDraft = this.getMyDraft(params);
		taskMap.put("myDraft", myDraft);
		//已办
		List<?extends IProcessRun> alreadyMatters = this.alreadyMatters(params);
		taskMap.put("alreadyMatters", alreadyMatters);
		//办结
		List<? extends IProcessRun> completedMatters = this.completedMatters(params);
		taskMap.put("completedMatters", completedMatters);
		//我的转办代理
		List<?extends ITaskExe> myAccordingMatter = this.getMyAccordingMatter(params);
		taskMap.put("myAccordingMatter", myAccordingMatter);
		
		//抄送转发
		List<?extends IProCopyto> MyProCopyto = this.getMyProCopyto(params);
		taskMap.put("MyProCopyto", MyProCopyto);
		
		//我的请求
		List<?extends IProcessRun> myRequest = this.myRequest(params);
		taskMap.put("myRequest", myRequest);
		
	    //预警任务
		Map<String,Object> warning = this.getPortalWarnTask(params);
        taskMap.put("myWarning", warning);
        
		return taskMap;
	}
	
	
    /** 
    * @Title: getPortalWarnTask 
    * @Description: TODO(获取预警任务) 
    * @param @param params
    * @param @return     
    * @return Map<String,Object>    返回类型 
    * @throws 
    */
    public Map<String,Object> getPortalWarnTask(BaseInsPortalParams params){
        
        Map<String,Object> result=new HashMap<String,Object>();
        Date curDate=new Date();
        List<ITaskWarningSet> tab=new ArrayList<ITaskWarningSet>();
        Map<String,List<IProcessTask>> taskMap = new HashMap<String, List<IProcessTask>>();
        //待办任务
        Long userId = UserContextUtil.getCurrentUserId();
        List<? extends IProcessTask> pendingMatters = this.pendingMatters(params,userId);
        //按照预警等级 作为key 划分待办数据
        for(IProcessTask task:pendingMatters){
            ITaskWarningSet warn=taskReminderService.getWarningSet(task,curDate);
            if(warn!=null){
                if(!hasWarn(tab,warn)){
                    tab.add(warn);
                }
                task.setWarnSet(this.getWarnSet(warn.getLevel()));
                List<IProcessTask> tasks=taskMap.get(warn.getKey());
                if(tasks==null){
                    tasks=new ArrayList<IProcessTask>();
                }
                tasks.add(task);
                taskMap.put(String.valueOf(warn.getKey()), tasks);
            }
        }
        result.put("tab", tab);
        result.put("taskMap", taskMap);
        return result;
    }
	
    private  boolean hasWarn(List<ITaskWarningSet> tab,ITaskWarningSet warn){
        if(tab==null){
            return true;
        }
        for(ITaskWarningSet ws:tab){
            if(ws.getLevel()==warn.getLevel()){
                return true;
            }
        }
        return false;
    }
    private String getWarnSet(int level){
        String  taskReminderConfs=this.sysParameterService.getByAlias(ISysParameterService.taskReminderConf);
        JSONArray confs=JSONArray.parseArray(taskReminderConfs);
        for(Object conf:confs){
            JSONObject jconf=(JSONObject)conf;
            int l=jconf.getInteger("level");
            if(l==level){
                return jconf.toString();
            }
        }
        return null;
    }
}
