package com.cssrc.ibms.core.mail.api;


import javax.mail.Part;

import com.cssrc.ibms.core.mail.model.Mail;

public abstract class AttacheHandler 
{
	protected String emailId;
	protected String mailAddress;
	
	public abstract void handle(Part paramPart, Mail paramMail);

	public abstract Boolean isDownlad(String paramString);
	
	public AttacheHandler(String emailId,String mailAddress){
		this.emailId=emailId;
		this.mailAddress=mailAddress;
	}
	
	
}

