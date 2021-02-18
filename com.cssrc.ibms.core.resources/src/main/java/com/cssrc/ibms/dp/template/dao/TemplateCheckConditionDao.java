package com.cssrc.ibms.dp.template.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.dp.template.model.TemplateCheckCondition;

/**
 * @description 模板检查条件数据库操作类
 * @author xie chen
 * @date 2019年12月5日 上午9:03:28
 * @version V1.0
 */
@Repository
public class TemplateCheckConditionDao extends BaseDao<TemplateCheckCondition>{
	
	@Resource
	private JdbcDao jdbcDao;

	public Class getEntityClass() {
		return TemplateCheckCondition.class;
	}
	
	/**
	 * @Desc 根据模板id删除模板下的所有检查项
	 * @param templateId
	 */
	public void delByTemplateId(String templateId){
		Map<String, Object> param = new HashMap<>();
		param.put("templateId", templateId);
		delItemByID("delByTemplateId",param);
	}
	
	/**
	 * @Desc 获取模板检查条件集合
	 * @param templateId
	 * @return
	 */
	public List<Map<String, Object>> getCheckConditionListMapByTemplateId(String templateId) {
        String sql = "SELECT * FROM W_CK_CONDITION WHERE F_TABLE_TEMP_ID=:templateId";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("templateId", templateId);
        return jdbcDao.queryForList(sql, param);
    }
	
	public List<TemplateCheckCondition> getByTemplateId(String templateId) {
		Map<String, Object> params = new HashMap<>();
		params.put("templateId", templateId);
		return getBySqlKey("getByTemplateId", params);
	}

}
