package com.cssrc.ibms.core.flow.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.ReminderState;

@Repository
public class ReminderStateDao extends BaseDao<ReminderState>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return ReminderState.class;
	}
	
	/**
	 * 取得任务执行的次数。
	 * @param taskId
	 * @return
	 */
	public Integer getAmountByUserTaskId(long taskId,long userId,int remindType){
		Map params=new HashMap();
		params.put("taskId", taskId);
		params.put("userId", userId);
		params.put("remindType", remindType);
		Integer rtn=(Integer)this.getOne("getAmountByUserTaskId", params);
		return rtn;
	}
	
	
	/**
     * 取得任务执行最后一次时间。
     * @param taskId
     * @return
     */
    public Date getLastByUserTaskId(long taskId,long userId,int remindType){
        Map params=new HashMap();
        params.put("taskId", taskId);
        params.put("userId", userId);
        params.put("remindType", remindType);
        try{
            Object rtn=this.getOne("getLastByUserTaskId", params);
            if(rtn!=null){
                return (Date)rtn;
            }
            return null;
        }catch(Exception e){
            return null;
        }
    }
    
	/**
	 * 根据任务id查询催办执行次数。
	 * 这个方法用在查询到期的催办。
	 * @param taskId
	 * @param remindType
	 * @return
	 */
	public Integer getAmountByTaskId(long taskId,int remindType){
		Map params=new HashMap();
		params.put("taskId", taskId);
		params.put("remindType", remindType);
		Integer rtn=(Integer)this.getOne("getAmountByTaskId", params);
		return rtn;
	}
	
	
	/**
	 * 清除已过期的任务提醒状态数据。
	 */
	public void delExpiredTaskReminderState(){
		this.delBySqlKey("delExpiredTaskReminderState", null);
	}
	
	/**
	 * 根据act流程定义Id删除任务提醒状态数据。
	 * @param actDefId
	 */
	public void delByActDefId(String actDefId){
		delBySqlKey("delByActDefId", actDefId);
	}
}