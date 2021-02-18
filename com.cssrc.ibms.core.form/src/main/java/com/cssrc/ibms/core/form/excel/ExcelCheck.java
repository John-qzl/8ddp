package com.cssrc.ibms.core.form.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.form.model.FormData;
import com.cssrc.ibms.core.form.model.FormField;
import com.cssrc.ibms.core.form.model.PkValue;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.date.DateUtil;

public class ExcelCheck {
	
	public static final short REQUIRE_CHECK = 0;
	//名称转换ID
	public static final short NAMEID_CHECK = 1;
	//日期校验
	public static final short TIME_CHECK = 2;
	
	private List<FormField> formFieldList;
	private TableEntity tableEntity;
	StringBuffer checkInfo = new StringBuffer();
	
	public ExcelCheck(StringBuffer sb,List<FormField> formFieldList,TableEntity tableEntity){
		this.checkInfo = sb; 
		this.formFieldList = formFieldList;
		this.tableEntity = tableEntity;
	}
	
	public StringBuffer check(short type){
		switch(type){
		case  ExcelCheck.REQUIRE_CHECK:
			requeirCheck(); 
			break;
		case  ExcelCheck.NAMEID_CHECK:
			idConvert(); 
			break;
		case  ExcelCheck.TIME_CHECK:
			dateCheck();
			break;
		default:
	         ;
		}
		return checkInfo;
	}
	/**
	 * 必填校验
	 */
	private void requeirCheck(){
		String needRequiedName = ",";
		for(FormField field : formFieldList){
			boolean required = field.getIsRequired()==1;
			if(required){
				needRequiedName += field.getFieldDesc().replaceAll("\\s", "")+",";
			}
		}
			
		List<DataEntity> dataList = tableEntity.getDataEntityList();
		int rowNum = 2;
		for(DataEntity data : dataList){
			int lineNum = 1;
			List<FieldEntity> fields = data.getFieldEntityList();
			for(FieldEntity field : fields){
				String filedName = ","+field.getName().replaceAll("\\s", "")+",";
				boolean needReq = needRequiedName.contains(filedName);
				if(needReq){
					String value = CommonTools.Obj2String(field.getValue()).replaceAll("\\s", "");
					if(value.equals("")){
						if(!checkInfo.toString().contains("必填校验结果如下：")){
							checkInfo.append("---------------------必填校验结果如下：-------------------------\r\n");
						}
						checkInfo.append("第").append(rowNum).append("行").append("第").append(lineNum)
						.append("列（列名为"+field.getName()+"）,不能为空！\r\n");
					}
				}
				lineNum ++;
			}
			rowNum ++;
		}
	}
	
	//由人名得出其对应的ID
	private void idConvert(){	
		String singlePerson = ",";
		String belongOrg = ",";
		for(FormField field : formFieldList){
			short ctype = field.getControlType();		
			if(ctype==4){//若为人员选择器，进行校验
				singlePerson += field.getFieldDesc().replaceAll("\\s", "")+",";
			}
			if(ctype==18){//若为组织选择器，进行校验
				belongOrg += field.getFieldDesc().replaceAll("\\s", "")+",";
			}
		}
		List<DataEntity> dataList = tableEntity.getDataEntityList();
		//从第二行第一列开始循环，找到需要进行名称ID转换的列
		int rowNum = 2;
		for(DataEntity data : dataList){
			int lineNum = 1;
			List<FieldEntity> fields = data.getFieldEntityList();
			List<FieldEntity> extField = new ArrayList(); 
			for(FieldEntity field : fields){
				String filedName = ","+field.getName().replaceAll("\\s", "")+",";
				boolean hasPerson= singlePerson.contains(filedName);
				if(hasPerson){
					String value = CommonTools.Obj2String(field.getValue()).replaceAll("\\s", "");
					ISysUserService sysUserService=(ISysUserService)AppUtil.getBean(ISysUserService.class);
					List<? extends ISysUser> list = sysUserService.getByFullname(value);
					if(BeanUtils.isEmpty(list)){//list为空，人名不存在
						checkInfo.append("---------------------人名校验结果如下：-------------------------\r\n");
						checkInfo.append("第").append(rowNum).append("行").append("第").append(lineNum)
						.append("列（人名:"+value+"）,不存在！\r\n");
						
					}else{//list不为空，人名存在，取出对应的ID；取第一个（重名的情况）
							Long userId = ((ISysUser)list.get(0)).getUserId();
							FieldEntity userIdField  = new FieldEntity();
							userIdField.setIsKey(FieldEntity.NOT_KEY);
							userIdField.setName(filedName.replaceAll(",", "")+"ID");
							userIdField.setValue(String.valueOf(userId));
							extField.add(userIdField);						
					}	
				}
				lineNum ++;
			}
			fields.addAll(extField);
			rowNum ++;
		}
		
		//名称转换ID，根据名称得到对应的ID
		for(DataEntity data : dataList){
			int lineNum = 1;
			List<FieldEntity> areas = data.getFieldEntityList();
			List<FieldEntity> extArea = new ArrayList(); 
			for(FieldEntity area : areas){
				String areaName = ","+area.getName().replaceAll("\\s", "")+",";
				boolean hasOrg= belongOrg.contains(areaName);
				if(hasOrg){
					String value = CommonTools.Obj2String(area.getValue()).replaceAll("\\s", "");
					ISysOrgService sysOrgService=(ISysOrgService)AppUtil.getBean(ISysOrgService.class);
					List<? extends ISysOrg> list = sysOrgService.getByOrgName(value);
					if(BeanUtils.isEmpty(list)){
						checkInfo.append("---------------------名称校验结果如下：-------------------------\r\n");
						checkInfo.append("第").append(rowNum).append("行").append("第").append(lineNum)
						.append("列（名称:"+value+"）,不存在！\r\n");
						
					}else{
							Long orgId = ((ISysOrg)list.get(0)).getOrgId();
							FieldEntity orgIdField  = new FieldEntity();
							orgIdField.setIsKey(FieldEntity.NOT_KEY);
							orgIdField.setName(areaName.replaceAll(",", "")+"ID");
							orgIdField.setValue(String.valueOf(orgId));
							extArea.add(orgIdField);						
					}	
				}
				lineNum ++;
			}
			areas.addAll(extArea);
			rowNum ++;
		}	
	}
	
	/**
	 * 日期校验
	 */
	private void dateCheck(){
		//1.取出需要校验的列（字段类型为日期）
		List<FormField> needCheckFields = new ArrayList();
		for(FormField field : formFieldList){
			String ftype = field.getFieldType();
			if(ftype.equals("date")){//若字段类型为日期，进行校验
				needCheckFields.add(field);
			}
		}
		//2.循环Excel表头
		List<DataEntity> dataList = tableEntity.getDataEntityList();
		int rowNum = 2;
		for(DataEntity data : dataList){
			int lineNum = 1;
			List<FieldEntity> fields = data.getFieldEntityList();
			for(FieldEntity field : fields){
				String filedName = field.getName().replaceAll("\\s", "");
				//3.将Excel表头与需要校验的所有列名进行匹对
				FormField field_t = needCheckField(filedName,needCheckFields);
				if(field_t!=null){
					String value = CommonTools.Obj2String(field.getValue()).replaceAll("\\s", "");
					String ctl = field_t.getCtlProperty();
					JSONObject obj = JSONObject.fromObject(ctl);
					//4.具体情况校验
					if(BeanUtils.isNotEmpty(obj)){
						String formatStr = obj.getString("format");
						String dealDate = isValidDate(value,formatStr);
						if(dealDate==null){
							checkInfo.append("---日期格式必须为yyyy-mm-dd---");
						}else{
							if(formatStr.equalsIgnoreCase("yyyy")){
								field.setValue(dealDate+"-01-01");
							}
						}
					}
					lineNum ++;
			}
			rowNum ++;
			}
		}
	}
	
	private String isValidDate(String date,String formatStr){
		String dealDate = date;
		try{
			DateFormat formatter = new SimpleDateFormat(formatStr);
			formatter.parse(date);
			Date time =  (Date)formatter.parse(date);
			dealDate = formatter.format(time);
			return dealDate;
		}catch(Exception e){
			return null;
		}
	}
	
	//Excel表头与需要校验的所有列名进行匹对的方法，并返回存在需要校验的列
	private FormField needCheckField(String lineName,List<FormField> fields){
		if(BeanUtils.isEmpty(fields)){
			return null;
		}
		FormField result = null ;
		for(FormField field : fields){
			String name = field.getFieldDesc();
			if(name.trim().equals(lineName)){
				result = field;
				break;
			}
		}
		return result;
	}
	
	public void getImportInfo(FormData bpmFormData,DataEntity dataEntity,int rowNum,StringBuffer sb){
		if(!sb.toString().contains("导入结果如下：")){
			sb.append("---------------------导入结果如下：-------------------------\r\n");
		}
		PkValue pkValue = bpmFormData.getPkValue();
		if(pkValue.getIsAdd()){
			sb.append("第").append(rowNum+2).append("行记录插入成功！\r\n");
		}else{
			sb.append("第").append(rowNum+2).append("行记录更新成功！   ");
			sb.append(dataEntity.getPkName()+":").append(dataEntity.getPkVal())
			.append("与数据库中id=").append(pkValue.getValue()).append("的记录相同！\r\n");
		}
	}

	/**
	 * 导出校验结果信息
	 * @param info
	 * @param fileName
	 * @param response
	 * @throws IOException
	 */
	public static void exportCheckInfo(StringBuffer info,String fileName,HttpServletResponse response) throws IOException{
		String time = DateUtil.getDateString(new Date(), "yyyyMMddHHmmss");
		response.setContentType("application/txt");
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName+time, "UTF-8")+".txt");
		OutputStream os = null;
		try {
			byte[] bs = info.toString().getBytes();
			os = response.getOutputStream();
			os.write(bs);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null)
				os.close();
		}
	}
}
