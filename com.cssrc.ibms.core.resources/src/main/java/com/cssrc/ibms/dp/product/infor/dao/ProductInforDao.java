package com.cssrc.ibms.dp.product.infor.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.bean.FileData;
import com.cssrc.ibms.core.resources.product.util.SqlHelp;
import com.cssrc.ibms.core.util.common.MapUtil;
import com.cssrc.ibms.dp.product.acceptance.bean.ProductInfo;

/**
 * @description 产品信息数据库操作类
 * @author xie chen
 * @date 2019年11月21日 下午7:50:23
 * @version V1.0
 */
@Repository
public class ProductInforDao {
	
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * @Desc 根据批次id获取产品
	 * @param batchId
	 * @return
	 */
	public List<Map<String, Object>> getByProductBatchId(String batchId){
		String sql="SELECT * FROM W_CPB WHERE F_SSCPPC = '" + batchId + "' ORDER BY ID ";
		return jdbcDao.queryForList(sql,null);
	}
	
	/**
	 * @Desc （自动）添加产品数据
	 * @param map
	 */
	public void addProduct(Map<String, Object> map) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO w_cpb (ID,F_SSPCCH,F_SSCPPC,F_SSCPLB,F_CPPCDH,F_CPMC,F_CPBH,F_SSXH)");
		sql.append(" VALUES ("+UniqueIdUtil.genId()+",'"+map.get("acceptancePlanId")+"','"+map.get("productBatchId")+"','"+map.get("productCategoryId")+"',");
		sql.append("'"+map.get("productBatchKey")+"','"+map.get("productKey")+"','"+map.get("productKey")+"','"+map.get("modelId")+"')");
		jdbcTemplate.execute(sql.toString());
	}
	
	/**
	 * @Desc 获取批次策划下产品数据集合
	 * @param acceptancePlanId
	 * @return
	 */
	public List<Map<String, Object>> getByAcceptancePlanId(String acceptancePlanId){
		String sql="SELECT * FROM W_CPB WHERE F_SSPCCH = '" + acceptancePlanId + "' ORDER BY ID ";
		return jdbcDao.queryForList(sql,null);
	}
	
	
	public List<Map<String,Object>> getProductInfoByPlanId(String planId){
		String sql="SELECT * FROM W_CPB WHERE F_PLANID = '" + planId + "' ORDER BY ID ";
		return jdbcDao.queryForList(sql,null);
	}
	
	public List<Map<String, Object>> getProductByPlanId(String planId){
		String sql="select A.* from W_CPB A ";
		sql=sql+"INNER  JOIN W_TB_INSTANT B ON A.F_SSSLID = B.ID where ";
		sql=sql+"A.F_PLANID = '" + planId + "' and  B.F_STATUS!='废弃' ORDER BY A.ID ";
		return jdbcDao.queryForList(sql,null);
	}
	
	
	public List<ProductInfo> getByPlanId(String planId){
		String sql="SELECT * FROM W_CPB WHERE F_PLANID = '" + planId + "' ORDER BY ID ";
		List<Map<String, Object>> list=jdbcDao.queryForList(sql,null);
		if(list==null) {
			return null;
		}
		List<ProductInfo> productInfoList=new ArrayList<>();
		for (Map<String, Object> map : list) {
			productInfoList.add(new ProductInfo(map));
		}
		return productInfoList;
	}
	
	public List<ProductInfo> getByInstanceId(String instanceId){
		String sql="select * from w_CPB where F_SSSLID='" + instanceId +"' and F_CPMC!='/'";
		List<Map<String, Object>> list=jdbcDao.queryForList(sql,null);
		if(list==null) {
			return new ArrayList<ProductInfo>();
		}
		List<ProductInfo> productInfoList=new ArrayList<>();
		for (Map<String, Object> map : list) {
			productInfoList.add(new ProductInfo(map));
		}
		return productInfoList;
	}
	
	public ProductInfo getById(String id) {
		String sql="select * from W_CPB where ID='"+id+"'";
		Map<String,Object> map=jdbcDao.queryForMap(sql, null);
		if(map==null) {
			return null;
		}
		return new ProductInfo(map);
	}
	
	public void insert(ProductInfo productInfo) {
		String  sql = SqlHelp.getInsertSql(ProductInfo.class, "W_CPB");
		Map<String, Object> map = MapUtil.transBean2Map(productInfo);
		jdbcDao.exesql(sql, map);
	}
	
	
	public void update(ProductInfo productInfo) {
		String  sql = SqlHelp.getUpdateSql(ProductInfo.class, "W_CPB");
		Map<String, Object> map = MapUtil.transBean2Map(productInfo);
		jdbcDao.exesql(sql, map);
	}
	public List<ProductInfo> getProudctById(String productId) {
		String sql="select * from W_CPB where F_CPMC='"+productId+"'";
		List<Map<String,Object>> mapList=jdbcDao.queryForList(sql, null);
		if(mapList==null) {
			return null;
		}
		List<ProductInfo> productInfoList=new ArrayList<>();
		for (Map<String, Object> map : mapList) {
			productInfoList.add(new ProductInfo(map));
		}
		return productInfoList;
	}
}


