package com.cssrc.ibms.dbom.service;

import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.dbom.model.DBomNode;

public abstract class IDbomTreeSqlService {

	/**
	 * 根据动态子节点数据设置得到sql 的 select部分
	 * 
	 * @param fileds
	 *            查询的字段
	 * @param dynamicNode
	 *            动态子节点数据
	 * @param dataFormat
	 *            动态子节点格式化数据
	 * @return
	 */
	public StringBuffer creatSelect(String fileds, String dynamicNode,
			String dataFormat) {
		StringBuffer selectSql = new StringBuffer("SELECT ");
		if ("".equals(dataFormat) || dataFormat.equals(dynamicNode)) {
			if (fileds.indexOf(",") > 0) {
				// 处理多个字段
				selectSql.append(fileds);
			} else {
				selectSql.append(" DISTINCT ").append(fileds);
			}
		} else {
			selectSql.append(" DISTINCT ").append(dataFormat).append(" AS ")
					.append(fileds);
		}
		return selectSql;
	}

	/**
	 * 根据动态子节点数据设置得到sql 的 from部分
	 * 
	 * @param fileds
	 *            查询的字段
	 * @param dynamicNode
	 *            动态子节点数据
	 * @param dataFormat
	 *            动态子节点格式化数据
	 * @param tableSource
	 *            要查询的表
	 * @param paramSql
	 *            条件语句
	 * @return
	 */
	public StringBuffer creatFrom(String fileds, String dynamicNode,
			String dataFormat, String tableSource, String paramSql) {
		StringBuffer fromSql = new StringBuffer(" FROM ");
		if ("".equals(dataFormat) || dataFormat.equals(dynamicNode)) {
			if (fileds.indexOf(",") > 0) {
				// 处理多个字段
				fromSql.append(tableSource);
			} else {
				fromSql.append(creatSubSql(tableSource, paramSql));
			}
		} else {
			fromSql.append(creatSubSql(tableSource, paramSql));
		}
		return fromSql;
	}

	/**
	 * sql where 部分，目前只需要 返回“where 1=1”
	 * 
	 * @return
	 */
	public StringBuffer creatWhere() {
		StringBuffer whereSql = new StringBuffer(" WHERE 1=1 ");
		return whereSql;
	}

	/**
	 * order 部分 只根据查询的字段进行简单排序
	 * 
	 * @param fieldName
	 *            要查询的字段
	 * @return
	 */
	public StringBuffer creatOrder(String fieldName) {
		StringBuffer sql = new StringBuffer();
		return sql.append(" order by ")
				.append(fieldName.replaceAll(",", " asc, ")).append(" asc");
	}

	/**
	 * 因为是distinct 查询 只能按照distinct 字段简单排序 排序语句
	 * 
	 * @param fieldName
	 * @return
	 */
	public StringBuffer getOrderSql(String fieldName) {
		StringBuffer sql = new StringBuffer();
		return sql.append(" order by ")
				.append(fieldName.replaceAll(",", " asc, ")).append(" asc");

	}

	/**
	 * 得到子查询
	 * 
	 * @param paramSql
	 * @param tableSource
	 * @return
	 */
	public String creatSubSql(String paramSql, String tableSource) {
		StringBuffer subSlectSql = new StringBuffer("(SELECT * FROM ");
		subSlectSql.append(tableSource);
		subSlectSql.append(" where 1=1 ");
		subSlectSql.append(paramSql + ")sub_sql_" + tableSource);
		return subSlectSql.toString();
	}

	/**
	 * 根据pdbomNode设置动态资金节点数据源获取查询的主表
	 * 
	 * @param dbomNode
	 * @return
	 */
	public String getDataSource(DBomNode dbomNode) {
		// 不管动态子节点是多个还是单个，取第一个动态子节点作为datasource
		String[] sArrFieldName = dbomNode.getShowFiled().split(",");
		if (sArrFieldName != null && sArrFieldName.length > 0) {
			String dataSource = sArrFieldName[0];
			return sArrFieldName[0].substring(0, dataSource.lastIndexOf("."));
		} else {
			return null;
		}
	}

	/**
	 * 根据设置动态子节点数据获取要查询的表字段
	 * 
	 * @param dbomNode
	 * @param dataSource
	 * @return
	 */
	public String getSelectFiled(DBomNode dbomNode, String dataSource) {
		return dbomNode.getShowFiled().replaceAll(dataSource + ".", "");

	}

	/**
	 * 
	 * @param fileds
	 *            查询字段
	 * @param tableSource
	 *            查询表，可以是查询语句
	 * @param format
	 *            查询字段格式化数据
	 * @param orderSql
	 *            排序sql
	 * @param paramSql
	 * @return
	 */

	/**
	 * @param dbomNode
	 * @param paramSql
	 * @return
	 */
	public StringBuffer createTreeSql(DBomNode dbomNode, String paramSql) {
		String tableSource = this.getDataSource(dbomNode);
		String dynamicNode = dbomNode.getShowFiled();
		// 动态子节点查询字段
		String fileds = getSelectFiled(dbomNode, tableSource);
		// 查询字段是否需要自定义格式化
		String dataFormat = CommonTools.Obj2String(dbomNode.getShowFiled());
		StringBuffer selectSql = this.creatSelect(fileds, dynamicNode,
				dataFormat);
		StringBuffer fromSql = this.creatFrom(fileds, dynamicNode, dataFormat,
				tableSource, paramSql);
		StringBuffer whereSql = this.creatWhere();
		StringBuffer orderSql = this.creatOrder(fileds);

		return selectSql.append(fromSql).append(whereSql).append(orderSql);
	}

	/**
	 * @param pdbomNode
	 * @param dataTemplate 表单模板获取排序语句
	 * @param paramSql 过滤条件
	 * @return
	 */
	public StringBuffer createSelectSql(DBomNode pdbomNode,IDataTemplate dataTemplate,String paramSql){
		String dataSource=this.getDataSource(pdbomNode);
		//动态子节点查询字段
		String fieldName = getSelectFiled(pdbomNode,dataSource);
		//查询字段是否需要自定义格式化
		String dataFormat = CommonTools.Obj2String(pdbomNode.getShowFiled());
		StringBuffer selectSql = new  StringBuffer("SELECT ");
		StringBuffer fromSql = new  StringBuffer(" FROM ");
		StringBuffer whereSql = new  StringBuffer(" WHERE 1=1 ");
		StringBuffer orderSql = this.getOrderSql(fieldName);

		if("".equals(dataFormat) || dataFormat.equals(pdbomNode.getShowFiled())){
			if(fieldName.indexOf(",")>0){
				//处理多个字段
				selectSql.append(fieldName);
				fromSql.append(dataSource);
			}else{
				selectSql.append(" DISTINCT ").append(fieldName);
				StringBuffer subSlectSql = new  StringBuffer("SELECT * FROM ");
				subSlectSql.append(dataSource);
				subSlectSql.append(" where 1=1 ");
				subSlectSql.append(paramSql);
				//根据bpmDataTemplate 表单模板获取排序select 语句 因为是distinct 所以不支持自定义排序，只能按照查询字段进行排序
				//subSlectSql=dataTemplateService.getOrderBySql(subSlectSql, dataTemplate);
				fromSql.append("(").append(subSlectSql).append(")s_table");
			}
		}else{
			selectSql.append(" DISTINCT ").append(dataFormat).append(" AS ").append(fieldName);
			StringBuffer subSlectSql = new  StringBuffer("SELECT * FROM ");
			subSlectSql.append(dataSource);
			subSlectSql.append(" where 1=1 ");
			subSlectSql.append(paramSql);
			//根据bpmDataTemplate 表单模板获取排序select 语句
			//subSlectSql=dataTemplateService.getOrderBySql(subSlectSql, dataTemplate);
			fromSql.append("(").append(subSlectSql).append(")").append(dataSource);
		}
		return selectSql.append(fromSql).append(whereSql).append(orderSql);
	}

}
