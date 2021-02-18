package com.cssrc.ibms.api.report.inf;

import java.util.List;
import java.util.Map;


import com.cssrc.ibms.api.report.model.ISignModel;

public interface ISignModelService {

	/**
	 * 获取用户所以印章模型
	 * @param userId
	 * @return
	 */
	List<?extends ISignModel> getByUserId(Long userId);

	/**
	 * 获取用户默认印章模型
	 * @param user
	 * @return
	 */
	ISignModel getDefaultByUserId(Long user);
	
    /**
     * 获取用户的电子签章单图片查找方法路径,若没有,则返回默认签章(
     * 
     * @param defaultSignModelPath
     * @return
     */
    String getSignModelPath(Long userId,String defaultSignModelPath);
    /**
     * 获取用户的电子签章的id,若没有,则返回默认签章
     * 
     * @param userId
     * @return
     */
    Long getSignModelPathFileID(Long userId);
    
    /**
     * 新增及修改用户的签章
     * 	若signModel表中有该用户的id,则覆盖该条记录
     * 	若没有,则新增
     * 		因为没有需求,所以除了userid和path之外的值不传,存储时使用了平台的默认值,没有实际意义
     * @param userId
     * @param signModelId
     * @return
     */
    void saveAndUpdateSignModel(long userId,String signModelId);

	/**
	 * 保存从pad同步过来的被分配的用户的签章
	 * @param mainFields
	 */
	void syncPadToLocal(Map<String, Object> mainFields);
    

}
