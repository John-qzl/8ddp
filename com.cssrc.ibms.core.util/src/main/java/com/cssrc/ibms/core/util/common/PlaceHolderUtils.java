package com.cssrc.ibms.core.util.common;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * 
* @ClassName: PlaceHolderUtils 
* @Description: 字符串占位符替换工具类 
* @author zxg 
* @date 2017年3月20日 下午2:34:00 
*  
*/
public class PlaceHolderUtils
{
    
    private static final Logger logger = LoggerFactory.getLogger(PlaceHolderUtils.class);
    
    /**
     * Prefix for system property placeholders: "${"
     */
    public static final String PLACEHOLDER_PREFIX = "${";
    
    /**
     * Suffix for system property placeholders: "}"
     */
    public static final String PLACEHOLDER_SUFFIX = "}";
    
    public static String resolvePlaceholders(String text, Map<String, Object> parameter)
    {
        if (parameter == null || parameter.isEmpty())
        {
            return text;
        }
        StringBuffer buf = new StringBuffer(text);
        int startIndex = buf.indexOf(PLACEHOLDER_PREFIX);
        while (startIndex != -1)
        {
            int endIndex = buf.indexOf(PLACEHOLDER_SUFFIX, startIndex + PLACEHOLDER_PREFIX.length());
            if (endIndex != -1)
            {
                String placeholder = buf.substring(startIndex + PLACEHOLDER_PREFIX.length(), endIndex);
                int nextIndex = endIndex + PLACEHOLDER_SUFFIX.length();
                try
                {
                    String propVal = parameter.get(placeholder).toString();
                    if (propVal != null)
                    {
                        buf.replace(startIndex, endIndex + PLACEHOLDER_SUFFIX.length(), propVal);
                        nextIndex = startIndex + propVal.length();
                    }
                    else
                    {
                        logger.warn("Could not resolve placeholder '" + placeholder + "' in [" + text + "] ");
                    }
                }
                catch (Exception ex)
                {
                    logger.warn("Could not resolve placeholder '" + placeholder + "' in [" + text + "]: " + ex);
                }
                startIndex = buf.indexOf(PLACEHOLDER_PREFIX, nextIndex);
            }
            else
            {
                startIndex = -1;
            }
        }
        return buf.toString();
    }
    
}
