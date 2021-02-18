package com.cssrc.ibms.api.system.model;

public interface ISysTemplate
{
    
    public static Integer IS_DEFAULT_YES = 1;
    
    public static Integer IS_DEFAULT_NO = 0;
    
    /** 模板用途类型静态变量 **/
    /**
     * 终止提醒=1
     */
    public static Integer USE_TYPE_TERMINATION = 1;
    
    /**
     * 催办提醒=2
     */
    public static Integer USE_TYPE_URGE = 2;
    
    /**
     * 审批提醒（任务通知）=3
     */
    public static Integer USE_TYPE_NOTIFY = 3;
    
    /**
     * 撤销提醒=4
     */
    public static Integer USE_TYPE_REVOKED = 4;
    
    /**
     * 取消转办=5
     */
    public static Integer USE_TYPE_CANCLE_DELEGATE = 5;
    
    /**
     * 沟通提醒=6
     */
    public static Integer USE_TYPE_COMMUNICATION = 6;
    
    /**
     * 归档提醒（通知发起人）=7
     */
    public static Integer USE_TYPE_NOTIFY_STARTUSER = 7;
    
    /**
     * 转办提醒=8
     */
    public static Integer USE_TYPE_DELEGATE = 8;
    
    /**
     * 退回提醒(驳回 ,驳回到发起人)=9
     */
    public static Integer USE_TYPE_REJECT = 9;
    
    /**
     * 被沟通人提交（沟通反馈）=10
     */
    public static Integer USE_TYPE_FEEDBACK = 10;
    
    /**
     * 取消代理=11
     */
    public static Integer USE_TYPE_CANCLE_AGENT = 11;
    
    /**
     * 抄送提醒=12
     */
    public static Integer USE_TYPE_COPYTO = 12;
    
    /**
     * 流程节点无人员=13
     */
    public static Integer USE_TYPE_NOBODY = 13;
    
    /**
     * 跟进事项预警=14
     */
    public static Integer USE_TYPE_FOLLOWWARN = 14;
    
    /**
     * 逾期提醒=19
     */
    public static Integer USE_TYPE_OVERDUE = 19;
    
    /**
     * 代理提醒=22
     */
    public static Integer USE_TYPE_AGENT = 22;
    
    /**
     * 消息转发
     */
    public static Integer USE_TYPE_FORWARD = 23;
    
    /**
     * 重启任务
     */
    public static Integer USE_TYPE_RESTARTTASK = 24;
    
    /**
     * 通知任务所属人(代理)
     */
    public static Integer USE_TYPE_NOTIFYOWNER_AGENT = 25;
    
    /**
     * 加签提醒
     */
    public static Integer USE_TYPE_TRANSTO = 26;
    
    /**
     * 被加签人提交(加签反馈)
     */
    public static Integer USE_TYPE_TRANSTO_FEEDBACK = 27;
    
    /**
     * 取消流转
     */
    public static Integer USE_TYPE_CANCLE_TRANSTO = 28;
    
    /**
     * 取回
     */
    public static Integer USE_TYPE_RETROACTIVE = 29;
    
    /**
     * 问题提醒
     */
    public static Integer USE_TYPE_PROBLEM = 30;
    
    /**
     * 模板类型=html消息
     */
    public static String TEMPLATE_TYPE_HTML = "html";
    
    /**
     * 模板类型=plain消息
     */
    public static String TEMPLATE_TYPE_PLAIN = "plain";
    
    /**
     * 模板标题=流程标题
     */
    public static String TEMPLATE_TITLE = "title";
    
    /**
     * 模板标题=邮件标题
     */
    public static String TEMPLATE_TITLE_MAIL = "mailtitle";
    
    /**
     * 模板标题=站内标题
     */
    public static String TEMPLATE_TITLE_INNER = "innertitle";
    
    String getTitle();
    
    String getHtmlContent();
    
    String getPlainContent();
    
    public Long getId();
}