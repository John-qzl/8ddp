package com.cssrc.ibms.reportclient;

import java.util.ResourceBundle;

import com.cssrc.ibms.util.http.HttpClientUtil;
import com.fr.script.AbstractFunction;
import com.fr.web.core.SessionDealWith;
import com.fr.web.core.SessionIDInfor;


public class TaskOpinion extends AbstractFunction
{
    private static final long serialVersionUID = 5363477973717524838L;
    
    private String displayId, dataKey, nodeSetKey,voteCode;
    @Override
    public Object run(Object[] param)
    {
        try
        {
            String opinion = getOpinion(param);
            return opinion;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        
    }
    
    /**
     * 从 ibms 平台获取审批意见
     * 
     * @param param
     * @return
     * @throws Exception
     */
    private String getOpinion(Object[] param)
        throws Exception
    {
        //String sessionID=null;
        //String ip=null;
        //String port=null;
        //String contextPath=null;
        //获取ibms平台app IP
        /*  if(param.length==4){
            sessionID=param[3].toString();
            SessionIDInfor info=SessionDealWith.getSessionIDInfor(sessionID);
            ip=param[3].toString();
        }
      //app 端口
        if(param.length==5){
            port=param[4].toString();
        }
        //app contextPath
        if(param.length==6){
        	sessionID=param[3].toString();
            SessionIDInfor info=SessionDealWith.getSessionIDInfor(sessionID);
            ip=info.getRemoteAddress();
            port=param[4].toString();
            contextPath=param[5].toString();
        }
        //app contextPath
        if(param.length==7){
        	sessionID=param[3].toString();
            SessionIDInfor info=SessionDealWith.getSessionIDInfor(sessionID);
            ip=info.getRemoteAddress();
            port=param[4].toString();
            contextPath=param[5].toString();
        }*/
        //ResourceBundle resourceBundle = ResourceBundle.getBundle("ibms-server");
        //String ibmsServer = resourceBundle.getString("ibms.server.url");
        this.displayId = param[0].toString();
        this.dataKey = param[1].toString();
        this.nodeSetKey = param[2].toString();
        String appclient = param[3].toString();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("ibms-server");
        String ibmsServer = resourceBundle.getString(appclient+".server.url");
        
        this.voteCode =  param.length== 5 ? param[4].toString() : null;
        
        String url = ibmsServer + "/getOpinion?dataKey=" + dataKey;
        url += "&nodeSetKey=" + nodeSetKey + "&displayId=" + displayId;
        if (voteCode != null)
        {
            url += "&voteCode=" + voteCode;
        }
        String opinion = HttpClientUtil.httpGet(url);
        return opinion;
    }
  
}
