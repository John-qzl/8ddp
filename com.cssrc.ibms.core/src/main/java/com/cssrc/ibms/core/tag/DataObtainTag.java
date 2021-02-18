package com.cssrc.ibms.core.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.springframework.jdbc.core.JdbcTemplate;

import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

public class DataObtainTag extends BodyTagSupport {
	private static ThreadLocal<Map<String, Map<String, String>>> dataLocal = new ThreadLocal();

	private String alias = "";

	private String dbObjName = "";

	private String queryField = "";

	private String fieldValue = "";

	private String dataType = "string|number";

	private String displayFormat = "";

	public static void cleanData() {
		dataLocal.remove();
	}

	public void setDbObjName(String dbObjName) {
		this.dbObjName = dbObjName;
	}

	public void setQueryField(String queryField) {
		this.queryField = queryField;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setDisplayFormat(String displayFormat) {
		this.displayFormat = displayFormat;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	private String getData() {
		String sql = getSql();
		JdbcTemplate template = (JdbcTemplate) AppUtil.getBean("jdbcTemplate");
		Object param = this.fieldValue;
		if ("number".equals(this.dataType)) {
			param = Long.valueOf(Long.parseLong(this.fieldValue));
		}
		Map map = template.queryForMap(sql, new Object[] { param });

		return getDisplay(map);
	}

	public String getResult() {
		String rtnData = "";
		Map map = (Map) dataLocal.get();
		if (map == null) {
			String rtn = getData();
			Map temp = new HashMap();

			Map tempMap = new HashMap();
			tempMap.put(this.fieldValue, rtn);
			temp.put(this.alias, tempMap);

			dataLocal.set(temp);

			rtnData = rtn;
		} else {
			Map tempMap = (Map) map.get(this.alias);
			if (tempMap == null) {
				String rtn = getData();
				tempMap = new HashMap();
				tempMap.put(this.fieldValue, rtn);
				map.put(this.alias, tempMap);
				rtnData = rtn;
			} else if (!tempMap.containsKey(this.fieldValue)) {
				String rtn = getData();
				tempMap.put(this.fieldValue, rtn);
				rtnData = rtn;
			} else {
				rtnData = (String) tempMap.get(this.fieldValue);
			}

		}

		return rtnData;
	}

	private String getDisplay(Map<String, Object> map) {
		List<String> queryFieldList = getQueryField();

		String tmp = this.displayFormat;

		for (String field : queryFieldList) {
			tmp = tmp.replace("[" + field + "]", (String) map.get(field));
		}
		return tmp;
	}

	private String getSql() {
		List<String> queryFieldList = getQueryField();
		String queryFieldTemp = "";
		String fields = "";
		for (String tempField : queryFieldList) {
			queryFieldTemp = queryFieldTemp + tempField + ",";
		}
		if (StringUtil.isNotEmpty(queryFieldTemp)) {
			fields = queryFieldTemp.substring(0, queryFieldTemp.length() - 1);
		}

		return "select " + fields + " from " + this.dbObjName + " where "
				+ this.queryField + "=?";
	}

	private List<String> getQueryField() {
		List result = new ArrayList();

		Pattern regex = Pattern.compile("\\[(\\w+)\\]");
		Matcher regexMatcher = regex.matcher(this.displayFormat);
		while (regexMatcher.find()) {
			result.add(regexMatcher.group(1));
		}

		return result;
	}

	public int doStartTag() throws JspTagException {
		return 2;
	}

	public int doEndTag() throws JspTagException {
		try {
			String str = getResult();
			this.pageContext.getOut().print(str);
		} catch (Exception e) {
			throw new JspTagException(e.getMessage());
		}
		return 0;
	}

}
