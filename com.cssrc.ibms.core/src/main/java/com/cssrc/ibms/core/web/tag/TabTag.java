package com.cssrc.ibms.core.web.tag;

import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;


/**
 * tabtag 用途，在于显示tab控件。
 *
 * <pre>
 * 每一个tab选项卡都为一个独立的页面。
 * 用法：
 * &lt;f:tab curKey="key" curTab="key" tabName="" />
 *
 * tabName为conf/tabConfig.xml 定义的名称key。
 *
 * 用法2：
 * &lt;f:tab curTab="key" tabName="定义的名称key"  hideTabs="key1,key2"/>
 *
 * </pre>
 *
 * @author ray
 *
 */
@SuppressWarnings("serial")
public class TabTag extends TagSupport {
	private String tab = "tab";
	/**
	 * 当前选中的选项
	 */
	private String curTab = "";

	/**
	 * 在tabConfig.xml 中定义的tab名称。
	 */
	private String tabName = "";

	/**
	 * 需要隐藏的TAB。
	 */
	private String hideTabs = "";

	public int doEndTag() {
		try {
			String html = getTabHtml();
			pageContext.getOut().print(html);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	@SuppressWarnings("unchecked")
	private String getTabHtml() throws IOException {
		/*String path = SysConfConstant.CONF_ROOT + File.separator + "conf" + File.separator + "tabConfig.xml";
		InputStream stream = new BufferedInputStream(new FileInputStream(path));*/
        InputStream stream=this.getClass().getResourceAsStream("/tabConfig.xml");

		String xml = FileOperator.inputStream2String(stream, "utf-8");
		stream.close();
		Document document = Dom4jUtil.loadXml(xml);
		Element root = document.getRootElement();
		ISysUser currentUser = UserContextUtil.getCurrentUser();
		//判断该tab需不需要对三员和实施人员进行权限处理
		if(this.tabName!=null&&this.tabName.contains("%")){
			if(currentUser.getUserId().equals(ISysUser.RIGHT_USER))//权限管理员
				this.tabName = this.tabName.replace("%", "_") + "right";
			else if(currentUser.getUserId().equals(ISysUser.IMPLEMENT_USER))//系统实施人员
				this.tabName = this.tabName.replace("%", "_") + "implement";
			else
				this.tabName = this.tabName.replace("%", "");
		}
		Element tabEl = (Element) root.selectSingleNode("tab[@name='"
				+ this.tabName + "']");
		List<Element> list = tabEl.elements();
		String parameter = getParameter();
		StringBuffer sb = new StringBuffer();
		sb.append("<div class='panel-nav'>");
		sb.append("<div class='l-tab-links'><ul style='left: 0px; '>");
		String firstKey = list.get(0).attributeValue("key");
		for (int i = 0; i < list.size(); i++) {
			Element el = list.get(i);
			String key = el.attributeValue("key");
			String code = tab + "." + this.tabName + "." + key;
			String curTabName = el.attributeValue("title") ;
			// 如果包含在隐藏的tab中，指定的页签将不出现。
			if (hideTabs.contains(key))
				continue;
			String url = el.attributeValue("url");
			if (StringUtil.isNotEmpty(parameter)) {
				if (url.indexOf("?") != -1) {
					url += "&" + parameter;
				} else {
					url += "?" + parameter;
				}
			}
			if (StringUtil.isEmpty(this.curTab))
				this.curTab = firstKey;

			boolean isCurrent = this.curTab.equals(key);
			String html = this.getTabHtml(curTabName, i, isCurrent, url);
			sb.append(html);
		}
		sb.append("</ul></div></div>");
		return sb.toString();
	}

	/**
	 * 获取tab选项卡的html。
	 *
	 * @param tilte
	 * @param tabId
	 * @param isCurrentTab
	 * @param url
	 * @return
	 */
	private String getTabHtml(String tilte, int tabId, boolean isCurrentTab,
			String url) {
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		String ctx = request.getContextPath();
		String curUrl = isCurrentTab ? "####" : (ctx + url);// 取出当前URL
		String classes = isCurrentTab ? "class='l-selected'" : "";
		return "<li " + classes + "  tabid='tab" + tabId + "'><a href=\""
				+ curUrl + "\">" + tilte + "</a></li>";
	}

	/**
	 * 从request中获取所有的参数和参数值
	 *
	 * @return
	 */
	private String getParameter() {
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		Enumeration<?> enumeration = request.getParameterNames();
		StringBuffer sb = new StringBuffer();
		while (enumeration.hasMoreElements()) {
			String parameter = enumeration.nextElement().toString();
			String value = request.getParameter(parameter);

			sb.append(parameter + "=" + value + "&");
		}
		String returnUrl = request.getParameter(RequestUtil.RETURNURL);
		if (StringUtil.isEmpty(returnUrl)) {
			returnUrl = RequestUtil.getPrePage(request);
			sb.append(RequestUtil.RETURNURL + "=" + returnUrl + "&");
		}

		String rtn = sb.toString();
		if (rtn.length() > 0)
			rtn = rtn.substring(0, rtn.length() - 1);

		return rtn;

	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public void setCurTab(String tab) {
		this.curTab = tab;
	}

	public void setHideTabs(String hideTabs) {
		this.hideTabs = hideTabs;
	}

	public static void main(String[] args) {
		Date a = new Date();
		System.out.println(a.getTime());
	}
}