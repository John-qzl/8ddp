package com.cssrc.ibms.core.resources.io.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.resources.datapackage.dao.DataPackageDao;
import com.cssrc.ibms.core.resources.datapackage.model.DataPackage;
import com.cssrc.ibms.dp.product.acceptance.bean.AcceptanceGroup;
import com.cssrc.ibms.dp.product.acceptance.bean.acceptanceReport;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceGroupDao;
import com.cssrc.ibms.system.model.SysFile;

@XmlRootElement(name = "productCategoryBathBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductCategoryBathBean {
	
	
	private ProductCategoryBath productCategoryBath;
	
	@XmlElement(name="sysFile")
	@XmlElementWrapper(name="sysFileList")
	List<SysFile> sysFileList;
	
	@XmlElement(name="acceptancePlanBean")
	@XmlElementWrapper(name="acceptancePlanBeanList")
	List<AcceptancePlanBean> acceptancePlanBeanList;

	

	public List<AcceptancePlanBean> getAcceptancePlanBeanList() {
		return acceptancePlanBeanList;
	}
	public void setAcceptancePlanBeanList(List<AcceptancePlanBean> acceptancePlanBeanList) {
		this.acceptancePlanBeanList = acceptancePlanBeanList;
	}
	public ProductCategoryBath getProductCategoryBath() {
		return productCategoryBath;
	}
	public void setProductCategoryBath(ProductCategoryBath productCategoryBath) {
		this.productCategoryBath = productCategoryBath;
	}
	public List<SysFile> getSysFileList() {
		return sysFileList;
	}
	public void setSysFileList(List<SysFile> sysFileList) {
		this.sysFileList = sysFileList;
	}

	
	
	
}
