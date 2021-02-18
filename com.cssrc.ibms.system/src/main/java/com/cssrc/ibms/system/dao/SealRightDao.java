package com.cssrc.ibms.system.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.SealRight;

/**
 * 对象功能:印章授权 DAO类.
 *
 * <p>detailed comment</p>
 * @author [创建人] WeiLei <br/> 
 * 		   [创建时间] 2016-8-22 上午11:21:43 <br/> 
 * 		   [修改人] WeiLei <br/>
 * 		   [修改时间] 2016-8-22 上午11:21:43
 * @see
 */
@Repository
public class SealRightDao extends BaseDao<SealRight> {
	
	@SuppressWarnings("unchecked")
	@Override
	public Class getEntityClass() {
		return SealRight.class;
	}

	/**
	 * 根据印章ID和控件类型，获取印章授权信息.
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
	public List<SealRight> getRightBySealId(Long sealId, Short controlType) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sealId", sealId);
		map.put("controlType", controlType);
		return getBySqlKey("getBySealId", map);
	}

	/**
	 * 根据印章ID和控件类型，删除印章授权信息.
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
	public int delBySealId(Long sealId, Short controlType) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sealId", sealId);
		map.put("controlType", controlType);
		return delBySqlKey("delBySealId", map);
	}

}
