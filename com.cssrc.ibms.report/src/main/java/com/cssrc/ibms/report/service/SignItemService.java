package com.cssrc.ibms.report.service;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSON;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.report.inf.ISignItemService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.date.CertUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.report.dao.SignItemDao;
import com.cssrc.ibms.report.model.SignItem;

/** 
* @ClassName: SignItemService 
* @Description: 印章业务类
* @author zxg 
* @date 2016年12月17日 上午10:15:23 
*  
*/
@Service("signItemService")
public class SignItemService extends BaseService<SignItem> implements ISignItemService
{
    private static Logger logger = Logger.getLogger("IBMS.REPORT");
    
    @Resource
    private SignItemDao signItemDao;
    
    @Resource
    private SignModelService signModelService;
    
    @Override
    protected IEntityDao<SignItem, Long> getEntityDao()
    {
        return signItemDao;
    }
    
    /**
     * 保存盖章信息
     * @param request
     * @return
     * @throws Exception
     */
    public Long saveItem(HttpServletRequest request)
        throws Exception
    {
        Long taskId = RequestUtil.getLong(request, "signData[taskId]");
        Long signId = RequestUtil.getLong(request, "signId");
        SignItem item = new SignItem();
        String imgData = signModelService.getImageByte(signId);
        Map<String, String> signData = getSignData(request);
        // 流程ID
        item.setActivitiId(taskId);
        // 原始数据
        item.setData(JSON.toJSONString(signData));
        // 加密数据
        item.setEncryData(JSON.toJSONString(signData));
        // 印章ID
        item.setSignId(signId);
        // form 表单ID
        item.setFormId(null);
        // 印章数据
        item.setImage(imgData);
        // 印章位置信息
        item.setPosition(JSON.toJSONString(getPositionData(request)));
        // 印章签名信息
        item.setSignData("");
        // 业务数据模板ID
        item.setTemplateId(null);
        // 印章模板ID
        item.setCode(Long.valueOf(CertUtil.getNextVersion()));
        item.setId(UniqueIdUtil.genId());
        signItemDao.add(item);
        return item.getId();
        
    }
    
    /**
     * 保存盖章信息
     * @param 获取盖章表单的数据
     * @return
     * @throws Exception
     */
    private Map<String, String> getSignData(HttpServletRequest request)
    {
        Enumeration<String> params = request.getParameterNames();
        Map<String, String> signData = new HashMap<String, String>();
        while (params.hasMoreElements())
        {
            
            String paramName = params.nextElement();
            if (paramName.indexOf("signData[") > -1)
            {
                String val = request.getParameter(paramName);
                paramName = paramName.substring("signData[".length(), paramName.length() - 1);
                signData.put(paramName, val);
            }
            
        }
        return signData;
    }
    
    /**
     * 获取盖章坐标信息
     * @param request
     * @return
     */
    private Map<String, Object> getPositionData(HttpServletRequest request)
    {
        String img = RequestUtil.getString(request, "img");
        Long signId = RequestUtil.getLong(request, "signId");
        // 印章坐标
        Float top = RequestUtil.getFloat(request, "top");
        Float left = RequestUtil.getFloat(request, "left");
        // 客户端宽高
        Float width = RequestUtil.getFloat(request, "width");
        Float height = RequestUtil.getFloat(request, "height");
        // 印章域坐标
        Float areaTop = RequestUtil.getFloat(request, "areaTop");
        Float areaLeft = RequestUtil.getFloat(request, "areaLeft");
        
        Map<String, Object> positionData = new HashMap<String, Object>();
        positionData.put("signId", signId);
        positionData.put("top", top);
        positionData.put("left", left);
        positionData.put("areaTop", areaTop);
        positionData.put("areaLeft", areaLeft);
        positionData.put("width", width);
        positionData.put("height", height);
        
        return positionData;
    }
    

    /**
     * 获取印章image 字节流，印章表存储的是base64编码的字节流
     * @param signId 印章id
     * @return
     */
    @Override
    public String getImgeData(String signId)
    {
        if (signId != null)
        {
            String[] signIds=signId.split(",");
            signId=signIds[signIds.length-1];
            SignItem signItem = this.signItemDao.getById(Long.valueOf(signId));
            if(signItem!=null){
                return signItem.getImage();
            }else{
                return null;
            }
        }
        else
        {
            return null;
        }
        
    }
    
}
