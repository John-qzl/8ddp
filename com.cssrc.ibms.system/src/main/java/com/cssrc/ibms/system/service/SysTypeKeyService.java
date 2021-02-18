package com.cssrc.ibms.system.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;





import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.system.dao.SysTypeKeyDao;
 
import com.cssrc.ibms.system.model.SysTypeKey;

@Service
public class SysTypeKeyService extends BaseService<SysTypeKey>{

	@Resource
	private SysTypeKeyDao dao;
	
	@Override
	protected IEntityDao<SysTypeKey, Long> getEntityDao() { 
		return dao;
	}
	/**
	 * 由typekey或catkey得出标识key列表
	 * 等同getSysTypeKeyByCat 
	 * @param catKey
	 * @return
	 */
	public SysTypeKey getByKey(String catKey){
		return dao.getByKey(catKey);
	}
	
	public boolean isExistKey(String typeKey)
	{
		typeKey = typeKey.toLowerCase();
		return this.dao.isExistKey(typeKey);
	}

	public boolean isKeyExistForUpdate(String typeKey, Long typeKeyId)
	{
		typeKey = typeKey.toLowerCase();
		return this.dao.isKeyExistForUpdate(typeKey, typeKeyId);
	}

	public void saveSequence(Long[] aryTypeId)
	{
		for (int i = 0; i < aryTypeId.length; i++)
			this.dao.updateSequence(aryTypeId[i], i);
	}

}
