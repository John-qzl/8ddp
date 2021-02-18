package com.cssrc.ibms.api.system.intf;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import com.cssrc.ibms.api.system.model.ISysTemplate;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;

public interface ISysTemplateService{

	/**
	 * 设置默认模板
	 * @param id
	 */
	public abstract void setDefault(Long id);

	/**
	 * 删除指定类型模板
	 * @param id
	 */
	public abstract void delByUseType(Integer useType);

	/**
	 * 获取信息类型、用途类型的默认模板
	 * @param tempType
	 * @return
	 */
	public abstract ISysTemplate getDefaultByUseType(Integer useType);

	/**
	 * 
	 * 获取对应的消息模板字符串
	 * 
	 * @param useType 信息类型 （查看SysTemplate定义类型）
	 * @return
	 * @throws Exception
	 */
	public abstract Map<String, String> getTempByFun(Integer useType)
			throws Exception;

	/**
	 * 获取默认的消息模板字符串
	 * @return
	 * @throws Exception
	 */
	public abstract Map<String, String> getDefaultTemp() throws Exception;

	public abstract List<?extends ISysTemplate> unmarshal(String xml)
			throws JAXBException;

	public abstract List<?extends ISysTemplate> unmarshal(InputStream is)
			throws JAXBException;

	public abstract List<?extends ISysTemplate> getByIds(Long[] ids);

	public abstract String exportXml(Long[] ids) throws JAXBException;


	public abstract void importXml(String xml) throws JAXBException;

	public abstract void importXml(InputStream is) throws JAXBException;

	public abstract List<? extends ISysTemplate> getAll(QueryFilter queryFilter);

}