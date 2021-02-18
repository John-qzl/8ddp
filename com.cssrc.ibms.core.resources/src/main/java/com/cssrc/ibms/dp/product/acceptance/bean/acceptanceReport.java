package com.cssrc.ibms.dp.product.acceptance.bean;

import java.util.Map;

import javax.xml.bind.annotation.*;

import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.date.DateUtil;

@XmlRootElement(name = "acceptanceGroup")
@XmlAccessorType(XmlAccessType.FIELD)
public class acceptanceReport {
	
	
	public acceptanceReport() {
		
	}
	
	public acceptanceReport(Map<String,Object> map) {
		this.id=CommonTools.Obj2String(map.get("id"));
		this.fj=CommonTools.Obj2String(map.get("F_FJ"));
		this.bgbbh=CommonTools.Obj2String(map.get("F_bgbbh"));                                   
		this.ssxh=CommonTools.Obj2String(map.get("F_ssxh"));                              
		this.ssxhID=CommonTools.Obj2String(map.get("F_ssxhthis.id"));                               
		this.cpmc=CommonTools.Obj2String(map.get("F_cpmc"));                               
		this.cpdh=CommonTools.Obj2String(map.get("F_cpdh"));                                 
		this.yzdw=CommonTools.Obj2String(map.get("F_yzdw"));                      
		this.yzjd=CommonTools.Obj2String(map.get("F_yzjd"));               
		this.ysrq=CommonTools.Obj2String(map.get("F_ysrq"));    
		this.ysrq = DateUtil.getDateStr(ysrq, "yyyy-MM-dd");
		this.ysdd=CommonTools.Obj2String(map.get("F_ysdd"));                             
		this.yscpsl=CommonTools.Obj2String(map.get("F_yscpsl"));                        
		this.lsjbh=CommonTools.Obj2String(map.get("F_lsjbh"));                       
		this.yscpbh=CommonTools.Obj2String(map.get("F_yscpbh"));                         
		this.ysyj=CommonTools.Obj2String(map.get("F_ysyj"));                           
		this.ysqkcgjc=CommonTools.Obj2String(map.get("F_ysqkcgjc"));                        
		this.ysqkgnxnjc=CommonTools.Obj2String(map.get("F_ysqkgnxnjc"));                         
		this.yszfxdzywt=CommonTools.Obj2String(map.get("F_yszfxdzywt"));                         
		this.yszyj=CommonTools.Obj2String(map.get("F_yszyj"));                        
		this.czdwyj=CommonTools.Obj2String(map.get("F_czdwyj"));                            
		this.czf=CommonTools.Obj2String(map.get("F_czf"));                           
		this.czfqsrq=CommonTools.Obj2String(map.get("F_czfqsrq"));       
		this.czfqsrq = DateUtil.getDateStr(czfqsrq, "yyyy-MM-dd");
		this.qtsm=CommonTools.Obj2String(map.get("F_qtsm"));                        
		this.ylwtbhqk=CommonTools.Obj2String(map.get("F_ylwtbhqk"));                           
		this.yszzqsrq=CommonTools.Obj2String(map.get("F_yszzqsrq"));                                
		this.bmfzr=CommonTools.Obj2String(map.get("F_bmfzr"));                       
		this.bmfzrqsrq=CommonTools.Obj2String(map.get("F_bmfzrqsrq"));
		this.bhqkqr=CommonTools.Obj2String(map.get("F_bhqkqr"));
		this.xhzls=CommonTools.Obj2String(map.get("F_xhzls"));                           
		this.xhzlsqsrq=CommonTools.Obj2String(map.get("F_xhzlsqsrq"));
		this.xhzlsqsrq = DateUtil.getDateStr(xhzlsqsrq, "yyyy-MM-dd");
		this.planId=CommonTools.Obj2String(map.get("F_planthis.id"));                            
		this.spzt=CommonTools.Obj2String(map.get("F_spzt"));                            
		this.yszz=CommonTools.Obj2String(map.get("F_yszz"));                            
		this.yzdwId=CommonTools.Obj2String(map.get("F_yzdwthis.id"));                                   
		this.czfId=CommonTools.Obj2String(map.get("F_czfthis.id"));                               
		this.bmfzrId=CommonTools.Obj2String(map.get("F_bmfzrthis.id"));                               
		this.xhzlsId=CommonTools.Obj2String(map.get("F_xhzlsthis.id"));
		this.zjtgsj=CommonTools.Obj2String(map.get("F_zjtgsj"));
		if(map.get("F_lx")!=null) {
			this.lx=CommonTools.Obj2String(map.get("F_lx"));
		}
		else {
			this.lx="0";
		}
	}
	
	
	@XmlAttribute
	private String id; 
	@XmlAttribute
	private String lx; 
	@XmlAttribute
	private String bgbbh; 
		
	@XmlAttribute
	private String ssxh; 
		
	@XmlAttribute
	private String ssxhID; 
		
	@XmlAttribute
	private String cpmc; 
		
	@XmlAttribute
	private String cpdh; 
		
	@XmlAttribute
	private String yzdw; 
		
	@XmlAttribute
	private String yzjd; 
		
	@XmlAttribute
	private String ysrq; 
		
	@XmlAttribute
	private String ysdd; 
		
	@XmlAttribute
	private String yscpsl; 
		
	@XmlAttribute
	private String lsjbh; 
		
	@XmlAttribute
	private String yscpbh; 
		
	@XmlAttribute
	private String ysyj; 
		
	@XmlAttribute
	private String ysqkcgjc; 
		
	@XmlAttribute
	private String ysqkgnxnjc; 
		
	@XmlAttribute
	private String yszfxdzywt; 
		
	@XmlAttribute
	private String yszyj; 
		
	@XmlAttribute
	private String czdwyj; 
		
	@XmlAttribute
	private String czf; 
		
	@XmlAttribute
	private String czfqsrq; 
		
	@XmlAttribute
	private String qtsm; 
		
	@XmlAttribute
	private String ylwtbhqk; 
		
	@XmlAttribute
	private String yszzqsrq; 
	
	@XmlAttribute
	private String bmfzr; 
	
	@XmlAttribute
	private String bmfzrqsrq; 
	
	@XmlAttribute
	private String bhqkqr; 
	
	@XmlAttribute
	private String xhzls; 
	
	@XmlAttribute
	private String xhzlsqsrq; 
	
	@XmlAttribute
	private String planId; 
	
	@XmlAttribute
	private String spzt; 
	
	@XmlAttribute
	private String yszz; 
	
	@XmlAttribute
	private String yzdwId; 
	
	@XmlAttribute
	private String czfId; 
	
	@XmlAttribute
	private String bmfzrId; 
	
	@XmlAttribute
	private String xhzlsId;

	@XmlElement(required = false)
	private String fj;

	@XmlElement(required = false)
	private String zjtgsj;

	public String getZjtgsj() {
		return zjtgsj;
	}

	public void setZjtgsj(String zjtgsj) {
		this.zjtgsj = zjtgsj;
	}

	public String getFj() {
		return fj;
	}

	public void setFj(String fj) {
		this.fj = fj;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
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

	public String getSsxhID() {
		return ssxhID;
	}

	public void setSsxhID(String ssxhID) {
		this.ssxhID = ssxhID;
	}

	public String getCpmc() {
		return cpmc;
	}

	public void setCpmc(String cpmc) {
		this.cpmc = cpmc;
	}

	public String getCpdh() {
		return cpdh;
	}

	public void setCpdh(String cpdh) {
		this.cpdh = cpdh;
	}

	public String getYzdw() {
		return yzdw;
	}

	public void setYzdw(String yzdw) {
		this.yzdw = yzdw;
	}

	public String getYzjd() {
		return yzjd;
	}

	public void setYzjd(String yzjd) {
		this.yzjd = yzjd;
	}

	public String getYsrq() {
		return ysrq;
	}

	public void setYsrq(String ysrq) {
		this.ysrq = ysrq;
	}

	public String getYsdd() {
		return ysdd;
	}

	public void setYsdd(String ysdd) {
		this.ysdd = ysdd;
	}

	public String getYscpsl() {
		return yscpsl;
	}

	public void setYscpsl(String yscpsl) {
		this.yscpsl = yscpsl;
	}

	public String getLsjbh() {
		return lsjbh;
	}

	public void setLsjbh(String lsjbh) {
		this.lsjbh = lsjbh;
	}

	public String getYscpbh() {
		return yscpbh;
	}

	public void setYscpbh(String yscpbh) {
		this.yscpbh = yscpbh;
	}

	public String getYsyj() {
		return ysyj;
	}

	public void setYsyj(String ysyj) {
		this.ysyj = ysyj;
	}

	public String getYsqkcgjc() {
		return ysqkcgjc;
	}

	public void setYsqkcgjc(String ysqkcgjc) {
		this.ysqkcgjc = ysqkcgjc;
	}

	public String getYsqkgnxnjc() {
		return ysqkgnxnjc;
	}

	public void setYsqkgnxnjc(String ysqkgnxnjc) {
		this.ysqkgnxnjc = ysqkgnxnjc;
	}

	public String getYszfxdzywt() {
		return yszfxdzywt;
	}

	public void setYszfxdzywt(String yszfxdzywt) {
		this.yszfxdzywt = yszfxdzywt;
	}

	public String getYszyj() {
		return yszyj;
	}

	public void setYszyj(String yszyj) {
		this.yszyj = yszyj;
	}

	public String getCzdwyj() {
		return czdwyj;
	}

	public void setCzdwyj(String czdwyj) {
		this.czdwyj = czdwyj;
	}

	public String getCzf() {
		return czf;
	}

	public void setCzf(String czf) {
		this.czf = czf;
	}

	public String getCzfqsrq() {
		return czfqsrq;
	}

	public void setCzfqsrq(String czfqsrq) {
		this.czfqsrq = czfqsrq;
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

	public String getYszzqsrq() {
		return yszzqsrq;
	}

	public void setYszzqsrq(String yszzqsrq) {
		this.yszzqsrq = yszzqsrq;
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

	public String getXhzlsqsrq() {
		return xhzlsqsrq;
	}

	public void setXhzlsqsrq(String xhzlsqsrq) {
		this.xhzlsqsrq = xhzlsqsrq;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getSpzt() {
		return spzt;
	}

	public void setSpzt(String spzt) {
		this.spzt = spzt;
	}

	public String getYszz() {
		return yszz;
	}

	public void setYszz(String yszz) {
		this.yszz = yszz;
	}

	public String getYzdwId() {
		return yzdwId;
	}

	public void setYzdwId(String yzdwId) {
		this.yzdwId = yzdwId;
	}

	public String getCzfId() {
		return czfId;
	}

	public void setCzfId(String czfId) {
		this.czfId = czfId;
	}

	public String getBmfzrId() {
		return bmfzrId;
	}

	public void setBmfzrId(String bmfzrId) {
		this.bmfzrId = bmfzrId;
	}

	public String getXhzlsId() {
		return xhzlsId;
	}

	public void setXhzlsId(String xhzlsId) {
		this.xhzlsId = xhzlsId;
	} 

	
}
