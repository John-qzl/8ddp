package com.cssrc.ibms.core.rpc;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.cssrc.ibms.api.rpc.intf.CommonService;

public class DubboProviderFactory
{
    
    // 暴漏的服务接口缓存
    private static Map<String, ServiceConfig<Object>> serviceConfigCache = new ConcurrentHashMap<>();
    
    // 服务提供者协议配置
    private static ProtocolConfig protocol = new ProtocolConfig();
    
    static
    {
        // 采用 dubbo 通讯协议
        protocol.setName(DubboRegistryFactory.getDubboProtocolName());
        protocol.setPort(Integer.valueOf(DubboRegistryFactory.getDubboProtocolPort()));
        
    }
    
    /**
     * 获取暴漏的服务配置
     * 
     * @param address zk注册地址
     * @param group dubbo服务所在的组
     * @return
     */
    public static ServiceConfig<Object> getServiceConfig(List<RegistryConfig> registries, String beanId)
    {
        String serviceKey = DubboRegistryFactory.getKeyPre()+ "." + beanId;
        ServiceConfig<Object> service = serviceConfigCache.get(serviceKey);
        if (null == service)
        {
            service = new ServiceConfig<Object>(); // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
            service.setId(serviceKey);
            service.setApplication(DubboRegistryFactory.application);
            service.setRegistries(registries); // 多个注册中心可以用setRegistries()
            service.setProtocol(protocol); // 多个协议可以用setProtocols()
            // 需要暴漏的接口class
            service.setInterface(CommonService.class);
            serviceConfigCache.put(serviceKey, service);
            
        }
        return service;
        
    }

    public static void destroyed()
    {
        serviceConfigCache.clear();
        
    }
}
