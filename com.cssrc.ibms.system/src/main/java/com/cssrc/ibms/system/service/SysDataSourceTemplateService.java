package com.cssrc.ibms.system.service;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.system.dao.SysDataSourceTemplateDao;
import com.cssrc.ibms.system.model.SysDataSourceTemplate;

/**
 * SysDataSourceTemplateService
 * @author liubo
 * @date 2017年4月14日
 */
@Service
public class SysDataSourceTemplateService extends BaseService<SysDataSourceTemplate>{
	
	@Resource
	private SysDataSourceTemplateDao dao;
	
	protected IEntityDao<SysDataSourceTemplate, Long> getEntityDao() {
		return this.dao;
	}

	/**
	 * 获取数据源模板
	 * @param json
	 * @return
	 */      
	public SysDataSourceTemplate getSysDataSourceTemplate(String json) {
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] { "yyyy-MM-dd" }));
		if (StringUtil.isEmpty(json))
			return null;
		JSONObject obj = JSONObject.fromObject(json);
		SysDataSourceTemplate sysDataSourceTemplate = (SysDataSourceTemplate)JSONObject.toBean(obj, SysDataSourceTemplate.class);
		return sysDataSourceTemplate;
	}
	
	
	/**
	 * 通过模板路径获取模块所有的设置信息
	 * @param classPath
	 * @return
	 */  
	public JSONArray getHasSetterFieldsJsonArray(String classPath) {
		JSONArray jsonArray = new JSONArray();
		try {
			Class _class = Class.forName(classPath);
			for (Method method : _class.getMethods()) {
				if (!method.getName().startsWith("set"))
					continue;
				String fname = method.getName().replace("set", "");
				fname = fname.replaceFirst(fname.substring(0, 1), fname.substring(0, 1).toLowerCase());
				String ftype = method.getParameterTypes()[0].getName();
				JSONObject jsonObject = new JSONObject();
				jsonObject.accumulate("name", fname);
				jsonObject.accumulate("comment", fname);
				jsonObject.accumulate("type", ftype);
				jsonObject.accumulate("baseAttr", "0");
				jsonArray.add(jsonObject);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return jsonArray;
	}

}
