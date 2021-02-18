package com.cssrc.ibms.core.db.mybatis.query.annotion;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Field
{
  public abstract String name();

  public abstract String desc();

  public abstract String dataType();

  public abstract String options();

  public abstract short controlType();

  public abstract String style();
}

/* Location:           
 * Qualified Name:     com.ibms.core.annotion.query.Field
 * JD-Core Version:    0.6.0
 */