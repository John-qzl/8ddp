package com.cssrc.ibms.core.mdmsync.comand;

import java.util.Map;

import org.apache.log4j.Logger;

import com.cssrc.ibms.core.mdmsync.filter.MdmFilter;
import com.cssrc.ibms.core.mdmsync.service.ISyncMdmDataService;
import com.cssrc.ibms.core.util.appconf.AppUtil;

/**
 * 数据同步命名接口
 * 
 * @ClassName: SyncCommand
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zxg
 * @date 2017年5月18日 上午8:27:22
 * 
 */
public abstract class SyncCommand<T extends ISyncMdmDataService>
{
    public static Integer[] USER_SELECTOT = {4, 8};// 用户选择器，单选 多选
    
    public static Integer[] ROLE_SELECTOT = {17, 5};// 角色选择器，单选 多选
    
    public static Integer[] ORG_SELECTOT = {18, 6};// 组织选择器，单选 多选
    
    public static Integer[] POS_SELECTOT = {19, 7};// 岗位选择器，单选 多选
    
    public static Integer[] USER_ORG_SELECTOT = {4, 8,18, 6};// 岗位选择器，单选 多选

    
    public static Logger logger = Logger.getLogger(SyncCommand.class);
    /**
     * 同步数据处理代理类
     */
    private Class<T> t;
    
    /**
     * 同步数据
     */
    private Object mdmData;
    
    /**
     * 过滤条件
     */
    protected MdmFilter<?> filter;
    
    public SyncCommand(Class<T> t)
    {
        this.t = t;
        try{
            Map<String,MdmFilter> filters = AppUtil.getImplInstance(MdmFilter.class);
            for(String key: filters.keySet()){
                MdmFilter f=filters.get(key);
                if(this.t.getName().equals(f.getService().getName())){
                    this.filter=f;
                    break;
                }
               
            }
        }catch(Exception e){
            logger.warn("没有找到 过滤bean实现类"+t.getName());
        }

    }
    
    public SyncCommand(Class<T> t, Object mdmData)
    {
        this.t = t;
        this.mdmData = mdmData;
        try{
            Map<String,MdmFilter> filters = AppUtil.getImplInstance(MdmFilter.class);
            for(String key: filters.keySet()){
                MdmFilter f=filters.get(key);
                if(this.t.getName().equals(f.getService().getName())){
                    this.filter=f;
                    break;
                }
               
            }
        }catch(Exception e){
            logger.warn("没有找到 过滤bean实现类"+t.getName());
        }

    }

    public void sync()
    {
        ISyncMdmDataService service=null;
        try{
            service = AppUtil.getBean(t);
        }catch(Exception e){
            logger.info("没找到实现类 " + t.getName());
        }
        if(service!=null){
            logger.info("sync main data " + t.getName());
            service.setFilter(this.filter);
            service.setContolType(this.getConType());
            service.syncData(mdmData);
        }
     
    }
    
    public void setFilter(MdmFilter<?> filter)
    {
        this.filter = filter;
    }

    public abstract Integer[] getConType();
    
}
