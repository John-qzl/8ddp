package com.cssrc.ibms.api.rec.intf;

import java.util.List;

import com.cssrc.ibms.api.rec.model.IRecType;

/**
 * Description:
 * <p>IRecTypeService.java</p>
 * @author dengwenjie 
 * @date 2017年3月21日
 */
public  interface IRecTypeService {
	
	/** 通过父id获得父功能点 **/
	public abstract IRecType getParentTypeById(long parentId);
	
	/** 获取所有的表单类别 **/
	public abstract List<? extends IRecType> getAll();
}
