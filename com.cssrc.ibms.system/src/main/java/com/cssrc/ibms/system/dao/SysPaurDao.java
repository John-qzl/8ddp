package com.cssrc.ibms.system.dao;
 
 import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.SysPaur;
 /**
  * 
  * @author yangbo
  *2016-7-19
  */
 @Repository
 public class SysPaurDao extends BaseDao<SysPaur>
 {
   public Class<?> getEntityClass()
   {
     return SysPaur.class;
   }
   /**
    * 可给相应用户添加对应的别名，如皮肤属性
    * @param userId
    * @param aliasName
    * @return
    */
   public SysPaur getByUserAndAlias(Long userId, String aliasName)
  {
     Map map = new HashMap();
     map.put("userId", userId);
     map.put("aliasName", aliasName);
     SysPaur obj = (SysPaur)getOne("getByUserAndAlias", map);
     return obj;
   }
   
   /**
    * 更新系统主题
    *@author YangBo @date 2016年11月17日下午2:52:46
    *@param paurvalue 参数穿的值
    */
   public void updSysTheme(String paurvalue) {
		Map params = new HashMap();
		params.put("paurvalue", paurvalue);
		update("updSysTheme", params);
	}
}

