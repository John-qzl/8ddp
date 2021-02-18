package com.cssrc.ibms.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.SysScript;

/**
 * 对象功能:系统脚本
 * @author hxl
 *
 */
@Repository
public class SysScriptDao extends BaseDao<SysScript>
{
	@Override
	public Class<?> getEntityClass()
	{
		return SysScript.class;
	}
	
    public List<String> getDistinctCategory()
    {
        List list = getBySqlKey("getDistinctCategory");
        return list;
    }
	     
    public Integer isExistWithName(String name)
    {
        return (Integer)getOne("isExistWithName", name);
    }
    
    /**
     * 根据 脚本名称查找 脚本
     * @param name
     * @return
     */
    public SysScript getByName(String name)
    {
        return this.getUnique("getByName", name);
    }

}