package com.cssrc.ibms.core.form.service;


import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.form.intf.IFiledEncryptService;
import com.cssrc.ibms.core.encrypt.IFiledEncrypt;

/** 
* @ClassName: FiledEncryptService 
* @Description: TODO(字段加解密数据 service实现类) 
* @author zxg 
* @date 2017年5月27日 下午4:34:46 
*  
*/
@Service("filedEncryptService")
public class FiledEncryptService implements IFiledEncryptService
{
    @Override
    public Object encrypt(Class<?extends IFiledEncrypt> _class,Object data)
    {
        IFiledEncrypt filedEncrypt=this.getEncrypt(_class);
        if(filedEncrypt!=null){
            return filedEncrypt.encrypt(data);
        }else{
            return data;
        }
    }

    @Override
    public Object decrypt(Class<?extends IFiledEncrypt> _class,Object data)
    {
        IFiledEncrypt filedEncrypt=this.getEncrypt(_class);
        if(filedEncrypt!=null){
            return filedEncrypt.decrypt(data);
        }else{
            return data;
        }
    }
    
    
    private IFiledEncrypt getEncrypt(Class<?extends IFiledEncrypt> _class){
        try
        {
            return _class.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        
    }
}
