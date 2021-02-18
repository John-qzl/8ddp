package com.cssrc.ibms.core.rpc;

import java.util.List;
import java.util.Map;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.cssrc.ibms.api.rpc.intf.CommonService;
import com.cssrc.ibms.core.util.appconf.AppUtil;

/**
 * 
 * @ClassName: RpcInitService
 * @Description: dubbo 服务自动化 动态注册实现 需要注册的class 必须实现CommonService 接口，并且通过spring管理接口的实现类。
 * @author zxg
 * @date 2017年4月26日 下午2:18:25
 * 
 */
public class RpcRegistryUtil
{
    
    public static RpcRegistryUtil init;
    
    public static void init()
    {
        if (init == null)
        {
            init = new RpcRegistryUtil();
        }
    }
    
    public RpcRegistryUtil()
    {
        Map<String, CommonService> rpcServices = AppUtil.getImplInstance(CommonService.class);
        for (String beanId : rpcServices.keySet())
        {
            // 需要暴漏的接口服务实现class
            Object registryService = rpcServices.get(beanId);
            // 暴漏服务的dubbo 注册中心，可以是一个list集合
            List<RegistryConfig> registries = DubboRegistryFactory.getRegistry();
            ServiceConfig<Object> service = DubboProviderFactory.getServiceConfig(registries, beanId);// 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
            // 暴漏服务的实现class 引用
            service.setRef(registryService);
            // 版本号
            //service.setVersion("1.0.0");
            // 暴露及注册服务
            service.export();
            
        }
    }
    
    public static void destroyed()
    {
        DubboRegistryFactory.destroyed();
        DubboProviderFactory.destroyed();
        DubboCustomerFactory.destroyed();
    }
}
