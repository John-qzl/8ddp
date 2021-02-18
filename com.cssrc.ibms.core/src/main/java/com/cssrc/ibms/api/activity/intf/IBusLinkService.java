package com.cssrc.ibms.api.activity.intf;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.form.model.IFormTable;


public interface IBusLinkService{

	/**
	 * 添加业务数据。
	 * @param pk
	 * @param bpmFormTable
	 */
	public abstract void add(String pk, IFormTable bpmFormTable);

	/**
	 * 添加业务数据。
	 * @param pk
	 * @param defId
	 * @param flowRunId
	 * @param bpmFormTable
	 */
	public abstract void add(String pk, IProcessRun processRun,
			IFormTable bpmFormTable);

	/**
	 * 更新业务关联数据。
	 * @param pk
	 * @param bpmformtable
	 */
	public abstract void updBusLink(String pk, IFormTable bpmformtable);

	/**
	 * 删除关联数据记录。
	 * @param pk
	 * @param bpmformtable
	 */
	public abstract void delBusLink(String pk, IFormTable bpmformtable);

}