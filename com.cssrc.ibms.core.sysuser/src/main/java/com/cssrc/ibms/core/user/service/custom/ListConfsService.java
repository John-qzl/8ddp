package com.cssrc.ibms.core.user.service.custom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.custom.intf.IListConfsService;
import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.api.sysuser.intf.IResourcesService;
import com.cssrc.ibms.api.sysuser.model.IResources;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.user.model.UserCustom;
import com.cssrc.ibms.core.user.model.custom.ListConf;
import com.cssrc.ibms.core.user.model.custom.ListConfs;
import com.cssrc.ibms.core.user.service.UserCustomService;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.string.UrlHelper;
@Service
public class ListConfsService implements IListConfsService{
	@Resource
	private UserCustomService userCustomService;	
	@Resource
	private IResourcesService  resourcesService;
	@Resource
    private IDataTemplateService dataTemplateService;
	@Resource
    private FreemarkEngine freemarkEngine;
	public String getDefaultDataListHtml(Map<String,Object> params){
		String html="";
		Map<String,Object> map = new HashMap<String,Object>();
		
		ListConfs confs = this.getListConfs(ISysUser.IMPLEMENT_USER);
		if(confs==null){
			return html;
		}
		List<ListConf> confList = confs.getConfs();		
		
		for(int i=0;i<confList.size();i++){
			ListConf conf = confList.get(i);
			String singleHtml = getSingleHtml(conf,params);
			conf.setHtml(singleHtml);
		}
		map.put("confList", confList);
		try{
			html = this.freemarkEngine.mergeTemplateIntoString("/portal/ins/listingLayout.ftl", map);
		}catch(Exception e){
			html = "";
		}
		return html;
	}
	/**
	 * 单个 业务数据展示
	 * @param params
	 * @return
	 */
	public String getDataListHtml(Long userId,Map<String,Object> params){				
		String html="";
		Map<String,Object> map = new HashMap<String,Object>();
		
		ListConfs confs = this.getListConfs();
		if(confs==null || confs.getConfs().size()==0){
			return getDefaultDataListHtml(params);
		}
		List<ListConf> confList = confs.getConfs();		
		for(int i=0;i<confList.size();i++){
			ListConf conf = confList.get(i);
			String singleHtml = getSingleHtml(conf,params);
			conf.setHtml(singleHtml);
		}
		map.put("confList", confList);
		try{
			html = this.freemarkEngine.mergeTemplateIntoString("/portal/ins/listingLayout.ftl", map);
		}catch(Exception e){
			html = "";
		}
		return html;
	}
	private String getSingleHtml(ListConf conf,Map<String,Object> params){
		params.put("__displayId__", conf.getDisplayId());
		params.put("queryKey", conf.getAdvancedQueryKey());
		
		Long displayId = conf.getDisplayId();
		Long curUserId = UserContextUtil.getCurrentUserId();
		IDataTemplate bpmDataTemplate = dataTemplateService.getById(displayId);
		String html = "";
		try{
			String str = FileUtil.
					readFile(SysConfConstant.FTL_ROOT+"/portal/ins/singleListing.ftl");		
			String templateHtml = dataTemplateService.generateTemplate(bpmDataTemplate,str);
			bpmDataTemplate.setTemplateHtml(templateHtml);
			bpmDataTemplate.setPageSize(conf.getDataNum());
			html = dataTemplateService.getDisplay(bpmDataTemplate, curUserId, params);
		}catch(Exception e){
			html = "没有数据！！！";
		}
		return html;
	}
	public JSONArray getDatatemplate(){
		Long userId = UserContextUtil.getCurrentUserId();
		List<IResources> list = resourcesService.getDatatemplateRes(userId);
		JSONArray array = new JSONArray();
		for(int i=0;i<list.size();i++){
			IResources res = list.get(i);
			Long displayId = UrlHelper.getLongValue(res.getDefaultUrl(), "__displayId__");
			String name = res.getResName();
			JSONObject obj = new JSONObject();
			obj.put("displayId", displayId);
			obj.put("name", name);
			array.add(obj);
		}
		return array;
	}
	/**
	 * 删除
	 * @param aq
	 */
	public void del(){
		UserCustom uc = userCustomService.getById(UserContextUtil.getCurrentUserId());
		String key = ListConfs.KEY;
		
		JSONObject obj = JSONObject.fromObject(uc.getCustomInfo());
		if(obj.containsKey(key)){
			obj.remove(key);
		}
		uc.setCustomInfo(obj.toString());
		userCustomService.update(uc);
	}
	/**
	 * 保存
	 * @param confs
	 */
	public void save(ListConfs confs){
		String k = ListConfs.KEY;
		UserCustom uc = userCustomService.getById(UserContextUtil.getCurrentUserId());
		JSONObject obj = JSONObject.fromObject(uc.getCustomInfo());
		obj.put(k, JSONObject.fromObject(confs).toString());
		uc.setCustomInfo(obj.toString());
		userCustomService.update(uc);
	}
	
	public ListConfs getListConfs(Long userId){
		UserCustom uc = userCustomService.getById(userId);
		JSONObject obj = JSONObject.fromObject(uc.getCustomInfo());
		String k = ListConfs.KEY;
		if(obj.containsKey(k)){
			return ListConfs.toBean(obj.getString(k));
		}else{
			return ListConfs.getDefault();
		}
	}
	/**
	 * @return
	 */
	public ListConfs getListConfs(){
		return getListConfs(UserContextUtil.getCurrentUserId());
	}
}
