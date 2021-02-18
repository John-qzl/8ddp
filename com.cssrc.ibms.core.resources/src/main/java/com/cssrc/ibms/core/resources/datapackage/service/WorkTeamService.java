package com.cssrc.ibms.core.resources.datapackage.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.datapackage.dao.DataPackageDao;
import com.cssrc.ibms.core.resources.datapackage.dao.PackageDao;
import com.cssrc.ibms.core.resources.datapackage.dao.TeamDao;

import net.sf.json.JSONObject;

import java.util.List;

@Service
public class WorkTeamService {
	@Resource
	private DataPackageDao dataPackageDao;
	@Resource
	private PackageDao packageDao;
	@Resource
	private TeamDao teamDao;
	
	/**
	 * @param buttonName :要校验的按钮名称
	 * @param sssjb ： 所属数据包
	 * @param ids ： 要校验的记录主键（123,2342,...）
	 * @return
	 */
	public JSONObject getButtonRight(String buttonName,Long sssjb,String ids) {
		JSONObject rtn = new JSONObject();
		rtn.put("isCan", true);
		switch(buttonName) {
		case "add":
			getAddRight(rtn,sssjb);
			break;
		case "del":
			getDeleteRight(rtn,sssjb,ids);
			break;
		case "edit":
			getEditRight(rtn,sssjb,ids);
			break;
		}
		return rtn;
	}
	/**
	 * 添加、批量添加按钮权限
	 * @param rtn
	 */
	private void getAddRight(JSONObject rtn,Long sssjb) {
		Long userId = UserContextUtil.getCurrentUser().getUserId();
		boolean isNodeMan = packageDao.isNodeMan(userId,sssjb);
		if(isNodeMan) {return;}
		boolean isTeamPerson = teamDao.isBelongToTeam(userId,sssjb);
		if(!isTeamPerson) {
			rtn.put("isCan", false);
			rtn.put("message", "您不在工作队中，无法添加记录！");
		}
	}
	/**
	 * 删除按钮权限
	 * @param rtn
	 */
	private void getDeleteRight(JSONObject rtn,Long sssjb,String ids) {
		Long userId = UserContextUtil.getCurrentUser().getUserId();
		boolean isNodeMan = packageDao.isNodeMan(userId,sssjb);
		if(isNodeMan) {return;}
		boolean isTeamPerson = teamDao.isAllWorkTeamer(userId, ids);
		if(!isTeamPerson&&!isNodeMan) {
			rtn.put("isCan", false);
			rtn.put("message", "所选信息中有不属于本人工作队的，请重新选择！");
		}
	}
	/**
	 * 编辑按钮权限
	 * @param rtn
	 */
	private void getEditRight(JSONObject rtn,Long sssjb,String ids) {
		Long userId = UserContextUtil.getCurrentUser().getUserId();
		boolean isNodeMan = packageDao.isNodeMan(userId,sssjb);
		if(isNodeMan) {return;}
		boolean isTeamPerson = teamDao.isAllWorkTeamer(userId,ids);
		if(!isTeamPerson&&!isNodeMan) {
			rtn.put("isCan", false);
			rtn.put("message", "您不是本工作队人员，无法编辑！");
		}
	}


	/**
	 * @Author  shenguoliang
	 * @Description: 查询工作队是否在数据包实例中被使用，返回已使用的工作队名称
	 * @Params [sssjb, ids]
	 * @Date 2018/6/27 15:44
	 * @Return net.sf.json.JSONObject
	 */
	public List<String>  checkWorkTeamIfUsed(Long sssjb, String ids) {
		List<String> usedteamList = teamDao.checkWorkTeamIfUsed(sssjb, ids);
		return usedteamList;
	}
}
