package com.cssrc.ibms.core.log.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.system.intf.ISysLogSwitchService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.log.dao.SysLogSwitchDao;
import com.cssrc.ibms.core.log.intf.IExtendOwnerModelClazz;
import com.cssrc.ibms.core.log.model.SysLogSwitch;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
/**
 * 日志开关业务层
 * <p>Title:SysLogSwitchService</p>
 * @author Yangbo 
 * @date 2016年9月7日上午9:15:09
 */
@Service
public class SysLogSwitchService extends BaseService<SysLogSwitch> implements ISysLogSwitchService{

	@Resource
	private SysLogSwitchDao dao;

	protected IEntityDao<SysLogSwitch, Long> getEntityDao() {
		return this.dao;
	}

	@SuppressWarnings("rawtypes")
	public List<SysLogSwitch> getAll() {
		List<SysLogSwitch> switchs = new ArrayList<SysLogSwitch>();
		//首先，处理平台的日志模块
		getLogSwitch(switchs,SysAuditModelType.class);
		//其次，处理实施人员定制的日志模块
		try {
			IExtendOwnerModelClazz extendClazz=AppUtil.getBean(IExtendOwnerModelClazz.class);
	       	if(extendClazz!=null){
	       		Class clazz=extendClazz.getExtendOwnerModelClazz();
	       		if(clazz!=null){
	       			getLogSwitch(switchs,clazz);
	       		}
	       	}
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		return switchs;
	}

	@SuppressWarnings("rawtypes")
	private void getLogSwitch(List<SysLogSwitch> switchs, Class clazz) {
		Field[] fields = clazz.getFields();
		for (Field field : fields) {
			String ownerModel = null;
			
			try {
				ownerModel = field.get(clazz).toString();
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			if(ownerModel==null||ownerModel.equals(SysAuditModelType.NULL)){
				continue;
			}
			SysLogSwitch logSwitch = null;
			logSwitch = this.dao.getByModel(ownerModel);
			if (logSwitch == null) {
				logSwitch = getInitSysLogSwitch(ownerModel);
			}
			switchs.add(logSwitch);
		}
	}

	private SysLogSwitch getInitSysLogSwitch(String ownerModel) {
		SysLogSwitch sysLogSwitch = new SysLogSwitch();
		sysLogSwitch.setModel(ownerModel);
		sysLogSwitch.setStatus(Short.valueOf((short) 0));
		return sysLogSwitch;
	}

	public SysLogSwitch getByModel(String ownermodel) {
		return this.dao.getByModel(ownermodel);
	}
}
