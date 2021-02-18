package com.cssrc.ibms.core.form.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.form.model.QuerySql;
@Repository
public class QuerySqlDao extends BaseDao<QuerySql>
{
  public Class<?> getEntityClass()
  {
    return QuerySql.class;
  }
  /**
	 * 根据别名获取查询对象。
	 * @param alias		查询别名。
	 * @return
	 */
     public QuerySql getByAlias(String alias)
     {
       return (QuerySql)getUnique("getByAlias", alias);
     }
   
     public boolean isAliasExists(String alias, Long id)
     {
       Map params = new HashMap();
       params.put("alias", alias);
     params.put("id", id);
     return ((Integer)getOne("isAliasExists", params)).intValue() > 0;
     }
    
     /**
 	 * 根据别名获取是否存在。
 	 * @param alias
 	 * @return
 	 */
 	public Integer isExistAlias(String alias){
 		return (Integer)this.getOne("isExistAlias", alias);
 	}
     /**
 	 * 根据别名判断是否存在，用于更新判断。
 	 * @param id
 	 * @param alias
 	 * @return
 	 */
 	public Integer isExistAliasForUpd(Long id,String alias){
 		Map map=new HashMap();
 		map.put("id",id);
 		map.put("alias", alias);
 		return (Integer)this.getOne("isExistAliasForUpd", map);
 	}
     public void updCategory(Long categoryId, String[] aryId)
     {
       Map map = new HashMap();
      map.put("categoryId", categoryId);
     map.put("aryId", aryId);
      update("updCategory", map);
     }
}