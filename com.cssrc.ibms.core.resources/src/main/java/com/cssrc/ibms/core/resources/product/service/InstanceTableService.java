package com.cssrc.ibms.core.resources.product.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import com.cssrc.ibms.core.resources.datapackage.dao.RangeTestPlanDao;
import com.cssrc.ibms.core.resources.io.bean.ins.RangeTestTableIntance;
import com.cssrc.ibms.core.resources.io.dao.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.resources.io.bean.FileData;
import com.cssrc.ibms.core.resources.io.bean.TitleName;
import com.cssrc.ibms.core.resources.io.bean.ins.AcceptanceData;
import com.cssrc.ibms.core.resources.io.bean.ins.TableInstance;
import com.cssrc.ibms.core.resources.io.bean.template.SignDef;
import com.cssrc.ibms.core.resources.io.bean.ins.ProductColumn;

import com.cssrc.ibms.core.resources.product.dao.InstanceTableDao;
import com.cssrc.ibms.core.user.dao.SysUserDao;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.dp.form.dao.SignResultDao;
import com.cssrc.ibms.dp.form.model.SignResult;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptancePlanDao;
import com.cssrc.ibms.dp.product.acceptance.dao.AcceptanceReportDao;
import com.cssrc.ibms.dp.product.acceptance.dao.WorkBoardDao;
import com.cssrc.ibms.dp.sync.bean.Conventional;
import com.cssrc.ibms.dp.sync.service.DataSyncService;
import com.cssrc.ibms.report.model.SignModel;
import com.cssrc.ibms.report.service.SignModelService;
import com.cssrc.ibms.system.dao.SysFileDao;
import com.cssrc.ibms.system.model.SysFile;

@Service
public class InstanceTableService {
    @Resource
    InstanceTableDao instanceTableDao;
    @Resource
    DataSyncService dataSyncService;

    @Resource
    IOTableInstanceDao tableInstanceDao;
    @Resource
    IOSignResultDao iOSignResultDao;
    @Resource
    IOConventionalDao conventionalDao;
    @Resource
    FileDataDao fileDataDao;
    @Resource
    WorkBoardDao workBoardDao;
    @Resource
    IOSignDefDao signDefDao;
    @Resource
    SignResultDao signResultDao;
    @Resource
    SysFileDao sysFileDao;
    @Resource
    SignModelService signModelService;
    @Resource
    SysUserDao sysUserDao;
    @Resource
    AcceptancePlanDao acceptancePlanDao;
    @Resource
    RangeTestTableIntanceDao rangeTestTableIntanceDao;
    @Resource
    RangeTestPlanDao rangeTestPlanDao;
    @Resource
    AcceptanceReportDao acceptanceReportDao;
    /**
     * @param id
     * @return
     * @Desc 根据实例id返回实例html代码
     */
    public Map<String, Object> getInstanceHTML(String id) {
        return instanceTableDao.getInstanceHTML(id);
    }

    /**
     * @param
     * @return
     * @Desc excel导入靶场试验实例表单
     * by zmz
     * 20200828
     */
    public String importRangeTestFromExcel(InputStream input, String missionId) throws Exception {


		String msg = "{\"success\":\"true\",\"context\":\"上传成功\"}";// 返回执行情况\
		List<Map<String, Object>> instanceList = instanceTableDao.getInstanceByPlnanId(missionId);// 获取 策划下面所有的实例

		Workbook wb = null;
		String modelType = "";
		List<String> signList = new ArrayList<>();
		try {
			wb = WorkbookFactory.create(input);
			Sheet sheet = wb.getSheetAt(0); // 获得第一个表单-表单的签署标题检查条件注意事项
			int totalRow = sheet.getLastRowNum();// 得到excel的总记录条数
			int columtotal = sheet.getRow(0).getPhysicalNumberOfCells();// 表头总共的列数
			TableInstance tableInstance = new TableInstance();
			String id = String.valueOf(UniqueIdUtil.genId());
			System.out.println("总行数:" + totalRow + ",总列数:" + columtotal);
			int check = 0;
			for (int i = 0; i <= totalRow; i++) {// 遍历行
				for (int j = 0; j < columtotal; j++) {
					sheet.getRow(i).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
					System.out.print(sheet.getRow(i).getCell(j).getStringCellValue() + "   ");
					String data = sheet.getRow(i).getCell(j).getStringCellValue();
					if (data.indexOf("表单标题") >= 0) {
						String title = sheet.getRow(i).getCell(j + 1).getStringCellValue();
						tableInstance.setId(id);
						tableInstance.setName(title);
						check++;
					} else if (data.indexOf("模板种类") >= 0) {
						check++;
						modelType = sheet.getRow(i).getCell(j + 1).getStringCellValue();
						switch (modelType) {
							case "常规验收项目表":
								modelType = "1";
								break;
							case "功能性能验收项目表":
								modelType = "2";
								break;
							case "验收报告回传表":
								modelType = "6";
								break;
							case "靶场试验问题表":
								modelType="10";
								break;
                            case "武器系统所检问题表":
                                modelType="13";
                                break;

						}
					} else if (j == 0) {
						check++;
						String sign = sheet.getRow(i).getCell(j + 1).getStringCellValue();
						if (sign == null || sign.equals("")) {
							msg = "{\"success\":\"false\",\"context\":\"签署信息填写不正确!!\"}";
							return msg;
						}
						String string = data + ":" + sign;
						signList.add(string);
					}
				}
			}
			if (check < 3) {
				msg = "{\"success\":\"false\",\"context\":\"种类，标题，或签署未填!!\"}";
				return msg;
			}

			Sheet sheet2 = wb.getSheetAt(1); // 获得第一个表单-表单的表格
			int totalRow2 = sheet2.getLastRowNum();// 得到excel的总记录条数
			int columtotal2 = sheet2.getRow(0).getPhysicalNumberOfCells();// 表头总共的列数
			String html = "<!?xml version=\"1.0\" encoding=\"UTF-8\"?><html> <table width=\"100%\" class=\"layui-table\"><tbody><tr class=\"firstRow\">";
			int requireval = 0;
			List<Integer> actualval = new ArrayList<>();
			int prefixCode = 0;
			int actualvalNum = 0;
			JSONObject backData = new JSONObject();
			JSONArray signArray = new JSONArray();
			List<Integer> noqualified = new ArrayList<>();
			for (int i = 0; i <= totalRow2; i++) {// 遍历行
				for (int j = 0; j < columtotal2; j++) {
					sheet2.getRow(i).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
					System.out.print(sheet2.getRow(i).getCell(j).getStringCellValue() + "   ");
					String data = sheet2.getRow(i).getCell(j).getStringCellValue();
					if (i == 0) { // i=0表头
						if (data.indexOf("要求值") >= 0) {
							data = data.substring(0, data.indexOf("("));
							html += "<td valign=\"top\" class=\"requireval\" align=\"center\">";
							html += data + "<i class=\"markup\">(要求值)</i>";
							prefixCode = j;
							html += "</td>";
							requireval = j;
						} else if (data.indexOf("实测值") >= 0) {
							data = data.substring(0, data.indexOf("("));
							html += "<td valign=\"top\" class=\"actualval\" align=\"center\">";
							html += data + "<i class=\"markup\">(实测值)</i>";
							html += "</td>";
							actualvalNum = j;
							actualval.add(j);
						} else {
							html += "<td valign=\"top\" align=\"center\">";
							html += data;
							html += "</td>";
						}

						if (j == columtotal2 - 1) {
							html += "</tr>";
						}
					} else {
						if (j == 0) {
							html += "<tr>";
						}
						String tagStr = "";
						if (j == requireval) {
							tagStr = "class=\"requireval\"";
						} else if (actualval.contains(j)) {
							tagStr = "class=\"actualval\"";
						}
						String title = sheet2.getRow(0).getCell(j).getStringCellValue();
						if (title.indexOf("序号") >= 0 || title.indexOf("规定值") >= 0) {
							html += "<td " + tagStr + " valign=\"top\">";
							html += data;
							html += "</td>";
						} else if (title.indexOf("实测值") >= 0 && i != 1) {
							if (i != totalRow2) {
								String prefixCodeValue = sheet2.getRow(i).getCell(prefixCode).getStringCellValue();
								String actualvalNumValue = sheet2.getRow(i).getCell(j).getStringCellValue();
								JSONObject jsonObject = check(prefixCodeValue, actualvalNumValue);
								if (jsonObject.getString("success").equals("true")) {
									String judge = jsonObject.getString("check").toString();
									if (judge.equals("合格")) {
										html += "<td " + tagStr
												+ " style=\"width:60px\"  valign=\"top\" isFull=\"合格\">";
										html += data;
										html += "</td>";
									} else {
										html += "<td " + tagStr
												+ " style=\"background:red;width:60px\" isFull=\"不合格\" valign=\"top\">";
										html += data;
										html += "</td>";
										noqualified.add(j);
									}
								} else {
									msg = "{\"success\":\"false\",\"context\":\"模板导入失败，请检查模板格式是否正确\"}";// 返回执行情况
									return msg;
								}
							} else {
								if (noqualified.contains(j)) {
									html += "<td " + tagStr + " valign=\"top\">";
									html += "不合格";
									html += "</td>";
								} else {
									html += "<td " + tagStr + " valign=\"top\">";
									html += "合格";
									html += "</td>";

								}
							}
						} else if (!"".equals(data)) {
							html += "<td " + tagStr + " valign=\"top\">";
							html += data;
							html += "</td>";
						} else {
							String str = "";
							for (int c = 0; c < j; c++) {
								String cell = sheet2.getRow(i).getCell(c).getStringCellValue();
								if (!"".equals(cell)) {
									str += cell + "/";
								}
							}
							str = str.substring(0, str.length() - 1);
							str += "," + title;
							html += "<td valign=\"top\" " + tagStr + " input=\"1\">";
							html += "<input type=\"text\" class=\"dpInputText\" style=\"width: 60px;\"";
							html += " id=" + "\"" + UniqueIdUtil.genId() + "\"";
							html += " /></td>";
						}
						if (j == columtotal2 - 1) {
							html += "</tr>";
						}
					}
				}
			}
			html += "</tbody></table></html>";
			JSONObject jsonObject = analysisHtml(html, missionId, tableInstance.getId(), modelType);
			if (jsonObject.getString("success").equals("false")) {
				return msg = jsonObject.toString();// 返回执行情况
			}
			tableInstance.setContent(html);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
			tableInstance.setStartTime(df.format(new Date()));
			tableInstance.setEndTime(df.format(new Date()));
			tableInstance.setPlanId(missionId);
			tableInstance.setProductId(missionId);

			tableInstance.setType(modelType);
			int order = 0;
			SignDef signDef = new SignDef();
			for (String str : signList) {
				if (modelType.equals("6")) {
					String[] strGroup = str.split(":");
					str = strGroup[1];
					String fullName = strGroup[0] + "(" + str + ")";
					signDef.setName(fullName);
				} else {
					String[] strGroup = str.split(":");
					str = strGroup[1];
					signDef.setName(strGroup[0]);
				}
				List<SysUser> sysUserList = (List<SysUser>) sysUserDao.getByFullname(str);
				if (sysUserList == null || sysUserList.size() == 0) {
					msg = "{\"success\":\"false\",\"context\":\"无法在系统中找到签署人员，请重新填写！\"}";// 返回执行情况
					return msg;
				}
				SysUser sysUser = sysUserList.get(0);

				String signDefId = String.valueOf(UniqueIdUtil.genId());
				signDef.setId(signDefId);

				signDef.setOrder(String.valueOf(order));
				order++;
				List<SignModel> signModelList = (List<SignModel>) signModelService.getByUserId(sysUser.getUserId());
				if (signModelList == null || signModelList.size() == 0) {
					msg = "{\"success\":\"false\",\"context\":\"未找到该人员签署信息！\"}";// 返回执行情况
					return msg;
				}
				SysFile sysFile = sysFileDao.getById(Long.valueOf(signModelList.get(0).getImgPath()));
				Long fileId = UniqueIdUtil.genId();
				sysFile.setFileId(fileId);

				signDefDao.insert(signDef);
				SignResult signResult = new SignResult();
				Long signResultId = UniqueIdUtil.genId();
				signResult.setID(signResultId);
				signResult.setSignID(Long.valueOf(signDef.getId()));
				signResult.setInstantID(Long.valueOf(tableInstance.getId()));
				signResult.setSignTime(new Date());
				signResult.setSignUser(String.valueOf(sysUser.getUserId()));
				sysFile.setDataId(String.valueOf(signResultId));
				sysFileDao.add(sysFile);
				signResultDao.add(signResult);
				JSONObject signInfo = new JSONObject();
				signInfo.put("signDefId", signDef.getId());
				signInfo.put("signid", signResult.getID());
				signInfo.put("time", df.format(new Date()));
				signArray.add(signInfo);
			}
			tableInstanceDao.insert(tableInstance);

			if ("10".equals(modelType)||"13".equals(modelType)) {
//				backData.put("signreses", signArray);
				JSONObject data = jsonObject.getJSONObject("backData");
				backData.put("instructions", data.getString("instructions"));
//				backData.put("opinion", data.getString("opinion"));
				backData.put("problem", data.getString("problem"));
//				backData.put("sellerOpinion", data.getString("sellerOpinion"));
//				backData.put("serialNumber", data.getString("serialNumber"));
				rangeTestPlanDao.updateBackData(missionId, backData.toString());
			}
			switch (modelType) {
				case "1":
					modelType = "2";
					break;
				case "2":
					modelType = "3";
					break;
				case "6":
					modelType = "4";
					break;
				case "10":
					modelType = "10";
					break;
				case "13":
					modelType = "13";
					break;
				default:
					modelType = "0";
					break;
			}
			FileData fileData = new FileData();
			fileData.setId(String.valueOf(UniqueIdUtil.genId()));
			fileData.setPlanId(missionId);
			fileData.setMz(tableInstance.getName());
			fileData.setSjId(tableInstance.getId());
			fileData.setSjlb(modelType);
			fileDataDao.insert(fileData);

		} catch (Exception ex) {
			msg = "{\"success\":\"false\",\"context\":\"模板导入失败，请检查模板格式是否正确\"}";// 返回执行情况
			ex.printStackTrace();
			return msg;

		} finally {
			try {
				input.close();
				return msg;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return msg;
    }

    /**
     * @param
     * @return
     * @Desc 数据excel导入
     */
    public String readExcel(InputStream input, String acceptancePlanId, String productBatchId) throws Exception {
        String msg = "{\"success\":\"true\",\"context\":\"上传成功\"}";// 返回执行情况\
        List<Map<String, Object>> instanceList = instanceTableDao.getInstanceByPlnanId(acceptancePlanId);// 获取 策划下面所有的实例
		/*if (instanceList != null) {
			for (Map<String, Object> map : instanceList) { // 判断策划下面是否有pad回传的表单
				Object type = map.get("F_BDZL");
				if (type == null || "0".equals(type)) {
					msg = "{\"success\":\"false\",\"context\":\"当前策划下面已有pad回传实例，无法进行导入\"}";// 返回执行情况\
					return msg;
				}
			}
		}*/
        List<Map<String, Object>> acceptanceReport=acceptanceReportDao.getAcceptanceReport(acceptancePlanId);
        if(acceptanceReport!=null&&acceptanceReport.size()!=0) {
     	   if(CommonTools.Obj2String(acceptanceReport.get(0).get("F_SPZT")).equals("审批通过")) {
     		    msg = "{\"success\":\"false\",\"context\":\"验收报告已审批通过无法继续上传表格!\"}";// 返回执行情况
     		    return msg;
     	   }
        }
        Workbook wb = null;
        String modelType = "";
        List<String> signList = new ArrayList<>();
        try {
            wb = WorkbookFactory.create(input);
            Sheet sheet = wb.getSheetAt(0); // 获得第一个表单-表单的签署标题检查条件注意事项
            int totalRow = sheet.getLastRowNum();// 得到excel的总记录条数
            int columtotal = sheet.getRow(0).getPhysicalNumberOfCells();// 表头总共的列数
            /*
             * if (columtotal != 2) { msg =
             * "{\"success\":\"true\",\"context\":\"当前格式不正确请重新上传！\"}"; return msg; }
             */
            TableInstance tableInstance = new TableInstance();
            String id = String.valueOf(UniqueIdUtil.genId());
            System.out.println("总行数:" + totalRow + ",总列数:" + columtotal);
            int check = 0;
            for (int i = 0; i <= totalRow; i++) {// 遍历行
                for (int j = 0; j < columtotal; j++) {
                    sheet.getRow(i).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                    System.out.print(sheet.getRow(i).getCell(j).getStringCellValue() + "   ");
                    String data = sheet.getRow(i).getCell(j).getStringCellValue();
                    if (data.indexOf("表单标题") >= 0) {
                        String title = sheet.getRow(i).getCell(j + 1).getStringCellValue();
                        tableInstance.setId(id);
                        tableInstance.setName(title);
                        check++;
                    } else if (data.indexOf("模板种类") >= 0) {
                        check++;
                        modelType = sheet.getRow(i).getCell(j + 1).getStringCellValue();
                        switch (modelType) {
                            case "常规验收项目表":
                                modelType = "1";
                                break;
                            case "功能性能验收项目表":
                                modelType = "2";
                                break;
                            case "验收报告回传表":
                                modelType = "6";
                                break;
                        }
                    } else if (j == 0) {
                        check++;
                        String sign = sheet.getRow(i).getCell(j + 1).getStringCellValue();
                        if (sign == null || sign.equals("")) {
                            msg = "{\"success\":\"false\",\"context\":\"签署信息填写不正确!!\"}";
                            return msg;
                        }
                        String string = data + ":" + sign;
                        signList.add(string);
                    }
                }
            }
            if (check < 3) {
                msg = "{\"success\":\"false\",\"context\":\"种类，标题，或签署未填!!\"}";
                return msg;
            }

            Sheet sheet2 = wb.getSheetAt(1); // 获得第一个表单-表单的表格
            int totalRow2 = sheet2.getLastRowNum();// 得到excel的总记录条数
            int columtotal2 = sheet2.getRow(0).getPhysicalNumberOfCells();// 表头总共的列数
            String html = "<!?xml version=\"1.0\" encoding=\"UTF-8\"?><html> <table width=\"100%\" class=\"layui-table\"><tbody><tr class=\"firstRow\">";
            int requireval = 0;
            List<Integer> actualval = new ArrayList<>();
            int prefixCode = 0;
            int actualvalNum = 0;
            JSONObject backData = new JSONObject();
            JSONArray signArray = new JSONArray();
            List<Integer> noqualified = new ArrayList<>();
            for (int i = 0; i <= totalRow2; i++) {// 遍历行
                for (int j = 0; j < columtotal2; j++) {
                    sheet2.getRow(i).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                    System.out.print(sheet2.getRow(i).getCell(j).getStringCellValue() + "   ");
                    String data = sheet2.getRow(i).getCell(j).getStringCellValue();
                    if (i == 0) { // i=0表头
                        if (data.indexOf("要求值") >= 0) {
                            data = data.substring(0, data.indexOf("("));
                            html += "<td valign=\"top\" class=\"requireval\" align=\"center\">";
                            html += data + "<i class=\"markup\">(要求值)</i>";
                            prefixCode = j;
                            html += "</td>";
                            requireval = j;
                        } else if (data.indexOf("实测值") >= 0) {
                            data = data.substring(0, data.indexOf("("));
                            html += "<td valign=\"top\" class=\"actualval\" align=\"center\">";
                            html += data + "<i class=\"markup\">(实测值)</i>";
                            html += "</td>";
                            actualvalNum = j;
                            actualval.add(j);
                        } else {
                            html += "<td valign=\"top\" align=\"center\">";
                            html += data;
                            html += "</td>";
                        }

                        if (j == columtotal2 - 1) {
                            html += "</tr>";
                        }
                    } else {
                        if (j == 0) {
                            html += "<tr>";
                        }
                        String tagStr = "";
                        if (j == requireval) {
                            tagStr = "class=\"requireval\"";
                        } else if (actualval.contains(j)) {
                            tagStr = "class=\"actualval\"";
                        }
                        String title = sheet2.getRow(0).getCell(j).getStringCellValue();
                        if (title.indexOf("序号") >= 0 || title.indexOf("规定值") >= 0) {
                            html += "<td " + tagStr + " valign=\"top\">";
                            html += data;
                            html += "</td>";
                        } else if (title.indexOf("实测值") >= 0 && i != 1) {
                            if (i != totalRow2) {
                                String prefixCodeValue = sheet2.getRow(i).getCell(prefixCode).getStringCellValue();
                                String actualvalNumValue = sheet2.getRow(i).getCell(j).getStringCellValue();
                                JSONObject jsonObject = check(prefixCodeValue, actualvalNumValue);
                                if (jsonObject.getString("success").equals("true")) {
                                    String judge = jsonObject.getString("check").toString();
                                    if (judge.equals("合格")) {
                                        html += "<td " + tagStr
                                                + " style=\"width:60px\"  valign=\"top\" isFull=\"合格\">";
                                        html += data;
                                        html += "</td>";
                                    } else {
                                        html += "<td " + tagStr
                                                + " style=\"background:red;width:60px\" isFull=\"不合格\" valign=\"top\">";
                                        html += data;
                                        html += "</td>";
                                        noqualified.add(j);
                                    }
                                } else {
                                    msg = "{\"success\":\"false\",\"context\":\"模板导入失败，请检查模板格式是否正确\"}";// 返回执行情况
                                    return msg;
                                }
                            } else {
                                if (noqualified.contains(j)) {
                                    html += "<td " + tagStr + " valign=\"top\">";
                                    html += "不合格";
                                    html += "</td>";
                                } else {
                                    html += "<td " + tagStr + " valign=\"top\">";
                                    html += "合格";
                                    html += "</td>";

                                }
                            }
                        } else if (!"".equals(data)) {
                            html += "<td " + tagStr + " valign=\"top\">";
                            html += data;
                            html += "</td>";
                        } else {
                            String str = "";
                            for (int c = 0; c < j; c++) {
                                String cell = sheet2.getRow(i).getCell(c).getStringCellValue();
                                if (!"".equals(cell)) {
                                    str += cell + "/";
                                }
                            }
                            str = str.substring(0, str.length() - 1);
                            str += "," + title;
                            html += "<td valign=\"top\" " + tagStr + " input=\"1\">";
                            html += "<input type=\"text\" class=\"dpInputText\" style=\"width: 60px;\"";
                            html += " id=" + "\"" + UniqueIdUtil.genId() + "\"";
                            html += " /></td>";
                        }
                        if (j == columtotal2 - 1) {
                            html += "</tr>";
                        }
                    }
                }
            }
            html += "</tbody></table></html>";
            JSONObject jsonObject = analysisHtml(html, acceptancePlanId, tableInstance.getId(), modelType);
            if (jsonObject.getString("success").equals("false")) {
                return msg = jsonObject.toString();// 返回执行情况
            }
            tableInstance.setContent(html);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
            tableInstance.setStartTime(df.format(new Date()));
            tableInstance.setEndTime(df.format(new Date()));
            tableInstance.setPlanId(acceptancePlanId);
            tableInstance.setProductId(productBatchId);

            tableInstance.setType(modelType);
            int order = 0;
            SignDef signDef = new SignDef();
            for (String str : signList) {
                if (modelType.equals("6")) {
                    String[] strGroup = str.split(":");
                    str = strGroup[1];
                    String fullName = strGroup[0] + "(" + str + ")";
                    signDef.setName(fullName);
                } else {
                    String[] strGroup = str.split(":");
                    str = strGroup[1];
                    signDef.setName(strGroup[0]);
                }
                List<SysUser> sysUserList = (List<SysUser>) sysUserDao.getByFullname(str);
                if (sysUserList == null || sysUserList.size() == 0) {
                    msg = "{\"success\":\"false\",\"context\":\"无法在系统中找到签署人员，请重新填写！\"}";// 返回执行情况
                    return msg;
                }
                SysUser sysUser = sysUserList.get(0);

                String signDefId = String.valueOf(UniqueIdUtil.genId());
                signDef.setId(signDefId);

                signDef.setOrder(String.valueOf(order));
                order++;
                List<SignModel> signModelList = (List<SignModel>) signModelService.getByUserId(sysUser.getUserId());
                if (signModelList == null || signModelList.size() == 0) {
                    msg = "{\"success\":\"false\",\"context\":\"未找到该人员签署信息！\"}";// 返回执行情况
                    return msg;
                }
                SysFile sysFile = sysFileDao.getById(Long.valueOf(signModelList.get(0).getImgPath()));
                Long fileId = UniqueIdUtil.genId();
                sysFile.setFileId(fileId);

                signDefDao.insert(signDef);
                SignResult signResult = new SignResult();
                Long signResultId = UniqueIdUtil.genId();
                signResult.setID(signResultId);
                signResult.setSignID(Long.valueOf(signDef.getId()));
                signResult.setInstantID(Long.valueOf(tableInstance.getId()));
                signResult.setSignTime(new Date());
                signResult.setSignUser(String.valueOf(sysUser.getUserId()));
                sysFile.setDataId(String.valueOf(signResultId));
                sysFileDao.add(sysFile);
                signResultDao.add(signResult);
                JSONObject signInfo = new JSONObject();
                signInfo.put("signDefId", signDef.getId());
                signInfo.put("signid", signResult.getID());
                signInfo.put("time", df.format(new Date()));
                signArray.add(signInfo);
            }
            tableInstanceDao.insert(tableInstance);
            if ("6".equals(modelType)) {
                backData.put("signreses", signArray);
                JSONObject data = jsonObject.getJSONObject("backData");
                backData.put("instructions", data.getString("instructions"));
                backData.put("opinion", data.getString("opinion"));
                backData.put("problem", data.getString("problem"));
                backData.put("sellerOpinion", data.getString("sellerOpinion"));
                backData.put("serialNumber", data.getString("serialNumber"));
                acceptancePlanDao.updateBackData(acceptancePlanId, backData.toString());
            }
            switch (modelType) {
                case "2":
                    modelType = "3";
                    break;
                default:
                    modelType = "2";
                    break;
            }
            FileData fileData = new FileData();
            fileData.setId(String.valueOf(UniqueIdUtil.genId()));
            fileData.setPlanId(acceptancePlanId);
            fileData.setMz(tableInstance.getName());
            fileData.setSjId(tableInstance.getId());
            fileData.setSjlb(modelType);
            fileDataDao.insert(fileData);

        } catch (Exception ex) {
            msg = "{\"success\":\"false\",\"context\":\"模板导入失败，请检查模板格式是否正确\"}";// 返回执行情况
            ex.printStackTrace();
            return msg;

        } finally {
            try {
                input.close();
                return msg;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return msg;

    }

    /**
     * 填写常规项验收数据表
     * w_cgxyssj
     * 字段:检查内容,检查要求,检查结果,结论,planid,实例id,备注
     *
     * @param html
     * @param acceptancePlanId
     * @param tableInstanId
     * @param type
     * @return
     */
    public JSONObject analysisHtml(String html, String acceptancePlanId, String tableInstanId, String type) {
        JSONObject msg = new JSONObject();
        msg.put("success", "true");
        org.jsoup.nodes.Document doc = Jsoup.parse(html);
        JSONObject reportString = new JSONObject();
        List<Conventional> conventionalList = null;
        if (doc.select("table[class=layui-table]").size() != 0) {
            Elements rows = doc.select("table[class=layui-table]").get(0).select("tr");
            if (type.equals("2")) {
                List<ProductColumn> productList = new ArrayList<>();
                List<TitleName> titleNameList = new ArrayList<>();
                for (int i = 0; i < rows.size(); i++) {
                    org.jsoup.nodes.Element row = rows.get(i);
                    String acceptanceProject = "";
                    String indicators = "";
                    String unit = "";
                    String requirements = "";
                    if (i == 0) {
                        if (row.select("td").size() != 0) {
                            for (int j = 0; j < row.select("td").size(); j++) {
                                String title = row.select("td").get(j).text();
                                TitleName titleName = new TitleName();
                                titleName.setTitleName(title);
                                if (title.indexOf("实测值") >= 0) {
                                    title = rows.get(i + 1).select("td").get(j).text();
                                    String xh = title;
                                    if (xh != null && !xh.equals("")) {
                                        String F_JL = rows.get(rows.size() - 1).select("td").get(j).text();
                                        Long id = UniqueIdUtil.genId();
                                        titleName.setProductId(String.valueOf(id));
                                        titleName.setProductName(title);
                                        ProductColumn productColumn = new ProductColumn();
                                        productColumn.setProductId(String.valueOf(id));
                                        productColumn.setProductName(title);
                                        productColumn.setRow(j);
                                        productColumn.setCheck(F_JL);
                                        productColumn.setIns_Id(tableInstanId);
                                        productColumn.setPlainId(acceptancePlanId);
                                        productList.add(productColumn);
                                    }
                                }
                                titleName.setIndex(j);
                                titleNameList.add(titleName);
                            }
                        }
                        if (productList.size() == 0) {
                            msg.put("success", "false");
                            msg.put("context", "表头不能为空!");
                            return msg;
                        }
                    } else if (i != 1 && productList.size() > 0 && i != rows.size() - 1) {
                        for (TitleName titleName : titleNameList) {
                            String value = row.select("td").get(titleName.getIndex()).attr("value").equals("")
                                    ? row.select("td").get(titleName.getIndex()).text()
                                    : row.select("td").get(titleName.getIndex()).attr("value");
                            if (titleName.getTitleName().indexOf("验收项目") >= 0) {
                                acceptanceProject = value;
                            } else if (titleName.getTitleName().indexOf("计量单位") >= 0) {
                                unit = value;
                            } else if (titleName.getTitleName().indexOf("要求值") >= 0) {
                                indicators = value;
                            } else if (titleName.getTitleName().indexOf("操作要求") >= 0) {
                                requirements = value;
                            } else if (titleName.getTitleName().indexOf("实测值") >= 0) {
                                AcceptanceData acceptanceData = new AcceptanceData();
                                acceptanceData.setProductId(titleName.getProductId());
                                acceptanceData.setAcceptanceProject(acceptanceProject);
                                acceptanceData.setProductName(titleName.getProductName());
                                value = row.select("td").get(titleName.getIndex()).select("td").text();
                                acceptanceData.setIsCheck(
                                        row.select("td").get(titleName.getIndex()).select("td").attr("isFull"));
                                acceptanceData.setRealValue(value);
                                acceptanceData.setIns_Id(tableInstanId);
                                acceptanceData.setRequirements(requirements);
                                acceptanceData.setRequiredValue(indicators);
                                acceptanceData.setUnit(unit);
                                /*
                                 * JSONObject jsonObject=check(acceptanceData.getRequiredValue(),
                                 * acceptanceData.getRealValue());
                                 * if(jsonObject.getString("success").equals("false")) { msg.put("success",
                                 * "false"); msg.put("context", jsonObject.getString("msg")); return msg; }
                                 * acceptanceData.setIsCheck(jsonObject.getString("check"));
                                 */
                                if (!acceptanceData.getAcceptanceProject().equals("") && acceptanceData != null) {
                                    iOSignResultDao.insertAcceptanceData(acceptanceData);
                                }
                            }
                        }

                    }
                }
                for (int c = 0; c < productList.size(); c++) {
                    tableInstanceDao.insertcp(productList.get(c));
                }
            } else if (type.equals("1")) {
                List<TitleName> titleNameList = new ArrayList<>();
                for (int i = 0; i < rows.size(); i++) {
                    org.jsoup.nodes.Element row = rows.get(i);
                    if (i == 0) {
                        if (row.select("td").size() != 0) {
                            for (int j = 0; j < row.select("td").size(); j++) {
                                String title = row.select("td").get(j).text();
                                TitleName titleName = new TitleName();
                                titleName.setTitleName(title);
                                if (title.indexOf("检查内容") >= 0) {
                                    conventionalList = new ArrayList<>();
                                }
                                titleName.setIndex(j);
                                titleNameList.add(titleName);
                            }
                        }
                    } else if (i != 0 && conventionalList != null) {
                        Conventional conventional = new Conventional();
                        for (TitleName titleName : titleNameList) {
                            String value = row.select("td").get(titleName.getIndex()).attr("value").equals("")
                                    ? row.select("td").get(titleName.getIndex()).text()
                                    : row.select("td").get(titleName.getIndex()).attr("value");
                            if (titleName.getTitleName().indexOf("序号") >= 0) {
                                conventional.setXh(value);
                            } else if (titleName.getTitleName().indexOf("内容") >= 0) {
                                conventional.setJcnr(value);
                                ;
                            } else if (titleName.getTitleName().indexOf("要求") >= 0) {
                                conventional.setJcyq(value);
                            } else if (titleName.getTitleName().indexOf("检查结果") >= 0) {
                                value = row.select("td").get(titleName.getIndex()).text();
                                conventional.setJcjg(value);
                            } else if (titleName.getTitleName().indexOf("结论") >= 0) {
                                value = row.select("td").get(titleName.getIndex()).text();
                                conventional.setJl(value);
                            } else if (titleName.getTitleName().indexOf("备注") >= 0) {
                                value = row.select("td").get(titleName.getIndex()).text();
                                conventional.setBz(value);
                            }
                        }
                        conventional.setPlanId(acceptancePlanId);
                        conventional.setSlId(tableInstanId);
                        conventional.setId(String.valueOf(UniqueIdUtil.genId()));
                        conventionalDao.insert(conventional);
                    }
                }
            } else if (type.equals("6")) {
                for (int i = 0; i < rows.size(); i++) {
                    org.jsoup.nodes.Element row = rows.get(i);
                    if (i != 0 && row.select("td").size() != 0) {
                        for (int j = 0; j < row.select("td").size(); j++) {
                            String title = row.select("td").get(j).text();
                            if (title.indexOf("问题") > 0) {
                                String problem = row.select("td").get(j + 1).text();
                                if (!problem.equals("") && !problem.isEmpty()) {
                                    reportString.put("problem", problem);
                                }
                            } else if (title.indexOf("验收结论") > 0) {
                                String opinion = row.select("td").get(j + 1).text();
                                if (!opinion.equals("") && !opinion.isEmpty()) {
                                    reportString.put("opinion", opinion);
                                }
                            } else if (title.indexOf("承制方意见") >= 0) {
                                String sellerOpinion = row.select("td").get(j + 1).text();
                                if (!sellerOpinion.equals("") && !sellerOpinion.isEmpty()) {
                                    reportString.put("sellerOpinion", sellerOpinion);
                                }
                            } else if (title.indexOf("说明") > 0) {
                                String instructions = row.select("td").get(j + 1).text();
                                if (!instructions.equals("") && !instructions.isEmpty()) {
                                    reportString.put("instructions", instructions);
                                }
                            } else if (title.indexOf("编号") > 0) {
                                String serialNumber = row.select("td").get(j + 1).text();
                                if (!serialNumber.equals("") && !serialNumber.isEmpty()) {
                                    reportString.put("serialNumber", serialNumber);
                                }
                            }
                        }
                    }
                }
                tableInstanceDao.insertReport(acceptancePlanId, reportString);
                msg.put("backData", reportString);
            }else if ("10".equals(type)){
            	//解析试验问题回传表
				//把数据填到策划表的试验回传数据里
				//参照type 6
				for (int i = 0; i < rows.size(); i++) {
					org.jsoup.nodes.Element row = rows.get(i);
					if (i != 0 && row.select("td").size() != 0) {
						for (int j = 0; j < row.select("td").size(); j++) {
							String title = row.select("td").get(j).text();
							if (title.indexOf("问题") > 0) {
								String problem = row.select("td").get(j + 1).text();
								if (!problem.equals("") && !problem.isEmpty()) {
									reportString.put("problem", problem);
								}
							} else if (title.indexOf("说明") > 0) {
								String instructions = row.select("td").get(j + 1).text();
								if (!instructions.equals("") && !instructions.isEmpty()) {
									reportString.put("instructions", instructions);
								}
							}
						}
					}
				}
				tableInstanceDao.insertReport(acceptancePlanId, reportString);
				msg.put("backData", reportString);

			}

        }
        return msg;
    }

    public JSONObject check(String prefixCode, String actualvalNum) {
        String check = "";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", "true");
        jsonObject.put("check", "合格");
        String pattern = "^(\\≥|\\＞|\\≤|\\＜)(\\-|\\+)?([\\d]+)(\\.[\\d]+)?$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(prefixCode);
        // 要求值中是否包含汉字
        String pattern1 = "[\\u4e00-\\u9fa5]";
        Pattern r1 = Pattern.compile(pattern1);
        Matcher m1 = r1.matcher(prefixCode);
        // 要求值中是否包5(+1, -1)、 5[+1,-0]
        String pattern2 = "^([\\d]+)(\\.[\\d]+)?(\\(|\\[){1}(\\+){1}([\\d]+)(\\.[\\d]+)?(\\,)?(\\-){1}([\\d]+)(\\.[\\d]+)?(\\)|\\]){1}$";
        Pattern r2 = Pattern.compile(pattern2);
        Matcher m2 = r2.matcher(prefixCode);
        String pattern3 = "^(\\-|\\+)?([\\d]+)(\\.[\\d]+)?$";
        Pattern r3 = Pattern.compile(pattern3);
        Matcher m3 = r3.matcher(prefixCode);
        String pattern4 = "^^(0|[1-9]\\d*)$|^(0|[1-9]\\d*)\\.(\\d+)$";
        Pattern r4 = Pattern.compile(pattern4);
        Matcher m4 = r4.matcher(actualvalNum);
        if (!m4.matches()) {
            jsonObject.put("success", "false");
            jsonObject.put("msg", "实测值" + actualvalNum + "非纯数字，无法判读！");
            return jsonObject;
        }
        // 包含≥/≤/＞/＜等的情况
        if (m.matches()) {
            if (!m4.matches()) {
                jsonObject.put("success", "false");
                jsonObject.put("msg", "实测值" + actualvalNum + "非纯数字，无法判读！");
                return jsonObject;
            }
            // 要求值
            String requirevalNum = prefixCode.substring(1, prefixCode.length());
            String symbol = prefixCode.substring(0, 1);
            if (!actualvalNum.equals("") && !requirevalNum.equals("")) {

                switch (symbol) {
                    case "≥":
                        check = Float.valueOf(actualvalNum) >= Float.valueOf(requirevalNum) ? "合格" : "不合格";
                        break;
                    case "＞":
                        check = Float.valueOf(actualvalNum) > Float.valueOf(requirevalNum) ? "合格" : "不合格";
                        break;
                    case "≤":
                        check = Float.valueOf(actualvalNum) <= Float.valueOf(requirevalNum) ? "合格" : "不合格";
                        break;
                    case "＜":
                        check = Float.valueOf(actualvalNum) < Float.valueOf(requirevalNum) ? "合格" : "不合格";
                        break;
                }
                jsonObject.put("check", check);
            } else {
                jsonObject.put("check", "不合格");
            }
        }
        // 5(+1, -1)、 5[+1, 0]；
        else if (m2.matches()) {
            if (!m4.matches()) {
                jsonObject.put("success", "false");
                jsonObject.put("msg", "实测值非纯数字，无法判读！");
                return jsonObject;
            }
            if (!actualvalNum.equals("") && !prefixCode.equals("")) {
                String leftS = prefixCode.indexOf("(") >= 0 ? "1" : "0";// 判断有没有左小括号
                String rightS = prefixCode.indexOf(")") >= 0 ? "1" : "0";// 判断有没有右小括号
                String leftM = prefixCode.indexOf("[") >= 0 ? "1" : "0";// 判断有没有左中括号
                String rightM = prefixCode.indexOf("]") >= 0 ? "1" : "0";// 判断有没有右中括号
                String condition = leftS + rightS + leftM + rightM;
                switch (condition) {
                    case "1100":
                        Float mid = Float.valueOf(prefixCode.substring(0, prefixCode.indexOf("(")));
                        Float sup = Float.valueOf(prefixCode.substring(prefixCode.indexOf("+"), prefixCode.indexOf(",")));
                        Float sub = Float
                                .valueOf(prefixCode.substring(prefixCode.indexOf("-") + 1, prefixCode.lastIndexOf(")")));
                        Float min = mid - sub;
                        Float max = mid + sup;
                        Float req = Float.valueOf(actualvalNum);
                        if (req >= max || req <= min) {
                            check = "不合格";
                        } else {
                            check = "合格";
                        }
                        break;
                    case "1001":
                        Float mid1 = Float.valueOf(prefixCode.substring(0, prefixCode.indexOf("(")));
                        Float sup1 = Float.valueOf(prefixCode.substring(prefixCode.indexOf("+"), prefixCode.indexOf(",")));
                        Float sub1 = Float
                                .valueOf(prefixCode.substring(prefixCode.indexOf("-") + 1, prefixCode.lastIndexOf("]")));
                        Float min1 = mid1 - sub1;
                        Float max1 = mid1 + sup1;
                        Float req1 = Float.valueOf(actualvalNum);
                        if (req1 >= max1 || req1 < min1) {
                            check = "不合格";
                        } else {
                            check = "合格";
                        }
                        break;
                    case "0110":
                        Float mid2 = Float.valueOf(prefixCode.substring(0, prefixCode.indexOf("[")));
                        Float sup2 = Float.valueOf(prefixCode.substring(prefixCode.indexOf("+"), prefixCode.indexOf(",")));
                        Float sub2 = Float
                                .valueOf(prefixCode.substring(prefixCode.indexOf("-") + 1, prefixCode.lastIndexOf(")")));
                        Float min2 = mid2 - sub2;
                        Float max2 = mid2 + sup2;
                        Float req2 = Float.valueOf(actualvalNum);
                        if (req2 > max2 || req2 <= min2) {
                            check = "不合格";
                        } else {
                            check = "合格";
                        }
                        break;
                    case "0011":
                        Float mid3 = Float.valueOf(prefixCode.substring(0, prefixCode.indexOf("[")));
                        Float sup3 = Float.valueOf(prefixCode.substring(prefixCode.indexOf("+"), prefixCode.indexOf(",")));
                        Float sub3 = Float
                                .valueOf(prefixCode.substring(prefixCode.indexOf("-") + 1, prefixCode.lastIndexOf("]")));
                        Float min3 = mid3 - sub3;
                        Float max3 = mid3 + sup3;
                        Float req3 = Float.valueOf(actualvalNum);
                        if (req3 > max3 || req3 < min3) {
                            check = "不合格";
                        } else {
                            check = "合格";
                        }
                        break;
                }
            } else {
                check = "不合格";
            }
            jsonObject.put("check", check);
        }
        // 0~2
        else if (prefixCode.contains("~")) {
            if (!m4.matches()) {
                // jsonObject.put("success", "false");
                jsonObject.put("msg", "实测值非纯数字，无法判读！");
                return jsonObject;
            }
            if (!actualvalNum.equals("") && !prefixCode.equals("")) {
                String min = prefixCode.substring(0, prefixCode.indexOf("~"));
                String max = prefixCode.substring(prefixCode.indexOf("~") + 1, prefixCode.length());
                if ((Float.valueOf(actualvalNum) < Float.valueOf(min))
                        || (Float.valueOf(actualvalNum) > Float.valueOf(max))) {
                    check = "不合格";
                } else {
                    check = "合格";
                }
            } else {
                check = "不合格";
            }
            jsonObject.put("check", check);
        }
        // 5±0.2
        else if (prefixCode.contains("±")) {
            if (!m4.matches()) {
                jsonObject.put("msg", "实测值非纯数字，无法判读！");
                return jsonObject;
            }
            if (!actualvalNum.equals("") && !prefixCode.equals("")) {
                String mid = prefixCode.substring(0, prefixCode.indexOf("±"));
                String mm = prefixCode.substring(prefixCode.indexOf("±") + 1, prefixCode.length());
                Float min = Float.valueOf(mid) - Float.valueOf(mm);
                Float max = Float.valueOf(mid) + Float.valueOf(mm);
                if (Float.valueOf(actualvalNum) < min || Float.valueOf(actualvalNum) > max) {
                    check = "不合格";
                } else {
                    check = "合格";
                }
            } else {
                check = "不合格";
            }
            jsonObject.put("check", check);
        }
        return jsonObject;
    }

    /**
     * @param
     * @return
     * @Desc 下发策划表单
     */
    public void createPlan(Map<String, Object> acceptancePlanMap) {
        String planId = acceptancePlanMap.get("ID").toString();
        String html = "<!?xml version=\"1.0\" encoding=\"UTF-8\"?><html> <table width=\"100%\" class=\"layui-table\"><tbody><tr class=\"firstRow\">";
        html += "<td valign=\"top\" align=\"center\">";
    }

    public void updateStatus(String id, String status) {
        instanceTableDao.updateStatus(id, status);
    }
}
