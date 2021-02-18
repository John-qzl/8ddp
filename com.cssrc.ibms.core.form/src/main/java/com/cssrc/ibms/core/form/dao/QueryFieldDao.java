package com.cssrc.ibms.core.form.dao;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.form.model.QueryField;

@Repository
public class QueryFieldDao extends BaseDao<QueryField>
{
  public Class<?> getEntityClass()
  {
    return QueryField.class;
  }

  public List<QueryField> getDisplayFieldListBySqlId(Long sqlId) {
    Map params = new HashMap();
    params.put("sqlId", sqlId);
    return getBySqlKey("getDisplayFieldListBySqlId", params);
  }

  public List<QueryField> getConditionFieldListBySqlId(Long sqlId) {
    Map params = new HashMap();
    params.put("sqlId", sqlId);
    return getBySqlKey("getConditionFieldListBySqlId", params);
  }

  public void delBySqlId(Long sqlId) {
    Map params = new HashMap();
    params.put("sqlId", sqlId);
    delBySqlKey("delBySqlId", params);
  }
}