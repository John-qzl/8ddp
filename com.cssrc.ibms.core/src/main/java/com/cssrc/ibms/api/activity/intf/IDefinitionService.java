package com.cssrc.ibms.api.activity.intf;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.cssrc.ibms.api.activity.model.IDefinition;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;

public interface IDefinitionService{


	/**
	 * 取得某个流程下的所有历史版本的定义
	 * @param defId
	 * @return
	 */
	public abstract List<? extends IDefinition> getAllHistoryVersions(Long defId);

	/**
	 * 根据ACT流程定义id获取流程定义。
	 * @param actDefId
	 * @return
	 */
	public abstract IDefinition getByActDefId(String actDefId);

	/**
	 * 根据分类Id得到流程定义
	 * @param typeId
	 * @return
	 */
	public abstract List<? extends IDefinition> getByTypeId(Long typeId);

	/**
	 * 用于查询管理员下的所有流程
	 * @param queryFilter
	 * @return
	 */
	public abstract List<? extends IDefinition> getAllForAdmin(QueryFilter queryFilter);

	/**
	 * 删除流程定义
	 * @param flowDefId
	 * @param isOnlyVersion 是否仅删除本版本，不包括其他历史版本
	 */
	public abstract void delDefbyDeployId(Long flowDefId, boolean isOnlyVersion);

	// =========== TODO 导入数据库====
	/**
	 * 导入压缩文件
	 * @param fileLoad
	 * @throws Exception
	 */
	public abstract void importZip(MultipartFile fileLoad) throws Exception;

	/**
	 * 导入数据库
	 * 
	 * @param importFilePath
	 * 
	 * @param inputStream
	 * @param bpmDefinition
	 * @throws Exception
	 */
	public abstract void importXml(String xmlStr, String unzipFilePath)
			throws Exception;

	//TODO 导出exportXML
	/**
	 * 导出XML。
	 * 
	 * <pre>
	 * 导出以下 信息:
	 * 	
	 * ■ 流程定义 bpmDefinition
	 * ■ 历史版本 bpmDefinitionHistory
	 * 	
	 * ■ 流程节点设置 bpmNodeSet
	 * ■ 节点下的人员的配置规则 bpmUserCondition
	 * ■ 节点下的人员设置  bpmNodeUser
	 * ■ 节点下的人员上下级设置 bpmNodeUserUplow
	 * 	
	 * ■ 流程定义权限 bpmDefRights
	 * ■ 常用语设置 taskApprovalItems
	 * 	
	 * ■ 流程跳转规则  bpmNodeRule
	 * ■ 流程事件脚本  bpmNodeScript
	 * 	
	 * ■ 流程操作按钮设置 bpmNodeButton
	 * ■ 流程变量  bpmDefVar
	 * 	 
	 * ■ 流程消息  bpmNodeMessage
	 * ■ 流程会签规则  bpmNodeSign
	 * 
	 * ■ 任务节点催办时间设置 taskReminder
	 * ■ 内（外）部子流程 subDefinition
	 * </pre>
	 * 
	 * @param Long
	 *            [] bpmDefIds
	 * @param map
	 * @param filePath
	 * @return
	 */
	public abstract String exportXml(Long[] bpmDefIds,
			Map<String, Boolean> map, String filePath) throws Exception;


	/**
	 * 根据流程key取得流程定义数据。
	 * @param flowKey
	 * @return
	 */
	public abstract IDefinition getMainDefByActDefKey(String actDefKey);

	public abstract List<? extends IDefinition> getByUserId(QueryFilter queryFilter);

	/**
	 * 按用户Id及查询参数查找我能访问的所有流程定义
	 * @param queryFilter
	 * @return
	 */
	public abstract List<? extends IDefinition> getByUserIdFilter(QueryFilter queryFilter);

	/**
	 * 判断流程key是否存在。
	 * @param key
	 * @return
	 */
	public abstract boolean isActDefKeyExists(String key);

	/**
	 * 判断defkey是否存在。
	 * @param key
	 * @return
	 */
	public abstract boolean isDefKeyExists(String defkey);

	/**
	 * 通过标题模糊查询所有发布的、默认版本的流程
	 * @param subject
	 * @return
	 */
	public abstract List<? extends IDefinition> getAllPublished(String subject);

	/**
	 * 通过类型ID查询所有发布的、默认版本的流程
	 * @param typeId
	 * @return
	 */
	public abstract List<? extends IDefinition> getPublishedByTypeId(String typeId);

	/**
	 * 根据流程定义key获得当前最新版本的流程定义
	 * @param defkey 
	 * @return
	 */
	public abstract IDefinition getMainByDefKey(String defkey);

	/**
	 * 更新流程启动状态
	 * @param defId
	 * @param disableStatus
	 * @return
	 */
	public abstract int updateDisableStatus(Long defId, Short disableStatus);

	/**
	 * 根据用户ID，获该用户所创建的流程定义
	 * @param userId 用户ID
	 * @param pb 分页Bean
	 * @return
	 */
	public abstract List<? extends IDefinition> getByUserId(Long userId,
			Map<String, Object> params, PagingBean pb);

	/**
	 * 清除流程相关数据
	 * @param defId
	 * @throws Exception 
	 */
	public abstract void cleanData(Long defId) throws Exception;

	/**
	 * 查找我能访问的所有流程定义
	 * 
	 * @param filter
	 * @param typeId
	 * @return
	 */
	public abstract List<? extends IDefinition> getList(QueryFilter filter, Long typeId);

	public abstract List<? extends IDefinition> getMyList(Long userId);

	/**
	 * 判断流程是否允许转办。
	 * 
	 * @param actDefId
	 * @return
	 */
	public abstract boolean allowDivert(String actDefId);


	public abstract void updCategory(Long typeId, List<String> defKeylist);

	/**
	 * 根据流程定义key获得流程定义
	 * @param defkey 
	 * @return
	 */
	public abstract IDefinition getByDefKey(String defkey);

	public abstract IDefinition getByDefKeyIsMain(String defkey);

	public abstract List<? extends IDefinition> getDefinitionByFormKey(Long formKey);

	/**
	 * @author Yangbo 2016-7-22
	 * 按用户授权获取
	 * @param actRights
	 * @return
	 */
	public abstract List<? extends IDefinition> getMyDefListForDesktop(String actRights);

	/**
	 * @author Yangbo 2016-8-31
	 * @param filter
	 * @param typeId
	 * @return
	 */
	public abstract List<? extends IDefinition> getMyDefList(QueryFilter filter,
			Long typeId);

	public abstract IDefinition getById(Long defId);

    /** 
    * @Title: getbyKeyPath 
    * @Description: TODO(根据keypath 获取子流程定义) 
    * @param @param defKey
    * @param @return     
    * @return List<? extends IDefinition>    返回类型 
    * @throws 
    */
    public abstract List<? extends IDefinition> getbyKeyPath(String defKey);

}