package com.cssrc.ibms.dp.formvalidate.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.dp.formvalidate.inf.IFormValidate;

@Service
public class ComplexFormValidateService extends IFormValidate{
	/**
	 * 最复杂的表单模板-极简 页眉 页脚 标准四类模板的混合
	 * 1.需要先按照标记 进行拆解，拆解成单个的前四类模板
	 * 2.针对拆解出的多个单模板进行逐个校验
	 * 复杂表：必须标记出所有的表头 页眉 页脚
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> specialValidate(Map<String, Object> msg) {
		List<Element> tables=(List<Element>) msg.get("tables");
		msg.put("tableStrs", new ArrayList<String>());
		//获取所有的行
		List<Element> trs = tables.get(0).selectNodes(".//tr");
		String start="";
		int startIndex=0;
		int lastIndex=-1;
		for(int i=0;i<trs.size();i++){
			Element tr=trs.get(i);
			if(tr.attribute("type") != null){
				String type=tr.attribute("type").getValue();
				if(StringUtil.isNotEmpty(start)){
					//之前的类别为表头且当前类别为页眉或表头  则为一个table
					if((type.equals(TableReduction.front)||type.equals(TableReduction.title))&&start.equals(TableReduction.title)){
						//当前index-1为上一个table的结尾
						lastIndex=i-1;
						getSubTable(trs,startIndex,lastIndex,msg);
						if(validateMsg(msg)){
							return msg;
						}
						start=type;
						startIndex=i;
					}
					else if(type.equals(TableReduction.title)){
						start=type;
					}
				}else if(type.equals(TableReduction.front)||type.equals(TableReduction.title)){
					start=type;
					startIndex=i;
				}
				
				
				
			}
		}
		getSubTable(trs,startIndex,trs.size()-1,msg);
		if(validateMsg(msg)){
			return msg;
		}
		//如果校验通过，将所有的单个table 整合到一个html字符串中
		List<String> tableStrs=(List<String>) msg.get("tableStrs");
		StringBuffer html=new StringBuffer("<html>");
		for(String indexStr:tableStrs){
			String[] indexs=indexStr.split("-");
			int i1=Integer.valueOf(indexs[0]);
			int i2=Integer.valueOf(indexs[1]);
			//从i1 到 i2截取这段List 组成Table
			StringBuffer subTableHtml=new StringBuffer("<table width=\"100%\" class=\"layui-table\">");
			for(int i=i1;i<=i2;i++){
				subTableHtml.append(trs.get(i).asXML());
			}
			subTableHtml.append("</table>");
			html.append(subTableHtml);
		}
		html.append("</html>");
		msg.put("html", html.toString());
		return msg;
	}
	/**
	 * 将拆解出的子表进行校验
	 * 首先 进行约简 
	 * 然后判断是否满足一个表的基础条件（包括表头，且表头不能含有合并单元格）
	 * @param trs
	 * @param startIndex
	 * @param lastIndex
	 * @param msg
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getSubTable(List<Element> trs, int startIndex, int lastIndex,Map<String, Object> msg) {
		//进行约简
		TableReduction.reduceTable(trs, startIndex, lastIndex+1);
		//校验是否为一个表
		//获取标题标记的行号
		int titleTr=-1;
		for(int i=startIndex;i<=lastIndex;i++){
			Element tr=trs.get(i);
			if(tr.attribute("type") != null&&tr.attribute("type").getValue().equals(TableReduction.title)){
				 titleTr=i;
				 break;
			}
		}
		if(titleTr==-1){
			String info="组合型表单模板中，每个子表单均需要标记表头，请使用上部的表头标记按钮进行标记或选择其他模板。";
			msg.put("success", "false");
			msg.put("info", info);
			return msg;
		}
		//获取表头行
		Element tr = trs.get(titleTr);
		//校验表头
		validateTitleTr(msg,tr);
		if(validateMsg(msg)){
			return msg;
		}
		//校验成功后，将拆解的首尾位置记录，用于后面生成单独的table
		List<String> tableStrs=(List<String>) msg.get("tableStrs");
		tableStrs.add(startIndex+"-"+lastIndex);
		return msg;
	}
	
}
