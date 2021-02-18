package com.cssrc.ibms.core.flow.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.form.model.IFormDef;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.system.intf.IDictionaryService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.GangedSetDao;
import com.cssrc.ibms.core.flow.model.GangedSet;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 
 *<pre>
 * 对象功能:联动设置表 Service类 
 * 开发人员:zhulongchao 
 *</pre>
 */
@Service
public class GangedSetService extends BaseService<GangedSet>
{
    @Resource
    private GangedSetDao dao;
    
    @Resource
    private IFormTableService formTableService;
    
    @Resource
    private IFormFieldService formFieldService;
    
    @Resource
    private IFormDefService formDefService;
    @Resource
    IDictionaryService dictionaryService;
    
    public GangedSetService()
    {
    }
    
    @Override
    protected IEntityDao<GangedSet, Long> getEntityDao()
    {
        return dao;
    }
    
    /**
     * 过滤字段类型，只选取下拉框、单选按钮、复选框
     * @param tableFields
     */
    private List<IFormField> filterFields(List<? extends IFormField> tableFields)
    {
        List<IFormField> temp = new ArrayList<IFormField>();
        for (IFormField formField : tableFields)
        {
            Short cType = formField.getControlType();
            if (cType == IFieldPool.SELECT_INPUT || cType == IFieldPool.CHECKBOX || cType == IFieldPool.RADIO_INPUT
               )
            {
                temp.add(formField);
            }
            if(cType == IFieldPool.DICTIONARY) {
                   //如果是数据字典需要获取数据字典选项
                List<Map<String, Object>> options=dictionaryService.getByNodeDictKeyGangedSet(formField.getDictType());
                formField.setOptions(JSONArray.fromObject(options).toString());
                temp.add(formField);
            }
        }
        return temp;
    }
    
    /**
     * 通过流程定义ID获取联动设置
     * @param defId
     * @return
     */
    public List<GangedSet> getByDefId(Long defId)
    {
        return dao.getByDefId(defId);
    }
    
    /**
     * 通过流程定义ID和NODEID获取联动设置
     * <pre>
     * 	首先获取该节点的联动设置，如果不存在则获取所有节点的联动设置
     * </pre>
     * @param defId
     * @param nodeId
     * @return
     */
    public List<GangedSet> getByDefIdAndNodeId(Long defId, String nodeId)
    {
        List<GangedSet> list = dao.getByDefIdAndNodeId(defId, nodeId);
        if (BeanUtils.isEmpty(list))
            list = dao.getByDefIdAndNodeId(defId, GangedSet.ALL_NODEID);
        return list;
    }
    
    /**
     * 通过DEFID获取起始节点的联动设置
     * <pre>
     * 	首先获取起始节点的联动设置，如果不存在则获取所有节点的联动设置
     * </pre>
     * @param defId
     * @return
     */
    public List<GangedSet> getStartNodeByDefId(Long defId)
    {
        List<GangedSet> list = dao.getByDefIdAndNodeId(defId, "1");
        if (BeanUtils.isEmpty(list))
            list = dao.getByDefIdAndNodeId(defId, "2");
        return list;
    }
    
    /**
     * 批量保存联动设置，并删除不在保存列表中的记录
     * @param defId
     * @param json
     */
    public void batchSave(Long defId, String json)
    {
        List<Long> gangedSetId = new ArrayList<Long>();
        if (StringUtil.isEmpty(json))
        {
            delByDefIdExceptSetIds(defId, gangedSetId);
            return;
        }
        JSONArray jArray = (JSONArray)JSONArray.fromObject(json);
        for (Object object : jArray)
        {
            JSONObject jObject = (JSONObject)object;
            Long id = jObject.getLong("id");
            GangedSet gangedSet;
            if (id > 0)
            {
                gangedSet = dao.getById(id);
                gangedSetId.add(id);
            }
            else
            {
                Long newId = UniqueIdUtil.genId();
                gangedSet = new GangedSet();
                gangedSet.setId(newId);
                gangedSetId.add(newId);
            }
            gangedSet.setDefid(jObject.getLong("defId"));
            gangedSet.setNodeid(jObject.getString("nodeId"));
            gangedSet.setNodename(jObject.getString("nodeName"));
            gangedSet.setChoisefield(jObject.getString("chooseField"));
            gangedSet.setChangefield(jObject.getString("changeField"));
            if (id > 0)
                dao.update(gangedSet);
            else
                dao.add(gangedSet);
        }
        delByDefIdExceptSetIds(defId, gangedSetId);
    }
    
    /**
     * 通过defid删除除了setIds以外的记录
     * @param defId
     * @param setIds
     */
    private void delByDefIdExceptSetIds(Long defId, List<Long> setIds)
    {
        String ids = "";
        for (Long setId : setIds)
        {
            if (StringUtil.isEmpty(ids))
                ids += setId.toString();
            else
            {
                ids += ",";
                ids += setId.toString();
            }
        }
        dao.delByDefIdExceptSetId(defId, ids);
    }
    
    /**
     * 获取表单的所有主表、子表字段
     * @param formkey
     * @return
     */
    public String getFieldsByFormkey(Long formkey, boolean ifFilter)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        IFormDef bpmFormDef = formDefService.getDefaultVersionByFormKey(formkey);
        Long tableId = bpmFormDef.getTableId();
        
        IFormTable mainTable = formTableService.getById(tableId);
        List<? extends IFormField> mainTableFields = formFieldService.getByTableId(tableId);
        // 是否过滤字段类型（选择框字段需要过滤，变更字段无需过滤）
        if (ifFilter)
        {
            mainTableFields = this.filterFields(mainTableFields);
        }
        sb.append(getJson(mainTable, mainTableFields, true, ifFilter));
        List<? extends IFormTable> subTables = formTableService.getSubTableByMainTableId(tableId);
        for (IFormTable subTable : subTables)
        {
            Long subTableId = subTable.getTableId();
            List<? extends IFormField> subFields = formFieldService.getByTableId(subTableId);
            // 是否过滤字段类型（选择框字段需要过滤，变更字段无需过滤）
            if (ifFilter)
            {
                subFields = this.filterFields(subFields);
            }
            sb.append(getJson(subTable, subFields, false, ifFilter));
        }
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * 获取json
     * @param table 表
     * @param fields 字段
     * @param ifFilter  是否过滤
     * @return
     */
    public String getJson(IFormTable table, List<? extends IFormField> fields, Boolean isMain, Boolean ifFilter)
    {
        if (BeanUtils.isEmpty(fields))
            return "";
        StringBuffer sb = new StringBuffer();
        JSONArray jsonArray = formFieldService.getFiledJSON(table, fields, isMain, ifFilter);
        if (!isMain)
            sb.append(",");
        sb.append("{name:\"")
            // 表没有填写描述时，取表名
            .append(StringUtil.isEmpty(table.getTableDesc()) ? table.getTableName() : table.getTableDesc())
            .append("\",key:\"")
            .append(table.getTableName())
            .append("\",type:")
            .append(isMain ? "1" : "0")
            .append(",fields:")
            .append(jsonArray.toString())
            .append("}");
        
        return sb.toString();
    }
}
