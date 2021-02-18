package com.cssrc.ibms.core.flow.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.DefAct;

@Repository
public class DefActDao extends BaseDao<DefAct>
{
  public Class getEntityClass()
  {
    return DefAct.class;
  }

  public List<DefAct> getActByMap(Map<String, Object> params)
  {
    return getBySqlKey("getAll", params);
  }

  public void delByAuthorizeId(Long authorizeId)
  {
    getBySqlKey("delByAuthorizeId", authorizeId);
  }

  public void delByMap(Map<String, Object> params)
  {
    getBySqlKey("delByMap", params);
  }

  public List<DefAct> getActRightByUserMap(Map<String, Object> params)
  {
    return getBySqlKey("getActRightByUserMap", params);
  }
}