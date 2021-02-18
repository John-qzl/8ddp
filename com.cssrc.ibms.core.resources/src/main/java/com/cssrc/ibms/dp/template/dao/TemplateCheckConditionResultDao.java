package com.cssrc.ibms.dp.template.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;

/**
 * @description 模板检查条件实例结果DB操作类
 * @author xie chen
 * @date 2019年12月16日 上午10:58:42
 * @version V1.0
 */
@Repository
public class TemplateCheckConditionResultDao {
	
	@Resource
	private JdbcDao jdbcDao;
	
	/**
	 * @Desc 获取模板检查条件及空实例数据（预览）
	 * @param templateId
	 * @return
	 */
	public List<Map<String, Object>> getConditionDefAndResultByTemplateId(String templateId) {
        String sql = "SELECT conditionResult.*,checkCondition.F_NAME FROM W_CONDI_RES conditionResult RIGHT JOIN W_CK_CONDITION checkCondition ON conditionResult.F_CONDITION_ID=checkCondition.ID WHERE checkCondition.F_TABLE_TEMP_ID=:templateId";
        Map<String, Object> param = new HashMap<>();
        param.put("templateId", templateId);
        return jdbcDao.queryForList(sql, param);
    }
	
}
