package com.cssrc.ibms.core.flow.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.dao.ReferDefinitionDao;
import com.cssrc.ibms.core.flow.model.ReferDefinition;

/**
 * <pre>
 * 对象功能:ReferDefinition Service类 
 * 开发人员:zhulongchao 
 * </pre>
 */

@Service
public class ReferDefinitionService extends BaseService<ReferDefinition> {
	@Resource
	private ReferDefinitionDao dao;

	public ReferDefinitionService() {
	}

	@Override
	protected IEntityDao<ReferDefinition, Long> getEntityDao() {
		return dao;
	}

	public java.util.List<ReferDefinition> getByDefId(Long defId){
		return dao.getBySqlKey("getByDefId", defId);
	}
	
	public void saveReferDef(ReferDefinition refers){
		dao.add(refers);
	}
	
	public int delReferDef(Long refers){
		return dao.delById(refers);
	}
	
	public int updateReferDef(ReferDefinition refers){
		return dao.update(refers);
	}
	
	public java.util.List<ReferDefinition> getReferList(QueryFilter queryFilter){
		return dao.getAll(queryFilter);
	}
	
	public ReferDefinition getById(Long id){
		return  dao.getById(id);
	}
	public List<String> getDefKeysByDefId(Long defId) {
	  List list = new ArrayList();
	  List<ReferDefinition> refers = getByDefId(defId);
	  for (ReferDefinition refer : refers) {
	    list.add(refer.getReferDefKey());
	  }
	   return list;
	 }
}