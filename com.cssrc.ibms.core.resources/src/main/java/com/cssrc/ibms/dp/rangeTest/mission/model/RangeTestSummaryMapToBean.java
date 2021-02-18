package com.cssrc.ibms.dp.rangeTest.mission.model;

import com.cssrc.ibms.core.util.common.CommonTools;

import javax.xml.bind.annotation.XmlElement;
import java.util.Map;

public class RangeTestSummaryMapToBean {
    public RangeTestSummaryMapToBean(){

    }
    public RangeTestSummaryMapToBean(Map<String, Object> map){
        this.id = CommonTools.Obj2String(map.get("ID"));
        this.bgbbh = CommonTools.Obj2String(map.get("F_BGBBH"));
        this.ssxh = CommonTools.Obj2String(map.get("F_SSXH"));
        this.ssxhId = CommonTools.Obj2String(map.get("F_SSXHID"));
        this.csdw = CommonTools.Obj2String(map.get("F_CSDW"));
        this.csdwId = CommonTools.Obj2String(map.get("F_CSDWID"));
        this.csqr = CommonTools.Obj2String(map.get("F_CSRQ"));
        this.syf = CommonTools.Obj2String(map.get("F_SYF"));
        this.sydd = CommonTools.Obj2String(map.get("F_SYDD"));
        this.syrwbh = CommonTools.Obj2String(map.get("F_SYRWBH"));
        this.syqkcgjc = CommonTools.Obj2String(map.get("F_SYQKCGJC"));
        this.syqkgnxnjc = CommonTools.Obj2String(map.get("F_SYQKGNXNJC"));
        this.syfqsrq = CommonTools.Obj2String(map.get("F_SYFQSRQ"));
        this.syzzqsrq = CommonTools.Obj2String(map.get("F_SYZZQSRQ"));
        this.syfId = CommonTools.Obj2String(map.get("F_SYFID"));
        this.qtsm = CommonTools.Obj2String(map.get("F_QTSM"));
        this.ylwtbhqk = CommonTools.Obj2String(map.get("F_YLWTBHQK"));
        this.bmfzrId = CommonTools.Obj2String(map.get("F_BMFZRID"));
        this.bmfzr = CommonTools.Obj2String(map.get("F_BMFZR"));
        this.bmfzrqsrq = CommonTools.Obj2String(map.get("F_BMFZRQSRQ"));
        this.bhqkqr = CommonTools.Obj2String(map.get("F_BHQKQR"));
        this.xhzls = CommonTools.Obj2String(map.get("F_XHZLS"));
        this.xhzlsId = CommonTools.Obj2String(map.get("F_XHZLSID"));
        this.xhzlsqsrq = CommonTools.Obj2String(map.get("F_XHZLSQSRQ"));
        this.ssch = CommonTools.Obj2String(map.get("F_SSCH"));
        this.spzt = CommonTools.Obj2String(map.get("F_SPZT"));
        this.fj = CommonTools.Obj2String(map.get("F_FJ"));
        this.lx = CommonTools.Obj2String(map.get("F_LX"));
        this.syzfxdzywt = CommonTools.Obj2String(map.get("F_SYZFXDZYWT"));
        this.syzyj = CommonTools.Obj2String(map.get("F_SYZYJ"));
        this.syzz = CommonTools.Obj2String(map.get("F_SYZZ"));
        this.syzzId = CommonTools.Obj2String(map.get("F_SYZZID"));
        this.syzzqs = CommonTools.Obj2String(map.get("F_SYZZQS"));
        this.sydwyj = CommonTools.Obj2String(map.get("F_SYDWYJ"));
        this.syjd = CommonTools.Obj2String(map.get("F_SYJD"));
        this.wqxtbh = CommonTools.Obj2String(map.get("F_WQXTBH"));
        this.ddbh = CommonTools.Obj2String(map.get("F_DDBH"));
        this.jssyrq = CommonTools.Obj2String(map.get("F_JSSYRQ"));
        this.syyjwj = CommonTools.Obj2String(map.get("F_SYYJWJ"));
        this.syfqs = CommonTools.Obj2String(map.get("F_SYFQS"));
        this.zyqr = CommonTools.Obj2String(map.get("F_ZYQR"));
        this.zyqrId = CommonTools.Obj2String(map.get("F_ZYQRID"));
        this.fqr = CommonTools.Obj2String(map.get("F_FQR"));
        this.fqrId = CommonTools.Obj2String(map.get("F_FQRID"));
        this.sfydb = CommonTools.Obj2String(map.get("F_SFYDB"));
        this.bcbh = CommonTools.Obj2String(map.get("F_BCBH"));
        this.bcsybh = CommonTools.Obj2String(map.get("F_BCSYBH"));
        this.zjtgsj = CommonTools.Obj2String(map.get("F_ZJTGSJ"));

    }
    @XmlElement(name = "id", required = false)
    private String id;
    @XmlElement(name = "bgbbh", required = false)
    private String bgbbh;
    @XmlElement(name = "ssxh", required = false)
    private String ssxh;
    @XmlElement(name = "ssxhId", required = false)
    private String ssxhId;
    @XmlElement(name = "csdw", required = false)
    private String csdw;
    @XmlElement(name = "csdwId", required = false)
    private String csdwId;
    @XmlElement(name = "csqr", required = false)
    private String csqr;
    @XmlElement(name = "syf", required = false)
    private String syf;
    @XmlElement(name = "sydd", required = false)
    private String sydd;
    @XmlElement(name = "syrwbh", required = false)
    private String syrwbh;
    @XmlElement(name = "syqkcgjc", required = false)
    private String syqkcgjc;
    @XmlElement(name = "syqkgnxnjc", required = false)
    private String syqkgnxnjc;
    @XmlElement(name = "syfqsrq", required = false)
    private String syfqsrq;
    @XmlElement(name = "syzzqsrq", required = false)
    private String syzzqsrq;
    @XmlElement(name = "syfId", required = false)
    private String syfId;
    @XmlElement(name = "qtsm", required = false)
    private String qtsm;
    @XmlElement(name = "ylwtbhqk", required = false)
    private String ylwtbhqk;
    @XmlElement(name = "bmfzrId", required = false)
    private String bmfzrId;
    @XmlElement(name = "bmfzr", required = false)
    private String bmfzr;
    @XmlElement(name = "bmfzrqsrq", required = false)
    private String bmfzrqsrq;
    @XmlElement(name = "bhqkqr", required = false)
    private String bhqkqr;
    @XmlElement(name = "xhzls", required = false)
    private String xhzls;
    @XmlElement(name = "xhzlsId", required = false)
    private String xhzlsId;
    @XmlElement(name = "xhzlsqsrq", required = false)
    private String xhzlsqsrq;
    @XmlElement(name = "ssch", required = false)
    private String ssch;
    @XmlElement(name = "spzt", required = false)
    private String spzt;
    @XmlElement(name = "fj", required = false)
    private String fj;
    @XmlElement(name = "lx", required = false)
    private String lx;
    @XmlElement(name = "syzfxdzywt", required = false)
    private String syzfxdzywt;
    @XmlElement(name = "syzyj", required = false)
    private String syzyj;
    @XmlElement(name = "syzz", required = false)
    private String syzz;
    @XmlElement(name = "syzzId", required = false)
    private String syzzId;
    @XmlElement(name = "syzzqs", required = false)
    private String syzzqs;
    @XmlElement(name = "sydwyj", required = false)
    private String sydwyj;
    @XmlElement(name = "syjd", required = false)
    private String syjd;
    @XmlElement(name = "wqxtbh", required = false)
    private String wqxtbh;
    @XmlElement(name = "ddbh", required = false)
    private String ddbh;
    @XmlElement(name = "jssyrq", required = false)
    private String jssyrq;
    @XmlElement(name = "syyjwj", required = false)
    private String syyjwj;
    @XmlElement(name = "syfqs", required = false)
    private String syfqs;
    @XmlElement(name = "zyqr", required = false)
    private String zyqr;
    @XmlElement(name = "zyqrId", required = false)
    private String zyqrId;
    @XmlElement(name = "fqr", required = false)
    private String fqr;
    @XmlElement(name = "fqrId", required = false)
    private String fqrId;
    @XmlElement(name = "sfydb", required = false)
    private String sfydb;
    @XmlElement(name = "bcbh", required = false)
    private String bcbh;
    @XmlElement(name = "bcsybh", required = false)
    private String bcsybh;
    @XmlElement(name = "zjtgsj", required = false)
    private String zjtgsj;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBgbbh() {
        return bgbbh;
    }

    public void setBgbbh(String bgbbh) {
        this.bgbbh = bgbbh;
    }

    public String getSsxh() {
        return ssxh;
    }

    public void setSsxh(String ssxh) {
        this.ssxh = ssxh;
    }

    public String getSsxhId() {
        return ssxhId;
    }

    public void setSsxhId(String ssxhId) {
        this.ssxhId = ssxhId;
    }

    public String getCsdw() {
        return csdw;
    }

    public void setCsdw(String csdw) {
        this.csdw = csdw;
    }

    public String getCsdwId() {
        return csdwId;
    }

    public void setCsdwId(String csdwId) {
        this.csdwId = csdwId;
    }

    public String getCsqr() {
        return csqr;
    }

    public void setCsqr(String csqr) {
        this.csqr = csqr;
    }

    public String getSyf() {
        return syf;
    }

    public void setSyf(String syf) {
        this.syf = syf;
    }

    public String getSydd() {
        return sydd;
    }

    public void setSydd(String sydd) {
        this.sydd = sydd;
    }

    public String getSyrwbh() {
        return syrwbh;
    }

    public void setSyrwbh(String syrwbh) {
        this.syrwbh = syrwbh;
    }

    public String getSyqkcgjc() {
        return syqkcgjc;
    }

    public void setSyqkcgjc(String syqkcgjc) {
        this.syqkcgjc = syqkcgjc;
    }

    public String getSyqkgnxnjc() {
        return syqkgnxnjc;
    }

    public void setSyqkgnxnjc(String syqkgnxnjc) {
        this.syqkgnxnjc = syqkgnxnjc;
    }

    public String getSyfqsrq() {
        return syfqsrq;
    }

    public void setSyfqsrq(String syfqsrq) {
        this.syfqsrq = syfqsrq;
    }

    public String getSyzzqsrq() {
        return syzzqsrq;
    }

    public void setSyzzqsrq(String syzzqsrq) {
        this.syzzqsrq = syzzqsrq;
    }

    public String getSyfId() {
        return syfId;
    }

    public void setSyfId(String syfId) {
        this.syfId = syfId;
    }

    public String getQtsm() {
        return qtsm;
    }

    public void setQtsm(String qtsm) {
        this.qtsm = qtsm;
    }

    public String getYlwtbhqk() {
        return ylwtbhqk;
    }

    public void setYlwtbhqk(String ylwtbhqk) {
        this.ylwtbhqk = ylwtbhqk;
    }

    public String getBmfzrId() {
        return bmfzrId;
    }

    public void setBmfzrId(String bmfzrId) {
        this.bmfzrId = bmfzrId;
    }

    public String getBmfzr() {
        return bmfzr;
    }

    public void setBmfzr(String bmfzr) {
        this.bmfzr = bmfzr;
    }

    public String getBmfzrqsrq() {
        return bmfzrqsrq;
    }

    public void setBmfzrqsrq(String bmfzrqsrq) {
        this.bmfzrqsrq = bmfzrqsrq;
    }

    public String getBhqkqr() {
        return bhqkqr;
    }

    public void setBhqkqr(String bhqkqr) {
        this.bhqkqr = bhqkqr;
    }

    public String getXhzls() {
        return xhzls;
    }

    public void setXhzls(String xhzls) {
        this.xhzls = xhzls;
    }

    public String getXhzlsId() {
        return xhzlsId;
    }

    public void setXhzlsId(String xhzlsId) {
        this.xhzlsId = xhzlsId;
    }

    public String getXhzlsqsrq() {
        return xhzlsqsrq;
    }

    public void setXhzlsqsrq(String xhzlsqsrq) {
        this.xhzlsqsrq = xhzlsqsrq;
    }

    public String getSsch() {
        return ssch;
    }

    public void setSsch(String ssch) {
        this.ssch = ssch;
    }

    public String getSpzt() {
        return spzt;
    }

    public void setSpzt(String spzt) {
        this.spzt = spzt;
    }

    public String getFj() {
        return fj;
    }

    public void setFj(String fj) {
        this.fj = fj;
    }

    public String getLx() {
        return lx;
    }

    public void setLx(String lx) {
        this.lx = lx;
    }

    public String getSyzfxdzywt() {
        return syzfxdzywt;
    }

    public void setSyzfxdzywt(String syzfxdzywt) {
        this.syzfxdzywt = syzfxdzywt;
    }

    public String getSyzyj() {
        return syzyj;
    }

    public void setSyzyj(String syzyj) {
        this.syzyj = syzyj;
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

    public String getSyzzqs() {
        return syzzqs;
    }

    public void setSyzzqs(String syzzqs) {
        this.syzzqs = syzzqs;
    }

    public String getSydwyj() {
        return sydwyj;
    }

    public void setSydwyj(String sydwyj) {
        this.sydwyj = sydwyj;
    }

    public String getSyjd() {
        return syjd;
    }

    public void setSyjd(String syjd) {
        this.syjd = syjd;
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

    public String getJssyrq() {
        return jssyrq;
    }

    public void setJssyrq(String jssyrq) {
        this.jssyrq = jssyrq;
    }

    public String getSyyjwj() {
        return syyjwj;
    }

    public void setSyyjwj(String syyjwj) {
        this.syyjwj = syyjwj;
    }

    public String getSyfqs() {
        return syfqs;
    }

    public void setSyfqs(String syfqs) {
        this.syfqs = syfqs;
    }

    public String getZyqr() {
        return zyqr;
    }

    public void setZyqr(String zyqr) {
        this.zyqr = zyqr;
    }

    public String getZyqrId() {
        return zyqrId;
    }

    public void setZyqrId(String zyqrId) {
        this.zyqrId = zyqrId;
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

    public String getSfydb() {
        return sfydb;
    }

    public void setSfydb(String sfydb) {
        this.sfydb = sfydb;
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

    public String getZjtgsj() {
        return zjtgsj;
    }

    public void setZjtgsj(String zjtgsj) {
        this.zjtgsj = zjtgsj;
    }
}
