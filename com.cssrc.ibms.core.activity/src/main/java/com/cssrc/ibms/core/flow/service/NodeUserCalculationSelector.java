package com.cssrc.ibms.core.flow.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.cssrc.ibms.core.flow.intf.INodeUserCalculation;

/**
 * 用户选择类型选择,使用策略模式。
 * 在计算用户的时候，根据类型获取一种计算类。使用相应的策略进行计算。
 * <pre>
 * &lt;bean id="bpmNodeUserCalculationSelector" class="com.ibms.oa.service.bpm.BpmNodeUserCalculationSelector">
 * &lt;property name="bpmNodeUserCalculation">
 *		&lt;paraTypeMap>
 *			&lt;entry>
 *				&lt;key>&lt;value>0</value></key>
 *				&lt;ref bean="startUserCalculation" />
 *			&lt;/entry>
 *		&lt;/property>
 * &lt;/bean>
 * </pre> 
 * 
 *
 */
public class  NodeUserCalculationSelector {
	
	private Map<String,INodeUserCalculation> nodeUserCalculation=new LinkedHashMap<String, INodeUserCalculation>();

	public Map<String, INodeUserCalculation> getNodeUserCalculation() {
		return nodeUserCalculation;
	}
	
	/**
	 * 取得算法列表。
	 * @return
	 */
	public  List<INodeUserCalculation> getNodeUserCalculationList() {
		List<INodeUserCalculation> list=new ArrayList<INodeUserCalculation>();
		Set<Entry<String, INodeUserCalculation>>  entries = nodeUserCalculation.entrySet();
		for(Entry<String, INodeUserCalculation> entry:entries){
			list.add(entry.getValue());
		}
		return list;
		
	}

	/**
	 * 设置用户计算类型。
	 * @param bpmNodeUserCalculation
	 */
	public void setNodeUserCalculation(Map<String, INodeUserCalculation> bpmNodeUserCalculation) {
		this.nodeUserCalculation = bpmNodeUserCalculation;
	}
	
	/**
	 * 获取用户接口
	 * @param i
	 * @return
	 */
	public INodeUserCalculation getByKey(String key){
		return this.nodeUserCalculation.get(key);
	}
	

	
}
