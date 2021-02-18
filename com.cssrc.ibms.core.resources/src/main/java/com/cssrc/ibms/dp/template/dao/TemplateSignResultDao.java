package com.cssrc.ibms.dp.template.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;

/**
 * @description 模板签署（实例）数据结果DB操作类
 * @author xie chen
 * @date 2019年12月16日 上午10:26:32
 * @version V1.0
 */
@Repository
public class TemplateSignResultDao {
	
	@Resource
	private JdbcDao jdbcDao;
/*	
	*//**
	 * @Desc 获取模板签署项及空实例数据（预览）
	 * @param templateId
	 * @return
	 *//*
	public List<Map<String, Object>> getSignDefAndResultByTemplateId(String templateId) {
        String sql = "SELECT signResult.*,signDef.F_NAME FROM W_SIGNRESULT signResult RIGHT JOIN W_SIGNDEF signDef ON signResult.F_SIGNDEF_ID=signDef.ID WHERE signDef.F_TABLE_TEMP_ID=:templateId";
        Map<String, Object> param = new HashMap<>();
        param.put("templateId", templateId);
        return jdbcDao.queryForList(sql, param);
    }*/
	public List<Map<String, Object>> getSignDefAndResultByTemplateId(String templateId) {
        String sql = "SELECT * from W_SIGNDEF where F_TABLE_TEMP_ID='"+templateId+"'";
        Map<String, Object> param = new HashMap<>();
        param.put("templateId", templateId);
        return jdbcDao.queryForList(sql, param);
    }
}
