package com.cssrc.ibms.dp.template.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.dp.template.model.TemplateCheckItem;

/**
 * @description 模板检查项数据库操作类
 * @author xie chen
 * @date 2019年12月5日 下午1:54:00
 * @version V1.0
 */
@Repository
public class TemplateCheckItemDao  extends BaseDao<TemplateCheckItem> {
	
	@Resource
	private JdbcDao jdbcDao;

	@Override
	public Class getEntityClass() {
		return TemplateCheckItem.class;
	}
	
	public void delByTemplateId(String templateId){
		Map<String, Object> param = new HashMap<>();
		param.put("templateId", templateId);
		delItemByID("delByTemplateId",templateId);
	}
	
	public List<TemplateCheckItem> getByTemplateId(String templateId) {
		Map<String, Object> params = new HashMap<>();
		params.put("templateId", templateId);
		return getBySqlKey("getByTemplateId", params);
	}
	
	/**
	 * @Desc 获取模板检查项集合
	 * @param templateId
	 * @return
	 */
	public List<Map<String, Object>> getCheckItemListMapByTemplateId(String templateId) {
        String sql = "SELECT * FROM W_ITEMDEF WHERE F_TABLE_TEMP_ID=:templateId";
        Map<String, Object> param = new HashMap<>();
        param.put("templateId", templateId);
        return jdbcDao.queryForList(sql, param);
    }
	
}
