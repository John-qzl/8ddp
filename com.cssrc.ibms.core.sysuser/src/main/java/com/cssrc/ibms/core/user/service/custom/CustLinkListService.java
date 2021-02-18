package com.cssrc.ibms.core.user.service.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.custom.intf.ICustLinkListService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.user.model.custom.CustLinkList;
import com.cssrc.ibms.core.user.model.custom.CustLinkLists;
import com.cssrc.ibms.core.user.model.UserCustom;
import com.cssrc.ibms.core.user.service.UserCustomService;

@Service
public class CustLinkListService implements ICustLinkListService{
	@Resource
	private UserCustomService userCustomService;
	@Resource
    private FreemarkEngine freemarkEngine;
	/**
	 * 保存
	 * 
	 */
	public void save(CustLinkLists custLinkLists) {
		String k = CustLinkLists.KEY;
		long id = UserContextUtil.getCurrentUserId();
		UserCustom uc = userCustomService.getById(UserContextUtil
				.getCurrentUserId());		
		JSONObject obj = JSONObject.fromObject(uc.getCustomInfo());
		obj.put(k, JSONObject.fromObject(custLinkLists).toString());
		uc.setCustomInfo(obj.toString());
		userCustomService.update(uc);
	}

	
	public CustLinkLists getCustLinkLists(Long userId) {
		UserCustom uc = userCustomService.getById(userId);
		JSONObject obj = JSONObject.fromObject(uc.getCustomInfo());
		String k = CustLinkLists.KEY;
		if (obj.containsKey(k)) {
			return CustLinkLists.toBean(obj.getString(k));
		} else {
			return CustLinkLists.getDefault();
		}
	}

	/**
	 * 业务数据展示
	 * @param params
	 * @return
	 */
	public String getDataListHtml(){	
		String html="";
		Map<String,Object> map = new HashMap<String,Object>();
		CustLinkLists confs = this.getCustLinkLists();
		if(confs==null || confs.getConfs().size()==0){
			confs = this.getCustLinkLists(ISysUser.IMPLEMENT_USER);
		}
		List<CustLinkList> confList = confs.getConfs();	
		List<CustLinkList> linkList = new ArrayList<CustLinkList>();
		//数据筛选
		for(int i=0;i<confList.size();i++){
			CustLinkList conf = confList.get(i);
			if(conf.getDesc().equals("资料面板")){
				linkList.add(conf);
			}
		}
		map.put("linkList", linkList);
		try{
			html = this.freemarkEngine.mergeTemplateIntoString("/portal/ins/linkListLayout.ftl", map);
		}catch(Exception e){
			html = "";
		}
		return html;
	}
	
	//附件下载 分开测试
	public String getAttachListHtml(){	
		String html="";
		Map<String,Object> map = new HashMap<String,Object>();
		CustLinkLists confs = this.getCustLinkLists();
		if(confs==null || confs.getConfs().size()==0){
			confs = this.getCustLinkLists(ISysUser.IMPLEMENT_USER);
		}
		List<CustLinkList> confList = confs.getConfs();		
		//页面跳转  与 附件下载  数据筛选
		List<CustLinkList> attachLinkList = new ArrayList<CustLinkList>();
		for(int i=0;i<confList.size();i++){
		CustLinkList conf = confList.get(i);
		if (conf.getDesc().equals("附件下载")){
			attachLinkList.add(conf);
		}
	}
		
		map.put("linkList", attachLinkList);
		try{
			html = this.freemarkEngine.mergeTemplateIntoString("/portal/ins/linkListLayout.ftl", map);
		}catch(Exception e){
			html = "";
		}
		return html;
	}
	
	
	/**
	 * @return
	 */
	//获取当前用户的资料信息
	public CustLinkLists getCustLinkLists() {
		UserCustom uc = userCustomService.getById(UserContextUtil
				.getCurrentUserId());
		JSONObject obj = JSONObject.fromObject(uc.getCustomInfo());
		String k = CustLinkLists.KEY;
		if (obj.containsKey(k)) {
			return CustLinkLists.toBean(obj.getString(k));
		} else {
			return CustLinkLists.getDefault();
		}
	}
}
