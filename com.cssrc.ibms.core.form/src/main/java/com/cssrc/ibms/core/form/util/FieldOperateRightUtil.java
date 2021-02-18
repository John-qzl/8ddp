package com.cssrc.ibms.core.form.util;

import net.sf.json.JSONObject;

import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.core.form.model.FormRights;

/**
 * Description:用于解析字段控件对应的操作行为权限
 * <p>FieldOperateRightUtil.java</p>
 * @author dengwenjie 
 * @date 2017年8月28日
 */
public class FieldOperateRightUtil {
	public static final String OPERATE_RIGHT = "operate_right";
	public static final String FILE_UPLOAD = "upload";
	public static final String FILE_DOWNLOAD = "download";
	public static final String FILE_DEL = "del";
	public static String FILE_FORMAT = "{%s:%b,%s:%b,%s:%b}";
	
	/**
	 * @param right		: permission处理后的字段权限信息 {b,w,r,rp}
	 * @param permission: 字段权限详细信息 ibms_form_rights的pemission字段
	 * @param rightType : 权限类型[FieldRights,TableRights,TableRelRights,OpinionRights,AttachFileRights,tableGroupRights]
	 * @return 返回此字段对应操作按钮权限，没有则返回none
	 */
	public static String addOperateRight(String right,JSONObject permission,short rightType){
		boolean canAdd = canAdd(permission,rightType);
		if(!canAdd){
			return "none";
		}
		int clt = JSONObject.fromObject(permission.get("operate")).getInt("clt");
		switch(clt){
		case IFieldPool.ATTACHEMENT:  //添加文件字段对应的权限信息
			right = addFileRight(right,permission);
			break;
		default :			
			break;
		}
		return right;
	}
	
	public static String getLink(String html,String right){
		try{
			JSONObject operate = JSONObject.fromObject(right.replaceFirst(".*@", ""));
			boolean canUpload =  operate.getBoolean(FILE_UPLOAD);
			//boolean canDownload =  operate.getBoolean(FILE_DOWNLOAD);
			//boolean canDel =  operate.getBoolean(FILE_DEL);
			if(html.contains("selectFile")){			
				String operateAttr = "operateright=\'"+operate.toString().replaceAll("\"", "\\\"")+"\'";
				html = html.replaceAll("\\<a", "\\<a  " +operateAttr);
			}
			
			return html;
		}catch(Exception e){
			return html;
		}
	}
	
	private static String addFileRight(String right,JSONObject permission){
		JSONObject operate = JSONObject.fromObject(permission.get("operate"));
		boolean canUpload =  operate.getBoolean(FILE_UPLOAD);
		boolean canDownload =  operate.getBoolean(FILE_DOWNLOAD);
		boolean canDel =  operate.getBoolean(FILE_DEL);
		String operateRight = "";
		if(right.equals("b")){//required
			operateRight=String.format(FILE_FORMAT,FILE_UPLOAD, canUpload,FILE_DOWNLOAD,canDownload,FILE_DEL,canDel);
		}else if(right.equals("w")){//write
			operateRight=String.format(FILE_FORMAT,FILE_UPLOAD, canUpload,FILE_DOWNLOAD,canDownload,FILE_DEL,canDel);
		}else if(right.equals("rp")){//readPost
			operateRight=String.format(FILE_FORMAT,FILE_UPLOAD, false,FILE_DOWNLOAD,canDownload,FILE_DEL,false);
		}else{//read
			operateRight=String.format(FILE_FORMAT,FILE_UPLOAD, false,FILE_DOWNLOAD,canDownload,FILE_DEL,false);
		}
		return operateRight;
	}
	private static boolean canAdd(JSONObject permission,short rightType){
		boolean canAdd  = false;
		switch (rightType) {
		case FormRights.FieldRights:
			canAdd = canAdd(permission);
			break;
		case FormRights.TableRights:
			canAdd = canAdd(permission);
			break;
		case FormRights.TableRelRights:
			canAdd = canAdd(permission);
			break;
		case FormRights.OpinionRights:
			break;
		case FormRights.AttachFileRights:
			break;	
		case FormRights.tableGroupRights:
            break;  
        }
		return canAdd;
	}
	/**
	 * @param permission : permission有operate属性，且operate对象中有控件属性clt
	 * @return 
	 */
	private static boolean canAdd(JSONObject permission){
		try{
			JSONObject operate = JSONObject.fromObject(permission.get("operate"));
			operate.getInt("clt");
			return true;			
		}catch(Exception e){
			return false;
		}
	}
}
