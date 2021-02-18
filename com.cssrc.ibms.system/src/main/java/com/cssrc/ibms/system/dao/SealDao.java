package com.cssrc.ibms.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.Seal;

/**
 * 对象功能:电子印章 DAO类.
 *
 * <p>detailed comment</p>
 * @author [创建人] WeiLei <br/> 
 * 		   [创建时间] 2016-8-22 上午11:21:43 <br/> 
 * 		   [修改人] WeiLei <br/>
 * 		   [修改时间] 2016-8-22 上午11:21:43
 * @see
 */
@Repository
public class SealDao extends BaseDao<Seal> {
	
	@SuppressWarnings("unchecked")
	@Override
	public Class getEntityClass() {
		return Seal.class;
	}

	/**
	 * 根据用户Id、印章名称、用户所属角色、用户所属组织，获取印章信息.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-8-22 上午11:22:48 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-22 上午11:22:48
	 * @param params
	 * @return
	 * @see
	 */
	public List<Seal> getSealByUserId(Object params) {
		return getBySqlKey("getSealByUserId", params);
	}
	
}
