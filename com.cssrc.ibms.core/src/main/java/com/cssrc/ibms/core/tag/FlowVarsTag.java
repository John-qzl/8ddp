package com.cssrc.ibms.core.tag;


import java.util.List;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.cssrc.ibms.api.activity.intf.IDefVarService;
import com.cssrc.ibms.api.activity.model.IDefVar;
import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
/**
 * 流程变量标签。
 * <pre>
 * 功能获取与流程绑定表单的流程变量列表。
 * 权限标签的写法:&lt;f:flowVar defId="${defId}" controlName="控件名称" change="change(this);"/>
 * </pre>
 */
public class FlowVarsTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long defId=0L;
	
	private String controlName="flowVars";
	private String change="";
	private String parentActDefId = "";
	
	/**
	 * change事件。
	 * @param change
	 */
	public void setChange(String change) {
		this.change = change;
	}

	/**
	 * 设置流程定义ID
	 * @param defId
	 */
	public void setDefId(Long defId) {
		this.defId = defId;
	}
	
	/**
	 * 下拉框控件的name属性。
	 * @param controlName
	 */
	public void setControlName(String controlName) {
		this.controlName = controlName;
	}

	public int doStartTag() throws JspTagException {
		return EVAL_BODY_BUFFERED;
	}
	
	/**
	 * 根据流程定义ID获取流程变量。
	 * @return
	 */
	private String getFlowVars(){
		if(defId==0){
			return "";
		}
		IFormFieldService bpmFormFieldService=(IFormFieldService)AppUtil.getBean(IFormFieldService.class);
		IDefVarService bpmDefVarService=(IDefVarService)AppUtil.getBean(IDefVarService.class);
		List<?extends IFormField> fieldList= bpmFormFieldService.getFlowVarByFlowDefId(defId);
		List<?extends IDefVar> bpmdefVars= bpmDefVarService.getVarsByFlowDefId(defId);
		StringBuffer sb=new StringBuffer();
		sb.append("<select name='"+this.controlName+"'" );
		if(StringUtil.isNotEmpty(change)){
			sb.append(" onchange=\"" + this.change+ "\"");
		}
		sb.append(">");
		sb.append("<option value=''>请选择</option>");
		for(IFormField field:fieldList){
				sb.append("<option value='"+field.getFieldName()+"'>"+field.getFieldDesc()+"</option>");
		}
		for (IDefVar defVar:bpmdefVars) {
			if (defVar!=null) {
				sb.append("<option value='"+defVar.getVarKey()+"'>"+defVar.getVarName()+"</option>");
			}
		}
		sb.append("</select>");
		return sb.toString();
	}
	
	
	
	public int doEndTag() throws JspTagException {

		try {
			String str = getFlowVars();
			pageContext.getOut().print(str);
		} catch (Exception e) {
			throw new JspTagException(e.getMessage());
		}
		return SKIP_BODY;
	}

	public String getParentActDefId() {
		return parentActDefId;
	}

	public void setParentActDefId(String parentActDefId) {
		this.parentActDefId = parentActDefId;
	}

}
