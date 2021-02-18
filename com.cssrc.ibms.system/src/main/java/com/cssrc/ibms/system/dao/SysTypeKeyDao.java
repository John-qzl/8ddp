package com.cssrc.ibms.system.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.system.model.SysTypeKey;
/**
 * 分类标识Key管理
 * <p>Title:SysTypeKeyDao</p>
 * @author Yangbo 
 * @date 2016-8-29下午04:26:37
 */
@Repository
public class SysTypeKeyDao extends BaseDao<SysTypeKey>{
	@Override
	public Class getEntityClass() {
		return SysTypeKey.class;
	}
	/**
	 * 通过可以catkey或typekey获得标识列表
	 * 等同原来的getSysTypeKeyByCat
	 * @param key
	 * @return
	 */
	public SysTypeKey getByKey(String key) {
		key = key.toLowerCase();
		return (SysTypeKey) getUnique("getByKey", key);
	}
	/**
	 * 是否存在该type
	 * @param typeKey
	 * @return
	 */
	public boolean isExistKey(String typeKey) {
		Integer sn = (Integer) getOne("isExistKey", typeKey);
		return sn.intValue() > 0;
	}
	/**
	 * 是否存在同名key
	 * @param typeKey
	 * @param typeId
	 * @return
	 */
	public boolean isKeyExistForUpdate(String typeKey, Long typeKeyId) {
		Map params = new HashMap();
		params.put("typeKey", typeKey);
		params.put("typeId", typeKeyId);
		Integer sn = (Integer) getOne("isKeyExistForUpdate", params);
		return sn.intValue() > 0;
	}

	public void updateSequence(Long typekeyId, int sn) {
		SysTypeKey sysTypeKey = new SysTypeKey();
		sysTypeKey.setTypeKeyId(typekeyId);
		sysTypeKey.setSn(Integer.valueOf(sn));
		sysTypeKey.setTypekey_updateTime(new Date());
		sysTypeKey.setTypekey_updateId(UserContextUtil.getCurrentUserId());
		update("updateSequence", sysTypeKey);
	}

}
