package com.cssrc.ibms.core.mail.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.jms.intf.IOutMailUserSetingService;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.mail.MailUtil;
import com.cssrc.ibms.core.mail.dao.OutMailUserSetingDao;
import com.cssrc.ibms.core.mail.model.MailSeting;
import com.cssrc.ibms.core.mail.model.OutMailUserSeting;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.encrypt.EncryptUtil;
/**
 * 
 * <p>Title:OutMailUserSetingService</p>
 * @author Yangbo 
 * @date 2016年9月29日下午4:00:07
 */
@Service
public class OutMailUserSetingService extends BaseService<OutMailUserSeting> implements IOutMailUserSetingService{

	@Resource
	private OutMailUserSetingDao dao;

	@Resource
	protected OutMailService outMailService;

	protected IEntityDao<OutMailUserSeting, Long> getEntityDao() {
		return this.dao;
	}
	/**
	 * 测试外部邮件用户连接
	 *@author YangBo @date 2016年10月11日下午2:33:17
	 *@param outMailUserSeting
	 *@throws Exception
	 */
	public void testConnection(OutMailUserSeting outMailUserSeting)
			throws Exception {
		MailSeting seting = getByOutMailUserSeting(outMailUserSeting);
		MailUtil mailUtil = new MailUtil(seting);
		mailUtil.connectSmtpAndReceiver();
	}
	
	/**
	 * outMailUserSeting转成mailSetting
	 *@author YangBo @date 2016年10月10日上午10:41:46
	 *@param outMailUserSeting
	 *@return
	 *@throws Exception
	 */
	public static MailSeting getByOutMailUserSeting(
			OutMailUserSeting outMailUserSeting) throws Exception {
		MailSeting seting = new MailSeting();
		String protocal = outMailUserSeting.getMailType();
		seting.setSendHost(outMailUserSeting.getSmtpHost());
		seting.setSendPort(outMailUserSeting.getSmtpPort());
		seting.setProtocal(protocal);
		seting.setMailAddress(outMailUserSeting.getMailAddress());
		seting.setPassword(EncryptUtil.decrypt(outMailUserSeting.getMailPass()));
		seting.setNickName(outMailUserSeting.getUserName());
		seting.setSSL(Boolean.valueOf(outMailUserSeting.getSSL() == 1));
		seting.setValidate(Boolean.valueOf(outMailUserSeting.getValidate() == 1));
		seting.setIsDeleteRemote(Boolean.valueOf(outMailUserSeting
				.getIsDeleteRemote() == 1));
		seting.setIsHandleAttach(Boolean.valueOf(outMailUserSeting
				.getIsHandleAttach() == 1));
		if ("pop3".equals(protocal)) {
			seting.setReceiveHost(outMailUserSeting.getPopHost());
			seting.setReceivePort(outMailUserSeting.getPopPort());
		} else {
			seting.setReceiveHost(outMailUserSeting.getImapHost());
			seting.setReceivePort(outMailUserSeting.getImapPort());
		}
		return seting;
	}

	public void setDefault(OutMailUserSeting outMailUserSeting)
			throws Exception {
		OutMailUserSeting mail = this.dao.getByIsDefault(UserContextUtil
				.getCurrentUserId().longValue());
		if (BeanUtils.isNotEmpty(mail)) {
			mail.setIsDefault(Integer.valueOf(0));
			this.dao.updateDefault(mail);
		}
		this.dao.updateDefault(outMailUserSeting);
	}

	public boolean isExistMail(long id, OutMailUserSeting outMailUserSeting)
			throws Exception {
		String address = outMailUserSeting.getMailAddress();
		int result = this.dao.getCountByAddress(address);
		if (id != 0L) {
			OutMailUserSeting mail = (OutMailUserSeting) getById(Long
					.valueOf(id));

			return (result != 0) && (!address.equals(mail.getMailAddress()));
		}

		return result != 0;
	}
	/**
	 * 根据发件人邮箱地址获取该外部邮件用户设置
	 *@author Yangbo @date 2016年10月10日上午8:57:02
	 */
	public OutMailUserSeting getMailByAddress(String address) {
		return this.dao.getMailByAddress(address);
	}

	public OutMailUserSeting getByIsDefault(long userId) {
		return this.dao.getByIsDefault(userId);
	}

	public List<OutMailUserSeting> getMailByUserId(Long userId) {
		return this.dao.getMailByUserId(userId);
	}

	public List<OutMailUserSeting> getAllByUserId(QueryFilter queryFilter) {
		return this.dao.getAllByUserId(queryFilter);
	}

	public int getCountByUserId(long userId) {
		return this.dao.getCountByUserId(userId);
	}

	public void delAllByIds(Long[] lAryId) {
		for (Long setId : lAryId) {
			delById(setId);
			this.outMailService.delBySetId(setId);
		}
	}
}
