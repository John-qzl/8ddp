package com.cssrc.ibms.core.resources.product.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cssrc.ibms.core.excel.reader.DataEntity;
import com.cssrc.ibms.core.excel.reader.FieldEntity;
import com.cssrc.ibms.core.excel.reader.TableEntity;
import com.cssrc.ibms.core.resources.product.dao.ProductCategoryBatchDao;
import com.cssrc.ibms.core.resources.product.util.ExcelReadUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptancePlanDao;
import com.cssrc.ibms.dp.product.formassign.dao.FormAssignDao;

/**
 * @description 产品类别/批次业务解析类
 * @author xie chen
 * @date 2019年11月21日 下午7:34:21
 * @version V1.0
 */
@Service
public class ProductCategoryBatchService {

	@Resource
	private ProductCategoryBatchDao dao;
	@Resource
	private AcceptancePlanDao acceptancePlanDao;
	@Resource
	private FormAssignDao formAssignDao;

	/**
	 * @Desc 根据型号id获取产品类别
	 * @param moduleId
	 * @return
	 */
	public List<Map<String, Object>> getCategoriesByModuleId(String moduleId) {
		return dao.getCategoriesByModuleId(moduleId);
	}
	
	
	/**
	 * @Desc 根据型号ids获取产品类别
	 * @param moduleId
	 * @return
	 */
	public List<Map<String, Object>> getCategoriesByModuleIds(String moduleIds) {
		return dao.getCategoriesByModuleIds(moduleIds);
	}

	/**
	 * @Desc 根据产品类别id获取产品批次
	 * @param categoryId
	 * @return
	 */
	public List<Map<String, Object>> getBatchesByCategoryId(String categoryId) {
		return dao.getBatchesByCategoryId(categoryId);
	}
	
	
	/**
	 * @Desc 根据产品类别ids获取产品批次
	 * @param categoryIds
	 * @return
	 */
	public List<Map<String, Object>> getBatchesByCategoryIds(String categoryIds) {
		return dao.getBatchesByCategoryIds(categoryIds);
	}
	
	
	/**
	 * @Desc 获取所有类别
	 * @param categoryId
	 * @return
	 */
	public List<Map<String, Object>> getAllBatch() {
		return dao.getAllBatch();
	}
	/**
	 * @Desc 根据产品批次/类别id获取批次及型号完整信息
	 * @param batchOrCategoryId
	 * @return
	 */
	public Map<String, Object> getBatchAndModule(String batchOrCategoryId) {
		return dao.getBatchAndModule(batchOrCategoryId);
	}

	/**
	 * @Desc 获取这个批次下面策划的数量
	 * @param batchOrCategoryId
	 * @return
	 */
	public String getPlanNumber(String moduleId) {
		return dao.getPlanNumber(moduleId);
	}
	
	/**
	 * @Desc 导入产品类别
	 * @param file
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public ResultMessage importCategories(MultipartFile file, String moduleId) throws Exception {
		
		ResultMessage resultMessage = new ResultMessage(ResultMessage.Success, "产品导入成功");
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
	 * @Desc 校验产品导入的必填信息
	 * @param tableEntity
	 * @param importInfo
	 * @return
	 */
	private StringBuffer checkProductCategory(TableEntity tableEntity, StringBuffer importInfo) {

		int rowNum = 2;
		for (DataEntity dataEntity : tableEntity.getDataEntityList()) {
			String fieldName = "";
			List<FieldEntity> list = dataEntity.getFieldEntityList();
			// 对字段名称进行遍历
			for (FieldEntity fieldEntity : list) {
				String name = fieldEntity.getName();
				String value = fieldEntity.getValue();
				fieldName += name + ",";
				if ("序号".equals(name) || "产品编号".equals(name) || "产品名称".equals(name) || "责任（设计）部门".equals(name) || "研制（生产）单位".equals(name)
						|| "交付数量".equals(name)) {

				} else {
					return importInfo.append("列名不正确，请确保列名为以下其中一个：【序号】、【产品编号】、【产品名称】、【责任（设计）部门】、【研制（生产）单位】、【交付数量】");
				}
				if ("产品编号".equals(name) && "".equals(value)) {
					if (!importInfo.toString().contains("必填校验结果如下：")) {
						importInfo.append("--------必填校验结果如下：--------\r\n");
					}
					return importInfo.append("第").append(rowNum).append("行").append("（列名为" + name + "）,不能为空！\r\n");
				}
				if ("产品名称".equals(name) && "".equals(value)) {
					if (!importInfo.toString().contains("必填校验结果如下：")) {
						importInfo.append("--------必填校验结果如下：--------\r\n");
					}
					return importInfo.append("第").append(rowNum).append("行").append("（列名为" + name + "）,不能为空！\r\n");
				}
				if ("责任（设计）部门".equals(name) && "".equals(value)) {
					if (!importInfo.toString().contains("必填校验结果如下：")) {
						importInfo.append("--------必填校验结果如下：--------\r\n");
					}
					return importInfo.append("第").append(rowNum).append("行").append("（列名为" + name + "）,不能为空！\r\n");
				}
				if ("研制（生产）单位".equals(name) && "".equals(value)) {
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
	 * @Desc 向数据库（产品类别批次表）插入数据
	 * @param tableEntity
	 * @param importInfo
	 * @param resultMessage
	 * @param moduleId
	 */
	private void addProductCategory(TableEntity tableEntity, StringBuffer importInfo,ResultMessage resultMessage, String moduleId) {
		int rowNum = 2;
        for (DataEntity dataEntity : tableEntity.getDataEntityList()) {
            List<FieldEntity> list = dataEntity.getFieldEntityList();
            Map<String, Object> categoryInfor = new HashMap<>();
            // 对字段数据进行遍历
            for (FieldEntity fieldEntity : list) {
                String name = fieldEntity.getName();
                String value = fieldEntity.getValue();
                categoryInfor.put(name, value);
            }
            // 添加型号id
            categoryInfor.put("moduleId", moduleId);
            
            Boolean flag = true;
            try {
                flag = singleCategoryInsert(categoryInfor);
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
	
	private Boolean singleCategoryInsert(Map<String, Object> categoryInfor) {

		boolean flag = true;
		try {
			dao.singleCategoryInsert(categoryInfor);
			System.out.println("【" + categoryInfor.get("产品名称") + "】产品新增成功");
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			System.out.println("--------------------by xiechen-----------------错误信息为:" + e.getCause().getMessage() + ","
					+ "当前输出的所在类为:ProductCategoryBatchService.singleCategoryInsert()");
		}
		return flag;
	}
	
	public Map<String, Object> getById(String categoryOrBatchId) {
		return dao.getById(categoryOrBatchId);
	}
	
	/**
	 * @Desc 根据产品批次id校验表单下发权限
	 * @param batchId
	 * @return
	 */
	public Map<String, Object> formAssignCheck(String batchId){
		Map<String, Object> checkMap = new HashMap<>();
		// 默认不通过
		checkMap.put("success", false);
		// 获取产品批次下所有验收策划
		List<Map<String, Object>> planList = acceptancePlanDao.getPlansByProductBatchId(batchId);
		if (planList.isEmpty()) {
			// 尚未进行验收策划
			checkMap.put("msg", "尚未进行验收策划！");
			return checkMap;
		} else {
			checkMap.put("msg", "验收策划尚未通过审批！");
			// 校验验收策划是否审批通过
			for (Map<String, Object> map : planList) {
				if ("审批通过".equals(map.get("F_SPZT"))) {
					// 只需有一个审批通过即可
					checkMap.put("success", true);
				}
			}
			return checkMap;
		}
		
	}
	
	/**
	 * @Desc 根据产品批次id校验数据回传查看权限
	 * @param batchId
	 * @return
	 */
	public Map<String, Object> dataReturnViewCheck(String batchId){
		// 获取表单下发权限
		Map<String, Object> formCheck = this.formAssignCheck(batchId);
		if (formCheck.get("success").equals(false)) {
			return formCheck;
		} else {
			// 有表单下发权限
			Map<String, Object> checkMap = new HashMap<>();
			// 默认不通过
			checkMap.put("success", false);
			// 获取产品批次下所有表单下发
			List<Map<String, Object>> formList = formAssignDao.getFormAssignByProductBatchId(batchId);
			if (formList.isEmpty()) {
				// 尚未进行表单下发
				checkMap.put("msg", "尚未进行表单下发！");
				return checkMap;
			} else {
				checkMap.put("success", true);
				return checkMap;
			}
		}
	}

}
