package com.cssrc.ibms.core.web.security;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.cssrc.ibms.api.system.util.SysContextUtil;
import com.cssrc.ibms.core.constant.sysuser.SystemConst;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;


 

/**
 * 实现的功能。
 * <pre>
 * 1.系统初始化时，构建系统的url和角色映射。
 * 2.并根据当前的url取出url具有的角色权限。
 * 3.实现了 BeanPostProcessor接口，保证SysRoleService，SubSystemService，ICache能在初始化时进行注入，并调用其获取系统资源。
 * </pre>
 * @author zhulongchao
 */
public class IbmsSecurityMetadataSource implements FilterInvocationSecurityMetadataSource ,BeanPostProcessor {
    /**具有匿名访问权限的url*/
	private  HashSet<String> anonymousUrls=new HashSet<String>();
	/**定制配置  具有匿名访问权限的url*/
	private static HashSet<String>  customAnonymousUrls = new HashSet<String>();
	/**
	 * 设置具有匿名访问权限的url
	 * @param anonymousUrls
	 */
	public  void setAnonymousUrls(HashSet<String> anonymousUrls) {
		try{
			HashSet<String> strSet = getAnonymousUrls();
			strSet.addAll(anonymousUrls);
		
			this.anonymousUrls = strSet;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 获取  具有匿名访问权限的url
	 * @param anonymousUrls
	 */
	public HashSet<String> getAnonymousUrls() {
		return anonymousUrls;
	}
	/**
     * 根据当前的URL返回该url的角色集合。
     * 1.如果当前的URL在匿名访问的URL集合当中时，在当前的角色中添加匿名访问的角色(SysRole.ROLE_CONFIG_ANONYMOUS)。
     * 2.如果当前系统不存在的情况，给当前用户添加一个公共访问的角色(SysRole.ROLE_CONFIG_PUBLIC)。
     * 3.url 和角色映射，url和参数映射，给当前用户添加一个公共的角色(SysRole.ROLE_CONFIG_PUBLIC)。
     * 
     * @param url
     */
    public Collection<ConfigAttribute> getAttributes(Object object)throws IllegalArgumentException {
    	
		Collection<ConfigAttribute> configAttribute =new HashSet<ConfigAttribute>();
		
		FilterInvocation filterInvocation=((FilterInvocation)object);
    	HttpServletRequest request=filterInvocation.getRequest();
    	
    	String url = request.getRequestURI();
    	url=removeCtx(url,request.getContextPath());
    	//匿名访问的URL
    	if(anonymousUrls.contains(url)){
    		configAttribute.add(SystemConst.ROLE_CONFIG_ANONYMOUS);
    		return configAttribute;
    	}
    	//定制配置  匿名访问的URL
    	try {
			if(getCustomAnonymousUrls().contains(url)){
	    		configAttribute.add(SystemConst.ROLE_CONFIG_ANONYMOUS);
	    		return configAttribute;
	    	}
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	
    	configAttribute.add(SystemConst.ROLE_CONFIG_PUBLIC);
    	/*String funcId = RequestUtil.getString(request, "id");
    	if(!CommonTools.isNullString(funcId)){
    		IRoleUtil roleEngine = (IRoleUtil) AppUtil.getBean("RoleEngine");
    		Function f = roleEngine.getRoleModel(false).getFunctions().get(funcId);
    		if(null!=f){
    			Set roleFunc = f.getRoleFunctionTboms();
        		for(Iterator<RoleFunctionTbom> it = roleFunc.iterator(); it.hasNext();)
        		{
        			RoleFunctionTbom roleFunTbom = it.next();
        			configAttribute.add(new SecurityConfig(roleFunTbom.getRole().getId()));
        		}
    		}
    		
    		
    	}else{
    		if(url.indexOf("/index")>=0){
        		configAttribute.add(SystemConst.ROLE_CONFIG_PUBLIC);
        		return configAttribute;
        	}
    	}*/
    	
    	return configAttribute;
    }

    public boolean supports(Class<?> clazz) {
        return true;
    }
    
    /**
     * 返回系统中所有为url分配了的权限
     */
    public Collection<ConfigAttribute> getAllConfigAttributes() {
    	return null;
    }
    

	
	
	
	/**
	 * 获取当前URL
	 * @param url
	 * @param contextPath
	 * @return
	 */
	private static String removeCtx(String url,String contextPath){
		url=url.trim();
		if(StringUtil.isEmpty(contextPath)) return url;
		if(StringUtil.isEmpty(url)) return "";
		if(url.startsWith(contextPath)){
			url=url.replaceFirst(contextPath, "");
		}
		return url;
	}
	
	/**
	 * 保证资源只被初始化一次。
	 */
	boolean isInit=false;
	
	/**
	 * 保证service的注入。
	 * 获取系统资源。
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
	
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		
		return bean;
	}

	/**定制配置  具有匿名访问权限的url*/
	@SuppressWarnings("static-access")
	public HashSet<String> getCustomAnonymousUrls() throws IOException {
		//判断是否存在静态变量
		if(this.customAnonymousUrls!=null && this.customAnonymousUrls.size()>0){
				return customAnonymousUrls;
		}else{
			HashSet<String> anonymousUrls = new HashSet<String>();
			//定制文件路径
			String path = FileUtil.getClassesPath() + "com/ibms/"+SysContextUtil.getAppName()+"/conf/customConfig.xml";
			File file = new File(path);
			//判断文件是否存在
			if(file.isFile()){
				//获取文件流
				InputStream stream = new BufferedInputStream(new FileInputStream(path));

				String xml = FileUtil.inputStream2String(stream, "utf-8");
				stream.close();
				Document document = Dom4jUtil.loadXml(xml);
				Element root = document.getRootElement();
				Element tabEl = (Element) root.selectSingleNode("custom[@name='anonymousUrls']");
				List list = tabEl.elements();
				//
				for (int i = 0; i < list.size(); i++) {
					Element el = (Element) list.get(i);
					String key = el.attributeValue("key");
					anonymousUrls.add(key);
				}
			}
			if(anonymousUrls!=null && anonymousUrls.size()>0){
				this.customAnonymousUrls = anonymousUrls;
			}
			return anonymousUrls;
		}
	}
}
