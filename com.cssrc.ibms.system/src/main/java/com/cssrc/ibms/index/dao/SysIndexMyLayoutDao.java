 package com.cssrc.ibms.index.dao;

 import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.index.model.SysIndexMyLayout;

import org.springframework.stereotype.Repository;

 @Repository
 public class SysIndexMyLayoutDao extends BaseDao<SysIndexMyLayout>
{
  public Class<?> getEntityClass()
  {
    return SysIndexMyLayout.class;
  }
 
  public SysIndexMyLayout getByUserId(Long userId) {
    return (SysIndexMyLayout)getUnique("getByUserId", userId);
  }
 }

