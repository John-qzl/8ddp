package com.cssrc.ibms.core.flow.dao;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.DefUser;

@Repository
public class DefUserDao extends BaseDao<DefUser>
{
  public Class getEntityClass()
  {
    return DefUser.class;
  }

  public List<DefUser> getUserByMap(Map<String, Object> params)
  {
    return getBySqlKey("getAll", params);
  }

  public void delByAuthorizeId(Long authorizeId)
  {
    getBySqlKey("delByAuthorizeId", authorizeId);
  }
}