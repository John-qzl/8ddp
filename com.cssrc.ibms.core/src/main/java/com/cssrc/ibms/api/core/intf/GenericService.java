package com.cssrc.ibms.api.core.intf;


import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

import com.cssrc.ibms.api.core.util.ContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.bean.BeanUtils;

/**
 * 服务基类。
 * 所有的业务实现类均需要从此类继承
 * @author zhulongchao
 *
 */
public abstract class GenericService <E, PK extends Serializable> implements IGenericService<E, PK>{
	protected Logger logger = LoggerFactory.getLogger(GenericService.class);
    private MessageSourceAccessor messages;
	
	@Autowired
	public void setMessages(MessageSource messageSource) {
		messages = new MessageSourceAccessor(messageSource);
	}
	
	/**
	 * 根据键值，参数数组，local取得键值。
	 * @param msgKey
	 * @param args
	 * @param locale
	 * @return
	 */
	public String getText(String msgKey, Object[] args){
		return messages.getMessage(msgKey, args,ContextUtil.getLocale());
	}
	
	public String getText(String msgKey){
		return messages.getMessage(msgKey,ContextUtil.getLocale());
	}
	
	/**
	 * 需要被子类覆盖
	 * @return
	 */
	protected abstract IEntityDao<E,PK> getEntityDao();

	/**
	 * 添加对象
	 * @param entity
	 */
	public void add(E entity)
	{
		getEntityDao().add(entity);
	}

	/**
	 * 根据主键删除对象
	 * @param id
	 */
	public void delById(PK id)
	{
		getEntityDao().delById(id);
	}

	/**
	 * 根据主键批量删除对象
	 * @param ids
	 */
	public void delByIds(PK[] ids){
		if(BeanUtils.isEmpty(ids)) return;
		for (PK p : ids){
			delById(p);
		}
	}

	/**
	 * 修改对象
	 * @param entity
	 */
	public void update(E entity)
	{
		getEntityDao().update(entity);
	}

	/**
	 * 根据主键Id获取对象
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public E getById(PK id)
	{
		return (E) getEntityDao().getById(id);
	}

	/**
	 * 取得分页。
	 * @param statatementName
	 * @param pb
	 * @return
	 */
	public List<E> getList(String statatementName,PagingBean pb)
	{
		List<E>  list = getEntityDao().getList(statatementName, pb);
		return list;
	}
	/**
	 * 返回所有记录
	 * @return
	 */
	public List<E> getAll()
	{
		return getEntityDao().getAll();
	}
	
	/**
	 * 按过滤器查询记录列表
	 * @param queryFilter
	 * @return
	 */
	public List<E> getAll(QueryFilter queryFilter){
		return getEntityDao().getAll(queryFilter);
	}
	
    /** 
    * @Title: batchSql 
    * @Description: TODO(批处理脚本) 
    * @param @param sql
    * @param @return    设定文件 
    * @return int[]    返回类型 
    * @throws 
    */
    public int[] batchSql(String[] sql)
    {
        return getEntityDao().batchSql(sql);
    }
	
	
}
