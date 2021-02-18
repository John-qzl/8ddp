package com.cssrc.ibms.core.resources.io.bean;

import com.cssrc.ibms.system.model.SysFile;
import ij.plugin.ListVirtualStack;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "ArchiveFileBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class ArchiveFileBean {
    String xmlName="归档文件说明";

    //策划的依据文件
    @XmlElementWrapper(name="策划依据文件")
    @XmlElement(name="planBasisFile")
    List<SysFile> planBasisFileList;

    //实例的照片和签署
    @XmlElementWrapper(name="实例的照片和签署")
    @XmlElement(name="instancePhotoAndSign")
    List<SysFile> instancePhotoAndSignList;

    //总结的文件
    @XmlElementWrapper(name="总结的附件")
    @XmlElement(name="reportBasisFile")
    List<SysFile> reportBasisFileList;

    public List<SysFile> getPlanBasisFileList() {
        return planBasisFileList;
    }

    public void setPlanBasisFileList(List<SysFile> planBasisFileList) {
        this.planBasisFileList = planBasisFileList;
    }

    public List<SysFile> getInstancePhotoAndSignList() {
        return instancePhotoAndSignList;
    }

    public void setInstancePhotoAndSignList(List<SysFile> instancePhotoAndSignList) {
        this.instancePhotoAndSignList = instancePhotoAndSignList;
    }

    public List<SysFile> getReportBasisFileList() {
        return reportBasisFileList;
    }

    public void setReportBasisFileList(List<SysFile> reportBasisFileList) {
        this.reportBasisFileList = reportBasisFileList;
    }

    public String getXmlName() {
        return xmlName;
    }

    public void setXmlName(String xmlName) {
        this.xmlName = xmlName;
    }
}
