package com.cssrc.ibms.core.resources.io.bean;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.resources.io.bean.pack.SimplePackage;
import com.cssrc.ibms.core.resources.io.bean.template.TableTemp;
import com.cssrc.ibms.core.resources.io.bean.template.TemplateFLoder;
import com.cssrc.ibms.core.util.common.CommonTools;

/**
 * @author wenjie
 * 发次bean
 */
@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
public class Project {
public Project() {
		
	}
	public Project(Map<String, Object> map) {
		this.fcdh = CommonTools.Obj2String(map.get("F_FCDH"));
		this.fcmc = CommonTools.Obj2String(map.get("F_FCMC"));
		this.id = CommonTools.Obj2String(map.get("ID"));
	}
	/**
	 * 主键
	 */
	@XmlAttribute
	private String id;
	
	/**
	 * 发次名称
	 */
	@XmlAttribute
	private String fcmc;
	
	/**
	 * 发次代号
	 */
	@XmlAttribute
	private String fcdh;
	/**
	 * 所属型号
	 */
	@XmlAttribute
	private String productName;
	/**
	 * 所属型号代号
	 */
	@XmlAttribute
	private String productDh;
	
	/**
	 * 数据包集合
	 */
	@XmlElement(name ="package")
	private List<SimplePackage> list;
	
	/**
	 * 表单模板文件夹集合
	 * @return
	 */
	@XmlElement(name ="templateFLoder")
	private List<TemplateFLoder> floders;
	
	/**
	 * 表单模板集合
	 * @return
	 */
	@XmlElement(name ="tableTemp")
	private List<TableTemp> tableTempList;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFcmc() {
		return fcmc;
	}
	public void setFcmc(String fcmc) {
		this.fcmc = fcmc;
	}
	public String getFcdh() {
		return fcdh;
	}
	public void setFcdh(String fcdh) {
		this.fcdh = fcdh;
	}
	public List<SimplePackage> getList() {
		return list;
	}
	public void setList(List<SimplePackage> list) {
		this.list = list;
	}
	public List<TemplateFLoder> getFloders() {
		return floders;
	}
	public void setFloders(List<TemplateFLoder> floders) {
		this.floders = floders;
	}
	public List<TableTemp> getTableTempList() {
		return tableTempList;
	}
	public void setTableTempList(List<TableTemp> tableTempList) {
		this.tableTempList = tableTempList;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDh() {
		return productDh;
	}
	public void setProductDh(String productDh) {
		this.productDh = productDh;
	}
}
