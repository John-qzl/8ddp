package com.cssrc.ibms.dbom.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.dbom.model.DBom;

/**
 * 对象功能:DBom分类管理 DAO类.
 *
 * <p>detailed comment</p>
 * @author [创建人] WeiLei <br/> 
 * 		   [创建时间] 2016-7-13 上午08:15:01 <br/> 
 * 		   [修改人] WeiLei <br/>
 * 		   [修改时间] 2016-7-13 上午08:15:01
 * @see
 */
@Repository
public class DBomDao extends BaseDao<DBom>{

	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass() {
		return DBom.class;
	}
	
	/**
	 * 根据DBom分类代号获取DBom分类数据.
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-7-13 上午11:40:15 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-7-13 上午11:40:15
	 * @see
	 */
	public List<DBom> getByCode(String code){		
		return this.getBySqlKey("getByCode", code);
	}
	
	
	/**
	 * 根据code 获取唯一dbom编号
	 * @param code
	 * @return
	 */
	public DBom getUniqueByCode(String code){		
		return this.getUnique("getUniqueByCode", code);
	}
}
