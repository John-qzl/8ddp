package com.cssrc.ibms.core.log.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.log.model.SysLogSwitch;
import com.cssrc.ibms.core.util.bean.BeanUtils;
/**
 * 开馆日志持久层
 * <p>Title:SysLogSwitchDao</p>
 * @author Yangbo 
 * @date 2016年9月7日上午9:08:05
 */
@Repository
public class SysLogSwitchDao extends BaseDao<SysLogSwitch> {
	public Class<?> getEntityClass() {
		return SysLogSwitch.class;
	}

	public SysLogSwitch getByModel(String model) {
		List switchs = getBySqlKey("getByModel", model);
		if (BeanUtils.isNotEmpty(switchs)) {
			return (SysLogSwitch) switchs.get(0);
		}
		return null;
	}
}
