package com.cssrc.ibms.migration.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cssrc.ibms.api.migration.model.IOutField;
import com.cssrc.ibms.api.migration.model.IOutTable;
import com.cssrc.ibms.core.util.common.PinyinUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.migration.model.MigrationConfigure;
import com.cssrc.ibms.migration.model.tdm.Schema;
import com.cssrc.ibms.migration.model.tdm.restriction.Enum;
import com.cssrc.ibms.migration.model.tdm.restriction.SimpleRestriciton;
import com.cssrc.ibms.migration.model.tdm.table.RelationColumn;
import com.cssrc.ibms.migration.model.tdm.table.SimpleColumn;
import com.cssrc.ibms.migration.model.tdm.table.Table;
public class TDMUtil {
	public static String schemaXmlDeal(String xml,MigrationConfigure config,StringBuffer log){	
		//<schema:数据模型 xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns:schema="http://schema.ecore" 数据模型名称="电气本部QMS" 版本号="1.0" 密级设置="机密,秘密,内部,非密" 类型="普通模型">
		xml = xml.replaceAll(":数据模型", " "); 
		xml = xml.replaceAll("xmi:", " ");
		xml = xml.replaceAll("xmlns:", " ");
		log.append("转换成功！\r\n");
		//没有属性数据模型别名则添加 --》根据导入的文件名
		if(!xml.contains("数据模型别名")){
			String schemaAlias = config.getSchemaFile().getOriginalFilename().replace(".schema", "");
			String alias = "数据模型别名=\""+PinyinUtil.getPinYinHeadChar(schemaAlias)+"\"";
			xml = xml.replaceAll("版本号=", alias+" 版本号=");
		}
		return xml;
	}	
	
	public static void  transform(Schema schema,StringBuffer log){	
		List<Table> tables = schema.getTables();		
		List<SimpleRestriciton> res =schema.getRestriction().getSimpleRestriction();
		int tablelogNum = 1;
		//1.表对象调整
		for(Iterator it = tables.iterator();it.hasNext();){
			Table table  = (Table)it.next();
			//表属性调整
			table.setDisplayName(schema.getName()+"-"+table.getDisplayName());
			table.setTableName(table.getTableName()+"_"+schema.getAlias());
			
			log.append(LogUtil.getLS(1, tablelogNum)).append(table.getTableDesc()).append("\r\n");
			
			List<SimpleColumn> cols = table.getCwmTabColumnses();
			List<RelationColumn> relCols = table.getCwmRelationColumnses();
			List<RelationColumn> removeCols = new ArrayList<RelationColumn>();
			Integer colNum = 0;
			for(Iterator it2 = cols.iterator();it2.hasNext();){		
				SimpleColumn simpleColumn  = (SimpleColumn)it2.next();
				log.append(LogUtil.getLS(2, colNum+1)).append(simpleColumn.getDisplayName()).append("\r\n");
				
				//a1.将属性约束	//@数据约束/@枚举约束.63-------》E_ada
				String new_res = restrictionDeal(simpleColumn,res);
				if(!new_res.equals("")){
					log.append(LogUtil.getLS(3, 0))
					.append("属性约束转换："+simpleColumn.getRestriction()).append(LogUtil.ARROW).append(new_res).append("\r\n");
				}
				
				//a2.字段顺序属性设置
				simpleColumn.setSn(colNum);
				
				//a3.附件字段属性设置
				String att_rtn = attachMentDeal(simpleColumn);
				if(!att_rtn.equals("")){
					log.append(LogUtil.getLS(3, 0))
					.append("附件字段属性设置："+simpleColumn.getDisplayName()).append("\r\n");
				}
				//a4.流程字段属性设置
				String flowstate_rtn = flowStateDeal(simpleColumn);
				if(!flowstate_rtn.equals("")){
					log.append(LogUtil.getLS(3, 0))
					.append("流程字段属性设置："+simpleColumn.getDisplayName()).append("\r\n");
				}
				
				colNum++;
			}
			//a4.字段主键显示值设置
			String pk_string = isPkShowDeal(table,cols);
			if(!pk_string.equals("")){
				log.append(LogUtil.getLS(3, 0))
				.append("字段主键显示值设置："+pk_string).append("\r\n");
			}
			int relLogNum = 1;
			for(Iterator it3 = relCols.iterator();it3.hasNext();){				
				RelationColumn relColumn  = (RelationColumn)it3.next();
				//b1.移除被引用的关系字段
				if(relColumn.getIsNotFk().equals("True")){
					removeCols.add(relColumn);
				}else{
					log.append(LogUtil.getLS(2, colNum+1+relLogNum)).append(relColumn.getDisplayName()).append("\r\n");
					//b2.判断是否为人员或部门选择器
					String selectorRtn = isSelector(relColumn,tables);
					if(!selectorRtn.equals("")){
						log.append(LogUtil.getLS(3, 0))
						.append("人员或组织选择器设置："+selectorRtn).append("\r\n");
					}
					
					relLogNum++;
				}				
			}
			relCols.removeAll(removeCols);
			//b3.移除后，关系字段顺序属性设置
			for(Iterator it4 = relCols.iterator();it4.hasNext();){				
				RelationColumn relColumn  = (RelationColumn)it4.next();
				relColumn.setSn(colNum);	
				colNum++;
			}
			tablelogNum++;
		}
		
		//2.属性约束调整
		int resLogNum = 0;
		
		for(Iterator it5 = res.iterator();it5.hasNext(); ){
			SimpleRestriciton simple = (SimpleRestriciton)it5.next();
			//c1.枚举约束顺序属性设置
			simple.setSn(Long.valueOf(resLogNum));
			int enumLogNum = 0;
			for(Iterator it6 = simple.getEnums().iterator();it6.hasNext(); ){
				Enum em = (Enum)it6.next();
				em.setSn(Long.valueOf(enumLogNum));
				enumLogNum++;
			}
			resLogNum ++;
		}		
		
	}
	private static String restrictionDeal(SimpleColumn simpleColumn,List<SimpleRestriciton> res){
		String rtn = "";
		String restriction = simpleColumn.getRestriction();
		if(restriction.contains("@枚举约束.")){
			String ss = restriction.substring(restriction.indexOf("@枚举约束."),restriction.length());
			ss = ss.replace("@枚举约束.", "");
			int pos = Integer.parseInt(ss);
			if(res==null||res.size()<pos+1){
				rtn = "找不到对应的枚举约束！";
			}else{
				simpleColumn.setRestriction(res.get(pos).getName());
				rtn = res.get(pos).getName();
			}
		}else{
			simpleColumn.setRestriction("");
		}
		return rtn;
	}
	private static String isPkShowDeal(Table table,List<SimpleColumn> cols){
		String pkShow = table.getPkDisplay();
		String rtn = "";
		if(pkShow!=null&&pkShow.contains("/@普通属性.")){
			String ss = pkShow.substring(pkShow.indexOf("/@普通属性."),pkShow.length());
			ss = ss.replace("/@普通属性.", "");
			int pos = Integer.parseInt(ss);
			cols.get(pos).setPkShow(true);
			rtn = cols.get(pos).getDisplayName();
		}
		return rtn;
		
	}
	public static String attachMentDeal(SimpleColumn simpleColumn){
		String rtn = "";
		String rules = IOutField.FILEUPLOAD_FIELD_KEY;
		if(ruleCheck(rules,simpleColumn)){
			simpleColumn.setAttachCol(true);
			rtn = simpleColumn.getDisplayName();
		}
		return rtn;
	}
	public static String flowStateDeal(SimpleColumn simpleColumn){
		String rtn = "";
		String rules = IOutField.PROCESS_FIELD_KEY;
		if(ruleCheck(rules,simpleColumn)){
			simpleColumn.setFlowState(true);
			rtn = simpleColumn.getDisplayName();
		}
		return rtn;
	}
	public static String isSelector(RelationColumn relColumn,List<Table> tables){
		String rtn = "";
		String retablePos = relColumn.getRelTable();
		String ss = retablePos.substring(retablePos.indexOf("@数据类/@children."),retablePos.length());
		ss = ss.replace("@数据类/@children.", "");
		int pos = Integer.parseInt(ss);
		if(tables==null||tables.size()<pos+1){
			return "找不到对应的关系表("+retablePos+")！";
		}
		String tablename = tables.get(pos).getTableName();
		if(tablename.startsWith(IOutTable.USER_TABLE)){
			if(relColumn.getRelationType().equals("多对多")){
				relColumn.setMultiUserSelector(true);
				rtn = relColumn.getDisplayName()+"为人员选择器（多选）";
			}else{
				relColumn.setSingleUserSelector(true);
				rtn = relColumn.getDisplayName()+"为人员选择器（单选）";
			}
		}else if(tablename.startsWith(IOutTable.DEPT_TABLE)){
			if(relColumn.getRelationType().equals("多对多")){
				relColumn.setMultiDeptSelector(true);
				rtn = relColumn.getDisplayName()+"为组织选择器（多选）";
			}else{
				relColumn.setSingleDeptSelector(true);
				rtn = relColumn.getDisplayName()+"为组织选择器（单选）";
			}
		}
		return rtn;
	}
	private static boolean ruleCheck(String rules,SimpleColumn simpleColumn){
		boolean flag = false;
		JSONArray ruleArr = JSONArray.fromObject(rules);
		for(Iterator it = ruleArr.iterator();it.hasNext();){
			JSONObject rule = (JSONObject)it.next(); 
			String rule_att = rule.getString("key");
			String rule_val = rule.getString("value");	
			String value = "";
			if(rule_att.equals("name")){
				value = simpleColumn.getName();
			}else if(rule_att.equals("displayName")){
				value = simpleColumn.getDisplayName();
			}else{
				continue;
			}
			if(value.toUpperCase().contains(rule_val)){
				flag = true;
				break;
			}
		}
		return flag;
	}
}
