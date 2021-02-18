package com.cssrc.ibms.dp.sync.bean;

import com.cssrc.ibms.core.util.common.CommonTools;

import java.util.Map;

public class PadPhotoInfo {
    public PadPhotoInfo(Map<String,Object> map){
        this.id= CommonTools.Obj2String(map.get("ID"));
        this.instanceId=CommonTools.Obj2String(map.get("F_INSTANCEID"));
        this.wjm=CommonTools.Obj2String(map.get("F_WJM"));
        this.xddz=CommonTools.Obj2String(map.get("F_XDDZ"));
        this.fileId=CommonTools.Obj2String(map.get("F_FILEID"));
    }
    public PadPhotoInfo(){
    }
    String id;
    String instanceId;  //实例id
    String wjm;     //文件真实名字及后缀
    String xddz;  //文件相对地址,需要加上默认上传地址
    String fileId;  //照片在文件表里的id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getWjm() {
        return wjm;
    }

    public void setWjm(String wjm) {
        this.wjm = wjm;
    }

    public String getXddz() {
        return xddz;
    }

    public void setXddz(String xddz) {
        this.xddz = xddz;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
