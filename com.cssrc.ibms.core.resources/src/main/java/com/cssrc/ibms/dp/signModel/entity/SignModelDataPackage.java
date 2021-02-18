package com.cssrc.ibms.dp.signModel.entity;

import com.cssrc.ibms.dp.signModel.entity.CwmSysFile;
import com.cssrc.ibms.dp.signModel.entity.CwmSysSignModel;
import com.cssrc.ibms.dp.signModel.entity.WPadhcqzb;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "SignModelDataPackage")
@XmlAccessorType(XmlAccessType.FIELD)
public class SignModelDataPackage {

    @XmlElement(name="wPadhcqzbs")
    @XmlElementWrapper(name="wPadhcqzbsList")
    List<WPadhcqzb> wPadhcqzbsList;
    @XmlElement(name="cwmSysSignModels")
    @XmlElementWrapper(name="cwmSysSignModelsList")
    List<CwmSysSignModel> cwmSysSignModelsList;
    @XmlElement(name="cwmSysFile")
    @XmlElementWrapper(name="cwmSysFileList")
    List<CwmSysFile> cwmSysFileList;

    public List<WPadhcqzb> getwPadhcqzbsList() {
        return wPadhcqzbsList;
    }

    public void setwPadhcqzbsList(List<WPadhcqzb> wPadhcqzbsList) {
        this.wPadhcqzbsList = wPadhcqzbsList;
    }

    public List<CwmSysSignModel> getCwmSysSignModelsList() {
        return cwmSysSignModelsList;
    }

    public void setCwmSysSignModelsList(List<CwmSysSignModel> cwmSysSignModelsList) {
        this.cwmSysSignModelsList = cwmSysSignModelsList;
    }

    public List<CwmSysFile> getCwmSysFileList() {
        return cwmSysFileList;
    }

    public void setCwmSysFileList(List<CwmSysFile> cwmSysFileList) {
        this.cwmSysFileList = cwmSysFileList;
    }
}
