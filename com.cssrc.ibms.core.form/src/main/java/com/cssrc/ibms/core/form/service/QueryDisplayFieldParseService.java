package com.cssrc.ibms.core.form.service;
 
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.core.form.model.QueryField;
import com.cssrc.ibms.core.util.string.StringUtil;


@Service
public class QueryDisplayFieldParseService {

  @Resource
  private QueryFieldService sysQueryFieldService;

  @Resource
  private QueryRightParseService sysQueryRightParseService;

  public boolean hasDisplayField(String displayField)
  {
    if (StringUtil.isEmpty(displayField))
      return false;
    JSONArray jsonAry = JSONArray.fromObject(displayField);
    return jsonAry.size() > 0;
  }

  public String getDefaultDisplayField(Long sqlId)
  {
    List<QueryField> sysQueryFields = this.sysQueryFieldService
      .getDisplayFieldListBySqlId(sqlId);
    if (sysQueryFields == null)
      return null;
    JSONArray jsonArray = new JSONArray();
    for (QueryField field : sysQueryFields) {
      JSONObject jsonObject = new JSONObject();
      jsonObject.accumulate("id", field.getId());
      jsonObject.accumulate("name", field.getName());
      jsonObject.accumulate("desc", field.getFieldDesc());
      jsonObject.accumulate("right", getDefaultDisplayFieldRight());
      jsonArray.add(jsonObject);
    }
    return jsonArray.toString();
  }

  public String getDefaultDisplayFieldRight()
  {
    return this.sysQueryRightParseService
      .getDefaultRight(Integer.valueOf(0));
  }

  public Object getDisplayFieldPermission(int rightTypeShow, String displayField, Map<String, Object> rightMap)
  {
    return this.sysQueryRightParseService.getPermission(rightTypeShow, 
      displayField, rightMap);
  }
}