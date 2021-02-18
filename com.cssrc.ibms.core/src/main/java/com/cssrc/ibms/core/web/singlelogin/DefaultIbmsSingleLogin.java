package com.cssrc.ibms.core.web.singlelogin;

import java.util.Arrays;
import java.util.List;
import com.cssrc.ibms.core.encrypt.Coder;
import com.cssrc.ibms.core.encrypt.IFiledEncrypt;
import com.cssrc.ibms.core.util.string.StringUtil;

public class DefaultIbmsSingleLogin implements IbmsSingleLogin
{
    
    /** 
    * @Fields exclusionSign : TODO(过滤的签名) 
    */
    private String exclusionSign;
    
    /** 
    * @Fields isEncrypt : TODO(是否对参数进行加密) 
    */
    private boolean encrypt;
    
    /** 
    * @Fields enCryptBean : TODO(参数加解密算法bean) 
    */
    private IFiledEncrypt enCryptBean;
    
    /** 
    * @Fields enCryptKey : TODO(md5加密算法key) 
    */
    private String signKey;
    
    @Override
    public List<String> getExclusion()
    {
        if (StringUtil.isNotEmpty(exclusionSign))
        {
            return Arrays.asList(exclusionSign.split(","));
        }
        else
        {
            return null;
        }
    }
    
    @Override
    public String getSignParam(String params)
    {
        List<String> exclusions = this.getExclusion();
        
        try
        {
            String param = "";
            if (encrypt)
            {
                String[] querys = params.split("&");
                for (String p : querys)
                {
                    String[] pString = p.split("=");
                    if (exclusions == null || !exclusions.contains(pString[0]))
                    {
                        p = pString[0] + "=" + enCryptBean.encrypt(pString[1]);
                        p = p.trim();
                    }
                    if ("".equals(param))
                    {
                        param += p;
                    }
                    else
                    {
                        param += "&" + p;
                    }
                    
                }
            }
            param = param.trim();
            String encryptQuery = Coder.byteArrayToHexString(Coder.encryptMD5(param.getBytes(), signKey));
            param += "&" + token + "=" + encryptQuery;
            return param;
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return params;
    }
    
    @Override
    public boolean validUrl(String query)
    {
        try
        {
            String[] querys = query.split("&");
            String param = "";
            String tokenVal = "";
            for (String p : querys)
            {
                String[] pString = p.split("=");
                if (pString[0].equals(token))
                {
                    tokenVal = pString[1];
                }
                else
                {
                    if ("".equals(param))
                    {
                        param += p;
                    }
                    else
                    {
                        param += "&" + p;
                    }
                }
            }
            String encryptToken = Coder.byteArrayToHexString(Coder.encryptMD5(param.getBytes()));
            return tokenVal.equals(encryptToken);
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public boolean isEncrypt()
    {
        return encrypt;
    }
    
    public String getExclusionSign()
    {
        return exclusionSign;
    }
    
    public void setExclusionSign(String exclusionSign)
    {
        this.exclusionSign = exclusionSign;
    }
    
    public void setEncrypt(boolean encrypt)
    {
        this.encrypt = encrypt;
    }
    
    public IFiledEncrypt getEnCryptBean()
    {
        return enCryptBean;
    }
    
    public void setEnCryptBean(IFiledEncrypt enCryptBean)
    {
        this.enCryptBean = enCryptBean;
    }
    
    @Override
    public String decrypt(String key, String value)
    {
        if(encrypt) {
            return enCryptBean.decrypt(value).toString();
        }else {
           return value;
        }
    }
    
    public String getSignKey()
    {
        return signKey;
    }
    
    public void setSignKey(String signKey)
    {
        this.signKey = signKey;
    }
    
}
