package com.cssrc.ibms.core.util.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;

/**
 * @company 上海奥蓝托有限公司
 * @description 类的方法描述注解,用于aop拦截以获取正确方法其对应的描述
 * @author zhulongchao
 * @create 2014-10-28
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Action {

	/**
	 * 操作类型
	 * 
	 * @return
	 */
	// public String operateType() default "";
	/**
	 * 方法描述
	 * 
	 * @return
	 */
	public String description() default "no description";

	/**
	 * 归属模块
	 * 
	 * @return
	 */
	public String ownermodel() default SysAuditModelType.SYSTEM_SETTING;

	/**
	 * 日志类型
	 * 
	 * @return
	 */
	public SysAuditExecType exectype() default SysAuditExecType.SELECT_TYPE;

	/**
	 * 详细信息
	 * 
	 * @return
	 */
	public String detail() default "";

	/**
	 * 执行顺序
	 */
	public ActionExecOrder execOrder() default ActionExecOrder.AFTER;
	/**
	 * 扩展的所属模块
	 */
	public String extendOwnermodel()default "";
}