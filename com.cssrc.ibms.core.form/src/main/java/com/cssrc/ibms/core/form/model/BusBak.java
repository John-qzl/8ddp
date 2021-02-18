package com.cssrc.ibms.core.form.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.table.TableModel;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.date.DateUtil;

public class BusBak extends BaseModel
{
    private static final long serialVersionUID = 7312825798914353228L;
    
    // 主键
    protected Long busId;
    
    // 对应关联表主键
    protected Long busPk;
    
    // 关联表ID
    protected Long tableId;
    
    // 备份version
    protected String version;
    
    // 备份修改的数据
    protected String swichBakData;
    
    // 备份数据
    protected String bakData;
    
    // 备份时间
    protected Date bakDate;
    
    // 备注
    protected String remark;
    
    public BusBak()
    {
    }
    
    public BusBak(Map<String, Object> swichMap, Map curMaps)
    {
        this.swichBakData = JSON.toJSONString(swichMap);
        this.bakData = JSON.toJSONString(curMaps);
    }
    
    public Long getBusId()
    {
        return busId;
    }
    
    public void setBusId(Long busId)
    {
        this.busId = busId;
    }
    
    public Long getBusPk()
    {
        return busPk;
    }
    
    public void setBusPk(Long busPk)
    {
        this.busPk = busPk;
    }
    
    public String getVersion()
    {
        return version;
    }
    
    public void setVersion(String version)
    {
        this.version = version;
    }
    
    public String getBakData()
    {
        return bakData;
    }
    
    public void setBakData(String bakData)
    {
        this.bakData = bakData;
    }
    
    public Long getTableId()
    {
        return tableId;
    }
    
    public void setTableId(Long tableId)
    {
        this.tableId = tableId;
    }
    
    public Date getBakDate()
    {
        return bakDate;
    }
    
    public void setBakDate(Date bakDate)
    {
        this.bakDate = bakDate;
    }
    
    public String getSwichBakData()
    {
        return swichBakData;
    }
    
    public void setSwichBakData(String swichBakData)
    {
        this.swichBakData = swichBakData;
    }
    
    public static BusBak getInstance(Long tableId,Map<String, Object> curMap, Map<String, Object> editMap,Map<String, Object> params)
    {
        return instance(tableId,curMap, editMap,params);
    }
    
    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }
    
    
    /**
     * 比较 两个list<map> List<BusBak>
     * 
     * @param formTable
     * @param curMaps 当前数据库中的map list
     * @param editMaps 网页修改后的map list
     * @return
     */
    public static List<BusBak> getInstanceList(FormTable formTable, List<Map<String, Object>> curMaps,
        List<Map<String, Object>> editMaps,Map<String, Object> params)
    {
        // 获取主键field
        String pkField = formTable.getPkField();
        List<BusBak> rtnList = new ArrayList<BusBak>();
        Map<String, Map<String, Object>> curMap = convertMap(pkField, curMaps);
        Map<String, Map<String, Object>> editMap = convertMap(pkField, editMaps);
        Set<Entry<String, Map<String, Object>>> editSet = editMap.entrySet();
        // 遍历当前数据
        // 1.如果当前数据包含原来的数据，那么这个数据进行更新。
        for (Iterator<Entry<String, Map<String, Object>>> it = editSet.iterator(); it.hasNext();)
        {
            Entry<String, Map<String, Object>> ent = it.next();
            Map<String, Object> map = ent.getValue();
            // 包含修改数据，则更新。
            if (curMap.containsKey(ent.getKey()))
            {
                BusBak busBak = instance(formTable.getTableId(),curMap.get(ent.getKey()), map,params);
                if (busBak != null)
                {
                    busBak.setBusPk(Long.valueOf(map.get(formTable.getPkField().toLowerCase()).toString()));
                    busBak.setTableId(formTable.getTableId());
                    rtnList.add(busBak);
                }
            }
        }
        // 遍历原来的数据，当前数据不包含原来的数据，那么需要删除。
        Set<Entry<String, Map<String, Object>>> curSet = curMap.entrySet();
        for (Iterator<Entry<String, Map<String, Object>>> it = curSet.iterator(); it.hasNext();)
        {
            Entry<String, Map<String, Object>> ent = it.next();
            // 当前数据不包含之前的数据，则需要删除
            if (!curMap.containsKey(ent.getKey()))
            {
                
            }
        }
        return rtnList;
    }
    
    /**
     * 将列表转化为map对象。
     * 
     * <pre>
     * 主键和一行数据进行关联。
     * </pre>
     * 
     * @param list
     * @return
     */
    private static Map<String, Map<String, Object>> convertMap(String pkField, List<Map<String, Object>> list)
    {
        pkField = pkField.toLowerCase();
        Map<String, Map<String, Object>> rtnMap = new HashMap<String, Map<String, Object>>();
        for (Map<String, Object> map : list)
        {
            if (!map.containsKey(pkField))
            {
                continue;
            }
            String value = map.get(pkField).toString();
            rtnMap.put(value, map);
        }
        return rtnMap;
    }
    
    /**
     * @param curMaps 当期数据库中的数据
     * @param editMaps 网页修改后的数据
     * @return
     */
    public static BusBak instance(Long tableId,Map curMaps, Map editMaps,Map params)
    {
        Map<String, Object> swichMap = new HashMap<String, Object>();
        try
        {
            Set<?> curSet = curMaps.keySet();
            Iterator<?> curIt = curSet.iterator();
            while (curIt.hasNext())
            {
                
                String key = (String)curIt.next();
                if (key.equals(TableModel.PK_COLUMN_NAME) || key.equals(TableModel.PK_COLUMN_NAME.toLowerCase()))
                {
                    continue;
                }
                if (key.equals(TableModel.FK_COLUMN_NAME) || key.equals(TableModel.FK_COLUMN_NAME.toLowerCase()))
                {
                    continue;
                }
                
                Object cVal = curMaps.get(key);
                Object eval = null;
                if (!key.startsWith(TableModel.CUSTOMER_COLUMN_PREFIX))
                {
                    eval = editMaps.get(TableModel.CUSTOMER_COLUMN_PREFIX.toLowerCase() + key.toLowerCase());
                }
                else
                {
                    eval = editMaps.get(key.toLowerCase());
                }
                if (cVal != null && eval != null)
                {
                    if (cVal instanceof Date || eval instanceof Date)
                    {
                        Date eDate = null;
                        Date cDate = null;
                        if (eval instanceof Date && !(cVal instanceof Date))
                        {
                            cDate = DateUtil.getDate(cVal.toString());
                            eDate = (Date)eval;
                        }
                        else if (cVal instanceof Date && !(eval instanceof Date))
                        {
                            eDate = DateUtil.getDate(eval.toString());
                            cDate = (Date)cVal;
                        }
                        else
                        {
                            cDate = (Date)cVal;
                            eDate = (Date)eval;
                        }
                        if (cDate!=null&&eDate!=null&&cDate.getTime() - eDate.getTime() != 0)
                        {
                            if (!key.startsWith(TableModel.CUSTOMER_COLUMN_PREFIX))
                            {
                                swichMap.put(key, cVal);
                                eval =
                                    editMaps.get(TableModel.CUSTOMER_COLUMN_PREFIX.toLowerCase() + key.toLowerCase());
                            }
                            else
                            {
                                swichMap.put(key.toLowerCase()
                                    .replaceAll(TableModel.CUSTOMER_COLUMN_PREFIX.toLowerCase(), ""),
                                    cVal);
                            }
                        }
                    }
                    else if (cVal instanceof Number || eval instanceof Number)
                    {
                    	IFormFieldService fService=AppUtil.getBean(IFormFieldService.class);
                    	IFormField formField=fService.getFieldByTidFna(tableId, key);
        				Map<String,String> paraMap=formField.getPropertyMap();
        				//Object isShowComdify= paraMap.get("isShowComdify");
        				//Object decimalValue= paraMap.get("decimalValue");
        				Object coinValue= paraMap.get("coinValue");
        				cVal=cVal.toString().replace(coinValue.toString(), "").replace(",", "");
        				eval=eval.toString().replace(coinValue.toString(), "").replace(",", "");
        				Double.valueOf(eval.toString());
        				cVal = "".equals(cVal)?"0":cVal;
                        if (Double.valueOf(cVal.toString()) - Double.valueOf(eval.toString()) != 0)
                        {

            				
                            if (!key.startsWith(TableModel.CUSTOMER_COLUMN_PREFIX))
                            {
                                swichMap.put(key, cVal);
                                eval =
                                    editMaps.get(TableModel.CUSTOMER_COLUMN_PREFIX.toLowerCase() + key.toLowerCase());
                            }
                            else
                            {
                                swichMap.put(key.toLowerCase()
                                    .replaceAll(TableModel.CUSTOMER_COLUMN_PREFIX.toLowerCase(), ""),
                                    cVal);
                            }
                        }
                    }
                    else if (!eval.toString().equals(cVal.toString()))
                    {
                        if (!key.startsWith(TableModel.CUSTOMER_COLUMN_PREFIX))
                        {
                            swichMap.put(key, cVal);
                            eval = editMaps.get(TableModel.CUSTOMER_COLUMN_PREFIX.toLowerCase() + key.toLowerCase());
                        }
                        else
                        {
                            swichMap.put(key.toLowerCase().replaceAll(TableModel.CUSTOMER_COLUMN_PREFIX.toLowerCase(),
                                ""),
                                cVal);
                        }
                    }
                }
                else if (cVal == null && eval != null && !"".equals(eval))
                {
                    if (!key.startsWith(TableModel.CUSTOMER_COLUMN_PREFIX))
                    {
                        swichMap.put(key, cVal);
                        eval = editMaps.get(TableModel.CUSTOMER_COLUMN_PREFIX.toLowerCase() + key.toLowerCase());
                    }
                    else
                    {
                        swichMap.put(key.toLowerCase().replaceAll(TableModel.CUSTOMER_COLUMN_PREFIX.toLowerCase(), ""),
                            cVal);
                    }
                }
                
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (swichMap.keySet().size() > 0)
        {
            swichMap.put("__remark__", params.get("__remark__"));
            return new BusBak(swichMap, curMaps);
        }
        else
        {
            return null;
        }
    }


    
    
    
}
