package com.cssrc.ibms.core.resources.mission.model;

import com.cssrc.ibms.core.util.common.CommonTools;

import javax.xml.bind.annotation.XmlElement;
import java.util.Map;

/**
 * 所级和试验的数据确认类
 *
 */
public class DataConfirmMapToBean {
    public DataConfirmMapToBean(){}
    public DataConfirmMapToBean(Map<String,Object> map){
        this.id = CommonTools.Obj2String(map.get("ID"));
        this.xfr = CommonTools.Obj2String(map.get("F_XFR"));
        this.xfrId = CommonTools.Obj2String(map.get("F_XFRID"));
        this.dbnr = CommonTools.Obj2String(map.get("F_DBNR"));
        this.blr = CommonTools.Obj2String(map.get("F_BLR"));
        this.blrId = CommonTools.Obj2String(map.get("F_BLRID"));
        this.blzt = CommonTools.Obj2String(map.get("F_BLZT"));
        this.jhwcsj = CommonTools.Obj2String(map.get("F_JHWCSJ"));
        this.bhqk = CommonTools.Obj2String(map.get("F_BHQK"));
        this.fj = CommonTools.Obj2String(map.get("F_FJ"));
        this.fkjg = CommonTools.Obj2String(map.get("F_FKJG"));
        this.sszj = CommonTools.Obj2String(map.get("F_SSZJ"));
        this.dbjj = CommonTools.Obj2String(map.get("F_DBJJ"));
        this.sjwcsj = CommonTools.Obj2String(map.get("F_SJWCSJ"));
    }
    @XmlElement(name = "id", required = false)
    private String id;
    @XmlElement(name = "xfr", required = false)
    private String xfr;
    @XmlElement(name = "xfrId", required = false)
    private String xfrId;
    @XmlElement(name = "dbnr", required = false)
    private String dbnr;
    @XmlElement(name = "blr", required = false)
    private String blr;
    @XmlElement(name = "blrId", required = false)
    private String blrId;
    @XmlElement(name = "blzt", required = false)
    private String blzt;
    @XmlElement(name = "jhwcsj", required = false)
    private String jhwcsj;
    @XmlElement(name = "bhqk", required = false)
    private String bhqk;
    @XmlElement(name = "fj", required = false)
    private String fj;
    @XmlElement(name = "fkjg", required = false)
    private String fkjg;
    @XmlElement(name = "sszj", required = false)
    private String sszj;
    @XmlElement(name = "dbjj", required = false)
    private String dbjj;
    @XmlElement(name = "sjwcsj", required = false)
    private String sjwcsj;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getXfr() {
        return xfr;
    }

    public void setXfr(String xfr) {
        this.xfr = xfr;
    }

    public String getXfrId() {
        return xfrId;
    }

    public void setXfrId(String xfrId) {
        this.xfrId = xfrId;
    }

    public String getDbnr() {
        return dbnr;
    }

    public void setDbnr(String dbnr) {
        this.dbnr = dbnr;
    }

    public String getBlr() {
        return blr;
    }

    public void setBlr(String blr) {
        this.blr = blr;
    }

    public String getBlrId() {
        return blrId;
    }

    public void setBlrId(String blrId) {
        this.blrId = blrId;
    }

    public String getBlzt() {
        return blzt;
    }

    public void setBlzt(String blzt) {
        this.blzt = blzt;
    }

    public String getJhwcsj() {
        return jhwcsj;
    }

    public void setJhwcsj(String jhwcsj) {
        this.jhwcsj = jhwcsj;
    }

    public String getBhqk() {
        return bhqk;
    }

    public void setBhqk(String bhqk) {
        this.bhqk = bhqk;
    }

    public String getFj() {
        return fj;
    }

    public void setFj(String fj) {
        this.fj = fj;
    }

    public String getFkjg() {
        return fkjg;
    }

    public void setFkjg(String fkjg) {
        this.fkjg = fkjg;
    }

    public String getSszj() {
        return sszj;
    }

    public void setSszj(String sszj) {
        this.sszj = sszj;
    }

    public String getDbjj() {
        return dbjj;
    }

    public void setDbjj(String dbjj) {
        this.dbjj = dbjj;
    }

    public String getSjwcsj() {
        return sjwcsj;
    }

    public void setSjwcsj(String sjwcsj) {
        this.sjwcsj = sjwcsj;
    }
}

