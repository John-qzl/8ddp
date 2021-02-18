package com.cssrc.ibms.dp.signModel.entity;

import java.math.BigDecimal;
import java.util.Date;

public class CwmSysFile {
    private Long fileid;

    private String filename;

    private String filepath;

    private Date createtime;

    private String ext;

    private String filetype;

    private String note;

    private Long creatorid;

    private String creator;

    private Long totalbytes;

    private BigDecimal delflag;

    private Long protypeid;

    private String tableid;

    private String dataid;

    private Short shared;

    private Long folderid;

    private Short fileatt;

    private String folderpath;

    private String security;

    private String describe;


    private BigDecimal filing;

    private BigDecimal parentid;

    private BigDecimal isnew;

    private String version;

    private BigDecimal storeway;

    private String dimension;

    private BigDecimal isencrypt;

    private byte[] fileblob;

    public Long getFileid() {
        return fileid;
    }

    public void setFileid(Long fileid) {
        this.fileid = fileid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename == null ? null : filename.trim();
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath == null ? null : filepath.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext == null ? null : ext.trim();
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype == null ? null : filetype.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    public Long getCreatorid() {
        return creatorid;
    }

    public void setCreatorid(Long creatorid) {
        this.creatorid = creatorid;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Long getTotalbytes() {
        return totalbytes;
    }

    public void setTotalbytes(Long totalbytes) {
        this.totalbytes = totalbytes;
    }

    public BigDecimal getDelflag() {
        return delflag;
    }

    public void setDelflag(BigDecimal delflag) {
        this.delflag = delflag;
    }

    public Long getProtypeid() {
        return protypeid;
    }

    public void setProtypeid(Long protypeid) {
        this.protypeid = protypeid;
    }

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid == null ? null : tableid.trim();
    }

    public String getDataid() {
        return dataid;
    }

    public void setDataid(String dataid) {
        this.dataid = dataid == null ? null : dataid.trim();
    }

    public Short getShared() {
        return shared;
    }

    public void setShared(Short shared) {
        this.shared = shared;
    }

    public Long getFolderid() {
        return folderid;
    }

    public void setFolderid(Long folderid) {
        this.folderid = folderid;
    }

    public Short getFileatt() {
        return fileatt;
    }

    public void setFileatt(Short fileatt) {
        this.fileatt = fileatt;
    }

    public String getFolderpath() {
        return folderpath;
    }

    public void setFolderpath(String folderpath) {
        this.folderpath = folderpath == null ? null : folderpath.trim();
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security == null ? null : security.trim();
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe == null ? null : describe.trim();
    }

    public BigDecimal getFiling() {
        return filing;
    }

    public void setFiling(BigDecimal filing) {
        this.filing = filing;
    }

    public BigDecimal getParentid() {
        return parentid;
    }

    public void setParentid(BigDecimal parentid) {
        this.parentid = parentid;
    }

    public BigDecimal getIsnew() {
        return isnew;
    }

    public void setIsnew(BigDecimal isnew) {
        this.isnew = isnew;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version == null ? null : version.trim();
    }

    public BigDecimal getStoreway() {
        return storeway;
    }

    public void setStoreway(BigDecimal storeway) {
        this.storeway = storeway;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension == null ? null : dimension.trim();
    }

    public BigDecimal getIsencrypt() {
        return isencrypt;
    }

    public void setIsencrypt(BigDecimal isencrypt) {
        this.isencrypt = isencrypt;
    }

    public byte[] getFileblob() {
        return fileblob;
    }

    public void setFileblob(byte[] fileblob) {
        this.fileblob = fileblob;
    }
}