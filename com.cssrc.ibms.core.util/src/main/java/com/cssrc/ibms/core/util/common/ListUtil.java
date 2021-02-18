package com.cssrc.ibms.core.util.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

/**
 * @author wenjie
 * List ---> Map
 */
public class ListUtil {
	
	/**
	 * @param list ： 要转换的list
	 * @param fieldName4Key : 属性名称
	 * @return 以属性名fieldName4Key为key，以整个bean做为value
	 */
	public static <K,V> Map<K,V> list2Map(List<V> list,String fieldName4Key){
		Map<K,V> map = new HashMap<K,V>();
		if(list!=null) {
			try {
				for(int i= 0;i<list.size();i++) {
					V val = list.get(i);
					K k = (K) BeanUtils.getProperty(val, fieldName4Key);
					map.put(k, val);
				}
			}catch(Exception e) {
				throw new IllegalArgumentException("field can't match the key!");
			}
		}
		return map;
	}
	/**
	 * @param list ： 要转换的list
	 * @param fieldName4Key : 属性名称
	 * @return 以属性名fieldName4Key为key，以fieldName4value做为value
	 */
	public static <K,X,V> Map<K,X> list2Map(List<V> list,String fieldName4Key,String fieldName4value){
		Map<K,X> map = new HashMap<K,X>();
		if(list!=null) {
			try {
				for(int i= 0;i<list.size();i++) {
					V val = list.get(i);
					K k = (K) BeanUtils.getProperty(val, fieldName4Key);
					X x = (X) BeanUtils.getProperty(val, fieldName4value);
					map.put(k, x);
				}
			}catch(Exception e) {
				throw new IllegalArgumentException("field can't match the key!");
			}
		}
		return map;
	}
	
}
