package com.cssrc.ibms.core.form.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.cssrc.ibms.api.activity.intf.IBusBakService;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.form.dao.BusBakDao;
import com.cssrc.ibms.core.form.model.BusBak;
import com.cssrc.ibms.core.form.model.FormTable;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

@Service("busBakService")
public class BusBakService extends BaseService<BusBak> implements IBusBakService
{
    public static String version;
    public static Date bakDate;

    @Resource
    private BusBakDao dao;
    @Resource
    private FormTableService formTableService;
    
    @Override
    protected IEntityDao<BusBak, Long> getEntityDao() 
    {
        return dao;
    }
    
    public void setVersion()
    {
        version=DateUtil.getCurrentDate("yyyyMMddhhmmss");
    }
    
    public void setBakInfo()
    {
        bakDate=new Date();
        version=DateUtil.getDateString(bakDate,"yyyyMMddhhmmss");

    }
    
    /**
     * 添加备份数据日志
     * @param pk
     * @param bpmformtable
     */
    public void addBusBak(BusBak busBak,IProcessRun processRun){
        Long id=UniqueIdUtil.genId();
        busBak.setBusId(id);
        busBak.setBakDate(bakDate);
        busBak.setVersion(version);
        dao.add(busBak);
    }

    /**
     * 获取备份数据
     * @param pk
     * @param tableId
     * @param filedName
     */
    public List<BusBak> getBakData(String pk, String tableName, String filedName)
    {
        if(StringUtil.isEmpty(pk)){
        	return null;
        }
        FormTable t=formTableService.getByTableName(tableName);
        List<BusBak> list=dao.getBakData(pk,t.getTableId());
        List<BusBak> result=new ArrayList<>();
        for(BusBak busBak:list){
            String sd=busBak.getSwichBakData();
                if(sd!=null&&!"".equals(sd)){
                    com.alibaba.fastjson.JSONObject data=JSON.parseObject(sd);
                    Object fval=data.get(filedName);
                    Object __remark__=data.get("__remark__");

                    if(fval!=null&&!"".equals(fval.toString())){
                        busBak.setBakData(fval.toString());
                        busBak.setRemark(__remark__!=null?__remark__.toString():"");
                        result.add(busBak);
                    }
                }
        }
        return result;
    }
 
    
}
