package com.cssrc.ibms.core.flow.status;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.cssrc.ibms.core.flow.dao.ProStatusDao;
import com.cssrc.ibms.core.flow.model.ProStatus;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;

public class FlowStatusService
{
    private String stataConf;
    
    private ProStatusDao proStatusDao;
    
    private Map<Short, FlowStatus> statusColorMap;
    
    /** 
    * @Title: getStatusByInstanceId 
    * @Description: TODO(获取流程状态用于显示流程图task边框颜色) 
    * @param @param instanceId
    * @param @return     
    * @return Map<String,String>    返回类型 
    * @throws 
    */
    public Map<String, String> getStatusByInstanceId(Long instanceId)
    {
        Map<String, String> map = new HashMap<String, String>();
        List<ProStatus> list = this.proStatusDao.getByActInstanceId(instanceId.toString());
        for (ProStatus obj : list)
        {
            FlowStatus statu = statusColorMap.get(obj.getStatus());
            map.put(obj.getNodeid(), statu.getColor());
        }
        return map;
    }
    
    /** 
    * @Title: getTaskStatus 
    * @Description: TODO(获取流程状态用于显示状态文本描述) 
    * @param @param status
    * @param @param flag
    * @param @return     
    * @return String    返回类型 
    * @throws 
    */
    public String getTaskStatus(Short status, int flag)
    {
        if (statusColorMap.get(status) != null)
        {
            return statusColorMap.get(status).getTextHtml();
        }
        else
        {
            return "";
        }
    }
    
    public String getStataConf()
    {
        return stataConf;
    }
    
    public void setStataConf(String stataConf)
    {
        if (statusColorMap == null)
        {
            statusColorMap = new HashMap<>();
        }
        this.stataConf = stataConf;
        try
        {
            FlowStatusList statusList = XmlBeanUtil.unmarshall(new FileInputStream(stataConf), FlowStatusList.class);
            if (statusList != null)
            {
                List<FlowStatus> flowStatus = statusList.getStatus();
                for (FlowStatus fstat : flowStatus)
                {
                    statusColorMap.put((short)fstat.getKey(), fstat);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
    }
    
    public ProStatusDao getProStatusDao()
    {
        return proStatusDao;
    }
    
    public void setProStatusDao(ProStatusDao proStatusDao)
    {
        this.proStatusDao = proStatusDao;
    }
    
    public List<FlowStatus> getBarFlowStatus() {
        List<FlowStatus> list=new ArrayList<>();
        for(Short key:statusColorMap.keySet()) {
            if(statusColorMap.get(key).isShowbar()) {
                list.add(statusColorMap.get(key));
            }
        }
        return list;
    }
    
}
