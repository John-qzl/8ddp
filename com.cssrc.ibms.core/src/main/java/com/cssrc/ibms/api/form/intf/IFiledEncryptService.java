package com.cssrc.ibms.api.form.intf;

import com.cssrc.ibms.core.encrypt.IFiledEncrypt;

/** 
* @ClassName: IFiledEncryptService 
* @Description: TODO(字段加密顶层加解密接口) 
* @author zxg 
* @date 2017年5月27日 下午4:02:50 
*  
*/
public interface IFiledEncryptService
{

    /** 
    * @Title: encrypt 
    * @Description: TODO(加密算法) 
    * @param @param _class 加解密算法类
    * @param @param data
    * @param @return    加密后的数据
    * @return Object    返回类型 
    * @throws 
    */
    public Object encrypt(Class<?extends IFiledEncrypt> _class,Object data);
    

    /** 
    * @Title: decrypt 
    * @Description: TODO(解密算法) 
    * @param @param _class 加解密算法类
    * @param @param data
    * @param @return    解密后的数据
    * @return Object    返回类型 
    * @throws 
    */
    public Object decrypt(Class<?extends IFiledEncrypt> _class,Object data);
    
   
}
