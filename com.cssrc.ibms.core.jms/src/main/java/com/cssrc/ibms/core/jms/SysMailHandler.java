package com.cssrc.ibms.core.jms;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cssrc.ibms.core.jms.intf.IJmsHandler;
import com.cssrc.ibms.core.mail.MailUtil;
import com.cssrc.ibms.core.mail.model.MailSeting;
import com.cssrc.ibms.core.mail.model.OutMailUserSeting;
import com.cssrc.ibms.core.mail.model.SysMailModel;
import com.cssrc.ibms.core.mail.service.OutMailUserSetingService;
/**
 * 
 * <p>Title:SysMailHandler</p>
 * @author Yangbo 
 * @date 2016年9月29日下午4:42:08
 */
public class SysMailHandler implements IJmsHandler {

	@Resource
	private OutMailUserSetingService outMailUserSetingService;
	private final Log logger = LogFactory.getLog(SysMailHandler.class);

	public void handMessage(Object model) {
		try {
			if (!(model instanceof SysMailModel))
				return;
			SysMailModel sysMailModel = (SysMailModel) model;
			Long outMailUserSetingId = sysMailModel.getOutMailUserSetingId();
			if ((outMailUserSetingId == null)
					|| (outMailUserSetingId.longValue() < 0L))
				return;
			OutMailUserSeting outMailUserSeting =this.outMailUserSetingService
					.getById(outMailUserSetingId);
			MailSeting mailSetting = outMailUserSetingService
					.getByOutMailUserSeting(outMailUserSeting);
			MailUtil util = new MailUtil(mailSetting);
			util.send(sysMailModel.getMail());
			this.logger.debug("out mail process success.");
		} catch (Exception e) {
			this.logger.error("out mail process error "
					+ ExceptionUtils.getRootCauseMessage(e));
			e.printStackTrace();
		}
	}
}
