package com.cssrc.ibms.core.form.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.dom4j.Document;
import org.dom4j.Element;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.table.TableModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.form.model.IFormTable;

import net.sf.json.JSONArray;

/**
 * 对象功能:自定义表 Model对象 开发人员:zhulongchao
 */
@XmlRootElement(name = "table")
@XmlAccessorType(XmlAccessType.NONE)
public class FormTable extends BaseModel implements IFormTable {
	private static final long serialVersionUID = -5089427873782780713L;
	// tableId
	@XmlAttribute
	@SysFieldDescription(detail="表ID")
	protected Long tableId = 0L;
	// 表名
	@XmlAttribute
	@SysFieldDescription(detail="表名")
	protected String tableName = "";
	// 描述
	@XmlAttribute
	@SysFieldDescription(detail="描述")
	protected String tableDesc = "";
	// 是否主表 1-是 0-否
	@XmlAttribute
	@SysFieldDescription(detail="是否为主表",maps="{\"1\":\"主表\",\"0\":\"非主表\"}")
	protected Short isMain = 1;
	// 所属主表，该字段仅针对从表
	@XmlAttribute
	@SysFieldDescription(detail="所属主表")
	protected Long mainTableId = 0L;
	// 是否已发布
	@XmlAttribute
	@SysFieldDescription(detail="是否已发布",maps="{\"0\":\"未发布\",\"1\":\"已发布\"}")
	protected Short isPublished = 0;
	// 发布人
	@SysFieldDescription(detail="发布人")
	protected String publishedBy = "";
	// 发布时间
	@SysFieldDescription(detail="发布时间")
	protected java.util.Date publishTime;
	// 是否外部数据源
	@SysFieldDescription(detail="是否外部表",maps="{\""+NOT_EXTERNAL+"\":\"不是外部表\",\""+EXTERNAL+"\":\"是外部表\"}")
	protected int isExternal = NOT_EXTERNAL;
	// 数据源别名
	@SysFieldDescription(detail="数据源别名")
	protected String dsAlias = "";
	// 数据源
	@SysFieldDescription(detail="数据源")
	protected String dsName = "";
	// 外键字段字段
	@SysFieldDescription(detail="外键字段")
	protected String relation = "";
	// 键类型
	@SysFieldDescription(detail="键类型")
	protected Short keyType = 0;
	// 键值
	@SysFieldDescription(detail="键值")
	protected String keyValue = "";
	// 主键字段
	@SysFieldDescription(detail="主键字段")
	protected String pkField = "";
	// 列表模板
	@SysFieldDescription(detail="列表模板")
	protected String listTemplate = "";
	// 明细模板
	@SysFieldDescription(detail="明细模板")
	protected String detailTemplate = "";
	@SysFieldDescription(detail="是否有表单",maps="{\"0\":\"有\",\"1\":\"无\"}")
	protected Integer hasForm = 0;
	// 是否有表单产生(0,表设计,1,由表单生成)
	// 默认值为0
	@SysFieldDescription(detail="来源",maps="{\"0\":\"表设计\",\"1\":\"由表单生成\"}")
	protected Integer genByForm = 0;

	// 分组
	@XmlAttribute
	@SysFieldDescription(detail="分组")
	protected String team;
	/**
	 * 数据库主键类型。 主键数据类型(0,数字,1字符串)
	 */
	@XmlAttribute
	@SysFieldDescription(detail="数据库主键类型",maps="{\"0\":\"数字\",\"1\":\"字符串\"}")
	protected Short keyDataType = 0;

	/**
	 * 创建人
	 */
	@SysFieldDescription(detail="创建人")
	protected String creator;

	protected Set<String> hashSet = new HashSet<String>();
	// 字段信息
	@SysFieldDescription(detail="字段信息")
	protected List<FormField> fieldList = new ArrayList<FormField>();
	// 文件附件
	@SysFieldDescription(detail="文件附件")
	protected JSONArray attachJSONArry;
	// 子表列表
	@SysFieldDescription(detail="子表列表")
	protected List<FormTable> subTableList = new ArrayList<FormTable>();
	// 自定义rel关系表列表 获取mainTable被其他表引用的所有外键列。
	// *****不是获取当前表的所有外键列对应的表*****
	@SysFieldDescription(detail="主表被其他表引用的所有外键列")
	protected List<FormTable> relTableList = new ArrayList<FormTable>();
	// 取当期表的所有外键列
	@SysFieldDescription(detail="当前表的所有外键列")
	protected List<FormField> fkFieldList = new ArrayList<FormField>();
	// 获取关系列Map
	@SysFieldDescription(detail="获取关系列Map")
	protected Map relFieldMap = new HashMap();

	// 其它列表
	@SysFieldDescription(detail="其它列表")
	protected List<FormTable> otherTableList = new ArrayList<FormTable>();
	@SysFieldDescription(detail="父表ID")
	protected Long parentId;
	@SysFieldDescription(detail="isRoot")
	protected Short isRoot;
	@SysFieldDescription(detail="变量")
	protected Map<String, String> variable = new HashMap<String, String>();
	@SysFieldDescription(detail="主表描述")
	protected String mainTableDesc;
	
	
	public static FormTable getTable(){
		FormTable table = new FormTable();
		table.setIsMain(IFormTable.IS_MAIN);
		return table;
	}
	
	/**
	 * 主表名称
	 */
	@SysFieldDescription(detail="主表名称")
	protected String mainTableName = "";

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	/**
	 * 返回 tableId
	 * 
	 * @return
	 */
	public Long getTableId() {
		return tableId;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Short getIsRoot() {
		return isRoot;
	}

	public void setIsRoot(Short isRoot) {
		this.isRoot = isRoot;
	}

	public Map<String, String> getVariable() {
		return variable;
	}

	public void setVariable(Map<String, String> variable) {
		this.variable = variable;
	}

	/**
	 * 返回 表名
	 * 
	 * @return
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 数据库真正的表名
	 * 
	 * @return
	 */
	public String getFactTableName() {
		return this.isExtTable() ? tableName : "W_" + this.tableName;
	}

	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}

	/**
	 * 返回 描述
	 * 
	 * @return
	 */
	public String getTableDesc() {
		return tableDesc;
	}

	public void setIsMain(Short isMain) {
		this.isMain = isMain;
	}

	/**
	 * 返回 是否主表 1-是 0-否
	 * 
	 * @return
	 */
	public Short getIsMain() {
		return isMain;
	}

	public void setMainTableId(Long mainTableId) {
		this.mainTableId = mainTableId;
	}

	/**
	 * 返回 所属主表，该字段仅针对从表
	 * 
	 * @return
	 */
	public Long getMainTableId() {
		return mainTableId;
	}

	public void setIsPublished(Short isPublished) {
		this.isPublished = isPublished;
	}

	/**
	 * 返回 是否已发布
	 * 
	 * @return
	 */
	public Short getIsPublished() {
		return isPublished;
	}

	public void setPublishedBy(String publishedBy) {
		this.publishedBy = publishedBy;
	}

	/**
	 * 返回 发布人
	 * 
	 * @return
	 */
	public String getPublishedBy() {
		return publishedBy;
	}

	public void setPublishTime(java.util.Date publishTime) {
		this.publishTime = publishTime;
	}

	/**
	 * 返回 发布时间
	 * 
	 * @return
	 */
	public java.util.Date getPublishTime() {
		return publishTime;
	}

	/**
	 * 是否外部表 0，本地表 1，外地表
	 * 
	 * @return
	 */
	public int getIsExternal() {
		return isExternal;
	}

	public void setIsExternal(int isExternal) {
		this.isExternal = isExternal;
	}

	/**
	 * 是否外部表
	 * 
	 * @return
	 */
	public boolean isExtTable() {
		return isExternal == EXTERNAL;
	}

	/**
	 * 数据源别名
	 * 
	 * @return
	 */
	public String getDsAlias() {
		return dsAlias;
	}

	public void setDsAlias(String dsAlias) {
		this.dsAlias = dsAlias;
	}

	/**
	 * 数据源名称
	 * 
	 * @return
	 */
	public String getDsName() {
		return dsName;
	}

	public void setDsName(String dsName) {
		this.dsName = dsName;
	}

	/**
	 * 取得表间关联关系。
	 * 
	 * @return
	 */
	public String getRelation() {
		if (isExtTable())
			return relation;
		return "REFID";
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	/**
	 * 取得键类型。
	 * 
	 * @return
	 */
	public Short getKeyType() {
		return keyType;
	}

	public void setKeyType(Short keyType) {
		this.keyType = keyType;
	}

	/**
	 * 键值。
	 * 
	 * @return
	 */
	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	/**
	 * 主键字段
	 * 
	 * @return
	 */
	public String getPkField() {
		if (isExtTable())
			return pkField;
		return "ID";
	}

	public void setPkField(String pkField) {
		this.pkField = pkField;
	}

	public String getListTemplate() {
		return listTemplate;
	}

	public void setListTemplate(String listTemplate) {
		this.listTemplate = listTemplate;
	}

	public String getDetailTemplate() {
		return detailTemplate;
	}

	public void setDetailTemplate(String detailTemplate) {
		this.detailTemplate = detailTemplate;
	}

	public Integer getHasForm() {
		return hasForm;
	}

	public void setHasForm(Integer hasForm) {
		this.hasForm = hasForm;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getMainTableDesc() {
		return mainTableDesc;
	}

	public void setMainTableDesc(String mainTableDesc) {
		this.mainTableDesc = mainTableDesc;
	}

	// /**
	// * 取得关联关系。
	// * @return
	// */
	// public TableRelation getTableRelation() {
	// if (this.isExternal == 0)
	// return null;
	// if (StringUtil.isEmpty(relation))
	// return null;
	// return BpmFormTable.getRelationsByXml(this.relation);
	// }

	/**
	 * 根据xml取得关联关系。 xml格式为：
	 * 
	 * <pre>
	 * &lt;relation pk='主键表'>
	 * &lt;table name='外键表' fk="外键字段" />
	 * &lt;!--可以关联多个表-->
	 * &lt;/relation>
	 * </pre>
	 * 
	 * @param relationXml
	 * @return
	 */
	@SuppressWarnings("unchecked")
	// public static TableRelation getRelationsByXml(String relationXml) {
	// if (StringUtil.isEmpty(relationXml))
	// return null;
	// Document dom = Dom4jUtil.loadXml(relationXml);
	// Element root = dom.getRootElement();
	// String pk = root.attributeValue("pk");
	// TableRelation tableRelation = new TableRelation(pk);
	// Iterator<Element> tbIt = root.elementIterator();
	// while (tbIt.hasNext()) {
	// Element elTb = tbIt.next();
	// String tbName = elTb.attributeValue("name");
	// String fk = elTb.attributeValue("fk");
	// tableRelation.addRelation(tbName, fk);
	// }
	// return tableRelation;
	// }
	/**
	 * 修改xml字段。
	 * 
	 * @param relationXml
	 *            关联的XML。
	 * @param tbName
	 *            表名。
	 * @return
	 */
	public static String removeTable(String relationXml, String tbName) {
		Document dom = Dom4jUtil.loadXml(relationXml);
		Element root = dom.getRootElement();
		List<Element> list = root.elements();
		for (Element el : list) {
			String name = el.attributeValue("name");
			if (name.equals(tbName)) {
				root.remove(el);
				break;
			}
		}
		list = root.elements();
		if (list.size() == 0)
			return "";
		return root.asXML();
	}

	/**
	 * 添加字段。
	 * 
	 * <pre>
	 * 	1.做重复新检测。
	 *  2.重复字段返回失败。
	 * </pre>
	 * 
	 * @param field
	 */
	public boolean addField(FormField field) {
		String fieldName = field.getFieldName().toLowerCase();
		short controlType = field.getControlType();

		if (hashSet.contains(fieldName)) {
			// 如果字段类型为复选框或者单选按钮的时候可以重复。 13 14
			if (controlType == IFieldPool.CHECKBOX
					|| controlType == IFieldPool.RADIO_INPUT) {
				return true;
			}
			return false;
		}
		;
		hashSet.add(fieldName);
		this.fieldList.add(field);
		return true;
	}

	public List<FormField> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<FormField> fieldList) {
		this.fieldList = fieldList;
	}

	public JSONArray getAttachJSONArry() {
		return attachJSONArry;
	}

	public void setAttachJSONArry(JSONArray attachJSONArry) {
		this.attachJSONArry = attachJSONArry;
	}

	public List<FormTable> getSubTableList() {
		return subTableList;
	}

	public void setSubTableList(List<FormTable> subTableList) {
		this.subTableList = subTableList;
	}

	public List<FormTable> getOtherTableList() {
		return otherTableList;
	}

	public void setOtherTableList(List<FormTable> otherTableList) {
		this.otherTableList = otherTableList;
	}

	public Integer getGenByForm() {
		return genByForm;
	}

	public void setGenByForm(Integer genByForm) {
		this.genByForm = genByForm;
	}

	public Short getKeyDataType() {
		return keyDataType;
	}

	public void setKeyDataType(Short keyDataType) {
		this.keyDataType = keyDataType;
	}

	public String getMainTableName() {
		return mainTableName;
	}

	public void setMainTableName(String mainTableName) {
		this.mainTableName = mainTableName;
	}

	public List<FormTable> getRelTableList() {
		return relTableList;
	}

	public void setRelTableList(List<FormTable> relTableList) {
		this.relTableList = relTableList;
	}

	public Map getRelFieldMap() {
		return relFieldMap;
	}

	public void setRelFieldMap(Map relFieldMap) {
		this.relFieldMap = relFieldMap;
	}
	public String getDbTableName() {
		if (isExtTable()) {
			return this.tableName;
		}
		return TableModel.CUSTOMER_TABLE_PREFIX + this.tableName;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof FormTable)) {
			return false;
		}
		FormTable rhs = (FormTable) object;
		return new EqualsBuilder().append(this.tableId, rhs.tableId).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.tableId)
				.append(this.tableName).append(this.tableDesc)
				.append(this.isMain).append(this.mainTableId)

				.append(this.isPublished).append(this.publishedBy)
				.append(this.publishTime).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("tableId", this.tableId)
				.append("tableName", this.tableName)
				.append("tableDesc", this.tableDesc)
				.append("isMain", this.isMain)
				.append("mainTableId", this.mainTableId)

				.append("isPublished", this.isPublished)
				.append("publishedBy", this.publishedBy)
				.append("publishTime", this.publishTime).toString();
	}

	public void setFkFieldList(List<FormField> fkFieldList) {
		this.fkFieldList = fkFieldList;
	}

	// 获取当前表的所有外键列的"自定义对话框"信息
	public Map<String, Map<String, Object>> genFKColumnShowRelFormDialogMap() {
		Map relFormDialogMap = new HashMap();
		List<FormField> fieldList = this.getFieldList();
		for (FormField formField : fieldList) {
			Short controlType = formField.getControlType();
			Long tableId = formField.getTableId();
			if (controlType.shortValue() == IFieldPool.RELATION_COLUMN_CONTROL) {
				String fieldName = formField.getFieldName();
				String relFormDialog = formField.getRelFormDialog();
				relFormDialogMap.put(formField.getFieldName(), formField
						.getFKColumnShowMapByRelFormDialog(relFormDialog));
			}
		}
		return relFormDialogMap;
	}

	public Set<String> getHashSet() {
		return hashSet;
	}

	public void setHashSet(Set<String> hashSet) {
		this.hashSet = hashSet;
	}

	public List<FormField> getFkFieldList() {
		return fkFieldList;
	}
	
}