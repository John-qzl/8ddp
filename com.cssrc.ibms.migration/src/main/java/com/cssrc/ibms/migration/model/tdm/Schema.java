package com.cssrc.ibms.migration.model.tdm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.util.common.PinyinUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.migration.model.tdm.restriction.Restriction;
import com.cssrc.ibms.migration.model.tdm.restriction.SimpleRestriciton;
import com.cssrc.ibms.migration.model.tdm.table.Table;
import com.cssrc.ibms.migration.model.tdm.view.View;

@XmlRootElement(name = "schema")
@XmlAccessorType(XmlAccessType.NONE)
public class Schema {
	@XmlAttribute(name="数据模型别名")
	private String alias;
	
	@XmlAttribute(name="数据模型名称")
	private String name;
	
	@XmlAttribute(name="版本号")
	private String version;
	
	/** 对应该业务库能够使用的密级 */
	@XmlAttribute(name="密级设置")
	private String secrecySet;
	
	/** 对应schema的类型 0：普通业务库 1：共享业务库 */
	@XmlAttribute(name="类型")
	private String type;
	
	/** 对应所有的数据类 */
	@XmlElementWrapper(name="数据类")
	@XmlElement(name="children")
	private List<Table> tables;

	/** 对应所有的约束 */
	@XmlElement(name="数据约束")
	private Restriction restriction;
	
	/** 对应所有的视图 */
	@XmlElementWrapper(name="数据视图")
	@XmlElement(name="view")
	private List<View> views;


	public  SimpleRestriciton getSchemaRes(){
		SimpleRestriciton sr = new SimpleRestriciton();
		sr.setDisplayName(name);
		sr.setDescription("");
		sr.setName(PinyinUtil.getPinYinHeadChar(name));
		sr.setSn(100L);
		return sr;
	}
	public String getName() {
		//schema信息调整
		return StringUtil.EnglishRemove(name);
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getSecrecySet() {
		return secrecySet;
	}

	public void setSecrecySet(String secrecySet) {
		this.secrecySet = secrecySet;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Restriction getRestriction() {
		return restriction;
	}

	public void setRestriction(Restriction restriction) {
		this.restriction = restriction;
	}

	public List<Table> getTables() {
		return tables;
	}

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}

	public List<View> getViews() {
		return views;
	}

	public void setViews(List<View> views) {
		this.views = views;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
}
