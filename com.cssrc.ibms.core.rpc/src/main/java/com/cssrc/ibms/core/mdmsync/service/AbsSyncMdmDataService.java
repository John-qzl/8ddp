package com.cssrc.ibms.core.mdmsync.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.core.db.datasource.JdbcTemplateUtil;
import com.cssrc.ibms.core.mdmsync.filter.MdmFilter;
import com.cssrc.ibms.core.util.string.StringUtil;

public abstract class AbsSyncMdmDataService<T> implements ISyncMdmDataService<T>
{
    
    protected MdmFilter<T> filter;
    
    protected Integer[] contolType;
    
    @Resource
    IFormTableService formTableService;
    
    @Resource
    IFormFieldService formFieldService;
    
    @Override
    public void syncData(T mdmData)
    {
        logger.info("service sync  data:" + mdmData.getClass());
        if (contolType == null || contolType.length < 1)
        {
            return;
        }
        List<? extends IFormField> handelFiled = this.getHandelFiled(mdmData);
        for (IFormField f : handelFiled)
        {
            IFormTable formTable = formTableService.getById(f.getTableId());
            this.updateData(formTable, f, mdmData);
        }
    }
    
    /**
     * @Title: getHandelFiled
     * @Description: TODO(获取需要更新的字段)
     * @param @param mdmData
     * @param @return
     * @return List<? extends IFormField> 返回类型
     * @throws
     */
    public List<? extends IFormField> getHandelFiled(T mdmData)
    {
        List<? extends IFormField> handelFiled = formFieldService.getFiledBySelector(contolType);
        return handelFiled;
    }
    
    /**
     * 设置过滤器
     * 
     * @param filter
     */
    @Override
    public void setFilter(MdmFilter<T> filter)
    {
        this.filter = filter;
    }
    
    /**
     * 设置 filed controltype 类型
     * 
     * @param filter
     */
    @Override
    public void setContolType(Integer[] contolType)
    {
        this.contolType = contolType;
    }
    
    /**
     * 获取过滤条件语句接口
     * 
     * @param formTable
     * @param f
     * @param mdmData
     * @return
     */
    public List<String> getFilterSql(IFormTable formTable, IFormField f, T mdmData)
    {
        if (filter != null)
        {
            return this.filter.getFilter(formTable, f, mdmData);
        }
        else
        {
            return null;
        }
        
    }
    
    /**
     * 获取数据中的 ID值
     * 
     * @param mdmData
     * @return
     */
    public abstract String getFiledIdVal(T mdmData);
    
    /**
     * 获取同步数据的name值
     * 
     * @param mdmData
     * @return
     */
    public abstract String getFiledNameVal(T mdmData);
    
    /**
     * 
     * @param formTable
     * @param f
     * @param mdmData
     * @return
     */
    public void updateData(IFormTable formTable, IFormField f, T mdmData)
    {
        List<String> updates = new ArrayList<String>();
        JdbcTemplate jdbcTemplate = JdbcTemplateUtil.getFormTableJdbcTemplate(formTable);
        // name 字段名
        String fname = f.getFactFiledName();
        // id 字段名
        String fnameId = f.getFactFiledName() + "ID";
        // name 字段值
        String fnameVal = this.getFiledNameVal(mdmData);
        // ID 字段值
        String fnameIdVal = this.getFiledIdVal(mdmData);
        
        List<Map<String, Object>> result = this.getTableData(jdbcTemplate, formTable, f, mdmData);
        
        for (Map<String, Object> data : result)
        {
            String id = data.get("ID").toString();
            String[] fnameVals = data.get(fname).toString().split(",");
            String[] fnameIdVals = data.get(fnameId).toString().split(",");
            int i = 0;
            for (String idVal : fnameIdVals)
            {
                if (idVal.equals(fnameIdVal))
                {
                    fnameVals[i] = fnameVal;
                }
                i++;
            }
            String sqlUpdate =this.getUpDateSql(formTable, f, mdmData,id,fnameVals);
            if(StringUtil.isNotEmpty(sqlUpdate)){
                updates.add(sqlUpdate);
            }
            
        }
        if (updates != null && updates.size() > 0)
        {
            jdbcTemplate.batchUpdate(updates.toArray(new String[0]));
        }
    }
    
    public String getUpDateSql(IFormTable formTable, IFormField f, T mdmData,String id,String[] fnameVals)
    {
        if(filter!=null){
            return filter.getUpDateSql(formTable,f,mdmData,id,fnameVals);
        }else{
            // name 字段名
            String fname = f.getFactFiledName();
            String sqlUpdate = "update " + formTable.getFactTableName() + " set " + fname + "='"
            + StringUtil.getStringFromArray(fnameVals, ",") + "'" + " where ID=" + id;
            return sqlUpdate;
        }
       
    }

    private List<Map<String, Object>> getTableData(JdbcTemplate jdbcTemplate, IFormTable formTable, IFormField f,
        T mdmData)
    {
        // 更新的表名
        String tableName = formTable.getFactTableName();
        // name 字段名
        String fname = f.getFactFiledName();
        // id 字段名
        String fnameId = f.getFactFiledName() + "ID";
        // ID 字段值
        String fnameIdVal = this.getFiledIdVal(mdmData);
        
        StringBuffer sql = new StringBuffer("select ");
        sql.append(" ID as \"ID\",");
        sql.append(fname + " as \"" + fname + "\"" + ",");
        sql.append(fnameId + " as \"" + fnameId + "\"");
        sql.append(" from " + tableName + " where " + fnameIdVal + " in(" + fnameId + ")");
        List<String> sqlFilters = this.getFilterSql(formTable, f, mdmData);
        if (filter != null&&sqlFilters!=null)
        {
            for (String filter : sqlFilters)
            {
                sql.append(filter);
            }
        }
        return jdbcTemplate.queryForList(sql.toString());
        
    }
}
