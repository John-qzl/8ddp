package com.cssrc.ibms.core.resources.datapackage.dao;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.datapackage.model.CheckDataPackageInfo;
import com.cssrc.ibms.core.resources.datapackage.model.CheckPackage;
import com.cssrc.ibms.core.resources.io.bean.pack.SimplePackage;
import com.cssrc.ibms.core.util.date.DateUtil;

/**
 * @author user
 *
 */
@Repository
public class PackageDao {
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private DataPackageDao dataPackageDao;
	@Resource
	private TeamDao teamDao;
	/**
	 * 是否为数据包节点负责人
	 * @param userId
	 * @param sssjb： 所属数据包节点ID
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean isNodeMan(Long userId,Long sssjb) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*) from W_PACKAGE where id =:sssjb and F_FZRID = :userId ");
		Map map = new HashMap();
		map.put("userId", userId);
		map.put("sssjb", sssjb);
		int count = jdbcDao.queryForInt(sql.toString(), map);
		return count>0;
	}
	
	/** 根据数据包id和pid查询符合条件的数据包结构树  */
	public List<Map<String, Object>> queryDataPackageInfo(String id,String pid){
		String sql="SELECT * FROM W_PACKAGE WHERE (F_SSFC is null OR F_SSFC = '" +id+ "') AND F_SSXH = '"+pid+"' ORDER BY F_TCPX";

		return jdbcDao.queryForList(sql,null);
	}
	
	/** 根据发次id查询其相关子节点  */
	public List<Map<String, Object>> queryDataPackageById(String id){
		String sql="SELECT ID FROM W_PACKAGE WHERE F_SSFC in (" +id+ ")";
		return jdbcDao.queryForList(sql,null);
	}
	
	/** 查询数据包结构树普通分类节点 (PARENTID=0) */
	public List<Map<String, Object>> queryPackageNormalNode(String pId){
		String sql = "SELECT * FROM W_PACKAGE WHERE F_JDLX = '普通分类节点' AND F_PARENTID='0' AND F_SSXH = '"+pId+"' ORDER BY F_TCPX";
		return jdbcDao.queryForList(sql,null);
	}
	
	/** 查询数据包结构树非普通分类节点 (PARENTID=0) */
	public List<Map<String, Object>> queryPackageSpecNode(String pId){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM W_PACKAGE START WITH ID IN ( ");
		sql.append(" SELECT ID FROM W_PACKAGE WHERE F_JDLX != '普通分类节点' AND F_PARENTID='0' AND F_SSXH = '").append(pId).append("') ")
			.append(" connect by prior ID = F_PARENTID  ORDER BY F_TCPX ");
		return jdbcDao.queryForList(sql.toString(),null);
	}
			
	/** 根据数据包pid和productId查询所有数据包结构树  */
	public List<Map<String, Object>> queryDataPackageInfoByPId(String pid, String productId){
		String sql="SELECT * FROM W_PACKAGE WHERE F_SSXH = '" + productId + "' AND F_PARENTID IN (" + pid + ") ORDER BY F_TCPX";
		return jdbcDao.queryForList(sql, null);
	}
	
	/** 根据发次id查询发次名称  */
	public Map<String, Object> getProjectNameById(String projectId) {
		String sql="SELECT F_FCMC FROM W_PROJECT WHERE ID = '" + projectId + "'";
		return jdbcDao.queryForMap(sql, null);
	}
	
	/** 
	 * 根据数据包id删除数据包信息
	 * 删除
	 */
	public int deleteData(String id) throws Exception{
		int count;
		try{				
			StringBuffer sql = new StringBuffer();
			sql.append(" DELETE FROM W_PACKAGE WHERE ID in(");
			sql.append(" 	select ID from W_PACKAGE  start with ID="+id+" connect by prior ID = F_PARENTID");
			sql.append(" )");
			count = jdbcDao.exesql(sql.toString(), null);
			
		}catch(Exception e){
			count = -1;
		}
		return count;
	}
	
	/**
	 * 获取所有孙子节点信息
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String,Object>> getTreeBysID(Long id){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_PACKAGE  start with ID=:ID connect by prior ID = F_PARENTID ");
		sql.append(" order by to_number(F_TCPX) ");
		Map params = new HashMap();
		params.put("ID", id);
		return jdbcDao.queryForList(sql.toString(),params);
	}
	/**
	 * 获取子节点信息
	 * @param id： 父节点id
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String,Object>> getSon(Long id){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_PACKAGE where F_PARENTID =:F_PARENTID");
		Map params = new HashMap();
		params.put("F_PARENTID", id);
		return jdbcDao.queryForList(sql.toString(), params);
	}
	/**
	 * 根据主键获取节点信息
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String,Object> getById(Long id){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_PACKAGE where ID=:ID");
		Map params = new HashMap();
		params.put("ID", id);
		return jdbcDao.queryForMap(sql.toString(), params);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Long insert(SimplePackage pack) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into w_package (ID, F_JDMC, F_JDLX, F_FZR, F_FZRID, F_PARENTID, F_CJSJ, F_CHSPZT, F_TZZT,");
		sql.append(" F_SSXH, F_SSFC, F_PARENTNAME, F_TEST_SYDD, F_TEST_CSDW, F_TEST_JHKSSJ,F_TEST_JHJSSJ, ");
		sql.append(" F_SOFT_RJDH, F_SOFT_RJMC, F_SOFT_ZT, F_SOFT_ZRDW, F_SOFT_BBH, F_PART_CPDH, F_PART_CPMC, ");
		sql.append(" F_PART_ZT, F_PART_ZRDW, F_DESI_JHKSSJ, F_DESI_JHJSSJ, F_SM) ");
		sql.append(" values (:ID,:F_JDMC,:F_JDLX,:F_FZR,:F_FZRID,:F_PARENTID,:F_CJSJ,:F_CHSPZT,:F_TZZT,");
		sql.append(" :F_SSXH,:F_SSFC,:F_PARENTNAME,:F_TEST_SYDD,:F_TEST_CSDW,:F_TEST_JHKSSJ,:F_TEST_JHJSSJ, ");
		sql.append(" :F_SOFT_RJDH,:F_SOFT_RJMC,:F_SOFT_ZT,:F_SOFT_ZRDW,:F_SOFT_BBH,:F_PART_CPDH,:F_PART_CPMC, ");
		sql.append(" :F_PART_ZT,:F_PART_ZRDW,:F_DESI_JHKSSJ,:F_DESI_JHJSSJ,:F_SM) ");
		Map map = new HashMap();
		map.put("ID", pack.getId());
		map.put("F_JDMC", pack.getJdmc());
		map.put("F_JDLX", pack.getJdlx());
		map.put("F_FZR", pack.getFzr());
		map.put("F_FZRID", pack.getFzrID());
		map.put("F_PARENTID", pack.getParentID());
		map.put("F_CJSJ", DateUtil.getDate(pack.getCjsj()));
		map.put("F_CJSJ", pack.getCjsj());
		map.put("F_CHSPZT", pack.getChspzt());
		map.put("F_TZZT", pack.getTzzt());
		map.put("F_SSXH", pack.getSsxh());
		map.put("F_SSFC", pack.getSsfc());
		map.put("F_PARENTNAME", pack.getParentName());
		map.put("F_TEST_SYDD", pack.getTest_sydd());
		map.put("F_TEST_CSDW", pack.getTest_csdw());
		map.put("F_TEST_JHKSSJ", DateUtil.getDate(pack.getTest_jhkssj()));
		map.put("F_TEST_JHJSSJ", DateUtil.getDate(pack.getTest_jhjssj()));
		map.put("F_SOFT_RJDH", pack.getSoft_rjdh());
		map.put("F_SOFT_RJMC", pack.getSoft_rjmc());
		map.put("F_SOFT_ZT", pack.getSoft_zt());
		map.put("F_SOFT_ZRDW", pack.getSoft_zrdw());
		map.put("F_SOFT_BBH", pack.getSoft_bbh());
		map.put("F_PART_CPDH", pack.getPart_cpdh());
		map.put("F_PART_CPMC", pack.getPart_cpmc());
		map.put("F_PART_ZT", pack.getPart_zt());
		map.put("F_PART_ZRDW", pack.getPart_zrdw());
		map.put("F_DESI_JHKSSJ", pack.getDesi_jhkssj());
		map.put("F_DESI_JHJSSJ", pack.getDesi_jhjssj());
		map.put("F_SM", pack.getSm());
		jdbcDao.exesql(sql.toString(), map);
		return Long.valueOf(map.get("ID").toString());
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Long update(SimplePackage pack) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" update w_package set F_JDMC=:F_JDMC,F_JDLX=:F_JDLX, F_FZR=:F_FZR, F_FZRID=:F_FZRID,F_PARENTID =:F_PARENTID, F_CJSJ=:F_CJSJ, F_CHSPZT=:F_CHSPZT, F_TZZT=:F_TZZT,");
		sql.append(" F_SSXH=:F_SSXH, F_SSFC=:F_SSFC, F_PARENTNAME=:F_PARENTNAME, F_TEST_SYDD=:F_TEST_SYDD, F_TEST_CSDW=:F_TEST_CSDW, F_TEST_JHKSSJ=:F_TEST_JHKSSJ, F_TEST_JHJSSJ=:F_TEST_JHJSSJ, ");
		sql.append(" F_SOFT_RJDH=:F_SOFT_RJDH, F_SOFT_RJMC=:F_SOFT_RJMC, F_SOFT_ZT=:F_SOFT_ZT, F_SOFT_ZRDW=:F_SOFT_ZRDW, F_SOFT_BBH=:F_SOFT_BBH, F_PART_CPDH=:F_PART_CPDH, F_PART_CPMC=:F_PART_CPMC, ");
		sql.append(" F_PART_ZT=:F_PART_ZT, F_PART_ZRDW=:F_PART_ZRDW, F_DESI_JHKSSJ=:F_DESI_JHKSSJ, F_DESI_JHJSSJ=:F_DESI_JHJSSJ, F_SM=:F_SM  ");
		sql.append(" where ID=:ID");
		Map map = new HashMap();
		map.put("ID", Long.valueOf(pack.getId()));
		map.put("F_JDMC", pack.getJdmc());
		map.put("F_JDLX", pack.getJdlx());
		map.put("F_FZR", pack.getFzr());
		map.put("F_FZRID", pack.getFzrID());
		map.put("F_PARENTID", pack.getParentID());
		map.put("F_CJSJ", DateUtil.getDate(pack.getCjsj()));
		map.put("F_CHSPZT", pack.getChspzt());
		map.put("F_TZZT", pack.getTzzt());
		map.put("F_SSXH", pack.getSsxh());
		map.put("F_SSFC", pack.getSsfc());
		map.put("F_PARENTNAME", pack.getParentName());
		map.put("F_TEST_SYDD", pack.getTest_sydd());
		map.put("F_TEST_CSDW", pack.getTest_csdw());
		map.put("F_TEST_JHKSSJ", DateUtil.getDate(pack.getTest_jhkssj()));
		map.put("F_TEST_JHJSSJ", DateUtil.getDate(pack.getTest_jhjssj()));
		map.put("F_SOFT_RJDH", pack.getSoft_rjdh());
		map.put("F_SOFT_RJMC", pack.getSoft_rjmc());
		map.put("F_SOFT_ZT", pack.getSoft_zt());
		map.put("F_SOFT_ZRDW", pack.getSoft_zrdw());
		map.put("F_SOFT_BBH", pack.getSoft_bbh());
		map.put("F_PART_CPDH", pack.getPart_cpdh());
		map.put("F_PART_CPMC", pack.getPart_cpmc());
		map.put("F_PART_ZT", pack.getPart_zt());
		map.put("F_PART_ZRDW", pack.getPart_zrdw());
		map.put("F_DESI_JHKSSJ", DateUtil.getDate(pack.getDesi_jhkssj()));
		map.put("F_DESI_JHJSSJ", DateUtil.getDate(pack.getDesi_jhjssj()));
		map.put("F_SM", pack.getSm());
		jdbcDao.exesql(sql.toString(), map);
		return (Long)map.get("ID");
	}
	
	/**
	 * 查询
	 * @param keyValueMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map<String,Object>> query(Map<String,Object> keyValueMap){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from w_cplbpcb where 1=1 ");
		Set keys = keyValueMap.keySet();
		for(Iterator it = keys.iterator();it.hasNext();) {
			String key = (String)it.next();
			Object value = keyValueMap.get(key);
			sql.append(" and ").append(key).append(" = '").append(value).append("'");
		}
		sql.append(" order by to_number(ID) ");
		return jdbcDao.queryForList(sql.toString(), null);
	}
	public  List<Map<String,Object>> getByIds(String ids){
		String idStr = "'"+ids.replace(",", "','")+"'";
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_PACKAGE where 1=1 ");
		sql.append(" and id in (").append(idStr).append(")");
		return jdbcDao.queryForList(sql.toString(), null);		
	}

	/**
	 * @Author  shenguoliang
	 * @Description: 只获取第一层的节点,根据父节点来获取相关的子节点
	 * @Params [ids]
	 * @Date 2018/6/11 16:08
	 * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 */
	
	public  List<Map<String,Object>> getByIdsAndParentId(String ids){
		String idStr = "'"+ids.replace(",", "','")+"'";
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_PACKAGE where 1=1 ");
		sql.append(" and F_PARENTID = 0 ");
		sql.append(" and id in (").append(idStr).append(")");
		return jdbcDao.queryForList(sql.toString(), null);
	}
	
	/**根据同层排序查询ID*/
	public List<Map<String,Object>>  selectIdByTcpx(String tcpx,String prId,String fcId) {
		String sql="SELECT ID FROM W_PACKAGE  WHERE F_TCPX = '"+tcpx+"' AND F_PARENTID = '" +prId+"' AND F_SSFC = '"+fcId+"' ";
		return jdbcDao.queryForList(sql.toString(), null);
	}
	
	/**根据ID查询节点名*/
	public String selectNameById(String id) {

		String sql="SELECT F_JDMC FROM W_PACKAGE  WHERE ID = '"+id+"' ";
		return jdbcTemplate.queryForObject(sql, String.class);
	}
	
	
	public String selectIdByIntemdefId(String intemdefId,String oldId) {

		String sql="SELECT ID FROM W_CK_RESULT_CARRY  WHERE F_ITEMDEF_ID = '"+intemdefId+"'AND F_TB_INSTAN = '"+oldId+"'";
		return jdbcTemplate.queryForObject(sql, String.class);
	}
	
	/**根据ID查询同层排序*/
	public String selectTcpxById(String id) {
		String sql="SELECT F_TCPX FROM W_PACKAGE  WHERE ID = "+id;
	//	return jdbcTemplate.queryForObject(sql, String.class);
			String s=jdbcTemplate.queryForObject(sql, String.class);
		Map<String, Object>  ss= jdbcTemplate.queryForMap(sql);
		//Map<String, Object>  s = jdbcDao.queryForMap(sql, null);
		return s;
	}
	
	/** 查询tcpx为空 */
    public List<Map<String,Object>> selectNullTcpx(String prId){
    	
        String sql="SELECT * FROM W_PACKAGE WHERE F_TCPX is NULL AND F_PARENTID ="+prId;
		return jdbcDao.queryForList(sql,null);
    }
    
	/**根据ID查询父节点名*/
	public String selectParentNameById(String id) {
		String sql="SELECT F_PARENTNAME FROM W_PACKAGE  WHERE ID = "+id;
	//	return jdbcTemplate.queryForObject(sql, String.class);
			String s=jdbcTemplate.queryForObject(sql, String.class);
		Map<String, Object>  ss= jdbcTemplate.queryForMap(sql);
		//Map<String, Object>  s = jdbcDao.queryForMap(sql, null);
		return s;
	}
	
	/**根据ID查询父节点ID*/
	public String selectParentIdById(String id) {
		String sql="SELECT F_PARENTID FROM W_PACKAGE  WHERE ID = "+id;
	//	return jdbcTemplate.queryForObject(sql, String.class);
			String s=jdbcTemplate.queryForObject(sql, String.class);
		Map<String, Object>  ss= jdbcTemplate.queryForMap(sql);
		//Map<String, Object>  s = jdbcDao.queryForMap(sql, null);
		return s;
	}
	
	/**根据父节点ID查询*/
	public List<Map<String,Object>> selectByParentId(String id) {
		String sql="SELECT * FROM W_PACKAGE  WHERE F_PARENTID = "+id;
		List<Map<String,Object>> s = jdbcTemplate.queryForList(sql);
		return s;
	}
	
	/**根据ID查询W_PACKAGE*/
	public CheckPackage selectById(Long id) {
		String sql="SELECT * FROM W_PACKAGE  WHERE ID = "+id;
		CheckPackage s = jdbcTemplate.queryForObject(sql, CheckPackage.class);
		return s;
	}
	
	/**根据ID查询W_DataPACKAGEInfo*/
	public List<CheckDataPackageInfo> selectDataPackageInfoById(Long id) {
		String sql="SELECT * FROM W_DataPACKAGEInfo  WHERE F_SSSJB = "+id;
		List<CheckDataPackageInfo> s = jdbcTemplate.queryForList(sql, CheckDataPackageInfo.class);
		return s;
	}
	
	/**根据ID更新同层排序*/
	public void updateTcpxById(String id,String tcpx) {

		String sql="UPDATE W_PACKAGE SET F_TCPX ="+tcpx+ " WHERE ID = '"+id+"'";
		jdbcTemplate.update(sql);
	}
	
	/**根据ID更新父节点名*/
	public void updateParentNameById(String id,String prname) {

		String sql="UPDATE W_PACKAGE SET F_PARENTNAME ='"+prname+ "' WHERE ID = '"+id+"'";
		jdbcTemplate.update(sql);
	}
	
	/**插入一条新数据*/
	public void updateParentNameById(String field) {

		String sql="INSERT INTO W_PACKAGE VALUES"+field;
		jdbcTemplate.update(sql);
	}
	
	/**根据ID更新父节点ID*/
	public void updateParentIdById(String id,String prid) {

		String sql="UPDATE W_PACKAGE SET F_PARENTID ="+prid+ " WHERE ID = '"+id+"'";
		jdbcTemplate.update(sql);
	}
	
	/**根据父节点ID更新父节点ID*/
	public void updateParentIdByParentId(String id,String newid) {

		String sql="UPDATE W_PACKAGE SET F_PARENTID ="+newid+ " WHERE F_PARENTID = '"+id+"'";
		jdbcTemplate.update(sql);
	}
	
	/**根据父节点ID更新父节点名*/
	public void updateParentNameByParentId(String id,String newname) {

		String sql="UPDATE W_PACKAGE SET F_PARENTNAME ='"+newname+ "' WHERE F_PARENTID = '"+id+"'";
		jdbcTemplate.update(sql);
	}
	
	/**根据模版ID删除模版*/
	public void deleteTemplate(String Id) {

		String sql="DELETE FROM W_SJBJGSDCB WHERE ID ='"+Id+ "'";
		jdbcTemplate.update(sql);
	}
	/** 查询某发次下同一父节点下所有记录 */
    public List<Map<String,Object>> selectAllFrompackage(String prId,String fcId){
    	
        String sql="SELECT * FROM W_PACKAGE WHERE F_SSFC ='"+fcId+"'AND F_PARENTID ='"+prId+"' ORDER BY F_TCPX ";
		return jdbcDao.queryForList(sql,null);
    }
	
	/**
	 * 1.删除数据包详细信息
	 * 2.删除工作队
	 * 3.删除自身
	 * @param id
	 * @param deleteSon : 是否删除关联信息
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void deleteById(String id,boolean deleteSon) {
		if(deleteSon) {
			dataPackageDao.deleteByPackage(id, deleteSon);
			this.teamDao.deleteByPackage(id);
		}
		String sql = "delete from W_PACKAGE where ID=:ID";
		Map<String,Object> parameter = new HashMap();
		parameter.put("ID", id);
		jdbcDao.exesql(sql, parameter);	
	}
	public boolean isEmptyPackgaeNode(String id) {
		String idstr = "select ID from W_PACKAGE  start with ID="+id+" connect by prior ID = F_PARENTID";
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(num) c from ( ");
		sql.append(" select count(*)num from W_WORKTEAM where F_SSSJB in (").append(idstr).append(")");
		sql.append(" union ");
		sql.append(" select count(*)num from W_DATAPACKAGEINFO where F_SSSJB in (").append(idstr).append(")");
		sql.append(")");
		int num = jdbcDao.queryForInt(sql.toString(), null);
		return num>0?false:true;
	}
	public boolean checkFormInstant(String id) {
		String idstr = "select ID from W_PACKAGE  start with ID="+id+" connect by prior ID = F_PARENTID";
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(num) c from ( ");
		sql.append(" select count(*)num from W_WORKTEAM where F_SSSJB in (").append(idstr).append(")");
		sql.append(" union ");
		sql.append(" select count(*)num from W_DATAPACKAGEINFO where F_SSSJB in (").append(idstr).append(")");
		sql.append(")");
		int num = jdbcDao.queryForInt(sql.toString(), null);
		return num>0?false:true;
	}
	public Long uodateParentTypeName(Long dataId,String typeName) {
		StringBuffer sql = new StringBuffer();
		Map map = new HashMap();
		sql.append("update W_PACKAGE set F_PARENTNAME = '"+typeName+"' where ID = "+dataId+"");
		jdbcDao.exesql(sql.toString(), map);
		return (Long)map.get("ID");
	}


}
