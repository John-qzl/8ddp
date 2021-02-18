package com.cssrc.ibms.dp.form.dao;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ProductTypeDao {

	@Resource
	private JdbcDao jdbcDao;
	/**
	 * @Author  shenguoliang
	 * @Description:根据表单实例获取对应的型号的类型来区分检查结果
	 * @Params [instanId]表单实例
	 * @Date 2018/5/11 16:14
	 * @Return java.util.Map<java.lang.String,java.lang.Object>
	 */
	public Map<String, Object> getProductType( Long instanId) {

		String sql = " SELECT DISTINCT XH.ID,XH.F_TYPE TYPE FROM W_PRODUCT XH , " +
				"W_PROJECT FC , W_TABLE_TEMP MB ,W_TB_INSTANT SL " +
				"WHERE XH.ID = FC.F_SSXH " +
				"AND FC.ID = MB.F_PROJECT_ID " +
				"AND MB.ID = SL.F_TABLE_TEMP_ID  " +
				"AND SL.ID =:instanId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("instanId", instanId.toString());
		Map typeMap = jdbcDao.queryForMap(sql, params) ;

		return typeMap ;

	}
	/**
	 * @Description: 根据表单实例ID获取型号所属的类型表名
	 * @Author  shenguoliang
	 * @param instanId
	 * @Date 2018/12/19 9:19
	 * @Return java.lang.String
	 * @Line 53
	 */
	public String getProductTypeTableNameByInstanId(Long instanId) {
		String ck_resultName = "";

		Map typeMap = this.getProductType(instanId);

		if("空间".equals(typeMap.get("TYPE"))){
			ck_resultName = "W_CK_RESULT" ;
		}else if("运载".equals(typeMap.get("TYPE"))){
			ck_resultName = "W_CK_RESULT_CARRY" ;
		}else {
			ck_resultName = "W_CK_RESULT_JGJG" ;
		}
		return ck_resultName;
	}

/**
 * @Author  shenguoliang
 * @Description: 根据型号ID获取型号的类型
 * @Params [productId]
 * @Date 2018/5/21 15:51
 * @Return java.lang.String
 */
	public String getProductTypeByProductId( Long productId) {
		String sql = " SELECT F_TYPE FROM W_PRODUCT WHERE ID = :ID " ;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ID", productId);
		Map typeMap = jdbcDao.queryForMap(sql, params) ;
		String ck_resultName = "" ;
		if("空间".equals(typeMap.get("F_TYPE"))){
			ck_resultName = "W_CK_RESULT" ;
		}else if("运载".equals(typeMap.get("F_TYPE"))){
			ck_resultName = "W_CK_RESULT_CARRY" ;
		}else{
			ck_resultName = "W_CK_RESULT_JGJG" ;
		}
		return ck_resultName ;

	}
}
