package com.cssrc.ibms.core.resources.datapackage.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * W_BCSYZB
 */
@XmlRootElement(name = "RangeTestDataPackage")
@XmlAccessorType(XmlAccessType.FIELD)
public class RangeTestGroup {
    private String id;
    private String F_ZW;
    private String F_XM;
    private String F_XMID;
    private String F_DW;
    private String F_DWID;
    private String F_FZXM;
    //这个是所属任务的任务id,不是任务名称,懒得改了 by zmz 20200826
    private String F_SSRW;
    private String F_SSSYZJ;
    private String F_QSID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getF_ZW() {
        return F_ZW;
    }

    public void setF_ZW(String f_ZW) {
        F_ZW = f_ZW;
    }

    public String getF_XM() {
        return F_XM;
    }

    public void setF_XM(String f_XM) {
        F_XM = f_XM;
    }

    public String getF_XMID() {
        return F_XMID;
    }

    public void setF_XMID(String f_XMID) {
        F_XMID = f_XMID;
    }

    public String getF_DW() {
        return F_DW;
    }

    public void setF_DW(String f_DW) {
        F_DW = f_DW;
    }

    public String getF_DWID() {
        return F_DWID;
    }

    public void setF_DWID(String f_DWID) {
        F_DWID = f_DWID;
    }

    public String getF_FZXM() {
        return F_FZXM;
    }

    public void setF_FZXM(String f_FZXM) {
        F_FZXM = f_FZXM;
    }

    public String getF_SSRW() {
        return F_SSRW;
    }

    public void setF_SSRW(String f_SSRW) {
        F_SSRW = f_SSRW;
    }

    public String getF_SSSYZJ() {
        return F_SSSYZJ;
    }

    public void setF_SSSYZJ(String f_SSSYZJ) {
        F_SSSYZJ = f_SSSYZJ;
    }

    public String getF_QSID() {
        return F_QSID;
    }

    public void setF_QSID(String f_QSID) {
        F_QSID = f_QSID;
    }
}
