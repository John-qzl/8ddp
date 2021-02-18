package com.cssrc.ibms.dp.signModel.entity;

import java.math.BigDecimal;
import java.util.Date;

public class CwmSysSignModel {
    private BigDecimal id;

    private BigDecimal user_Id;

    private String code;

    private BigDecimal org_Id;

    private String name;

    private Long is_Default;

    private String desc_;

    private BigDecimal type_;

    private Date start_Date;

    private Date end_Date;

    private BigDecimal status;

    private String passw;

    private Short path_Type;

    private String version;

    private String img_Path;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(BigDecimal user_Id) {
        this.user_Id = user_Id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getOrg_Id() {
        return org_Id;
    }

    public void setOrg_Id(BigDecimal org_Id) {
        this.org_Id = org_Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIs_Default() {
        return is_Default;
    }

    public void setIs_Default(Long is_Default) {
        this.is_Default = is_Default;
    }

    public String getDesc_() {
        return desc_;
    }

    public void setDesc_(String desc_) {
        this.desc_ = desc_;
    }

    public BigDecimal getType_() {
        return type_;
    }

    public void setType_(BigDecimal type_) {
        this.type_ = type_;
    }

    public Date getStart_Date() {
        return start_Date;
    }

    public void setStart_Date(Date start_Date) {
        this.start_Date = start_Date;
    }

    public Date getEnd_Date() {
        return end_Date;
    }

    public void setEnd_Date(Date end_Date) {
        this.end_Date = end_Date;
    }

    public BigDecimal getStatus() {
        return status;
    }

    public void setStatus(BigDecimal status) {
        this.status = status;
    }

    public String getPassw() {
        return passw;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }

    public Short getPath_Type() {
        return path_Type;
    }

    public void setPath_Type(Short path_Type) {
        this.path_Type = path_Type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getImg_Path() {
        return img_Path;
    }

    public void setImg_Path(String img_Path) {
        this.img_Path = img_Path;
    }
}