package com.cssrc.ibms.dp.sync.bean;

public class ConditionBean{
	
	private String conditionId;			//conditionid
	private String conditionname;		//condition显示名称
	private String valuename;				//condition填写的真是数值
	private String order;						//order
	private String ref_conditionId;						//order
	
	public String getConditionId() {
		return conditionId;
	}
	public void setConditionId(String conditionId) {
		this.conditionId = conditionId;
	}
	public String getConditionname() {
		return conditionname;
	}
	public void setConditionname(String conditionname) {
		this.conditionname = conditionname;
	}
	public String getValuename() {
		return valuename;
	}
	public void setValuename(String valuename) {
		this.valuename = valuename;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getRef_conditionId() {
		return ref_conditionId;
	}
	public void setRef_conditionId(String ref_conditionId) {
		this.ref_conditionId = ref_conditionId;
	}
	
}
