package com.cssrc.ibms.core.util.common;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cssrc.ibms.core.util.bean.BeanUtils;

public class MapUtil
{
    public static String getString(Map<String, Object> map, String field)
    {
        if (BeanUtils.isEmpty(map))
        {
            return "";
        }
        field = field.toLowerCase();
        Set set = map.keySet();
        Iterator it = set.iterator();
        Hashtable ht = new Hashtable();
        while (it.hasNext())
        {
            String key = (String)it.next();
            ht.put(key.toLowerCase(), key);
        }
        field = (String)ht.get(field);
        Object obj = map.get(field);
        return obj != null ? obj.toString().trim() : "";
    }
    
    public static long getLong(Map<String, Object> map, String field)
    {
        String value = getString(map, field);
        if (value.equals(""))
            return -1L;
        return Long.parseLong(value);
    }
    
    public static int getInt(Map<String, Object> map, String field)
    {
        String value = getString(map, field);
        if (value.equals(""))
            return -1;
        return Integer.parseInt(value);
    }
    
    public static float getFloat(Map<String, Object> map, String field)
    {
        String value = getString(map, field);
        if (value.equals(""))
            return -1.0F;
        return Float.parseFloat(value);
    }
    
    public static double getDouble(Map<String, Object> map, String field)
    {
        String value = getString(map, field);
        if (value.equals(""))
            return -1.0D;
        return Double.parseDouble(value);
    }
    
    public static Object get(Map<String, Object> map, String field)
    {
        field = field.toLowerCase();
        Set set = map.keySet();
        Iterator it = set.iterator();
        Hashtable ht = new Hashtable();
        while (it.hasNext())
        {
            String key = (String)it.next();
            ht.put(key.toLowerCase(), key);
        }
        field = (String)ht.get(field);
        Object obj = map.get(field);
        return obj;
    }
    
    public static boolean containsKey(Map<String, Object> map, String field)
    {
        return get(map, field) != null;
    }
    
    public static void put(Map<String, Object> map, String field, Object val)
    {
        for (String key : map.keySet())
            if (key.equalsIgnoreCase(field))
                map.put(key, val);
    }
    
    public static Object transMap2Bean(Map<String, Object> map)
    {
        Object obj = new Object();
        if (map == null)
            return null;
        try
        {
            BeanUtils.populate(obj, map);
        }
        catch (Exception e)
        {
            System.out.println("transMap2Bean2 Error " + e);
        }
        return obj;
    }
    /**
     * 将bean转成Key Value的形式，Key为属性名，Value为属性值；对于其中的List<E>属性，正常处理，值为Object
     * @param obj
     * @return
     */
    @SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	public static Map<String, Object> transBean2Map(Object obj)
    {
        if (obj == null)
        {
            return null;
        }
        Map map = new LinkedHashMap();
        try
        {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors)
            {
                String key = property.getName();
                
                if (key.equals("class"))
                    continue;
                Method getter = property.getReadMethod();
                Object value = getter.invoke(obj, new Object[0]);
                if(value instanceof Date){//对于日期类型做转换
                	Date date = (Date)value;
                	value =date.toLocaleString();
                }
                if(key.equals("id")){//对于日期类型做转换
                	if(value!=null) {
                		value = Long.valueOf(value.toString());
                	}
                	
                }
                map.put(key, value);
            }
        }
        catch (Exception e)
        {
            System.out.println("transBean2Map Error " + e);
        }
        
        return map;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T get(Map<String, Object> map,final String field, Class<T> clazz)
    {
        if(map==null) {
            return null;
        }
        Object r=map.get(field);
        if(r==null) {
            return null;
        }
        return (T)r;
    }
}
