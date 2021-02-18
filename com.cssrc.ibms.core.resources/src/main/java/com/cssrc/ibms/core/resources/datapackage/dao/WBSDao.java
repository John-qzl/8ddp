package com.cssrc.ibms.core.resources.datapackage.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.datapackage.model.WorkPlan;
import com.cssrc.ibms.core.util.common.CommonTools;


/**
 * @author user
 * 甘特图相关的数据库操作
 */
@Repository
public class WBSDao extends JdbcDaoSupport{
	@Resource
	private JdbcDao jdbcDao;
	
	/**
	 * 获取指定数据包节点下的甘特图数据
	 */
	public List<WorkPlan> getDataInSjbID(Map<String,Object> parms) {
		String sql="SELECT * FROM W_TASK WHERE F_SSSJBJD=:F_SSSJBJD";
		List<Map<String,Object>> ress=jdbcDao.queryForList(sql, parms);
		List<WorkPlan> workPlans=new ArrayList<WorkPlan>();
		WorkPlan workPlan=null;
		for(Map<String,Object> res:ress){
			workPlan=new WorkPlan(res);
			workPlans.add(workPlan);
		}
		return workPlans;
	}

	public String getSubTaskBh(String sjbId, String curNodeId) {
		String sql="SELECT MAX(F_XH) as MAXORDER FROM W_TASK WHERE F_SSFSYRW=:curNodeId AND F_SSSJBJD=:sjbId";
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("sjbId", sjbId);
		params.put("curNodeId", curNodeId);
		Map<String,Object> res= jdbcDao.queryForMap(sql, params);
		return CommonTools.Obj2String(res.get("MAXORDER"));
	}

	public boolean updateTaskOrder(List<String> insertList) {
		Connection connection = null;
		Statement sta = null;
		try {
			// 获取数据库连接
			connection = this.getConnection();
			// 关闭自动提交
			connection.setAutoCommit(false);
			sta = connection.createStatement();
			for (String insertSql : insertList) {
				sta.addBatch(insertSql);
			}
			int []upDataNum = sta.executeBatch();
			//System.out.println("更新数据条数：" + upDataNum);
			// 提交数据
			connection.commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			try {
				sta.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
