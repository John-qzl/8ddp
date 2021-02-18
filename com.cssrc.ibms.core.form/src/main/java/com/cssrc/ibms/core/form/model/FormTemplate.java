package com.cssrc.ibms.core.form.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.form.model.IFormTemplate;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;

/**
 * 对象功能:表单模板 Model对象 开发人员:zhulongchao
 */
public class FormTemplate extends BaseModel implements IFormTemplate {
	private static final long serialVersionUID = -3452062660055269211L;
	// 模板Id
	@SysFieldDescription(detail="模板Id")
	protected Long templateId;
	// 模板名
	@SysFieldDescription(detail="模板名称")
	protected String templateName;
	// 模板别名
	@SysFieldDescription(detail="模板别名")
	protected String alias;
	// 模板类型 1-主表模板 2-子表模板 3-宏模板
	@SysFieldDescription(detail="模板类型",maps="{\"1\":\"主表模板\",\"2\":\"子表模板\",\"3\":\"宏模板\"}")
	protected String templateType;
	// 使用宏模板别名
	@SysFieldDescription(detail="使用宏模板别名")
	protected String macroTemplateAlias;
	// 模板html
	@SysFieldDescription(detail="模板html")
	protected String html;
	// 描述
	@SysFieldDescription(detail="描述")
	protected String templateDesc;
	// 是否可以被修改 0-不可修改 1-可以修改
	@SysFieldDescription(detail="是否可被修改",maps="{\"0\":\"不可修改\",\"1\":\"可以修改\"}")
	protected int canEdit;
	//业务数据列表、自定义sql列表中自定义js，css
	@SysFieldDescription(detail="自定义js,css")
	protected String headHtml;
	/**
	 * 返回 模板别名
	 * 
	 * @return
	 */
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * 是否可以修改
	 * 
	 * @return
	 */
	public int getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(int canEdit) {
		this.canEdit = canEdit;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	/**
	 * 返回 模板Id
	 * 
	 * @return
	 */
	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * 返回 模板名
	 * 
	 * @return
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * 返回 模板类型
	 * 
	 * @return
	 */
	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	/**
	 * 返回宏模板别名
	 * 
	 * @param macroTemplateAlias
	 */
	public String getMacroTemplateAlias() {
		return macroTemplateAlias;
	}

	public void setMacroTemplateAlias(String macroTemplateAlias) {
		this.macroTemplateAlias = macroTemplateAlias;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	/**
	 * 返回 模板html
	 * 
	 * @return
	 */
	public String getHtml() {
		return html;
	}

	public void setTemplateDesc(String templateDesc) {
		this.templateDesc = templateDesc;
	}

	/**
	 * 返回 描述
	 * 
	 * @return
	 */
	public String getTemplateDesc() {
		return templateDesc;
	}

	public String getHeadHtml() {
		return headHtml;
	}

	public void setHeadHtml(String headHtml) {
		this.headHtml = headHtml;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof FormTemplate)) {
			return false;
		}
		FormTemplate rhs = (FormTemplate) object;
		return new EqualsBuilder().append(this.templateId, rhs.templateId)
				.append(this.templateName, rhs.templateName)
				.append(this.templateType, rhs.templateType)
				.append(this.macroTemplateAlias, rhs.macroTemplateAlias)
				.append(this.html, rhs.html)
				.append(this.templateDesc, rhs.templateDesc)
				.append(this.headHtml, rhs.headHtml).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973)
				.append(this.templateId).append(this.templateName)
				.append(this.templateType).append(this.macroTemplateAlias)
				.append(this.html).append(this.templateDesc)
				.append(this.headHtml).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("templateId", this.templateId)
				.append("templateName", this.templateName)
				.append("templateType", this.templateType)
				.append("macroTemplateId", this.macroTemplateAlias)
				.append("html", this.html)
				.append("templateDesc", this.templateDesc)
				.append("headHtml", this.headHtml)
				.toString();
	}

}