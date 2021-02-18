package com.cssrc.ibms.dp.weaponCheck.service;

import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.resources.mission.dao.TestPlanDao;
import com.cssrc.ibms.core.user.dao.SysOrgDao;
import com.cssrc.ibms.core.user.dao.SysUserDao;
import com.cssrc.ibms.core.user.model.SysOrg;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 武器所检策划service
 * @author zmz
 *
 */
@Service
public class WeaponCheckService {
    @Resource
    private TestPlanDao dao;
    @Resource
    private SysUserDao sysUserDao;
    @Resource
    private SysOrgDao sysOrgDao;

    /**
     * @Deprecated 建议使用testPlanService的同名方法
     * excel导入组员信息
     * @param inputStream
     * @param missionId
     * 表格格式  岗位  姓名  单位  负责项目
     */
    public String importTeamInfo(InputStream inputStream, String missionId) throws IOException, InvalidFormatException {
        JSONObject jsonObject=new JSONObject();
        List<Map<String,String>> teamList=new ArrayList<>();
        Map<String,String> teamInfo;
        Workbook workbook=null;
        String modelType="";
        workbook= WorkbookFactory.create(inputStream);
        //获取第一个表单:表格文件的简单说明情况
        Sheet sheet=workbook.getSheetAt(0);
        //获取行数
        Integer totalRow=sheet.getLastRowNum();
        //获取列数
        Integer totalColumn=sheet.getRow(0).getPhysicalNumberOfCells();

        //遍历表头,是否符合填写要求
        for (int j=0;j<totalColumn;j++){
            //从左到右 遍历第一行每一列
            sheet.getRow(0).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
            String data = sheet.getRow(0).getCell(j).getStringCellValue();
            switch (j){
                case 0:if (!"岗位".equals(data)){
                    return "{\"success\":\"false\",\"context\":\"表格内容需严格符合以下顺序:岗位-姓名-单位-负责项目\"}";
                }
                    break;
                case 1:if (!"姓名".equals(data)){
                    System.out.println("2");
                    return "{\"success\":\"false\",\"context\":\"表格内容需严格符合以下顺序:岗位-姓名-单位-负责项目\"}";
                }
                    break;
                case 2:if (!"单位".equals(data)){
                    return "{\"success\":\"false\",\"context\":\"表格内容需严格符合以下顺序:岗位-姓名-单位-负责项目\"}";
                }
                    break;
                case 3:if (!"负责项目".equals(data)){
                    return "{\"success\":\"false\",\"context\":\"表格内容需严格符合以下顺序:岗位-姓名-单位-负责项目\"}";
                }
                    break;
            }
        }
        //开始取数据
        for (int i=1;i<=totalRow;i++){
            //每一行都是一个独立的组员信息
//			AcceptanceGroup acceptanceGroup=new AcceptanceGroup();
            //表格格式  岗位  姓名  单位  负责项目
//			acceptanceGroup.setId(String.valueOf(UniqueIdUtil.genId()));
//			acceptanceGroup.setSSBCCH(missionId);
            teamInfo=new HashMap<>();
            for (int j=0;j<totalColumn;j++){
                //从上到下 从左到右 遍历每一行每一列
                try{
                    sheet.getRow(i).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                }catch (NullPointerException e){
                    e.printStackTrace();
                    return "{\"success\":\"false\",\"context\":\"第"+(i+1)+"行,第"+(j+1)+"列为空！\"}";
                }

                String data = sheet.getRow(i).getCell(j).getStringCellValue();
                switch (j){
                    case 0:teamInfo.put("gw",data); break;
                    case 1:teamInfo.put("xm",data);
                        List<? extends ISysUser> sysUser=sysUserDao.getByFullname(data);
                        if (sysUser.size()==0){
                            return "{\"success\":\"false\",\"context\":\""+data+" 用户不存在\"}";
                        }else {
                            teamInfo.put("userId", sysUser.get(0).getUserId().toString());
                        }
                        break;
                    case 2:teamInfo.put("dw",data);
                        List<? extends SysOrg> sysOrg=sysOrgDao.getByOrgName(data);
                        if (sysOrg.size()==0){
                            return "{\"success\":\"false\",\"context\":\""+data+" 单位不存在\"}";
                        }else {
                            teamInfo.put("dwId", sysOrg.get(0).getOrgId().toString());
                        }
                        break;
                    case 3:teamInfo.put("fzxm",data); break;
					/*case 1:acceptanceGroup.setZw(data);break;
					case 2:acceptanceGroup.setXm(data);break;
					case 3:acceptanceGroup.setDw(data);break;
					case 4:acceptanceGroup.setFzxm(data);break;*/
                }
            }

            teamList.add(teamInfo);
            //遍历完一行信息了,该存到数据库了
//			acceptanceGroupDao.insert(acceptanceGroup);
        }
        jsonObject.put("teamList",teamList);
        jsonObject.put("success","true");
        //"{\"success\":\"true\"}"
        String teamListJson=jsonObject.toString();
        return teamListJson;
    }

    /**
     * 获取下一个武器所检的编号的尾号
     * @param xhId
     * @return
     */
    public String generatorReportNumber(String xhId) {
        String nextNumber=dao.getNextPlanNumber(xhId);
        return nextNumber;
    }
}
