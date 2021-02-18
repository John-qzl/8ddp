package com.cssrc.ibms.dp.template.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.dp.template.model.Template;

/**
 * @description 模板管理数据库操作类
 * @author xie chen
 * @date 2019年12月3日 下午3:52:59
 * @version V1.0
 */
@Repository
public class TemplateDao extends BaseDao<Template>{
	
    @Resource
    private JdbcDao jdbcDao;
    @Resource
    private JdbcTemplate jdbcTemplate;
    
    @Override
	public Class getEntityClass() {
		return Template.class;
	}

    /**
     * @Desc 获取型号id下的所有通用模板
     * @param moduleId
     * @return
     */
    public List<Map<String, Object>> getTemplatesByModuleId(String moduleId) {
        String sql = "SELECT * FROM W_TABLE_TEMP WHERE F_MODULE_ID=:moduleId   ORDER BY ID  ";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("moduleId", moduleId);
        return jdbcDao.queryForList(sql, param);
    }
    
    /**
     * @Desc 新增时校验模板编号是否已存在
     * @param templateCode
     * @return
     */
    public boolean isAddExistTemplateCode(String templateCode) {
		String sql = "SELECT COUNT(1) AS COUNT FROM W_TABLE_TEMP WHERE F_NUMBER=:templateCode";
		Map<String, Object> param = new HashMap<>();
		param.put("templateCode", templateCode);
    	return jdbcDao.queryForInt(sql, param)>0;
	}
    
    /**
     * @Desc 编辑时校验模板编号是否已存在
     * @param templateCode
     * @param templateId
     * @return
     */
    public boolean isEditExistTemplateCode(String templateCode, String templateId) {
		String sql = "SELECT COUNT(1) AS COUNT FROM W_TABLE_TEMP WHERE F_NUMBER='"+templateCode+"' AND ID="+templateId;
    	return jdbcTemplate.queryForInt(sql)>1;
	}
    
    /**
     * @Desc 更新模板html及模板状态
     * @param param
     */
    public void updateTemplateHtmlAndStatus(Map<String, Object> param) {
        String sql = "UPDATE W_TABLE_TEMP SET F_CONTENTS=:templateHtml, F_STATUS=:status WHERE ID=:templateId";
        jdbcDao.exesql(sql, param);
    }
    
    public void updateTemplateCreateInfor(String templateId,String creatorId,String createTime){
    	String sql="UPDATE W_TABLE_TEMP SET F_CREATE_ID ='"+creatorId+"' , F_CREATE_TIME ="+createTime+" WHERE ID = '"+templateId+"'";
		jdbcTemplate.update(sql);
    }
    
    /**
     * @Desc 根据模板id获取模板信息map
     * @param templateId
     * @return
     */
    public Map<String, Object> getTemplateMapById(String templateId) {
        String sql = "SELECT * FROM W_TABLE_TEMP WHERE ID="+templateId;
        return jdbcDao.queryForMap(sql, null);
    }
    
    /**
     * @Desc 获取产品类别下的指定模板名的所有模板
     * @param categoryId
     * @param templateName
     * @return
     */
    public List<Map<String,Object>> getTemplatesByCategoryIdAndName(String categoryId, String templateName){
    	String sql="SELECT * FROM W_TABLE_TEMP where F_PROJECT_ID = "+categoryId+" AND F_NAME = '"+templateName+"'";
		return jdbcDao.queryForList(sql,null);
    }
    
    public void updateTemplateHtmlById(Long id, String templateHtml) {
    	Map<String, Object> param = new HashMap<>();
    	param.put("id", id);
    	param.put("templateHtml", templateHtml);
        String sql = "UPDATE W_TABLE_TEMP SET F_CONTENTS=:templateHtml WHERE ID=:id";
        jdbcDao.exesql(sql, param);
    }
    
    /**
     * @Desc 获取产品类别下的模板集合
     * @param productCategoryId
     * @return
     */
    public List<Map<String, Object>> getTemplatesByCategoryId(String productCategoryId) {
        String sql = "SELECT * FROM W_TABLE_TEMP WHERE F_PROJECT_ID=:productCategoryId  ORDER BY ID  ";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("productCategoryId", productCategoryId);
        return jdbcDao.queryForList(sql, param);
    }
    
}
