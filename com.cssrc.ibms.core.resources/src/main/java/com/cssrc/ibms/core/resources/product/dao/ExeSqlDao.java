package com.cssrc.ibms.core.resources.product.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;

/**
 * @description 八部数据库同步生成压缩包
 * @author fuyong
 * @date 2020年06月03日 下午3:53:26
 * @version V1.0
 */
@Repository
public class ExeSqlDao {
	
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * @Desc 表名查询字段
	 * @param tableName
	 * @return
	 */
	public String getExeSql(String tableName){
		String sql="select COLUMN_NAME from user_tab_columns  where  TABLE_NAME=upper('"+tableName+"'"+")";
		/*List<Map<String,Object>> colonmNameList=new ArrayList<>();  //生成对应表的插入语句
		colonmNameList=jdbcDao.queryForList(sql,null);
		sql="select "+"'INSERT INTO "+tableName+"(";
		String str="";
		String str1="";
		for(int i=0;i<colonmNameList.size();i++) {
			String clonmName=(String) colonmNameList.get(i).get("COLUMN_NAME");
			str+=clonmName+",";
			str1+=" || '''' || "+clonmName+" ||'''' || ',' ";
		}
		str=str.substring(0,str.length()-1);
		str1=str1.substring(0,str1.length()-4);
		sql+=str+") VALUES("+"'"+str1+"');' "+"exesql"+" From "+tableName+" order by ID";
		List<Map<String,Object>> contextList=jdbcDao.queryForList(sql,null);
		String context="";
		for(int i=0;i<contextList.size();i++) {
			context+=contextList.get(i).get("exesql")+"\r\n";
		}*/
		String context="";
		List<Map<String,Object>> data=new ArrayList<>();
		sql="select * from "+tableName;
		data=jdbcDao.queryForList(sql,null);
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><bizdata>");
		for(int i=0;i<data.size();i++) {
			sb.append("<bean>");
			mapToXMLTest2(data.get(i), sb);
			sb.append("</bean>");
		}
		sb.append("</bizdata>");
		sb.append("</xml>");
		context+=sb.toString();
		return context;
	}  
	//把map转换成键值对的形式存入xml
	private static void mapToXMLTest2(Map map, StringBuffer sb) {
		Set set = map.keySet();
		for (Iterator it = set.iterator(); it.hasNext();) {
			String key = (String) it.next();
			Object value = map.get(key);
			if (null == value)
				value = "";
			if (value.getClass().getName().equals("java.util.ArrayList")) {
				ArrayList list = (ArrayList) map.get(key);
				sb.append("<" + key + ">");
				for (int i = 0; i < list.size(); i++) {
					HashMap hm = (HashMap) list.get(i);
					mapToXMLTest2(hm, sb);
				}
				sb.append("</" + key + ">");
 
			} else {
				if (value instanceof HashMap) {
					sb.append("<" + key + ">");
					mapToXMLTest2((HashMap) value, sb);
					sb.append("</" + key + ">");
				} else {
					sb.append("<" + key + ">" + value + "</" + key + ">");
				}
			}
 
		}
	}
	//解析xml
	public List<Map<String,Object>> Dom4JforXML(String tableName,String FilePath){
		List<Map<String,Object>> data=new ArrayList<>();
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(new File(FilePath+".xml"));
			//获取根节点元素对象
			Element root = document.getRootElement();
			List<Element> elementList=root.elements();
			for (Element e : elementList) {
				List<Element> beanElement=e.elements();
				Map<String,Object> map=new HashMap<>();
				for(Element c : beanElement) {
					map.put(c.getName(),c.getText());
					data.add(map);
				}
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
		
	}
	//遍历当前节点下的所有节点
		public void listNodes(Element node){
			System.out.println("当前节点的名称：" + node.getName());
			//首先获取当前节点的所有属性节点
			List<Attribute> list = node.attributes();
			//遍历属性节点
			for(Attribute attribute : list){
				System.out.println("属性"+attribute.getName() +":" + attribute.getValue());
			}
			//如果当前节点内容不为空，则输出
			if(!(node.getTextTrim().equals(""))){
				 System.out.println( node.getName() + "：" + node.getText());  
			}
			//同时迭代当前节点下面的所有子节点
			//使用递归
			Iterator<Element> iterator = node.elementIterator();
			while(iterator.hasNext()){
				Element e = iterator.next();
				listNodes(e);
			}
		}
}


