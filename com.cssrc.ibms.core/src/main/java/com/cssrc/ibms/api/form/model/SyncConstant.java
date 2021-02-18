package com.cssrc.ibms.api.form.model;

import com.cssrc.ibms.core.util.appconf.AppConfigUtil;


public class SyncConstant {
	public static final String id_from = "id_from";//数据源id
	public static final String place_from = "place_from";//数据源地址
	public static final String place_to = "place_to";//数据目标地址
	public static final String id_to = "id_to";//数据目标ID
	public static final String msg_no = "msg_no";//消息号
	
	//表中字段
	public static final String c_id_from = ITableModel.CUSTOMER_COLUMN_PREFIX+"ID_FROM";//数据源id
	public static final String c_place_from = ITableModel.CUSTOMER_COLUMN_PREFIX+"PLACE_FROM";//数据源地址
	public static final String c_place_to = ITableModel.CUSTOMER_COLUMN_PREFIX+"PLACE_TO";//数据目标地址
	public static final String c_id_to = ITableModel.CUSTOMER_COLUMN_PREFIX+"ID_TO";//数据目标ID
	public static final String c_msg_no = ITableModel.CUSTOMER_COLUMN_PREFIX+"MSG_NO";//消息号
	
	public static final String ForceUpdate = "ForceUpdate";//强制更新标记
	
	public static final String MainTable = "mainTable";//主表

	/**
	 * 同名表后缀
	 * */
	public static final String SYNC_TABLE_SUF = "_sync";
	
	
	
/*	
 * 移动到参数表里
  public static final String LocalWSAddress = AppConfigUtil.get("webservice.syncUrl");
	public static final String WsSyncName = AppConfigUtil.get("webservice.syncName");
	public static final String WsUpdateIDToMethod = AppConfigUtil.get("webservice.updateIdToMethod");
	public static final String WsFeeBackMethod = AppConfigUtil.get("webservice.feedBackMethod");
	public static final String WsRefuseMethod = AppConfigUtil.get("webservice.refuseMethod");
	public static final String ReceiveQueue = AppConfigUtil.get("receive.queue");
*/
	
	public static final String SyncUpByButton = "手动上报>>>";
	public static final String SyncDownByButton = "手动下发>>>";
	
	
	public static final String MsgSplit = "===";
}
