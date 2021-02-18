package com.cssrc.ibms.api.system.intf;

import java.util.List;
import java.util.Map;

import com.cssrc.ibms.api.system.model.IDictionary;

public interface IDictionaryService {

	public abstract List<?extends IDictionary> getByTypeId(Long typeId);

	/**
	 * 由节点分类key得出相应数据字典列表
	 * @param nodeKey
	 * @return
	 */
	public abstract List<?extends IDictionary> getByNodeKey(String nodeKey);

	/**
	 * 由分类ID得出数字字典列表
	 * @param typeId
	 * @param needRoot 根节点
	 * @return
	 */
	public abstract List<?extends IDictionary> getByTypeId(long typeId, boolean needRoot);

	public abstract List<?extends IDictionary> getByParentId(long parentId);

	public abstract void delByDicId(Long dicId);

	public abstract boolean isItemKeyExists(long typeId, String itemKey);

	public abstract boolean isItemKeyExistsForUpdate(long dicId, long typeId,
			String itemKey);

	public abstract void updSn(Long[] lAryId);

	public abstract void move(Long targetId, Long dragId, String moveType);

	public abstract List<String> getAllByItemName(String itemName);

	public abstract List<?extends IDictionary> getByItemName(String itemName);

	/**
	 * 获得所有itemName
	 * @return
	 */
	public abstract List<String> getAllItems();

    /** 
    * @Title: getByNodeDictKey ,流程联动专用
    * @Description: TODO(通过数据字典key查找所有数据，只返回key-value) 
    * @param @param string
    * @param @return     
    * @return List<Map<String,String>>    返回类型 
    * @throws 
    */
    public abstract List<Map<String, Object>> getByNodeDictKeyGangedSet(String code);

}