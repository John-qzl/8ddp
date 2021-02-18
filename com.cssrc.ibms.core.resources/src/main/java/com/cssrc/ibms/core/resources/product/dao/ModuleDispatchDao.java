package com.cssrc.ibms.core.resources.product.dao;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;

/**
 * @description 型号发次数据库操作类
 * @author xie chen
 * @date 2019年11月29日 下午8:18:13
 * @version V1.0
 */
@Repository
public class ModuleDispatchDao {
	
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * @Desc 根据型号批次id获取型号发次
	 * @param moduleBatchId
	 * @return
	 */
	public List<Map<String, Object>> getDispatchesByModuleBatchId(String moduleBatchId){
		String sql="SELECT * FROM W_XHFCB WHERE F_SSXHPC = '" + moduleBatchId + "' ORDER BY ID ";
		return jdbcDao.queryForList(sql,null);
	}
	
}


