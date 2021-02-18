package com.cssrc.ibms.core.form.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;











import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.api.form.intf.IFormDataSyncService;
import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.ITableModel;
import com.cssrc.ibms.core.db.datasource.JdbcTemplateUtil;
import com.cssrc.ibms.core.form.model.FormTable;
import com.cssrc.ibms.core.form.model.SqlModel;
import com.cssrc.ibms.core.util.string.StringUtil;

@Service("formDataSyncService")
public class FormDataSyncService implements IFormDataSyncService
{
    @Resource
    IFormTableService formTableService;
    @Resource
    IFormFieldService formFieldService;
    
    private Log logger = LogFactory.getLog(FormDataSyncService.class);

    @Override
    public void handelData(Map<String, Object> data)
    {
        Set<String> keys=data.keySet();
        for(String key:keys){
            Object dataMap=data.get(key);
            Map<String,Object> dataTable=null;
            if(dataMap instanceof Map){
                dataTable=(Map)dataMap;
            }else{
                continue;
            }
            if(dataTable.get("delete")!=null){
                this.handelDelete(key,dataTable);
            }else{
                this.handelUpdate(key, dataTable);
            }
            
            
        }
    }
    
    private void handelDelete(String key,Map<String, Object> dataTable)
    {
        String[] tableInfo=key.split("\\.");
        String tableId=null;
        String tableName=null;
        if(tableInfo.length==2){
            tableId=tableInfo[0];
            tableName=tableInfo[1];
        }else{
            logger.warn("tableInfo is error:"+tableInfo);
            return;
        }
        List<SqlModel> sqls=new ArrayList<SqlModel>();

        //根据表名、表ID 获取关联字段
        List<?extends IFormField> handelFiled=formFieldService.getRelFiledByTableIdAndName(tableId,tableName);
        for(IFormField f:handelFiled){
            String relDialog=f.getRelFormDialogStripCData();
            JSONObject relDialogJson=JSONObject.parseObject(relDialog);
            JSONArray fields=relDialogJson.getJSONArray("fields");
            
            sqls.add(getDelete(f, fields, dataTable));
            
        }
    
        
    }

    private SqlModel getDelete(IFormField f,JSONArray fields,Map<String,Object> mapData)
    {
        Long tableId=f.getTableId();
        FormTable formTable=(FormTable)formTableService.getById(tableId);
        List<Object> values = new ArrayList<Object>();
        String fk=f.getFieldName();
        for(Object field:fields){
            JSONObject _f=(JSONObject)field;
            String src=_f.getString(IFieldPool.FK_DIALOGFIELD);
            String target=_f.getString(IFieldPool.FK_TABLEFIELD);
            if(target.equals(fk)){
                //主键 where 条件 不需要加入set sql 中
                Object data=mapData.get(src.toLowerCase());
                if(data==null){
                    data=mapData.get(src.toUpperCase());
                }
                values.add(data);
                break;
            }
           
        }
        if(f.getRelDelType()==1){
            //直接删除数据
            String sql="delete from "+formTable.getFactTableName()+" where "+f.getFactFiledName()+"=?";
            SqlModel sqlModel=new SqlModel(sql.toString(), values.toArray(),SqlModel.SQLTYPE_DEL);
            sqlModel.setFormTable(formTable);
            return sqlModel;
        }else{
            //关联字段数据置空
            String sql="update "+formTable.getFactTableName()+" set "+f.getFactFiledName()+"=null where "+f.getFactFiledName()+"=?";
            SqlModel sqlModel=new SqlModel(sql.toString(), values.toArray(),SqlModel.SQLTYPE_UPDATE);
            sqlModel.setFormTable(formTable);
            return sqlModel;
        }
    }

    public  void handelUpdate(String key,Map<String,Object> dataTable){
        String[] tableInfo=key.split("\\.");
        String tableId=null;
        String tableName=null;
        if(tableInfo.length==2){
            tableId=tableInfo[0];
            tableName=tableInfo[1];
        }else{
            logger.warn("tableInfo is error:"+tableInfo);
            return;
        }
        List<SqlModel> sqls=new ArrayList<SqlModel>();

        //根据表名、表ID 获取关联字段
        List<?extends IFormField> handelFiled=formFieldService.getRelFiledByTableIdAndName(tableId,tableName);
        for(IFormField f:handelFiled){
            String relDialog=f.getRelFormDialogStripCData();
            JSONObject relDialogJson=JSONObject.parseObject(relDialog);
            JSONArray fields=relDialogJson.getJSONArray("fields");
            sqls.add(this.getUpdate(f, fields, dataTable));
        }
        
        //执行脚本
        for (SqlModel sqlModel : sqls) {
            String sql = sqlModel.getSql();
            FormTable formTable=sqlModel.getFormTable();
            if (StringUtil.isEmpty(sql)) continue;
            Object[] obs = sqlModel.getValues();
            try{
                if(StringUtil.isNotEmpty(sql)){
                    
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    
    /**
     * 获取主表的更新的sqlmodel对象。
     * <pre>
     * 只是更新客户端提交的json数据，如果表单中没有提交任何数据则不更新。
     * </pre>
     * @param bpmFormData
     * @return
     */
    private SqlModel getUpdate(IFormField f,JSONArray fields,Map<String,Object> mapData){
        StringBuffer set = new StringBuffer();
        //构建插入sql语句的2个部分：列名set、列值values。
        String fk=f.getFieldName();
        //关联表主键字段
        String relPK="";

        String factFk=f.getFactFiledName();
        Long tableId=f.getTableId();
        FormTable formTable=(FormTable)formTableService.getById(tableId);
        
        List<Object> values = new ArrayList<Object>();
        List<String> keys=new ArrayList<String>();
        for (Object field:fields){
            JSONObject _f=(JSONObject)field;
            String src=_f.getString(IFieldPool.FK_DIALOGFIELD);
            String target=_f.getString(IFieldPool.FK_TABLEFIELD);
            if(target.indexOf(IFieldPool.FK_SHOWAppCode)>-1){
                continue;
            }
            //通过src 获取参数值。
            Object data=mapData.get(src.toLowerCase());
            if(data==null){
                data=mapData.get(src.toUpperCase());
            }
            //判断当前 target 是否已经放入 set 语句中。
            int i=keys.indexOf(target);
            if(i>-1){
                //如果当前target 已经加入set中了，需要对value 集合对应的值进行修改 使用 ‘-’ 连接处理
                Object val=values.get(i);
                val+="-"+data.toString();
                values.set(i, val);
                continue;
            }
           
            if(!target.equals(fk)){
                //不是关联字段的target 字段都需要加入set sql 中
                keys.add(target);
                set.append(ITableModel.CUSTOMER_COLUMN_PREFIX+target).append("=?,");
                //同时加入参数值。
                values.add(data);
            }else{
                //主键 where 条件 不需要加入set sql 中
                relPK=src;
            }
           
        }
        if(values.size()==0) return null;
        //构建插入sql语句
        StringBuffer sql = new StringBuffer();
        if(set.length() > 0) {
            // sql
            sql.append(" update ");
            sql.append(formTable.getFactTableName());
            sql.append(" set ");
            sql.append(set.substring(0, set.length() - 1));
            sql.append(" where ");
            sql.append(factFk);
            sql.append("=?");
            Object data=mapData.get(relPK.toLowerCase());
            if(data==null){
                data=mapData.get(relPK.toUpperCase());
            }
            values.add(data);
        }
        //构建sqlmodel
        SqlModel sqlModel=new SqlModel(sql.toString(), values.toArray(),SqlModel.SQLTYPE_UPDATE);
        sqlModel.setFormTable(formTable);
        return sqlModel;
    }
    
}
