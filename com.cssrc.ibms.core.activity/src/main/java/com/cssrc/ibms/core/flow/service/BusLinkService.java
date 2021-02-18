package com.cssrc.ibms.core.flow.service;

import java.util.Date;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cssrc.ibms.api.activity.intf.IBusLinkService;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.BusLinkDao;
import com.cssrc.ibms.core.flow.model.BusLink;
import com.cssrc.ibms.core.util.bean.BeanUtils;


/**
 *<pre>
 * 对象功能:业务数据关联表 Service类 
 *</pre>
 */
@Service
public class BusLinkService extends BaseService<BusLink> implements IBusLinkService
{
	@Resource
	private BusLinkDao dao;
	
	@Override
	protected IEntityDao<BusLink, Long> getEntityDao() 
	{
		return dao;
	}
	
	/**
	 * 添加业务数据。
	 * @param pk
	 * @param bpmFormTable
	 */
	public void add(String pk, IFormTable bpmFormTable){
		this.add(pk,null, bpmFormTable);
	}
	
	/**
	 * 添加业务数据。
	 * @param pk
	 * @param defId
	 * @param flowRunId
	 * @param bpmFormTable
	 */
	public void add(String pk,IProcessRun processRun, IFormTable bpmFormTable){
		ISysUser curUser=(ISysUser)UserContextUtil.getCurrentUser();
		ISysOrg curOrg= null; //ContextUtil.getCurrentOrg();
		Long id=UniqueIdUtil.genId();
		BusLink busLink=new BusLink();
		busLink.setBusId(id);
		if(bpmFormTable.isExtTable()){
			if(IFormTable.PKTYPE_NUMBER.equals(bpmFormTable.getKeyDataType())){
				busLink.setBusPk(Long.parseLong(pk));
			}
			else{
				busLink.setBusPkstr(pk);
			}
		}
		else{
			busLink.setBusPk(Long.parseLong(pk));
		}
		
		busLink.setBusCreatorId(curUser==null?null:curUser.getUserId());
		busLink.setBusCreator(curUser==null?null:curUser.getFullname());
		busLink.setBusCreatetime(new Date());
		if(BeanUtils.isNotEmpty(curOrg)){
			busLink.setBusOrgName(curOrg.getOrgName());
			busLink.setBusOrgId(curOrg.getOrgId());
		}
		busLink.setBusUpdid(curUser==null?null:curUser.getUserId());
		busLink.setBusUpd(curUser==null?null:curUser.getFullname());
		busLink.setBusUpdtime(new Date());
		
		if(processRun!=null){
			busLink.setBusDefId(processRun.getDefId());
			busLink.setBusFlowRunid(processRun.getRunId());
		}
		dao.add(busLink);
	}
	
	/**
	 * 更新业务关联数据。
	 * @param pk
	 * @param bpmformtable
	 */
	public void updBusLink(String pk,IFormTable bpmformtable){
		ISysUser curUser=(ISysUser)UserContextUtil.getCurrentUser();
		BusLink link=null;
		if(bpmformtable.isExtTable()){
			if (IFormTable.PKTYPE_NUMBER.equals(bpmformtable.getKeyDataType())){
				link=dao.getByPk(new Long(pk));
			}
			else{
				link=dao.getByPkStr(pk);
			}
		}
		else{
			link=dao.getByPk(new Long(pk));
		}
		if(BeanUtils.isNotEmpty(link)){
			link.setBusUpdid(curUser==null?null:curUser.getUserId());
			link.setBusUpd(curUser==null?null:curUser.getFullname());
			link.setBusUpdtime(new Date());
			dao.update(link);
		}
		
	}
	
	/**
	 * 删除关联数据记录。
	 * @param pk
	 * @param bpmformtable
	 */
	public void delBusLink(String pk,IFormTable bpmformtable){
		if(bpmformtable.isExtTable()){
			if (IFormTable.PKTYPE_NUMBER.equals(bpmformtable.getKeyDataType())){
				dao.delByPk(new Long(pk));
			}
			else{
				dao.delByPkStr(pk);
			}
		}
		else{
			dao.delByPk(new Long(pk));
		}
		
	}


}
