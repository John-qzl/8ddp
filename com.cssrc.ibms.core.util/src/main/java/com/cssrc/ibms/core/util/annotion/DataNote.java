package com.cssrc.ibms.core.util.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @company  上海奥蓝托有限公司
 * @description 类的方法描述注解,用于aop拦截以获取正确方法其对应的描述
 * @author yangBo
 * @create 2016-11-19
 * @DataNote(beanName=SysUser.class,pkName="userId")
 * 多个就是@DataNote(beanName={SysUser.class,SysOrg.class,..},pkName="userId,orgId,...")
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)   
@Documented  
@Inherited 
public @interface DataNote {
	
	/**
	 * 详细信息
	 * @return
	 */
	public String detail() default "";
	
	//映射类
	public  Class<?>[]  beanName() ;
	
	//主键名
	public String pkName() default "";
	
	//表名
	public  String tableName() default "";
}