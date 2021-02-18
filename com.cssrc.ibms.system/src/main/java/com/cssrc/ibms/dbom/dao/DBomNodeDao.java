package com.cssrc.ibms.dbom.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.dbom.model.DBomNode;

/**
 * 对象功能:DBom节点管理 DAO类.
 *
 * <p>detailed comment</p>
 * @author [创建人] WeiLei <br/> 
 * 		   [创建时间] 2016-7-14 下午05:07:18 <br/> 
 * 		   [修改人] WeiLei <br/>
 * 		   [修改时间] 2016-7-14 下午05:07:18
 * @see
 */
@Repository
public class DBomNodeDao extends BaseDao<DBomNode>{

	@SuppressWarnings("rawtypes")
    @Override
	public Class getEntityClass() {
		return DBomNode.class;
	}

	/**
	 * 根据DBom节点代号获取DBom节点信息.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-7-14 下午01:05:34 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-7-14 下午01:05:34
	 * @param code
	 * @return
	 * @see
	 */
	public List<DBomNode> getByCode(String code){		
		return this.getBySqlKey("getByCode", code);
	}
	
	/**
	 * 根据DBom节点父代号获取DBom节点信息.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-7-14 下午01:05:34 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-7-14 下午01:05:34
	 * @param pcode
	 * @return
	 * @see
	 */
	public List<DBomNode> getByPCode(String pcode){		
		return this.getBySqlKey("getByPCode", pcode);
	}
	
	/**
	 * 根据DBom动态子节点的名称，判断DBom是否已经存在，用于在编辑子节点时使用.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-8-5 上午09:28:15 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-5 上午09:28:15
	 * @param name
	 * @return
	 * @see
	 */
	public List<DBomNode> getByName(String name){		
		return this.getBySqlKey("getByName", name);
	}

	/**
	 * 根据SQL和参数，查询数据.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-8-22 上午08:42:27 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-22 上午08:42:27
	 * @param querySql
	 * @return
	 * @see
	 */
	public List<Map<String, Object>> getBySql(String querySql, Object []object){
		if(object == null){
			return this.jdbcTemplate.queryForList(querySql);
		}else{
			return this.jdbcTemplate.queryForList(querySql, object);
		}
	}

	/**
	 * 根据pcode and name 查找动态子节点，返回唯一一条数据
	 * @param code 当前节点code ,动态子节点pcode
	 * @param fieldDesc 动态子节点name
	 * @return
	 */
	public DBomNode getByPCodeAndName(String code, String fieldDesc) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("name", fieldDesc);
		param.put("pcode", code);
		return this.getUnique("getByPCodeAndName", param);
	}
	
    /**
     * 根据dbom code 分类查找分类下所有树节点
     * @param dBomCode dbom分类code
     * @return
     */
    public List<DBomNode> getByDbomCode(String dBomCode)
    {
        return this.getBySqlKey("getByDbomCode", dBomCode);
    }
}
