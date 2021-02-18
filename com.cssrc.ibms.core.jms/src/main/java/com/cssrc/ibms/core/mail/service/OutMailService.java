package com.cssrc.ibms.core.mail.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.internet.MimeUtility;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.system.intf.ISysFileService;
import com.cssrc.ibms.api.system.model.ISysFile;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.jms.MessageProducer;
import com.cssrc.ibms.core.mail.MailUtil;
import com.cssrc.ibms.core.mail.api.AttacheHandler;
import com.cssrc.ibms.core.mail.dao.OutMailDao;
import com.cssrc.ibms.core.mail.model.Mail;
import com.cssrc.ibms.core.mail.model.MailAttachment;
import com.cssrc.ibms.core.mail.model.MailSeting;
import com.cssrc.ibms.core.mail.model.OaLinkman;
import com.cssrc.ibms.core.mail.model.OutMail;
import com.cssrc.ibms.core.mail.model.OutMailAttachment;
import com.cssrc.ibms.core.mail.model.OutMailUserSeting;
import com.cssrc.ibms.core.mail.model.SysMailModel;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

@Service
public class OutMailService extends BaseService<OutMail> {
	static Long MAIL_NO_READ = Long.valueOf(0L);
	static Long MAIL_IS_READ = Long.valueOf(1L);
	static Integer MAIL_IS_RECEIVE = Integer.valueOf(1);
	static Integer MAIL_IS_SEND = Integer.valueOf(2);
	static Integer MAIL_IS_DRAFT = Integer.valueOf(3);
	static Integer MAIL_IS_DELETE = Integer.valueOf(4);

	@Resource
	private OutMailDao dao;

	@Resource
	private OutMailUserSetingService outMailUserSetingService;

	@Resource
	private OaLinkmanService oaLinkmanService;

	@Resource
	private ISysFileService sysFileService;

	@Resource
	private OutMailAttachmentService outMailAttachmentService;

	@Resource
	private MessageProducer messageProducer;

	@Resource
	private ISysUserService userService;

	protected IEntityDao<OutMail, Long> getEntityDao() {
		return this.dao;
	}
	
	/**
	 * 邮件批量放入垃圾箱
	 *@author YangBo @date 2016年10月9日下午5:48:44
	 */
	public void addDump(Long[] lAryId) {
		for (Long l : lAryId) {
			OutMail outMail = (OutMail) this.dao.getById(l);
			this.dao.updateTypes(outMail.getMailId(), MAIL_IS_DELETE.intValue());
		}
	}
	
	/**
	 *收件箱未读邮件已读 
	 *@author YangBo @date 2016年10月10日上午8:30:18
	 */
	public void emailRead(OutMail outMail) throws NoSuchProviderException,
			MessagingException {
		if ((OutMail.Mail_IsNotRead.shortValue() == outMail.getIsRead()
				.shortValue())
				&& (OutMail.Mail_InBox.shortValue() != outMail.getTypes()
						.shortValue()))
			return;
		outMail.setIsRead(OutMail.Mail_IsRead);
		this.dao.update(outMail);
	}
	
	/**
	 * 通过setId获取emailId数组
	 *@author YangBo @date 2016年10月9日上午8:59:46
	 */
	public List<String> getUIDBySetId(Long setId) {
		List uidList = this.dao.getUIDBySetId(setId);
		return uidList;
	}

	public List<Mail> getMailListBySetting(OutMailUserSeting outMailUserSeting, List<String> uidList)
			throws Exception
			{
		MailSeting seting = OutMailUserSetingService.getByOutMailUserSeting(outMailUserSeting);
		MailUtil mailUtil = new MailUtil(seting);

		String latestEmailId = "";

		if (BeanUtils.isNotEmpty(uidList))
			latestEmailId = (String)uidList.get(0);
		else if (uidList == null) {
			uidList = new ArrayList();
		}
		List finalList = uidList;
		
	     /*honghuaju  List list = mailUtil.receive(new AttacheHandler(finalList, outMailUserSeting)
        {
          public Boolean isDownlad(String UID)
          {
         return Boolean.valueOf(!this.finalList.contains(UID));
          }
    
          public void handle(Part part, Mail mail)
          {
            try {
            OutMailService.this.saveAttach(part, mail, this.val$outMailUserSeting.getMailAddress());
            } catch (Exception e) {
            e.printStackTrace();
            }
          }
        }
        , latestEmailId);
    
     return list;*/	
		return null;
			}

	public void saveMail(List<Mail> list, Long setId) throws Exception {
		for (Mail mail : list) {
			OutMail bean = getOutMail(mail, setId);

			Long mailId = Long.valueOf(UniqueIdUtil.genId());
			bean.setMailId(mailId);

			bean.setEmailId(mail.getUID());
			this.dao.add(bean);
			this.logger.info("已下载邮件" + bean.getTitle());
			List<MailAttachment> attachments = mail.getMailAttachments();
			if (BeanUtils.isEmpty(attachments))
				continue;
			for (MailAttachment attachment : attachments) {
				String fileName = attachment.getFileName();
				String filePath = attachment.getFilePath();
				String ext = FileUtil.getFileExt(fileName);
				Long fileId = Long
						.valueOf(StringUtil.isNotEmpty(filePath) ? new Long(
								new File(filePath).getName().replace("." + ext,
										"")).longValue() : UniqueIdUtil.genId());
				OutMailAttachment outMailAttachment = new OutMailAttachment();
				outMailAttachment.setFileId(fileId);
				outMailAttachment.setFileName(attachment.getFileName());
				outMailAttachment.setFilePath(filePath);
				outMailAttachment.setMailId(mailId);
				this.outMailAttachmentService.add(outMailAttachment);
			}
		}
	}

	private OutMail getOutMail(Mail mail, Long setId) throws Exception {
		OutMail bean = new OutMail();
		Date sentDate = null;
		if (mail.getSendDate() != null)
			sentDate = mail.getSendDate();
		else {
			sentDate = new Date();
		}

		bean.setMailDate(sentDate);
		bean.setSetId(setId);
		bean.setTitle(mail.getSubject());
		bean.setContent(mail.getContent());

		bean.setSenderAddresses(mail.getSenderAddress());
		bean.setSenderName(mail.getSenderName());

		bean.setReceiverAddresses(mail.getReceiverAddresses());
		bean.setReceiverNames(mail.getReceiverName());

		bean.setBcCAddresses(mail.getBcCAddresses());
		bean.setBcCAnames(mail.getBccName());

		bean.setCcAddresses(mail.getCopyToAddresses());
		bean.setCcNames(mail.getCopyToName());
		bean.setTypes(MAIL_IS_RECEIVE);
		bean.setIsRead(OutMail.Mail_IsNotRead);
		bean.setUserId(UserContextUtil.getCurrentUserId());
		return bean;
	}

	private void saveAttach(Part message, Mail mail, String userAccount)
			throws Exception {
		String filename = MimeUtility.decodeText(message.getFileName());
		Calendar cal = Calendar.getInstance();
		int year = cal.get(1);
		int month = cal.get(2) + 1;
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		String curAccount = curUser.getUsername();
		String relateFilePath = "/emailAttachs/" + curAccount + "/"
				+ userAccount + "/" + year + "/" + month + "/"
				+ UniqueIdUtil.genId() + "." + FileUtil.getFileExt(filename);
		String filePath = AppUtil.getRealPath(relateFilePath);
		FileUtil.createFolderFile(filePath);
		FileUtil.writeFile(filePath, message.getInputStream());
		mail.getMailAttachments().add(
				new MailAttachment(filename, relateFilePath));
	}
	
	/**
	 * 整合外部邮件功能菜单
	 *@author YangBo @date 2016年10月11日上午10:33:29
	 *@param userId
	 *@return
	 *@throws Exception
	 */
	public List<OutMailUserSeting> getMailTreeData(Long userId)
			throws Exception {
		List<OutMailUserSeting> list = this.outMailUserSetingService
				.getMailByUserId(userId);
		List temp = new ArrayList();
		OutMailUserSeting omus = null;
		for (OutMailUserSeting beanTemp : list) {
			beanTemp.setParentId(Long.valueOf(0L));
			long id = beanTemp.getId().longValue();
			temp.add(beanTemp);
			for (int i = 0; i < 4; i++) {
				omus = new OutMailUserSeting();
				if (i == 0) {
					omus.setUserName("收件箱("
							+ getCount(id, MAIL_IS_RECEIVE.intValue()) + ")");
					omus.setTypes(MAIL_IS_RECEIVE);
				} else if (i == 1) {
					omus.setUserName("发件箱("
							+ getCount(id, MAIL_IS_SEND.intValue()) + ")");
					omus.setTypes(MAIL_IS_SEND);
				} else if (i == 2) {
					omus.setUserName("草稿箱("
							+ getCount(id, MAIL_IS_DRAFT.intValue()) + ")");
					omus.setTypes(MAIL_IS_DRAFT);
				} else {
					omus.setUserName("垃圾箱("
							+ getCount(id, MAIL_IS_DELETE.intValue()) + ")");
					omus.setTypes(MAIL_IS_DELETE);
				}
				omus.setId(Long.valueOf(UniqueIdUtil.genId()));
				omus.setParentId(beanTemp.getId());
				temp.add(omus);
			}
		}
		return temp;
	}
	/**
	 * 查看邮件分页信息
	 *@author YangBo @date 2016年10月9日上午8:41:20
	 */
	public List<OutMail> getFolderList(QueryFilter queryFilter) {
		return this.dao.getFolderList(queryFilter);
	}

	private int getCount(long id, int type) {
		return this.dao.getFolderCount(id, type);
	}
	/**
	 * 发送邮件方法
	 *@author YangBo @date 2016年10月10日上午9:09:34
	 */
	public Long sendMail(OutMail outMail, long userId, long mailId,
			int isReply, String context, String basePath) throws Exception {
		String content = outMail.getContent();
		if ((mailId == 0L) || (isReply == 1)) {
			outMail.setMailId(Long.valueOf(UniqueIdUtil.genId()));
			add(outMail);
		} else {
			this.dao.updateTypes(Long.valueOf(mailId), 2);
		}
		outMail.setContent(content);
		Mail mail = getMail(outMail, basePath);
		sendToMQ(mail, outMail.getSetId());
		return outMail.getMailId();
	}
	
	/**
	 * 发送到MQ
	 *@author YangBo @date 2016年10月10日上午10:27:29
	 *@param mail
	 *@param outMailUserSetingId
	 */
	public void sendToMQ(Mail mail, Long outMailUserSetingId) {
		SysMailModel model = new SysMailModel();
		model.setMail(mail);
		model.setOutMailUserSetingId(outMailUserSetingId);
		//发送jms消息
		this.messageProducer.send(model);
	}

	public OutMail getOutMailReply(Long mailId) {
		OutMail outMail = (OutMail) getById(mailId);
		outMail.setIsReply(OutMail.Mail_IsReplay);
		outMail.setTitle("回复:" + outMail.getTitle());
		return outMail;
	}
	
	/**
	 * outMail转成mail
	 *@author YangBo @date 2016年10月10日上午10:38:30
	 *@param outMail
	 *@param basePath
	 *@return
	 *@throws Exception
	 */
	private Mail getMail(OutMail outMail, String basePath) throws Exception {
		Mail mail = new Mail();
		if (BeanUtils.isNotEmpty(outMail)) {
			mail.setSenderName(outMail.getSenderName());
			mail.setSenderAddress(outMail.getSenderAddresses());
			mail.setReceiverAddresses(outMail.getReceiverAddresses());
			if (StringUtil.isNotEmpty(outMail.getBcCAddresses())) {
				mail.setBcCAddresses(outMail.getBcCAddresses());
			}
			if (StringUtil.isNotEmpty(outMail.getCcAddresses())) {
				mail.setCopyToAddresses(outMail.getCcAddresses());
			}
			String fileIds = outMail.getFileIds().replaceAll("quot;", "\"");

			JSONObject jsonObj = JSONObject.fromObject(fileIds);
			JSONArray jsonArray = JSONArray.fromObject(jsonObj.get("attachs"));
			if (jsonArray.size() > 0) {
				ISysFile sysFile = null;
				List attachments = mail.getMailAttachments();
				for (Iterator localIterator = jsonArray.iterator(); localIterator
						.hasNext();) {
					Object obj = localIterator.next();
					JSONObject json = (JSONObject) obj;
					long id = Long.parseLong(json.getString("id"));
					sysFile = (ISysFile) this.sysFileService.getById(Long
							.valueOf(id));
					String filePath = sysFile.getFilepath();
					String fileName = sysFile.getFilename() + "."
							+ sysFile.getExt();
					if (StringUtil.isEmpty(filePath)) {
						attachments.add(new MailAttachment(fileName, sysFile
								.getFileBlob()));
					} else {
						if (StringUtil.isEmpty(basePath)) {
							basePath = AppUtil.getAttachPath();
						}
						filePath = basePath + File.separator + filePath;
						attachments.add(new MailAttachment(fileName, filePath));
					}
				}
			}
			mail.setContent(outMail.getContent());
			mail.setSubject(outMail.getTitle());
		}
		return mail;
	}
	
	/**
	 * 发送邮件错误
	 *@author YangBo @date 2016年10月10日上午10:44:22
	 *@param errorMsg
	 *@param recieveAdress
	 *@param outMailUserSeting
	 *@throws Exception
	 */
	public void sendError(String errorMsg, String recieveAdress,
			OutMailUserSeting outMailUserSeting) throws Exception {
		MailSeting seting = OutMailUserSetingService
				.getByOutMailUserSeting(outMailUserSeting);
		MailUtil mailUtil = new MailUtil(seting);
		Mail mail = new Mail();
		mail.setContent(errorMsg);
		mail.setSubject("IBMS错误报告！");
		mail.setReceiverAddresses(recieveAdress);
		mailUtil.send(mail);
	}
	
	/**
	 *根据外部邮件用户设置主键删除对应邮件 
	 *@author YangBo @date 2016年10月11日上午10:21:44
	 *@param setId
	 */
	public void delBySetId(Long setId) {
		this.dao.delBySetId(setId);
	}

	public String mailAttachementFilePath(OutMailAttachment entity)
			throws Exception
	{
		OutMail outMail = (OutMail)getById(entity.getMailId());
		Long setId = outMail.getSetId();
		String emailId = outMail.getEmailId();
		OutMailUserSeting outMailUserSeting = (OutMailUserSeting)this.outMailUserSetingService.getById(setId);
		String mailAddress = outMailUserSeting.getMailAddress();
		MailSeting seting = OutMailUserSetingService.getByOutMailUserSeting(outMailUserSeting);
		seting.setIsHandleAttach(Boolean.valueOf(true));
		MailUtil mailUtil = new MailUtil(seting);
		
		List<Mail> list = mailUtil.receive(new AttacheHandler(emailId, mailAddress)
		{
			public Boolean isDownlad(String UID)
			{
				if (StringUtil.isEmpty(UID)) 
					return Boolean.valueOf(false);
				return Boolean.valueOf(UID.equals(this.emailId));
			}

			public void handle(Part part, Mail mail)
			{
				try {
					OutMailService.this.saveAttach(part, mail, this.mailAddress);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, emailId);
		if (BeanUtils.isEmpty(list)) throw new Exception("找不到该邮件，可能邮件已被删除！");
		Long mailId = outMail.getMailId();
		String attachFileName = entity.getFileName();
		String resultPath = "";
		Mail mail = (Mail)list.get(0);
		List<MailAttachment> attachments = mail.getMailAttachments();
		for (MailAttachment attachment : attachments) {
			String fileName = attachment.getFileName();
			String filePath = attachment.getFilePath();
			if (fileName.equals(attachFileName)) resultPath = filePath;
			this.outMailAttachmentService.updateFilePath(fileName, mailId, filePath);
		}
		return resultPath;
	}
	
	/**
	 * 联系人名称
	 *@author YangBo @date 2016年10月10日上午11:04:04
	 *@param email
	 *@return
	 */
	public String getNameByEmail(String email) {
		Long userId = UserContextUtil.getCurrentUserId();
		String linkName = "陌生人";
		List<OaLinkman> linkmans = this.oaLinkmanService.getByUserEmail(userId, email);
		if (BeanUtils.isNotEmpty(linkmans)) {
			linkName = linkmans.get(0).getName();
		} else {
			List users = this.userService.findLinkMan(email);
			if (BeanUtils.isNotEmpty(users)) {
				linkName = ((ISysUser) users.get(0)).getFullname();
			}
		}
		return linkName;
	}
}
