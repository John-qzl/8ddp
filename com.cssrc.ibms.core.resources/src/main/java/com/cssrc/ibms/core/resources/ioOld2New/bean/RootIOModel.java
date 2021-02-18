package com.cssrc.ibms.core.resources.ioOld2New.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "product")
@XmlAccessorType(XmlAccessType.FIELD)
public class RootIOModel {
	@XmlAttribute
	private String productId;
	
	@XmlElement(name ="package")
	private List<PackageIOModel> packageList = new ArrayList<PackageIOModel>();
	
	@XmlElement(name ="folder")
	private List<TemplateFolderIOModel> templateFolderList = new ArrayList<TemplateFolderIOModel>();
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String name;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}

	public List<PackageIOModel> getPackageList() {
		return packageList;
	}
	public void setPackageList(List<PackageIOModel> packageList) {
		this.packageList = packageList;
	}
	public List<TemplateFolderIOModel> getTemplateFolderList() {
		return templateFolderList;
	}
	public void setTemplateFolderList(List<TemplateFolderIOModel> templateFolderList) {
		this.templateFolderList = templateFolderList;
	}
}
