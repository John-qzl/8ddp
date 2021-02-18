package com.cssrc.ibms.core.resources.product.dao;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;

/**
 * @description 型号批次数据库操作类
 * @author xie chen
 * @date 2019年11月29日 下午7:53:26
 * @version V1.0
 */
@Repository
public class ModuleBatchDao {
	
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * @Desc 根据型号阶段id获取型号批次
	 * @param modulePeriodId
	 * @return
	 */
	public List<Map<String, Object>> getBatchesByModulePeriodId(String modulePeriodId){
		String sql="SELECT * FROM W_XHPCB WHERE F_SSXHJD = '" + modulePeriodId + "' ORDER BY ID ";
		return jdbcDao.queryForList(sql,null);
	}
	
	/**
	 * @Desc 根据型号批次id获取型号批次、阶段、型号完整信息
	 * @param moduleBatchId
	 * @return
	 */
	public Map<String, Object> getBatchPeriodModule(String moduleBatchId) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select bat.ID batchId,bat.F_PCDH batchKey,bat.F_PCMC batchName,");
		sqlBuilder.append("period.ID periodId,module.ID moduleId,module.F_XHDH moduleKey ");
		sqlBuilder.append("from DP8D.W_XHPCB bat,DP8D.W_XHJDB period,DP8D.W_XHJBSXB module ");
		sqlBuilder.append("where bat.ID= ").append(moduleBatchId).append(" and bat.F_SSXHJD=period.ID and bat.F_SSXH=module.ID");
		return jdbcDao.queryForMap(sqlBuilder.toString(), null);
	}
	
}


