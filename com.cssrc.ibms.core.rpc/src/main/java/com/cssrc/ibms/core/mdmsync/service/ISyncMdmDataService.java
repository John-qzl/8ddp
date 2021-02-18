package com.cssrc.ibms.core.mdmsync.service;

import org.apache.log4j.Logger;

import com.cssrc.ibms.core.mdmsync.filter.MdmFilter;

/**
 * 数据同步业务处理接口
 * 
 * @ClassName: ISyncMdmDataService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zxg
 * @date 2017年5月17日 下午5:28:19
 * 
 */
public interface ISyncMdmDataService<T>
{

    
    public static Logger logger = Logger.getLogger(ISyncMdmDataService.class);
    
    /**
     * 同步数据处理
     * 
     * @param mdmData
     */
    public void syncData(T mdmData);
    
    /**
     * 获取数据中的 ID值
     * 
     * @param mdmData
     * @return
     */
    public abstract String getFiledIdVal(T mdmData);
    
    /**
     * 获取同步数据的name值
     * 
     * @param mdmData
     * @return
     */
    public abstract String getFiledNameVal(T mdmData);
    
    /**
     * 设置过滤器
     * 
     * @param filter
     */
    public void setFilter(MdmFilter<T> filter);
    
    
    /**
     * 设置 filed controltype 类型
     * 
     * @param contolType
     */
    public void setContolType(Integer[] contolType);
}
