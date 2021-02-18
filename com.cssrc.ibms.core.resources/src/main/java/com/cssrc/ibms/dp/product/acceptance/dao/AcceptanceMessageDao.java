package com.cssrc.ibms.dp.product.acceptance.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.dp.product.acceptance.bean.AcceptanceGroup;
import com.fr.data.core.db.dml.Where;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.core.intf.IGenericService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.util.date.DateUtil;

/**
 * @description 产品验收组数据库操作类
 * @author xie chen
 * @date 2019年12月11日 下午7:04:54
 * @version V1.0
 */
@Repository
public class AcceptanceMessageDao {
	
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * @Desc 验收策划流程结束组员添加消息
	 * @param
	 */
	public void insertMessageByPlan(Map<String,Object> userInfo,Map<String,Object> dataInfo) {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into W_LCDZJTXXB (ID,F_xxid,F_zl,F_zw,F_xxzt,F_xxmc,F_xxjsr,F_xxjsrID,F_JSSJ,F_SFYD)");
		sql.append(" values (:ID,:F_xxid,:F_zl,:F_zw,:F_xxzt,:F_xxmc,:F_xxjsr,:F_xxjsrID,:F_JSSJ,:F_SFYD)");
		Map map = new HashMap();
		Long Id=UniqueIdUtil.genId();
		map.put("ID", Id);
		map.put("F_zw", userInfo.get("F_zw"));
		map.put("F_xxid", dataInfo.get("ID"));
		map.put("F_zl", "验收策划");
		map.put("F_xxzt", "审批通过");
		map.put("F_xxmc", dataInfo.get("F_CHBGBBH").toString()+"-"+dataInfo.get("F_CPMC")+"验收策划");
		map.put("F_xxjsr", userInfo.get("F_XM"));
		map.put("F_xxjsrID", userInfo.get("F_XMID"));
		java.util.Date a = new java.util.Date();
		java.sql.Timestamp d = new java.sql.Timestamp(a.getTime());
		map.put("F_JSSJ", d);
		map.put("F_SFYD", "0");
		jdbcDao.exesql(sql.toString(), map);
	}
	/**
	 * @Desc  归档流程结束组员添加消息
	 * @param
	 */
	public void insertMessageByArchive(Map<String,Object> userInfo,Map<String,Object> dataInfo) {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into W_LCDZJTXXB (ID,F_xxid,F_zl,F_zw,F_xxzt,F_xxmc,F_xxjsr,F_xxjsrID,F_JSSJ,F_SFYD)");
		sql.append(" values (:ID,:F_xxid,:F_zl,:F_zw,:F_xxzt,:F_xxmc,:F_xxjsr,:F_xxjsrID,:F_JSSJ,:F_SFYD)");
		Map map = new HashMap();
		Long Id=UniqueIdUtil.genId();
		map.put("ID", Id);
		map.put("F_zw", userInfo.get("F_zw"));
		map.put("F_xxid", dataInfo.get("ID"));
		map.put("F_zl", "验收策划");
		map.put("F_xxzt", "已归档");
		map.put("F_xxmc", dataInfo.get("F_CHBGBBH").toString()+"-"+dataInfo.get("F_CPMC")+"归档");
		map.put("F_xxjsr", userInfo.get("F_XM"));
		map.put("F_xxjsrID", userInfo.get("F_XMID"));
		java.util.Date a = new java.util.Date();
		java.sql.Timestamp d = new java.sql.Timestamp(a.getTime());
		map.put("F_JSSJ", d);
		map.put("F_SFYD", "0");
		jdbcDao.exesql(sql.toString(), map);
	}
	
	/**
	 * @Desc 验收报告流程结束组员添加消息
	 * @param
	 */
	public void insertMessageByReport(Map<String,Object> userInfo,Map<String,Object> dataInfo) {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into W_LCDZJTXXB (ID,F_xxid,F_zl,F_zw,F_xxzt,F_xxmc,F_xxjsr,F_xxjsrID,F_JSSJ,F_SFYD)");
		sql.append(" values (:ID,:F_xxid,:F_zl,:F_zw,:F_xxzt,:F_xxmc,:F_xxjsr,:F_xxjsrID,:F_JSSJ,:F_SFYD)");
		Map map = new HashMap();
		Long Id=UniqueIdUtil.genId();
		map.put("ID", Id);
		map.put("F_zw", userInfo.get("F_zw"));
		map.put("F_xxid", dataInfo.get("ID"));
		map.put("F_zl", "验收报告");
		map.put("F_xxzt", "验收通过");
		map.put("F_xxmc",  dataInfo.get("F_BGBBH").toString()+"-"+dataInfo.get("F_CPMC")+"验收报告");
		map.put("F_xxjsr", userInfo.get("F_XM"));
		map.put("F_xxjsrID", userInfo.get("F_XMID"));
		java.util.Date a = new java.util.Date();
		java.sql.Timestamp d = new java.sql.Timestamp(a.getTime());
		map.put("F_JSSJ", d);
		map.put("F_SFYD", "0");
		jdbcDao.exesql(sql.toString(), map);
	}
	/**
	 * @Desc 获取当前用户消息
	 * @param
	 */
	public List<Map<String,Object>> getUserMessage() {
		SysUser sysUser=(SysUser) UserContextUtil.getCurrentUser();
		String sql="SELECT * FROM W_LCDZJTXXB WHERE F_XXJSRID = '"+sysUser.getUserId()+"' and F_SFYD=0 order by ID desc";
		List<Map<String,Object>> acceptanceMessgeList=jdbcDao.queryForList(sql, null);
		return acceptanceMessgeList;
	}

	/**
	 * @Desc 获取当前用户消息不返回时间类型
	 * @param
	 */
	public List<Map<String,Object>> getUserMessageByDate() {
		SysUser sysUser=(SysUser) UserContextUtil.getCurrentUser();
		String sql="SELECT ID,F_XXID,F_XXBDKEY,F_XXZT,F_XXMC,F_XXJSR,F_XXJSRID,F_ZW,F_ZL,to_char(F_JSSJ,'YYYY-MM-dd') F_JSSJ,F_SFYD FROM W_LCDZJTXXB WHERE F_XXJSRID = '"+sysUser.getUserId()+"' order by ID desc";
		List<Map<String,Object>> acceptanceMessgeList=jdbcDao.queryForList(sql, null);
		return acceptanceMessgeList;
	}
	/**
	 * @Desc 获取当前用户消息不返回时间类型
	 * @param
	 */
	public List<Map<String,Object>> getUserMessageByRead(String check) {
		String sql="";
		SysUser sysUser=(SysUser) UserContextUtil.getCurrentUser();
		if("1".equals(check)) { //已读消息
			sql="SELECT ID,F_XXID,F_XXBDKEY,F_XXZT,F_XXMC,F_XXJSR,F_XXJSRID,F_ZW,F_ZL,to_char(F_JSSJ,'YYYY-MM-dd') F_JSSJ,F_SFYD FROM W_LCDZJTXXB WHERE F_XXJSRID = '"+sysUser.getUserId()+"' and F_SFYD!=0 order by ID desc";
		}
		else {
			sql="SELECT ID,F_XXID,F_XXBDKEY,F_XXZT,F_XXMC,F_XXJSR,F_XXJSRID,F_ZW,F_ZL,to_char(F_JSSJ,'YYYY-MM-dd') F_JSSJ,F_SFYD FROM W_LCDZJTXXB WHERE F_XXJSRID = '"+sysUser.getUserId()+"' and F_SFYD=0 order by ID desc";
		}
		
		List<Map<String,Object>> acceptanceMessgeList=jdbcDao.queryForList(sql, null);
		return acceptanceMessgeList;
	}
	/**
	 * 靶场策划
	 * 通知这个审批通过了
	 */
	public void insertMessageByRangeTestPlan(SysUser userInfo, Map<String,Object> dataInfo,String role) {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into W_LCDZJTXXB (ID,F_xxid,F_zl,F_zw,F_xxzt,F_xxmc,F_xxjsr,F_xxjsrID,F_JSSJ)");
		sql.append(" values (:ID,:F_xxid,:F_zl,:F_zw,:F_xxzt,:F_xxmc,:F_xxjsr,:F_xxjsrID,:F_JSSJ)");
		Map map = new HashMap();
		Long Id=UniqueIdUtil.genId();
		//userInfo要改成和model相同的取值,而不是原来的map形式
		//rangeTestPlan取相应的信息,
		map.put("ID", Id);
		map.put("F_zw", role);
		map.put("F_xxid", dataInfo.get("ID"));
		if (dataInfo.get("F_CHBGBBH").toString().indexOf("WQSJ")!=-1){
			//是武器所检
			map.put("F_zl", "武器所检策划");
			map.put("F_xxmc",  dataInfo.get("F_CHBGBBH").toString()+"-"+dataInfo.get("F_SYRWMC")+"武器系统所检策划");
		}else {
			//是靶场
			map.put("F_zl", "靶场试验策划");
			map.put("F_xxmc",  dataInfo.get("F_CHBGBBH").toString()+"-"+dataInfo.get("F_SYRWMC")+"靶场试验策划");
		}
		map.put("F_xxzt", "审批通过");

		map.put("F_xxjsr", userInfo.getFullname());
		map.put("F_xxjsrID", userInfo.getUserId());
		java.util.Date a = new java.util.Date();
		java.sql.Timestamp d = new java.sql.Timestamp(a.getTime());
		map.put("F_JSSJ", d);

		jdbcDao.exesql(sql.toString(), map);
	}

	/**
	 * 靶场策划
	 * 通知这个审批通过了
	 */
	public void insertMessageByRangeTestSummary(SysUser userInfo, Map<String,Object> dataInfo,String role) {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into W_LCDZJTXXB (ID,F_xxid,F_zl,F_zw,F_xxzt,F_xxmc,F_xxjsr,F_xxjsrID,F_JSSJ)");
		sql.append(" values (:ID,:F_xxid,:F_zl,:F_zw,:F_xxzt,:F_xxmc,:F_xxjsr,:F_xxjsrID,:F_JSSJ)");
		Map map = new HashMap();
		Long Id=UniqueIdUtil.genId();
		//userInfo要改成和model相同的取值,而不是原来的map形式
		//rangeTestPlan取相应的信息,
		map.put("ID", Id);
		map.put("F_zw", role);
		map.put("F_xxid", dataInfo.get("ID"));
		if (dataInfo.get("F_BCSYBH").toString().indexOf("WQSJ")!=-1){
			//是武器所检
			map.put("F_zl", "所检数据确认");
		}else {
			//是靶场
			map.put("F_zl", "靶场数据确认");
		}

		map.put("F_xxzt", "审批通过");
		map.put("F_xxmc",  dataInfo.get("F_BCSYBH").toString()+dataInfo.get("F_SYJD")+"-数据确认");
		map.put("F_xxjsr", userInfo.getFullname());
		map.put("F_xxjsrID", userInfo.getUserId());
		java.util.Date a = new java.util.Date();
		java.sql.Timestamp d = new java.sql.Timestamp(a.getTime());
		map.put("F_JSSJ", d);
		jdbcDao.exesql(sql.toString(), map);
	}
	/**
	 * @Description  服务器下发表单的时候发消息
	 * @Author ZMZ
	 * @Date 2020/12/7 9:00
	 * @param userInfo 接收人的SysUser
	 * @param planId ID->策划id
	 * @param planId planTitle 策划编号和标题(中间用横线链接
	 * @param role 接收人的角色
	 * @Return void
	 */
	public void insertMessageWhenServerExportZip(SysUser userInfo,String planId,String planTitle,String role) {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into W_LCDZJTXXB (ID,F_xxid,F_zl,F_zw,F_xxzt,F_xxmc,F_xxjsr,F_xxjsrID,F_JSSJ,F_SFYD)");
		sql.append(" values (:ID,:F_xxid,:F_zl,:F_zw,:F_xxzt,:F_xxmc,:F_xxjsr,:F_xxjsrID,:F_JSSJ,:F_SFYD)");
		Map map = new HashMap();
		Long Id=UniqueIdUtil.genId();
		map.put("ID", Id);
		map.put("F_zw", role);
		map.put("F_xxid", planId);
		map.put("F_zl", "服务器生成压缩包");
		map.put("F_xxzt", "表单下发完成");
		map.put("F_xxmc",  planTitle);
		map.put("F_xxjsr", userInfo.getFullname());
		map.put("F_xxjsrID", userInfo.getUserId());
		java.util.Date a = new java.util.Date();
		java.sql.Timestamp d = new java.sql.Timestamp(a.getTime());
		map.put("F_JSSJ", d);
		map.put("F_SFYD", "0");
		jdbcDao.exesql(sql.toString(), map);
	}

	/**
	 * @Description  通用消息消息
	 * @Author ZMZ
	 * @Date 2020/12/7 9:00
	 * @param userInfo 接收人的SysUser
	 * @param planId ID->策划id
	 * @param planId planTitle 策划编号和标题(中间用横线链接
	 * @param title 消息概要
	 * @param role 接收人的角色
	 * @Return void
	 */
	public void insertMessageForCommon(SysUser userInfo,String planId,String planTitle,String title,String role) {
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into W_LCDZJTXXB (ID,F_xxid,F_zl,F_zw,F_xxzt,F_xxmc,F_xxjsr,F_xxjsrID,F_JSSJ,F_SFYD)");
		sql.append(" values (:ID,:F_xxid,:F_zl,:F_zw,:F_xxzt,:F_xxmc,:F_xxjsr,:F_xxjsrID,:F_JSSJ,:F_SFYD)");
		Map map = new HashMap();
		Long Id=UniqueIdUtil.genId();
		map.put("ID", Id);
		map.put("F_zw", role);
		map.put("F_xxid", planId);
		map.put("F_zl", "通用消息");
		map.put("F_xxzt", title);
		map.put("F_xxmc",  planTitle);
		map.put("F_xxjsr", userInfo.getFullname());
		map.put("F_xxjsrID", userInfo.getUserId());
		java.util.Date a = new java.util.Date();
		java.sql.Timestamp d = new java.sql.Timestamp(a.getTime());
		map.put("F_JSSJ", d);
		map.put("F_SFYD", "0");
		jdbcDao.exesql(sql.toString(), map);
	}
	/**
	 * 主页消息设置消息已读
	 * 
	 */
	public void setMessageStatus(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append("update W_LCDZJTXXB set F_SFYD='1'");
		sql.append(" where ID=:id");
		Map<String,Object> map=new HashMap<>();
		map.put("id", id);
		jdbcDao.exesql(sql.toString(), map);
	}

}


