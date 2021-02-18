package com.cssrc.ibms.core.resources.product.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.bean.ProductCategoryBath;

/**
 * @description 产品类别/批次数据库操作类
 * @author xie chen
 * @date 2019年11月21日 下午7:22:09
 * @version V1.0
 */
@Repository
public class ProductCategoryBatchDao {
	
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * @Desc 根据型号id获取产品类别
	 * @param moduleId
	 * @return
	 */
	public List<Map<String, Object>> getCategoriesByModuleId(String moduleId){
		String sql="SELECT * FROM W_CPLBPCB WHERE F_SSXH = '" + moduleId + "' AND F_LBHPC = 'category' ORDER BY ID ";
		return jdbcDao.queryForList(sql,null);
	}
	/**
	 * @Desc 根据型号ids获取产品类别
	 * @param moduleId
	 * @return
	 */
	public List<Map<String, Object>> getCategoriesByModuleIds(String moduleIds){
		String sql="SELECT * FROM W_CPLBPCB WHERE F_SSXH in(" + moduleIds + ") AND F_LBHPC = 'category' ORDER BY ID ";
		return jdbcDao.queryForList(sql,null);
	}
	public String getPlanNumber(String moduleId){

		String sql="SELECT * FROM(SELECT F_CHBGBBH FROM W_CPYSCHBGB where  F_SSXHID='"+moduleId+"' ORDER BY ID DESC) WHERE ROWNUM<=1";
		Map<String, Object> map=jdbcDao.queryForMap(sql, null);
		String fullNumber="001";
		if(map!=null) {
			fullNumber=map.get("F_CHBGBBH").toString();
		}
		//String number= String.valueOf(jdbcDao.queryForInt(sql, null)+1);
		String lastThreeNumber=fullNumber.substring(fullNumber.length()-3);
		if(map==null){
			//如果找不到记录,直接返回001,如果继续向下执行会返回002
			return fullNumber;
		}
		Integer lastNumber=Integer.valueOf(lastThreeNumber)+1;
		String number=(lastNumber).toString();
		switch (number.length()) {
		case 1:
			number="00"+number;
			break;
		case 2:
			number="0"+number;
			break;
		case 3:
			number=number;
			break;
		}
		return number;
	}
	public void insert(ProductCategoryBath productCategoryBath) {
		String sql="insert into W_CPLBPCB(ID,F_CPDH,F_CPMC,F_SSXH,F_XHJD,F_ZRBM,F_ZRBMID,"
				+ "F_CYBM,F_CYBMID,F_LBHPC,F_PCH,F_SL,F_SSXHFC,F_SSXHPC,F_SSXHJD,F_SSCPLB,F_YZDW,F_YZDWID) values(:ID,:F_CPDH,:F_CPMC,:F_SSXH,"
				+ ":F_XHJD,:F_ZRBM,:F_ZRBMID,:F_CYBM,:F_CYBMID,:F_LBHPC,:F_PCH,:F_SL,:F_SSXHFC,:F_SSXHPC,:F_SSXHJD,:F_SSCPLB,:F_YZDW,:F_YZDWID)";
		Map<String,Object> map=new HashedMap();
		map.put("ID", productCategoryBath.getId());
		map.put("F_CPDH", productCategoryBath.getCpdh());
		map.put("F_CPMC", productCategoryBath.getCpmc());
		map.put("F_SSXH", productCategoryBath.getSsxh());
		map.put("F_XHJD", productCategoryBath.getXhjd());
		map.put("F_ZRBM", productCategoryBath.getZrbm());
		map.put("F_ZRBMID", productCategoryBath.getZrbmId());
		map.put("F_CYBM", productCategoryBath.getCybm());
		map.put("F_CYBMID", productCategoryBath.getCybmId());
		map.put("F_LBHPC", productCategoryBath.getLbhpc());
		map.put("F_PCH", productCategoryBath.getPch());
		map.put("F_SL", productCategoryBath.getSl());
		map.put("F_SSXHFC", productCategoryBath.getSsxhfc());
		map.put("F_SSXHPC", productCategoryBath.getSsxhpc());
		map.put("F_SSXHJD", productCategoryBath.getSsxhjd());
		map.put("F_SSCPLB", productCategoryBath.getSscplb());
		map.put("F_YZDW", productCategoryBath.getYzdw());
		map.put("F_YZDWID", productCategoryBath.getYzdwId());
		jdbcDao.exesql(sql, map);
	}
	
	/**
	 * @Desc 根据产品类别id获取产品批次
	 * @param categoryId
	 * @return
	 */
	public List<Map<String, Object>> getBatchesByCategoryId(String categoryId) {
		String sql = "SELECT * FROM W_CPLBPCB WHERE F_SSCPLB = '" + categoryId + "' ORDER BY ID ";
		return jdbcDao.queryForList(sql, null);
	}
	/**
	 * @Desc 根据产品类别ids获取产品批次
	 * @param categoryId
	 * @return
	 */
	public List<Map<String, Object>> getBatchesByCategoryIds(String categoryIds) {
		String sql = "SELECT * FROM W_CPLBPCB WHERE F_SSCPLB in(" + categoryIds + ") ORDER BY ID ";
		return jdbcDao.queryForList(sql, null);
	}
	/**
	 * @Desc 获取所有类别
	 * @param categoryId
	 * @return
	 */
	public List<Map<String, Object>> getAllBatch() {
		String sql = "SELECT * FROM W_CPLBPCB WHERE  F_SSCPLB is null";
		return jdbcDao.queryForList(sql, null);
	}
	/**
	 * @Desc 根据id查询产品类别/批次
	 * @param categoryOrBatchId
	 * @return
	 */
	public Map<String, Object> getById(String categoryOrBatchId) {
		String sql = "SELECT * FROM W_CPLBPCB WHERE ID = '" + categoryOrBatchId + "'";
		return jdbcDao.queryForMap(sql, null);
	}
	/**
	 * @Desc 根据ids查询产品类别/批次
	 * @param categoryOrBatchIds
	 * @return
	 */
	public List<Map<String, Object>> getByIds(String id,String categoryOrBatchIds) throws Exception{
		List<Map<String,Object>> categoryList=new ArrayList<>();
		String[] str=categoryOrBatchIds.split(",");
		for (String string : str) {
			if(string.equals(id)) {
				continue;
			}
			String sql = "SELECT * FROM W_CPLBPCB WHERE ID = '" + string + "'";
			categoryList.add(jdbcDao.queryForMap(sql, null));
		}
		
		return categoryList;
	}
	/**
	 * @Desc 根据产品批次id/类别id查询产品批次及批次所属型号信息
	 * @param batchOrCategoryId
	 * @return
	 */
	public Map<String, Object> getBatchAndModule(String batchOrCategoryId) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT bat.*,module.ID as moduleId,module.F_XHDH as moduleKey,module.F_XHMC as moduleName ");
		sqlBuilder.append("FROM W_CPLBPCB bat,W_XHJBSXB module ");
		sqlBuilder.append("WHERE bat.ID = ").append(batchOrCategoryId).append(" and bat.F_SSXH = module.ID");
		return jdbcDao.queryForMap(sqlBuilder.toString(), null);
	}
	
	public void singleCategoryInsert(Map<String, Object> categoryMap) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO w_cplbpcb (ID,F_CPDH,F_CPMC,F_SSXH,F_ZRBM,F_YZDW,F_LBHPC)");
		sql.append(" VALUES ("+UniqueIdUtil.genId()+",'"+categoryMap.get("产品编号")+"','"+categoryMap.get("产品名称")+"',");
		sql.append("'"+categoryMap.get("moduleId")+"','"+categoryMap.get("责任（设计）部门")+"','"+categoryMap.get("研制（生产）单位")+"',");
		sql.append("'category')");
		jdbcTemplate.execute(sql.toString());
	}
	
}


