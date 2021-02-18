package com.cssrc.ibms.core.mail.dao;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.mail.model.OutMailAttachment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
/**
 * 
 * <p>Title:OutMailAttachmentDao</p>
 * @author Yangbo 
 * @date 2016年9月29日下午3:51:34
 */
@Repository
public class OutMailAttachmentDao extends BaseDao<OutMailAttachment>
{
	public Class<?> getEntityClass()
	{
		return OutMailAttachment.class;
	}
	/**
	 * 根据mailId获取外部邮件的附件信息
	 *@author Yangbo @date 2016年10月10日上午8:34:14
	 */
	public List<OutMailAttachment> getByMailId(long mailId) {
		return getBySqlKey("getByMailId", Long.valueOf(mailId));
	}

	public void updateFilePath(String fileName, Long mailId, String filePath) {
		Map params = new HashMap();
		params.put("fileName", fileName);
		params.put("mailId", mailId);
		params.put("filePath", filePath);
		update("updateFilePath", params);
	}
}

