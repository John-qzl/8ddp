package com.cssrc.ibms.system.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.SysParameter;
/**
 * 参数dao层
 * @see
 */
@Repository
public class SysParameterDao extends BaseDao<SysParameter>{
	public Class getEntityClass() {
		return SysParameter.class;
	}
	/**
	 * 根据参数名称获取参数数据
	 * 
	 * @param paramName
	 * @return
	 */
	public List<SysParameter> getByParamName(String paramName){
		return getBySqlKey("getByParamname", paramName);
	}
	
	
	//判断参数名称是否存在
	public boolean isExistParamName(String paramname) {
		Integer count = (Integer) getOne("isExistParamName", paramname);
		return count.intValue() > 0;
	}
	//判断该参数是否存在
	public boolean isExistParam(String paramname, Long id) {
		Map params = new HashMap();
		params.put("paramname", paramname);
		params.put("id", id);
		Integer count = (Integer) getOne("isExistParam", params);
		return count.intValue() > 0;
	} 
	
	//获取参数唯一对象
	public SysParameter getOneByParamName(String paramName){
		return (SysParameter)getUnique("getByParamname", paramName);
	}
	
	/**
	 * 设置系统参数分类。
	 * @author liubo
	 * @param type
	 * @param parameterIdList
	 */
	public void updCategory(String type,List<Long> parameterIdList){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		map.put("parameterIds", parameterIdList);
		this.update("updCategory",  map);
	}
	
	/**
	 * 获取系统参数分类。
	 * @author liubo
	 */
	public List<Map<String, Object>> getType(){
		return getBySqlKeyGenericity("getType", null);
	}
}

