package com.cssrc.ibms.core.resources.product.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cssrc.ibms.core.util.common.CommonTools;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;

@Repository
public class ProductDao {
	
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/** 查询目录树所有信息  */
	public List<Map<String, Object>> queryTreeAll(){
		String sql="SELECT * FROM W_PRODUCT ORDER BY ID ";
		return jdbcDao.queryForList(sql,null);
	}
	/**
	 * @param fcId :发次ID
	 * @return 发次的型号信息
	 */
	public Map<String, Object> getByFcId(Long fcId){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM W_PRODUCT where id in( ");
		sql.append(" select F_SSXH from W_PROJECT where ID='").append(fcId).append("')");
		return jdbcDao.queryForMap(sql.toString(), null);
	}
	/**
	 * @Author  shenguoliang
	 * @Description: 根据类型查询目录树信息
	 * @Params [flag]
	 * @Date 2018/5/4 8:48
	 * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 */
	public List<Map<String, Object>> queryTreeAllByType(String flag) {

		String sql="SELECT * FROM W_PRODUCT  WHERE F_TYPE = '"+flag+"' ORDER BY F_TCPX";
		return jdbcDao.queryForList(sql, null);
	}
	
	/**根据同层排序查询ID*/
	public String selectIdByTcpx(String tcpx,String prId) {

		String sql="SELECT ID FROM W_PROJECT  WHERE F_TCPX = '"+tcpx+"' AND F_SSXH = '" +prId+"'";
		String Id = jdbcTemplate.queryForObject(sql, String.class);
		return Id;
	}
	
	/**根据发次名称查询ID*/
	public String selectIdByName(String Name) {

		String sql="SELECT ID FROM W_PROJECT  WHERE F_FCMC = '"+Name+"'";
		String Id = jdbcTemplate.queryForObject(sql, String.class);
		return Id;
	}
	
	/** 查询所有记录（型号层） */
    public List<Map<String,Object>> selectAllFromproduct(){
    	
        String sql="SELECT * FROM W_PRODUCT ORDER BY F_TCPX ";
		return jdbcDao.queryForList(sql,null);
    }
    
    /**
     * Description : 获取型号表所有数据（过滤掉项目办人员管理表中已经存在的型号）
     * Author : XYF
     * Date : 2018年10月19日上午10:00:30
     * Return : List<Map<String,Object>>
     */
    public List<Map<String,Object>> getAllProduct (String product){
    	
        String sql="SELECT * FROM W_PRODUCT WHERE ID NOT IN"+product;
		return jdbcDao.queryForList(sql,null);
    }
    
    /**
     * Description : 获取项目办人员管理表中所有数据
     * Author : XYF
     * Date : 2018年10月19日上午9:45:09
     * Return : List<Map<String,Object>>
     */
    public List<Map<String,Object>> getProjectOffice(){
    	
        String sql="SELECT * FROM W_PROJECTOFFICE";
		return jdbcDao.queryForList(sql,null);
    }
    
    /**
     * Description : 根据型号ID查询型号信息
     * Author : XYF
     * Date : 2018年10月18日下午6:23:49
     * Return : List<Map<String,Object>>
     */
    public List<Map<String,Object>> selectProductById(String Id){
        String sql="SELECT * FROM W_PRODUCT WHERE ID = '"+Id+"'";
		return jdbcDao.queryForList(sql,null);
    }
    /**
     * Description : 根据发次ID查询发次信息
     * Author : XYF
     * Date : 2018年10月19日下午3:31:06
     * Return : List<Map<String,Object>>
     */
    public List<Map<String,Object>> selectProjectById(String Id){
        String sql="SELECT * FROM W_PROJECT WHERE ID = '"+Id+"'";
		return jdbcDao.queryForList(sql,null);
    }
    /**
     * Description : 根据型号ID查询项目办人员管理信息
     * Author : XYF
     * Date : 2018年10月19日下午2:05:08
     * Return : List<Map<String,Object>>
     */
    public List<Map<String,Object>> getProjectOffById(String Id){
        String sql="SELECT * FROM W_PROJECTOFFICE WHERE F_PRODUCT = '"+Id+"'";
		return jdbcDao.queryForList(sql,null);
    }
    
    /**
     * Description : 将型号名称保存至项目办人员管理
     * Author : XYF
     * Date : 2018年10月19日上午8:16:47
     * Return : void
     */
    public void saveProductName(String Id,String productName,String type){
    	String sql="UPDATE W_PROJECTOFFICE SET F_PRODUCTNAME ='"+productName+"' ,F_TYPE ='"+type+ "' WHERE ID = '"+Id+"'";
		jdbcTemplate.update(sql);
    }
    
    /** 查询某型号下所有记录（发次层） */
    public List<Map<String,Object>> selectAllFromproject(String prId){
    	
        String sql="SELECT * FROM W_PROJECT WHERE F_SSXH ='"+prId+"' ORDER BY F_TCPX ";
		return jdbcDao.queryForList(sql,null);
    }
    
	/** 查询tcpx为空（型号层） */
    public List<Map<String,Object>> selectNullTcpxproduct(){
    	
        String sql="SELECT * FROM W_PRODUCT WHERE F_TCPX is NULL ";
		return jdbcDao.queryForList(sql,null);
    }
    
    /** 查询tcpx为空（发次层） */
    public List<Map<String,Object>> selectNullTcpxproject(){
    	
        String sql="SELECT * FROM W_PROJECT WHERE F_TCPX is NULL ";
		return jdbcDao.queryForList(sql,null);
    }
	/**根据ID查询同层排序*/
	public String selectTcpxById(String id) {
		String sql="SELECT F_TCPX FROM W_PROJECT  WHERE ID = "+id;
	//	return jdbcTemplate.queryForObject(sql, String.class);
			String s=jdbcTemplate.queryForObject(sql, String.class);
		Map<String, Object>  ss= jdbcTemplate.queryForMap(sql);
		//Map<String, Object>  s = jdbcDao.queryForMap(sql, null);
		return s;
	}
	
	/**根据ID更新同层排序*/
	public void updateTcpxById(String id,String tcpx) {

		String sql="UPDATE W_PROJECT SET F_TCPX ="+tcpx+ " WHERE ID = '"+id+"'";
		jdbcTemplate.update(sql);
	}
	
	/**根据同层排序查询ID(型号层)*/
	public String selectIdByTcpxFromProduct(String tcpx) {

		String sql="SELECT ID FROM W_PRODUCT  WHERE F_TCPX = '"+tcpx+"' ";
		return jdbcTemplate.queryForObject(sql, String.class);
	}
	
	/**根据ID查询父节点ID(发次层)*/
	public String selectPridByIdFromProject(String Id) {

		String sql="SELECT F_SSXH FROM W_PROJECT  WHERE ID = '"+Id+"' ";
		return jdbcTemplate.queryForObject(sql, String.class);
	}
	
	
	/**根据fcID查询发次名*/
	public String selectNameByfcId(String fcId) {

		String sql="SELECT F_FCMC FROM W_PROJECT  WHERE ID = '"+fcId+"' ";
		return jdbcTemplate.queryForObject(sql, String.class);
	}
	/**根据ID查询同层排序(型号层)*/
	public String selectTcpxByIdFromProduct(String id) {
		String sql="SELECT F_TCPX FROM W_PRODUCT  WHERE ID = "+id;
	//	return jdbcTemplate.queryForObject(sql, String.class);
			String s=jdbcTemplate.queryForObject(sql, String.class);
		Map<String, Object>  ss= jdbcTemplate.queryForMap(sql);
		//Map<String, Object>  s = jdbcDao.queryForMap(sql, null);
		return s;
	}
	
	/**根据ID更新同层排序(型号层)*/
	public void updateTcpxByIdFromProduct(String id,String tcpx) {

		String sql="UPDATE W_PRODUCT SET F_TCPX ="+tcpx+ " WHERE ID = '"+id+"'";
		jdbcTemplate.update(sql);
	}
	
	/**根据同层排序更新ID*/
	public void updateIdByTcpx(String id,String tcpx) {

		String sql="UPDATE W_PROJECT SET ID ="+id+ " WHERE F_TCPX = '"+tcpx+"'";
		jdbcTemplate.update(sql);
	}
	
	/**根据父节点ID查询*/
	public List<Map<String,Object>> selectByParentId(String id) {
		String sql="SELECT * FROM W_PROJECT  WHERE F_SSXH = "+id ;
		List<Map<String,Object>> s = jdbcTemplate.queryForList(sql);
		return s;
	}
	
	/**根据父节点ID查询(型号层)*/
	public List<Map<String,Object>> selectByParentIdFromProduct() {
		String sql="SELECT * FROM W_PRODUCT ";
		List<Map<String,Object>> s = jdbcTemplate.queryForList(sql);
		return s;
	}
	
	
	/**
	 * @Author  shenguoliang
	 * @Description: 根据型号获取非普通节点的所有负责人
	 * @Params [fcId, userId]
	 * @Date 2018/5/19 10:01
	 * @Return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
	 */
	public List<Map<String, Object>>  getNodeChargerByProjectId(String fcId, Long userId) {

		String sql = "SELECT F_FZRID FROM W_PACKAGE WHERE F_JDLX != '普通分类节点' AND F_SSFC = "+fcId+"  " ;
		Map params = new HashMap();
		params.put("F_FCDH", fcId);
		List<Map<String, Object>> dataList = jdbcDao.queryForList(sql.toString(), null);
		return dataList ;
	}
	/**
	 * Description : 查询指定发次下非普通分类节点的ID
	 * Author : XYF
	 * Date : 2018年10月12日下午1:51:06
	 * Return : List<Map<String,Object>>
	 */
	public List<Map<String, Object>>  getNodeIdByProjectId(String fcId) {

		String sql = "SELECT ID FROM W_PACKAGE WHERE F_JDLX != '普通分类节点' AND F_SSFC = "+fcId+"  " ;
		Map params = new HashMap();
		params.put("F_FCDH", fcId);
		List<Map<String, Object>> dataList = jdbcDao.queryForList(sql.toString(), null);
		return dataList ;
	}
	/**
	 * Description : 根据节点ID查询工作队人员ID
	 * Author : XYF
	 * Date : 2018年10月12日下午2:04:04
	 * Return : List<Map<String,Object>>
	 */
	public List<Map<String, Object>>  getWorkTeamTeamPersonByProjectId(String NodeId) {

		String sql = "SELECT F_CYID FROM W_WORKTEAM WHERE F_SSSJB IN "+NodeId;
		Map params = new HashMap();
		params.put("NodeId", NodeId);
		List<Map<String, Object>> dataList = jdbcDao.queryForList(sql.toString(), null);
		return dataList ;
	}
	
	public String getCpMc(String planId) {

		/*String sql = "SELECT F_CPMC FROM W_CPB WHERE F_PLANID = "+planId;*/
		String sql="select A.*,B.* from W_CPB A ";
		sql=sql+"INNER  JOIN W_TB_INSTANT B ON A.F_SSSLID = B.ID where ";
		sql=sql+"A.F_PLANID = '" + planId + "' and  B.F_STATUS!='废弃'";
		Map params = new HashMap();
		params.put("planId", planId);
		List<Map<String, Object>> dataList = jdbcDao.queryForList(sql.toString(), null);
		String cpmc="";
		for (Map<String, Object> map : dataList) {
			cpmc+=map.get("F_CPMC")+",";
		}
		if(!cpmc.equals("")) {
			cpmc=cpmc.substring(0,cpmc.length()-1);
		}
		return cpmc ;
	}
	
	public String getCpNumber(String planId) {
		String sql="select A.*,B.* from W_CPB A ";
		sql=sql+"INNER  JOIN W_TB_INSTANT B ON A.F_SSSLID = B.ID where ";
		sql=sql+"A.F_PLANID = '" + planId + "' and  B.F_STATUS!='废弃'";
		Map params = new HashMap();
		params.put("planId", planId);
		List<Map<String, Object>> dataList = jdbcDao.queryForList(sql.toString(), null);
		return String.valueOf(dataList.size());
	}

	public List<Map<String,Object>> getProductsByPlanId(String planId){
		Map<String,Object> sqlMap=new HashMap<>();
		sqlMap.put("planId",planId);
		String sql="select * from W_CPB where F_PLANID=:planId";
		List<Map<String,Object>> products=jdbcDao.queryForList(sql,sqlMap);
		return products;
	}
}


