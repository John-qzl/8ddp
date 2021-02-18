package com.cssrc.ibms.core.form.model;

import com.cssrc.ibms.api.form.model.IPkValue;

	/**
 * 主键值。
 * @author zhulongchao
 *
 */
public class PkValue implements IPkValue {
	 
	 public PkValue(){}
	 
	 public PkValue(String pkField,Object value){
		 this.name=pkField;
		 this.value=value;
	 }
	 
	
	 
	 /**
	  * 主键
	  */
	 private String name="";
	/**
	 * 主键值
	 */
	private Object value;
	
	/**
	 * 是否添加
	 */
	private boolean isAdd=false;
	
	
	
	public boolean getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}

