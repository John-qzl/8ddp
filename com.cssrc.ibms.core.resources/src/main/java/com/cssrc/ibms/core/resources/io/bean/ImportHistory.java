package com.cssrc.ibms.core.resources.io.bean;

import com.cssrc.ibms.core.util.common.CommonTools;

import java.util.Map;

public class ImportHistory {
    public ImportHistory(Map<String,Object> map){
        this.id=CommonTools.Obj2String(map.get("ID"));
        this.chbh= CommonTools.Obj2String(map.get("F_CHBH"));
        this.rwmc= CommonTools.Obj2String(map.get("F_RWMC"));
        this.drsj= CommonTools.Obj2String(map.get("F_DRSJ"));
        this.zz= CommonTools.Obj2String(map.get("F_ZZ"));
        this.zzId= CommonTools.Obj2String(map.get("F_ZZID"));
        this.ssxhhpc= CommonTools.Obj2String(map.get("F_SSXHHPC"));
    }
    public ImportHistory(){

    }
    String id;
    String chbh;
    String rwmc;
    String drsj;
    String zz;
    String zzId;
    String ssxhhpc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChbh() {
        return chbh;
    }

    public void setChbh(String chbh) {
        this.chbh = chbh;
    }

    public String getRwmc() {
        return rwmc;
    }

    public void setRwmc(String rwmc) {
        this.rwmc = rwmc;
    }

    public String getDrsj() {
        return drsj;
    }

    public void setDrsj(String drsj) {
        this.drsj = drsj;
    }

    public String getZz() {
        return zz;
    }

    public void setZz(String zz) {
        this.zz = zz;
    }

    public String getZzId() {
        return zzId;
    }

    public void setZzId(String zzId) {
        this.zzId = zzId;
    }

    public String getSsxhhpc() {
        return ssxhhpc;
    }

    public void setSsxhhpc(String ssxhhpc) {
        this.ssxhhpc = ssxhhpc;
    }
}
