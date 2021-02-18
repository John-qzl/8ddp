package com.cssrc.ibms.core.resources.io.bean.ins;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 靶场试验表单实例表 w_bcsybgsl
 */
@XmlRootElement(name = "rangeTestTableIntance")
@XmlAccessorType(XmlAccessType.FIELD)
public class RangeTestTableIntance {
    private String id;
    private String F_BGMC;
    private String F_BGBH;
    private String F_NR;
    private String F_JCBMB;
    private String F_SSGZXM;
    private String F_VERSION;
    private String F_KSJCSJ;
    private String F_JSJCSJ;
    private String F_BDZL;
    private String F_SSCH;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getF_BGMC() {
        return F_BGMC;
    }

    public void setF_BGMC(String f_BGMC) {
        F_BGMC = f_BGMC;
    }

    public String getF_BGBH() {
        return F_BGBH;
    }

    public void setF_BGBH(String f_BGBH) {
        F_BGBH = f_BGBH;
    }

    public String getF_NR() {
        return F_NR;
    }

    public void setF_NR(String f_NR) {
        F_NR = f_NR;
    }

    public String getF_JCBMB() {
        return F_JCBMB;
    }

    public void setF_JCBMB(String f_JCBMB) {
        F_JCBMB = f_JCBMB;
    }

    public String getF_SSGZXM() {
        return F_SSGZXM;
    }

    public void setF_SSGZXM(String f_SSGZXM) {
        F_SSGZXM = f_SSGZXM;
    }

    public String getF_VERSION() {
        return F_VERSION;
    }

    public void setF_VERSION(String f_VERSION) {
        F_VERSION = f_VERSION;
    }

    public String getF_KSJCSJ() {
        return F_KSJCSJ;
    }

    public void setF_KSJCSJ(String f_KSJCSJ) {
        F_KSJCSJ = f_KSJCSJ;
    }

    public String getF_JSJCSJ() {
        return F_JSJCSJ;
    }

    public void setF_JSJCSJ(String f_JSJCSJ) {
        F_JSJCSJ = f_JSJCSJ;
    }

    public String getF_BDZL() {
        return F_BDZL;
    }

    public void setF_BDZL(String f_BDZL) {
        F_BDZL = f_BDZL;
    }

    public String getF_SSCH() {
        return F_SSCH;
    }

    public void setF_SSCH(String f_SSCH) {
        F_SSCH = f_SSCH;
    }
}
