package com.cssrc.ibms.core.mail.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.system.intf.ISysFileService;
import com.cssrc.ibms.api.system.model.ISysFile;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.mail.dao.OutMailAttachmentDao;
import com.cssrc.ibms.core.mail.model.OutMailAttachment;
import com.cssrc.ibms.core.util.appconf.AppUtil;

@Service
public class OutMailAttachmentService extends BaseService<OutMailAttachment> {

	@Resource
	private OutMailAttachmentDao dao;

	@Resource
	private ISysFileService sysFileService;

	protected IEntityDao<OutMailAttachment, Long> getEntityDao() {
		return this.dao;
	}
	/**
	 * 根据mailId获取附件数组
	 *@author YangBo @date 2016年10月10日上午8:35:21
	 */
	public List<OutMailAttachment> getByMailId(long mailId) {
		return this.dao.getByMailId(mailId);
	}

	public void updateFilePath(String fileName, Long mailId, String filePath) {
		this.dao.updateFilePath(fileName, mailId, filePath);
	}

	public List<OutMailAttachment> getByOutMailFileIds(String fileIds) {
		List result = new ArrayList();
		if (fileIds == null)
			return result;
		fileIds = fileIds.replaceAll("quot;", "\"");
		JSONObject jsonObj = JSONObject.fromObject(fileIds);
		JSONArray jsonArray = JSONArray.fromObject(jsonObj.get("attachs"));
		for (Iterator localIterator = jsonArray.iterator(); localIterator
				.hasNext();) {
			Object obj = localIterator.next();
			JSONObject json = (JSONObject) obj;
			long id = Long.parseLong(json.getString("id"));
			ISysFile file =this.sysFileService.getById(Long
					.valueOf(id));
			String filePath = AppUtil.getAttachPath() + File.separator + file.getFilepath();
			OutMailAttachment attachment = new OutMailAttachment();
			attachment.setFileId(Long.valueOf(id));
			attachment.setFileName(json.getString("name"));
			attachment.setFilePath(filePath);
			result.add(attachment);
		}
		return result;
	}
}
