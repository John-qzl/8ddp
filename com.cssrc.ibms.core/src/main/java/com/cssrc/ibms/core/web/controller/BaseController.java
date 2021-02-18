package com.cssrc.ibms.core.web.controller;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.validation.commons.ConfigurableBeanValidator;

import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.util.appconf.ConfigUtil;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringPool;
/**
 * Controller基类，提供获取国际化，邮件发送等相关服务。
 * @author zhulongchao
 */
public class BaseController extends GenericController
{
	public static final String Message="message";
	
	

	/**
	 * 添加消息
	 * @param message
	 * @param model
	 */
	public void addMessage(ResultMessage message,HttpServletRequest request){
		HttpSession session= request.getSession();
		
		session.setAttribute(Message, message);
	}
	
	/**
	 * 移除消息
	 */
	public void removeMessage(HttpServletRequest request){
		HttpSession session= request.getSession();
		
		session.removeAttribute(Message);
	}
	
	@Resource
	protected ConfigurableBeanValidator confValidator;

	/**
	 * Set up a custom property editor for converting form inputs to real
	 * objects
	 * 
	 * @param request
	 *            the current request
	 * @param binder the data binder
	 * @deprecated move to BaseFormController
	 */
	@InitBinder
	protected void initBinder(HttpServletRequest request,ServletRequestDataBinder binder) {
		logger.debug("init binder ....");
		binder.registerCustomEditor(Integer.class, null,new CustomNumberEditor(Integer.class, null, true));
		binder.registerCustomEditor(Long.class, null, new CustomNumberEditor(Long.class, null, true));
		binder.registerCustomEditor(byte[].class,new ByteArrayMultipartFileEditor());
		SimpleDateFormat dateFormat = new SimpleDateFormat(StringPool.DATE_FORMAT_DATE);
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true));
	}
	
  /**
	 * 服务端验证表单输入内容。<br>
	 * 使用方法：
	 * 
	 * @param form
	 * @param obj
	 * @param result
	 * @param request
	 * @return
	 * @deprecated move to BaseFormController
	 */
	protected  ResultMessage validForm(String form,Object obj,BindingResult result,HttpServletRequest request) {
    	ResultMessage resObj=new ResultMessage(ResultMessage.Success,"");
		confValidator.setFormName(form);
		confValidator.validate(obj, result);
		if(result.hasErrors())
		{
			resObj.setResult(ResultMessage.Fail);
			List<FieldError> list= result.getFieldErrors();
			String errMsg="";
		    for(FieldError err :list)
		    {
		    	String msg=err.getDefaultMessage()/* getText(err.getDefaultMessage(),err.getArguments(), request)*/;
		    	errMsg+=msg +"\r\n";
		    }
		    resObj.setMessage(errMsg);
		}
		return resObj;
	}
	
	public PagingBean getInitPagingBean(HttpServletRequest request){
		Integer start = Integer.valueOf(0);
	    Integer limit = PagingBean.DEFAULT_PAGE_SIZE;

	    String s_start = request.getParameter("start");
	    String s_limit = request.getParameter("limit");
	    if (!CommonTools.isNullString(s_start)) {
	      start = new Integer(s_start);
	    }
	    if (!CommonTools.isNullString(s_limit)) {
	      limit = new Integer(s_limit);
	    }
	    PagingBean pb = new PagingBean(start.intValue(), limit.intValue());
	    return pb;
	}
	
	/**
	 * 传入参数找到对应jsp文件
	 * @param category
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	/** @deprecated */
	 public ModelAndView getView(String category, String id)
	 {
		 String view = ConfigUtil.getVal(category, id);
		 return new ModelAndView(view);
	 }
	 
	
}
