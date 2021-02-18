package com.cssrc.ibms.api.report.inf;

public interface ISignItemService
{

    /**
     * 获取印章image 字节流，印章表存储的是base64编码的字节流
     * @param signId 印章id
     * @return
     */
    String getImgeData(String signId);
    
}
