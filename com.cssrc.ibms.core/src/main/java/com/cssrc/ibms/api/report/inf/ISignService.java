package com.cssrc.ibms.api.report.inf;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cssrc.ibms.api.report.model.IbmsSign;

public interface ISignService
{
    
    /**
     * 签名
     * 
     * @param ibmsSign
     * @return
     * @throws Exception
     */
    public byte[] sign(IbmsSign ibmsSign)
        throws Exception;
    
    /**
     * 前端js签名后下载
     * 
     * @param title
     * @param refUserSign
     * @return
     * @throws Exception
     */
    public byte[] signDownload(HttpServletRequest request, String title, String refUserSign)
        throws Exception;

    /**
     * 签名 批量报表
     * @param ibmsSigns
     * @return
     * @throws Exception
     */
    byte[] sign(List<IbmsSign> ibmsSigns)
        throws Exception;
    
}
