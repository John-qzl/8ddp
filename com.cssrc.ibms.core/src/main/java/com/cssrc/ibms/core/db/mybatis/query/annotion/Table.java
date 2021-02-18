package com.cssrc.ibms.core.db.mybatis.query.annotion;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Table
{
  public abstract String name();

  public abstract String var();

  public abstract String displayTagId();

  public abstract String pk();

  public abstract String comment();

  public abstract boolean isPrimary();

  public abstract String relation();

  public abstract String primaryTable();
}

/* Location:           
 * Qualified Name:     com.ibms.core.annotion.query.Table
 * JD-Core Version:    0.6.0
 */