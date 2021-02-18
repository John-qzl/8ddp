package com.cssrc.ibms.dp.template.dao;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.dp.template.model.TemplateInstance;

/**
 * @description 模板实例数据库操作类
 * @author xie chen
 * @date 2019年12月5日 下午4:18:24
 * @version V1.0
 */
@Repository
public class TemplateInstanceDao  extends BaseDao<TemplateInstance>{

	@Override
	public Class getEntityClass() {
		return TemplateInstance.class;
	}
	
	/**
	 * @Desc 根据模板id校验模板是否已存在实例
	 * @param templateId
	 * @return
	 */
	public boolean isExistInstance(String templateId) {
		Map<String, Object> param = new HashMap<>();
		param.put("templateId", templateId);
		return (Integer)getOne("countTemplate", param)>0;
	}
	
}
