package com.cssrc.ibms.core.db.mybatis.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.util.ParamEncoder;

import com.cssrc.ibms.core.db.mybatis.page.PageUtils;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.http.RequestUtil;

public class QueryFilter implements Serializable{
	private static final long serialVersionUID = 7763395680128056916L;
	public static final Log logger = LogFactory.getLog(QueryFilter.class);
	private PagingBean pagingBean = null;
	private List<CriteriaCommand> commands = new ArrayList<CriteriaCommand>();

	/**
	 * 过滤参数
	 */
	private Map<String, Object> filters = new HashMap<String, Object>();
	/**
	 * 取得查询条件。
	 * 
	 * @return
	 */
	private String filterName = null;

	private boolean searchAll = false;

	private boolean isExport = false;

	private Set<String> aliasSet = new HashSet<String>();

	private HttpServletRequest request = null;

	private List<Object> paramValues = new ArrayList<Object>();
	
	private String requestURI;

	public QueryFilter() {
		this.pagingBean = new PagingBean();
	}

	public Map<String, Object> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, Object> filters) {
		this.filters = filters;
	}

	public PagingBean getPagingBean() {
		return pagingBean;
	}

	public void setPagingBean(PagingBean pagingBean) {
		this.pagingBean = pagingBean;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public boolean getSearchAll() {
		return searchAll;
	}

	public void setSearchAll(boolean searchAll) {
		this.searchAll = searchAll;
	}

	public boolean isExport() {
		return isExport;
	}

	public void setExport(boolean isExport) {
		this.isExport = isExport;
	}

	public Set<String> getAliasSet() {
		return aliasSet;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public List<Object> getParamValueList() {
		return this.paramValues;
	}

	/* for ibatis displaytag */
	private String tableId = "";

	private ParamEncoder paramEncoder;

	public final static String ORDER_ASC = "1";

	public final static String ORDER_DESC = "2";

	/**
	 * 构造QueryFilter，适用于使用displaytag列表的情况，默认为分页处理。
	 * 
	 * @param request
	 * @param tableId
	 *            displaytag的id
	 */
	public QueryFilter(HttpServletRequest request, String tableId) {
		this(request, tableId, true);
	}

	/**
	 * 构建QueryFilter，可以根据needPage参数构建是否需要分页。<br>
	 * 如果需要分页。<br>
	 * 1.page 作为当前页参数。<br>
	 * 2.pageSize 作为获取分页大小的参数。
	 * 
	 * @param request
	 *            HttpServletRequest 对象
	 * @param needPage
	 *            是否需要分页
	 */
	public QueryFilter(HttpServletRequest request, boolean needPage) {
		this.requestURI=request.getRequestURI();
		this.request = request;
		try {
			if (needPage) {
				int page = RequestUtil.getInt(request, "page", 1);
				int pageSize = RequestUtil.getInt(request, "pageSize", 15);
				this.pagingBean = new PagingBean(page, pageSize);
			}
			Map<String, Object> map = RequestUtil.getQueryMap(request);
			filters = map;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
	}

	
	public QueryFilter(int page, int pageSize,Map<String, Object> map,String requestURI) {
		this.request = null;
		this.requestURI=requestURI;
		try {
			this.pagingBean = new PagingBean(page, pageSize);
			filters = map;
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
	}
	
	/**
	 * 这个QueryFilter构造方法用于使用displaytag的情况。<br>
	 * 如果需要碰到需要分页的情况， 请
	 * {@link com.ibms.core.web.util.RequestUtil#getQueryMap(HttpServletRequest)
	 * 参考}定义的参数规格。
	 * 
	 * @param request
	 *            HttpServletRequest对象
	 * @param tableId
	 *            tableId 对应diaplaytag的 id属性
	 * @param needPage
	 *            是否需要分页
	 */
	public QueryFilter(HttpServletRequest request, String tableId,
			boolean needPage) {
		this.tableId = tableId;
		this.request = request;
		this.requestURI=request.getRequestURI();
		this.paramEncoder = new ParamEncoder(tableId);
		String tableIdCode = this.paramEncoder.encodeParameterName("");
		try {
			// 排序字段
			String orderField = request.getParameter(tableIdCode + "s");
			String orderSeqNum = request.getParameter(tableIdCode + "o");

			String orderSeq = "desc";
			if (orderSeqNum != null && ORDER_ASC.equals(orderSeqNum)) {
				orderSeq = "asc";
			}
			Map<String, Object> map = RequestUtil.getQueryMap(request);
			if (orderField != null) {
				map.put("orderField", orderField);
				map.put("orderSeq", orderSeq);
			}
			this.filters = map;
			if (needPage) {
				int page = RequestUtil.getInt(request, tableIdCode + "p", 1);
				int pageSize = RequestUtil.getInt(request, tableIdCode + "z",
						PagingBean.DEFAULT_PAGE_SIZE);
				int oldPageSize = RequestUtil.getInt(request, tableIdCode
						+ "oz", PagingBean.DEFAULT_PAGE_SIZE);
				if (pageSize != oldPageSize) {
					int first = PageUtils.getFirstNumber(page, oldPageSize);
					page = first / pageSize + 1;
				}
				this.pagingBean = new PagingBean(Integer.valueOf(page),
						pageSize);
				pagingBean.setStart(PageUtils.getFirstNumber(page, pageSize));
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
	}

	public QueryFilter(HttpServletRequest request) {
		this.requestURI=request.getRequestURI();
		this.request = request;
		Enumeration paramEnu = request.getParameterNames();
		while (paramEnu.hasMoreElements()) {
			String paramName = (String) paramEnu.nextElement();
			if (paramName.startsWith("Q_")) {
				String paramValue = request.getParameter(paramName);
				addFilterForH(paramName, paramValue);
			}
		}
		Integer start = Integer.valueOf(0);
		Integer limit = PagingBean.DEFAULT_PAGE_SIZE;

		String s_start = request.getParameter("start");
		String s_limit = request.getParameter("limit");
		if (!CommonTools.isNullString(s_start)) {
			start = new Integer(s_start);
		}
		if (!CommonTools.isNullString(s_limit)) {
			limit = new Integer(s_limit);
		}
		String sort = request.getParameter("sort");
		String dir = request.getParameter("dir");
		if ((!CommonTools.isNullString(sort))
				&& (!CommonTools.isNullString(dir))) {
			addSorted(sort, dir);
		}

		if ("true".equals(request.getParameter("isExport"))) {
			this.isExport = true;
			request.setAttribute("colId", request.getParameter("colId"));
			request.setAttribute("colName", request.getParameter("colName"));
			request.setAttribute("exportType", request
					.getParameter("exportType"));
		}
		request.setAttribute("isExport", Boolean.valueOf(this.isExport));

		if ("true".equals(request.getParameter("searchAll"))) {
			this.searchAll = true;
			this.pagingBean = new PagingBean();
		} else {
			this.pagingBean = new PagingBean(start.intValue(), limit.intValue());
		}
		request.setAttribute("searchAll", Boolean.valueOf(this.searchAll));

	}

	public void addFilterForH(String paramName, String paramValue) {
		String[] fieldInfo = paramName.split("[_]");
		Object value = null;
		if ((fieldInfo != null) && (fieldInfo.length == 4)) {
			value = ParamUtil.convertObject(fieldInfo[2], paramValue);
			if (value != null) {
				FieldCommandImpl fieldCommand = new FieldCommandImpl(
						fieldInfo[1], value, fieldInfo[3], this);
				this.commands.add(fieldCommand);
			}
		} else if ((fieldInfo != null) && (fieldInfo.length == 3)) {
			value = ParamUtil.convertObject(fieldInfo[2], paramValue);
			FieldCommandImpl fieldCommand = new FieldCommandImpl(fieldInfo[1],
					value, null, this);
			this.commands.add(fieldCommand);
		} else {
			logger.error("Query param name [" + paramName
					+ "] is not right format.");
		}
	}

	public void addSorted(String orderBy, String ascDesc) {
		this.commands.add(new SortCommandImpl(orderBy, ascDesc, this));
	}
	
	public void setForWeb() {
		String pbName = "pageBean";
		String href = "requestURI";
		if (this.tableId != null) {
			pbName = pbName + this.tableId;
			href = href + this.tableId;
		}
		if(request!=null){
			this.request.setAttribute(href, this.requestURI);
			this.request.setAttribute(pbName, this.pagingBean);
		}
	}

	public void setForWeb(String tableId) {
		this.tableId = tableId;
		this.setForWeb();
	}

	/**
	 * 添加过滤字段条件
	 * 
	 * @param filterName
	 *            参数名
	 * @param params
	 *            参数值
	 */
	public void addFilterForIB(String filterName, Object params) {
		this.filters.put(filterName, params);
	}

	/**
	 * 删除过滤字段条件
	 * 
	 * @param filterName
	 *            参数名
	 * @param params
	 *            参数值
	 */
	public void delFilter(String filterName) {
		Map<String, Object> filterNew = new HashMap<String, Object>();
		Iterator<Map.Entry<String, Object>> it = this.filters.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			if (!key.equals(filterName)) {
				filterNew.put(entry.getKey(), entry.getValue());
			}
		}
		this.filters = filterNew;
	}

	public ParamEncoder getParamEncoder() {
		return paramEncoder;
	}

}
