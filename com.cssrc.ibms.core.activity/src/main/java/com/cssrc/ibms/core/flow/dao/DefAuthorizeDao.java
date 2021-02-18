package com.cssrc.ibms.core.flow.dao;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.flow.model.DefAuthorize;

@Repository
public class DefAuthorizeDao extends BaseDao<DefAuthorize>
{
  public Class getEntityClass()
  {
    return DefAuthorize.class;
  }
}