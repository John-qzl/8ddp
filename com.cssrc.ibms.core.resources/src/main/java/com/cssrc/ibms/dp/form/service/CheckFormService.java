package com.cssrc.ibms.dp.form.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.core.resources.io.util.IOConstans;
import com.cssrc.ibms.dp.form.dao.CheckFormDao;
import com.cssrc.ibms.dp.form.dao.FormFolderDao;

@Service
public class CheckFormService {
	@Resource 
	CheckFormDao checkFormDao;
	@Resource
	FormFolderDao formFolderDao;
	public Map temporaryStorage(String id, String html) {
		Map<String,Object> msg=new HashMap<String,Object>();
		try {
			Map<String, Object> param=new HashMap<String,Object>();
			param.put("ID", id);
			param.put("CONTENTS", html);
			param.put("status", IOConstans.TABLE_TEMP_UNCOMPLETE);
			formFolderDao.updateTableTempContent(param);
			msg.put("success", true);
			msg.put("info", "更新成功");
		} catch (Exception e) {
			msg.put("success", false);
			msg.put("info", "更新失败："+e.getMessage());
			e.printStackTrace();
		}
		return msg;
	}
}
