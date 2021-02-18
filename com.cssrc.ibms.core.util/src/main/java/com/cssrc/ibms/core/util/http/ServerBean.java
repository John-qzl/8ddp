/*
 * 文 件 名:  ServiceBean.java
 * 版    权:  xiaoyuer Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  zhuguanglong
 * 修改时间:  2014-10-23
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.cssrc.ibms.core.util.http;

/**
 * 接口信息
 * 
 * @author  zxg
 * @version  [版本号, 2016-11-30]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ServerBean
{
    /**
     * 接口类型
     */
    String serviceType;
    
    /**
     * 发送内容
     */
    String content;
    
    /**
     * 发送地址
     */
    String url;
    
    /**
     * 默认编码格式
     */
    String charsetName = "gbk";
    
    /**
     * 默认post请求
     */
    String requestMethod = "POST";
    
    public String getRequestMethod()
    {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod)
    {
        this.requestMethod = requestMethod;
    }

    /**
     * @return 返回 serviceType
     */
    public String getServiceType()
    {
        return serviceType;
    }
    
    /**
     * @param 对serviceType进行赋值
     */
    public void setServiceType(String serviceType)
    {
        this.serviceType = serviceType;
    }
    
    /**
     * @return 返回 content
     */
    public String getContent()
    {
        return content;
    }
    
    /**
     * @param 对content进行赋值
     */
    public void setContent(String content)
    {
        this.content = content;
    }
    
    /**
     * @return 返回 url
     */
    public String getUrl()
    {
        return url;
    }
    
    /**
     * @param 对url进行赋值
     */
    public void setUrl(String url)
    {
        this.url = url;
    }

    /**
     * @return 返回 charsetName
     */
    public String getCharsetName()
    {
        return charsetName;
    }

    /**
     * @param 对charsetName进行赋值
     */
    public void setCharsetName(String charsetName)
    {
        this.charsetName = charsetName;
    }
    
}
