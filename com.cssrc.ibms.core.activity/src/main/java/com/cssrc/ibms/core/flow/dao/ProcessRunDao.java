/**
 * <pre>
 * 对象功能:流程实例扩展Dao类  
 * 创建时间:2014-12-03 09:33:06
 * </pre>
 */
package com.cssrc.ibms.core.flow.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.ProcessRun;
import com.cssrc.ibms.core.util.bean.BeanUtils;

@Repository
public class ProcessRunDao extends BaseDao<ProcessRun>
{
    @SuppressWarnings("rawtypes")
    @Override
    public Class getEntityClass()
    {
        return ProcessRun.class;
    }
    
    @Override
    public ProcessRun getById(Long primaryKey)
    {
        ProcessRun processRun = super.getById(primaryKey);
        if (processRun == null)
        {
            return getByIdHistory(primaryKey);
        }
        else
        {
            return processRun;
        }
    }
    
    /**
     * 获取历史实例
     * @param queryFilter
     * @return
     */
    public List<ProcessRun> getAllHistory(QueryFilter queryFilter)
    {
        return getBySqlKey("getAllFinish", queryFilter);
    }
    
    /**
     * 通过Act的流程实例Id获取ProcessRun实体
     * @param processInstanceId
     * @return
     */
    public ProcessRun getByActInstanceId(Long processInstanceId)
    {
        ProcessRun processRun = getUnique("getByActInstanceId", processInstanceId);
        if (processRun == null)
        {
            return getByActInstanceIdHistory(processInstanceId);
        }
        else
        {
            return processRun;
        }
    }
    
    /**
     * 通过Act的流程实例Id获取流程ProcessRun实体
     * @param processInstanceId
     * @return
     */
    public ProcessRun getByActInstanceIdHistory(Long actInstId)
    {
        return getUnique("getByActInstanceIdHistory", actInstId);
    }
    
    /**
     * 查看我参与审批流程列表
     * @param filter
     * @return
     */
    public List<ProcessRun> getMyAttend(QueryFilter filter)
    {
        return getBySqlKey("getMyAttend", filter);
    }
    
    public List<ProcessRun> getMyProcessRun(Long creatorId, String subject, Short status, PagingBean pb)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("creatorId", creatorId);
        params.put("subject", subject);
        params.put("status", status);
        return getBySqlKey("getMyProcessRun", params, pb);
    }
    
    /**
     * 工作台显示我参与审批 流程
     * @param assignee
     * @param status
     * @param pb
     * @return
     */
    public List<ProcessRun> getMyAttend(Long assignee, Short status, PagingBean pb)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("assignee", assignee);
        params.put("status", status);
        return getBySqlKey("getMyAttend", params, pb);
    }
    
    /**
     * 工作台显示我发起的流程
     * @param creatorId
     * @param pb
     * @return
     */
    public List<ProcessRun> myStart(Long creatorId, PagingBean pb)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("creatorId", creatorId);
        return getBySqlKey("getMyRequestList", params, pb);
    }
    
    /**
     * 根据Act流程定义ID，获取流程实例
     * @param actDefId
     * @param pb
     * @return
     */
    public List<ProcessRun> getByActDefId(String actDefId, PagingBean pb)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("actDefId", actDefId);
        return getBySqlKey("getAll", params, pb);
    }
    
    /**
     * 添加流程运行实例历史记录
     * @param entity
     */
    public void addHistory(ProcessRun entity)
    {
        String addStatement = getIbatisMapperNamespace() + ".addHistory";
        if (entity instanceof BaseModel)
        {
            BaseModel baseModel = ((BaseModel)entity);
            if (baseModel.getCreatetime() == null)
            {
                baseModel.setCreatetime(new Date());
            }
            if (baseModel.getUpdatetime() == null)
            {
                baseModel.setUpdatetime(new Date());
            }
            // 添加更新人ID以及修改人ID
            Long curUserId = UserContextUtil.getCurrentUserId();
            if (curUserId != null)
            {
                baseModel.setCreateBy(curUserId);
                baseModel.setUpdateBy(curUserId);
            }
        }
        getSqlSessionTemplate().insert(addStatement, entity);
    }
    
    /**
     * 更新对象。
     * @return 返回更新的记录数
     */
    public int updateHistory(ProcessRun entity)
    {
        String updStatement = getIbatisMapperNamespace() + ".updateHistory";
        
        if (entity instanceof BaseModel)
        {
            BaseModel baseModel = ((BaseModel)entity);
            baseModel.setUpdatetime(new Date());
            // 添加更新人ID以及修改人ID
            Long curUserId = UserContextUtil.getCurrentUserId();
            if (curUserId != null)
            {
                baseModel.setUpdateBy(curUserId);
            }
        }
        
        int affectCount = getSqlSessionTemplate().update(updStatement, entity);
        return affectCount;
    }
    
    /**
     * 删除流程历史
     * @param id 流程历史记录ID
     * @return
     */
    public int delByIdHistory(Long id)
    {
        String delStatement = getIbatisMapperNamespace() + ".delByIdHistory";
        int affectCount = getSqlSessionTemplate().delete(delStatement, id);
        return affectCount;
    }
    
    /**
     * 到历史表查询流程实例
     * @param primaryKey
     * @return
     */
    public ProcessRun getByIdHistory(Long primaryKey)
    {
        String getStatement = getIbatisMapperNamespace() + ".getByIdHistory";
        return (ProcessRun)getSqlSessionTemplate().selectOne(getStatement, primaryKey);
    }
    
    @Override
    public void add(ProcessRun entity)
    {
        super.add(entity);
    }
    
    /**
     * 根据act流程定义Id获取流程运行实例
     * @param actDefId
     */
    public List<ProcessRun> getbyActDefId(String actDefId)
    {
        return getBySqlKey("getbyActDefId", actDefId);
    }
    
    /**
     * 根据act流程定义Id删除流程运行实例
     * @param actDefId
     */
    public void delByActDefId(String actDefId)
    {
        delBySqlKey("delByActDefId", actDefId);
    }
    
    /**
     * 根据act流程定义Id删除流程历史
     * @param actDefId
     */
    public void delHistroryByActDefId(String actDefId)
    {
        delBySqlKey("delHistroryByActDefId", actDefId);
    }
    
    /*
     * @Override public int update(ProcessRun entity) { return super.update(entity); }
     * 
     * @Override public int update(String sqlKey, Object params) { return super.update(sqlKey, params); }
     * 
     * @Override public int delById(Long id) { return super.delById(id); }
     * 
     * @Override public int delBySqlKey(String sqlKey, Object params) { return super.delBySqlKey(sqlKey, params); }
     */
    /**
     * 我的草稿箱(filter过滤语句)
     * @author Yangbo 2016-7-22
     */
    public List<ProcessRun> getMyDraft(QueryFilter queryFilter)
    {
        return this.getBySqlKey("getMyDraft", queryFilter);
    }
    
    /**
     * 我的草稿箱
     * 根据用户id获取列表
     * @author Yangbo 2016-7-22
     */
    public List<ProcessRun> getMyDraft(Long userId, PagingBean pb)
    {
        Map params = new HashMap();
        params.put("userId", userId);
        return getBySqlKey("getMyDraft", params, pb);
    }
    
    /**
     * 我的请求办结的
     * @param filter
     * @return
     */
    public List<ProcessRun> getMyRequestCompletedList(QueryFilter filter)
    {
        return getBySqlKey("getMyRequestCompletedList", filter);
    }
    
    /**
     * 我的请求
     * @return
     * @author liguang 2012.11.30
     */
    public List<ProcessRun> getMyRequestList(QueryFilter filter)
    {
        return this.getBySqlKey("getMyRequestList", filter);
    }
    
    /**
     * 我的办结
     * @return
     * @author liguang 2012.11.30
     */
    public List<ProcessRun> getMyCompletedList(QueryFilter filter)
    {
        return this.getBySqlKey("getMyCompletedList", filter);
    }
    
    /**
     * 已办事宜
     * @return
     * @author liguang 2012.11.30
     */
    public List<ProcessRun> getAlreadyMattersList(QueryFilter filter)
    {
        return this.getBySqlKey("getAlreadyMattersList", filter);
    }
    
    /**
     * 办结事宜
     * @return
     * @author liguang 2012.11.30
     */
    public List<ProcessRun> getCompletedMattersList(QueryFilter filter)
    {
        return this.getBySqlKey("getCompletedMattersList", filter);
    }
    
    /**
     * 获取参考的流程实例。
     * @param defId
     * @param creatorId
     * @param instanceAmount
     * @return
     */
    public List<ProcessRun> getRefList(Long defId, Long creatorId, Integer instanceAmount)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("defId", defId);
        params.put("creatorId", creatorId);
        params.put("instanceAmount", instanceAmount);
        
        return this.getBySqlKey("getRefList", params);
    }
    
    /**
     * 获取监控的流程实例
     * @param filter
     * @return
     */
    public List<ProcessRun> getMonitor(QueryFilter filter)
    {
        return this.getBySqlKey("getMonitor", filter);
    }
    
    /**
     * 获取我审批过的某个流程实例的数据。
     * @param defId				流程定义
     * @param currentUserId		当前人
     * @param instanceAmount	实例数
     * @return
     */
    public List<ProcessRun> getRefListApprove(Long defId, Long currentUserId, Integer instanceAmount)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("defId", defId);
        params.put("curUser", currentUserId);
        params.put("instanceAmount", instanceAmount);
        
        return this.getBySqlKey("getRefListCopyTo", params);
        
    }
    
    public ProcessRun getByBusinessKey(Long formDefId, String businessKey)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("formDefId", formDefId);
        params.put("businessKey", businessKey);
        List<ProcessRun> processRuns = this.getBySqlKey("getByBusinessKey", params);
        if (processRuns != null && processRuns.size() > 0)
        {
            return processRuns.get(0);
        }
        else
        {
            return null;
        }
    }
    
    public List<ProcessRun> getByBusinessKeyAndFormDef(String businesskey, String formdefid)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("businesskey", businesskey);
        params.put("formdefid", formdefid);
        return this.getBySqlKey("getByBusinessKeyAndFormDef", params);
    }
    
    public List<ProcessRun> getByBusinessKeyAndDefId(String businesskey, Long defId)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("businessKey", businesskey);
        params.put("defId", defId);
        return this.getBySqlKey("getByBusinessKeyAndDefId", params);
    }
    
    /**
     * 工作台显示我的办结
     * @param creatorId
     * @param pb
     * @return
     */
    public List<ProcessRun> myCompleted(long curUserId, PagingBean pb)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("creatorId", curUserId);
        return this.getBySqlKey("getMyCompletedList", params, pb);
    }
    
    /**
     * 工作台显示已办事宜
     * @param assignee
     * @param pb
     * @return
     */
    public List<ProcessRun> Myalready(long assignee, PagingBean pb)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("assignee", assignee);
        return this.getBySqlKey("getAlreadyMattersList", params, pb);
    }
    
    /**
     * 工作台显示办结事宜
     * @param assignee
     * @param pb
     * @return
     */
    public List<ProcessRun> completedMatters(Long assignee, PagingBean pb)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("assignee", assignee);
        return this.getBySqlKey("getCompletedMattersList", params, pb);
    }
    
    public List<ProcessRun> getTestRunsByActDefId(String actDefId)
    {
        return this.getBySqlKey("getTestRunsByActDefId", actDefId);
    }
    
    public List<ProcessRun> getProcessRunsByParentId(Long pId)
    {
        return this.getBySqlKey("getProcessRunsByParentId", pId);
        
    }
    
    /**
     * 根据流程实例Id删除非主线程的流程。
     * @param procInstId
     */
    public int delSubProByProcInstId(String procInstId)
    {
        return delBySqlKey("delSubProByProcInstId", procInstId);
    }
    
    /**
     * 根据流程实例Id删除非主线程的流程历史记录。
     * @param procInstId
     */
    public int delSubHistoryByProcInstId(String procInstId)
    {
        return delBySqlKey("delSubHistoryByProcInstId", procInstId);
    }
    
    /**
     * 根据act流程实例Id删除扩展的流程历史实例记录。
     * @param procInstId
     */
    public int delHistoryByActinstid(String actInstId)
    {
        return delBySqlKey("delHistoryByActinstid", actInstId);
    }
    
    /**
     * 根据act流程实例Id删除扩展的流程实例记录。
     * @param procInstId
     */
    public int delProByActinstid(String actInstId)
    {
        return delBySqlKey("delProByActinstid", actInstId);
    }
    
    public String getParentProcessRunActDefId(Long runId)
    {
        ProcessRun processRun = getById(runId);
        return getParentProcessRunActDefId(processRun);
    }
    
    public String getParentProcessRunActDefId(ProcessRun processRun)
    {
        if ((BeanUtils.isNotEmpty(processRun)) && (processRun.getParentId().longValue() > 0L))
        {
            ProcessRun parentProcessRun = getById(processRun.getParentId());
            if (BeanUtils.isNotEmpty(parentProcessRun))
            {
                return parentProcessRun.getActDefId();
            }
        }
        return "";
    }
    
    public List<ProcessRun> getMyCompletedAndCptoList(QueryFilter filter)
    {
        return getBySqlKey("getMyCompletedAndCptoList", filter);
    }
    
    public List<ProcessRun> getMyFlowsAndCptoList(QueryFilter filter)
    {
        return getBySqlKey("getMyFlowsAndCptoList", filter);
    }
    
    public List<ProcessRun> getByFlowKey(String flowKey, String businesskey)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("businessKey", businesskey);
        params.put("flowKey", flowKey);
        return this.getBySqlKey("getByFlowKey", params);
    }
    
    public ProcessRun getByBusinessKey(String businesskey)
    {
        String getStatement = getIbatisMapperNamespace() + ".getRunByBusinessKey";
        List<ProcessRun> processRuns = getSqlSessionTemplate().selectList(getStatement, businesskey);
        if (processRuns == null || processRuns.size() < 1)
        {
            getStatement = getIbatisMapperNamespace() + ".getByBusinessKeyHistory";
            processRuns = getSqlSessionTemplate().selectList(getStatement, businesskey);
        }
        if (processRuns == null || processRuns.size() < 1)
        {
            return null;
        }
        else if (processRuns.size() == 1)
        {
            return processRuns.get(0);
        }
        else
        {
            return null;
        }
        
    }
    
    /** 
    * @Title: deleteFormData 
    * @Description: TODO(删除草稿数据) 
    * @param @param formKey 流程表单key
    * @param @param pk     业务主键
    * @return void    返回类型 
    * @throws 
    */
    public int deleteFormData(Long formKey, String pk)
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("formKey", formKey);
        params.put("businessKey", pk);
        return delBySqlKey("deleteFormData", params);
        
    }
}