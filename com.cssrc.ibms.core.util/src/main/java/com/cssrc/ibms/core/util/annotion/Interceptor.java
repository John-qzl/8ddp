package com.cssrc.ibms.core.util.annotion;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({TYPE,METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Interceptor {

	public Class[] interceptor() default {};
}
