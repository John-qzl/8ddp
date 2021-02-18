package com.cssrc.ibms.report.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.cssrc.ibms.api.form.model.IFormDef;
import com.cssrc.ibms.api.report.model.IReportTemplate;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.appconf.AppConfigUtil;
/**
 * <per>
 * 对象功能:业务数据模板 Model 
 * 开发人员:zhulongchao 
 * </per>
 */
public class ReportTemplate extends BaseModel implements IReportTemplate{
	private static final long serialVersionUID = 8857761044238145587L;

	// 主键
    private Long reportid;

    //报表标题
    private String title;

    //报表描述
    private String descp;

    //报表位置
    private String reportlocation;

    //报表创建时间
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private Date createtime;

    //报表更新时间
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private Date updatetime;

    //报表类型:pageoffice,finerreport
    private String reporttype;

    //报表分类ID
    private Long typeid;
    //report full path
    protected String fileName;
    
    /**
     * 构造一个报表
     * @param bpmFormDef
     */
    public ReportTemplate(IFormDef bpmFormDef)
    {
        
        this.title=bpmFormDef.getSubject();
        this.fileName=bpmFormDef.getSubject()+"_REC.cpt";
        this.reportlocation=bpmFormDef.getSubject()+"_REC.cpt";
    }

    public ReportTemplate()
    {
    }

    public Long getReportid() {
        return reportid;
    }

    public void setReportid(Long reportid) {
        this.reportid = reportid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp == null ? null : descp.trim();
    }

    public String getReportlocation() {
        return reportlocation;
    }

    public void setReportlocation(String reportlocation) {
        this.reportlocation = reportlocation == null ? null : reportlocation.trim();
    }
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    public Date getCreatetime() {
        return createtime;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    public Date getUpdatetime() {
        return updatetime;
    }
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getReporttype() {
        return reporttype;
    }

    public void setReporttype(String reporttype) {
        this.reporttype = reporttype == null ? null : reporttype.trim();
    }

    public Long getTypeid() {
        return typeid;
    }

    public void setTypeid(Long typeid) {
        this.typeid = typeid;
    }

	public String getReportSeverlet() {
		String url=AppConfigUtil.get("pluginproperties","fr.server.url");
		String mapping=AppConfigUtil.get("pluginproperties","fr.servlet.mapping");
		return url+mapping+"?reportlet=";
	}

	public String getFileName() {
		return this.reportlocation.replace("\\", "/");
	}
}