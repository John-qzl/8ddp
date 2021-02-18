package com.cssrc.ibms.core.resources.io.service;

import com.cssrc.ibms.core.resources.datapackage.dao.RangeTestPlanDao;
import com.cssrc.ibms.core.resources.datapackage.model.RangeTestPlan;
import com.cssrc.ibms.system.dao.SysFileDao;
import com.cssrc.ibms.system.model.SysFile;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;
import java.util.List;

/**
 * 获取某一策划下的所有的文件里密级最高的那个
 * 默认秘密
 */
@Service
public class SecretLevelService {
    @Resource
    private SysFileDao sysFileDao;
    @Resource
    private RangeTestPlanDao rangeTestPlanDao;

    /**
     * fileInfo是文件的json字符串
     * String fileInfo=rangeTestPlan.getF_SYYJWJ();
     */
    public String getSecretLevel(String fileInfo){
        Integer security=3;
        if(fileInfo!=null&&!fileInfo.equals("")) {
            JSONArray jsonArray=JSONArray.fromObject(fileInfo);
            for(int i=0;i<jsonArray.size();i++) {
                JSONObject jsonObject=jsonArray.optJSONObject(i);
                SysFile sysFile=sysFileDao.getFileByFileId(jsonObject.optString("id"));
                if (sysFile.getSecurity()!=null){
                    if (Integer.valueOf(sysFile.getSecurity())>security){
                        security=Integer.valueOf(sysFile.getSecurity());
                    }
                }
            }
        }
        switch (security){
            case 3:return "公开";
            case 6:return "内部";
            case 9:return "秘密";
            case 12:return "机密";
            default:return "无法计算当前密级";
        }
    }

    /**
     * 获取当前策划id数组里密级最高的
     * @param missionIds
     * @return
     */
    public String getAllPlanSecretLevel(String[] missionIds){
        Integer security=3;
        for (String missionId:missionIds){
            Integer curSecurity=getSinglePlanSecretLevelNumber(missionId);
            security=curSecurity>security?curSecurity:security;
        }
        switch (security){
            case 3:return "公开";
            case 6:return "内部";
            case 9:return "秘密";
            case 12:return "机密";
            default:return "无法计算当前密级";
        }
    }

    /**
     * 返回当前策划的文件密级  是数字形式的
     * @param missionId
     * @return
     */
    private Integer getSinglePlanSecretLevelNumber(String missionId){
        RangeTestPlan rangeTestPlan=rangeTestPlanDao.selectById(missionId);
        String fileInfo=rangeTestPlan.getF_SYYJWJ();

        Integer security=3;
        if(fileInfo!=null&&!fileInfo.equals("")) {
            JSONArray jsonArray=JSONArray.fromObject(fileInfo);
            for(int i=0;i<jsonArray.size();i++) {
                JSONObject jsonObject=jsonArray.optJSONObject(i);
                SysFile sysFile=sysFileDao.getFileByFileId(jsonObject.optString("id"));
                if (sysFile.getSecurity()!=null){
                    if (Integer.valueOf(sysFile.getSecurity())>security){
                        security=Integer.valueOf(sysFile.getSecurity());
                    }
                }
            }
        }
        return security;
    }

    /**
     * 获取两个fileList中密级最高的那个
     * @param planBasisFileList
     * @param reportBasisFileList
     * @return
     */
    public String getHigherSecret(List<SysFile> planBasisFileList, List<SysFile> reportBasisFileList) {
        Integer security=3;//默认密级公开

        if (planBasisFileList!=null){
            for (SysFile file:planBasisFileList){
                if (file.getSecurity()!=null){
                    if (Integer.valueOf(file.getSecurity())>security){
                        security=Integer.valueOf(file.getSecurity());
                    }
                }
            }
        }
        //其实不用判空的,因为进来之前这个list被arrayList初始化了, 但还是留着吧,万一呢
        if (reportBasisFileList!=null){
            if (reportBasisFileList.size()!=0){
                for (SysFile file:reportBasisFileList){
                    if (file.getSecurity()!=null){
                        if (Integer.valueOf(file.getSecurity())>security){
                            security=Integer.valueOf(file.getSecurity());
                        }
                    }
                }
            }
        }
        switch (security){
            case 3:return "公开";
            case 6:return "内部";
            case 9:return "秘密";
            case 12:return "机密";
            default:return "无法计算当前密级";
        }
    }
}
