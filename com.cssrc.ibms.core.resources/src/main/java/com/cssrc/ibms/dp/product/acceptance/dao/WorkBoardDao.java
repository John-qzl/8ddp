package com.cssrc.ibms.dp.product.acceptance.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.solr.common.util.Hash;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.product.util.SqlHelp;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.common.MapUtil;
import com.cssrc.ibms.dp.product.acceptance.bean.AcceptanceGroup;
import com.cssrc.ibms.dp.product.acceptance.bean.WorkBoard;
/**
 * @description 工作项看板数据库操作类
 * @author fu yong
 * W_GZXKB
 * @date 2020年7月9日 下午3:00:54
 * @version V1.0
 */
@Repository
public class WorkBoardDao {
	@Resource
	private JdbcDao jdbcDao;
	
	
	public void insertwork(Map<String,Object> map) {
		String planId=map.get("ID").toString();
		String planName=map.get("F_chbgbbh").toString();
		Long Id=UniqueIdUtil.genId();
		String sql="insert into w_gzxkb values('"+Id+"','"+planId+"',"+"'1','"+planName+"策划','策划通过','下发表单到PAD','"+map.get("F_yszzId").toString()+"','"+map.get("F_yszz").toString()+"','"+map.get("F_JSFZR").toString()+"','"+map.get("F_JSFZRID").toString()+"'"+")";
		jdbcDao.exesql(sql, null);
	}
	public void  updatework(String acceptancePlanId,String firstString,String EndString) {
		String sql="update w_gzxkb set F_DQZT='"+firstString+"',F_XYB='"+EndString+"' where F_ZJID='"+acceptancePlanId+"'";
		jdbcDao.exesql(sql, null);
		
	}
	
	public WorkBoard getByPlanId(String planId) {
		String sql="select * from w_gzxkb where F_ZJID='"+planId+"'";
		Map<String,Object> map=jdbcDao.queryForMap(sql, null);
		WorkBoard workBoard;
		if(map!=null) {
			workBoard=new WorkBoard(map);
			return workBoard;
		}
		return null;
	}
	public void insert(WorkBoard workBoard) {
		String  sql = SqlHelp.getInsertSql(WorkBoard.class, "w_gzxkb");
		Map<String, Object> map = MapUtil.transBean2Map(workBoard);
		jdbcDao.exesql(sql, map);
	}
	public void update(WorkBoard workBoard) {
		String  sql = SqlHelp.getUpdateSql(WorkBoard.class, "w_gzxkb");
		Map<String, Object> map = MapUtil.transBean2Map(workBoard);
		jdbcDao.exesql(sql, map);
	}

	/**
	 * 保存工作板信息
	 * by zmz 20200827
	 *
	 * @param id
	 * @return
	 */
	public WorkBoard selectById(String id) {
		String sql="select * from w_gzxkb where ID="+id;
		Map<String,Object> map=jdbcDao.queryForMap(sql, null);
		WorkBoard workBoard;
		if(map!=null) {
			workBoard=new WorkBoard(map);
			return workBoard;
		}
		return null;
	}
	public int getByCurrentNumber() {
		String currentUserId=CommonTools.Obj2String(UserContextUtil.getCurrentUserId());
		Map<String, Object> param=new HashMap<String, Object>();
		List<Map<String, Object>> list=new ArrayList<>();
		String sql="select * from W_GZXKB where F_ZZID=:zzID and F_XYB!='任务结束'";
		param.put("zzID", currentUserId);
		list=jdbcDao.queryForList(sql, param);
		return list.size();
	}
}
