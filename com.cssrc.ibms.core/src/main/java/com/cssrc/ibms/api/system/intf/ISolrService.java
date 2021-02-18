package com.cssrc.ibms.api.system.intf;

import org.com.cssrc.ibms.solrclient.intf.ISolrFile;

import com.cssrc.ibms.api.form.model.IFormData;

/**
 *@author vector
 *@date 2017年9月26日 下午3:14:00
 *@Description Solr搜索相关的处理接口
 */
public interface ISolrService {
	/**
	 * 创建文件索引
	 */
	public void createIndex(ISolrFile solrFile);
	/**
	 * 删除文件索引
	 * @param ids
	 */
	public void deleteFileIndex(Long[] ids);
	/**
	 * 创建数据库数据索引
	 */
	public void createSqlDataIndex(IFormData formData);
	/**
	 * 删除数据库索引
	 */
	public void deleteSqlDataIndex(String pk);
}

