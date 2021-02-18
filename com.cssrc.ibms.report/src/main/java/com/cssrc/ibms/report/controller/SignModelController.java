package com.cssrc.ibms.report.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.report.model.ISignModel;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.encrypt.Coder;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.annotion.Interceptor;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.file.FileResult;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.interceptor.file.FileHandlerInterceptor;
import com.cssrc.ibms.core.webseal.image.ImageUtil;
import com.cssrc.ibms.report.model.SignModel;
import com.cssrc.ibms.report.service.SignModelService;

@Controller
@Action(ownermodel = SysAuditModelType.USER_MANAGEMENT)
@DataNote(beanName = SignModelController.class)
public class SignModelController
{
    @Resource
    SignModelService signModelService;
    
    /**
     * 签章moel编辑
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("oa/system/sign/edit")
    public ModelAndView edit(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mv = new ModelAndView("report/sign/signModelEdit.jsp");
        String userId = request.getParameter("userId");
        String id = request.getParameter("id");
        SignModel signModel = null;
        if (id != null)
        {
            signModel = signModelService.getById(Long.valueOf(id));
        }
        else if (userId != null)
        {
            signModel = new SignModel();
            signModel.setStartDate(DateUtil.getCurrentDateDate("yyyy-MM-dd"));
            signModel.setEndDate(DateUtil.addYear(signModel.getStartDate(), 2));
            signModel.setUserId(Long.valueOf(userId));
        }
        return mv.addObject("signModel", signModel);
    }
    
    /**
     * 签章模型保存
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("oa/system/sign/save")
    @Interceptor(interceptor = FileHandlerInterceptor.class)
    @Action(description = "签章模型保存", execOrder = ActionExecOrder.AFTER, detail = "保存签章模型【${signModel.name}(${signModel.desc})】", exectype = SysAuditExecType.UPDATE_TYPE)
    public void save(SignModel signModel, MultipartHttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ResultMessage result = new ResultMessage(ResultMessage.Fail);
        List<FileResult> resultFile = RequestUtil.getFileResult(request);
        if (resultFile != null && resultFile.size() == 1)
        {
            FileResult file = resultFile.get(0);
            signModel.setImgPath(file.getStorePath());
            signModel.setPathType(file.getStoreType());
        }else{
            if(signModel.getId()==null){
                //印章模板存储在本地
                signModel.setPathType(2);
            }
        }
        result = signModelService.save(signModel);
        PrintWriter writer = response.getWriter();
        writer.print(result.toString());
        try {
        	LogThreadLocalHolder.putParamerter("signModel", signModel);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
    
    /**
     * 签章moel删除
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("oa/system/sign/del/{id}")
    public void del(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        List<FileResult> resultFile = RequestUtil.getFileResult(request);
    }
    
    /**
     * 获取印章列表
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("oa/system/sign/getSign")
    public ModelAndView getSignImage(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        ModelAndView mv=new ModelAndView("report/sign/signModelList.jsp");
        List<? extends ISignModel> signList=null;
        signList=this.signModelService.getByUserId(UserContextUtil.getCurrentUserId());
        mv.addObject("signList", signList);
        mv.addObject("userId", UserContextUtil.getCurrentUserId());
        return mv;
        
    }
    
    /**
     * 设置默认签章
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("oa/system/sign/default/{id}/yes.do")
    @Action(description = "设置默认签章", execOrder = ActionExecOrder.AFTER, detail = "<#assign entity=signModelService.getById(Long.valueOf(modelId))/> 设置【${entity.name}(${entity.desc})】为默认签章", exectype = SysAuditExecType.UPDATE_TYPE)
    public void setDefaultYes(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long userId = RequestUtil.getLong(request, "userId");
        try {
        	signModelService.updateDefaultYes(userId, id);
		} catch (Exception e) {
			// TODO: handle exception
		}
        try {
        	LogThreadLocalHolder.putParamerter("modelId",id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
        
    }
    
    /**
     * 取消默认签章
     * 
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("oa/system/sign/default/{id}/not.do")
    @Action(description = "取消默认签章", execOrder = ActionExecOrder.AFTER, detail = "<#assign entity=signModelService.getById(Long.valueOf(modelId))/> 取消【${entity.name}(${entity.desc})】为默认签章", exectype = SysAuditExecType.UPDATE_TYPE)
    public void setDefaultNot(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        Long userId = RequestUtil.getLong(request, "userId");
    	signModelService.updateDefaultNot(userId, id);
        try {
        	LogThreadLocalHolder.putParamerter("modelId",id);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
    
    /**
     * 画布签名
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("oa/system/sign/drawSign")
    public ModelAndView drawSign(HttpServletRequest request, HttpServletResponse response)
    {
        ModelAndView mv=new ModelAndView("report/sign/drawSign.jsp");
        return mv;
    }
    
    /**
     * 画布签名 转换成png
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("oa/system/sign/savePng")
    public ModelAndView saveDrawSign(HttpServletRequest request, HttpServletResponse response)
    {
        String data=RequestUtil.getString(request,"svg");
        //png 图片临时文件
        File tem=null;
        try
        {
            tem=FileUtil.generateTempFile();
            ImageUtil.convertToPng(data, tem.getPath());
            byte[] bt=FileUtil.readByte(new FileInputStream(tem));
            String img=Coder.encryptBASE64(bt);
            JSONObject json=new JSONObject();
            json.put("img", img);
            PrintWriter writer = response.getWriter();
            writer.print(json.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }finally{
            if(tem!=null&&tem.exists()){
                tem.delete();
            }
        }
        return null;
    }
    
}
