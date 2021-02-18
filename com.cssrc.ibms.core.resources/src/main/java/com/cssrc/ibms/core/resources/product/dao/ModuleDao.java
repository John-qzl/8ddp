package com.cssrc.ibms.core.resources.product.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.bean.datapackageModel.ModelBean;
import com.cssrc.ibms.core.resources.product.util.SqlHelp;
import com.cssrc.ibms.core.util.common.MapUtil;

import javassist.expr.NewArray;

/**
 * @description 型号管理数据库操作类
 * @author xie chen
 * @date 2019年11月21日 下午1:51:28
 * @version V1.0
 */
@Repository
public class ModuleDao {
	
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * @Desc 获取所有型号数据
	 * @return
	 */
	public List<Map<String, Object>> getAllModules(){
		String sql="SELECT * FROM W_XHJBSXB ORDER BY ID ";
		return jdbcDao.queryForList(sql,null);
	}
	
	public Map<String,Object> getById(String id){
		String sql="SELECT * FROM W_XHJBSXB  where ID="+"'"+id+"'";
		return jdbcDao.queryForMap(sql, null);
	}
	public void insert(ModelBean modelBean) {
		String  sql = SqlHelp.getInsertSql(ModelBean.class, "W_XHJBSXB");
		Map<String, Object> map = MapUtil.transBean2Map(modelBean);
		jdbcDao.exesql(sql, map);
	}

	public void update(ModelBean modelBean) {
		String  sql = SqlHelp.getUpdateSql(ModelBean.class, "W_XHJBSXB");
		Map<String, Object> map = MapUtil.transBean2Map(modelBean);
		jdbcDao.exesql(sql, map);
	}

	public List<Map<String, Object>> getModelTeamUser(String teamId){
		String sql="SELECT * FROM W_glytdb where F_SSGLYTD='"+teamId+"'";
		List<Map<String, Object>> list=jdbcDao.queryForList(sql, null);
		return list;
	}
	public Map<String, Object> getModelTeam(String teamId){
		Map<String,Object> map=new HashMap<String, Object>();
		String sql="select * from w_xhglyb where ID='"+teamId+"'";
		map=jdbcDao.queryForMap(sql, null);
		return map;
	}

	public String isEditRole(String UserId,String typeId) 
	{
		String sql="select * from W_XHGLYB where F_GLXHID='"+typeId+"'";
		List<Map<String, Object>> list=jdbcDao.queryForList(sql, null);
		for (Map<String, Object> map : list) {
			sql="select count(*) from w_glytdb where F_glyID='"+UserId+"' and F_SSGLYTD='"+map.get("ID").toString()+"'";
			int number=jdbcDao.queryForInt(sql, null);
			if(number!=0) {
				return "1";
			}
		}
		return "0";
	}

	/**
	 * 获取一个部门下的所有型号
	 * @param orgId
	 * @return
	 */
	public List<Map<String,Object>> getModuleByOrgId(String orgId){
		Map<String,Object> sqlMap=new HashMap<>();
		sqlMap.put("orgId",orgId);
		String sql="select * from W_XHJBSXB where F_XMBID=:orgId";
		List<Map<String,Object>> resMapList=jdbcDao.queryForList(sql,sqlMap);
		return resMapList;
	}

}


