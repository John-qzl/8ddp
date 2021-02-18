package com.cssrc.ibms.core.util.annotion;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @company  上海奥蓝托有限公司
 * @description 对于系统表的字段进行中文描述
 * @author liubo
 * @create 2017-8-23
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)   
@Documented  
@Inherited 
public @interface SysFieldDescription {
	
	/**
	 * 详细描述
	 * @return
	 */
	public String detail() default "";
	
	/**
	 * 键值对转换（将数值转换为用户可看懂的标识）
	 * @return
	 */
	public String maps() default "";
}