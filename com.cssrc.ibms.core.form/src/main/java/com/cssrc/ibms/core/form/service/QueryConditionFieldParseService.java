package com.cssrc.ibms.core.form.service;

 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.core.db.mybatis.entity.SQLClause;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.form.model.DataTemplate;
import com.cssrc.ibms.core.form.model.QueryField;
import com.cssrc.ibms.core.form.model.QuerySetting;
import com.cssrc.ibms.core.table.ColumnModel;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;

@Service
public class QueryConditionFieldParseService {

  @Resource
  private QueryFieldService sysQueryFieldService;

  @Resource
  private GroovyScriptEngine groovyScriptEngine;

  public boolean hasConditionField(String conditionField)
  {
    if (StringUtil.isEmpty(conditionField))
      return false;
    JSONArray jsonAry = JSONArray.fromObject(conditionField);
    return jsonAry.size() > 0;
  }

  public String getDefaultConditionField(Long sqlId)
  {
    List<QueryField> sysQueryFields = this.sysQueryFieldService
      .getConditionFieldListBySqlId(sqlId);
    if (sysQueryFields == null)
      return null;
    JSONArray jsonArray = new JSONArray();
    for (QueryField field : sysQueryFields) {
      JSONObject jsonObject = new JSONObject();
      jsonObject.accumulate("id", field.getId());
      jsonObject.accumulate("na", field.getName());
      if ("VARCHAR2".equalsIgnoreCase(field.getType()))
        jsonObject.accumulate("ty", "varchar");
      else {
        jsonObject.accumulate("ty", field.getType().toLowerCase());
      }
      jsonObject.accumulate("op", "1");
      jsonObject.accumulate("cm", field.getFieldDesc());
      jsonObject.accumulate("va", "");
      jsonObject.accumulate("vf", "1");
      jsonObject.accumulate("ct", field.getControlType() == null ? 1 : 
        field.getControlType().shortValue());
      jsonObject.accumulate("qt", "S");
      jsonArray.add(jsonObject);
    }
    return jsonArray.toString();
  }

  public String getQuerySQL(QuerySetting sysQuerySetting, String where, Map<String, Object> params)
  {
    StringBuffer sb = new StringBuffer();
    String and = StringUtils.isEmpty(where) ? "" : " AND ";
    List<SQLClause> conditionFields = getConditionList(sysQuerySetting.getConditionField());
    if (BeanUtils.isEmpty(conditionFields))
      return where;
    //查询条件字段的sql拼接(where *and*)
    for (SQLClause condition : conditionFields) {
      getCluaseSQL(sysQuerySetting, condition, params, sb);
    }
    if (sb.length() > 0) {
      where = where + and + "  1=1 " + sb.toString();
    }
    return where;
  }

  private List<SQLClause> getConditionList(String conditionField)
  {
    List<SQLClause> conditionFields = new ArrayList<SQLClause>();
    if (StringUtil.isEmpty(conditionField))
      return conditionFields;
    JSONArray jsonArray = JSONArray.fromObject(conditionField);
    for (int i = 0; i < jsonArray.size(); i++) {
      JSONObject jsonObject = (JSONObject)jsonArray.get(i);
      SQLClause field = new SQLClause();

      field.setJoinType("AND");
      field.setName(jsonObject.getString("na"));
      field.setComment(jsonObject.getString("cm"));
      field.setType(jsonObject.getString("ty"));
      field.setValue(jsonObject.get("va"));
      field.setValueFrom(jsonObject.getInt("vf"));
      field.setOperate(jsonObject.getString("op"));
      field.setControlType(jsonObject.getString("ct"));
      field.setQueryType(jsonObject.getString("qt"));
      conditionFields.add(field);
    }
    return conditionFields;
  }

  private void getCluaseSQL(QuerySetting sysQuerySetting, SQLClause condition, Map<String, Object> params, StringBuffer sb)
  {
    String field = condition.getName();
    String operate = condition.getOperate();
    int valueFrom = condition.getValueFrom();
    String joinType = condition.getJoinType();
    String type = condition.getType();
    joinType = " " + joinType + " ";
    String controlType = condition.getControlType();
    //判断是否选择器
    Boolean isSelector = isSelector(controlType);
    Map<String, Object> dateMap = getQueryValue(condition, params, field, type, operate);
    //查询值确定(选择器选的是ID并不是显示值)
    Object value = getQueryValue(condition, params, field, isSelector);
    if ((BeanUtils.isEmpty(value)) && (BeanUtils.isEmpty(dateMap)))
      return;
    if (isSelector.booleanValue()) {
      if (isrMultiSelector(condition.getControlType()).booleanValue()) {
        sb.append(joinType + field+"ID" + " in ( " + addColon(value) + " )");
      }
      else if (operate.equalsIgnoreCase("2")) {
        sb.append(joinType + field+"ID" + "!=:" + field);
        params.put(field, String.valueOf(value));
      }else {
        sb.append(joinType + field+"ID" + "=:" + field);
        params.put(field, String.valueOf(value));
      }

    }
    else if (type.equals(ColumnModel.COLUMNTYPE_VARCHAR)) {
      value = value.toString();
      if (operate.equalsIgnoreCase("1")) {
        sb.append(joinType).append(field).append("=").append(":")
          .append(field);
      } else if (operate.equalsIgnoreCase("2")) {
        sb.append(joinType).append(field).append(" != ")
          .append(":").append(field);
      } else if (operate.equalsIgnoreCase("3")) {
        value = "%" + value.toString() + "%";
        sb.append(joinType).append(field).append(" LIKE :")
          .append(field);
      } else if (operate.equalsIgnoreCase("4")) {
        value = "%" + value.toString();
        sb.append(joinType).append(field).append(" LIKE :")
          .append(field);
      } else if (operate.equalsIgnoreCase("5")) {
        value = value.toString() + "%";
        sb.append(joinType).append(field).append(" LIKE :")
          .append(field);
      } else {
        value = "%" + value.toString() + "%";
        sb.append(joinType).append(field).append(" LIKE :")
          .append(field);
      }
      params.put(field, value);
    } else if (type.equals(ColumnModel.COLUMNTYPE_DATE)) {
      if ("6".equalsIgnoreCase(operate)) {
        if (BeanUtils.isNotEmpty(dateMap
          .get("begin"))) {
          String begingField = "begin" + field;
          sb.append(joinType + field + ">=:" + begingField + " ");
          params.put(begingField, 
            dateMap.get("begin"));
        }
        if (BeanUtils.isNotEmpty(dateMap.get("end"))) {
          String endField = "end" + field;
          sb.append(joinType + field + "<:" + endField + " ");
          params.put(endField, dateMap.get("end"));
        }
      } else {
        String op = getOperate(operate);
        if (valueFrom == 1) {
          if (params.containsKey(field))
            sb.append(joinType + field + op + ":" + field + " ");
        }
        else {
          sb.append(joinType + field + op + ":" + field + " ");
          params.put(field, value);
        }
      }
    }else if(type.equals(ColumnModel.COLUMNTYPE_NUMBER)){//数字
		if("7".equalsIgnoreCase(operate)){
			//数字范围特殊处理
			if (BeanUtils.isNotEmpty(dateMap.get(DataTemplate.NUMBER_BEGIN))) {
				String begingField = "begin" + field;
				sb.append(joinType + field + ">=:" + begingField + " ");
				params.put(begingField,
						dateMap.get("begin"));
			}
			if (BeanUtils.isNotEmpty(dateMap.get(DataTemplate.NUMBER_END))) {
				String endField = "end" + field;
				sb.append(joinType + field + "<=:" + endField + " ");
				params.put(endField, dateMap.get("end"));
			}
		}else{
			//一般的>、<、>=、<=、=处理
			String op = this.getOperate(operate);
			if (valueFrom == 1) {
				if (params.containsKey(field)) {

					sb.append(joinType + field + op + ":" + field + " ");
				}
			} else {
				sb.append(joinType + field + op + ":" + field + " ");
				params.put(field, value);
			}
		}
	} else {
      String op = getOperate(operate);
      if (valueFrom == 1) {
        if (params.containsKey(field))
        {
          sb.append(joinType + field + op + ":" + field + " ");
        }
      } else {
        sb.append(joinType + field + op + ":" + field + " ");
        params.put(field, value);
      }
    }
  }

  private String addColon(Object value)
  {
    StringBuffer sbf = new StringBuffer();
    if (value != null) {
      String[] StrArr = value.toString().split(",");
      int size = StrArr.length;
      for (int i = 0; i < size; i++) {
        sbf.append("'").append(StrArr[i]).append("'");
        if (size - i > 1) {
          sbf.append(",");
        }
      }
      return sbf.toString();
    }
    return "";
  }

  private String getOperate(String operate)
  {
    String op = "=";
    if ("1".equalsIgnoreCase(operate))
      op = "=";
    else if ("2".equalsIgnoreCase(operate))
      op = ">";
    else if ("3".equalsIgnoreCase(operate))
      op = "<";
    else if ("4".equalsIgnoreCase(operate))
      op = ">=";
    else if ("5".equalsIgnoreCase(operate)) {
      op = "<=";
    }
    return op;
  }

  private Boolean isrMultiSelector(String controlType)
  {
    if (BeanUtils.isEmpty(controlType))
      return Boolean.valueOf(false);
    Short controlType1 = Short.valueOf(controlType);
    if ((controlType1.shortValue() == 8) || 
      (controlType1.shortValue() == 6) || 
      (controlType1.shortValue() == 7) || 
      (controlType1.shortValue() == 5))
      return Boolean.valueOf(true);
    return Boolean.valueOf(false);
  }

  private Map<String, Object> getQueryValue(SQLClause condition, Map<String, Object> params, String field, String type, String operate)
  {
		Map<String, Object> map = new HashMap<String, Object>();
		if (type.equals(ColumnModel.COLUMNTYPE_DATE)
				&& "6".equalsIgnoreCase(operate)) {
			String beginKey = DataTemplate.DATE_BEGIN + field;
			Object beginVal = null;
			String endKey = DataTemplate.DATE_END + field;
			Object endVal = null;
			if (params.containsKey(beginKey))
				beginVal = params.get(beginKey);
			if (params.containsKey(endKey))
				endVal = params.get(endKey);
			if (BeanUtils.isNotEmpty(beginVal) || BeanUtils.isNotEmpty(endVal)) {
				map.put(DataTemplate.DATE_BEGIN, beginVal);
				map.put(DataTemplate.DATE_END, endVal);
			}
		}
		if (type.equals(ColumnModel.COLUMNTYPE_NUMBER)
				&& "7".equalsIgnoreCase(operate)) {
			String beginKey = DataTemplate.DATE_BEGIN + field;
			Object beginVal = null;
			String endKey = DataTemplate.DATE_END + field;
			Object endVal = null;
			if (params.containsKey(beginKey))
				beginVal = params.get(beginKey);
			if (params.containsKey(endKey))
				endVal = params.get(endKey);
			if (BeanUtils.isNotEmpty(beginVal) || BeanUtils.isNotEmpty(endVal)) {
				map.put(DataTemplate.DATE_BEGIN, beginVal);
				map.put(DataTemplate.DATE_END, endVal);
			}
		}
		return map;
	}

  private Object getQueryValue(SQLClause condition, Map<String, Object> params, String field, Boolean isSelector)
  {
    int valueFrom = condition.getValueFrom();
    Object value = null;
    switch (valueFrom)
    {
    case 1:
      if (isSelector.booleanValue())
        field = field + "ID";
      if (params.containsKey(field)) {
        value = params.get(field);
      }
      break;
    case 2:
      value = condition.getValue();
      break;
    case 3:
      String script = (String)condition.getValue();
      if (StringUtil.isNotEmpty(script)) {
        value = this.groovyScriptEngine.executeObject(script, null);
      }
      break;
    case 4:
    }

    return value;
  }

  private Boolean isSelector(String controlType)
  {//controlType = 人员选择器(单选4,多选8)、角色选择器(单选17,多选5)、组织选择器(单选18,多选6)、岗位选择器(单选19,多选7)
    if (BeanUtils.isEmpty(controlType))
      return Boolean.valueOf(false);
    if ((controlType.equals(String.valueOf(4))) || 
      (controlType.equals(
      String.valueOf(8))) || 
      (controlType.equals(
      String.valueOf(18))) || 
      (controlType.equals(
      String.valueOf(6))) || 
      (controlType.equals(
      String.valueOf(19))) || 
      (controlType.equals(
      String.valueOf(7))) || 
      (controlType.equals(
      String.valueOf(17))) || 
      (controlType.equals(
      String.valueOf(5))))
      return Boolean.valueOf(true);
    return Boolean.valueOf(false);
  }
}

 

 
 