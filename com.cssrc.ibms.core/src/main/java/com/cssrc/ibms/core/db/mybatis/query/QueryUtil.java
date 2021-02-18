package com.cssrc.ibms.core.db.mybatis.query;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

public class QueryUtil {
	/**
	 * 是否是超级管理员
	 * 
	 * @return
	 */
	public static boolean isSuperAdmin() {
		/*
		 * List<GrantedAuthority> roles = (List<GrantedAuthority>) ContextUtil
		 * .getCurrentUser().getAuthorities(); boolean hasAuth = false; if
		 * (roles.size() > 0) { // 先判断有没有超级管理员角色 for (GrantedAuthority
		 * grantedAuthority : roles) { if
		 * (grantedAuthority.equals(SystemConst.ROLE_GRANT_SUPER)) { // 超级管理员
		 * hasAuth = true; break; } } } if (hasAuth) return true;
		 */
		return false;
	}

	// TODO 获取分页List
	/**
	 * 
	 * 获取分页List
	 * 
	 * @param dataRule
	 * @param queryFilter
	 * @return
	 */
	public static List<?> getPageList(BusQueryRule busQueryRule,
			QueryFilter queryFilter) {
		if (StringUtil.isEmpty(busQueryRule.getFilterKey())
				&& busQueryRule.getIsFilter().shortValue() == QueryConstants.IS_QUERY_NOT) {
			return null;
		}

		List<?> list = null;
		// HttpServletRequest request = queryFilter.getRequest();
		// short isQuery = getIsQuery(request, busQueryRule);
		// 是否初始化查询
		/*
		 * if (isQuery == QueryConstants.IS_QUERY_YES) { TableEntity tableEntity
		 * = getTableEntity(busQueryRule .getTableName()); BusQueryFilter
		 * busQueryFilter = getBusQueryFilter(busQueryRule .getFilterFlag());
		 * Map<String, Object> paraMap = getFilters(queryFilter.getFilters(),
		 * busQueryFilter); // 获取查询SQL String querySQL =
		 * getQuerySQL(busQueryRule, tableEntity, queryFilter, paraMap,
		 * busQueryFilter); JdbcHelper<?, ?> jdbcHelper = getConfigJdbcHelper();
		 * 
		 * // 是否分页 if (busQueryRule.getNeedPage().intValue() == 1) {// 分页
		 * ParamEncoder paramEncoder = queryFilter.getParamEncoder(); String
		 * tableIdCode = paramEncoder.encodeParameterName(""); PageBean page =
		 * queryFilter.getPageBean(); int currentPage = page.getCurrentPage();
		 * int pageSize = page.getPageSize(); int oldPageSize =
		 * RequestUtil.getInt(request, tableIdCode + "oz", -1); if (oldPageSize
		 * < 0) pageSize = (page.getPageSize() != busQueryRule .getPageSize()) ?
		 * busQueryRule.getPageSize() : page .getPageSize(); PageBean pageBean =
		 * new PageBean(currentPage, pageSize);
		 * queryFilter.setPageBean(pageBean); list =
		 * jdbcHelper.getPage(querySQL, paraMap, pageBean); } else { list =
		 * jdbcHelper.queryForList(querySQL, paraMap); } } // 设置回请求对象，避免前台页面报错
		 * queryFilter.setForWeb();
		 */
		return list;
	}

	/**
	 * 获取是否查询
	 * 
	 * @param request
	 * @param busQueryRule
	 * @return
	 */
	@SuppressWarnings("unused")
	private static short getIsQuery(HttpServletRequest request,
			BusQueryRule busQueryRule) {
		if (BeanUtils.isEmpty(busQueryRule))
			return QueryConstants.IS_QUERY_YES;
		Short isQuery = RequestUtil.getShort(request, QueryConstants.IS_QUERY,
				null);
		if (BeanUtils.isEmpty(isQuery))
			return busQueryRule.getIsQuery();
		return isQuery;
	}
	
	
	public static String getRightsName(JSONObject json) {
		String var = (String) json.get("variable");
		if (StringUtil.isEmpty(var))
			var = json.getString("name");
		return var;
	}

}
