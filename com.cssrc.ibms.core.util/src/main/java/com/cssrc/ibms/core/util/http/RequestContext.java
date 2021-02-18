package com.cssrc.ibms.core.util.http;
     
     import javax.servlet.http.HttpServletRequest;
     import javax.servlet.http.HttpServletResponse;
     
     public class RequestContext
     {
     private static ThreadLocal<HttpServletRequest> requestLocal = new ThreadLocal();
     
     private static ThreadLocal<HttpServletResponse> responseLocal = new ThreadLocal();
     
       public static void setHttpServletRequest(HttpServletRequest request) {
       requestLocal.set(request);
       }
     
       public static void clearHttpReqResponse()
       {
       requestLocal.remove();
       responseLocal.remove();
       }
     
       public static void setHttpServletResponse(HttpServletResponse response)
       {
       responseLocal.set(response);
       }
     
       public static HttpServletRequest getHttpServletRequest()
       {
       return (HttpServletRequest)requestLocal.get();
       }
     
       public static HttpServletResponse getHttpServletResponse()
       {
       return (HttpServletResponse)responseLocal.get();
       }
     }