package com.cssrc.ibms.core.resources.io.bean.datapackageModel;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.resources.datapackage.model.DataPackage;
import com.cssrc.ibms.core.resources.io.bean.AcceptancePlan;
import com.cssrc.ibms.core.resources.io.bean.ProductCategoryBath;
import com.cssrc.ibms.core.resources.io.bean.TableTempBean;
import com.cssrc.ibms.core.resources.io.bean.template.TableTemp;
import com.cssrc.ibms.core.resources.product.bean.ModuleManage;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.dp.product.acceptance.bean.AcceptanceGroup;
import com.cssrc.ibms.dp.product.acceptance.bean.WorkBoard;
import com.cssrc.ibms.dp.product.acceptance.dao.WorkBoardDao;
import com.cssrc.ibms.system.model.SysFile;
import com.sun.tools.internal.ws.processor.model.Model;


@XmlRootElement(name = "acceptanceplanModel")
@XmlAccessorType(XmlAccessType.FIELD)
public class AcceptanceplanModel {
	ModelBean modelBean;
	
	String name;
	
	ProductCategoryBath productCategory;
	
	ProductCategoryBath productCategoryBath;
	
	AcceptancePlan acceptancePlan;
	
	List<ModuleManage> moduleManageList;
	
	WorkBoard workBoard;
	
	@XmlElement(name="SysUser")
	@XmlElementWrapper(name="userList")
	List<SysUser> userList;

	@XmlElement(name="tableTempBean")
	@XmlElementWrapper(name="tableTempBeanList")
	List<TableTempBean> tableTempBeanList;
	
	
	@XmlElement(name="dataPackage")
	@XmlElementWrapper(name="dataPackageList")
	List<DataPackage> dataPackageList;

	@XmlElement(name="acceptanceGroup")
	@XmlElementWrapper(name="acceptanceGroupList")
	List<AcceptanceGroup> acceptanceGroupList;
	
	@XmlElement(name="sysFile")
	@XmlElementWrapper(name="sysFileList")
	List<SysFile> sysFileList;
	
	
	

	public List<ModuleManage> getModuleManageList() {
		return moduleManageList;
	}

	public void setModuleManageList(List<ModuleManage> moduleManageList) {
		this.moduleManageList = moduleManageList;
	}

	public List<SysUser> getUserList() {
		return userList;
	}

	public void setUserList(List<SysUser> userList) {
		this.userList = userList;
	}

	public ModelBean getModelBean() {
		return modelBean;
	}

	public void setModelBean(ModelBean modelBean) {
		this.modelBean = modelBean;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProductCategoryBath getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategoryBath productCategory) {
		this.productCategory = productCategory;
	}

	public ProductCategoryBath getProductCategoryBath() {
		return productCategoryBath;
	}

	public void setProductCategoryBath(ProductCategoryBath productCategoryBath) {
		this.productCategoryBath = productCategoryBath;
	}

	public AcceptancePlan getAcceptancePlan() {
		return acceptancePlan;
	}

	public void setAcceptancePlan(AcceptancePlan acceptancePlan) {
		this.acceptancePlan = acceptancePlan;
	}

	public List<TableTempBean> getTableTempBeanList() {
		return tableTempBeanList;
	}

	public void setTableTempBeanList(List<TableTempBean> tableTempBeanList) {
		this.tableTempBeanList = tableTempBeanList;
	}

	public List<DataPackage> getDataPackageList() {
		return dataPackageList;
	}

	public void setDataPackageList(List<DataPackage> dataPackageList) {
		this.dataPackageList = dataPackageList;
	}

	public List<AcceptanceGroup> getAcceptanceGroupList() {
		return acceptanceGroupList;
	}

	public void setAcceptanceGroupList(List<AcceptanceGroup> acceptanceGroupList) {
		this.acceptanceGroupList = acceptanceGroupList;
	}

	public List<SysFile> getSysFileList() {
		return sysFileList;
	}

	public void setSysFileList(List<SysFile> sysFileList) {
		this.sysFileList = sysFileList;
	}

	public WorkBoard getWorkBoard() {
		return workBoard;
	}

	public void setWorkBoard(WorkBoard workBoard) {
		this.workBoard = workBoard;
	}
	
	
	
	
	
}
