package com.cssrc.ibms.record.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.rec.intf.IRecTypeService;
import com.cssrc.ibms.api.rec.model.IRecType;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;
import com.cssrc.ibms.record.dao.RecTypeDao;
import com.cssrc.ibms.record.model.RecType;
import com.cssrc.ibms.system.model.CurrentSystem;
import com.cssrc.ibms.system.service.SysParameterService;

/**
 * Description:
 * <p>RecTypeService.java</p>
 * @author dengwenjie 
 * @date 2017年3月13日
 */
@Service
public class RecTypeService  extends BaseService<RecType> implements IRecTypeService{
	@Resource
	private RecTypeDao recTypeDao;
	@Resource
	SysParameterService sysParameterService;
	
	public  List<RecType> getAll(){
		return recTypeDao.getAll();
	}
	
	protected IEntityDao<RecType, Long> getEntityDao() {
		return this.recTypeDao;
	}
	/**
	 * 是否存在该类别
	 * 
	 * @param function
	 * @return
	 */
	public Integer isAliasExists(RecType type) {
		String alias = type.getAlias();
		return this.recTypeDao.isAliasExists(alias);
	}
	/**
	 * 通过父id获得父功能点
	 * 
	 * @param parentId
	 * @return
	 */
	public RecType getParentTypeById(long parentId) {
		RecType parent = (RecType) this.recTypeDao.getById(Long
				.valueOf(parentId));
		if (parent != null)
			return parent;
		parent = new RecType();
		parent.setTypeId(Long.valueOf(0L));
		parent.setParentId(Long.valueOf(-1L));
		parent.setTypeName("角色类别");

		return parent;
	}
	
	public RecType getByAlias(String alias) {
		return this.recTypeDao.getByAlias(alias);
	}
}
