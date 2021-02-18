package com.cssrc.ibms.core.form.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

/**
 * <per>
 * 对象功能:业务数据模板 Model 
 * 开发人员:zhulongchao 
 * </per>
 */
@XmlRootElement(name = "bpmReportTemplate")
@XmlAccessorType(XmlAccessType.NONE)
public class ReportTemplate extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 主键
	@XmlAttribute
	protected Long id;
	// 自定义表单key
	@XmlAttribute
	protected Long formKey;
	// 自定义表单tableId
	@XmlAttribute
	protected Long tableId;
	// 名称
	@XmlAttribute
	protected String name;
	// 业务数据表字段拼接串
	@XmlAttribute
	protected String content;
	// 流程数据表字段拼接串
	@XmlAttribute
	protected String content_spyj;
	// 文件路径
	@XmlAttribute
	protected String filepath;
	// 创建人
	@XmlAttribute
	protected String create_user;
	// 创建时间
	@XmlAttribute
	protected Date create_time;
	//报表类型
	@XmlAttribute
	protected String type;
	
	/**
	 * 主键ID
	 * @return
	 */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 自定义表单key formKey
	 * @return
	 */
	public Long getFormKey() {
		return formKey;
	}
	public void setFormKey(Long formKey) {
		this.formKey = formKey;
	}
	/**
	 * 自定义表单 tableId
	 * @return
	 */
	public Long getTableId() {
		return tableId;
	}
	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}
	/**
	 * 名称 name
	 * @return
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 业务数据表字段拼接串 content
	 * @return
	 */
	public String getContent() {
		return content;
	}
	public void setContent(String content
			) {
		this.content = content;
	}
	/**
	 * 流程数据表字段拼接串 filepath
	 * @return
	 */
	public String getContent_spyj() {
		return content_spyj;
	}
	public void setContent_spyj(String contentSpyj) {
		content_spyj = contentSpyj;
	}
	/**
	 * 文件路径 filepath
	 * @return
	 */
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	/**
	 *  创建人 create_user
	 * @return
	 */
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String createUser) {
		create_user = createUser;
	}
	/**
	 * 创建时间 create_time
	 * @return
	 */
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date createTime) {
		create_time = createTime;
	}
	/**
	 * 报表类型 type
	 * @return
	 */
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	

}