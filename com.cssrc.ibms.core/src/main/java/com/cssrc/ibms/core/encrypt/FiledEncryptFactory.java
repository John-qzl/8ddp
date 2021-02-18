package com.cssrc.ibms.core.encrypt;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @ClassName: FiledEncryptFactory
 * @Description: TODO(加密算法工厂类)
 * @author zxg
 * @date 2017年5月31日 上午9:52:44
 * 
 */
public class FiledEncryptFactory
{
    
    private Log logger = LogFactory.getLog(FiledEncryptFactory.class);

    private Map<String, Class<? extends IFiledEncrypt>> encryptCalculation =
        new LinkedHashMap<String, Class<? extends IFiledEncrypt>>();
    
    public Map<String, Class<? extends IFiledEncrypt>> getEncryptCalculation()
    {
        return encryptCalculation;
    }
    
    public void setEncryptCalculation(Map<String, Class<? extends IFiledEncrypt>> encryptCalculation)
    {
        this.encryptCalculation = encryptCalculation;
    }
    
    /**
     * 取得算法列表。
     * 
     * @return
     */
    public List<String> getEncryptCalculationList()
    {
        List<String> list = new ArrayList<String>();
        Set<String> keys = encryptCalculation.keySet();
        for (String key : keys)
        {
            list.add(key);
        }
        return list;
    }
    
    /**
     * @Title: creatEncrypt
     * @Description: TODO(从map中获取加密算法)
     * @param @param encrypt
     * @param @return 设定文件
     * @return IFiledEncrypt 返回类型
     * @throws
     */
    public IFiledEncrypt creatEncrypt(String encrypt)
    {
        try
        {
            return encryptCalculation.get(encrypt).newInstance();
        }
        catch (Exception e)
        {
            logger.warn("没有找到对应的加密算法");
            return null;
        }
        
    }
    
    /**
     * @Title: creatEncrypt
     * @Description: TODO(自定义加密算法)
     * @param @param encrypt
     * @param @return 设定文件
     * @return IFiledEncrypt 返回类型
     * @throws
     */
    public IFiledEncrypt creatEncrypt(Class<? extends IFiledEncrypt> encrypt)
    {
        try
        {
            return encrypt.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        
    }
}
