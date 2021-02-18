package com.cssrc.ibms.core.rpc.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.rpc.intf.CommonService;
import com.cssrc.ibms.api.rpc.intf.ICommonParam;
import com.cssrc.ibms.api.rpc.intf.IDubboInterfaceService;
import com.cssrc.ibms.core.rpc.DubboCustomerFactory;

@Service("dubboInterfaceService")
public class DubboInterfaceService implements IDubboInterfaceService
{
    public  <T> T getService(Class<T> _class, String version){
        return DubboCustomerFactory.getReferenceService(_class, version);
    }
    
    public  <T> List<T> getReferenceServiceList(Class<T> _class, String version){
        return DubboCustomerFactory.getReferenceServiceList(_class, version,true);
    }
    @Override
    public <T> List<T> getReferenceServiceList(Class<T> _class, boolean filterorg)
    {
        return DubboCustomerFactory.getReferenceServiceList(_class, null,filterorg);
    }
    
    @Override
    public <T> List<Map<String, String>> getReferenceServiceNameList(Class<T> _class, boolean filterorg)
    {
        return DubboCustomerFactory.getReferenceServiceNameList(_class, null,filterorg);
    }

    @Override
    public int getMyRequestList(ICommonParam commonParam)
    {
        List<CommonService> sercices=this.getReferenceServiceList(CommonService.class,null);
        int i=0;
        for(CommonService service:sercices){
            i+=service.getMyRequestList(commonParam);
        }
        return i;
    }

    @Override
    public int getPendingMattersList(ICommonParam commonParam)
    {
        List<CommonService> sercices=this.getReferenceServiceList(CommonService.class,null);
        int i=0;
        for(CommonService service:sercices){
            i+=service.getPendingMattersList(commonParam);
        }
        return i;
    }

    @Override
    public int getTemplateDataList(ICommonParam commonParam)
    {
        List<CommonService> sercices=this.getReferenceServiceList(CommonService.class,null);
        int i=0;
        for(CommonService service:sercices){
            i+=service.getTemplateDataList(commonParam);
        }
        return i;
    }

    @Override
    public <T> T getDubboService(Class<T> class_)
    {
        return DubboCustomerFactory.getDubboService(class_);
    }



}
