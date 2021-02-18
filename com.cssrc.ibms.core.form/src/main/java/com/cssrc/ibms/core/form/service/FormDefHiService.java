package com.cssrc.ibms.core.form.service;


import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.form.dao.FormDefHiDao;
import com.cssrc.ibms.core.form.model.FormDefHi;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
/**
 * 自定义表单历史Service层
 * @author YangBo
 *
 */
@Service
public class FormDefHiService extends BaseService<FormDefHi>
{

	@Resource
	private FormDefHiDao dao;

	protected IEntityDao<FormDefHi, Long> getEntityDao()
	{
		return this.dao;
	}
	
	/**
	 * 添加一条新的历史记录
	 * @param formDefHi
	 */
	public void addHisRecord(FormDefHi formDefHi)
	{
		ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();
		formDefHi.setHisId(Long.valueOf(UniqueIdUtil.genId()));
		formDefHi.setCreateBy(sysUser.getUserId());
		formDefHi.setCreator(sysUser.getFullname());
		formDefHi.setCreatetime(new Date());
		add(formDefHi);
	}
}

