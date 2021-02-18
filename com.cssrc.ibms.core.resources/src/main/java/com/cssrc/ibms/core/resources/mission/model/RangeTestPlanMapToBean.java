package com.cssrc.ibms.core.resources.mission.model;

import com.cssrc.ibms.core.util.common.CommonTools;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.text.ParseException;
import java.util.Map;

/**
 * 靶场及武器所检的策划信息表,是可以走系统CURD的类
 */
@XmlRootElement(name = "RangeTestAcceptancePlan")
@XmlAccessorType(XmlAccessType.FIELD)
public class RangeTestPlanMapToBean {
    public RangeTestPlanMapToBean() {}
    public RangeTestPlanMapToBean(Map<String,Object> map) throws ParseException {
        this.id = CommonTools.Obj2String(map.get("ID"));
        this.xhdh = CommonTools.Obj2String(map.get("F_XHDH"));
        this.syrwmc = CommonTools.Obj2String(map.get("F_SYRWMC"));
        this.wqxtbh = CommonTools.Obj2String(map.get("F_WQXTBH"));
        this.ddbh = CommonTools.Obj2String(map.get("F_DDBH"));
        this.sjdd = CommonTools.Obj2String(map.get("F_SJDD"));
        this.kssj = CommonTools.Obj2String(map.get("F_KSSJ"));
        this.jssj = CommonTools.Obj2String(map.get("F_JSSJ"));
        this.syfzr = CommonTools.Obj2String(map.get("F_SYFZR"));
        this.syfzrId = CommonTools.Obj2String(map.get("F_SYFZRID"));
        this.xhId = CommonTools.Obj2String(map.get("F_XHID"));
        this.spzt = CommonTools.Obj2String(map.get("F_SPZT"));
        this.syyjwj = CommonTools.Obj2String(map.get("F_SYYJWJ"));
        this.sybghcsj = CommonTools.Obj2String(map.get("F_SYBGHCSJ"));
        this.chbgbbh = CommonTools.Obj2String(map.get("F_CHBGBBH"));
        this.sydw = CommonTools.Obj2String(map.get("F_SYDW"));
        this.sydwId = CommonTools.Obj2String(map.get("F_SYDWID"));
        this.syjd = CommonTools.Obj2String(map.get("F_SYJD"));
        this.gdlczt = CommonTools.Obj2String(map.get("F_GDLCZT"));
        this.gdwj = CommonTools.Obj2String(map.get("F_GDWJ"));
        this.syzz = CommonTools.Obj2String(map.get("F_SYZZ"));
        this.syzzId = CommonTools.Obj2String(map.get("F_SYZZID"));
        this.fqr = CommonTools.Obj2String(map.get("F_FQR"));
        this.fqrId = CommonTools.Obj2String(map.get("F_FQRID"));
        this.bcbh = CommonTools.Obj2String(map.get("F_BCBH"));
        this.bcsybh = CommonTools.Obj2String(map.get("F_BCSYBH"));

    }

    @XmlElement(name = "id", required = false)
    private String id;
    @XmlElement(name = "xhdh", required = false)
    private String xhdh;
    @XmlElement(name = "syrwmc", required = false)
    private String syrwmc;
    @XmlElement(name = "wqxtbh", required = false)
    private String wqxtbh;
    @XmlElement(name = "ddbh", required = false)
    private String ddbh;
    @XmlElement(name = "sjdd", required = false)
    private String sjdd;
    @XmlElement(name = "kssj", required = false)
    private String kssj;
    @XmlElement(name = "jssj", required = false)
    private String jssj;
    @XmlElement(name = "syfzr", required = false)
    private String syfzr;
    @XmlElement(name = "syfzrId", required = false)
    private String syfzrId;

    @XmlElement(name = "xhId", required = false)
    private String xhId;

    @XmlElement(name = "spzt", required = false)
    private String spzt;

    @XmlElement(name = "syyjwj", required = false)
    private String syyjwj;

    @XmlElement(name = "sybghcsj", required = false)
    private String sybghcsj;

    @XmlElement(name = "chbgbbh", required = false)
    private String chbgbbh;

    @XmlElement(name = "sydw", required = false)
    private String sydw;

    @XmlElement(name = "sydwId", required = false)
    private String sydwId;

    @XmlElement(name = "syjd", required = false)
    private String syjd;

    @XmlElement(name = "gdlczt", required = false)
    private String gdlczt;

    @XmlElement(name = "gdwj", required = false)
    private String gdwj;

    @XmlElement(name = "syzz", required = false)
    private String syzz;

    @XmlElement(name = "syzzId", required = false)
    private String syzzId;

    @XmlElement(name = "fqr", required = false)
    private String fqr;

    @XmlElement(name = "fqrId", required = false)
    private String fqrId;

    @XmlElement(name = "bcbh", required = false)
    private String bcbh;

    @XmlElement(name = "bcsybh", required = false)
    private String bcsybh;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getXhdh() {
        return xhdh;
    }

    public void setXhdh(String xhdh) {
        this.xhdh = xhdh;
    }

    public String getSyrwmc() {
        return syrwmc;
    }

    public void setSyrwmc(String syrwmc) {
        this.syrwmc = syrwmc;
    }

    public String getWqxtbh() {
        return wqxtbh;
    }

    public void setWqxtbh(String wqxtbh) {
        this.wqxtbh = wqxtbh;
    }

    public String getDdbh() {
        return ddbh;
    }

    public void setDdbh(String ddbh) {
        this.ddbh = ddbh;
    }

    public String getSjdd() {
        return sjdd;
    }

    public void setSjdd(String sjdd) {
        this.sjdd = sjdd;
    }

    public String getKssj() {
        return kssj;
    }

    public void setKssj(String kssj) {
        this.kssj = kssj;
    }

    public String getJssj() {
        return jssj;
    }

    public void setJssj(String jssj) {
        this.jssj = jssj;
    }

    public String getSyfzr() {
        return syfzr;
    }

    public void setSyfzr(String syfzr) {
        this.syfzr = syfzr;
    }

    public String getSyfzrId() {
        return syfzrId;
    }

    public void setSyfzrId(String syfzrId) {
        this.syfzrId = syfzrId;
    }

    public String getXhId() {
        return xhId;
    }

    public void setXhId(String xhId) {
        this.xhId = xhId;
    }

    public String getSpzt() {
        return spzt;
    }

    public void setSpzt(String spzt) {
        this.spzt = spzt;
    }

    public String getSyyjwj() {
        return syyjwj;
    }

    public void setSyyjwj(String syyjwj) {
        this.syyjwj = syyjwj;
    }

    public String getSybghcsj() {
        return sybghcsj;
    }

    public void setSybghcsj(String sybghcsj) {
        this.sybghcsj = sybghcsj;
    }

    public String getChbgbbh() {
        return chbgbbh;
    }

    public void setChbgbbh(String chbgbbh) {
        this.chbgbbh = chbgbbh;
    }

    public String getSydw() {
        return sydw;
    }

    public void setSydw(String sydw) {
        this.sydw = sydw;
    }

    public String getSydwId() {
        return sydwId;
    }

    public void setSydwId(String sydwId) {
        this.sydwId = sydwId;
    }

    public String getSyjd() {
        return syjd;
    }

    public void setSyjd(String syjd) {
        this.syjd = syjd;
    }

    public String getGdlczt() {
        return gdlczt;
    }

    public void setGdlczt(String gdlczt) {
        this.gdlczt = gdlczt;
    }

    public String getGdwj() {
        return gdwj;
    }

    public void setGdwj(String gdwj) {
        this.gdwj = gdwj;
    }

    public String getSyzz() {
        return syzz;
    }

    public void setSyzz(String syzz) {
        this.syzz = syzz;
    }

    public String getSyzzId() {
        return syzzId;
    }

    public void setSyzzId(String syzzId) {
        this.syzzId = syzzId;
    }

    public String getFqr() {
        return fqr;
    }

    public void setFqr(String fqr) {
        this.fqr = fqr;
    }

    public String getFqrId() {
        return fqrId;
    }

    public void setFqrId(String fqrId) {
        this.fqrId = fqrId;
    }

    public String getBcbh() {
        return bcbh;
    }

    public void setBcbh(String bcbh) {
        this.bcbh = bcbh;
    }

    public String getBcsybh() {
        return bcsybh;
    }

    public void setBcsybh(String bcsybh) {
        this.bcsybh = bcsybh;
    }
}
