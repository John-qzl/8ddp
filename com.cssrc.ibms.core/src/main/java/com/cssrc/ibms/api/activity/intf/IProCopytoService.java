package com.cssrc.ibms.api.activity.intf;

import java.util.List;

import com.cssrc.ibms.api.activity.model.IProCopyto;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
public interface IProCopytoService {

	/**
	 * 标记为已读
	 * @param ids
	 */
	public abstract void updateReadStatus(String ids);

	/**
	 * 获取我的抄送列表
	 * @param queryFilter
	 * @return
	 */
	public abstract List<?extends IProCopyto> getMyList(QueryFilter queryFilter);

	/**
	 * 标记为已读
	 * @param ids
	 */
	public abstract void markCopyStatus(String id);

	/**
	 * 根据运行Id获取用户数据。
	 * @param queryFilter
	 * @return
	 */
	public abstract List<?extends IProCopyto> getUserInfoByRunId(QueryFilter queryFilter);

	/**
	 * 根据流程runId删除抄送转发事宜
	 * @param runId
	 */
	public abstract void delByRunId(Long runId);
	/**
	 * 通过抄送人id获得数量
	 * @author Yangbo 2016-7-22
	 * @param userId
	 * @return
	 */
	public abstract Integer getCountByUser(Long userId);
	/**
	 * 抄送人未阅读的数量
	 * @param userId
	 * @return
	 */
	public abstract Integer getCountNotReadByUser(Long userId);
	
	/**
	 * 根据提供page获取抄送数据
	 * @param curUserId
	 * @param pg
	 * @return
	 */
	public abstract List<?extends IProCopyto> getMyProCopytoList(Long curUserId, PagingBean pg);

}