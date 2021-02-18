package com.cssrc.ibms.core.db.mybatis.query.scan;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.db.mybatis.query.annotion.Table;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;
/**
 * 
 * <p>Title:TableEntity</p>
 * @author Yangbo 
 * @date 2016-8-8下午03:13:52
 */
public class TableEntity {
	private String tableName;
	private String var;
	private String displayTagId;
	private String pk;
	private String comment;
	private boolean isPrimary = true;

	private String relation = "";

	private String primaryTable = "";

	private List<TableField> tableFieldList = new ArrayList();

	private List<TableEntity> subTableList = new ArrayList();

	private static ThreadLocal<Map<String, Object>> queryFilterLocal = new ThreadLocal();

	public TableEntity() {
	}

	public TableEntity(String claName, Table table) {
		this.tableName = table.name();
		this.var = (StringUtils.isNotEmpty(table.var()) ? table.var()
				: StringUtil.makeFirstLetterLowerCase(claName));

		this.displayTagId = (table.var() + "Item");

		this.pk = table.pk();
		this.comment = table.comment();
		this.isPrimary = table.isPrimary();
		this.relation = table.relation();
		this.primaryTable = table.primaryTable();
	}

	public String getTableName() {
		return this.tableName;
	}

	public String getVar() {
		return this.var;
	}

	public String getDisplayTagId() {
		return this.displayTagId;
	}

	public String getPk() {
		return this.pk;
	}

	public String getComment() {
		return this.comment;
	}

	public List<TableField> getTableFieldList() {
		return this.tableFieldList;
	}

	public void setTableFieldList(List<TableField> tableFieldList) {
		this.tableFieldList = tableFieldList;
	}

	public static void setQueryFilterLocal(Map<String, Object> map) {
		queryFilterLocal.set(map);
	}

	public static Map<String, Object> getQueryFilterLocal() {
		return (Map) queryFilterLocal.get();
	}

	public static void removeQueryFilterLocal() {
		queryFilterLocal.remove();
	}

	public static List<TableEntity> getAll(QueryFilter queryFilter) {
		List tableEntitylist = new ArrayList(SearchCache.getTableEntityMap()
				.values());

		List queryList = new ArrayList(tableEntitylist);

		if (BeanUtils.isNotEmpty(queryFilter.getFilters())) {
			queryList = getQueryList(queryList, tableEntitylist, queryFilter);

			queryList = getSortList(queryList, queryFilter);
		}

		PagingBean pageBean = queryFilter.getPagingBean();

		int total = queryList.size();

		int pageSize = pageBean.getPageSize();

		int currentPage = pageBean.getCurrentPage();

		int fromIndex = pageSize * (currentPage - 1);
		int toIndex = pageSize * currentPage > total ? total : pageSize
				* currentPage;

		List list = queryList.subList(fromIndex, toIndex);
		pageBean.setTotalCount(total);
		queryFilter.setForWeb();
		return list;
	}

	private static List<TableEntity> getSortList(List<TableEntity> queryList,
			QueryFilter queryFilter) {
		Object orderField = queryFilter.getFilters().get("orderField");
		Object orderSeq = queryFilter.getFilters().get("orderSeq");

		if ((BeanUtils.isEmpty(orderField)) || (BeanUtils.isEmpty(orderSeq)))
			return queryList;
		setQueryFilterLocal(queryFilter.getFilters());

		Comparator comparator = new Comparator<TableEntity>() {
			public int compare(TableEntity o1, TableEntity o2) {
				Map m = TableEntity.getQueryFilterLocal();
				Object field = m.get("orderField");
				Object seq = m.get("orderSeq");
				String orderField = "";
				if ("tableName".equalsIgnoreCase((String) field))
					orderField = "tableName";
				boolean order = true;
				if ("asc".equalsIgnoreCase((String) seq)) {
					order = false;
				}
				String s1 = BeanUtils.isNotEmpty(orderField) ? o1
						.getTableName() : o1.getComment();

				String s2 = BeanUtils.isNotEmpty(orderField) ? o2
						.getTableName() : o2.getComment();

				return compare(s1, s2, order);
			}

			public int compare(String s1, String s2, boolean order) {
				Comparator cmp = Collator.getInstance(Locale.CHINA);

				if (cmp.compare(s1, s2) < 0)
					return order ? -1 : 1;
				if (cmp.compare(s1, s2) > 0) {
					return order ? 1 : -1;
				}
				return 0;
			}
		};
		Collections.sort(queryList, comparator);
		removeQueryFilterLocal();
		return queryList;
	}

	private static List<TableEntity> getQueryList(List<TableEntity> queryList,
			List<TableEntity> list, QueryFilter queryFilter) {
		Object tableName = queryFilter.getFilters().get("tableName");
		Object description = queryFilter.getFilters().get("comment");
		if ((BeanUtils.isEmpty(tableName)) && (BeanUtils.isEmpty(description)))
			return queryList;
		queryList = new ArrayList();
		int type = getQueryType(tableName, description);

		for (TableEntity tableEntity : list) {
			boolean flag = isContainsQuery(tableEntity, tableName, description,
					type);

			if (flag)
				queryList.add(tableEntity);
		}
		return queryList;
	}

	private static boolean isContainsQuery(TableEntity tableEntity,
			Object tableName, Object description, int type) {
		switch (type) {
		case 1:
			return isContainsQuery(tableEntity, tableName, description);
		case 2:
			return StringUtils.containsIgnoreCase(tableEntity.getTableName(),
					tableName.toString());
		case 3:
			return StringUtils.containsIgnoreCase(tableEntity.getComment(),
					description.toString());
		}

		return false;
	}

	private static boolean isContainsQuery(TableEntity tableEntity,
			Object tableName, Object description) {
		return (isContainsQuery(tableEntity, tableName, Integer.valueOf(2)))
				&& (isContainsQuery(tableEntity, description, Integer
						.valueOf(3)));
	}

	private static int getQueryType(Object tableName, Object description) {
		int type = 0;
		if ((BeanUtils.isNotEmpty(tableName))
				&& (BeanUtils.isNotEmpty(description))) {
			type = 1;
		} else if ((BeanUtils.isNotEmpty(tableName))
				&& (BeanUtils.isEmpty(description))) {
			type = 2;
		} else if ((BeanUtils.isEmpty(tableName))
				&& (BeanUtils.isNotEmpty(description))) {
			type = 3;
		}
		return type;
	}

	public boolean isPrimary() {
		return this.isPrimary;
	}

	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public String getRelation() {
		return this.relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getPrimaryTable() {
		return this.primaryTable;
	}

	public void setPrimaryTable(String primaryTable) {
		this.primaryTable = primaryTable;
	}

	public List<TableEntity> getSubTableList() {
		return this.subTableList;
	}

	public void setSubTableList(List<TableEntity> subTableList) {
		this.subTableList = subTableList;
	}

	public void addSubTable(TableEntity tableEnt) {
		this.subTableList.add(tableEnt);
	}

	public static Map<String, TableEntity> getSubTableMap(
			TableEntity tableEntity) {
		List<TableEntity> subTableList = tableEntity.getSubTableList();
		Map map = new HashMap();
		if (BeanUtils.isEmpty(subTableList))
			return map;
		for (TableEntity table : subTableList) {
			map.put(table.getTableName(), table);
		}
		return map;
	}

	public String toString() {
		return new ToStringBuilder(this).append("tableName", this.tableName)
				.append("pk", this.pk).append("comment", this.comment).append(
						"tableFieldList.size", this.tableFieldList.size())
				.toString();
	}
}
