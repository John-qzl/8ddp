package com.cssrc.ibms.core.util.appconf;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

import com.cssrc.ibms.core.constant.system.SysConfConstant;

/**
 * 应用服务全局资源.
 *
 * <p>
 * detailed comment
 * </p>
 * 
 * @author [创建人] zhulongchao <br/>
 *         [创建时间] 2015-3-11 上午09:17:45 <br/>
 *         [修改时间] 2015-3-11 上午09:17:45
 * @see
 */
public class AppUtil implements ApplicationContextAware {
	private static ApplicationContext appContext;
	public static ServletContext servletContext = null;
	private static HashSet<Long> onlineUserIds = new HashSet<Long>();

	public static Object getBean(String beanId) {
		try{
			return appContext.getBean(Class.forName(beanId));
		}catch(Exception e){
			return appContext.getBean(beanId);
		}
	}
	
	/**
     * 根据类从spring上下文获取bean。
     * 
     * @param cls
     * @return
     */
    public static <T> T getBean(String beanId,Class<T> cls) {
        return (T)appContext.getBean(beanId);
    }

	/**
	 * 根据类从spring上下文获取bean。
	 * 
	 * @param cls
	 * @return
	 */
	public static <T> T getBean(Class<T> cls) {
		return appContext.getBean(cls);
	}
	
	/**
	 * 根据类从spring上下文获取项目中的bean。
	 * 没有实现则返回null
	 * @param cls
	 * @return
	 */
	public static <T> T getProjectBean(Class<T> cls) {
		try{
			return appContext.getBean(cls);
		}catch(Exception e){
			return null;
		}
	}
	
	
	public static String getAppAbsolutePath() {
		return servletContext.getRealPath("/");
	}

	// 获取工程相对路径 如：/ibms
	public static String getContextPath() {
		return servletContext.getContextPath();
	}

	/**
	 * 在web环境中根据web页面的路径获取对应页面的绝对路径。
	 * 
	 * @param path
	 * @return
	 */
	public static String getRealPath(String path) {
		return servletContext.getRealPath(path);
	}

	/**
	 * 配置在tomcat中的server.xml，在host内添加<Context path="/uploadFiles"
	 * docBase="D:\\ibms\\attachFile" reloadable="true" debug="0" />
	 * 文件访问路径为/uploadFiles
	 *
	 * @return
	 */
	public static String getCallFilePath() {
		return "/uploadFiles/";
	}

	/**
	 * 配置在tomcat中的server.xml，在host内添加<Context path="/uploadFiles"
	 * docBase="D:\\ibms\\attachFile" reloadable="true" debug="0" />
	 * 文件临时访问路径为/uploadFiles/temp
	 *
	 * @return
	 */
	public static String getCallFileTempPath() {
		return getCallFilePath() + "temp/";
	}

	/**
	 * 配置在tomcat中的server.xml，在host内添加<Context path="/uploadFiles"
	 * docBase="D:\\ibms\\attachFile" reloadable="true" debug="0" />
	 * 文件临时访问路径为/uploadFiles/temp
	 *
	 * @return
	 */
	public static String getCallFileSystemPath() {
		return getCallFilePath() + "system/";
	}

	public static void init(ServletContext in_servletContext) {
		servletContext = in_servletContext;

	}

	public static HashSet<Long> getOnlineUserIds() {
		return onlineUserIds;
	}

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		appContext = context;

	}

	public static ApplicationContext getContext() {
		return appContext;
	}
	
	public static String getAttachPath(){
		/*String attachPath=AppConfigUtil.get("file.upload");
		if (StringUtil.isEmpty(attachPath)) {
			attachPath = getRealPath("/attachFile/");
		}*/
		/*String filePath = CommonTools.getRootPath() + File.separator + "WEB-INF" + File.separator + "classes"
		+ File.separator + "fileServer.properties";
		Properties propertie = new Properties();
		try {
			FileInputStream inputFile = new FileInputStream(filePath);
			propertie.load(inputFile);
			inputFile.close();
		} catch (FileNotFoundException ex) {
			System.out.println("读取属性文件--->失败！- 原因：文件路径错误或者文件不存在");
			ex.printStackTrace();
			return null;
		} catch (IOException ex) {
			System.out.println("装载文件--->失败!");
			ex.printStackTrace();
		}*/
		//Folder路径
		String folderPath = SysConfConstant.UploadFileFolder;
		File folder = new File(folderPath);
		//判断文件夹是否为存在
		if(!folder.exists()){
			//不存在的话，生成文件
			folder.mkdirs();
		}
		return folderPath;
	}

	/**
	 * @author Yangbo 2016-7-20
	 * @param clazz
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static <T> Map<String, T> getImplInstance(Class<T> clazz){
		Map<String, T> map = appContext.getBeansOfType(clazz);
		return map;
	}

	public static void publishEvent(ApplicationEvent applicationEvent) {
		appContext.publishEvent(applicationEvent);
	}
	
	/**
	 * 获取上下文继承和实现的类
	 * @param _c
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static <T> List<T> getAllImplByBeanType(Class<T> _c) throws ClassNotFoundException {

		List<T> list = new ArrayList<T>();
		Map<?,?> map = appContext.getBeansOfType(_c);

		for (Iterator i$ = map.values().iterator(); i$.hasNext(); ) { 
			Object obj = i$.next();
			String name = obj.getClass().getName();
			int pos = name.indexOf("$$");
			if (pos > 0) {
				name = name.substring(0, name.indexOf("$$"));
			}
			Class<?> cls = Class.forName(name);

			list.add((T)cls);
		}

		return list;
	}
}
