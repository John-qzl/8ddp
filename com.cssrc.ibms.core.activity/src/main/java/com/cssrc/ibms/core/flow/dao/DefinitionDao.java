
package com.cssrc.ibms.core.flow.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.Definition;
/**
 * 对象功能:流程定义扩展 Dao类 
 * 开发人员:zhulongchao 
 */
@Repository
public class DefinitionDao extends BaseDao<Definition>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return Definition.class;
	}

	/**
	 * 更新流程序的主版本状态。
	 * @param defId
	 * @param isMain
	 * @return
	 */
//	public int updateMain(Long defId,Short isMain)
//	{
//		Map<String,Object> params=new HashMap<String,Object>();
//		params.put("defId",defId);
//		params.put("isMain", isMain);
//		
//		return update("updateMain", params);
//	}
	
	public int updateSubVersions(Long parentDefId,String defKey)
	{
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("defKey", defKey);
		params.put("parentDefId", parentDefId);
		
		return update("updateSubVersions",params);
	}
	
	/**
	 * 更新流程启动状态
	 * @param defId
	 * @param disableStatus
	 * @return
	 */
	public int updateDisableStatus(Long defId,Short disableStatus)
	{
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("defId", defId);
		params.put("disableStatus", disableStatus);
		return update("updateDisableStatus",params);
	}
	
	/**
	 * 通过DefId获取其所有的历史版本的流程定义
	 * @param defId 
	 * @return
	 */
	public List<Definition> getByParentDefId(Long defId)
	{
		return getBySqlKey("getByParentDefId", defId);
	}
	
	public List<Definition> getByParentDefIdIsMain(Long parentDefId,Short isMain)
	{
		Map<String,Object> params=new HashMap<String,Object>();
		
		params.put("parentDefId", parentDefId);
		params.put("isMain", isMain);
		
		return getBySqlKey("getByParentDefIdIsMain",params);
	}
	/**
	 * 按Activiti流程定义Id获取Definition实体
	 * @param actDefId
	 * @return
	 */
	public Definition getByActDefId(String actDefId)
	{
		return getUnique("getByActDefId", actDefId);
	}
	/**
	 * 按Activiti的流程定义Key获取Definition实体
	 * @param actDefKey
	 * @return
	 */
	public List<Definition>  getByActDefKey(String actDefKey)
	{
		return getBySqlKey("getByActDefKey", actDefKey);
	}
	
	/**
	 * 按Key取得主版本的流程定义
	 * @param actDefKey
	 * @param isMain
	 * @return
	 */
	public Definition getByActDefKeyIsMain(String actDefKey)
	{
		return getUnique("getByActDefKeyIsMain", actDefKey);
	}
	
	
	/**
	 * 根据分类Id得到流程定义
	 * @param typeId
	 * @return
	 */
	public List<Definition> getByTypeId(Long typeId){
		return getBySqlKey("getByTypeId", typeId);
	}
	
	public List<Definition> getAllForAdmin(QueryFilter queryFilter)
	{
		return getBySqlKey("getAllForAdmin", queryFilter);
	}
	/**
	 * 设置流程标题规则
	 * @param defId
	 * @param isMain
	 * @return
	 */
	public int saveParam(Definition bpmDefinition)
	{
		return update("saveParam", bpmDefinition);
	}
	/**
	 * 根据actDeployId删除流程定义
	 * @param actDeployId
	 */
	public void delByDeployId(String actDeployId){
		
		 delBySqlKey("delByDeployId", actDeployId);
	}
	/**
	 * 根据actDeployId查询该流程定义
	 * @param actDeployId
	 * @return
	 */
	public Definition getByDeployId(String actDeployId){
		return getUnique("getByDeployId", actDeployId);
	}
	
	/**
	 * 判断流程key是否存在。
	 * @param key
	 * @return
	 */
	public boolean isActDefKeyExists(String key){
		Integer rtn=(Integer) getOne("isActDefKeyExists", key);
		return rtn>0;
	}
	
	/**
	 * 判断defkey是否存在。
	 * @param key
	 * @return
	 */
	public boolean isDefKeyExists(String defkey){
		Integer rtn=(Integer) getOne("isDefKeyExists", defkey);
		return rtn>0;
	}
	
	public List<Definition> getByUserId(QueryFilter queryFilter)
	{
		return getBySqlKey("getByUserId", queryFilter);
	}
	/**
	 * 按用户Id及查询参数查找我能访问的所有流程定义
	 * @param queryFilter
	 * @return
	 */
	public List<Definition> getByUserIdFilter(QueryFilter queryFilter){
		return getBySqlKey("getByUserIdFilter", queryFilter);
	}
	
	public List<Definition> getByUserId(Map<String,Object> params){
		return getBySqlKey("getByUserIdFilter", params);
	}
	
	/**
	 * 通过标题模糊查询所有发布的、默认版本的流程
	 * @param subject
	 * @return
	 */
	public List<Definition> getAllPublished(String subject){
		Map filter=new HashMap();
		filter.put("subject", subject);
		return getBySqlKey("getAllPublished", filter);
	}
	
	/**
	 * 通过类型ID查询所有发布的、默认版本的流程
	 * @param typeId
	 * @return
	 */
	public List<Definition> getPublishedByTypeId(String typeId){
		return getBySqlKey("getPublishedByTypeId", typeId);
	}
	
	/**
	 * 根据流程定义key获得当前最新版本的流程定义
	 * @param defkey 
	 * @return
	 */
	public Definition getMainByDefKey(String defKey){
		return getUnique("getMainByDefKey", defKey);
	}

	/**
	 * 根据用户ID，获该用户所创建的流程定义
	 * @param userId 用户ID
	 * @param pb 分页Bean
	 * @return
	 */
	public List<Definition> getByUserId(Long userId ,Map<String,Object> params,PagingBean pb) {
		return getBySqlKey("getByUserIdFilter", params,pb);
	}
	/**
	 * 根据actDeployId删除流程定义数据表
	 * @param actDeployId
	 * @return
	 */
	public void delProcDefByActDeployId(Long actDeployId){
		delBySqlKey("delBytearRayByActDeployId", actDeployId);
		delBySqlKey("delDeployMentByActDeployId", actDeployId);
		delBySqlKey("delProcDefByActDeployId", actDeployId);
	}

	public void updCategory(Long typeId, List<String> defKeyList) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("typeId", typeId);
		map.put("defKeys", defKeyList);
		this.update("updCategory",  map);
		
	}
	/**
	 * 根据流程定义key获得流程定义
	 * @param defkey 
	 * @return
	 */
	public Definition getByDefKey(String defKey){
		return getUnique("getByDefKey", defKey);
	}
	
	public List<Definition> getDefinitionByFormKey(Long formKey){
		return getBySqlKey("getDefinitionByFormKey", formKey);
	}
	
	public List<String> getByCreateBy(Long userId) {
		return getListBySqlKey("getByCreateBy", userId);
	}
	
	/**
	 * 按用户授权查询
	 * @author Yangbo 2016-7-22
	 * @param actRights
	 * @return
	 */
	public List<Definition> getMyDefListForDesktop(String actRights) {
		Map map = new HashMap();
		map.put("isNeedRight", "yes");
		map.put("actRights", actRights);
		return getBySqlKey("getByAuthorizeFilter", map);
	}
	
	
	public List<Definition> getMyDefList(QueryFilter queryFilter)
	{
		return getBySqlKey("getByAuthorizeFilter", queryFilter);
	}

    /** 
    * @Title: updatePendingSetting 
    * @Description: TODO(更新代办已办模板配置) 
    * @param @param defId
    * @param @param displayField
    * @param @param conditionField    设定文件 
    * @return void    返回类型 
    * @throws 
    */
    public void updatePendingSetting(Long defId, String pendingSetting)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("pendingSetting", pendingSetting);
        map.put("defId", defId);
        this.update("updatePendingSetting",  map);
    }

    public void updateKeyPath(String flowKey, String keyPath)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("flowKey", flowKey);
        map.put("keyPath", keyPath);
        this.update("updateKeyPath",  map);
    }

    public void updateKeyPathByPKeyPath(String keyPath)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("keyPath", keyPath);
        this.update("updateKeyPathByPKeyPath",  map);
    }
    
    /** 
    * @Title: getbyKeyPath 
    * @Description: TODO(根据keypath 获取子流程定义) 
    * @param @param defKey
    * @param @return     
    * @return List<? extends IDefinition>    返回类型 
    * @throws 
    */
    public List<Definition> getbyKeyPath(String defKey)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("defKey", defKey);
        return getBySqlKey("getbyKeyPath", map);

    }

    public Definition getByDefKeyIsMain(String defkey, boolean ismain)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("defKey", defkey);
        map.put("ismain", ismain);
        return getUnique("getByDefKeyIsMain", map);
    }
}