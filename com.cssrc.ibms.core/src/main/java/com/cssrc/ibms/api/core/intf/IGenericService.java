package com.cssrc.ibms.api.core.intf;

import java.io.Serializable;
import java.util.List;

import org.springframework.context.MessageSource;

import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;

public interface IGenericService<E, PK extends Serializable> {

	public abstract void setMessages(MessageSource messageSource);

	/**
	 * 根据键值，参数数组，local取得键值。
	 * @param msgKey
	 * @param args
	 * @param locale
	 * @return
	 */
	public abstract String getText(String msgKey, Object[] args);

	public abstract String getText(String msgKey);

	/**
	 * 添加对象
	 * @param entity
	 */
	public abstract void add(E entity);

	/**
	 * 根据主键删除对象
	 * @param id
	 */
	public abstract void delById(PK id);

	/**
	 * 根据主键批量删除对象
	 * @param ids
	 */
	public abstract void delByIds(PK[] ids);

	/**
	 * 修改对象
	 * @param entity
	 */
	public abstract void update(E entity);

	/**
	 * 根据主键Id获取对象
	 * @param id
	 * @return
	 */
	public abstract E getById(PK id);

	/**
	 * 取得分页。
	 * @param statatementName
	 * @param pb
	 * @return
	 */
	public abstract List<E> getList(String statatementName, PagingBean pb);

	/**
	 * 返回所有记录
	 * @return
	 */
	public abstract List<E> getAll();

	/**
	 * 按过滤器查询记录列表
	 * @param queryFilter
	 * @return
	 */
	public abstract List<E> getAll(QueryFilter queryFilter);

}