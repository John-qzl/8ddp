package com.cssrc.ibms.core.resources.product.dao;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;

/**
 * @description 型号阶段数据库操作类
 * @author xie chen
 * @date 2019年11月29日 下午7:16:33
 * @version V1.0
 */
@Repository
public class ModulePeriodDao {
	
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * @Desc 根据型号id获取型号阶段
	 * @param moduleId
	 * @return
	 */
	public List<Map<String, Object>> getPeriodsByModuleId(String moduleId){
		String sql="SELECT * FROM W_XHJDB WHERE F_SSXH = '" + moduleId + "' ORDER BY ID ";
		return jdbcDao.queryForList(sql,null);
	}
	
	/**
	 * @Desc 根据型号阶段id获取型号阶段及阶段所属型号信息
	 * @param modulePeriodId
	 * @return
	 */
	public Map<String, Object> getPeriodAndModule(String modulePeriodId) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT period.*,module.ID as moduleId,module.F_XHMC as moduleName,module.F_XHZS as xhzs,module.F_XHZSID as xhzsID,module.F_XHZZH as xhzzh,module.F_XHZZHID as xhzzhID ");
		sqlBuilder.append("FROM W_XHJDB period,W_XHJBSXB module ");
		sqlBuilder.append("WHERE period.ID = ").append(modulePeriodId).append(" and period.F_SSXH = module.ID");
		return jdbcDao.queryForMap(sqlBuilder.toString(), null);
	}
	
}


