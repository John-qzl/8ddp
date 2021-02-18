package com.cssrc.ibms.core.resources.io.bean;

import com.cssrc.ibms.system.model.SysFile;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 靶场试验策划的导入导出
 */
@XmlRootElement(name = "tableTempBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class RangeTestPlanBean {

    RangeTestTransferData rangeTestTransferData;

    @XmlElement(name="photoFile")
    @XmlElementWrapper(name="photoFileList")
    List<SysFile> photoFileList;

    @XmlElement(name="tableTempBean")
    @XmlElementWrapper(name="tableTempBeanList")
    List<TableTempBean> tableTempBeanList;

    public List<TableTempBean> getTableTempBeanList() {
        return tableTempBeanList;
    }

    public void setTableTempBeanList(List<TableTempBean> tableTempBeanList) {
        this.tableTempBeanList = tableTempBeanList;
    }

    public RangeTestTransferData getRangeTestTransferData() {
        return rangeTestTransferData;
    }

    public void setRangeTestTransferData(RangeTestTransferData rangeTestTransferData) {
        this.rangeTestTransferData = rangeTestTransferData;
    }

    public List<SysFile> getPhotoFileList() {
        return photoFileList;
    }

    public void setPhotoFileList(List<SysFile> photoFileList) {
        this.photoFileList = photoFileList;
    }
}

