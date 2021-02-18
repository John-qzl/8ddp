package com.cssrc.ibms.core.util.json;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate3.Hibernate3Module;
import com.fasterxml.jackson.datatype.hibernate3.Hibernate3Module.Feature;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class JacksonMapper extends ObjectMapper
{
  private static final long serialVersionUID = 1232645849307489985L;

  public JacksonMapper()
  {
  }

  public JacksonMapper(boolean forceLazyLoading)
  {
    Hibernate3Module mod = new Hibernate3Module();
    mod.configure(Hibernate3Module.Feature.FORCE_LAZY_LOADING, 
      forceLazyLoading);
    registerModule(mod);
  }

  public JacksonMapper(String dateFormat)
  {
    setDateFormat(new SimpleDateFormat(dateFormat));
  }

  public JacksonMapper(boolean forceLazyLoading, String dateFormat)
  {
    this(forceLazyLoading);
    setDateFormat(new SimpleDateFormat(dateFormat));
  }

  public String toJson(Object object)
  {
    try
    {
      return writeValueAsString(object);
    } catch (Exception e) {
      e.printStackTrace();
    }throw new RuntimeException("解析对象错误");
  }

  public List<Map<String, Object>> toList(String json)
  {
    try
    {
      return (List)readValue(json, List.class);
    } catch (Exception e) {
      e.printStackTrace();
    }throw new RuntimeException("解析json错误");
  }
  
  

  public <T> T toObject(String json, Class<T> clazz)
  {
    try
    {
      return readValue(json, clazz);
    } catch (Exception e) {
      e.printStackTrace();
    }throw new RuntimeException("解析json错误");
  }

  public String toPageJson(List<?> list, Integer totalCounts)
  {
    StringBuffer sb = new StringBuffer("{\"success\":true,\"totalCounts\":")
      .append(totalCounts).append(",\"result\":");
    sb.append(toJson(list));
    sb.append("}");
    return sb.toString();
  }

  public String toDataJson(Object object)
  {
    StringBuffer sb = new StringBuffer("{\"success\":true,\"data\":");
    sb.append(toJson(object));
    sb.append("}");
    return sb.toString();
  }

  public String toResultJson(Object object)
  {
    StringBuffer sb = new StringBuffer("{success:true,result:");
    sb.append(toJson(object));
    sb.append("}");
    return sb.toString();
  }
}

 
