package com.cssrc.ibms.dp.product.formassign.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;

/**
 * @description 表单下发DB操作类（w_dataPackageInfo）
 * @author xie chen
 * @date 2019年12月17日 下午3:18:54
 * @version V1.0
 */
@Repository
public class FormAssignDao {
	
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * @Desc 获取产品批次下所有表单下发
	 * @param productBatchId
	 * @return
	 */
	public List<Map<String, Object>> getFormAssignByProductBatchId(String productBatchId){
		String sql="SELECT * FROM w_dataPackageInfo WHERE F_PRODUCTBATCHID='"+productBatchId+"' ORDER BY ID ";
		return jdbcDao.queryForList(sql,null);
	}
	
}


