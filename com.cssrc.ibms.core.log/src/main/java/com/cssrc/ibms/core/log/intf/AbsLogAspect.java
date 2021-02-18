package com.cssrc.ibms.core.log.intf;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.intf.IFormHandlerService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.intf.IFormTemplateService;
import com.cssrc.ibms.api.log.intf.ISysAuditLinkService;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.system.model.ISysLog;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.log.LogExecutor;
import com.cssrc.ibms.core.log.LogHolder;
import com.cssrc.ibms.core.log.WorkQueue;
import com.cssrc.ibms.core.log.model.SysLog;
import com.cssrc.ibms.core.log.service.SysLogCustomService;
import com.cssrc.ibms.core.log.service.SysLogService;
import com.cssrc.ibms.core.log.service.SysLogSwitchService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.string.StringUtil;







import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

/**
 *@author vector
 *@date 2017年9月16日 上午11:20:19
 *@Description 日志切点处理抽象类
 */
public abstract class AbsLogAspect implements ILogAspect{
	
	protected Log logger = LogFactory.getLog(AbsLogAspect.class);

	private static WorkQueue wq = new WorkQueue(10);
	@Resource
	protected SysLogService sysLogService;
	@Resource
	private SysLogSwitchService sysLogSwitchService;
	@Resource
	private FreemarkEngine freemarkEngine;
	@Resource
	public ISysAuditLinkService sysAuditLinkService;
	@Resource
	public IFormTableService formTableService;
	@Resource
	public IFormHandlerService formHandlerService;
	@Resource
	public SysLogCustomService sysLogCustomService;
	@Resource
	public ISysUserService sysUserService;
	@Resource
	public IFormTemplateService formTemplateService;
	@Resource
	public IDataTemplateService dataTemplateService;

	private static boolean isCommonServicesInited = false;
	private static Map<String, Object> commonServices = new HashMap<String, Object>();

	// 添加FreeMarker可访问的类静态方法的字段
	static Map<String, TemplateHashModel> STATIC_CLASSES = new HashMap<String, TemplateHashModel>();
	static {
		try {
			BeansWrapper beansWrapper = BeansWrapper.getDefaultInstance();
			TemplateHashModel staticModel = beansWrapper.getStaticModels();
			STATIC_CLASSES.put(Long.class.getSimpleName(),(TemplateHashModel) staticModel.get(java.lang.Long.class.getName()));
			STATIC_CLASSES.put(Integer.class.getSimpleName(),(TemplateHashModel) staticModel.get(java.lang.Integer.class.getName()));
			STATIC_CLASSES.put(java.lang.String.class.getSimpleName(),(TemplateHashModel) staticModel.get(java.lang.String.class.getName()));
			STATIC_CLASSES.put(Short.class.getSimpleName(),(TemplateHashModel) staticModel.get(java.lang.Short.class.getName()));
			STATIC_CLASSES.put(Boolean.class.getSimpleName(),(TemplateHashModel) staticModel.get(java.lang.Boolean.class.getName()));
			STATIC_CLASSES.put(CommonTools.class.getSimpleName(),(TemplateHashModel) staticModel.get(CommonTools.class.getName()));
			STATIC_CLASSES.put(StringUtil.class.getSimpleName(),(TemplateHashModel) staticModel.get(StringUtil.class.getName()));
		} catch (TemplateModelException e) {
			e.printStackTrace();
		}
	}
//	/**
//	 * 切点方法
//	 */
//	protected Method method;
//	/**
//	 * 方法上的注解
//	 */
//	protected Action annotation;
//	/**
//	 * 方法所属模块
//	 */
//	protected String ownermodel;
//	/**
//	 * 方法上的bean和主键名等信息
//	 */
//	protected DataNote methodDataNote;
//	/**
//	 * request
//	 */
//	protected HttpServletRequest request;
	/**
	 * 执行日志写入
	 *@author YangBo @date 2016年11月25日下午4:16:49
	 *@param point
	 *@param async 是否异步处理
	 *@param oldData
	 *@param newData
	 *修改时间 2017年09月16日下午2:34:14
	 *比较新旧数据的不同，写入日志。
	 *
	 *1.初始化SysLog基础属性
	 *2.获取Syslog新旧数据比对详情
	 *3.根据需要进行日志的不同写入
	 *@author scc 
	 */
	private void doLog(ProceedingJoinPoint point, boolean async,List<Map<String,Object>> oldData,
			List<Map<String,Object>> newData,Action annotation,HttpServletRequest request,
			String ownermodel,Method method) {
		try {
			//初始化日志信息
			SysLog log=initSysLog(annotation,request,ownermodel,method);
			//初始化明细detail
			String detail = "";
			//临时明细
			String tempDetail="";
			//添加明细信息记录:数据的更新和修改
			if(oldData!=null&&newData!=null&&oldData.size()!=0&&newData.size()!=0){
				tempDetail=checkData(oldData,newData, method.getAnnotation(DataNote.class), request);
			}else{
				//暂时未用到，此处利用LogThreadLocalHolder临时记录，可做调试用 ??????
				detail = LogThreadLocalHolder.getDetail();
			}

			//判断detail是否记录变化，无则不做变化说明，赋值null  ???????
			if (CommonTools.isNullString(tempDetail)) {
				detail = annotation.detail();
			}else{
				detail = annotation.detail()+" "+tempDetail;
//				detail=detail.substring(0, detail.length()-1);//??????
			}
			//执行日志写入
			writeLog(async,detail,log,request);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据参数进行日志结果写入
	 * @param async 是否异步
	 * @param detail 数据变化
	 * @param log 基础日志信息
	 */
	private void writeLog(boolean async,String detail,SysLog log,HttpServletRequest request) {
        Map<String, Object> param=new HashMap<String, Object>();
        param.put(LogThreadLocalHolder.ACT_RUN_LOG_ID, LogThreadLocalHolder.getParamerter(LogThreadLocalHolder.ACT_RUN_LOG_ID));
		if (async) {
			LogHolder logHolder = new LogHolder();
			if (!CommonTools.isNullString(detail)) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 添加Request查询参数
				if (request != null) {
					map.putAll(RequestUtil.getQueryMap(request));
				}
				// 添加线程相关变量
				map.putAll(LogThreadLocalHolder.getParamerters());
				// 添加通用的服务类
				map.put("sysAuditLinkService", sysAuditLinkService);
				initCommonServices();
				map.putAll(commonServices);
				// 添加通用静态类
				map.putAll(STATIC_CLASSES);
				logHolder.setParseDataModel(map);
				logHolder.setNeedParse(true);
			}
			
			//操作失败时,记录操作失败错误信息
			//没有设置默认为操作成功
			if(LogThreadLocalHolder.getResult() == null||LogThreadLocalHolder.getResult() == 1){
				log.setDetail(detail);
			}else{
				log.setDetail(detail+"失败 <span style=\"color:red;\">"+LogThreadLocalHolder.getParamerters().get("resultMsg")+"</span>");
			}
			logHolder.setSyslog(log);
			logHolder.setCallBackData(param);
			doLogAsync(logHolder);
			
		} else {
			if (!CommonTools.isNullString(detail)) {
				try {
					detail = parseDetail(detail, request);
				} catch (Exception ex) {
					logger.error(ex.getMessage());
					ex.printStackTrace();
					detail = null;
				}
			}
			if(StringUtil.isNotEmpty(detail)){
				log.setDetail(detail);
				sysLogService.add(log);
			}
			
			//回调不支持异步
	        this.callBack(log,param);
		}
		
	}
	/**
	 * 基础日志信息
	 * @return
	 */
	private SysLog initSysLog(Action annotation,HttpServletRequest request,String ownermodel,Method method) {
		SysLog log = new SysLog();
		// 方法体的描述
		String methodDescp = annotation.description();
		// 保存前端传来的数据
		Map<String, Object> detailMap = RequestUtil.getQueryMap(request);
		JSONObject jsonObj = JSONObject.fromObject(detailMap);
		log.setJsonData(jsonObj.toString());
		log.setFromIp(RequestUtil.getIpAddr(request));
		//id
		log.setAuditId(Long.valueOf(UniqueIdUtil.genId()));
		//人员信息
		ISysUser user = (ISysUser) UserContextUtil.getCurrentUser();
		Map<String, Object> parameters = LogThreadLocalHolder.getParamerters();
		if (user == null) {// 登录失败时处理 || 此处比较特别，仅在登录时会遇到
			String username = parameters.get("username").toString();
			ISysUser loginUser = sysUserService.getByUsername(username);
			log.setExecutor(username);
			if (loginUser != null) {
				log.setExecutorId(loginUser.getUserId());
				log.setExecutorName(loginUser.getFullname());
			}
		} else {
			log.setExecutorId(user.getUserId());
			log.setExecutor(user.getUsername());
			log.setExecutorName(user.getFullname());
			log.setOrgid(UserContextUtil.getCurrentOrgId());
		}
		// 没有设置默认为操作成功
		if (LogThreadLocalHolder.getResult() == null) {
			log.setResult((short) 1);
		} else {
			log.setResult(LogThreadLocalHolder.getResult());
		}
		log.setOpName(methodDescp);
		log.setOwnermodel(ownermodel);
		Object isAdd = parameters.get("isAdd");
		if (BeanUtils.isNotEmpty(isAdd) && isAdd.toString().equals("true")
				&& annotation.exectype().equals(SysAuditExecType.UPDATE_TYPE)) {
			log.setExectype(SysAuditExecType.ADD_TYPE.toString());
		} else {
			log.setExectype(annotation.exectype().toString());
		}

		log.setOpTime(new Date());
		log.setExeMethod(method.getDeclaringClass().getName() + "."+ method.getName());
		
		return log;
	}
	/**
	 * 根据bean的新旧数据，比对数据的不同
	 * @param oldMap
	 * @param newMap
	 * @param clazz
	 * @return 新旧数据的比对结果
	 * @author scc 
	 * @date 2017年09月15日下午7:11:58
	 */
	protected String matchDataDiff(Map<String, Object> oldMap, Map<String, Object> newMap, Class<?> clazz) {
		StringBuffer detail=new StringBuffer();
		for(String fieldName:oldMap.keySet()){
			Object oldValue=oldMap.get(fieldName);oldMap.get("fieldList");
			Object newValue=newMap.get(fieldName);
			if(CommonTools.isNoEqual(oldValue,newValue)){
				try {
					Field field=clazz.getDeclaredField(fieldName);
					SysFieldDescription sysFieldDescription=field.getAnnotation(SysFieldDescription.class);
					if(sysFieldDescription==null){
						System.err.println(clazz.getSimpleName()+"."+fieldName+"未加属性描述信息!");
						logger.error(clazz.getSimpleName()+"."+fieldName+"未加属性描述信息!");
						continue;
					}
					//字段描述信息。 例如 字段：userName 描述信息：用户名 
					String fieldDescriptionName=sysFieldDescription.detail();
					if(BeanUtils.isEmpty(fieldDescriptionName)){
						System.err.println(clazz.getSimpleName()+"."+fieldName+"未加属性描述信息!");
						logger.error(clazz.getSimpleName()+"."+fieldName+"未加属性描述信息!");
						continue;
					}
					//数据转换、说明。例如 字段：sex  转换：1男 2女
					if(StringUtil.isNotEmpty(sysFieldDescription.maps())){
						JSONObject fieldMaps= JSONObject.fromObject(sysFieldDescription.maps());
						if(oldValue!=null){
							oldValue=fieldMaps.get(oldValue.toString());
						}
						if(newValue!=null){
							newValue=fieldMaps.get(newValue.toString());
						}
						
					}
					detail.append(getChangeDetail(oldValue,newValue,fieldDescriptionName));
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		return detail.toString();
	}
	/**
	 * 获取字段值，如果加密，返回加密后的结果
	 * @param formTable
	 * @param entry
	 * @return
	 */
/*    protected Object getFiledValue(Object filedValue,IFormField field){
        try{
            FiledEncryptFactory filedEncryptFactory=AppUtil.getBean(FiledEncryptFactory.class);
            if(StringUtil.isEmpty(field.getEncrypt())){
            	return filedValue;
            }
            // 根据form table 来判断是否需要加密 处理
            IFiledEncrypt filedEncrypt=filedEncryptFactory.creatEncrypt(field.getEncrypt());
            if(filedEncrypt!=null){
                return filedEncrypt.encrypt(filedValue);
            }else{
                return filedValue;
            }
        }catch(Exception e){
            return filedValue;
        }
    }*/
	/**
	 * 获取字段修改信息
	 * @param oldValue
	 * @param newValue
	 * @param fieldDescriptionName
	 * @return
	 */
	protected String getChangeDetail(Object oldValue, Object newValue,String fieldDescriptionName) {
		//新值为空时
		if(BeanUtils.isEmpty(newValue)){
			return "将字段【"+fieldDescriptionName+"】的值由"+oldValue.toString()+"<span style=\"color:green;\">改为了</span>NULL；";
		}
		//旧值为空时
		else if(BeanUtils.isEmpty(oldValue)){
			return "将字段【"+fieldDescriptionName+"】的值由NULL<span style=\"color:green;\">改为了</span>"+newValue.toString()+"；";
		}else{
			return "将字段【"+fieldDescriptionName+"】的值由"+oldValue.toString()+"<span style=\"color:green;\">改为了</span>"+newValue.toString()+"；";
		}
	}

	private void doLogAsync(LogHolder holder) {
		LogExecutor logExecutor = new LogExecutor(this);
		logExecutor.setLogHolders(holder);
		wq.execute(logExecutor);
	}

	private String parseDetail(String detail, HttpServletRequest request)throws TemplateException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();

		// 添加Request查询参数
		if (request != null) {
			map.putAll(RequestUtil.getQueryMap(request));
		}
		map.put("request", request);
		// 添加线程相关变量
		map.putAll(LogThreadLocalHolder.getParamerters());
		// 添加通用的服务类
		map.put("sysAuditLinkService", sysAuditLinkService);
		initCommonServices();
		map.putAll(commonServices);
		// 添加通用静态类
		map.putAll(STATIC_CLASSES);

		return freemarkEngine.parseByStringTemplate(map, detail);
	}

	private void initCommonServices() {
		if (isCommonServicesInited) {
			return;
		}
		String[] beanNames = AppUtil.getContext().getBeanDefinitionNames();
		for (String beanName : beanNames) {
			if(beanName.equals("baseWebService")){
	    		continue;
	    	}
			Object bean = AppUtil.getBean(beanName);
			if(bean==null){
				continue;
			}
			if (BeanUtils.isInherit(bean.getClass(),BaseService.class)) {
				commonServices.put(beanName, bean);
			}
		}
		isCommonServicesInited = true;
	}
	
	 
	 /**
	  * 是否 存在DataNote
	  * DataNote中是否存在beanName
	  * DataNote中是否存在pkName
	  */
	 protected boolean isCorrectDataNote(DataNote methodDataNote){
		 if(methodDataNote==null||methodDataNote.beanName()==null||StringUtil.isEmpty(methodDataNote.pkName())){
			 return false;
		 }
		 return true;
	 }
	 
	 
	 /**
	  * 根据注解属性[Controller前/后执行],进行数据获取和日志写入
	  * 修改时间：20170914 
	  */
	public Object getReturnResult(Method method, Action annotation,String ownermodel, ProceedingJoinPoint point) throws Throwable {
		
		DataNote methodDataNote = method.getAnnotation(DataNote.class);
		HttpServletRequest request = RequestUtil.getHttpServletRequest();
		Object returnVal = null;
		// 拦截之前的表数据
		List<Map<String, Object>> oldData =null;
		// 拦截之后的表数据
		List<Map<String, Object>> newData =null;
		// 判断拦截类型
		if (ActionExecOrder.BEFORE.equals(annotation.execOrder())) {
			try {
				doLog(point,false, oldData, newData, annotation, request, ownermodel, method);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			returnVal = point.proceed();
		} else if (ActionExecOrder.AFTER.equals(annotation.execOrder())) {
			// 记录旧数据
			try {
				oldData = getTableData(false,methodDataNote,request);
			} catch (Exception e) {
				e.printStackTrace();
			}

			returnVal = point.proceed();
			// 记录新数据

			try {
				newData = getTableData(true,methodDataNote,request);
				doLog(point,true, oldData, newData, annotation, request, ownermodel, method);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			//此处为切点方法体抛出异常时的处理  未使用
			//若拦截，日志部分不处理，让其正常执行
			returnVal = point.proceed();
			//throw new Exception(this.getClass()+"类别为抛出异常时执行");
		}
		return returnVal;
	}
	
    @Override
    public void callBack(ISysLog log,Map<String, Object> data)
    {
        //清空历史数据
        LogThreadLocalHolder.clearResult();
        LogThreadLocalHolder.clearParameters();
    }
}
