    package com.cssrc.ibms.index.dao;
     
     import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.index.model.SysIndexLayout;

import org.springframework.stereotype.Repository;
     
     @Repository
     public class SysIndexLayoutDao extends BaseDao<SysIndexLayout>
     {
       public Class<?> getEntityClass()
       {
      return SysIndexLayout.class;
       }
     }

