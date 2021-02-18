package com.cssrc.ibms.core.util.result;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.activiti.engine.impl.util.json.JSONStringer;
/**
 * 消息处理
 * 
 * @author zhulongchao
 * 
 */
public class ResultMessage implements Serializable
{
    /** */
    private static final long serialVersionUID = -7102370526170507252L;
    
    /** 成功 */
    public static final int Success = 1;
    
    /** 失败 */
    public static final int Fail = 0;
    
    // 返回结果(成功或失败)
    private int result = Success;
    
    // 返回消息
    private String message = "";
    
    // 引起原因
    private String cause = "";
    
    // 附加数据
    private Map<String, Object> data = new HashMap<String, Object>();
    
    public ResultMessage()
    {
    }
    
    public ResultMessage(int result)
    {
        this.result = result;
    }
    
    public ResultMessage(int result, String message)
    {
        this.result = result;
        this.message = message;
    }
    
    public ResultMessage(int result, String message, String cause)
    {
        this.result = result;
        this.message = message;
        this.cause = cause;
    }
    
    public int getResult()
    {
        return result;
    }
    
    public void setResult(int result)
    {
        this.result = result;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public String getCause()
    {
        return cause;
    }
    
    public void setCause(String cause)
    {
        this.cause = cause;
    }
    
    public void addData(String key, Object val)
    {
        this.data.put(key, val);
    }
    
    public String toString()
    {
        JSONStringer stringer = new JSONStringer();
        stringer.object();
        stringer.key("success");
        stringer.value(result == 1 ? "true" : "false");
        stringer.key("message");
        stringer.value(message);
        stringer.key("cause");
        stringer.value(cause);
        stringer.key("result");
        stringer.value(this.result);
        for(String key:data.keySet()){
            stringer.key(key);
            stringer.value(data.get(key));
        }
        stringer.endObject();
        return stringer.toString();
    }
   
}
