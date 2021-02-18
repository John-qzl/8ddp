package com.cssrc.ibms.core.resources.io.dao;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.bean.ImportHistory;
import com.cssrc.ibms.core.resources.product.util.SqlHelp;
import com.cssrc.ibms.core.util.common.MapUtil;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class ImportHistoryDao {
    @Resource
    private JdbcDao jdbcDao;

    public void insert(ImportHistory importHistory){
        String sql= SqlHelp.getInsertSql(ImportHistory.class,"W_SJDRLS");
        Map<String, Object> map = MapUtil.transBean2Map(importHistory);
        jdbcDao.exesql(sql,map);
    }
}
