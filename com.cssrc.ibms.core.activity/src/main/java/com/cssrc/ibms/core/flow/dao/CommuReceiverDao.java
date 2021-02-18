package com.cssrc.ibms.core.flow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.CommuReceiver;
 
@Repository
public class CommuReceiverDao extends BaseDao<CommuReceiver>
{
	@Override
	public Class<?> getEntityClass()
	{
		return CommuReceiver.class;
	}
	
	/**
	 * 根据意见ID获取接收人。
	 * @param opinionId
	 * @return
	 */
	public List<CommuReceiver> getByOpinionId(Long opinionId){
		return this.getBySqlKey("getByOpinionId", opinionId);
	}
	
	/**
	 * 根据任务ID获取接收对象。
	 * @param opinionId
	 * @return
	 */
	public CommuReceiver getByTaskId(Long taskId){
		CommuReceiver commuReceiver=this.getUnique("getByTaskId", taskId);
		return commuReceiver;
	}
	
	/**
	 * 根据意见ID和接收人获取对象。
	 * @param opinionId
	 * @param recevierId
	 * @return
	 */
	public CommuReceiver getByTaskReceiver(Long taskId,Long recevierId){
		Map<String,Long> params=new HashMap<String, Long>(); 
		params.put("taskId", taskId);
		params.put("recevierid", recevierId);
		CommuReceiver commuReceiver=this.getUnique("getByTaskReceiver", params);
		return commuReceiver;
	}

	public int delByTaskId(Long taskId) {
		return this.delBySqlKey("delByTaskId", taskId);
	}

}