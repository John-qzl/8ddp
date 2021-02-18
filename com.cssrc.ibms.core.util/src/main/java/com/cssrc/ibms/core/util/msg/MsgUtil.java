package com.cssrc.ibms.core.util.msg;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 消息Util工具类,消息使用list存放。
 * 
 * @author zhulongchao
 * 
 */
public class MsgUtil {

	private static ThreadLocal<List<String>> msglist=new ThreadLocal<List<String>>();
	
	private static ThreadLocal<String> filePathLocal = new ThreadLocal<String>();	

	/**成功 */
	public static final int SUCCESS = 1;
	/**警告*/
	public static final int WARN = 2;
	/**错误*/
	public static final int ERROR = 3;
	/**其它*/
	public static final int OTHER = 0;

	/**
	 * 添加消息。
	 * @param type 消息的类型
	 * @param msg 添加的消息
	 */
	public static void addMsg(int type, String msg) {
		List<String> list = msglist.get();
		if (BeanUtils.isNotEmpty(msg))
			msg = convertMsg(type, msg);
		if (BeanUtils.isEmpty(list)) {
			list = new ArrayList<String>();
			list.add(msg);
			msglist.set(list);
		} else {
			list.add(msg);
		}
	}
	/**
	 * 转换消息
	 * @param type
	 * @param msg
	 * @return
	 */
	private static String convertMsg(int type, String msg) {
		StringBuffer sb = new StringBuffer();
		Boolean flag = true;
		if (BeanUtils.isNotEmpty(type)) {
			if (type == SUCCESS) {
				sb.append("!!! style=###color:green;###>");
			} else if (type == WARN) {
				sb.append("!!!  style=###color:red;###>");
			} else if(type == ERROR) {
				sb.append("!!!  style=###color:orange;###>");
			}else{
				flag = false;
			}
			sb.append(msg);
			if(flag)
				sb.append("%%%");
		}
		return sb.toString();
	}
	/**
	 * 增加换行
	 */
	public static void addSplit(){
		addMsg(OTHER, "------------------------------------------------------------");
	}
	
	/**
	 * 增加空白
	 * @return
	 */
	public static String addSpace() {
		return "&nbsp;&nbsp;&nbsp;&nbsp;";
	}


	/**
	 * 获取消息数据，并直接清除消息中的数据。
	 * 
	 * @return
	 */
	public static List<String> getMsg() {
		return getMsg(true);
	}

	/**
	 * 获取消息数据。
	 * 
	 * @param clean
	 * @return
	 */
	public static List<String> getMsg(boolean clean) {
		List<String> list = msglist.get();
		if (clean)
			clean();
		return list;
	}

	/**
	 * 返回消息。
	 * 
	 * @return
	 */
	public static String getMessage() {
		return getMessage(true);
	}

	/**
	 * 获取消息。
	 * 
	 * @param clean
	 * @return
	 */
	public static String getMessage(boolean clean) {
		List<String> list = getMsg(clean);
		String str = "";
		if (BeanUtils.isEmpty(list)) 
			return str;
		for (String msg : list) {
			str += msg + "</br>";
		}
		return str;
	}
	
	/**
	 * 
	 * @param filePath
	 */
	public static void addFilePath(String filePath){
		filePathLocal.set(filePath);
	}

	/**
	 * 
	 */
	public static void cleanFilePath(){
		filePathLocal.remove();
	}

	/**
	 * 
	 * @return
	 */
	public static String  getFilePath(){
		if( filePathLocal.get()==null)
			return "";
		return filePathLocal.get();
	}

	/**
	 * 清除消息。
	 */
	public static void clean(){
		msglist.remove();
	}
	/**
	 * 替换模板中的标签。
	 * 
	 * @param template
	 *            模板
	 * @param receiver
	 *            收件人
	 * @param sender
	 *            发件人
	 * @param subject
	 *            主题
	 * @param url
	 *            url。
	 * @param cause
	 *            原因
	 * @param isSms
	 *            是否手机短信
	 * @return
	 */
	public static String replaceTemplateTag(String template, String receiver,
			String sender, String subject, String url, String cause,
			boolean isSms) {		if (StringUtil.isEmpty(template))
			return "";
		template = template.replace("${收件人}", receiver).replace("${发件人}",
				sender).replace("${转办人}", sender).replace("${代理人}", sender);
		if (StringUtil.isNotEmpty(cause)) {
			template = template.replace("${原因}", cause).replace("${逾期级别}",
					cause).replace("${事项意见}", cause);
		}
		if (isSms || StringUtil.isEmpty(url)) {
			template = template.replace("${事项名称}", subject);
		} else {
			template = template.replace("${事项名称}", "<a href='" + url
					+ "' target='_blank'>" + subject + "</a>");
		}
		try
        {
	        template=template.toString().replace("${task.url}", url);
	        template=template.toString().replace("${task.name}", subject);
	        template=template.toString().replace("${process.subject}", subject);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
		// 替换特殊字符
		template = template.replaceAll("&nuot;", "\n");
		return template;
	}
	
	
	
	/**
	 * 替换模板中标题的标签。
	 * 
	 * @param template
	 *            模板
	 * @param receiver
	 *            收件人
	 * @param sender
	 *            发件人
	 * @param subject
	 *            事项名称
	 * @param cause
	 *            原因
	 * @return
	 */
	public static String replaceTitleTag(String template, String receiver,
			String sender, String subject, String cause) {
		if (StringUtil.isEmpty(template))
			return "";
		template = template.replace("${收件人}", receiver).replace("${发件人}",
				sender).replace("${转办人}", sender).replace("${代理人}", sender);
		if (StringUtil.isEmpty(cause))
			cause = "无";
		template = template.replace("${原因}", cause).replace("${逾期级别}", cause);
		template = template.replace("${事项名称}", subject);
		// 替换特殊字符
		template = template.replaceAll("&nuot;", "\n");
		return template;
	}

    public static String replaceVars(String content, Map<String, Object> vars)
    {
        for (Map.Entry<String, Object> entry : vars.entrySet())
        {
            String hold = "${" + (String)entry.getKey() + "}";
            if (BeanUtils.isNotEmpty(entry.getValue()))
            {
                if (entry.getValue() instanceof Date)
                {
                    content = content.replace(hold, DateUtil.getDateString((Date)entry.getValue(), "yyyy-MM-dd"));
                }
                else
                {
                    content = content.replace(hold, entry.getValue().toString());
                    
                }
            }
        }
        return content;
    }

}
