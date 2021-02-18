package com.cssrc.ibms.core.msg.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.msg.model.MessageSend;

@Repository
public class MessageSendDao  extends BaseDao<MessageSend>{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass() {
		return MessageSend.class;
	}
	
	/**
	 * 查询某个用户的接收消息
	 * @param queryFilter
	 * @return
	 */
	public List<MessageSend> getReceiverByUser(QueryFilter queryFilter)
	{
		return this.getBySqlKey("getReceiverByUser", queryFilter);
	}
	
	/**
	 * 获取单条个人未读信息，按发送时间排序
	 * @param userId
	 * @return
	 */
	public List<MessageSend> getNotReadMsg(Long receiverId)
	{ 
		return getBySqlKey("getNotReadMsgByUserId",receiverId);
	}
	
	/**
	 * @author YangBo 2016-7-22
	 * 获取未读信息
	 * @param userId
	 * @param pb
	 * @return
	 */
	public List<MessageSend> getNotReadMsgByUserId(long userId, PagingBean pb) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("receiverId", Long.valueOf(userId));
		return getBySqlKey("getNotReadMsgByUserId", params, pb);
	}
	
	/**
	 * 获取接收的消息数量
	 * @author YangBo
	 *  2016-7-22
	 * @param userId
	 * @return
	 */
	public Integer getCountReceiverByUser(Long userId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("receiverId", userId);
		return (Integer) getOne("getCountReceiverByUser", params);
	}
	
	/**
	 * 获取未读消息数量
	 * @param userId
	 * @return
	 */
	public Integer getCountNotReadMsg(Long userId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("receiverId", userId);
		return (Integer) getOne("getCountNotReadMsg", params);
	}
	
}
