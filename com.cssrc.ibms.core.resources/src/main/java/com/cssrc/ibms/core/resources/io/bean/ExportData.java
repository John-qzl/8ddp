package com.cssrc.ibms.core.resources.io.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.resources.io.bean.ins.SignResult;
import com.cssrc.ibms.core.resources.io.bean.ins.TableInstance;
import com.cssrc.ibms.core.resources.io.bean.template.SignDef;
import com.cssrc.ibms.core.resources.io.bean.template.TableTemp;
import com.cssrc.ibms.core.resources.product.controller.InstanceTableController;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.dp.form.model.CheckCondition;
import com.cssrc.ibms.system.model.SysFile;

@XmlRootElement(name = "exportData")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExportData {
	
	
	
	String name;
	
	@XmlElement(name="productCategoryBathBean")
	@XmlElementWrapper(name="productCategoryBathBeanList")
	List<ProductCategoryBathBean> productCategoryBathBean;
	
	
	
	@XmlElement(name="TableTempBean")
	@XmlElementWrapper(name="TableTempBeanList")
	List<TableTempBean> tableTempBeanList;
	

	
	

	public List<TableTempBean> getTableTempBeanList() {
		return tableTempBeanList;
	}

	public void setTableTempBeanList(List<TableTempBean> tableTempBeanList) {
		this.tableTempBeanList = tableTempBeanList;
	}

	


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ProductCategoryBathBean> getProductCategoryBathBean() {
		return productCategoryBathBean;
	}

	public void setProductCategoryBathBean(List<ProductCategoryBathBean> productCategoryBathBean) {
		this.productCategoryBathBean = productCategoryBathBean;
	}
	
	
	
}
