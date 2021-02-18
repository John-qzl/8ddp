package com.cssrc.ibms.core.util.dbom;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sf.json.JSONArray;




public class JsonTree {

	public static String generate(List<?> dataList, String[] otherNode,
			Boolean sort) {
		StringBuffer result = new StringBuffer("[");
		HashMap<String, Node> nodeList = construction(dataList, otherNode);
		List<Node> rootNodeList = new ArrayList<Node>();
		Set<Entry<String, Node>> set = nodeList.entrySet();
		Node node;
		for (Iterator<Entry<String, Node>> it = set.iterator(); it.hasNext();) {
			node = it.next().getValue();
			if ((node.parentId == null) || node.parentId.equals("")||node.parentId.equals("0")||node.parentId.equals("-1")) {
				rootNodeList.add(node);
			} else if (nodeList.get(node.parentId) != null) {
				nodeList.get(node.parentId).children.addChild(node);
			}
		}
		if (sort.booleanValue()) {
			Collections.sort(rootNodeList, new NodeSnComparator());
		}
		for (Node rootNode : rootNodeList) {
			if (sort.booleanValue())
				rootNode.sortSnChildren();
			else {
				rootNode.sortChildren();
			}
			result.append(rootNode.toString()).append(",");
		}
		if (!rootNodeList.isEmpty()) {
			result.deleteCharAt(result.length() - 1);
		}
		result.append("]");
		return result.toString();
	}
	
	public static JSONArray generateNodes(List<?> dataList, String[] otherNode,
			Boolean sort) {
		return JSONArray.fromObject(JsonTree.generate(dataList, otherNode, sort)); 
	}

	
	public static TreeNode beanToTree(ITreeNode obj) {
		TreeNode tree = new TreeNode();
		Map<String, String> propMap = obj.getRelation();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : propertyDescriptors) {
				Object result = pd.getReadMethod().invoke(obj, new Object[0]);
				if(null==result&&(!pd.getName().equalsIgnoreCase(propMap.get("midIcon"))&&!pd.getName().equalsIgnoreCase(propMap.get("bigIcon")))){
					continue;
				}
				if (pd.getName().equalsIgnoreCase(propMap.get("id"))) {
					tree.setId(result.toString());
				} else if (pd.getName().equalsIgnoreCase(propMap.get("text"))) {
					tree.setText(result.toString());
				} else if (pd.getName().equalsIgnoreCase(propMap.get("url"))) {
					tree.setUrl(result.toString());
				} else if (pd.getName().equalsIgnoreCase(propMap.get("iconCls"))) {
					tree.setIconCls(result.toString());
					tree.setIconSkin(result.toString());
				} else if (pd.getName().equalsIgnoreCase(propMap.get("midIcon"))) {
					tree.setMidIcon(result==null?"":result.toString());
				} else if (pd.getName().equalsIgnoreCase(propMap.get("bigIcon"))) {
					tree.setBigIcon(result==null?"":result.toString());
				} else if (pd.getName().equalsIgnoreCase(propMap.get("code"))) { //节点代号
					tree.setCode(result==null?"":result.toString());
				} else if (pd.getName().equalsIgnoreCase(propMap.get("type"))) { //节点类型
					tree.setType(result==null?"":result.toString());
				} else if (pd.getName().equalsIgnoreCase(propMap.get("children"))) {
					if (null != result) {
						Collection<ITreeNode> list = (Collection<ITreeNode>) result;
						for (ITreeNode o : list) {
							tree.getChildren().add(beanToTree(o));
						}
						if(list.size()>0){
							tree.setLeaf(false);
						}
					}
				}
			} 
		} catch (Exception e) { 
			e.printStackTrace();
		}

		return tree;
	}
	
	@SuppressWarnings("unchecked")
	private static HashMap<String, Node> construction(List<?> dataList,
			String[] otherNode) {
		HashMap<String, Node> nodeMap = new HashMap<String, Node>();
		for (Iterator it = dataList.iterator(); it.hasNext();) {
			Map dataRecord = (Map) it.next();
			Node node = new Node();
			node.id = ((String) dataRecord.get("id"));
			node.text = ((String) dataRecord.get("text"));
			if(null!=dataRecord.get("iconCls")){
				node.iconCls = ((String) dataRecord.get("iconCls"));
			}else{
				node.iconCls = "default";
			}
			
			node.parentId = ((String) dataRecord.get("parentId"));
			node.sn = ((String) dataRecord.get("sn"));
			if ((otherNode != null) && (otherNode.length > 0)) {
				for (String key : otherNode) {
					Map map = new HashMap();
					map.put("key", key);
					map.put("value", dataRecord.get(key) == null ? ""
							: dataRecord.get(key));
					node.otherList.add(map);
				}
			}
			nodeMap.put(node.id, node);
		}
		return nodeMap;
	}
	
	public static TreeNode beansToTree(List<ITreeNode> objList,boolean hasRoot) throws Exception{
		Map<String,TreeNode> treeMap = new HashMap<String,TreeNode>();
		List<TreeNode> nodeList = new ArrayList<TreeNode>();
		for(ITreeNode obj:objList){
			TreeNode node = new TreeNode();
			Map<String, String> propMap = obj.getRelation();
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor pd : propertyDescriptors) {
				Object result = pd.getReadMethod().invoke(obj, new Object[0]);
				if(null==result){
					continue;
				}
				if (pd.getName().equalsIgnoreCase(propMap.get("id"))) {
					node.setId(result.toString());
				} else if (pd.getName().equalsIgnoreCase(propMap.get("text"))) {
					node.setText(result.toString());
				}else if (pd.getName().equalsIgnoreCase(propMap.get("parentId"))) {
					node.setParentId(result.toString());
				}else if (pd.getName().equalsIgnoreCase(propMap.get("systemNode"))) {
					node.setSystemNode(result.toString());
				} else if (pd.getName().equalsIgnoreCase(propMap.get("iconCls"))) {
					node.setIconCls(result.toString());
				}       
			} 
			treeMap.put(node.getId(), node);
		}
		for(Map.Entry<String, TreeNode> entry:treeMap.entrySet()){
			if(treeMap.containsKey(entry.getValue().getParentId())){
				treeMap.get(entry.getValue().getParentId()).getChildren().add(entry.getValue());
				treeMap.get(entry.getValue().getParentId()).setLeaf(false);
			}else{
				nodeList.add(entry.getValue());
			}
		} 
		if(!hasRoot){
			TreeNode node = new TreeNode();
			node.setId("0");
			node.setText("总分类");		
			node.getChildren().addAll(nodeList); 
			if(nodeList.size()>0){
				node.setLeaf(false);
			}
			return node;
		} 
		
		if(nodeList.size()==1){
			return nodeList.get(0);
		}
		return null;
	}
	
	public static TreeNode beansToTree(List<ITreeNode> objList) throws Exception{
		return beansToTree(objList,false);
	}


}
