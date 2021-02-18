package com.cssrc.ibms.core.flow.service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.api.activity.model.IDefinition;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;

@Service("alreadyTaskTemplateService")
public class AlreadyTaskTemplateService extends IPendingTaskTemplateService
{
    protected Logger logger = LoggerFactory.getLogger(AlreadyTaskTemplateService.class);
    
    /**
     * @Title: getBusSql
     * @Description: TODO(获取业务表查询语句)
     * @param @param param
     * @param @param pendingSetting
     * @param @return
     * @return String 返回类型
     * @throws
     */
    public String getBusSql(Map<String, Object> params, JSONObject setting)
    {
        StringBuffer sql = new StringBuffer(" select ID as id");
        long tableId = setting.getLong("tableId");
        IFormTable bustab = this.formTableService.getById(tableId);
        List<? extends IFormField> fields = this.formFieldService.getByTableId(tableId);
        for (IFormField f : fields)
        {
            sql.append(",").append(f.getFactFiledName()).append(" as ").append(f.getFieldName());
        }
        sql.append(" FROM ").append(bustab.getFactTableName()).append(" " + bustab.getTableName());
        sql.append(" WHERE 1=1 ");
        String conditionField = setting.getString("conditionField");
        JSONArray conditionFields = JSONArray.parseArray(conditionField);
        for (Object condition : conditionFields)
        {
            JSONObject _condition = (JSONObject)condition;
            if (IPendingTaskTemplateService.settype_bus.equals(_condition.getString("settype")))
            {
                String and = this.getCluaseSQL(_condition, params);
                sql.append(and);
            }
        }
        StringBuffer sql_ = new StringBuffer("select * from (").append(sql).append(")").append("bus WHERE 1=1");
        return sql_.toString();
    }
    
    /**
     * @Title: getTaskSql
     * @Description: TODO(获取流程任务表查询语句)
     * @param @param param
     * @param @param pendingSetting
     * @param @return
     * @return String 返回类型
     * @throws
     */
    public String getTaskSql(Map<String, Object> params, JSONObject setting)
    {
        StringBuffer sql = new StringBuffer(" select DISTINCT ");
        String conditionField = setting.getString("conditionField");
        JSONArray conditionFields = JSONArray.parseArray(conditionField);
        int i = 0;
        for (String filed : already_fields)
        {
            String[] _filed = filed.split("\\.");
            sql.append(filed).append(" as ").append("f_" + _filed[1]);
            if (i < already_fields.length - 1)
            {
                sql.append(",");
            }
            i++;
        }
        sql.append(" FROM IBMS_PRO_RUN_HIS run ");
        // 关联 ACT_HI_ACTINST
        sql.append(" JOIN ACT_HI_ACTINST actinst ON run.actInstId=actinst.PROC_INST_ID_ AND actinst.isstart=0");
        // 关联 CWM_SYS_GLTYPE
        sql.append(" LEFT JOIN CWM_SYS_GLTYPE type on run.typeId = type.typeId");
        // 关联 IBMS_DEFINITION
        sql.append(" LEFT JOIN IBMS_DEFINITION def on run.defId = def.defId");
        // where 条件
        sql.append(" WHERE");
        sql.append(" actinst.end_time_ is not null");
        sql.append(" AND def.status!=3");
        if(params.get("defId")!=null){
            sql.append(" AND def.DEFID in(:defId)");
            Object defids=Arrays.asList(params.get("defId").toString().split(","));
            params.remove("defId");
            params.put("defId", defids);
            
        }
        sql.append(" and run.status in (1,5,6,7)");
        sql.append(" and actinst.ASSIGNEE_=:userId");
        params.put("userId", UserContextUtil.getCurrentUserId());

        StringBuffer sql_ = new StringBuffer("select * from (").append(sql).append(")").append("task WHERE 1=1");
        for (Object condition : conditionFields)
        {
            JSONObject _condition = (JSONObject)condition;
            if (this.equalsSetType(_condition.getString("settype")))
            {
                String and = this.getCluaseSQL(_condition, params);
                sql_.append(and);
            }
        }
        return sql_.toString();
    }
    
    @Override
    public boolean equalsSetType(String settype)
    {
        if (settype.equals(IPendingTaskTemplateService.settype_already))
        {
            return true;
        }
        else if (settype.equals(IPendingTaskTemplateService.settype_all))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public Object getStatus()
    {
        LinkedHashMap<String, String> status = new LinkedHashMap<String, String>();
        status.put("1", "审批中");
        status.put("5", "已撤销");
        status.put("6", "已驳回");
        status.put("11", "重启任务");
        return status;
    }
    
    @Override
    public String getFieldSql(JSONObject pendingSetting)
    {
        
        StringBuffer sql = new StringBuffer("ID as \"id\"");
        long tableId = pendingSetting.getLong("tableId");
        List<? extends IFormField> fields = this.formFieldService.getByTableId(tableId);
        for (IFormField f : fields)
        {
            sql.append(",").append(f.getFieldName()).append(" as \"").append(f.getFieldName()).append("\"");
        }
        for (String filed : already_fields)
        {
            String[] _filed = filed.split("\\.");
            sql.append(",").append("f_" + _filed[1]).append(" as \"").append("f_" + _filed[1]).append("\"");
        }
        
        return sql.toString();
    }

    @Override
    public Object getHasRead()
    {
        return null;
    }

    @Override
    public Object getPendingType()
    {
        return null;
    }

    @Override
    public String getUrl()
    {
        return searchFormURLAlready;

    }

    @Override
    public String getPKField()
    {
        return f_runId;
    }
    public IDefinition getDefinition(Map<String, Object> params){
        Object runId=params.get("runId");
        if(runId!=null){
            IProcessRun processRun=this.processRunService.getById(Long.valueOf(runId.toString()));
            IDefinition definition = definitionService.getById(processRun.getDefId());
            return getRootDefinition(definition);
        }
      return null;
    }
    

    @Override
    public String getPKValue(Map<String, Object> params)
    {
        return params.get("runId").toString();
    }
}
