package com.cssrc.ibms.core.msg.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.model.IbatisSql;
import com.cssrc.ibms.core.msg.model.MessageReceiver;
import com.cssrc.ibms.core.util.bean.BeanUtils;

@Repository
public class MessageReceiverDao  extends BaseDao<MessageReceiver>{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass() {
		return MessageReceiver.class;
	}
	
	/**
	 * 查询某个用户的接收消息
	 * 
	 * @param queryFilter
	 * @return
	 */
	public List<MessageReceiver> getMessageReceiverList(Long messageId)
	{
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("messageId", messageId);
		return this.getBySqlKey("getAll", param);
	}

	/**
	 * 查询某个用户的接收消息
	 * 
	 * @param queryFilter
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> getReadReplyByUser(Long messageId)
	{
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("messageId", messageId);
		String statement = getIbatisMapperNamespace() + "." + "getReadReplyByUser";
		List<Map> list = getSqlSessionTemplate().selectList(statement, param);
		return list;
	}

	/**
	 * 查询某个组织的接收消息
	 * 
	 * @param queryFilter
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map> getReadReplyByPath(Long messageId, String path)
	{
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("messageId", messageId);
		param.put("path", path+"%");
		String statement = getIbatisMapperNamespace() + "." + "getReadReplyByPath";
		List<Map> list = getSqlSessionTemplate().selectList(statement, param);
		return list;
	}
	
	/**
	 * 根据消息ID删除接收者信息
	 * @param messageId
	 */
	public int delReceiverByMsgId(Long messageId){
		return delBySqlKey("delByMessageId", messageId);
	}
	
	
	/**
	 * 根据消息ID删除接收者信息
	 * @param messageId
	 */
	public int delReceiverByMsgIds(Long[] messageIds){
		int delCount = 0;
		if(BeanUtils.isEmpty(messageIds))
			return 0;
		for(Long messageId:messageIds){
			int i=delReceiverByMsgId(messageId);
			delCount+=i;
		}
		return delCount;
	}
	
	
	/**
	 * 根据消息ID，计算消息发送成功后，接收者收到且末删除该消息的接收者数。
	 * @param id 内部消息ID
	 * @return 接收者数
	 */
	public int getCountByMsgId(Long id){
		IbatisSql ibatisSql = getIbatisSql("getCountByMessageId", id);
		int totalCount=jdbcTemplate.queryForInt(ibatisSql.getSql(),id);
		return totalCount;
	}
}
