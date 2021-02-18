package com.cssrc.ibms.core.log.service;

import java.lang.reflect.Method;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.IBaseService;
import com.cssrc.ibms.api.sysuser.model.IResources;
import com.cssrc.ibms.core.log.model.SysLogSwitch;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 日志处理-根据不同日志类型进行分类处理
 * @author sc
 * @date 2017年11月6日 上午9:31:44
 */
@Service
public class LogRouting {
	@Resource
	private SysLogSwitchService sysLogSwitchService;
	@Resource
	private SysLogAspectService sysLogAspectService;
	@Resource
	private IBaseService<IResources> resourcesService;
	/**
	 * 业务数据的日志处理
	 */
	@Resource
	private BussinessLogAspectService bussinessLogAspectService;
	/**
	 * 表单模块的日志处理
	 */
	@Resource
	private FormLogAspectService formLogAspectService;
	/**
	 * 流程模块的日志处理
	 */
	@Resource
	private ActivityLogAspectService activityLogAspectService;
	/**
	 * 根据不同的日志类别，分类处理
	 * @throws Throwable 
	 */
	public  Object routing(Method method, Action annotation, ProceedingJoinPoint point) throws Throwable{

		String extendOwnerModel=getExtendOwnerModel(point,annotation);
		String ownermodel;
		if(StringUtil.isNotEmpty(extendOwnerModel)){
			ownermodel=extendOwnerModel;
		}else{
			ownermodel=getMethodOwnerModel(point,annotation);
			if (StringUtil.isEmpty(ownermodel)||ownermodel.equals("未指定")) {
				return point.proceed();
			}
			// 判断此处日志记录是否开启
			if (testLogSwitch(point,ownermodel,annotation) == false) {
				return point.proceed();
			}
		}
		Object returnVal=null;
		System.err.println(ownermodel);
		
		if(ownermodel.startsWith("业务管理")){
		    //用户操作的业务数据
			try {
				HttpServletRequest request = RequestUtil.getHttpServletRequest();
				String tabId=String.valueOf(request.getSession().getAttribute("tabid"));
				if(StringUtil.isNotEmpty(tabId)){
					if(!StringUtil.isNumeric(tabId)){
						//全文检索-更多页  特殊处理
						if(tabId.contains("-g")){
							ownermodel="全文检索";
						}
					}else{
						IResources res=resourcesService.getById(Long.valueOf(tabId));
						if(res!=null){
							ownermodel=res.getResName();
							while(res.getParentId()!=null&&res.getParentId()!=0&&res.getParentId()!=2){
								res=resourcesService.getById(res.getParentId());
								ownermodel=res.getResName()+"-"+ownermodel;
							}
						}
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
				if(StringUtil.isEmpty(ownermodel)){
					ownermodel="业务管理";
				}
			}
			returnVal=bussinessLogAspectService.getReturnResult(method, annotation, ownermodel, point);
		}else if(ownermodel.startsWith("表单管理")){
			return point.proceed();
			//表单管理模块-实施人员操作记录未完成
			//returnVal=formLogAspectService.getReturnResult(method, annotation, ownermodel, point);
		}else if(ownermodel.startsWith("流程管理")){
		    HttpServletRequest request = RequestUtil.getHttpServletRequest();
	        String taskId = request.getParameter("taskId");
	        String defId = request.getParameter("defId");
	        ownermodel+="-"+activityLogAspectService.getFlowSubject(taskId,defId);
			returnVal=activityLogAspectService.getReturnResult(method, annotation, ownermodel, point);
		}else {
			returnVal=sysLogAspectService.getReturnResult(method,annotation,ownermodel,point);
		}
		return returnVal;
	}
	/**
	 * 根据切点获取方法的归属模块
	 * @param point
	 */
	private String getMethodOwnerModel(ProceedingJoinPoint point,Action annotation){
		try {
			// 方法归属模块
			String modelType = annotation.ownermodel();
			//方法体上没有归属模块，则使用类的
			if (modelType.equals(SysAuditModelType.NULL)) {
				// 类Action
				Action classAction = point.getTarget().getClass().getAnnotation(Action.class);
				if (classAction != null) {
					modelType = classAction.ownermodel();
				}
			}
			return  modelType.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	/**
	 * 判断日志记录是否开启[需对应到具体的日志类型是否开启]
	 * @param annotation 
	 * @param ownermodel 
	 * @return
	 */
	protected boolean testLogSwitch(ProceedingJoinPoint point, String ownermodel, Action annotation){
		try {
			// 获取该模块日志开关状态
			SysLogSwitch sysLogSwitch = sysLogSwitchService.getByModel(ownermodel);
			if (sysLogSwitch == null) {
				return false;
			}
			short status = sysLogSwitch.getStatus() == null ? SysLogSwitch.STATUS_CLOSE
					: sysLogSwitch.getStatus();
			if (status != SysLogSwitch.STATUS_OPEN) {
				return false;
			}
			// 日志类型
			SysAuditExecType exectype = annotation.exectype();
			
			String exectypes = sysLogSwitch.getExecTypes();
			if(BeanUtils.isEmpty(exectypes)){
				return false;
			}
			return exectypes.contains(exectype.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	private String getExtendOwnerModel(ProceedingJoinPoint point,Action annotation) {
		try {
			String extendOwnerModel=annotation.extendOwnermodel();
			if(StringUtil.isEmpty(extendOwnerModel)){
				Action classAction=point.getTarget().getClass().getAnnotation(Action.class);
				if(classAction!=null){
					extendOwnerModel=classAction.extendOwnermodel();
				}
			}
			return extendOwnerModel;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
