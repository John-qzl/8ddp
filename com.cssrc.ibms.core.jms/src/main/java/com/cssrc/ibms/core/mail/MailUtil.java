package com.cssrc.ibms.core.mail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.search.MessageIDTerm;
import javax.mail.search.SearchTerm;
import javax.mail.util.ByteArrayDataSource;
import com.cssrc.ibms.core.mail.api.AttacheHandler;
import com.cssrc.ibms.core.mail.model.Mail;
import com.cssrc.ibms.core.mail.model.MailAddress;
import com.cssrc.ibms.core.mail.model.MailAttachment;
import com.cssrc.ibms.core.mail.model.MailSeting;
import com.sun.net.ssl.internal.ssl.Provider;

public class MailUtil {
	private MailSeting mailSetting;
	private AttacheHandler handler;

	public MailUtil(MailSeting mailSeting) {
		this.mailSetting = mailSeting;
	}

	public void connectSmtpAndReceiver() throws MessagingException {
		connectSmtp();
		connectReciever();
	}

	public void connectSmtp() throws MessagingException {
		Session session = getMailSession("smtp");

		Transport transport = null;
		try {
			transport = session.getTransport("smtp");
			transport.connect(this.mailSetting.getSendHost(),
					this.mailSetting.getMailAddress(),
					this.mailSetting.getPassword());
		} catch (MessagingException e) {
			e.printStackTrace();
			throw e;
		} finally {
			transport.close();
		}
	}

	public void connectReciever() throws MessagingException {
		Session session = getMailSession(this.mailSetting.getProtocal());

		Store store = null;
		URLName urln = new URLName(this.mailSetting.getProtocal(),
				this.mailSetting.getReceiveHost(),
				Integer.parseInt(this.mailSetting.getReceivePort()), null,
				this.mailSetting.getMailAddress(),
				this.mailSetting.getPassword());
		try {
			store = session.getStore(urln);
			store.connect();
		} catch (MessagingException e) {
			e.printStackTrace();
			throw e;
		} finally {
			store.close();
		}
	}

	public void send(Mail mail) throws UnsupportedEncodingException,
			MessagingException {
		Session session = getMailSession("smtp");

		MimeMessage message = new MimeMessage(session);
		addAddressInfo(mail, message);

		BodyPart contentPart = new MimeBodyPart();
		Multipart multipart = new MimeMultipart();
		contentPart.setHeader("Content-Transfer-Encoding", "base64");

		contentPart.setContent(mail.getContent(), "text/html;charset=utf-8");
		message.setSubject(mail.getSubject(), "utf-8");
		message.setText("utf-8", "utf-8");
		message.setSentDate(new Date());
		multipart.addBodyPart(contentPart);
		message.setContent(multipart);

		for (MailAttachment attachment : mail.getMailAttachments()) {
			BodyPart messageBodyPart = new MimeBodyPart();
			DataSource source = null;
			String filePath = attachment.getFilePath();
			if ((filePath == null) || ("".equals(filePath)))
				source = new ByteArrayDataSource(attachment.getFileBlob(),
						"application/octet-stream");
			else {
				source = new FileDataSource(new File(filePath));
			}
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(MimeUtility.encodeWord(
					attachment.getFileName(), "UTF-8", "Q"));
			multipart.addBodyPart(messageBodyPart);
		}
		message.setContent(multipart);
		message.saveChanges();
		Transport transport = session.getTransport("smtp");
		transport.connect(this.mailSetting.getSendHost(),
				this.mailSetting.getMailAddress(),
				this.mailSetting.getPassword());
		transport.sendMessage(message, message.getAllRecipients());
	}

	public List<Mail> receive(AttacheHandler handler) throws Exception {
		return receive(handler, "");
	}

	public List<Mail> receive(AttacheHandler handler, String lastHandleUID)
			throws Exception {
		this.handler = handler;
		Store connectedStore = getConnectedStore();
		Folder folder = getFolder(connectedStore);
		try {
			List localList = getMessages(folder, lastHandleUID);
			return localList;
		} catch (MessagingException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			close(folder, connectedStore);
		}
	
	}

	public Mail getByMessageID(AttacheHandler handler, String messageID)
			throws Exception {
		this.handler = handler;
		Store connectedStore = getConnectedStore();
		Folder folder = getFolder(connectedStore);
		SearchTerm searchTerm = new MessageIDTerm(messageID);
		Message[] messages = folder.search(searchTerm);
		if ((messages == null) || (messages.length == 0))
			return null;
		List mailList = new ArrayList();
		buildMailList(messageID, (MimeMessage) messages[0], mailList);
		return (Mail) mailList.get(0);
	}

	private Store getConnectedStore() throws MessagingException {
		Session session = getMailSession(this.mailSetting.getProtocal());
		URLName urln = new URLName(this.mailSetting.getProtocal(),
				this.mailSetting.getReceiveHost(),
				Integer.parseInt(this.mailSetting.getReceivePort()), null,
				this.mailSetting.getMailAddress(),
				this.mailSetting.getPassword());

		Store store = session.getStore(urln);
		store.connect();
		return store;
	}

	private Folder getFolder(Store store) throws MessagingException {
		if (!store.isConnected())
			store = getConnectedStore();
		Folder folder = store.getFolder("INBOX");
		if (this.mailSetting.getIsDeleteRemote().booleanValue())
			folder.open(2);
		else {
			folder.open(1);
		}
		return folder;
	}

	private List<Mail> getMessages(Folder folder, String lastHandleUID)
			throws Exception {
		int total = folder.getMessageCount();
		List mailList = new ArrayList();
		return getMessages(folder, lastHandleUID, mailList, total);
	}

	private List<Mail> getMessages(Folder folder, String lastHandleUID,
			List<Mail> mailList, int endIndex) throws Exception {
		MimeMessage msg = null;
		boolean isLastHandleUIDNotEmpty = (lastHandleUID != null)
				&& (!"".equals(lastHandleUID.trim()));
		try {
			for (int i = endIndex; i > 0; i--) {
				if (!folder.isOpen())
					folder = getFolder(folder.getStore());
				msg = (MimeMessage) folder.getMessage(i);
				String messageId = msg.getMessageID();
				if ((isLastHandleUIDNotEmpty)
						&& (lastHandleUID.equals(messageId)))
					break;
				buildMailList(messageId, msg, mailList);
			}
		} catch (FolderClosedException closeException) {
			folder = getFolder(folder.getStore());
			getMessages(folder, lastHandleUID, mailList,
					endIndex - mailList.size());
		}

		Collections.reverse(mailList);
		return mailList;
	}

	private void buildMailList(String messageId, MimeMessage message,
			List<Mail> list) throws Exception {
		if ((this.handler.isDownlad(messageId) == null)
				|| (!this.handler.isDownlad(messageId).booleanValue()))
			return;
		Mail mail = getMail(message);
		mail.setUID(messageId);
		list.add(mail);
		if (this.mailSetting.getIsDeleteRemote().booleanValue()) {
			message.setFlag(Flags.Flag.DELETED, true);
		}
		System.gc();
	}

	private Mail getMail(MimeMessage message) throws Exception {
		Mail mail = new Mail();
		Date sentDate = null;
		if (message.getSentDate() != null)
			sentDate = message.getSentDate();
		else {
			sentDate = new Date();
		}

		mail.setSendDate(sentDate);
		mail.setSubject(MimeUtility.decodeText(message.getSubject()));

		StringBuffer bodytext = new StringBuffer();
		getMailContent(message, bodytext, mail);
		mail.setContent(bodytext.toString());

		MailAddress temp = getFrom(message);
		mail.setSenderAddress(temp.getAddress());
		mail.setSenderName(temp.getName());

		temp = getMailAddress(Message.RecipientType.TO, message);
		mail.setReceiverAddresses(temp.getAddress());
		mail.setReceiverName(temp.getName());

		temp = getMailAddress(Message.RecipientType.BCC, message);
		mail.setBcCAddresses(temp.getAddress());
		mail.setBccName(temp.getName());

		temp = getMailAddress(Message.RecipientType.CC, message);
		mail.setCopyToAddresses(temp.getAddress());
		mail.setCopyToName(temp.getName());
		return mail;
	}

	private MailAddress getFrom(MimeMessage mimeMessage) throws Exception {
		MailAddress mailAddress = new MailAddress();
		try {
			InternetAddress[] address = (InternetAddress[]) mimeMessage
					.getFrom();
			if ((address == null) || (address.length == 0))
				return mailAddress;
			mailAddress.setAddress(address[0].getAddress());
			mailAddress.setName(address[0].getPersonal());
		} catch (Exception localException) {
		}
		return mailAddress;
	}

	private MailAddress getMailAddress(Message.RecipientType recipientType,
			MimeMessage mimeMessage) throws Exception {
		MailAddress mailAddress = new MailAddress();
		InternetAddress[] address = (InternetAddress[]) mimeMessage
				.getRecipients(recipientType);
		if (address == null)
			return mailAddress;
		StringBuffer addresses = new StringBuffer("");
		StringBuffer name = new StringBuffer("");
		for (int i = 0; i < address.length; i++) {
			String email = address[i].getAddress();
			if (email != null) {
				String personal = address[i].getPersonal();
				if (personal == null)
					personal = email;
				switch (i) {
				case 0:
					addresses.append(MimeUtility.decodeText(email));
					name.append(MimeUtility.decodeText(personal));
					break;
				default:
					addresses.append(",").append(MimeUtility.decodeText(email));
					name.append(",").append(MimeUtility.decodeText(personal));
				}
			}
		}
		mailAddress.setAddress(addresses.toString());
		mailAddress.setName(name.toString());
		return mailAddress;
	}

	private void getMailContent(Part message, StringBuffer bodyText, Mail mail)
			throws Exception {
		String contentType = message.getContentType();
		int nameindex = contentType.indexOf("name");
		boolean conname = false;
		if (nameindex != -1) {
			conname = true;
		}
		if (((message.isMimeType("text/plain")) || (message
				.isMimeType("text/html"))) && (!conname)) {
			bodyText.append((String) message.getContent());
		} else if (message.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) message.getContent();

			int count = multipart.getCount();
			Map partMap = new LinkedHashMap();

			boolean blnTxt = false;
			boolean blnHtml = false;
			for (int i = 0; i < count; i++) {
				Part tmpPart = multipart.getBodyPart(i);
				String partType = tmpPart.getContentType();
				if (tmpPart.isMimeType("text/plain")) {
					partMap.put("text/plain", tmpPart);
					blnTxt = true;
				} else if (tmpPart.isMimeType("text/html")) {
					partMap.put("text/html", tmpPart);
					blnHtml = true;
				} else {
					partMap.put(partType, tmpPart);
				}
			}
			if ((blnTxt) && (blnHtml)) {
				partMap.remove("text/plain");
			}
			Set set = partMap.entrySet();
			for (Iterator it = set.iterator(); it.hasNext();) {
				getMailContent((Part) ((Map.Entry) it.next()).getValue(),
						bodyText, mail);
			}
		} else if (message.isMimeType("message/rfc822")) {
			getMailContent((Part) message.getContent(), bodyText, mail);
		} else if ((message.isMimeType("application/octet-stream"))
				|| (message.isMimeType("image/*"))
				|| (message.isMimeType("application/*"))) {
			if (this.mailSetting.getIsHandleAttach().booleanValue()) {
				this.handler.handle(message, mail);
			} else {
				String filename = MimeUtility.decodeText(message.getFileName());
				mail.getMailAttachments().add(new MailAttachment(filename, ""));
			}
		}
	}

	private Properties getProperty(String protocal) {
		Security.addProvider(new Provider());
		Properties props = new Properties();
		if (this.mailSetting.getSSL().booleanValue()) {
			props.setProperty("mail." + protocal + ".socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
		}
		props.setProperty("mail." + protocal + ".socketFactory.fallback",
				"false");

		if ("smtp".equals(protocal)) {
			String host = this.mailSetting.getSendHost();
			props.setProperty("mail.smtp.host", host);
			props.setProperty("mail.smtp.port", this.mailSetting.getSendPort());
			props.setProperty("mail.smtp.socketFactory.port",
					this.mailSetting.getSendPort());
			props.setProperty("mail.smtp.auth",
					String.valueOf(this.mailSetting.getValidate()));
			int gmail = host.indexOf("gmail");
			int live = host.indexOf("live");
			if ((gmail != -1) || (live != -1)) {
				props.setProperty("mail.smtp.starttls.enable", "true");
			}
			if (!this.mailSetting.getSSL().booleanValue())
				props.setProperty("mail.smtp.socketFactory.class",
						"javax.net.SocketFactory");
		} else {
			props.setProperty("mail." + protocal + ".host",
					this.mailSetting.getReceiveHost());
			props.setProperty("mail." + protocal + ".port",
					this.mailSetting.getReceivePort());
			props.setProperty("mail." + protocal + ".socketFactory.port",
					this.mailSetting.getReceivePort());
			if ("pop3".equals(protocal))
				props.setProperty("mail.smtp.starttls.enable", "true");
			else {
				props.setProperty("mail.store.protocol", "imap");
			}
		}
		return props;
	}

	private Session getMailSession(String protocal) {
		Properties props = getProperty(protocal);

		Session instance = null;
		if ("imap".equals(protocal))
			instance = Session.getDefaultInstance(props, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(MailUtil.this.mailSetting
							.getMailAddress(), MailUtil.this.mailSetting
							.getPassword());
				}
			});
		else
			instance = Session.getInstance(props);

		return instance;
	}

	private void addAddressInfo(Mail mail, Message message)
			throws UnsupportedEncodingException, MessagingException {
		InternetAddress senderAddress = toInternetAddress(
				this.mailSetting.getNickName(), mail.getSenderAddress());
		message.setFrom(senderAddress);

		addAddressInfo(message, mail.getReceiverAddresses(),
				Message.RecipientType.TO);

		addAddressInfo(message, mail.getCopyToAddresses(),
				Message.RecipientType.CC);

		addAddressInfo(message, mail.getBcCAddresses(),
				Message.RecipientType.BCC);
	}

	private void addAddressInfo(Message message, String address,
			Message.RecipientType recipientType)
			throws UnsupportedEncodingException, MessagingException {
		MailAddress mailAddress = new MailAddress();
		Set addresseSet = new HashSet();
		if ((address != null) && (!"".equals(address.trim()))) {
			String[] addressArr = address.split(",");
			for (String id : addressArr)
				if ((id != null) && (!"".equals(id.trim()))) {
					mailAddress = new MailAddress();
					mailAddress.setAddress(id);
					mailAddress.setName(id);
					addresseSet.add(mailAddress);
				}
		}
		InternetAddress[] addressArr = toInternetAddress(addresseSet);
		if (addressArr != null)
			message.addRecipients(recipientType, addressArr);
	}

	private InternetAddress toInternetAddress(String name, String address)
			throws UnsupportedEncodingException, AddressException {
		if ((name != null) && (!name.trim().equals(""))) {
			return new InternetAddress(address, MimeUtility.encodeWord(name,
					"utf-8", "Q"));
		}
		return new InternetAddress(address);
	}

	private InternetAddress toInternetAddress(MailAddress emailAddress)
			throws UnsupportedEncodingException, AddressException {
		return toInternetAddress(emailAddress.getName(),
				emailAddress.getAddress());
	}

	private InternetAddress[] toInternetAddress(Set<MailAddress> set)
			throws UnsupportedEncodingException, AddressException {
		if ((set == null) || (set.size() < 1))
			return null;
		InternetAddress[] address = new InternetAddress[set.size()];
		Iterator it = set.iterator();
		int i = 0;
		while (it.hasNext()) {
			address[(i++)] = toInternetAddress((MailAddress) it.next());
		}
		return address;
	}

	private void close(Folder folder, Store store) {
		try {
			if ((folder != null) && (folder.isOpen())) {
				folder.close(this.mailSetting.getIsDeleteRemote()
						.booleanValue());
			}
			if ((store != null) && (store.isConnected()))
				store.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			folder = null;
			store = null;
		}
	}
}
