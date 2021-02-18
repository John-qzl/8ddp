package com.cssrc.ibms.core.flow.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.cssrc.ibms.api.activity.intf.IBpmService;
import com.cssrc.ibms.api.activity.intf.IProcessRunService;
import com.cssrc.ibms.api.activity.intf.ITaskOpinionService;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.api.report.inf.ISignItemService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.flow.dao.ExecutionDao;
import com.cssrc.ibms.core.flow.dao.FinishTask;
import com.cssrc.ibms.core.flow.dao.TaskOpinionDao;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.TaskOpinion;
import com.cssrc.ibms.core.flow.util.FlowUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 对象功能:流程任务审批意见 Service类  
 * 开发人员:zhulongchao  
 */
@Service
public class TaskOpinionService extends BaseService<TaskOpinion> implements ITaskOpinionService
{
    @Resource
    private TaskOpinionDao dao;
    
    @Resource
    private ExecutionDao executionDao;
    
    @Resource
    private IBpmService bpmService;
    
    @Resource
    private ISysUserService sysUserService;
    
    @Resource
    private TaskUserService taskUserService;
    
    @Resource
    ISignItemService signItemService;
    
    @Resource
    IProcessRunService processRunService;
    
    @Resource
    IDataTemplateService dataTemplateService;
    
    @Resource
    IFormDefService formDefService;
    
    @Resource
    DefinitionService definitionService;
    
    protected static Logger logger = LoggerFactory.getLogger(TaskOpinionService.class);
    
    public TaskOpinionService()
    {
    }
    
    @Override
    protected IEntityDao<TaskOpinion, Long> getEntityDao()
    {
        return dao;
    }
    
    /**
     * 取得对应该任务的执行
     * 
     * @param taskId
     * @return
     */
    public TaskOpinion getByTaskId(Long taskId)
    {
        return dao.getByTaskId(taskId);
    }
    
    /**
     * 取得某个任务的所有审批意见 按时间排序
     * 
     * @param actInstId
     * @return
     */
    public List<TaskOpinion> getByActInstId(String actInstId, boolean isAsc)
    {
        
        String supInstId = getTopInstId(actInstId);
        
        List<String> instList = new ArrayList<String>();
        instList.add(supInstId);
        
        getChildInst(supInstId, instList);
        
        return dao.getByActInstId(instList, isAsc);
    }
    
    /**
     * 返回的是最早的父流程实例ID
     * 注：yangbo
     * @param actInstId
     * @return
     */
    private String getTopInstId(String actInstId)
    {
        String rtn = actInstId;
        try
        {
            String supInstId = dao.getSupInstByActInstId(actInstId);
            while (StringUtil.isNotEmpty(supInstId))
            {
                rtn = supInstId;
                supInstId = dao.getSupInstByActInstId(supInstId);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return rtn;
    }
    
    /**
     * 通过父流程实例ID遍历所有流程实例ID（记住一个流程实例ID会有一个流程任务审批意见列表）
     * 注：yangbo
     * @param supperId
     * @param instList
     */
    private void getChildInst(String supperId, List<String> instList)
    {
        List<TaskOpinion> list = this.dao.getBySupInstId(supperId);
        if (BeanUtils.isEmpty(list))
            return;
        for (TaskOpinion taskOpinion : list)
        {
            String instId = taskOpinion.getActInstId();
            instList.add(instId);
            getChildInst(instId, instList);
        }
    }
    
    /**
     * 取得某个任务的所有审批意见 
     * 
     * @param actInstId
     * @return
     */
    public List<TaskOpinion> getByActInstId(String actInstId)
    {
        return getByActInstId(actInstId, true);
    }
    
    public List<TaskOpinion> getFormOptionsByInstance(String instanceId)
    {
        return dao.getFormOptionsByInstance(instanceId);
    }
    
    public List<TaskOpinion> getFormOpinionByActInstId(String actInstId)
    {
        return dao.getFormOpinionByActInstId(actInstId, false);
    }
    
    public List<TaskOpinion> getOpinionByActInstId(String actInstId)
    {
        return dao.getOpinionByActInstId(actInstId);
    }
    
    /**
     * 向上查找。
     * 递归获取所有关联的流程实例ID.(根据子流程获取所有父流程ID)
     * 
     * @param actInstIds
     * @param actInstId
     */
    private void putRelateActInstIdIntoList(List<Long> actInstIds, Long actInstId)
    {
        TaskOpinion opinion = dao.getByInstId(actInstId);
        if (opinion != null && StringUtil.isNotEmpty(opinion.getSuperExecution()))
        {
            actInstIds.add(new Long(opinion.getSuperExecution()));
            putRelateActInstIdIntoList(actInstIds, new Long(opinion.getSuperExecution()));
        }
    }
    
    /**
     * 向下查找。
     * @param actInstIds
     * @param actInstId
     */
    private void putSubActInstIdIntoList(List<Long> actInstIds, Long actInstId)
    {
        
        List<TaskOpinion> list = dao.getBySuperInstId(actInstId);
        if (list.size() > 0)
        {
            for (TaskOpinion opinion : list)
            {
                actInstIds.add(new Long(opinion.getActInstId()));
                putSubActInstIdIntoList(actInstIds, new Long(opinion.getActInstId()));
            }
        }
    }
    
    /**
     * 根据act流程定义Id删除对应在流程任务审批意见
     * 
     * @param actDefId
     */
    public void delByActDefIdTaskOption(String actDefId)
    {
        dao.delByActDefIdTaskOption(actDefId);
    }
    
    /**
     * 根据流程实例Id及任务定义Key取得审批列表
     * 
     * @param actInstId
     * @param taskKey
     * @return
     */
    public List<TaskOpinion> getByActInstIdTaskKey(Long actInstId, String taskKey)
    {
        return dao.getByActInstIdTaskKey(actInstId, taskKey);
    }
    
    /**
     * 取到最新的某个节点的审批记录
     * 
     * @param actInstId
     * @param taskKey
     * @return
     */
    public TaskOpinion getLatestTaskOpinion(Long actInstId, String taskKey)
    {
        List<TaskOpinion> list = getByActInstIdTaskKey(actInstId, taskKey);
        if (list != null && list.size() > 0)
        {
            return list.get(0);
        }
        return null;
    }
    
    /**
     * 取得某个流程实例中，某用户最新的完成的审批记录
     * 
     * @param actInstId
     * @param exeUserId
     * @return
     */
    public TaskOpinion getLatestUserOpinion(String actInstId, Long exeUserId)
    {
        List<TaskOpinion> taskOpinions = dao.getByActInstIdExeUserId(actInstId, exeUserId);
        if (taskOpinions.size() == 0)
            return null;
        return taskOpinions.get(0);
    }
    
    /**
     * 按任务ID删除
     * 
     * @param taskId
     */
    public void delByTaskId(Long taskId)
    {
        dao.delByTaskId(taskId);
    }
    
    /**
     * 取得已经审批完成的任务
     * 
     * @param userId
     * @param subject
     *            事项名
     * @param taskName
     *            任务名
     * @param pb
     *            分页
     * @return
     */
    public List<FinishTask> getByFinishTask(Long userId, String subject, String taskName, PagingBean pb)
    {
        return dao.getByFinishTask(userId, subject, taskName, pb);
    }
    
    /**
     * 获取正在审批的意见。
     * @param actInstId
     * @return
     */
    public List<TaskOpinion> getCheckOpinionByInstId(Long actInstId)
    {
        return dao.getCheckOpinionByInstId(actInstId);
    }
    
    public List<TaskOpinion> setTaskOpinionExecutor(List<TaskOpinion> opinions){
		for (TaskOpinion taskOpinion : opinions) {
		    Definition definition=definitionService.getByActDefId(taskOpinion.getActDefId());
		    taskOpinion.setFlowSubject(definition.getSubject());
		    //设置印章信息
		    taskOpinion.setSignImage(signItemService.getImgeData(taskOpinion.getSignId()));
			if(taskOpinion.getCheckStatus()==TaskOpinion.STATUS_CHECKING.shortValue()){
				TaskEntity task = bpmService.getTask(taskOpinion.getTaskId().toString());
				if(BeanUtils.isNotEmpty(task) ){
					//执行人不为空
					String assignee=task.getAssignee();
					if(FlowUtil.isAssigneeNotEmpty(assignee)){
						String fullname = "";
						ISysUser sysuser = sysUserService.getById(new Long(assignee));
						if(BeanUtils.isNotEmpty(sysuser))
							fullname =  sysuser.getFullname();
						taskOpinion.setExeFullname(fullname);
					}
					//获取候选人
					else{
						 Set<TaskExecutor> set= taskUserService.getCandidateExecutors(task.getId());
//						 String fullname="";
						 Set<ISysUser> sysUsers=new HashSet<ISysUser>();
						 for(Iterator<TaskExecutor> it=set.iterator();it.hasNext();){
							 TaskExecutor taskExe=it.next();
							 if(taskExe==null){
								 continue;
							 }
							 Set<ISysUser> users = taskExe.getSysUser();
							 sysUsers.addAll(users);
						 }
						 if(taskOpinion.getCandidateUsers()==null){
							 taskOpinion.setCandidateUsers(new ArrayList<ISysUser>());
						 }
						taskOpinion.getCandidateUsers().addAll(sysUsers);
					}
				}
			}
		}
		return opinions;
	}
    
    /**
     * 设置执行人的名称
     * @param list
     * @return
     */
    public List<TaskOpinion> setTaskOpinionExeFullname(List<TaskOpinion> list)
    {
        for (TaskOpinion taskOpinion : list)
        {
            if (taskOpinion.getCheckStatus() == TaskOpinion.STATUS_CHECKING.shortValue())
            {
                TaskEntity task = bpmService.getTask(taskOpinion.getTaskId().toString());
                if (BeanUtils.isNotEmpty(task))
                {
                    // 执行人为空
                    String assignee = task.getAssignee();
                    if (FlowUtil.isAssigneeNotEmpty(assignee))
                    {
                        String fullname = "";
                        ISysUser sysuser = sysUserService.getById(new Long(assignee));
                        if (BeanUtils.isNotEmpty(sysuser))
                            fullname = sysuser.getFullname();
                        taskOpinion.setExeFullname(fullname);
                    }
                    // 获取候选人
                    else
                    {
                        Set<TaskExecutor> set = taskUserService.getCandidateExecutors(task.getId());
                        String fullname = "";
                        for (Iterator<TaskExecutor> it = set.iterator(); it.hasNext();)
                        {
                            TaskExecutor taskExe = it.next();
                            String type = taskExe.getType();
                            if (taskExe.getType().equals(TaskExecutor.USER_TYPE_USER))
                            {
                                fullname +=
                                    sysUserService.getById(new Long(taskExe.getExecuteId())).getFullname() + "<br/>";
                            }
                        }
                        taskOpinion.setExeFullname(fullname);
                        taskOpinion.setCandidateUser(fullname);
                    }
                }
            }
        }
        return list;
    }
    
    /**
     * 设置执行人的名称
     * @param list
     * @return
     */
    public List<TaskOpinion> setTaskOpinionListExeFullname(List<TaskOpinion> list)
    {
        for (TaskOpinion taskOpinion : list)
        {
            if (taskOpinion.getCheckStatus() == TaskOpinion.STATUS_CHECKING.shortValue())
            {
                TaskEntity task = bpmService.getTask(taskOpinion.getTaskId().toString());
                if (BeanUtils.isNotEmpty(task))
                {
                    // 执行人为空
                    String assignee = task.getAssignee();
                    if (FlowUtil.isAssigneeNotEmpty(assignee))
                    {
                        String fullname = sysUserService.getById(new Long(assignee)).getFullname();
                        taskOpinion.setExeFullname(fullname);
                    }
                    // 获取候选人
                    else
                    {
                        Set<TaskExecutor> set = taskUserService.getCandidateExecutors(task.getId());
                        String fullname = "";
                        for (Iterator<TaskExecutor> it = set.iterator(); it.hasNext();)
                        {
                            TaskExecutor taskExe = it.next();
                            String type = taskExe.getType();
                            if (taskExe.getType().equals(TaskExecutor.USER_TYPE_USER))
                            {
                                fullname += sysUserService.getById(new Long(assignee)).getFullname() + "<br/>";
                            }
                        }
                        // taskOpinion.setExeFullname(fullname);
                        taskOpinion.setCandidateUser(fullname);
                    }
                }
                else
                {
                    if (BeanUtils.isNotEmpty(taskOpinion.getExeUserId()))
                    {
                        String fullname = sysUserService.getById(new Long(taskOpinion.getExeUserId())).getFullname();
                        taskOpinion.setExeFullname(fullname);
                    }
                }
            }
            else
            {
                String fullname = sysUserService.getById(new Long(taskOpinion.getExeUserId())).getFullname();
                taskOpinion.setExeFullname(fullname);
            }
        }
        return list;
    }
    
    /**
     * 根据实例节点获取任务实例状态。
     * @param actInstId
     * @param taskKey
     * @param checkStatus
     * @return
     */
    public List<TaskOpinion> getByActInstIdTaskKeyStatus(String actInstId, String taskKey, Short checkStatus)
    {
        return dao.getByActInstIdTaskKeyStatus(actInstId, taskKey, checkStatus);
    }
    
    public TaskOpinion getOpinionByTaskId(Long taskId, Long userId)
    {
        return dao.getOpinionByTaskId(taskId, userId);
    }
    
    /**
     * 根据actInstId更新。
     * @param actInstId
     * @param oldActInstId
     * @return
     */
    public int updateActInstId(String actInstId, String oldActInstId)
    {
        return dao.updateActInstId(actInstId, oldActInstId);
    }
    
    /**
     * 任务审批意见Model对象字符串化
     * @param opinion
     * @param isHtml
     * @return
     */
    public static String getOpinion(TaskOpinion opinion, boolean isHtml)
    {
        Map model = new HashMap();
        model.put("opinion", opinion);
        FreemarkEngine freemarkEngine = (FreemarkEngine)AppUtil.getBean(FreemarkEngine.class);
        String rtn = "";
        try
        {
            String template = isHtml ? "opinion.ftl" : "opiniontext.ftl";
            rtn = freemarkEngine.mergeTemplateIntoString(template, model);
            if (!isHtml)
                rtn = rtn.replaceAll("</?[^>]+>", "");
        }
        catch (Exception e)
        {
            logger.debug(e.getMessage());
        }
        return rtn;
    }
    
    @Override
    public String getOpinion(Map<String, String> parmas)
    {
        
        String dataKey = parmas.get("dataKey");
        String nodeSetKey = parmas.get("nodeSetKey");
        String displayId = parmas.get("displayId");
        String voteCode = parmas.get("voteCode");
        IDataTemplate bpmDataTemplate = dataTemplateService.getById(Long.valueOf(displayId));
        Long defId = bpmDataTemplate.getDefId();
        IProcessRun processRun = processRunService.getByBusinessKeyAndDefId(dataKey, defId);
        if (processRun == null)
        {
            return "";
        }
        else
        {
            String actInstId = processRun.getActInstId();
            parmas.clear();
            parmas.put("actInstId", actInstId);
            parmas.put("taskKey", nodeSetKey);
            parmas.put("voteCode", voteCode);
            List<TaskOpinion> taskOpinions = dao.getOpinionForReport(parmas);
            if (taskOpinions != null && taskOpinions.size() > 0)
            {
                return JSON.toJSONString(taskOpinions.get(taskOpinions.size() - 1));
            }
            else
            {
                return JSON.toJSONString(new ResultMessage(ResultMessage.Fail, "没有相关的数据"));
            }
        }
    }
}
