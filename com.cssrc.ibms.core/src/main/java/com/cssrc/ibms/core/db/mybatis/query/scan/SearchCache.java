package com.cssrc.ibms.core.db.mybatis.query.scan;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import com.cssrc.ibms.core.db.mybatis.query.annotion.Field;
import com.cssrc.ibms.core.db.mybatis.query.annotion.Table;
/**
 * 
 * <p>Title:SearchCache</p>
 * @author Yangbo 
 * @date 2016-8-8下午03:15:12
 */
public class SearchCache implements InitializingBean {
	public Logger logger = LoggerFactory.getLogger(SearchCache.class);

	private static Map<String, TableEntity> tableEntityMap = new HashMap();

	private static Map<String, TableEntity> displayTagIdMap = new HashMap();

	private static Map<String, TableEntity> tableVarMap = new HashMap();
	private Resource[] basePackage;

	public void constractSearchTableList() throws IOException,
			ClassNotFoundException {
		this.logger.debug("开始扫描元数据..");
		Long start = Long.valueOf(System.currentTimeMillis());

		List<Class<?>> tableList = TableScaner.findTableScan(this.basePackage);

		this.logger.debug("扫描结束,共耗时:"
				+ (System.currentTimeMillis() - start.longValue()) + "毫秒");

		for (Class cls : tableList) {
			Table table = (Table) cls.getAnnotation(Table.class);
			TableEntity tableEntity = new TableEntity(cls.getSimpleName(),
					table);
			java.lang.reflect.Field[] fields = cls.getDeclaredFields();
			for (java.lang.reflect.Field field : fields) {
				Field qField = (Field) field
						.getAnnotation(Field.class);

				if (qField != null) {
					TableField tableField = new TableField(field, qField);
					tableEntity.getTableFieldList().add(tableField);
				}
			}
			tableEntityMap.put(tableEntity.getTableName(), tableEntity);

			if (StringUtils.isNotEmpty(tableEntity.getDisplayTagId())) {
				displayTagIdMap.put(tableEntity.getDisplayTagId(), tableEntity);
			}
			if (StringUtils.isNotEmpty(tableEntity.getVar())) {
				tableVarMap.put(tableEntity.getVar(), tableEntity);
			}
		}
		Collection<TableEntity> list = tableEntityMap.values();
		for (TableEntity sub : list) {
			if (sub.isPrimary()) {
				continue;
			}
			String mainTable = sub.getPrimaryTable();
			if (StringUtils.isNotEmpty(mainTable)) {
				TableEntity primaryTable = (TableEntity) tableEntityMap
						.get(mainTable);
				primaryTable.addSubTable(sub);
			}
		}
	}

	public void afterPropertiesSet() throws Exception {
		constractSearchTableList();
	}

	public Resource[] getBasePackage() {
		return this.basePackage;
	}

	public void setBasePackage(Resource[] basePackage) {
		this.basePackage = basePackage;
	}

	public static Map<String, TableEntity> getTableEntityMap() {
		return tableEntityMap;
	}

	public static Map<String, TableEntity> getDisplayTagIdMap() {
		return displayTagIdMap;
	}

	public static Map<String, TableEntity> getTableVarMap() {
		return tableVarMap;
	}
}
