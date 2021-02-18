package com.cssrc.ibms.report.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.cssrc.ibms.api.report.model.IOfficeTemplate;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

public class OfficeTemplate extends BaseModel implements IOfficeTemplate{
	protected static final long serialVersionUID = -751909945832973795L;
	
	protected String officeid;
	
	//数据源表ID --为保证所有的地方使用的都是 tableName
    //protected String tableId;
	
	protected String tableName;
	
    //数据源视图ID --为保证所有的地方使用的都是 viewsName
    //protected String viewsId;
	protected String viewsName;
    //模板路径
    protected String filepath;

    //模板global分类
    protected Long typeId;
    
    //模板类型
    protected String officeType;

    //模板名
    protected String title;
    
    //数据主入口
    protected String dataEntry;
    
    //模板所有标签
    protected String content;
    
    protected String createUser;
    
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    protected Date createTime;

    //模板状态:未发布、已发布、已删除
    private String status;
    
    //模板发布时间
    private String publishedTime;
    
    //模板存储目录类型
    private Integer pathType;
    
	protected Long template_updateId;// 更改人ID
	
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
	protected Date template_updateTime;// 更改时间

	public Long getTemplate_updateId() {
		return template_updateId;
	}

	public void setTemplate_updateId(Long template_updateId) {
		this.template_updateId = template_updateId;
	}

	public Date getTemplate_updateTime() {
		return template_updateTime;
	}

	public void setTemplate_updateTime(Date template_updateTime) {
		this.template_updateTime = template_updateTime;
	}

	public String getOfficeid() {
        return officeid;
    }

    public void setOfficeid(String officeid) {
        this.officeid = officeid == null ? null : officeid.trim();
    }

    public String getViewsName()
    {
        return viewsName;
    }

    public void setViewsName(String viewsName)
    {
        this.viewsName = viewsName;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath == null ? null : filepath.trim();
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getOfficeType() {
        return officeType;
    }

    public void setOfficeType(String officeType) {
        this.officeType = officeType == null ? null : officeType.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getDataEntry() {
        return dataEntry;
    }

    public void setDataEntry(String dataEntry) {
        this.dataEntry = dataEntry == null ? null : dataEntry.trim();
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }
    
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }
    
    @DateTimeFormat(pattern="yyyy-MM-dd hh:mm:ss")
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPublishedTime() {
		return publishedTime;
	}

	public void setPublishedTime(String publishedTime) {
		this.publishedTime = publishedTime;
	}

    public Integer getPathType()
    {
        return pathType;
    }

    public void setPathType(Integer pathType)
    {
        this.pathType = pathType;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }
    
	
}