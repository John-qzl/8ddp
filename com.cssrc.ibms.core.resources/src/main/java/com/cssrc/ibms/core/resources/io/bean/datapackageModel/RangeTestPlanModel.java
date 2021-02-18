package com.cssrc.ibms.core.resources.io.bean.datapackageModel;

import com.cssrc.ibms.core.resources.datapackage.model.DataPackage;

import com.cssrc.ibms.core.resources.datapackage.model.RangeTestPlan;
import com.cssrc.ibms.core.resources.io.bean.TableTempBean;

import com.cssrc.ibms.core.resources.product.bean.ModuleManage;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.dp.product.acceptance.bean.AcceptanceGroup;

import com.cssrc.ibms.dp.product.acceptance.bean.WorkBoard;
import com.cssrc.ibms.system.model.SysFile;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 靶场试验某个策划的表单下发的数据包总节点
 * by zmz
 * 20200826
 */
@XmlRootElement(name = "rangeTestPlanModel")
@XmlAccessorType(XmlAccessType.FIELD)
public class RangeTestPlanModel {
    ModelBean modelBean;    //型号

    List<ModuleManage> moduleManageList;  //型号团队

    String name;    //压缩包名

    RangeTestPlan rangeTestPlan;    //策划

    WorkBoard workBoard;    //工作板

    @XmlElement(name="SysUser")
    @XmlElementWrapper(name="userList")
    List<SysUser> userList; //  实验组成员详细信息

    /*@XmlElement(name="RangeTestDataPackage")
    @XmlElementWrapper(name="rangeTestDataPackage")
    List<RangeTestDataPackage> rangeTestDataPackageList;*/    //数据包(要下发的表单

    @XmlElement(name="dataPackage")
    @XmlElementWrapper(name="dataPackageList")
    List<DataPackage> dataPackageList;

    @XmlElement(name="acceptanceGroup")
    @XmlElementWrapper(name="acceptanceGroupList")
    List<AcceptanceGroup> acceptanceGroupList;    //实验组成员

    @XmlElement(name="sysFile")
    @XmlElementWrapper(name="sysFileList")
    List<SysFile> sysFileList;      //试验依据文件

    @XmlElement(name="tableTempBean")
    @XmlElementWrapper(name="tableTempBeanList")
    List<TableTempBean> tableTempBeanList;

    public List<ModuleManage> getModuleManageList() {
        return moduleManageList;
    }

    public void setModuleManageList(List<ModuleManage> moduleManageList) {
        this.moduleManageList = moduleManageList;
    }

    public List<DataPackage> getDataPackageList() {
        return dataPackageList;
    }

    public void setDataPackageList(List<DataPackage> dataPackageList) {
        this.dataPackageList = dataPackageList;
    }

    public List<TableTempBean> getTableTempBeanList() {
        return tableTempBeanList;
    }

    public void setTableTempBeanList(List<TableTempBean> tableTempBeanList) {
        this.tableTempBeanList = tableTempBeanList;
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

    public RangeTestPlan getRangeTestPlan() {
        return rangeTestPlan;
    }

    public void setRangeTestPlan(RangeTestPlan rangeTestPlan) {
        this.rangeTestPlan = rangeTestPlan;
    }

    public WorkBoard getWorkBoard() {
        return workBoard;
    }

    public void setWorkBoard(WorkBoard workBoard) {
        this.workBoard = workBoard;
    }

    public List<SysUser> getUserList() {
        return userList;
    }

    public void setUserList(List<SysUser> userList) {
        this.userList = userList;
    }

/*    public List<RangeTestDataPackage> getRangeTestDataPackageList() {
        return rangeTestDataPackageList;
    }

    public void setRangeTestDataPackageList(List<RangeTestDataPackage> rangeTestDataPackageList) {
        this.rangeTestDataPackageList = rangeTestDataPackageList;
    }*/

/*    public List<RangeTestGroup> getRangeTestGroupList() {
        return rangeTestGroupList;
    }

    public void setRangeTestGroupList(List<RangeTestGroup> rangeTestGroupList) {
        this.rangeTestGroupList = rangeTestGroupList;
    }*/

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
}
