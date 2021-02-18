package com.cssrc.ibms.core.resources.datapackage.dao;

import java.util.*;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.io.bean.TestTeam;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * @author user
 * 工作对表操作类
 */
@Repository
public class TeamDao {
	@Resource
	private JdbcDao jdbcDao;
	
	/**
	 * 当前用户将是否对ids的所有工作队的共有人员
	 * @param userId
	 * @param ids
	 * @return
	 */
	public boolean isAllWorkTeamer(Long userId,String ids) {
		int pkNum = ids.split(",").length;
		ids = ids.replace(",", "','");
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) from W_WORKTEAM where  F_CYID like '%").append(userId).append("%'");
		sql.append(" and id in ('").append(ids).append("')");
		int recordNum = jdbcDao.queryForInt(sql.toString(), null);
		return pkNum==recordNum?true:false;
	}
	/**
	 * 判断当前用户是否在数据包节点的工作队中
	 * @param userId : 
	 * @param sssjb ： 所属数据包节点ID
	 * @return 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean isBelongToTeam(Long userId,Long sssjb) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*) from W_WORKTEAM ");
		sql.append(" where F_SSSJB =:sssjb");
		sql.append(" and F_CYID||',' like '%" + userId + ",%'");
		Map map = new HashMap();
		map.put("sssjb", sssjb);
		int count = jdbcDao.queryForInt(sql.toString(), map);
		return count>0;
	}
	/**
	 * 判断当前用户所选择的记录是否都在 数据包节点的工作队中
	 * @param userId
	 * @param sssjb： 所属数据包节点ID
	 * @param ids ：数据包详细信息主键（11,222,33,...）
	 * @return
	 */
	public boolean isBelongToTeam(Long userId,Long sssjb,String ids) {
		boolean flag = true;
		if(ids.contains(",")) {
			String[] idArr = ids.split(",");
			for(String id : idArr) {
				flag = flag&&isBelongToTeam(userId,id);
				if(!flag) {
					break;
				}
			}
		}
		return flag;
	}
	/**
	 * 判断当前用户所选择的记录是否都在 数据包节点的工作队中
	 * @param userId
	 * @param sssjb： 所属数据包节点ID
	 * @param id : 数据包详情主键
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean isBelongToTeam(Long userId,String id) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*) from W_WORKTEAM ");
		sql.append(" where F_CYID||',' like '%"+userId+",%' ");
		sql.append(" and id in ( ");
		sql.append(" 	select F_GW from W_DATAPACKAGEINFO where id  =:id ");
		sql.append(" ) ");
		Map map = new HashMap();
		map.put("id", id);
		int count = jdbcDao.queryForInt(sql.toString(), map);
		return count>0;
	}
	/**
	 * 根据主键获取工作队信息
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String,Object> getById(Long id){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_WORKTEAM where ID=:ID");
		Map params = new HashMap();
		params.put("ID", id);
		return jdbcDao.queryForMap(sql.toString(), params);
	}
	/**
	 * 查询
	 * @param keyValueMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<Map<String,Object>> query(Map<String,Object> keyValueMap){
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from W_WORKTEAM where 1=1 ");
		Set keys = keyValueMap.keySet();
		for(Iterator it = keys.iterator();it.hasNext();) {
			String key = (String)it.next();
			Object value = keyValueMap.get(key);
			sql.append(" and ").append(key).append(" = '").append(value).append("'");
		}
		return jdbcDao.queryForList(sql.toString(), null);
	}
	public void insert(TestTeam team) {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into W_WORKTEAM (ID, F_GZDMC, F_CY, F_CYID, F_SSSJB)");
		sql.append(" values (:ID, :F_GZDMC, :F_CY, :F_CYID, :F_SSSJB)");
		Map map = new HashMap();
		map.put("ID", team.getId());
		map.put("F_GZDMC", team.getGzdmc());
		map.put("F_CY", team.getCy());
		map.put("F_CYID", team.getCyID());
		map.put("F_SSSJB", team.getSssjb());
		jdbcDao.exesql(sql.toString(), map);
	}
	public void deleteByPackage(String packageId) {
		String sql = "delete from W_WORKTEAM where F_SSSJB=:F_SSSJB";
		Map<String,Object> parameter = new HashMap();
		parameter.put("F_SSSJB", packageId);
		jdbcDao.exesql(sql, parameter);	

	}
	public void deleteById(String id) {
		String sql = "delete from W_WORKTEAM where ID=:ID";
		Map<String,Object> parameter = new HashMap();
		parameter.put("ID", id);
		jdbcDao.exesql(sql, parameter);

	}

	/**
	 * @Author  shenguoliang
	 * @Description: 返回使用的工作队名称，同时删除未使用的被选择删除的工作队
	 * @Params [sssjb, ids]
	 * @Date 2018/6/28 8:30
	 * @Return java.util.List<java.lang.String>
	 */
	public List<String> checkWorkTeamIfUsed(Long sssjb, String ids) {
		List<String> usedteamList = new ArrayList<>();
		boolean flag = true;
		List<String> idArr = new ArrayList<>();
		if (ids.contains(",")) {
			idArr = Arrays.asList(ids.split(","));
		}else {
			idArr.add(ids);
		}

		for (String id : idArr) {
			flag = queryPackageInfoForTeam(sssjb, id);
			if (flag) {
				//根据工作队的ID进行相关的
				Map map = this.getById(Long.valueOf(id));
				usedteamList.add(CommonTools.Obj2String(map.get("F_GZDMC")));
			}else{
				this.deleteById(id);
			}
		}
		return usedteamList;

	}
	/**
	 * @Author  shenguoliang
	 * @Description: 根据数据包ID 以及 工作队ID 获取数据包实例信息数量
	 * @Params [sssjb, id]
	 * @Date 2018/6/28 8:29
	 * @Return boolean
	 */
	public boolean queryPackageInfoForTeam(Long sssjb, String id) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(*) from W_DATAPACKAGEINFO where ");
		sql.append(" F_GW in ( ");
		sql.append(" select id from W_WORKTEAM WHERE F_SSSJB = :F_SSSJB");
		sql.append(" and id = :ID ");
		sql.append(" ) ");
		Map map = new HashMap();
		map.put("F_SSSJB", sssjb);
		map.put("ID",id);
		int count = jdbcDao.queryForInt(sql.toString(), map);
		return count>0;
	}

}
