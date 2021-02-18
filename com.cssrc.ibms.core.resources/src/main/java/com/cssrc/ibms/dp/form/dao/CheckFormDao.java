package com.cssrc.ibms.dp.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.dp.form.model.CheckForm;

@Repository
public class CheckFormDao  extends BaseDao<CheckForm>{
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass() {
		return CheckForm.class;
	}
	
	/**
	 * 表单ID是否存在
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Integer isIdExists(String id) {
		Map params = new HashMap();
		params.put("snum", id);
		return (Integer) getOne("isIdExists", params);
	}
	
	/**
	 * @param Id
	 * @return 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CheckForm getById(Long Id){
		Map map = new HashMap();
		map.put("Id", Id);
		return (CheckForm)this.getOne("getById", map);
	}
	public Map<String,Object> getByIdM(Long id){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_TABLE_TEMP where ID=:ID");
		Map params = new HashMap();
		params.put("ID", id);
		return jdbcDao.queryForMap(sql.toString(), params);
	}
	/**
	 * Description : 通过ID查找对应的执行状态
	 * Author : XYF
	 * Date : 2018年8月14日上午9:17:36
	 * Return : String
	 */
	public String selectZxztById(String id){
		String sql="SELECT F_ZXZT FROM W_DATAPACKAGEINFO  WHERE ID = '"+id+"'";
		String state = jdbcTemplate.queryForObject(sql, String.class);
		return state;
	}
	/**
	 * Description : 通过ID查找该ID下的岗位
	 * Author : XYF
	 * Date : 2018年8月14日上午9:31:36
	 * Return : String
	 */
	public String selectGwById(String dataId){
		String sql="SELECT F_GW FROM W_DATAPACKAGEINFO  WHERE ID = '"+dataId+"'";
		String gw = jdbcTemplate.queryForObject(sql, String.class);
		return gw;
	}
	
	/**
	 * Description : 查找某一发次某一文件夹下是否有指定name的模版
	 * Author : XYF
	 * Date : 2018年9月14日上午10:36:05
	 * Return : List<Map<String,Object>>
	 */
	public List<Map<String,Object>> selectTemplates(Long fcId,Long fileId,String name){
    	String sql="SELECT * FROM W_TABLE_TEMP where F_PROJECT_ID = "+fcId+" AND F_TEMP_FILE_ID="+fileId+" AND F_NAME = '"+name+"'";
    	List<Map<String,Object>> Templates= jdbcDao.queryForList(sql,null);
		return Templates;
    }
	
	/**
	 * Description : 根据ID查找表单模版
	 * Author : XYF
	 * Date : 2018年10月18日上午11:19:09
	 * Return : List<Map<String,Object>>
	 */
	public List<Map<String,Object>> selectTableTempById(String Id){
    	String sql="SELECT * FROM W_TABLE_TEMP where ID = '"+Id+"'";
    	List<Map<String,Object>> Templates= jdbcDao.queryForList(sql,null);
		return Templates;
    }
	
	/**
	 * Description : 保存表单模版的创建人和创建时间
	 * Author : XYF
	 * Date : 2018年10月18日上午11:26:36
	 * Return : void
	 */
	public void saveCreateInformation(String Id,String userId,String time){
    	String sql="UPDATE W_TABLE_TEMP SET F_CREATE_ID ='"+userId+"' , F_CREATE_TIME ="+time+" WHERE ID = '"+Id+"'";
		jdbcTemplate.update(sql);
    }
	/**
	 * Description : 通过ID寻找该工作队下的所有用户ID
	 * Author : XYF
	 * Date : 2018年8月14日上午9:37:53
	 * Return : String
	 */
	public String selectWorkTeamById(String gw){
		String sql="SELECT F_CYID FROM W_WORKTEAM  WHERE ID = '"+gw+"'";
		String workteam = jdbcTemplate.queryForObject(sql, String.class);
		return workteam;
	}
	
	/**
	 * Description : 在W_DATAPACKAGEINFO表中通过ID查找所属数据包
	 * Author : XYF
	 * Date : 2018年8月15日上午10:14:40
	 * Return : String
	 */
	public String selectSssgbById(String dataId){
		String sql="SELECT F_SSSJB FROM W_DATAPACKAGEINFO  WHERE ID = '"+dataId+"'";
		String sssgb = jdbcTemplate.queryForObject(sql, String.class);
		return sssgb;
	}
	
	/**
	 * Description :  在W_PACKAGE表中通过ID查找F_FZRID
	 * Author : XYF
	 * Date : 2018年8月15日上午10:14:24
	 * Return : String
	 */
	public String selectFzrIdById(String Id){
		String sql="SELECT F_FZRID FROM W_PACKAGE  WHERE ID = '"+Id+"'";
		String fzr = jdbcTemplate.queryForObject(sql, String.class);
		return fzr;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteTemp(String id){
		Map params = new HashMap();
		params.put("ID", id);
		delItemByID("deleteById",params);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Integer isNumberExistsByTabelTempId(String snum, String mid) {
		String sql="SELECT count(*) FROM W_TABLE_TEMP WHERE F_NUMBER='"+snum+"' AND ID="+mid;
		return jdbcTemplate.queryForInt(sql);
	}
	
}
