package com.cssrc.ibms.core.resources.product.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.bean.datapackageModel.ModelBean;
import com.cssrc.ibms.core.resources.product.bean.ModuleManage;
import com.cssrc.ibms.core.resources.product.util.SqlHelp;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.service.SysOrgService;
import com.cssrc.ibms.core.util.common.MapUtil;

import javassist.expr.NewArray;
/**
 * @description 型号管理员控制dao
 * @author fuyong
 * @date 2020年08月22日 下午3:53:26
 * @version V1.0
 */
@Repository
public class ModuleManageDao {
	
	
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private SysOrgService sysOrgService;
	
	/**
	 * @Desc 根据型号ID获取管理人员ID
	 * @param moduleId
	 * @return
	 */
	public List<String> getModuleManger(String moduleId,String type){
		StringBuffer sql=new StringBuffer();
		sql.append("select * from w_xhglryb where F_SSXHID='"+moduleId+"'");
		sql.append(" and F_QX='"+type+"'");
		List<Map<String, Object>> UserMapList=jdbcDao.queryForList(sql.toString(), null);
		List<String> list=new ArrayList<>();
		for (Map<String, Object> map : UserMapList) {
			list.add(map.get("F_RYID").toString());
		}
		return list;
	}
	/**
	 * @Desc 插入管理员数据
	 * @param moduleId,userid,fullName,type,
	 * @return
	 */
	public void  insert(String moduleId,String userid,String fullName,String type) {
		StringBuffer sql=new StringBuffer();
		Long id=UniqueIdUtil.genId();
		SysOrg sysorg=sysOrgService.getDefaultOrgByUserId(Long.valueOf(userid));
		sql.append("insert into w_xhglryb");
		sql.append("(ID,F_ssxhID,F_QX,F_RY,F_RYID,F_BM,F_BMID)");
		sql.append(" values(:ID,:F_ssxhId,:F_QX,:F_RY,:F_RYID,:F_BM,:F_BMID)");
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("F_ssxhId", moduleId);
		map.put("F_QX", type);
		map.put("F_RY", fullName);
		map.put("F_RYID", userid);
		map.put("ID", id);
		map.put("F_BM","");
		map.put("F_BMID", "");
		if(sysorg!=null) {
			map.put("F_BM", sysorg.getOrgName());
			map.put("F_BMID", sysorg.getOrgId());
		}

		jdbcDao.exesql(sql.toString(), map);
	}


	
	/**
	 * @Desc 通过moduleId获取人员
	 * @param moduleId
	 * @return
	 */
	public List<String>  getByModuleId(String moduleId,String type) {
		StringBuffer stringBuffer=new StringBuffer();
		stringBuffer.append("select * from w_xhglryb where F_ssxhID='"+moduleId+"'");
		stringBuffer.append(" and F_qx='"+type+"'");
		List<Map<String, Object>> managerList=jdbcDao.queryForList(stringBuffer.toString(),null);
		List<String> userList=new ArrayList<>();
		for (Map<String, Object> map : managerList) {
			userList.add(map.get("F_ryId").toString());
		}
		return userList;
	}
	
	/**
	 * @Desc 通过moduleId和权限种类获取人员
	 * @param moduleId
	 * @return
	 */
	public List<String>  getByModuleId(String moduleId) {
		StringBuffer stringBuffer=new StringBuffer();
		stringBuffer.append("select * from w_xhglryb where F_ssxhID='"+moduleId+"'");
		List<Map<String, Object>> managerList=jdbcDao.queryForList(stringBuffer.toString(),null);
		List<String> userList=new ArrayList<>();
		for (Map<String, Object> map : managerList) {
			userList.add(map.get("F_ryId").toString());
		}
		return userList;
	}
	/**
	 * @Desc 通过moduleId和权限种类获取人员去除重复
	 * @param moduleId
	 * @return
	 */
	public List<String>  getByModuleIdDic(String moduleId) {
		StringBuffer stringBuffer=new StringBuffer();
		stringBuffer.append("select DISTINCT F_RYID from w_xhglryb where F_ssxhID='"+moduleId+"'");
		List<Map<String, Object>> managerList=jdbcDao.queryForList(stringBuffer.toString(),null);
		List<String> userList=new ArrayList<>();
		for (Map<String, Object> map : managerList) {
			userList.add(map.get("F_ryId").toString());
		}
		return userList;
	}
	/**
	 * @Desc 通过moduleId和权限种类获取人员Bean
	 * @param moduleId
	 * @return
	 */
	public List<ModuleManage> getManageByModuleId(String moduleId){
		StringBuffer stringBuffer=new StringBuffer();
		stringBuffer.append("select * from w_xhglryb where F_ssxhID='"+moduleId+"'");
		List<Map<String, Object>> managerList=jdbcDao.queryForList(stringBuffer.toString(),null);
		List<ModuleManage> manageList=new ArrayList<>();
		for (Map<String, Object> map : managerList) {
			manageList.add(new ModuleManage(map));
		}
		return manageList;
	}
	/**
	 * @Desc 删除指定id数据
	 * @param id
	 * @return
	 */
	public void deleteById(String id) {
		String sql="delete from w_xhglryb where ID=:ID";
		Map<String, Object> paramMap=new HashMap<>();
		paramMap.put("ID", id);
		jdbcDao.exesql(sql, paramMap);
	}
	
	/**
	 * @Desc 通过id获取
	 * @param id
	 * @return
	 */
	public Map<String, Object> getManageById(String id){
		StringBuffer stringBuffer=new StringBuffer();
		stringBuffer.append("select * from w_xhglryb where ID='"+id+"'");
		Map<String, Object> managerMap=jdbcDao.queryForMap(stringBuffer.toString(),null);
		return managerMap;
	}
	/**
	 * @Desc 更新数据
	 * @param moduleManage
	 * @return
	 */
	public void update(ModuleManage moduleManage) {
		String  sql = SqlHelp.getUpdateSql(ModuleManage.class, "w_xhglryb");
		Map<String, Object> map = MapUtil.transBean2Map(moduleManage);
		jdbcDao.exesql(sql, map);
	}
	/**
	 * @Desc 插入数据
	 * @param moduleManage
	 * @return
	 */
	public void insert(ModuleManage moduleManage) {
		String  sql = SqlHelp.getInsertSql(ModuleManage.class, "w_xhglryb");
		Map<String, Object> map = MapUtil.transBean2Map(moduleManage);
		jdbcDao.exesql(sql, map);
	}
}
