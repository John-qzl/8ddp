package com.cssrc.ibms.core.resources.mission.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cssrc.ibms.core.excel.reader.DataEntity;
import com.cssrc.ibms.core.excel.reader.FieldEntity;
import com.cssrc.ibms.core.excel.reader.TableEntity;
import com.cssrc.ibms.core.resources.mission.dao.RangeMissionDao;
import com.cssrc.ibms.core.resources.product.dao.ModuleDao;
import com.cssrc.ibms.core.resources.product.util.ExcelReadUtil;
import com.cssrc.ibms.core.user.dao.SysOrgDao;
import com.cssrc.ibms.core.user.dao.SysUserDao;
import com.cssrc.ibms.core.util.result.ResultMessage;
/**
 * 任务解析类
 * @author zmz
 *
 */
@Service
public class MissionService {
	@Resource
	private SysOrgDao sysOrgDao;
	@Resource
	private SysUserDao sysUserDao;
	@Resource
	private RangeMissionDao dao;
	@Resource
	private ModuleDao moduleDao;
	
	/**
	 * @Desc 导入任务 - 获取excel中的内容
	 * @param file
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public ResultMessage importMissions(MultipartFile file, String moduleId) throws Exception {
		
		ResultMessage resultMessage = new ResultMessage(ResultMessage.Success, "任务导入成功");
		// 用于记录导出校验信息
		StringBuffer importInfo = new StringBuffer();
		// 读出execl
		TableEntity tableEntity = ExcelReadUtil.readFile(file);
		// 校验excel信息
		importInfo = checkProductCategory(tableEntity, importInfo);
		if (!importInfo.toString().equals("")) {
			resultMessage = new ResultMessage(ResultMessage.Fail, importInfo.toString());
			return resultMessage;
		}
		if (tableEntity.getDataEntityList().size() == 0) {
			importInfo.append("此表未有数据导入！\r\n");
			resultMessage = new ResultMessage(ResultMessage.Fail, importInfo.toString());
		}
		this.addProductCategory(tableEntity, importInfo, resultMessage, moduleId);
		return resultMessage;
	}
	
	/**
	 * @Desc 校验任务导入的必填信息
	 * @param tableEntity
	 * @param importInfo
	 * @return
	 */
	private StringBuffer checkProductCategory(TableEntity tableEntity, StringBuffer importInfo) {
		//从第二行开始读数据,因为第一行是表头
		int rowNum = 2;
		for (DataEntity dataEntity : tableEntity.getDataEntityList()) {
			String fieldName = "";
			List<FieldEntity> list = dataEntity.getFieldEntityList();
			// 对字段名称进行遍历
			for (FieldEntity fieldEntity : list) {
				String name = fieldEntity.getName();
				String value = fieldEntity.getValue();
				fieldName += name + ",";
				if ("序号".equals(name) 
						|| "型号代号".equals(name) 
						|| "任务名称".equals(name) 
						|| "武器系统编号".equals(name) 
						|| "导弹编号".equals(name)
						|| "所检地点".equals(name) 
						|| "开始时间".equals(name) 
						|| "结束时间".equals(name)
						|| "试验负责人".equals(name) ) {
					
				} else {
					return importInfo.append("列名不正确，请确保列名为以下其中一个：【序号】、【型号代号】、【任务名称】、【武器系统编号】、【导弹编号】、【所检地点】、【开始时间】、【结束时间】、【试验负责人】");
				}
				if ("型号代号".equals(name) && "".equals(value)) {
					if (!importInfo.toString().contains("必填校验结果如下：")) {
						importInfo.append("--------必填校验结果如下：--------\r\n");
					}
					return importInfo.append("第").append(rowNum).append("行").append("（列名为" + name + "）,不能为空！\r\n");
				}
				if ("任务名称".equals(name) && "".equals(value)) {
					if (!importInfo.toString().contains("必填校验结果如下：")) {
						importInfo.append("--------必填校验结果如下：--------\r\n");
					}
					return importInfo.append("第").append(rowNum).append("行").append("（列名为" + name + "）,不能为空！\r\n");
				}
				if ("所检地点".equals(name) && "".equals(value)) {
					if (!importInfo.toString().contains("必填校验结果如下：")) {
						importInfo.append("--------必填校验结果如下：--------\r\n");
					}
					return importInfo.append("第").append(rowNum).append("行").append("（列名为" + name + "）,不能为空！\r\n");
				}
				if ("开始时间".equals(name) && "".equals(value)) {
					if (!importInfo.toString().contains("必填校验结果如下：")) {
						importInfo.append("--------必填校验结果如下：--------\r\n");
					}
					return importInfo.append("第").append(rowNum).append("行").append("（列名为" + name + "）,不能为空！\r\n");
				}
				if ("结束时间".equals(name) && "".equals(value)) {
					if (!importInfo.toString().contains("必填校验结果如下：")) {
						importInfo.append("--------必填校验结果如下：--------\r\n");
					}
					return importInfo.append("第").append(rowNum).append("行").append("（列名为" + name + "）,不能为空！\r\n");
				}
				if ("试验负责人".equals(name) && "".equals(value)) {
					if (!importInfo.toString().contains("必填校验结果如下：")) {
						importInfo.append("--------必填校验结果如下：--------\r\n");
					}
					return importInfo.append("第").append(rowNum).append("行").append("（列名为" + name + "）,不能为空！\r\n");
				}
				
				
			}
			rowNum++;
		}
		return importInfo;
	}

	/**
	 * @Desc 向数据库插入数据
	 * @param tableEntity
	 * @param importInfo
	 * @param resultMessage
	 * @param moduleId
	 */
	private void addProductCategory(TableEntity tableEntity, StringBuffer importInfo,ResultMessage resultMessage, String moduleId) {
		int rowNum = 2;
        for (DataEntity dataEntity : tableEntity.getDataEntityList()) {
            List<FieldEntity> list = dataEntity.getFieldEntityList();
            Map<String, Object> missionInfo = new HashMap<>();
            // 对字段数据进行遍历
            for (FieldEntity fieldEntity : list) {
                String name = fieldEntity.getName();
                String value = fieldEntity.getValue();
                missionInfo.put(name, value);
            }
            // 添加型号id
            missionInfo.put("所属型号ID", moduleId);
            String moduleCode=moduleDao.getById(moduleId).get("F_XHDH").toString();
            missionInfo.put("所属型号代号", moduleCode);
            // 获取缺失的字段id信息
            missionInfo=getMissingIDinfo(missionInfo);
            missionInfo=dateTimeTodate(missionInfo);
            Boolean flag = true;
            try {
                flag = singleMissionInsert(missionInfo);
            } catch (Exception e) {
                resultMessage = new ResultMessage(ResultMessage.Fail, e.getCause().getMessage());
                System.out.println("--------------------by xiechen-----------------错误信息为:" + e.getCause().getMessage() + ","
                        + "当前输出的所在类为:ProductCategoryBatchService.addProductCategory()");
            }
            if (!importInfo.toString().contains("导入结果如下：")) {
                importInfo.append("---------------------导入结果如下：-------------------------\r\n");
            }
            if (!flag) { 
                importInfo.append("第").append(rowNum).append("行记录插入失败！\r\n");
                resultMessage = new ResultMessage(ResultMessage.Fail, importInfo.toString());
            }
            rowNum++;
        }
		
	}
	
	

	/**
	 * 将excel传来单时间转化为只有日期,没有小时
	 * @param missionInfo
	 * @return
	 */
	private Map<String, Object> dateTimeTodate(Map<String, Object> missionInfo) {
		String startTime=missionInfo.get("开始时间").toString();
		startTime=startTime.substring(0,10);
		missionInfo.put("开始时间", startTime);
		String endTime=missionInfo.get("结束时间").toString();
		endTime=endTime.substring(0,10);
		missionInfo.put("结束时间", endTime);
		return missionInfo;
	}

	/**
	 * 由于excel中只有 责任部门 和 试验单位 的name,没有id,
	 * 这里手动查部门表对要存储的键值对进行补全
	 * @param missionInfo
	 * @return
	 */
	private Map<String, Object> getMissingIDinfo(
			Map<String, Object> missionInfo) {
		String syfzrName=missionInfo.get("试验负责人").toString();
		String syfzrId=sysUserDao.getByFullname(syfzrName).get(0).getUserId().toString();
		missionInfo.put("试验负责人ID", syfzrId);
		return missionInfo;
	}

	private Boolean singleMissionInsert(Map<String, Object> missionInfo) {
		boolean flag = true;
		try {
			dao.singleMissionInsert(missionInfo);
			System.out.println("【" + missionInfo.get("任务名称") + "】任务新增成功");
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			System.out.println("--------------------by zmz-----------------错误信息为:" + e.getCause().getMessage() + ","
					+ "当前输出的所在类为:MissionService.singleMissionInsert()");
		}
		return flag;
	}
}
