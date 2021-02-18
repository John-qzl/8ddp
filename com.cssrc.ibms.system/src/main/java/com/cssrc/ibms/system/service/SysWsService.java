package com.cssrc.ibms.system.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.system.dao.SysWsDao;
import com.cssrc.ibms.system.dao.SysWsParamsDao;
import com.cssrc.ibms.system.model.SysWs;
import com.cssrc.ibms.system.model.SysWsParams;

/**
 *<pre>
 * 对象功能:通用webservice调用设置 Service类 
 * 开发人员:zhulongchao 
 *</pre>
 */
@Service
public class SysWsService extends BaseService<SysWs>
{
	@Resource
	private SysWsDao dao;
	
	@Resource
	private SysWsParamsDao sysWsParamsDao;
	 
	
	
	public SysWsService()
	{
	}
	
	@Override
	protected IEntityDao<SysWs, Long> getEntityDao() 
	{
		return dao;
	}
	
	private void delByPk(Long id){
		sysWsParamsDao.delByMainId(id);
	}
	
	public void delAll(Long[] lAryId) throws Exception {
		for(Long id:lAryId){				 
			delByPk(id);
			dao.delById(id);	
		}	
	}
	
	public void addAll(SysWs ws) throws Exception{
		add(ws);
		addSubList(ws);
	}
	
	public void updateAll(SysWs ws) throws Exception{
		update(ws);
		delByPk(ws.getId());
		addSubList(ws);
	}
	
	public void addSubList(SysWs ws) throws Exception{
		List<SysWsParams> wsParamsList=ws.getSysWsParamsList();
		if(BeanUtils.isNotEmpty(wsParamsList)){
			for(SysWsParams wsParams:wsParamsList){
				wsParams.setSetid(ws.getId());
				wsParams.setId(UniqueIdUtil.genId());
				sysWsParamsDao.add(wsParams);
			}
		}
	}
	
	public SysWs getByAlias(String alias){
		return dao.getByAlias(alias);
	}
	
	public List<SysWsParams> getWsParamsList(Long id) {
		return sysWsParamsDao.getByMainId(id);
	}
}
