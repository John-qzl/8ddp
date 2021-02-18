package com.cssrc.ibms.core.encrypt;

/** 
* @ClassName: IFiledEncrypt 
* @Description: TODO(字段加密顶层加解密接口) 
* @author zxg 
* @date 2017年5月27日 下午3:53:39 
*  
*/
public interface IFiledEncrypt
{
    /** 
    * @Title: encrypt 
    * @Description: TODO(加密算法) 
    * @param @param data
    * @param @return    加密后的数据
    * @return Object    返回类型 
    * @throws 
    */
    public Object encrypt(Object data);
    
    /** 
    * @Title: decrypt 
    * @Description: TODO(解密算法) 
    * @param @param data
    * @param @return    解密后的数据 
    * @return Object    返回类型 
    * @throws 
    */
    public Object decrypt(Object data);

}
