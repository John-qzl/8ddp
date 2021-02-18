package com.cssrc.ibms.api.jms.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
 
/**
 * 消息模型类
 * 必须序列化
 * @author lenovo
 *
 */
public class MessageModel implements Serializable,IMessageModel{
	//接收人 sysuser无法序列化 
	private Long[] receiveUser;
	//发送人 sysuser无法序列化 
	private Long  sendUser;
	//消息类型，BpmConst.MESSAGE_TYPE_SMS， BpmConst.MESSAGE_TYPE_MAIL,BpmConst.MESSAGE_TYPE_INNER
	private String informType;
	//模版
	private Map<String,String>  templateMap;
	
	//发送内容,无模版就取发送内容
	private String content ;
	//主题
	private String subject;
	//事件原因
	private String opinion;
	//发送时间
	private Date sendDate;
	

	//任务ID或runid
	private Long extId;
	
	private boolean isTask;
	//扩展用
	private Map<String,Object> vars;
	
	// 邮件群发，邮件接收者
	private String[] to;	
	//邮件抄送者
	private List<String> cc;
	//邮件秘密抄送者
	private List<String> bcc;
	//点击title跳转的url
	private String url;
	
	/**
	 * 构造函数
	 * 消息模型必须指定消息类型
	 * @param informType
	 */
	public MessageModel(String informType){
		this.informType=informType;
		
	}
	
	public boolean getIsTask() {
		return isTask;
	}

	public void setIsTask(boolean isTask) {
		this.isTask = isTask;
	}     
	/**
	 * 获取发送时间
	 */
	public Date getSendDate() {
		if(sendDate==null)
			return new Date();
		return sendDate;
	}

	/**
	 * 设置发送时间
	 */
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	/**
	 * 获取消息内容模版
	 */
	public Map<String, String> getTemplateMap() {
		return templateMap;
	}

	/**
	 * 设置消息内容模版
	 */
	public void setTemplateMap(Map<String, String> templateMap) {
		this.templateMap = templateMap;
	}

	/**
	 * 获取接收人
	 */
	public Long[] getReceiveUser() {
		return receiveUser;
	}

	/**
	 * 设置接收人
	 */
	public void setReceiveUser(Long... receiveUser) {
		this.receiveUser = receiveUser;
	}

	/**
	 * 获取发送人
	 */
	public Long getSendUser() {
		return sendUser;
	}

	/**
	 * 设置发送人
	 */
	public void setSendUser(Long sendUser) {
		this.sendUser = sendUser;
	}

	/**
	 * 获取信息类型
	 */
	public String getInformType() {
		return informType;
	}

	/**
	 * 设置信息类型，
	 * informType一定要指定
	 * BpmConst.MESSAGE_TYPE_MAIL
	 * BpmConst.MESSAGE_TYPE_SMS
	 * BpmConst.MESSAGE_TYPE_INNER
	 */
	public void setInformType(String informType) {
		this.informType = informType;
	}


	/**
	 * 获取主题
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * 设置主题
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * 获取意见原因
	 */
	public String getOpinion() {
		return opinion;
	}

	/**
	 * 设置意见原因
	 */
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}



	public Long getExtId() {
		return extId;
	}

	public void setExtId(Long extId) {
		this.extId = extId;
	}

	/**
	 * 设置变量
	 */
	public Map<String,Object> getVars() {
		return vars;
	}

	/**
	 * 获取变量
	 */
	public void setVars(Map<String,Object> vars) {
		this.vars = vars;
	}

	/**
	 * 获取发送邮件地址
	 */
	public String[] getTo() {
		return to;
	}

	/**
	 * 设置发送邮件地址
	 */
	public void setTo(String[] to) {
		this.to = to;
	}

	/**
	 * 获取抄送送邮件地址
	 */
	public String[] getCc() {
	    if(cc==null) {
	        return null;
	    }
		return cc.toArray(new String[0]);
	}

	/**
	 *设置抄送送邮件地址
	 */
	public void setCc(List<String> cc) {
		this.cc = cc;
	}

	/**
	 * 获取暗送送邮件地址
	 */
	public String[] getBcc() {
        if(bcc==null) {
            return null;
        }
        return bcc.toArray(new String[0]);
    }

	/**
	 * 设置暗送送邮件地址
	 */
	public void setBcc(List<String> bcc) {
		this.bcc = bcc;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public void addCC(String email)
    {
        if(this.cc==null) {
            this.cc=new ArrayList<String>();
        }
        if(!cc.contains(email)) {
            cc.add(email);
        }
        
    }


}
