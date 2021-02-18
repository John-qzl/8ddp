package com.cssrc.ibms.core.rpc;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.cssrc.ibms.core.util.appconf.AppUtil;

public class DubboRegistryFactory
{
    // 当前应用的信息
    public static ApplicationConfig application = new ApplicationConfig();
    
    // 注册中心信息缓存
    public static Map<String, RegistryConfig> registryConfigCache = new ConcurrentHashMap<>();
    
    // dubbo 所有相关配置文件，这一块后续可以移到数据库进行维护
    public static Properties properties = (Properties)AppUtil.getBean("dubboproperties");
    
    // 当前系统dubbo 服务注册中心列表
    private static List<RegistryConfig> registries = new ArrayList<RegistryConfig>();
    
    static
    {
        // 当前应用配置
        application.setName(getAppOrg() + "." + getAppName());
    }
    
    /**
     * 获取注册中心信息
     * 
     * @param address zk注册地址
     * @return
     */
    public static RegistryConfig getRegistryConfig(String address, String id)
    {
        // 连接注册中心配置
        String key = address;
        // 先从缓存中获取
        RegistryConfig registryConfig = registryConfigCache.get(key);
        if (null == registryConfig)
        {
            registryConfig = new RegistryConfig();
            registryConfig.setAddress(address);
            registryConfig.setId(id);
            registryConfigCache.put(key, registryConfig);
        }
        return registryConfig;
    }
    
    /**
     * 获取当前系统应用 所有dubbo 服务注册列表
     * 
     * @param properties
     * @return
     */
    public static List<RegistryConfig> getRegistry()
    {
        
        if (registries.size() < 1)
        {
            Enumeration<?> keys = properties.propertyNames();
            while (keys.hasMoreElements())
            {
                String address = (String)keys.nextElement();
                String registryId = "";
                if (address.startsWith("dubbo.registry.") && address.endsWith(".url"))
                {
                    registryId = address.replace(".url", ".id");
                    registryId = properties.getProperty(registryId);
                    String[] registryIds=registryId.split(",");
                    address = properties.getProperty(address);
                    RegistryConfig registryConfig = getRegistryConfig(address, registryIds[0]);
                    registries.add(registryConfig);
                }
                
            }
        }
        
        return registries;
    }
    
    
    /**
     * 获取当前系统应用 所有dubbo 服务注册列表
     * 
     * @param properties
     * @return
     */
    public static RegistryConfig getDefaltRegistry()
    {
        
        Enumeration<?> keys = properties.propertyNames();
        while (keys.hasMoreElements())
        {
            String address = (String)keys.nextElement();
            String registryId = "";
            if (address.startsWith("dubbo.registry.") && address.endsWith(".url"))
            {
                registryId = address.replace(".url", ".id");
                registryId = properties.getProperty(registryId);
                String[] registryIds=registryId.split(",");
                if("default".equals(registryIds[1])){
                    address = properties.getProperty(address);
                    RegistryConfig registryConfig = getRegistryConfig(address, registryId);
                    return registryConfig; 
                }

            }
            
        }
        return null;
        
      
    }
    
    
    public static String getAppName()
    {
        return properties.get("app.name").toString();
    }
    
    public static String getAppOrg()
    {
        return properties.get("app.org").toString();
    }
    
    public static String getKeyPre()
    {
        return getAppName() + "." + getAppOrg();
    }
    
    public static String getDubboProtocolPort()
    {
        return properties.get("dubbo.protocol.port").toString();
    }
    
    public static String getDubboProtocolName()
    {
        return properties.get("dubbo.protocol.name").toString();
    }
    
    public static void destroyed()
    {
        registryConfigCache.clear();
        registries.clear();
        
    }
    
}
