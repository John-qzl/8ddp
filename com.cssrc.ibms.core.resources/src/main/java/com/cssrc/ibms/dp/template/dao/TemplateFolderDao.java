package com.cssrc.ibms.dp.template.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;

/**
 * @description 模板文件夹数据库操作类
 * @author xie chen
 * @date 2019年12月3日 下午4:08:59
 * @version V1.0
 */
@Repository
public class TemplateFolderDao {
    @Resource
    private JdbcDao jdbcDao;
    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * @Desc 获取型号下所有文件夹数据
     * @param moduleId
     * @return
     */
    public List<Map<String, Object>> getTemplateFolderByModuleId(String moduleId) {
        Map<String, Object> param = new HashMap<>();
        param.put("moduleId", moduleId);
        String sql = "SELECT * FROM W_TEMP_FILE WHERE F_MODULE_ID=:moduleId";
        return jdbcDao.queryForList(sql, param);
    }
    
    /**
     * @Desc 获取产品类别下所有文件夹
     * @param productCategoryId
     * @return
     */
    public List<Map<String, Object>> getTemplateFolderByCategoryId(String productCategoryId) {
        Map<String, Object> param = new HashMap<>();
        param.put("productCategoryId", productCategoryId);
        String sql = "SELECT * FROM W_TEMP_FILE WHERE F_PROJECT_ID=:productCategoryId";
        return jdbcDao.queryForList(sql, param);
    }
    
}
