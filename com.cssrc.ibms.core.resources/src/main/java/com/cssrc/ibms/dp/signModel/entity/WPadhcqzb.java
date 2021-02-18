package com.cssrc.ibms.dp.signModel.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
@XmlRootElement(name = "WPadhcqzb")
@XmlAccessorType(XmlAccessType.FIELD)
public class WPadhcqzb {
    private String id;

    private String f_Qzid;

    private String f_Yh;

    private String f_Yhid;

    private String f_Padhcqm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getF_Qzid() {
        return f_Qzid;
    }

    public void setF_Qzid(String f_Qzid) {
        this.f_Qzid = f_Qzid;
    }

    public String getF_Yh() {
        return f_Yh;
    }

    public void setF_Yh(String f_Yh) {
        this.f_Yh = f_Yh;
    }

    public String getF_Yhid() {
        return f_Yhid;
    }

    public void setF_Yhid(String f_Yhid) {
        this.f_Yhid = f_Yhid;
    }

    public String getF_Padhcqm() {
        return f_Padhcqm;
    }

    public void setF_Padhcqm(String f_Padhcqm) {
        this.f_Padhcqm = f_Padhcqm;
    }
}