package com.cssrc.ibms.system.dao;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.system.model.Dictionary;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
/**
 *  数据字典(业务表定义字段)
 * <p>Title:DictionaryDao</p>
 * @author YangBo 
 * @date 2016-8-3上午10:21:54
 */
@Repository
public class DictionaryDao extends BaseDao<Dictionary> {
	public Class<Dictionary> getEntityClass() {
		return Dictionary.class;
	}
	
	/**
	 * 获取分类节点下数据字典内容
	 * @param typeId
	 * @return
	 */
	public List<Dictionary> getByTypeId(long typeId) {
		return getBySqlKey("getByTypeId", Long.valueOf(typeId));
	}
	
	/**
	 * 数据字典分类树
	 * @param nodePath
	 * @return
	 */
	public List<Dictionary> getByNodePath(String nodePath) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("nodePath", nodePath + "%");
		return getBySqlKey("getByNodePath", params);
	}
	/**
	 * 删除分类节点
	 * @param typeId
	 */
	public void delByTypeId(Long typeId) {
		delBySqlKey("delByTypeId", typeId);
	}

	public boolean isItemKeyExists(long typeId, String itemKey) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("typeId", Long.valueOf(typeId));
		params.put("itemKey", itemKey);
		int count = ((Integer) getOne("isItemKeyExists", params)).intValue();
		return count > 0;
	}

	public boolean isItemKeyExistsForUpdate(long dicId, long typeId,
			String itemKey) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("dicId", Long.valueOf(dicId));
		params.put("typeId", Long.valueOf(typeId));
		params.put("itemKey", itemKey);
		int count = ((Integer) getOne("isItemKeyExistsForUpdate", params))
				.intValue();
		return count > 0;
	}

	public void updSn(Long dicId, Integer sn) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("dicId", dicId);
		params.put("sn", sn);
		update("updSn", params);
	}
	
	/**
	 * 通过path更新父子数据字典
	 * @param path
	 */
	public void updateStatusByPath(String path,Long currentUserId,short status) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("path", path);
		params.put("currentUserId", currentUserId);
		params.put("status", status);
		params.put("updateTime", new Date());
		update("updateStatusByPath", params);
	}

	/**
	 * 通过类型id更新数据字典
	 * @param path
	 */
	public void updateStatusByTypeId(Long typeId,Long currentUserId,short status) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("typeId", typeId);
		params.put("currentUserId", currentUserId);
		params.put("status", status);
		params.put("updateTime", new Date());
		update("updateStatusByTypeId", params);
	}
	
	public List<Dictionary> getByParentId(long id) {
		return getBySqlKey("getByParentId", Long.valueOf(id));
	}
	/**
	 * 过得所有项目名
	 * @return
	 */
	public List<String> getAllItems() {
		return getListBySqlKey("getAllItems",null);
	}
	/**
	 * 根据项目名获取项值
	 * @param itemName
	 * @return
	 */
	public List<String> getAllByItemName(String itemName) {
		return getListBySqlKey("getAllByItemName", itemName);
	}
	/**
	 * 所有满足项名的列表
	 * @param itemName
	 * @return
	 */
	public List<Dictionary> getByItemName(String itemName) {
		return getBySqlKey("getByItemName", itemName);
	}

    /** 
    * @Title: getByNodeDictKey 流程联动专用
    * @Description: TODO(流程联动获取数据字典) 
    * @param @param typeId
    * @param @return     
    * @return List<Map<String,Object>>    返回类型 
    * @throws 
    */
    public List<Map<String, Object>> getByNodeDictKeyGangedSet(long typeId)
    {
        /*
         * 因为，业务表数据联动存储的是 itemName，显示的也是itemName，所以，这里key必须是itemName，text也是itemName。
         * 不然表单不会有联动效果
         * 数据字典中的 itemName 代表下拉框的key，itemName 代表下拉框的 text
         * 
         * */
        StringBuffer sql=new StringBuffer("SELECT itemName AS \"value\", itemName AS \"key\"");
        sql.append(" FROM ");
        sql.append(" CWM_SYS_DIC ");
        sql.append(" where typeId="+typeId);
        sql.append(" Order BY itemName");
        return queryForList(sql.toString());
    }
	
}
