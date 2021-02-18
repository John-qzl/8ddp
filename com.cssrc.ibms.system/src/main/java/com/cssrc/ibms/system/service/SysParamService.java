package com.cssrc.ibms.system.service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.system.intf.ISysParamService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.system.dao.SysParamDao;
import com.cssrc.ibms.system.model.SysParam;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Service;
/**
 * 
 * <p>Title:SysParamService</p>
 * @author Yangbo 
 * @date 2016-8-3上午09:53:55
 */
@Service
public class SysParamService extends BaseService<SysParam> implements ISysParamService{

	@Resource
	private SysParamDao dao;

	protected IEntityDao<SysParam, Long> getEntityDao() {
		return this.dao;
	}

	public List<SysParam> getStatusParam() {
		return this.dao.getUserParam();
	}

	public List<SysParam> getUserParam() {
		return this.dao.getUserParam();
	}

	public List<SysParam> getOrgParam(long demId) {
		return this.dao.getOrgParam(demId);
	}

	public List<SysParam> getOrgParam() {
		return this.dao.getOrgParam();
	}

	private static String getIconPath(int type) {
		String path = "";
		switch (type) {
		case 1:
			path = "/themes/img/commons/or.gif";
			break;
		case 2:
			path = "/themes/img/commons/and.gif";
			break;
		case 3:
			path = "/themes/img/commons/code.gif";
		}

		return path;
	}

	public static String setParamIcon(String ctx, String json) {
		if ((json == null) || (json.equals("")))
			return null;
		JSONArray ja = JSONArray.fromObject(json);
		List ml = (List) JSONArray.toCollection(ja, Map.class);
		if (BeanUtils.isEmpty(ml))
			return null;
		for (int i = 0; i < ml.size(); i++) {
			Map m = (Map) ml.get(i);
			int type = Integer.parseInt(m.get("type").toString());
			String icon = ctx + getIconPath(type);
			m.put("icon", icon);
			JSONArray children = JSONArray.fromObject(m.get("children"));
			if (!children.get(0).toString().equals("null")) {
				List childrenMap = (List) JSONArray.toCollection(children,
						Map.class);
				if (BeanUtils.isEmpty(childrenMap))
					return null;
				for (int j = 0; j < childrenMap.size(); j++) {
					Map mc = (Map) childrenMap.get(j);
					int type_ = Integer.parseInt(mc.get("type").toString());
					String icon_ = ctx + getIconPath(type_);
					mc.put("icon", icon_);
				}
				m.put("children", JSONArray.fromObject(childrenMap));
			}
		}
		JSONArray j1 = JSONArray.fromObject(ml);
		return j1.toString();
	}

	public SysParam getByParamKey(String paramKey) {
		return this.dao.getByParamKey(paramKey);
	}

	public List<String> getDistinctCategory(Integer type, Long dimId) {
		return this.dao.getDistinctCategory(type, dimId);
	}
}
