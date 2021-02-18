package com.cssrc.ibms.api.system.intf;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;

import org.dom4j.Element;

import com.cssrc.ibms.api.migration.model.IOutDicGlobalType;
import com.cssrc.ibms.api.system.model.IGlobalType;

public interface IGlobalTypeService{
	/**
	 * 根据分类类型和分类别名获取分类数据
	 * @param catKey
	 * @param nodeKey
	 * @return
	 */
	public IGlobalType getByCateKeyAndNodeKey(String catKey, String nodeKey);
	/**
	 * @param obj
	 * @return
	 */
	public String createFormGlobalType(JSONObject obj);
	/**
	 * @param outGlobalType 
	 * @param log :日志信息载体
	 * @return 
	 */
	public Long saveSchemaGlobalType(IOutDicGlobalType outGlobalType,StringBuffer log);
	/**
	 * @param dicGlobalType：外部数据字典
	 * @param log:日志信息载体
	 * @return
	 */
	public boolean saveDicGlobalType(IOutDicGlobalType dicGlobalType,StringBuffer log);

	public abstract IGlobalType getByDictNodeKey(String nodeKey);

	/**
	 * 根据catKey获取分类的xml数据。
	 * @param catKEY
	 * @return
	 */
	public abstract String getXmlByCatkey(String catKEY);

	/**
	 * 根据catkey取得根节点
	 * @param catKey
	 * @return
	 */
	public abstract IGlobalType getRootByCatKey(String catKey);

	/**
	 * 分类Key树
	 * @param catKey
	 * @param hasRoot
	 * @return
	 */
	public abstract List<?extends IGlobalType> getByCatKey(String catKey, boolean hasRoot);

	/**
	 * 根据分类类型和分类名称获取分类数据
	 * 
	 * @param catKey
	 * @param typeName
	 * @return
	 */
	public abstract List<?extends IGlobalType> getByCatKeyAndTypeName(String catKey,
			String typeName);

	/**
	 * 
	 * @param parentId
	 * @return
	 */
	public abstract List<?extends IGlobalType> getByParentId(Long parentId);

	public abstract void delByTypeId(Long typeId);

	public abstract String exportXml(long typeId);

	public abstract void importXml(InputStream inputStream, long typeId);

	public abstract void addDicData(Element e, String basePath);

	public abstract void updSn(Long typeId, Long sn);

	public abstract void move(Long targetId, Long dragId, String moveType);

	public abstract List<?extends IGlobalType> getByNodePath(String nodePath);


	public abstract Set<?extends IGlobalType> getByFormRightCat(Long userId, String roleIds, String orgIds, boolean hasRoot);

	/**
	 * 用户个人分类管理节点树
	 *@author YangBo @date 2016年10月11日下午4:52:16
	 *@param catKey
	 *@param userId
	 *@param hasRoot
	 *@return
	 */
	public abstract List<?extends IGlobalType> getPersonType(String catKey, Long userId, boolean hasRoot);

	public abstract boolean isNodeKeyExistsForUpdate(Long typeId, String catKey, String nodeKey);

	public abstract boolean isNodeKeyExists(String catKey, String nodeKey);

	public abstract IGlobalType getById(Long typeId);

}