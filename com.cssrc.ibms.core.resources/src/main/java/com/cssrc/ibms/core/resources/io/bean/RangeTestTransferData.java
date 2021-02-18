package com.cssrc.ibms.core.resources.io.bean;

import com.cssrc.ibms.core.resources.datapackage.model.DataPackage;
import com.cssrc.ibms.core.resources.datapackage.model.RangeTestDataPackage;
import com.cssrc.ibms.core.resources.datapackage.model.RangeTestPlan;
import com.cssrc.ibms.dp.product.acceptance.bean.WorkBoard;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "rangeTestTransferData")
@XmlAccessorType(XmlAccessType.FIELD)
public class RangeTestTransferData {
    //策划
    RangeTestPlan rangeTestPlan;
    //工作项看板(暂时不要)
/*    WorkBoard workBoard;*/
    //数据包
    @XmlElement(name="dataPackage")
    @XmlElementWrapper(name="dataPackageList")
    List<DataPackage> dataPackageList;
    //策划的文件
    @XmlElement(name="fileData")
    @XmlElementWrapper(name="fileDataList")
    List<FileData> fileDataList;
    //实例
    @XmlElement(name="rangeTestInstanceBean")
    @XmlElementWrapper(name="rangeTestInstanceBeanList")
    List<RangeTestInstanceBean> rangeTestInstanceBeanList;



    public RangeTestPlan getRangeTestPlan() {
        return rangeTestPlan;
    }

    public void setRangeTestPlan(RangeTestPlan rangeTestPlan) {
        this.rangeTestPlan = rangeTestPlan;
    }

    public List<DataPackage> getDataPackageList() {
        return dataPackageList;
    }

    public void setDataPackageList(List<DataPackage> dataPackageList) {
        this.dataPackageList = dataPackageList;
    }

    public List<FileData> getFileDataList() {
        return fileDataList;
    }

    public void setFileDataList(List<FileData> fileDataList) {
        this.fileDataList = fileDataList;
    }

    public List<RangeTestInstanceBean> getRangeTestInstanceBeanList() {
        return rangeTestInstanceBeanList;
    }

    public void setRangeTestInstanceBeanList(List<RangeTestInstanceBean> rangeTestInstanceBeanList) {
        this.rangeTestInstanceBeanList = rangeTestInstanceBeanList;
    }
}
