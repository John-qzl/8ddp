package com.cssrc.ibms.system.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.web.controller.BaseController;
/**
 *<pre>
 * 对象功能:通用webservice调用设置 控制器类 
 * 开发人员:WCan 
 *</pre>
 */
@Controller
@RequestMapping("/oa/system/version/")
@Action(ownermodel=SysAuditModelType.SYSTEM_SETTING)
public class VersionController extends BaseController
{
	
	/**
	 * 添加或更新通用webservice调用设置。
	 * @param request
	 * @param response
	 * @param bpmCommonWsSet 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	
	
	/** 
	 	public Map<SysOrg, List<SysRole>> getOrgRoles(Long userId) {
		List<SysOrg> sysOrgs = this.sysOrgService.getOrgsByUserId(userId);
		Map roles = new HashMap();
		if (BeanUtils.isNotEmpty(sysOrgs)) {
			for (SysOrg sysOrg : sysOrgs) {
				Long orgId = sysOrg.getOrgId();
				List<String> roleList = this.sysRoleService.getOrgRoles(orgId);
				List sysRoles = new ArrayList();
				for (String role : roleList) {
					SysRole sysRole = this.sysRoleService.getByRoleAlias(role);
					sysRoles.add(sysRole);
					roles.put(sysOrg, sysRoles);
				}
			}
		}
		return roles;
	}
	 */
	
	
	@RequestMapping("show")
	public ModelAndView showVersions(HttpServletRequest request,HttpServletResponse response) throws Exception{
		/**ModelAndView mv = getAutoView();*/
		/** 访问versionShow.jsp */
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/versionShow.jsp");
		
		// Map用于存放所有版本信息及版本列表
		Map<String,List> versions = new HashMap();
		List<String> versionList = new ArrayList();		// 用于存放版本列表
		
		int count = 0;
		
		// 解析XML文件
		SAXReader reader=new SAXReader();		// 创建SAXBuilder对象
		InputStream in;

		try{
			String path = FileUtil.getRootPath()+"versions.xml";
			in = new FileInputStream(path);								// 创建输入流,并将xml文件加载到输入流中
			Document doc=reader.read(new File(path));					// 通过saxBuilder的Build方法将输入流加载到saxBuilder中
			
			Element root = doc.getRootElement();						// 通过Document对象获取xml文件根节点
			
			// 获取根节点下的指定节点
			//Element software = root.element("software");
			Element updates = root.element("software").element("updates");
			
			// 获取所有更新版本信息
			for(Iterator<Element> i=updates.elementIterator();i.hasNext();)
			{
				// 获取版本更新
				Element update=(Element)i.next();
				
				// 添加版本号
				String versionName = update.attribute("ver").getValue();
				if(count<3){
					count++;
					// 对最新更新的3个版本进行特殊处理
					mv.addObject("version"+count, versionName);
				}else
				{
					versionList.add( versionName );
				}
				
				// 获取版本更新说明
				Element descriptions = update.element("descriptions");
				List<String> descriptionList = new ArrayList();		// 用于存放更新说明
				for(Iterator<Element> j=descriptions.elementIterator();j.hasNext();)
				{
					Element description=(Element)j.next();
					descriptionList.add( description.getStringValue() );
				}
				
				// 将版本更新内容添加到Map中
				versions.put(versionName, descriptionList);
			}
		}
		finally{
			
		}

		// 将读取到的版本信息传到jsp页面中
		//mv.addObject("versionList", versionList);
		mv.addObject("versions", versions);
		
		return mv;
		
	}	
}
