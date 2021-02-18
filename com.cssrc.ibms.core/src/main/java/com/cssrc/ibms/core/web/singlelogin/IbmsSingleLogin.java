package com.cssrc.ibms.core.web.singlelogin;

import java.util.List;

import javax.servlet.ServletRequest;


/** 
* @ClassName: IbmsSingleLogin 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author zxg 
* @date 2017年9月15日 上午8:42:49 
*  
*/
public interface IbmsSingleLogin
{
    public static String token="token";
    /** 
    * @Title: getExclusion 
    * @Description: TODO(不需要数字签名的 参数名) 
    * @param @return     
    * @return List<String>    返回类型 
    * @throws 
    */
    public List<String> getExclusion();
    
    
    /** 
    * @Title: getExclusion 
    * @Description: TODO(是否对参数进行加密) 
    * @param @return     
    * @return List<String>    返回类型 
    * @throws 
    */
    public boolean isEncrypt();
    
    /** 
    * @Title: getSignUrl 
    * @Description: TODO(获取 签名后的 url) 
    * @param @param url
    * @param @return     
    * @return String    返回类型 
    * @throws 
    */
    public String getSignParam(String url);
    
    
    
    /** 
    * @Title: validUrl 
    * @Description: TODO(校验url 有效性) 
    * @param @param url
    * @param @return     
    * @return boolean    返回类型 
    * @throws 
    */
    public boolean validUrl(String url);


    /** 
    * @Title: decrypt 
    * @Description: TODO(对参数进行解密) 
    * @param @param key
    * @param @param value
    * @param @return     
    * @return String    返回类型 
    * @throws 
    */
    public String decrypt(String key, String value);


}
