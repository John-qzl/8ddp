package com.cssrc.ibms.report.model;

import java.io.IOException;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.cssrc.ibms.api.report.model.ISignModel;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.file.FastDFSFileOperator;
import com.cssrc.ibms.core.web.servlet.FileController;

public class SignModel implements ISignModel{
	private Long id;

	private String code;

	private String name;

	private String desc;

	private String imgPath;
	
    private Integer isDefault;

	private Long userId;

	private Long orgId;

	private Long type;
	
	private String passw;
	
	private Integer pathType;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date startDate;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date endDate;

	private Long status;

	private String version;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath == null ? null : imgPath.trim();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version == null ? null : version.trim();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

    public String getPassw()
    {
        return passw;
    }

    public void setPassw(String passw)
    {
        this.passw = passw;
    }

    public Integer getPathType()
    {
        return pathType;
    }

    public void setPathType(Integer pathType)
    {
        this.pathType = pathType;
    }
    
    public String getImageSrc()
    {
        try
        {
            if(this.pathType==1){
                return FastDFSFileOperator.getInterviewServer()+this.getImgPath();
            }else if(this.pathType==0){
                return AppUtil.getContextPath()+FileController.urlPatterns+"?opath=";
            }else{
                return "data:image/gif;base64,";
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    
    }
    

    @Override
    public String getSignImage()
    {

        try
        {
            if(this.pathType==1){
                return FastDFSFileOperator.getInterviewServer()+this.getImgPath();
            }else if(this.pathType==0){
                return this.imgPath;
            }else{
                return "data:image/gif;base64,"+this.imgPath;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    
    
    }

}