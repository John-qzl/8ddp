package com.cssrc.ibms.core.table;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.cssrc.ibms.core.db.mybatis.dialect.Dialect;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.form.intf.ITableOperator;
import com.cssrc.ibms.core.form.model.FormTableIndex;

public class AbstractTableOperator implements ITableOperator{

	//数据库类型
	protected String dbType;
	
	//JdbcTemplate
	protected JdbcTemplate jdbcTemplate;
	
	//logger
	protected Logger logger=LoggerFactory.getLogger(this.getClass());
	
	//方言
	protected Dialect dialect;
	
	
	
	@Override
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	
	@Override
	public String getDbType() {
		return this.dbType;
	}

	@Override
	public void setJdbcTemplate(JdbcTemplate template) {
		this.jdbcTemplate=template;
	}

	@Override
	public void createTable(TableModel model) throws SQLException {
		throw new UnsupportedOperationException("Current Implement not supported");
	}

	@Override
	public void dropTable(String tableName) {
		throw new UnsupportedOperationException("Current Implement not supported");
	}

	@Override
	public void updateTableComment(String tableName, String comment)
			throws SQLException {
		throw new UnsupportedOperationException("Current Implement not supported");
	}

	@Override
	public void addColumn(String tableName, ColumnModel model)
			throws SQLException {
		throw new UnsupportedOperationException("Current Implement not supported");
	}

	@Override
	public void updateColumn(String tableName, String columnName,
			ColumnModel model) throws SQLException {
		throw new UnsupportedOperationException("Current Implement not supported");
	}

	@Override
	public void addForeignKey(String pkTableName, String fkTableName,
			String pkField, String fkField) {
		throw new UnsupportedOperationException("Current Implement not supported");
	}

	@Override
	public void dropForeignKey(String tableName, String keyName) {
		throw new UnsupportedOperationException("Current Implement not supported");
	}

	@Override
	public void createIndex(FormTableIndex index) throws SQLException {
		throw new UnsupportedOperationException("Current Implement not supported");
	}

	@Override
	public void dropIndex(String tableName, String indexName) {
		throw new UnsupportedOperationException("Current Implement not supported");
	}

	@Override
	public FormTableIndex getIndex(String tableName, String indexName) {
		throw new UnsupportedOperationException("Current Implement not supported");
	}

	@Override
	public List<FormTableIndex> getIndexesByTable(String tableName) {
		throw new UnsupportedOperationException("Current Implement not supported");
	}

	@Override
	public List<FormTableIndex> getIndexesByFuzzyMatching(String tableName,
			String indexName, Boolean getDDL) {
		throw new UnsupportedOperationException("Current Implement not supported");
	}

	@Override
	public List<FormTableIndex> getIndexesByFuzzyMatching(String tableName,
			String indexName, Boolean getDDL, PagingBean pageBean) {
		throw new UnsupportedOperationException("Current Implement not supported");
	}

	@Override
	public void rebuildIndex(String tableName, String indexName) {
		throw new UnsupportedOperationException("Current Implement not supported");
	}


	@Override
	public void setDialect(Dialect dialect) {
		this.dialect=dialect;
	}

	@Override
	public List<String> getPKColumns(String tableName) throws SQLException {
		throw new UnsupportedOperationException("Current Implement not supported");
		
	}

	@Override
	public Map<String, List<String>> getPKColumns(List<String> tableNames)
			throws SQLException {
		throw new UnsupportedOperationException("Current Implement not supported");
	}

	@Override
	public void createIndex(String tableName, String fieldName) {
		String regex="(?im)"+TableModel.CUSTOMER_TABLE_PREFIX;
		String shortTableName=tableName.replaceFirst(regex, "");
		String sqlIndex="create index idx_" +shortTableName +"_" +fieldName +" on " +tableName +"(" +fieldName+")";
		jdbcTemplate.execute(sqlIndex);
		
	}

	@Override
	public boolean isTableExist(String tableName) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void dropColumn(String tableName, ColumnModel model)
			throws SQLException {
		
		throw new UnsupportedOperationException("Current Implement not supported");
	}

	
}
