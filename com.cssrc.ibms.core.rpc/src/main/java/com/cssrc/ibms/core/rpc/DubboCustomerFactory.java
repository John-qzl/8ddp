package com.cssrc.ibms.core.rpc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.cssrc.ibms.api.rpc.intf.CommonService;
import com.cssrc.ibms.core.util.appconf.AppUtil;

public class DubboCustomerFactory
{
    
    // 各个业务方的ReferenceConfig缓存
    private static Map<String, ReferenceConfig<Object>> referenceCache = new ConcurrentHashMap<>();
    
    /**
     * 动态获取服务的代理对象
     * 
     * @param _class
     * @param version
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> ReferenceConfig<T> getReferenceConfig(Class<T> _class, String version)
    {
        String referenceKey = DubboRegistryFactory.getKeyPre() + "." + _class.getName();
        
        ReferenceConfig<Object> referenceConfig = referenceCache.get(referenceKey);
        if (null == referenceConfig)
        {
            referenceConfig = new ReferenceConfig<>();
            referenceConfig.setId(referenceKey);
            referenceConfig.setApplication(DubboRegistryFactory.application);
            // 获取代理服务只从当前应用相关的 注册中心中获取代理对象
            referenceConfig.setRegistry(DubboRegistryFactory.getDefaltRegistry());// 多个注册中心可以用setRegistries()
            referenceConfig.setInterface(_class);
            referenceConfig.setVersion(version);
            
            referenceCache.put(referenceKey, referenceConfig);
        }
        return (ReferenceConfig<T>)referenceConfig;
    }
    
    /**
     * 获取服务的代理对象
     * 
     * @param _class
     * @param version
     * @return
     * @return
     */
    public static <T> T getReferenceService(Class<T> _class, String version)
    {
        ReferenceConfig<T> reference = getReferenceConfig(_class, null);
        if (null != reference)
        {
            T callbackService = reference.get();
            return callbackService;
        }
        return null;
    }
    
    /**
     * 获取服务的代理对象
     * 
     * @param _class
     * @param version
     * @param filterorg 是否根据当前应用所属单位进行过滤
     * @return
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> getReferenceServiceList(Class<T> _class, String version,boolean filterorg)
    {
        return getDubboService(_class,version,filterorg);
    }
    
    /**
     * 获取服务的代理对象
     * 
     * @param _class
     * @param version
     * @param filterorg 是否根据当前应用所属单位进行过滤
     * @return
     * @return
     */
    public static <T> List<Map<String, String>> getReferenceServiceNameList(Class<T> _class, String version,
        boolean filterorg)
    {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        List<T> services = getDubboService(_class, version, filterorg);
        Map<String, String> serviceMap = new HashMap<String, String>();
        for (T t : services)
        {
            try
            {
            	String path=((CommonService)t).getClassName();

                /*Object target=ReflectionUtils.getTarget(t);
                target=ReflectionUtils.getFieldValue(target, getTargetKey(target,"handler"));
                target=ReflectionUtils.getFieldValue(target, getTargetKey(target,"invoker"));
                target=ReflectionUtils.getFieldValue(target, getTargetKey(target,"invoker,val$invoker"));
                target=ReflectionUtils.getFieldValue(target, getTargetKey(target,"invoker,val$invoker"));
                Object interface_=ReflectionUtils.getFieldValue(target, "key");
                */
                serviceMap.clear();
                serviceMap.put("intf", path);
                result.add(serviceMap);
            
            }
            catch (Exception e)
            {
                
            }
            
        }
        
        return result;
        
    }
    
    
    @SuppressWarnings("unchecked")
    public static <T> List<T> getDubboService(Class<T> _class, String version,boolean filterorg)
    {

        List<T> result = new ArrayList<>();
        Map<String, T> map = AppUtil.getImplInstance(_class);

        for (String key : map.keySet())
        {
        	T service = map.get(key);
            if(service==null){
                continue;
            }
            
        	String path=((CommonService)service).getClassName();
            //Object target;
            String class_ = service.getClass().getName();
            if (class_.indexOf("com.alibaba.dubbo.common.bytecode.proxy") >= 0)
            {
                try{
                    /*target=ReflectionUtils.getTarget(service);
                    target=ReflectionUtils.getFieldValue(target, getTargetKey(target,"handler"));
                    target=ReflectionUtils.getFieldValue(target, getTargetKey(target,"invoker"));
                    target=ReflectionUtils.getFieldValue(target, getTargetKey(target,"invoker,val$invoker"));
                    target=ReflectionUtils.getFieldValue(target, getTargetKey(target,"invoker,val$invoker"));
                    Object interface_=ReflectionUtils.getFieldValue(target, "key");*/
                    if (path.toString().indexOf(DubboRegistryFactory.getAppOrg() + ".") > 0)
                    {
                        // 表示是dubbo 代理类,并且只取当前系统所属单位单位的消费者
                        result.add(service);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                
                
            }
        }
        return result;
        
    }
    
    public static String getTargetKey(Object obj,String field){
        
        Field[] fs=obj.getClass().getDeclaredFields();
        for(Field f:fs){
            if(f.getName().equals(field)){
                return f.getName();
            }else if(field.indexOf(f.getName())>=0){
                return f.getName();
            }
        }
        return null;
    }
    
    /**
     * 获取服务的代理对象
     * 
     * @param _class
     * @param version
     * @param filterorg 是否根据当前应用所属单位进行过滤
     * @return
     * @return
     */
    public static <T> T getDubboService(Class<T> _class)
    {
        List<T> list= getDubboService(_class,null,true);
        if(list!=null&&list.size()==1){
            return list.get(0);
        }else{
            return null;
        }
    } 
    
    public static void destroyed()
    {
        referenceCache.clear();
        
    }
    
}
