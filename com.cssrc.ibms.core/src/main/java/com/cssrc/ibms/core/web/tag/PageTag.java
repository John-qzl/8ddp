package com.cssrc.ibms.core.web.tag;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.displaytag.util.ParamEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.util.appconf.AppUtil;


/**
 * 分页标签,用于displaytag分页。<br/>
 * 
 * <pre>
 * 使用方法如下：
 * <b>paging.tld文件配置如下：</b>
 *  &lt;tag>
 *   &lt;description>
 *    分页标签
 *   &lt;/description>
 *   &lt;name>paging&lt;/name>
 *   &lt;tag-class>com.ibms.core.web.tag.PageTag&lt;/tag-class>
 *   &lt;body-content>JSP&lt;/body-content>
 *   &lt;attribute>
 *       &lt;name>tableId&lt;/name>
 *       &lt;required>true&lt;/required>
 *       &lt;rtexprvalue>true&lt;/rtexprvalue>
 *   &lt;/attribute>
 * &lt;/tag>
 * <b> 页面使用如下：</b>
 * tableId代表displaytag的Id。
 * &lt;ibms:paging tableId="roleItem"/>
 * </pre>
 * 
 * @author csx
 * 
 */
public class PageTag extends TagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5021452024918045959L;

	private static Logger logger = LoggerFactory.getLogger(PageTag.class);

	/**
	 * 分页对应的TableID
	 */
	private String tableId;

	/**
	 * 是否显示 信息： 显示记录从1到20，总数 37条
	 */
	private boolean showExplain = true;

	/**
	 * 是否显示 每页记录数量
	 */
	private boolean showPageSize = true;

	public String getTableId() {
		return tableId;
	}

	/**
	 * 设置displaytag的Id。
	 * 
	 * @param tableId
	 */
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public boolean isShowExplain() {
		return showExplain;
	}

	public void setShowExplain(boolean showExplain) {
		this.showExplain = showExplain;
	}

	public boolean isShowPageSize() {
		return showPageSize;
	}

	public void setShowPageSize(boolean showPageSize) {
		this.showPageSize = showPageSize;
	}

	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
	    HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
	    try
	    {
	      FreemarkEngine freemarkEngine = (FreemarkEngine)AppUtil.getBean("freemarkEngine");
	      Map<String, Object> model = new HashMap<String, Object>();
	      PagingBean pb = null;
	      logger.debug("table id:" + this.tableId);

	      String url = null;

	      if (this.tableId != null) {
	        pb = (PagingBean)request.getAttribute("pageBean" + this.tableId);
	        url = (String)request.getAttribute("requestURI" + this.tableId);
	        ParamEncoder paramEncoder = new ParamEncoder(this.tableId);
	        model.put("tableIdCode", paramEncoder.encodeParameterName(""));
	      } else {
	        pb = (PagingBean)request.getAttribute("pageBean");
	        url = url + request.getRequestURI();
	        model.put("tableIdCode", "");
	      }
	      if (pb == null) {
	        throw new RuntimeException("pagingBean can't no be null");
	      }
	      model.put("pageBean", pb);

	      String params = getQueryParameters(request);
	      if (url.indexOf("?") > 0) {
	        if (!"".equals(params))
	          url = url + "&" + params;
	        else
	          url = url + "?" + params;
	      }
	      else if (!"".equals(params)) {
	        url = url + "?" + params;
	      }
	      logger.info("current url:" + url);
	      model.put("showExplain", Boolean.valueOf(this.showExplain));
	      model.put("showPageSize", Boolean.valueOf(this.showPageSize));
	      model.put("baseHref", url);
	      
	      String templetName = "page.ftl";
	      int type = SysConfConstant.SHOW_TYPE;
			if(type == 0){
				templetName = "oldpage.ftl";//旧版本分页模板 by YangBo
			}
	      String html = freemarkEngine.mergeTemplateIntoString(templetName, model);
	      out.println(html);
	    } catch (Exception ex) {
	      ex.printStackTrace();
	    }
		return SKIP_BODY;
	}

	private String getQueryParameters(HttpServletRequest request) {
		Enumeration<?> names = request.getParameterNames();
		StringBuffer sb = new StringBuffer();
		int i = 0;
		while (names.hasMoreElements()) {
			if (i++ > 0) {
				sb.append("&");
			}
			String name = (String) names.nextElement();
			String value = request.getParameter(name);
			sb.append(name).append("=").append(value);
		}
		return sb.toString();
	}
}
