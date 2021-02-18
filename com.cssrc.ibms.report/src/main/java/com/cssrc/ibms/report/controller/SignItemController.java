package com.cssrc.ibms.report.controller;


import java.io.PrintWriter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.report.model.SignItem;
import com.cssrc.ibms.report.service.SignItemService;

@Controller
public class SignItemController
{
    @Resource
    SignItemService signItemService;
    
    /**
     * get sign image
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("oa/system/sign/saveItem")
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response)
    {
        try{
            ResultMessage message = new ResultMessage(ResultMessage.Fail,"系统异常");
            JSONObject json=JSONObject.parseObject(JSONObject.toJSONString(message));
            Long signId=signItemService.saveItem(request);
            message=new ResultMessage(ResultMessage.Success,"签名成功");
            PrintWriter writer = response.getWriter();
            json.put("sign_id", signId.toString());
            writer.print(json.toJSONString());
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * get sign image
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("oa/system/sign/getItem")
    public ModelAndView getItem(HttpServletRequest request, HttpServletResponse response)
    {
        try{
            Long id=RequestUtil.getLong(request, "id");
            SignItem signItem=signItemService.getById(id);
            PrintWriter writer = response.getWriter();
            writer.print(JSON.toJSONString(signItem));
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
