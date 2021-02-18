package com.cssrc.ibms.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.system.dao.SealRightDao;
import com.cssrc.ibms.system.model.SealRight;

/**
 * 对象功能:印章授权 Service类.
 *
 * <p>detailed comment</p>
 * @author [创建人] WeiLei <br/> 
 * 		   [创建时间] 2016-8-22 下午12:32:19 <br/> 
 * 		   [修改人] WeiLei <br/>
 * 		   [修改时间] 2016-8-22 下午12:32:19
 * @see
 */
@Service
public class SealRightService extends BaseService<SealRight> {

	@Resource
	private SealRightDao sealRightDao;

	protected IEntityDao<SealRight, Long> getEntityDao() {
		return this.sealRightDao;
	}
	
	/**
	 * 保存印章授权信息.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-8-22 下午12:40:32 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-22 下午12:40:32
	 * @param sealId
	 * @param rightType
	 * @param rightIds
	 * @param rightNames
	 * @param userId
	 * @param controlType
	 * @see
	 */
	public void saveSealRight(Long sealId, String rightType, String rightIds,
			String rightNames, Long userId, Short controlType) {
		
		//删除印章授权信息
		delBySealId(sealId, controlType);
		//保存印章授权信息
		Date date = new Date();
		if ("all".equals(rightType)) {//权限类型为：所有
			SealRight sr = new SealRight();
			sr.setId(UniqueIdUtil.genId());
			sr.setSealId(sealId);
			sr.setRightType(rightType);
			sr.setRightId(Long.valueOf(0L));
			sr.setRightName("所有");
			sr.setCreateUser(userId);
			sr.setCreateTime(date);
			sr.setControlType(controlType);
			this.sealRightDao.add(sr);
		} else if ("not".equals(rightType)) {//权限类型为：没有
			this.logger.info("没有分配权限");
		} else if (StringUtil.isNotEmpty(rightIds)) {//权限类型为：用户、角色、组织
			String[] ids = rightIds.split(",");
			String[] names = rightNames.split(",");
			for (int i = 0; i < ids.length; i++) {
				SealRight sr = new SealRight();
				sr.setId(UniqueIdUtil.genId());
				sr.setSealId(sealId);
				sr.setRightType(rightType);
				sr.setRightId(Long.valueOf(Long.parseLong(ids[i])));
				sr.setRightName(names[i]);
				sr.setCreateUser(userId);
				sr.setCreateTime(date);
				sr.setControlType(controlType);
				this.sealRightDao.add(sr);
			}
		}
	}

	/**
	 * 根据印章Id和控件类型，获取印章授权信息.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-8-22 下午12:52:31 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-22 下午12:52:31
	 * @param sealId
	 * @param controlType
	 * @return
	 * @see
	 */
	public Map<String, Object> getSealRight(Long sealId, Short controlType) {
		
		//获取印章授权信息
		List<SealRight> rightList = getRightBySealId(sealId, controlType);
		String rightIds = "";
		String rightNames = "";
		String rightType = "not";
		if (BeanUtils.isNotEmpty(rightList)) {
			for (SealRight sealRight : rightList) {
				rightIds = rightIds + sealRight.getRightId() + ",";
				rightNames = rightNames + sealRight.getRightName() + ",";
			}
			rightIds = rightIds.substring(0, rightIds.length() - 1);
			rightNames = rightNames.substring(0, rightNames.length() - 1);
			rightType = ((SealRight) rightList.get(0)).getRightType();
		}
		//返回值
		Map<String, Object> rightMap = new HashMap<String, Object>();
		rightMap.put("rightType", rightType);
		rightMap.put("rightIds", rightIds);
		rightMap.put("rightNames", rightNames);
		return rightMap;
	}
	
	/**
	 * 根据印章Id，获取印章授权信息.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-8-22 下午12:47:26 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-22 下午12:47:26
	 * @param sealId
	 * @param controlType
	 * @return
	 * @see
	 */
	public List<SealRight> getRightBySealId(Long sealId, Short controlType) {
		return this.sealRightDao.getRightBySealId(sealId, controlType);
	}
	
	/**
	 * 根据印章授权ids，删除印章授权信息.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-8-22 下午12:27:40 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-22 下午12:27:40
	 * @param userId
	 * @param sealName
	 * @return
	 * @see
	 */
	public void delByIds(Long[] ids) {
		
		if (BeanUtils.isEmpty(ids))
			return;
		for (Long p : ids) {
			//删除印章授权信息
			delById(p);
		}
	}
	
	/**
	 * 根据印章Id和控件类型，删除印章授权信息.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-8-22 下午12:47:26 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-22 下午12:47:26
	 * @param sealId
	 * @param controlType
	 * @return
	 * @see
	 */
	public int delBySealId(Long sealId, Short controlType) {
		return this.sealRightDao.delBySealId(sealId, controlType);
	}

	/**
	 * 获取印章授权分配类型，包括：没有、用户、角色、组织、所有.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-8-22 下午12:48:29 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-22 下午12:48:29
	 * @return
	 * @see
	 */
	public List<Map<String, Object>> getRightType() {
		
		List<Map<String, Object>> rtList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		//没有
		map.put("id", "not");
		map.put("name", "没有");
		rtList.add(map);
		//用户
		map = new HashMap<String, Object>();
		map.put("id", "user");
		map.put("name", "用户");
		rtList.add(map);
		//角色
		map = new HashMap<String, Object>();
		map.put("id", "role");
		map.put("name", "角色");
		rtList.add(map);
		//组织
		map = new HashMap<String, Object>();
		map.put("id", "org");
		map.put("name", "组织");
		rtList.add(map);
		//所有
		map = new HashMap<String, Object>();
		map.put("id", "all");
		map.put("name", "所有");
		rtList.add(map);
		
		return rtList;
	}
	
}
