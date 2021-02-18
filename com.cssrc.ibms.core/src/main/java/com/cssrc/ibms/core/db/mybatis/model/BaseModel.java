package com.cssrc.ibms.core.db.mybatis.model;


import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlType;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

/*import zlicense.verify.VerifyLicense;*/

/**
 * 实体基类
 * 
 * @author zhulongchao
 * 
 */
@XmlType(name = "iBaseModel")
public class BaseModel implements Serializable
{
    public  static Logger logger = Logger.getLogger(BaseModel.class);
    private static final long serialVersionUID = 255014927471726913L;
    
    public BaseModel()
    {
        try
        {
//            String license =
//                SysConfConstant.CONF_ROOT + File.separator + "properties" + File.separator + "lincense.lic";
//            VerifyLicense.verifyLicense(license);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 创建人ID
     */
    protected Long createBy;
    
    /**
     * 创建时间
     */
    protected Date createtime;
    
    /**
     * 更新时间
     */
    protected Date updatetime;
    
    /**
     * 更新人ID
     */
    protected Long updateBy;
    
    public Long getCreateBy()
    {
        return createBy;
    }
    
    public void setCreateBy(Long createBy)
    {
        this.createBy = createBy;
    }
    
    public Date getCreatetime()
    {
        return createtime;
    }
    
    public void setCreatetime(Date createtime)
    {
        this.createtime = createtime;
    }
    
    public Date getUpdatetime()
    {
        return updatetime;
    }
    
    public void setUpdatetime(Date updatetime)
    {
        this.updatetime = updatetime;
    }
    
    public Long getUpdateBy()
    {
        return updateBy;
    }
    
    public void setUpdateBy(Long updateBy)
    {
        this.updateBy = updateBy;
    }
    
    public String getStingFromJson(String key, String json)
    {
        try
        {
            String[] keys = key.split("\\.");
            JSONObject jsonObject = JSONObject.parseObject(json);
            for(int i=0;i<keys.length;i++) {
                if(i!=keys.length-1) {
                    jsonObject=jsonObject.getJSONObject(keys[i]);
                }else {
                    return jsonObject.getString(keys[i]);
                }
            }
            return "";
        }
        catch (Exception e)
        {
            return "";
        }
    }
}
