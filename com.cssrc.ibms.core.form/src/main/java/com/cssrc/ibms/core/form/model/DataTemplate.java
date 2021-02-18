package com.cssrc.ibms.core.form.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;

/**
 * <per> 对象功能:业务数据模板 Model 开发人员:zhulongchao </per>
 */
@XmlRootElement(name = "bpmDataTemplate")
@XmlAccessorType(XmlAccessType.NONE)
public class DataTemplate extends BaseModel implements IDataTemplate {

	// 主键
	@XmlAttribute
	protected Long id;
	// 自定义表ID
	@XmlAttribute
	protected Long tableId;
	// 自定义表单key
	@XmlAttribute
	protected Long formKey;
	// 名称
	@XmlAttribute
	protected String name;
	// 别名
	@XmlAttribute
	protected String alias;
	// 样式：0-列表，1-树形
	@XmlAttribute
	protected Short style = STYLE_LIST;
	// 是否需要分页：0-不分页，1-分页
	@XmlAttribute
	protected Short needPage = 1;
	// 分页大小
	@XmlAttribute
	protected Integer pageSize = 20;
	// 数据模板别名
	@XmlAttribute
	protected String templateAlias;
	// 数据模板代码
	@XmlAttribute
	protected String templateHtml;
	// 显示字段
	@XmlAttribute
	protected String displayField;
	// 条件字段
	@XmlAttribute
	protected String conditionField;
	// 排序字段
	@XmlAttribute
	protected String sortField;
	// 子表排序字段
	@XmlAttribute
	protected String subSortField;
	// 关联表排序字段
	@XmlAttribute
	protected String relSortField;
	// 管理字段
	@XmlAttribute
	protected String manageField;
	// 过滤条件
	@XmlAttribute
	protected String filterField;
	// 导出字段
	@XmlAttribute
	protected String exportField;
	// 打印字段
	@XmlAttribute
	protected String printField;

	// 变量字段
	@XmlAttribute
	protected String varField;
	// 过滤条件
	@XmlAttribute
	protected Short filterType = 0;

	// 流程定义Id
	@XmlAttribute
	protected Long defId;
	// 是否查询
	@XmlAttribute
	protected Short isQuery = 1;
	// 默认过滤条件
	@XmlAttribute
	protected Short isFilter = 1;
	//文件附件HTML by YangBo
	@XmlAttribute 
	protected String fileTempHtml;
	@XmlAttribute 
	protected String attacTempHtml;
	//流程监控HTML by Liubo
	@XmlAttribute 
	protected String processTempHtml;
	@XmlAttribute 
	protected String processCondition;
	
	@XmlAttribute 
	protected Short isBakData;
	
	/*数据模板与表单权限关联字段 by dwj*/
	@XmlAttribute 
	protected String recRightField;  
	@XmlAttribute 
	protected String multiTabTempHtml;
	/**
	 * 数据来源（ 1来自自定义表）
	 */
	@XmlAttribute
	protected String source = SOURCE_CUSTOM_TABLE;

	// 以下非数据库字段
	// 分页数据
	// private List<HashMap<String, Object>>list1=new ArrayList<HashMap<String,
	// Object>>();
	private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	// 分页bean。
	private PagingBean pageBean;
	// 流程定义标题
	private String subject;

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 返回 主键
	 * 
	 * @return
	 */
	public Long getId() {
		return this.id;
	}

	public Long getTableId() {
		return tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public Long getFormKey() {
		return formKey;
	}

	public void setFormKey(Long formKey) {
		this.formKey = formKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Short getStyle() {
		return style;
	}

	public void setStyle(Short style) {
		this.style = style;
	}

	public Short getNeedPage() {
		return needPage;
	}

	public void setNeedPage(Short needPage) {
		this.needPage = needPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getTemplateAlias() {
		return templateAlias;
	}

	public void setTemplateAlias(String templateAlias) {
		this.templateAlias = templateAlias;
	}

	public String getTemplateHtml() {
		return templateHtml;
	}

	public void setTemplateHtml(String templateHtml) {
		this.templateHtml = templateHtml;
	}

	public String getDisplayField() {
		return displayField;
	}

	public void setDisplayField(String displayField) {
		this.displayField = displayField;
	}

	public String getConditionField() {
		return conditionField;
	}

	public void setConditionField(String conditionField) {
		this.conditionField = conditionField;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getManageField() {
		return manageField;
	}

	public void setManageField(String manageField) {
		this.manageField = manageField;
	}

	public String getFilterField() {
		return filterField;
	}

	public void setFilterField(String filterField) {
		this.filterField = filterField;
	}

	public String getVarField() {
		return varField;
	}

	public void setVarField(String varField) {
		this.varField = varField;
	}

	public Short getFilterType() {
		return filterType;
	}

	public void setFilterType(Short filterType) {
		this.filterType = filterType;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	public PagingBean getPageBean() {
		if (pageBean == null)
			pageBean = new PagingBean(Integer.valueOf(1), pageSize.intValue());
		return pageBean;
	}

	public void setPageBean(PagingBean pageBean) {
		this.pageBean = pageBean;
	}

	public Long getDefId() {
		return defId;
	}

	public void setDefId(Long defId) {
		this.defId = defId;
	}

	public Short getIsQuery() {
		return isQuery;
	}

	public void setIsQuery(Short isQuery) {
		this.isQuery = isQuery;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Short getIsFilter() {
		return isFilter;
	}

	public void setIsFilter(Short isFilter) {
		this.isFilter = isFilter;
	}

	public String getExportField() {
		return exportField;
	}

	public void setExportField(String exportField) {
		this.exportField = exportField;
	}

	public String getPrintField() {
		return printField;
	}

	public void setPrintField(String printField) {
		this.printField = printField;
	}

	public String getSubSortField() {
		return subSortField;
	}

	public void setSubSortField(String subSortField) {
		this.subSortField = subSortField;
	}

	public String getRelSortField() {
		return relSortField;
	}

	public void setRelSortField(String relSortField) {
		this.relSortField = relSortField;
	}
	
	public String getFileTempHtml() {
		return fileTempHtml;
	}

	public void setFileTempHtml(String fileTempHtml) {
		this.fileTempHtml = fileTempHtml;
	}
	
	public String getAttacTempHtml() {
		return attacTempHtml;
	}
	public void setAttacTempHtml(String attacTempHtml) {
		this.attacTempHtml = attacTempHtml;
	}
	
	public String getProcessTempHtml() {
		return processTempHtml;
	}
	public void setProcessTempHtml(String processTempHtml) {
		this.processTempHtml = processTempHtml;
	}

	public String getProcessCondition() {
		return processCondition;
	}

	public void setProcessCondition(String processCondition) {
		this.processCondition = processCondition;
	}
	
	public Short getIsBakData()
    {
        return isBakData;
    }

    public void setIsBakData(Short isBakData)
    {
        this.isBakData = isBakData;
    }

    public String getRecRightField() {
		return recRightField;
	}

	public void setRecRightField(String recRightField) {
		this.recRightField = recRightField;
	}

	public String getMultiTabTempHtml() {
		return multiTabTempHtml;
	}

	public void setMultiTabTempHtml(String multiTabTempHtml) {
		this.multiTabTempHtml = multiTabTempHtml;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof DataTemplate)) {
			return false;
		}
		DataTemplate rhs = (DataTemplate) object;
		return new EqualsBuilder().append(this.id, rhs.id)
				.append(this.tableId, rhs.tableId)
				.append(this.formKey, rhs.formKey).append(this.name, rhs.name)
				.append(this.alias, rhs.alias).append(this.style, rhs.style)
				.append(this.needPage, rhs.needPage)
				.append(this.pageSize, rhs.pageSize)
				.append(this.templateAlias, rhs.templateAlias)
				.append(this.templateHtml, rhs.templateHtml)
				.append(this.displayField, rhs.displayField)
				.append(this.conditionField, rhs.conditionField)
				.append(this.sortField, rhs.sortField)
				.append(this.manageField, rhs.manageField)
				.append(this.filterField, rhs.filterField)
				.append(this.filterType, rhs.filterType)
				.append(this.varField, rhs.varField)
				.append(this.recRightField, rhs.recRightField).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.tableId).append(this.formKey).append(this.name)
				.append(this.alias).append(this.style).append(this.needPage)
				.append(this.pageSize).append(this.templateAlias)
				.append(this.templateHtml).append(this.displayField)
				.append(this.conditionField).append(this.sortField)
				.append(this.manageField).append(this.filterField)
				.append(this.filterType).append(this.varField)
				.append(this.recRightField)
				.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id)
				.append("tableId", this.tableId)
				.append("formKey", this.formKey).append("name", this.name)
				.append("alias", this.alias).append("style", this.style)
				.append("needPage", this.needPage)
				.append("pageSize", this.pageSize)
				.append("templateId", this.templateAlias)
				.append("templateHtml", this.templateHtml)
				.append("displayField", this.displayField)
				.append("conditionField", this.conditionField)
				.append("sortField", this.sortField)
				.append("manageField", this.manageField)
				.append("filterField", this.filterField)
				.append("filterType", this.filterType)
				.append("varField", this.varField)
				.append("recRightField", this.recRightField).toString();
	}

}