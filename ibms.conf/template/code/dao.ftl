<#assign package=table.variable.package>
<#assign class=table.variable.class>
<#assign classVar=table.variable.classVar>

package com.ibms.${system}.dao.${package};

import java.util.List;
import org.springframework.stereotype.Repository;

import com.ibms.core.dao.mybatis.BaseDao;
<#if table.isMain!=1>
import com.ibms.core.util.UniqueIdUtil;
import com.ibms.core.util.BeanUtils;
import com.ibms.core.web.query.QueryFilter;
</#if>
import com.ibms.${system}.model.${package}.${class};

@Repository
public class ${class}Dao extends BaseDao<${class}>
{
	@Override
	public Class<?> getEntityClass()
	{
		return ${class}.class;
	}

	<#if table.isMain!=1>
	/**
	 * 根据外键获取${table.tableDesc}列表
	 * @param refId
	 * @return
	 */
	public List<${class}> getByMainId(Long refId) {
		return this.getBySqlKey("get${class}List", refId);
	}
	
	/**
	 * 根据外键删除${table.tableDesc}
	 * @param refId
	 * @return
	 */
	public void delByMainId(Long refId) {
		this.delBySqlKey("delByMainId", refId);
	}
	</#if>	
}