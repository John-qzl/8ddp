package com.cssrc.ibms.core.flow.service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.api.activity.model.IDefinition;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;

@Service("pendingTaskTemplateService")
public class PendingTaskTemplateService extends IPendingTaskTemplateService
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
        params.put("userId", UserContextUtil.getCurrentUserId());
        StringBuffer sql = new StringBuffer("SELECT * from ( ");
        String commonSql = getCommonSql(params);
        sql.append(commonSql);
        sql.append("WHERE task.ASSIGNEE_ = "+UserContextUtil.getCurrentUserId()+" and task.DESCRIPTION_!='39' and ");
        sql.append("def.status!=3 and execution.SUSPENSION_STATE_ = 1 ");
        sql.append("UNION ALL ");
        sql.append(commonSql);
        sql.append("JOIN act_ru_identitylink ident ON task.id_ = ident.TASK_ID_ ");
        sql.append("WHERE ident.USER_ID_ = :userId ");
        sql.append("and task.ASSIGNEE_ =0 and task.DESCRIPTION_!='39' and def.status!=3 ");
        sql.append("and execution.SUSPENSION_STATE_ = 1 ");
        sql.append("UNION ALL ");
        sql.append(commonSql);
        sql.append("JOIN act_ru_identitylink ident ON task.id_ = ident.TASK_ID_ ");
        sql.append("WHERE ident.type_ = 'org' and ident.group_id_ in (select uo.orgid from ");
        sql.append("cwm_sys_user_position uo where uo.userid= "+UserContextUtil.getCurrentUserId()+" ) ");
        sql.append("and task.ASSIGNEE_ =0 and task.DESCRIPTION_!='39' and def.status!=3 ");
        sql.append("and execution.SUSPENSION_STATE_ = 1 ");
        sql.append("UNION ALL ");
        sql.append(commonSql);
        sql.append("JOIN act_ru_identitylink ident ON task.id_ = ident.TASK_ID_ ");
        sql.append("WHERE ident.type_ = 'role' and ident.group_id_ in (select ur.roleid from ");
        sql.append("cwm_sys_role_user ur where ur.userid= :userId) ");
        sql.append("and task.ASSIGNEE_ =0 and task.DESCRIPTION_!='39' and def.status!=3 ");
        sql.append("and execution.SUSPENSION_STATE_ = 1 ");
        sql.append("UNION ALL ");
        sql.append(commonSql);
        sql.append("JOIN act_ru_identitylink ident ON task.id_ = ident.TASK_ID_ ");
        sql.append("WHERE ident.type_ = 'pos' and ident.group_id_ in (select up.posid from ");
        sql.append("cwm_sys_user_position up where up.userid= :userId ) ");
        sql.append("and task.ASSIGNEE_ =0 and task.DESCRIPTION_!='39' and def.status!=3 ");
        sql.append("and execution.SUSPENSION_STATE_ = 1 ");
        sql.append("UNION ALL ");
        sql.append(commonSql);
        sql.append("JOIN act_ru_identitylink ident ON task.id_ = ident.TASK_ID_ ");
        sql.append("WHERE ident.type_ = 'job' and ident.group_id_ in (select p.jobid from ");
        sql.append("cwm_sys_user_position uo inner join cwm_sys_pos p on p.posid=uo.posid ");
        sql.append("where uo.userid= :userId ) ");
        sql.append("and task.ASSIGNEE_ =0 and task.DESCRIPTION_!='39' and def.status!=3 ");
        sql.append("and execution.SUSPENSION_STATE_ = 1 ");
        sql.append( " )task where 1=1");
        
        String conditionField = setting.getString("conditionField");
        JSONArray conditionFields = JSONArray.parseArray(conditionField);
        for (Object condition : conditionFields)
        {
            JSONObject _condition = (JSONObject)condition;
            if (this.equalsSetType(_condition.getString("settype")))
            {
                String and = this.getCluaseSQL(_condition, params);
                sql.append(and);
            }
        }
        return sql.toString();
    }
    
    private String getCommonSql(Map<String, Object> params)
    {
        params.put("userId", UserContextUtil.getCurrentUserId());
        StringBuffer sql = new StringBuffer("SELECT DISTINCT ");
        for (String filed : pending_fields)
        {
            String[] _filed = filed.split(",");
            sql.append("task.").append(_filed[0]).append(" as ").append("f_" + _filed[1]).append(",");
        }
        sql.append("run.subject f_subject, ");
        sql.append("run.runId f_runId, ");
        sql.append("run.BUSINESSKEY f_businessKey,");
        sql.append("run.ROOT_BUSINESSKEY f_root_businessKey,");

        sql.append("run.processName f_processName, ");
        sql.append("TYPE.typeid f_typeId, ");
        sql.append("run.creator f_creator, ");
        sql.append("run.creatorId f_creatorId, ");
        sql.append("( case when tread.id is NULL then 0 else 1 end) f_hasRead, ");
        sql.append("run.status f_status,");
        sql.append("TYPE.nodePath f_nodePath ");
        sql.append(" FROM ACT_RU_TASK task ");
        sql.append(" LEFT JOIN IBMS_task_read tread ON task.id_ =tread.TASKID and ");
        sql.append(" tread.userId= :userId");
        sql.append(" LEFT JOIN IBMS_PRO_RUN run ON task.PROC_INST_ID_ = run.actInstId ");
        sql.append(" LEFT JOIN cwm_sys_gltype TYPE ON TYPE.typeid = run.typeid ");
        sql.append(" LEFT JOIN IBMS_DEFINITION def on task.PROC_DEF_ID_=def.actDefId ");
        if(params.get("defId")!=null){
            sql.append(" AND def.DEFID in(:defId)");
            Object defids=Arrays.asList(params.get("defId").toString().split(","));
            params.remove("defId");
            params.put("defId", defids);
        }
        sql.append(" LEFT JOIN ACT_RU_EXECUTION execution on task.PROC_INST_ID_ = execution.PROC_INST_ID_ ");
        return sql.toString();
    }
    
    @Override
    public boolean equalsSetType(String settype)
    {
        if (settype.equals(IPendingTaskTemplateService.settype_pending))
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
    public Object getHasRead()
    {
        LinkedHashMap<String, String> status = new LinkedHashMap<String, String>();
        status.put("0", "未读");
        status.put("1", "已读");
        return status;
    }

    @Override
    public Object getPendingType()
    {
        LinkedHashMap<String, String> status = new LinkedHashMap<String, String>();
        status.put("-1", "待办");
        status.put("21", "转办");
        status.put("15", "沟通意见");
        status.put("26", "代理");
        status.put("38", "流转意见");
        return status;
    }
    
    @Override
    public String getFieldSql(JSONObject pendingSetting)
    {
        StringBuffer sql = new StringBuffer();
        long tableId = pendingSetting.getLong("tableId");
        List<? extends IFormField> fields = this.formFieldService.getByTableId(tableId);
        for (IFormField f : fields)
        {
            sql.append(f.getFieldName()).append(" as \"").append(f.getFieldName()).append("\"").append(",");
        }
        for (String filed : pending_fields)
        {
            String[] _filed = filed.split(",");
            sql.append("f_" + _filed[1]).append(" as \"").append("f_" + _filed[1]).append("\",");
        }
        sql.append("f_subject \"f_subject\", ");
        sql.append("f_runId \"f_runId\", ");
        sql.append("f_businessKey \"f_businessKey\", ");
        sql.append("f_root_businessKey \"f_root_businessKey\", ");
        sql.append("f_processName \"f_processName\", ");
        sql.append("f_typeId \"f_typeId\", ");
        sql.append("f_creator \"f_creator\", ");
        sql.append("f_creatorId \"f_creatorId\", ");
        sql.append("f_hasRead \"f_hasRead\", ");
        sql.append("f_status \"f_status\", ");
        sql.append("f_nodePath \"f_nodePath\" ");
        return sql.toString();
    }

    @Override
    public String getUrl()
    {
        return searchFormURLPending;
    }

    @Override
    public String getPKField()
    {
        return "f_taskId";
    }
    
    public IDefinition getDefinition(Map<String, Object> params){
        Object taskId=params.get("taskId");
        if(taskId!=null){
            ExecutionEntity executionEntity=this.bpmService.getExecutionByTaskId(taskId.toString());
            IDefinition definition = definitionService.getByActDefId(executionEntity.getProcessDefinitionId());
            return getRootDefinition(definition);
        }
      return null;
    }

    @Override
    public String getPKValue(Map<String, Object> params)
    {
        return params.get("taskId").toString();
    }


}

