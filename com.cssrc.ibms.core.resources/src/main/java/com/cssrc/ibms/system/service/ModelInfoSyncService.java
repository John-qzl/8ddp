package com.cssrc.ibms.system.service;

import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.user.service.SysUserService;

import com.cssrc.ibms.report.service.SignModelService;
import com.cssrc.ibms.system.dao.ModelInfoSyncDao;
import org.springframework.stereotype.Service;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;

import javax.annotation.Resource;
import java.util.List;

/**
 * pad回传数据时保存签章信息
 */
@Service
public class ModelInfoSyncService {
    @Resource
    ModelInfoSyncDao dao;
    @Resource
    SysUserService sysUserService;
    @Resource
    SignModelService signModelService;

    /**
     * 自动辨别回传的签章
     * 如果系统已有该用户，则直接分配签章到该用户
     * 无论系统是否有改用户，都会存在PADHCQZB表里
     * 返回1代表有用户，
     * 返回0代表无此用户
     * 20200911更新了接口,接口变得更智能了(￣▽￣)~*
     * by zmz
     * @param user
     * @param fileId
     * @return
     */
    public int autoSaveModel(ISysUser user, String fileId,String unrealFullName) {
        //先确定有没有这个用户
        if (user == null) {
            //没有这个用户
            insertSignModelWithoutUserId(fileId,unrealFullName);
            return 0;
        } else {
                //有这个用户(暂时不考虑fullname重名的情况
                Long userId = user.getUserId();
                String userName=user.getUsername();
                //保存签章
                signModelService.saveAndUpdateSignModel(userId, fileId);
                insertSignModelAndUserId(userId, userName, fileId,unrealFullName);
            return 1;
        }
    }




    /**
     * 将当前签章id复制到PADHCQZB这张表里
     * 返回值是该条目的id
     *
     * @param fileId
     */
    public Long insertSignModelWithoutUserId(String fileId,String unrealFullName) {
        Long id = UniqueIdUtil.genId();
        dao.insert(id, fileId,unrealFullName);
        return id;
    }

    /**
     * 将当前签章id和用户id保存到PADHCQZB这张表里
     * 返回值是该条目的id
     *
     * @param fileId
     */
    public Long insertSignModelAndUserId(Long userId, String userName, String fileId,String unrealFullName) {
        Long id = UniqueIdUtil.genId();
        dao.insert(id, userId, userName, fileId,unrealFullName);
        return id;
    }

}
