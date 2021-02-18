package com.cssrc.ibms.dp.template.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.dp.template.model.TemplateSign;

/**
 * @description 模板签署数据库操作类
 * @author xie chen
 * @date 2019年12月5日 上午9:23:48
 * @version V1.0
 */
@Repository
public class TemplateSignDao extends BaseDao<TemplateSign> {
	
	@Resource
	private JdbcDao jdbcDao;

	@Override
	public Class getEntityClass() {
		return TemplateSign.class;
	}
	
	public void delByTemplateId(String templateId){
		Map<String, Object> param = new HashMap<>();
		param.put("templateId", templateId);
		delItemByID("delByTemplateId",param);
	}
	
	/**
	 * @Desc 获取模板下的所有签署集合
	 * @param templateId
	 * @return
	 */
	public List<Map<String, Object>> getSignListMapByTemplateId(String templateId) {
        String sql = "SELECT * FROM W_SIGNDEF WHERE F_TABLE_TEMP_ID=:templateId";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("templateId", templateId);
        return jdbcDao.queryForList(sql, param);
    }
	
	public List<TemplateSign> getByTemplateId(String templateId) {
		Map<String, Object> params = new HashMap<>();
		params.put("templateId", templateId);
		return getBySqlKey("getByTemplateId", params);
	}
	
}
